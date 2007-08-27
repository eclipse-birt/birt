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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ICombinedExpression;
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
import org.eclipse.birt.data.engine.script.ScriptEvalUtil.ExprTextAndValue;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

/**
 * Expression evaluation utility class for compiled expression or uncompiled
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
				if ( value instanceof BirtException )
					throw (BirtException) value;
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
			Object[] op1Value = new Object[0], op2Value = new Object[0];
			boolean isCombined = false;

			if ( ce.getOperand1( ) != null )
			{
				if ( ce.getOperand1( ) instanceof IScriptExpression )
				{
					op1Value = new Object[1];
					op1Value[0] = evaluateExpression( ce.getOperand1( ),
							odiResult,
							scope );
				}
				else if ( ce.getOperand1( ) instanceof ICombinedExpression )
				{
					isCombined = true;
					int length = ( (ICombinedExpression) ce.getOperand1( ) ).getExpressions( ).length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateExpression( ( (ICombinedExpression) ce.getOperand1( ) ).getExpressions( )[i],
								odiResult,
								scope );
					}
					op1Value = flatternMultipleValues( result );
				}
			}
			if ( ce.getOperand2( ) != null )
			{
				if ( ce.getOperand2( ) instanceof IScriptExpression )
				{
					op2Value = new Object[1];
					op2Value[0] = evaluateExpression( ce.getOperand2( ),
							odiResult,
							scope );
				}
			}
			if ( isCombined )
				exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
						ce.getOperator( ),
						op1Value );
			else
				exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
						ce.getOperator( ),
						op1Value.length > 0 ? op1Value[0] : null,
						op2Value.length > 0 ? op2Value[0] : null );
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
	
	public static Object evaluateCompiledExpression( CompiledExpression expr,
			IResultObject ro, int currentIndex, Scriptable scope )
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
					return new Integer( currentIndex );
				else if ( ro != null )
				{
					try
					{
						return DataTypeUtil.convert( ro.getFieldValue( idx ),
								colref.getDataType( ) );
					}
					catch ( BirtException e )
					{
						throw DataException.wrap( e );
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
					return new Integer( currentIndex );
				else if ( ro != null )
				{
					try
					{
						return DataTypeUtil.convert( ro.getFieldValue( name ),
								colref.getDataType( ) );
					}
					catch ( BirtException e )
					{
						throw DataException.wrap( e );
					}
				}
				else
					return null;
			}
		}
		else
		{
			Context cx = Context.enter( );
			try
			{
				return expr.evaluate( cx, scope );
			}
			finally
			{
				Context.exit( );
			}
		}
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
		return evaluateCompiledExpression( expr,
				odiResult.getCurrentResult( ),
				odiResult.getCurrentResultIndex( ),
				scope );
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
				IBaseExpression operand1 = ( (IConditionalExpression) dataExpr ).getOperand1( );
				IBaseExpression operand2 = ( (IConditionalExpression) dataExpr ).getOperand2( );
				
				if ( operand1 instanceof ICombinedExpression )
				{
					IBaseExpression[] expr = ( (ICombinedExpression) operand1 ).getExpressions( );
					Object[] result = new Object[expr.length];
					for ( int i = 0; i < result.length; i++ )
					{
						result[i] = doEvaluateRawExpression( expr[i],
								scope,
								javaType );
					}
					return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
							scope,
							javaType ),
							oper,
							flatternMultipleValues( result ) );
				}
				else
				{
					return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
							scope,
							javaType ),
							oper,
							doEvaluateRawExpression( operand1, scope, javaType ),
							doEvaluateRawExpression( operand2, scope, javaType ) );
				}
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
		else if ( dataExpr instanceof ConditionalExpression )
		{
			ConditionalExpression ce = (ConditionalExpression) dataExpr;
			Object resultExpr = evaluateValue( ce.getExpression( ),
					index,
					roObject,
					scope );
			Object[] op1Value = new Object[0], op2Value = new Object[0];
			boolean isCombined = false;

			if ( ce.getOperand1( ) != null )
			{
				if ( ce.getOperand1( ) instanceof IScriptExpression )
				{
					op1Value = new Object[1];
					op1Value[0] = evaluateValue( ce.getOperand1( ),
							index,
							roObject,
							scope );
				}
				else if ( ce.getOperand1( ) instanceof ICombinedExpression )
				{
					isCombined = true;
					int length = ( (ICombinedExpression) ce.getOperand1( ) ).getExpressions( ).length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateValue( ( (ICombinedExpression) ce.getOperand1( ) ).getExpressions( )[i],
								index,
								roObject,
								scope );
					}
					op1Value = flatternMultipleValues( result );
				}
			}
			if ( ce.getOperand2( ) != null )
			{
				if ( ce.getOperand2( ) instanceof IScriptExpression )
				{
					op2Value = new Object[1];
					op2Value[0] = evaluateValue( ce.getOperand2( ),
							index,
							roObject,
							scope );
				}
				else if ( ce.getOperand2( ) instanceof ICombinedExpression )
				{
					int length = ( (ICombinedExpression) ce.getOperand2( ) ).getExpressions( ).length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateValue( ( (ICombinedExpression) ce.getOperand2( ) ).getExpressions( )[i],
								index,
								roObject,
								scope );
					}
					op2Value = flatternMultipleValues( result );
				}
			}
			if ( isCombined )
				exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
						ce.getOperator( ),
						op1Value );
			else
				exprValue = ScriptEvalUtil.evalConditionalExpr( resultExpr,
						ce.getOperator( ),
						op1Value.length > 0 ? op1Value[0] : null,
						op2Value.length > 0 ? op2Value[0] : null );
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
	
	/**
	 * 
	 * @return
	 */
	private static Object[] flatternMultipleValues( Object[] values )
	{
		if ( values == null || values.length == 0 )
			return new Object[0];
		List flattern = new ArrayList( );
		for ( int i = 0; i < values.length; i++ )
		{
			if ( values[i] instanceof Object[] )
			{
				Object[] flatternObj = (Object[]) values[i];
				flattern.addAll( Arrays.asList( flatternMultipleValues( flatternObj ) ) );
			}
			else
			{
				flattern.add( values[i] );
			}
		}
		return flattern.toArray( );
	}
}
