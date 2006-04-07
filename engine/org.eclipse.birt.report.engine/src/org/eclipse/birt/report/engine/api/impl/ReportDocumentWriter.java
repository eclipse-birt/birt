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
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;

/**
 * 
 */
public class ReportDocumentWriter implements ReportDocumentConstants
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	private IDocArchiveWriter archive;
	private String designName;
	private HashMap paramters = new HashMap( );
	private HashMap globalVariables = new HashMap( );

	public ReportDocumentWriter( IDocArchiveWriter archive )
	{
		this.archive = archive;
		try
		{
			archive.initialize( );
			saveCoreStreams( );
		}
		catch ( IOException e )
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
			saveCoreStreams( );
			archive.setStreamSorter( new ReportDocumentStreamSorter( ) );
			archive.finish( );
		}
		catch ( IOException e )
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
	public void saveTOC( TOCNode node )
	{
		if ( node.getChildren( ).isEmpty( ) )
		{
			return;
		}
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( TOC_STREAM );
			DataOutputStream output = new DataOutputStream( out );
			TOCBuilder.write( node, output );
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
			DocumentUtil.serialize( design, out );
			// design.serialize( out );
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
		paramters.putAll( map );
	}

	public void savePersistentObjects( Map map )
	{
		globalVariables = new HashMap( );
		globalVariables.putAll( map );
	}

	protected void lock( ) throws BirtException
	{
		ReportDocumentLockManager.getInstance( ).lock( getName( ), true );
	}

	protected void unlock( )
	{
		ReportDocumentLockManager.getInstance( ).unlock( getName( ), true );
	}

	public void saveCoreStreams( )
	{
		// create a mutex named with the system
		// lock the mutex
		try
		{
			lock( );
			RAOutputStream out;
			DataOutputStream coreStream = null;
			try
			{
				out = archive.createRandomAccessStream( CORE_STREAM );
				coreStream = new DataOutputStream( new BufferedOutputStream(
						out ) );
				IOUtil.writeString( coreStream, REPORT_DOCUMENT_TAG );
				IOUtil.writeString( coreStream, REPORT_DOCUMENT_VERSION_1_2_1 );
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
						coreStream.flush( );
						coreStream.close( );
					}
					catch ( Exception ex )
					{
					}
				}
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to lock the core stream!", ex );
		}
		finally
		{
			unlock( );
		}

	}

	public void saveReportlets( Map map )
	{

		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( REPORTLET_STREAM );
			DataOutputStream output = new DataOutputStream( out );
			IOUtil.writeLong( output, map.size( ) );
			Iterator iter = map.entrySet( ).iterator( );
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
