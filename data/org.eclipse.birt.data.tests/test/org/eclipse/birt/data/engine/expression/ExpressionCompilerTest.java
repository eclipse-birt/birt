/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.expression;
import java.util.ArrayList;


import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for ExpressionCompiler
 */
public class ExpressionCompilerTest {

	private ExpressionCompiler compiler;
	
	private ArrayList aggrExprs = new ArrayList();
	
	private AggregateRegistry aggrReg = 
		new AggregateRegistry()
		{
			public int register(AggregateExpression aggregationExpr) throws DataException
			{
				aggrExprs.add( aggregationExpr );
				return aggrExprs.size() - 1;
			}
		};
	
	ScriptContext cx;
	Scriptable scope;
	
	/*
	 * @see TestCase#setUp()
	 */
	@Before
    public void expressionCompilerSetUp() throws Exception
	{

		compiler = new ExpressionCompiler( );
		
		// Test cases can share one context because they are executed on the same test thread
		cx = new ScriptContext();
		cx.compile("javascript", null, 0, "1 == 1;");
		scope = Context.getCurrentContext( ).initStandardObjects();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
    public void expressionCompilerTearDown() throws Exception
	{
		cx.close( );
	}
	@Test
    public void testDirectColRefByName( ) throws Exception
	{
		CompiledExpression expr;
		int type;
		String strColumnName;
		expr = compiler.compile( "row.col", null, cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_DIRECT_COL_REF );
		strColumnName = ( (ColumnReferenceExpression) expr ).getColumnName( );
		assertEquals( "col", strColumnName );
		
		expr = compiler
				.compile( "r.col", null, cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_COMPLEX_EXPR );

		//				expr = compiler.compile( "row.1" );
		//				type = expr.getType( );
		//				if ( type == CompiledExpression.TYPE_DIRECT_COL_REF )
		//				{
		//					System.out
		//							.println( "\"row.1\" is recognised as a direct column ref." );
		//				}
		//				else
		//				{
		//					System.out.println( "\"row.1\" failed." );
		//				}
		expr = compiler.compile( "row_a_boat.col", null, cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_COMPLEX_EXPR );
			expr = compiler.compile( "row.col.prop", null,cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_COMPLEX_EXPR );
		
	}
	@Test
    public void testDirectColRefByName1( ) throws Exception
	{
		CompiledExpression expr;
		int type;
		String strColumnName;

		expr = compiler.compile( "row[\"col\"]", null, cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_DIRECT_COL_REF );
		strColumnName = ( (ColumnReferenceExpression) expr ).getColumnName( );
		assertEquals( "col", strColumnName );
		
	}
	@Test
    public void testDirectColRefByIndex( ) throws Exception
	{
		CompiledExpression expr;
		int type;
		expr = compiler.compile( "row[1]", null, cx );
		type = expr.getType( );
		assertEquals( type, CompiledExpression.TYPE_DIRECT_COL_REF );
		assertEquals( ( (ColumnReferenceExpression) expr ).getColumnindex( ), 1 );
	}
	
	// Test aggregate expressions
	@Test
    public void testAggregateExpression() throws Exception
	{
		CompiledExpression expr, arg1, arg2, arg3, arg4;
		AggregateExpression aggr; 
		
		aggrExprs.clear();
		expr = compiler.compile(
				"Total.Sum( row.x )",
				aggrReg, cx);
		assertTrue( aggrExprs.size() == 1 );
		assertTrue( aggrExprs.get(0) == expr);
		assertTrue( expr.getType() == CompiledExpression.TYPE_SINGLE_AGGREGATE );
		aggr = (AggregateExpression ) expr;
		assertTrue( aggr.getArguments().size() == 1 );
		arg1 = (CompiledExpression) aggr.getArguments().get(0);
		assertTrue( arg1.getType() == CompiledExpression.TYPE_DIRECT_COL_REF);

		aggrExprs.clear();
		expr = compiler.compile(
				"Total.Sum( row[1], row.y > row.z, \"Group1\" )",
				aggrReg, cx);
		assertTrue( aggrExprs.size() == 1 );
		assertTrue( aggrExprs.get(0) == expr);
		assertTrue( expr.getType() == CompiledExpression.TYPE_SINGLE_AGGREGATE );
		aggr = (AggregateExpression ) expr;
		assertTrue( aggr.getArguments().size() == 3 );
		arg1 = (CompiledExpression) aggr.getArguments().get(0);
		assertTrue( arg1.getType() == CompiledExpression.TYPE_DIRECT_COL_REF);
		arg2 = (CompiledExpression)aggr.getArguments().get(1);
		assertTrue( arg2.getType() == CompiledExpression.TYPE_COMPLEX_EXPR);
		arg3 = (CompiledExpression)aggr.getArguments().get(0);
		assertTrue( arg3.getType() == CompiledExpression.TYPE_DIRECT_COL_REF);

		aggrExprs.clear();
		expr = compiler.compile(
				"Total.Sum( Total.Max(row.y), null, 1 )",
				aggrReg, cx);
		assertTrue( aggrExprs.size() == 2 );
		assertTrue( aggrExprs.get(0) == expr || aggrExprs.get(1) == expr );
		assertTrue( expr.getType() == CompiledExpression.TYPE_SINGLE_AGGREGATE );
		aggr = (AggregateExpression ) expr;
		assertTrue( aggr.getArguments().size() == 3 );
		arg1 = (CompiledExpression) aggr.getArguments().get(0);
		assertTrue( aggrExprs.get(0) == arg1 || aggrExprs.get(1) == arg1 );
		assertTrue( arg1.getType() == CompiledExpression.TYPE_SINGLE_AGGREGATE);
		arg2 = (CompiledExpression)aggr.getArguments().get(1);
		assertTrue( arg2.getType() == CompiledExpression.TYPE_CONSTANT_EXPR);
		arg3 = (CompiledExpression)aggr.getArguments().get(2);
		assertTrue( arg3.getType() == CompiledExpression.TYPE_CONSTANT_EXPR);
		
		aggrExprs.clear();
		expr = compiler.compile(
				" row[\"x\"] / Total.Sum( 1 )",
				aggrReg, cx);
		assertTrue( aggrExprs.size() == 1 );
		assertTrue( expr.getType() == CompiledExpression.TYPE_COMPLEX_EXPR );
		
		aggrExprs.clear();
		expr = compiler.compile(
				"Total.MovingAve( row.x, myfunc(), null, 1 )",
				aggrReg, cx);
		assertTrue( aggrExprs.size() == 1 );
		assertTrue( aggrExprs.get(0) == expr );
		assertTrue( expr.getType() == CompiledExpression.TYPE_SINGLE_AGGREGATE );
		aggr = (AggregateExpression ) expr;
		assertTrue( aggr.getArguments().size() == 4 );
		arg1 = (CompiledExpression) aggr.getArguments().get(0);
		assertTrue( arg1.getType() == CompiledExpression.TYPE_DIRECT_COL_REF);
		arg2 = (CompiledExpression)aggr.getArguments().get(1);
		assertTrue( arg2.getType() == CompiledExpression.TYPE_COMPLEX_EXPR);
		arg3 = (CompiledExpression)aggr.getArguments().get(2);
		assertTrue( arg3.getType() == CompiledExpression.TYPE_CONSTANT_EXPR);
		arg4 = (CompiledExpression)aggr.getArguments().get(3);
		assertTrue( arg4.getType() == CompiledExpression.TYPE_CONSTANT_EXPR);
	
		// Test invalid agg expr
		aggrExprs.clear();
		
		expr = compiler.compile( "Total.Invalid( row.x )", aggrReg, cx );
		assertTrue( expr instanceof InvalidExpression );
		
	}
	
	// Test expressions that contain multiple statements
	@Test
    public void testMultiExpression() throws Exception
	{
		this.aggrExprs.clear();
		CompiledExpression expr = compiler.compile(
				"a=1; b=a+1; b+1;",
				this.aggrReg,
				cx);
		assertEquals( expr.getType(), CompiledExpression.TYPE_COMPLEX_EXPR );
		assertTrue( this.aggrExprs.size() == 0);
		Object result = expr.evaluate( cx, scope );
		assertEquals( DataTypeUtil.convert( result, DataType.INTEGER_TYPE), new Integer(3));
		
		this.aggrExprs.clear();
		expr = compiler.compile(
				"a=true; if ( ! a && Total.Count() > 0 ) b=Total.Sum(row.x); else b=1 ; b",
				this.aggrReg,
				cx);
		assertEquals( expr.getType(), CompiledExpression.TYPE_COMPLEX_EXPR );
		result = expr.evaluate( cx, scope );
		assertEquals( DataTypeUtil.convert( result, DataType.INTEGER_TYPE), new Integer(1));
		// Expression should produce 2 aggregate expressions
		assertTrue( this.aggrExprs.size() == 2);
		assertTrue( ((IAggrFunction)((AggregateExpression)( aggrExprs.get(0))).getAggregation()).getName().equalsIgnoreCase("COUNT"));
		assertTrue( ((AggregateExpression)( aggrExprs.get(0))).getArguments().size() == 0 );
		assertTrue( ((IAggrFunction)((AggregateExpression)( aggrExprs.get(1))).getAggregation()).getName().equalsIgnoreCase("SUM")); 
		assertTrue( ((AggregateExpression)( aggrExprs.get(1))).getArguments().size() == 1 );
	}
	
	/*
	 * Please refer to SCR #75905
	 * Exception thrown out when choose "select value" without expression if set filter  
	 */
	@Test
    public void testSCR75905()
	{
		ExpressionCompiler compiler = new ExpressionCompiler( );

		CompiledExpression expr = compiler.compile( "", null, cx );
		assertTrue( expr instanceof InvalidExpression );
	}
}
