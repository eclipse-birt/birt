/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;

/**
 * Represents a data row in an open data set. Methods in this interface allows
 * getting and setting column data
 */
public interface IDataRow {
	/**
	 * Gets the data set runtime instance which contains this row
	 */
	public abstract IDataSetInstanceHandle getDataSet();

	/**
	 * Gets the metadata of the data row. This is a shortcut to
	 * getDataSet().getResultMetaData().
	 * 
	 * @return result metadata. If no result metadata is currently availabe, null is
	 *         returned.
	 */
	public abstract IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Gets the column data by index. Data row column index starts from 1.
	 * 
	 * @param index 1-based index of column. If value is 0, an internal index of the
	 *              current row (if available) is returned
	 * @exception BirtException if index is out of bounds, or if current data row is
	 *                          unavailable
	 */
	public abstract Object getColumnValue(int index) throws BirtException;

	/**
	 * Sets the column data by index. Column index starts from 1.
	 * 
	 * @param index 1-based index of column. Value must be between 1 and the number
	 *              of columns
	 * @param value New value for column (can be null)
	 * @exception BirtException if index is out of bounds, or if value has an
	 *                          incompatible data type, or if update is not allowed
	 *                          at this time
	 */
	public abstract void setColumnValue(int index, Object value) throws BirtException;

	/**
	 * Gets the column data by column name.
	 * 
	 * @param name of column
	 * @exception BirtException if column name is not found, or if current data row
	 *                          is unavailable
	 */
	public abstract Object getColumnValue(String name) throws BirtException;

	/**
	 * Sets the column data by column name.
	 * 
	 * @param name of column
	 * @exception BirtException if column name is not found, or if value has an
	 *                          incompatible data type, or if update is not allowed
	 *                          at this time.
	 */
	public abstract void setColumnValue(String name, Object value) throws BirtException;
}
