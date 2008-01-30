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

import java.util.List;

import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement;

/**
 * 
 */

public class FilterConditionElement extends DesignElement
		implements
			IFilterConditionElement
{

	/**
	 * Default constructor.
	 * 
	 * @param handle
	 */

	public FilterConditionElement( FilterConditionElementHandle handle )
	{
		super( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#getExpr()
	 */
	public String getExpr( )
	{
		return ( (FilterConditionElementHandle) handle ).getExpr( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#getFilterTarget()
	 */

	public String getFilterTarget( )
	{
		return ( (FilterConditionElementHandle) handle ).getFilterTarget( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#getOperator()
	 */
	public String getOperator( )
	{
		return ( (FilterConditionElementHandle) handle ).getOperator( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#getValue1List()
	 */
	public List getValue1List( )
	{
		return ( (FilterConditionElementHandle) handle ).getValue1List( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#getValue2()
	 */
	public String getValue2( )
	{
		return ( (FilterConditionElementHandle) handle ).getValue2( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#isOptional()
	 */
	public boolean isOptional( )
	{
		return ( (FilterConditionElementHandle) handle ).isOptional( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setExpr(java.lang.String)
	 */
	public void setExpr( String filterExpr ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle ).setExpr( filterExpr );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setFilterTarget(java.lang.String)
	 */
	public void setFilterTarget( String filterTarget ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle )
				.setFilterTarget( filterTarget );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setOperator(java.lang.String)
	 */
	public void setOperator( String operator ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle ).setOperator( operator );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setOptional(boolean)
	 */
	public void setOptional( boolean isOptional ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle ).setOptional( isOptional );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setValue1(java.util.List)
	 */

	public void setValue1( List value1List ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle ).setValue1( value1List );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement#setValue2(java.lang.String)
	 */
	public void setValue2( String value2Expr ) throws SemanticException
	{
		( (FilterConditionElementHandle) handle ).setValue2( value2Expr );

	}

}
