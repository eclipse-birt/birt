/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>Test DataSetID API</b>
 */
public class DataSetIDTest extends EngineCase {

	final static String INPUT = "dataSetID.rptdesign";

	/**
	 * Test DataSetID methods with input report design
	 *
	 * @throws EngineException
	 * @throws IOException
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testDataSetIDFromReport() throws EngineException, IOException {
		String inputFile = this.genInputFile(INPUT);
		IReportRunnable reportRunnable = engine.openReportDesign(inputFile);
		HTMLRenderOption options = new HTMLRenderOption();
		options.setOutputFormat("html");
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		options.setOutputStream(ostream);
		options.setEnableMetadata(true);
		IRunAndRenderTask task = engine.createRunAndRenderTask(reportRunnable);
		task.setRenderOption(options);
		task.run();
		assertTrue(task.getErrors().size() <= 0);
		task.close();

		// get instance id of two tables and one list in report
		ArrayList iids = new ArrayList();
		String content = ostream.toString("utf-8");
		ostream.close();
		Pattern typePattern = DataExtractionTaskTest.buildPattern("TABLE");
		Matcher matcher = typePattern.matcher(content);
		String strIid = null;
		while (matcher.find()) {
			String tmp_type;
			tmp_type = matcher.group(0);
			strIid = tmp_type.substring(tmp_type.indexOf("iid"));
			strIid = strIid.substring(5, strIid.indexOf("\"", 6));
			iids.add(strIid);
		}
		typePattern = DataExtractionTaskTest.buildPattern("LIST");
		matcher = typePattern.matcher(content);
		while (matcher.find()) {
			String tmp_type;
			tmp_type = matcher.group(0);
			strIid = tmp_type.substring(tmp_type.indexOf("iid"));
			strIid = strIid.substring(5, strIid.indexOf("\"", 6));
			iids.add(strIid);
		}

		// DataID: dataSet:0
		InstanceID iid = InstanceID.parse(iids.get(1).toString());
		iid = iid.getParentID().getParentID().getParentID();
		DataSetID dsID = iid.getDataID().getDataSetID();
		String dsName = iid.toString().substring(iid.toString().indexOf("(") + 1, iid.toString().indexOf(":"));
		assertEquals(dsName, dsID.getDataSetName());
		assertEquals(0, dsID.getRowID());
		assertNull(dsID.getParentID());
		assertNull(dsID.getQueryName());

		iid = iid.getParentID();
		dsID = iid.getDataID().getDataSetID();
		dsName = iid.toString().substring(iid.toString().indexOf("(") + 1, iid.toString().indexOf(":"));
		assertEquals(dsName, dsID.getDataSetName());
		assertEquals(0, dsID.getRowID());
		assertNull(dsID.getParentID());
		assertNull(dsID.getQueryName());

		// DataID: {dataSet}.0.group:0
		iid = InstanceID.parse(iids.get(2).toString());
		iid = iid.getParentID().getParentID().getParentID();
		dsID = iid.getDataID().getDataSetID();
		assertNull(dsID.getDataSetName());
		assertEquals(0, dsID.getRowID());
		assertNotNull(dsID.getParentID());
		assertEquals(dsName, dsID.getParentID().getDataSetName());
		assertEquals("52", dsID.getQueryName());

		// TODO: add case for datasetID in such format:
		// DataID:{{dataSet}.0.group}.0.group1:0
	}

	/**
	 * Test getParentID() method
	 */
	public void testGetParentID() {
		DataSetID dsID = new DataSetID(new DataSetID("parent"), 1, "query");
		assertNotNull(dsID.getParentID());
		assertEquals("parent", dsID.getParentID().getDataSetName());

		// dsID = new DataSetID( null, 0, null );
		// assertNull( dsID.getParentID( ) );

		dsID = new DataSetID("dataset");
		assertNull(dsID.getParentID());
	}

	/**
	 * Test getDataSetName() method
	 */
	public void testGetDataSetName() {
		DataSetID dsID = new DataSetID("ds");
		assertEquals("ds", dsID.getDataSetName());

		// dsID = new DataSetID( null );
		// assertNull( dsID.getDataSetName( ) );
	}

	/**
	 * Test getQueryName() method
	 */
	public void testGetQueryName() {
		DataSetID parent = new DataSetID("parent");
		DataSetID dsID = new DataSetID(parent, 0, "query");
		assertEquals("query", dsID.getQueryName());

		dsID = new DataSetID(parent, 0, "��ѯ");
		assertEquals("��ѯ", dsID.getQueryName());

		dsID = new DataSetID(parent, 0, "~!@#$%^&*()_+?>:");
		assertEquals("~!@#$%^&*()_+?>:", dsID.getQueryName());

		dsID = new DataSetID(parent, 0, "~!@#$%^&*()_+?>:");
		assertEquals("~!@#$%^&*()_+?>:", dsID.getQueryName());

		// dsID = new DataSetID( parent, 0, null );
		// assertNull( dsID.getQueryName( ) );
	}

	/**
	 * Test getRowID() method
	 */
	public void testGetRowID() {
		DataSetID parent = new DataSetID("parent");
		String query = "query";

		DataSetID dsID = new DataSetID(parent, 0, query);
		assertEquals(0, dsID.getRowID());

		dsID = new DataSetID(parent, 1, query);
		assertEquals(1, dsID.getRowID());

		dsID = new DataSetID(parent, -1, query);
		assertEquals(-1, dsID.getRowID());

		dsID = new DataSetID(parent, Long.MIN_VALUE, query);
		assertEquals(Long.MIN_VALUE, dsID.getRowID());

		dsID = new DataSetID(parent, Long.MAX_VALUE, query);
		assertEquals(Long.MAX_VALUE, dsID.getRowID());
	}

	/**
	 * Test toString() method
	 */
	public void testToString() {
		DataSetID dsID = new DataSetID("ds");
		assertEquals("ds", dsID.toString());

		dsID = new DataSetID(new DataSetID("parent"), 1, "query");
		assertEquals("{parent}.1.query", dsID.toString());

		// dsID = new DataSetID( parent, 1, "query" );
		// assertNull( dsID.toString( ) );

		dsID = new DataSetID(new DataSetID(new DataSetID("grandpa"), 0, " "), 1, "query");
		assertEquals("{{grandpa}.0. }.1.query", dsID.toString());

		dsID = new DataSetID(new DataSetID("parent"), 1, "");
		assertEquals("{parent}.1.", dsID.toString());
	}
}
