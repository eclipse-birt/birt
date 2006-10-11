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
package org.eclipse.birt.data.engine.expression;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.DataExceptionMocker;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.NEvaluator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Expression evaluation utitlity class for compiled expression or uncompiled
 * expression.
 */
public class ExprEvaluateUtil
{
	
	/**
	 * @param dataExpr
	 * @param odiResult
	 * @param scope
	 * @param logger
	 * @return
	 * @throws BirtException
	 */
	public static Object evaluateExpression( IBaseExpression dataExpr,
			IResultIterator odiResult, Scriptable scope )
			throws BirtException
	{
		Object exprValue = null;
		
		//TODO here the dataExpr should not be null.
		//This is only a temporary solution.
		
		if( dataExpr == null )
			throw new DataException(ResourceConstants.BAD_DATA_EXPRESSION);
		
		Object handle = dataExpr.getHandle( );
		if ( handle instanceof CompiledExpression )
		{
			CompiledExpression expr = (CompiledExpression) handle;
			Object value = evaluateCompiledExpression( expr, odiResult, scope );

			try
			{
				exprValue = DataTypeUtil.convert( value, dataExpr.getDataType( ) );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}
		}
		else if ( handle instanceof ConditionalExpression )
		{
			ConditionalExpression ce = (ConditionalExpression) handle;
			Object resultExpr = evaluateExpression( ce.getExpression( ),
					odiResult,
					scope );
			Object resultOp1 = ce.getOperand1( ) != null
					? evaluateExpression( ce.getOperand1( ), odiResult, scope )
					: null;
			Object resultOp2 = ce.getOperand2( ) != null
					? evaluateExpression( ce.getOperand2( ), odiResult, scope )
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
	public static Object evaluateCompiledExpression( CompiledExpression expr,
			IResultIterator odiResult, Scriptable scope ) throws DataException
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
					return new Integer( odiResult.getCurrentResultIndex( ) );
				else if ( odiResult.getCurrentResult( ) != null )
				{	
					try 
					{
						return DataTypeUtil.convert(odiResult.getCurrentResult( )
								.getFieldValue( idx ),colref.getDataType());
					}
					catch (BirtException e) 
					{
						throw DataException.wrap(e);
					}
				}
				else
					return null;
			}
			else
			{
				String name = colref.getColumnName( );
				// Special case: row._rowPosition refers to internal rowID
				if ( JSRowObject.ROW_POSITION.equals( name ) )
					return new Integer( odiResult.getCurrentResultIndex( ) );
				else if ( odiResult.getCurrentResult( ) != null )
				{
					try 
					{
						return DataTypeUtil.convert(odiResult.getCurrentResult( )
								.getFieldValue( name ),colref.getDataType());
					}
					catch (BirtException e) 
					{
						throw DataException.wrap(e);
					}
				}
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
	
	/**
	 * Evaluate non-compiled expression
	 * 
	 * @param dataExpr
	 * @param scope
	 * @return the value of raw data type, Java or Java Script
	 * @throws BirtException
	 */
	public static Object evaluateRawExpression( IBaseExpression dataExpr,
			Scriptable scope ) throws BirtException
	{
		return doEvaluateRawExpression( dataExpr, scope, false );
	}
	
	/**
	 * @param dataExpr
	 * @param scope
	 * @return the value of Java data type
	 * @throws BirtException
	 */
	public static Object evaluateRawExpression2( IBaseExpression dataExpr,
			Scriptable scope ) throws BirtException
	{
		return doEvaluateRawExpression( dataExpr, scope, true );
	}
	
	/**
	 * @param dataExpr
	 * @param scope
	 * @return
	 * @throws BirtException
	 */
	private static Object doEvaluateRawExpression( IBaseExpression dataExpr,
			Scriptable scope, boolean javaType ) throws BirtException
	{
		if ( dataExpr == null )
			return null;
		
		try
		{
			Context cx = Context.enter( );
			if ( dataExpr instanceof IScriptExpression )
			{
				if ( ( (IScriptExpression) dataExpr ).getText( ) == null )
					throw new DataException( ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK );
				
				Object value = JavascriptEvalUtil.evaluateRawScript( cx,
						scope,
						( (IScriptExpression) dataExpr ).getText( ),
						"source",
						0 );
				
				if ( javaType == true )
					value = JavascriptEvalUtil.convertJavascriptValue( value );
				
				value = DataTypeUtil.convert( value, dataExpr.getDataType( ) );
				
				return value;
			}
			else if ( dataExpr instanceof IConditionalExpression )
			{
				if( dataExpr.getHandle( )!= null )
					return new Boolean(((NEvaluator)dataExpr.getHandle( )).evaluate( cx, scope ));
				
				IScriptExpression opr = ( (IConditionalExpression) dataExpr ).getExpression( );
				int oper = ( (IConditionalExpression) dataExpr ).getOperator( );
				IScriptExpression operand1 = ( (IConditionalExpression) dataExpr ).getOperand1( );
				IScriptExpression operand2 = ( (IConditionalExpression) dataExpr ).getOperand2( );
				
				return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
						scope,
						javaType ),
						oper,
						doEvaluateRawExpression( operand1, scope, javaType ),
						doEvaluateRawExpression( operand2, scope, javaType ) );
			}
			else
			{
				assert false;
				return null;
			}
			
		}
		finally
		{
			Context.exit( );
		}
	}
	
	//------------------------------------------------------------------
		
	/**
	 * TODO: need refactoring
	 * 
	 * @param dataExpr
	 * @return
	 * @throws BirtException
	 */
	public static Object evaluateValue( IBaseExpression dataExpr, int index,
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
	
}
