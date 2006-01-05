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

package org.eclipse.birt.report.engine.api.script;

/**
 * Represents the computed expression results that are bound to the current row.
 * The index starts with 1, which reprents the first expression in the row.
 */

public interface IRowData
{
	/**
	 * Return the value of the provided expression. The provided expression must
	 * have been bound to the current row. Otherwise, it returns null.
	 * @throws ScriptException 
	 */
	public Object getExpressionValue( String expression ) throws ScriptException;

	/**
	 * Return the value of the i:th expression in the current row. Null will be
	 * return if the i:th expression doesn't exist.
	 * @throws ScriptException 
	 */
	public Object getExpressionValue( int i ) throws ScriptException;

	/**
	 * Return the number of expressions bound to the current row.
	 */
	public int getExpressionCount( );

}
