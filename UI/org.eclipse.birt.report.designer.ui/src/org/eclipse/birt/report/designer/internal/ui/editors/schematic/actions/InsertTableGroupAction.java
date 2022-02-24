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
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting a group into table
 */

public class InsertTableGroupAction extends InsertRowAction {

	private static final String ACTION_MSG_GROUP = Messages.getString("InsertGroupAction.actionMsg.group"); //$NON-NLS-1$

	private static final String STACK_MSG_ADD_GROUP = Messages.getString("InsertGroupAction.stackMsg.addGroup"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part
	 */
	public InsertTableGroupAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_GROUP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * InsertRowAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return getTableEditPart() != null;
	}

	/**
	 * Creates the group and run an Edit Action on it to configure it.
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert table group action >> Run ..."); //$NON-NLS-1$
		}
		if (getTableEditPart() != null) {
			CommandStack stack = getActiveCommandStack();
			stack.startTrans(STACK_MSG_ADD_GROUP);
			if (getTableEditPart().insertGroup()) {
				stack.commit();
			} else {
				stack.rollbackAll();
			}
		}
	}
}
