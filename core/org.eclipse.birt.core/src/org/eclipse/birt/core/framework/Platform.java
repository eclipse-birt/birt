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

package org.eclipse.birt.core.framework;

import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.core.framework.eclipse.EclipsePlatform;
import org.eclipse.birt.core.framework.server.ServerPlatform;


/**
 * Defines a generic Platform class that wraps around an <code>EclipsePlatform</code> 
 * or <code>ServerPlatform</code> class. 
 * 
 * This class is a singleton.
 * 
 * To use SeverPlatform, you need define two system properties:
 * <code>PROPERTY_RUN_UNDER_ECLIPSE<code> to "false"
 * <code>PROPERTY_BIRT_HOME</code> to a folder which contains a sub folder "plugins".
 * 
 * There are some limitations in server side platform:
 * <li> only support re-export dependence (all packags in depended plugins are re-exported).
 * <li> don't support version matches
 * <li> don't support OSGi bundle mainfest.mf files
 * <li> don't support package level export in running time, all packages are exported.
 * 
 * Assume there is three plugins: A, B, C has following content:
 * <li> plugin A: exportA, exportB, exportC
 * <li> plugin B: exportB, exportC
 * <li> plugin C: exportC
 * If we define the plugin A depends on C, B (exact the order), then from the pluginA, we 
 * can only access:
 * exportA(plugin A), exportB (pluginB), exportC(pluginC).
 * If the dependcy order of plugin A is: B, C, then we can only access classes in plugin A:
 * exportA(pluginA), exportB(plugin B), exportC(plugin B).
 * 
 * @version $Revision: 1.9 $ $Date: 2005/07/07 00:26:36 $
 */
public class Platform
{
	public static String PROPERTY_RUN_UNDER_ECLIPSE = "RUN_UNDER_ECLIPSE";
	public static String PROPERTY_BIRT_HOME = "BIRT_HOME";

    public static int UNKNOWN_PLATFORM = 0;    
    public static int ECLIPSE_PLATFORM = 1;
    public static int SERVER_PLATFORM = 2;
    
	protected static int platformType = UNKNOWN_PLATFORM;
	protected static IPlatform platform = null;
	
	/**
	 * creates the appropriate platform object based on the platform type 
	 * If not running from Eclipse, this functions must be called before calling other functions.
	 */
	synchronized static public void initialize( IPlatformContext context )
	{
		if (platform == null)
		{
			if (runningEclipse())
			{
				platform = new EclipsePlatform(); 
				platformType = ECLIPSE_PLATFORM;
			}
			else
			{
				if ( context == null )
					context = new PlatformFileContext();

				platform = new ServerPlatform( context );
				platformType = SERVER_PLATFORM;
			}
		}
	}
	
	/**
	 * @return an extension registry
	 */
	public static IExtensionRegistry getExtensionRegistry()
	{
		if ( platform == null )
		{
	        initialize( null );
		}		
	    assert platform != null; // Note: If not runningEclipse, initialize function must be called explicitly before other functions get called.
	    
		return platform.getExtensionRegistry();
	}
	
	public static IBundle getBundle (String symbolicName)
	{
		if ( platform == null )
		{ 
	        initialize( null );
		}		
	    assert platform != null; // Note: If not runningEclipse, initialize function must be called explicitly before other functions get called.

		return platform.getBundle(symbolicName);
	}
	
	public static URL find(IBundle bundle, IPlatformPath path)
	{
		if ( platform == null )
		{
	        initialize( null );
		}		
	    assert platform != null; // Note: If not runningEclipse, initialize function must be called explicitly before other functions get called.
	    
		return platform.find( bundle, path );
	}
	
	/**
	 * @return the type of the platform. Available values are ECLIPSE_PLATFORM and
	 * SERVER_PLATFORM.
	 */
	public static int getPlatformType()
	{
		if ( platform == null)
		{
	        initialize( null );
		}		
	    assert platform != null; // Note: If not runningEclipse, initialize function must be called explicitly before other functions get called.
	    
		return platformType;
	}
	
	public static URL asLocalURL(URL url) throws IOException
	{
		if ( platform == null )
		{
	        initialize( null );
		}		
	    assert platform != null; // Note: If not runningEclipse, initialize function must be called explicitly before other functions get called.

		return platform.asLocalURL(url);
	}
	/**
	 * Checks whether Eclipse is running
	 * 
	 * @return whether we are running in Eclipse
	 */
	public static boolean runningEclipse()
	{
		String runningUnderEclipse = System.getProperty("RUN_UNDER_ECLIPSE");
		if ("true".equalsIgnoreCase(runningUnderEclipse))
		{
			return true;
		}
		if ("false".equalsIgnoreCase(runningUnderEclipse))
		{
			return false;
		}
		if (System.getProperty("eclipse.startTime") != null)
		{
			return true;
		}
		return false;
	}
}
