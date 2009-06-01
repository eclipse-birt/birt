/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.compound.ArchiveFileFactory;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.archive.compound.IArchiveFileFactory;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.ir.Expression;

abstract public class EngineCase extends TestCase
{

	protected static final String REPORT_DESIGN = "design.rptdesign";
	protected static final String REPORT_DOCUMENT = "reportdocument";

	protected IReportEngine engine;
	protected IArchiveFileFactory archiveFactory;

	public EngineCase( )
	{
		super( );
		this.archiveFactory = new ArchiveFileFactory( );
	}
	
	public EngineCase( String name )
	{
		super( name );
		this.archiveFactory = new ArchiveFileFactory( );
	}

	protected void setUp( ) throws Exception
	{
		engine = createReportEngine( );
	}

	public void copyResource( String src, String tgt )
	{
		File parent = new File( tgt ).getParentFile( );
		if ( parent != null )
		{
			parent.mkdirs( );
		}
		InputStream in = getClass( ).getClassLoader( )
				.getResourceAsStream( src );
		assertTrue( in != null );
		try
		{
			FileOutputStream fos = new FileOutputStream( tgt );
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ( ( readCount = in.read( fileData ) ) != -1 )
			{
				fos.write( fileData, 0, readCount );
			}
			fos.close( );
			in.close( );

		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	public byte[] loadResource( String src )
	{
		InputStream in = getClass( ).getClassLoader( )
				.getResourceAsStream( src );
		assertTrue( in != null );
		try
		{
			int size = in.available( );
			byte[] buffer = new byte[size];
			in.read( buffer );
			in.close( );
			return buffer;
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
		return null;
	}

	public void removeFile( File file )
	{
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			for ( int i = 0; i < children.length; i++ )
			{
				removeFile( children[i] );
			}
		}
		if ( file.exists( ) )
		{
			if ( !file.delete( ) )
			{
				System.out.println( file.toString( ) + " can't be removed in "
						+ this.getClass( ) );
			}
		}
	}

	public void removeFile( String file )
	{
		removeFile( new File( file ) );
	}

	public void unzip( String src, String folder )
	{

	}

	public IReportEngine createReportEngine( )
	{
		return createReportEngine( null );
	}

	public IReportEngine createReportEngine( EngineConfig config )
	{
		if ( config == null )
		{
			config = new EngineConfig( );
		}

		// assume we has in the platform
		Object factory = Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		if ( factory instanceof IReportEngineFactory )
		{
			return ( (IReportEngineFactory) factory )
					.createReportEngine( config );
		}
		return null;
	}

	public String renderDocument( String reportDocument )
			throws EngineException, IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		IArchiveFile af = archiveFactory.openArchive( reportDocument, "r" );
		try
		{
			IReportDocument document = engine.openReportDocument( af
					.getSystemId( ), new ArchiveReader( af ), new HashMap( ) );
			try
			{
				IRenderTask render = engine.createRenderTask( document );
				try
				{
					HTMLRenderOption option = new HTMLRenderOption( );
					option.setOutputFormat( IRenderOption.OUTPUT_FORMAT_HTML );
					option.setOutputStream( out );
					render.setRenderOption( option );
					render.render( );

					if ( !render.getErrors( ).isEmpty( ) )
					{
						for ( Object e : render.getErrors( ) )
						{
							( (Exception) e ).printStackTrace( );
						}
						fail( "render error" );
					}
					try
					{
						return out.toString( "utf-8" );
					}
					catch ( UnsupportedEncodingException ue )
					{
						return out.toString( );
					}
				}
				finally
				{
					render.close( );
				}
			}
			finally
			{
				document.close( );
			}
		}
		finally
		{
			af.close( );
		}
	}

	public void render( String design, IRenderOption options )
			throws EngineException
	{
		IReportDocument document = createReportDocument( design );
		IRenderTask render = engine.createRenderTask( document );
		render.setRenderOption( options );
		render.render( );
		render.close( );
		document.close( );
	}

	protected IReportDocument createReportDocument( String designFileName )
			throws EngineException
	{
		useDesignFile( designFileName );
		createReportDocument( );
		return engine.openReportDocument( REPORT_DOCUMENT );
	}

	public void createReportDocument( String reportDesign, String reportDocument )
			throws EngineException
	{
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( reportDesign );
		// create an IRunTask
		IRunTask task = engine.createRunTask( report );
		try
		{
			// execute the report to create the report document.
			task.run( reportDocument );
		}
		finally
		{
			// close the task, release the resource.
			task.close( );
		}
	}

	protected void useDesignFile( String fileName )
	{
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		copyResource( fileName, REPORT_DESIGN );
	}

	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument( ) throws EngineException
	{
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		// create an IRunTask
		IRunTask task = engine.createRunTask( report );
		// execute the report to create the report document.
		task.run( REPORT_DOCUMENT );
		// close the task, release the resource.
		task.close( );
	}

	/**
	 * Run and render the report, and return the render result.
	 * 
	 * @param designFile
	 * @return render result.
	 * @throws EngineException
	 * @throws IOException
	 */
	protected String runAndRender( String designFile ) throws EngineException,
			IOException
	{
		IRunAndRenderTask runAndRenderTask = createRunAndRenderTask( designFile );
		HTMLRenderOption options = new HTMLRenderOption( );
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		options.setOutputStream( out );
		options.setOutputFormat( "html" );
		options.setHtmlPagination( true );
		runAndRenderTask.setRenderOption( options );
		runAndRenderTask.run( );
		runAndRenderTask.close( );
		String result = new String( out.toByteArray( ) );
		out.close( );
		return result;
	}

	/**
	 * Create run and render result for the design file.
	 * 
	 * @param designFile
	 * @return run and render task.
	 * @throws EngineException
	 */
	protected IRunAndRenderTask createRunAndRenderTask( String designFile )
			throws EngineException
	{
		useDesignFile( designFile );
		IReportRunnable reportDesign = engine.openReportDesign( REPORT_DESIGN );
		IRunAndRenderTask runAndRenderTask = engine
				.createRunAndRenderTask( reportDesign );
		return runAndRenderTask;
	}

	/**
	 * Render a report design file and return the result.
	 * 
	 * @param designFile
	 * @return render result.
	 * @throws EngineException
	 * @throws IOException
	 */
	protected String render( String designFile ) throws EngineException,
			IOException
	{
		HTMLRenderOption options = new HTMLRenderOption( );
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		options.setOutputStream( out );
		options.setHtmlPagination( true );
		options.setOutputFormat( "html" );
		render( designFile, options );
		String result = new String( out.toByteArray( ) );
		out.close( );
		return result;
	}

	/**
	 * Get the <code>match</code> string count in the <code>source</code>
	 * String.
	 * 
	 * @param source
	 *            the source String
	 * @param match
	 *            the match String.
	 * @return
	 */
	protected int getCount( String source, String match )
	{
		int count = 0;
		int index = 0;
		do
		{
			index = source.indexOf( match, index );
			if ( index >= 0 )
			{
				++count;
				index += 1;
			}
			else
			{
				break;
			}
		} while ( index < source.length( ) );
		return count;
	}
}