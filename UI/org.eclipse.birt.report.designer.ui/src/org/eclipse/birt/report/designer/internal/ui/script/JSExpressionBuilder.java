/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContext;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.swt.widgets.Shell;

/**
 * JSExpressionBuilder
 */
public class JSExpressionBuilder extends ExpressionBuilder implements IExpressionBuilder {

	public JSExpressionBuilder(Shell parentShell, String initExpression) {
		super(parentShell, initExpression);
	}

	public Object getExpression() {
		return expression;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String newTitle) {
		this.title = newTitle;

		super.setTitle(newTitle);
	}

	public void setExpression(Object exprObj) {
		String exp = exprObj == null ? null : exprObj.toString();

		this.expression = UIUtil.convertToGUIString(exp);
	}

	public void setExpressionContext(IExpressionContext context) {
		if (context instanceof JSExpressionContext) {
			this.setExpressionProvider(((JSExpressionContext) context).getExpressionProvider());
		}

	}

}
