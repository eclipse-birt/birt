/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.EngineCase;

/**
 * 
 */

public class DataSourceCompareTest extends EngineCase {

	static final String DATASOURCE_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource.rptdesign";
	static final String MODIFIED_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource_target.rptdesign";
	static final String NEST_QUERY_DATASOURCE_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource_nestQuery.rptdesign";
	static final String MODIFIED_NEST_QUERY_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource_nestQuery_target.rptdesign";
	static final String SUB_QUERY_DATASOURCE_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource_subQuery.rptdesign";
	static final String MODIFIED_SUB_QUERY_RESOURCE = "org/eclipse/birt/report/engine/api/dataSource_subQuery_target.rptdesign";

	static final String UTEST_FOLDER = "./utest/.birt.report.engine.api.DataSourceTest/";
	static final String DATASOURCE_DESIGN = UTEST_FOLDER + "source.rptdesign";
	static final String DATASOURCE_DOCUMENT = UTEST_FOLDER + "source.rptdocument";
	static final String MODIFIED_DESIGN = UTEST_FOLDER + "modified.rptdesign";
	static final String MODIFIED_DOCUMENT = UTEST_FOLDER + "modified.rptdocument";
	static final String GOLDEN_HTML = UTEST_FOLDER + "golden.html";
	static final String RESULT_HTML = UTEST_FOLDER + "modified.html";

	public void setUp() {
		removeFile(UTEST_FOLDER);
		new File(UTEST_FOLDER).mkdirs();
	}

	public void tearDown() {
		removeFile(UTEST_FOLDER);
	}

	private void doTestRender(String sourceResource, String changedResource) throws Exception {
		// create data source document
		copyResource(sourceResource, DATASOURCE_DESIGN);
		new EngineTask(DATASOURCE_DESIGN, DATASOURCE_DOCUMENT, null, null).doRunTask();

		// use the data source doucment to creat a new document and use that two
		// document to render
		copyResource(changedResource, MODIFIED_DESIGN);
		new EngineTask(MODIFIED_DESIGN, MODIFIED_DOCUMENT, DATASOURCE_DOCUMENT, null).doRunTask();
		new EngineTask(null, MODIFIED_DOCUMENT, DATASOURCE_DOCUMENT, RESULT_HTML).doRenderTask();

		// use the modified design to create the
		new EngineTask(MODIFIED_DESIGN, MODIFIED_DOCUMENT, null, null).doRunTask();
		new EngineTask(null, MODIFIED_DOCUMENT, null, GOLDEN_HTML).doRenderTask();

		compare(GOLDEN_HTML, RESULT_HTML);
	}

	private void doTestRunAndRender(String sourceResource, String changedResource) throws Exception {
		// create data source document
		copyResource(sourceResource, DATASOURCE_DESIGN);
		new EngineTask(DATASOURCE_DESIGN, DATASOURCE_DOCUMENT, null, null).doRunTask();

		// use the data source doucment to creat a new document and use that two
		// document to render
		copyResource(changedResource, MODIFIED_DESIGN);
		new EngineTask(MODIFIED_DESIGN, null, DATASOURCE_DOCUMENT, RESULT_HTML).doRunAndRenderTask();

		// use the modified design to create the golden file
		new EngineTask(MODIFIED_DESIGN, null, null, GOLDEN_HTML).doRunAndRenderTask();

		compare(GOLDEN_HTML, RESULT_HTML);
	}

	public void testTable() throws Exception {
		doTestRender(DATASOURCE_RESOURCE, MODIFIED_RESOURCE);
		doTestRunAndRender(DATASOURCE_RESOURCE, MODIFIED_RESOURCE);
	}

	/*
	 * TODO: test failed as DTE doesn't support it. public void testNestQuery( )
	 * throws Exception { doTestRender( NEST_QUERY_DATASOURCE_RESOURCE,
	 * MODIFIED_NEST_QUERY_RESOURCE ); doTestRunAndRender(
	 * NEST_QUERY_DATASOURCE_RESOURCE, MODIFIED_NEST_QUERY_RESOURCE ); }
	 */
	public void testSubQuery() throws Exception {
		doTestRender(SUB_QUERY_DATASOURCE_RESOURCE, MODIFIED_SUB_QUERY_RESOURCE);
		doTestRunAndRender(SUB_QUERY_DATASOURCE_RESOURCE, MODIFIED_SUB_QUERY_RESOURCE);
	}

