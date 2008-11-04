/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.index.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.internal.index.IDocumentIndexReader;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

public class DocumentIndexReaderV2
		implements
			IDocumentIndexReader,
			ReportDocumentConstants,
			DocumentIndexV2Constants
{

	private IndexReader bookmarks;
	private IndexReader reportlets;
	private IndexReader pageNumbers;

	private IDocArchiveReader archive;

	public DocumentIndexReaderV2( IDocArchiveReader archive )
			throws IOException
	{
		this.archive = archive;
	}

	public int getVersion( )
	{
		return VERSION_1;
	}

	public void close( )
	{
		if ( bookmarks != null )
		{
			try
			{
				bookmarks.close( );
			}
			catch ( IOException ex )
			{
			}
			bookmarks = null;
		}

		if ( reportlets != null )
		{
			try
			{
				reportlets.close( );
			}
			catch ( IOException ex )
			{

			}
			reportlets = null;
		}

		if ( pageNumbers != null )
		{
			try
			{
				pageNumbers.close( );
			}
			catch ( IOException ex )
			{

			}
			pageNumbers = null;
		}
	}

	public long getOffsetOfBookmark( String bookmark ) throws IOException
	{
		if ( bookmarks == null )
		{
			bookmarks = createIndexReader( REPORTLET_BOOKMARK_INDEX_STREAM );
		}
		if ( bookmarks != null )
		{
			return bookmarks.get( bookmark );
		}
		return -1L;
	}

	public long getOffsetOfInstance( String instanceId ) throws IOException
	{
		if ( reportlets == null )
		{
			reportlets = createIndexReader( REPORTLET_ID_INDEX_STREAM );
		}
		if ( reportlets != null )
		{
			return reportlets.get( instanceId );
		}
		return -1L;
	}

	public long getPageOfBookmark( String bookmark ) throws IOException
	{
		if ( pageNumbers == null )
		{
			pageNumbers = createIndexReader( BOOKMARK_STREAM );
		}
		if ( pageNumbers != null )
		{
			return pageNumbers.get( bookmark );
		}
		return -1L;
	}

	public List<String> getBookmarks( ) throws IOException
	{
		if ( pageNumbers == null )
		{
			pageNumbers = createIndexReader( BOOKMARK_STREAM );
		}
		if ( pageNumbers != null )
		{
			final ArrayList<String> allBookmarks = new ArrayList<String>( );
			pageNumbers.forAllKeys( new IndexReader.KeyListener( ) {

				public void onKey( String key )
				{
					if ( key != null && !key.startsWith( TOCBuilder.TOC_PREFIX ) )
					{
						allBookmarks.add( key );
					}
				}
			} );
			return allBookmarks;
		}
		return null;
	}

	private IndexReader createIndexReader( String stream ) throws IOException
	{
		return new IndexReader( archive, stream );
	}
}
