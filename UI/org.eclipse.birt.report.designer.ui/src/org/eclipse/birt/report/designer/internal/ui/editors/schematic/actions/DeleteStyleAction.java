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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;

/**
 * DeleteStyleAction
 */
public class DeleteStyleAction extends DynamicItemAction {

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteStyleAction"; //$NON-NLS-1$

	private DeleteAction action = null;

	/**
	 * @param handle
	 */
	public DeleteStyleAction(SharedStyleHandle handle) {
		setId(ID);
		if (handle.getContainerSlotHandle() != null
				&& handle.getContainerSlotHandle().getElementHandle() instanceof AbstractThemeHandle) {
			setText(((AbstractThemeHandle) handle.getContainerSlotHandle().getElementHandle()).getName() + "." //$NON-NLS-1$
					+ DEUtil.getEscapedMenuItemText(DEUtil.getDisplayLabel(handle, false)));
		} else {
			setText(DEUtil.getEscapedMenuItemText(DEUtil.getDisplayLabel(handle, false)));
		}
		action = new DeleteAction(handle);
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		return action.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit style action >> Run ..."); //$NON-NLS-1$
		}

		action.run();

	}

}
