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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.data.oda.util.driverconfig.DriverLibraries;
import org.eclipse.birt.data.oda.util.driverconfig.LibrariesForOS;
import org.eclipse.birt.data.oda.util.driverconfig.OpenDataAccessConfig;
import org.eclipse.birt.data.oda.util.driverconfig.RunTimeInterface;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class JdbcDriverConfigUtil
{

	// Corrresponds to a subdirectory within the
	// ODA home directro
	private String driverName = "jdbc"; //$NON-NLS-1$
	private File odaHomeDirectory = null;

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

	private String getDriverLocation( )
	{
		if ( odaHomeDirectory != null && driverName != null )
		{
			return odaHomeDirectory.getAbsolutePath( );
		}

		return null;
	}

	/**
	 * @return
	 */
	public ArrayList getDriverFiles( )
	{
		ConfigManager configMgr = ConfigManager.getInstance( );

		if ( getDriverLocation( ) == null )
		{
			registerODADriver( );
		}

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
				libLocation = this.getDriverLocation( );
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
    
    // Searches Eclipse plugins for all ODA drivers, which implement the birt ODA extension point
    private void registerODADriver()
    {
        // Find all extensions implementing ODA extension point
        IExtensionRegistry reg = Platform.getExtensionRegistry();
        if ( reg == null )
            return;             // Not running within eclipse
        
        IExtensionPoint extPoint = reg.getExtensionPoint(
                                    "org.eclipse.birt.data.oda" );
        if ( extPoint == null )
            return;
        
        IExtension[] exts = extPoint.getExtensions();
        for ( int i = 0; i < exts.length; i ++)
        {
            IExtension ext = exts[i];
            IConfigurationElement[] elems = ext.getConfigurationElements();
            
            // We expect one "driver" element; find it in the array
            IConfigurationElement elem = null;
            for ( int j = 0; elems != null && j < elems.length; j ++)
            {
                if ( "driver".equals(elems[j].getName()))
                {
                    elem = elems[j];
                    break;
                }
                // This is not an expected element
                // TODO: log a warming about invalid plugin manifest
                System.err.println( "Invalid config element in plugin manifest: " + elems[i].getName() );
            }
            
            if ( elem != null )
            {
                String name = elem.getAttribute( "name");
                String configDir = elem.getAttribute( "configFileDir");
                if ( name != null && name.equals(getDriverName()))
                {
                    addODADriverPlugin( ext, name, configDir );
                    break;
                }
            }
            else
            {
                // TODO: log an error about invalid extension
                // this should not stop us from continuing though
                System.err.println( "Invalid plugin manifest for ODA driver" );
            }
        }
    }
    
    private void addODADriverPlugin( IExtension extension, String driverName, String configDir )
    {
        // configDir, if not absolute, is relative to the plugin's root directory
        // If it is empty, it's the same as the plugin's root
        if ( configDir == null )
            configDir = "";
        File absConfigDir = new File( configDir );
        if ( ! absConfigDir.isAbsolute() )
        {
            // Get the plugin as a Bundle object
            String pluginName = extension.getNamespace();
            Bundle bundle = Platform.getBundle( pluginName );
        
            // Find the local URL of the plugin's root
            // This is expected to be in the "file://" protocol
            URL root = bundle.getEntry( "/" );
            URL localRoot;
            try
            {
                localRoot = Platform.asLocalURL( root );
            }
            catch ( IOException e )
            {
                // TODO: log exception
                e.printStackTrace( System.err );
                return;
            }

			absConfigDir = new File( localRoot.getPath( ), configDir );
		}
		odaHomeDirectory = absConfigDir;

		ConfigManager.getInstance( ).addDriver( driverName, absConfigDir );
	}

}
