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

import org.eclipse.birt.data.engine.api.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * Default implementation of the IBaseExpression interface.
 */
public abstract class BaseExpression implements IBaseExpression
{
    protected int			dataType;
    protected Object		handle;

    public BaseExpression( )
    {
    	this.dataType = DataType.UNKNOWN_TYPE;
    }
    
    public BaseExpression( int dataType )
    {
    	this.dataType = dataType;
    }
    
	/* (non-Javadoc)
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getHandle()
	 */
	public Object getHandle()
	{
		return this.handle;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setHandle(java.lang.Object)
	 */
	public void setHandle(Object handle)
	{
		this.handle = handle;
	}

}
