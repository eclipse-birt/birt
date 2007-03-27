/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.IExternalContext;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

/**
 * Class for managing image resources, including generating and disposing.
 * 
 */
public class ChartImageManager
{

	/**
	 * Flag indicating that if initialize the context.
	 */
	protected static boolean isInitContext = false;

	private static final String IMAGE_FOLDER = "imageTemp"; //$NON-NLS-1$

	private static final String IMAGE_NAME_PREFIX = "ChartImage"; //$NON-NLS-1$

	/**
	 * Image folder to put the image files
	 */
	public static String imageFolder = null;

	private static List sessionIds = new ArrayList( );

	private static int imageIndex = 0;

	private String sessionId = null;

	private Chart cm = null;

	private String sExtension = null;

	private IDataRowExpressionEvaluator evaluator = null;

	private IStyleProcessor styleProc = null;

	private RunTimeContext rtc = null;

	private IExternalContext externalContext = null;

	private IDeviceRenderer idr = null;

	private String imageMap = null;

	private File imageFile;

	private int dpi = 72;

	public ChartImageManager( String sessionId, Chart chartModel,
			String outputFormat, IDataRowExpressionEvaluator evaluator,
			RunTimeContext rtc, IExternalContext externalContext,
			IStyleProcessor styleProc ) throws Exception
	{
		this.sessionId = sessionId;
		this.cm = chartModel;
		this.sExtension = outputFormat;
		this.evaluator = evaluator;
		this.styleProc = styleProc;

		if ( externalContext == null )
		{
			this.externalContext = new IExternalContext( ) {

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

		sessionIds.add( sessionId );

		generateImage( );
	}

	private InputStream generateStream( ) throws BirtException
	{
		InputStream fis = null;
		Generator gr = Generator.instance( );
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
				gr.bindData( evaluator, cm, rtc );
			}

			// FETCH A HANDLE TO THE DEVICE RENDERER
			idr = PluginSettings.instance( ).getDevice( "dv." //$NON-NLS-1$
					+ sExtension.toUpperCase( Locale.US ) );

			idr.setProperty( IDeviceRenderer.DPI_RESOLUTION, new Integer( dpi ) );

			if ( "SVG".equalsIgnoreCase( sExtension ) ) //$NON-NLS-1$
			{
				idr.setProperty( "resize.svg", Boolean.TRUE ); //$NON-NLS-1$
			}

			// BUILD THE CHART
			final Bounds originalBounds = cm.getBlock( ).getBounds( );

			// we must copy the bounds to avoid that setting it on one object
			// unsets it on its precedent container

			final Bounds bo = (Bounds) EcoreUtil.copy( originalBounds );

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

		return fis;
	}

	private void generateImage( ) throws Exception
	{
		String imageName = IMAGE_NAME_PREFIX
				+ +imageIndex++ + "." + this.sExtension.toLowerCase( ); //$NON-NLS-1$
		imageFile = new File( getAbsoluteImageFolder( )
				+ File.separator + imageName );
		if ( !imageFile.getParentFile( ).exists( ) )
		{
			imageFile.getParentFile( ).mkdirs( );
		}
		System.out.println( "Generated file: " + imageFile.getPath( ) ); //$NON-NLS-1$
		OutputStream fos = new FileOutputStream( imageFile );
		InputStream fis = generateStream( );
		byte[] buffer = new byte[1024];
		int readSize = 0;
		while ( ( readSize = fis.read( buffer ) ) != -1 )
		{
			fos.write( buffer );
		}
		fis.close( );
		fos.close( );
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
		if ( isInitContext )
		{
			return;
		}

		// Image folder setting
		imageFolder = processRealPath( context, IMAGE_FOLDER, true );

		// Set standalone mode rather than OSGI mode
		System.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Disposes all resources and reset.
	 */
	public static void dispose( )
	{
		isInitContext = false;
		for ( int i = 0, n = sessionIds.size( ); i < n; i++ )
		{
			String sessionId = (String) sessionIds.get( i );
			clearSessionFiles( sessionId );
		}
		sessionIds.clear( );
	}

	/**
	 * Clear the temp files when session is expired
	 * 
	 * @param id
	 *            session id
	 */
	public static void clearSessionFiles( String id )
	{
		if ( id == null )
		{
			return;
		}

		// clear image folder
		String tempFolder = imageFolder + File.separator + id;
		File file = new File( tempFolder );
		deleteDir( file );
	}

	public String getAbsoluteImageFolder( )
	{
		return imageFolder + File.separator + sessionId;
	}

	public String getRelativeImageFolder( )
	{
		return IMAGE_FOLDER + "/" + sessionId; //$NON-NLS-1$
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 */

	private static boolean deleteDir( File dir )
	{
		if ( dir.isDirectory( ) )
		{
			String[] children = dir.list( );
			for ( int i = 0; i < children.length; i++ )
			{
				boolean success = deleteDir( new File( dir, children[i] ) );
				if ( !success )
				{
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		System.out.println( "Cleaned file: " + dir.getPath( ) ); //$NON-NLS-1$
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
