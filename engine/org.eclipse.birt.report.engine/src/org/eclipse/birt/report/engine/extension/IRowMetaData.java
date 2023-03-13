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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Defines a row that the extension might receive. Each column in a row comes
 * from the evaluation of a expression. The getColumnExpression method returns
 * such an expression.
 */
public interface IRowMetaData {

	/**
	 * Returns the number of columns in a detail row of the result set.
	 *
	 * @return the number of columns in a detail row.
	 */
	int getColumnCount();

	/**
	 * Returns the column name at the specified index.
	 *
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	String getColumnName(int index) throws BirtException;

	int getColumnType(int index) throws BirtException;

	/**
	 * Returns the column expression that results in the data at the specified
	 * index.
	 *
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	// public String getColumnExpression( int index ) throws BirtException;
	// TODO removed, DTE doesn't support this feature.

}
