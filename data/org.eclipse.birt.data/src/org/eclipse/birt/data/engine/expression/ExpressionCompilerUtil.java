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
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.mozilla.javascript.Context;

/**
 * The utility class to provide method for expression compiling
 * 
 */
public class ExpressionCompilerUtil
{
	private static final String ROWNUM = "__rownum";
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
	 */
	public static boolean hasColumnRow( String name, ExprManager exprManager )
	{
		if( name == null )
			return false;
		if(name.equals( ROWNUM ))
			return true;
		
		IScriptExpression expr = ( (IScriptExpression) exprManager.getExpr( name ));
		if( expr == null )
		{
			//Sometimes the binding name could be an implicit binding, say, 
			//row.__rownum.
			if ( name.matches( ".*\\Q__rownum\\E.*" ) )
				return compile( name, exprManager );
			else
				return false;
		}
		
		return compile( expr.getText( ), exprManager );

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
		populateColumnList( list, expression.getExpression( ), true );
		populateColumnList( list, expression.getOperand1( ), true );
		populateColumnList( list, expression.getOperand2( ), true );
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
		populateColumnList( list, expression.getExpression( ), false );
		populateColumnList( list, expression.getOperand1( ), false );
		populateColumnList( list, expression.getOperand2( ), false );
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
		if ( expression instanceof IScriptExpression )
		{
			String text = ((IScriptExpression)expression).getText( );
			return ExpressionUtil.hasAggregation( text );
		}
		else if ( expression instanceof IConditionalExpression )
		{
			String expr = getExprText(((IConditionalExpression)expression).getExpression( ));
			String oprand1 = getExprText(((IConditionalExpression)expression).getOperand1( ));
			String oprand2 = getExprText(((IConditionalExpression)expression).getOperand2( ));
			return ExpressionUtil.hasAggregation(  expr )
				   ||ExpressionUtil.hasAggregation(  oprand1 )
				   ||ExpressionUtil.hasAggregation( oprand2 );
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

			// fake a registry to register the aggragation.
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
			IScriptExpression oprand1 = ( (IConditionalExpression) expression ).getOperand1( );
			IScriptExpression oprand2 = ( (IConditionalExpression) expression ).getOperand2( );
			return isValidExpressionInQueryFilter( expr )
					&& isValidExpressionInQueryFilter( oprand1 )
					&& isValidExpressionInQueryFilter( oprand2 );
		}
		return true;
	}
	
	/**
	 * 
	 * @param expr
	 * @return
	 */
	private static String getExprText( IScriptExpression expr )
	{
		if( expr!= null )
			return expr.getText( );
		return null;
	}
	
	/**
	 * 
	 * @param expression
	 * @param exprManager
	 * @return
	 */
	private static boolean compile( String expression, ExprManager exprManager )
	{
		Context context = Context.enter( );

		// fake a registry to register the aggragation.
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
	 */
	private static boolean flattenExpression( CompiledExpression expr,
			ExprManager exprManager )
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
				if ( ROWNUM.equals(columnName))
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
		if ( BuiltInAggregationFactory.TOTAL_BOTTOM_N_FUNC.equals( aggName )
				|| BuiltInAggregationFactory.TOTAL_BOTTOM_PERCENT_FUNC.equals( aggName )
				|| BuiltInAggregationFactory.TOTAL_TOP_N_FUNC.equals( aggName )
				|| BuiltInAggregationFactory.TOTAL_TOP_PERCENT_FUNC.equals( aggName ) )
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
			IScriptExpression expression, boolean rowMode ) throws DataException
	{
		if ( expression != null )
		{
			List l;
			try
			{
				l = ExpressionUtil.extractColumnExpressions( expression.getText( ),
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
