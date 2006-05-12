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

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;

/**
 * Represents the scriptable object for Java object which implements the
 * interface <code>Map</code>.
 * 
 * @version $Revision: 1.4 $ $Date: 2006/04/21 07:13:02 $
 */
public class NativeRowObject implements Scriptable
{

	Scriptable prototype;
	Scriptable parent;
	LinkedList rsets;

	static final String JS_CLASS_NAME = "DataSetRow";

	public String getClassName( )
	{
		return JS_CLASS_NAME;
	}

	public NativeRowObject( )
	{
	}

	public NativeRowObject( Scriptable parent, LinkedList rsets )
	{
		setParentScope( parent );
		this.rsets = rsets;
	}

	public Object get( String name, Scriptable start )
	{
		if ( "_outer".equals( name ) )
		{
			LinkedList outRsets = new LinkedList( );
			outRsets.addAll( rsets );
			if ( outRsets != null )
			{
				outRsets.removeFirst( );
			}
			return new NativeRowObject( start, outRsets );
		}
		Iterator iter = rsets.iterator( );
		if ( "__rownum".equals( name ) )
		{
			if ( iter.hasNext( ) )
			{
				IResultSet rset = (IResultSet) iter.next( );
				return new Long( rset.getCurrentPosition( ) );
			}
		}
		else
		{
			while ( iter.hasNext( ) )
			{
				IResultSet rset = (IResultSet) iter.next( );
				try
				{
					return rset.getValue( name );
				}
				catch ( BirtException ex )
				{
				}
			}
		}
		throw new EvaluatorException("Can't find the column: " + name);
	}

	public Object get( int index, Scriptable start )
	{
		if ( index == 0 )
		{
			return get( "__rownum",start );
		}
		return get( String.valueOf( index ), start );
		
		/*
		if ( !rsets.isEmpty( ) )
		{
			IResultSet rset = (IResultSet) rsets.getFirst( );
			try
			{
				IResultMetaData metaData = rset.getResultMetaData( );
				if ( index >= 0 && index < metaData.getColumnCount( ) )
				{
					String name = metaData.getColumnName( index );
					return rset.getValue( name );
				}
			}
			catch ( BirtException ex )
			{
			}
		}

		return NOT_FOUND;
		*/
	}

	public boolean has( String name, Scriptable start )
	{
		Iterator iter = rsets.iterator( );
		while ( iter.hasNext( ) )
		{
			IResultSet rset = (IResultSet) iter.next( );
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
		if ( !rsets.isEmpty( ) )
		{
			IResultSet rset = (IResultSet) rsets.getFirst( );
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
