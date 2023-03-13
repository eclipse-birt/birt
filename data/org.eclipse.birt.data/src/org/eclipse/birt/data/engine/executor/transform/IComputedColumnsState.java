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

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * The implementation of this interface serve as data communication tool between
 * CachedResultSet and IExpressionProcessor.
 */
public interface IComputedColumnsState {
	/**
	 * Whether the value of expression with given index is available or not.
	 *
	 * @param index the index of expression.
	 * @return
	 */
	boolean isValueAvailable(int index);

	/**
	 * TODO Remove it The name of ComputedColumn with given index.
	 *
	 * @param index
	 * @return
	 */
	String getName(int index);

	/**
	 * TODO Remove it The expression of ComputedColumn with given index.
	 *
	 * @param index
	 * @return
	 * @throws DataException
	 */
	IBaseExpression getExpression(int index) throws DataException;

	/**
	 * Mark the state of one ComputedColumn whose value is available
	 *
	 * @param index
	 */
	void setValueAvailable(int index);

	/**
	 * Give the number of ComputedColumns hosted in the instance.
	 *
	 * @return
	 */
	int getCount();

	/**
	 * Get ComputedColum with given index.
	 *
	 * @param index
	 * @return
	 */
	IComputedColumn getComputedColumn(int index);

	/**
	 * Set the model to be used by current computed columns
	 *
	 * @param status
	 */
	void setModel(int model);
}
