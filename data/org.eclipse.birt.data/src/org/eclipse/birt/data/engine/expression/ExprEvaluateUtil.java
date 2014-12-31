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
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.NEvaluator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
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
			IResultIterator odiResult, Scriptable scope, ScriptContext cx )
			throws BirtException
	{
		Object exprValue = null;
		
		//TODO here the dataExpr should not be null.
		//This is only a temporary solution.
		
		if( dataExpr == null )
			throw new DataException(ResourceConstants.BAD_DATA_EXPRESSION);
		
		Object handle = dataExpr.getHandle( );
		if ( handle instanceof ICompiledScript )
		{
			return cx.newContext( scope ).evaluate( (ICompiledScript )handle);
		}
		else if ( handle instanceof CompiledExpression )
		{
			CompiledExpression expr = (CompiledExpression) handle;
			Object value = evaluateCompiledExpression( expr, odiResult, scope, cx );

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
					scope, cx );
			Object[] op1Value = new Object[0], op2Value = new Object[0];
			boolean isCombined = false;

			if ( ce.getOperand1( ) != null )
			{
				if ( ce.getOperand1( ) instanceof IScriptExpression )
				{
					op1Value = new Object[1];
					op1Value[0] = evaluateExpression( ce.getOperand1( ),
							odiResult,
							scope, cx );
				}
				else if ( ce.getOperand1( ) instanceof IExpressionCollection )
				{
					isCombined = true;
					Object[] exprs = ( (IExpressionCollection) ce.getOperand1( ) ).getExpressions( ).toArray( );
					int length = exprs.length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateExpression( (IBaseExpression) exprs[i],
								odiResult,
								scope, cx );
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
							scope, cx );
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
		else if ( BaseExpression.constantId.equals( dataExpr.getScriptId( ) ) )
		{
			//if expression is constant
			Object value = ( (IScriptExpression) dataExpr ).getHandle( );
			if ( value == null )
			{
				value = ( (IScriptExpression) dataExpr ).getText( );
				( (IScriptExpression) dataExpr ).setHandle( value );
			}
			exprValue = DataTypeUtil.convert( value, dataExpr.getDataType( ) );
			return exprValue;
		}
		else
		{
			DataException e = new DataException( ResourceConstants.INVALID_EXPR_HANDLE );
			throw e;
		}
		return exprValue;
	}
	
	public static Object evaluateCompiledExpression( CompiledExpression expr,
			IResultObject ro, int currentIndex, Scriptable scope, ScriptContext cx )
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
					return Integer.valueOf( currentIndex );
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
					return Integer.valueOf( currentIndex );
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
			return expr.evaluate( cx, scope );
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
			IResultIterator odiResult, Scriptable scope, ScriptContext cx ) throws DataException
	{
		return evaluateCompiledExpression( expr,
				odiResult.getCurrentResult( ),
				odiResult.getCurrentResultIndex( ),
				scope, cx );
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
			Scriptable scope, ScriptContext cx ) throws BirtException
	{
		return doEvaluateRawExpression( dataExpr, scope, false, cx );
	}
	
	/**
	 * @param dataExpr
	 * @param scope
	 * @return the value of Java data type
	 * @throws BirtException
	 */
	public static Object evaluateRawExpression2( IBaseExpression dataExpr,
			Scriptable scope, ScriptContext cx, DataSetRuntime dataSet ) throws BirtException
	{
		return doEvaluateRawExpression( dataExpr, scope, true, cx, dataSet );
	}
	
	/**
	 * @param dataExpr
	 * @param scope
	 * @return
	 * @throws BirtException
	 */
	protected static Object doEvaluateRawExpression( IBaseExpression dataExpr,
			Scriptable scope, boolean javaType, ScriptContext cx )
			throws BirtException
	{
		if ( dataExpr == null )
			return null;

		if ( dataExpr instanceof IScriptExpression )
		{
			if ( ( (IScriptExpression) dataExpr ).getText( ) == null
					&& !( BaseExpression.constantId.equals( dataExpr.getScriptId( ) ) ) )
				throw new DataException( ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK );
			Object value = null;
			if ( BaseExpression.constantId.equals( dataExpr.getScriptId( ) ) )
			{
				value = ( (IScriptExpression) dataExpr ).getHandle( );
				if ( value == null )
				{
					value = ( (IScriptExpression) dataExpr ).getText( );
					( (IScriptExpression) dataExpr ).setHandle( value );
				}
			}
			else
			{
				value = ScriptEvalUtil.evaluateJSAsExpr( cx,
						scope,
						( (IScriptExpression) dataExpr ).getText( ),
						ScriptExpression.defaultID,
						0 );
			}

			if ( javaType == true )
				value = JavascriptEvalUtil.convertJavascriptValue( value );

			value = DataTypeUtil.convert( value, dataExpr.getDataType( ) );

			return value;
		}
		else if ( dataExpr instanceof IConditionalExpression )
		{
			return evaluateConditionExpression( (IConditionalExpression) dataExpr,
					scope,
					javaType,
					cx,
					null );
		}
		else
		{
			assert false;
			return null;
		}
	}
	
	/**
	 * 
	 * @param dataExpr
	 * @param cx
	 * @param isRow true:row["xxx"]; false:dataSetRow["xxx"]
	 * @return
	 * @throws BirtException 
	 */
	private static String extractDirectColumn( IBaseExpression dataExpr,
			ScriptContext cx, 
			boolean isRow ) throws BirtException
	{
		if ( dataExpr instanceof IScriptExpression )
		{
			String exprText = ((IScriptExpression)dataExpr).getText( );
			if ( isRow )
			{
				return ExpressionUtil.getColumnBindingName( exprText );
			}
			else
			{
				return ExpressionUtil.getColumnName( exprText );
			}
		}
		return null;
	}
	
	/**
	 * @param dataExpr
	 * @param scope
	 * @return
	 * @throws BirtException
	 */
	private static Object doEvaluateRawExpression( IBaseExpression dataExpr,
			Scriptable scope, boolean javaType, ScriptContext cx,
			DataSetRuntime dataSet )
			throws BirtException
	{
		if ( dataSet == null )
		{
			return doEvaluateRawExpression( dataExpr, scope, javaType, cx );
		}
		String dataSetColumn = extractDirectColumn( dataExpr, cx, false );
		if ( dataSetColumn != null )
		{
			if ( dataSet.getCurrentRow( ) != null 
					&& dataSet.getCurrentRow( ).getResultClass( ).getFieldIndex( dataSetColumn ) >= 0 )
			{
				Object value = dataSet.getCurrentRow( ).getFieldValue( dataSetColumn );
				return DataTypeUtil.convert( value, dataExpr.getDataType( ) );
			}
		}
		
		String rowName = extractDirectColumn( dataExpr, cx, true );
		if ( rowName != null )
		{
			Scriptable scriptable = dataSet.getJSResultRowObject( );
			if ( scriptable instanceof JSResultSetRow )
			{
				JSResultSetRow resultSetRow = (JSResultSetRow)scriptable;
				IBinding b = resultSetRow.getBinding( rowName );
				if ( b != null && b.getAggrFunction( ) == null )
				{
					IBaseExpression expr = b.getExpression( );
					dataSetColumn = extractDirectColumn( expr, cx, false );
					if ( dataSetColumn != null )
					{
						//binding "xxx" expression is just dataSetRow["xxx"]
						if ( dataSet.getCurrentRow( ) != null 
								&& dataSet.getCurrentRow( ).getResultClass( ).getFieldIndex( dataSetColumn ) >= 0 )
						{
							Object value = dataSet.getCurrentRow( ).getFieldValue( dataSetColumn );
							return DataTypeUtil.convert( value, b.getDataType( ) );
						}
					}
				}
			}
			else
			{
				//row["xxx"] is added on data set level
				if ( dataSet.getCurrentRow( ) != null 
						&& dataSet.getCurrentRow( ).getResultClass( ).getFieldIndex( rowName ) >= 0 )
				{
					Object value = dataSet.getCurrentRow( ).getFieldValue( rowName );
					return DataTypeUtil.convert( value, dataExpr.getDataType( ) );
				}
			}
		}
		
		return doEvaluateRawExpression( dataExpr, scope, javaType, cx );
	}

	/**
	 * 
	 * @param dataExpr
	 * @param scope
	 * @param javaType
	 * @param cx
	 * @return
	 * @throws DataException
	 * @throws BirtException
	 */
	public static Object evaluateConditionExpression(
			IConditionalExpression dataExpr, Scriptable scope,
			boolean javaType, ScriptContext cx, CompareHints filterHints )
			throws DataException, BirtException
	{
		if ( dataExpr.getHandle( ) != null )
			return Boolean.valueOf( ( (NEvaluator) dataExpr.getHandle( ) ).evaluate( cx,
					scope,
					null ) );

		IScriptExpression opr = ( (IConditionalExpression) dataExpr ).getExpression( );
		int oper = ( (IConditionalExpression) dataExpr ).getOperator( );
		IBaseExpression operand1 = ( (IConditionalExpression) dataExpr ).getOperand1( );
		IBaseExpression operand2 = ( (IConditionalExpression) dataExpr ).getOperand2( );

		if ( operand1 instanceof IExpressionCollection )
		{
			Object[] expr = ( (IExpressionCollection) operand1 ).getExpressions( )
					.toArray( );
			Object[] result = new Object[expr.length];
			for ( int i = 0; i < result.length; i++ )
			{
				result[i] = doEvaluateRawExpression( (IBaseExpression) expr[i],
						scope,
						javaType,
						cx );
			}
			return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
					scope,
					javaType,
					cx ),
					oper,
					flatternMultipleValues( result ),
					filterHints );
		}
		else
		{
			return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
					scope,
					javaType,
					cx ),
					oper,
					doEvaluateRawExpression( operand1, scope, javaType, cx ),
					doEvaluateRawExpression( operand2, scope, javaType, cx ),
					filterHints );
		}
	}
	
	
	public static Object evaluateConditionExpression(
			IConditionalExpression dataExpr, Scriptable scope,
			boolean javaType, ScriptContext cx, CompareHints filterHints,
			DataSetRuntime dataSet)
			throws DataException, BirtException
	{
		if ( dataExpr.getHandle( ) != null )
			return Boolean.valueOf( ( (NEvaluator) dataExpr.getHandle( ) ).evaluate( cx,
					scope,
					dataSet ) );

		IScriptExpression opr = ( (IConditionalExpression) dataExpr ).getExpression( );
		int oper = ( (IConditionalExpression) dataExpr ).getOperator( );
		IBaseExpression operand1 = ( (IConditionalExpression) dataExpr ).getOperand1( );
		IBaseExpression operand2 = ( (IConditionalExpression) dataExpr ).getOperand2( );

		if ( operand1 instanceof IExpressionCollection )
		{
			Object[] expr = ( (IExpressionCollection) operand1 ).getExpressions( )
					.toArray( );
			Object[] result = new Object[expr.length];
			for ( int i = 0; i < result.length; i++ )
			{
				result[i] = doEvaluateRawExpression( (IBaseExpression) expr[i],
						scope,
						javaType,
						cx,
						dataSet);
			}
			return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
					scope,
					javaType,
					cx,
					dataSet),
					oper,
					flatternMultipleValues( result ),
					filterHints );
		}
		else
		{
			return ScriptEvalUtil.evalConditionalExpr( doEvaluateRawExpression( opr,
					scope,
					javaType,
					cx,
					dataSet),
					oper,
					doEvaluateRawExpression( operand1, scope, javaType, cx, dataSet ),
					doEvaluateRawExpression( operand2, scope, javaType, cx, dataSet ),
					filterHints );
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
			IResultObject roObject, Scriptable scope, ScriptContext cx ) throws BirtException
	{	
		Object exprValue = null;

		// TODO: find reasons
		Object handle = dataExpr == null ? null:dataExpr.getHandle( );
		if ( handle instanceof ICompiledScript )
		{
			return cx.newContext( scope ).evaluate( (ICompiledScript )handle);
		}
		else if ( handle instanceof CompiledExpression )
		{
			CompiledExpression expr = (CompiledExpression) handle;
			Object value = evaluateCompiledExpression( expr,
					index,
					roObject,
					scope, cx );

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
					scope, cx );
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
							scope, cx );
				}
				else if ( ce.getOperand1( ) instanceof IExpressionCollection )
				{
					isCombined = true;
					Object[] exprs = ( (IExpressionCollection) ce.getOperand1( ) ).getExpressions( ).toArray( ); 
					int length = exprs.length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateValue( (IBaseExpression)exprs[i],
								index,
								roObject,
								scope, cx );
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
							scope, cx );
				}
				else if ( ce.getOperand2( ) instanceof IExpressionCollection )
				{
					Object[] exprs = ( (IExpressionCollection) ce.getOperand2( ) ).getExpressions( ).toArray( ); 
					int length = exprs.length;
					Object[] result = new Object[length];
					for ( int i = 0; i < length; i++ )
					{
						result[i] = evaluateValue( (IBaseExpression)exprs[i],
								index,
								roObject,
								scope, cx );
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
			int index, IResultObject roObject, Scriptable scope, ScriptContext cx )
			throws DataException
	{
		// Special case for DirectColRefExpr: it's faster to directly access
		// column value using the Odi IResultIterator.
		if ( expr instanceof ColumnReferenceExpression )
		{
			// Direct column reference
			ColumnReferenceExpression colref = (ColumnReferenceExpression) expr;
			return evaluateColumnReferenceExpression( roObject, index, colref );
		}
		else
		{
			return  expr.evaluate( cx, scope );
		}

	}
	
	public static Object evaluateColumnReferenceExpression( IResultObject roObject, int index, ColumnReferenceExpression colref ) throws DataException
	{
		if ( colref.isIndexed( ) )
		{
			int idx = colref.getColumnindex( );
			// Special case: row[0] refers to internal rowID
			if ( idx == 0 )
				return Integer.valueOf( index );
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
				return Integer.valueOf( index );
			else if ( roObject != null )
				return roObject.getFieldValue( name );
			else
				return null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Object[] flatternMultipleValues( Object[] values )
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
