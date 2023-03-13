/*******************************************************************************
 * Copyright (c)2008, 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;

/**
 * The application class loader.
 *
 * The class loader first try to the load the class as following sequence:
 * <li>1. standard java class loader,
 * <li>2. classloader setted through the appContext.
 * <li>3. CLASSPATH setted by WEBAPP_CLASSPATH_KEY
 * <li>4. PROJECT_CLASSPATH_KEY
 * <li>5. WORKSAPCE_CLASSPATH_KEY
 * <li>6. JARs define in the report design
 */
public class ApplicationClassLoader extends ClassLoader {

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(ApplicationClassLoader.class.getName());

	private URLClassLoader designClassLoader = null;
	private final IReportRunnable runnable;
	private Map<String, Object> appContext = null;

	private final ReportEngine engine;

	public ApplicationClassLoader(ReportEngine engine, IReportRunnable reportRunnable, Map<String, Object> appContext) {
		this.runnable = reportRunnable;
		this.engine = engine;
		this.appContext = appContext;
	}

	public void close() {
		if (this.appContext != null) {
			this.appContext.clear();
			this.appContext = null;
		}
		if (designClassLoader != null) {
			designClassLoader.close();
			designClassLoader = null;
		}
	}

	public URLClassLoader getDesignClassLoader() {
		if (designClassLoader == null) {
			createDesignClassLoader();
		}
		return designClassLoader;
	}

	@Override
	public Class loadClass(String className) throws ClassNotFoundException {
		if (designClassLoader == null) {
			createDesignClassLoader();
		}
		return designClassLoader.loadClass(className);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (designClassLoader == null) {
			createDesignClassLoader();
		}

		Class clazz = designClassLoader.loadClass(name);
		{
			if (resolve && clazz != null) {
				resolveClass(clazz);
			}

		}
		return clazz;
	}

	@Override
	public URL getResource(String name) {
		if (designClassLoader == null) {
			createDesignClassLoader();
		}
		return designClassLoader.getResource(name);
	}

	/**
	 * create the class loader used by the design.
	 *
	 * the method should be synchronized as the class loader of a document may be
	 * used by multiple tasks.
	 */
	protected synchronized void createDesignClassLoader() {
		if (designClassLoader != null) {
			return;
		}
		ArrayList<URL> urls = new ArrayList<>();
		if (runnable != null) {
			ModuleHandle module = (ModuleHandle) runnable.getDesignHandle();
			Iterator iter = module.scriptLibsIterator();
			while (iter.hasNext()) {
				ScriptLibHandle lib = (ScriptLibHandle) iter.next();
				String libPath = lib.getName();
				if (libPath == null) {
					continue;
				}
				URL url = module.findResource(libPath, IResourceLocator.LIBRARY, appContext);
				if (url != null) {
					urls.add(url);
				} else {
					logger.log(Level.SEVERE, "Can not find specified jar: " + libPath); //$NON-NLS-1$
				}
			}
		}
		final URL[] jarUrls = urls.toArray(new URL[] {});
		if (engine != null) {
			designClassLoader = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

				@Override
				public URLClassLoader run() {
					return new URLClassLoader(jarUrls, engine.getEngineClassLoader());
				}
			});
		} else {
			designClassLoader = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

				@Override
				public URLClassLoader run() {
					return new URLClassLoader(jarUrls);
				}
			});
		}
	}
}
