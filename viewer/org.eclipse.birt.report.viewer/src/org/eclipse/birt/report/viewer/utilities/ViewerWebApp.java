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

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.ee8.webapp.WebAppClassLoader;
import org.eclipse.jetty.ee8.webapp.WebAppContext;
import org.eclipse.jetty.ee8.webapp.WebXmlConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.osgi.framework.Bundle;

/**
 *
 */

public class ViewerWebApp {
	private Bundle bundle;
	private String webAppPath;
	private String contextPath;
	private String encoding;
	private Server server;
	private WebAppContext webAppContext;

	ViewerWebApp(Server server, Bundle bundle, String webAppPath, String contextPath, String encoding) {
		this.server = server;
		this.bundle = bundle;
		this.webAppPath = webAppPath;
		this.contextPath = contextPath;
		this.encoding = encoding;
	}

	public void start() throws Exception {
		this.webAppContext = new WebAppContext();
		this.webAppContext.setContextPath(this.contextPath);
		WebXmlConfiguration servletsConfiguration = new WebXmlConfiguration();

		this.webAppContext.addConfiguration(servletsConfiguration);

		this.webAppContext.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());

		URL webAppUrl = bundle.getEntry(webAppPath);
		URL webDescriptorUrl = bundle.getEntry(webAppPath + "/WEB-INF/web-viewer.xml");
		if (webAppUrl != null && webDescriptorUrl != null) {
			URL resolvedWebAppUrl = FileLocator.resolve(webAppUrl);
			URL resolvedWebDescriptorUrl = FileLocator.resolve(webDescriptorUrl);
			this.webAppContext.setBaseResourceAsString(resolvedWebAppUrl.toString());
			this.webAppContext.setDescriptor(resolvedWebDescriptorUrl.toString());
		}

		if (encoding != null) {
			// Jetty need those property to change the request encoding
			// the setting may changed with different jetty version
			System.setProperty("org.eclipse.jetty.util.UrlEncoding.charset", encoding); //$NON-NLS-1$
			System.setProperty("org.eclipse.jetty.util.URI.charset", encoding); //$NON-NLS-1$
		}
		Handler handler = this.server.getHandler();
		if (handler instanceof ContextHandlerCollection) {
			ContextHandlerCollection contextHandlerCollection = (ContextHandlerCollection) handler;
			contextHandlerCollection.addHandler(this.webAppContext);
		}

		this.webAppContext.setClassLoader(ViewerWebServer.class.getClassLoader());

		WebAppClassLoader.runWithServerClassAccess(() -> {
			this.webAppContext.start();
			return null;
		});

	}

	public void stop() throws Exception {
		if (this.webAppContext != null) {
			this.webAppContext.stop();

			Handler handler = this.server.getHandler();
			if (handler instanceof ContextHandlerCollection) {
				ContextHandlerCollection contextHandlerCollection = (ContextHandlerCollection) handler;
				contextHandlerCollection.removeHandler(this.webAppContext.get());
			}

			this.webAppContext = null;
		}
	}
}
