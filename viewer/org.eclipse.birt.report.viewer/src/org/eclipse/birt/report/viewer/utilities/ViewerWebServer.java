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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.URLResourceFactory;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.osgi.framework.Bundle;

/**
 *
 */
public class ViewerWebServer {

	private static final String JETTY_HOME = "jetty.home"; //$NON-NLS-1$
	private static final String JETTY_BASE = "jetty.base"; //$NON-NLS-1$

	private static final String JETTY_HOST = "jetty.http.host"; //$NON-NLS-1$
	private static final String JETTY_PORT = "jetty.http.port"; //$NON-NLS-1$

	private static final String JETTY_FOLDER_NAME = "jetty"; //$NON-NLS-1$
	private static final String JETTY_HOME_FOLDER_NAME = "home"; //$NON-NLS-1$
	private static final String JETTY_BASE_FOLDER_NAME = "base"; //$NON-NLS-1$

	private static String[] configFiles = { "/etc/jetty.xml", //$NON-NLS-1$
			"/etc/jetty-http.xml", //$NON-NLS-1$
			"/etc/jetty-deploy.xml" //$NON-NLS-1$
	};

	/**
	 * the web server id used to register web application.
	 */
	public static final String VIEWER_WEB_SERVER_ID = "org.eclipse.birt.report.viewer.server"; //$NON-NLS-1$

	static {
		// Make sure that the bundleresource / entry: is registered to a URLResourceFactory
		URLResourceFactory urlResourceFactory = new URLResourceFactory();
		ResourceFactory.registerResourceFactory("bundleresource", urlResourceFactory);
		ResourceFactory.registerResourceFactory("bundleentry", urlResourceFactory);
	}

	private String host;
	private int port;
	private Server server;

	public ViewerWebServer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		LogUtil.logInfo("BIRT Server starting", null); //$NON-NLS-1$

		if (this.port > 0) {
			try {
				this.server = startAndConfigure(this.host, this.port);

				LogUtil.logInfo(String.format("BIRT HTTP Server listening to: %s:%s", this.host, this.port), null); //$NON-NLS-1$

			} catch (Exception e) {
				LogUtil.logError("Error while initialzing http server.", e); //$NON-NLS-1$
			}
		} else {
			LogUtil.logError(String.format(
					"Could not start BIRT server. HTTP port configuration: \"%s\"", //$NON-NLS-1$
					this.port), null);
		}
	}

	private static Server startAndConfigure(String httpListenOnAddress, int httpServerPort) throws Exception {
		Bundle bundle = ViewerPlugin.getDefault().getBundle();
		String jettyBase = JETTY_FOLDER_NAME + "/" + JETTY_HOME_FOLDER_NAME;

		List<URL> resolvedXmlPaths = new ArrayList<>();

		for (String xmlFile : configFiles) {
			URL url = bundle.getEntry(jettyBase + xmlFile);
			URL fileURL = URIUtil.toURI(FileLocator.toFileURL(url)).toURL();
			if (fileURL != null) {
				resolvedXmlPaths.add(fileURL);
			}
		}

		URL jettyHomeUrl = bundle.getEntry(JETTY_FOLDER_NAME + "/" + JETTY_HOME_FOLDER_NAME);
		URL jettyHomeFileUrl = URIUtil.toURI(FileLocator.toFileURL(jettyHomeUrl)).toURL();

		URL jettyBaseUrl = bundle.getEntry(JETTY_FOLDER_NAME + "/" + JETTY_BASE_FOLDER_NAME);
		URL jettyBaseFileUrl = URIUtil.toURI(FileLocator.toFileURL(jettyBaseUrl)).toURL();

		// Lets load our properties
		Map<String, String> properties = new HashMap<>();

		properties.put(JETTY_HOST, httpListenOnAddress);
		properties.put(JETTY_PORT, String.valueOf(httpServerPort));

		properties.put(JETTY_HOME, jettyHomeFileUrl.toString());
		properties.put(JETTY_BASE, jettyBaseFileUrl.toString());

		// Now lets tie it all together

		Map<String, Object> idMap = new HashMap<>();

		ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();

		Thread.currentThread().setContextClassLoader(ViewerWebServer.class.getClassLoader());
		// Configure everything
		try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {

			for (URL resolvedXmlPath : resolvedXmlPaths) {
				Resource xmlResource = resourceFactory.newResource(resolvedXmlPath);
				XmlConfiguration configuration = new XmlConfiguration(xmlResource);
				configuration.getIdMap().putAll(idMap);
				configuration.getProperties().putAll(properties);
				configuration.configure();
				idMap.putAll(configuration.getIdMap());
			}
		}

		Thread.currentThread().setContextClassLoader(currentContextClassLoader);
		// Fetch the configured Server
		Server server = (Server) idMap.get("Server"); //$NON-NLS-1$

		// Start the server
		server.start();

		return server;
	}

	public void stop() {
		if (this.server != null) {
			try {
				this.server.stop();
				this.server = null;
			} catch (Exception e) {
				LogUtil.logError("Could not stop BIRT server.", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Returns the Jetty Server that was started
	 *
	 * @return A Jetty server
	 */
	public Server getServer() {
		return this.server;
	}

}
