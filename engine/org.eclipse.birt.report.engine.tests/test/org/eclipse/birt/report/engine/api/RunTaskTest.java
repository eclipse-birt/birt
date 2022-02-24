/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import org.eclipse.birt.core.archive.compound.ArchiveView;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;

/**
 * 
 */
public class RunTaskTest extends EngineCase {

	static final String TEST_FOLDER = "./utest/";

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/run_task_design.xml";
	static final String BLANK_REPORT_DOCUMENT_RESOURCE = "org/eclipse/birt/report/engine/api/BlankReport.rptdocument";
	static final String BLANK_REPORT_DOCUMENT = "./utest/BlankReport.rptdocument";
	static final String VIEW_DOCUMENT = "./utest/view.rptdocument";
	static final String REPORT_DESIGN = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/reportdocument/";
	static final String REPORT_DOCUMENT_ZIP = "./utest/reportdocument.zip";

	static final String REPORT_DESIGN_RESOURCE1 = "org/eclipse/birt/report/engine/api/render_task_design.xml";
	static final String REPORT_DESIGN1 = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT1 = "./utest/reportdocument.folder/";

	public void setUp() throws Exception {
		super.setUp();
		removeFile(TEST_FOLDER);
		removeFile(REPORT_DOCUMENT_ZIP);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		copyResource(BLANK_REPORT_DOCUMENT_RESOURCE, BLANK_REPORT_DOCUMENT);
	}

	public void tearDown() throws Exception {
		removeFile(TEST_FOLDER);
		super.tearDown();
	}

	public void testRun() {
		try {
			IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
			IRunTask task = engine.createRunTask(report);
			task.run(REPORT_DOCUMENT);
			task.close();
			IReportDocument doc = engine.openReportDocument(REPORT_DOCUMENT);
			doc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	public void testRunWithArchiveView() {
		try {
			ArchiveView view = new ArchiveView(VIEW_DOCUMENT, BLANK_REPORT_DOCUMENT, "rw");
			ArchiveWriter writer = new ArchiveWriter(view);
			IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
			IRunTask task = engine.createRunTask(report);
			task.run(writer);
			task.close();
			writer.finish();
			view.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

//	public void testCancel( )
//	{
//		/*
//		 * delete this UnitTest
//		 * new UnitTest will be checkin to test task.cancel()
//		 */
//		copyResource( REPORT_DESIGN_RESOURCE1, REPORT_DESIGN1 );
//		removeFile( REPORT_DOCUMENT1 );
//		IReportEngine engine = createReportEngine( );
//		try
//		{
//			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN1 );
//			IRunTask task = engine.createRunTask( report );
//			task.setPageHandler( new RenderTaskTrigger( engine, task ) );
//			task.run( REPORT_DOCUMENT1 );
//			task.close( );
//
//			IReportDocument reportDoc = engine
//					.openReportDocument( REPORT_DOCUMENT1 );
//			assertEquals( 3, reportDoc.getPageCount( ) );
//			reportDoc.close( );
//		}
//		catch ( Exception ex )
//		{
//			ex.printStackTrace( );
//			fail( );
//		};
//		engine.shutdown( );
//		removeFile( REPORT_DESIGN1 );
//		removeFile( REPORT_DOCUMENT1 );
//	}

//	 This class never been used locally. Comment this class to fix the warning.
//	private class RenderTaskTrigger implements IPageHandler
//	{
//
//		IRenderTask renderTask;
//		IReportEngine engine;
//		IRunTask task;
//
//		RenderTaskTrigger( IReportEngine engine, IRunTask task )
//		{
//			this.engine = engine;
//			this.task = task;
//		}
//
//		public void onPage( int pageNumber, boolean checkpoint,
//				IReportDocumentInfo doc )
//		{
//			try
//			{
//				if ( pageNumber == 2 )
//				{
//					task.cancel( );
//				}
//				if ( pageNumber == 4 )
//				{
//					fail( );
//				}
//			}
//			catch ( Exception ex )
//			{
//				ex.printStackTrace( );
//				fail( );
//			}
//		}
//	}
}
