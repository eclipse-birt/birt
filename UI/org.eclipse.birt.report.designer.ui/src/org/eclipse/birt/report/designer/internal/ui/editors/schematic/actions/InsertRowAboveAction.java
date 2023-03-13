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
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting one row above the current selected row.
 */

public class InsertRowAboveAction extends InsertRowAction {

	private static final String ACTION_MSG_ROW_ABOVE = Messages.getString("InsertRowAboveAction.actionMsg.rowAbove"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 *
	 * @param part current work bench part
	 */
	public InsertRowAboveAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_ROW_ABOVE);
	}

	/**
	 * Runs action.
	 *
	 */
	@Override
	public void run() {

		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_ROW_POSITION, Integer.valueOf(-1));

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.insertRowCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}
}
