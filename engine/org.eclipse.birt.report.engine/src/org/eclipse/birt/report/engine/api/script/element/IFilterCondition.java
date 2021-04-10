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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an FilterCondition in the scripting environment
 * 
 */

public interface IFilterCondition {

	/**
	 * Returns operator
	 * 
	 * @return operator
	 */

	public String getOperator();

	/**
	 * Sets operator
	 * 
	 * @param operator
	 * @throws ScriptException
	 */

	public void setOperator(String operator) throws ScriptException;

	/**
	 * Returns value1
	 * 
	 * @return value1
	 */

	public String getValue1();

	/**
	 * Sets value1
	 * 
	 * @param value1
	 * @throws ScriptException
	 */

	public void setValue1(String value1) throws ScriptException;

	/**
	 * Returns value2
	 * 
	 * @return value2
	 */

	public String getValue2();

	/**
	 * Sets value2
	 * 
	 * @param value2
	 * @throws ScriptException
	 */

	public void setValue2(String value2) throws ScriptException;

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure();

	/**
	 * Sets expr
	 * 
	 * @param expr
	 */

	public void setExpr(String expr) throws ScriptException;

	/**
	 * Returns expr
	 * 
	 * @return expr
	 */

	public String getExpr();
}
