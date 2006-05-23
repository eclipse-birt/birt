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

import java.util.Iterator;

import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.mozilla.javascript.Context;

/**
 * 
 */
public class ExpressionCompilerUtil
{
	private static final String ROWNUM = "__rownum";
	private static ExpressionCompiler expressionCompiler = new ExpressionCompiler( );

	/**
	 * @param expr
	 * @param registry
	 * @param cx
	 * @return
	 */
	public static CompiledExpression compile( String expr, Context cx )
	{
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
			return false;
		
		String expression = expr.getText( );

		return compile( expression, exprManager );

	}

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

}
