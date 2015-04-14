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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.eclipse.birt.report.engine.EngineCase;

public class ProgressiveViewingTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/progressive_viewing.rptdesign";
	// static final String REPORT_DESIGN_RESOURCE =
	// "org/eclipse/birt/report/engine/api/empty_page.rptdesign";
	static final String REPORT_DESIGN = "progressive_viewing.rptdesign";
	static final String REPORT_DOCUMENT = "./reportdocument";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
	}

	public void tearDown( ) throws Exception
	{
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		super.tearDown();
	}

	/**
	 * new a thread to start the run task. In the run task, we register a
	 * PageHander to triger the render task.
	 */
	public void testProgressiveViewing( )
	{
		try
		{
			// open the report runnable to execute.
			IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
			// create an IRunTask
			IRunTask task = engine.createRunTask( report );
			// execute the report to create the report document.
			task.setPageHandler( new RenderTaskTrigger( ) );
			task.run( REPORT_DOCUMENT );
			// close the task, release the resource.
			task.close( );

			for ( int i = 0; i < pages.size( ); i++ )
			{
				PageContent pageContent = (PageContent) pages.get( i );

				IReportDocument reportDocument = engine
						.openReportDocument( REPORT_DOCUMENT );
				IRenderTask renderTask = engine
						.createRenderTask( reportDocument );
				HTMLRenderOption options = new HTMLRenderOption( );
				options.setOutputFormat( "html" );
				ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
				options.setOutputStream( ostream );
				renderTask.setRenderOption( options );
				renderTask.setPageNumber( pageContent.page );
				renderTask.render( );
				assertTrue( renderTask.getErrors( ).isEmpty( ) );
				renderTask.close( );
				reportDocument.close( );
				String content = ostream.toString( "utf-8" );
				assertEquals( content, pageContent.content );

			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};

	}

	class PageContent
	{

		PageContent( int page, String content )
		{
			this.page = page;
			this.content = content;
		}
		int page;
		String content;
	}

	ArrayList pages = new ArrayList( );

	class RenderTaskTrigger implements IPageHandler
	{

		IRenderTask renderTask;

		RenderTaskTrigger( )
		{
		}

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			try
			{
				if ( checkpoint == true )
				{
					IReportDocument reportDocument = doc.openReportDocument( );
					renderTask = engine.createRenderTask( reportDocument );
					HTMLRenderOption options = new HTMLRenderOption( );
					options.setOutputFormat( "html" );
					ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
					options.setOutputStream( ostream );
					renderTask.setRenderOption( options );
					renderTask.setPageNumber( pageNumber );
					renderTask.render( );
					renderTask.close( );
					reportDocument.close( );
					assertTrue( renderTask.getErrors( ).isEmpty( ) );
					String content = ostream.toString( "utf-8" );
					assertTrue( content.length( ) > 1024 );
					pages.add( new PageContent( pageNumber, content ) );
				}
			}
			catch ( Exception ex )
			{
				ex.printStackTrace( );
				fail( );
			}
		}
	}
}
