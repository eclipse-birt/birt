/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.data.ui.providers;

import org.eclipse.birt.report.designer.data.ui.util.WizardUtil;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetsNodeProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the data sets node
 *
 */
public class EditableDataSetsNodeProvider extends DataSetsNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the given menu.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		WizardUtil.createNewDataSetMenu(menu);
		super.createContextMenu(sourceViewer, object, menu);

	}

}
