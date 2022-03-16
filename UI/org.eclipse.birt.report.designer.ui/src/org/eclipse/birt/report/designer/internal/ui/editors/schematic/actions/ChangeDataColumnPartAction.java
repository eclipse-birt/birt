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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ChangeDataColumnAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public class ChangeDataColumnPartAction extends WrapperSelectionAction {

	private static final String DEFAULT_TEXT = Messages.getString("ChangeDataColumnAction.text"); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.ChangeDataColumnAction"; //$NON-NLS-1$

	public ChangeDataColumnPartAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(DEFAULT_TEXT);
	}

	/**
	 * Gets the activity stack of the report
	 *
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	@Override
	protected IAction createActionHandler(ISelection model) {
		return new ChangeDataColumnAction(model);
	}

}
