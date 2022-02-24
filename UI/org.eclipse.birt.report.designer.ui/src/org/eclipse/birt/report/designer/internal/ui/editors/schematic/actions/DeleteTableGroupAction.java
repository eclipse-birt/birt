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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Delete table group action
 */

public class DeleteTableGroupAction extends ContextSelectionAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction"; //$NON-NLS-1$

	private String ACTION_MSG_DELETE_GROUP = Messages.getString("DeleteGroupAction.actionMsg.deleteGroup"); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public DeleteTableGroupAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_DELETE_GROUP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return getRowHandles().size() == 1 && getTableGroup() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Delete table action >> Run ..."); //$NON-NLS-1$
		}
		if (getTableGroup() != null && getTableEditPart() != null) {
			TableEditPart part = getTableEditPart();
			EditPartViewer viewer = part.getViewer();
			part.removeGroup(getTableGroup());
			viewer.select(part);
		}
	}
}
