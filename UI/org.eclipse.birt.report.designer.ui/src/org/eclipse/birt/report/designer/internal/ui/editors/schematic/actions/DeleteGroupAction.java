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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.GroupHandle;

/**
 *
 */

public class DeleteGroupAction extends DynamicItemAction {

	private static final String STACK_MSG_DELETE_GROUP = Messages.getString("DeleteGroupAction.stackMsg.deleteGroup"); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction"; //$NON-NLS-1$

	private GroupHandle handle;

	private ReportElementEditPart editPart;

	/**
	 * @param part
	 */
	public DeleteGroupAction(ReportElementEditPart editPart, GroupHandle handle) {
		this.handle = handle;
		this.editPart = editPart;
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
		return handle.canDrop();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Delete group action >> Run ..."); //$NON-NLS-1$
		}

		CommandUtils.setVariable(ICommandParameterNameContants.DELETE_GROUP_HANDLE, handle);
		CommandUtils.setVariable(ICommandParameterNameContants.DELETE_GROUP_EDIT_PART, editPart);

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.deleteGroupCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

}
