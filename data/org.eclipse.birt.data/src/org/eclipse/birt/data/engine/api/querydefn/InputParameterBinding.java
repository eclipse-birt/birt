/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IInputParameterBinding} interface.
 * <p>
 */
public class InputParameterBinding implements IInputParameterBinding {
	protected String name;
	protected int position = -1;
	protected IBaseExpression expr;

	/**
	 * Constructs a binding based on parameter name
	 */
	public InputParameterBinding(String paramName, IBaseExpression boundExpression) {
		name = paramName;
		expr = boundExpression;
	}

	/**
	 * Constructs a binding based on parameter position
	 */
	public InputParameterBinding(int paramPosn, IBaseExpression boundExpression) {
		position = paramPosn;
		expr = boundExpression;
	}

	/**
	 * Returns the expression that provides the value of the parameter.
	 * 
	 * @return the bound expression
	 */

	public IBaseExpression getExpr() {
		return expr;
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the name. If null, this parameter is bound by position
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the parameter position.
	 * 
	 * @return the name. If -1, this parameter is bound by name
	 */
	public int getPosition() {
		return position;
	}

}
