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

import javax.olap.OLAPException;

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
	public boolean next() throws OLAPException;

	/**
	 * Move the cursor to the previous row in the result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean previous() throws OLAPException;

	/**
	 * Move the cursor to a relative number of rows, either positive or negative
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean relative(int arg0) throws OLAPException;

	/**
	 * Move the cursor to the first row in the result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean first() throws OLAPException;

	/**
	 * Move the cursor to the last row in the result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean last() throws OLAPException;

	/**
	 * Indicate whether the cursor is before the first row of result set
	 * 
	 * @return
	 */
	public boolean isBeforeFirst();

	/**
	 * Indicate whether the cursor is after the last row of result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isAfterLast() throws OLAPException;

	/**
	 * Indicate whether the cursor is the first row of result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isFirst() throws OLAPException;

	/**
	 * Indicate whether the cursor is the last row of result set
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isLast() throws OLAPException;

	/**
	 * Move the cursor to the end of result set, just after the last row
	 * 
	 * @throws OLAPException
	 */
	public void afterLast() throws OLAPException;

	/**
	 * Move the cursor before the first row of result set
	 * 
	 * @throws OLAPException
	 */
	public void beforeFirst() throws OLAPException;

	/**
	 * Move the cursor to the certain position
	 * 
	 * @param position
	 * @throws OLAPException
	 */
	public void setPosition(long position) throws OLAPException;

	/**
	 * Get the current position of cursor
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public long getPosition() throws OLAPException;

	/**
	 * Release the ResultSet Object's resources
	 * 
	 * @throws OLAPException
	 */
	public void close() throws OLAPException;;

	/**
	 * Get the number of values positional values based on cursor dependency
	 * 
	 * @return
	 */
	public long getExtend();

	/**
	 * Return the type of result set
	 * 
	 * @return
	 */
	public int getType();

	/**
	 * Get the OLAP Warnings
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public Collection getWarnings() throws OLAPException;

	/**
	 * Clear the warnings collection
	 * 
	 * @throws OLAPException
	 */
	public void clearWarnings() throws OLAPException;

	/**
	 * 
	 * @param position
	 */
	public void synchronizedPages(int position);

}
