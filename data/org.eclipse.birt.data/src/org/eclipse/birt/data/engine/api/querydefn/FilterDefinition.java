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
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IFilterDefinition} interface.
 */
public class FilterDefinition implements IFilterDefinition {
	IBaseExpression expr;

	boolean updateAggr;

	private FilterTarget filterTarget;

	/**
	 * Constructs a new filter using the specified expression. The expression is
	 * expected to return a Boolean value at runtime to be used as the filtering
	 * criteria.
	 */
	public FilterDefinition(IBaseExpression filterExpr) {
		this.expr = filterExpr;
		this.updateAggr = true;
	}

	/**
	 * Constructs a new filter with filter expression and update option.
	 * 
	 * @param filterExpr Filter evaluate expression
	 * @param updateAggr While <code>true</code>, the aggregation values are updated
	 *                   prior to apply this filter; Otherwise the aggregation
	 *                   values are not updated.
	 */
	public FilterDefinition(IBaseExpression filterExpr, boolean updateAggr) {
		this.expr = filterExpr;
		this.updateAggr = updateAggr;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IFilterDefinition#getExpression()
	 */
	public IBaseExpression getExpression() {
		return expr;
	}

	/**
	 * Sets a new expression for the filter.
	 */
	public void setExpression(IBaseExpression filterExpr) {
		this.expr = filterExpr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IFilterDefinition#updateAggregation()
	 */
	public boolean updateAggregation() {
		return updateAggr;
	}

	/**
	 * Set update aggregation flag.
	 * <p>
	 * While the flag is <code>true</code>, the aggregation values are updated prior
	 * to apply this filter; Otherwise the aggregation values are not updated.
	 * 
	 * @param update
	 */
	public void setUpdateAggregation(boolean flag) {
		this.updateAggr = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IFilterDefinition#getFilterTarget()
	 */
	public FilterTarget getFilterTarget() {
		return filterTarget;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IFilterDefinition#setFilterTarget(org.
	 * eclipse.birt.data.engine.api.IFilterDefinition.FilterTarget)
	 */
	public void setFilterTarget(FilterTarget filterTarget) {
		this.filterTarget = filterTarget;
	}
}
