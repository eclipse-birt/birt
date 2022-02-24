/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.expression;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexExpressionCompilerTest {
	private ScriptableObject m_scope;
	private ExpressionCompiler m_compiler;
	private AggregateRegistry m_registry;
	private ScriptContext context;

	@Before
	public void complexExpressionCompilerSetUp() throws Exception {
		context = new ScriptContext();
		context.compile("javascript", null, 0, "1==1");
		m_scope = Context.getCurrentContext().initStandardObjects();
		ScriptableObject.defineClass(m_scope, ComplexExpressionCompilerTest.Row.class);
		ScriptableObject.defineClass(m_scope, ComplexExpressionCompilerTest.AggrValue.class);
		Scriptable row = Context.getCurrentContext().newObject(m_scope, "Row");
		m_scope.put("row", m_scope, row);
		Scriptable aggr = Context.getCurrentContext().newObject(m_scope, "AggrValue");
		m_scope.put("_aggr_value", m_scope, aggr);
		m_compiler = new ExpressionCompiler();
		m_registry = new AggregateRegistry() {
			private int m_index = 1;

			public int register(AggregateExpression aggregationExpr) {
				return m_index++;
			}
		};
	}

	@After
	public void complexExpressionCompilerTearDown() throws Exception {
		context.close();
	}

	@Test
	public void testDirectReference1() throws Exception {
		CompiledExpression expr = m_compiler.compile("row.col1", m_registry, context);
		checkDirectRef1(expr);
	}

	@Test
	public void testDirectReference2() throws Exception {
		CompiledExpression expr = m_compiler.compile("row[\"col1\"]", m_registry, context);
		checkDirectRef1(expr);
	}

	@Test
	public void testDirectReference3() throws Exception {
		CompiledExpression expr = m_compiler.compile("row[ 1 ]", m_registry, context);
		assertTrue(expr instanceof ColumnReferenceExpression);
		assertNull(((ColumnReferenceExpression) expr).getColumnName());
		assertEquals(1, ((ColumnReferenceExpression) expr).getColumnindex());
	}

	// helper method that checks for row.col1 or row["col1"]
	private void checkDirectRef1(CompiledExpression expr) {
		assertNotNull(expr);
		assertTrue(expr instanceof ColumnReferenceExpression);
		assertEquals("col1", ((ColumnReferenceExpression) expr).getColumnName());
		assertEquals(-1, ((ColumnReferenceExpression) expr).getColumnindex());
	}

	@Test
	public void testNonDirectRefOnRow1() throws Exception {
		ComplexExpression expr = isComplexExpression("row[col1]");
		checkSubExpressionSize(expr, 0);
		checkSubConstantsSize(expr, 0);
	}

	@Test
	public void testNonDirectRefOnRow2() throws Exception {
		ComplexExpression expr = isComplexExpression("row[getColumnId()]");
		checkSubExpressionSize(expr, 0);
		checkSubConstantsSize(expr, 0);
	}

	@Test
	public void testNonDirectRefOnRow3() throws Exception {
		ComplexExpression expr = isComplexExpression("row[SomeObject.getColumnId()]");
		checkSubExpressionSize(expr, 0);
		checkSubConstantsSize(expr, 1);
	}

	@Test
	public void testMisc1() throws Exception {
		ComplexExpression expr = isComplexExpression("obj1.prop1 + obj2.prop2 + 100");
		checkSubExpressionSize(expr, 0);
		checkSubConstantsSize(expr, 3);
	}

	@Test
	public void testComplexExpr1() throws Exception {
		ComplexExpression expr = isComplexExpression("row.col1 + row[\"col2\"]");
		checkComplexExpr1(expr);
		checkSubConstantsSize(expr, 0);

		Object o = expr.evaluate(context, m_scope);
		assertTrue(o instanceof Double);
		assertEquals(new Double(100), o);
	}

	@Test
	public void testComplexExpr2() throws Exception {
		ComplexExpression expr = isComplexExpression("row[1] * row[\"col2\"]");
		checkSubConstantsSize(expr, 0);
		Object o = expr.evaluate(context, m_scope);
		assertTrue(o instanceof Double);
		assertEquals(new Double(0), o);

		Collection subExpressions = checkSubExpressionSize(expr, 2);
		Iterator iter = subExpressions.iterator();
		int count = 1;
		while (iter.hasNext()) {
			CompiledExpression ex = (CompiledExpression) iter.next();
			assertTrue(ex instanceof ColumnReferenceExpression);

			if (count++ == 1)
				assertEquals(1, ((ColumnReferenceExpression) ex).getColumnindex());
			else
				assertEquals("col2", ((ColumnReferenceExpression) ex).getColumnName());
		}
	}

	@Test
	public void testComplexExpr3() throws Exception {
		ComplexExpression expr = isComplexExpression("CustomFunction( row.col1 )");
		checkSubConstantsSize(expr, 0);
		hasOneSubExprAsDirectRef1(expr);
	}

	@Test
	public void testComplexExpr4() throws Exception {
		ComplexExpression expr = isComplexExpression("CustomObject.CustomFunction( row.col1 )");
		checkSubConstantsSize(expr, 1);
		hasOneSubExprAsDirectRef1(expr);
	}

	@Test
	public void testComplexExpr5() throws Exception {
		ComplexExpression expr = isComplexExpression("CustomObject[1]( row.col1 )");
		checkSubConstantsSize(expr, 1);
		hasOneSubExprAsDirectRef1(expr);
	}

	@Test
	public void testComplexExpr6() throws Exception {
		ComplexExpression expr = isComplexExpression("CustomObject[1].CustomFunction( row.col1 )");
		checkSubConstantsSize(expr, 2);
		hasOneSubExprAsDirectRef1(expr);
	}

	private ComplexExpression isComplexExpression(String exprStr) throws DataException {
		CompiledExpression expr = m_compiler.compile(exprStr, m_registry, context);
		assertTrue(expr instanceof ComplexExpression);
		return (ComplexExpression) expr;
	}

	private void hasOneSubExprAsDirectRef1(ComplexExpression expr) {
		Collection subExpressions = checkSubExpressionSize(expr, 1);
		Iterator iter = subExpressions.iterator();
		CompiledExpression ex = (CompiledExpression) iter.next();
		checkDirectRef1(ex);
	}

	@Test
	public void testAggregate1() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum( row.col1 )");

		checkAggregate1(expr);
	}

	@Test
	public void testAggregate2() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum( row[\"col1\"] )");

		checkAggregate1(expr);
	}

	@Test
	public void testAggregate3() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum( row[2] )");

		checkAggregate2(expr);
	}

	@Test
	public void testAggregate4() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum( row.col1 + row.col2 )");

		List args = expr.getArguments();
		assertEquals(1, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ComplexExpression);
		checkComplexExpr1((ComplexExpression) arg1);
	}

	@Test
	public void testAggregate5() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum( row.col1 + row.col2, true, null )");

		List args = expr.getArguments();
		assertEquals(3, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ComplexExpression);
		checkComplexExpr1((ComplexExpression) arg1);

		CompiledExpression arg2 = (CompiledExpression) args.get(1);
		assertTrue(arg2 instanceof ConstantExpression);
		assertEquals(Boolean.TRUE, ((ConstantExpression) arg2).getValue());

		CompiledExpression arg3 = (CompiledExpression) args.get(2);
		assertTrue(arg3 instanceof ConstantExpression);
		assertNull(((ConstantExpression) arg3).getValue());
	}

	@Test
	public void testAggregate6() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate(
				"Total.sum( row.col1 + row.col2, row.col1 == 100, \"MyGroup\" )");

		List args = expr.getArguments();
		assertEquals(3, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ComplexExpression);
		checkComplexExpr1((ComplexExpression) arg1);
		checkSubConstantsSize((ComplexExpression) arg1, 0);

		CompiledExpression arg2 = (CompiledExpression) args.get(1);
		assertTrue(arg2 instanceof ComplexExpression);
		checkComplexExpr2((ComplexExpression) arg2);
		checkSubConstantsSize((ComplexExpression) arg2, 1);

		CompiledExpression arg3 = (CompiledExpression) args.get(2);
		assertTrue(arg3 instanceof ConstantExpression);
		assertEquals("MyGroup", ((ConstantExpression) arg3).getValue());
	}

	@Test
	public void testAggregate7() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate(
				"Total.sum( row.col1 + row.col2, row.col1 == 100, 12 )");

		checkAggregate3(expr);
	}

	@Test
	public void testAggregate8() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate(
				"Total.sum( row.col1 + row.col2, row.col1 == 100, GetGroupName() )");

		List args = expr.getArguments();
		assertEquals(3, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ComplexExpression);
		checkComplexExpr1((ComplexExpression) arg1);

		CompiledExpression arg2 = (CompiledExpression) args.get(1);
		assertTrue(arg2 instanceof ComplexExpression);
		checkComplexExpr2((ComplexExpression) arg2);

		CompiledExpression arg3 = (CompiledExpression) args.get(2);
		assertTrue(arg3 instanceof ComplexExpression);
	}

	@Test
	public void testAggregate9() throws Exception {
		AggregateExpression expr = checkTopLevelNonNestedAggregate("Total.sum(row[\"repID\"],null,Total.OVERALL)");

		List args = expr.getArguments();
		assertEquals(3, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ColumnReferenceExpression);
		assertEquals("repID", ((ColumnReferenceExpression) arg1).getColumnName());
		assertEquals(-1, ((ColumnReferenceExpression) arg1).getColumnindex());

		CompiledExpression arg2 = (CompiledExpression) args.get(1);
		assertTrue(arg2 instanceof ConstantExpression);
		assertEquals(null, ((ConstantExpression) arg2).getValue());

		CompiledExpression arg3 = (CompiledExpression) args.get(2);
		assertTrue(arg3 instanceof ConstantExpression);
		assertEquals("OVERALL", ((ConstantExpression) arg3).getValue());

	}

	// checks either: Total.sum( row.col1 ) or Total.sum( row["col1"] )
	private void checkAggregate1(AggregateExpression expr) {
		List args = expr.getArguments();
		assertEquals(1, args.size());
		checkDirectRef1((CompiledExpression) args.get(0));
	}

	// checks: Total.sum( row[2] )
	private void checkAggregate2(AggregateExpression expr) {
		List args = expr.getArguments();
		assertEquals(1, args.size());
		assertNotNull(args.get(0));
		assertTrue(args.get(0) instanceof ColumnReferenceExpression);
		assertEquals(2, ((ColumnReferenceExpression) args.get(0)).getColumnindex());
	}

	// checks the following aggregate expression:
	// Total.sum( row.col1 + row.col2, row.col1 == 100, 12 )
	private void checkAggregate3(AggregateExpression expr) throws DataException {
		List args = expr.getArguments();
		assertEquals(3, args.size());

		CompiledExpression arg1 = (CompiledExpression) args.get(0);
		assertTrue(arg1 instanceof ComplexExpression);
		checkComplexExpr1((ComplexExpression) arg1);

		CompiledExpression arg2 = (CompiledExpression) args.get(1);
		assertTrue(arg2 instanceof ComplexExpression);
		checkComplexExpr2((ComplexExpression) arg2);

		CompiledExpression arg3 = (CompiledExpression) args.get(2);
		assertTrue(arg3 instanceof ConstantExpression);
		assertEquals(new Double(12), ((ConstantExpression) arg3).getValue());
	}

	private AggregateExpression checkTopLevelNonNestedAggregate(String exprStr) throws DataException {
		CompiledExpression expr = m_compiler.compile(exprStr, m_registry, context);
		assertTrue(expr instanceof AggregateExpression);
		Object o = ((AggregateExpression) expr).evaluate(context, m_scope);
		assertTrue(o instanceof Integer);
		assertEquals(21, ((Integer) o).intValue());

		return (AggregateExpression) expr;
	}

	// "row.col1 + row.col2" and execute this argument
	private void checkComplexExpr1(ComplexExpression arg1) throws DataException {
		assertNotNull(arg1);
		Collection subExpressions = checkSubExpressionSize(arg1, 2);
		Iterator i = subExpressions.iterator();
		int j = 1;
		while (i.hasNext()) {
			CompiledExpression e = (CompiledExpression) i.next();
			assertEquals(e.getClass(), ColumnReferenceExpression.class);
			assertEquals((j++ == 1) ? "col1" : "col2", ((ColumnReferenceExpression) e).getColumnName());
		}

		// tests the execution of the subtree for the aggregate's argument
		Object o = arg1.evaluate(context, m_scope);
		assertTrue(o instanceof Double);
		assertEquals(100, ((Double) o).intValue());
	}

	// "row.col1 == 100" and execute this argument
	private void checkComplexExpr2(ComplexExpression arg2) throws DataException {
		hasOneSubExprAsDirectRef1(arg2);

		// tests the execution of the subtree for the aggregate's argument
		Object o = arg2.evaluate(context, m_scope);
		assertEquals(Boolean.TRUE, o);
	}

	@Test
	public void testNestedAggregate1() throws Exception {
		CompiledExpression expr = m_compiler.compile("Total.sum( Total.sum( row.col1 ) )", m_registry, context);
		assertTrue(expr instanceof AggregateExpression);
		Object o = ((AggregateExpression) expr).evaluate(context, m_scope);
		assertTrue(o instanceof Integer);
		assertEquals(42, ((Integer) o).intValue());

		List args = ((AggregateExpression) expr).getArguments();
		assertEquals(1, args.size());
		AggregateExpression expr1 = (AggregateExpression) args.get(0);
		checkAggregate1(expr1);
	}

	@Test
	public void testNestedAggregate2() throws Exception {
		CompiledExpression expr = m_compiler.compile("Total.sum( Total.sum( row.col1 ), Total.sum( row[\"col1\"] ) )",
				m_registry, context);
		assertTrue(expr instanceof AggregateExpression);
		Object o = ((AggregateExpression) expr).evaluate(context, m_scope);
		assertTrue(o instanceof Integer);
		assertEquals(63, ((Integer) o).intValue());

		List args = ((AggregateExpression) expr).getArguments();
		assertEquals(2, args.size());

		// 1st argument Total.sum( row.col1 )
		AggregateExpression arg1 = (AggregateExpression) args.get(0);
		checkAggregate1(arg1);

		// 2nd argument Total.sum( row["col1"] )
		AggregateExpression arg2 = (AggregateExpression) args.get(1);
		checkAggregate1(arg2);
	}

	@Test
	public void testComplexAggregates1() throws Exception {
		ComplexExpression expr = isComplexExpression("Total.sum( row[\"col1\"] ) + Total.sum( row[2] )");
		Object o = expr.evaluate(context, m_scope);
		assertTrue(o instanceof Double);
		assertEquals(63, ((Double) o).intValue());

		Collection subExpressions = checkSubExpressionSize(expr, 2);
		Iterator iter = subExpressions.iterator();

		CompiledExpression subExpr1 = (CompiledExpression) iter.next();
		assertTrue(subExpr1 instanceof AggregateExpression);
		checkAggregate1((AggregateExpression) subExpr1);

		CompiledExpression subExpr2 = (CompiledExpression) iter.next();
		assertTrue(subExpr2 instanceof AggregateExpression);
		checkAggregate2((AggregateExpression) subExpr2);
	}

	@Test
	public void testComplexAggregates2() throws Exception {
		ComplexExpression expr = isComplexExpression("CustomFunction( Total.sum( row.col1 ) )");
		Collection subExpressions = checkSubExpressionSize(expr, 1);
		Iterator iter = subExpressions.iterator();
		AggregateExpression aggregate = (AggregateExpression) iter.next();
		checkAggregate1(aggregate);
	}

	@Test
	public void testComplexAggregates3() throws Exception {
		ComplexExpression expr = isComplexExpression(
				"Total.sum( row.col1 + row.col2, row.col1 == 100, 12 ) + " + "row.col1 + 300");
		Collection subExpressions = checkSubExpressionSize(expr, 2);
		Iterator iter = subExpressions.iterator();

		CompiledExpression subExpr1 = (CompiledExpression) iter.next();
		assertTrue(subExpr1 instanceof AggregateExpression);
		checkAggregate3((AggregateExpression) subExpr1);

		CompiledExpression subExpr2 = (CompiledExpression) iter.next();
		assertTrue(subExpr2 instanceof ColumnReferenceExpression);
		checkDirectRef1(subExpr2);

		Object o = expr.evaluate(context, m_scope);
		assertEquals(new Double(421), o);
	}

	@Test
	public void testComplexAggregates4() throws Exception {
		ComplexExpression expr = isComplexExpression(
				"CustomFunc1( CustomFunc2( Total.sum( row.col1 ) +" + " row[2] ) )");
		Collection subExpressions = checkSubExpressionSize(expr, 2);
		Iterator iter = subExpressions.iterator();

		CompiledExpression subExpr1 = (CompiledExpression) iter.next();
		assertTrue(subExpr1 instanceof AggregateExpression);
		checkAggregate1((AggregateExpression) subExpr1);

		CompiledExpression subExpr2 = (CompiledExpression) iter.next();
		assertTrue(subExpr2 instanceof ColumnReferenceExpression);
		assertEquals(2, ((ColumnReferenceExpression) subExpr2).getColumnindex());
	}

	@Test
	public void testComplexConstants1() throws Exception {
		doTestComplexConstants("100 + 200 + Total.sum( row.col1 )");
	}

	@Test
	public void testComplexConstants2() throws Exception {
		doTestComplexConstants("100 + Total.sum( row.col1 ) + 200");
	}

	// tests the following complex statement:
	// 100 + 200 + Total.sum( row.col1 ) or some variant with different orders
	private void doTestComplexConstants(String exprStr) throws Exception {
		ComplexExpression expr = isComplexExpression(exprStr);

		Collection subExpressions = checkSubExpressionSize(expr, 1);
		Iterator iter = subExpressions.iterator();
		CompiledExpression subExpr = (CompiledExpression) iter.next();
		assertTrue(subExpr instanceof AggregateExpression);
		checkAggregate1((AggregateExpression) subExpr);

		Object o = expr.evaluate(context, m_scope);
		assertEquals(321, ((Double) o).intValue());
	}

	@Test
	public void testComplexConstants3() throws Exception {
		ComplexExpression expr = isComplexExpression("100 + Total.sum( row.col1 ) + 200 + Total.sum( row[2] )");

		Collection subExpressions = checkSubExpressionSize(expr, 2);
		Iterator iter = subExpressions.iterator();

		CompiledExpression subExpr1 = (CompiledExpression) iter.next();
		assertTrue(subExpr1 instanceof AggregateExpression);
		checkAggregate1((AggregateExpression) subExpr1);

		CompiledExpression subExpr2 = (CompiledExpression) iter.next();
		assertTrue(subExpr2 instanceof AggregateExpression);
		checkAggregate2((AggregateExpression) subExpr2);

		Object o = expr.evaluate(context, m_scope);
		assertEquals(363, ((Double) o).intValue());
	}

	@Test
	public void testConstants1() throws Exception {
		Object o = checkConstantExpression("null");
		assertNull(o);
	}

	@Test
	public void testConstants2() throws Exception {
		Object o = checkConstantExpression("100");
		assertEquals(new Double(100), o);
	}

	@Test
	public void testConstants3() throws Exception {
		Object o = checkConstantExpression("\"null\"");
		assertEquals("null", o);
	}

	@Test
	public void testConstants4() throws Exception {
		Object o = checkConstantExpression("100 + 200 + 300");
		assertEquals(600, ((Double) o).intValue());
	}

	@Test
	public void testConstants5() throws Exception {
		Object o = checkConstantExpression("true");
		assertTrue(((Boolean) o).booleanValue());
	}

	@Test
	public void testConstants6() throws Exception {
		Object o = checkConstantExpression("false");
		assertFalse(((Boolean) o).booleanValue());
	}

	@Test
	public void testConstants7() throws Exception {
		Object o = checkConstantExpression("\"hello\" + \" world\"");
		assertEquals("hello world", o);
	}

	@Test
	public void testConstants8() throws Exception {
		Object o = checkConstantExpression("\"hello \" + 21");
		assertEquals("hello 21", o);
	}

	private Object checkConstantExpression(String exprStr) throws DataException {
		CompiledExpression expr = m_compiler.compile(exprStr, m_registry, context);
		assertTrue(expr instanceof ConstantExpression);
		return ((ConstantExpression) expr).getValue();
	}

	private Collection checkSubExpressionSize(ComplexExpression expr, int expected) {
		Collection subExpressions = expr.getSubExpressions();
		assertEquals(expected, subExpressions.size());
		return subExpressions;
	}

	private Collection checkSubConstantsSize(ComplexExpression expr, int expected) {
		Collection constantExpressions = expr.getConstantExpressions();
		assertEquals(expected, constantExpressions.size());
		return constantExpressions;
	}

	@Test
	public void testBlock() throws Exception {
		ComplexExpression expr = isComplexExpression("if( row.col1 == 100 ) row.col1 + row[\"col2\"]; else 100;");
		Object o = expr.evaluate(context, m_scope);
		assertTrue(o instanceof Double);
		assertEquals(new Double(100), o);
	}

	public static class Row extends ScriptableObject {
		private static final long serialVersionUID = 1756453855L;

		private int col1 = 100;
		private int col2 = 0;

		public int jsGet_col1() {
			return col1;
		}

		public int jsGet_col2() {
			return col2;
		}

		public Object get(int index, Scriptable start) {
			switch (index) {
			case 0:
				return new Integer(0); // index 0 is the row index position
			case 1:
				return new Integer(col1);
			case 2:
				return new Integer(col2);
			}

			return null;
		}

		public String getClassName() {
			return "Row";
		}
	}

	public static class AggrValue extends ScriptableObject {
		private static final long serialVersionUID = 244365478768L;

		public String getClassName() {
			return "AggrValue";
		}

		public Object get(int index, Scriptable start) {
			return new Integer(21 * index);
		}
	}
}
