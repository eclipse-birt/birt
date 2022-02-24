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

package org.eclipse.birt.data.engine.api;

import java.util.ArrayList;

import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for scripted data source/data set
 */

public class ScriptedDSTest extends APITestCase {
	// data set name
	private final String dataSetName = "ScriptedDataSet";

	// test features
	private final static int TEST_NONE = 0;
	private final static int TEST_SORT = 1;
	private final static int TEST_GROUP = 2;
	private final static int TEST_MAXROW = 3;
	private final static int TEST_TOPN_FILTER = 4;

	private boolean ADD_ALIAS = true;

	// aggreation expression
	private ScriptExpression queryExpr = new ScriptExpression("Total.Count( )");
	private String queryName = "_query_count";
	private ScriptExpression groupExpr = new ScriptExpression("Total.Count( )");
	private String groupName = "_group_count";

	// column name
	private String[] scriptColumnNames = new String[] { "NUM", "SQUARE", "STR", "ANY" };
	private int[] scriptColumnTypes = new int[] { DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE, DataType.STRING_TYPE,
			DataType.ANY_TYPE };
	private String[] scriptColumnTypeNames = new String[] { "INTEGER", "DOUBLE", "STRING", "ANY" };
	// column alias name
	private String[] scriptColumnAliasNames = new String[] { "NUM2", "SQUARE2", "STR2", "ANY2" };

	private String[] computedColumnNames = new String[] { "CP1", "CP2" };
	private IScriptExpression[] scriptExprs;

	private IScriptExpression[] aliasScriptExprs;

	private ScriptDataSourceDesign dsource;

	private ScriptDataSetDesign dset;

	@Before
	public void scriptedDSSetUp() throws Exception {
		this.ADD_ALIAS = true;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return null;
	}

	/**
	 * @param dynamicMetadata
	 * @throws Exception
	 */
	private void init(boolean dynamicMetadata) throws Exception {
		// Create script data set and data source
		dsource = new ScriptDataSourceDesign("JUST as place folder");
		dset = newDataSet(null, dynamicMetadata);
		dset.setDataSource(dsource.getName());

		// Add event scripts for data source
		dsource.setBeforeOpenScript("testPrintln(\"@@EVENT:DataSource.beforeOpen\")");
		dsource.setBeforeCloseScript("testPrintln(\"@@EVENT:DataSource.beforeClose\")");
		dsource.setAfterOpenScript("testPrintln(\"@@EVENT:DataSource.afterOpen\")");
		dsource.setAfterCloseScript("testPrintln(\"@@EVENT:DataSource.afterClose\")");
		dsource.setOpenScript("testPrintln(\"@@EVENT:DataSource.open\")");
		dsource.setCloseScript("testPrintln(\"@@EVENT:DataSource.close\")");

		dataEngine.defineDataSource(dsource);
		dataEngine.defineDataSet(dset);

		// Generate script expressions
		scriptExprs = new IScriptExpression[] { new ScriptExpression("dataSetRow." + scriptColumnNames[0]),
				new ScriptExpression("dataSetRow." + scriptColumnNames[1]),
				new ScriptExpression("dataSetRow." + scriptColumnNames[2]),
				new ScriptExpression("dataSetRow." + scriptColumnNames[3]) };

		aliasScriptExprs = new IScriptExpression[] { new ScriptExpression("dataSetRow." + scriptColumnAliasNames[0]),
				new ScriptExpression("dataSetRow." + scriptColumnAliasNames[1]),
				new ScriptExpression("dataSetRow." + scriptColumnAliasNames[2]),
				new ScriptExpression("dataSetRow." + scriptColumnAliasNames[3]) };
	}

