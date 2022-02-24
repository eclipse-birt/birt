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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IParameterMetaData;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryExecutionHints;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import testutil.ConfigText;

import com.ibm.icu.text.DateFormat;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DtE features test.
 */
public class FeaturesTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * A standard report, test feature of: group, sort, filter.
	 */
	@Test
	public void test1() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_SALE_DATE");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.FILTER_AMOUNT > 100")) };

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

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
				bindingNameFilter, bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * A standard report, test feature of: group, sort, filter.
	 */
	@Test
	public void test2() throws Exception {
		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setExpression("row[\"__rownum\"]");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

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

		createAndRunQuery(null, null, null, bindingNameSort, bindingExprSort, sortDefn, null, null, null,
				bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of group.
	 */
	@Test
	public void test3() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

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

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of group, without pre-sorting.
	 */
	@Test
	public void test31() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOTAL_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[5];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.Sum(row.ROW_AMOUNT, null, \"group0\")");
		QueryDefinition query = createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null,
				null, bindingNameRow, bindingExprRow);

		QueryExecutionHints hints = new QueryExecutionHints();
		hints.setSortBeforeGrouping(false);
		query.setQueryExecutionHints(hints);
		executeQuery(query, bindingNameRow);

		checkOutputFile();
	}

	/**
	 * Test feature of incorrect sort key, throw exception
	 */
	@Test
	public void test4() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SALE_SORT";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("row.SALE_SORT");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

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

		try {
			createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
					null, null, null, bindingNameRow, bindingExprRow);
			fail("Should throw DteException!");
		} catch (Exception e) {
			// TODO: verify e has expected error code
		}
	}

	/**
	 * Test feature without any group, sort, filter
	 */
	@Test
	public void test6() throws Exception {
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

		createAndRunQuery(null, null, null, null, null, null, null, null, null, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of incorrect filter expression, throw exception
	 */
	@Test
	public void test7() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "SALE_FILTER";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMONT && 100");
//		wrong operator
		FilterDefinition[] filters = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.SALE_FILTER_E")) };

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

		try {
			createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, bindingNameFilter,
					bindingExprFilter, filters, bindingNameRow, bindingExprRow);
			fail("Should throw DteException!");
		} catch (Exception e) {
			// TODO: verify e has expected error code
		}
	}

	/**
	 * Test feature of group, sort, ConditionalExpression
	 */
	@Test
	public void test8() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_SALE_DATE");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		String[] bindingNameRow = new String[9];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_AMOUNT_2";
		bindingNameRow[5] = "ROW_CITY_2";
		bindingNameRow[6] = "ROW_CITY_3";
		bindingNameRow[7] = "ROW_SALE_DATE_2";
		bindingNameRow[8] = "ROW_AMOUNT_4";
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0),
				new ConditionalExpression("dataSetRow.AMOUNT", ConditionalExpression.OP_GT, "5"),
				new ConditionalExpression("dataSetRow.CITY", ConditionalExpression.OP_EQ, "'Beijing'"),
				new ConditionalExpression("dataSetRow.CITY", ConditionalExpression.OP_NE, "'Beijing'"),
				new ConditionalExpression("dataSetRow.SALE_DATE", ConditionalExpression.OP_GE, "'01/01/2004'"),
				new ConditionalExpression("dataSetRow.AMOUNT", ConditionalExpression.OP_BETWEEN, "5", "100") };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
				null, null, null, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of group, sort, ConditionalExpression
	 */
	@Test
	public void test19() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setExpression(new ScriptExpression("row[\"foo\"]"));
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		String[] bindingNameRow = new String[9];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_AMOUNT_2";
		bindingNameRow[5] = "ROW_CITY_2";
		bindingNameRow[6] = "ROW_CITY_3";
		bindingNameRow[7] = "ROW_SALE_DATE_2";
		bindingNameRow[8] = "ROW_AMOUNT_4";
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0),
				new ConditionalExpression("dataSetRow.AMOUNT", ConditionalExpression.OP_GT, "5"),
				new ConditionalExpression("dataSetRow.CITY", ConditionalExpression.OP_EQ, "'Beijing'"),
				new ConditionalExpression("dataSetRow.CITY", ConditionalExpression.OP_NE, "'Beijing'"),
				new ConditionalExpression("dataSetRow.SALE_DATE", ConditionalExpression.OP_GE, "'01/01/2004'"),
				new ConditionalExpression("dataSetRow.AMOUNT", ConditionalExpression.OP_BETWEEN, "5", "100") };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
				null, null, null, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Regression Test for SRC #78568 Filetr on date time doesn't work properly
	 */
	public void regressionTest78568() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_SALE_DATE");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_SALE_DATE";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ConditionalExpression("row.FILTER_SALE_DATE", ConditionalExpression.OP_GE,
						"'2004-03-20 00:00:00.0'")) };

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

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
				bindingNameFilter, bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of group, sort, filter
	 */
	@Test
	public void test9() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.SALE_DATE");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_SALE_DATE");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

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

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filterDefn = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("row.FILTER_AMOUNT", ConditionalExpression.OP_GT, "400")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
				bindingNameFilter, bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test feature of duplicate column name from different tables
	 */
	@Test
	public void test10_DuplicateColName() throws Exception {
		// Test a SQL with duplicate column name (quite common with join data sets)
		String testSQL = "select COUNTRY, COUNTRY, CITY from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		IBaseExpression[] bindingExprRow = new IBaseExpression[2];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator resultIt = queryResults.getResultIterator();
		assertTrue(resultIt.next());

		resultIt.getValue(bindingNameRow[0]);
		resultIt.getValue(bindingNameRow[1]);
	}

	/**
	 * Test aggregation with similar features.
	 */
	@Test
	public void test11() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameRow = new String[6];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_AMOUNT_2";
		bindingNameRow[5] = "ROW_AMOUNT_3";
		IBaseExpression[] bindingExprRow = new IBaseExpression[6];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.sum(row.ROW_AMOUNT,row.ROW_COUNTRY == \"US\",0)", 0);
		bindingExprRow[5] = new ScriptExpression("Total.sum(row.ROW_AMOUNT,row.ROW_COUNTRY == \"CHINA\",0)", 0);

		this.createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);
		checkOutputFile();
	}

	/**
	 * Test aggregation with similar features.
	 */
	@Test
	public void test12() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT",
				"ROW_AMOUNT_2", "ROW_AMOUNT_3", "ROW_AMOUNT_4", "ROW_AMOUNT_5", "ROW_AMOUNT_6", "ROW_AMOUNT_7",
				"ROW_AMOUNT_8", "ROW_AMOUNT_9", "ROW_AMOUNT_10", "ROW_AMOUNT_11", "ROW_AMOUNT_12", "ROW_AMOUNT_13",
				"ROW_AMOUNT_14", "ROW_AMOUNT_15", "ROW_AMOUNT_16", "ROW_AMOUNT_17", "ROW_AMOUNT_18", "ROW_AMOUNT_19",
				"ROW_AMOUNT_20", "ROW_AMOUNT_21", "ROW_AMOUNT_22", "ROW_AMOUNT_23", "ROW_AMOUNT_24", };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0),

				new ScriptExpression("Total.Rank(row.ROW_AMOUNT,true,null,2)", 0),
				new ScriptExpression("Total.Rank(row.ROW_AMOUNT,true,null,1)", 0),
				new ScriptExpression("Total.Rank(row.ROW_AMOUNT,true,null,Total.OVERALL)", 0),
				new ScriptExpression("Total.Sum(row.ROW_AMOUNT,null,2)", 0),
				new ScriptExpression("Total.isTopN(row.ROW_AMOUNT,1,null,2)", 0),
				new ScriptExpression("Total.isBottomN(row.ROW_AMOUNT,1,null,2)", 0),
				new ScriptExpression("Total.isTopNPercent(row.ROW_AMOUNT,50,null,2)", 0),
				new ScriptExpression("Total.isBottomNPercent(row.ROW_AMOUNT,50,null,2)", 0),

				new ScriptExpression("Total.Percentile(row.ROW_AMOUNT,0)", 0),
				new ScriptExpression("Total.Percentile(row.ROW_AMOUNT,1)", 0),
				new ScriptExpression("Total.Percentile(row.ROW_AMOUNT,0.5)", 0),

				new ScriptExpression("Total.Quartile(row.ROW_AMOUNT,0)", 0),
				new ScriptExpression("Total.Quartile(row.ROW_AMOUNT,1)", 0),
				new ScriptExpression("Total.Quartile(row.ROW_AMOUNT,0.5)", 0),

				new ScriptExpression("Total.PercentSum(row.ROW_AMOUNT,null,2)", 0),
				new ScriptExpression("Total.PercentSum(row.ROW_AMOUNT,null,1)", 0),
				new ScriptExpression("Total.PercentSum(row.ROW_AMOUNT,null,Total.OVERALL)", 0),

				new ScriptExpression("Total.PercentRank(row.ROW_AMOUNT,null,2)", 0),
				new ScriptExpression("Total.PercentRank(row.ROW_AMOUNT,null,1)", 0),
				new ScriptExpression("Total.PercentRank(row.ROW_AMOUNT,null,Total.OVERALL)", 0),

				new ScriptExpression("Total.RunningCount(null,2)", 0),
				new ScriptExpression("Total.RunningCount(null,1)", 0),
				new ScriptExpression("Total.RunningCount(null,Total.OVERALL)", 0)

		};

		this.createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);
		checkOutputFile();
	}

	/**
	 * Test filters in data sets and queries. The dataset filters should always be
	 * applied before the query filters.
	 */
	@Test
	public void test14() throws Exception {
		this.dataSet.addFilter(
				new FilterDefinition(new ConditionalExpression("row.AMOUNT", ConditionalExpression.OP_BOTTOM_N, "7")));
		this.dataSet.addFilter(
				new FilterDefinition(new ConditionalExpression("row.AMOUNT", ConditionalExpression.OP_LE, "600")));

		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT",
				"ROW_AMOUNT_2", "ROW_AMOUNT_3", };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0),
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT,dataSetRow.COUNTRY == \"US\",0)", 0),
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT,dataSetRow.COUNTRY == \"CHINA\",0)", 0) };

		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(
						new ConditionalExpression("row.ROW_COUNTRY", ConditionalExpression.OP_EQ, "\"CHINA\"")),
				new FilterDefinition(
						new ConditionalExpression("row.ROW_AMOUNT", ConditionalExpression.OP_BOTTOM_N, "4")),
				new FilterDefinition(
						new ConditionalExpression("row.ROW_AMOUNT", ConditionalExpression.OP_TOP_N, "5")) };

		this.createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, filterDefn,
				bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * Test filters in data sets and query with . The dataset filters should always
	 * be applied before the query filters.
	 */
	@Test
	public void test15() throws Exception {
		this.dataSet.addFilter(
				new FilterDefinition(new ConditionalExpression("row.AMOUNT", ConditionalExpression.OP_BOTTOM_N, "7")));

		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT",
				"ROW_AMOUNT_2", "ROW_AMOUNT_3", };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0),
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT,dataSetRow.COUNTRY == \"US\",0)", 0),
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT,dataSetRow.COUNTRY == \"CHINA\",0)", 0) };

		FilterDefinition[] filterDefn = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("row.ROW_COUNTRY", ConditionalExpression.OP_EQ, "\"CHINA\"")) };

		QueryDefinition queryDefn = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null,
				null, null, filterDefn, bindingNameRow, bindingExprRow);

		queryDefn.setMaxRows(2);
		executeQuery(queryDefn, bindingNameRow);

		checkOutputFile();
	}

	/**
	 * In java script expression, all the java type should be convert to java script
	 * type.
	 */
	@Test
	public void test16() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0"),
				new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("row.ROW_FILTER");
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ConditionalExpression("row.ROW_FILTER", IConditionalExpression.OP_EQ,
						"\"Date:\"+dataSetRow.SALE_DATE")) };

		String[] bindingNameRow = new String[7];
		bindingNameRow[0] = "ROW_0";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_COUNTRY";
		bindingNameRow[3] = "ROW_CITY";
		bindingNameRow[4] = "ROW_SALE_DATE";
		bindingNameRow[5] = "ROW_AMOUNT";
		bindingNameRow[6] = "ROW_FILTER";
		IBaseExpression[] bindingExprRow = new IBaseExpression[7];
		bindingExprRow[0] = new ScriptExpression("dataSetRow[0]");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[4] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[5] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[6] = new ScriptExpression("\"Date:\"+row.ROW_SALE_DATE");
		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, bindingNameFilter,
				bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * 
	 */
	@Test
	public void test17() {
		try {
			QueryDefinition query = new QueryDefinition(true);
			query.setDataSetName(this.dataSet.getName());

			GroupDefinition group = new GroupDefinition("G1");
			group.setKeyExpression("row.SALE_DATE");
			query.addGroup(group);

			GroupDefinition group1 = new GroupDefinition("G2");
			group1.setKeyExpression("row.COUNTRY");
			query.addGroup(group1);
			SubqueryDefinition subQuery = new SubqueryDefinition("Sub1", query);
			Binding binding = new Binding("COUNTRY_1", new ScriptExpression("dataSetRow.COUNTRY"));
			subQuery.addBinding(binding);
			GroupDefinition gd = new GroupDefinition("G2");
			gd.setKeyExpression("row.COUNTRY_1");
			subQuery.addGroup(gd);
			group.addSubquery(subQuery);

			IResultIterator it = this.executeQuery(query);
			it.next();
			it = it.getSecondaryIterator("Sub1", null);
		} catch (Exception e) {
			fail("Should not throw exception");
		}
	}

	/**
	 * Test feature with 1 group, the group key is an expression.
	 */
	@Test
	public void test18() throws Exception {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");

		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY + \"ABC\"");

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

		createAndRunQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	/**
	 * A summary table
	 */
	@Test
	public void testSummaryTable() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");

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

		QueryDefinition query = createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null,
				null, bindingNameRow, bindingExprRow);
		query.setIsSummaryQuery(true);
		executeQuery(query, bindingNameRow);
		checkOutputFile();
	}

	/**
	 * A summary table
	 */
	@Test
	public void testSummaryTableWithSub() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");

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

		QueryDefinition query = createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null,
				null, bindingNameRow, bindingExprRow);
		query.setIsSummaryQuery(true);
		SubqueryDefinition sub = new SubqueryDefinition("Sub", query);
		sub.addBinding(new Binding("b1", bindingExprRow[2]));
		query.addSubquery(sub);

		IResultIterator resultIt = executeQuery(query);
		testPrintln("*****A new Report Start!*****");
		while (resultIt.next()) {
			testPrint("S:");
			testPrint(Integer.toString(resultIt.getStartingGroupLevel()));
			testPrint(" E:");
			testPrint(Integer.toString(resultIt.getEndingGroupLevel()));
			testPrint(" ");
			for (int i = 0; i < bindingNameRow.length; i++) {
				testPrint(evalAsString(bindingNameRow[i], resultIt));
				testPrint("    ");
			}
			testPrintln("");
			IResultIterator subIt = resultIt.getSecondaryIterator(null, "Sub");
			while (subIt.next()) {
				testPrintln("          " + subIt.getValue("ROW_COUNTRY"));
			}
			subIt.close();
		}
		testPrintln("");

		checkOutputFile();
	}

	/**
	 * Test feature of GetParameterMetaData
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBasicGetParameterMetaData() throws Exception {
		String sql = "select COUNTRY, CITY from " + getTestTableName() + " where city = ?";
		((OdaDataSetDesign) this.dataSet).setQueryText(sql);

		QueryDefinition queryDefn = newReportQuery();
		;

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		Collection parameterMetaData = preparedQuery.getParameterMetaData();
		assertTrue(parameterMetaData != null && parameterMetaData.size() == 1);

		Iterator iter = parameterMetaData.iterator();
		while (iter.hasNext()) {
			IParameterMetaData paramMd = (IParameterMetaData) iter.next();
			assertEquals(1, paramMd.getPosition());
			assertEquals(DataType.STRING_TYPE, paramMd.getDataType());
			assertEquals(null, paramMd.getName());
			assertEquals("VARCHAR", paramMd.getNativeTypeName());
			assertEquals(0, paramMd.getScale());
			assertEquals(10, paramMd.getPrecision());
		}
	}

	/**
	 * Test feature of GetParameterMetaData1
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBasicGetParameterMetaData1() throws Exception {
		String sql = "select COUNTRY, CITY from " + getTestTableName() + " where city = ?";
		((OdaDataSetDesign) this.dataSet).setQueryText(sql);

		// add an input parameter hint
		ParameterDefinition inputParamDefn = new ParameterDefinition("param1", DataType.DECIMAL_TYPE, true, false);
		inputParamDefn.setPosition(1);
		inputParamDefn.setDefaultInputValue("0");
		((OdaDataSetDesign) this.dataSet).addParameter(inputParamDefn);

		QueryDefinition queryDefn = newReportQuery();

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		Collection parameterMetaData = preparedQuery.getParameterMetaData();
		assertTrue(parameterMetaData != null && parameterMetaData.size() == 1);

		Iterator iter = parameterMetaData.iterator();
		while (iter.hasNext()) {
			IParameterMetaData paramMd = (IParameterMetaData) iter.next();
			assertEquals(1, paramMd.getPosition());
			assertEquals(DataType.STRING_TYPE, paramMd.getDataType());
			assertEquals("param1", paramMd.getName());
			assertEquals("VARCHAR", paramMd.getNativeTypeName());
			assertEquals(0, paramMd.getScale());
			assertEquals(10, paramMd.getPrecision());
		}
	}

	/**
	 * Test feature of GetParameterMetaDataDefaultValue
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBasicGetParameterMetaDataDefaultValue() throws Exception {
		String sql = "select COUNTRY, CITY from " + getTestTableName() + " where city = ?";
		((OdaDataSetDesign) this.dataSet).setQueryText(sql);

		// add an input parameter hint
		ParameterDefinition inputParamDefn = new ParameterDefinition("param1", DataType.DECIMAL_TYPE);
		inputParamDefn.setInputMode(true);
		inputParamDefn.setDefaultInputValue("123");
		inputParamDefn.setPosition(1);

		((OdaDataSetDesign) this.dataSet).addParameter(inputParamDefn);

		QueryDefinition queryDefn = newReportQuery();

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		Collection parameterMetaData = preparedQuery.getParameterMetaData();
		assertTrue(parameterMetaData != null && parameterMetaData.size() == 1);

		Iterator iter = parameterMetaData.iterator();
		while (iter.hasNext()) {
			IParameterMetaData paramMd = (IParameterMetaData) iter.next();
			assertEquals(1, paramMd.getPosition());
			assertEquals(DataType.STRING_TYPE, paramMd.getDataType());
			assertEquals("param1", paramMd.getName());
			assertEquals("VARCHAR", paramMd.getNativeTypeName());
			assertEquals(0, paramMd.getScale());
			assertEquals(10, paramMd.getPrecision());
			assertEquals("123", paramMd.getDefaultInputValue());
		}
	}

	/**
	 * Test feature of NativeColumnTypeName
	 * 
	 * @throws Exception
	 */
