/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

import org.eclipse.birt.report.engine.api.EngineConstants;

/**
 * Interface used to find classpath based on projects or workspace
 * 
 * @deprecated
 */
public interface IWorkspaceClasspathFinder {
	// separator
	public static final String PROPERTYSEPARATOR = EngineConstants.PROPERTYSEPARATOR;

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
