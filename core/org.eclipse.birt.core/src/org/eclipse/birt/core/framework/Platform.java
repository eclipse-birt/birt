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
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.osgi.OSGILauncher;
import org.osgi.framework.BundleContext;

/**
 * Defines a generic Platform class that wraps around an
 * <code>EclipsePlatform</code> or <code>ServerPlatform</code> class.
 * 
 * This class is a singleton.
 * 
 */
public class Platform
{
	/**
	 * @deprecated since BIRT 2.1
	 */
	public static final String PROPERTY_RUN_UNDER_ECLIPSE = "RUN_UNDER_ECLIPSE";
	public static final String PROPERTY_BIRT_HOME = "BIRT_HOME";

	public static int UNKNOWN_PLATFORM = 0;
	public static int ECLIPSE_PLATFORM = 1;
	/**
	 * @deprecated since BIRT 2.1
	 */
	public static final int SERVER_PLATFORM = 2;

	protected static int platformType = UNKNOWN_PLATFORM;
	protected static IPlatform platform = null;

	protected static Logger log = Logger.getLogger( Platform.class.getName( ) );

	protected static OSGILauncher launcher;

	/**
	 * startup the platform. 
	 * The PlatformContext is get from the config.
	 * @param config PlatformConfig
	 */
	synchronized static public void startup( PlatformConfig config )
			throws BirtException
	{
		if( platform == null )
		{
			if (config == null)
			{
				config = new PlatformConfig();
			}
			IPlatformContext context = config.getPlatformContext( );
			
			if( context == null )
			{
				context = new PlatformFileContext( config );
				config.setProperty( PlatformConfig.PLATFORM_CONTEXT, context );
			}
			// start up the OSGI framework
			try
			{
				launcher = new OSGILauncher( );
				// startup the OSGi framework,
				launcher.startup( config );
				// the core plugins is loaded in the start up
				// 1. platform should not be null any more.
				// 2. the IFactoryService has been registed
				// see
				// org.eclipse.birt.core.plugin.CorePlugin#start(BundleContext
				// context).
				assert platform != null;
			}
			catch ( Exception ex )
			{
				platform = null;
				throw new BirtException( "Can't startup the OSGI framework",
						new Object[]{}, ex );
			}
		}
	}
	
	public synchronized static void shutdown( )
	{
		if ( launcher != null )
		{
			launcher.shutdown( );
			launcher = null;
		}
		platform = null;
	}

	/**
	 * 
	 * @param context
	 * @see org.eclipse.birt.core.Platform.startup(IPlatformContext context)
	 * @deprecated since BIRT 2.1
	 */
	synchronized static public void initialize( PlatformConfig config )
	{
		try
		{
			startup( config );
		}
		catch ( BirtException ex )
		{
			ex.printStackTrace( );
		}
	}

	/**
	 * this class can only be called by
	 * org.eclipse.birt.core.plugin.CorePlugin#start(BundleContext)
	 * 
	 * @see org.eclipes.birt.core.plugin.CorePlugin#start(BundleContext)
	 * @param platform
	 */
	public static void setPlatform( IPlatform platform )
	{
		Platform.platform = platform;
	}

	/**
	 * @return an extension registry
	 * @see org.eclipse.core.runtime.IExtensionRegistry
	 */
	public static IExtensionRegistry getExtensionRegistry( )
	{
		if ( platform != null )
		{
			return platform.getExtensionRegistry( );
		}
		return null;
	}

	/**
	 * 
	 * @param symbolicName
	 * @return
	 * @deprecated since BIRT 2.1
	 */
	public static IBundle getBundle( String symbolicName )
	{
		if ( platform != null )
		{
			return platform.getBundle( symbolicName );
		}
		return null;
	}

	/**
	 * 
	 * @param bundle
	 * @param path
	 * @return
	 * @deprecated since BIRT 2.1
	 */
	public static URL find( IBundle bundle, IPlatformPath path )
	{
		if ( platform != null )
		{
			return platform.find( bundle, path );
		}
		return null;
	}

	/**
	 * @return the type of the platform. Available values are ECLIPSE_PLATFORM
	 *         and SERVER_PLATFORM.
	 * @deprecated since BIRT 2.1
	 */
	public static int getPlatformType( )
	{
		return platformType;
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @deprecated since BIRT 2.1
	 */
	public static URL asLocalURL( URL url ) throws IOException
	{
		if ( platform != null )
		{
			return platform.asLocalURL( url );
		}
		return null;
	}

	/**
	 * Checks whether Eclipse is running
	 * 
	 * @return whether we are running in Eclipse
	 * @deprecated since BIRT 2.1
	 */
	public static boolean runningEclipse( )
	{
		if ( platform != null )
		{
			return true;
		}
		return false;
	}

	public static void intializeTracing( String pluginName )
	{
		if ( platform != null )
		{
			platform.initializeTracing( pluginName );
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @deprecated since BIRT 2.1
	 * @see org.eclipse.core.runtime.Platform.getDebugOption(String name)
	 */
	public static String getDebugOption( String name )
	{
		if ( platform != null )
		{
			return platform.getDebugOption( name );
		}
		return null;
	}

	/**
	 * create an object inside the OSGIframework and give it out of the
	 * framework. This object can be used in client side.
	 * 
	 * If a bundle need export some function outside of the framework, it
	 * need implmenet a extension "org.eclipse.birt.core.FactoryService".
	 * 
	 * @see org.eclipse.birt.core.IPlatform#
	 * @param extensionId
	 *            factory extension id
	 * @return the service object.
	 */
	public static Object createFactoryObject( String extensionId )
	{
		if ( platform != null )
		{
			return platform.createFactoryObject( extensionId );
		}
		return null;
	}

}
