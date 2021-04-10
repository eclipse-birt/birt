/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class IncreCacheDataSetTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Impl.TestIncreCacheData.TableName"),
				ConfigText.getString("Impl.TestIncreCacheData.TableSQL"),
				ConfigText.getString("Impl.TestIncreCacheData.TestDataFileName"));
	}

	private Map appContextMap = new HashMap();
	private File tempDataFile;
	private static final String LINE_SEP = System.getProperty("line.separator");

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void increCacheDataSetSetUp() throws Exception {

		defineDataSourceAndDataSet();
		String configName = "testIncreCacheConfig.txt";
		URL url = this.getClass().getResource("input/" + configName);
		appContextMap.put(DataEngine.INCREMENTAL_CACHE_CONFIG, url);
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void increCacheDataSetTearDown() throws Exception {
		dataEngine.shutdown();
		// clear persistent cache to void conflict with other test cases
		getDataSetCacheManager(dataEngine).clearCache(dataSource, dataSet);
		getDataSetCacheManager((DataEngineImpl) dataEngine).resetForTest();
	}

	/**
	 * @return
	 * @throws BirtException
	 */
	private void defineDataSourceAndDataSet() throws BirtException {
		OdaDataSetDesign odaDesign = new OdaDataSetDesign("Test Data Set");
		odaDesign.setExtensionID(((OdaDataSetDesign) this.dataSet).getExtensionID());
		odaDesign.setQueryText(((OdaDataSetDesign) this.dataSet).getQueryText());

		dataEngine.defineDataSource(this.dataSource);
		dataEngine.defineDataSet(this.dataSet);
	}

	/**
	 * 
	 * @param dataEngine
	 * @return
	 */
	private DataSetCacheManager getDataSetCacheManager(DataEngine dataEngine) {
		DataEngineImpl engine = (DataEngineImpl) dataEngine;
		return engine.getSession().getDataSetCacheManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see testutil.BaseTestCase#getInputFolder(java.lang.String)
	 */
	protected InputStream getInputFolder(String dataFileName) {
		InputStream in = super.getInputFolder(dataFileName);
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDataFile = new File(tempDir, dataFileName);
		try {
			if (tempDataFile.exists() == false) {
				copy(in, tempDataFile);
			}
			tempDataFile.deleteOnExit();
			return new FileInputStream(tempDataFile);
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}
	}

	/**
	 * copy the data file to temporary directory.
	 * 
	 * @param in
	 * @param tempFile
	 * @throws IOException
	 */
	private void copy(InputStream in, File tempFile) throws IOException {
		BufferedInputStream bin = new BufferedInputStream(in);
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(tempFile));
		byte[] buffer = new byte[1024 * 8];
		int len = bin.read(buffer);
		while (len > 0) {
			bout.write(buffer, 0, len);
			len = bin.read(buffer);
		}
		bout.flush();
		bout.close();
		bin.close();
	}

	/**
	 * append <code>count</code> rows to the original data set.
	 * 
	 * @param count
	 * @throws IOException
	 */
	private void appendNewData(int count) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(tempDataFile, "rw");
		raf.seek(raf.length());
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < count; i++) {
			Date curDate = new Date();
			String latestTime = fm.format(curDate);
			String rowData = LINE_SEP + "'CHINA','Shangahi','" + latestTime + "',800,1,null";
			raf.writeBytes(rowData);
		}
		raf.close();
	}

	/**
	 * Test feature of whether incremental cache will be used
	 */
	@Test
	public void testBasicIncreCache() {
		try {
			assertEquals(8, getQueryResultCount());
			assertTrue(getDataSetCacheManager(dataEngine).doesSaveToCache());
			assertTrue(getDataSetCacheManager(dataEngine).doesLoadFromCache());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * 
	 */
	@Test
	public void testUpdateIncreCache() {
		try {
			assertEquals(8, getQueryResultCount());
			getDataSetCacheManager((DataEngineImpl) dataEngine).resetForTest();
			// append new data to the original data set
			Random random = new Random();
			int count = random.nextInt(100) + 1;
			appendNewData(count);
			// populate the data set with the updated file
			prepareDataSet(getDataSourceInfo());
			assertEquals(count + 8, getQueryResultCount());
			assertTrue(getDataSetCacheManager(dataEngine).doesSaveToCache());
			assertTrue(getDataSetCacheManager(dataEngine).doesLoadFromCache());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	private int getQueryResultCount() throws BirtException {
		QueryDefinition qd = newReportQuery();
		IQueryResults qr = dataEngine.prepare(qd, appContextMap).execute(null);
		IResultIterator resultIterator = qr.getResultIterator();
		int count = 0;
		while (resultIterator.next()) {
			count++;
		}
		qr.close();
		return count;
	}
}
