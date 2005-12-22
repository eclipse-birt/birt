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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.report.engine.api.TOCNode;
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

	public ReportDocumentWriter( IDocArchiveWriter archive )
	{
		this.archive = archive;
		try
		{
			archive.initialize( );
			writeVersion( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed in initializing the archive", e );
		}
	}

	protected void writeVersion( ) throws IOException
	{
		RAOutputStream out = archive.createRandomAccessStream( VERSION_STREAM );
		try
		{
			PrintWriter writer = new PrintWriter( new OutputStreamWriter( out,
					"UTF-8" ) );
			writer.println( REPORT_DOCUMENT_TAG );
			writer.println( REPORT_DOCUMENT_VERSION_1_0_0 );
			writer.flush( );
			writer.close( );
		}
		finally
		{
			if ( out != null )
			{
				out.close( );
			}
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
		try
		{
			RAOutputStream out = archive.createRandomAccessStream( TOC_STREAM );
			TOCBuilder.write( node, out );
			out.close( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Save TOC failed!", ex );
			ex.printStackTrace( );
		}
	}

	/**
	 * save the page description into the stream.
	 * 
	 * @param hints
	 */
	public void savePageHints( ArrayList hints )
	{
		try
		{
			saveObject( archive.createRandomAccessStream( PAGEHINT_STREAM ),
					hints );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save the page hints!", ex );
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
		try
		{
			saveObject( archive.createRandomAccessStream( BOOKMARK_STREAM ),
					bookmarks );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save the bookmarks!", ex );
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
		try
		{
			RAOutputStream out = archive
					.createRandomAccessStream( DESIGN_STREAM );
			design.serialize( out );
			out.close( );
			String designName = design.getFileName( );
			if ( designName != null )
			{
				out = archive.createRandomAccessStream( DESIGN_NAME_STREAM );
				ObjectOutputStream oo = new ObjectOutputStream( out );
				oo.writeUTF( designName );
				oo.close( );
				out.close( );
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save design!", ex );
		}
	}

	/**
	 * save the paramters into the stream.
	 * 
	 * @param paramters
	 *            HashMap cotains (name, value) pair.
	 */
	public void saveParamters( HashMap paramters )
	{
		try
		{
			saveObject( archive.createRandomAccessStream( PARAMTER_STREAM ),
					paramters );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "failed to save the paramters", ex );
		}
	}

	public void savePersistentObjects( Map map )
	{
		try
		{
			saveObject( archive
					.createRandomAccessStream( PERSISTENT_OBJECTS_STREAM ), map );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "failed to save the persistent objects",
					ex );
		}
	}

	/**
	 * save an object into the file
	 * 
	 * @param file
	 *            target file
	 * @param object
	 *            object to be saved (must be serialiable)
	 */
	private void saveObject( RAOutputStream stream, Object object )
			throws Exception
	{
		FileOutputStream out = null;
		try
		{
			ObjectOutputStream oo = new ObjectOutputStream( stream );
			oo.writeObject( object );
			oo.close( );
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
