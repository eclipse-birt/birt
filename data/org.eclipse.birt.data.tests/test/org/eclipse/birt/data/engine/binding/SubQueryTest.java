/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.binding;

import static org.junit.Assert.fail;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;

/**
 * Test table schema and data: col0 col1 col2 col3 0..2 0..2 0..2 0..2 line
 * number: 3*3*3*3
 */
public class SubQueryTest extends APITestCase {
	/** row expression defined in query defintion */
	private BaseExpression[] expressions;

	private String[] bindingNameRow;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData1.TableName"),
				ConfigText.getString("Api.TestData1.TableSQL"), ConfigText.getString("Api.TestData1.TestDataFileName"));
	}

	/**
	 * Sub query test Normal case: add subquery to GroupDefinition
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		// 1 prepare query execution
		Context cx = Context.enter();
		Scriptable sharedScope = cx.initStandardObjects();

		Scriptable subScope = cx.newObject(sharedScope);
		subScope.setParentScope(sharedScope);

		IQueryDefinition queryDefn = getDefaultQueryDefnWithSubQuery(dataSet.getName());
		expressions = getExpressionsOfDefaultQuery();

		// 2 do query execution
		IResultIterator resultIt = executeQuery(queryDefn);

		// 3.1 get sub query data
		resultIt.next();
		IResultIterator subIterator = resultIt.getSecondaryIterator("IAMTEST", sharedScope);

		// 3.2 get sub query of sub query data
		subIterator.next();
		IResultIterator subSubIterator = subIterator.getSecondaryIterator("IAMTEST2", subScope);

		bindingNameRow = this.getBindingExpressionName();
		// 4.1 output sub query data
		testPrintln("sub query data");
		outputData(subIterator);
		testPrintln("");

		// 4.2 output sub query of sub query data
		testPrintln("sub query of sub query data");
		outputData(subSubIterator);
		testPrintln("");

		// check whether output is correct
		checkOutputFile();
	}

	/**
	 * Boundary case: add subquery to QueryDefinition The data operation in subquery
	 * should not affect the data of outer query.
	 *
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		// execute query and return sub query
		IResultIterator resultIt = executeQuery(getAnotherSubQuery());
		resultIt.next();
		resultIt.getSecondaryIterator("IAMTEST", null);

		testPrintln("query data");
		outputData(resultIt);

		// check whether output is correct
		checkOutputFile();
	}

	/**
	 * Nearly same as test2, a little difference is there is no next operation
	 * applied to parent query to get the result iterator of sub query
	 *
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		// execute query and return sub query
		IResultIterator resultIt = executeQuery(getAnotherSubQuery());
		resultIt.getSecondaryIterator("IAMTEST", null);

		testPrintln("query data");
		outputData(resultIt);

		// check whether output is correct
		checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		// execute query and return sub query
		IResultIterator resultIt = executeQuery(getAnotherSubQuery(false));

		testPrintln("query data");
		while (resultIt.next()) {
			IResultIterator resultIt2 = resultIt.getSecondaryIterator("IAMTEST", null);
			outputData(resultIt2);
		}
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private QueryDefinition getAnotherSubQuery() throws DataException {
		return getAnotherSubQuery(true);
	}

	/**
	 * Create another subquery
	 *
	 * @return
	 * @throws DataException
	 */
	private QueryDefinition getAnotherSubQuery(boolean onGroup) throws DataException {
		// prepare query and sub query
		QueryDefinition queryDefn = (QueryDefinition) getDefaultQueryDefn(dataSet.getName());

		SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST", queryDefn);
		if (!onGroup) {
			subqueryDefn.setApplyOnGroupFlag(false);
		}

		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_COL2";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COL2");
		GroupDefinition[] subGroupDefn = { new GroupDefinition("group1") };
		subGroupDefn[0].setKeyExpression("row.GROUP_COL2");

		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[] { new ScriptExpression("dataSetRow[\"COL0\"]", 0),
				new ScriptExpression("dataSetRow[\"COL1\"]", 0), new ScriptExpression("dataSetRow.COL2", 0),
				new ScriptExpression("dataSetRow[\"COL3\"]", 0) };
		for (int i = 0; i < subGroupDefn.length; i++) {
			subqueryDefn.addGroup(subGroupDefn[i]);
			subqueryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
		}
		for (int i = 0; i < bindingNameRow.length; i++) {
			subqueryDefn.addResultSetExpression(bindingNameRow[i], expressions[i]);
		}

		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[] { new ScriptExpression("row[\"ROW_COL0\"]", 0),
				new ScriptExpression("row[\"ROW_COL1\"]", 0), new ScriptExpression("dataSetRow.COL2", 0),
				new ScriptExpression("row._outer[\"ROW_COL3\"]", 0) };

		queryDefn.addSubquery(subqueryDefn);
		return queryDefn;
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testMaxRow() throws Exception {
		// prepare query and sub query
		QueryDefinition queryDefn = (QueryDefinition) getDefaultQueryDefn(dataSet.getName());

		SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST", queryDefn);
		subqueryDefn.setMaxRows(10);

		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_COL2";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COL2");
		GroupDefinition[] subGroupDefn = { new GroupDefinition("group1") };

		subGroupDefn[0].setKeyExpression("row.GROUP_COL2");

		for (int k = 0; k < subGroupDefn.length; k++) {
			if (bindingNameGroup != null) {
				for (int i = 0; i < bindingNameGroup.length; i++) {
					subqueryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
				}
			}
			for (int i = 0; i < subGroupDefn.length; i++) {
				subqueryDefn.addGroup(subGroupDefn[i]);
			}
		}
		/* this.populateQueryExprMapping( subqueryDefn ); */

		FilterDefinition exprFilter = new FilterDefinition(new ScriptExpression("row.ROW_COL0+row.ROW_COL2>0"));
		subqueryDefn.addFilter(exprFilter);

		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[] { new ScriptExpression("dataSetRow.COL0", 0),
				new ScriptExpression("dataSetRow.COL1", 0), new ScriptExpression("dataSetRow.COL2", 0),
				new ScriptExpression("dataSetRow.COL3", 0) };
		for (int i = 0; i < expressions.length; i++) {
			queryDefn.addResultSetExpression(bindingNameRow[i], expressions[i]);
		}

		queryDefn.addSubquery(subqueryDefn);

		// execute query and return sub query
		IResultIterator resultIt = executeQuery(queryDefn);
		resultIt.next();

		bindingNameRow = this.getBindingExpressionName();
		IResultIterator subIterator = resultIt.getSecondaryIterator("IAMTEST", null);

		testPrintln("query data");
		outputData(subIterator);

		// check whether output is correct
		checkOutputFile();
	}

	/**
	 * Test case in which the sub query column binding uses column binding defined
	 * in parent query.
	 *
	 * @throws Exception
	 *
	 * @throws Exception
	 */
	@Test
	public void testUseParentColumnBindings() throws Exception {
		this.useParentColumnBindings(false);
		checkOutputFile();
	}

	/**
	 *
	 */
	@Test
	public void testUseParentColumnBindingWithAggregation() {
		try {
			this.useParentColumnBindings(true);
			fail("Should not arrive here!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void useParentColumnBindings(boolean includeTotal) throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.setDataSetName(dataSet.getName());

		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[] { new ScriptExpression("dataSetRow.COL0", 0),
				new ScriptExpression("dataSetRow.COL1", 0), new ScriptExpression("dataSetRow.COL2", 0),
				new ScriptExpression("dataSetRow.COL3", 0) };

		for (int i = 0; i < bindingNameRow.length; i++) {
			qd.addResultSetExpression(bindingNameRow[i], expressions[i]);
		}

		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression("row.ROW_COL0");
		qd.addGroup(groupDefn);

		// ---------- begin sub query ----------
		SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST", qd);
		groupDefn.addSubquery(subqueryDefn);

		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "sub1Group";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("row.ROW_COL1");
		GroupDefinition subGroupDefn = new GroupDefinition("group2");
		subGroupDefn.setKeyExpression("row.sub1Group");

		if (bindingNameGroup != null) {
			for (int i = 0; i < bindingNameGroup.length; i++) {
				subqueryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
			}
		}

		subqueryDefn.addGroup(subGroupDefn);
		subqueryDefn.addResultSetExpression("sub1Binding1",
				new ScriptExpression(includeTotal ? "Total.sum(row.ROW_COL2,null,1)" : "row.ROW_COL2"));

		// populateQueryExprMapping(subqueryDefn);

		// --- sub query of sub query
		SubqueryDefinition subSubqueryDefn = new SubqueryDefinition("IAMTEST2", subqueryDefn);
		subGroupDefn.addSubquery(subSubqueryDefn);
		subSubqueryDefn.addResultSetExpression("subsubBinding1", new ScriptExpression("row.sub1Group"));
		subSubqueryDefn.addResultSetExpression("subsubBinding2", new ScriptExpression("row.ROW_COL2"));
		subSubqueryDefn.addResultSetExpression("subsubBinding3", new ScriptExpression("row.sub1Binding1"));

		IResultIterator resultIt = executeQuery(qd);

		resultIt.next();

		String outputStr = "";
		for (int i = 0; i < expressions.length; i++) {
			Object object = resultIt.getValue(bindingNameRow[i]);
			outputStr += object.toString() + "    ";
		}
		testPrintln(outputStr);
		IResultIterator subIt = resultIt.getSecondaryIterator("IAMTEST", null);
		subIt.next();

		outputStr = "	";
		for (int i = 0; i < expressions.length; i++) {
			Object object = subIt.getValue(bindingNameRow[i]);
			outputStr += object.toString() + "    ";
		}
		outputStr += subIt.getValue("sub1Binding1").toString();
		testPrintln(outputStr);
		IResultIterator subsubIt = subIt.getSecondaryIterator("IAMTEST2", null);
		{
			while (subsubIt.next()) {
				outputStr = "		";
				for (int i = 0; i < expressions.length; i++) {
					Object object = subsubIt.getValue(bindingNameRow[i]);
					outputStr += object.toString() + "    ";
				}
				outputStr += subsubIt.getValue("sub1Binding1").toString() + "	";
				outputStr += subsubIt.getValue("subsubBinding1").toString() + "	";
				outputStr += subsubIt.getValue("subsubBinding2").toString() + "	";
				outputStr += subsubIt.getValue("subsubBinding3").toString() + "	";
				testPrintln(outputStr);
			}
		}
	}

	/**
	 * Output row data
	 *
	 * @param resultIt
	 * @throws DataException
	 */
	private void outputData(IResultIterator resultIt) throws Exception {
		while (resultIt.next()) {
			StringBuilder outputStr = new StringBuilder();
			for (int i = 0; i < expressions.length; i++) {
				Object object = resultIt.getValue(bindingNameRow[i]);
				outputStr.append(object.toString()).append("    ");
			}
			testPrintln(outputStr.toString());
		}
	}
}
