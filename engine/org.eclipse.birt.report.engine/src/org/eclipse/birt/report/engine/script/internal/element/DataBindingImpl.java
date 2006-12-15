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

/**
 * Implements of DataBinding.
 * 
 */

public class DataBindingImpl implements IDataBinding
{

	private ComputedColumn column;
	
	/**
	 * Constructor
	 * 
	 * @param columnHandle
	 */

	public DataBindingImpl( )
	{
		column = createComputedColumn( );
	}

	/**
	 * Constructor
	 * 
	 * @param columnHandle
	 */

	public DataBindingImpl( ComputedColumnHandle columnHandle )
	{
		if ( columnHandle == null )
		{
			column = createComputedColumn( );
		}
		else
		{
			column = (ComputedColumn) columnHandle.getStructure( );
		}
	}

	/**
	 * Constructor
	 * 
	 * @param column
	 */

	public DataBindingImpl( ComputedColumn column )
	{
		if ( column == null )
		{
			column = createComputedColumn( );
		}
		else
		{

			this.column = column;
		}
	}

	/**
	 * Create computed column.
	 * 
	 * @return instance of <code>ComputedColumn</code>
	 */
	private ComputedColumn createComputedColumn( )
	{
		ComputedColumn c = new ComputedColumn( );
		return c;
	}

	public String getAggregateOn( )
	{
		return column.getAggregateOn( );
	}

	public String getDataType( )
	{
		return column.getDataType( );
	}

	public String getExpression( )
	{
		return column.getExpression( );
	}

	public String getName( )
	{
		return column.getName( );
	}

	public void setAggregateOn( String on )
	{
		column.setAggregateOn( on );
	}

	public void setDataType( String dataType )
	{
		column.setDataType( dataType );
	}

	public void setExpression( String expression )
	{
		// expression is required.
		column.setExpression( expression );
	}

	public void setName( String name )
	{
		// name is required.
		column.setName( name );
	}

	public IStructure getStructure( )
	{
		return column;
	}

}
