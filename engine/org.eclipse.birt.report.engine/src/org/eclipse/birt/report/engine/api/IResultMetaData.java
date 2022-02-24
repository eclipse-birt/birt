
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
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Describes the metadata of a detail row in an IResultIterator. A detail row is
 * defined based on a query's runtime metadata (as described by its data source
 * driver), merging with static result set hints specified in a data set design.
 * It includes projected columns only, which are all columns returned by a query
 * if no explicit projection is specified. A detail row would also include any
 * computed columns and custom columns specified in a data set design.
 */
public interface IResultMetaData {
	/**
	 * Returns the number of columns in a detail row of the result set.
	 * 
	 * @return the number of columns in a detail row.
	 */
	public int getColumnCount();

	/**
	 * Returns the column name at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnName(int index) throws BirtException;

	/**
	 * Returns the column alias at the specified index. An alias is given to a
	 * column as a programmatic convenience. A column can be referred using a name
	 * or an alias interchangeably.
	 * 
	 * @param index The projected column index.
	 * @return The alias of the specified column. Null if none is defined.
	 * @throws BirtException if given index is invalid.
	 * @deprecated it's not supported now
	 */
	public String getColumnAlias(int index) throws BirtException;

	/**
	 * Returns the data type of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The data type of the specified column, as an integer defined in
	 *         org.eclipse.birt.data.engine.api.DataType.
	 * @throws BirtException if given index is invalid.
	 */
	public int getColumnType(int index) throws BirtException;

	/**
	 * Returns the Data Engine data type name of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The Data Engine data type name of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnTypeName(int index) throws BirtException;

	/**
	 * Gets the label or display name of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The label of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnLabel(int index) throws BirtException;

	/**
	 * Gets if the column can be exported
	 * 
	 * @param index The projected column index.
	 * @return If the column can be exported
	 * @throws BirtException if given index is invalid.
	 */
	public boolean getAllowExport(int index) throws BirtException;

}
