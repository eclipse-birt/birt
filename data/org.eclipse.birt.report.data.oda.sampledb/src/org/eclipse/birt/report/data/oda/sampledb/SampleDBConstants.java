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
package org.eclipse.birt.report.data.oda.sampledb;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.datatools.connectivity.oda.OdaException;

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

	static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar";
	static final String SAMPLE_DB_NAME = "BirtSample";
	static final String SAMPLE_DB_HOME_DIR = "db";
	static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
	static final String SAMPLE_DB_SCHEMA = "ClassicModels";

	private static final Logger logger = Logger.getLogger( SampleDBConstants.class.getName( ) );

	static String dbUrl;
	
	static
	{
		// Class static code to initialize Derby resources

		// Find the absolute path of the plugin home directory
		try
		{
			// Construct a Derby embedded URL using the absolute
			// path to the Db directory
			String driverHome = getHomeDir( );

			File dbDir = new File( driverHome, SAMPLE_DB_HOME_DIR );
			File dbFile = new File( dbDir, SAMPLE_DB_JAR_FILE );
			dbUrl = "jdbc:derby:jar:("
					+ dbFile.getAbsolutePath( ) + ")" + SAMPLE_DB_NAME;
			logger.log( Level.INFO, "SampleDB driver loaded. Url=" + dbUrl );

			System.setProperty( "derby.system.home", dbDir.getAbsolutePath( ) );
			System.setProperty( "derby.storage.tempDirectory",
					dbDir.getAbsolutePath( ) );
			System.setProperty( "derby.stream.error.file", new File( dbDir,
					"error.log" ).getAbsolutePath( ) );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING,
					"SampleDB: cannot resolve local file path for plugin:  "
							+ SampleDBConstants.PLUGIN_ID,
					e );
		}
	}
	
	/**
	 * @return Local path of the plugin's home directory
	 */
	private static String getHomeDir( ) throws OdaException, IOException
	{
		String result = null;
		IBundle bundle = Platform.getBundle( SampleDBConstants.PLUGIN_ID );
		URL pluginHomeUrl = bundle.getEntry( "/" );
		pluginHomeUrl = Platform.asLocalURL( pluginHomeUrl );
		try
		{
			URI uri = new URI( pluginHomeUrl.toString( ) );
			result = uri.getPath( );
		}
		catch ( URISyntaxException e )
		{
			result = pluginHomeUrl.getFile( );
		}
		return result;
	}

}
