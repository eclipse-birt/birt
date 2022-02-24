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

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting a column into table.
 * 
 * @author Dazhen Gao
 * @version $Revision: 1.8 $ $Date: 2008/01/25 08:37:53 $
 */
public class InsertColumnAction extends ContextSelectionAction {

	private static final String ACTION_MSG_INSERT = Messages.getString("InsertColumnAction.actionMsg.insert"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 * 
	 * @param part current work bench part
	 */
	public InsertColumnAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_INSERT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return !getColumnHandles().isEmpty() && canDrop(getColumnHandles());
	}

	private boolean canDrop(List columns) {
		for (Iterator it = columns.iterator(); it.hasNext();) {
			if (!canDrop((ColumnHandle) it.next())) {
				return false;
			}
		}
		return true;
	}

	private boolean canDrop(ColumnHandle handle) {
		return ((ColumnHandle) handle).canDrop();
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert column action >> Run ..."); //$NON-NLS-1$
		}

		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_COLUMN_POSITION, Integer.valueOf(-1));

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.insertColumnCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
