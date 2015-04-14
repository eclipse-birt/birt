/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
 * register a page hander to see if the page handle is been called.
 * 
 * This class must be running as plugin unit test.
 * 
 */
public class PageHandlerTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/page-handler.rptdesign";
	static final String REPORT_DESIGN = "page-handler.rptdesign";
	static final String REPORT_DOCUMENT = "./reportdocument.rptdocument";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
	}

	public void tearDown( ) throws Exception
	{
		// shut down the engine.
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		super.tearDown();
	}

	class TestPageHandler implements IPageHandler
	{

		int callBackCount = 0;
		private long[] pageNumberStatus;
		private boolean[] checkPointStatus;

		public TestPageHandler( long[] pageNumbers, boolean[] checkPointStatus)
		{
			this.pageNumberStatus = pageNumbers;
			this.checkPointStatus = checkPointStatus;
		}
		
		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			assertEquals( pageNumberStatus[callBackCount], pageNumber );
			assertEquals( checkPointStatus[callBackCount], checkpoint );
			callBackCount++;
		}
		
		public int getCallCount( )
		{
			return callBackCount;
		}
	}

	public void testHandlerOfRunTask( )
	{
		long pageNumberStatus[] = new long[]{1, 2, 3, 3};
		boolean checkPointStatus[] = new boolean[]{true, false, false, true};

		try
		{
			// open the report runnable to execute.
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			// create an IRunTask
			IRunTask task = engine.createRunTask( report );
			// execute the report to create the report document.
			task.setPageHandler( new TestPageHandler( pageNumberStatus,
					checkPointStatus ) );
			task.run( REPORT_DOCUMENT );
			// close the task, release the resource.
			task.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};
	}

	public void testHandlerOfRunAndRenderTask( )
	{
		testRunAndRender( "html", 1, 2, 3, 3 );
		//testRunAndRender( "pdf", 1, 2, 3, 3 );
	}

	private void testRunAndRender( String format, long... pageNumberStatus )
	{
		try
		{
			boolean[] checkPointStatus = new boolean[pageNumberStatus.length];
			// open the report runnable to execute.
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			// create an IRunTask
			IRunAndRenderTask task = engine.createRunAndRenderTask( report );
			HTMLRenderOption options = new HTMLRenderOption();
			options.setHtmlPagination( true );
			options.setOutputFormat( format );
			task.setRenderOption(options);
			// execute the report to create the report document.
			TestPageHandler handler = new TestPageHandler( pageNumberStatus,
					checkPointStatus );
			task.setPageHandler( handler );
			task.run( );
			assertEquals( pageNumberStatus.length, handler.getCallCount( ) );
			// close the task, release the resource.
			task.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};
	}

	public void testHandlerOfRenderTask( )
	{
		testRender( "html", 1, 2, 3, 3 );
		testRender( "pdf", 1, 2, 3, 3 );
	}

	
	private void testRender( String format, long... pageNumberStatus )
	{
		try
		{
			// open the report runnable to execute.
			IReportDocument document = this.createReportDocument( REPORT_DESIGN_RESOURCE );
			// create an IRunTask
			IRenderTask task = engine.createRenderTask( document );
			HTMLRenderOption options = new HTMLRenderOption();
			options.setHtmlPagination( true );
			options.setOutputFormat( format );
			task.setRenderOption(options);
			// execute the report to create the report document.
			TestPageHandler handler = new TestPageHandler( pageNumberStatus,
					new boolean[pageNumberStatus.length] );
			task.setPageHandler( handler );
			task.render( );
			// close the task, release the resource.
			task.close( );
			assertEquals( pageNumberStatus.length, handler.getCallCount( ) );
			document.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};
	}
}
