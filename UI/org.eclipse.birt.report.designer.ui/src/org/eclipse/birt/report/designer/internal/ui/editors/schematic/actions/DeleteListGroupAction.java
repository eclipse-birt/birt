/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Delete List Group Action
 */
public class DeleteListGroupAction extends ContextSelectionAction {

	private static final String ACTION_MSG_DELETE_GROUP = Messages
			.getString("DeleteListGroupAction.actionMsg.deleteGroup"); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteListGroup"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public DeleteListGroupAction(IWorkbenchPart part) {
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
		return getListGroup() != null;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Delete list group action >> Run ..."); //$NON-NLS-1$
		}
		if (getListGroup() != null && getListEditPart() != null) {
			getListEditPart().removeGroup(getListGroup());
		}
	}
}