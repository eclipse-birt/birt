/*
 *************************************************************************
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
	String getName();

	/**
	 * Gets the expression of the computed column
	 */
	IBaseExpression getExpression();

	/**
	 * Gets the data type of the computed column.
	 *
	 * @return Data type as an integer.
	 */
	int getDataType();

	/**
	 * Return the aggregation function.
	 *
	 * @return
	 */
	String getAggregateFunction();

	/**
	 * Return the filter of this computed column if it is an aggregation.
	 *
	 * @return
	 */
	IScriptExpression getAggregateFilter();

	/**
	 * Return aggregation arguments of this computed column if it is an aggregation.
	 *
	 * @return
	 */
	List getAggregateArgument();
}
