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

import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Read the raw expression result from report document.
 */
interface IExprDataReader {

	/**
	 * @return the id of current row
	 */
	int getRowId();

	/**
	 * @return the index of current row
	 */
	int getRowIndex();

	/**
	 * @return the row count value
	 */
	int getCount();

	/**
	 * @return
	 * @throws DataException
	 */
	boolean next() throws DataException;

	/**
	 * Move the cursor forward
	 * 
	 * @param index
	 * @throws DataException
	 */
	void moveTo(int index) throws DataException;

	/**
	 * @return value map of current row
	 * @throws DataException
	 */
	Map getRowValue() throws DataException;

	/**
	 * close the reader
	 */
	void close();

}
