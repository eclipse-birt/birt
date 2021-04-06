/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public abstract int getColumnCount();

	/**
	 * Returns the column name at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public abstract String getColumnName(int index) throws BirtException;

	/**
	 * Returns the data type of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The data type of the specified column, as an integer defined in
	 *         org.eclipse.birt.data.engine.api.DataType.
	 * @throws BirtException if given index is invalid.
	 */
	public abstract int getColumnType(int index) throws BirtException;

}