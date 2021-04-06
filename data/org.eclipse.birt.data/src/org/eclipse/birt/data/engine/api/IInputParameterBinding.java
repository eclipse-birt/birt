/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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