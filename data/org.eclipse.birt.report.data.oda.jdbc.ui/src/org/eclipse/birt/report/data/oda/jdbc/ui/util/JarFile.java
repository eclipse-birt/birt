
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ResourceBundle;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Jar file information and related action like add jar, delelte jar, check jar
 * state
 */
public class JarFile implements Serializable
{
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -765442524028110564L;

	/**
	 * File name of the Jar file
	 */
	private String fileName;
	
	/**
	 * full path of the Jar file
	 */
	private String filePath;

	/**
	 * jar state of 'oda file not exist','original file not exist','has been restored'
	 */
	private transient String state;

	/**
	 * whether the jar file will be deleted on exit.
	 */
	private transient boolean toBeDeleted;

	/**
	 * indicate whether the jar file has been restored,it is a inner state flag
	 * used by checkJarState()
	 */
	private transient boolean hasRestored;

	/**
	 * The key for BIRT viewer drivers path property in plugin.properties.
	 */
	private static final String VIEWER_DRIVER_PATH_KEY = "birt-viewer-driver-path"; //$NON-NLS-1$
	
	public static final String FILE_HAS_BEEN_RESOTRED = "+"; //$NON-NLS-1$
	public static final String ODA_FILE_NOT_EXIST_TOKEN = "x"; //$NON-NLS-1$
	public static final String ORIGINAL_FILE_NOT_EXIST_TOKEN = "*"; //$NON-NLS-1$

	public JarFile( String fileName, String filePath, String state,
			boolean toBeDeleted )
	{
		this.fileName = fileName;
		this.filePath = filePath;
		this.state = state;
		this.toBeDeleted = toBeDeleted;
		hasRestored = false;
	}
	
	public String getFilePath( )
	{
		return filePath;
	}

	public String getFileName( )
	{
		return fileName;
	}
	
	public String getState( )
	{
		return state;
	}
	
	public boolean isToBeDeleted( )
	{
		return toBeDeleted;
	}
	
	public void setToBeDeleted( boolean toBeDeleted )
	{
		this.toBeDeleted = toBeDeleted;
	}
	
	public void setRestored( )
	{
		this.hasRestored = true;
	}
	
