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
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;

import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Provide an example to use PerfTestUtil to do bench mark test. If you want to
 * define your datasource and dataset, please use this case.
 * 
 * Make sure your defined datasource is available when running test.
 */
@Ignore("ignore performance test")
public class APIPerfTest2 {
	/** instance of performance test utility */
	private APIPerfTestUtil perfTest = APIPerfTestUtil.newInstance();

	/** JDBC data source and data set info */
	public static final String JDBC_DATA_SOURCE_TYPE = "org.eclipse.birt.report.data.oda.jdbc";
	public static final String JDBC_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet";

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void apiPerfSetUp() {
		System.setProperty("BIRT_HOME", "./test");
	}

	/**
	 * Test simple JDBC query
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryWithJDBC() {
		/**
		 * Basic info for this data set is: row number: 150,000 column info: C_CUSTKEY
		 * int C_NAME String C_ADDRESS String C_NATIONKEY int C_PHONE String C_ACCTBAL
		 * int C_MKTSEGMENT String C_COMMENT String
		 */
		// Define queryInfo needs to be tested
		QueryInfo queryInfo = new QueryInfo() {

			private String url = "jdbc:mysql://spmdb/test";
			private String driverClass = "com.mysql.jdbc.Driver";
			private String user = "root";
			private String password = "root";
			private String queryText = "select * from l_customer";

			private OdaDataSourceDesign odaDataSource;
			private OdaDataSetDesign odaDataSet;
			private QueryDefinition queryDefinition;
			private IBaseExpression[] expressionArray;
			private String[] exprNames;

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getDataSource()
			 */
			public IBaseDataSourceDesign getDataSource() throws Exception {
				if (odaDataSource != null)
					return odaDataSource;

				odaDataSource = new OdaDataSourceDesign("Test Data Source");
				odaDataSource.setExtensionID(JDBC_DATA_SOURCE_TYPE);
				odaDataSource.addPublicProperty("odaURL", url);
				odaDataSource.addPublicProperty("odaDriverClass", driverClass);
				odaDataSource.addPublicProperty("odaUser", user);
				odaDataSource.addPublicProperty("odaPassword", password);

				return odaDataSource;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getDataSet()
			 */
			public IBaseDataSetDesign getDataSet() throws Exception {
				if (odaDataSet != null)
					return odaDataSet;

				odaDataSet = new OdaDataSetDesign("Test Data Set");
				odaDataSet.setDataSource(getDataSource().getName());
				odaDataSet.setExtensionID(JDBC_DATA_SET_TYPE);
				odaDataSet.setQueryText(getQueryText());

				return odaDataSet;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getQueryDefn()
			 */
			public QueryDefinition getQueryDefn() throws Exception {
				if (queryDefinition != null)
					return queryDefinition;

				queryDefinition = new QueryDefinition();
				queryDefinition.setDataSetName(getDataSet().getName());

				// add expression based on group defintion
				expressionArray = new IBaseExpression[3];
				exprNames = new String[3];

				ScriptExpression expr = new ScriptExpression("dataSetRow.C_CUSTKEY");
				expressionArray[0] = expr;
				exprNames[0] = "C_CUSTKEY";

				expr = new ScriptExpression("dataSetRow.C_NAME");
				expressionArray[1] = expr;
				exprNames[1] = "C_NAME";

				expr = new ScriptExpression("dataSetRow.C_ACCTBAL");
				expressionArray[2] = expr;
				exprNames[2] = "C_ACCTBAL";
				for (int i = 0; i < expressionArray.length; i++)
					queryDefinition.addResultSetExpression(exprNames[i], expressionArray[i]);

				furthurProcessQueryDefn(queryDefinition);

				return queryDefinition;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getExprArray()
			 */
			public String[] getExprNames() {
				return exprNames;
			}

			/**
			 * @return query text used in JDBC data set
			 */
			private String getQueryText() {
				int maxRows = -1;
				if (maxRows > 0)
					return queryText + " where l_customer.C_CUSTKEY < " + maxRows;
				else
					return queryText;
			}

			/**
			 * Add more operation to query definition
			 * 
			 * @param queryDefn2
			 */
			private void furthurProcessQueryDefn(QueryDefinition queryDefn2) {
				boolean filter = true;
				boolean sorter = false;

				if (filter == true) {
					int maxKey = 2;
					FilterDefinition exprFilter = new FilterDefinition(
							new ScriptExpression("dataSetRow.C_CUSTKEY<" + maxKey));
					queryDefn2.getFilters().add(exprFilter);
				}

				if (sorter == true) {
					SortDefinition sd = new SortDefinition();
					sd.setExpression("dataSetRow.C_ACCTBAL");
					sd.setSortDirection(SortDefinition.SORT_DESC);
					queryDefn2.addSort(sd);
				}
			}
		};

		try {
			perfTest.setQueryInfo(queryInfo);

			boolean isTimeTest = true;
			boolean isSpaceTest = false;
			if (isTimeTest) {
				System.out.println("time bench mark of query");

				boolean isTimeAveValue = true;
				perfTest.runTimeBenchMark(isTimeAveValue);
			}
			if (isSpaceTest) {
				System.out.println("space bench mark of query");

				boolean isSpaceAveValue = true;
				perfTest.runSpaceBenchMark(isSpaceAveValue);
			}
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test simple SCRIPT query
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQueryWithScript() {
		/**
		 * Basic info for this data set is: row number: 150,000 column info: NUM int
		 * SQUARE double STR String
		 */
		// Define queryInfo needs to be tested
		QueryInfo queryInfo = new QueryInfo() {

			private ScriptDataSourceDesign odaDataSource;
			private ScriptDataSetDesign odaDataSet;
			private QueryDefinition queryDefinition;
			private IBaseExpression[] expressionArray;
			private String[] exprNames;

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getDataSource()
			 */
			public IBaseDataSourceDesign getDataSource() throws Exception {
				if (odaDataSource != null)
					return odaDataSource;

				odaDataSource = new ScriptDataSourceDesign("JUST as place folder");

				return odaDataSource;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getDataSet()
			 */
			public IBaseDataSetDesign getDataSet() throws Exception {
				if (odaDataSet != null)
					return odaDataSet;

				// set script for data set
				odaDataSet = new ScriptDataSetDesign("ScriptedDataSet");
				odaDataSet.setDataSource(getDataSource().getName());
				odaDataSet.setOpenScript("count=300000;");
				odaDataSet.setFetchScript("if (count==0) " + "{" + "return false; " + "} " + "else " + "{ "
						+ "row.NUM=count; " + "row.SQUARE=count*count; " + "row.STR=\"row#\" + count; " + "--count; "
						+ "return true; " + "}");

				// set column defintion for data set
				String[] scriptColumnNames = new String[] { "NUM", "SQUARE", "STR" };
				int[] scriptColumnTypes = new int[] { DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE,
						DataType.STRING_TYPE };
				for (int i = 0; i < scriptColumnNames.length; i++) {
					ColumnDefinition colInfo = new ColumnDefinition(scriptColumnNames[i]);
					colInfo.setDataType(scriptColumnTypes[i]);
					odaDataSet.getResultSetHints().add(colInfo);
				}

				return odaDataSet;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getQueryDefn()
			 */
			public QueryDefinition getQueryDefn() throws Exception {
				if (queryDefinition != null)
					return queryDefinition;

				queryDefinition = new QueryDefinition();
				queryDefinition.setDataSetName(getDataSet().getName());

				// add expression based on group defintion
				expressionArray = new IBaseExpression[3];
				exprNames = new String[3];

				ScriptExpression expr = new ScriptExpression("dataSetRow.NUM");
				expressionArray[0] = expr;
				exprNames[0] = "NUM";

				expr = new ScriptExpression("dataSetRow.SQUARE");
				expressionArray[1] = expr;
				exprNames[1] = "SQUARE";

				expr = new ScriptExpression("dataSetRow.STR");
				expressionArray[2] = expr;
				exprNames[2] = "STR";

				for (int i = 0; i < expressionArray.length; i++)
					queryDefinition.addResultSetExpression(exprNames[i], expressionArray[i]);

				return queryDefinition;
			}

			/*
			 * @see org.eclipse.birt.data.engine.perf.QueryInfo#getExprArray()
			 */
			public String[] getExprNames() {
				return exprNames;
			}
		};

		try {
			perfTest.setQueryInfo(queryInfo);

			boolean isTimeTest = true;
			boolean isSpaceTest = false;
			if (isTimeTest) {
				System.out.println("time bench mark of query");

				boolean isTimeAveValue = true;
				perfTest.runTimeBenchMark(isTimeAveValue);
			}
			if (isSpaceTest) {
				System.out.println("space bench mark of query");

				boolean isSpaceAveValue = true;
				perfTest.runSpaceBenchMark(isSpaceAveValue);
			}
		} catch (Throwable e) {
			fail(e.getMessage());
		}
	}

}
