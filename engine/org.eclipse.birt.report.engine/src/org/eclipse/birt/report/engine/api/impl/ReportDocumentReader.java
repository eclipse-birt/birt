/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.executor.ApplicationClassLoader;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.v4.InstanceIDComparator;
import org.eclipse.birt.report.engine.internal.executor.doc.Fragment;
import org.eclipse.birt.report.engine.ir.EngineIRReader;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCTree;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class ReportDocumentReader
		implements
			IInternalReportDocument,
			ReportDocumentConstants
{
	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	private ReportEngine engine;
	private IDocArchiveReader archive;
	private Map moduleOptions;
	/*
	 * version, paramters, globalVariables are loaded from core stream.
	 */
	private int coreVersion = -1;
	private Map properties = new HashMap( );
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
	
	private boolean sharedArchive;

	private ClassLoader applicationClassLoader;
	
	private IReportRunnable preparedRunnable = null;

	public ReportDocumentReader( ReportEngine engine,
			IDocArchiveReader archive, boolean sharedArchive )
			throws EngineException
	{
		this( null, engine, archive, sharedArchive );
	}

	public ReportDocumentReader( ReportEngine engine, IDocArchiveReader archive )
		throws EngineException
	{
		this( null, engine, archive, false );
	}

	public ReportDocumentReader( String systemId, ReportEngine engine,
			IDocArchiveReader archive, Map options) throws EngineException
	{
		this( systemId, engine, archive, false, options );
	}

	public ReportDocumentReader( String systemId, ReportEngine engine,
			IDocArchiveReader archive, boolean sharedArchive, Map options )
			throws EngineException
	{
		this.engine = engine;
		this.archive = archive;
		this.systemId = systemId;
		this.sharedArchive = sharedArchive;
		this.moduleOptions = new HashMap( );
		this.moduleOptions.put( ModuleOption.PARSER_SEMANTIC_CHECK_KEY,
				Boolean.FALSE );
		if ( options != null )
		{
			this.moduleOptions.putAll( options );
		}
		

		try
		{
			archive.open( );
			doRefresh( );
		}
		catch ( IOException ee )
		{
			close( );
			throw new EngineException( "Failed to open the report document", ee );
		}
	}
	
	public ReportDocumentReader( String systemId, ReportEngine engine,
			IDocArchiveReader archive, boolean sharedArchive )
			throws EngineException
	{
		this( systemId, engine, archive, sharedArchive, null );
	}

	public IDocArchiveReader getArchive( )
	{
		return this.archive;
	}

	public String getVersion( )
	{
		return (String) properties.get( BIRT_ENGINE_VERSION_KEY );
	}
	
	public String getProperty( String key )
	{
		return (String) properties.get( key );
	}

	protected class ReportDocumentCoreInfo
	{
		HashMap globalVariables;
		HashMap parameters;
		String systemId;
		int checkpoint;
		long pageCount;
		ClassLoader applicationClassLoader;
	}

	public void refresh( )
	{
		if ( checkpoint == CHECKPOINT_END )
		{
			return;
		}
		try
		{
			doRefresh( );
		}
		catch ( IOException ee )
		{
			logger.log( Level.SEVERE, "Failed to refresh", ee ); //$NON-NLS-1$
		}
	}

	protected void doRefresh( ) throws IOException
	{
		Object lock = archive.lock( CORE_STREAM );
		try
		{
			synchronized ( lock )
			{
				RAInputStream in = archive.getStream( CORE_STREAM );
				try
				{
					DataInputStream di = new DataInputStream( in );

					// check the document version and core stream version
					checkVersion( di );

					if ( coreVersion == -1 )
					{
						doOldRefresh( di );
					}
					else if ( coreVersion == 0 )
					{
						doRefreshV0( di );
					}
					else
					{
						throw new IOException(
								"unsupported core stream version: " +
										coreVersion );
					}
				}
				finally
				{
					in.close( );
				}
			}
		}
		finally
		{
			archive.unlock( lock );
		}
	}

	protected void doRefreshV0( DataInputStream di ) throws IOException
	{
		// load info into a document info object
		ReportDocumentCoreInfo documentInfo = new ReportDocumentCoreInfo( );
		documentInfo.checkpoint = CHECKPOINT_INIT;
		documentInfo.pageCount = PAGECOUNT_INIT;

		documentInfo.checkpoint = IOUtil.readInt( di );
		documentInfo.pageCount = IOUtil.readLong( di );

		if ( documentInfo.checkpoint == checkpoint )
		{
			return;
		}

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
		documentInfo.applicationClassLoader = getClassLoader( documentInfo.systemId );
		Map originalParameters = IOUtil.readMap( di,
				documentInfo.applicationClassLoader );
		documentInfo.parameters = convertToCompatibleParameter( originalParameters );
		// load the persistence object
		documentInfo.globalVariables = (HashMap) IOUtil.readMap( di,
				documentInfo.applicationClassLoader );

		// save the document info into the object.
		checkpoint = documentInfo.checkpoint;
		pageCount = documentInfo.pageCount;
		systemId = documentInfo.systemId;
		globalVariables = documentInfo.globalVariables;
		parameters = documentInfo.parameters;
		applicationClassLoader = documentInfo.applicationClassLoader;

		if ( documentInfo.checkpoint == CHECKPOINT_END )
		{
			bookmarks = readMap( di );
			tocTree = new TOCTree( );
			TOCBuilder.read( tocTree, di, applicationClassLoader );
			reportletsIndexById = readMap( di );
			reportletsIndexByBookmark = readMap( di );
		}
	}

	private HashMap readMap( DataInputStream di ) throws IOException
	{
		HashMap map = new HashMap( );
		long count = IOUtil.readLong( di );
		for ( long i = 0; i < count; i++ )
		{
			String bookmark = IOUtil.readString( di );
			long pageNumber = IOUtil.readLong( di );
			map.put( bookmark, new Long( pageNumber ) );
		}
		return map;
	}

	protected void doOldRefresh( DataInputStream coreStream )
			throws IOException
	{
		// load info into a document info object
		ReportDocumentCoreInfo documentInfo = new ReportDocumentCoreInfo( );
		documentInfo.checkpoint = CHECKPOINT_INIT;
		documentInfo.pageCount = PAGECOUNT_INIT;
		if ( !archive.exists( CHECKPOINT_STREAM ) )
		{
			// no check point stream, old version, return -1
			documentInfo.checkpoint = CHECKPOINT_END;
			initializePageHintReader( );
			if ( pageHintReader != null )
			{
				documentInfo.pageCount = pageHintReader.getTotalPage( );
			}
		}
		else
		{
			RAInputStream in = archive.getStream( CHECKPOINT_STREAM );

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

		// load the report design name
		String orgSystemId = IOUtil.readString( coreStream );
		if ( systemId == null )
		{
			documentInfo.systemId = orgSystemId;
		}
		else
		{
			documentInfo.systemId = systemId;
		}
		// load the report paramters
		documentInfo.applicationClassLoader = getClassLoader( documentInfo.systemId );
		Map originalParameters = IOUtil.readMap( coreStream,
				documentInfo.applicationClassLoader );
		documentInfo.parameters = convertToCompatibleParameter( originalParameters);
		// load the persistence object
		documentInfo.globalVariables = (HashMap) IOUtil.readMap( coreStream,
				documentInfo.applicationClassLoader );
		// save the document info into the object.

		checkpoint = documentInfo.checkpoint;
		pageCount = documentInfo.pageCount;
		systemId = documentInfo.systemId;
		globalVariables = documentInfo.globalVariables;
		parameters = documentInfo.parameters;
		applicationClassLoader = documentInfo.applicationClassLoader;
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

	protected void checkVersion( DataInputStream di ) throws IOException
	{
		String tag = IOUtil.readString( di );
		String docVersion = IOUtil.readString( di );
		if ( CORE_VERSION_0.equals( docVersion ) )
		{
			coreVersion = Integer.parseInt( CORE_VERSION_0
					.substring( CORE_VERSION_PREFIX.length( ) ) );
			docVersion = IOUtil.readString( di );
		}
		else if (CORE_VERSION_1.equals( docVersion ))
		{
			coreVersion = Integer.parseInt( CORE_VERSION_0
					.substring( CORE_VERSION_PREFIX.length( ) ) );
			docVersion = IOUtil.readString( di );
			properties = IOUtil.readMap( di );
		}

		String[] supportedVersions = new String[]{
				REPORT_DOCUMENT_VERSION_1_2_1, REPORT_DOCUMENT_VERSION_2_1_0,
				REPORT_DOCUMENT_VERSION_2_1_3};
		boolean supportedVersion = false;
		if ( REPORT_DOCUMENT_TAG.equals( tag ) )
		{
			for ( int i = 0; i < supportedVersions.length; i++ )
			{
				if ( supportedVersions[i].equals( docVersion ) )
				{
					supportedVersion = true;
					break;
				}
			}
		}
		if ( supportedVersion == false )
		{
			throw new IOException(
					"unsupport report document tag" + tag + " version " + docVersion ); //$NON-NLS-1$
		}
		
		if ( properties.get( BIRT_ENGINE_VERSION_KEY ) == null )
		{
			if ( REPORT_DOCUMENT_VERSION_1_2_1.equals( docVersion ) )
			{
				properties.put( BIRT_ENGINE_VERSION_KEY,
						BIRT_ENGINE_VERSION_2_1 );
			}
			else if ( REPORT_DOCUMENT_VERSION_2_1_0.equals( docVersion ) )
			{
				properties.put( BIRT_ENGINE_VERSION_KEY,
						BIRT_ENGINE_VERSION_2_1_RC5 );
			}
			else if ( REPORT_DOCUMENT_VERSION_2_1_3.equals( docVersion ) )
			{
				properties.put( BIRT_ENGINE_VERSION_KEY,
						BIRT_ENGINE_VERSION_2_1_3 );
			}
		}
		

		String version = getVersion( );
		//FIXME: test if the version is later than BIRT_ENGINE_VERSION

		if ( properties.get( DATA_EXTRACTION_TASK_VERSION_KEY ) == null )
		{
			// check the data extraction task version
			if ( BIRT_ENGINE_VERSION_2_1.equals( docVersion ) ||
					BIRT_ENGINE_VERSION_2_1_RC5.equals( version ) )
			{
				properties.put( DATA_EXTRACTION_TASK_VERSION_KEY,
						DATA_EXTRACTION_TASK_VERSION_0 );
			}
			else
			{
				properties.put( DATA_EXTRACTION_TASK_VERSION_KEY,
						DATA_EXTRACTION_TASK_VERSION_1 );
			}
		}
		// assign the page-hint version
		if ( properties.get( PAGE_HINT_VERSION_KEY ) == null )
		{
			properties.put( PAGE_HINT_VERSION_KEY, PAGE_HINT_VERSION_2 );
		}
	}

	public void close( )
	{
		try
		{
			if ( pageHintReader != null )
			{
				pageHintReader.close( );
				pageHintReader = null;
			}
			if ( archive != null )
			{
				if ( !sharedArchive )
				{
					archive.close( );
				}
				archive = null;
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed to close the archive", e ); //$NON-NLS-1$
		}
	}
	
	public InputStream getOriginalDesignStream( )
	{
		try
		{
			return archive.getStream( ORIGINAL_DESIGN_STREAM );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to open the design!", ex ); //$NON-NLS-1$
			return null;
		}
	}
	
	public InputStream getDesignStream( boolean isOriginal )
	{
		try
		{
			if(isOriginal && archive.exists( ORIGINAL_DESIGN_STREAM ))
			{
				return archive.getStream( ORIGINAL_DESIGN_STREAM );
			}
			return archive.getStream( DESIGN_STREAM );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to open the design!", ex ); //$NON-NLS-1$
			return null;
		}
	}
	
	private IReportRunnable getReportRunnable(boolean isOriginal, String systemId)
	{
		if( !isOriginal && preparedRunnable!=null )
		{
			return preparedRunnable;
		}
		IReportRunnable reportRunnable = null;
		String name = null;
		if ( systemId == null )
		{
			name = archive.getName( );
		}
		else
		{
			name = systemId;
		}
		InputStream stream = getDesignStream( isOriginal );
		if ( stream != null )
		{
			try
			{
				reportRunnable = (ReportRunnable) engine.openReportDesign(
						name, stream, moduleOptions );
				stream.close( );
			}
			catch ( Exception ex )
			{
				logger.log( Level.SEVERE, "Failed to get the report runnable", //$NON-NLS-1$
						ex );
			}
			finally
			{
				try
				{
					if ( stream != null )
					{
						stream.close( );
					}
				}
				catch ( IOException ex )
				{
				}
			}
		}
		if( !isOriginal && preparedRunnable == null )
		{
			 preparedRunnable = reportRunnable;
		}
		return reportRunnable;
	}

	public IReportRunnable getReportRunnable( )
	{
		return getReportRunnable( true, systemId );
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
		initializePageHintReader( );
		if ( pageHintReader != null )
		{
			try
			{
				return pageHintReader.getPageHint( pageNumber );
			}
			catch ( IOException ex )
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
		if ( !isComplete( ) )
		{
			return -1;
		}

		intializeBookmarks( );
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
		if ( !isComplete( ) )
		{
			return null;
		}
		intializeBookmarks( );
		ArrayList list = new ArrayList( );
		Set bookmarkSet = bookmarks.keySet( );
		Iterator iterator = bookmarkSet.iterator( );
		while ( iterator.hasNext( ) )
		{
			String bookmark = (String) iterator.next( );
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
		if ( !isComplete( ) )
		{
			return -1;
		}
		intializeBookmarks( );
		Long pageNumber = (Long) bookmarks.get( bookmark );
		if ( pageNumber == null )
		{
			return 0;
		}
		return pageNumber.longValue( );
	}

	public ITOCTree getTOCTree( String format, ULocale locale )
	{
		return getTOCTree( format, locale, TimeZone.getDefault( ) );
	}
	
	public ITOCTree getTOCTree( String format, ULocale locale, TimeZone timeZone )
	{
		if ( !isComplete( ) )
		{
			return null;
		}
		intializeTOC( );
		TOCTree result = new TOCTree( tocTree.getTOCRoot( ), format, locale,
				timeZone, ( (ReportRunnable) getReportRunnable( ) ).getReport( ) );
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#findTOC(java.lang.String)
	 */
	public TOCNode findTOC( String tocNodeId )
	{
		if ( !isComplete( ) )
		{
			return null;
		}
		intializeTOC( );
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
		if ( !isComplete( ) )
		{
			return null;
		}
		intializeTOC( );
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
		if ( !isComplete( ) )
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
	protected void intializeTOC( )
	{
		if ( tocTree != null )
		{
			return;
		}
		if ( coreVersion != -1 )
		{
			return;
		}
		synchronized ( this )
		{
			if ( tocTree != null )
			{
				return;
			}
			tocTree = new TOCTree( );
			if ( archive.exists( TOC_STREAM ) )
			{
				InputStream in = null;
				try
				{
					in = archive.getStream( TOC_STREAM );
					DataInputStream input = new DataInputStream( in );
					TOCBuilder.read( tocTree, input, applicationClassLoader );
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
	}

	private void intializeBookmarks( )
	{
		if ( bookmarks != null )
		{
			return;
		}
		if ( coreVersion != -1 )
		{
			return;
		}
		synchronized ( this )
		{
			if ( bookmarks != null )
			{
				return;
			}
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
	}

	private void initializePageHintReader( )
	{
		if ( pageHintReader != null )
		{
			return;
		}
		synchronized ( this )
		{
			if ( pageHintReader != null )
			{
				return;
			}
			try
			{
				pageHintReader = new PageHintReader( this );
			}
			catch ( IOException ex )
			{
				logger
						.log( Level.SEVERE, "can't open the page hint stream",
								ex );
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
		if ( !isComplete( ) )
		{
			return -1;
		}
		initializePageHintReader( );
		if ( pageHintReader == null )
		{
			return -1;
		}

		int version = pageHintReader.getVersion( );

		try
		{
			if ( version == IPageHintReader.VERSION_0 )
			{
				long offset = getInstanceOffset( iid );
				if ( offset == -1 )
				{
					return -1;
				}
				long totalPage = pageHintReader.getTotalPage( );
				for ( long pageNumber = 1; pageNumber <= totalPage; pageNumber++ )
				{
					IPageHint hint = pageHintReader.getPageHint( pageNumber );
					PageSection section = hint.getSection( 0 );

					if ( offset >= section.startOffset )
					{
						return pageNumber;
					}
				}
			}

			else if ( version == IPageHintReader.VERSION_1 )
			{
				long offset = getInstanceOffset( iid );
				if ( offset == -1 )
				{
					return -1;
				}
				long totalPage = pageHintReader.getTotalPage( );
				for ( long pageNumber = 1; pageNumber <= totalPage; pageNumber++ )
				{
					IPageHint hint = pageHintReader.getPageHint( pageNumber );
					int sectionCount = hint.getSectionCount( );
					for ( int i = 0; i < sectionCount; i++ )
					{
						PageSection section = hint.getSection( i );

						if ( section.startOffset <= offset
								&& offset <= section.endOffset )
						{
							return pageNumber;
						}
					}
				}
			}
			else if ( version == IPageHintReader.VERSION_2
					|| version == IPageHintReader.VERSION_3
					|| version == IPageHintReader.VERSION_4 )
			{
				long totalPage = pageHintReader.getTotalPage( );
				for ( long pageNumber = 1; pageNumber <= totalPage; pageNumber++ )
				{
					IPageHint hint = pageHintReader.getPageHint( pageNumber );
					int sectionCount = hint.getSectionCount( );
					Fragment fragment = new Fragment(
							new InstanceIDComparator( ) );
					for ( int i = 0; i < sectionCount; i++ )
					{
						PageSection section = hint.getSection( i );
						fragment.addFragment( section.starts, section.ends );
					}
					if ( fragment.inFragment( iid ) )
					{
						return pageNumber;
					}
				}
			}
		}
		catch ( IOException ex )
		{

		}
		return -1;
	}

	public long getInstanceOffset( InstanceID iid )
	{
		if ( !isComplete( ) )
		{
			return -1l;
		}
		if ( iid == null )
		{
			return -1l;
		}
		initializeReportlet( );
		long offset = getOffset( reportletsIndexById, iid.toUniqueString( ) );
		if ( offset == -1 )
		{
			offset = getOffset( reportletsIndexById, iid.toString( ) );
		}
		return offset;
	}

	public long getBookmarkOffset( String bookmark )
	{
		if ( !isComplete( ) )
		{
			return -1;
		}
		if ( bookmark == null )
		{
			return -1l;
		}
		initializeReportlet();
		return getOffset( reportletsIndexByBookmark, bookmark );
	}

	private long getOffset( Map index, String key )
	{
		Long offset = (Long) index.get( key );
		if ( offset != null )
		{
			return offset.longValue( );
		}
		return -1;
	}

	private void initializeReportlet()
	{
		if ( reportletsIndexById != null )
		{
			return;
		}
		if ( coreVersion != -1 )
		{
			return;
		}
		synchronized ( this )
		{
			if ( reportletsIndexById != null )
			{
				return;
			}
			reportletsIndexById = new HashMap( );
			reportletsIndexByBookmark = new HashMap( );
			loadReportletStream( reportletsIndexById, REPORTLET_ID_INDEX_STREAM );
			loadReportletStream( reportletsIndexByBookmark,
					REPORTLET_BOOKMARK_INDEX_STREAM );
		}
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

	public ClassLoader getClassLoader( )
	{
		return getClassLoader( systemId );
	}

	private ClassLoader getClassLoader( String systemId )
	{
		if ( applicationClassLoader == null )
		{
			applicationClassLoader = new ApplicationClassLoader( engine,
					getReportRunnable( false, systemId ) );
		}
		return applicationClassLoader;
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
	
	

	public ReportDesignHandle getReportDesign( )
	{
		IReportRunnable reportRunnable = getReportRunnable( );
		if ( reportRunnable != null )
		{
			return (ReportDesignHandle) reportRunnable.getDesignHandle( );
		}
		return null;
	}

	public Report getReportIR( ReportDesignHandle designHandle)
	{
		try
		{
			InputStream stream = archive.getStream( DESIGN_IR_STREAM );
			EngineIRReader reader = new EngineIRReader( );
			Report reportIR = reader.read( stream );
			reportIR.setVersion( getVersion( ) );
			reader.link( reportIR, designHandle );
			return reportIR;
		}
		catch ( IOException ioex )
		{
			// an error occurs in reading the engine ir
			logger.log( Level.FINE, "Failed to load the engine IR",
					ioex );
		}
		return null;
	}

	public IReportRunnable getOnPreparedRunnable( )
	{
		return getReportRunnable(false, systemId);
	}

	public InputStream getDesignStream( )
	{
		return getDesignStream(true);
	}
}
