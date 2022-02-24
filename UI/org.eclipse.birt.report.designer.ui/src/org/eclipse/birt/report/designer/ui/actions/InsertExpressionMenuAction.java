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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.gef.Request;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action class for insert an expression item. Actually we insert a Data element
 * and fill its expression property.
 */

public class InsertExpressionMenuAction extends BaseInsertMenuAction {

	/**
	 * ID for insert Expression action.
	 */
	public static final String ID = "Insert Expression"; //$NON-NLS-1$

	/**
	 * Display text for insert Expression action.
	 */
	public static final String DISPLAY_TEXT = Messages.getString("InsertExpressionMenuAction.text.Expression"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param part
	 */
	public InsertExpressionMenuAction(IWorkbenchPart part) {
		super(part, ReportDesignConstants.DATA_ITEM);

		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.actions.BaseInsertMenuAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert expression menu action >> Run ..."); //$NON-NLS-1$
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder(); // $NON-NLS-1$

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
	}
}
