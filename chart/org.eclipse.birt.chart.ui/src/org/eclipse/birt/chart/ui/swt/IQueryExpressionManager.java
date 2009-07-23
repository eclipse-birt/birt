/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.model.data.Query;


/**
 * The interface is used for subclass to manipulate query expression for drag&drop and others operation.
 * @since 2.3
 */
public interface IQueryExpressionManager
{
	/**
	 * Return query object.
	 * @return
	 */
	public Query getQuery();
	
	/**
	 * Update query with specified expression.
	 * 
	 * @param expression
	 */
	public void updateQuery( String expression );
	
	/**
	 * Returns display expression.
	 */
	public String getDisplayExpression();
	
	/**
	 * Check if expression is valid to current query.
	 * 
	 * @param expression
	 * @return
	 */
	public boolean isValidExpression( String expression );

	/**
	 * Set tooltip for input control.
	 * 
	 * @since 2.5
	 */
	public void setTooltipForInputControl( );
}
