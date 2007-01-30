/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;

/**
 * Represents the scriptable object for Java object which implements the
 * interface <code>Map</code>.
 * 
 */
public class NativeRowObject implements Scriptable
{

	ExecutionContext context;
	Scriptable prototype;
	Scriptable parent;
	IResultSet rset;

	static final String JS_CLASS_NAME = "DataSetRow";

	public String getClassName( )
	{
		return JS_CLASS_NAME;
	}

	public NativeRowObject( )
	{
	}

	public NativeRowObject( Scriptable parent, ExecutionContext context )
	{
		setParentScope( parent );
		this.context = context;
	}

	public NativeRowObject( Scriptable parent, IResultSet rset )
	{
		setParentScope( parent );
		this.rset = rset;
	}

	protected IResultSet getResultSet()
	{
		if (rset == null)
		{
			return context.getResultSet( );
		}
		return rset;
	}
	
	public Object get( String name, Scriptable start )
	{
		IResultSet rset = getResultSet( );
		if ( rset == null )
		{
			return null;
		}

		if ( "_outer".equals( name ) )
		{
			IResultSet parent = rset.getParent( );
			if ( parent != null )
			{
				return new NativeRowObject( start, parent );
			}
			return null;
		}
		if ( "__rownum".equals( name ) )
		{
			return new Long( rset.getCurrentPosition( ) );
		}
		try
		{
			return rset.getValue( name );
		}
		catch ( BirtException ex )
		{
			throw new EvaluatorException( ex.toString( ) );
		}
	}

	public Object get( int index, Scriptable start )
	{
		if ( index == 0 )
		{
			return get( "__rownum", start );
		}
		return get( String.valueOf( index ), start );

	}

	public boolean has( String name, Scriptable start )
	{
		IResultSet rset = getResultSet( );
		if ( rset == null )
		{
			return false;
		}

		try
		{
			IResultMetaData metaData = rset.getResultMetaData( );
			for ( int i = 0; i < metaData.getColumnCount( ); i++ )
			{
				String colName = metaData.getColumnName( i );
				if ( colName.equals( name ) )
				{
					return true;
				}
			}
		}
		catch ( BirtException ex )
		{
			// not exist
		}
		return false;
	}

	public boolean has( int index, Scriptable start )
	{
		return false;
	}

	public void put( String name, Scriptable start, Object value )
	{
	}

	public void put( int index, Scriptable start, Object value )
	{
	}

	public void delete( String name )
	{
	}

	public void delete( int index )
	{
	}

	public Scriptable getPrototype( )
	{
		return prototype;
	}

	public void setPrototype( Scriptable prototype )
	{
		this.prototype = prototype;
	}

	public Scriptable getParentScope( )
	{
		return parent;
	}

	public void setParentScope( Scriptable parent )
	{
		this.parent = parent;
	}

	public Object[] getIds( )
	{
		IResultSet rset = getResultSet( );
		if ( rset == null )
		{
			return null;
		}
		try
		{
			IResultMetaData metaData = rset.getResultMetaData( );
			Object[] names = new Object[metaData.getColumnCount( )];
			for ( int i = 0; i < names.length; i++ )
			{
				names[i] = metaData.getColumnName( i );
			}
			return names;
		}
		catch ( BirtException ex )
		{
		}
		return null;
	}

	public Object getDefaultValue( Class hint )
	{
		return null;
	}

	public boolean hasInstance( Scriptable instance )
	{
		return false;
	}
}
