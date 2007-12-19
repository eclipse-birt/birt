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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Context;

/**
 * The utility class to provide method for expression compiling
 * 
 */
public class ExpressionCompilerUtil
{
	//private static ExpressionCompiler expressionCompiler = new ExpressionCompiler( );

	/**
	 * compile the expression
	 * @param expr
	 * @param registry
	 * @param cx
	 * @return
	 */
	public static CompiledExpression compile( String expr, Context cx )
	{
		ExpressionCompiler expressionCompiler = new ExpressionCompiler( );
		expressionCompiler.setDataSetMode( true );
		return expressionCompiler.compile( expr, null, cx );
	}

	/**
	 * 
	 * @param name
	 * @param exprManager
	 * @param scope
	 * @return
	 * @throws DataException 
	 */
	public static boolean hasColumnRow( String expression, ExprManager exprManager ) throws DataException
	{
		if( expression == null )
			return false;
		
		return compile( expression, exprManager );

	}
	
	/**
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractColumnExpression( IBaseExpression expression )
			throws DataException
	{
		if ( expression == null )
			return new ArrayList( );
		List columnList = null;
		if ( expression instanceof IScriptExpression )
		{
			columnList = extractColumnExpression( (IScriptExpression) expression );
		}
		else if ( expression instanceof IConditionalExpression )
		{
			columnList = extractColumnExpression( (IConditionalExpression) expression );
		}
		else if ( expression instanceof IExpressionCollection )
		{
			columnList = extractColumnExpression( (IExpressionCollection) expression );
		}
		return columnList;
	}
	
	/**
	 * The utility method is to compile expression to get a list of column
	 * expressions which is depended by given expression.
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractColumnExpression( IScriptExpression expression )
			throws DataException
	{
		List list = new ArrayList( );
		if ( expression == null )
			return list;
		populateColumnList( list, expression, true );
		return list;
	}
	
	/**
	 * 
	 * This utility method is to compile expression to get a list of column
	 * expressions which is depended by given expression.
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractColumnExpression(
			IConditionalExpression expression ) throws DataException
	{
		List list = new ArrayList( );
		if ( expression == null )
			return list;
		list.addAll( extractColumnExpression( expression.getExpression( ) ) );
		List valueList = extractColumnExpression( expression.getOperand1( ) );
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			if ( !list.contains( valueList.get( i ) ) )
				list.add( valueList.get( i ) );
		}
		valueList = extractColumnExpression( expression.getOperand2( ) );
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			if ( !list.contains( valueList.get( i ) ) )
				list.add( valueList.get( i ) );
		}
		return list;
	}
	
	/**
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractColumnExpression( IExpressionCollection expression )
			throws DataException
	{
		List list = new ArrayList( );
		if ( expression == null )
			return list;
		Object[] ce =  expression.getExpressions( ).toArray( );
		for ( int i = 0; i < ce.length; i++ )
		{
			List valueList = extractColumnExpression( (IBaseExpression)ce[i] );
			for ( int j = 0; j < valueList.size( ); j++ )
			{
				if ( !list.contains( valueList.get( j ) ) )
					list.add( valueList.get( j ) );
			}
		}
		return list;
	}
	
	/**
	 * This utility method is to compile expression to get a list of dataset
	 * column expressions which is depended by given expression.
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractDataSetColumnExpression(
			IBaseExpression expression ) throws DataException
	{
		List columnList = new ArrayList( );

		if ( expression == null )
			return columnList;
		if ( expression instanceof IScriptExpression )
		{
			columnList = extractDataSetColumnExpression( (IScriptExpression) expression );
		}
		else if ( expression instanceof IConditionalExpression )
		{
			columnList = extractDataSetColumnExpression( (IConditionalExpression) expression );
		}
		else if ( expression instanceof IExpressionCollection )
		{
			columnList = extractDataSetColumnExpression( (IExpressionCollection) expression );
		}
		return columnList;
	}
	
	/**
	 * This utility method is to compile expression to get a list of dataset
	 * column expressions which is depended by given expression.
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractDataSetColumnExpression(
			IConditionalExpression expression ) throws DataException
	{
		List list = new ArrayList( );
		if ( expression == null )
			return list;
		list.addAll( extractDataSetColumnExpression( expression.getExpression( ) ) );
		List valueList = extractDataSetColumnExpression( expression.getOperand1( ) );
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			if ( !list.contains( valueList.get( i ) ) )
				list.add( valueList.get( i ) );
		}
		valueList = extractDataSetColumnExpression( expression.getOperand2( ) );
		for ( int i = 0; i < valueList.size( ); i++ )
		{
			if ( !list.contains( valueList.get( i ) ) )
				list.add( valueList.get( i ) );
		}
		return list;
	}

	/**
	 * 
	 * @param expression
	 * @return
	 * @throws DataException 
	 */
	public static List extractDataSetColumnExpression(
			IExpressionCollection expression ) throws DataException
	{
		List list = new ArrayList( );
		if ( expression == null )
			return list;
		Object[] exprs = expression.getExpressions( ).toArray( );
		for ( int i = 0; i < exprs.length; i++ )
		{
			List valueList = extractDataSetColumnExpression( (IBaseExpression)exprs[i] );
			for ( int j = 0; j < valueList.size( ); j++ )
			{
				if ( !list.contains( valueList.get( j ) ) )
					list.add( valueList.get( j ) );
			}
		}
		return list;
	}
	
