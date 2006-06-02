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
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportDocumentLock;
import org.eclipse.birt.report.engine.api.IReportDocumentLockManager;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.v1.PageHintReaderV1;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintReaderV2;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.IResourceLocator;

public class ReportDocumentReader
		implements
			IReportDocument,
			ReportDocumentConstants
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	private IReportEngine engine;
	private IDocArchiveReader archive;
	private IReportRunnable reportRunnable;
	private IResourceLocator locator;
	/*
	 * version, paramters, globalVariables are loaded from core stream.
	 */
	private boolean coreStreamLoaded;
	private String version;
	private HashMap parameters;
	private HashMap globalVariables;
	/**
	 * bookmark is loadded from bookmark stream.
	 */
	private HashMap bookmarks;
	/**
	 * used to load the page hints
	 */
	private IPageHintReader pageHintReader;
	/** root TOC, id is "/" */
	private TOCNode tocRoot;
	/** tocId, TOCNode map */
	private HashMap tocMapByID;
	/** Map from TOC name to a list of TOCNodes */
	private HashMap tocMapByName;
	/** Map from the id to offset */
	private HashMap reportlets;
	/** Design name */
	private String designName;

	public ReportDocumentReader( IReportEngine engine, IDocArchiveReader archive )
	{
		this( null, engine, archive );
	}

	public ReportDocumentReader( String designName, IReportEngine engine, IDocArchiveReader archive )
	{
		this.engine = engine;
		this.archive = archive;
		this.designName = designName;
		try
		{
			archive.open( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed to open the archive", e ); //$NON-NLS-1$
		}
	}

	
	void setResourceLocator( IResourceLocator locator )
	{
		this.locator = locator;
	}
	
	
	public IDocArchiveReader getArchive( )
	{
		return this.archive;
	}

	public String getVersion( )
	{
		loadCoreStream( );
		return version;
	}

	/**
	 * create a locker used to lock the report document
	 * 
	 * @return
	 * @throws BirtException
	 */
	protected IReportDocumentLock lock( String documentName )
			throws BirtException
	{
		IReportDocumentLockManager manager = null;
		if ( engine != null )
		{
			EngineConfig config = engine.getConfig( );
			if ( config != null )
			{
				manager = config.getReportDocumentLockManager( );
			}
		}
		if ( manager == null )
		{
			manager = ReportDocumentLockManager.getInstance( );
		}
		return manager.lock( documentName );
	}

	protected void loadCoreStream( )
	{
		if ( coreStreamLoaded )
		{
			return;
		}

		try
		{
			IReportDocumentLock lock = lock( getName( ) );
			synchronized ( lock )
			{
				RAInputStream in = archive.getStream( CORE_STREAM );
				try
				{
					DataInputStream di = new DataInputStream(
							new BufferedInputStream( in ) );

					// check the design name
					checkVersion( di );

					// load the report design name, never used
					IOUtil.readString( di );
					// load the report paramters
					parameters = (HashMap) IOUtil.readMap( di );
					// load the persistence object
					globalVariables = (HashMap) IOUtil.readMap( di );
					coreStreamLoaded = true;
				}
				finally
				{
					if ( in != null )
					{
						in.close( );
					}
				}
				lock.unlock( );
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "load cores tream failed", ex );
		}
	}

	protected void checkVersion( DataInputStream di ) throws IOException
	{
		String tag = IOUtil.readString( di );
		version = IOUtil.readString( di );
		if ( !REPORT_DOCUMENT_TAG.equals( tag )
				|| !( REPORT_DOCUMENT_VERSION_1_0_0.equals( version ) || REPORT_DOCUMENT_VERSION_1_2_1
						.equals( version ) ) )
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
			if ( pageHintReader != null )
			{
				pageHintReader.close( );
			}
			pageHintReader = null;
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
			String name = null;
			if ( designName == null )
			{
				name = archive.getName( );
			}
			else
			{
				name = designName;
			}
			InputStream stream = getDesignStream( );
			if ( stream != null )
			{
				try
				{
					reportRunnable = engine.openReportDesign( name, stream, locator );
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
		loadCoreStream( );
		return parameters;
	}

	public long getPageCount( )
	{
		if ( pageHintReader == null )
		{
			createPageHintReader( );
		}
		return pageHintReader.getTotalPage( );
	}

	public IPageHint getPageHint( long pageNumber )
	{
		if ( pageHintReader == null )
		{
			createPageHintReader( );
		}
		return pageHintReader.getPageHint( pageNumber );
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
		return (TOCNode) tocMapByID.get( tocNodeId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOCByName(java.lang.String)
	 */
	public List findTOCByName( String tocName )
	{
		if ( tocName == null )
		{
			return null;
		}
		if ( tocRoot == null )
		{
			loadTOC( );
		}
		return (List) tocMapByName.get( tocName );
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
		tocMapByID = new HashMap( );
		tocMapByName = new HashMap( );
		generateTOCIndex( tocRoot );
	}

	/**
	 * add the TOC node into the map for search.
	 * 
	 * @param node
	 *            TOC cache.
	 * @param map
	 *            map contains the (id, TOC) pair.
	 */
	private void generateTOCIndex( TOCNode node )
	{
		tocMapByID.put( node.getNodeID( ), node );
		addTOCNameEntry( node, tocMapByName );
		Iterator iter = node.getChildren( ).iterator( );
		while ( iter.hasNext( ) )
		{
			TOCNode child = (TOCNode) iter.next( );
			generateTOCIndex( child );
		}
	}

	/**
	 * Add a toc node into the map which cache the map from toc display string
	 * to nodes.
	 * 
	 * @param node
	 *            the node.
	 * @param map
	 *            the map.
	 */
	private void addTOCNameEntry( TOCNode node, HashMap map )
	{
		List tocs = (List) map.get( node.getDisplayString( ) );
		if ( tocs == null )
		{
			tocs = new ArrayList( );
			map.put( node.getDisplayString( ), tocs );
		}
		tocs.add( node );
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

	private void createPageHintReader( )
	{
		if ( REPORT_DOCUMENT_VERSION_1_0_0.equals( getVersion( ) ) )
		{
			pageHintReader = new PageHintReaderV1( this );
		}
		else
		{
			pageHintReader = new PageHintReaderV2( this );
		}
		try
		{
			pageHintReader.open( );
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "can't open the page hint stream", ex );
			if ( pageHintReader != null )
			{
				pageHintReader.close( );
			}
			pageHintReader = null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getGlobalVariables()
	 */
	public Map getGlobalVariables( String option )
	{
		loadCoreStream( );
		return globalVariables;
	}

	public long getPageNumber( InstanceID iid )
	{
		// version 1.0.0 don't support this feature
		if ( REPORT_DOCUMENT_VERSION_1_0_0.equals( getVersion( ) ) )
		{
			return -1;
		}
		long offset = getInstanceOffset( iid );
		if ( offset != -1 )
		{
			if ( pageHintReader == null )
			{
				createPageHintReader( );
			}
			return pageHintReader.findPage( offset );
		}
		return -1;
	}

	public long getInstanceOffset( InstanceID iid )
	{
		// version 1.0.0 don't support this feature
		if ( REPORT_DOCUMENT_VERSION_1_0_0.equals( getVersion( ) ) )
		{
			return -1;
		}
		if ( reportlets == null )
		{
			loadReportletStream( );
		}
		Long offset = (Long) reportlets.get( iid.toString( ) );
		if ( offset != null )
		{
			return offset.longValue( );
		}
		return -1;
	}

	private void loadReportletStream( )
	{
		RAInputStream in = null;
		try
		{
			reportlets = new HashMap( );
			in = archive.getStream( REPORTLET_STREAM );
			DataInputStream di = new DataInputStream( new BufferedInputStream(
					in ) );
			long count = IOUtil.readLong( di );
			for ( long i = 0; i < count; i++ )
			{
				String instance = IOUtil.readString( di );
				long offset = IOUtil.readLong( di );
				reportlets.put( instance, new Long( offset ) );
			}
			in.close( );
		}
		catch ( Exception ex )
		{
			logger
					.log( Level.SEVERE,
							"Failed to load the reportlet stream", ex ); //$NON-NLS-1$
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

}
