
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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;

/**
 * 
 */

public class Binding implements IBinding
{
	private List aggregateOn;
	private List argument;
	private String expr;
	private IBaseExpression filter;
	private String aggrFunc;
	private String name;
	private int dataType;
	
	public Binding( String name )
	{
		this.name = name;
		this.aggregateOn = new ArrayList();
		this.argument = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#addAggregateOn(java.lang.String)
	 */
	public void addAggregateOn( String levelName )
	{
		this.aggregateOn.add( levelName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#addArgument(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public void addArgument( IBaseExpression expr )
	{
		this.argument.add( expr );
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getAggrFunction()
	 */
	public String getAggrFunction( )
	{
		return aggrFunc;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getAggregatOns()
	 */
	public List getAggregatOns( )
	{
		return this.aggregateOn;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getArguments()
	 */
	public List getArguments( )
	{
		return this.argument;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getDataType()
	 */
	public int getDataType( )
	{
		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getExpression()
	 */
	public String getExpression( )
	{
		return expr;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getFilter()
	 */
	public IBaseExpression getFilter( )
	{
		return this.filter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#setAggrFunction(java.lang.String)
	 */
	public void setAggrFunction( String functionName )
	{
		this.aggrFunc = functionName;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#setDataType(int)
	 */
	public void setDataType( int type )
	{
		this.dataType = type;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#setExpression(java.lang.String)
	 */
	public void setExpression( String expr )
	{
		this.expr = expr;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#setFilter(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public void setFilter( IBaseExpression expr )
	{
		this.filter = expr;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBinding#getBindingName()
	 */
	public String getBindingName( )
	{
		return this.name;
	}

}
