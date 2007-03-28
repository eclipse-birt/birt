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

import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IGroup;

public class Group extends DesignElement implements IGroup
{

	public Group( GroupHandle handle )
	{
		super( handle );
	}

	public String getKeyExpr( )
	{
		return ( (GroupHandle) handle ).getKeyExpr( );
	}

	public void setKeyExpr( String expr ) throws SemanticException
	{
		( (GroupHandle) handle ).setKeyExpr( expr );
	}

	public String getName( )
	{
		return ( (GroupHandle) handle ).getName( );
	}

	public void setName( String name ) throws SemanticException
	{
		( (GroupHandle) handle ).setName( name );
	}

	public String getIntervalBase( )
	{
		return ( (GroupHandle) handle ).getIntervalBase( );
	}

	public void setIntervalBase( String intervalBase ) throws SemanticException
	{
		( (GroupHandle) handle ).setIntervalBase( intervalBase );
	}

	public String getInterval( )
	{
		return ( (GroupHandle) handle ).getInterval( );
	}

	public void setInterval( String interval ) throws SemanticException
	{

		( (GroupHandle) handle ).setInterval( interval );
	}

	public double getIntervalRange( )
	{
		return ( (GroupHandle) handle ).getIntervalRange( );
	}

	public void setIntervalRange( double intervalRange )
			throws SemanticException
	{
		( (GroupHandle) handle ).setIntervalRange( intervalRange );
	}

	public String getSortDirection( )
	{
		return ( (GroupHandle) handle ).getSortDirection( );
	}

	public void setSortDirection( String direction ) throws SemanticException
	{

		( (GroupHandle) handle ).setSortDirection( direction );
	}

	public boolean hasHeader( )
	{
		return ( (GroupHandle) handle ).hasHeader( );
	}

	public boolean hasFooter( )
	{
		return ( (GroupHandle) handle ).hasFooter( );
	}

	public String getTocExpression( )
	{
		return ( (GroupHandle) handle ).getTocExpression( );
	}

	public void setTocExpression( String expression ) throws SemanticException
	{

		( (GroupHandle) handle ).setTocExpression( expression );
	}

	public String getSortType( )
	{
		return ( (GroupHandle) handle ).getSortType( );
	}

	public void setSortType( String sortType ) throws SemanticException
	{

		( (GroupHandle) handle ).setSortType( sortType );
	}

	/**
	 * Returns hide detail.
	 * 
	 * @return hide detail.
	 */

	public boolean getHideDetail( )
	{
		Boolean value = (Boolean) ( (GroupHandle) handle )
				.getProperty( GroupHandle.HIDE_DETAIL_PROP );
		if ( value == null )
			return false;
		return value.booleanValue( );
	}

	/**
	 * Sets hide detail
	 * 
	 * @param hideDetail
	 *            hide detail
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setHideDetail( boolean hideDetail ) throws SemanticException
	{
		( (GroupHandle) handle ).setHideDetail( hideDetail );

	}

}
