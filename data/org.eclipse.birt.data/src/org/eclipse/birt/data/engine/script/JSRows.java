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

package org.eclipse.birt.data.engine.script;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Underlying implementation of the Javascript "rows" object. The ROM scripts use this 
 * JS object to access the array of row object 
 */
public class JSRows extends ScriptableObject 
{

	/**
	 * Comment for
	 * <code>rows: In the arraylist,the current 'row' Object's index is 0
	 * the index of the outer-most 'row' is ArrayList.size()-1 
	 * </code>
	 */
	 private ArrayList rows = new ArrayList( );

	/*
	 * return the Class Name
	 * 
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName( )
	{
		return "DataRows";
	}

	/**
	 * Construct the rows object from the outer query result and the row object of the current result. 
	 * @param outerResults the outer query result 
	 * @param currentRowObj the row object of the cuurent result 
	 * @throws DataException
	 */
	public JSRows( IQueryResults outerResults, JSRowObject currentRowObj ) throws DataException
	{
		try
		{
			if ( currentRowObj != null )
				rows.add( currentRowObj );
			if ( outerResults == null )
				return;
			IResultIterator resultIterator = outerResults.getResultIterator( );
			Scriptable scope = resultIterator.getScope( );
			JSRowObject rowobj;
	
			Object obj = scope.get( "rows", scope );
	
			if ( obj instanceof JSRows )
			{
				JSRows rowsobj = (JSRows) obj;
				for ( int i = 0; i < rowsobj.size( ); i++ )
				{
					rowobj = (JSRowObject) rowsobj.get( i, scope );
					rows.add( rowobj );
				}
			}
		}
		catch (BirtException e)
		{
			assert e instanceof DataException;
			throw (DataException)e;
		}
	}

	/**
	 * Gets an indexed Row Object
	 */
	public Object get( int index, Scriptable start )
	{
		if ( has( index, start ) )
		{
			return rows.get( rows.size( ) - 1 - index );
		}
		else
		{
			return null;
		}
	}

	/**
	 * Checks if an row Object exists
	 */
	public boolean has( int index, Scriptable start )
	{
		return ( rows.size( ) > index ) ? true : false;
	}

	/**
	 * Get the size of the 'rows' object
	 * @return size of the 'rows' object.
	 */
	public int size( )
	{
		return rows.size( );
	}
}