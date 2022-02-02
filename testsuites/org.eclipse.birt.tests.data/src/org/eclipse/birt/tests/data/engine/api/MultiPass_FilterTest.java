/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;

import testutil.APITestCase;
import testutil.ConfigText;

public class MultiPass_FilterTest extends APITestCase {

	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * Test feature of aggregation expression
	 */
	public void test_FilteWithTopN() throws Exception {

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		bindingNameFilter[1] = "FILTER_COUNTRY";
		bindingNameFilter[2] = "FILTER_SALE_DATE";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprFilter[2] = new ScriptExpression("dataSetRow.SALE_DATE");

		String[] bindingNameRow = new String[6];
		bindingNameRow[0] = "ROW_0";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_COUNTRY";
		bindingNameRow[3] = "ROW_CITY";
		bindingNameRow[4] = "ROW_SALE_DATE";
		bindingNameRow[5] = "ROW_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[6];
		bindingExprRow[0] = new ScriptExpression("dataSetRow[0]");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[4] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[5] = new ScriptExpression("dataSetRow.AMOUNT");
		// --- end binding
//		GroupDefinition[] groupDefn = null;
//		SortDefinition[] sortDefn = null;

//		String[] bindingNameFilter1 = new String[1];
//		bindingNameFilter[0] = "FILTER_AMOUNT";
//		IBaseExpression[] bindingExprFilter1 = new IBaseExpression[1];
//		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.AMOUNT" );

		FilterDefinition[] filters = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_TOP_N, "3")) };

		createAndRunQuery(null, null, null, null, null, null, bindingNameFilter, bindingExprFilter, filters,
				bindingNameRow, bindingExprRow);
	}

	/**
	 * filter on date time type column with operator Bottom N
	 */

	public void test_FilterWithBottomN() throws Exception {

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		bindingNameFilter[1] = "FILTER_COUNTRY";
		bindingNameFilter[2] = "FILTER_SALE_DATE";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprFilter[2] = new ScriptExpression("dataSetRow.SALE_DATE");

		String[] bindingNameRow = new String[6];
		bindingNameRow[0] = "ROW_0";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_COUNTRY";
		bindingNameRow[3] = "ROW_CITY";
		bindingNameRow[4] = "ROW_SALE_DATE";
		bindingNameRow[5] = "ROW_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[6];
		bindingExprRow[0] = new ScriptExpression("dataSetRow[0]");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[4] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[5] = new ScriptExpression("dataSetRow.AMOUNT");
		// --- end binding
//		GroupDefinition[] groupDefn = null;
//		SortDefinition[] sortDefn = null;
//
//		String[] bindingNameFilter1 = new String[1];
//		bindingNameFilter[0] = "FILTER_AMOUNT";
//		IBaseExpression[] bindingExprFilter1 = new IBaseExpression[1];
//		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.AMOUNT" );

		FilterDefinition[] filters = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_BOTTOM_N, "3")) };

		createAndRunQuery(null, null, null, null, null, null, bindingNameFilter, bindingExprFilter, filters,
				bindingNameRow, bindingExprRow);

	}

	/**
	 * add a filter to group
	 */

	public void test_FilterGroup() throws Exception {
		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filters = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.FILTER_AMOUNT > 100")) };

		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("Total.sum(row.ROW_AMOUNT)", IConditionalExpression.OP_TOP_PERCENT, "40"));

		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");

		groupDefn[0].addFilter(filterDefn);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, bindingNameFilter,
				bindingExprFilter, filters, bindingNameRow, bindingExprRow);

	}

	/**
	 * filter on group with bottom N
	 * 
	 * @throws Exception
	 */

	public void test_MultiPassFilterGroup() throws Exception {
		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filters = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.FILTER_AMOUNT > 100")) };

		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("Total.sum(row.ROW_AMOUNT)", IConditionalExpression.OP_TOP_PERCENT, "40"));

		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		groupDefn[0].addFilter(filterDefn);

		// add the second filter to group1
		filterDefn = new FilterDefinition(new ScriptExpression("Total.sum(row.ROW_AMOUNT,Total.NO_FILTER,2)<=400"));
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		groupDefn[1].addFilter(filterDefn);
		ConditionalExpression ce = new ConditionalExpression("Total.sum(row.ROW_AMOUNT)",
				IConditionalExpression.OP_BOTTOM_N, "1");
		ce.setGroupName("group1");
		filterDefn = new FilterDefinition(ce);
		groupDefn[1].addFilter(filterDefn);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, bindingNameFilter,
				bindingExprFilter, filters, bindingNameRow, bindingExprRow);
	}

	/**
	 * filter on group with bottom N with negative value
	 * 
	 * @throws Exception
	 */

	public void test_NegativeValueFilterGroup() throws Exception {
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(sqlStatement);

		IBaseExpression[] expressions = new IBaseExpression[] { new ScriptExpression("row.COUNTRY"),
				new ScriptExpression("row.AMOUNT"), new ScriptExpression("row.SALE_DATE") };
		String names[] = { "COL_COUNTRY", "COL_AMOUNT", "COL_SALE_DATE" };

		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());
		for (int i = 0; i < expressions.length; i++) {
			queryDefn.addResultSetExpression(names[i], expressions[i]);
		}

		FilterDefinition filterDefn = new FilterDefinition(new ConditionalExpression("Total.sum(row.Amount,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "-10"));
		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression("row.SALE_DATE");
		groupDefn.setInterval(2);
		groupDefn.setIntervalRange(IGroupDefinition.MONTH_INTERVAL);

		groupDefn.addFilter(filterDefn);
		try {
			queryDefn.addGroup(groupDefn);
			IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
			IQueryResults queryResults = preparedQuery.execute(null);
			IResultIterator resultIt = queryResults.getResultIterator();
			fail("should throw out exception here");
		} catch (DataException e) {

		}

	}

	/**
	 * filter on group with bottom N with invalid value
	 * 
	 * @throws Exception
	 */

	public void test_InvalidValueFilterGroup() throws Exception {
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(sqlStatement);

		IBaseExpression[] expressions = new IBaseExpression[] { new ScriptExpression("row.COUNTRY"),
				new ScriptExpression("row.AMOUNT"), new ScriptExpression("row.SALE_DATE") };

		String names[] = { "COL_COUNTRY", "COL_AMOUNT", "COL_SALE_DATE" };

		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());
		for (int i = 0; i < expressions.length; i++) {
			queryDefn.addResultSetExpression(names[i], expressions[i]);
		}

		FilterDefinition filterDefn = new FilterDefinition(new ConditionalExpression("Total.sum(row.Amount,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "abc"));
		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression("row.SALE_DATE");
		groupDefn.setInterval(2);
		groupDefn.setIntervalRange(IGroupDefinition.MONTH_INTERVAL);

		groupDefn.addFilter(filterDefn);
		try {
			queryDefn.addGroup(groupDefn);
			IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
			IQueryResults queryResults = preparedQuery.execute(null);
			IResultIterator resultIt = queryResults.getResultIterator();
			fail("should throw out exception here");
		} catch (DataException e) {

		}

	}

}
