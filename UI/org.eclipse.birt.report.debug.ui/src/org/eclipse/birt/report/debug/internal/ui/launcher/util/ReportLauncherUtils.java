/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.debug.internal.ui.launcher.util;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.birt.report.debug.internal.ui.launcher.IReportLauncherSettings;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.ibm.icu.util.StringTokenizer;

/**
 * ReportLauncherUtils
 *
 * @deprecated
 */
public class ReportLauncherUtils {


	/**
	 * Parses the IReportLauncherSettings.IMPORTPROJECT attribute on a
	 * ILaunchConfiguration
	 *
	 * @param config The ILaunchConfiguration
	 * @return a Set of string ids corresponding to the deselected IProject:s in the
	 *         ILaunchConfiguration
	 * @throws CoreException
	 */
	public static Set<String> parseDeselectedWSIds(ILaunchConfiguration config) throws CoreException {
		TreeSet<String> deselected = new TreeSet<>();
		String ids = config.getAttribute(IReportLauncherSettings.IMPORTPROJECT, (String) null);
		if (ids != null && ids.length() > 0) {
			StringTokenizer token = new StringTokenizer(ids, ";"); //$NON-NLS-1$
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				int index = str.lastIndexOf(File.separator);
				if (index > 0) {
					str = str.substring(index + 1);
				}
				deselected.add(str);
			}
		}
		return deselected;
	}

	/**
	 * Parses the IReportLauncherSettings.OPENFILENAMES attribute on a
	 * ILaunchConfiguration
	 *
	 * @param config The ILaunchConfiguration
	 * @return a Set of string ids corresponding to the deselected IResource:a in
	 *         the ILaunchConfiguration
	 * @throws CoreException
	 */
	public static Set<String> parseDeselectedOpenFileNames(ILaunchConfiguration config) throws CoreException {
		TreeSet<String> deselected = new TreeSet<>();
		String ids = config.getAttribute(IReportLauncherSettings.OPENFILENAMES, (String) null);
		if (ids != null && ids.length() > 0) {
			StringTokenizer token = new StringTokenizer(ids, ";"); //$NON-NLS-1$
			while (token.hasMoreTokens()) {
				String str = token.nextToken();
				int index = str.lastIndexOf(File.separator);
				if (index > 0) {
					str = str.substring(index + 1);
				}
				deselected.add(str);
			}
		}
		return deselected;
	}
}
