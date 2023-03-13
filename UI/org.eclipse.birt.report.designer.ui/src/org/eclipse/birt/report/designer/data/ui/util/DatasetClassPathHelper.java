/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.report.designer.ui.IDatasetWorkspaceClasspathFinder;
import org.eclipse.birt.report.designer.ui.IReportClasspathResolver;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 *
 */

public class DatasetClassPathHelper {

	public static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath"; //$NON-NLS-1$
	private static final String FINDER_BUNDLE_NAME = "org.eclipse.birt.report.debug.ui"; //$NON-NLS-1$
	private static final String FINDER_CLASSNAME = "org.eclipse.birt.report.debug.internal.ui.launcher.util.WorkspaceClassPathFinder"; //$NON-NLS-1$

	static protected boolean inDevelopmentMode = false;
	static protected String[] devDefaultClasspath;
	static protected Properties devProperties = null;

	public static final String PROPERTYSEPARATOR = File.pathSeparator;

	static {
		// Check the osgi.dev property to see if dev classpath entries have been
		// defined.
		String osgiDev = System.getProperty("osgi.dev"); //$NON-NLS-1$
		if (osgiDev != null) {
			try {
				inDevelopmentMode = true;
				URL location = new URL(osgiDev);
				devProperties = load(location);
				if (devProperties != null) {
					devDefaultClasspath = getArrayFromList(devProperties.getProperty("*")); //$NON-NLS-1$
				}
			} catch (MalformedURLException e) {
				devDefaultClasspath = getArrayFromList(osgiDev);
			}
		}
	}

	/**
	 * Returns the result of converting a list of comma-separated tokens into an
	 * array
	 *
	 * @return the array of string tokens
	 * @param prop the initial comma-separated string
	 */
	private static String[] getArrayFromList(String prop) {
		if (prop == null || prop.trim().equals("")) { //$NON-NLS-1$
			return new String[0];
		}
		Vector list = new Vector();
		StringTokenizer tokens = new StringTokenizer(prop, ","); //$NON-NLS-1$
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken().trim();
			if (!token.equals("")) { //$NON-NLS-1$
				list.addElement(token);
			}
		}
		return list.isEmpty() ? new String[0] : (String[]) list.toArray(new String[list.size()]);
	}

	public static boolean inDevelopmentMode() {
		return inDevelopmentMode;
	}

	/*
	 * Load the given properties file
	 */
	private static Properties load(URL url) {
		Properties props = new Properties();
		try {
			try (InputStream is = url.openStream()) {
				props.load(is);
			}
		} catch (IOException e) {
			// TODO consider logging here
		}
		return props;
	}

	/**
	 * Gets the workspace classpath
	 *
	 * @return
	 *
	 * @deprecated use {@link #getWorkspaceClassPath(String)}
	 */
	@Deprecated
	public static String getWorkspaceClassPath() {
		try {
			Bundle bundle = Platform.getBundle(FINDER_BUNDLE_NAME);
			if (bundle != null) {
				if (bundle.getState() == Bundle.RESOLVED) {
					bundle.start(Bundle.START_TRANSIENT);
				}
			}

			if (bundle == null) {
				return null;
			}

			Class clz = bundle.loadClass(FINDER_CLASSNAME);

			// register workspace classpath finder
			IDatasetWorkspaceClasspathFinder finder = (IDatasetWorkspaceClasspathFinder) clz.newInstance();
			if (finder == null) {
				return null;
			}

			return finder.getClassPath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the classpath associated with given report file.
	 *
	 * @param reportFilePath The full path of the report file.
	 * @return
	 */
	public static List<URL> getWorkspaceClassPath(String reportFilePath) {
		ArrayList<URL> urls = new ArrayList<>();

		IReportClasspathResolver provider = ReportPlugin.getDefault().getReportClasspathResolverService();

		if (provider != null) {
			String[] classpaths = provider.resolveClasspath(reportFilePath);

			if (classpaths != null && classpaths.length != 0) {
				for (int j = 0; j < classpaths.length; j++) {
					File file = new File(classpaths[j]);
					try {
						urls.add(file.toURI().toURL());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return urls;
	}

}
