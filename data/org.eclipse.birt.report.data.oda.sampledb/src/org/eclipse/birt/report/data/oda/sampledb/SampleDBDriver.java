/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.oda.sampledb;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.manifest.ExtensionManifest;
import org.eclipse.birt.data.oda.util.manifest.ManifestExplorer;
import org.eclipse.birt.report.data.oda.jdbc.Connection;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;

/**
 * Implements the BIRT ODA connection factory interface. 
 */
public class SampleDBDriver extends OdaJdbcDriver
{
	public static final String SAMPLE_DB_JAR_FILE="BirtSample.jar";
	public static final String SAMPLE_DB_NAME="BirtSample";
	public static final String SAMPLE_DB_HOME_DIR="db";
	public static final String DRIVER_CLASS="org.apache.derby.jdbc.EmbeddedDriver";
	
	public static final String DATA_SOURCE_ID="org.eclipse.birt.report.data.oda.sampledb";
	public static final String SAMPLE_DB_SCHEMA="ClassicModels";
	
	private static Logger logger = Logger.getLogger( SampleDBDriver.class.getName());
	
	private static String dbUrl = ""; 
	
	static
	{
		// Class static code to initialize Derby resources
		
		// Find the absolute path of the plugin home directory
		try
		{
			// Construct a Derby embedded URL using the absolute
			// path to the Db directory			
			String driverHome = getHomeDir();
	    	
			File dbDir = new File( driverHome, SAMPLE_DB_HOME_DIR );
			File dbFile = new File (dbDir, SAMPLE_DB_JAR_FILE);
			dbUrl = "jdbc:derby:jar:(" + dbFile.getAbsolutePath() + ")" + SAMPLE_DB_NAME;
			logger.log( Level.INFO, "SampleDB driver loaded. Url=" + dbUrl);
			
			System.setProperty( "derby.system.home", dbDir.getAbsolutePath() );
			System.setProperty( "derby.storage.tempDirectory", dbDir.getAbsolutePath() );
			System.setProperty( "derby.stream.error.file", 
					new File( dbDir, "error.log").getAbsolutePath() );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, "SampleDB: cannot find driver config for: " + DATA_SOURCE_ID, 
					e );
		}
	}
	
	/**
	 * @see org.eclipse.birt.data.oda.IDriver#getConnection(java.lang.String)
	 */
	public IConnection getConnection(String connectionClassName) throws OdaException
	{
		return new SampleDBConnection();
	}

	/**
	 * Implements the BIRT ODA connection  interface. 
	 * This class wraps the oda.jdbc driver's actual implementation of ODA. 
	 * Connection properties as fixed for the Sample Database
	 */
	static private class SampleDBConnection extends Connection
	{
		
		/**
		 * @see org.eclipse.birt.data.oda.IConnection#open(java.util.Propertikes)
		 */
		public void open(Properties connProperties) throws OdaException
		{
			logger.entering( SampleDBConnection.class.getName(), "open");
			
			// Ignore all properties passed in (it's expected to be empty anyway)
			Properties props = new Properties();
			props.setProperty( Connection.Constants.ODADriverClass, DRIVER_CLASS );
			props.setProperty( Connection.Constants.ODAURL, getUrl( ) );
			props.setProperty( Connection.Constants.ODAUser, SAMPLE_DB_SCHEMA);
			
			if ( logger.isLoggable(Level.FINE ))
			{
				logger.log( Level.FINE, "Opening SampleDB connection. DriverClass="
						+ DRIVER_CLASS + "; url=" + getUrl() );
			}
			
			super.open( props );
			
			logger.exiting( SampleDBConnection.class.getName(), "open");
		}
	}
	
	public static String getUrl()
	{
		return dbUrl;
	}
	
	static private String getHomeDir() throws OdaException, IOException
	{
		String result = null;
		ExtensionManifest extMF = 
			ManifestExplorer.getInstance().getExtensionManifest( DATA_SOURCE_ID );
		if ( extMF != null )
		{
		    URL url = extMF.getRuntimeInterface().getLibraryLocation();
	        try 
			{
	            URI uri = new URI(url.toString());
	            result = uri.getPath();            
	        } 
	        catch ( URISyntaxException e)
			{
	            result = url.getFile();
	        }
		}
        return result;
	}
	
}
