/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.integrate.SimpleActionEvaluator;
import org.eclipse.birt.chart.integrate.SimpleActionRenderer;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.IExternalContext;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

/**
 * Class for managing image resources, including generating and disposing.
 * 
 */
public class ChartImageManager
{

	private static final String IMAGE_FOLDER = "imageTemp"; //$NON-NLS-1$

	private static final String IMAGE_NAME_PREFIX = "ChartImage"; //$NON-NLS-1$

	/**
	 * Image folder to put the image files
	 */
	private static String imageFolder = null;

	private static List<String> sessionIds = new ArrayList<String>( );

	private static int imageIndex = 0;

	private final HttpServletRequest request;

	private final Chart cm;

	private String sExtension = null;

	private final IDataRowExpressionEvaluator evaluator;

	private final IStyleProcessor styleProc;

	private RunTimeContext rtc = null;

	private IExternalContext externalContext = null;

	private String imageMap = null;

	private File imageFile;

	private int dpi = 72;

	public ChartImageManager( HttpServletRequest request, Chart chartModel,
			String outputFormat, IDataRowExpressionEvaluator evaluator,
			RunTimeContext rtc, IExternalContext externalContext,
			IStyleProcessor styleProc ) throws Exception
	{
		this.request = request;
		this.cm = chartModel;
		this.sExtension = outputFormat;
		this.evaluator = evaluator;
		this.styleProc = styleProc;

		if ( externalContext == null )
		{
			this.externalContext = new IExternalContext( ) {

				private static final long serialVersionUID = 4666361117214885689L;

				public Object getObject( )
				{
					// TODO Auto-generated method stub
					return null;
				}

				public Scriptable getScriptable( )
				{
					// TODO Auto-generated method stub
					return null;
				}
			};
		}
		else
		{
			this.externalContext = externalContext;
		}

		if ( rtc == null )
		{
			this.rtc = new RunTimeContext( );
			this.rtc.setULocale( ULocale.getDefault( ) );
		}
		else
		{
			this.rtc = rtc;
		}

		sessionIds.add( request.getSession( ).getId( ) );

		generateImage( );
	}

