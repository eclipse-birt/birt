/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * 
 * 
 */
public class ReportDocumentTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/test.xml";
	static final String REPORT_DESIGN_RESOURCE_TEMP = "test_temp.xml";
	static final String REPORT_DESIGN = "./test.xml";
	static final String REPORT_DOCUMENT = "./reportdocument";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void tearDown( ) throws Exception
	{
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		super.tearDown();
	}

	public void testDocument( )
	{
		createDocument( );
		checkDocument( );
	}

	public void testDesignStream( )
	{
		try
		{
			IReportRunnable runnable = engine
					.openReportDesign( new FileInputStream( new File(
							REPORT_DESIGN ) ) );
			IRunTask runTask = engine.createRunTask( runnable );
			runTask.run( REPORT_DOCUMENT );

			IReportDocument rptDoc = engine
					.openReportDocument( REPORT_DOCUMENT );
			InputStream inputStream = rptDoc.getDesignStream( );
			assertTrue( inputStream != null );
			int streamLength = ( (RAInputStream) inputStream ).available( );
			assertTrue( streamLength > 0 );
			rptDoc.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	/**
	 * write empty content, the stream shouldn't be created.
	 */
	public void testEmptyDocument( )
	{
		try
		{
			IDocArchiveWriter archive = new FileArchiveWriter( REPORT_DOCUMENT );
			ReportDocumentWriter document = new ReportDocumentWriter( engine,
					archive );
			document.close( );

			IDocArchiveReader reader = new FileArchiveReader( REPORT_DOCUMENT );
			IReportDocument docReader = engine.openReportDocument( null,
					reader, null );

			assertFalse( reader.exists( ReportDocumentReader.BOOKMARK_STREAM ) );

			assertTrue( docReader.getBookmarks( ).isEmpty( ) );

			docReader.close( );
			reader.close( );

		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}

	}

	protected void createDocument( )
	{
		try
		{
			IDocArchiveWriter archive = new FileArchiveWriter( REPORT_DOCUMENT );
			ReportDocumentWriter document = new ReportDocumentWriter( engine,
					archive );

			ReportRunnable runnable = (ReportRunnable) engine
					.openReportDesign( REPORT_DESIGN );
			Report reportIR = new ReportParser( )
					.parse( (ReportDesignHandle) runnable.getDesignHandle( ) );
			document.saveDesign( runnable, null );
			document.saveReportIR( reportIR );

			HashMap parameters = createParamters( );
			document.saveParamters( parameters );

			Map map = createPersistentObjects( );
			document.savePersistentObjects( map );
			
			createBookmarks( document );
			
			document.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	protected void checkDocument( )
	{
		try
		{
			IDocArchiveReader archive = new FileArchiveReader( REPORT_DOCUMENT );
			IReportDocument document = engine.openReportDocument( null,
					archive, null );

			assertTrue( document.getName( ) != null );
			assertTrue( document.getReportRunnable( ) != null );
			checkParamters( document.getParameterValues( ) );
			checkBookmarks( document );
			checkPersistentObjects( document.getGlobalVariables( null ) );

			document.close( );

			archive.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}

	}

	protected HashMap createParamters( )
	{
		HashMap paramters = new HashMap( );
		ParameterAttribute paa = new ParameterAttribute( new Integer( 100 ),
				"100" );
		ParameterAttribute pab = new ParameterAttribute(
				new String( "STRING" ), "STRING" );
		paramters.put( "A", paa );
		paramters.put( "B", pab );
		return paramters;
	}

	protected void checkParamters( Map paramters )
	{
		assertEquals( 2, paramters.size( ) );
		assertEquals( new Integer( 100 ), paramters.get( "A" ) );
		assertEquals( "STRING", paramters.get( "B" ) );
	}

	protected Map createPersistentObjects( )
	{
		HashMap map = new HashMap( );
		map.put( "string", "STRING" );
		map.put( "integer", new Integer( 3 ) );
		return map;
	}

	protected void checkPersistentObjects( Map map )
	{
		assertEquals( 2, map.size( ) );
		assertEquals( "STRING", map.get( "string" ) );
		assertEquals( new Integer( 3 ), map.get( "integer" ) );
	}

	protected void createBookmarks( ReportDocumentWriter writer )
	{
		BookmarkContent info = new BookmarkContent( "A", -1L );
		info.setPageNumber( 1 );
		writer.setBookmark( "A", info );
		info = new BookmarkContent( "B", -1L );
		info.setPageNumber( 2 );
		writer.setBookmark( "B", info );
	}

	protected void checkBookmarks( IReportDocument document )
	{
		assertEquals( 1, document.getPageNumber( "A" ) );
		assertEquals( 2, document.getPageNumber( "B" ) );
	}
}
