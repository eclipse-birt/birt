
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import org.eclipse.birt.core.exception.BirtException;

/**
 * 
 */

public interface IDatasetIterator {
	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	public int getFieldIndex(String name) throws BirtException;

	/**
	 * Returns the data type of this iterator's member.
	 * 
	 * @param fieldIndex
	 * @return the data type of memeber.
	 */
	public int getFieldType(String name) throws BirtException;

	/**
	 * Moves down one element from its current position of the iterator. This method
	 * applies to a result whose ReportQuery is defined to use detail or group rows.
	 * 
	 * @return true if next element exists and has not reached the limit on the
	 *         maximum number of rows that can be accessed.
	 * @throws BirtException if error occurs in Data Engine
	 */
	public boolean next() throws BirtException;

	/**
	 * Returns the value of a bound column. Currently it is only a dummy
	 * implementation.
	 * 
	 * @param fieldIndex
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Object getValue(int fieldIndex) throws BirtException;

	/**
	 * Closes this result and any associated secondary result iterator(s), providing
	 * a hint that the consumer is done with this result, whose resources can be
	 * safely released as appropriate.
	 * 
	 * @throws BirtException
	 */
	public void close() throws BirtException;

}
