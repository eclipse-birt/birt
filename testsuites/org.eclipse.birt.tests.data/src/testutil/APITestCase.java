/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package testutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.datatools.connectivity.oda.IResultSet;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * Base class for test cases that work with Data Engine public API
 */
abstract public class APITestCase extends BaseTestCase {

	/** connection property */
	protected String DriverClass;
	protected String URL;
	protected String User;
	protected String Password;

	/** test table and util */
	private String tableName;
	protected TestDataSource dataSourceInstance;

	/** instance of DataEngine */
	protected DataEngine dataEngine;
	/**
	 * Every test case might have one datasource and dataset. They are defined in
	 * base class for convinience to use.
	 */
	protected BaseDataSourceDesign dataSource;
	protected BaseDataSetDesign dataSet;

	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden";
	protected static final String OUTPUT_FOLDER = "output";

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		dataEngine = DataEngine.newDataEngine(
				DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, jsScope, null, null));
		prepareDataSource();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		dataEngine.shutdown();
		closeDataSource();

		super.tearDown();
	}

	/**
	 * Prepare test table and oda data source and data set design. In most case,
	 * only one data source needs to be defined.
	 *
	 * @throws Exception
	 */
	private void prepareDataSource() throws Exception {
		DataSourceInfo dataSourceInfo = getDataSourceInfo();
		if (dataSourceInfo != null) {
			// derived class might want to use other data base rather than derby
			prepareDataSet(dataSourceInfo);

			dataSource = this.dataSourceInstance.getOdaDataSourceDesign();
			dataSet = this.dataSourceInstance.getOdaDataSetDesign();
			((OdaDataSetDesign) dataSet).setQueryText("select * from " + dataSourceInfo.tableName);
			dataEngine.defineDataSource(this.dataSource);
			dataEngine.defineDataSet(this.dataSet);
		}
	}

	/**
	 * Prepare data source connection property, these properties will be used in
	 * test table preparation and oda data source preparation.
	 */
	/*
	 * private void prepareDataSourceProperty() { if ( DriverClass == null )
	 * DriverClass = "org.apache.derby.jdbc.EmbeddedDriver"; if ( URL == null ) URL
	 * = JDBCDataSourceUtil.getURL(); if ( User == null ) User = "user"; if (
	 * Password == null ) Password = "password"; System.setProperty(
	 * "DTETest.driver", DriverClass ); System.setProperty( "DTETest.url", URL );
	 * System.setProperty( "DTETest.user",User); System.setProperty(
	 * "DTETest.password",Password); }
	 */
	/**
	 * Prepare test table. This method is defined separatelly since in some test
	 * cases, they might use more than one data set, although they share the same
	 * data source.
	 *
	 * @param dataSourceInfo
	 * @throws Exception
	 */
	protected void prepareDataSet(DataSourceInfo dataSourceInfo) throws Exception {
		if (dataSourceInfo != null && dataSourceInfo.tableName != null && dataSourceInfo.createSql != null
				&& dataSourceInfo.dataFileName != null) {
			this.tableName = dataSourceInfo.tableName;

			this.prepareTestTable(dataSourceInfo.tableName, dataSourceInfo.createSql, dataSourceInfo.dataFileName);
		}
	}

	/**
	 * Create test table and populate data into table, currently only derby data
	 * base is used.
	 *
	 * @param tableName
	 * @param createSql
	 * @param dataFileName
	 * @throws Exception
	 */
	private void prepareTestTable(String tableName, String createSql, String dataFileName) throws Exception {
		if (dataSourceInstance == null) {
			dataSourceInstance = JDBCDataSource.newInstance();
		}

		// create table
		this.dataSourceInstance.createTable(tableName, createSql, true);

		// insert data into table
		this.dataSourceInstance.populateTable(tableName, getInputFolder(dataFileName));
	}

	/**
	 * Normally, an API test case will be based on a particular data source, so
	 * before test begins, test case provides which data source will be used and
	 * then its data will be prepared and meantime its data source and data set will
	 * be defined in DataEngine. If the return value is null, there is no any
	 * datasource and dataset is defined in dataEngine, which means other methods
	 * such as newDataSet and newReportQuery can not be used.
	 *
	 * @return which data source will be used in this test case
	 */
	protected abstract DataSourceInfo getDataSourceInfo();

	/**
	 * Wrap the info for the preparation of data source, when derby is used, it
	 * needs to provide the createSql and dataFileName to create a table for test.
	 */
	public class DataSourceInfo {

		private String tableName;
		private String createSql;
		private String dataFileName;

		/**
		 * @param tableName,    used table name
		 * @param createSql,    sql to create table
		 * @param dataFileName, data to insert table
		 */
		public DataSourceInfo(String tableName, String createSql, String dataFileName) {
			this.tableName = tableName;
			this.createSql = createSql;
			this.dataFileName = dataFileName;
		}
	}

	/**
	 * @throws Exception
	 */
	protected void closeDataSource() throws Exception {
		if (this.dataSourceInstance != null) {
			if (tableName != null) {
				dataSourceInstance.dropTable(tableName);
			}
			this.dataSourceInstance.close(true);
			this.dataSourceInstance = null;
		}
	}

	/**
	 * @return test table name
	 */
	protected String getTestTableName() {
		return this.tableName;
	}

	/**
	 * new a JDBC dataset with specified datasetname and querytext
	 *
	 * @param datasetName
	 * @param queryText
	 * @return dataset
	 * @throws Exception
	 */
	protected OdaDataSetDesign newDataSet(String datasetName, String queryText) throws Exception {
		OdaDataSetDesign dset = new OdaDataSetDesign(datasetName);
		dset.setDataSource(this.dataSource.getName());
		dset.setQueryText(queryText);
		dset.setExtensionID(JDBCOdaDataSource.DATA_SET_TYPE);
		dataEngine.defineDataSet(dset);

		return dset;
	}

	/**
	 * new a default query, which only has data set information
	 *
	 * @return queryDefn QueryDefinition
	 */
	protected QueryDefinition newReportQuery() {
		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());

		return queryDefn;
	}

	/**
	 * new a simple query with specified dataset
	 *
	 * @return queryDefn QueryDefinition
	 */
	protected QueryDefinition newReportQuery(IBaseDataSetDesign dataset) {
		QueryDefinition queryDefn = new QueryDefinition();
		if (dataset == null) {
			queryDefn.setDataSetName(this.dataSet.getName());
		} else {
			queryDefn.setDataSetName(dataset.getName());
		}

		return queryDefn;
	}

	/**
	 * Execute query definition
	 *
	 * @param query
	 * @return resultIterator
	 * @throws Exception
	 */
	protected IResultIterator executeQuery(IQueryDefinition query) throws Exception {
		IPreparedQuery preparedQuery = dataEngine.prepare(query);
		IQueryResults queryResults = preparedQuery.execute(null);
		return queryResults.getResultIterator();
	}

	/**
	 * Output result of executing query defintion
	 *
	 * @param resultIt
	 * @param expressions
	 * @throws Exception
	 */
	protected void outputQueryResult(IResultIterator resultIt, String[] expressions) throws Exception {
		assert testOut != null;

		// output result
		testPrintln("*****A new Report Start!*****");
		while (resultIt.next()) {
			testPrint("S:");
			testPrint(Integer.toString(resultIt.getStartingGroupLevel()));
			testPrint(" E:");
			testPrint(Integer.toString(resultIt.getEndingGroupLevel()));
			testPrint(" ");
			for (int i = 0; i < expressions.length; i++) {
				testPrint(evalAsString(expressions[i], resultIt));
				testPrint("    ");
			}
			testPrintln("");
		}
		testPrintln("");
	}

	/**
	 * @param queryDefn
	 * @param exprName
	 * @throws Exception
	 */
	protected void executeQuery(QueryDefinition queryDefn, String[] exprName) throws Exception {
		IResultIterator resultIt = executeQuery(queryDefn);
		testPrintln("*****A new Report Start!*****");
		while (resultIt.next()) {
			testPrint("S:");
			testPrint(Integer.toString(resultIt.getStartingGroupLevel()));
			testPrint(" E:");
			testPrint(Integer.toString(resultIt.getEndingGroupLevel()));
			testPrint(" ");
			for (int i = 0; i < exprName.length; i++) {
				testPrint(evalAsString(exprName[i], resultIt));
				testPrint("    ");
			}
			testPrintln("");
		}
		testPrintln("");
	}

	/**
	 * @param name
	 * @param result
	 * @return
	 */
	protected String evalAsString(String name, IResultIterator result) {
		try {
			Object val = result.getValue(name);
			if (val == null) {
				return "<null>";
			} else {
				return val.toString();
			}
		} catch (Exception e) {
			// Not all expressions can be evaluated in all rows
			// Print an error if it cannot be
			return "<EXCEPTION>";
		}
	}

	/**
	 * Return default query definition
	 *
	 * @param dataSetName
	 * @return default query definition
	 */
	protected IQueryDefinition getDefaultQueryDefn(String dataSetName) {
		return Util.instance.getDefaultQueryDefn(dataSetName);
	}

	/**
	 * Return default query definition with subquery
	 *
	 * @param dataSetName
	 * @return default query definition with subquery
	 */
	protected IQueryDefinition getDefaultQueryDefnWithSubQuery(String dataSetName) {
		return Util.instance.getDefaultQueryDefnWithSubQuery(dataSetName);
	}

	/**
	 * Return expression of default query
	 *
	 * @return expression of default query
	 */
	protected BaseExpression[] getExpressionsOfDefaultQuery() {
		return Util.instance.getExpressionsOfDefaultQuery();
	}

	protected String[] getBindingExpressionName() {
		return Util.instance.getBindingExpressionName();
	}

	protected void populateQueryExprMapping(SubqueryDefinition subqueryDefn) {
		Util.instance.populateQueryExprMapping(subqueryDefn);
	}

	/**
	 * Utility
	 */
	private static class Util {

		private static Util instance = new Util();

		private static BaseExpression[] expressions;

		private static String[] bindingNameRow;

		/**
		 * Create a general Query with groups,sorts and subquery
		 *
		 * @param dataSetName
		 * @return queryDefn
		 */
		protected IQueryDefinition getDefaultQueryDefn(String dataSetName) {

			String[] bindingNameGroup = new String[3];
			bindingNameGroup[0] = "GROUP_COL0";
			bindingNameGroup[1] = "GROUP_COL1";
			bindingNameGroup[2] = "GROUP_COL2";
			IBaseExpression[] bindingExprGroup = new IBaseExpression[3];
			bindingExprGroup[0] = new ScriptExpression("dataSetRow.COL0");
			bindingExprGroup[1] = new ScriptExpression("dataSetRow.COL1");
			bindingExprGroup[2] = new ScriptExpression("dataSetRow.COL2");
			GroupDefinition[] groupDefn = { new GroupDefinition("group1"), new GroupDefinition("group2"),
					new GroupDefinition("group3") };
			groupDefn[0].setKeyExpression("row.GROUP_COL0");
			groupDefn[1].setKeyExpression("row.GROUP_COL1");
			groupDefn[2].setKeyExpression("row.GROUP_COL2");

			String[] bindingNameSort = new String[1];
			bindingNameSort[0] = "SORT_COL3";
			IBaseExpression[] bindingExprSort = new IBaseExpression[1];
			bindingExprSort[0] = new ScriptExpression("dataSetRow.COL3");
			SortDefinition[] sortDefn = { new SortDefinition() };
			sortDefn[0].setColumn("SORT_COL3");
			sortDefn[0].setSortDirection(ISortDefinition.SORT_ASC);

			bindingNameRow = new String[4];
			bindingNameRow[0] = "ROW_COL0";
			bindingNameRow[1] = "ROW_COL1";
			bindingNameRow[2] = "ROW_COL2";
			bindingNameRow[3] = "ROW_COL3";
			// 2.3: ExpressionKey
			expressions = new BaseExpression[] { new ScriptExpression("dataSetRow.COL0", 0),
					new ScriptExpression("dataSetRow.COL1", 0), new ScriptExpression("dataSetRow.COL2", 0),
					new ScriptExpression("dataSetRow.COL3", 0) };

			return this.getQueryDefinition(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort,
					bindingExprSort, sortDefn, null, null, null, bindingNameRow, expressions, dataSetName);
		}

		private QueryDefinition getQueryDefinition(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
				GroupDefinition[] groupDefn, String[] bindingNameSort, IBaseExpression[] bindingExprSort,
				SortDefinition[] sortDefn, String[] bindingNameFilter, IBaseExpression[] bindingExprFilter,
				FilterDefinition[] filterDefn, String[] bindingNameRow, IBaseExpression[] bindingExprRow,
				String dataSetName) {
			QueryDefinition queryDefn = new QueryDefinition();
			queryDefn.setDataSetName(dataSetName);

			// add transformation definition
			if (groupDefn != null) {
				if (bindingNameGroup != null) {
					for (int i = 0; i < bindingNameGroup.length; i++) {
						queryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
					}
				}
				for (int i = 0; i < groupDefn.length; i++) {
					queryDefn.addGroup(groupDefn[i]);
				}
			}

			if (sortDefn != null) {
				if (bindingNameSort != null) {
					for (int i = 0; i < bindingNameSort.length; i++) {
						queryDefn.addResultSetExpression(bindingNameSort[i], bindingExprSort[i]);
					}
				}
				for (int i = 0; i < sortDefn.length; i++) {
					queryDefn.addSort(sortDefn[i]);
				}
			}

			// add value retrive tansformation
			if (bindingNameRow != null) {
				for (int i = 0; i < bindingNameRow.length; i++) {
					queryDefn.addResultSetExpression(bindingNameRow[i], expressions[i]);
				}
			}
			return queryDefn;

		}

		/**
		 * Get query definition with sub query
		 *
		 * @return queryDefn
		 */
		protected IQueryDefinition getDefaultQueryDefnWithSubQuery(String dataSetName) {
			IQueryDefinition queryDefn = getDefaultQueryDefn(dataSetName);

			// row.Col1
			GroupDefinition groupDefn = (GroupDefinition) queryDefn.getGroups().get(1);

			// ---------- begin sub query ----------
			SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST");
			groupDefn.addSubquery(subqueryDefn);

			String[] bindingNameGroup = new String[1];
			bindingNameGroup[0] = "GROUP_COL2";
			IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
			bindingExprGroup[0] = new ScriptExpression("dataSetRow.COL2");
			GroupDefinition[] subGroupDefn = { new GroupDefinition("group2") };
			// subGroupDefn[0].setKeyExpression( "row.GROUP_COL2" );
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
			populateQueryExprMapping(subqueryDefn);

			// --- sub query of sub query
			SubqueryDefinition subSubqueryDefn = new SubqueryDefinition("IAMTEST2");
			subGroupDefn[0].addSubquery(subSubqueryDefn);

			bindingNameGroup = new String[1];
			bindingNameGroup[0] = "GROUP_COL3";
			bindingExprGroup = new IBaseExpression[1];
			bindingExprGroup[0] = new ScriptExpression("dataSetRow.COL3");
			GroupDefinition[] subSubGroupDefn = { new GroupDefinition("group3") };
			// subSubGroupDefn[0].setKeyExpression( "row.GROUP_COL3" );
			subSubGroupDefn[0].setKeyExpression("row.GROUP_COL3");

			for (int k = 0; k < subSubGroupDefn.length; k++) {
				if (bindingNameGroup != null) {
					for (int i = 0; i < bindingNameGroup.length; i++) {
						subSubqueryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
					}
				}

				for (int i = 0; i < subSubGroupDefn.length; i++) {
					subSubqueryDefn.addGroup(subSubGroupDefn[i]);
				}
			}
			populateQueryExprMapping(subSubqueryDefn);
			// --- sub query of sub query

			// ---------- end sub query ----------

			return queryDefn;
		}

		protected void populateQueryExprMapping(SubqueryDefinition subqueryDefn) {
			// ///TODO remove in future
			for (int i = 0; i < bindingNameRow.length; i++) {
				subqueryDefn.addResultSetExpression(bindingNameRow[i], expressions[i]);
				// ///////////////////////
			}
		}

		/**
		 * Get default query expressions
		 *
		 * @return expressions BaseExpression[]
		 */
		protected BaseExpression[] getExpressionsOfDefaultQuery() {
			return expressions;
		}

		protected String[] getBindingExpressionName() {
			return bindingNameRow;
		}
	}

	/**
	 * @param bindingNameGroup
	 * @param bindingExprGroup
	 * @param groupDefn
	 * @param bindingNameSort
	 * @param bindingExprSort
	 * @param sortDefn
	 * @param bindingNameFilter
	 * @param bindingExprFilter
	 * @param filterDefn
	 * @param bindingNameRow
	 * @param bindingExprRow
	 * @throws Exception
	 */
	protected void createAndRunQuery(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
			GroupDefinition[] groupDefn, String[] bindingNameSort, IBaseExpression[] bindingExprSort,
			SortDefinition[] sortDefn, String[] bindingNameFilter, IBaseExpression[] bindingExprFilter,
			FilterDefinition[] filterDefn, String[] bindingNameRow, IBaseExpression[] bindingExprRow) throws Exception {
		executeQuery(
				createQuery(bindingNameGroup, bindingExprGroup, groupDefn, bindingNameSort, bindingExprSort, sortDefn,
						bindingNameFilter, bindingExprFilter, filterDefn, bindingNameRow, bindingExprRow),
				bindingNameRow);
	}

	/**
	 * @param bindingNameGroup
	 * @param bindingExprGroup
	 * @param groupDefn
	 * @param bindingNameSort
	 * @param bindingExprSort
	 * @param sortDefn
	 * @param bindingNameFilter
	 * @param bindingExprFilter
	 * @param filterDefn
	 * @param bindingNameRow
	 * @param bindingExprRow
	 * @return
	 * @throws Exception
	 */
	protected QueryDefinition createQuery(String[] bindingNameGroup, IBaseExpression[] bindingExprGroup,
			GroupDefinition[] groupDefn, String[] bindingNameSort, IBaseExpression[] bindingExprSort,
			SortDefinition[] sortDefn, String[] bindingNameFilter, IBaseExpression[] bindingExprFilter,
			FilterDefinition[] filterDefn, String[] bindingNameRow, IBaseExpression[] bindingExprRow) throws Exception {
		QueryDefinition queryDefn = newReportQuery();

		// add transformation definition
		if (groupDefn != null) {
			if (bindingNameGroup != null) {
				for (int i = 0; i < bindingNameGroup.length; i++) {
					queryDefn.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
				}
			}
			for (int i = 0; i < groupDefn.length; i++) {
				queryDefn.addGroup(groupDefn[i]);
			}
		}

		if (sortDefn != null) {
			if (bindingNameSort != null) {
				for (int i = 0; i < bindingNameSort.length; i++) {
					queryDefn.addResultSetExpression(bindingNameSort[i], bindingExprSort[i]);
				}
			}
			for (int i = 0; i < sortDefn.length; i++) {
				queryDefn.addSort(sortDefn[i]);
			}
		}

		if (filterDefn != null) {
			if (bindingNameFilter != null) {
				for (int i = 0; i < bindingNameFilter.length; i++) {
					queryDefn.addResultSetExpression(bindingNameFilter[i], bindingExprFilter[i]);
				}
			}
			for (int i = 0; i < filterDefn.length; i++) {
				queryDefn.addFilter(filterDefn[i]);
			}
		}

		// add value retrive tansformation
		if (bindingNameRow != null) {
			for (int i = 0; i < bindingNameRow.length; i++) {
				queryDefn.addResultSetExpression(bindingNameRow[i], bindingExprRow[i]);
			}
		}

		return queryDefn;
	}

	public String getOutputStrForFlatfileTest(int expectedLen, IResultSet result, int ColumnCount, String[] columnStr,
			int dateTypeColumnNum) throws Exception {

		StringBuffer sBuffer = new StringBuffer();
		String metaData = "";
		for (int i = 0; i < ColumnCount; i++) {
			metaData += formatStr(columnStr[i], expectedLen);
		}
		sBuffer.append(metaData);
		sBuffer.append("\n");

		while (result.next()) {

			String rowData = "";
			for (int i = 1; i <= ColumnCount; i++) {
				String value;

				if (result.getString(i) != null) {
					// Convert date time according to the format as following
					if (!result.getString(i).equals("DATE") && dateTypeColumnNum == i) {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
						value = sdf.format(result.getDate(i));
					} else {
						value = result.getString(i);
					}
				} else {
					value = "";
				}
				rowData += formatStr(value, expectedLen);
			}
			sBuffer.append(rowData);
			sBuffer.append("\n");
		}
		return new String(sBuffer);
	}

	// expectedlen is the distance between two columns
	public String getOutputStrForGroupTest(int expectedLen, QueryDefinition qd, int groupDefCount, String[] beArray,
			String[] columStr) throws Exception {
		StringBuffer sBuffer = new StringBuffer();

		// execute query
		IResultIterator ri = this.executeQuery(qd);

		String metaData = "";
		for (int i = 0; i < columStr.length; i++) {
			metaData += formatStr(columStr[i], expectedLen);
		}
		sBuffer.append(metaData);
		sBuffer.append("\n");

		int groupCount = groupDefCount;

		while (ri.next()) {
			String rowData = "";
			// group level 1 base = ri.getStartingGroupLevel( )
			// if there is one group, startlevel =2
			int startLevel = ri.getStartingGroupLevel();
			if (startLevel <= groupCount) {
				if (startLevel == 0) {
					startLevel = 1;
				}

				for (int j = 0; j < startLevel - 1; j++) {
					rowData += formatStr("", expectedLen);
				}
				for (int j = startLevel - 1; j < beArray.length; j++) {
					String value;
					if (ri.getValue(beArray[j]) != null) {
						if (ri.getValue(beArray[j]) instanceof Date) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
							value = sdf.format(ri.getValue(beArray[j]));
						} else {
							value = ri.getValue(beArray[j]).toString();
						}
					} else {
						value = "null";
					}

					rowData += formatStr(value, expectedLen);
				}
			}
			// if the content belongs to group1, codes following simply display
			// the content if g2, display the contents of g2
			else {
				for (int j = 0; j < groupCount; j++) {
					rowData += formatStr("", expectedLen);

				}
				for (int j = groupCount; j < beArray.length; j++) {
					String value;
					if (ri.getValue(beArray[j]) instanceof Date) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
						value = sdf.format(ri.getValue(beArray[j]));
					} else {
						value = ri.getValue(beArray[j]).toString();
					}
					rowData += formatStr(value, expectedLen);
				}
			}
			sBuffer.append(rowData);
			sBuffer.append("\n");
		}

		return new String(sBuffer);
	}

	private static String formatStr(String inputStr, int length) {
		if (inputStr == null) {
			return null;
		}

		int inputLen = inputStr.length();
		if (inputLen >= length) {
			return inputStr;
		}

		int appendLen = length - inputLen;
		char[] appendChar = new char[appendLen];
		for (int i = 0; i < appendLen; i++) {
			appendChar[i] = ' ';
		}
		String result = inputStr + new String(appendChar);

		return result;
	}

	// methods from engine case
	protected void copyResource(String src, String tgt, String folder) {

		String className = getFullQualifiedClassName();
		tgt = className + "/" + folder + "/" + tgt;
		className = className.replace('.', '/');

		src = className + "/" + folder + "/" + src;

		System.out.println("src: " + src);
		System.out.println("tgt: " + tgt);
		File parent = new File(tgt).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	protected void copyResource_INPUT(String input_resource, String input) {
		this.copyResource(input_resource, input, INPUT_FOLDER);
	}

	protected void copyResource_GOLDEN(String input_resource, String golden) {
		this.copyResource(input_resource, golden, GOLDEN_FOLDER);
	}

	/**
	 * Remove a given file or directory recursively.
	 *
	 * @param file
	 */
	public void removeFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				removeFile(children[i]);
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println(file.toString() + " can't be removed"); //$NON-NLS-1$
			}
		}
	}

	protected String getFullQualifiedClassName() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);

		return className;
	}

	public void removeResource() {
		String className = getFullQualifiedClassName();
		removeFile(className);
	}

	public void removeFile(String file) {
		removeFile(new File(file));
	}

	protected String genOutputFile(String output) {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator)) {
			tempDir += File.separator;
		}

		String tempDirgetFullQualifiedClassName = tempDir + getFullQualifiedClassName();

		File path = new File(tempDirgetFullQualifiedClassName);

		if (path.exists()) {
			path.deleteOnExit();
		}
		if (!path.exists()) {
			path.mkdir();
		}

		String outputFile = tempDir + getFullQualifiedClassName() // $NON-NLS-1$
				+ "/" + OUTPUT_FOLDER + "/" + output;

		File fullpath = new File(outputFile);

		if (fullpath.exists()) {
			fullpath.delete();
			// if()
		}

		return outputFile;
	}

}