	/**
	 * do run
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	private void compare(String goldenFile, String resultFile) throws Exception {
		loadContent(goldenFile);
		loadContent(resultFile);
		// assertEquals( golden, result );
	}

	public String loadContent(String src) throws Exception {
		InputStream in = new FileInputStream(src);
		assertTrue(in != null);
		try {
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			return new String(buffer);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		return null;
	}

	class EngineTask {

		/**
		 * design file
		 */
		private String designFile;

		/**
		 * decument file
		 */
		private String documentFile;

		/**
		 * dataSource file
		 */
		private String dataSourceFile;

		/**
		 * target file
		 */
		private String targetFile;

		/**
		 * 
		 */
		public EngineTask(String designFile, String documentFile, String dataSourceFile, String targetFile) {
			this.designFile = designFile;
			this.documentFile = documentFile;
			this.dataSourceFile = dataSourceFile;
			this.targetFile = targetFile;
		}

		/**
		 * get dataSource form file
		 */
		protected IDocArchiveReader openDataSource(String dataSourceFile) throws Exception {
			if (dataSourceFile == null || dataSourceFile.length() == 0) {
				return null;
			}
			if (dataSourceFile.endsWith("\\") || dataSourceFile.endsWith("/")) {
				return new FolderArchiveReader(dataSourceFile);
			} else {
				return new FileArchiveReader(dataSourceFile);
			}
		}

		/**
		 * create document
		 */
		public void doRunTask() throws Exception {
			EngineConfig config = new EngineConfig();
			IReportEngine engine = new ReportEngine(config);
			IReportRunnable report = engine.openReportDesign(designFile);
			IRunTask task = engine.createRunTask(report);
			IDocArchiveReader dataSource = openDataSource(dataSourceFile);
			if (dataSource != null) {
				task.setDataSource(dataSource);
			}
			task.run(documentFile);
			task.close();
			if (dataSource != null) {
				dataSource.close();
			}
			engine.shutdown();
		}

		/**
		 * create the report document.
		 * 
		 * @throws Exception
		 */
		public void doRenderTask() throws Exception {
			EngineConfig config = new EngineConfig();
			IReportEngine engine = new ReportEngine(config);

			// open the document in the archive.
			IReportDocument reportDoc = engine.openReportDocument(documentFile);
			// create an RenderTask using the report document
			IRenderTask task = engine.createRenderTask(reportDoc);

			// create the render options
			IRenderOption option = new HTMLRenderOption();
			option.setOutputFormat("html"); //$NON-NLS-1$
			option.setOutputFileName(targetFile);
			// set the render options
			task.setRenderOption(option);

			IDocArchiveReader dataSource = openDataSource(dataSourceFile);
			if (dataSource != null) {
				task.setDataSource(dataSource);
			}
			// execute the report to create the report document.
			task.render();
			reportDoc.close();
			if (dataSource != null) {
				dataSource.close();
			}
			// close the task, release the resource.
			task.close();
			engine.shutdown();
		}

		/**
		 * create document
		 */
		public void doRunAndRenderTask() throws Exception {
			EngineConfig config = new EngineConfig();
			IReportEngine engine = new ReportEngine(config);
			IReportRunnable report = engine.openReportDesign(designFile);

			IRunAndRenderTask task = engine.createRunAndRenderTask(report);

			// create the render options
			IRenderOption option = new HTMLRenderOption();
			option.setOutputFormat("html"); //$NON-NLS-1$
			option.setOutputFileName(targetFile);
			// set the render options
			task.setRenderOption(option);

			IDocArchiveReader dataSource = openDataSource(dataSourceFile);
			if (dataSource != null) {
				task.setDataSource(dataSource);
			}

			task.run();
			task.close();
			if (dataSource != null) {
				dataSource.close();
			}
			engine.shutdown();
		}
	}
}
