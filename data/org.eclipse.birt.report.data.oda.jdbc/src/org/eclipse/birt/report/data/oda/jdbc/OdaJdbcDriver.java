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
	
	/** ODA data source ID; must match value of dataSource.id attribute defined in extension */
	public static final String DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.jdbc";

	/** Name of directory that contains user provided JDBC drivers */
	public static final String DRIVER_DIRECTORY = "drivers";

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
			ManifestExplorer.getInstance().getExtensionManifest( OdaJdbcDriver.DATA_SOURCE_ID );
		if ( extMF != null )
		{
		    URL url = extMF.getRuntimeInterface().getLibraryLocation();
		    try
			{
		    	URI uri = new URI(url.toString());
				result = new File( uri.getPath(), 
						DRIVER_DIRECTORY );
			}
			catch ( URISyntaxException e )
			{
				result = new File( url.getFile(), 
						DRIVER_DIRECTORY );
			}
		}
		
		return result;
	}
}