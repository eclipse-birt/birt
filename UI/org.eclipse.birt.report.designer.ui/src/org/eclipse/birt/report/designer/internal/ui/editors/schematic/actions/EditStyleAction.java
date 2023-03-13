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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;

/**
 * Edits style.
 */
public class EditStyleAction extends DynamicItemAction {

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditStyleAction"; //$NON-NLS-1$

	private SharedStyleHandle handle;

	/**
	 * @param handle
	 */
	public EditStyleAction(SharedStyleHandle handle) {
		this.handle = handle;
		setId(ID);
		if (handle.getContainerSlotHandle() != null
				&& handle.getContainerSlotHandle().getElementHandle() instanceof AbstractThemeHandle) {
			setText(((AbstractThemeHandle) handle.getContainerSlotHandle().getElementHandle()).getName() + "." //$NON-NLS-1$
					+ DEUtil.getEscapedMenuItemText(DEUtil.getDisplayLabel(handle, false)));
		} else {
			setText(DEUtil.getEscapedMenuItemText(DEUtil.getDisplayLabel(handle, false)));
		}
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	@Override
	public boolean isEnabled() {
		if (handle.canEdit()) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {

		CommandUtils.setVariable(ICommandParameterNameContants.EDIT_STYLE_SHARED_STYLE_HANDLE_NAME, handle);
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.editStyleCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}
}
