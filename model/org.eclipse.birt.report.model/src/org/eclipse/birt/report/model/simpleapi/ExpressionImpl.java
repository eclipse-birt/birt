/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IExpression;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;

/**
 *
 */

public class ExpressionImpl implements IExpression {

	private ExpressionHandle exprHandle = null;

	private Expression expr = null;

	/**
	 * Constructor with the given expression.
	 * 
	 * @param expr
	 */

	public ExpressionImpl(Expression expr) {
		this.expr = expr;
	}

	/**
	 * Constructor with the given expression handle.
	 * 
	 * @param exprHandle
	 */

	ExpressionImpl(ExpressionHandle exprHandle) {
		this.exprHandle = exprHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpression#getExpression()
	 */

	public Object getExpression() {
		if (exprHandle != null)
			return exprHandle.getExpression();
		else if (expr != null)
			return expr.getExpression();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpression#getType()
	 */
	public String getType() {
		if (exprHandle != null)
			return exprHandle.getType();
		else if (expr != null)
			return expr.getType();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpression#setExpression
	 * (java.lang.Object)
	 */

	public void setExpression(Object value) throws SemanticException {
		if (exprHandle != null)
			exprHandle.setExpression(value);
		else if (expr != null)
			expr = new Expression(value, expr.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpression#setType(java.
	 * lang.String)
	 */
	public void setType(String type) throws SemanticException {
		if (exprHandle != null)
			exprHandle.setType(type);
		else if (expr != null)
			expr = new Expression(expr.getExpression(), type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpression#getTypes()
	 */

	public IExpressionType getTypes() {
		return ExpressionTypeImpl.getInstance();
	}

}
