/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.internal.rcp.actions;

/**
 * Support RCP open and create a new type file.
 */

public interface IExtensionFile {
	/**
	 * Gets the new action
	 * 
	 * @return
	 */
	INewExtensionFileWorkbenchAction getNewAction();

	/**
	 * Gets the file extension
	 * 
	 * @return
	 */
	String getFileExtension();
}
