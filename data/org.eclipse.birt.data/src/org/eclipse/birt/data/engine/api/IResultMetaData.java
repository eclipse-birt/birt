/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Describes the metadata of a detail row in an IResultIterator. A detail row is
 * defined based on a query's runtime metadata (as described by its data source
 * driver), merging with static result set hints specified in a data set design.
 * It includes projected columns only, which are all columns returned by a query
 * if no explicit projection is specified. A detail row would also include any
 * computed columns and custom columns specified in a data set design.
 */
public interface IResultMetaData extends IBaseResultMetaData {
	/**
	 * Returns the column alias at the specified index. An alias is given to a
	 * column as a programmatic convenience. A column can be referred using a name
	 * or an alias interchangeably.
	 * 
	 * @param index The projected column index.
	 * @return The alias of the specified column. Null if none is defined.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnAlias(int index) throws BirtException;

	/**
	 * Returns the Data Engine data type name of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The Data Engine data type name of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnTypeName(int index) throws BirtException;

	/**
	 * Returns the data provider specific data type name of the specified column.
	 * 
	 * @return the data type name as defined by the data provider.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnNativeTypeName(int index) throws BirtException;

	/**
	 * Gets the label or display name of the column at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The label of the specified column.
	 * @throws BirtException if given index is invalid.
	 */
	public String getColumnLabel(int index) throws BirtException;

	/**
	 * Indicates whether the specified projected column is defined as a computed
	 * column. A computed column is one that is not retrieved from the underlying
	 * data provider. Only those computed columns declared explicitly in a data set
	 * design are considered as "computed" columns.
	 * 
	 * @param index The projected column index.
	 * @return true if the given column is a computed column; false otherwise.
	 * @throws BirtException if given index is invalid.
	 */
	public boolean isComputedColumn(int index) throws BirtException;

}
