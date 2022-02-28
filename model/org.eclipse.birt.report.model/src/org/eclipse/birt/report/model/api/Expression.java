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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents the object for the expression. The expression has an expression
 * value and its type.
 *
 * @see ExpressionType
 */

public class Expression {

	private Object expr;

	private String type;

	/**
	 * Constructor
	 *
	 * @param expr the value
	 * @param type the type
	 */

	public Expression(Object expr, String type) {
		this.expr = expr;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return expr == null ? null : expr.toString();
	}

	/**
	 * Return the raw expression if the type is not constant. If the type is
	 * constant, get the value in string.
	 *
	 * @return the raw expression or the value in string
	 */

	public String getStringExpression() {
		return expr == null ? null : expr.toString();
	}

	/**
	 * Return the raw expression if the type is not constant. If the type is
	 * constant, get the value.
	 *
	 * @return the raw expression or the value
	 */

	public Object getExpression() {
		return expr;
	}

	/**
	 * Return the type of the expression. It can be one of defined in
	 * <code>ExpressionType</code>. For the compatibility issue, in default, it is
	 * <code>ExpressionType.JAVASCRIPT</code>.
	 *
	 * @see ExpressionType
	 *
	 * @return the type
	 */

	public String getType() {
		if (type == null) {
			return ExpressionType.JAVASCRIPT;
		}

		return type;
	}

	/**
	 * Return the type of the expression set by the user. This method ignore the
	 * compatibility issue.
	 *
	 * @see ExpressionType
	 *
	 * @return the type
	 */

	public String getUserDefinedType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Expression)) {
			return false;
		}

		Expression tmpExpr = (Expression) obj;
		if (!ModelUtil.isEquals(expr, tmpExpr.getExpression()) || !ModelUtil.isEquals(type, tmpExpr.getUserDefinedType())) {
			return false;
		}

		return true;
	}
}
