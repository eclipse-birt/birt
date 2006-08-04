/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.help.internal.appserver.IWebappServer;
import org.eclipse.help.internal.appserver.PluginClassLoaderWrapper;
import org.osgi.framework.Bundle;

/**
 * Singleton class for web application.
 * <p>
 */
public class WebappAccessor
{
	private static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath";

	private static boolean applicationsStarted = false;

	/**
	 * Private constructor, so no instances can be created.
	 * 
	 * @see java.lang.Object#Object()
	 */
	private WebappAccessor( )
	{
	}

	/**
	 * Runs a webapp on the server. The webapp is defined in a plugin and the
	 * path is relative to the plugin directory.
	 * <p>
	 * It is assumed that webapp names are unique. It is suggested to create
	 * unique web app names by prefixing them with the plugin id.
	 * </p>
	 * 
	 * @param webappName
	 *            the name of the web app (also knowns as application context)
	 * @param pluginId
	 *            plugin that defines the webapp
	 * @param path
	 *            webapp relative path to the plugin directory
	 * @exception CoreException
	 */
	public synchronized static void start( String webappName, String pluginId,
			IPath path ) throws CoreException
	{
		// Set the classpath property (used in Java scripting)
		String projectClassPaths = WorkspaceClasspathManager.getClassPath( );

		// HashTable doesn't accept null value
		if ( projectClassPaths == null )
		{
			projectClassPaths = ""; //$NON-NLS-1$
		}
		System.setProperty( WORKSPACE_CLASSPATH_KEY, projectClassPaths );
		if ( !applicationsStarted )
		{

			IPath webappPath = getWebappPath( pluginId, path );

			// we get the server before constructing the class loader, so
			// class loader exposed by the server is available to the webapps.
			IWebappServer server = AppServerWrapper.getInstance( )
					.getAppServer( );
			PluginClassLoaderWrapper loader = new PluginClassLoaderWrapper(pluginId);
			server.start( webappName, webappPath, loader );
			applicationsStarted = true;
		}
	}

	/**
	 * Stops the specified web application.
	 * 
	 * @param webappName
	 *            web application name
	 * @exception CoreException
	 */
	public static void stop( String webappName ) throws CoreException
	{
		if ( !applicationsStarted )
		{
			// do not obtain (start) appserver when no reason
			return;
		}

		AppServerWrapper.getInstance( ).getAppServer( ).stop( webappName );
	}

	/**
	 * Returns the port number the app server listens on.
	 * 
	 * @return integer port number, 0 if server not started
	 */
	public static int getPort( )
	{
		try
		{
			return AppServerWrapper.getInstance( ).getAppServer( ).getPort( );
		} catch ( CoreException e )
		{
			return 0;
		}
	}

	/**
	 * Returns the host name or ip the app server runs on.
	 * 
	 * @return String representaion of host name of IP, null if server not
	 *         started yet
	 */
	public static String getHost( )
	{
		try
		{
			return AppServerWrapper.getInstance( ).getAppServer( ).getHost( );
		} catch ( CoreException e )
		{
			return null;
		}
	}

	/**
	 * Get path of web applcation.
	 * 
	 * @param pluginId
	 *            Embedded web application id
	 * @param path
	 *            webapp path relative to the plugin directory
	 * @return String absolute webapp path
	 * @exception CoreException
	 */
	private static IPath getWebappPath( String pluginId, IPath path )
			throws CoreException
	{
		Bundle bundle = Platform.getBundle( pluginId );

		if ( bundle == null )
		{
			throw new CoreException( new Status( IStatus.ERROR,
					ViewerPlugin.PLUGIN_ID, IStatus.OK, ViewerPlugin
							.getFormattedResourceString(
									"viewer.appserver.cannotfindplugin", //$NON-NLS-1$
									new Object[] { pluginId } ), null ) );
		}

		// Note: we just look for one webapp directory.
		// If needed, may want to use the locale specific path.
		URL webappURL = Platform.find( bundle, path );

		if ( webappURL == null )
		{
			throw new CoreException( new Status( IStatus.ERROR,
					ViewerPlugin.PLUGIN_ID, IStatus.OK,
					ViewerPlugin.getFormattedResourceString(
							"viewer.appserver.cannotfindpath", //$NON-NLS-1$
							new Object[] { pluginId, path.toOSString( ) } ),
					null ) );
		}

		try
		{
			String webappLocation = Platform.asLocalURL(
					Platform.resolve( webappURL ) ).getFile( );
			webappLocation += "birt/"; //$NON-NLS-1$
			return new Path( webappLocation );
		} catch ( IOException ioe )
		{
			throw new CoreException( new Status( IStatus.ERROR,
					ViewerPlugin.PLUGIN_ID, IStatus.OK,
					ViewerPlugin.getFormattedResourceString(
							"viewer.appserver.cannotresolvepath", //$NON-NLS-1$
							new Object[] { pluginId, path.toOSString( ) } ),
					ioe ) );
		}
	}

}