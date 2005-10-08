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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JS object for Output parameter of prepared query.
 */
public class JSOutputParams extends ScriptableObject
{
	// JS class name
	final private static String className = JSOutputParams.class.getName( );

	// map to cache the parmeter name and its value
	private Map valueMap = new HashMap( );

	// parameter value provider
	private IPreparedDSQuery preparedQuery;
	
	/**
	 * @param preparedQuery
	 */
	public void setPreparedQuery( IPreparedDSQuery preparedDSQuery )
	{
		assert preparedDSQuery != null;
		
		this.preparedQuery = preparedDSQuery;
	}
	
	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName( )
	{
		return className;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(int, org.mozilla.javascript.Scriptable)
	 */
	public Object get( int index, Scriptable scope )
	{
		// needs to log
		if ( preparedQuery == null )			
			return null;
		
		Object paramValue = null;
		
		if ( valueMap.containsKey( new Integer( index ) ) )
		{
			paramValue = valueMap.get( new Integer( index ) );
		}
		else
		{
			try
			{
				paramValue = preparedQuery.getOutputParameterValue( index );
				valueMap.put( new Integer( index ), paramValue );
			}
			catch ( DataException e )
			{
				// needs to log here. throw new IllegalArgumentException( e.getMessage( ) );
			}
		}

		return paramValue;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public Object get( String name, Scriptable scope )
	{
		// needs to log
		if ( preparedQuery == null )
			return null;
		
		Object paramValue = null;

		if ( valueMap.containsKey( name ))
		{
			paramValue = valueMap.get( name );
		}
		else
		{
			try
			{
				paramValue = preparedQuery.getOutputParameterValue( name );
				valueMap.put( name, paramValue );
			}
			catch ( DataException e )
			{
				// needs to log here. throw new IllegalArgumentException( e.getMessage( ) );
			}
		}

		return paramValue;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#put(int, org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put( int index, Scriptable scope, Object value )
	{
		throw new IllegalArgumentException( "Put value on output parameter object is not supported." );
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#put(java.lang.String, org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put( String name, Scriptable scope, Object value )
	{
		throw new IllegalArgumentException( "Put value on output parameter object is not supported." );
	}

}