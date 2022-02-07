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
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Action delete a row or multi rows of a table or a grid.
 * 
 */
public class DeleteRowAction extends ContextSelectionAction {

	/** action text */
	private static final String ACTION_MSG_DELETE = Messages.getString("DeleteRowAction.actionMsg.delete"); //$NON-NLS-1$

	/** action ID for registry */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteRowAction"; //$NON-NLS-1$

	/**
	 * Constructs a new instance of this action.
	 * 
	 * @param part The current work bench part
	 */
	public DeleteRowAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_DELETE);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setAccelerator(SWT.DEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
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
	 * Runs this action.
	 * 
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Delete row action >> Run ..."); //$NON-NLS-1$
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.deleteRowCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
