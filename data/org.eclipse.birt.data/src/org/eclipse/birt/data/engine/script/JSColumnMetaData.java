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

import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static Logger logger = Logger.getLogger( JSColumnMetaData.class.getName( ) );

	public JSColumnMetaData( IResultClass resultClass )
	{
		logger.entering( JSColumnMetaData.class.getName( ), "JSColumnMetaData" );
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
		logger.entering( JSColumnMetaData.class.getName( ),
				"get",
				new Integer( index ) );
		// Our index is 1-based
		if ( index > 0 && index <= resultClass.getFieldCount() )
		{
			// JSColumnDefn objects are created only on demand
			if ( cachedColumns[index-1] == null )
				cachedColumns[index-1] = new JSColumnDefn( resultClass, index );
			logger.exiting( JSColumnMetaData.class.getName( ),
					"get",
					cachedColumns[index - 1] );
			return cachedColumns[index-1];
		}
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSColumnMetaData.class.getName( ),
					"get",
					super.get( index, start ) );
		return super.get(index, start);
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start)
	{
		logger.entering( JSColumnMetaData.class.getName( ), "get", name );
		int index = resultClass.getFieldIndex(name);
		if ( index <= 0 ){
			// Not the name of a field
			if ( logger.isLoggable( Level.FINER ) )
				logger.exiting( JSColumnMetaData.class.getName( ),
						"get",
						super.get( name, start ) );
			return super.get(name, start);
		}
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSColumnMetaData.class.getName( ),
					"get",
					get( index, start ) );
		return get( index, start);
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds()
	{
		logger.entering( JSColumnMetaData.class.getName( ), "getIds" );
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
				// Should not get here. 
				logger.logp( Level.FINER,
						JSColumnMetaData.class.getName( ),
						"get",
						e.getMessage( ),
						e );
			}
			ids[ 2* i + 1] = new Integer(i + 1);
		}
		
		logger.exiting( JSColumnMetaData.class.getName( ), "getIds", ids );
		return ids;
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(int, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(int index, Scriptable start)
	{
		logger.entering( JSColumnMetaData.class.getName( ),
				"has",
				new Integer( index ) );
		if ( index > 0 && index <= resultClass.getFieldCount( ) )
		{
			logger.exiting( JSColumnMetaData.class.getName( ),
					"has",
					new Boolean( true ) );
			return true;
		}
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSColumnMetaData.class.getName( ),
					"has",
					new Boolean( super.has( index, start ) ) );
		return super.has( index, start );
	}
	
	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public boolean has(String name, Scriptable start)
	{
		logger.entering( JSColumnMetaData.class.getName( ), "has" );
		if ( resultClass.getFieldIndex( name ) > 0 )
		{
			logger.exiting( JSColumnMetaData.class.getName( ),
					"has",
					new Boolean( true ) );
			return true;
		}
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSColumnMetaData.class.getName( ),
					"has",
					new Boolean( super.has( name, start ) ) );
		return super.has( name, start);
	}
}
