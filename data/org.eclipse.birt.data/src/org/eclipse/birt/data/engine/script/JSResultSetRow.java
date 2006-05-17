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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ColumnReferenceExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
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
				value = evaluateValue( dataExpr, -1, rsObject, this.scope );
				value = JavascriptEvalUtil.convertJavascriptValue( value );
			}
			catch ( BirtException e )
			{
			}
		}
		return value;
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
				if( dataExpr == null )
				{
					return null;
				}
				value = evaluateValue( dataExpr,
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
	 * @param dataExpr
	 * @return
	 * @throws BirtException
	 */
	private static Object evaluateValue( IBaseExpression dataExpr, int index,
			IResultObject roObject, Scriptable scope ) throws BirtException
	{	
		Object exprValue = null;

		// TODO: find reasons
		Object handle = dataExpr == null ? null:dataExpr.getHandle( );
		if ( handle instanceof CompiledExpression )
		{
			CompiledExpression expr = (CompiledExpression) handle;
			Object value = evaluateCompiledExpression( expr,
					index,
					roObject,
					scope );

			try
			{
				exprValue = DataTypeUtil.convert( value, dataExpr.getDataType( ) );
			}
			catch ( BirtException e )
			{
				throw new DataException( ResourceConstants.INCONVERTIBLE_DATATYPE,
						new Object[]{
								value,
								value.getClass( ),
								DataType.getClass( dataExpr.getDataType( ) )
						} );
			}
		}
		else if ( handle instanceof ConditionalExpression )
		{
			ConditionalExpression ce = (ConditionalExpression) handle;
			Object resultExpr = evaluateValue( ce.getExpression( ),
					index,
					roObject,
					scope );
			Object resultOp1 = ce.getOperand1( ) != null
					? evaluateValue( ce.getOperand1( ), index, roObject, scope )
					: null;
			Object resultOp2 = ce.getOperand2( ) != null
					? evaluateValue( ce.getOperand2( ), index, roObject, scope )
					: null;
			String op1Text = ce.getOperand1( ) != null ? ce.getOperand1( )
					.getText( ) : null;
			String op2Text = ce.getOperand2( ) != null ? ce.getOperand2( )
					.getText( ) : null;
			exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
					ce.getOperator( ),
					ScriptEvalUtil.newExprInfo( op1Text, resultOp1 ),
					ScriptEvalUtil.newExprInfo( op2Text, resultOp2 ) );
		}
		else
		{
			DataException e = new DataException( ResourceConstants.INVALID_EXPR_HANDLE );
			throw e;
		}

		// the result might be a DataExceptionMocker.
		if ( exprValue instanceof DataExceptionMocker )
		{
			throw ( (DataExceptionMocker) exprValue ).getCause( );
		}

		return exprValue;
	}

	/**
	 * @param expr
	 * @param odiResult
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	private static Object evaluateCompiledExpression( CompiledExpression expr,
			int index, IResultObject roObject, Scriptable scope )
			throws DataException
	{
		// Special case for DirectColRefExpr: it's faster to directly access
		// column value using the Odi IResultIterator.
		if ( expr instanceof ColumnReferenceExpression )
		{
			// Direct column reference
			ColumnReferenceExpression colref = (ColumnReferenceExpression) expr;
			if ( colref.isIndexed( ) )
			{
				int idx = colref.getColumnindex( );
				// Special case: row[0] refers to internal rowID
				if ( idx == 0 )
					return new Integer( index );
				else if ( roObject != null )
					return roObject.getFieldValue( idx );
				else
					return null;
			}
			else
			{
				String name = colref.getColumnName( );
				// Special case: row._rowPosition refers to internal rowID
				if ( JSRowObject.ROW_POSITION.equals( name ) )
					return new Integer( index );
				else if ( roObject != null )
					return roObject.getFieldValue( name );
				else
					return null;
			}
		}
		else
		{
			Context cx = Context.enter();
			try
			{
				return  expr.evaluate( cx, scope );
			}
			finally
			{
				Context.exit();
			}
		}

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
