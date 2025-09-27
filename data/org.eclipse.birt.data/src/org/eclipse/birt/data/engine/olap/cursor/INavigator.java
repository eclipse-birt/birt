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

package org.eclipse.birt.data.engine.olap.cursor;

import java.util.Collection;

import jakarta.olap.OLAPException;

/**
 * INavigator interface provide a cursor pointing to its current row of data.
 * Initially the cursor is positioned before the first row.next method could be
 * used in a while loop to iterator through the result set.
 *
 */
public interface INavigator {

	/**
	 * Move the cursor down one row from its current position. if it returns false,
	 * it means there are no more rows in INavigator Object
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean next() throws OLAPException;

	/**
	 * Move the cursor to the previous row in the result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean previous() throws OLAPException;

	/**
	 * Move the cursor to a relative number of rows, either positive or negative
	 *
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	boolean relative(int arg0) throws OLAPException;

	/**
	 * Move the cursor to the first row in the result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean first() throws OLAPException;

	/**
	 * Move the cursor to the last row in the result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean last() throws OLAPException;

	/**
	 * Indicate whether the cursor is before the first row of result set
	 *
	 * @return
	 */
	boolean isBeforeFirst();

	/**
	 * Indicate whether the cursor is after the last row of result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean isAfterLast() throws OLAPException;

	/**
	 * Indicate whether the cursor is the first row of result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean isFirst() throws OLAPException;

	/**
	 * Indicate whether the cursor is the last row of result set
	 *
	 * @return
	 * @throws OLAPException
	 */
	boolean isLast() throws OLAPException;

	/**
	 * Move the cursor to the end of result set, just after the last row
	 *
	 * @throws OLAPException
	 */
	void afterLast() throws OLAPException;

	/**
	 * Move the cursor before the first row of result set
	 *
	 * @throws OLAPException
	 */
	void beforeFirst() throws OLAPException;

	/**
	 * Move the cursor to the certain position
	 *
	 * @param position
	 * @throws OLAPException
	 */
	void setPosition(long position) throws OLAPException;

	/**
	 * Get the current position of cursor
	 *
	 * @return
	 * @throws OLAPException
	 */
	long getPosition() throws OLAPException;

	/**
	 * Release the ResultSet Object's resources
	 *
	 * @throws OLAPException
	 */
	void close() throws OLAPException;

	/**
	 * Get the number of values positional values based on cursor dependency
	 *
	 * @return
	 */
	long getExtend();

	/**
	 * Return the type of result set
	 *
	 * @return
	 */
	int getType();

	/**
	 * Get the OLAP Warnings
	 *
	 * @return
	 * @throws OLAPException
	 */
	Collection getWarnings() throws OLAPException;

	/**
	 * Clear the warnings collection
	 *
	 * @throws OLAPException
	 */
	void clearWarnings() throws OLAPException;

	/**
	 *
	 * @param position
	 */
	void synchronizedPages(int position);

}
