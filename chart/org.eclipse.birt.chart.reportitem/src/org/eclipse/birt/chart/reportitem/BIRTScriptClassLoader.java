/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.script.ScriptClassLoaderAdapter;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;

/**
 * A BIRT implementation for IScriptClassLoader
 */
public class BIRTScriptClassLoader extends ScriptClassLoaderAdapter {

	private static final class DoubleParentClassLoader extends ClassLoader {
		private final ClassLoader cldParent2;

		private DoubleParentClassLoader(final ClassLoader parent1, final ClassLoader parent2) {
			super(parent1);
			cldParent2 = parent2;
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			return SecurityUtil.loadClass(cldParent2, name);
		}

	}

	private ClassLoader classLoader;

	/**
	 * Constructor
	 *
	 * @param classLoader
	 */
	public BIRTScriptClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IScriptClassLoader#loadClass(java.lang.String,
	 * java.lang.ClassLoader)
	 */
	@Override
	public Class<?> loadClass(String className, ClassLoader parentLoader) throws ClassNotFoundException {
		if (className == null) {
			return null;
		}

		Class<?> c = null;
		ClassNotFoundException ex = null;

		// Use app classLoader to load class first
		if (this.classLoader != null) {
			try {
				c = SecurityUtil.loadClass(this.classLoader, className);

				if (c != null) {
					return c;
				}
			} catch (Throwable e) {
				// app loader failed, need try dev loader
			}
		}

		try {
			// try context loader first
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			ex = e;
			// Try using web application's webapplication.projectclasspath
			// to load it.
			// This would be the case where the application is deployed on
			// web server.
			c = getClassUsingCustomClassPath(className, EngineConstants.WEBAPP_CLASSPATH_KEY, parentLoader);
			if (c == null) {
				// Try using the user.projectclasspath property to load it
				// using the classpath specified. This would be the case
				// when debugging is used
				c = getClassUsingCustomClassPath(className, EngineConstants.PROJECT_CLASSPATH_KEY, parentLoader);
				if (c == null) {
					// The class is not on the current classpath.
					// Try using the workspace.projectclasspath property
					c = getClassUsingCustomClassPath(className, EngineConstants.WORKSPACE_CLASSPATH_KEY, parentLoader);
				}
			}
		}

		if (c == null) {
			// Didn't find the class using any method, so throw the
			// exception
			throw ex;
		}

		return c;
	}

	private static Class<?> getClassUsingCustomClassPath(final String className,
			final String classPathKey,
			final ClassLoader parentLoader) {
		Class<?> c = null;
				String classPath = System.getProperty(classPathKey);
				if (classPath == null || classPath.length() == 0 || className == null) {
					return null;
				}
				String[] classPathArray = classPath.split(EngineConstants.PROPERTYSEPARATOR, -1);
				URL[] urls = null;
				if (classPathArray.length != 0) {
					List<URL> l = new ArrayList<>();
					for (int i = 0; i < classPathArray.length; i++) {
						String cpValue = classPathArray[i];
						File file = new File(cpValue);
						try {
							l.add(file.toURI().toURL());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
					urls = l.toArray(new URL[l.size()]);
				}

				if (urls != null) {
					DoubleParentClassLoader cmLoader = new DoubleParentClassLoader(parentLoader,
							IReportEngine.class.getClassLoader());
					ClassLoader cl = new URLClassLoader(urls, cmLoader);
					try {
						c = cl.loadClass(className);
						// Note: If the class can
						// not even be loadded by this
						// loader either, null will be returned
					} catch (ClassNotFoundException e) {
						// Ignore
					}
				}
				return c;
	}

}