	/**
	 * This utility method is to compile expression to get a list of dataset
	 * column expressions which is depended by given expression.
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List extractDataSetColumnExpression(
			IScriptExpression expression ) throws DataException
	{
		List list = new ArrayList( );

		if ( expression == null )
			return list;
		populateColumnList( list, expression, false );
		return list;
	}
	
	/**
	 * Check whether there is columnReferenceExpression in aggregation. If so,
	 * return true. else return false;
	 * 
	 * @return
	 */
	public static boolean hasAggregationInExpr( IBaseExpression expression )
	{
		if ( expression == null )
			return false;
		if ( expression instanceof IScriptExpression )
		{
			String text = ( (IScriptExpression) expression ).getText( );
			return ExpressionUtil.hasAggregation( text );
		}
		else if ( expression instanceof IConditionalExpression )
		{
			return hasAggregationInExpr( ( (IConditionalExpression) expression ).getExpression( ) ) ||
					hasAggregationInExpr( ( (IConditionalExpression) expression ).getOperand1( ) ) ||
					hasAggregationInExpr( ( (IConditionalExpression) expression ).getOperand2( ) );
		}
		else if ( expression instanceof IExpressionCollection )
		{
			Object[] text = ( (IExpressionCollection) expression ).getExpressions( ).toArray( );
			for ( int i = 0; i < text.length; i++ )
			{
				if ( hasAggregationInExpr( (IBaseExpression) text[i] ) )
					return true;
			}
		}
		return false;
	}

	/**
	 * Check whether filter in query contains aggregation. If aggregation is
	 * TOPN,BOTTOMN,TOPPERCENT,BOTTMEPERCENT return true. else return false;
	 * 
	 * @return
	 */
	public static boolean isValidExpressionInQueryFilter(
			IBaseExpression expression )
	{
		if ( expression instanceof IScriptExpression )
		{
			String text = ( (IScriptExpression) expression ).getText( );
			Context context = Context.enter( );

			// fake a registry to register the aggregation.
			AggregateRegistry aggrReg = new AggregateRegistry( ) {

				public int register( AggregateExpression aggregationExpr )
				{
					return -1;
				}
			};
			try
			{
				ExpressionCompiler expressionCompiler = new ExpressionCompiler( );
				CompiledExpression expr = expressionCompiler.compile( text,
						aggrReg,
						context );
				return flattenFilterExpression( expr );
			}
			finally
			{
				Context.exit( );
			}
		}
		else if ( expression instanceof IConditionalExpression )
		{
			IScriptExpression expr = ( (IConditionalExpression) expression ).getExpression( );
			IBaseExpression oprand1 = ( (IConditionalExpression) expression ).getOperand1( );
			IBaseExpression oprand2 = ( (IConditionalExpression) expression ).getOperand2( );
			return isValidExpressionInQueryFilter( expr ) &&
					isValidExpressionInQueryFilter( oprand1 ) &&
					isValidExpressionInQueryFilter( oprand2 );
		}
		return true;
	}
	
