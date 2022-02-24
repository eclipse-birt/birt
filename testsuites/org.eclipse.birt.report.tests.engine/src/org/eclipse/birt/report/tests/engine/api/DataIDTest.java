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

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>Test DataID API</b>
 */
public class DataIDTest extends EngineCase {

	final static String INPUT = "dataID.rptdesign";

	/**
	 * Test DataID methods with input report design
	 *
	 * @throws EngineException
	 * @throws IOException
	 */
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testDataIDFromReport() throws EngineException, IOException {
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
			String tmp_type = null;
			tmp_type = matcher.group(0);
			strIid = tmp_type.substring(tmp_type.indexOf("iid"));
			strIid = strIid.substring(5, strIid.indexOf("\"", 6));
			iids.add(strIid);
		}
		typePattern = DataExtractionTaskTest.buildPattern("LIST");
		matcher = typePattern.matcher(content);
		while (matcher.find()) {
			String tmp_type = null;
			tmp_type = matcher.group(0);
			strIid = tmp_type.substring(tmp_type.indexOf("iid"));
			strIid = strIid.substring(5, strIid.indexOf("\"", 6));
			iids.add(strIid);
		}

		InstanceID iid = InstanceID.parse(iids.get(1).toString());
		// DataID: dataSet:0
		iid = iid.getParentID().getParentID().getParentID();
		DataID dataID = iid.getDataID();
		assertEquals(iid.toString(), "/0.-2(" + dataID.getDataSetID() + ":" + dataID.getRowID() + ")");

		iid = iid.getParentID();
		dataID = iid.getDataID();
		assertNotNull(dataID);
		assertEquals(iid.toString(), "/0.28(" + dataID.getDataSetID() + ":" + dataID.getRowID() + ")");

		// DataID: {dataSet}.0.group:0
		iid = InstanceID.parse(iids.get(2).toString());
		iid = iid.getParentID().getParentID().getParentID();
		dataID = iid.getDataID();
		assertNotNull(dataID);
		assertEquals(iid.toString(), "/0.-3(" + dataID.getDataSetID() + ":" + dataID.getRowID() + ")");

		// TODO: add case for DataID:{{dataSet}.0.group}.0.group1:0
	}

	/**
	 * Test getDataSetID() method
	 */
	public void testGetDataSetID() {
		DataSetID dsID = new DataSetID("dsid");
		DataID dataID = new DataID(dsID, 1);
		assertEquals(dsID, dataID.getDataSetID());
	}

	/**
	 * Test getDataSetID() method
	 */
	public void testGetRowID() {
		DataID dataID = new DataID(null, 0);
		assertEquals(0, dataID.getRowID());

		dataID = new DataID(null, 1);
		assertEquals(1, dataID.getRowID());

		dataID = new DataID(null, -1);
		assertEquals(-1, dataID.getRowID());

		dataID = new DataID(null, Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, dataID.getRowID());

		dataID = new DataID(null, Long.MIN_VALUE);
		assertEquals(Long.MIN_VALUE, dataID.getRowID());
	}

	/**
	 * Test append() method
	 */
	public void testAppend() {
		DataSetID dsID = new DataSetID("ds1");
		DataID dataID = new DataID(dsID, 0);
		dataID.append(new StringBuffer("buffer"));
		assertEquals(dsID, dataID.getDataSetID());
		// TODO: no enough javadoc
		dataID = new DataID(null, 0);
		dataID.append(new StringBuffer("buffer"));
		assertNull(dataID.getDataSetID());
	}

	/**
	 * Test toString() method
	 */
	public void testToString() {
		DataSetID dsID = new DataSetID("ds1");
		DataID dataID = new DataID(dsID, 0);
		assertEquals("ds1:0", dataID.toString());

		dsID = new DataSetID("��ݼ�");
		dataID = new DataID(dsID, 0);
		assertEquals("��ݼ�:0", dataID.toString());

		dsID = new DataSetID("�ˤۤ����");
		dataID = new DataID(dsID, 0);
		assertEquals("�ˤۤ����:0", dataID.toString());

		dsID = new DataSetID("~!@#$%^&*()_+?>:");
		dataID = new DataID(dsID, 0);
		assertEquals("~!@#$%^&*()_+?>::0", dataID.toString());

		dsID = new DataSetID("");
		dataID = new DataID(dsID, 0);
		assertEquals(":0", dataID.toString());

		dataID = new DataID(dsID, Long.MAX_VALUE);
		assertEquals(":" + Long.MAX_VALUE, dataID.toString());
		// TODO: no enough javadoc
	}

}
