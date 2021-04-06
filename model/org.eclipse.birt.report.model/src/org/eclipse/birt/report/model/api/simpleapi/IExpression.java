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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 *
 */

public interface IExpression {

	/**
	 * Return the raw expression if the type is not constant. If the type is
	 * constant, get the value.
	 * 
	 * @return the raw expression or the value
	 */

	public Object getExpression();

	/**
	 * Sets the raw expression if the type is not constant. If the type is constant,
	 * sets the value.
	 * 
	 * @param expr the raw expression or the value
	 * @throws SemanticException
	 * 
	 */

	public void setExpression(Object expr) throws SemanticException;

	/**
	 * Return the type of the expression.
	 * 
	 * @return the expression type
	 */

	public String getType();

	/**
	 * Sets the type of the expression.
	 * 
	 * @param type the expression type.
	 * @throws SemanticException
	 * 
	 */

	public void setType(String type) throws SemanticException;

	/**
	 * Returns the object represents all possible expression types.
	 * 
	 * @return the expression type object
	 */

	public IExpressionType getTypes();

}
