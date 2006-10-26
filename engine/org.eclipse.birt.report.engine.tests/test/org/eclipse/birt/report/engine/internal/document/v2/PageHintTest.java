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

package org.eclipse.birt.report.engine.internal.document.v2;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentReader;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageHint;

public class PageHintTest extends EngineCase
{

	final static String REPORT_DOCUMENT_NAME = ".internal.test.rptdocument";

	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT_NAME );
	}

	public void tearDown( )
	{
		removeFile( REPORT_DOCUMENT_NAME );
	}

	public void testPageHintStream( ) throws Exception
	{
		doWrite( );
		doRead( );
	}

	protected void doWrite( ) throws Exception
	{
		FileArchiveWriter archive = new FileArchiveWriter( REPORT_DOCUMENT_NAME );
		ReportDocumentWriter document = new ReportDocumentWriter( null, archive );
		PageHintWriterV2 hintWriter = new PageHintWriterV2( document );
		hintWriter.open( );
		hintWriter.writePageHint( new PageHint( 1, 0, 0, 500 ) );
		hintWriter.writePageHint( new PageHint( 2, 200, 600, 1000 ) );
		hintWriter.writeTotalPage( 2 );
		hintWriter.close( );
		document.close( );
	}

	protected void doRead( ) throws Exception
	{
		FileArchiveReader archive = new FileArchiveReader( REPORT_DOCUMENT_NAME );
		ReportDocumentReader document = new ReportDocumentReader( null, archive );
		PageHintReaderV2 reader = new PageHintReaderV2( document );
		reader.open( );
		long pageNumber = reader.getTotalPage( );
		assertEquals( pageNumber, 2 );
		IPageHint hint = reader.getPageHint( 1 );
		checkPageHint( hint, 1, 0, 0, 500 );
		hint = reader.getPageHint( 2 );
		checkPageHint( hint, 2, 200, 600, 1000 );

		reader.close( );
		document.close( );
	}

	protected void checkPageHint( IPageHint hint, long number, long offset,
			long start, long end )
	{
		assertTrue( hint != null );
		assertEquals( number, hint.getPageNumber( ) );
		assertEquals( offset, hint.getOffset( ) );
		assertEquals( 1, hint.getSectionCount( ) );
		assertEquals( start, hint.getSectionStart( 0 ) );
		assertEquals( end, hint.getSectionEnd( 0 ) );
	}
}
