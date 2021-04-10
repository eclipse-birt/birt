/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.CopyCellContentsHandler;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Copy cell's contents context menu
 */
public class CopyCellContentsContextAction extends ContextSelectionAction {

	public static final String ID = "org.eclipse.birt.report.designer.ui.command.copyCellContentsContextAction"; //$NON-NLS-1$

	public CopyCellContentsContextAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(Messages.getString("CopyCellContentsContextAction.actionText")); //$NON-NLS-1$
	}

	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Copy action >> Copy " + getSelection()); //$NON-NLS-1$
		}

		try {
			CommandUtils.executeCommand(CopyCellContentsHandler.ID);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public boolean calculateEnabled() {
		if (canCopy(getSelectedObjects()))
			return true;
		return false;
	}

	public boolean canCopy(List selection) {
		if (selection.size() == 1 && selection.get(0) instanceof TableCellEditPart) {
			TableCellEditPart tcep = (TableCellEditPart) selection.get(0);
			CellHandle cellHandle = (CellHandle) tcep.getModel();
			return cellHandle.getContent().getCount() > 0;
		}
		return false;
	}
}
