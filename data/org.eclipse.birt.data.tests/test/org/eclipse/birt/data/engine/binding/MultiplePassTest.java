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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 */

public class MultiplePassTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * Test TopN and BottomN filters
	 */
	@Test
	public void testTopBottomN() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = null;

		IBaseExpression[] bindingExprGroup = null;

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

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
		GroupDefinition[] groupDefn = null;
		SortDefinition[] sortDefn = null;
		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_0", 0),
				new ScriptExpression("row.ROW_rowPosition ", 0), new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };
		FilterDefinition[] filters = {

				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_BOTTOM_N, "6")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_COUNTRY", IConditionalExpression.OP_BOTTOM_N, "5")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_SALE_DATE", IConditionalExpression.OP_TOP_N, "3")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
	}

	private void createAndRunQuery(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
			String[] bindingNameSort, IBaseExpression[] bindingExprSort, String[] bindingNameFilter,
			IBaseExpression[] bindingExprFilter, String[] bindingNameRow, IBaseExpression[] bindingExprRow,
			IBaseExpression[] expressions, GroupDefinition[] groupDefn, SortDefinition[] sortDefn,
			FilterDefinition[] filters) throws Exception, IOException {
		QueryDefinition queryDefn = createQueryDefn(bindingNameRow, expressions, groupDefn, sortDefn, filters);

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, queryDefn);
	}

	private void populateBindings(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
			String[] bindingNameSort, IBaseExpression[] bindingExprSort, String[] bindingNameFilter,
			IBaseExpression[] bindingExprFilter, String[] bindingNameRow, IBaseExpression[] bindingExprRow,
			QueryDefinition queryDefn) {
		populateBindings(queryDefn, bindingNameGroup, bindingExprGroup);
		populateBindings(queryDefn, bindingNameSort, bindingExprSort);
		populateBindings(queryDefn, bindingNameFilter, bindingExprFilter);
		populateBindings(queryDefn, bindingNameRow, bindingExprRow);
	}

	private void populateBindings(QueryDefinition queryDefn, String[] name, IBaseExpression[] expr) {
		if (name != null && expr != null) {
			for (int i = 0; i < name.length; i++) {
				queryDefn.addResultSetExpression(name[i], expr[i]);
			}
		}
	}

	/**
	 * Test TopPercent and BottomPercent filters
	 */
	@Test
	public void testTopBottomPercent() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = null;

		IBaseExpression[] bindingExprGroup = null;

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

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

		GroupDefinition[] groupDefn = null;
		SortDefinition[] sortDefn = null;

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_0", 0),
				new ScriptExpression("row.ROW_rowPosition ", 0), new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_BOTTOM_PERCENT, "75")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_COUNTRY", IConditionalExpression.OP_BOTTOM_N, "5")),
				new FilterDefinition(new ConditionalExpression("row.FILTER_SALE_DATE",
						IConditionalExpression.OP_TOP_PERCENT, "60")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
	}

	/**
	 * Test TopN and BottomN filters
	 */
	@Test
	public void testTopBottmNwithOtherFilters() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = null;

		IBaseExpression[] bindingExprGroup = null;

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

		String[] bindingNameFilter = new String[2];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		bindingNameFilter[1] = "FILTER_COUNTRY";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[2];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.COUNTRY");

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

		GroupDefinition[] groupDefn = null;
		SortDefinition[] sortDefn = null;

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_0", 0),
				new ScriptExpression("row.ROW_rowPosition ", 0), new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_GT, "100")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_GT, "400")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_BOTTOM_N, "16")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_COUNTRY", IConditionalExpression.OP_BOTTOM_N, "1")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
	}

	private void createAndRunQuery(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
			String[] bindingNameSort, IBaseExpression[] bindingExprSort, String[] bindingNameFilter,
			IBaseExpression[] bindingExprFilter, String[] bindingNameRow, IBaseExpression[] bindingExprRow,
			IBaseExpression[] expressions, QueryDefinition queryDefn) throws Exception, IOException {
		// --- begin binding use
		populateBindings(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, queryDefn);
		// --- end binding use

		executeQuery(queryDefn, bindingNameRow);

		checkOutputFile();
	}

	/**
	 * Test TopPercent and BottomPercent filters
	 */
	@Test
	public void testTopBottmPercentWithOtherFilters() throws Exception {

		// --- begin binding
		String[] bindingNameGroup = null;

		IBaseExpression[] bindingExprGroup = null;

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

		String[] bindingNameFilter = new String[2];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		bindingNameFilter[1] = "FILTER_COUNTRY";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[2];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.COUNTRY");

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

		GroupDefinition[] groupDefn = null;
		SortDefinition[] sortDefn = null;

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_0", 0),
				new ScriptExpression("row.ROW_rowPosition ", 0), new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(new ConditionalExpression("row.FILTER_AMOUNT",
						IConditionalExpression.OP_BOTTOM_PERCENT, "100")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_GT, "100")),
				new FilterDefinition(new ConditionalExpression("row.FILTER_COUNTRY",
						IConditionalExpression.OP_BOTTOM_PERCENT, "75")),
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_GT, "400")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
	}

	/**
	 * Test TopN and BottomN filters
	 */
	@Test
	public void testInvalidFilter() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = null;

		IBaseExpression[] bindingExprGroup = null;

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_AMOUNT";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");

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

		GroupDefinition[] groupDefn = null;
		SortDefinition[] sortDefn = null;

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_0", 0),
				new ScriptExpression("row.ROW_rowPosition ", 0), new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = { new FilterDefinition(
				new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_TOP_N, "-1")) };

		try {
			createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
					bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
			fail("Should not reach here");
		} catch (Exception e) {

		}

		filters = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("row.FILTER_AMOUNT", IConditionalExpression.OP_TOP_N, "abc")) };
		try {
			createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
					bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, sortDefn, filters);
			fail("Should not reach here");
		} catch (Exception e) {

		}
	}

	/**
	 * Test feature of group filtering.
	 */
	@Test
	public void testGroupFiltering() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = null;
		IBaseExpression[] bindingExprSort = null;

		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		FilterDefinition filterDefn = new FilterDefinition(
				new ScriptExpression("Total.sum(row.ROW_AMOUNT,Total.NO_FILTER)>7000"));
		groupDefn[0].addFilter(filterDefn);

		filterDefn = new FilterDefinition(new ScriptExpression("Total.sum(row.ROW_AMOUNT,Total.NO_FILTER,2)<=400"));
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		groupDefn[1].addFilter(filterDefn);
		ConditionalExpression ce = new ConditionalExpression("Total.sum(row.ROW_AMOUNT)",
				IConditionalExpression.OP_BOTTOM_N, "1");
		ce.setGroupName("group1");
		filterDefn = new FilterDefinition(ce);
		groupDefn[1].addFilter(filterDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);
	}

	/**
	 * Test feature of group sorting.
	 */
	@Test
	public void testGroupSorting() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,Total.NO_FILTER,1)");
		// desc
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		// This line should not affect the final result, for it is overpowered by
		// the group sorts defined using "addSort()" method.
		groupDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,Total.NO_FILTER,Total.CURRENT_GROUP)");
		// desc
		sortDefn.setSortDirection(0);
		groupDefn[1].addSort(sortDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);

	}

	/**
	 * Test feature of group sorting.
	 */
	@Test
	public void testGroupSorting2() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		groupDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		groupDefn[1].setSortDirection(ISortDefinition.SORT_DESC);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);

	}

	/**
	 * Test feature of group filtering + sorting.
	 */
	@Test
	public void testGroupFilteringSorting1() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,null,Total.CURRENT_GROUP)");
		// desc
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		FilterDefinition filterDefn = new FilterDefinition(
				new ScriptExpression("Total.sum(dataSetRow.AMOUNT,null,2)>400"));
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		groupDefn[1].addFilter(filterDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);

	}

	/**
	 * Test feature of group filtering + sorting.
	 */
	@Test
	public void testGroupFilteringSorting2() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_1";
		bindingNameFilter[1] = "FILTER_2";
		bindingNameFilter[2] = "FILTER_3";

		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT,null,1)");
		bindingExprFilter[1] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT,null,2)");
		bindingExprFilter[2] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT,null,2)<7400");

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("row.FILTER_1", IConditionalExpression.OP_BOTTOM_N, "2"));
		groupDefn[0].addFilter(filterDefn);

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");

		filterDefn = new FilterDefinition(
				new ConditionalExpression("row.FILTER_2", IConditionalExpression.OP_TOP_N, "1"));
		groupDefn[1].addFilter(filterDefn);
		filterDefn = new FilterDefinition(new ScriptExpression("row.FILTER_3"));
		groupDefn[1].addFilter(filterDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);
	}

	/**
	 * Test feature of group filtering + sorting.
	 */
	@Test
	public void testGroupFilteringSorting3() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_GROUP0";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "75")));
		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_TOP_N, "2")));

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,null,1)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };
		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);
	}

	/**
	 * This test is to fix a topN related bug with bugzilla bug#124447
	 */
	@Test
	public void testGroupFilteringSorting4() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_GROUP0";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "75")));
		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_TOP_N, "2")));

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,null,1)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		QueryDefinition queryDefn = createQueryDefn(bindingNameRow, bindingExprRow, groupDefn, null, null);

		// --- begin binding use
		populateBindings(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, queryDefn);
		// --- end binding use

		executeQuery(queryDefn, bindingNameRow);

		executeQuery(queryDefn, bindingNameRow);

		checkOutputFile();
	}

	/**
	 * Test filterings including group filters and multi-pass row filters
	 *
	 * @throws Exception
	 */
	@Test
	public void testMixedFiltering() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_GROUP0";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};

		String[] bindingNameFilter = new String[2];
		bindingNameFilter[0] = "FILTER_1";
		bindingNameFilter[1] = "FILTER_2";

		IBaseExpression[] bindingExprFilter = new IBaseExpression[2];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.AMOUNT");

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "75")));

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,null,1)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_1", IConditionalExpression.OP_BOTTOM_N, "7")),
				new FilterDefinition(new ConditionalExpression("row.FILTER_2", IConditionalExpression.OP_LT, "700")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, filters);

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testIndirectNestedTotal() throws Exception {
		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		IBaseExpression[] bindingExprRow = new IBaseExpression[5];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT)");
		bindingExprRow[4] = new ScriptExpression("Total.isTopN(row.ROW_AMOUNT,3)");
		// --- end binding

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0), new ScriptExpression("row.ROW_TOPN", 0) };

		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, expressions, null, null,
				null);
	}

	/**
	 * Test the nested totals.
	 *
	 * @throws Exception
	 * @throws IOException
	 *
	 */
	@Test
	public void testNestedTotal() throws IOException, Exception {
		String[] bindingNameRow = new String[16];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		bindingNameRow[5] = "ROW_TOTALALL1";
		bindingNameRow[6] = "ROW_TOTALALL2";
		bindingNameRow[7] = "ROW_TOTALALL3";
		bindingNameRow[8] = "ROW_TOTALALL4";
		bindingNameRow[9] = "ROW_TOTALALL5";
		bindingNameRow[10] = "ROW_TOTALALL6";
		bindingNameRow[11] = "ROW_TOTALALL7";
		bindingNameRow[12] = "ROW_TOTALALL8";
		bindingNameRow[13] = "ROW_TOTALALL9";
		bindingNameRow[14] = "ROW_TOTALALL10";
		bindingNameRow[15] = "ROW_TOTALALL11";

		IBaseExpression[] bindingExprRow = new IBaseExpression[16];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.isTopN(Total.sum(dataSetRow.AMOUNT,null,2),2)");
		bindingExprRow[5] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT)");
		bindingExprRow[6] = new ScriptExpression("Total.sum(Total.sum(dataSetRow.AMOUNT,null,1))");// 8900
		bindingExprRow[7] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1))");// 8900
		bindingExprRow[8] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+1,null,1))");// 8904
		bindingExprRow[9] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+1)");// 8902
		bindingExprRow[10] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+dataSetRow.AMOUNT)");// 44500
		bindingExprRow[11] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+dataSetRow.AMOUNT,null,1))");// 26700
		bindingExprRow[12] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+row.ROW_AMOUNT)");
		bindingExprRow[13] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+row.ROW_AMOUNT,null,1))");
		bindingExprRow[14] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");
		bindingExprRow[15] = new ScriptExpression("Total.sum(row.ROW_AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
				null);
	}

	/**
	 * Test the nested totals.Company with aggregateOn values.
	 *
	 * @throws Exception
	 * @throws IOException
	 *
	 */
	@Test
	public void testNestedTotal1() throws IOException, Exception {
		String[] bindingNameRow = new String[16];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		bindingNameRow[5] = "ROW_TOTALALL1";
		bindingNameRow[6] = "ROW_TOTALALL2";
		bindingNameRow[7] = "ROW_TOTALALL3";
		bindingNameRow[8] = "ROW_TOTALALL4";
		bindingNameRow[9] = "ROW_TOTALALL5";
		bindingNameRow[10] = "ROW_TOTALALL6";
		bindingNameRow[11] = "ROW_TOTALALL7";
		bindingNameRow[12] = "ROW_TOTALALL8";
		bindingNameRow[13] = "ROW_TOTALALL9";
		bindingNameRow[14] = "ROW_TOTALALL10";
		bindingNameRow[15] = "ROW_TOTALALL11";

		IBaseExpression[] bindingExprRow = new IBaseExpression[16];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.isTopN(Total.sum(dataSetRow.AMOUNT),2,null, 0)");
		bindingExprRow[4].setGroupName("group1");
		bindingExprRow[5] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT)");
		bindingExprRow[6] = new ScriptExpression("Total.sum(Total.sum(dataSetRow.AMOUNT),null,0)");// 8900
		bindingExprRow[6].setGroupName("group0");
		bindingExprRow[7] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT),null,1),null,0)");// 8900
		bindingExprRow[7].setGroupName("group1");
		bindingExprRow[8] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+1),null,0)");// 8904
		bindingExprRow[8].setGroupName("group0");
		bindingExprRow[9] = new ScriptExpression("Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2))+1,null,0)");// 8902
		bindingExprRow[9].setGroupName("group0");
		bindingExprRow[10] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2))+dataSetRow.AMOUNT,null,0)");// 44500
		bindingExprRow[10].setGroupName("group0");
		bindingExprRow[11] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+dataSetRow.AMOUNT,null,1))");// 26700
		bindingExprRow[12] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+row.ROW_AMOUNT)");
		bindingExprRow[13] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+row.ROW_AMOUNT,null,1))");
		bindingExprRow[14] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");
		bindingExprRow[15] = new ScriptExpression("Total.sum(row.ROW_AMOUNT/Total.sum(dataSetRow.AMOUNT),null,0)");
		bindingExprRow[15].setGroupName("group0");

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");
		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
				null);
	}

	/**
	 * Test the nested totals.Company with aggregateOn values.
	 *
	 * @throws Exception
	 * @throws IOException
	 *
	 */
	@Test
	public void testNestedTotal2() throws IOException, Exception {
		String[] bindingNameRow = new String[16];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		bindingNameRow[5] = "ROW_TOTALALL1";
		bindingNameRow[6] = "ROW_TOTALALL2";
		bindingNameRow[7] = "ROW_TOTALALL3";
		bindingNameRow[8] = "ROW_TOTALALL4";
		bindingNameRow[9] = "ROW_TOTALALL5";
		bindingNameRow[10] = "ROW_TOTALALL6";
		bindingNameRow[11] = "ROW_TOTALALL7";
		bindingNameRow[12] = "ROW_TOTALALL8";
		bindingNameRow[13] = "ROW_TOTALALL9";
		bindingNameRow[14] = "ROW_TOTALALL10";
		bindingNameRow[15] = "ROW_TOTALALL11";

		IBaseExpression[] bindingExprRow = new IBaseExpression[16];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.isTopN(Total.sum(dataSetRow.AMOUNT), 1, null, 0)");
		bindingExprRow[4].setGroupName("group1");
		bindingExprRow[5] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT, dataSetRow.AMOUNT>100, 0)");
		bindingExprRow[6] = new ScriptExpression(
				"Total.sum(Total.sum(dataSetRow.AMOUNT),Total.sum(dataSetRow.AMOUNT)>2000,0)");// 8900
		bindingExprRow[6].setGroupName("group0");
		bindingExprRow[7] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT, dataSetRow.AMOUNT>400),null,1),null,0)");// 8900
		bindingExprRow[7].setGroupName("group1");
		bindingExprRow[8] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,dataSetRow.AMOUNT>100,2)),null,0)");// 8904
		bindingExprRow[8].setGroupName("group0");
		///////////////// PROBLEM//////////////////
		bindingExprRow[9] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),Total.sum(dataSetRow.AMOUNT,null,2)>1,1),null,0)");// 8902
		///////////////////////////////////////////
		bindingExprRow[9].setGroupName("group0");
		bindingExprRow[10] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2))+dataSetRow.AMOUNT,null,0)");// 44500
		bindingExprRow[10].setGroupName("group0");
		bindingExprRow[11] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1),Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)>7000,0)");// 26700
		bindingExprRow[12] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+row.ROW_AMOUNT)");
		bindingExprRow[13] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+row.ROW_AMOUNT,null,1))");
		bindingExprRow[14] = new ScriptExpression("Total.sum(dataSetRow.AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");
		bindingExprRow[15] = new ScriptExpression("Total.sum(row.ROW_AMOUNT/Total.sum(dataSetRow.AMOUNT),null,0)");
		bindingExprRow[15].setGroupName("group0");

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");
		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
				null);
	}

	/**
	 * Test the nested totals.Company with aggregateOn values.
	 *
	 * @throws Exception
	 * @throws IOException
	 *
	 */
	@Test
	public void testNestedTotal3() throws IOException, Exception {
		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";

		IBaseExpression[] bindingExprRow = new IBaseExpression[5];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		///////////////// PROBLEM//////////////////
		bindingExprRow[4] = new ScriptExpression(
				"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1),Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)>7000,0)");// 8902
		///////////////////////////////////////////
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");
		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
				null);
	}

	/**
	 * Test the nested totals.Company with aggregateOn values.
	 *
	 * @throws Exception
	 * @throws IOException
	 *
	 */
	@Test
	public void testNestedTotal4() throws IOException, Exception {
		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_5";

		IBaseExpression[] bindingExprRow = new IBaseExpression[5];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[4] = new ScriptExpression("Total.sum(Total.sum(dataSetRow.AMOUNT,null,0),null,1)");
		///////////////////////////////////////////
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");
		createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
				null);

		///////////////// PROBLEM//////////////////
		try {
			bindingNameRow = new String[3];
			bindingNameRow[0] = "ROW_COUNTRY";
			bindingNameRow[1] = "ROW_CITY";
			bindingNameRow[2] = "ROW_TOPN";

			bindingExprRow = new IBaseExpression[3];
			bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
			bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
			bindingExprRow[2] = new ScriptExpression(
					"Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1),dataSetRow.AMOUNT>10,0)");
			createAndRunQuery(null, null, null, null, null, null, bindingNameRow, bindingExprRow, null, groupDefn, null,
					null);
			fail("expected error here");
		} catch (DataException e) {
			assertTrue(e.getErrorCode() == ResourceConstants.INVALID_JS_EXPR);
		}

	}

	/**
	 * Test filterings including group row filters, group instance filters and
	 * multi-pass row filters
	 *
	 * @throws Exception
	 */
	@Test
	public void testMixedMultipassFilting() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_GROUP0";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_1";
		bindingNameFilter[1] = "FILTER_2";
		bindingNameFilter[2] = "FILTER_3";

		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprFilter[2] = new ScriptExpression("Total.isTopN(dataSetRow.AMOUNT,1,null,1)");

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
		// --- end binding
		GroupDefinition[] groupDefn = { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "75")));

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.AMOUNT,null,1)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_1", IConditionalExpression.OP_BOTTOM_N, "7")),
				new FilterDefinition(new ConditionalExpression("row.FILTER_2", IConditionalExpression.OP_LT, "700")),
				new FilterDefinition(new ConditionalExpression("Total.isTopN(dataSetRow.AMOUNT,1,null,1)",
						IConditionalExpression.OP_TRUE)) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, filters);

	}

	/**
	 * Test filterings including group instance filters
	 *
	 * @throws Exception
	 */
	@Test
	public void testTop_Bottom_FilteringInGroupInstance_1() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1")

		};
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = { new FilterDefinition(
				new ConditionalExpression("Total.isTopN(row.ROW_AMOUNT,3,null,1)", IConditionalExpression.OP_TRUE)) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, filters);

	}

	/**
	 * Test filterings including multi group filters
	 *
	 * @throws Exception
	 */
	@Test
	public void testTop_Bottom_FilteringInGroupInstance_2() throws Exception {
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_GROUP0";
		bindingNameGroup[1] = "GROUP_GROUP1";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};
		String[] bindingNameFilter = null;

		IBaseExpression[] bindingExprFilter = null;

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
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1")

		};
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");
		groupDefn[1].setKeyExpression("row.GROUP_GROUP1");
		IBaseExpression[] expressions = { new ScriptExpression("row.COUNTRY", 0), new ScriptExpression("row.CITY", 0),
				new ScriptExpression("row.SALE_DATE", 0), new ScriptExpression("row.AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(new ConditionalExpression("Total.isTopN(dataSetRow.AMOUNT,3,null,1)",
						IConditionalExpression.OP_TRUE)),
				new FilterDefinition(new ConditionalExpression("Total.isBottomN(dataSetRow.AMOUNT,2,null,2)",
						IConditionalExpression.OP_TRUE)),
				new FilterDefinition(
						new ConditionalExpression("dataSetRow.CITY", IConditionalExpression.OP_NE, "\"Chicago\"")) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, filters);
	}

	/**
	 * Test multipass with column alias.
	 */
	@Test
	public void testMultipassWithAlias() throws Exception {
		ColumnDefinition cd = new ColumnDefinition("AMOUNT");
		cd.setAlias("A");
		this.dataSet.addResultSetHint(cd);
		// --- begin binding
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_GROUP0";

		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");

		String[] bindingNameSort = {};

		IBaseExpression[] bindingExprSort = {};

		String[] bindingNameFilter = new String[3];
		bindingNameFilter[0] = "FILTER_1";
		bindingNameFilter[1] = "FILTER_2";
		bindingNameFilter[2] = "FILTER_3";

		IBaseExpression[] bindingExprFilter = new IBaseExpression[3];
		bindingExprFilter[0] = new ScriptExpression("dataSetRow.A");
		bindingExprFilter[1] = new ScriptExpression("dataSetRow.A");
		bindingExprFilter[2] = new ScriptExpression("Total.isTopN(dataSetRow.A,1,null,1)");

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.CITY");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.A");
		// --- end binding
		GroupDefinition[] groupDefn = { new GroupDefinition("group0") };
		groupDefn[0].setKeyExpression("row.GROUP_GROUP0");

		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.A,null,1)",
				IConditionalExpression.OP_BOTTOM_PERCENT, "75")));

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("Total.sum(dataSetRow.A,null,1)");
		sortDefn.setSortDirection(0);
		groupDefn[0].addSort(sortDefn);

		IBaseExpression[] expressions = { new ScriptExpression("row.ROW_COUNTRY", 0),
				new ScriptExpression("row.ROW_CITY", 0), new ScriptExpression("row.ROW_SALE_DATE", 0),
				new ScriptExpression("row.ROW_AMOUNT", 0) };

		FilterDefinition[] filters = {
				new FilterDefinition(
						new ConditionalExpression("row.FILTER_1", IConditionalExpression.OP_BOTTOM_N, "7")),
				new FilterDefinition(new ConditionalExpression("row.FILTER_2", IConditionalExpression.OP_LT, "700")),
				new FilterDefinition(new ConditionalExpression("Total.isTopN(dataSetRow.A,1,null,1)",
						IConditionalExpression.OP_TRUE)) };

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, filters);
	}

	/**
	 * Create query definition from passed parameters
	 *
	 * @param expressions row expression
	 * @param groupDefn
	 * @param sortDefn
	 * @param filters
	 * @return QueryDefinition
	 */
	private QueryDefinition createQueryDefn(String[] exprNames, IBaseExpression[] expressions,
			GroupDefinition[] groupDefn, SortDefinition[] sortDefn, FilterDefinition[] filters) {
		// define a query design
		QueryDefinition queryDefn = newReportQuery();

		if (groupDefn != null) {
			for (int i = 0; i < groupDefn.length; i++) {
				queryDefn.addGroup(groupDefn[i]);
			}
		}
		if (sortDefn != null) {
			for (int i = 0; i < sortDefn.length; i++) {
				queryDefn.addSort(sortDefn[i]);
			}
		}
		if (expressions != null) {
			for (int i = 0; i < expressions.length; i++) {
				queryDefn.addResultSetExpression(exprNames[i], expressions[i]);
			}
		}
		if (filters != null) {
			for (int i = 0; i < filters.length; i++) {
				queryDefn.addFilter(filters[i]);
			}
		}

		return queryDefn;
	}
}
