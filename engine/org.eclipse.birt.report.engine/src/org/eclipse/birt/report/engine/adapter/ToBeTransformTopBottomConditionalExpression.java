/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.adapter;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public class ToBeTransformTopBottomConditionalExpression extends ConditionalExpression {

	private boolean transformed = false;

	public ToBeTransformTopBottomConditionalExpression(IScriptExpression expr, int operator, IBaseExpression op1,
			IBaseExpression op2) {
		super(expr, operator, op1, op2);
	}

	public Binding transform(String name, String group) throws DataException {
		if (transformed) {
			return null;
		}
		transformed = true;
		Binding result = new Binding(name);
		if (group != null) {
			result.addAggregateOn(group);
		}
		result.setExpression(this.expr);
		switch (this.operator) {
		case IConditionalExpression.OP_TOP_N:
			result.setAggrFunction("ISTOPN");
			break;
		case IConditionalExpression.OP_TOP_PERCENT:
			result.setAggrFunction("ISTOPNPERCENT");
			break;
		case IConditionalExpression.OP_BOTTOM_PERCENT:
			result.setAggrFunction("ISBOTTOMNPERCENT");
			break;
		case IConditionalExpression.OP_BOTTOM_N:
			result.setAggrFunction("ISBOTTOMN");
		}
		result.addArgument(this.op1);
		this.operator = IConditionalExpression.OP_TRUE;
		this.expr = new ScriptExpression(org.eclipse.birt.core.data.ExpressionUtil.createJSRowExpression(name));
		this.op1 = null;
		this.op2 = null;
		return result;
	}
}
