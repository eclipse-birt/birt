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

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Implements of DataBinding.
 * 
 */

public class DataBindingImpl implements IDataBinding
{

	private ComputedColumnHandle column;

	private ReportItemHandle handle;

	/**
	 * Constructor
	 * 
	 * @param column
	 * @param handle
	 */

	public DataBindingImpl( ComputedColumnHandle column, ReportItemHandle handle )
	{
		this.column = column;
		this.handle = handle;
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

	public void setAggregateOn( String on ) throws ScriptException
	{
		checkHandle( );
		column.setAggregateOn( on );
	}

	public void setDataType( String dataType ) throws ScriptException
	{
		checkHandle( );
		try
		{
			column.setDataType( dataType );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public void setExpression( String expression ) throws ScriptException
	{
		//expression is required.
		
		checkHandle( );
		try
		{
			column.setExpression( expression );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public void setName( String name ) throws ScriptException
	{
		//name is required.
		
		checkHandle( );
		try
		{
			column.setName( name );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	private void checkHandle( ) throws ScriptException
	{
		if ( column != null )
			return;
		
		throw new ScriptException( "ComputedColumnHandle is null" ); //$NON-NLS-1$
	
	}

}
