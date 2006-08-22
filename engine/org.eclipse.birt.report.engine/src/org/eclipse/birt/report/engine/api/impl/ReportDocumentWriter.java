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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportDocumentLock;
import org.eclipse.birt.report.engine.api.IReportDocumentLockManager;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCTree;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;

/**
 * 
 */
public class ReportDocumentWriter implements ReportDocumentConstants
{
	static private Logger logger = Logger.getLogger( ReportDocumentWriter.class
			.getName( ) );
	
	private IReportEngine engine;
	private IDocArchiveWriter archive;
	private String designName;
	private HashMap paramters = new HashMap( );
	private HashMap globalVariables = new HashMap( );
	private int checkpoint = CHECKPOINT_INIT;
	private long pageCount = PAGECOUNT_INIT;

	public ReportDocumentWriter( IReportEngine engine, IDocArchiveWriter archive )
	{
		this.engine = engine;
		this.archive = archive;
		try
		{
			archive.initialize( );
			IReportDocumentLock lock = lock( getName( ) );
			synchronized ( lock )
			{
				try
				{
					saveCoreStreams( );
				}
				catch ( Exception ex )
				{				
				}
				finally
				{				
					lock.unlock( );
				}
			}
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed in initializing the archive", e );
		}
	}

	public IDocArchiveWriter getArchive( )
	{
		return this.archive;
	}

