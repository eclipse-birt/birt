/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;

/**
 * 
 * @version $Revision: 1.1 $ $Date: 2006/10/26 02:42:38 $
 */
public class RunTaskTest extends EngineCase
{

	static final String TEST_FOLDER = "./utest/";
	
	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/run_task_design.xml";
	static final String REPORT_DESIGN = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/reportdocument/";
	static final String REPORT_DOCUMENT_ZIP = "./utest/reportdocument.zip";

	static final String REPORT_DESIGN_RESOURCE1 = "org/eclipse/birt/report/engine/api/render_task_design.xml";
	static final String REPORT_DESIGN1 = "./utest/design.rptdesign";
	static final String REPORT_DOCUMENT1 = "./utest/reportdocument.folder/";

	public void setUp( )
	{
		removeFile( TEST_FOLDER );
		removeFile( REPORT_DOCUMENT_ZIP );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
	}

	public void tearDown( )
	{
		removeFile( TEST_FOLDER );
	}

	public void testRun( )
	{
		try
		{
			EngineConfig config = new EngineConfig( );
			ReportEngine engine = new ReportEngine( config );
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			IRunTask task = engine.createRunTask( report );
			task.run( REPORT_DOCUMENT );
			task.close( );
			IReportDocument doc = engine.openReportDocument( REPORT_DOCUMENT );
			doc.close( );
			engine.shutdown( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
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
