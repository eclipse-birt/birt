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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Simplifies working with expression properties. An expression value consists
 * of two parts: the raw expression string if the type is not constant; or the
 * value if the type is constant. If the type is constant, the value can be in
 * String, Integer, DimensionValue, etc.
 * 
 * @see ExpressionType
 */

public class ExpressionHandle extends ComplexValueHandle {

	/**
	 * Constructs an expression handle for the structure member.
	 * 
	 * @param element the design element handle
	 * @param context the context for the member property
	 */

	public ExpressionHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Constructs an expression handle for the structure member.
	 * 
	 * @param element   the design element handle
	 * @param memberRef the memberRef for the member property
	 * @deprecated
	 */

	public ExpressionHandle(DesignElementHandle element, MemberRef memberRef) {
		super(element, memberRef);
	}

	/**
	 * Constructs an expression handle for an element property.
	 * 
	 * @param element     handle to the element that defined the property.
	 * @param thePropDefn definition of the expression property.
	 */

	public ExpressionHandle(DesignElementHandle element, ElementPropertyDefn thePropDefn) {
		super(element, thePropDefn);
	}

	/**
	 * Return the raw expression if the type is not constant. If the type is
	 * constant, returns the value.
	 * 
	 * @return the raw expression
	 */

	public Object getExpression() {
		Expression value = (Expression) getValue();
		if (value != null)
			return value.getExpression();

		return null;
	}

	/**
	 * Sets the raw expression if the type is not constant. If the type is constant,
	 * sets the value.
	 * 
	 * @param expr the raw expression or the value
	 * @throws SemanticException
	 * 
	 */

	public void setExpression(Object expr) throws SemanticException {
		Expression value = (Expression) getValue();

		Expression newValue = null;
		if (value != null)
			newValue = new Expression(expr, value.getType());
		else
			newValue = new Expression(expr, null);

		setValue(newValue);
	}

	/**
	 * Return the type of the expression.
	 * 
	 * @return the expression type
	 */

	public String getType() {
		Expression value = (Expression) getValue();
		if (value != null)
			return value.getType();

		return null;
	}

	/**
	 * Sets the type of the expression.
	 * 
	 * @param type the expression type.
	 * @throws SemanticException
	 * 
	 */

	public void setType(String type) throws SemanticException {
		Expression value = (Expression) getValue();

		Expression newValue = null;
		if (value != null)
			newValue = new Expression(value.getExpression(), type);
		else if (type != null)
			newValue = new Expression(null, type);

		setValue(newValue);
	}

	/**
	 * Return the expression in string format.
	 * <p>
	 * <ul>
	 * <li>if the type is not constant, return the raw expression;
	 * <li>if the type is constant, return the value in string.
	 * </ul>
	 * 
	 * @return the raw expression or the value in string
	 */

	public String getStringExpression() {
		return ((PropertyDefn) getDefn()).getStringValue(getModule(), getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ComplexValueHandle#getValue()
	 */

	public Object getValue() {
		return getRawValue();
	}

}
