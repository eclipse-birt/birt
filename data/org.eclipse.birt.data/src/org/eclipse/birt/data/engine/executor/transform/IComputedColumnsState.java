/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public boolean isValueAvailable(int index);

	/**
	 * TODO Remove it The name of ComputedColumn with given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getName(int index);

	/**
	 * TODO Remove it The expression of ComputedColumn with given index.
	 * 
	 * @param index
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getExpression(int index) throws DataException;

	/**
	 * Mark the state of one ComputedColumn whose value is available
	 * 
	 * @param index
	 */
	public void setValueAvailable(int index);

	/**
	 * Give the number of ComputedColumns hosted in the instance.
	 * 
	 * @return
	 */
	public int getCount();

	/**
	 * Get ComputedColum with given index.
	 * 
	 * @param index
	 * @return
	 */
	public IComputedColumn getComputedColumn(int index);

	/**
	 * Set the model to be used by current computed columns
	 * 
	 * @param status
	 */
	public void setModel(int model);
}
