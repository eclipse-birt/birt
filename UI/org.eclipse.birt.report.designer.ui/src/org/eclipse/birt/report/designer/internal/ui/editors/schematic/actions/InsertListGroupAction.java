/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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
 * add comment here
 *
 */
public class InsertListGroupAction extends ContextSelectionAction {

	private static final String ACTION_MSG_INSERT_GROUP = Messages
			.getString("InsertListGroupAction.actionMsg.insertGroup"); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertListGroup"; //$NON-NLS-1$

	private static final String STACK_MSG_INSERT_GROUP = Messages
			.getString("InsertListGroupAction.stackMsg.insertListGroup"); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public InsertListGroupAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_INSERT_GROUP);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		return getListEditPart() != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */

	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert list group action >> Run ..."); //$NON-NLS-1$
		}
		if (getListEditPart() != null) {
			CommandStack stack = getActiveCommandStack();
			stack.startTrans(STACK_MSG_INSERT_GROUP);
			if (getListEditPart().insertGroup()) {
				stack.commit();
			} else {
				stack.rollbackAll();
			}
		}
	}
}
