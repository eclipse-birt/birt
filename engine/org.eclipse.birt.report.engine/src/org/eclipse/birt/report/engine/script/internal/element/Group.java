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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IGroup;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class Group extends ReportElement implements IGroup
{

	public Group( GroupHandle group )
	{
		super( group );
	}

	public String getKeyExpr( )
	{
		return ( (GroupHandle) handle ).getKeyExpr( );
	}

	public void setKeyExpr( String expr ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setKeyExpr( expr );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getName( )
	{
		return ( (GroupHandle) handle ).getName( );
	}

	public void setName( String name )
	{
		( (GroupHandle) handle ).setName( name );
	}

	public String getIntervalBase( )
	{
		return ( (GroupHandle) handle ).getIntervalBase( );
	}

	public void setIntervalBase( String intervalBase ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setIntervalBase( intervalBase );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getInterval( )
	{
		return ( (GroupHandle) handle ).getInterval( );
	}

	public void setInterval( String interval ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setInterval( interval );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public double getIntervalRange( )
	{
		return ( (GroupHandle) handle ).getIntervalRange( );
	}

	public void setIntervalRange( double intervalRange ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setIntervalRange( intervalRange );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getSortDirection( )
	{
		return ( (GroupHandle) handle ).getSortDirection( );
	}

	public void setSortDirection( String direction ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setSortDirection( direction );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
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

	public void setTocExpression( String expression ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setTocExpression( expression );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getSortType( )
	{
		return ( (GroupHandle) handle ).getSortType( );
	}

	public void setSortType( String sortType ) throws ScriptException
	{
		try
		{
			( (GroupHandle) handle ).setSortType( sortType );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}
}
