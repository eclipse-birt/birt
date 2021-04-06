/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.impl.binding;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import com.ibm.icu.util.Calendar;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for aggregate JSExpression
 */
public class AggregationTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Impl.TestData2.TableName"),
				ConfigText.getString("Impl.TestData2.TableSQL"),
				ConfigText.getString("Impl.TestData2.TestDataFileName"));
	}

	// A test case with some mixed aggregate functions at different levels
	@Test
	public void test1() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	@Test
	public void test2() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		FilterDefinition f1 = new FilterDefinition(
				new ConditionalExpression("row[\"e7\"]", IConditionalExpression.OP_TOP_N, "2"));
		query.addFilter(f1);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	@Test
	public void test4() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		FilterDefinition f1 = new FilterDefinition(
				new ConditionalExpression("row[\"e7\"]", IConditionalExpression.OP_TOP_N, "1"), false);
		query.addFilter(f1);

		FilterDefinition f2 = new FilterDefinition(new ScriptExpression("row[\"e10\"] < 200"));
		query.addFilter(f2);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	// add for ted 56549
	@Test
	public void test8() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("row.e7 / row.e81"));
		query.addBinding(e8);

		// Aggregate: day total sales
		IBinding e9 = new Binding("e9", new ScriptExpression("row.e8"));
		e9.setAggrFunction("SUM");
		query.addBinding(e9);

		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	@Test
	public void test6() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		FilterDefinition f1 = new FilterDefinition(
				new ConditionalExpression("row[\"e7\"]", IConditionalExpression.OP_TOP_N, "1"));
		query.addFilter(f1);

		FilterDefinition f2 = new FilterDefinition(new ScriptExpression("row[\"e10\"] < 200"));
		query.addFilter(f2);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	@Test
	public void test7() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		FilterDefinition f1 = new FilterDefinition(
				new ConditionalExpression("row[\"e7\"]", IConditionalExpression.OP_TOP_N, "1"));
		query.addFilter(f1);

		FilterDefinition f2 = new FilterDefinition(new ScriptExpression("row[\"e10\"] < 200"), false);
		query.addFilter(f2);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	@Test
	public void test11() throws Exception {
		QueryDefinition query = newReportQuery();

		Calendar c = Calendar.getInstance();
		c.clear();
		// 3 grouping levels: CITY, STORE, SALE_DATE(by month)
		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");

		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("row.e5");
		sortDefn.setSortDirection(ISortDefinition.SORT_DESC);
		g1.addSort(sortDefn);
		FilterDefinition filter = new FilterDefinition(new ScriptExpression("row.e7 > 100"));
		g1.addFilter(filter);

		query.addGroup(g1);

		GroupDefinition g2 = new GroupDefinition("G2");
		g2.setKeyExpression("row.e2");

		query.addGroup(g2);

		GroupDefinition g3 = new GroupDefinition("G3");
		g3.setKeyExpression("row.e3");
		g3.setInterval(GroupDefinition.MONTH_INTERVAL);
		g3.setIntervalRange(1);

		c.set(2004, 9, 1);
		g3.setIntervalStart(c.getTime());
		query.addGroup(g3);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row.e3");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		query.addSort(sort);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));
		query.addBinding(new Binding("e2", new ScriptExpression("dataSetRow.STORE")));
		query.addBinding(new Binding("e3", new ScriptExpression("dataSetRow.SALE_DATE")));
		query.addBinding(new Binding("e4", new ScriptExpression("dataSetRow.SKU")));
		query.addBinding(new Binding("e10", new ScriptExpression("dataSetRow.PRICE")));
		query.addBinding(new Binding("e11", new ScriptExpression("dataSetRow.QUANTITY")));

		// Aggregate: count at city level
		IBinding e5 = new Binding("e5");
		e5.setAggrFunction("COUNT");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		// Aggregate: count at city level but added to Store group
		IBinding e6 = new Binding("e6");
		e6.setAggrFunction("COUNT");
		e6.addAggregateOn("G1");
		query.addBinding(e6);

		// Aggregate: day total sales
		IBinding e7 = new Binding("e7", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e7.setAggrFunction("SUM");
		e7.addAggregateOn("G3");
		query.addBinding(e7);

		IBinding e81 = new Binding("e81", new ScriptExpression("dataSetRow.PRICE*dataSetRow.QUANTITY"));
		e81.setAggrFunction("SUM");
		query.addBinding(e81);

		// Aggregate: Percent of grand total
		IBinding e8 = new Binding("e8", new ScriptExpression("dataSetRow.PRICE * dataSetRow.QUANTITY / row.e81"));
		query.addBinding(e8);

		// Aggregate: a moving ave with a filtering condition
		IBinding e9 = new Binding("e9", new ScriptExpression("dataSetRow.PRICE"));
		e9.setAggrFunction("MOVINGAVE");
		e9.setFilter(new ScriptExpression("dataSetRow.QUANTITY>1"));
		e9.addArgument(new ScriptExpression("3"));
		query.addBinding(e9);
		String[] exprs = new String[] { "e1", "e2", "e3", "e4", "e10", "e11", "e5", "e6", "e7", "e8", "e9" };

		outputQueryResult(executeQuery(query), exprs);
		checkOutputFile();
	}

	// Test aggregates on empty result set
	@Test
	public void test3() throws Exception {
		QueryDefinition query = newReportQuery();

		// Add a filter to filter out all rows
		query.addFilter(new FilterDefinition(new ScriptExpression("false")));

		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e1");
		query.addGroup(g1);

		query.addBinding(new Binding("e1", new ScriptExpression("dataSetRow.CITY")));

		Binding e2 = new Binding("e2");
		e2.addAggregateOn("G1");
		e2.setAggrFunction("COUNT");
		query.addBinding(e2);

		IResultIterator resultIt = executeQuery(query);
		assertFalse(resultIt.next());
		// The Total.Count() against empty result set should return 0
		assertEquals(new Integer(0), resultIt.getValue("e2"));
	}

	// When there is exception thrown by the calculator of aggregation,it
	// caused by "expression is invalid", "filter is invalid" etc, but it should
	// not affect latter aggregations caculation.
	@Test
	public void test5() throws Exception {
		QueryDefinition query = newReportQuery();

		GroupDefinition g1 = new GroupDefinition("G1");
		g1.setKeyExpression("row.e0");
		query.addGroup(g1);

		query.addBinding(new Binding("e0", new ScriptExpression("dataSetRow.CITY")));

		Binding e1 = new Binding("e1", new ScriptExpression("dataSetRow.PRICE"));
		e1.setAggrFunction("RUNNINGSUM");
		e1.addAggregateOn("G1");
		e1.addArgument(new ScriptExpression("abc"));
		query.addBinding(e1);

		Binding e2 = new Binding("e2", new ScriptExpression("dataSetRow.PRICE"));
		e2.setAggrFunction("SUM");
		e2.addAggregateOn("G1");
		query.addBinding(e2);

		Binding e3 = new Binding("e3", new ScriptExpression("dataSetRow.PRICE"));
		e3.setAggrFunction("RANK");
		e3.addArgument(new ScriptExpression("true"));
		e3.addAggregateOn("G1");
		query.addBinding(e3);

		Binding e4 = new Binding("e4", new ScriptExpression("dataSetRow.PRICE"));
		e4.setAggrFunction("SUM");
		e4.addArgument(new ScriptExpression("a"));
		e4.addAggregateOn("G1");
		query.addBinding(e4);

		Binding e5 = new Binding("e5", new ScriptExpression("dataSetRow.PRICE"));
		e5.setAggrFunction("RUNNINGSUM");
		e5.addAggregateOn("G1");
		query.addBinding(e5);

		IResultIterator resultIt = executeQuery(query);

		String[] exprs = new String[] { "e0", "e1", "e2", "e3", "e4", "e5" };

		outputQueryResult(resultIt, exprs);
		checkOutputFile();
	}

}
