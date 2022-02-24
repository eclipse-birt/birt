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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Splits cell action
 * 
 */
public class SplitAction extends ContextSelectionAction {

	private static final String ACTION_MSG_SPLIT_CELLS = Messages.getString("SplitAction.actionMsg.splitCells"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 * 
	 * @param part current work bench part
	 */
	public SplitAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_SPLIT_CELLS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getRowHandles().isEmpty() && getColumnHandles().isEmpty()) {
			return getTableEditPart() != null && cellCanSplit();
		}
		return false;
	}

	/**
	 * Determines whether selected cell can be splited.
	 * 
	 * @return
	 */
	private boolean cellCanSplit() {
		if (getSelectedObjects().size() != 1) {
			return false;
		}
		Object obj = getSelectedObjects().get(0);
		if (obj instanceof TableCellEditPart) {
			CellHandleAdapter adapt = HandleAdapterFactory.getInstance()
					.getCellHandleAdapter(((TableCellEditPart) obj).getModel());
			if (adapt.getRowSpan() != 1 || adapt.getColumnSpan() != 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.splitCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	/**
	 * Gets cell edit part.
	 * 
	 * @return current table cell edit part
	 */
	private TableCellEditPart getTableCellEditPart() {
		return (TableCellEditPart) getSelectedObjects().get(0);
	}
}
