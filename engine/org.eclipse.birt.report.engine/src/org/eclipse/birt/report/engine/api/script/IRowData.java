/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.script;

/**
 * Represents the computed expression results that are bound to the current row.
 * The index starts with 1, which reprents the first expression in the row.
 */

public interface IRowData {
	/**
	 * @deprecated Return the value of the provided expression. The provided
	 *             expression must have been bound to the current row. Otherwise, it
	 *             returns null.
	 * @throws ScriptException
	 */
	public Object getExpressionValue(String expression) throws ScriptException;

	/**
	 * @deprecated Now do not support get expression value by index. Return the
	 *             value of the i:th expression in the current row. Null will be
	 *             return if the i:th expression doesn't exist.
	 * @throws ScriptException
	 */
	public Object getExpressionValue(int i) throws ScriptException;

	/**
	 * Return the number of expressions bound to the current row.
	 * 
	 * @deprecated
	 */
	public int getExpressionCount();

	/**
	 * Return the value of the bouding exprssion.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public Object getColumnValue(String name) throws ScriptException;

	/**
	 * Return the value of the bouding exprssion by id.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public Object getColumnValue(int index) throws ScriptException;

	/**
	 * Return the name of the bouding exprssion by id.
	 * 
	 * @param index
	 */
	public String getColumnName(int index);

	/**
	 * Return the count of the bouding exprssions.
	 */
	public int getColumnCount();

}
