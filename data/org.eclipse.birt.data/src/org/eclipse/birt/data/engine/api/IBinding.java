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

package org.eclipse.birt.data.engine.api;

import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This interface define a BIRT query binding. The binding defined the script
 * that to be used to calculate the specific data.
 */
public interface IBinding {
	/**
	 * Return the name of the binding.
	 * 
	 * @return
	 * @throws DataException
	 */
	public String getBindingName() throws DataException;

	/**
	 * Return the target data type of the binding.
	 * 
	 * @return
	 * @throws DataException
	 */
	public int getDataType() throws DataException;

	/**
	 * Set the binding data type.
	 * 
	 * @param type
	 * @throws DataException
	 */
	public void setDataType(int type) throws DataException;

	/**
	 * Get the expression of the binding.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getExpression() throws DataException;

	/**
	 * Set the expression of the binding.
	 * 
	 * @param expr
	 * @throws DataException
	 */
	public void setExpression(IBaseExpression expr) throws DataException;

	/**
	 * Get the aggregate target group(s) of the binding. When doing tabular query,
	 * the target group name of the aggregation is returned. When doing cube query,
	 * the target column/row/page dimension level is returned.
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getAggregatOns() throws DataException;

	/**
	 * Add aggregate on to the binding.
	 * 
	 * @param levelName
	 * @throws DataException
	 */
	public void addAggregateOn(String levelName) throws DataException;

	/**
	 * Get the arguments of the binding. This is only used when the binding is an
	 * aggregate binding.
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getArguments() throws DataException;

	/**
	 * Imply whether the data of this binding should be candidate for data
	 * exportation.
	 * 
	 * @return
	 * @throws DataException
	 */
	public boolean exportable() throws DataException;

	/**
	 * Set whether the binding is exportable.
	 * 
	 * @param exportable
	 * @throws DataException
	 */
	public void setExportable(boolean exportable) throws DataException;

	/**
	 * 
	 * @param expr
	 * @throws DataException
	 * @deprecated
	 */
	public void addArgument(IBaseExpression expr) throws DataException;

	/**
	 * Add a binding argument.
	 * 
	 * @param name
	 * @param expr
	 * @throws DataException
	 */
	public void addArgument(String name, IBaseExpression expr) throws DataException;

	/**
	 * Add a binding aggregation filter.
	 * 
	 * @param expr
	 * @throws DataException
	 */
	public void setFilter(IBaseExpression expr) throws DataException;

	/**
	 * Return the binding aggregation filter.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getFilter() throws DataException;

	/**
	 * Return the binding aggregation function.
	 * 
	 * @return
	 * @throws DataException
	 */
	public String getAggrFunction() throws DataException;

	/**
	 * Set the binding aggregation function.
	 * 
	 * @param functionName
	 * @throws DataException
	 */
	public void setAggrFunction(String functionName) throws DataException;

	/**
	 * Return the display name of the binding.
	 * 
	 * @return
	 * @throws DataException
	 */
	public String getDisplayName() throws DataException;

	/**
	 * Set the display name of the binding.
	 * 
	 */
	public void setDisplayName(String displayName) throws DataException;

	/**
	 * Set the time function name if used
	 * 
	 * @param timeFunction
	 */
	public void setTimeFunction(ITimeFunction timeFunction);

	/**
	 * 
	 * @return the time function used in binding
	 */
	public ITimeFunction getTimeFunction();
}
