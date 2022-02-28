/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.report.viewer.utilities;

/**
 * A class used to get a workspace classpath
 *
 * @deprecated
 */
@Deprecated
public class WorkspaceClasspathManager {
	// implementation instance
	private static IWorkspaceClasspathFinder finder;

	/**
	 * Returns the workspace class path
	 *
	 * @return
	 */
	public static String getClassPath() {
		if (finder == null) {
			return null;
		}

		return finder.getClassPath();
	}

	/**
	 * Register ClassPathFinder instance
	 *
	 * @param pathfinder
	 */
	public static void registerClassPathFinder(IWorkspaceClasspathFinder pathfinder) {
		finder = pathfinder;
	}
}
