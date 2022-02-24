/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.designer.internal.lib.editors.actions;

import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Exports the designelement handle of the seletion to a other file.
 * 
 */
public class ExportAction extends SelectionAction {

	/**
	 * display label of action
	 */
	private static final String ACTION_MSG_INSERT = Messages.getString("ExportAction.actionMsg.export"); //$NON-NLS-1$

	/**
	 * action id
	 */
	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.ExportAction"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public ExportAction(IWorkbenchPart part) {
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
		List list = getSelectedObjects();
		if (list.isEmpty() && list.size() != 1) {
			return false;
		}
		Object obj = list.get(0);
		if (obj instanceof EditPart) {
			return ((EditPart) obj).getModel() instanceof DesignElementHandle;
		}
		return false;
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {
		// Do nothing
	}
}
