/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