	/**
	 * Get test ScriptDataSetDesign
	 * 
	 * @return ScriptDataSetDesign
	 */
	private ScriptDataSetDesign newDataSet(String name, boolean dynamicMetadata) {
		ScriptDataSetDesign dataSet = new ScriptDataSetDesign(name == null ? dataSetName : name);

		// 1: add scripts
		dataSet.setOpenScript("testPrintln(\"@@EVENT:ScriptDataSet.open\");" + "count=11; dset_open=true; count--;");

		dataSet.setCloseScript("testPrintln(\"@@EVENT:ScriptDataSet.close\")");

		dataSet.setFetchScript("if (count==0) {return false; } else "
				+ "{ row.NUM=count; row.SQUARE=count*count; row.STR=\"row#\" + count; " + " row[4]=count<5; "
				+ "--count; return true; }");

		if (dynamicMetadata) {
			String describeScript = new String();
			for (int i = 0; i < scriptColumnNames.length; i++) {
				// Make calls to addDataSetColumn( name, type )
				String callText = "addDataSetColumn(\"" + scriptColumnNames[i] + "\", \"" + scriptColumnTypeNames[i]
						+ "\"); ";
				describeScript += callText;
			}
			describeScript += " return true; ";
			dataSet.setDescribeScript(describeScript);
		}

		dataSet.setBeforeOpenScript("testPrintln(\"@@EVENT:DataSet.beforeOpen\")");
		dataSet.setBeforeCloseScript("testPrintln(\"@@EVENT:DataSet.beforeClose\")");
		dataSet.setAfterOpenScript("testPrintln(\"@@EVENT:DataSet.afterOpen\")");
		dataSet.setAfterCloseScript("testPrintln(\"@@EVENT:DataSet.afterClose\")");

		// 2: add column hints - only in the case of static metadata
		if (!dynamicMetadata) {
			dataSet.getResultSetHints().addAll(getColumnHints());
		}

		IComputedColumn cp1 = new ComputedColumn("CP1", "12345", DataType.ANY_TYPE);
		IComputedColumn cp2 = new ComputedColumn("CP2", "54321", DataType.ANY_TYPE);
		dataSet.addComputedColumn(cp1);
		dataSet.addComputedColumn(cp2);
		return dataSet;
	}

