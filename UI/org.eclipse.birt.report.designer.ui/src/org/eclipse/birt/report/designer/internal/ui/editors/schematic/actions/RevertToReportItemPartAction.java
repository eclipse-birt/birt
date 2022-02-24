/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RevertToReportItemAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class RevertToReportItemPartAction extends WrapperSelectionAction {
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.RevertToReportItemAction"; //$NON-NLS-1$
	private static final String DEFAULT_TEXT = Messages.getString("RevertToReportItemAction.text"); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public RevertToReportItemPartAction(IWorkbenchPart part) {
		super(part);

		setId(ID);
		setText(DEFAULT_TEXT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * WrapperSelectionAction#createActionHandler(org.eclipse.jface.viewers.
	 * ISelection)
	 */
	protected IAction createActionHandler(ISelection model) {
		return new RevertToReportItemAction(model);
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}
}
