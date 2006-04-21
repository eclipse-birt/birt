/*******************************************************************************
 * Copyright (c) 2004, 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.sampledb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.osgi.framework.Bundle;

/**
 * Wrap the constants used in SampleDB.
 */
public class SampleDBConstants
{
	// Driver class name. Note that this class does not actually exist. It's
	// only a name to identify this connection provider
	public static final String DRIVER_CLASS = "org.eclipse.birt.report.data.oda.sampledb.Driver";

	// URL accepted by this driver
	public static final String DRIVER_URL = "jdbc:classicmodels:sampledb";

	// ID of this plugin
	public static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.sampledb";
	
	static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
	static final String SAMPLE_DB_SCHEMA = "ClassicModels";
	
	private static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar";
	private static final String SAMPLE_DB_NAME = "BirtSample";
	private static final String SAMPLE_DB_HOME_DIR = "db";
	private static String dbUrl;

	private static final Logger logger = Logger.getLogger( SampleDBConstants.class.getName( ) );
	
	/**
	 * @return
	 * @throws SQLException
	 */
	static String getDBUrl( ) throws SQLException
	{
		if ( dbUrl != null )
			return dbUrl;
		
		synchronized ( SampleDBConstants.class )
		{
			if ( dbUrl == null )
			{
				try
				{
					// Find the absolute path of the plugin home directory
					
					// Construct a Derby embedded URL using the absolute
					// path to the Db directory
					File dbDir = getDBDir( );
					
					if ( dbDir.exists( ) == false ) // maybe in war environment
					{
						dbDir = getNewDBDir( );
						if ( dbDir == null || dbDir.exists( ) == false )
							dbDir = new File( "." );
					}
					
					File dbFile = new File( dbDir, SAMPLE_DB_JAR_FILE );
					dbUrl = "jdbc:derby:jar:("
							+ dbFile.getAbsolutePath( ) + ")" + SAMPLE_DB_NAME;
					logger.log( Level.INFO, "SampleDB driver loaded. Url="
							+ dbUrl );
					
					System.setProperty( "derby.system.home",
							dbDir.getAbsolutePath( ) );
					System.setProperty( "derby.storage.tempDirectory",
							dbDir.getAbsolutePath( ) );
					System.setProperty( "derby.stream.error.file",
							new File( dbDir, "error.log" ).getAbsolutePath( ) );
				}
				catch ( IOException e )
				{
					throw new SQLException( "Can not open the database file because of: "
							+ e.getMessage( ) );
				}
			}
			
			return dbUrl;
		}
	}
	
	/**
	 * @return Local path of the plugin's home directory
	 */
	private static File getDBDir( ) throws IOException
	{
		String driverHome = null;
		Bundle bundle = Platform.getBundle( SampleDBConstants.PLUGIN_ID );
		URL pluginHomeUrl = bundle.getEntry( "/" );
		pluginHomeUrl = Platform.asLocalURL( pluginHomeUrl );
		try
		{
			URI uri = new URI( pluginHomeUrl.toString( ) );
			driverHome = uri.getPath( );
		}
		catch ( URISyntaxException e )
		{
			driverHome = pluginHomeUrl.getFile( );
		}
		
		return new File( driverHome, SAMPLE_DB_HOME_DIR );
	}

	/**
	 * @return
	 * @throws OdaException
	 * @throws IOException
	 */
	private static File getNewDBDir( ) throws IOException
	{
		File newFile = null;

		Bundle bundle = Platform.getBundle( SampleDBConstants.PLUGIN_ID );
		Enumeration files = bundle.getEntryPaths( SAMPLE_DB_HOME_DIR );
		if ( files != null && files.hasMoreElements( ) )
		{
			String fileName = (String) files.nextElement( );
			URL fileURL = bundle.getEntry( fileName );

			InputStream is = fileURL.openStream( );
			BufferedInputStream bis = new BufferedInputStream( is );

			newFile = new File( getTempDir( )
					+ File.separator + SAMPLE_DB_JAR_FILE );
			OutputStream os = new FileOutputStream( newFile );
			BufferedOutputStream bos = new BufferedOutputStream( os );

			while ( true )
			{
				int b = bis.read( );
				if ( b == -1 )
					break;
				else
					bos.write( b );
			}

			bos.close( );
			os.close( );

			bis.close( );
			is.close( );
		}

		if ( newFile == null )
			return null;
		else
			return new File( newFile.getAbsolutePath( ) ).getParentFile( );
	}

	/**
	 * @return temp dir for sampledb jar file
	 * @throws IOException
	 */
	private synchronized static String getTempDir( ) throws IOException
	{
		final String DirName = "BIRTSampleDBTemp";
		String tempDirStr = System.getProperty( "java.io.tmpdir" );
		File tempDBDir = new File( tempDirStr, DirName );
		if ( tempDBDir.exists( ) == false )
		{
			tempDBDir.mkdir( );
		}
		else
		{
			File[] sessionsFolder = tempDBDir.listFiles( );
			for ( int i = 0; i < sessionsFolder.length; i++ )
			{
				File[] oneSessionFolder = sessionsFolder[i].listFiles( );
				if ( oneSessionFolder != null )
					for ( int j = 0; j < oneSessionFolder.length; j++ )
						oneSessionFolder[j].delete( );
				sessionsFolder[i].delete( );
			}
		}

		File newFile = null;
		int i = 0;
		while ( true )
		{
			newFile = new File( tempDirStr
					+ File.separator + DirName + File.separator + ( i++ ) );
			if ( newFile.exists( ) == false )
			{
				newFile.mkdir( );
				break;
			}
		}

		return newFile.getAbsolutePath( );
	}

}
