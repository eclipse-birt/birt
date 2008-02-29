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
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public class RenderTaskTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/render_task_design.xml";

	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void testRenderOnTransientFile( ) throws Exception
	{
		ArchiveFile archive = new ArchiveFile( REPORT_DOCUMENT, "rw" );
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		// create an IRunTask
		IRunTask task = engine.createRunTask( report );
		// execute the report to create the report document.
		ArchiveWriter writer = new ArchiveWriter( archive );
		task.run( writer );
		// close the task, release the resource.
		task.close( );

		archive.saveAs( "test.archive");
		
		// open the document in the archive.
		ArchiveReader reader = new ArchiveReader( archive );
		IReportDocument reportDoc = engine.openReportDocument( null, reader,
				new HashMap( ) );
		assertTrue( reportDoc.getVersion( ).equals(
				ReportDocumentConstants.BIRT_ENGINE_VERSION ) );
		// create an RenderTask using the report document
		IRenderTask renderTask = engine.createRenderTask( reportDoc );
		// get the page number
		long pageNumber = reportDoc.getPageCount( );
		assertEquals( 3, pageNumber );

		for ( long i = 1; i <= pageNumber; i++ )
		{
			renderTask.setPageNumber( i );
			assertRender( renderTask, "page" + i + ".html" );
		}
		
		archive.close( );

	}

	public void testRender( )
	{
		try
		{
			createReportDocument( );
			doRenderTest( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		};
	}

	protected void doRenderTest( ) throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		assertTrue( reportDoc.getVersion( ).equals(
				ReportDocumentConstants.BIRT_ENGINE_VERSION ) );
		// create an RenderTask using the report document
		IRenderTask task = engine.createRenderTask( reportDoc );
		// get the page number
		long pageNumber = reportDoc.getPageCount( );
		assertEquals( 3, pageNumber );

		for ( long i = 1; i <= pageNumber; i++ )
		{
			task.setPageNumber( i );
			assertRender( task, "page" + i + ".html" );
		}

		task.setPageRange( "1, 3" ); //$NON-NLS-1$
		assertRender( task, "page13.html" ); //$NON-NLS-1$

		task.setPageRange( "1-2, 3" ); //$NON-NLS-1$
		assertRender( task, "page123.html" ); //$NON-NLS-1$

		task.setPageRange( "all" ); //$NON-NLS-1$
		assertRender( task, "pageAll.html" ); //$NON-NLS-1$

		task.setBookmark( "__TOC_0" ); //$NON-NLS-1$
		assertRender( task, "pageToc0.html" ); //$NON-NLS-1$

		task.setBookmark( "__TOC_1" ); //$NON-NLS-1$
		assertRender( task, "pageToc1.html" ); //$NON-NLS-1$

		task.setBookmark( "__TOC_2" ); //$NON-NLS-1$
		assertRender( task, "pageToc2.html" ); //$NON-NLS-1$

		// close the task.
		task.close( );

		reportDoc.close( );
	}

	private void assertRender( IRenderTask task, String outputFileName )
			throws EngineException
	{
		removeFile( outputFileName );
		// create the render options
		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" ); //$NON-NLS-1$
		option.setOutputFileName( outputFileName );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );
		File htmlFile = new File( outputFileName );
		assertTrue( htmlFile.exists( ) );
		assertTrue( htmlFile.length( ) != 0 );
		removeFile( outputFileName );
	}

	public void testRenderPDFFromDocument( ) throws Exception
	{
		String outputFileName = ".render_pdf_from_document.pdf";
		createReportDocument( );

		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		// create an RenderTask using the report document
		IRenderTask task = engine.createRenderTask( reportDoc );

		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "pdf" ); //$NON-NLS-1$
		option.setOutputFileName( outputFileName );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );
		File pdfFile = new File( outputFileName );
		assertTrue( pdfFile.exists( ) );
		assertTrue( pdfFile.length( ) != 0 );
		removeFile( outputFileName );
		task.close( );
		reportDoc.close( );
	}

	public void testCloseOnExitRenderOption( ) throws EngineException
	{
		String design = "org/eclipse/birt/report/engine/api/testCloseOnExit.rptdesign";
		IReportDocument document = createReportDocument( design );
		String[] formats = {"html", "pdf", "postscript", "ppt", "doc", "xls"};
		for ( String format : formats )
		{
			assertEquals( false, isRenderTaskCloseStreamOnExit( document,
					format, false ) );
			assertEquals( true, isRenderTaskCloseStreamOnExit( document,
					format, true ) );
		}
		document.close( );
	}
	
	public void testGetPageCount( ) throws EngineException
	{
		String design = "org/eclipse/birt/report/engine/api/TestGetPageCount.rptdesign";
		IReportDocument document = createReportDocument( design );
		test( document, "pdf" );
		test( document, "html" );
	}

	private void test( IReportDocument document, String format )
			throws EngineException
	{
		testRenderPageCount( document, 1, format, new RenderItemSetter( ) {

			public void setRenderItem( IRenderTask task )
					throws EngineException
			{
				task.setReportlet( "grid" );
			}
		} );
		testRenderPageCount( document, 1, format, new RenderItemSetter( ) {

			public void setRenderItem( IRenderTask task )
					throws EngineException
			{
				task.setPageNumber( 1 );
			}
		} );
		testRenderPageCount( document, 2, format, new RenderItemSetter( ) {

			public void setRenderItem( IRenderTask task )
					throws EngineException
			{
				task.setPageRange( "1,3" );
			}
		} );
		testRenderPageCount( document, 4, format, new RenderItemSetter( ) {

			public void setRenderItem( IRenderTask task )
					throws EngineException
			{
				task.setPageRange( "all" );
			}
		} );
	}

	private void testRenderPageCount( IReportDocument document,
			int expectedPageCount, String format, RenderItemSetter renderItemSetter )
			throws EngineException
	{
		IRenderTask task = createRenderTask( document, format );
		renderItemSetter.setRenderItem( task );
		task.render( );
		long pageCount = task.getPageCount( );
		task.close( );
		assertEquals( expectedPageCount, pageCount );
	}

	private static interface RenderItemSetter
	{
		void setRenderItem( IRenderTask task ) throws EngineException;
	}

	private IRenderTask createRenderTask( IReportDocument document,
			String format )
	{
		IRenderTask task = engine.createRenderTask( document );
		HTMLRenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( format );
		options.setOutputStream( new ByteArrayOutputStream( ) );
		options.setHtmlPagination( true );
		task.setRenderOption( options );
		return task;
	}
	
	private boolean isRenderTaskCloseStreamOnExit( IReportDocument document,
			String format, boolean closeOnExit ) throws EngineException
	{
		IRenderTask task = engine.createRenderTask( document );
		RenderOption options = new RenderOption( );
		options.setOutputFormat( format );
		TestOutputStream output = new TestOutputStream( );
		options.setOutputStream( output );
		options.closeOutputStreamOnExit( closeOnExit );
		task.setRenderOption( options );
		task.render( );
		task.close( );
		return output.isClosed( );
	}
	
	private static class TestOutputStream extends FilterOutputStream
	{
		private boolean isClosed = false; 
		public TestOutputStream( )
		{
			super( new ByteArrayOutputStream( ) );
		}

		public void close( ) throws IOException
		{
			super.close( );
			isClosed = true;
		}
		
		public boolean isClosed()
		{
			return isClosed;
		}
	}
}