	/**
	 * Get test ColumnHints for data set
	 * 
	 * @return columnHints list
	 */
	private List getColumnHints() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < scriptColumnNames.length; i++) {
			ColumnDefinition col = new ColumnDefinition(scriptColumnNames[i]);
			col.setDataType(scriptColumnTypes[i]);
			col.setAlias(scriptColumnAliasNames[i]);
			list.add(col);
		}
		;
		return list;
	}

	/**
	 * Test getMetaData function of script data set; uses static metadata
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetMetaDataStatic() throws Exception {
		getMetadataTestImpl(false);
	}

	/**
	 * Test getMetaData function of script data set; uses dynamic metadata
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetMetaDataDynamic() throws Exception {
		getMetadataTestImpl(true);
	}

	public void getMetadataTestImpl(boolean dynamicMetadata) throws Exception {
		init(dynamicMetadata);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_SORT);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);

		// test metadata function
		IResultMetaData metaData = qr.getResultMetaData();
		int count = getNoneTempColumCount(metaData);
		for (int i = 0; i < count; i++) {
			if (i < scriptColumnNames.length) {
				assertEquals(metaData.getColumnName(i + 1), "_" + scriptColumnNames[i]);
				assertEquals(metaData.getColumnType(i + 1), scriptColumnTypes[i]);
			} else if (i < scriptColumnNames.length + scriptColumnAliasNames.length)
				assertEquals(metaData.getColumnName(i + 1), "_" + scriptColumnAliasNames[i - scriptColumnNames.length]);
		}
	}

	private int getNoneTempColumCount(IResultMetaData metaData) throws BirtException {
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			// TODO The regex should be refined.
			if (metaData.getColumnName(i).matches("\\Q_{$TEMP\\E.*\\d*\\Q$}_\\E"))
				return i - 1;
		}
		return metaData.getColumnCount();
	}

	/**
	 * Test SetMaxRow function of script data set
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMaxRows() throws Exception {
		init(false);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_MAXROW);

		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator resultIterator = qr.getResultIterator();
		int rowCount = 0;
		// the rows value is supposed to be 81
		while (resultIterator.next()) {
			rowCount++;
		}
		assertEquals(rqDefn.getMaxRows(), rowCount);

	}

	/**
	 * Test includes filter, sort
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetch1() throws Exception {
		fetch1_test_impl(false);
	}

	@Test
	public void testFetch1Dynamic() throws Exception {
		this.ADD_ALIAS = false;
		fetch1_test_impl(true);
	}

	private void fetch1_test_impl(boolean dynamicMetadata) throws Exception {
		init(dynamicMetadata);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_SORT);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator ri = qr.getResultIterator();

		int rowIndex = 0;
		while (ri.next()) {
			String outputStr = "";
			for (int i = 0; i < scriptColumnNames.length; i++) {
				Object value = ri.getValue("_" + scriptColumnNames[i]);
				outputStr += scriptExprs[i].getText().replaceAll("dataSetRow", "row") + " value is:" + value.toString()
						+ "    ";
			}

			rowIndex++;
			testPrintln(outputStr);
		}
		ri.close();
		qr.close();

		// Shutdown data engine to close all data sources; this ensures that
		// the script data source's close event scripts are run
		dataEngine.shutdown();
		checkOutputFile();
	}

	/**
	 * Test fetch with alias
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetchWithAlias() throws Exception {
		init(false);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_GROUP);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator ri = qr.getResultIterator();

		int rowIndex = 0;

		while (ri.next()) {
			String outputStr = "";
			for (int i = 0; i < this.scriptColumnAliasNames.length; i++) {
				Object value = ri.getValue("_" + scriptColumnAliasNames[i]);
				outputStr += aliasScriptExprs[i].getText().replaceAll("dataSetRow", "row") + " value is:"
						+ value.toString() + "    ";
			}

			rowIndex++;
			testPrintln(outputStr);
		}
		ri.close();
		qr.close();

		// Shutdown data engine to close all data sources; this ensures that
		// the script data source's close event scripts will run
		dataEngine.shutdown();
		checkOutputFile();
	}

	/**
	 * Test includes filter, group
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetch2() throws Exception {
		init(false);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_GROUP);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator ri = qr.getResultIterator();

		int groupLevel = -1;
		int rowIndex = 0;

		while (ri.next()) {
			if (groupLevel == -1) {
				testPrintln("Query row count:" + ri.getValue(queryName));
			}
			if (groupLevel < ri.getEndingGroupLevel()) {
				testPrintln("");
				testPrintln("Group row count:" + ri.getValue(groupName));
			}
			groupLevel = ri.getEndingGroupLevel();

			String outputStr = "";
			for (int i = 0; i < scriptColumnNames.length; i++) {
				Object value = ri.getValue("_" + scriptColumnNames[i]);
				outputStr += scriptExprs[i].getText().replaceAll("dataSetRow", "row") + " value is:" + value.toString()
						+ "    ";
			}

			rowIndex++;
			testPrintln(outputStr);
		}
		ri.close();
		qr.close();

		// Shutdown data engine to close all data sources; this ensures that
		// the script data source's close event scripts will run
		dataEngine.shutdown();
		checkOutputFile();
	}

	/**
	 * Test TOP N filter
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFetch3() throws Exception {
		init(false);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_TOPN_FILTER);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator ri = qr.getResultIterator();

		int rowIndex = 0;

		while (ri.next()) {
			String outputStr = "";
			for (int i = 0; i < scriptColumnNames.length; i++) {
				Object value = ri.getValue("_" + scriptColumnNames[i]);
				outputStr += scriptExprs[i].getText().replaceAll("dataSetRow", "row") + " value is:" + value.toString()
						+ "    ";
			}

			rowIndex++;
			testPrintln(outputStr);
		}
		ri.close();
		qr.close();

		// Shutdown data engine to close all data sources; this ensures that
		// the script data source's close event scripts will run
		dataEngine.shutdown();
		checkOutputFile();
	}

	/**
	 * @throws Exception
	 * 
	 *
	 */
	@Test
	public void testFetchLimit1() throws Exception {
		this.testFetchLimit(-1);
	}

	/**
	 * @throws Exception
	 * 
	 *
	 */
	@Test
	public void testFetchLimit2() throws Exception {
		this.testFetchLimit(0);
	}

	/**
	 * @throws Exception
	 * 
	 *
	 */
	@Test
	public void testFetchLimit3() throws Exception {
		this.testFetchLimit(2);
	}

	/**
	 * Test setting of RowFetchLimit.
	 * 
	 * @throws Exception
	 */
	private void testFetchLimit(int limit) throws Exception {
		init(false);
		dset.setRowFetchLimit(limit);
		IQueryDefinition rqDefn = getReportQueryDefn(TEST_TOPN_FILTER);
		IPreparedQuery pq = dataEngine.prepare(rqDefn);
		IQueryResults qr = pq.execute(jsScope);
		IResultIterator ri = qr.getResultIterator();

		int rowIndex = 0;

		while (ri.next()) {
			String outputStr = "";
			for (int i = 0; i < scriptColumnNames.length; i++) {
				Object value = ri.getValue("_" + scriptColumnNames[i]);
				outputStr += scriptExprs[i].getText().replaceAll("dataSetRow", "row") + " value is:" + value.toString()
						+ "    ";
			}

			rowIndex++;
			testPrintln(outputStr);
		}
		ri.close();
		qr.close();

		// Shutdown data engine to close all data sources; this ensures that
		// the script data source's close event scripts will run
		dataEngine.shutdown();
		checkOutputFile();
	}

	/**
	 * Test feature of import packages from java.
	 */
	@Test
	public void testImportPackage() throws Exception {
		// Test a SQL with duplicate column name (quite common with join data
		// sets)
		init(false);
		IQueryDefinition queryDefn = getReportQueryDefn(TEST_NONE);
		try {
			dsource.setBeforeOpenScript("importPackage(Packages.javax.swing.tree);");
			dset.setBeforeOpenScript("dmtn = new DefaultMutableTreeNode();");
			IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
			preparedQuery.execute(null);
		} catch (Exception e) {
			fail("Should not arrive here");
		}
	}

	/**
	 * Test feature of Global variable defined in datasource and used in datasets
	 */
	@Test
	public void testGlobalVariable() throws Exception {
		init(false);
		try {
			dsource.setBeforeOpenScript("i = 100;");
			iterateResultSet(dset);
		} catch (Exception e) {
			fail("Should not arrive here");
		}
	}

	/**
	 * Test feature of Global variable defined in datasource and used in datasets
	 */
	@Test
	public void testMultipleDataSource() throws Exception {
		init(false);
		ScriptDataSetDesign dset1 = newDataSet("dset1", false);
		dset1.setDataSource(dsource.getName());
		dataEngine.defineDataSet(dset1);

		ScriptDataSourceDesign dsource1 = new ScriptDataSourceDesign("datasource1");
		ScriptDataSetDesign dset2 = newDataSet("dset2", false);
		dset2.setDataSource(dsource1.getName());
		dataEngine.defineDataSource(dsource1);
		dataEngine.defineDataSet(dset2);

		try {
			dsource.setBeforeOpenScript("i = 100;");
			iterateResultSet(dset2);
			fail("Sould not arrive here");
		} catch (DataException e) {
		}
		try {
			ScriptDataSetDesign dset3 = newDataSet("dset3", false);
			dset3.setDataSource(dsource1.getName());
			dataEngine.defineDataSource(dsource1);
			dataEngine.defineDataSet(dset3);
			dsource1.setBeforeOpenScript("i = 100;");
			iterateResultSet(dset3);
		} catch (DataException e) {
			fail("Sould not arrive here");
		}
	}

	/**
	 * Tests get/set/manipulate date/time values. Verifies fixes to bugzilla 122860,
	 * 123153
	 */
	@Test
	public void testDateTimeValues() throws Exception {
		// Create script data set and data source
		dsource = new ScriptDataSourceDesign("data source");
		dset = new ScriptDataSetDesign(" data set");
		dset.setDataSource(dsource.getName());

		// 5 columns: 2 scripted and 3 computed
		ColumnDefinition c = new ColumnDefinition("d1");
		c.setDataType(DataType.DATE_TYPE);
		dset.addResultSetHint(c);

		c = new ColumnDefinition("d2");
		c.setDataType(DataType.DATE_TYPE);
		dset.addResultSetHint(c);

		dset.addComputedColumn(new ComputedColumn("CP1", "row.d1.getUTCFullYear()", DataType.INTEGER_TYPE));
		dset.addComputedColumn(new ComputedColumn("CP2", "row.d2.getUTCFullYear()", DataType.INTEGER_TYPE));
		dset.addComputedColumn(new ComputedColumn("CP3", "new Date(0)", DataType.DATE_TYPE));

		dset.setOpenScript("count=0;");
		dset.setFetchScript(" if (++count > 1) return false;  " + "row.d1 = new Date(0);"
				+ "row.d2 = new Packages.java.util.Date(0); " + "return true;");

		dataEngine.defineDataSource(dsource);
		dataEngine.defineDataSet(dset);

		QueryDefinition rqDefn = new QueryDefinition();
		rqDefn.setDataSetName(dset.getName());
		String e1Name = "_e1";
		String e2Name = "_e2";
		String e3Name = "_e3";
		String e4Name = "_e4";
		String e5Name = "_e5";

		ScriptExpression e1 = new ScriptExpression("dataSetRow.d1");
		ScriptExpression e2 = new ScriptExpression("dataSetRow.d2");
		ScriptExpression e3 = new ScriptExpression("dataSetRow.CP1");
		ScriptExpression e4 = new ScriptExpression("dataSetRow.CP2");
		ScriptExpression e5 = new ScriptExpression("dataSetRow.CP3");

//		rqDefn.getRowExpressions( ).add( e1 );
//		rqDefn.getRowExpressions( ).add( e2 );
//		rqDefn.getRowExpressions( ).add( e3 );
//		rqDefn.getRowExpressions( ).add( e4 );
//		rqDefn.getRowExpressions( ).add( e5 );
		rqDefn.addResultSetExpression(e1Name, e1);
		rqDefn.addResultSetExpression(e2Name, e2);
		rqDefn.addResultSetExpression(e3Name, e3);
		rqDefn.addResultSetExpression(e4Name, e4);
		rqDefn.addResultSetExpression(e5Name, e5);

		IPreparedQuery preparedQuery = dataEngine.prepare(rqDefn);
		IQueryResults result = preparedQuery.execute(null);
		IResultIterator resultIt = result.getResultIterator();
		assertTrue(resultIt.next());

		assertEquals(resultIt.getDate(e1Name).getTime(), 0);
		assertEquals(resultIt.getDate(e2Name).getTime(), 0);
		assertEquals(resultIt.getInteger(e3Name).intValue(), 1970);
		assertEquals(resultIt.getInteger(e4Name).intValue(), 1970);
		assertEquals(resultIt.getDate(e5Name).getTime(), 0);

		assertFalse(resultIt.next());

		resultIt.close();
		result.close();

	}

	/**
	 * @param dataset
	 * @param preparedQuery
	 * @param result
	 * @param resultIt
	 * @throws BirtException
	 */
	private void iterateResultSet(ScriptDataSetDesign dataset) throws BirtException {
		dataset.setFetchScript("i++; if(i > 150) return false; else {row.NUM = i;return true;}");
		IPreparedQuery preparedQuery = dataEngine.prepare(getReportQueryDefn(TEST_NONE, dataset.getName()));
		IQueryResults result = preparedQuery.execute(null);
		IResultIterator resultIt = result.getResultIterator();
		for (int i = 0; i < 50; i++) {
			assertTrue(resultIt.next());
		}
		resultIt.close();
		result.close();
	}

	private IQueryDefinition getReportQueryDefn(int testUnit) throws BirtException {
		return getReportQueryDefn(testUnit, null);
	}

	/**
	 * Get test ReportQueryDefn
	 * 
	 * @return ReportQueryDefn
	 * @throws BirtException
	 */
	private IQueryDefinition getReportQueryDefn(int testUnit, String dsetName) throws BirtException {
		QueryDefinition rqDefn = new QueryDefinition();

		// add expressions
		rqDefn.setDataSetName(dsetName == null ? dataSetName : dsetName);
		for (int i = 0; i < this.scriptExprs.length; i++) {
			((ScriptExpression) scriptExprs[i]).setDataType(scriptColumnTypes[i]);
			rqDefn.addResultSetExpression("_" + scriptColumnNames[i], scriptExprs[i]);
		}

		if (this.ADD_ALIAS) {
			for (int i = 0; i < this.aliasScriptExprs.length; i++) {
				rqDefn.addResultSetExpression("_" + scriptColumnAliasNames[i], aliasScriptExprs[i]);
			}
		}

		rqDefn.addResultSetExpression("_groupCol0", new ScriptExpression("dataSetRow.NUM"));
		rqDefn.addResultSetExpression("_sortCol0", new ScriptExpression("dataSetRow.NUM"));

//		rqDefn.getRowExpressions( ).addAll( Arrays.asList( this.scriptExprs ) );
//		rqDefn.getRowExpressions( ).addAll( Arrays.asList( this.aliasScriptExprs ) );

		// add filter
		String[] filterStr = new String[] { "dataSetRow.NUM > 2" };

		rqDefn.addResultSetExpression("_filterCol0", new ScriptExpression(filterStr[0]));

		for (int i = 0; i < filterStr.length; i++) {
			rqDefn.addFilter(new FilterDefinition(new ScriptExpression("row._filterCol0")));
		}

		// add sort
		if (testUnit == TEST_SORT) {
			SortDefinition sort = new SortDefinition();
			sort.setExpression("row._sortCol0");
			sort.setSortDirection(ISortDefinition.SORT_ASC);
			rqDefn.addSort(sort);
		}

		// add group
		if (testUnit == TEST_GROUP) {
			SortDefinition sort = new SortDefinition();
			sort.setExpression("row._sortCol0");
			sort.setSortDirection(ISortDefinition.SORT_ASC);
			rqDefn.addSort(sort);
			GroupDefinition group = new GroupDefinition("group1");
			group.setKeyExpression("row._groupCol0");
			group.setInterval(IGroupDefinition.NUMERIC_INTERVAL);
			group.setIntervalStart(new Double(3.0));
			group.setIntervalRange(3.0);
			group.setIntervalStart(new Integer(3));
			group.setSortDirection(IGroupDefinition.SORT_ASC);

			// add aggregation
			groupExpr.setGroupName("group1");
			rqDefn.addResultSetExpression(groupName, groupExpr);
			rqDefn.addGroup(group);
		}

		// add MaxRow
		if (testUnit == TEST_MAXROW) {

			IQueryDefinition queryDefinition = getReportQueryDefn(TEST_NONE);
			IPreparedQuery pq = dataEngine.prepare(queryDefinition);
			IQueryResults qr = pq.execute(jsScope);
			IResultIterator resultIterator = qr.getResultIterator();
			int realRows = 0;
			while (resultIterator.next()) {
				realRows++;
			}
			rqDefn.setMaxRows(realRows / 2);
		}

		// add topN filter
		if (testUnit == TEST_TOPN_FILTER) {
			ConditionalExpression conditionExpr = new ConditionalExpression("row._filterCol0",
					ConditionalExpression.OP_TOP_N, "5");
			FilterDefinition exprFilter = new FilterDefinition(conditionExpr);
			rqDefn.addFilter(exprFilter);
		}

		rqDefn.addResultSetExpression(queryName, queryExpr);
		return rqDefn;
	}

}
