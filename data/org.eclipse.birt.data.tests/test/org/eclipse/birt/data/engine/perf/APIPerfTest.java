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
package org.eclipse.birt.data.engine.perf;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import org.junit.Test;
import org.junit.Ignore;

/**
 * Test bench mark of DtE by using DtE API.
 * 
 * The test data is input from text file, and then can be done independently.
 */
@Ignore("Ignore performance test")
public class APIPerfTest extends APITestCase {
	/** defined query defintion */
	private QueryDefinition queryDefn;

	/** defined expression array */
	private IBaseExpression[] exprArray;

	private String[] exprNames;

	/** instance of performance test utility */
	private APIPerfTestUtil perfTest = APIPerfTestUtil.newInstance();

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData2.TableName"),
				ConfigText.getString("Api.TestData2.TableSQL"), ConfigText.getString("Api.TestData2.TestDataFileName"));
	}

	/**
	 * Test simple query without any procession
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuery() throws Exception {
		// Define queryInfo needs to be tested
		/**
		 * Basic info for this data set is: row number: 3003 column info: id INT amount1
		 * INT amount2 INT date_for_group DATE date_for_quarter DATE
		 */
		QueryInfo queryInfo = new QueryInfo() {

			public IBaseDataSourceDesign getDataSource() {
				return dataSource;
			}

			public IBaseDataSetDesign getDataSet() {
				return dataSet;
			}

			public QueryDefinition getQueryDefn() {
				return getQueryDefintion(false, false, false, false);
			}

			public String[] getExprNames() {
				return getExpressionArray();
			}
		};

		System.out.println("time bench mark of raw query");
		perfTest.setQueryInfo(queryInfo);
		perfTest.runTimeBenchMark(true);
	}

	/**
	 * Test simple query with filter
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryWithFilter() throws Exception {
		// Define queryInfo needs to be tested
		QueryInfo queryInfo = new QueryInfo() {

			public IBaseDataSourceDesign getDataSource() {
				return dataSource;
			}

			public IBaseDataSetDesign getDataSet() {
				return dataSet;
			}

			public QueryDefinition getQueryDefn() {
				return getQueryDefintion(false, false, false, true);
			}

			public String[] getExprNames() {
				return getExpressionArray();
			}
		};

		System.out.println("time bench mark of query with filter");
		perfTest.setQueryInfo(queryInfo);
		perfTest.runTimeBenchMark(true);
	}

	/**
	 * @param withGroup          whethter group is used
	 * @param withSort           whethter sort is used
	 * @param withFilter         whethter filter is used
	 * @param withComputedColumn whethter computed column is used
	 * @return query definition
	 */
	private QueryDefinition getQueryDefintion(boolean withGroup, boolean withSort, boolean withFilter,
			boolean withComputedColumn) {
		if (queryDefn != null)
			return queryDefn;

		queryDefn = newReportQuery();

		// add expression based on group defintion
		exprArray = new IBaseExpression[3];

		exprNames = new String[3];
		ScriptExpression expr = new ScriptExpression("dataSetRow.ID");
		exprArray[0] = expr;
		exprNames[0] = "ID";

		expr = new ScriptExpression("dataSetRow.AMOUNT1");
		exprArray[1] = expr;
		exprNames[1] = "AMOUNT1";

		expr = new ScriptExpression("dataSetRow.AMOUNT2");
		exprArray[2] = expr;
		exprNames[2] = "AMOUNT2";

		for (int i = 0; i < exprArray.length; i++)
			queryDefn.addResultSetExpression(exprNames[i], exprArray[i]);

		// add group
		if (withGroup) {
			GroupDefinition[] gdArray = new GroupDefinition[1];

			GroupDefinition gd = new GroupDefinition();
			gd.setKeyExpression("dataSetRow[1]");
			gdArray[0] = gd;

			for (int i = 0; i < gdArray.length; i++)
				queryDefn.addGroup(gdArray[i]);
		}

		// add sort
		if (withSort) {
			SortDefinition[] sdArray = new SortDefinition[1];

			SortDefinition sd = new SortDefinition();
			sd.setExpression("dataSetRow[1]");
			sd.setSortDirection(ISortDefinition.SORT_DESC);
			sdArray[0] = sd;

			for (int i = 0; i < sdArray.length; i++)
				queryDefn.addSort(sdArray[i]);
		}

		// add filter
		if (withFilter) {
			FilterDefinition exprFilter = new FilterDefinition(new ScriptExpression("row.ID>13"));
			queryDefn.getFilters().add(exprFilter);
		}

		// add computed column
		if (withComputedColumn) {
			ComputedColumn computedColumn = new ComputedColumn("cc", "dataSetRow.ID*2", DataType.ANY_TYPE);
			this.dataSet.addComputedColumn(computedColumn);
		}

		return queryDefn;
	}

	/**
	 * @return used expression in row
	 */
	private String[] getExpressionArray() {
		return exprNames;
	}

}
