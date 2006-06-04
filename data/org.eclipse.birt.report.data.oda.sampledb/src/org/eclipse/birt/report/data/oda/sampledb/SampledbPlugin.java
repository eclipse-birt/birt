/*******************************************************************************
 * Copyright (c) 2004 - 2006 Actuate Corporation.
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


/**
 * Plugin class for Sample DB
 * This class initializes a private copy of the Sample database by unzipping the DB
 * files to a subdir in the TEMP directory. A private copy is required because
 * (1) Derby 10.1.2.1 has a bug which disabled BIRT read-only access to a JAR'ed 
 *     DB. (See http://issues.apache.org/jira/browse/DERBY-854)
 * (2) BIRT instances in multiple JVMs may try to access the sample DB (such when preview
 *     mode). We will corrupt the DB if a single copy of the DB is used
 */
public class SampledbPlugin extends BIRTPlugin
{
	private static final Logger logger = Logger.getLogger( SampledbPlugin.class.getName( ) );
	
	private static String dbDir;
	private static final String SAMPLE_DB_NAME = "BirtSample";
	private static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar";
	private static final String SAMPLE_DB_HOME_DIR = "db";
	
	private static int startCount = 0;
	
	/**
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		logger.info( "Sampledb plugin starts up. Current startCount=" + startCount);
		synchronized ( SampledbPlugin.class )
		{
			if ( ++startCount == 1 )
			{
				// First time to start for this instance of JVM
				// initialze database directory now
				init();
			}
		}
		super.start(context);
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		logger.info( "Sampledb plugin stopping. Current startCount=" + startCount);
		synchronized ( SampledbPlugin.class )
		{
			if ( startCount >= 1 )
			{
				if ( -- startCount == 0 )
				{
					// Last one to stop for this instance of JVM
					// Clean up Derby and temp files
					cleanUp();
				}
			}
		}
		super.stop(context);
	}
	
	private void cleanUp() throws Exception
	{
		// Stop Derby engine
		shutDownDatabase();
		// Clean up database files
		removeDatabase();
		
		dbDir = null;
	}
	
	/**
	 * Initialization for first time startup in this instance of JVM
	 */
	private void init() throws IOException
	{
		assert dbDir == null;
		
		// Create and remember our private directory under system temp
		// Name it "BIRTSampleDB_$timestamp$_$classinstanceid$"
		String tempDir = System.getProperty( "java.io.tmpdir" );
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String instanceId = Integer.toHexString( hashCode() );
		dbDir = tempDir + "/BIRTSampleDB_" + timeStamp + "_" + instanceId;
		logger.info( "Creating Sampledb database at location " + dbDir );
		(new File(dbDir)).mkdir();

		// Set up private copy of Sample DB in system temp directory

		// Get an input stream to read DB Jar file
		Bundle bundle = Platform.getBundle( SampleDBConstants.PLUGIN_ID );
		String dbEntryName = SAMPLE_DB_HOME_DIR + "/" + SAMPLE_DB_JAR_FILE;
		URL fileURL = bundle.getEntry( dbEntryName );
		if ( fileURL == null )
		{
			String errMsg = "INTERNAL ERROR: SampleDB DB file not found: " + dbEntryName;
			logger.severe( errMsg );
			throw new RuntimeException( errMsg );
		}

		// Copy entries in the DB jar file to corresponding location in db dir
		InputStream dbFileStream = new BufferedInputStream( fileURL.openStream( ) );
		ZipInputStream zipStream = new ZipInputStream( dbFileStream );
		ZipEntry entry;
		while ( (entry = zipStream.getNextEntry()) != null )
		{
			File entryFile = new File( dbDir, entry.getName() );
			if ( entry.isDirectory() )
			{
				entryFile.mkdir();
			}
			else
			{
				// Copy zip entry to local file
				OutputStream os = new FileOutputStream( entryFile );
		        byte[] buf = new byte[4000];
		        int len;
		        while ( (len = zipStream.read(buf)) > 0) 
		        {
		            os.write(buf, 0, len);
		        }
		        os.close();
			}
		}
			
		zipStream.close();
		dbFileStream.close();
	}

	/**
	 * Gets Derby connection URL
	 * */
	public static String getDBUrl( )
	{
		return getDBUrl(false);
	}
	
	/**
	 * Gets Derby connection URL
	 * @param shutdown if true, generate a URL for shutting down DB
	 */
	public static String getDBUrl( boolean shutdown )
	{
		String url = "jdbc:derby:" + dbDir + "/" + SAMPLE_DB_NAME;
		if ( shutdown )
			url += ";shutdown=true";
		return url;
	}
	
	/**
	 * Shuts down the Derby database
	 */
	private void shutDownDatabase()
	{
		logger.info( "Stopping Sampledb database at location " + dbDir );
		// Must shutdown this Derby DB before attempting to delete files
		try
		{
			JDBCDriverManager.getInstance().getConnection( 
					SampleDBConstants.DERBY_DRIVER_CLASS, 
					getDBUrl(true), 
					SampleDBConstants.SAMPLE_DB_SCHEMA, "" );
		}
		catch (Exception e)
		{
			// Exception is always generated when shutting down Derby; ignore it here
			logger.info( "Expected exception: " + e.getLocalizedMessage() );
		}
	}
	
	/**
	 * Deletes all files created for the Derby database
	 */
	private void removeDatabase()
	{
		logger.info( "Removing Sampledb DB directory at location " + dbDir );
		// recursively delete the DB directory
		if ( ! removeDirectory( new File(dbDir) ))
		{
			logger.warning( "Failed to remove one or more file in temp db directory: " + dbDir );
		}
	}
	
	/**
	 * Do a best-effort removal of directory. 
	 */
	static boolean removeDirectory( File dir ) 
	{
		assert dir != null && dir.isDirectory();
		boolean success = true;
		String[] children = dir.list();
		for	( int i=0; i < children.length; i++) 
		{
			File child = new File( dir, children[i]);
			if( child.isDirectory()) 
			{
				if (! removeDirectory( child ) )
				{
					success = false;
				}
			}
			else 
			{
				if ( ! child.delete() )
				{
					logger.info( "Failed to delete temp file " + child.getAbsolutePath());
					success = false;
				}
	         }
		}
		if ( ! dir.delete() )
		{
			success = false;
		}
		return success;
	}
}

