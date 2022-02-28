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

package org.eclipse.birt.data.engine.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import testutil.ConfigText;

/**
 * Test cache feature for oda result set.
 */
public class DataSetCacheTest extends APITestCase {

	/** check value */
	private List expectedValue;

	private IBaseExpression[] rowBeArray;
	private IBaseExpression[] totalBeArray;

	private String[] bindingNameRow;
	private String[] bindingExprRow;

	Map appContextMap = new HashMap();
	ScriptableObject scope;
	private DataEngineImpl myDataEngine;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void datasetCacheSetUp() throws Exception {

		Context context = Context.enter();
		scope = context.initStandardObjects();
		Context.exit();

		expectedValue = new ArrayList();
		appContextMap.put(DataEngine.DATASET_CACHE_OPTION, "true");
		myDataEngine = newDataEngine();
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void datasetCacheTearDown() throws Exception {
		myDataEngine.clearCache(this.dataSource, this.dataSet);
		myDataEngine.shutdown();
	}

	@Test
	public void testCacheIsRealDataSetLevel() throws BirtException {
		this.dataSource.setBeforeOpenScript("i=0");
		this.dataSet.addComputedColumn(new ComputedColumn("cc1", "++i", DataType.INTEGER_TYPE));
		this.dataSet.addComputedColumn(new ComputedColumn("cc2", (IScriptExpression) null, DataType.INTEGER_TYPE,
				"COUNT", null, new ArrayList()));
		this.dataSet.addFilter(new FilterDefinition(new ScriptExpression("row[\"cc1\"] <= 7")));
		this.dataSet.setCacheRowCount(6);

		myDataEngine = newDataEngine();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());
		QueryDefinition qd = this.newReportQuery(true);

		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		IResultIterator itr = qr.getResultIterator();
		int i = 1;
		while (itr.next()) {
			// "cc1" values sequence is {1, 2, 3, 4, 5, 6}
			assertEquals(i, itr.getInteger("cc1").intValue());

			// row count is 7
			assertEquals(7, itr.getInteger("cc2").intValue());
			i++;
		}
		itr.close();
		assertEquals(7, i);

		qr.close();

		assertTrue(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		// the data set result set for this query is loaded from cache
		qd.addFilter(new FilterDefinition(new ScriptExpression("row[\"cc1\"] >= 5 ")));
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"cc1\"] ");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		qd.addSort(sd);

		qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		itr = qr.getResultIterator();
		i = 6;
		while (itr.next()) {
			// "cc1" values sequence is {6, 5}
			assertEquals(i, itr.getInteger("cc1").intValue());

			// "cc2" value is still 7
			assertEquals(7, itr.getInteger("cc2").intValue());
			i--;
		}
		itr.close();

		// row count is 2
		assertEquals(4, i);
		qr.close();

		assertTrue(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		// the data set result set for this query is loaded from cache
		qd.getFilters().clear();
		qd.getSorts().clear();

		qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		itr = qr.getResultIterator();
		i = 1;
		while (itr.next()) {
			// "cc1" values sequence is {1, 2, 3, 4, 5, 6}
			assertEquals(i, itr.getInteger("cc1").intValue());

			// "cc2" value is still 7
			assertEquals(7, itr.getInteger("cc2").intValue());
			i++;
		}
		itr.close();
		assertEquals(7, i);

		qr.close();

		getDataSetCacheManager(myDataEngine).resetForTest();
		myDataEngine.shutdown();

	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * Test feature of whether cache will be used
	 *
	 * @throws BirtException
	 */
	@Test
	public void testUseCache() throws BirtException {
		this.dataSet.setCacheRowCount(4);
		myDataEngine = newDataEngine();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		QueryDefinition qd = this.newReportQuery();
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);
		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		qr.getResultIterator().next();
		qr.close();

		assertTrue(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());
		getDataSetCacheManager(myDataEngine).resetForTest();
		myDataEngine.shutdown();
	}

	/**
	 * Test feature of cancel cache. This test case may fail in some cases.
	 *
	 * @throws BirtException
	 */
//	public void testCancelCache( ) throws BirtException
//	{
//		this.dataSet.setCacheRowCount( 4 );
//		myDataEngine = newDataEngine( );
//
//		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
//		assertFalse( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
//
//		QueryDefinition qd = this.newReportQuery( );
//		rowBeArray = getRowExpr( );
//		totalBeArray = getAggrExpr( );
//		bindingNameRow = getRowExprName( );
//		bindingExprRow = getAggrExprName( );
//
//		prepareExprNameAndQuery( rowBeArray,
//				bindingNameRow,
//				totalBeArray,
//				bindingExprRow,
//				qd );
//		IQueryResults qr = myDataEngine.prepare( qd, appContextMap )
//				.execute( null );
//		CancelCacheThread cancelThread = new CancelCacheThread( qr );
//		cancelThread.start( );
//		IResultIterator iterator = qr.getResultIterator( );
//		if ( iterator != null )
//			iterator.next( );
//		qr.close( );
//
//		assertFalse( getDataSetCacheManager( myDataEngine ).doesLoadFromCache( ) );
//		assertTrue( getDataSetCacheManager( myDataEngine ).doesSaveToCache( ) );
//		getDataSetCacheManager( myDataEngine ).resetForTest( );
//		myDataEngine.shutdown( );
//	}

	/**
	 * Test feature of whether cache will be used
	 *
	 * @throws BirtException
	 */
	@Test
	public void testUseCache1() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(100);
		myDataEngine = newDataEngine();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		executeQuery(myDataEngine);
	}