	/**
	 * 
	 * @param expression
	 * @param exprManager
	 * @return
	 * @throws DataException 
	 */
	private static boolean compile( String expression, ExprManager exprManager ) throws DataException
	{
		Context context = Context.enter( );

		// fake a registry to register the aggregation.
		AggregateRegistry aggrReg = new AggregateRegistry( ) {

			public int register( AggregateExpression aggregationExpr )
			{
				return -1;
			}
		};
		try
		{
			ExpressionCompiler expressionCompiler = new ExpressionCompiler( );
			CompiledExpression expr = expressionCompiler.compile( expression,
					aggrReg,
					context );
			return flattenExpression( expr, exprManager );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * 
	 * @param expr
	 * @throws DataException 
	 */
	private static boolean flattenExpression( CompiledExpression expr,
			ExprManager exprManager ) throws DataException
	{
		int type = expr.getType( );
		switch ( type )
		{
			case CompiledExpression.TYPE_COMPLEX_EXPR :
			{
				Iterator col = ( (ComplexExpression) expr ).getSubExpressions( )
						.iterator( );
				while ( col.hasNext( ) )
				{
					if ( !flattenExpression( (CompiledExpression) col.next( ),
							exprManager ) )
						return false;
				}
				break;
			}
			case CompiledExpression.TYPE_DIRECT_COL_REF :
			{
				String columnName = ( (ColumnReferenceExpression) expr ).getColumnName( );
				if ( ScriptConstants.ROW_NUM_KEYWORD.equals(columnName))
					return true;
				if ( exprManager.getExpr( columnName )!=null )
				{
					String expression = ( (IScriptExpression) exprManager.getExpr( columnName ) ).getText( );
					return compile( expression, exprManager );
				}
				else
				{
					return false;
				}
			}
			case CompiledExpression.TYPE_SINGLE_AGGREGATE :
			{
				Iterator args = ( (AggregateExpression) expr ).getArguments( )
						.iterator( );
				while ( args.hasNext( ) )
				{
					if ( !flattenExpression( (CompiledExpression) args.next( ),
							exprManager ) )
						return false;
				}
			}
			case CompiledExpression.TYPE_CONSTANT_EXPR :
			case CompiledExpression.TYPE_INVALID_EXPR :
			{
				return true;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 */
	private static boolean flattenFilterExpression( CompiledExpression expr )
	{
		int type = expr.getType( );
		switch ( type )
		{
			case CompiledExpression.TYPE_COMPLEX_EXPR :
			{
				Iterator col = ( (ComplexExpression) expr ).getSubExpressions( )
						.iterator( );
				while ( col.hasNext( ) )
				{
					if ( !flattenFilterExpression( (CompiledExpression) col.next( ) ) )
						return false;
				}
				break;
			}
			case CompiledExpression.TYPE_DIRECT_COL_REF :
			{
				break;
			}
			case CompiledExpression.TYPE_SINGLE_AGGREGATE :
			{
				String aggName = ( (AggregateExpression) expr ).getAggregation( )
						.getName( );
				if ( !isTopBottomN( aggName ) )
					return false;
				break;
			}
			case CompiledExpression.TYPE_CONSTANT_EXPR :
			case CompiledExpression.TYPE_INVALID_EXPR :
			{
				break;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param aggName
	 * @return
	 */
	private static boolean isTopBottomN( String aggName )
	{
		if ( IBuildInAggregation.TOTAL_BOTTOM_N_FUNC.equals( aggName )
				|| IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC.equals( aggName )
				|| IBuildInAggregation.TOTAL_TOP_N_FUNC.equals( aggName )
				|| IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC.equals( aggName ) )
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param list
	 * @param expression
	 * @throws DataException
	 */
	private static void populateColumnList( List list,
			IBaseExpression expression, boolean rowMode ) throws DataException
	{
		if ( expression != null )
		{
			List l = new ArrayList( );
			try
			{
				if ( expression instanceof IScriptExpression )
					l = ExpressionUtil.extractColumnExpressions( ( (IScriptExpression) expression ).getText( ),
							rowMode );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}

			for ( int i = 0; i < l.size( ); i++ )
			{
				IColumnBinding cb = (IColumnBinding) l.get( i );
				if ( !list.contains( cb.getResultSetColumnName( ) )
						&& cb.getOuterLevel( ) == 0 )
					list.add( cb.getResultSetColumnName( ) );
			}
		}
	}

}