	/**
	 * Copies the specified file to ODA driver path and viewer dirver path.
	 * 
	 * @param filePath
	 */
	public void copyJarToODADir( )
	{
		File source = new File( filePath );

		File odaDir = getDriverLocation( );
		File viewDir = getViewerDriverLocation( );

		File dest1 = null, dest2 = null;

		if ( odaDir != null )
		{
			dest1 = new File( odaDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
		}
		if ( viewDir != null )
		{
			dest2 = new File( viewDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
		}

		if ( source.exists( ) )
		{
			FileChannel in = null, out1 = null, out2 = null;
			try
			{
				if ( dest1 != null )
				{
					try
					{
						out1 = new FileOutputStream( dest1 ).getChannel( );
					}
					catch ( FileNotFoundException e )
					{
						//does nothing.
					}
				}
				if ( dest2 != null )
				{
					try
					{
						out2 = new FileOutputStream( dest2 ).getChannel( );
					}
					catch ( FileNotFoundException e )
					{
						//does nothing.
					}
				}

				if ( out1 != null )
				{
					in = new FileInputStream( source ).getChannel( );
					long size = in.size( );
					MappedByteBuffer buf = in.map( FileChannel.MapMode.READ_ONLY,
							0,
							size );
					out1.write( buf );
				}
				
				try
				{
					if ( in != null )
					{
						in.close( );
					}
				}
				catch ( IOException e1 )
				{
					//does nothing.
				}
				
				if ( out2 != null )
				{
					in = new FileInputStream( source ).getChannel( );
					long size = in.size( );
					MappedByteBuffer buf = in.map( FileChannel.MapMode.READ_ONLY,
							0,
							size );
					out2.write( buf );
				}

			}
			catch ( FileNotFoundException e )
			{
				//does nothing.
			}
			catch ( IOException e )
			{
				//does nothing.
			}
			finally
			{
				try
				{
					if ( in != null )
					{
						in.close( );
					}
					if ( out1 != null )
					{
						out1.close( );
					}
					if ( out2 != null )
					{
						out2.close( );
					}
				}
				catch ( IOException e1 )
				{
					//does nothing.
				}
			}
		}
	}

	/**
	 * Deletes the specified file from ODA driver path and viewer dirver path,
	 * NOTE just the file name is used.
	 * 
	 * @param filePath
	 */
	public void deleteJarFromODADir()
	{
		File source = new File( filePath );

		File odaDir = getDriverLocation( );
		File viewDir = getViewerDriverLocation( );

		File dest1 = null, dest2 = null;

		if ( odaDir != null )
		{
			dest1 = new File( odaDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );

			if ( dest1.exists( ) )
			{
				if ( !dest1.delete( ) )
				{
					dest1.deleteOnExit( );
				}
			}
		}
		if ( viewDir != null )
		{
			dest2 = new File( viewDir.getAbsolutePath( )
					+ File.separator
					+ source.getName( ) );
			if ( dest2.exists( ) )
			{
				if ( !dest2.delete( ) )
				{
					dest2.deleteOnExit( );
				}
			}
		}
	}
		
	/**
	 * check if the jar exist in the oda driver directory or exist in the disk.
	 * x - not exist in the oda dirctory. <br>* - not exist in the disk.
	 */
	public void checkJarState( )
	{
		if ( hasRestored == true )
		{
			state = FILE_HAS_BEEN_RESOTRED;
		}
		else
		{
			File f = new File( filePath );
			if ( !isUnderODAPath( f ) )
			{
				if ( f.exists( ) )
					state = JarFile.ODA_FILE_NOT_EXIST_TOKEN;
				else
					state = JarFile.ODA_FILE_NOT_EXIST_TOKEN
							+ JarFile.ORIGINAL_FILE_NOT_EXIST_TOKEN;
			}
			else
			{
				if ( f.exists( ) )
					state = ""; //$NON-NLS-1$
				else
					state = JarFile.ORIGINAL_FILE_NOT_EXIST_TOKEN;
			}
		}
	}
	
	/**
	 * Returns the ODA dirvers directory path. <br>
	 */
	public static File getDriverLocation( )
	{
		try
		{
			return OdaJdbcDriver.getDriverDirectory();
		}
		catch ( IOException e )
		{ // TODO
			ExceptionHandler.showException( null, "title", "msg", e );
		}
		catch ( OdaException e )
		{ // TODO
			ExceptionHandler.showException( null, "title", "msg", e );
		}

		return null;
	}
	
	/**
	 * check whether the given file is under ODA Drivers path.
	 * @param f  the file to be checked
	 * @return true if <tt>f</tt> is under ODA Drivers path,else false
	 */
	private boolean isUnderODAPath( File f )
	{
		File odaPath = getDriverLocation( );

		File ff = new File( odaPath + File.separator + f.getName( ) );

		return ff.exists( );
	}
	
	/**
	 * Returns the viewer drivers directory path. <br>
	 * TODO: may change if viewer plugin provide more convenient api.
	 * 
	 * @return directory path indicate DriverLocation in Viewer
	 */
	private File getViewerDriverLocation( )
	{
		//get the driver path under viewer plug-in.
		Bundle viewerBundle = Platform.getBundle( "org.eclipse.birt.report.viewer" ); //$NON-NLS-1$
		if ( viewerBundle != null )
		{
			ResourceBundle resBundle = Platform.getResourceBundle( JdbcPlugin.getDefault( )
					.getBundle( ) );
			if ( resBundle != null )
			{
				String viewerLocation = null;
				try
				{
					URL url = Platform.asLocalURL( viewerBundle.getEntry( "/" ) );
					try
					{
						URI uri = new URI( url.toString( ) );
						viewerLocation = uri.getPath( );
					}
					catch ( URISyntaxException e )
					{
						viewerLocation = url.getFile( );
					}
				}
				catch ( Exception e )
				{
					viewerLocation = viewerBundle.getLocation( ).substring( 7 );
				}
				String driverPath = viewerLocation
						+ resBundle.getString( JarFile.VIEWER_DRIVER_PATH_KEY );
				
				File driverLoc = new File( driverPath );
				if ( driverLoc.exists( ) == false )
					driverLoc.mkdir( );
				
				return driverLoc;
			}
		}
		return null;
	}

}
