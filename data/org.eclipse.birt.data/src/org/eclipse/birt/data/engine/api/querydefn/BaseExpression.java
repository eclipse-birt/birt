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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * Default implementation of the {@link org.eclipse.birt.data.engine.api.IBaseExpression} interface.
 */
public abstract class BaseExpression implements IBaseExpression
{
	private String exprID;
		
    protected int			dataType;
    protected Object		handle;
    private String groupName = GROUP_OVERALL;
    
    /**
     * Constructs an instance with unknown data type
     */
    public BaseExpression( )
    {
    	this.dataType = DataType.UNKNOWN_TYPE;
    }
    
    /**
     * Constructs an instance with specified data type
     */
    public BaseExpression( int dataType )
    {
    	this.dataType = dataType;
    }
    
    /*
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getID()
	 */
	public String getID( )
	{
		return this.exprID;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setID(java.lang.String)
	 */
	public void setID( String exprID )
	{
		this.exprID = exprID;
	}
    
	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getDataType()
	 */
	public int getDataType()
	{
		return this.dataType;
	}

	/**
	 * Sets the data type of the expression
	 */
	public void setDataType( int dataType )
	{
		this.dataType = dataType;
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getHandle()
	 */
	public Object getHandle()
	{
		return this.handle;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setHandle(java.lang.Object)
	 */
	public void setHandle(Object handle)
	{
		this.handle = handle;
	}

	public void setGroupName( String name )
	{
		if( name!= null )
			this.groupName = name;
		else
			this.groupName = GROUP_OVERALL;
	}
	
	public String getGroupName( )
	{
		return this.groupName;
	}
}