	public void close( )
	{
		try
		{
			IReportDocumentLock lock = lock( getName( ) );
			synchronized ( lock )
			{
				try
				{
					checkpoint = CHECKPOINT_END;
					saveCoreStreams( );
				}
				finally
				{				
					lock.unlock( );
				}
			}
			archive.setStreamSorter( new ReportDocumentStreamSorter( ) );
			archive.finish( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed in close the archive", e );
		}
	}

	public String getName( )
	{
		return archive.getName( );
	}

	/**
	 * save the TOC stream into the report document.
	 * 
	 * @param node
	 *            TOC nodes.
	 */
	public void saveTOC( TOCTree tocTree )
	{
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( TOC_STREAM );
			DataOutputStream output = new DataOutputStream( out );
			TOCBuilder.write( tocTree, output );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Save TOC failed!", ex );
			ex.printStackTrace( );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );

				}
				catch ( Exception ex )
				{
				}
			}
		}
	}

	/**
	 * save the page description into the stream.
	 * 
	 * @param hints
	 */
	public void savePageHints( ArrayList hints )
	{
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( PAGEHINT_STREAM );
			DataOutputStream oo = new DataOutputStream(
					new BufferedOutputStream( out ) );
			IOUtil.writeLong( oo, hints.size( ) );
			for ( int i = 0; i < hints.size( ); i++ )
			{
				PageHint hint = (PageHint) hints.get( i );
				hint.writeObject( oo );
			}
			oo.close( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save the page hints!", ex );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );

				}
				catch ( Exception ex )
				{
				}
			}
		}
	}

	/**
	 * save bookmarks into the stream.
	 * 
	 * @param bookmarks
	 *            HashMap contains (bookmark, page) pair.
	 */
	public void saveBookmarks( HashMap bookmarks )
	{
		if ( bookmarks.isEmpty( ) )
		{
			return;
		}
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( BOOKMARK_STREAM );
			DataOutputStream oo = new DataOutputStream(
					new BufferedOutputStream( out ) );
			IOUtil.writeLong( oo, bookmarks.size( ) );
			Iterator iter = bookmarks.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				String bookmark = (String) entry.getKey( );
				Long pageNumber = (Long) entry.getValue( );
				IOUtil.writeString( oo, bookmark );
				IOUtil.writeLong( oo, pageNumber.longValue( ) );

			}
			oo.close( );

		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save the bookmarks!", ex );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );

				}
				catch ( Exception ex )
				{
				}
			}
		}
	}

	/**
	 * save the design into the stream.
	 * 
	 * @param design
	 *            design handler
	 */
	public void saveDesign( ReportDesignHandle design )
	{
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( DESIGN_STREAM );
			//design.serialize( out );
			DocumentUtil.serialize(design, out);
			designName = design.getFileName( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save design!", ex );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );
				}
				catch ( Exception ex )
				{
				}
			}
		}
	}

	/**
	 * save the paramters into the stream.
	 * 
	 * @param paramters
	 *            HashMap cotains (name, value) pair.
	 */
	public void saveParamters( HashMap map )
	{
		paramters = new HashMap( );
		Iterator iter = map.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			Object key = entry.getKey( );
			ParameterAttribute valueObj = (ParameterAttribute) entry.getValue( );
			Object value = valueObj.getValue( );
			String display = valueObj.getDisplayText( );
			paramters.put( key, new Object[]{value, display} );
		}
	}

	public void savePersistentObjects( Map map )
	{
		globalVariables = new HashMap( );
		globalVariables.putAll( map );
	}
	/**
	 * create a locker used to lock the report document
	 * @return
	 * @throws BirtException
	 */
	public IReportDocumentLock lock( String documentName )
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
		
	public void saveCoreStreams( ) throws Exception
	{
		RAOutputStream out;
		DataOutputStream coreStream = null;
		try
		{
			out = archive.createRandomAccessStream( CORE_STREAM );
			coreStream = new DataOutputStream( new BufferedOutputStream(
					out ) );
			IOUtil.writeString( coreStream, REPORT_DOCUMENT_TAG );
			IOUtil.writeString( coreStream, REPORT_DOCUMENT_VERSION_2_1_0 );
			IOUtil.writeString( coreStream, designName );
			IOUtil.writeMap( coreStream, paramters );
			IOUtil.writeMap( coreStream, globalVariables );			
		}
		catch ( IOException ex )
		{
			logger
					.log( Level.SEVERE, "Failed to save the core stream!",
							ex );
		}
		finally
		{
			if ( coreStream != null )
			{
				try
				{
					coreStream.close( );
				}
				catch ( Exception ex )
				{
				}
			}
		}
		
		DataOutputStream checkpointStream = null;
		try
		{
			out = archive.createRandomAccessStream( CHECKPOINT_STREAM );			
			checkpointStream = new DataOutputStream( new BufferedOutputStream(
					out ) );
			if ( checkpoint != CHECKPOINT_END )
			{
				checkpoint++;
			}
			IOUtil.writeInt( checkpointStream, checkpoint );
			IOUtil.writeLong( checkpointStream, pageCount );
		}
		catch ( IOException ex )
		{
			logger
					.log( Level.SEVERE, "Failed to save the checkpoint stream!",
							ex );
		}
		finally
		{
			if ( checkpointStream != null )
			{
				try
				{
					checkpointStream.close( );
				}
				catch ( Exception ex )
				{
				}
			}
		}
	}
	
	public void setPageCount( long pageCount )
	{
		this.pageCount = pageCount;
	}

	public void saveReprotletsBookmarkIndex( Map bookmarkToOffset)
	{
		saveReportletsIndex( bookmarkToOffset, REPORTLET_BOOKMARK_INDEX_STREAM );
	}

	public void saveReportletsIdIndex( Map idToOffset )
	{
		saveReportletsIndex( idToOffset, REPORTLET_ID_INDEX_STREAM );
	}

	private void saveReportletsIndex( Map index, String streamName )
	{
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( streamName );
			DataOutputStream output = new DataOutputStream( out );
			IOUtil.writeLong( output, index.size( ) );
			Iterator iter = index.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				String instance = (String) entry.getKey( );
				Long offset = (Long) entry.getValue( );
				IOUtil.writeString( output, instance );
				IOUtil.writeLong( output, offset.longValue( ) );

			}
			output.flush( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save design!", ex );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );

				}
				catch ( Exception ex )
				{
				}
			}
		}
	}
}