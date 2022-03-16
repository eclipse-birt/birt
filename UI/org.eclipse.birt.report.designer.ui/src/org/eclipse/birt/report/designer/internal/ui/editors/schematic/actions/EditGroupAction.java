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
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 *
 */

public class EditGroupAction extends DynamicItemAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction"; //$NON-NLS-1$

	public static final String GROUP_HANDLE_NAME = "EditGroupAction.GroupHandleName"; //$NON-NLS-1$
	private GroupHandle handle;

	/**
	 * @param part
	 */
	public EditGroupAction(IWorkbenchPart part) {
		setId(ID);
	}

	/**
	 * @param part
	 */
	public EditGroupAction(IWorkbenchPart part, GroupHandle handle) {
		this.handle = handle;
		setId(ID);
		setText(DEUtil.getEscapedMenuItemText(handle.getDisplayLabel()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return // !DEUtil.getDataSetList( handle ).isEmpty( );
		handle.canEdit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {

		CommandUtils.setVariable(GROUP_HANDLE_NAME, handle);
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.editGroupCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
