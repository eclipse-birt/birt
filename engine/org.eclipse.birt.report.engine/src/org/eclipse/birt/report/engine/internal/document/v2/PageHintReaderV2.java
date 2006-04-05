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

import java.io.DataInputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentReader;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageHint;

public class PageHintReaderV2 implements IPageHintReader
{

	protected ReportDocumentReader document;
	protected RAInputStream indexStream;
	protected RAInputStream hintsStream;
	protected long totalPage = -1;

	public PageHintReaderV2( ReportDocumentReader reader )
	{
		document = reader;
	}

	public void open( ) throws IOException
	{
		IDocArchiveReader reader = document.getArchive( );
		hintsStream = reader
				.getStream( ReportDocumentConstants.PAGEHINT_STREAM );
		indexStream = reader
				.getStream( ReportDocumentConstants.PAGEHINT_INDEX_STREAM );;
	}

	public void close( )
	{
		try
		{
			if ( hintsStream != null )
			{
				hintsStream.close( );
				hintsStream = null;
			}
		}
		catch ( IOException ex )
		{

		}

		try
		{
			if ( indexStream != null )
			{
				indexStream.close( );
				indexStream = null;
			}
		}
		catch ( IOException ex )
		{

		}
	}

	public long getTotalPage( )
	{
		if ( totalPage == -1 )
		{
			try
			{
				indexStream.seek( 0 );
				totalPage = indexStream.readLong( );
			}
			catch ( IOException ex )
			{
				ex.printStackTrace( );
			}
		}
		return totalPage;
	}

	public IPageHint getPageHint( long pageNumber )
	{
		try
		{
			indexStream.seek( pageNumber * 8 );
			long offset = indexStream.readLong( );
			hintsStream.seek( offset );
			PageHint hint = new PageHint( );
			hint.readObject( new DataInputStream( hintsStream ) );
			return hint;
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
		return null;
	}

	public long findPage( long offset )
	{
		for ( long page = 1; page <= totalPage; page++ )
		{
			IPageHint pageHint = getPageHint( page );
			for ( int section = 0; section < pageHint.getSectionCount( ); section++ )
			{
				long start = pageHint.getSectionStart( section );
				long end = pageHint.getSectionEnd( section );
				if ( start >= offset && end <= offset )
				{
					return page;
				}
			}
		}
		return -1;
	}

}
