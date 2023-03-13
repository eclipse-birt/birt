/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;

/**
 * An iterator on a result set from a prepared and executed query. Multiple
 * IResultIterator objects could be associated with the same IExtractionResults
 * object, if extraction is done at report level
 */
public interface IDataIterator {
	/**
	 * Returns {@link org.eclipse.birt.report.engine.api.IExtractionResults} from
	 * which this data iterator is obtained.
	 */
	IExtractionResults getQueryResults();

	/**
	 * Returns the metadata of this result set's detail row.
	 *
	 * @return The result metadata of a detail row.
	 */
	IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Moves down one element from its current position of the iterator.
	 *
	 * @return true if next element exists and has not reached the limit on the
	 *         maximum number of rows that can be accessed.
	 * @throws BirtException if error occurs
	 */
	boolean next() throws BirtException;

	/**
	 * Returns the value of a column.
	 *
	 * @param columnName the name of the column
	 * @return The value of the given column. It could be null.
	 * @throws BirtException if error occurs
	 */
	Object getValue(String columnName) throws BirtException;

	/**
	 * @param index column index. It is 1 based
	 * @throws BirtException if error occurs
	 */
	Object getValue(int index) throws BirtException;

	/**
	 * Closes this result and provide a hint that the consumer is done with this
	 * result, whose resources can be safely released as appropriate.
	 */
	void close();

	/**
	 * see whether this iterator is empty
	 *
	 * @return true if this iterator is false; otherwise, return false
	 * @throws BirtException
	 */
	boolean isEmpty() throws BirtException;

	/**
	 * return the IResultIterator directly
	 *
	 * @return the IResultIterator
	 */
	IResultIterator getResultIterator();
}
