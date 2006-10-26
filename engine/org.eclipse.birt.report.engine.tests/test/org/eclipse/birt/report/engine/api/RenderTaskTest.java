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

import java.io.File;

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
				ReportDocumentConstants.REPORT_DOCUMENT_VERSION_2_1_0 ) );
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
}