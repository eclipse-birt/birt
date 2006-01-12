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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

public class ReportDocumentReader
		implements
			IReportDocument,
			ReportDocumentConstants
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	private ReportEngine engine;
	private IDocArchiveReader archive;
	private String designName;
	private IReportRunnable reportRunnable;
	private HashMap parameters;
	private HashMap globalVariables;
	private HashMap bookmarks;
	private List pageHints;
	/** root TOC, id is "/" */
	private TOCNode tocRoot;
	/** tocId, TOCNode map */
	private HashMap tocMap;

	public ReportDocumentReader( ReportEngine engine, IDocArchiveReader archive )
	{
		this.engine = engine;
		this.archive = archive;
		try
		{
			archive.open( );
			loadCoreStream( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed to open the archive", e ); //$NON-NLS-1$
		}
	}

	public IDocArchiveReader getArchive( )
	{
		return this.archive;
	}

	protected void loadCoreStream( ) throws Exception
	{
		RAInputStream in = archive.getStream( CORE_STREAM );
		try
		{
			DataInputStream di = new DataInputStream( new BufferedInputStream(
					in ) );

			// check the design name
			checkVersion( di );

			// load the report design name
			designName = IOUtil.readString( di );
			// load the report paramters
			parameters = (HashMap) IOUtil.readMap( di );
			// load the persistence object
			globalVariables = (HashMap) IOUtil.readMap( di );
		}
		finally
		{
			if ( in != null )
			{
				in.close( );
			}
		}
	}

	protected void checkVersion( DataInputStream di ) throws IOException
	{
		String tag = IOUtil.readString( di );
		String version = IOUtil.readString( di );
		if ( !REPORT_DOCUMENT_TAG.equals( tag )
				|| !REPORT_DOCUMENT_VERSION_1_0_0.equals( version ) )
		{
			logger
					.log(
							Level.SEVERE,
							"unsupport report document tag" + tag + " version " + version ); //$NON-NLS-1$
		}
	}

	public void close( )
	{
		try
		{
			archive.close( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed to close the archive", e ); //$NON-NLS-1$
		}
	}

	public InputStream getDesignStream( )
	{
		try
		{
			return archive.getStream( DESIGN_STREAM );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to open the design!", ex ); //$NON-NLS-1$
			return null;
		}
	}

	public IReportRunnable getReportRunnable( )
	{
		if ( reportRunnable == null )
		{
			String name = archive.getName( );
			InputStream stream = getDesignStream( );
			if ( stream != null )
			{
				try
				{
					reportRunnable = engine.openReportDesign( name, stream );

				}
				catch ( Exception ex )
				{
					logger.log( Level.SEVERE,
							"Failed to get the report runnable", //$NON-NLS-1$
							ex );
				}
				finally
				{
					try
					{
						stream.close( );
					}
					catch ( IOException ex )
					{
					}
				}
			}
		}
		return reportRunnable;
	}

	public Map getParameterValues( )
	{
		return parameters;
	}

	public long getPageCount( )
	{
		if ( pageHints == null )
		{
			loadPageHintStream( );
		}
		return pageHints.size( );
	}

	public PageHint getPageHint( long pageNumber )
	{
		if ( pageHints == null )
		{
			loadPageHintStream( );
		}
		if ( pageHints != null )
		{
			if ( pageNumber >= 1 && pageNumber <= pageHints.size( ) )
			{
				return (PageHint) pageHints.get( (int) ( pageNumber - 1 ) );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getPageNumber(java.lang.String)
	 */
	public long getPageNumber( String bookmark )
	{
		if ( bookmarks == null )
		{
			loadBookmarks( );
		}
		Object number = bookmarks.get( bookmark );
		if ( number instanceof Number )
		{
			return ( (Number) number ).intValue( );
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getBookmarks()
	 */
	public List getBookmarks( )
	{
		if ( bookmarks == null )
		{
			loadBookmarks( );
		}
		ArrayList list = new ArrayList( );
		list.addAll( bookmarks.keySet( ) );
		return list;
	}

	/**
	 * @param bookmark
	 *            the bookmark that a page number is to be retrieved upon
	 * @return the page number that the bookmark appears
	 */
	public long getBookmark( String bookmark )
	{
		if ( bookmarks == null )
		{
			loadBookmarks( );
		}
		Long pageNumber = (Long) bookmarks.get( bookmark );
		if ( pageNumber == null )
		{
			return 0;
		}
		return pageNumber.longValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOC(java.lang.String)
	 */
	public TOCNode findTOC( String tocNodeId )
	{
		if ( tocRoot == null )
		{
			loadTOC( );
		}
		if ( tocNodeId == null || "/".equals( tocNodeId ) ) //$NON-NLS-1$
		{
			return tocRoot;
		}
		return (TOCNode) tocMap.get( tocNodeId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getChildren(java.lang.String)
	 */
	public List getChildren( String tocNodeId )
	{
		TOCNode node = findTOC( tocNodeId );
		if ( node != null )
		{
			return node.getChildren( );
		}
		return null;
	}

	/**
	 * load the TOC from the stream.
	 */
	protected void loadTOC( )
	{
		tocRoot = new TOCNode( );
		if ( archive.exists( TOC_STREAM ) )
		{
			InputStream in = null;
			try
			{
				in = archive.getStream( TOC_STREAM );
				DataInputStream input = new DataInputStream( in );
				TOCBuilder.read( tocRoot, input );
			}
			catch ( Exception ex )
			{
				logger.log( Level.SEVERE, "Failed to load the TOC", ex ); //$NON-NLS-1$
			}
			finally
			{
				if ( in != null )
				{
					try
					{
						in.close( );
					}
					catch ( IOException ex )
					{
					}
				}
			}
		}
		tocMap = new HashMap( );
		addTOCEntry( tocRoot, tocMap );
	}

	/**
	 * add the TOC node into the map for search.
	 * 
	 * @param node
	 *            TOC cache.
	 * @param map
	 *            map contains the (name, TOC) pair.
	 */
	private void addTOCEntry( TOCNode node, HashMap map )
	{
		map.put( node.getNodeID( ), node );
		Iterator iter = node.getChildren( ).iterator( );
		while ( iter.hasNext( ) )
		{
			TOCNode child = (TOCNode) iter.next( );
			addTOCEntry( child, map );
		}
	}

	private void loadBookmarks( )
	{
		bookmarks = new HashMap( );
		if ( !archive.exists( BOOKMARK_STREAM ) )
		{
			return;
		}
		RAInputStream in = null;
		try
		{
			in = archive.getStream( BOOKMARK_STREAM );
			DataInputStream di = new DataInputStream( new BufferedInputStream(
					in ) );
			long count = IOUtil.readLong( di );
			for ( long i = 0; i < count; i++ )
			{
				String bookmark = IOUtil.readString( di );
				long pageNumber = IOUtil.readLong( di );
				bookmarks.put( bookmark, new Long( pageNumber ) );
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "failed to load the bookmarks", ex ); //$NON-NLS-1$
		}
		finally
		{
			if ( in != null )
			{
				try
				{
					in.close( );
				}
				catch ( Exception ex )
				{
				};
			}
		}
	}

	private void loadPageHintStream( )
	{
		RAInputStream in = null;
		try
		{
			pageHints = new ArrayList( );
			in = archive.getStream( PAGEHINT_STREAM );
			DataInputStream di = new DataInputStream( new BufferedInputStream(
					in ) );
			long pageCount = IOUtil.readLong( di );
			for ( long i = 0; i < pageCount; i++ )
			{
				PageHint hint = new PageHint( );
				hint.readObject( di );
				pageHints.add( hint );
			}
			in.close( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page hints", ex ); //$NON-NLS-1$
		}
		finally
		{
			if ( in != null )
			{
				try
				{
					in.close( );
				}
				catch ( Exception ex )
				{
				};
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getName()
	 */
	public String getName( )
	{
		return archive.getName( );
	}

	public String getDesignName( )
	{
		return designName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getGlobalVariables()
	 */
	public Map getGlobalVariables( String option )
	{
		return globalVariables;
	}

}
