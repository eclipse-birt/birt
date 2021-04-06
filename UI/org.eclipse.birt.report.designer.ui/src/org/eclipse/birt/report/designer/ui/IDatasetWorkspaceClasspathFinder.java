/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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