/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
