/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl.document.util;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;

/**
 * Read the expression value from report document.
 */
public interface IExprResultSet {

	/**
	 * @return
	 * @throws DataException
	 */
	boolean next() throws DataException;

	/**
	 * @param name
	 * @return
	 * @throws DataException
	 */
	Object getValue(String name) throws DataException;

	/**
	 * @param rowIndex
	 */
	void moveTo(int rowIndex) throws DataException;

	/**
	 * @return
	 */
	int getCurrentId();

	/**
	 * @return
	 */
	int getCurrentIndex();

	/**
	 * @return
	 * @throws DataException
	 */
	int getStartingGroupLevel() throws DataException;

	/**
	 * @return
	 * @throws DataException
	 */
	int getEndingGroupLevel() throws DataException;

	/**
	 * @param groupLevel
	 * @throws DataException
	 */
	void skipToEnd(int groupLevel) throws DataException;

	/**
	 * @throws DataException
	 */
	void close() throws DataException;

	/**
	 *
	 * @throws DataException
	 */
	boolean isEmpty();

	/**
	 * Get the group starting and ending index + 1.
	 *
	 * @param groupIndex
	 * @return
	 */
	int[] getGroupStartAndEndIndex(int groupIndex) throws DataException;

	/**
	 * Get the data set result set enclosed in the IExprResultSet instance.
	 *
	 * @return
	 */
	IDataSetResultSet getDataSetResultSet();

	List[] getGroupInfos() throws DataException;
}
