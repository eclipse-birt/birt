/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Wrapper class for embedded web application server.
 * <p>
 */
public class AppServerWrapper {

	/**
	 * key word for host address
	 */
	public final static String HOST_KEY = "host"; //$NON-NLS-1$

	/**
	 * Key word for port #
	 */
	public final static String PORT_KEY = "port"; //$NON-NLS-1$

	/**
	 * Singleton instance of app server wrapper
	 */
	private static AppServerWrapper wrapper = null;

	/**
	 * host for web application
	 */
	private String host = null;

	/**
	 * port for web application
	 */
	private Map<String, Integer> ports = new HashMap<String, Integer>();

	/**
	 * Get wrapper instance.
	 *
	 * @return wrapper instance
	 */
	public synchronized static AppServerWrapper getInstance() {
		if (wrapper == null) {
			wrapper = new AppServerWrapper();
		}

		return wrapper;
	}

	/**
	 * Configure web server.
	 */
	private void configureServer(String webappName) {
		// Initialize host and port from preferences
		host = ViewerPlugin.getDefault().getPluginPreferences().getString(HOST_KEY);
		int port = ViewerPlugin.getDefault().getPluginPreferences().getInt(PORT_KEY);

		// apply host and port overrides passed as command line arguments
		try {
			String hostCommandLineOverride = System.getProperty("server_host"); //$NON-NLS-1$
			if (hostCommandLineOverride != null && hostCommandLineOverride.trim().length() > 0)
				host = hostCommandLineOverride;
		} catch (Exception e) {
		}

		try {
			String portCommandLineOverride = System.getProperty("server_port"); //$NON-NLS-1$
			if (portCommandLineOverride != null && portCommandLineOverride.trim().length() > 0)
				port = Integer.parseInt(portCommandLineOverride);
		} catch (Exception e) {
		}

		// Set default host
		if (host == null || host.trim().length() <= 0) {
			// for all network adapters
			host = null;
		}

		// Set random port
		if (port <= 0) {
			port = SocketUtil.findUnusedLocalPort();
		}

		// TODO check dup port
		ports.put(webappName, port);

		// Set default fiscal year start date via System config in Designer.
		// Finally it will set into appContext
		System.setProperty(EngineConstants.PROPERTY_FISCAL_YEAR_START_DATE, UIUtil.getFiscalYearStart());
	}

	/**
	 * Start web appserver based on Jetty Http Service
	 *
	 * @param webappName
	 * @throws Exception
	 *
	 * @deprecated use {@link #start(String, String)}
	 */
	public void start(String webappName) throws Exception {
		start(webappName, ViewerPlugin.PLUGIN_ID);
	}

	private boolean useWebApp = true;

	/**
	 * Start web appserver based on Jetty Http Service
	 *
	 * @param webappName
	 * @throws Exception
	 */
	public void start(String webappName, String pluginID) throws Exception {
		configureServer(webappName);
		if (useWebApp) {
			startJettyServer(webappName);
		} else {
			startHttpService(webappName, pluginID);
		}
	}

	private void startHttpService(String webappName, String pluginID) throws Exception {
		// configure web server
		Dictionary dict = new Hashtable();

		// configure the port
		dict.put("http.port", ports.get(webappName)); //$NON-NLS-1$

		// configure the host
		dict.put("http.host", host == null ? "0.0.0.0" : host); //$NON-NLS-1$ //$NON-NLS-2$

		// set the base URL
		dict.put("context.path", "/" + webappName); //$NON-NLS-1$ //$NON-NLS-2$

		dict.put("other.info", pluginID); //$NON-NLS-1$

		JettyConfigurator.startServer(webappName, dict);

		ensureBundleStarted("org.eclipse.equinox.http.registry"); //$NON-NLS-1$
	}

	/**
	 * Stop http server by webapp name
	 *
	 * @param webappName
	 * @throws Exception
	 */
	public void stop(String webappName) throws Exception {
		if (useWebApp) {
			stopJettyServer(webappName);
		} else {
			stopHttpService(webappName);
		}
	}

	private void stopHttpService(String webappName) throws Exception {
		JettyConfigurator.stopServer(webappName);
	}

	/**
	 * Ensures that the bundle with the specified name and the highest available
	 * version is started.
	 *
	 * @param symbolicName
	 */
	private void ensureBundleStarted(String symbolicName) throws BundleException {
		Bundle bundle = EclipseUtil.getBundle(symbolicName);
		if (bundle != null) {
			if (bundle.getState() == Bundle.RESOLVED) {
				bundle.start(Bundle.START_TRANSIENT);
			}
		}
	}

	/**
	 * @return the host
	 */
	String getHost() {
		return host == null ? "127.0.0.1" : host; //$NON-NLS-1$
	}

	/**
	 * @return the port
	 */
	int getPort(String webappName) {
		return ports.get(webappName);
	}

	private ViewerWebServer viewerServer;
	private ViewerWebApp viewerApp;

	private void startJettyServer(String webAppName) throws IOException, BundleException {

		// must setup logging configuration before jetty loaded
//		System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.JavaUtilLog");
		System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
		System.setProperty("org.eclipse.jetty.LEVEL", "DEBUG");
		System.setProperty("org.eclipse.jetty.websocket.LEVEL", "DEBUG");

		ensureBundleStarted("org.eclipse.jetty.osgi.boot"); //$NON-NLS-1$

		viewerServer = new ViewerWebServer(getHost(), getPort(webAppName));
		viewerServer.start();

		IWebAppInfo webAppInfo = WebViewer.getCurrentWebApp();
		Bundle webAppBundle = EclipseUtil.getBundle(webAppInfo.getID());
		viewerApp = new ViewerWebApp(webAppBundle, webAppInfo.getWebAppPath(), webAppInfo.getWebAppContextPath(),
				webAppInfo.getURIEncoding());
		viewerApp.start();
	}

	private void stopJettyServer(String webAppName) {
		viewerApp.stop();
		viewerServer.stop();
	}
}