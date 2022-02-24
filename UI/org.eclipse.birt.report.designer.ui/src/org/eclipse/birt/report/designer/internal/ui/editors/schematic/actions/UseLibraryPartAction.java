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

import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportLibraryAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Wrappers ImportLibraryPartAction from a viewer action to a part selection
 * action.
 */

public class UseLibraryPartAction extends WrapperSelectionAction {

	public UseLibraryPartAction(IWorkbenchPart part) {
		super(part);

		setText(ImportLibraryAction.ACTION_TEXT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * WrapperSelectionAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getId() {
		return ImportLibraryAction.ID;
	}

	protected IAction createActionHandler(ISelection model) {
		if (actionHandler == null) {
			actionHandler = new ImportLibraryAction();
		}
		return actionHandler;
	}

}
