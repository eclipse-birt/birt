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

package org.eclipse.birt.data.engine.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for DistinctValue feature
 */

public class UsesDetailFalseTest extends APITestCase {
	/** Expression indicates in which column the distinct value will be gotten */
	private final IScriptExpression keyExpr1 = new ScriptExpression("dataSetRow.COL0");
	private final IScriptExpression keyExpr2 = new ScriptExpression("dataSetRow.COL1");
	private final IScriptExpression keyExpr3 = new ScriptExpression("dataSetRow.COL2");
	private final String keyName1 = "_COL0";
	private final String keyName2 = "_COL1";
	private final String keyName3 = "_COL2";

	/** Aggregation expression which require calculate the sum data in col0 */
	private final IScriptExpression aggrExpr1 = new ScriptExpression("Total.Sum( dataSetRow.COL0 )");
	private final IScriptExpression aggrExpr2 = new ScriptExpression("Total.Sum( dataSetRow.COL1 + dataSetRow.COL2 )");
	private final String aggrName1 = "_COL3";
	private final String aggrName2 = "_COL4";

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData1.TableName"),
				ConfigText.getString("Api.TestData1.TableSQL"), ConfigText.getString("Api.TestData1.TestDataFileName"));
	}

	/**
	 * Test whether result list value can be correctly gotten Result list includes:
	 * row data list aggregation value list starting group level list ending group
	 * level list
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		// step1: prepare querydefn, get IReportQueryDefn
		IQueryDefinition queryDefn = prepareReportQueryDefn(dataSet.getName());
		// step2: execute query, get IResultIterator
		IResultIterator resultIt = executeQuery(queryDefn);
		// step3: extract data from specifiled column, get result list
		List[] resultList = getQueryResult(resultIt);

		baseTest(resultList);

	}

	/**
	 * Test whether result list value can be correctly gotten Result list includes:
	 * row data list aggregation value list starting group level list ending group
	 * level list
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		// step1: prepare querydefn, get IReportQueryDefn
		IQueryDefinition queryDefn = prepareReportQueryDefn(dataSet.getName());
		// step2: execute query, get IResultIterator
		IResultIterator resultIt = executeQuery(queryDefn);

		resultIt.next();
		resultIt.skipToEnd(0);
		assertFalse(resultIt.next());
	}

	public void baseTest(List[] resultList) throws Exception {

		List rowList = resultList[0];
		List sumList = resultList[1];
		List sglList = resultList[2];
		List eglList = resultList[3];
		List subList = resultList[4];

		// row data
		testPrintln("---row data");
		for (int i = 0, j = 0; i < rowList.size(); i++, j++) {
			Integer value = (Integer) rowList.get(i);
			testPrintln(value.toString() + " (row0)");
			List subKeyRow1List = (List) subList.get(i * 3);
			List subKeyRow2List = (List) subList.get(i * 3 + 1);
			List subAggregaList = (List) subList.get(i * 3 + 2);
			testPrintln("   " + "---sub query data");
			testPrintln("   " + "row1" + "   " + "row2" + "  " + "aggr value(col0+col1)");
			for (int k = 0; k < subKeyRow1List.size(); k++) {
				String k1Value = subKeyRow1List.get(k).toString();
				String k2Value = subKeyRow2List.get(k).toString();
				String a1Value = subAggregaList.get(k).toString();
				testPrintln("    " + k1Value + "     " + k2Value + "        " + a1Value);
			}
		}

		// aggregation data
		testPrintln("---sum data (col0)");
		for (int i = 0, j = 0; i < sumList.size(); i++, j++) {
			Double value = (Double) sumList.get(i);
			testPrintln(value.toString());
		}

		// starting group level data
		testPrintln("---starting group level data");
		for (int i = 0; i < sglList.size(); i++) {
			Integer value = (Integer) sglList.get(i);
			testPrintln(value.toString());
		}

		// ending group level data
		testPrintln("---ending group level data");
		for (int i = 0; i < eglList.size(); i++) {
			Integer value = (Integer) eglList.get(i);
			testPrintln(value.toString());
		}

		checkOutputFile();
	}

	/**
	 * Prepare ReportQueryDefn 1: > only has one groupdefn 2: > set usesDetails flag
	 * as false
	 * 
	 * @param dataSetName
	 * @return queryDefn
	 */
	private IQueryDefinition prepareReportQueryDefn(String dataSetName) {
		// QueryDefn
		QueryDefinition queryDefn = new QueryDefinition(null);
		queryDefn.setDataSetName(dataSetName);
		queryDefn.setUsesDetails(false);

		// GroupDefn
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"), };
		keyExpr1.setGroupName("group1");
		groupDefn[0].setKeyColumn(keyName1);
		queryDefn.addResultSetExpression(keyName1, keyExpr1);
		aggrExpr1.setGroupName("group1");
		queryDefn.addResultSetExpression(aggrName1, aggrExpr1);

		// sub query
		SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST", queryDefn);
		subqueryDefn.setUsesDetails(false);

		GroupDefinition[] subGroupDefn = new GroupDefinition[] { new GroupDefinition("group2"),
				new GroupDefinition("group3") };
		keyExpr2.setGroupName("group2");
		keyExpr3.setGroupName("group3");
		subGroupDefn[0].setKeyColumn(keyName2);
		subqueryDefn.addResultSetExpression(keyName2, keyExpr2);
		aggrExpr2.setGroupName("group2");
		subqueryDefn.addResultSetExpression(aggrName2, aggrExpr2);
		subGroupDefn[1].setKeyColumn(keyName3);
		subqueryDefn.addResultSetExpression(keyName3, keyExpr3);

		for (int i = 0; i < subGroupDefn.length; i++)
			subqueryDefn.addGroup(subGroupDefn[i]);

		// add subquery
		groupDefn[0].addSubquery(subqueryDefn);

		for (int i = 0; i < groupDefn.length; i++)
			queryDefn.addGroup(groupDefn[i]);

		return queryDefn;
	}

	/**
	 * Get result list
	 * 
	 * @param resultIt
	 * @return List[] rowList, sumList, sglList, eglList
	 * @throws DataException
	 */
	private List[] getQueryResult(IResultIterator resultIt) throws Exception {
		List rowList = new ArrayList();
		List sumList = new ArrayList();
		List sglList = new ArrayList();
		List eglList = new ArrayList();
		List subList = new ArrayList();

		Scriptable subScope = new NativeObject();
		subScope.setPrototype(jsScope);
		subScope.setParentScope(jsScope);

		while (resultIt.next()) {
			rowList.add(resultIt.getValue(keyName1));
			sumList.add(resultIt.getValue(aggrName1));
			sglList.add(new Integer(resultIt.getStartingGroupLevel()));
			eglList.add(new Integer(resultIt.getEndingGroupLevel()));

			List subKeyRow1List = new ArrayList();
			List subKeyRow2List = new ArrayList();
			List subAggregaList = new ArrayList();
			IResultIterator subIterator = resultIt.getSecondaryIterator("IAMTEST", subScope);
			while (subIterator.next()) {
				subKeyRow1List.add(subIterator.getValue(keyName2));
				subKeyRow2List.add(subIterator.getValue(keyName3));
				subAggregaList.add(subIterator.getValue(aggrName2));
			}
			subList.add(subKeyRow1List);
			subList.add(subKeyRow2List);
			subList.add(subAggregaList);
		}

		return new List[] { rowList, sumList, sglList, eglList, subList };
	}
}
