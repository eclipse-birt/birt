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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

public class ReportDocumentReader implements IReportDocument
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );
	protected static final String DESIGN_STREAM = "/design";		//$NON-NLS-1$
	protected static final String PARAMTER_STREAM = "/paramter";	//$NON-NLS-1$
	protected static final String BOOKMARK_STREAM = "/bookmark";	//$NON-NLS-1$
	protected static final String PAGEHINT_STREAM = "/pages";		//$NON-NLS-1$
	protected static final String TOC_STREAM = "/toc";				//$NON-NLS-1$
	protected static final String CONTENT_FOLDER = "/content";		//$NON-NLS-1$

	private ReportEngine engine;
	private IDocArchiveReader archive;
	private IReportRunnable reportRunnable;
	private HashMap parameters;
	private HashMap bookmarks;
	private List pageHints;
	private TOCNode tocRoot;
	private HashMap tocMap;

	public ReportDocumentReader( ReportEngine engine, IDocArchiveReader archive )
	{
		this.engine = engine;
		this.archive = archive;
		try
		{
			archive.open( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed to open the archive", e );	//$NON-NLS-1$
		}

	}

	public IDocArchiveReader getArchive( )
	{
		return this.archive;
	}

	public void close( )
	{
		try
		{
			archive.close( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed to close the archive", e );	//$NON-NLS-1$
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
			logger.log( Level.SEVERE, "Failed to open the design!", ex );	//$NON-NLS-1$
			return null;
		}
	}

	public IReportRunnable getReportRunnable( )
	{
		if ( reportRunnable == null )
		{
			try
			{
				reportRunnable = engine.openReportDesign( getDesignStream( ) );
			}
			catch ( Exception ex )
			{
				logger.log( Level.SEVERE, "Failed to get the report runnable",	//$NON-NLS-1$
						ex );
			}
		}
		return reportRunnable;
	}

	public HashMap getParameterValues( )
	{
		if ( parameters == null )
		{
			loadParamters( );
		}
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
	 * @param bookmark the bookmark that a page number is to be retrieved upon
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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOC(java.lang.String)
	 */
	public TOCNode findTOC( String tocNodeId )
	{
		if ( tocRoot == null )
		{
			loadTOC( );
		}
		if ( tocNodeId == null || "/".equals( tocNodeId ) )	//$NON-NLS-1$
		{
			return tocRoot;
		}
		return (TOCNode) tocMap.get( tocNodeId );
	}

	/* (non-Javadoc)
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
		try
		{
			tocRoot = TOCBuilder.read( archive.getStream( TOC_STREAM ) );
			tocMap = new HashMap( );
			if ( tocRoot != null )
			{
				addTOCEntry( tocRoot, tocMap );
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the TOC", ex );	//$NON-NLS-1$
		}
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

	/**
	 * load an object from the file. File is written using ObjectOuputStream and
	 * contains at least one valid object.
	 * 
	 * @param file
	 *            source file
	 * @return the object read from the file.
	 */
	protected Object loadObject( InputStream istream ) throws Exception
	{
		try
		{
			ObjectInputStream di = new ObjectInputStream( istream );
			return di.readObject( );
		}
		finally
		{
			if ( istream != null )
			{
				try
				{
					istream.close( );
				}
				catch ( IOException ex )
				{
				}
			}
		}
	}

	private void loadBookmarks( )
	{
		try
		{
			bookmarks = (HashMap) loadObject( archive
					.getStream( BOOKMARK_STREAM ) );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "failed to load the bookmarks", ex );	//$NON-NLS-1$
		}
	}

	private void loadParamters( )
	{
		try
		{
			parameters = (HashMap) loadObject( archive
					.getStream( PARAMTER_STREAM ) );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the paramters", ex ); 	//$NON-NLS-1$
		}
	}

	private void loadPageHintStream( )
	{
		try
		{
			pageHints = (ArrayList) loadObject( archive
					.getStream( PAGEHINT_STREAM ) );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page hints", ex );	//$NON-NLS-1$
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

}
