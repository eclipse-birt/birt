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

/**
 * Describes a data row filter defined in a data set or a report query. A filter
 * is defined as an expression that returns a Boolean type. The expression
 * normally operates on the "row" Javascript object to apply conditions based on
 * column values of a data row.
 */
public interface IFilterDefinition {

	public enum FilterTarget {
		DATASET, RESULTSET
	}

	/**
	 * Gets the Boolean expression used to define this filter.
	 */
	IBaseExpression getExpression();

	/**
	 * Indicate whether the aggreation will be recalculated after this filter has
	 * been applied.
	 * 
	 * @return <code>true</code> if the aggregation values should be updated prior
	 *         to apply this filter; Otherwise, return <code>false</code>.
	 */
	boolean updateAggregation();

	/**
	 * Set update aggregation flag.
	 * <p>
	 * While the flag is <code>true</code>, the aggregation values are updated prior
	 * to apply this filter; Otherwise the aggregation values are not updated.
	 * 
	 * @param update
	 */
	public void setUpdateAggregation(boolean flag);

	/**
	 * get the filter target
	 * 
	 * @return
	 */
	public FilterTarget getFilterTarget();

	/**
	 * set filter target
	 * 
	 * @param filterTarget
	 */
	public void setFilterTarget(FilterTarget filterTarget);
}
