/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ScalarParameterDefn;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>ReportEngine test</b>
 * <p>
 * This case tests methods in ReportEngine API.
 */
public class ReportEngineTest extends EngineCase {

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("report_engine.rptdesign", "report_engine.rptdesign");
		copyResource_INPUT("parameter.rptdesign", "parameter.rptdesign");
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	/**
	 * @param name
	 */
	public ReportEngineTest(String name) {
		super(name);
	}

	/**
	 * Test suite
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(ReportEngineTest.class);
	}

	/**
	 * Test getConfig() method
	 */
	public void testGetConfig() {
		EngineConfig config = new EngineConfig();
		config.setTempDir("tempdir");
		ReportEngine engine = new ReportEngine(config);
		EngineConfig configGet = engine.getConfig();
		assertEquals("getConfig() fail", config.getTempDir(), configGet.getTempDir());
	}

	/**
	 * Test openReportDesign(string)
	 *
	 * @throws EngineException
	 */
	public void testOpenReportDesign() throws EngineException {
		EngineConfig config = new EngineConfig();
		IReportRunnable reportRunner;
		config.setTempDir("tempdir");
		ReportEngine engine = new ReportEngine(config);
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE
		 * .getString("CASE_INPUT"); input += System.getProperty("file.separator") ;
		 * String designName=input+"report_engine.rptdesign";
		 */
		String designName = this.genInputFile("report_engine.rptdesign");

		try {
			reportRunner = engine.openReportDesign(designName);
			designName = "file:" + designName;
			designName = designName.replace('/', '\\');
			String reportName = reportRunner.getReportName().replace('/', '\\');
			assertEquals("openReportDesign(String) fail",
					designName.substring(designName.indexOf("org"), designName.length()),
					reportName.substring(reportName.indexOf("org"), reportName.length()));
			assertNotNull("openReportDesign(String) fail", reportRunner.getImage("23.gif"));
		} catch (EngineException ee) {
			ee.printStackTrace();
			fail("openReportDesign(String) fail");
		}
		engine.destroy();

	}

	/**
	 * Test openReportDesign(inputStream)
	 */
	public void testOpenReportDesign1() {
		EngineConfig config = new EngineConfig();
		IReportRunnable reportRunner;
		config.setTempDir("tempdir");
		ReportEngine engine = new ReportEngine(config);
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE
		 * .getString("CASE_INPUT"); input += System.getProperty("file.separator") ;
		 * String designName=input+"report_engine.rptdesign";
		 */

		String designName = this.genInputFile("report_engine.rptdesign");

		try {
			File file = new File(designName);
			FileInputStream fis = new FileInputStream(file);
			reportRunner = engine.openReportDesign(fis);
			assertEquals("openReportDesign(InputStream) fail", "<stream>", reportRunner.getReportName());
			assertNotNull("openReportDesign(InputStream) fail", reportRunner.getImage("23.gif"));
		} catch (EngineException ee) {
			ee.printStackTrace();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		}
		engine.destroy();

	}

	/**
	 * Test createGetParameterDefinitionTask()
	 */
	public void testCreateGetParameterDefinitionTask() {
		EngineConfig config = new EngineConfig();
		IReportRunnable reportRunner;
		ReportEngine engine = new ReportEngine(config);
		/*
		 * String input =
		 * PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE
		 * .getString("CASE_INPUT"); input += System.getProperty("file.separator") ;
		 * String designName=input+"parameter.rptdesign";
		 */
		String designName = this.genInputFile("parameter.rptdesign");

		try {
			reportRunner = engine.openReportDesign(designName);
			IGetParameterDefinitionTask getParamTask = engine.createGetParameterDefinitionTask(reportRunner);
			getParamTask.evaluateDefaults();
			IParameterDefnBase paramDefn = getParamTask.getParameterDefn("p1");
			System.err.println(paramDefn.getTypeName());
			System.err.println(paramDefn instanceof ScalarParameterDefn);
			assertEquals("creatGetParameterDefinitionTask() fail", "abc", getParamTask.getDefaultValue(paramDefn));
		} catch (EngineException ee) {
			ee.printStackTrace();
		}

	}

	public void testGetEmitterInfos() {
		String[][] expected = {
				{ "uk.co.spudsoft.birt.emitters.excel.XlsxEmitter", "xlsx", null,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "page-break-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.pdf", "pdf", null, "application/pdf",
						"paper-size-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.postscript", "postscript", null, "application/postscript",
						"paper-size-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.word", "doc", null, "application/msword",
						"page-break-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.docx", "docx", null,
						"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
						"page-break-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.html", "html", null, "text/html", "page-break-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.prototype.ods", "ods", "resource/ODS.gif",
						"application/vnd.oasis.opendocument.spreadsheet", "no-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.ppt", "ppt", null, "application/vnd.ms-powerpoint",
						"paper-size-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.pptx", "pptx", null,
						"application/vnd.openxmlformats-officedocument.presentationml.presentation",
						"paper-size-pagination" },
				{ "org.eclipse.birt.report.engine.emitter.odt", "odt", "resource/ODT.gif",
						"application/vnd.oasis.opendocument.text", "page-break-pagination" },
				{ "uk.co.spudsoft.birt.emitters.excel.XlsEmitter", "xls_spudsoft", null, "application/vnd.ms-excel",
						"page-break-pagination" } };
		EngineConfig config = new EngineConfig();
		ReportEngine engine = new ReportEngine(config);
		EmitterInfo[] emitters = engine.getEmitterInfo();
		assertNotNull(emitters);
		for (int i = 0; i < expected.length; i++) {
			String found = "";
			for (int j = 0; j < emitters.length; j++) {
				if (expected[i][0].equals(emitters[j].getID())) {
					found = emitters[j].getID();
					assertEquals(expected[i][1], emitters[j].getFormat());
					assertEquals(expected[i][2], emitters[j].getIcon());
					assertEquals(expected[i][3], emitters[j].getMimeType());
					assertEquals(expected[i][4], emitters[j].getPagination());
					break;
				}
			}
			assertEquals(expected[i][0], found);
		}
	}
}
