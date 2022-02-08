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

public interface IBaseResultIterator {

	/**
	 * Returns the metadata of this result set's detail row.
	 * 
	 * @return The result metadata of a detail row.
	 */
	public abstract IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Each row has its own index, which indicates this row position in the result
	 * set. This method retrieves current row index. The row index is 0 based, and
	 * -1 is returned when there is no current row.
	 * 
	 * @return row index of current row
	 * @throws BirtException if error occurs in Data Engine
	 */
	public abstract int getRowIndex() throws BirtException;

	/**
	 * Returns the value of a bound column
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public abstract Object getValue(String name) throws BirtException;

	/**
	 * Closes this result set. Housekeeping all the resources allocated to this
	 * result set.
	 * 
	 * @throws BirtException
	 */
	public abstract void close() throws BirtException;

	/**
	 * Indicate if the IResultSetIterator is empty or not
	 * 
	 * @return true if IResultSetIterator is empty. false if the IResultSetIterator
	 *         is not empty.
	 * @throws BirtException
	 */
	public abstract boolean isEmpty() throws BirtException;

	/**
	 * Move to the next record of the result set.
	 * 
	 * @return true if next element exists and has not reached the limit on the
	 *         maximum number of rows that can be accessed.
	 * @throws BirtException if error occurs in Data Engine
	 */
	public boolean next() throws BirtException;

}
