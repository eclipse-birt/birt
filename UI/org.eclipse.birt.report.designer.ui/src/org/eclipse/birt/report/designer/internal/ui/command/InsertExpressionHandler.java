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
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.Request;
import org.eclipse.jface.window.Window;

/**
 * 
 */

public class InsertExpressionHandler extends BaseInsertHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert expression menu action >> Run ..."); //$NON-NLS-1$
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder();

		if (slotHandle != null) {
			expressionBuilder.setExpressionProvier(new ExpressionProvider(slotHandle.getElementHandle()));

		}
		if (expressionBuilder.open() == Window.OK) {
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(STACK_MSG_INSERT_ELEMENT);

			try {
				Request req = insertElement();
				Object obj = req.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT);

				if (obj instanceof DataItemHandle) {
					((DataItemHandle) obj).setResultSetColumn(expressionBuilder.getResult());
				}

				stack.commit();

				selectElement(obj, false);
			} catch (Exception e) {
				stack.rollbackAll();
				ExceptionHandler.handle(e);
			}
		}

		return Boolean.FALSE;
	}
}
