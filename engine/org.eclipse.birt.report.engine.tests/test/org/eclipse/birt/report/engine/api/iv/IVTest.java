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

package org.eclipse.birt.report.engine.api.iv;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.RunnableMonitor;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class IVTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_SalesByProducts.rptdesign";
	static final String REPORT_DESIGN_WITH_PARAM_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_ReportWithParam.rptdesign";
	static final String TEST_FOLDER = "./utest/";
	static final String REPORT_DESIGN = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/reportdocument.rptdocument";

	IReportEngine engine;

	public void setUp() {
		removeFile(TEST_FOLDER);
		EngineConfig config = new EngineConfig();
		engine = new ReportEngine(config);
	}

	public void tearDown() {
		engine.destroy();
		removeFile(TEST_FOLDER);
	}

	protected void createIVReportDocument() throws Exception {
		createReportDocument(new HashMap());
	}

	protected void createReportDocument(Map paramValues) throws Exception {
		// create the orignal report document
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		IRunTask task = engine.createRunTask(report);
		try {
			task.setParameterValues(paramValues);
			task.run(REPORT_DOCUMENT);
		} finally {
			task.close();
		}
	}

	public void testMutipleRun() throws Exception {
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		createIVReportDocument();
		RunnableMonitor monitor = new RunnableMonitor();
		for (int i = 0; i < 8; i++) {
			new IVRunnable(engine, monitor);
		}
		monitor.start();
		monitor.printStackTrace();
		assertTrue(monitor.getFailedRunnables().isEmpty());
	}

	public void testRun() throws Exception {
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		createIVReportDocument();
		new IVTask(engine, REPORT_DOCUMENT).run();
	}

	public void testRunWithParamters() throws Exception {
		copyResource(REPORT_DESIGN_WITH_PARAM_RESOURCE, REPORT_DESIGN);
		Map params = new HashMap();
		params.put("param", new Integer(100));
		createReportDocument(params);
		new IVTask(engine, REPORT_DOCUMENT).run();
	}

	/*
	 * public void testRunWithNoReportDesign( ) throws Exception { copyResource(
	 * REPORT_DESIGN_RESOURCE, REPORT_DESIGN ); createIVReportDocument( ); // new
	 * UnpackTask(REPORT_DOCUMENT_FOLDER).run( ); // remove the design file
	 * ArchiveFile archive = new ArchiveFile( REPORT_DOCUMENT, "rw+" );
	 * archive.removeEntry( "/design" ); archive.close( ); // new
	 * File(REPORT_DOCUMENT_FOLDER + "design").delete( );
	 * 
	 * try { new RenderTask( engine, REPORT_DOCUMENT ).run( ); } catch (
	 * EngineException ex ) { ex.printStackTrace( ); } }
	 * 
	 * public void testRunWithCorruptDocument( ) throws Exception { copyResource(
	 * REPORT_DESIGN_RESOURCE, REPORT_DESIGN ); createIVReportDocument( );
	 * 
	 * // corrupt the content file ArchiveFile archive = new ArchiveFile(
	 * REPORT_DOCUMENT, "rw+" ); ArchiveEntry entry = archive.getEntry(
	 * "/content/content.dat" ); entry.setLength( entry.getLength( ) / 2 );
	 * archive.close( );
	 * 
	 * new RenderTask( engine, REPORT_DOCUMENT ).run( ); }
	 */
	static final String REPORT_DESIGN_NO_FILTER_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignNoFilter.rptdesign";
	static final String REPORT_DESIGN_WITH_FILTER_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithFilter.rptdesign";

	public void testFilters() throws Exception {
		doTestIV(REPORT_DESIGN_NO_FILTER_RESOURCE, REPORT_DESIGN_WITH_FILTER_RESOURCE);
	}

	static final String REPORT_DESIGN_WITHOUT_CC_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithoutCC.rptdesign";
	static final String REPORT_DESIGN_WITH_CC_RESOURCE = "org/eclipse/birt/report/engine/api/iv/IV_DesignWithCC.rptdesign";

	/*
	 * still failed. public void testCC( ) throws Exception { doTestIV(
	 * REPORT_DESIGN_WITHOUT_CC_RESOURCE, REPORT_DESIGN_WITH_CC_RESOURCE ); }
	 */

	protected void doTestIV(String originalDesign, String changedDesign) throws Exception {
		copyResource(originalDesign, REPORT_DESIGN);

		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		IRunTask task = engine.createRunTask(report);
		task.run(REPORT_DOCUMENT);
		task.close();

		// rerun the report based on the report document
		copyResource(changedDesign, REPORT_DESIGN);
		report = engine.openReportDesign(REPORT_DESIGN);
		task = engine.createRunTask(report);

		ArchiveFile archive = new ArchiveFile(REPORT_DOCUMENT, "rw+");

		task.setDataSource(new ArchiveReader(archive));
		task.run(new ArchiveWriter(archive));
		task.close();

		archive.close();

		// render the generated report document
		IReportDocument doc = engine.openReportDocument(REPORT_DOCUMENT);
		long pageCount = doc.getPageCount();
		for (int i = 1; i <= pageCount; i++) {
			IRenderTask renderTask = engine.createRenderTask(doc);

			HTMLRenderOption option = new HTMLRenderOption();
			option.setOutputFormat("html");
			option.setOutputFileName("./utest/output" + i + ".html");
			renderTask.setRenderOption(option);
			renderTask.setPageNumber(i);

			renderTask.render();
			List errors = renderTask.getErrors();
			assertEquals(0, errors.size());
			renderTask.close();
		}
		doc.close();
	}

	static private class IVRunnable extends RunnableMonitor.Runnable {

		static int TOTAL_THREAD = 0;
		int threadNumber;
		IReportEngine engine;

		IVRunnable(IReportEngine engine, RunnableMonitor monitor) {
			super(monitor);
			threadNumber = TOTAL_THREAD++;
			this.engine = engine;
		}

		public void doRun() throws Exception {
			new IVTask(engine, REPORT_DOCUMENT + threadNumber).run();
		}
	}

	static private class RenderTask {

		IReportEngine engine;
		String fileName;

		RenderTask(IReportEngine engine, String fileName) {
			this.engine = engine;
			this.fileName = fileName;
		}

		public void run() throws Exception {
			// render the generated report document
			IReportDocument doc = engine.openReportDocument(fileName);
			long pageCount = doc.getPageCount();
			for (int i = 1; i <= pageCount; i++) {
				IRenderTask renderTask = engine.createRenderTask(doc);

				HTMLRenderOption option = new HTMLRenderOption();
				option.setOutputFormat("html");
				option.setOutputStream(new ByteArrayOutputStream());
				renderTask.setRenderOption(option);
				renderTask.setPageNumber(i);

				renderTask.render();
				List errors = renderTask.getErrors();
				assertEquals(0, errors.size());
				renderTask.close();
			}
			doc.close();
		}
	}

	static private class IVTask {

		IReportEngine engine;
		String fileName;

		IVTask(IReportEngine engine, String fileName) {
			this.engine = engine;
			if (fileName == null) {
				fileName = REPORT_DOCUMENT;
			}
			this.fileName = fileName;

		}

		public void run() throws Exception {
			ArchiveFile archive = null;

			// unpack it to a folder
			if (!REPORT_DOCUMENT.equals(fileName)) {
				archive = new ArchiveFile(REPORT_DOCUMENT, "r");
				archive.saveAs(fileName);
				archive.close();
			}

			archive = new ArchiveFile(fileName, "rw+");
			ArchiveReader reader = new ArchiveReader(archive);

			// get the runnable in the report document
			IReportDocument doc = engine.openReportDocument(null, reader, new HashMap());
			IReportRunnable report = doc.getReportRunnable();
			doc.close();

			// rerun the report based on the report document
			IRunTask task = engine.createRunTask(report);
			task.setDataSource(new ArchiveReader(archive));
			task.run(new ArchiveWriter(archive));
			task.close();

			// render the generated report document
			doc = engine.openReportDocument(null, reader, new HashMap());
			long pageCount = doc.getPageCount();
			for (int i = 1; i <= pageCount; i++) {
				IRenderTask renderTask = engine.createRenderTask(doc);

				HTMLRenderOption option = new HTMLRenderOption();
				option.setOutputFormat("html");
				option.setOutputStream(new ByteArrayOutputStream());
				renderTask.setRenderOption(option);
				renderTask.setPageNumber(i);

				renderTask.render();
				List errors = renderTask.getErrors();
				assertEquals(0, errors.size());
				renderTask.close();
			}
			doc.close();

			archive.close();
		}
	}
}
