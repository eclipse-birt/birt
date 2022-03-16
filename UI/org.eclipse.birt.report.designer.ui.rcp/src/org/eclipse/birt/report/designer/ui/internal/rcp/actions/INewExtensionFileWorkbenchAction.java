/*******************************************************************************
 * Copyright (c) 2008, 2014 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.internal.rcp.actions;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * Workbench action and initialize the window
 */

public interface INewExtensionFileWorkbenchAction extends IWorkbenchAction {
	/**
	 * Initialize the window
	 *
	 * @param window
	 */
	void init(IWorkbenchWindow window);
}
