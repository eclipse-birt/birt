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
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * 
 */
public class ReportDocumentWriter implements ReportDocumentConstants
{

	static private Logger logger = Logger.getLogger( ReportDocumentReader.class
			.getName( ) );

	private IDocArchiveWriter archive;
	private DataOutputStream coreStream;
	private String designName;
	private HashMap paramters;
	private HashMap globalVariables;

	public ReportDocumentWriter( IDocArchiveWriter archive )
	{
		this.archive = archive;
		try
		{
			archive.initialize( );
			RAOutputStream out = archive.createRandomAccessStream( CORE_STREAM );
			coreStream = new DataOutputStream( new BufferedOutputStream( out ) );
			writeVersion( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed in initializing the archive", e );
		}
	}

	protected void writeVersion( ) throws IOException
	{
		IOUtil.writeString( coreStream, REPORT_DOCUMENT_TAG );
		IOUtil.writeString( coreStream, REPORT_DOCUMENT_VERSION_1_0_0 );
	}

	public IDocArchiveWriter getArchive( )
	{
		return this.archive;
	}

	public void close( )
	{
		try
		{
			if ( coreStream != null )
			{
				try
				{
					IOUtil.writeString( coreStream, designName );
					IOUtil.writeMap( coreStream, paramters );
					IOUtil.writeMap( coreStream, globalVariables );
				}
				finally
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
			archive.finish( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed in close the archive", e );
		}
	}

	public String getReportDocumentName( )
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
		RAOutputStream out = null;
		try
		{
			out = archive.createRandomAccessStream( TOC_STREAM );
			TOCBuilder.write( node, out );
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
			design.serialize( out );
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

}
