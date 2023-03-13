/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.List;

/**
 *
 */
public abstract class RuleDesign {
	protected Expression testExpression = null;

	protected Expression value1;
	protected Expression value2;
	protected String operator;
	protected boolean valueIsList = false;
	protected List<Expression> value1List;
	transient protected Expression expr;

	public void setExpression(String operator, List<Expression> value) {
		this.operator = operator;
		this.value1List = value;
		this.valueIsList = true;
	}

	public List<Expression> getValue1List() {
		return this.value1List;
	}

	public boolean ifValueIsList() {
		return this.valueIsList;
	}

	public void setValueIsList(boolean valueIsList) {
		this.valueIsList = valueIsList;
	}

	public void setExpression(String operator, Expression value1, Expression value2) {
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
		this.valueIsList = false;
	}

	public void setConditionExpr(Expression expr) {
		this.expr = expr;
	}

	public Expression getConditionExpr() {
		return expr;
	}

	/**
	 * @return Returns the operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return Returns the value1.
	 */
	public Expression getValue1() {
		return value1;
	}

	/**
	 * @return Returns the value2.
	 */
	public Expression getValue2() {
		return value2;
	}

	/**
	 * @return Returns the testExpression.
	 */
	public Expression getTestExpression() {
		return testExpression;
	}

	/**
	 * @param testExpression The testExpression to set.
	 */
	public void setTestExpression(Expression testExpression) {
		this.testExpression = testExpression;
	}
}
