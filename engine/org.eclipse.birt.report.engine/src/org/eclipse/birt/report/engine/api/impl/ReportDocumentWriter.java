/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.index.DocumentIndexWriter;
import org.eclipse.birt.report.engine.internal.util.BundleVersionUtil;
import org.eclipse.birt.report.engine.ir.EngineIRWriter;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.DocumentUtil;

/**
 * 
 */
public class ReportDocumentWriter implements ReportDocumentConstants
{
	static private Logger logger = Logger.getLogger( ReportDocumentWriter.class
			.getName( ) );
	
	protected IReportEngine engine;
	protected IDocArchiveWriter archive;
	private String designName;
	private String extensions;
	private HashMap paramters = new HashMap( );
	private HashMap globalVariables = new HashMap( );
	private DocumentIndexWriter indexWriter;
	private int checkpoint = CHECKPOINT_INIT;
	private long pageCount = PAGECOUNT_INIT;
	
	private static final int AUTO_LAYOUT_DESIGN = 0;
	private static final int FIXED_LAYOUT_DESIGN = 1;
	private int designType = AUTO_LAYOUT_DESIGN;
	
	public ReportDocumentWriter( IReportEngine engine, IDocArchiveWriter archive )
			throws EngineException
	{
		this( engine, archive, null );
	}

	public ReportDocumentWriter( IReportEngine engine,
			IDocArchiveWriter archive, String[] extensions )
			throws EngineException
	{
		this.engine = engine;
		this.archive = archive;
		if ( extensions != null && extensions.length > 0 )
		{
			StringBuilder sb = new StringBuilder( );
			for ( String ext : extensions )
			{
				sb.append( ext );
				sb.append( ";" );
			}
			if ( sb.length( ) > 0 )
			{
				sb.setLength( sb.length( ) - 1 );
			}
			this.extensions = sb.toString( );
		}
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

	protected boolean finished = false;
	
	public void finish()
	{
		try
		{
			checkpoint = CHECKPOINT_END;
			saveCoreStreams( );
			archive.setStreamSorter( new ReportDocumentStreamSorter( ) );
			archive.flush( );
			finished = true;
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed in flush the archive", e );
		}
	}
	
	public void close( )
	{
		if ( indexWriter != null )
		{
			try
			{
				indexWriter.close( );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "Failed to close the indexes", ex );
			}
			indexWriter = null;
		}
		if ( !finished )
		{
			finish();
		}
		try
		{
			// close the archive;
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

	public void saveReportIR(Report reportIR)
	{
		RAOutputStream out = null;
		try
		{
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
	 * save the design into the stream.
	 * 
	 * @param design
	 *            design handler
	 */
	public ReportRunnable saveDesign( ReportRunnable runnable,
			ReportRunnable originalRunnable ) throws EngineException
	{
		RAOutputStream out = null;
		ReportRunnable newRunnable = runnable;
		try
		{
			ReportDesignHandle design = runnable.getReport( );
			if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
					.equals( design.getLayoutPreference( ) ) )
			{
				designType = FIXED_LAYOUT_DESIGN;
			}
			out = archive.createRandomAccessStream( DESIGN_STREAM );
			// design.serialize( out );
			ReportDesignHandle newDesign = DocumentUtil.serialize( design, out );
			designName = design.getFileName( );
			newRunnable = new ReportRunnable( engine, newDesign );
			newRunnable.setReportName( runnable.getReportName( ) );
			newRunnable.cachedScripts = runnable.cachedScripts;
			newDesign.getModule( ).tidy( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to save design!", ex );
			throw new EngineException( MessageConstants.SAVE_DESIGN_ERROR, ex );
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

		
		return newRunnable;
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
			Object display = valueObj.getDisplayText( );
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
			IOUtil.writeString( coreStream, CORE_VERSION_2 );
			IOUtil.writeString( coreStream, REPORT_DOCUMENT_VERSION );
			
			HashMap properties = new HashMap( );
			if ( designType == AUTO_LAYOUT_DESIGN )
			{
				properties.put( PAGE_HINT_VERSION_KEY, PAGE_HINT_VERSION_3 );	
			}
			else
			{
				properties.put( PAGE_HINT_VERSION_KEY, PAGE_HINT_VERSION_FIXED_LAYOUT );
			}
			properties.put( BIRT_ENGINE_VERSION_KEY, BIRT_ENGINE_VERSION );
			properties.put( BIRT_ENGINE_BUILD_NUMBER_KEY, getBuildNumber( ) );
			if ( extensions != null )
			{
				properties.put( BIRT_ENGINE_EXTENSIONS, extensions );
			}
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

	private String getBuildNumber( )
	{
		return BundleVersionUtil
				.getBundleVersion( "org.eclipse.birt.report.engine" );
	}

	public void removeReportletDoucment( )
	{
		if ( archive.exists( REPORTLET_DOCUMENT_STREAM ) )
		{
			archive.dropStream( REPORTLET_DOCUMENT_STREAM );
		}
	}

	public void saveReportletDocument( String bookmark, InstanceID iid )
			throws IOException
	{
		RAOutputStream out = archive
				.createOutputStream( REPORTLET_DOCUMENT_STREAM );
		try
		{
			IOUtil.writeInt( out, REPORTLET_DOCUMENT_VERSION_0 );
			ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
			DataOutputStream s = new DataOutputStream( buffer );
			IOUtil.writeString( s, bookmark );
			IOUtil.writeString( s, iid == null ? null : iid.toUniqueString( ) );
			out.writeInt( buffer.size( ) );
			out.write( buffer.toByteArray( ) );
		}
		finally
		{
			out.close( );
		}
	}
	
	public void setBookmark( String bookmark, BookmarkContent content )
	{
		try
		{
			if ( indexWriter == null )
			{
				indexWriter = new DocumentIndexWriter( archive );
			}
			if ( indexWriter != null )
			{
				indexWriter.setBookmark( bookmark, content );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.WARNING, "Failed to save the bookmark", ex );
		}
	}

	public void setOffsetOfBookmark( String bookmark, long offset ) throws IOException
	{
		try
		{
			if ( indexWriter == null )
			{
				indexWriter = new DocumentIndexWriter( archive );
			}
			if ( indexWriter != null )
			{
				indexWriter.setOffsetOfBookmark( bookmark, offset );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.WARNING, "Failed to save the bookmark", ex );
			throw ex;
		}
	}

	public void setOffsetOfInstance( String instanceId, long offset ) throws IOException
	{
		try
		{
			if ( indexWriter == null )
			{
				indexWriter = new DocumentIndexWriter( archive );
			}
			if ( indexWriter != null )
			{
				indexWriter.setOffsetOfInstance( instanceId, offset );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.WARNING, "Failed to save the bookmark", ex );
			throw ex;
		}
	}
}