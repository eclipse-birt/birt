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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IFilterDefinition;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IBaseTransform} interface.
 *
 */
abstract public class BaseTransform implements IBaseTransform {
	// Enumeration constants for expressionTiming
	/**
	 * The expression is evaluated before the first row in the series. A constant
	 * for the expressionTiming parameter of addExpression.
	 */
	public static final int BEFORE_FIRST_ROW = 0;
	/**
	 * The expression is evaluated after the last row in the series. A constant for
	 * the expressionTiming parameter of addExpression.
	 */
	public static final int AFTER_LAST_ROW = 1;
	/**
	 * The expression is evaluated on each row. A constant for the expressionTiming
	 * parameter of addExpression.
	 */
	public static final int ON_EACH_ROW = 2;

	protected List filters = new ArrayList();
	protected List subqueries = new ArrayList();
	protected List sorts = new ArrayList();
	protected List rowExpressions = new ArrayList();
	protected List beforeExpressions = new ArrayList();
	protected List afterExpressions = new ArrayList();

	/**
	 * Returns the filters defined in this transform, as an ordered list of
	 * <code>IFilterDefintion</code> objects.
	 * 
	 * @return the filters. null if no filter is defined.
	 */
	public List getFilters() {
		return filters;
	}

	/**
	 * Add one filter to the filter list
	 */
	public void addFilter(IFilterDefinition filter) {
		filters.add(filter);
	}

	/**
	 * Returns an unordered collection of subqueries that are alternative views of
	 * the result set for this transform. Objects are of type
	 * <code>SubqueryDefinition</code>.
	 * 
	 * @return the subqueries for this transform
	 */

	public Collection getSubqueries() {
		return subqueries;
	}

	/**
	 * Add a subquery to the list
	 * 
	 * @param subquery one subquery to add to the subquery set
	 */
	public void addSubquery(SubqueryDefinition subquery) {
		subqueries.add(subquery);
	}

	/**
	 * Returns the sort criteria as an ordered list of <code>SortDefinition</code>
	 * objects.
	 * 
	 * @return the sort criteria
	 */

	public List getSorts() {
		return sorts;
	}

	/**
	 * Appends one sort definition to the list of sort criteria
	 */
	public void addSort(SortDefinition sort) {
		sorts.add(sort);
	}
}
