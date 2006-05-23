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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This JS object serves for the row of binding columns.
 */
public class JSResultSetRow extends ScriptableObject
{
	private IResultIterator odiResult;
	private ExprManager exprManager;	
	private Scriptable scope;
	private IExecutorHelper helper;
	
	private int currRowIndex;
	private Map valueCacheMap;
	
	/** */
	private static final long serialVersionUID = 649424371394281464L;

	/**
	 * @param odiResult
	 * @param exprManager
	 * @param scope
	 * @param helper
	 */
	public JSResultSetRow( IResultIterator odiResult, ExprManager exprManager,
			Scriptable scope, IExecutorHelper helper )
	{
		this.odiResult = odiResult;
		this.exprManager = exprManager;
		this.scope = scope;
		this.helper = helper;
		
		this.currRowIndex = -1;
		this.valueCacheMap = new HashMap( );
	}
	
	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName( )
	{
		return "ResultSetRow";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(int,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public boolean has( int index, Scriptable start )
	{
		return this.has( String.valueOf( index ), start );
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public boolean has( String name, Scriptable start )
	{
		return exprManager.getExpr( name ) != null;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(int,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object get( int index, Scriptable start )
	{
		return this.get( String.valueOf( index ), start );
	}
	
	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object get( String name, Scriptable start )
	{
		if( "_outer".equalsIgnoreCase( name ))
		{
			if( this.helper.getParent( )!= null)
				return helper.getParent( ).getJSRowObject( );
			else
				return null;
		}
		int rowIndex = -1;
		try
		{
			rowIndex = odiResult.getCurrentResultIndex( );
		}
		catch ( BirtException e1 )
		{
			// impossible, ignore
		}
		
		if( "__rownum".equalsIgnoreCase( name )||"0".equalsIgnoreCase( name ))
		{
			return new Integer( rowIndex );
		}
		
		if ( rowIndex == currRowIndex && valueCacheMap.containsKey( name ) )
		{
			return valueCacheMap.get( name );
		}
		else
		{
			Object value = null;
			try
			{
				IBaseExpression dataExpr = this.exprManager.getExpr( name );
				if ( dataExpr == null )
				{
					return new DataExceptionMocker( new DataException( ResourceConstants.INVALID_BOUND_COLUMN_NAME,
							name ) );
				}
				value = ExprEvaluateUtil.evaluateValue( dataExpr,
						this.odiResult.getCurrentResultIndex( ),
						this.odiResult.getCurrentResult( ),
						this.scope );
			}
			catch ( BirtException e )
			{
				value = null;
			}
			if ( this.currRowIndex != rowIndex )
			{
				this.valueCacheMap.clear( );
				this.currRowIndex = rowIndex;
			}

			valueCacheMap.put( name, value );
			return value;
		}
	}
	
	/**
	 * @param rsObject
	 * @param index
	 * @param name
	 * @return value
	 * @throws DataException 
	 */
	public Object getValue( IResultObject rsObject, int index, String name )
			throws DataException
	{
		Object value = null;
		if ( name.startsWith( "_{" ) )
		{

			try
			{
				value = rsObject.getFieldValue( name );
			}
			catch ( DataException e )
			{
				// ignore
			}
		}
		else
		{
			IBaseExpression dataExpr = this.exprManager.getExpr( name );
			try
			{
				value = ExprEvaluateUtil.evaluateValue( dataExpr,
						-1,
						rsObject,
						this.scope );
				value = JavascriptEvalUtil.convertJavascriptValue( value );
			}
			catch ( BirtException e )
			{
			}
		}
		return value;
	}
	
	/*
	 * @see org.mozilla.javascript.ScriptableObject#put(int,
	 *      org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put( int index, Scriptable scope, Object value )
	{
		throw new IllegalArgumentException( "Put value on result set row is not supported." );
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#put(java.lang.String,
	 *      org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put( String name, Scriptable scope, Object value )
	{
		throw new IllegalArgumentException( "Put value on result set row is not supported." );
	}

}
