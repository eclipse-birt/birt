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

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Insert row action,insert a row or multi rows into a table or a grid.
 *
 */
public class InsertRowAction extends ContextSelectionAction {

	/**
	 * display label of action
	 */
	private static final String ACTION_MSG_INSERT = Messages.getString("InsertRowAction.actionMsg.insert"); //$NON-NLS-1$

	/**
	 * action id
	 */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAction"; //$NON-NLS-1$

	/**
	 * Constructs a insert row action.
	 *
	 * @param part work bench part.
	 */
	public InsertRowAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_INSERT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		return !getRowHandles().isEmpty() && canDrop(getRowHandles());
	}

	private boolean canDrop(List rowHandles) {
		for (Iterator it = rowHandles.iterator(); it.hasNext();) {
			if (!canDrop((RowHandle) it.next())) {
				return false;
			}
		}
		return true;
	}

	private boolean canDrop(RowHandle handle) {
		return ((RowHandle) handle).canDrop();
	}

	/**
	 * Runs the action.
	 */
	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert row action >> Run ..."); //$NON-NLS-1$
		}
		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_ROW_POSITION, Integer.valueOf(-1));

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.insertRowCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
