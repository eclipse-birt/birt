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

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class EditStyleHandler extends SelectionHandler {

	SharedStyleHandle handle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context,
				ICommandParameterNameContants.EDIT_STYLE_SHARED_STYLE_HANDLE_NAME);
		if (obj != null && obj instanceof SharedStyleHandle) {
			handle = (SharedStyleHandle) obj;
		}

		if (handle == null) {
			return Boolean.FALSE;
		}

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Edit style action >> Run ..."); //$NON-NLS-1$
		}
		StyleBuilder builder = new StyleBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), handle,
				StyleBuilder.DLG_TITLE_EDIT);
		builder.open();

		return Boolean.TRUE;
	}
}