	private InputStream generateStream( ) throws BirtException
	{
		InputStream fis = null;
		Generator gr = Generator.instance( );
		IDeviceRenderer idr = null;
		try
		{
			if ( evaluator == null )
			{
				// If chart has runtime dataset, do not create sample data
				if ( !ChartWebHelper.isChartInRuntime( cm ) )
				{
					cm.createSampleRuntimeSeries( );
				}
			}
			else
			{
				gr.bindData( evaluator, new SimpleActionEvaluator( ), cm, rtc );
			}

			rtc.setActionRenderer( new SimpleActionRenderer( evaluator ) );

			// FETCH A HANDLE TO THE DEVICE RENDERER
			idr = ChartEngine.instance( ).getRenderer( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			idr.setProperty( IDeviceRenderer.DPI_RESOLUTION,
					Integer.valueOf( dpi ) );

			if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
			{
				idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
			}

			// BUILD THE CHART
			final Bounds originalBounds = cm.getBlock( ).getBounds( );

			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container

			final Bounds bo = originalBounds.copyInstance( );

			GeneratedChartState gcs = gr.build( idr.getDisplayServer( ),
					cm,
					bo,
					externalContext,
					rtc,
					styleProc );

			// WRITE TO THE IMAGE FILE
			ByteArrayOutputStream baos = new ByteArrayOutputStream( );
			BufferedOutputStream bos = new BufferedOutputStream( baos );

			idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, bos );
			idr.setProperty( IDeviceRenderer.UPDATE_NOTIFIER,
					new EmptyUpdateNotifier( cm, gcs.getChartModel( ) ) );

			gr.render( idr, gcs );

			// cleanup the dataRow evaluator.
			// rowAdapter.close( );

			// RETURN A STREAM HANDLE TO THE NEWLY CREATED IMAGE
			try
			{
				fis = new ByteArrayInputStream( baos.toByteArray( ) );
				bos.close( );
			}
			catch ( Exception ioex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						ioex );
			}

			if ( !"SVG".equalsIgnoreCase( sExtension ) && idr instanceof IImageMapEmitter ) //$NON-NLS-1$
			{
				imageMap = ( (IImageMapEmitter) idr ).getImageMap( );
			}

		}
		catch ( BirtException birtException )
		{
			Throwable ex = birtException;
			while ( ex.getCause( ) != null )
			{
				ex = ex.getCause( );
			}

			if ( ex instanceof ChartException
					&& ( (ChartException) ex ).getType( ) == ChartException.ZERO_DATASET )
			{
				// if the Data set has zero lines, just
				// returns null gracefully.
				return null;
			}

			if ( ex instanceof ChartException
					&& ( (ChartException) ex ).getType( ) == ChartException.ALL_NULL_DATASET )
			{
				// if the Data set contains all null values, just
				// returns null gracefully and render nothing.
				return null;
			}

			if ( ( ex instanceof ChartException && ( (ChartException) ex ).getType( ) == ChartException.INVALID_IMAGE_SIZE ) )
			{
				// if the image size is invalid, this may caused by
				// Display=None, lets ignore it.
				return null;
			}

			throw birtException;
		}
		catch ( RuntimeException ex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					ex );
		}
		finally
		{
			if ( idr != null )
			{
				idr.dispose( );
			}
		}

		return fis;
	}

	private void generateImage( ) throws Exception
	{
		String imageName = IMAGE_NAME_PREFIX
				+ +imageIndex++
				+ "." + this.sExtension.toLowerCase( ); //$NON-NLS-1$
		imageFile = new File( getAbsoluteImageFolder( )
				+ File.separator
				+ imageName );
		if ( !imageFile.getParentFile( ).exists( ) )
		{
			imageFile.getParentFile( ).mkdirs( );
		}
		request.getSession( )
				.getServletContext( )
				.log( "Generated file: " + imageFile.getPath( ) ); //$NON-NLS-1$
		OutputStream fos = null;
		InputStream fis = null;
		try
		{
			fos = new FileOutputStream( imageFile );
			fis = generateStream( );
			byte[] buffer = new byte[1024];
			int readSize = 0;
			while ( ( readSize = fis.read( buffer ) ) != -1 )
			{
				// Bug 200777
				// Only write the read size of input stream into output stream.
				fos.write( buffer, 0, readSize );
			}
		}
		finally
		{
			if ( fos != null )
			{
				fos.close( );
			}
			if ( fis != null )
			{
				fis.close( );
			}

		}

	}

	public File getImage( )
	{
		return imageFile;
	}

	public String getImageMap( )
	{
		return imageMap;
	}

	public synchronized static void init( ServletContext context )
	{
		// Initialize chart engine in standalone mode
		PlatformConfig config = new PlatformConfig( );
		config.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
		ChartEngine.instance( config );

		// Image folder setting
		imageFolder = processRealPath( context, IMAGE_FOLDER, true );
	}

	/**
	 * Disposes all resources and reset.
	 */
	public static void dispose( ServletContext context )
	{
		for ( int i = 0, n = sessionIds.size( ); i < n; i++ )
		{
			String sessionId = sessionIds.get( i );
			clearSessionFiles( sessionId, context );
		}
		sessionIds.clear( );
	}

	/**
	 * Clear the temporary files when session is expired
	 * 
	 * @param id
	 *            session id
	 * @param context
	 *            sevlet context
	 */
	public static void clearSessionFiles( String id, ServletContext context )
	{
		if ( id == null )
		{
			return;
		}

		// clear image folder
		String tempFolder = imageFolder + File.separator + id;
		File file = new File( tempFolder );
		deleteDir( file, context );
	}

	public String getAbsoluteImageFolder( )
	{
		return imageFolder + File.separator + request.getSession( ).getId( );
	}

	public String getRelativeImageFolder( )
	{
		// Here do not use File.separator since it's in URL
		return request.getContextPath( ) + "/" //$NON-NLS-1$
				+ IMAGE_FOLDER
				+ "/" //$NON-NLS-1$
				+ request.getSession( ).getId( );
	}

	/**
	 * Deletes all files and sub-directories under directories. Returns true if
	 * all deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 */

	private static boolean deleteDir( File dir, ServletContext context )
	{
		if ( dir.isDirectory( ) )
		{
			String[] children = dir.list( );
			for ( int i = 0; i < children.length; i++ )
			{
				boolean success = deleteDir( new File( dir, children[i] ),
						context );
				if ( !success )
				{
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		context.log( "Cleaned file: " + dir.getPath( ) ); //$NON-NLS-1$
		return dir.delete( );
	}

	/**
	 * Process folder settings with absolute path. If path is a relative path,
	 * first relative to context. If set canWrite to true, then check the folder
	 * if writable.If not, relative to ${java.io.tmpdir} folder.
	 * 
	 * @param context
	 * @param path
	 * @param defaultPath
	 * @param canWrite
	 * @return
	 */
	private static String processRealPath( ServletContext context, String path,
			boolean canWrite )
	{
		String realPath = null;

		// If path is a relative path
		if ( !new File( path ).isAbsolute( ) )
		{
			if ( !path.startsWith( "/" ) ) //$NON-NLS-1$
				path = "/" + path; //$NON-NLS-1$

			realPath = getRealPath( path, context );
			if ( realPath != null && makeDir( realPath ) )
			{
				if ( !canWrite )
					return trimSep( realPath );

				// check if the folder is writable
				try
				{
					if ( canWrite && new File( realPath ).canWrite( ) )
						return trimSep( realPath );
				}
				catch ( Exception e )
				{

				}
			}

			// try to create folder in ${java.io.tmpdir}
			realPath = trimSep( System.getProperty( "java.io.tmpdir" ) ) + path; //$NON-NLS-1$
		}
		else
		{
			// Path is an absolute path
			realPath = trimSep( path );
		}

		// try to create folder
		makeDir( realPath );

		return realPath;
	}

	/**
	 * Returns real path relative to context
	 * 
	 * @param path
	 * @param context
	 * @return
	 */
	private static String getRealPath( String path, ServletContext context )
	{
		assert path != null;
		String realPath = null;
		try
		{
			if ( !path.startsWith( "/" ) ) //$NON-NLS-1$
				path = "/" + path; //$NON-NLS-1$

			realPath = context.getRealPath( path );
			if ( realPath == null )
			{
				URL url = context.getResource( "/" ); //$NON-NLS-1$
				if ( url != null )
					realPath = trimString( url.getFile( ) ) + path;
			}
		}
		catch ( Exception e )
		{
			realPath = path;
		}

		return realPath;
	}

	/**
	 * Trim the end separator
	 * 
	 * @param path
	 * @return
	 */
	protected static String trimSep( String path )
	{
		path = trimString( path );
		if ( path.endsWith( File.separator ) )
		{
			path = path.substring( 0, path.length( ) - 1 );
		}

		return path;
	}

	/**
	 * Returns trim string, not null
	 * 
	 * @param str
	 * @return
	 */
	private static String trimString( String str )
	{
		if ( str == null )
			return ""; //$NON-NLS-1$

		return str.trim( );
	}

	/**
	 * Make directory
	 * 
	 * @param path
	 * @return
	 */
	private static boolean makeDir( String path )
	{
		assert path != null;
		File file = new File( path );
		if ( !file.exists( ) )
			return file.mkdirs( );

		return true;
	}

}
