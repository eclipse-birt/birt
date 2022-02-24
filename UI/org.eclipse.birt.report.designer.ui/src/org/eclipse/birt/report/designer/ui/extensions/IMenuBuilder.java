/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.extensions;

import java.util.List;

import org.eclipse.jface.action.IMenuManager;

/**
 * The interface used to extend context menu in the layout editeor
 */

public interface IMenuBuilder {

	/**
	 * Build extended menu for the given menu manager by the selection
	 * 
	 * @param menu         the menu manager to extend
	 * @param selectedList the selection list
	 */
	public void buildMenu(IMenuManager menu, List selectedList);
}
