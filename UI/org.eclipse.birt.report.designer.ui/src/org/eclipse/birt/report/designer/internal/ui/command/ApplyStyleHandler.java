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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;

/**
 *
 */

public class ApplyStyleHandler extends SelectionHandler {

	private static final String STACK_MSG_APPLY_STYLE = Messages.getString("ApplyStyleAction.stackMsg.applyStyle"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Apply style rule action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(STACK_MSG_APPLY_STYLE);

		boolean isChecked = true;
		SharedStyleHandle handle = null;

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.STYLE_HANDLE_NAME);
		if (obj instanceof SharedStyleHandle) {
			handle = (SharedStyleHandle) obj;
		}

		obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.APPLAY_STYLE_ACTION_STYLE_CHECKED);
		if (obj instanceof Boolean) {
			isChecked = ((Boolean) obj).booleanValue();
		}

		try {
			List handles = getElementHandles();
			for (int i = 0; i < handles.size(); i++) {
				((DesignElementHandle) handles.get(i)).setStyle(isChecked ? handle : null);
			}
			stack.commit();
		} catch (StyleException e) {
			stack.rollbackAll();
			ExceptionHandler.handle(e);
		}
		return Boolean.TRUE;
	}

}
