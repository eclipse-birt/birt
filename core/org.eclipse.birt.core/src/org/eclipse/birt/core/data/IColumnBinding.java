/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.data;

/**
 * The instance of this interface defines a name-expression pair of column
 * binding.
 *
 */
public interface IColumnBinding {
	/**
	 * Return the name of result set column.
	 * 
	 * @return
	 */
	String getResultSetColumnName();

	/**
	 * Return the bound expression.
	 *
	 * @return
	 */
	String getBoundExpression();

	/**
	 * Get the outer level of column expression
	 *
	 * @return
	 */
	int getOuterLevel();
}
