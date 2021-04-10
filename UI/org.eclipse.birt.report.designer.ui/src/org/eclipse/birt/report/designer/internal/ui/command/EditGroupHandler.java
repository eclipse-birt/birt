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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class EditGroupHandler extends SelectionHandler {

	private static final String STACK_MSG_EDIT_GROUP = Messages.getString("EditGroupAction.stackMsg.editGroup"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		GroupHandle handle = null;
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context, EditGroupAction.GROUP_HANDLE_NAME);
		if (obj != null && obj instanceof GroupHandle) {
			handle = (GroupHandle) obj;
		}

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit group action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = getActiveCommandStack();
		stack.startTrans(STACK_MSG_EDIT_GROUP);

		GroupDialog dialog = new GroupDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				GroupDialog.GROUP_DLG_TITLE_EDIT);
		dialog.setInput(handle);

		if (dialog.open() == Window.OK) {
			stack.commit();
		} else {
			stack.rollbackAll();
		}

		return Boolean.TRUE;
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
