/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of DataBinding.
 */

public class DataBindingImpl implements IDataBinding
{

    private org.eclipse.birt.report.model.api.simpleapi.IDataBinding dataBindingImpl;

    /**
     * Constructor
     * 
     * @param columnHandle
     */

    public DataBindingImpl()
    {
        dataBindingImpl = SimpleElementFactory.getInstance()
                .createDataBinding();
    }

    /**
     * Constructor
     * 
     * @param columnHandle
     */

    public DataBindingImpl( ComputedColumnHandle columnHandle )
	{
        dataBindingImpl = SimpleElementFactory.getInstance().createDataBinding( columnHandle );
	}

    /**
     * Constructor
     * 
     * @param column
     */

    public DataBindingImpl( ComputedColumn column )
	{
        dataBindingImpl = SimpleElementFactory.getInstance().createDataBinding( column );
	}
    
    public DataBindingImpl( org.eclipse.birt.report.model.api.simpleapi.IDataBinding columnBindingImpl )
    {
        dataBindingImpl = columnBindingImpl;
    }

    public String getAggregateOn()
    {
        return dataBindingImpl.getAggregateOn();
    }

    public String getDataType()
    {
        return dataBindingImpl.getDataType();
    }

    public String getExpression()
    {
        return dataBindingImpl.getExpression();
    }

    public String getName()
    {
        return dataBindingImpl.getName();
    }

    public void setAggregateOn( String on )
    {
        dataBindingImpl.setAggregateOn( on );
    }

    public void setDataType( String dataType )
    {
        dataBindingImpl.setDataType( dataType );
    }

    public void setExpression( String expression )
    {
        // expression is required.
        dataBindingImpl.setExpression( expression );
    }

    public void setName( String name )
    {
        // name is required.
        dataBindingImpl.setName( name );
    }

    public IStructure getStructure()
    {
        return dataBindingImpl.getStructure();
    }

}
