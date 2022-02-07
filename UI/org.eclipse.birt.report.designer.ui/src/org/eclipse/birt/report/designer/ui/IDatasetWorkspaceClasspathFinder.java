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

package org.eclipse.birt.report.designer.ui;

/**
 * Interface used to find classpath based on projects or workspace
 * 
 * @deprecated the function of this class is replaced by
 *             {@link IReportClasspathResolver}
 */
public interface IDatasetWorkspaceClasspathFinder {

	/**
	 * Get classpath based on the provided project names (separated by ;)
	 * 
	 */
	String getClassPath(String projects);

	/**
	 * Get classpath based on projects in workspace
	 * 
	 */
	String getClassPath();
}
