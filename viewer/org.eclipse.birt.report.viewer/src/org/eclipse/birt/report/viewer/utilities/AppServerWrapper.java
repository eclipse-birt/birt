/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.help.internal.appserver.AppserverPlugin;
import org.eclipse.help.internal.appserver.IWebappServer;
import org.osgi.framework.BundleContext;

/**
 * Wrapper class for embedded web application server.
 * <p>
 */
public class AppServerWrapper
{
	/**
	 * key word for host address
	 */
	public final static String HOST_KEY = "host"; //$NON-NLS-1$

	/**
	 * Key word for port #
	 */
	public final static String PORT_KEY = "port"; //$NON-NLS-1$

	private final static String APP_SERVER_EXTENSION_ID = AppserverPlugin.PLUGIN_ID + ".server"; //$NON-NLS-1$

	private static final String APP_SERVER_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	private static final String APP_SERVER_DEFAULT_ATTRIBUTE = "default"; //$NON-NLS-1$

	/**
	 * Singleton instance of app server wrapper 
	 */
	private static AppServerWrapper wrapper = null;

	/**
	 * App server instance
	 */
	private IWebappServer appServer;

	private static BundleContext bundleContext;

	/**
	 * Host address
	 */
	private String hostAddress;

	/**
	 * Port #
	 */
	private int port;

	/**
	 * Get wrapper instance.
	 * 
	 * @return wrapper instance
	 */
	public synchronized static AppServerWrapper getInstance( )
	{
		if ( wrapper == null )
		{
			wrapper = new AppServerWrapper( );
		}

		return wrapper;
	}

	/**
	 * Returns the instance of WebappServer.
	 * 
	 * @return app server instance
	 * @exception CoreException
	 */
	public synchronized IWebappServer getAppServer( ) throws CoreException
	{
		if ( appServer == null )
		{
			createWebappServer( );

			startWebappServer( );
		}

		return appServer;
	}

	/**
	 * Initializes the app server by getting an instance via app-server the
	 * extension point get the app server extension from the system plugin
	 * registry
	 * 
	 * @throws CoreException
	 */
	private void createWebappServer( ) throws CoreException
	{
		IExtensionPoint point = Platform.getExtensionRegistry( )
				.getExtensionPoint( APP_SERVER_EXTENSION_ID );

		if ( point != null )
		{
			IExtension[] extensions = point.getExtensions( );

			if ( extensions.length != 0 )
			{
				// We need to pick up the non-default configuration
				IConfigurationElement[] elements = extensions[0].getConfigurationElements( );

				if ( elements.length == 0 )
				{
					return;
				}

				IConfigurationElement serverElement = null;

				for ( int i = 0; i < elements.length; i++ )
				{
					String defaultValue = elements[i].getAttribute( APP_SERVER_DEFAULT_ATTRIBUTE );

					if ( defaultValue == null || defaultValue.equals( "false" ) ) //$NON-NLS-1$
					{
						serverElement = elements[i];

						break;
					}
				}

				// if all the servers are default, then pick the first one
				if ( serverElement == null )
				{
					serverElement = elements[0];
				}

				// Instantiate the app server
				try
				{
					appServer = (IWebappServer) serverElement.createExecutableExtension( APP_SERVER_CLASS_ATTRIBUTE );
				}
				catch ( CoreException e )
				{
					ViewerPlugin.getDefault( ).getLog( ).log( e.getStatus( ) );

					throw e;
				}
			}
		}
	}

	private void startWebappServer( ) throws CoreException
	{
		// Initialize host and port from preferences
		hostAddress = ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( HOST_KEY );

		if ( "".equals( hostAddress ) )
		{
			hostAddress = null;
		}

		port = ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.getInt( PORT_KEY );

		// apply host and port overrides passed as command line arguments
		try
		{
			String hostCommandLineOverride = System.getProperty( "server_host" ); //$NON-NLS-1$

			if ( hostCommandLineOverride != null
					&& hostCommandLineOverride.trim( ).length( ) > 0 )
			{
				hostAddress = hostCommandLineOverride;
			}

		}
		catch ( Exception e )
		{
			;
		}

		try
		{
			String portCommandLineOverride = System.getProperty( "server_port" ); //$NON-NLS-1$

			if ( portCommandLineOverride != null
					&& portCommandLineOverride.trim( ).length( ) > 0 )
			{
				port = Integer.parseInt( portCommandLineOverride );
			}

		}
		catch ( Exception e )
		{
			;
		}

		if ( appServer == null )
		{
			throw new CoreException( new Status( IStatus.ERROR,
					AppserverPlugin.PLUGIN_ID,
					IStatus.OK,
					ViewerPlugin.getResourceString( "viewer.appserver.errorstart" ), //$NON-NLS-1$
					null ) );
		}

		appServer.start( port, hostAddress );
	}
}