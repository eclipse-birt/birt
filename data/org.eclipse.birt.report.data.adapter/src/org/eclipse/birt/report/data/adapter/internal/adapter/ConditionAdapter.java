/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;

/**
 * A boolean expression defined with a main expression, an operator, and 0 to 2
 * operands
 */
public class ConditionAdapter extends ConditionalExpression {
	/**
	 * Constructs an instance, setting main expression and the operator (which takes
	 * no operands) The operator parameter contains a String operator defined in
	 * Model
	 */
	public ConditionAdapter(String mainExpr, String operator) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator));
	}

	/**
	 * Constructs an instance, setting main expression, a unary operator, and its
	 * operand The operator parameter contains a String operator defined in Model
	 */
	public ConditionAdapter(String mainExpr, String operator, String operand1) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator),
				operand1);
	}

	/**
	 * Constructs an instance, setting main expression, a binary operator, and its
	 * two operands The operator parameter contains a String operator defined in
	 * Model
	 */
	public ConditionAdapter(String mainExpr, String operator, String operand1, String operand2) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator),
				operand1, operand2);
	}

	/**
	 *
	 * @param mainExpr
	 * @param operator
	 * @param operands
	 */
	public ConditionAdapter(String mainExpr, String operator, List operands) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator),
				operands);
	}

	public ConditionAdapter(IScriptExpression mainExpr, String operator, List operands) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator),
				operands);
	}

	public ConditionAdapter(IScriptExpression mainExpr, String operator, IBaseExpression op1, IBaseExpression op2) {
		super(mainExpr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelFilterOperator(operator),
				op1, op2);
	}
}
