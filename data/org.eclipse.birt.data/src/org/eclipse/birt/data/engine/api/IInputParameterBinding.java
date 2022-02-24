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
package org.eclipse.birt.data.engine.api;

/**
 * Describes on input parameter binding, which associates one input parameter
 * (identified by either parameter name or position) to a JavaScript expression
 */
public interface IInputParameterBinding {
	/**
	 * Returns the expression that provides the value of the parameter.
	 * 
	 * @return the bound expression
	 */
	public IBaseExpression getExpr();

	/**
	 * Returns the parameter name.
	 * 
	 * @return the name. If null, this parameter is bound by position
	 */
	public String getName();

	/**
	 * Returns the parameter position.
	 * 
	 * @return the name. If -1, this parameter is bound by name
	 */
	public int getPosition();
}
