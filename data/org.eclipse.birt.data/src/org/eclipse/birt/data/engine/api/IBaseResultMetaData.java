/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 *
 */

public interface IBaseResultMetaData {

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
	 * @throws BirtException if given index is invalid.
	 */
	String getColumnName(int index) throws BirtException;

	/**
	 * Returns the data type of the column at the specified index.
	 *
	 * @param index The projected column index.
	 * @return The data type of the specified column, as an integer defined in
	 *         org.eclipse.birt.data.engine.api.DataType.
	 * @throws BirtException if given index is invalid.
	 */
	int getColumnType(int index) throws BirtException;

}
