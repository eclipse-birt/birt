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

import org.eclipse.birt.core.framework.eclipse.EclipsePlatform;
import org.eclipse.birt.core.framework.server.ServerPlatform;


/**
 * Defines a generic Platform class that wraps around an <code>EclipsePlatform</code> 
 * or <code>ServerPlatform</code> class. 
 * 
 * This class is a singleton. 
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/08 02:48:35 $
 */
public class Platform
{
    public static int UNKNOWN_PLATFORM = 0;    
    public static int ECLIPSE_PLATFORM = 1;
    public static int SERVER_PLATFORM = 2;
    
	protected static int platformType = UNKNOWN_PLATFORM;
	protected static IPlatform platform = null;
	
	/**
	 * creates the appropriate platform object based on the platform type 
	 */
	synchronized static protected void initialize()
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
				platform = new ServerPlatform();
				platformType = SERVER_PLATFORM;
			}
		}
	}
	
	/**
	 * @return an extension registry
	 */
	public static IExtensionRegistry getExtensionRegistry()
	{
	    if (platform == null)
	    {
	        initialize();
	        assert platform != null;
	    }
	    
		return platform.getExtensionRegistry();
	}
	
	public static IBundle getBundle (String symbolicName)
	{
		if (platform == null)
		{
			initialize();
			assert platform != null;
		}
		return platform.getBundle(symbolicName);
	}
	
	/**
	 * @return the type of the platform. Available values are ECLIPSE_PLATFORM and
	 * SERVER_PLATFORM.
	 */
	public static int getPlatformType()
	{
	    if (platform == null)
	    {
	        initialize();
	        assert platform != null;
	    }
	    
		return platformType;
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
