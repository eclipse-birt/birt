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

	Object getExpression();

	/**
	 * Sets the raw expression if the type is not constant. If the type is constant,
	 * sets the value.
	 *
	 * @param expr the raw expression or the value
	 * @throws SemanticException
	 *
	 */

	void setExpression(Object expr) throws SemanticException;

	/**
	 * Return the type of the expression.
	 *
	 * @return the expression type
	 */

	String getType();

	/**
	 * Sets the type of the expression.
	 *
	 * @param type the expression type.
	 * @throws SemanticException
	 *
	 */

	void setType(String type) throws SemanticException;

	/**
	 * Returns the object represents all possible expression types.
	 *
	 * @return the expression type object
	 */

	IExpressionType getTypes();

}
