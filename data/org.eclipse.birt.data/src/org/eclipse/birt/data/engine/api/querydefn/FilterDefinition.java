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
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IFilterDefinition} interface.
 */
public class FilterDefinition implements IFilterDefinition
{
	IBaseExpression 	expr;

	/**
	 * Constructs a new filter using the specified expression. The expression is expected to 
	 * return a Boolean value at runtime to be used as the filtering criteria.
	 */
	public FilterDefinition( IBaseExpression filterExpr )
	{
		this.expr = filterExpr;
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.IFilterDefinition#getExpression()
	 */
	public IBaseExpression getExpression()
	{
		return expr;
	}
	
	/**
	 * Sets a new expression for the filter. 
	 */
	public void setExpression( IBaseExpression filterExpr )
	{
		this.expr = filterExpr;
	}
}
