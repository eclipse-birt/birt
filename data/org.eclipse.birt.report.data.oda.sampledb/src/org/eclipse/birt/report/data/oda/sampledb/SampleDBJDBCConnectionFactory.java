/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides JDBC connection to the Derby database embedded in this plugin.
 */
import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;
import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class SampleDBJDBCConnectionFactory implements IConnectionFactory
{
	static private final Logger logger = Logger.getLogger( SampleDBJDBCConnectionFactory.class.getName() );
	
	public static class Constants
	{
		// Driver class name. Note that this class does not actually exist. It's only a name to identify
		// this connection provider
		public static final String DRIVER_CLASS = "org.eclipse.birt.report.data.oda.sampledb.Driver";
		
		// URL accepted by this driver
		public static final String DRIVER_URL = "jdbc:classicmodels:sampledb";
		
		// ID of this plugin
		public static final String PLUGIN_ID="org.eclipse.birt.report.data.oda.sampledb";
	}
	
	private static final String SAMPLE_DB_JAR_FILE="BirtSample.jar";
	private static final String SAMPLE_DB_NAME="BirtSample";
	private static final String SAMPLE_DB_HOME_DIR="db";
	private static final String DERBY_DRIVER_CLASS="org.apache.derby.jdbc.EmbeddedDriver";
	private static final String SAMPLE_DB_SCHEMA="ClassicModels";

	private static String dbUrl;
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
			logger.log( Level.WARNING, "SampleDB: cannot resolve local file path for plugin:  " 
					+ Constants.PLUGIN_ID,	e );
		}
	}

	/**
	 * Creates a new JDBC connection to the embedded sample database. 
	 * @see org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory#getConnection(java.lang.String, java.lang.String, java.util.Properties)
	 */
	public Connection getConnection(String driverClass, String url, Properties connectionProperties)
			throws SQLException
	{
		if ( ! driverClass.equals( Constants.DRIVER_CLASS) )
		{
			// This is unexpected; we shouldn't be getting this call
			logger.log( Level.SEVERE, "Unexpected driverClass: " + driverClass );
			throw new SQLException("Unexpected driverClass " + driverClass);
		}
		if ( ! url.equals(Constants.DRIVER_URL) )
		{
			// Wrong url
			logger.log( Level.WARNING, "Unexpected url: " + url );
			throw new SQLException("Classic Models Inc. Sample Database Driver does not recognize url: " + driverClass);
		}

		String dbUrl = getDbUrl();
		
		// Copy connection properties and replace user and password with fixed value
		Properties props;
		if ( connectionProperties != null )
			props = (Properties)connectionProperties.clone();
		else 
			props = new Properties(); 
		props.put( "user", SAMPLE_DB_SCHEMA);
		props.put("password", "");
		
		try
		{
			return JDBCDriverManager.getInstance().getConnection( 
					DERBY_DRIVER_CLASS, dbUrl, props );
		}
		catch (OdaException e)
		{
			throw new SQLException(e.getLocalizedMessage());
		}
	}
	
	/**
	 * @return Url to be used with Derby Embedded driver to connect to embedded database
	 */
	public static String getDbUrl()
	{
		return dbUrl;
	}
	
	/**
	 * @return user name for db connection
	 */
	public static String getDbUser()
	{
		return SAMPLE_DB_SCHEMA;
	}
	
	
	/**
	 * @return Local path of the plugin's home directory
	 */
	private static String getHomeDir() throws OdaException, IOException
	{
		String result = null;
		IBundle bundle = Platform.getBundle( Constants.PLUGIN_ID );
		URL pluginHomeUrl = bundle.getEntry( "/" );
		pluginHomeUrl = Platform.asLocalURL( pluginHomeUrl );
        try 
		{
            URI uri = new URI(pluginHomeUrl.toString());
            result = uri.getPath();            
        } 
        catch ( URISyntaxException e)
		{
	            result = pluginHomeUrl.getFile();
        }
        return result;
	}
}