	private void executeQuery(DataEngineImpl myDataEngine) throws BirtException, DataException, Exception, IOException {
		QueryDefinition qd = this.newReportQuery();
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);
		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		qr.getResultIterator().next();
		qr.close();

		// add a group
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_CITY");
		groupDefn[0].setInterval(IGroupDefinition.STRING_PREFIX_INTERVAL);
		groupDefn[0].setIntervalRange(1);
		qd.addResultSetExpression(bindingNameGroup[0], bindingExprGroup[0]);

		qd.addGroup(groupDefn[0]);

		String[] columnStr = { "Country", "City", "date", "amount" };

		String outputStr = getOutputStrForGroupTest(30, qd, 0, bindingNameRow, columnStr);

		testPrint(outputStr);
		this.checkOutputFile();
		getDataSetCacheManager(myDataEngine).resetForTest();
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testUseAppContextCacheRowLimit1() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(100);
		DataEngineContext dec = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null);
		dec.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 100);
		dec.setTmpdir(this.getTempDir());
		myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(dec);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		// The setting of data set cache row limit in app context will take priority
		// over all other
		// cache settings.
		this.appContextMap.put(DataEngine.DATA_SET_CACHE_ROW_LIMIT, new Integer(0));
		this.testUseCache1();
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testUseAppContextCacheRowLimit2() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(2);
		DataEngineContext dec = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null);
		dec.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 3);
		dec.setTmpdir(this.getTempDir());
		DataEngineImpl myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(dec);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		// The setting of data set cache row limit in app context will take priority
		// over all other
		// cache settings.
		this.appContextMap.put(DataEngine.DATA_SET_CACHE_ROW_LIMIT, new Integer(-1));
		this.testUseCache1();
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testUseAppContextCacheRowLimit3() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(100);
		this.dataSet.setBeforeOpenScript("a = 0;");
		this.dataSet.setOnFetchScript("if ( a == 0  ) a = 1;");
		DataEngineContext dec = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null);
		dec.setTmpdir(this.getTempDir());
		myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(dec);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		// The setting of data set cache row limit in app context will take priority
		// over all other
		// cache settings.
		this.appContextMap.put(DataEngine.DATA_SET_CACHE_ROW_LIMIT, new Integer(4));
		this.testUseCache1();
	}

	/**
	 * Test acquire saved cache result meta
	 *
	 */
	@Test
	public void testUseCachedMeta1() throws BirtException {
		this.appContextMap.put(DataEngine.DATA_SET_CACHE_ROW_LIMIT, new Integer(1));
		this.genCache();
		myDataEngine = newDataEngine();
		IResultMetaData meta = myDataEngine.getCachedDataSetMetaData(dataSource, dataSet);
		assertTrue(meta != null);
		assertTrue(meta.getColumnCount() == 6);
	}

	/**
	 * Test acqurire inexist save cache result meta
	 *
	 */
	@Test
	public void testUseCachedMeta2() throws BirtException {
		this.appContextMap.put(DataEngine.DATA_SET_CACHE_ROW_LIMIT, new Integer(0));
		this.genCache();
		myDataEngine = newDataEngine();
		IResultMetaData meta = myDataEngine.getCachedDataSetMetaData(dataSource, dataSet);
		assertTrue(meta == null);
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testUseAppContextMemoryCache1() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(7);
		DataEngineContext dec = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null);
		dec.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 100);
		dec.setTmpdir(this.getTempDir());
		myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(dec);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		// The setting of data set cache row limit in app context will take priority
		// over all other
		// cache settings.
		this.appContextMap.put(DataEngine.MEMORY_DATA_SET_CACHE, new Integer(7));
		this.testUseCache1();
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testUseAppContextMemoryCache2() throws BirtException, Exception {
		this.dataSet.setCacheRowCount(2);
		DataEngineContext dec = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null);
		dec.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 3);
		dec.setTmpdir(this.getTempDir());
		myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(dec);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		// The setting of data set cache row limit in app context will take priority
		// over all other
		// cache settings.
		this.appContextMap.put(DataEngine.MEMORY_DATA_SET_CACHE, new Integer(3));
		this.testUseCache1();
	}

	/**
	 * Test acqurire save cache result meta
	 *
	 */
	@Test
	public void testUseMemoryCachedMeta1() throws BirtException {
		this.appContextMap.put(DataEngine.MEMORY_DATA_SET_CACHE, new Integer(1));
		this.genCache();
		myDataEngine = newDataEngine();
		IResultMetaData meta = myDataEngine.getCachedDataSetMetaData(dataSource, dataSet);
		assertTrue(meta != null);
		assertTrue(meta.getColumnCount() == 6);
	}

	/**
	 * Test acqurire inexist save cache result meta
	 *
	 */
	@Test
	public void testUseMemoryCachedMeta2() throws BirtException {
		this.appContextMap.put(DataEngine.MEMORY_DATA_SET_CACHE, new Integer(0));
		myDataEngine = newDataEngine();
		IResultIterator ri = getResultIterator1(myDataEngine);

		while (ri.next()) {
			for (int i = 0; i < bindingNameRow.length; i++) {
				expectedValue.add(ri.getValue(bindingNameRow[i]));
			}

			for (int i = 0; i < totalBeArray.length; i++) {
				expectedValue.add(ri.getValue(bindingExprRow[i]));
			}
		}

		ri.close();

		IResultMetaData meta = myDataEngine.getCachedDataSetMetaData(dataSource, dataSet);
		assertTrue(meta == null);
	}

	/**
	 * Test feature of whether cache will be used. The populated computed name
	 * contains blank space and quotes.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testUseCache3() throws BirtException {
		this.dataSet.setCacheRowCount(4);

		DataEngineImpl myDataEngine = newDataEngine();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		QueryDefinition qd = this.newReportQuery();
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		String[] ccName = { "col0 col1", "\"col0+col1\"" };
		String[] ccExpr = { "row.AMOUNT", "row.AMOUNT*2" };

		for (int i = 0; i < ccName.length; i++) {
			ComputedColumn computedColumn = new ComputedColumn(ccName[i], ccExpr[i], DataType.DECIMAL_TYPE);
			((BaseDataSetDesign) this.dataSet).addComputedColumn(computedColumn);
		}

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);
		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		qr.getResultIterator().next();
		qr.close();
		myDataEngine.shutdown();

		assertTrue(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());
		getDataSetCacheManager(myDataEngine).resetForTest();
	}

	/**
	 * Return query result
	 *
	 * @param expectedLen
	 * @param qd
	 * @param gdArray
	 * @param beArray
	 * @param columStr
	 * @return query string output
	 * @throws Exception
	 */
	private String getOutputStrForGroupTest(int expectedLen, QueryDefinition qd, int groupDefCount, String[] beArray,
			String[] columStr) throws Exception {
		StringBuffer sBuffer = new StringBuffer();

		// execute query
		IResultIterator ri = newDataEngine().prepare(qd, appContextMap).execute(null).getResultIterator();

		String metaData = "";
		for (int i = 0; i < columStr.length; i++) {
			metaData += formatStr(columStr[i], expectedLen);
		}
		sBuffer.append(metaData);
		sBuffer.append("\n");

		int groupCount = groupDefCount;
		while (ri.next()) {
			String rowData = "";

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
						value = ri.getValue(beArray[j]).toString();
					} else {
						value = "null";
					}

					rowData += formatStr(value, expectedLen);
				}
			} else {
				for (int j = 0; j < groupCount; j++) {
					rowData += formatStr("", expectedLen);

				}
				for (int j = groupCount; j < beArray.length; j++) {
					String value = ri.getValue(beArray[j]).toString();
					rowData += formatStr(value, expectedLen);
				}
			}
			sBuffer.append(rowData);
			sBuffer.append("\n");
		}

		return new String(sBuffer);
	}

	/**
	 * Format string to specified length, if the length of input string is larger
	 * than expected length, then input string will be directly return without any
	 * format.
	 *
	 * @param str    needs to be formatted string
	 * @param length expected length
	 * @return formatted string
	 */
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

	/**
	 * Test the feature of clear cache
	 *
	 * @throws BirtException
	 */
	@Test
	public void testClearCache() throws BirtException {
		this.dataSet.setCacheRowCount(4);

		DataEngineImpl myDataEngine = newDataEngine();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		QueryDefinition qd = newReportQuery();
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);
		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		qr.getResultIterator().next();
		qr.close();
		myDataEngine.shutdown();

		assertTrue(getDataSetCacheManager(myDataEngine).doesLoadFromCache());

		myDataEngine.clearCache(this.dataSource, this.dataSet);
		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
	}

	/**
	 * Test the feature of enable cache
	 *
	 * @throws Exception
	 */
	@Test
	public void testEnableCache() throws Exception {
		DataEngineImpl myDataEngine = newDataEngine();
		getDataSetCacheManager(myDataEngine).resetForTest();

		this.dataSet.setCacheRowCount(4);

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());

		QueryDefinition qd = newReportQuery();
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);

		IQueryResults qr = myDataEngine.prepare(qd).execute(null);
		qr.getResultIterator().next();
		qr.close();
		myDataEngine.shutdown();

		assertFalse(getDataSetCacheManager(myDataEngine).doesLoadFromCache());
		assertFalse(getDataSetCacheManager(myDataEngine).doesSaveToCache());
	}

	/**
	 * Test the feature of always cache.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testAlwaysCache() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 4);
		context.setTmpdir(this.getTempDir());
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine(context);
		myDataEngine2.defineDataSource(this.dataSource);
		myDataEngine2.defineDataSet(this.dataSet);
		QueryDefinition qd = newReportQuery();
		IQueryResults qr = myDataEngine2.prepare(qd).execute(null);

		assertFalse(getDataSetCacheManager(myDataEngine2).doesLoadFromCache());

		qr.getResultIterator();

		assertTrue(getDataSetCacheManager(myDataEngine2).doesLoadFromCache());

		qr.close();
		myDataEngine2.shutdown();
	}

	/**
	 * Test the feature of disable cache.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDisableCache() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setCacheOption(DataEngineContext.CACHE_USE_DISABLE, 4);
		context.setTmpdir(this.getTempDir());
		DataEngineImpl myDataEngine2 = (DataEngineImpl) DataEngine.newDataEngine(context);
		myDataEngine2.defineDataSource(this.dataSource);
		myDataEngine2.defineDataSet(this.dataSet);
		QueryDefinition qd = newReportQuery();
		IQueryResults qr = myDataEngine2.prepare(qd).execute(null);

		assertFalse(getDataSetCacheManager(myDataEngine2).doesLoadFromCache());

		qr.getResultIterator();

		assertFalse(getDataSetCacheManager(myDataEngine2).doesLoadFromCache());

		qr.close();
		myDataEngine2.shutdown();
	}

	/**
	 * Test the feature of cache. Check the data in between the cache use is corret.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testOdaCache() throws BirtException {
		genCache();
		useCache();
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testSubqueryCache() throws BirtException {
		genCache3();
		useCache3();
	}

	/**
	 * @throws BirtException
	 */
	private void genCache() throws BirtException {
		this.dataSet.setCacheRowCount(4);

		myDataEngine = newDataEngine();
		IResultIterator ri = getResultIterator1(myDataEngine);

		while (ri.next()) {
			for (int i = 0; i < bindingNameRow.length; i++) {
				expectedValue.add(ri.getValue(bindingNameRow[i]));
			}

			for (int i = 0; i < totalBeArray.length; i++) {
				expectedValue.add(ri.getValue(bindingExprRow[i]));
			}
		}

		ri.close();
		myDataEngine.shutdown();
	}

	/**
	 * @throws BirtException
	 */
	private void genCache3() throws BirtException {
		this.dataSet.setCacheRowCount(4);

		myDataEngine = newDataEngine();
		IResultIterator parentRi = getResultIterator1(myDataEngine);
		parentRi.next();
		IResultIterator ri = parentRi.getSecondaryIterator("IAMTEST", scope);

		while (ri.next()) {
			expectedValue.add(ri.getValue("COL1"));
		}
		assertEquals(expectedValue.size(), 1);
		ri.close();
		parentRi.close();
		myDataEngine.shutdown();
	}

	/**
	 * @throws BirtException
	 */
	private void useCache() throws BirtException {
		myDataEngine = newDataEngine();
		IResultIterator ri = getResultIterator1(myDataEngine);
		checkResult(ri);

		ri.close();
		myDataEngine.shutdown();
	}

	/**
	 * @throws BirtException
	 */
	private void useCache3() throws BirtException {
		myDataEngine = newDataEngine();
		IResultIterator parentRi = getResultIterator1(myDataEngine);
		parentRi.next();
		IResultIterator ri = parentRi.getSecondaryIterator("IAMTEST", scope);
		Iterator it = this.expectedValue.iterator();

		while (ri.next()) {
			String str = "";

			Object ob1 = it.next();
			Object ob2 = ri.getValue("COL1");
			assertEquals(ob1, ob2);
			str += " " + ob2.toString();
			System.out.println("row result set: " + str);
		}
		ri.close();
		parentRi.close();
		myDataEngine.shutdown();
	}

	/**
	 * @param myDataEngine
	 * @return
	 * @throws BirtException
	 */
	private IResultIterator getResultIterator1(DataEngine myDataEngine) throws BirtException {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.CITY");
		GroupDefinition[] groupDefn = { new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_COUNTRY");
		groupDefn[1].setKeyExpression("row.GROUP_CITY");

		QueryDefinition qd = newReportQuery();
		// add transformation definition
		if (groupDefn != null) {
			if (bindingNameGroup != null) {
				for (int i = 0; i < bindingNameGroup.length; i++) {
					qd.addResultSetExpression(bindingNameGroup[i], bindingExprGroup[i]);
				}
			}
			for (int i = 0; i < groupDefn.length; i++) {
				qd.addGroup(groupDefn[i]);
			}
		}

		// prepare
		rowBeArray = getRowExpr();
		totalBeArray = getAggrExpr();
		bindingNameRow = getRowExprName();
		bindingExprRow = getAggrExprName();

		prepareExprNameAndQuery(rowBeArray, bindingNameRow, totalBeArray, bindingExprRow, qd);

		SubqueryDefinition subqueryDefn = new SubqueryDefinition("IAMTEST", qd);
		subqueryDefn.addResultSetExpression("COL1", new ScriptExpression("dataSetRow.CITY"));
		subqueryDefn.setApplyOnGroupFlag(false);
		qd.addSubquery(subqueryDefn);
		// generation
		IQueryResults qr = myDataEngine.prepare(qd, appContextMap).execute(null);
		assertTrue(qr.getResultMetaData() != null);

		return qr.getResultIterator();
	}

	/**
	 * @throws BirtException
	 *
	 */
	@Test
	public void testScriptedCache() throws BirtException {
		genCache2();
		useCache2();
	}

	/**
	 * @throws BirtException
	 */
	private void genCache2() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 4);
		context.setTmpdir(this.getTempDir());
		DataEngine myDataEngine2 = DataEngine.newDataEngine(context);
		IResultIterator ri = getResultIterator2(myDataEngine2);
		while (ri.next()) {
			for (int i = 0; i < bindingNameRow.length; i++) {
				expectedValue.add(ri.getValue(bindingNameRow[i]));
			}
		}
		ri.close();
		myDataEngine2.shutdown();
	}

	/**
	 * @throws BirtException
	 */
	private void useCache2() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setCacheOption(DataEngineContext.CACHE_USE_ALWAYS, 4);
		context.setTmpdir(this.getTempDir());
		DataEngine myDataEngine2 = DataEngine.newDataEngine(context);
		IResultIterator ri = getResultIterator2(myDataEngine2);

		checkResult(ri);

		ri.close();
		myDataEngine2.shutdown();
	}

	/**
	 *
	 * @param myDataEngine
	 * @return
	 * @throws BirtException
	 */
	private IResultIterator getResultIterator2(DataEngine myDataEngine2) throws BirtException {
		ScriptDataSourceDesign odaDataSource = new ScriptDataSourceDesign("JUST as place folder");

		ScriptDataSetDesign odaDataSet = new ScriptDataSetDesign("ScriptedDataSet");
		odaDataSet.setDataSource(odaDataSource.getName());
		odaDataSet.setOpenScript("count=100;");
		odaDataSet.setFetchScript("if (count==0) " + "{" + "return false; " + "} " + "else " + "{ "
				+ "dataSetRow.NUM=count; " + "dataSetRow.SQUARE=count*count; " + "dataSetRow.STR=\"row#\" + count; "
				+ "--count; " + "return true; " + "}");

		// set column defintion for data set
		String[] scriptColumnNames = { "NUM", "SQUARE", "STR" };
		int[] scriptColumnTypes = { DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE, DataType.STRING_TYPE };
		for (int i = 0; i < scriptColumnNames.length; i++) {
			ColumnDefinition colInfo = new ColumnDefinition(scriptColumnNames[i]);
			colInfo.setDataType(scriptColumnTypes[i]);
			odaDataSet.getResultSetHints().add(colInfo);
		}

		QueryDefinition queryDefinition = this.newReportQuery();
		queryDefinition.setDataSetName(odaDataSet.getName());

		bindingNameRow = new String[3];
		bindingNameRow[0] = "ROW_NUM";
		bindingNameRow[1] = "ROW_SQUARE";
		bindingNameRow[2] = "ROW_STR";
		IBaseExpression[] expressionArray = new IBaseExpression[3];
		ScriptExpression expr = new ScriptExpression("dataSetRow.NUM");
		expressionArray[0] = expr;
		expr = new ScriptExpression("dataSetRow.SQUARE");
		expressionArray[1] = expr;
		expr = new ScriptExpression("dataSetRow.STR");
		expressionArray[2] = expr;

		for (int i = 0; i < bindingNameRow.length; i++) {
			queryDefinition.addResultSetExpression(bindingNameRow[i], expressionArray[i]);
		}

		rowBeArray = expressionArray;

		myDataEngine2.defineDataSource(odaDataSource);
		myDataEngine2.defineDataSet(odaDataSet);

		IQueryResults qr = myDataEngine2.prepare(queryDefinition).execute(null);

		return qr.getResultIterator();
	}

	/**
	 * @return
	 * @throws BirtException
	 */
	private DataEngineImpl newDataEngine() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setTmpdir(this.getTempDir());
		DataEngineImpl myDataEngine = (DataEngineImpl) DataEngine.newDataEngine(context);

		myDataEngine.defineDataSource(this.dataSource);
		myDataEngine.defineDataSet(this.dataSet);

		return myDataEngine;
	}

	/**
	 * @return
	 */
	private IBaseExpression[] getRowExpr() {
		// row test
		int num = 4;
		IBaseExpression[] _rowBeArray = new IBaseExpression[num];
		_rowBeArray[0] = new ScriptExpression("dataSetRow.COUNTRY");
		_rowBeArray[1] = new ScriptExpression("dataSetRow.CITY");
		_rowBeArray[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		_rowBeArray[3] = new ScriptExpression("dataSetRow.AMOUNT");

		return _rowBeArray;
	}

	/**
	 *
	 * @return
	 */
	private String[] getRowExprName() {
		// row test
		int num = 4;
		String[] _rowNameArray = new String[num];
		_rowNameArray[0] = "ROW_COUNTRY";
		_rowNameArray[1] = "ROW_CITY";
		_rowNameArray[2] = "ROW_SALE_DATE";
		_rowNameArray[3] = "ROW_AMOUNT";

		return _rowNameArray;
	}

	/**
	 * @return aggregation expression array
	 */
	private IBaseExpression[] getAggrExpr() {
		int num2 = 2;
		IBaseExpression[] _totalBeArray = new IBaseExpression[num2];
		_totalBeArray[0] = new ScriptExpression("Total.Count( )");
		_totalBeArray[1] = new ScriptExpression("Total.Sum( dataSetRow.AMOUNT )");

		return _totalBeArray;
	}

	/**
	 *
	 * @return
	 */
	private String[] getAggrExprName() {
		// row test
		int num = 2;
		String[] _rowAggrArray = new String[num];
		_rowAggrArray[0] = "ROW_AGG1";
		_rowAggrArray[1] = "ROW_AGG2";

		return _rowAggrArray;
	}

	/**
	 * @param rowBeArray
	 * @param totalBeArray
	 * @param qd
	 */
	private void prepareExprNameAndQuery(IBaseExpression[] rowBeArray, String[] rowExprName,
			IBaseExpression[] totalBeArray, String[] rowAggrName, QueryDefinition qd) {
		// add value retrive tansformation
		if (rowExprName != null) {
			for (int i = 0; i < rowExprName.length; i++) {
				qd.addResultSetExpression(rowExprName[i], rowBeArray[i]);
			}
		}

		if (rowAggrName != null) {
			for (int i = 0; i < rowAggrName.length; i++) {
				qd.addResultSetExpression(rowAggrName[i], totalBeArray[i]);
			}
		}
	}

	/**
	 * Only check the result of the expectedValue of the result set
	 *
	 * @param data.it
	 * @param ri
	 * @throws DataException
	 * @throws BirtException
	 */
	private void checkResult(IResultIterator ri) throws BirtException {
		Iterator it = this.expectedValue.iterator();

		while (ri.next()) {
			String str = "";

			for (int i = 0; i < bindingNameRow.length; i++) {
				Object ob1 = it.next();
				Object ob2 = ri.getValue(bindingNameRow[i]);
				assertEquals(ob1, ob2);
				str += " " + ob2.toString();
			}

			if (totalBeArray != null) {
				for (int i = 0; i < bindingExprRow.length; i++) {
					Object ob1 = it.next();
					Object ob2 = ri.getValue(bindingExprRow[i]);
					assertEquals(ob1, ob2);
					str += " " + ob2.toString();
				}
			}

			System.out.println("row result set: " + str);
		}
	}

	/**
	 *
	 * @param dataEngine
	 * @return
	 */
	private DataSetCacheManager getDataSetCacheManager(DataEngineImpl dataEngine) {
		return dataEngine.getSession().getDataSetCacheManager();
	}
}

class CancelCacheThread extends Thread {
	IQueryResults queryResults;

	CancelCacheThread(IQueryResults queryResults) {
		this.queryResults = queryResults;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
//				Thread.sleep( 10 );
				queryResults.cancel();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
