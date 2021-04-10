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

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Action to delete a column or multi columns of a table or a grid.
 * 
 */
public class DeleteColumnAction extends ContextSelectionAction {

	/** action text */
	private static final String ACTION_MSG_DELETE = Messages.getString("DeleteColumnAction.actionMsg.delete"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "DeleteGroupAction.DeleteColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs a new instance of this acion.
	 * 
	 * @param part The current work bench part
	 */
	public DeleteColumnAction(IWorkbenchPart part) {
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
		return !getColumnHandles().isEmpty() && canDrop(getColumnHandles());
	}

	private boolean canDrop(List columnHandles) {
		for (Iterator it = columnHandles.iterator(); it.hasNext();) {
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
	 * Runs this action.
	 * 
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Delete column action >> Run ..."); //$NON-NLS-1$
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.deleteColumnCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}