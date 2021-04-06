/*******************************************************************************
 * Copyright (c) 2008, 2014 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