//	public void testNativeColumnTypeName() throws Exception
//	{
//		String testSQL = "select COUNTRY, CITY from " + getTestTableName( );
//		( (OdaDataSetDesign) this.dataSet ).setQueryText( testSQL );
//		
//		QueryDefinition queryDefn = newReportQuery( );
//
//		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
//		IQueryResults queryResults = preparedQuery.execute( null );
//		IResultMetaData metadata = queryResults.getResultMetaData( );
//
//		assertEquals( "VARCHAR", metadata.getColumnNativeTypeName( 1 ) );
//		assertEquals( "VARCHAR", metadata.getColumnNativeTypeName( 2 ) );
//	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPassThruContext() throws Exception {
		String testSQL = "select COUNTRY, CITY from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0) };

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);
		;

		// pass in an application context to the underlying ODA driver;
		// limited testing here to make sure normal processing succeeds
		HashMap appContext = new HashMap();
		appContext.put(dataSet.getDataSourceName(), testSQL);
		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn, appContext);

		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator resultIt = queryResults.getResultIterator();
		assertTrue(resultIt.next());

		resultIt.getValue(bindingNameRow[0]);
		resultIt.getValue(bindingNameRow[1]);
	}

	/**
	 * Test feature of the usage of Expression Data Type
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAggrExprAndInconvertibleDataType() throws Exception {
		Object[] expectedValue = new Object[] { "CHINA", Timestamp.valueOf("2004-01-01 00:00:00.0"), Integer.class };

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_SALE_DATE", "ROW_AMOUNT" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] {
				new ScriptExpression("dataSetRow.COUNTRY", DataType.STRING_TYPE),
				new ScriptExpression("dataSetRow.SALE_DATE", DataType.UNKNOWN_TYPE),
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT)", DataType.INTEGER_TYPE) };

		IBaseExpression[] expressions = new IBaseExpression[] {
				new ScriptExpression("row.COUNTRY", DataType.STRING_TYPE),
				new ScriptExpression("row.SALE_DATE", DataType.UNKNOWN_TYPE),
				new ScriptExpression("Total.sum(row.AMOUNT)", DataType.INTEGER_TYPE) };

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);

		IResultIterator resultIt = queryResults.getResultIterator();
		resultIt.next();
		for (int i = 0; i < expressions.length; i++) {
			Object value = expectedValue[i];
			if (value instanceof Class)
				assertTrue(((Class) value).isInstance(resultIt.getValue(bindingNameRow[i])));
			else
				assertEquals(value, resultIt.getValue(bindingNameRow[i]));

		}

		// wrong configuration

		queryDefn = createQuery(null, null, null, null, null, null, null, null, null, new String[] { "ROW_CITY", },
				new IBaseExpression[] { new ScriptExpression("dataSetRow.CITY", DataType.INTEGER_TYPE) });

		preparedQuery = dataEngine.prepare(queryDefn);
		queryResults = preparedQuery.execute(null);

		try {
			queryResults.getResultIterator();
			fail("expected error here");
		} catch (DataException e) {
			assertEquals(e.getErrorCode(), ResourceConstants.WRAPPED_BIRT_EXCEPTION);
		}

	}

	/**
	 * Test of invalid filter expression row[0]
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRow0() throws Exception {
		String[] bindingNameFilter = new String[] { "ROW_0" };
		IBaseExpression[] bindingExprFilter = new IBaseExpression[] { new ScriptExpression("dataSetRow[0]", 0) };
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ConditionalExpression("row.ROW_0", ConditionalExpression.OP_GT, "400")) };

		try {
			this.createAndRunQuery(null, null, null, null, null, null, bindingNameFilter, bindingExprFilter, filterDefn,
					null, null);
			// TODO: does it can be determined?
			// fail( "exception expected" );
		} catch (DataException e) {
			// assertEquals( "The filter expression \"row[0]\" is not valid.",
			// e.getMessage( ) );
		}
	}

	/**
	 * Tests BEFORE_FIRST_ROW and AFTER_LAST_ROW expressions
	 */
	@Test
	public void testExprTiming() throws Exception {
		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_ORDERED", "ROW_ORDERED_2" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] {
				new ScriptExpression("dataSetRow.COUNTRY", DataType.STRING_TYPE),
				new ScriptExpression("Total.Sum(dataSetRow.ORDERED)", DataType.INTEGER_TYPE),
				new ScriptExpression("Total.Sum(dataSetRow.ORDERED * 2)", DataType.INTEGER_TYPE) };

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults results = preparedQuery.execute(null);
		IResultIterator it = results.getResultIterator();

		// Test before expressions
		final int totalOrdered = 4;
		int total1 = it.getInteger(bindingNameRow[1]).intValue();
		assertEquals(total1, totalOrdered);

		// Test row expressions
		int count = 0;
		while (it.next()) {
			++count;
			String country = it.getString(bindingNameRow[0]);
			assertTrue(country.length() > 0);
		}
		assertEquals(count, 8);

		// Test after expression
		int total2 = it.getInteger(bindingNameRow[2]).intValue();
		assertEquals(total2, totalOrdered * 2);

		// No expression should evaluate after iterator.close
		it.close();
		try {
			it.getValue(bindingNameRow[2]);
			fail("Failed: evaluation should fail after IResultIterator.close()");
		} catch (BirtException e) {
			// good
		}
		results.close();
	}

	/**
	 * Test feature of group.
	 */
	@Test
	public void testMoveto() throws Exception {
		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT", };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0), };

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator resultIterator = queryResults.getResultIterator();
		final int destIndex = 3;
		resultIterator.next();
		resultIterator.moveTo(destIndex - 2);
		resultIterator.moveTo(destIndex - 1);
		resultIterator.moveTo(destIndex - 1);

		testPrint(evalAsString(bindingNameRow[0], resultIterator));

		while (resultIterator.next()) {
			for (int i = 0; i < bindingNameRow.length; i++) {
				testPrint(evalAsString(bindingNameRow[i], resultIterator));
				testPrint("    ");
			}
			testPrintln("");
		}

		checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testResultIteratorFindGroup() throws Exception {
		ComputedColumn computedColumn = new ComputedColumn("\"doubleQuo\"", "row.CITY", DataType.STRING_TYPE);
		this.dataSet.addComputedColumn(computedColumn);

		String[] bindingNameGroup = new String[] { "GROUP_COUNTRY", "GROUP_CITY", "GROUP_NULL_COLUMN" };
		IBaseExpression[] bindingExprGroup = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]"),
				new ScriptExpression("dataSetRow[\"\\\"doubleQuo\\\"\"]"),
				new ScriptExpression("dataSetRow.NULL_COLUMN") };
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2"), new GroupDefinition("group3") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");
		groupDefn[2].setKeyExpression("row.GROUP_NULL_COLUMN");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT",
				"ROW_NULL_COLUMN" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0), new ScriptExpression("dataSetRow.NULL_COLUMN") };

		QueryDefinition queryDefn = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null,
				null, null, null, bindingNameRow, bindingExprRow);
		IResultIterator it = executeQuery(queryDefn);
		it.next();
		testFindGroup(it, new Object[] { "CHINA" }, 8);
		testFindGroup(it, new Object[] { "CHINA", "Beijing" }, 8);
		testFindGroup(it, new Object[] { "CHINA", "Shanghai" }, 6);
		testFindGroup(it, new Object[] { "US" }, 4);
		testFindGroup(it, new Object[] { "US", "New York" }, 2);
		testFindGroup(it, new Object[] { "US", "Chicago" }, 4);

		assertFalse(it.findGroup(new Object[] { null, null }));
		assertFalse(it.findGroup(new Object[] { null, "Chicago" }));
		assertFalse(it.findGroup(new Object[] { "CHINA", "Chicago" }));
		assertFalse(it.findGroup(new Object[] { "Chicago" }));
		assertFalse(it.findGroup(new Object[] { "CHINA", "Shanghai", "not null" }));

		try {
			it.findGroup(new Object[] { null, null, null, null });
			fail("Should not arrive here");
		} catch (DataException e) {

		}
	}

	/**
	 * 
	 */
	@Test
	public void testResultIteratorFindGroup2() throws Exception {
		String[] bindingNameGroup = new String[] { "GROUP_COUNTRY", "GROUP_AMOUNT" };
		IBaseExpression[] bindingExprGroup = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]"),
				new ScriptExpression("dataSetRow.AMOUNT") };
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_AMOUNT");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0) };

		QueryDefinition queryDefn = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null,
				null, null, null, bindingNameRow, bindingExprRow);
		IResultIterator it = executeQuery(queryDefn);
		it.next();
		testFindGroup(it, new Object[] { "CHINA" }, 8);
		testFindGroup(it, new Object[] { "CHINA", "100" }, 8);
		testFindGroup(it, new Object[] { "CHINA", new Integer(100) }, 8);
		testFindGroup(it, new Object[] { "CHINA", "400" }, 6);

		try {
			testFindGroup(it, new Object[] { "CHINA", "abc" }, 6);
			fail("Should convert fails");
		} catch (BirtException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	@Test
	public void testResultIteratorFindGroup3() throws Exception {
		String[] bindingNameGroup = new String[] { "GROUP_SALE_DATE", "GROUP_AMOUNT" };
		IBaseExpression[] bindingExprGroup = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"SALE_DATE\"]"),
				new ScriptExpression("dataSetRow.AMOUNT") };
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2") };
		groupDefn[0].setKeyExpression("row.GROUP_SALE_DATE");
		groupDefn[1].setKeyExpression("row.GROUP_AMOUNT");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0) };

		QueryDefinition queryDefn = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null,
				null, null, null, bindingNameRow, bindingExprRow);

		IResultIterator it = executeQuery(queryDefn);
		it.next();

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
		testFindGroup(it, new Object[] { df.format(df.parse("05/01/2004")) }, 5);
		testFindGroup(it, new Object[] { df.format(df.parse("06/01/2004")), "100" }, 3);
		testFindGroup(it, new Object[] { df.format(df.parse("06/01/2004")), new Integer(100) }, 3);
		testFindGroup(it, new Object[] { df.format(df.parse("06/05/2004")), "400" }, 1);

		try {
			testFindGroup(it, new Object[] { "CHINA", "abc" }, 6);
			fail("Should convert fails");
		} catch (BirtException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResultSetIteratorIsEmptyFalse() throws Exception {
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

		QueryDefinition queryDefination = createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IResultIterator result = executeQuery(queryDefination);

		assertFalse(result.isEmpty());
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResultSetIteratorIsEmptyTrue() throws Exception {
		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.FILTER_AMOUNT < 0")) };

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

		QueryDefinition queryDefination = createQuery(null, null, null, null, null, null, bindingNameFilter,
				bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		IResultIterator result = executeQuery(queryDefination);

		assertTrue(result.isEmpty());
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNestedDummyQuery() throws Exception {
		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		FilterDefinition[] filterDefn = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.FILTER_AMOUNT < 0")) };

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

		QueryDefinition queryDefination = createQuery(null, null, null, null, null, null, bindingNameFilter,
				bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow);

		IResultIterator result = executeQuery(queryDefination);

		IBaseQueryResults outer = result.getQueryResults();

		QueryDefinition dummyQd = new QueryDefinition();
		IPreparedQuery preparedQuery = dataEngine.prepare(dummyQd);
		IQueryResults queryResults = preparedQuery.execute(outer, null);

		assertNotNull(queryResults);
	}

	/**
	 * Bug 153006
	 */
	@Test
	public void testGroupWithNoDetails() throws Exception {
		String[] bindingNameGroup = new String[] { "GROUP_ROW_COUNTRY" };
		IBaseExpression[] bindingExprGroup = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]") };
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");

		String[] bindingNameRow = new String[] { "ROW_COUNTRY", "ROW_CITY", "ROW_SALE_DATE", "ROW_AMOUNT" };
		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow[\"COUNTRY\"]", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0) };

		QueryDefinition queryDefn = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null,
				null, null, null, bindingNameRow, bindingExprRow);
		queryDefn.setUsesDetails(false);

		IResultIterator it = executeQuery(queryDefn);
		while (it.next()) {
			int start1 = it.getStartingGroupLevel();
			it.getEndingGroupLevel();
			int start2 = it.getStartingGroupLevel();
			assertEquals(start1, start2);
		}
	}

	/**
	 * @param it
	 * @param keyValues
	 * @param restRowCount
	 * @throws DataException
	 * @throws BirtException
	 */
	private void testFindGroup(IResultIterator it, Object[] keyValues, int restRowCount)
			throws DataException, BirtException {
		assertTrue(it.findGroup(keyValues));
		int i = 1;
		while (it.next()) {
			i++;
		}
		assertEquals(i, restRowCount);
	}

	/**
	 * Test feature without any group, sort, filter
	 */
	@Test
	public void testRowIndex() throws Exception {
		String[] bindingNameRow = new String[1];
		bindingNameRow[0] = "ROW_COUNTRY";
		IBaseExpression[] bindingExprRow = new IBaseExpression[1];
		bindingExprRow[0] = new ScriptExpression("dataSetRow._rowPosition");

		createAndRunQuery(null, null, null, null, null, null, null, null, null, bindingNameRow, bindingExprRow);

		// checkOutputFile();
	}

	/**
	 * Test "FetchRowLimit" feature. The fetch row limit is the number of rows that
	 * a data set can fetch from data source. In this test case the limit is set to
	 * zero, which indicate no limit at all.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetchRowLimit1() throws Exception {
		this.fetchRowLimit(0);
	}

	/**
	 * Test "FetchRowLimit" feature. The fetch row limit is the number of rows that
	 * a data set can fetch from data source. In this test case the limit is set to
	 * 3, which indicate to max three rows should be fetched from data source.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetchRowLimit2() throws Exception {
		this.fetchRowLimit(3);
	}

	/**
	 * Test "FetchRowLimit" feature. The fetch row limit is the number of rows that
	 * a data set can fetch from data source. In this test case the limit is set to
	 * negative number, which indicate no limit at all.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetchRowLimit3() throws Exception {
		this.fetchRowLimit(-10);
	}

	/**
	 * 
	 * @param limit
	 * @throws Exception
	 */
	private void fetchRowLimit(int limit) throws Exception {
		String[] bindingNameRow = new String[1];
		bindingNameRow[0] = "ROW_COUNTRY";
		IBaseExpression[] bindingExprRow = new IBaseExpression[1];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		this.dataSet.setRowFetchLimit(limit);

		FilterDefinition[] filters = new FilterDefinition[] {
				new FilterDefinition(new ScriptExpression("row.ROW_COUNTRY != \"CHINA\"")) };
		createAndRunQuery(null, null, null, null, null, null, null, null, filters, bindingNameRow, bindingExprRow);

		checkOutputFile();
	}

	@Test
	public void testRefToInvalidDataSetColumn() {
		String[] bindingNameRow = new String[1];
		bindingNameRow[0] = "ROW_COUNTRY";
		IBaseExpression[] bindingExprRow = new IBaseExpression[1];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.invalid");

		try {
			createAndRunQuery(null, null, null, null, null, null, null, null, null, bindingNameRow, bindingExprRow);
			fail("Should not arrive here");
		} catch (Exception e) {

		}

	}
}
