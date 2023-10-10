/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.birt.report.viewer.utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.server.Server;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * https://kishanthan.wordpress.com/2014/03/23/osgi-and-jetty-integration/
 */

public class ViewerWebServer {

	/**
	 * the web server id used to register web application.
	 */
	public static final String VIEWER_WEB_SERVER_ID = "org.eclipse.birt.report.viewer.server"; //$NON-NLS-1$

	private ServiceRegistration<Server> serviceRegister;
	private String host;
	private int port;

	public ViewerWebServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		Server server = new Server();
		// server configuration goes here
		Dictionary<String, Object> serverProps = new Hashtable<>();
		// TODO
		// serverProps.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME,
		// VIEWER_WEB_SERVER_ID);
		serverProps.put("managedServerName", VIEWER_WEB_SERVER_ID);
		// serverProps.put(OSGiServerConstants.JETTY_HOST, host);
		serverProps.put("jetty.http.host", host);
		// serverProps.put(OSGiServerConstants.JETTY_PORT, port);
		serverProps.put("jetty.http.port", port);
		// serverProps.put(OSGiServerConstants.MANAGED_JETTY_XML_CONFIG_URLS,
		// getJettyConfigURLs());
		serverProps.put("jetty.etc.config.urls", getJettyConfigURLs());

		// register as an OSGi Service for Jetty to find
		BundleContext context = ViewerPlugin.getDefault().getBundleContext();
		serviceRegister = context.registerService(Server.class, server, serverProps);
	}

	public void stop() {
		if (serviceRegister != null) {
			serviceRegister.unregister();
			serviceRegister = null;
		}
	}

	/**
	 * return the configuration files URLS for jetty.
	 *
	 * @return
	 */
	private String getJettyConfigURLs() {
		String[] configFiles = { "/jettyhome/etc/jetty.xml", //$NON-NLS-1$
				"/jettyhome/etc/jetty-selector.xml", //$NON-NLS-1$
				"/jettyhome/etc/jetty-deployer.xml", //$NON-NLS-1$
				"/jettyhome/etc/jetty-special.xml" //$NON-NLS-1$
		};

		Bundle bundle = ViewerPlugin.getDefault().getBundle();
		StringBuilder sb = new StringBuilder();
		for (String configFile : configFiles) {
			String strURL = null;
			try {
				URL url = bundle.getEntry(configFile);
				if (url != null) {
					// Avoid invalid characters like white space in URI
					strURL = FileLocator.toFileURL(url).toExternalForm();
					strURL = strURL.replace(" ", "%20"); //$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (IOException ex) {

			}
			if (strURL != null) {
				sb.append(strURL);
				sb.append(","); //$NON-NLS-1$
			}

		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
}
