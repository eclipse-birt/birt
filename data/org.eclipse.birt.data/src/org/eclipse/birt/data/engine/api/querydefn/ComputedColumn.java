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

import org.eclipse.birt.data.engine.api.IComputedColumn;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IComputedColumn} interface.<p>
 */
public class ComputedColumn implements IComputedColumn
{
    protected String	name;
    protected String	expr;
    
    /**
     * Constructs a new computed column with specified name and expression
     * @param name Name of computed column
     * @param expr Expression of computed column
     */
    public ComputedColumn( String name, String expr )
    {
        this.name = name;
        this.expr = expr;
    }
    
    /**
     * Gets the name of the computed column
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the expression of the computed column
     */
    public String getExpression()
    {
        return expr;
    }
}
