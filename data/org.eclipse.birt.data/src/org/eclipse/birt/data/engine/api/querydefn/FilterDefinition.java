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
 * 
 */
public class FilterDefinition implements IFilterDefinition
{
	IBaseExpression 	expr;

	public FilterDefinition( IBaseExpression filterExpr )
	{
		this.expr = filterExpr;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IFilterDefn#getExpression()
	 */
	public IBaseExpression getExpression()
	{
		return expr;
	}
	
	public void setExpression( IBaseExpression filterExpr )
	{
		this.expr = filterExpr;
	}
}
