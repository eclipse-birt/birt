/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements a Javascript array of ColumnDefn objects which wraps around an odi 
 * IResultClass
 */
public class JSColumnMetaData extends ScriptableObject
{
	private IResultClass resultClass;
	private Scriptable[] cachedColumns;
	
	public JSColumnMetaData( IResultClass resultClass )
	{
		this.resultClass = resultClass;
		cachedColumns = new Scriptable[resultClass.getFieldCount()];
		
		// This object is not modifiable in any way
		sealObject();
	}
	
	public IResultClass getResultClass()
	{
		return resultClass;
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName()
	{
		return "Array";
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#get(int, org.mozilla.javascript.Scriptable)
	 */
	public Object get(int index, Scriptable start)
	{
		// Our index is 1-based
		if ( index > 0 && index <= resultClass.getFieldCount() )
		{
			// JSColumnDefn objects are created only on demand
			if ( cachedColumns[index-1] == null )
				cachedColumns[index-1] = new JSColumnDefn( resultClass, index );
			return cachedColumns[index-1];
		}
		return super.get(index, start);
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start)
	{
		int index = resultClass.getFieldIndex(name);
		if ( index <= 0 )
			// Not the name of a field
			return super.get(name, start);
		
		return get( index, start);
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds()
	{
		// Each field can be accessec by name or index
		Object[] ids = new Object[ 2 * resultClass.getFieldCount() ];
		
		for ( int i = 0; i < resultClass.getFieldCount(); i ++ )
		{
			try
			{
				ids[ 2 * i ] = resultClass.getFieldName( i + 1 );
			}
			catch (DataException e)
			{
				// Should not get here. TODO: log exception
				e.printStackTrace();
			}
			ids[ 2* i + 1] = new Integer(i + 1);
		}
		
		return ids;
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(int, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(int index, Scriptable start)
	{
		if ( index >0 && index <= resultClass.getFieldCount() )
			return true;
		return super.has( index, start );
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(String name, Scriptable start)
	{
		if ( resultClass.getFieldIndex( name) > 0 )
			return true;
		return super.has( name, start);
	}
}
