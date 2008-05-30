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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;

/**
 * Implements of DataBinding.
 * 
 */

public class DataBindingImpl extends Structure implements IDataBinding
{

	private ComputedColumn column;

	/**
	 * Constructor
	 * 
	 * @param columnHandle
	 */

	public DataBindingImpl( )
	{
		super( null );
		column = createComputedColumn( );
	}

	/**
	 * Constructor
	 * 
	 * @param columnHandle
	 */

	public DataBindingImpl( ComputedColumnHandle columnHandle )
	{
		super( columnHandle );
		if ( columnHandle == null )
		{
			column = createComputedColumn( );
		}
		else
		{
			structureHandle = columnHandle;

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
		super( null );
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

	public void setAggregateOn( String on ) throws SemanticException
	{
		// special case.
		if ( structureHandle != null )
		{

			( (ComputedColumnHandle) structureHandle ).setAggregateOn( on );
			return;
		}
		column.setAggregateOn( on );
	}

	public void setDataType( String dataType ) throws SemanticException
	{
		if ( structureHandle != null )
		{
			setProperty( ComputedColumn.DATA_TYPE_MEMBER, dataType );
			return;
		}

		column.setDataType( dataType );
	}

	public void setExpression( String expression ) throws SemanticException
	{
		if ( structureHandle != null )
		{
			setProperty( ComputedColumn.EXPRESSION_MEMBER, expression );
			return;
		}

		// expression is required.
		column.setExpression( expression );
	}

	public void setName( String name ) throws SemanticException
	{
		if ( structureHandle != null )
		{
			setProperty( ComputedColumn.NAME_MEMBER, name );
			return;
		}

		// name is required.
		column.setName( name );
	}

	public IStructure getStructure( )
	{
		return column;
	}

}
