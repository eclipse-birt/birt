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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.v1.PageHintReaderV1;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintReaderV2;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCTree;
import org.eclipse.birt.report.model.api.ModuleOption;

import com.ibm.icu.util.ULocale;

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
	private Map moduleOptions;
	/*
	 * version, paramters, globalVariables are loaded from core stream.
	 */
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
	private TOCTree tocTree;
	/** Map from the id to offset */
	private HashMap reportletsIndexById;
	/** Map from the bookmark to offset */
	private HashMap reportletsIndexByBookmark;
	/** Design name */
	private String systemId;
	
	private int checkpoint = CHECKPOINT_INIT;
	
	private long pageCount;

	public ReportDocumentReader( IReportEngine engine, IDocArchiveReader archive ) throws EngineException
	{
		this( null, engine, archive );
	}

	public ReportDocumentReader( String systemId, IReportEngine engine, IDocArchiveReader archive ) throws EngineException
	{
		this.engine = engine;
		this.archive = archive;
		this.systemId = systemId;
		this.moduleOptions = new HashMap( );
		this.moduleOptions.put( ModuleOption.PARSER_SEMANTIC_CHECK_KEY,
				Boolean.FALSE );
		
		try
		{
			archive.open( );
		}
		catch ( Exception e )
		{
			throw new EngineException( "Failed to open the report document", e );
		}
		try
		{
			doRefresh( );
		}
		catch ( EngineException ee )
		{
			try
			{
				archive.close(); 
			}
			catch(Exception ex)
			{
			}
			throw ee; 
		}
	}

	/**
	 * set the options used to parse the report design in the report document.
	 * If the options has no PARSER_SEMANTIC_CHECK_KEY, set it to FALSE.
	 * 
	 * @param options
	 *            options used to control the design parser.
	 */
	void setModuleOptions( Map options )
	{
		moduleOptions = new HashMap( );
		moduleOptions.putAll( options );
		Object semanticCheck = moduleOptions
				.get( ModuleOption.PARSER_SEMANTIC_CHECK_KEY );
		if ( semanticCheck != null )
		{
			moduleOptions.put( ModuleOption.PARSER_SEMANTIC_CHECK_KEY,
					Boolean.FALSE );
		}
	}
	
	
	public IDocArchiveReader getArchive( )
	{
		return this.archive;
	}

	public String getVersion( )
	{
		return version;
	}

	protected class ReportDocumentCoreInfo
	{
		String version;
		HashMap globalVariables;
		HashMap parameters;
		String systemId;
		int checkpoint;			
		long pageCount;
	}
	
	public void refresh()
	{
		if ( checkpoint == CHECKPOINT_END )
		{
			return;
		}
		try
		{
			doRefresh();
		}
		catch( EngineException ee )
		{
			logger.log( Level.SEVERE, "Failed to refresh", ee ); //$NON-NLS-1$
		}
	}
	
	protected void doRefresh( ) throws EngineException
	{
		try
		{
			Object lock = archive.lock( CORE_STREAM );
			try
			{
				// load info into a document info object
				ReportDocumentCoreInfo documentInfo = new ReportDocumentCoreInfo( );
				documentInfo.checkpoint = CHECKPOINT_INIT;
				documentInfo.pageCount = PAGECOUNT_INIT;
				RAInputStream in = archive.getStream( CHECKPOINT_STREAM );
				if ( in == null )
				{
					// no check point stream, old version, return -1
					documentInfo.checkpoint = CHECKPOINT_END;
					if ( pageHintReader == null )
					{
						createPageHintReader( );
					}
					if ( pageHintReader != null )
					{
						documentInfo.pageCount = pageHintReader.getTotalPage( );
					}
				}
				else
				{
					try
					{
						DataInputStream di = new DataInputStream( in );
						documentInfo.checkpoint = IOUtil.readInt( di );
						documentInfo.pageCount = IOUtil.readLong( di );
					}
					finally
					{
						if ( in != null )
						{
							in.close( );
						}
					}
					if ( documentInfo.checkpoint == checkpoint )
					{
						return;
					}
				}

				in = archive.getStream( CORE_STREAM );
				try
				{
					DataInputStream di = new DataInputStream( in );

					// check the design name
					documentInfo.version = checkVersion( di );

					// load the report design name
					String orgSystemId = IOUtil.readString( di );
					if ( systemId == null )
					{
						documentInfo.systemId = orgSystemId;
					}
					else
					{
						documentInfo.systemId = systemId;
					}
					// load the report paramters
					Map originalParameters = IOUtil.readMap( di );
					documentInfo.parameters = convertToCompatibleParameter( originalParameters );
					// load the persistence object
					documentInfo.globalVariables = (HashMap) IOUtil
							.readMap( di );

				}
				finally
				{
					if ( in != null )
					{
						in.close( );
					}
				}

				// save the document info into the object.
				checkpoint = documentInfo.checkpoint;
				pageCount = documentInfo.pageCount;
				version = documentInfo.version;
				systemId = documentInfo.systemId;
				globalVariables = documentInfo.globalVariables;
				parameters = documentInfo.parameters;
			}
			finally
			{
				archive.unlock( lock );
			}
		}
		catch ( EngineException ee )
		{
			throw ee;
		}
		catch ( Exception ex )
		{
			throw new EngineException( "document refresh failed", ex );
		}
	}


	private HashMap convertToCompatibleParameter( Map parameters )
	{
		if ( parameters == null )
		{
			return null;
		}
		HashMap result = new HashMap( );
		Iterator iterator = parameters.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			Object key = entry.getKey( );
			Object valueObj = entry.getValue( );
			ParameterAttribute paramAttr = null;
			if ( valueObj instanceof ParameterAttribute )
			{
				paramAttr = (ParameterAttribute) valueObj;
			}
			else if ( valueObj instanceof Object[] )
			{
				Object[] values = (Object[]) valueObj;
				if ( values.length == 2 )
				{
					Object value = values[0];
					String displayText = (String) values[1];
					paramAttr = new ParameterAttribute( value, displayText );
				}
			}
			if ( paramAttr == null )
			{
				paramAttr = new ParameterAttribute( valueObj, null );
			}
			result.put( key, paramAttr );
		}
		return result;
	}

	protected String checkVersion( DataInputStream di ) throws IOException, EngineException
	{
		String tag = IOUtil.readString( di );
		String version = IOUtil.readString( di );
		if ( !REPORT_DOCUMENT_TAG.equals( tag )
				|| !( REPORT_DOCUMENT_VERSION_1_2_1.equals( version ) || REPORT_DOCUMENT_VERSION_2_1_0
						.equals( version ) ) )
		{
			throw new EngineException( "unsupport report document tag" + tag + " version " + version ); //$NON-NLS-1$
		}
		return version;		
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
			if ( systemId == null )
			{
				name = archive.getName( );
			}
			else
			{
				name = systemId;
			}
			InputStream stream = getDesignStream( );
			if ( stream != null )
			{
				try
				{
					reportRunnable = engine.openReportDesign( name, stream, moduleOptions );
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
		Map result = new HashMap( );
		if ( parameters != null )
		{
			Iterator iterator = parameters.entrySet( ).iterator( );
			while ( iterator.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iterator.next( );
				String name = (String) entry.getKey( );
				ParameterAttribute value = (ParameterAttribute) entry
						.getValue( );
				result.put( name, value.getValue( ) );
			}
		}
		return result;
	}
	
	public Map getParameterDisplayTexts( )
	{
		Map result = new HashMap( );
		if ( parameters != null )
		{
			Iterator iterator = parameters.entrySet( ).iterator( );
			while ( iterator.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iterator.next( );
				String name = (String) entry.getKey( );
				ParameterAttribute value = (ParameterAttribute) entry
						.getValue( );
				result.put( name, value.getDisplayText( ) );
			}
		}
		return result;
	}

	public long getPageCount( )
	{
		return pageCount;
	}

	public IPageHint getPageHint( long pageNumber )
	{
		if ( pageHintReader == null )
		{
			createPageHintReader( );
		}
		if (pageHintReader != null)
		{
			try
			{
				return pageHintReader.getPageHint( pageNumber );
			}
			catch(IOException ex)
			{
				logger.log( Level.WARNING, "Failed to load page hint "
						+ pageNumber, ex );
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
		if ( !isComplete() )
		{
			return -1;
		}
	
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
		if ( !isComplete() )
		{
			return null;
		}
		if ( bookmarks == null )
		{
			loadBookmarks( );
		}
		ArrayList list = new ArrayList( );
		Set bookmarkSet = bookmarks.keySet( );
		Iterator iterator = bookmarkSet.iterator( );
		while ( iterator.hasNext( ) )
		{
			String bookmark = (String)iterator.next( );
			if ( bookmark != null
					&& !bookmark.startsWith( TOCBuilder.TOC_PREFIX ) )
			{
				list.add( bookmark );
			}
		}
		return list;
	}

	/**
	 * @param bookmark
	 *            the bookmark that a page number is to be retrieved upon
	 * @return the page number that the bookmark appears
	 */
	public long getBookmark( String bookmark )
	{
		if ( !isComplete() )
		{
			return -1;
		}
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

	public ITOCTree getTOCTree( String format, ULocale locale )
	{
		if ( !isComplete() )
		{
			return null;
		}
		if ( tocTree == null )
		{
			loadTOC( );
		}
		TOCTree result = new TOCTree( tocTree.getTOCRoot( ), format, locale );
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOC(java.lang.String)
	 */
	public TOCNode findTOC( String tocNodeId )
	{
		if ( !isComplete() )
		{
			return null;
		}
		if ( tocTree == null )
		{
			loadTOC( );
		}
		ITOCTree tree = getTOCTree( "all", ULocale.getDefault( ) );
		return tree.findTOC( tocNodeId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOCByName(java.lang.String)
	 */
	public List findTOCByName( String tocName )
	{
		if ( !isComplete() )
		{
			return null;
		}
		if ( tocTree == null )
		{
			loadTOC( );
		}
		ITOCTree tree = getTOCTree( "all", ULocale.getDefault( ) );
		return tree.findTOCByValue( tocName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#getChildren(java.lang.String)
	 */
	public List getChildren( String tocNodeId )
	{
		if ( !isComplete() )
		{
			return null;
		}
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
		tocTree = new TOCTree( );
		if ( archive.exists( TOC_STREAM ) )
		{
			InputStream in = null;
			try
			{
				in = archive.getStream( TOC_STREAM );
				DataInputStream input = new DataInputStream( in );
				TOCBuilder.read( tocTree, input );
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
			DataInputStream di = new DataInputStream( in );
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
		return globalVariables;
	}

	public long getPageNumber( InstanceID iid )
	{
		if ( !isComplete() )
		{
			return -1;
		}
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
			if ( pageHintReader != null )
			{
				try
				{
					return pageHintReader.findPage( offset );
				}
				catch ( IOException ex )
				{
					logger.log( Level.WARNING,
							"Failed to find page which contains " + iid, ex );
				}
			}
		}
		return -1;
	}

	public long getInstanceOffset( InstanceID iid )
	{
		if ( !isComplete() )
		{
			return -1l;
		}
		if ( iid == null )
		{
			return -1l;
		}
		if ( reportletsIndexById == null )
		{
			loadReportletStream( );
		}
		return getOffset( reportletsIndexById, iid.toString( ) );
	}

	public long getBookmarkOffset( String bookmark )
	{
		if ( !isComplete() )
		{
			return -1;
		}
		if ( bookmark == null )
		{
			return -1l;
		}
		if ( reportletsIndexByBookmark == null )
		{
			loadReportletStream( );
		}
		return getOffset( reportletsIndexByBookmark, bookmark );
	}

	private long getOffset( Map index, String key )
	{
		// version 1.0.0 don't support this feature
		if ( REPORT_DOCUMENT_VERSION_1_0_0.equals( getVersion( ) ) )
		{
			return -1;
		}
		Long offset = (Long) index.get( key );
		if ( offset != null )
		{
			return offset.longValue( );
		}
		return -1;
	}

	private void loadReportletStream( )
	{
		reportletsIndexById = new HashMap( );
		reportletsIndexByBookmark = new HashMap( );
		loadReportletStream( reportletsIndexById, REPORTLET_ID_INDEX_STREAM );
		loadReportletStream( reportletsIndexByBookmark,
				REPORTLET_BOOKMARK_INDEX_STREAM );
	}

	private void loadReportletStream( Map index, String streamName )
	{
		RAInputStream in = null;
		try
		{
			in = archive.getStream( streamName );
			DataInputStream di = new DataInputStream( in );
			long count = IOUtil.readLong( di );
			for ( long i = 0; i < count; i++ )
			{
				String key = IOUtil.readString( di );
				long offset = IOUtil.readLong( di );
				index.put( key, new Long( offset ) );
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#isComplete()
	 */
	public boolean isComplete( )
	{
		return checkpoint == CHECKPOINT_END;
	}
}
