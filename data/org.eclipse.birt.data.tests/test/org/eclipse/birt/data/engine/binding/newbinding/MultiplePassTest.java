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
package org.eclipse.birt.data.engine.binding.newbinding;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.binding.APITestCase;
import org.eclipse.birt.data.engine.core.DataException;
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
		} catch (DataException e) {

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
	 * Test feature of aggr filtering.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAggrFilter1() throws IOException, Exception {
		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		qd.addBinding(new Binding(bindingNameRow[4], new ScriptExpression("row.b4")));

		Binding b4 = new Binding("b4", new ScriptExpression("dataSetRow.AMOUNT"));
		b4.setAggrFunction("ISTOPN");
		b4.addArgument(new ScriptExpression("2"));
		b4.addAggregateOn("group0");
		qd.addBinding(b4);

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("row.b4", IConditionalExpression.OP_TRUE));

		qd.addFilter(filterDefn);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
	}

	/**
	 * Test feature of aggr filtering.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAggrFilter2() throws IOException, Exception {
		String[] bindingNameRow = new String[6];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		bindingNameRow[5] = "ROW_BOTTOMN";

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		qd.addBinding(new Binding(bindingNameRow[4], new ScriptExpression("row.b4")));
		qd.addBinding(new Binding(bindingNameRow[5], new ScriptExpression("row.b5")));

		Binding b4 = new Binding("b4", new ScriptExpression("dataSetRow.AMOUNT"));
		b4.setAggrFunction("ISTOPN");
		b4.addArgument(new ScriptExpression("3"));
		b4.addAggregateOn("group0");
		qd.addBinding(b4);

		Binding b5 = new Binding("b5", new ScriptExpression("dataSetRow.AMOUNT"));
		b5.setAggrFunction("ISBOTTOMN");
		b5.addArgument(new ScriptExpression("3"));
		b5.addAggregateOn("group0");
		qd.addBinding(b5);

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		FilterDefinition filterDefn1 = new FilterDefinition(
				new ConditionalExpression("row.b4", IConditionalExpression.OP_TRUE));

		FilterDefinition filterDefn2 = new FilterDefinition(
				new ConditionalExpression("row.b5", IConditionalExpression.OP_TRUE));

		qd.addFilter(filterDefn1);
		qd.addFilter(filterDefn2);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
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
	 * This test is to test simple filter and multipass filter. For bug 54826, we
	 * change the logic of filters in group, now we first execute the simple filter
	 * then execute the multipass filter(topN, bottom N)
	 */
	@Test
	public void testGroupFilteringSorting5() throws Exception {
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
				IConditionalExpression.OP_LT, "5000")));
		groupDefn[0].addFilter(new FilterDefinition(new ConditionalExpression("Total.sum(dataSetRow.AMOUNT,null,1)",
				IConditionalExpression.OP_TOP_N, "1")));

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

	@Test
	public void testTotalCount() throws Exception {
		String[] bindingNameRow = new String[2];
		bindingNameRow[0] = "amount";
		bindingNameRow[1] = "amountTOTAL";

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.AMOUNT")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("Total.count(row[\"amount\"]<=500)")));

		IResultIterator itr = this.executeQuery(qd);
		int count = 0;
		while (itr.next()) {
			assertTrue(itr.getInteger(bindingNameRow[1]) == 6);
			count++;
		}
		assertTrue(count > 0);
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

	@Test
	public void testSimpleNestedTotal() throws Exception {
		String[] bindingNameRow = new String[5];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_AMOUNT_TOTAL";

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		qd.addBinding(new Binding(bindingNameRow[4], new ScriptExpression("row.b4")));

		Binding b4 = new Binding("b4", new ScriptExpression("row.b41"));
		b4.setAggrFunction("SUM");
		qd.addBinding(b4);

		Binding b41 = new Binding("b41", new ScriptExpression("dataSetRow.AMOUNT"));
		b41.setAggrFunction("SUM");
		b41.addAggregateOn("group0");
		qd.addBinding(b41);

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();

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

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		qd.addBinding(new Binding(bindingNameRow[4], new ScriptExpression("row.b4")));

		Binding b4 = new Binding("b4", new ScriptExpression("row.b41"));
		b4.setAggrFunction("ISTOPN");
		b4.addArgument(new ScriptExpression("2"));
		qd.addBinding(b4);

		Binding b41 = new Binding("b41", new ScriptExpression("dataSetRow.AMOUNT"));
		b41.setAggrFunction("SUM");
		b41.addAggregateOn("group1");
		qd.addBinding(b41);

		// bindingExprRow[4] = new ScriptExpression(
		// "Total.isTopN(Total.sum(dataSetRow.AMOUNT,null,2),2)" );
		Binding b5 = new Binding(bindingNameRow[5], new ScriptExpression("dataSetRow.AMOUNT"));
		b5.setAggrFunction("SUM");
		qd.addBinding(b5);

		// bindingExprRow[5] = new ScriptExpression( "Total.sum(dataSetRow.AMOUNT)");
		qd.addBinding(new Binding(bindingNameRow[6], new ScriptExpression("row.b6")));

		Binding b6 = new Binding("b6", new ScriptExpression("row.b61"));
		b6.setAggrFunction("SUM");
		qd.addBinding(b6);

		Binding b61 = new Binding("b61", new ScriptExpression("dataSetRow.AMOUNT"));
		b61.setAggrFunction("SUM");
		b61.addAggregateOn("group0");
		qd.addBinding(b61);

		// bindingExprRow[6] = new ScriptExpression(
		// "Total.sum(Total.sum(dataSetRow.AMOUNT,null,1))");//8900

		qd.addBinding(new Binding(bindingNameRow[7], new ScriptExpression("row.b7")));

		Binding b7 = new Binding("b7", new ScriptExpression("row.b71"));
		b7.setAggrFunction("SUM");
		qd.addBinding(b7);

		Binding b71 = new Binding("b71", new ScriptExpression("row.b72"));
		b71.setAggrFunction("SUM");
		b71.addAggregateOn("group0");
		qd.addBinding(b71);

		Binding b72 = new Binding("b72", new ScriptExpression("dataSetRow.AMOUNT"));
		b72.setAggrFunction("SUM");
		b72.addAggregateOn("group1");
		qd.addBinding(b72);

		// bindingExprRow[7] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1))");//8900
		qd.addBinding(new Binding(bindingNameRow[8], new ScriptExpression("row.b8")));

		Binding b8 = new Binding("b8", new ScriptExpression("row.b81"));
		b8.setAggrFunction("SUM");
		qd.addBinding(b8);

		Binding b81 = new Binding("b81", new ScriptExpression("row.b82 + 1"));
		b81.setAggrFunction("SUM");
		b81.addAggregateOn("group0");
		qd.addBinding(b81);

		Binding b82 = new Binding("b82", new ScriptExpression("dataSetRow.AMOUNT"));
		b82.setAggrFunction("SUM");
		b82.addAggregateOn("group1");
		qd.addBinding(b82);

		// bindingExprRow[8] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+1,null,1))");//8904
		qd.addBinding(new Binding(bindingNameRow[9], new ScriptExpression("row.b9")));

		Binding b9 = new Binding("b9", new ScriptExpression("row.b91+1"));
		b9.setAggrFunction("SUM");
		qd.addBinding(b9);

		Binding b91 = new Binding("b91", new ScriptExpression("row.b92"));
		b91.setAggrFunction("SUM");
		b91.addAggregateOn("group0");
		qd.addBinding(b91);

		Binding b92 = new Binding("b92", new ScriptExpression("dataSetRow.AMOUNT"));
		b92.setAggrFunction("SUM");
		b92.addAggregateOn("group1");
		qd.addBinding(b92);

		// bindingExprRow[9] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+1)");//8902
		qd.addBinding(new Binding(bindingNameRow[10], new ScriptExpression("row.b10")));

		Binding b10 = new Binding("b10", new ScriptExpression("row.b101 + dataSetRow.AMOUNT"));
		b10.setAggrFunction("SUM");
		qd.addBinding(b10);

		Binding b101 = new Binding("b101", new ScriptExpression("row.b102"));
		b101.setAggrFunction("SUM");
		b101.addAggregateOn("group0");
		qd.addBinding(b101);

		Binding b102 = new Binding("b102", new ScriptExpression("dataSetRow.AMOUNT"));
		b102.setAggrFunction("SUM");
		b102.addAggregateOn("group1");
		qd.addBinding(b102);

		// bindingExprRow[10] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+dataSetRow.AMOUNT)");//44500
		qd.addBinding(new Binding(bindingNameRow[11], new ScriptExpression("row.b11")));

		Binding b11 = new Binding("b11", new ScriptExpression("row.b111"));
		b11.setAggrFunction("SUM");
		qd.addBinding(b11);

		Binding b111 = new Binding("b111", new ScriptExpression("row.b112 + dataSetRow.AMOUNT"));
		b111.setAggrFunction("SUM");
		b111.addAggregateOn("group0");
		qd.addBinding(b111);

		Binding b112 = new Binding("b112", new ScriptExpression("dataSetRow.AMOUNT"));
		b112.setAggrFunction("SUM");
		b112.addAggregateOn("group1");
		qd.addBinding(b112);

		// bindingExprRow[11] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+dataSetRow.AMOUNT,null,1))");//26700
		qd.addBinding(new Binding(bindingNameRow[12], new ScriptExpression("row.b12")));

		Binding b12 = new Binding("b12", new ScriptExpression("row.b121 + row.ROW_AMOUNT"));
		b12.setAggrFunction("SUM");
		qd.addBinding(b12);

		Binding b121 = new Binding("b121", new ScriptExpression("row.b122"));
		b121.setAggrFunction("SUM");
		b121.addAggregateOn("group0");
		qd.addBinding(b121);

		Binding b122 = new Binding("b122", new ScriptExpression("dataSetRow.AMOUNT"));
		b122.setAggrFunction("SUM");
		b122.addAggregateOn("group1");
		qd.addBinding(b122);

		// bindingExprRow[12] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2),null,1)+row.ROW_AMOUNT)");

		qd.addBinding(new Binding(bindingNameRow[13], new ScriptExpression("row.b13")));

		Binding b13 = new Binding("b13", new ScriptExpression("row.b131"));
		b13.setAggrFunction("SUM");
		qd.addBinding(b13);

		Binding b131 = new Binding("b131", new ScriptExpression("row.b132 + row.ROW_AMOUNT"));
		b131.setAggrFunction("SUM");
		b131.addAggregateOn("group0");
		qd.addBinding(b131);

		Binding b132 = new Binding("b132", new ScriptExpression("dataSetRow.AMOUNT"));
		b132.setAggrFunction("SUM");
		b132.addAggregateOn("group1");
		qd.addBinding(b132);

		// bindingExprRow[13] = new ScriptExpression(
		// "Total.sum(Total.sum(Total.sum(dataSetRow.AMOUNT,null,2)+row.ROW_AMOUNT,null,1))");
		qd.addBinding(new Binding(bindingNameRow[14], new ScriptExpression("row.b14")));

		Binding b14 = new Binding("b14", new ScriptExpression("dataSetRow.AMOUNT/row.b141"));
		b14.setAggrFunction("SUM");
		qd.addBinding(b14);

		Binding b141 = new Binding("b141", new ScriptExpression("row.ROW_AMOUNT"));
		b141.setAggrFunction("SUM");
		b141.addAggregateOn("group0");
		qd.addBinding(b141);

		// bindingExprRow[14] = new ScriptExpression(
		// "Total.sum(dataSetRow.AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");
		qd.addBinding(new Binding(bindingNameRow[15], new ScriptExpression("row.b15")));

		Binding b15 = new Binding("b15", new ScriptExpression("row.ROW_AMOUNT/row.b151"));
		b15.setAggrFunction("SUM");
		qd.addBinding(b15);

		Binding b151 = new Binding("b151", new ScriptExpression("dataSetRow.AMOUNT"));
		b151.setAggrFunction("SUM");
		b151.addAggregateOn("group0");
		qd.addBinding(b151);
		// bindingExprRow[15] = new ScriptExpression(
		// "Total.sum(row.ROW_AMOUNT/Total.sum(dataSetRow.AMOUNT,null,1))");

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
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

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		Binding b4 = new Binding(bindingNameRow[4], new ScriptExpression("row.b41"));
		b4.setAggrFunction("ISTOPN");
		b4.addArgument(new ScriptExpression("2"));
		qd.addBinding(b4);

		Binding b41 = new Binding("b41", new ScriptExpression("dataSetRow.AMOUNT"));
		b41.setAggrFunction("SUM");
		b41.addAggregateOn("group1");
		qd.addBinding(b41);

		Binding b5 = new Binding(bindingNameRow[5], new ScriptExpression("dataSetRow.AMOUNT"));
		b5.setAggrFunction("SUM");
		qd.addBinding(b5);

		Binding b6 = new Binding(bindingNameRow[6], new ScriptExpression("row.b61"));
		b6.setAggrFunction("SUM");
		qd.addBinding(b6);

		Binding b61 = new Binding("b61", new ScriptExpression("dataSetRow.AMOUNT"));
		b61.setAggrFunction("SUM");
		b61.addAggregateOn("group0");
		qd.addBinding(b61);

		Binding b7 = new Binding(bindingNameRow[7], new ScriptExpression("row.b71"));
		b7.setAggrFunction("SUM");
		qd.addBinding(b7);

		Binding b71 = new Binding("b71", new ScriptExpression("row.b72"));
		b71.setAggrFunction("SUM");
		b71.addAggregateOn("group0");
		qd.addBinding(b71);

		Binding b72 = new Binding("b72", new ScriptExpression("dataSetRow.AMOUNT"));
		b72.setAggrFunction("SUM");
		b72.addAggregateOn("group1");
		qd.addBinding(b72);

		Binding b8 = new Binding(bindingNameRow[8], new ScriptExpression("row.b81"));
		b8.setAggrFunction("SUM");
		qd.addBinding(b8);

		Binding b81 = new Binding("b81", new ScriptExpression("row.b82 + 1"));
		b81.setAggrFunction("SUM");
		b81.addAggregateOn("group0");
		qd.addBinding(b81);

		Binding b82 = new Binding("b82", new ScriptExpression("dataSetRow.AMOUNT"));
		b82.setAggrFunction("SUM");
		b82.addAggregateOn("group1");
		qd.addBinding(b82);

		Binding b9 = new Binding(bindingNameRow[9], new ScriptExpression("row.b91 + 1"));
		b9.setAggrFunction("SUM");
		qd.addBinding(b9);

		Binding b91 = new Binding("b91", new ScriptExpression("row.b92"));
		b91.setAggrFunction("SUM");
		b91.addAggregateOn("group0");
		qd.addBinding(b91);

		Binding b92 = new Binding("b92", new ScriptExpression("dataSetRow.AMOUNT"));
		b92.setAggrFunction("SUM");
		b92.addAggregateOn("group1");
		qd.addBinding(b92);

		Binding b10 = new Binding(bindingNameRow[10], new ScriptExpression("row.b101 + dataSetRow.AMOUNT"));
		b10.setAggrFunction("SUM");
		qd.addBinding(b10);

		Binding b101 = new Binding("b101", new ScriptExpression("row.b102"));
		b101.setAggrFunction("SUM");
		b101.addAggregateOn("group0");
		qd.addBinding(b101);

		Binding b102 = new Binding("b102", new ScriptExpression("dataSetRow.AMOUNT"));
		b102.setAggrFunction("SUM");
		b102.addAggregateOn("group1");
		qd.addBinding(b102);

		Binding b11 = new Binding(bindingNameRow[11], new ScriptExpression("row.b111"));
		b11.setAggrFunction("SUM");
		qd.addBinding(b11);

		Binding b111 = new Binding("b111", new ScriptExpression("row.b112 + dataSetRow.AMOUNT"));
		b111.setAggrFunction("SUM");
		b111.addAggregateOn("group0");
		qd.addBinding(b111);

		Binding b112 = new Binding("b112", new ScriptExpression("dataSetRow.AMOUNT"));
		b112.setAggrFunction("SUM");
		b112.addAggregateOn("group1");
		qd.addBinding(b112);

		Binding b12 = new Binding(bindingNameRow[12], new ScriptExpression("row.b121 + row.ROW_AMOUNT"));
		b12.setAggrFunction("SUM");
		qd.addBinding(b12);

		Binding b121 = new Binding("b121", new ScriptExpression("row.b122"));
		b121.setAggrFunction("SUM");
		b121.addAggregateOn("group0");
		qd.addBinding(b121);

		Binding b122 = new Binding("b122", new ScriptExpression("dataSetRow.AMOUNT"));
		b122.setAggrFunction("SUM");
		b122.addAggregateOn("group1");
		qd.addBinding(b122);

		Binding b13 = new Binding(bindingNameRow[13], new ScriptExpression("row.b131"));
		b13.setAggrFunction("SUM");
		qd.addBinding(b13);

		Binding b131 = new Binding("b131", new ScriptExpression("row.b132+row.ROW_AMOUNT"));
		b131.setAggrFunction("SUM");
		b131.addAggregateOn("group0");
		qd.addBinding(b131);

		Binding b132 = new Binding("b132", new ScriptExpression("dataSetRow.AMOUNT"));
		b132.setAggrFunction("SUM");
		b132.addAggregateOn("group1");
		qd.addBinding(b132);

		Binding b14 = new Binding(bindingNameRow[14], new ScriptExpression("dataSetRow.AMOUNT/row.b141"));
		b14.setAggrFunction("SUM");
		qd.addBinding(b14);

		Binding b141 = new Binding("b141", new ScriptExpression("dataSetRow.AMOUNT"));
		b141.addAggregateOn("group0");
		b141.setAggrFunction("SUM");
		qd.addBinding(b141);

		Binding b15 = new Binding(bindingNameRow[15], new ScriptExpression("dataSetRow.AMOUNT/row.b151"));
		b15.setAggrFunction("SUM");
		qd.addBinding(b15);

		Binding b151 = new Binding("b151", new ScriptExpression("dataSetRow.AMOUNT"));
		b151.addAggregateOn("group0");
		b151.setAggrFunction("SUM");
		qd.addBinding(b151);

		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
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
		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));

		Binding b4 = new Binding(bindingNameRow[4], new ScriptExpression("row.b41"));
		b4.setAggrFunction("ISTOPN");
		b4.addArgument(new ScriptExpression("1"));
		qd.addBinding(b4);

		Binding b41 = new Binding("b41", new ScriptExpression("dataSetRow.AMOUNT"));
		b41.setAggrFunction("SUM");
		b41.addAggregateOn("group1");
		qd.addBinding(b41);

		Binding b5 = new Binding(bindingNameRow[5], new ScriptExpression("dataSetRow.AMOUNT"));
		b5.setAggrFunction("SUM");
		b5.setFilter(new ScriptExpression("dataSetRow.AMOUNT>100"));
		qd.addBinding(b5);

		Binding b6 = new Binding(bindingNameRow[6], new ScriptExpression("row.b61"));
		b6.setAggrFunction("SUM");
		b6.setFilter(new ScriptExpression("row.b61 > 2000"));
		qd.addBinding(b6);

		Binding b61 = new Binding("b61", new ScriptExpression("dataSetRow.AMOUNT"));
		b61.setAggrFunction("SUM");
		b61.addAggregateOn("group0");
		qd.addBinding(b61);

		Binding b7 = new Binding(bindingNameRow[7], new ScriptExpression("row.b71"));
		b7.setAggrFunction("SUM");
		qd.addBinding(b7);

		Binding b71 = new Binding("b71", new ScriptExpression("row.b72"));
		b71.setAggrFunction("SUM");
		b71.addAggregateOn("group0");
		qd.addBinding(b71);

		Binding b72 = new Binding("b72", new ScriptExpression("dataSetRow.AMOUNT"));
		b72.setAggrFunction("SUM");
		b72.setFilter(new ScriptExpression("dataSetRow.AMOUNT >400"));
		b72.addAggregateOn("group1");
		qd.addBinding(b72);

		Binding b8 = new Binding(bindingNameRow[8], new ScriptExpression("row.b81"));
		b8.setAggrFunction("SUM");
		qd.addBinding(b8);

		Binding b81 = new Binding("b81", new ScriptExpression("row.b82"));
		b81.setAggrFunction("SUM");
		b81.addAggregateOn("group0");
		qd.addBinding(b81);

		Binding b82 = new Binding("b82", new ScriptExpression("dataSetRow.AMOUNT"));
		b82.setAggrFunction("SUM");
		b82.setFilter(new ScriptExpression("dataSetRow.AMOUNT > 100 "));
		b82.addAggregateOn("group1");
		qd.addBinding(b82);

		///////////////// PROBLEM//////////////////
		Binding b9 = new Binding(bindingNameRow[9], new ScriptExpression("row.b91"));
		b9.setAggrFunction("SUM");
		qd.addBinding(b9);

		Binding b91 = new Binding("b91", new ScriptExpression("row.b92"));
		b91.setAggrFunction("SUM");
		b91.setFilter(new ScriptExpression("row.b92>1"));
		b91.addAggregateOn("group0");
		qd.addBinding(b91);

		Binding b92 = new Binding("b92", new ScriptExpression("dataSetRow.AMOUNT"));
		b92.setAggrFunction("SUM");
		b92.addAggregateOn("group1");
		qd.addBinding(b92);

		Binding b10 = new Binding(bindingNameRow[10], new ScriptExpression("row.b101 + dataSetRow.AMOUNT"));
		b10.setAggrFunction("SUM");
		qd.addBinding(b10);

		Binding b101 = new Binding("b101", new ScriptExpression("row.b102"));
		b101.setAggrFunction("SUM");
		b101.addAggregateOn("group0");
		qd.addBinding(b101);

		Binding b102 = new Binding("b102", new ScriptExpression("dataSetRow.AMOUNT"));
		b102.setAggrFunction("SUM");
		b102.addAggregateOn("group1");
		qd.addBinding(b102);

		Binding b11 = new Binding(bindingNameRow[11], new ScriptExpression("row.b111"));
		b11.setAggrFunction("SUM");
		b11.setFilter(new ScriptExpression("row.b111 > 7000"));
		qd.addBinding(b11);

		Binding b111 = new Binding("b111", new ScriptExpression("row.b112"));
		b111.setAggrFunction("SUM");
		b111.addAggregateOn("group0");
		qd.addBinding(b111);

		Binding b112 = new Binding("b112", new ScriptExpression("dataSetRow.AMOUNT"));
		b112.setAggrFunction("SUM");
		b112.addAggregateOn("group1");
		qd.addBinding(b112);

		Binding b12 = new Binding(bindingNameRow[12], new ScriptExpression("row.b121 + row.ROW_AMOUNT"));
		b12.setAggrFunction("SUM");
		qd.addBinding(b12);

		Binding b121 = new Binding("b121", new ScriptExpression("row.b122"));
		b121.setAggrFunction("SUM");
		b121.addAggregateOn("group0");
		qd.addBinding(b121);

		Binding b122 = new Binding("b122", new ScriptExpression("dataSetRow.AMOUNT"));
		b122.setAggrFunction("SUM");
		b122.addAggregateOn("group1");
		qd.addBinding(b122);

		Binding b13 = new Binding(bindingNameRow[13], new ScriptExpression("row.b131"));
		b13.setAggrFunction("SUM");
		qd.addBinding(b13);

		Binding b131 = new Binding("b131", new ScriptExpression("row.b132+row.ROW_AMOUNT"));
		b131.setAggrFunction("SUM");
		b131.addAggregateOn("group0");
		qd.addBinding(b131);

		Binding b132 = new Binding("b132", new ScriptExpression("dataSetRow.AMOUNT"));
		b132.setAggrFunction("SUM");
		b132.addAggregateOn("group1");
		qd.addBinding(b132);

		Binding b14 = new Binding(bindingNameRow[14], new ScriptExpression("dataSetRow.AMOUNT/row.b141"));
		b14.setAggrFunction("SUM");
		qd.addBinding(b14);

		Binding b141 = new Binding("b141", new ScriptExpression("dataSetRow.AMOUNT"));
		b141.addAggregateOn("group0");
		b141.setAggrFunction("SUM");
		qd.addBinding(b141);

		Binding b15 = new Binding(bindingNameRow[15], new ScriptExpression("dataSetRow.AMOUNT/row.b151"));
		b15.setAggrFunction("SUM");
		qd.addBinding(b15);

		Binding b151 = new Binding("b151", new ScriptExpression("dataSetRow.AMOUNT"));
		b151.addAggregateOn("group0");
		b151.setAggrFunction("SUM");
		qd.addBinding(b151);

		// --- end binding
		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
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

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));
		///////////////// PROBLEM//////////////////

		Binding b4 = new Binding(bindingNameRow[4], new ScriptExpression("row.b41"));
		b4.setAggrFunction("SUM");
		b4.setFilter(new ScriptExpression("row.b41>7000"));

		Binding b41 = new Binding("b41", new ScriptExpression("row.b42"));
		b41.addAggregateOn("group0");
		b41.setAggrFunction("SUM");

		Binding b42 = new Binding("b42", new ScriptExpression("dataSetRow.AMOUNT"));
		b42.addAggregateOn("group1");
		b42.setAggrFunction("SUM");

		qd.addBinding(b4);
		qd.addBinding(b41);
		qd.addBinding(b42);
		///////////////////////////////////////////
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		this.executeQuery(qd, bindingNameRow);
		this.checkOutputFile();
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
		String[] bindingNameRow = new String[6];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUNT";
		bindingNameRow[4] = "ROW_TOPN";
		bindingNameRow[5] = "ROW_5";

		QueryDefinition qd = newReportQuery();

		qd.addBinding(new Binding(bindingNameRow[0], new ScriptExpression("dataSetRow.COUNTRY")));
		qd.addBinding(new Binding(bindingNameRow[1], new ScriptExpression("dataSetRow.CITY")));
		qd.addBinding(new Binding(bindingNameRow[2], new ScriptExpression("dataSetRow.SALE_DATE")));
		qd.addBinding(new Binding(bindingNameRow[3], new ScriptExpression("dataSetRow.AMOUNT")));
		///////////////// PROBLEM//////////////////

		Binding b4 = new Binding(bindingNameRow[4], new ScriptExpression("row.b41"));
		b4.setAggrFunction("SUM");
		b4.setFilter(new ScriptExpression("dataSetRow.AMOUNT>10"));

		Binding b41 = new Binding("b41", new ScriptExpression("row.b42"));
		b41.addAggregateOn("group0");
		b41.setAggrFunction("SUM");

		Binding b42 = new Binding("b42", new ScriptExpression("dataSetRow.AMOUNT"));
		b42.addAggregateOn("group1");
		b42.setAggrFunction("SUM");

		qd.addBinding(b4);
		qd.addBinding(b41);
		qd.addBinding(b42);

		/*
		 * Binding b5 = new Binding( bindingNameRow[5], new
		 * ScriptExpression("row.b51")); b5.setAggrFunction( "SUM" ); b5.addAggregateOn(
		 * "group0" ); Binding b51 = new Binding( "b51", new
		 * ScriptExpression("dataSetRow.AMOUNT")); b51.setAggrFunction( "SUM" );
		 *
		 * qd.addBinding( b5 ); qd.addBinding( b51 );
		 */

		///////////////////////////////////////////
		// --- end binding

		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.ROW_COUNTRY");
		groupDefn[1].setKeyExpression("row.ROW_CITY");

		qd.addGroup(groupDefn[0]);
		qd.addGroup(groupDefn[1]);

		try {
			this.executeQuery(qd, bindingNameRow);
			this.checkOutputFile();
			fail("should not arrive here");
		} catch (Exception e) {

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
	 * Test filterings including group instance filters
	 *
	 * @throws Exception
	 */
	@Test
	public void testIN_FilteringInGroup() throws Exception {
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

		List combinedValue = new ArrayList();
		combinedValue.add("7600");
		combinedValue.add("1000");
		IConditionalExpression expr = new ConditionalExpression("Total.Sum(row.ROW_AMOUNT,null,1)",
				IConditionalExpression.OP_IN, combinedValue);
		FilterDefinition filters = new FilterDefinition(expr);
		groupDefn[0].addFilter(filters);

		createAndRunQuery(bindingNameGroup, bindingExprGroup, bindingNameSort, bindingExprSort, bindingNameFilter,
				bindingExprFilter, bindingNameRow, bindingExprRow, expressions, groupDefn, null, null);

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
