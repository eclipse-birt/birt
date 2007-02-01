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

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAFileInputStream;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCEntry;
import org.eclipse.birt.report.engine.toc.TOCTree;

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

	protected IReportEngine engine;

	public void setUp( )
	{
		engine = createReportEngine( );

		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void tearDown( )
	{
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
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
			int streamLength = ( (RAFileInputStream) inputStream ).available( );
			assertTrue( streamLength > 0 );
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

			TOCTree tree = new TOCTree( );
			document.saveTOC( tree );

			HashMap bookmarks = new HashMap( );
			document.saveBookmarks( bookmarks );

			document.close( );

			IDocArchiveReader reader = new FileArchiveReader( REPORT_DOCUMENT );
			ReportDocumentReader docReader = new ReportDocumentReader( engine,
					reader );

			assertFalse( reader.exists( ReportDocumentReader.BOOKMARK_STREAM ) );
			// we now create TOC_STREAM whenever where is TOC or not.
			// assertFalse( reader.exists( ReportDocumentReader.TOC_STREAM ) );

			TOCNode root = tree.getTOCRoot( );
			assertTrue( root.getChildren( ).isEmpty( ) );

			assertTrue( docReader.getBookmarks( ).isEmpty( ) );

			docReader.close( );

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
			document.saveDesign( runnable );

			HashMap parameters = createParamters( );
			document.saveParamters( parameters );

			TOCTree tocTree = createTOC( );
			document.saveTOC( tocTree );

			HashMap bookmarks = createBookmarks( );
			document.saveBookmarks( bookmarks );

			Map map = createPersistentObjects( );
			document.savePersistentObjects( map );

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
			ReportDocumentReader document = new ReportDocumentReader( engine,
					archive );

			assertTrue( document.getName( ) != null );
			assertTrue( document.getReportRunnable( ) != null );
			checkParamters( document.getParameterValues( ) );
			checkTOC( document );
			checkBookmarks( document );
			checkPersistentObjects( document.getGlobalVariables( null ) );

			document.close( );
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

	protected TOCTree createTOC( )
	{
		TOCTree tree = new TOCTree( );
		TOCBuilder builder = new TOCBuilder( tree );
		TOCEntry rootEntry = builder.getTOCEntry( );
		{
			TOCEntry chapter1 = startEntry( builder, rootEntry,
					"Chapter 1 title", null );
			{
				createEntry( builder, chapter1, "Section 1 title", null );
				createEntry( builder, chapter1, "Section 1 title", null );
				createEntry( builder, chapter1, "Chapter 2 title", null );
			}
			builder.closeEntry( chapter1 );
			createEntry( builder, rootEntry, "Chapter 2 title", null );
			createEntry( builder, rootEntry, "Section 1 title", null );
		}
		return builder.getTOCTree( );
	}

	private TOCEntry startEntry( TOCBuilder builder, TOCEntry entry,
			String displayString, String bookmark )
	{
		return builder.startEntry( entry, displayString, bookmark );
	}
	
	private TOCEntry createEntry( TOCBuilder builder, TOCEntry entry,
			String displayString, String bookmark )
	{
		return builder.createEntry( entry, displayString, bookmark );
	}
	
	protected void checkTOC( ReportDocumentReader document )
	{
		// assertTOCNode( root, null, "/", "ROOT", "0" );
		ITOCTree tree = document.getTOCTree( "viewer", null );
		TOCNode root = tree.getRoot( );
		assertTrue( root != null );
		assertTrue( root.getNodeID( ) == null );
		assertEquals( 3, root.getChildren( ).size( ) );
		Iterator iter = root.getChildren( ).iterator( );
		TOCNode chart1 = (TOCNode) iter.next( );
		assertTOCNode( chart1, root, "__TOC_0", "Chapter 1 title", "__TOC_0" );
		assertEquals( 3, chart1.getChildren( ).size( ) );
		Iterator sectionIter = chart1.getChildren( ).iterator( );

		TOCNode section1 = (TOCNode) sectionIter.next( );
		assertTOCNode( section1, chart1, "__TOC_0_0", "Section 1 title",
				"__TOC_0_0" );

		section1 = (TOCNode) sectionIter.next( );
		assertTOCNode( section1, chart1, "__TOC_0_1", "Section 1 title",
				"__TOC_0_1" );

		section1 = (TOCNode) sectionIter.next( );
		assertTOCNode( section1, chart1, "__TOC_0_2", "Chapter 2 title",
				"__TOC_0_2" );
		assertFalse( sectionIter.hasNext( ) );

		TOCNode chart2 = (TOCNode) iter.next( );
		assertTOCNode( chart2, root, "__TOC_1", "Chapter 2 title", "__TOC_1" );
		chart2 = (TOCNode) iter.next( );
		assertTOCNode( chart2, root, "__TOC_2", "Section 1 title", "__TOC_2" );
		assertFalse( sectionIter.hasNext( ) );

		checkFindTOCByName( document );
	}

	protected void checkFindTOCByName( ReportDocumentReader document )
	{
		TOCNode root = document.findTOC( "/" );
		assertTrue( root != null );

		List tocs = (List) document.findTOCByName( "Chapter 1 title" );
		assertEquals( 1, tocs.size( ) );

		tocs = (List) document.findTOCByName( "Chapter 2 title" );
		assertEquals( 2, tocs.size( ) );

		tocs = (List) document.findTOCByName( "Section 1 title" );
		assertEquals( 3, tocs.size( ) );

		tocs = (List) document.findTOCByName( "Unexist toc" );
		assertNull( tocs );

		assertNull( document.findTOCByName( null ) );
	}

	protected void assertTOCNode( TOCNode node, TOCNode parent, String id,
			String label, String bookmark )
	{
		assertEquals( parent, node.getParent( ) );
		assertEquals( id, node.getNodeID( ) );
		assertEquals( label, node.getDisplayString( ) );
		assertEquals( bookmark, node.getBookmark( ) );
	}

	protected HashMap createBookmarks( )
	{
		HashMap bookmarks = new HashMap( );
		bookmarks.put( "A", new Long( 1 ) );
		bookmarks.put( "B", new Long( 2 ) );
		return bookmarks;
	}

	protected void checkBookmarks( IReportDocument document )
	{
		assertEquals( 1, document.getPageNumber( "A" ) );
		assertEquals( 2, document.getPageNumber( "B" ) );
	}
}
