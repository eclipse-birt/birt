/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.data.oda.util.driverconfig.DriverLibraries;
import org.eclipse.birt.data.oda.util.driverconfig.DriverSetup;
import org.eclipse.birt.data.oda.util.driverconfig.LibrariesForOS;
import org.eclipse.birt.data.oda.util.driverconfig.OpenDataAccessConfig;
import org.eclipse.birt.data.oda.util.driverconfig.RunTimeInterface;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public class JdbcDriverConfigUtil
{

	private String driverName = "jdbc"; //$NON-NLS-1$

	/**
	 *  
	 */
	public JdbcDriverConfigUtil( )
	{
		super( );
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param driverName
	 */
	public JdbcDriverConfigUtil( String driverName )
	{
		super( );
		this.driverName = driverName;
	}

	/**
	 * @return Returns the driverName.
	 */
	public String getDriverName( )
	{
		return driverName;
	}

	/**
	 * @param driverName
	 *            The driverName to set.
	 */
	public void setDriverName( String driverName )
	{
		this.driverName = driverName;
	}

	private File getDriverLocation( )
	{
        registerDriver();
        ConfigManager configMgr = ConfigManager.getInstance( );
        return configMgr.getDriverDefaultLibPath(getDriverName());
	}
    
    private final void registerDriver()
    {
        DriverSetup.setUp();
    }

	/**
	 * @return
	 */
	public ArrayList getDriverFiles( )
	{
        registerDriver();
		ConfigManager configMgr = ConfigManager.getInstance( );

		OpenDataAccessConfig config = null;

		try
		{
			config = configMgr.getDriverConfig( driverName );
		}
		catch ( FileNotFoundException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}

		if ( config == null )
		{
			return null;
		}

		// get library information
		RunTimeInterface runtimeIntf = config.getRunTimeInterface( );
		DriverLibraries driverLibs = runtimeIntf.getDriverLibraries( );
		LibrariesForOS[] libSets = driverLibs.getLibrariesForOS( );
		//LibrariesForOS[] libSets = driverLibs.getLibrariesForOS();

		ArrayList files = new ArrayList( );
		for ( int index = 0; index < libSets.length; index++ )
		{
			LibrariesForOS lib4OS = libSets[index];

			// location of the libraries
			String libLocation = lib4OS.getLocation( );

			if ( libLocation == null || libLocation.trim( ).length( ) == 0 )
			{
				// If no Location is specified , then it is the directory where
				// the
				// oadconfig.xml file exists
				// i.e the odahomedirectory + "/" + drivername
				libLocation = this.getDriverLocation( ).getAbsolutePath();
			}

			// iterate through the library names
			String[] libraryNames = lib4OS.getLibraryName( );
			for ( int j = 0; j < libraryNames.length; j++ )
			{
				// Construct the full path of the jar file
				String fullPath = libraryNames[j];
				if ( libLocation != null )
				{
					fullPath = libLocation + "/" + fullPath; //$NON-NLS-1$
				}
				files.add( new File( fullPath ) );
			}
		}

		return files;

	}
}
