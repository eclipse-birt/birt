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

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IDriver;
import org.eclipse.birt.data.oda.LogConfiguration;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.manifest.ExtensionManifest;
import org.eclipse.birt.data.oda.util.manifest.ManifestExplorer;

/**
 * This class implements IDriver, which is the entry point for the
 * ODA consumer.
 * 
 */
public class OdaJdbcDriver implements IDriver
{
	private static Logger logger = Logger.getLogger( OdaJdbcDriver.class.getName( ) );	
	

	public static final class Constants
	{
		/** Name of directory that contains user provided JDBC drivers */
		public static final String DRIVER_DIRECTORY = "drivers";
		
		/** ODA data source ID; must match value of dataSource.id attribute defined in extension */
		public static final String DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.jdbc";
		
		/** Full name of driverinfo extension */
		public static final String DRIVER_INFO_EXTENSION = "org.eclipse.birt.report.data.oda.jdbc.driverinfo";
		
		public static final String DRIVER_INFO_ATTR_NAME = "name";
		public static final String DRIVER_INFO_ATTR_DRIVERCLASS= "driverClass";
		public static final String DRIVER_INFO_ATTR_URLTEMPL = "urlTemplate";
		public static final String DRIVER_INFO_ATTR_CONNFACTORY = "connectionFactory";
		public static final String DRIVER_INFO_ELEM_JDBCDRIVER = "jdbcDriver";
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDriver#getConnection(java.lang.String)
	 */
	public IConnection getConnection( String connectionClassName )
			throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				OdaJdbcDriver.class.getName( ),
				"getConnection",
				"JDBCConnectionFactory.getConnection( ) connectionClassName=" +connectionClassName);
		return new Connection( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.oda.IDriver#getMaxConnections()
	 */
	public int getMaxConnections() throws OdaException
	{
		// The max # of connection is unknown to this Driver; each JDBC driver can have a limit
		// on the # of connection. This Driver can connect to mulitple JDBC drivers.
		return 0;
	}
	
	
	/**
	 * @see org.eclipse.birt.data.oda.IDriver#setLogConfiguration(org.eclipse.birt.data.oda.LogConfiguration)
	 */
	public void setLogConfiguration(LogConfiguration logConfig)
			throws OdaException
	{
		// no-op; this drivers uses its own logging facility (java.util.logging)
	}
	
	/**
	 * Gets the location of the "drivers" subdirectory of this plugin
	 */
	public static File getDriverDirectory() throws OdaException, IOException
	{
		File result = null;
		ExtensionManifest extMF = 
			ManifestExplorer.getInstance().getExtensionManifest( Constants.DATA_SOURCE_ID );
		if ( extMF != null )
		{
		    URL url = extMF.getRuntimeInterface().getLibraryLocation();
		    try
			{
		    	URI uri = new URI(url.toString());
				result = new File( uri.getPath(), Constants.DRIVER_DIRECTORY );
			}
			catch ( URISyntaxException e )
			{
				result = new File( url.getFile(), Constants.DRIVER_DIRECTORY );
			}
		}
		
		return result;
	}
	
	/**
	 * Lists all possible driver files (those ending with .zip or .jar) in the drivers directory
	 */
	public static File[] getDriverFileList() throws OdaException, IOException
	{
		File driverHomeDir = getDriverDirectory();
		String files[] = driverHomeDir.list( new FilenameFilter() {
					public boolean accept( File dir,String name )
					{	return isDriverFile( name ); }
				} );
		File[] ret = new File[files.length];
		for ( int i = 0; i < files.length; i++ )
		{
			ret[i] = new File( driverHomeDir, files[i]);
		}
		return ret;
	}
		
    /**
     * Check to see if a file has the correct extension for a JDBC driver. ZIP and JAR files
     * are accepted
     */
	static boolean isDriverFile( String fileName )
	{
		String lcName = fileName.toLowerCase();
		return lcName.endsWith(".jar") || lcName.endsWith(".zip");
	}
}