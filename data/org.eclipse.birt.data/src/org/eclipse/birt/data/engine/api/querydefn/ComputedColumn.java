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
import org.eclipse.birt.data.engine.api.IComputedColumn;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IComputedColumn} interface.<p>
 */
public class ComputedColumn implements IComputedColumn
{
    protected String name;
	protected IBaseExpression expr;
	protected int dataType;
    
    /**
     * Constructs a new computed column with specified name and expression
     * @param name Name of computed column
     * @param expr Expression of computed column
     * @param dataType data Type of computed column
     */
    public ComputedColumn( String name, String expr, int dataType )
	{
		this.name = name;
		this.expr = new ScriptExpression(expr);
		this.dataType = dataType;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IComputedColumn#getName()
     */
    public String getName()
    {
        return name;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IComputedColumn#getExpression()
     */
    public IBaseExpression getExpression()
    {
        return expr;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IComputedColumn#getDataType()
     */
	public int getDataType( )
	{
		return this.dataType;
	}
}
