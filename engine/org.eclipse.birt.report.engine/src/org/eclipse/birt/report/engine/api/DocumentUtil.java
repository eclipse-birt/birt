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

package org.eclipse.birt.report.engine.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.api.impl.BookmarkInfo;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.internal.index.v2.DocumentIndexReaderV2;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

import com.ibm.icu.util.ULocale;

public class DocumentUtil
{

	public static void copy( IArchiveFile source, IArchiveFile target,
			IReportRunnable runnable ) throws EngineException
	{
		try
		{
			ArchiveUtil.copy( source, target );
		}
		catch ( IOException ex )
		{
			throw new EngineException( "exception when copying archives", ex );
		}

		if ( runnable != null )
		{
			IDocumentWriter writer = runnable.getReportEngine( )
					.openDocumentWriter( target );
			writer.setRunnable( runnable );
			writer.close( );
		}

	}

	/**
	 * @return a collection of all bookmark info; null is returned if no
	 *         bookmark info.
	 */
	public static Collection<IBookmarkInfo> getBookmarks(
			IReportDocument document, Locale locale ) throws EngineException
	{
		DocumentIndexReaderV2 indexReader = null;
		try
		{
			IDocArchiveReader archive = document.getArchive( );
			indexReader = new DocumentIndexReaderV2( archive );

			List<BookmarkContent> bookmarks = indexReader.getBookmarkContents( );
			if ( bookmarks == null )
			{
				return null;
			}

			ReportDesignHandle report = document.getReportDesign( );
			ArrayList<IBookmarkInfo> results = new ArrayList<IBookmarkInfo>( );
			for ( BookmarkContent bookmark : bookmarks )
			{
				long designId = bookmark.getElementId( );
				DesignElementHandle handle = report.getElementByID( designId );
				if ( handle == null )
					continue;
				String elementType = handle.getName( );
				String displayName = null;
				if ( handle instanceof ReportItemHandle )
				{
					displayName = ( (ReportItemHandle) handle )
							.getBookmarkDisplayName( );
				}
				if ( locale != null )
				{
					if ( handle instanceof ReportElementHandle )
					{
						ReportElementHandle elementHandle = (ReportElementHandle) handle;
						displayName = ModuleUtil.getExternalizedValue(
								elementHandle, bookmark.getBookmark( ),
								displayName, ULocale.forLocale( locale ) );

					}
				}
				results.add( new BookmarkInfo( bookmark.getBookmark( ),
						displayName, elementType ) );
			}
			return results;
		}
		catch ( IOException ex )
		{
			throw new EngineException( "exception when fetching bookmarks", ex );
		}
		finally
		{
			if ( indexReader != null )
			{
				indexReader.close( );
			}
		}
	}
}
