/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.util.List;

/**
 * Describes a computed column defined for a data set, or a report query. A
 * computed column has a name, and an JavaScript expression used to caculate
 * value of the column.
 */
public interface IComputedColumn {
	/**
	 * Gets the name of the computed column
	 */
	public abstract String getName();

	/**
	 * Gets the expression of the computed column
	 */
	public abstract IBaseExpression getExpression();

	/**
	 * Gets the data type of the computed column.
	 * 
	 * @return Data type as an integer.
	 */
	public abstract int getDataType();

	/**
	 * Return the aggregation function.
	 * 
	 * @return
	 */
	public abstract String getAggregateFunction();

	/**
	 * Return the filter of this computed column if it is an aggregation.
	 * 
	 * @return
	 */
	public abstract IScriptExpression getAggregateFilter();

	/**
	 * Return aggregation arguments of this computed column if it is an aggregation.
	 * 
	 * @return
	 */
	public abstract List getAggregateArgument();
}