/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.report.viewer.utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.osgi.boot.OSGiServerConstants;
import org.eclipse.jetty.osgi.boot.OSGiWebappConstants;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
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
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(OSGiWebappConstants.RFC66_WEB_CONTEXTPATH, contextPath);
		props.put(OSGiWebappConstants.JETTY_WAR_RESOURCE_PATH, getWebAppPath(bundle, webAppPath));
		props.put(OSGiServerConstants.MANAGED_JETTY_SERVER_NAME, ViewerWebServer.VIEWER_WEB_SERVER_ID);
		if (encoding != null) {
			// Jetty need those property to change the request encoding
			// the setting may changed with different jetty version
			System.setProperty("org.eclipse.jetty.util.UrlEncoding.charset", encoding);
			System.setProperty("org.eclipse.jetty.util.URI.charset", encoding);
		}
		bundle.getBundleContext().registerService(ContextHandler.class, webapp, props);
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
