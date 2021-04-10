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

import java.util.Vector;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Singleton class for web application.
 * <p>
 */
public class WebappAccessor {

	/**
	 * indicate whether startup application
	 */
	private static Vector<String> applicationsStarted = new Vector<String>();

	/**
	 * Startup web application on the server.
	 * <p>
	 * It is assumed that webapp names are unique. It is suggested to create unique
	 * web app names.
	 * </p>
	 * 
	 * @param webappName
	 * @throws CoreException
	 * 
	 * @deprecated use {@link #start(String, String)}
	 */
	public synchronized static void start(String webappName) throws CoreException {
		start(webappName, ViewerPlugin.PLUGIN_ID);
	}

	/**
	 * Startup web application on the server.
	 * <p>
	 * It is assumed that webapp names are unique. It is suggested to create unique
	 * web app names.
	 * </p>
	 * 
	 * @param webappName
	 * @throws CoreException
	 */
	public synchronized static void start(String webappName, String pluginID) throws CoreException {
		if (applicationsStarted.contains(webappName)) {
			return;
		}

		try {
			AppServerWrapper.getInstance().start(webappName, pluginID);
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR, ViewerPlugin.PLUGIN_ID, IStatus.OK,
					ViewerPlugin.getFormattedResourceString("viewer.appserver.errorstart", //$NON-NLS-1$
							new Object[] {}),
					e));
		}

		applicationsStarted.add(webappName);
	}

	/**
	 * Runs a webapp on the server. The webapp is defined in a plugin and the path
	 * is relative to the plugin directory.
	 * <p>
	 * It is assumed that webapp names are unique. It is suggested to create unique
	 * web app names by prefixing them with the plugin id.
	 * </p>
	 * 
	 * @param webappName the name of the web app (also knowns as application
	 *                   context)
	 * @param pluginId   plugin that defines the webapp
	 * @param path       webapp relative path to the plugin directory
	 * @deprecated
	 * @exception CoreException
	 */
	public synchronized static void start(String webappName, String pluginId, IPath path) throws CoreException {
		start(webappName, pluginId);
	}

	/**
	 * Stops the specified web application.
	 * 
	 * @param webappName web application name
	 * @exception CoreException
	 */
	public synchronized static void stop(String webappName) throws CoreException {
		if (!applicationsStarted.contains(webappName)) {
			return;
		}

		try {
			AppServerWrapper.getInstance().stop(webappName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		applicationsStarted.remove(webappName);
	}

	public synchronized static void stopAll() throws CoreException {
		for (int i = 0; i < applicationsStarted.size(); i++) {
			try {
				AppServerWrapper.getInstance().stop(applicationsStarted.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		applicationsStarted.clear();
	}

	/**
	 * Returns the port number the app server listens on.
	 * 
	 * @return integer port number, 0 if server not started
	 */
	public static int getPort(String webappName) {
		return AppServerWrapper.getInstance().getPort(webappName);
	}

	/**
	 * Returns the host name or ip the app server runs on.
	 * 
	 * @return String representation of host name of IP, null if server not started
	 *         yet
	 */
	public static String getHost() {
		return AppServerWrapper.getInstance().getHost();
	}
}