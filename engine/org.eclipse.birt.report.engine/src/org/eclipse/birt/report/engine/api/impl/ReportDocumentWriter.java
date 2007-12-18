/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.ir.EngineIRWriter;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCTree;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * 
 */
public class ReportDocumentWriter implements ReportDocumentConstants
{
	static private Logger logger = Logger.getLogger( ReportDocumentWriter.class
			.getName( ) );
	
	protected IReportEngine engine;
	private IDocArchiveWriter archive;
	private String designName;
	private HashMap paramters = new HashMap( );
	private HashMap globalVariables = new HashMap( );
	private int checkpoint = CHECKPOINT_INIT;
	private long pageCount = PAGECOUNT_INIT;
	
	private HashMap bookmarks = new HashMap( );
	private TOCTree tocTree = null;
	private HashMap idToOffset = new HashMap( );
	private HashMap bookmarkToOffset = new HashMap( );

	public ReportDocumentWriter( IReportEngine engine, IDocArchiveWriter archive )
	{
		this.engine = engine;
		this.archive = archive;
		try
		{
			archive.initialize( );
			saveCoreStreams( );
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
			checkpoint = CHECKPOINT_END;
			saveCoreStreams( );
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
		this.tocTree = tocTree;
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
		
		this.bookmarks = new HashMap();
		this.bookmarks.putAll( bookmarks );
	}

	/**
	 * save the design into the stream.
	 * 
	 * @param design
	 *            design handler
	 */
	public void saveDesign( ReportRunnable runnable )
	{
		RAOutputStream out = null;
		try
		{
			ReportDesignHandle design = runnable.getReport( );
			out = archive.createRandomAccessStream( DESIGN_STREAM );
			//design.serialize( out );
			ReportDesignHandle newDesign = DocumentUtil.serialize(design, out);
			designName = design.getFileName( );
			runnable.setDesignHandle( newDesign );
			
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
			out = null;
		}

		try
		{
			Report reportIR = runnable.getReportIR( );
			out = archive.createRandomAccessStream( DESIGN_IR_STREAM );
			new EngineIRWriter().write(out, reportIR);
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save design IR!", ex );
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

	public void saveCoreStreams( ) throws Exception
	{
		Object lock = archive.lock( CORE_STREAM );
		try
		{
			synchronized ( lock )
			{
				doSaveCoreStreams( );
			}
		}
		finally
		{
			archive.unlock( lock );
		}
	}

	protected void doSaveCoreStreams( ) throws Exception
	{
		RAOutputStream out = null;
		DataOutputStream coreStream = null;
		try
		{
			out = archive.createRandomAccessStream( CORE_STREAM );
			coreStream = new DataOutputStream( new BufferedOutputStream(
					out ) );
			IOUtil.writeString( coreStream, REPORT_DOCUMENT_TAG );			
			IOUtil.writeString( coreStream, CORE_VERSION_1 );
			IOUtil.writeString( coreStream, REPORT_DOCUMENT_VERSION );
			
			HashMap properties = new HashMap( );
			properties.put( PAGE_HINT_VERSION_KEY, PAGE_HINT_VERSION_3 );
			properties.put( BIRT_ENGINE_VERSION_KEY, BIRT_ENGINE_VERSION );
			properties.put( BIRT_ENGINE_BUILD_NUMBER_KEY, getBuildNumber( ) );
			IOUtil.writeMap( coreStream, properties );
			
			if ( checkpoint != CHECKPOINT_END )
			{
				checkpoint++;
			}
			IOUtil.writeInt( coreStream, checkpoint );
			IOUtil.writeLong( coreStream, pageCount );
			
			IOUtil.writeString( coreStream, designName );
			IOUtil.writeMap( coreStream, paramters );
			IOUtil.writeMap( coreStream, globalVariables );
			
			if ( checkpoint == CHECKPOINT_END )
			{
				writeMap( coreStream, bookmarks );
				TOCBuilder.write( tocTree, coreStream );
				writeMap( coreStream, idToOffset );
				writeMap( coreStream, bookmarkToOffset );
			}
			coreStream.flush( );
		}
		catch ( IOException ex )
		{
			logger
					.log( Level.SEVERE, "Failed to save the core stream!",
							ex );
		}
		finally
		{
			if ( out != null )
			{
				try
				{
					out.close( );
					out = null;
				}
				catch ( Exception ex )
				{
				}
			}
		}	
	}
	
	private void writeMap( DataOutputStream stream, HashMap map )
			throws Exception
	{
		if ( map == null )
		{
			map = new HashMap( );
		}
		IOUtil.writeLong( stream, map.size( ) );
		Iterator iter = map.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			String key = (String) entry.getKey( );
			Long value = (Long) entry.getValue( );
			IOUtil.writeString( stream, key );
			IOUtil.writeLong( stream, value.longValue( ) );
		}
	}
	
	public void setPageCount( long pageCount )
	{
		this.pageCount = pageCount;
	}

	public void saveReprotletsBookmarkIndex( Map bookmarkToOffset)
	{
		this.bookmarkToOffset = new HashMap();
		this.bookmarkToOffset.putAll( bookmarkToOffset );
	}

	public void saveReportletsIdIndex( Map idToOffset )
	{
		this.idToOffset = new HashMap();
		this.idToOffset.putAll( idToOffset );
	}
	
	private String getBuildNumber( )
	{
		Bundle bundle = Platform.getBundle( "org.eclipse.birt.report.engine" );
		if ( bundle != null )
		{
			Dictionary dict = bundle.getHeaders( );
			if ( dict != null )
			{
				Object version = dict
						.get( org.osgi.framework.Constants.BUNDLE_VERSION );
				if ( version != null )
				{
					return version.toString( );
				}
			}
		}
		return "UNKNOWN";
	}
}