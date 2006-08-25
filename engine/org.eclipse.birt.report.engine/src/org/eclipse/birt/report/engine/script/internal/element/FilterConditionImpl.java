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
import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Implements of FilterCondition.
 * 
 */

public class FilterConditionImpl implements IFilterCondition
{

	private FilterConditionHandle condition;

	private DesignElementHandle handle;

	public FilterConditionImpl( FilterConditionHandle condition,
			DesignElementHandle handle )
	{
		this.condition = condition;
		this.handle = handle;
	}

	public String getOperator( )
	{
		return condition.getOperator( );
	}

	public String getValue1( )
	{
		return condition.getValue1( );
	}

	public String getValue2( )
	{
		return condition.getValue2( );
	}

	public void setOperator( String operator ) throws ScriptException
	{
		checkHandle( );
		try
		{
			condition.setOperator( operator );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public void setValue1( String value1 ) throws ScriptException
	{
		checkHandle( );
		condition.setValue1( value1 );
	}

	public void setValue2( String value2 ) throws ScriptException
	{
		checkHandle( );
		condition.setValue2( value2 );
	}

	private void checkHandle( ) throws ScriptException
	{
		if ( condition != null )
			return;
		
		throw new ScriptException( "FilterConditionHandle is null" ); //$NON-NLS-1$
		
	}

}
