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

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.osgi.boot.OSGiServerConstants;
import org.eclipse.jetty.osgi.boot.OSGiWebappConstants;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */

public class ViewerWebApp {

	private ServiceRegistration<ContextHandler> serviceRegister;
	private Bundle bundle;
	private String webAppPath;
	private String contextPath;
	private String encoding;

	ViewerWebApp(Bundle bundle, String webAppPath, String contextPath, String encoding) {
		this.bundle = bundle;
		this.webAppPath = webAppPath;
		this.contextPath = contextPath;
		this.encoding = encoding;
	}

	public void start() throws IOException {
		WebAppContext webapp = new WebAppContext();
		WebXmlConfiguration servletsConfiguration = new WebXmlConfiguration();

		webapp.addConfiguration(servletsConfiguration);

		webapp.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());

		Dictionary<String, Object> props = new Hashtable<>();
		props.put(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH, contextPath); // Web-ContextPath: /viewer
		props.put(OSGiWebappConstants.JETTY_WAR_RESOURCE_PATH, getWebAppPath(bundle, webAppPath)); // Jetty-WarResourcePath:
		props.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, ViewerWebServer.VIEWER_WEB_SERVER_ID);
		props.put("Jetty-WebXmlFilePath", "birt/WEB-INF/web-viewer.xml"); //$NON-NLS-1$ //$NON-NLS-2$

		URL url = bundle.getEntry(webAppPath);
		if (url != null) {
			URL fileUrl = FileLocator.toFileURL(url);
			webapp.setResourceBase(fileUrl.toString());
		}

		if (encoding != null) {
			// Jetty need those property to change the request encoding
			// the setting may changed with different jetty version
			System.setProperty("org.eclipse.jetty.util.UrlEncoding.charset", encoding); //$NON-NLS-1$
			System.setProperty("org.eclipse.jetty.util.URI.charset", encoding); //$NON-NLS-1$
		}
		bundle.getBundleContext().registerService(WebAppContext.class, webapp, props);
	}

	public void stop() {
		if (serviceRegister != null) {
			serviceRegister.unregister();
			serviceRegister = null;
		}
	}

	private String getWebAppPath(Bundle bundle, String path) throws IOException {
		URL url = bundle.getEntry(path);
		if (url != null) {
			URL fileUrl = FileLocator.toFileURL(url);
			return fileUrl.getFile();
		}
		return path;
	}
}
