/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionSupportManager;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

/**
 * The utility class of expression, if the expression is column, return true,
 * else return false. The column format should like row.aaa , row["aaa"] or
 * row[index]
 */
public class ExpressionUtility
{

	private final static String STRING_ROW = "row"; //$NON-NLS-1$
	// the default cache size
	private final static int EXPR_CACHE_SIZE = 50;
	/**
	 * Use the LRU cache for the compiled expression.For performance reasons,
	 * The compiled expression put in a cache. Repeated compile of the same
	 * expression will then used the cached value.
	 */
	private static Map compiledExprCache = Collections.synchronizedMap( new LinkedHashMap( EXPR_CACHE_SIZE,
			(float) 0.75,
			true ) {

		private static final long serialVersionUID = 54331232145454L;

		protected boolean removeEldestEntry( Map.Entry eldest )
		{
			return size( ) > EXPR_CACHE_SIZE;
		}
	} );

	/**
	 * whether the expression is column reference
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isColumnExpression( String expression )
	{
		boolean isColumn = false;
		if ( expression == null || expression.trim( ).length( ) == 0 )
			return isColumn;
		if ( compiledExprCache.containsKey( expression ) )
			return ( (Boolean) compiledExprCache.get( expression ) ).booleanValue( );
		Context context = Context.enter( );
		AstRoot tree;
		try
		{
			CompilerEnvirons m_compilerEnv = new CompilerEnvirons( );
			m_compilerEnv.initFromContext( context );
			Parser p = new Parser( m_compilerEnv, context.getErrorReporter( ) );
			tree = p.parse( expression, null, 0 );
		}
		catch ( Exception e )
		{
			compiledExprCache.put( expression, Boolean.valueOf( false ) );
			return false;
		}
		finally
		{
			Context.exit( );
		}

		if ( tree.getFirstChild( ) == tree.getLastChild( ) )
		{
			// A single expression
			if ( tree.getFirstChild( ).getType( ) != Token.EXPR_RESULT
					&& tree.getFirstChild( ).getType( ) != Token.EXPR_VOID
					&& tree.getFirstChild( ).getType( ) != Token.BLOCK )
			{
				isColumn = false;
			}
			Node exprNode = tree.getFirstChild( );
			Node child = exprNode.getFirstChild( );
			assert ( child != null );
			if ( child.getType( ) == Token.GETELEM
					|| child.getType( ) == Token.GETPROP )
				isColumn = getDirectColRefExpr( child );
			else
				isColumn = false;
		}
		else
		{
			isColumn = false;
		}

		compiledExprCache.put( expression, Boolean.valueOf( isColumn ) );
		return isColumn;
	}

	/**
	 * replace the row[], row.xx with dataSetRow[],dataSetRow.xx
	 * 
	 * @param refNode
	 * @return
	 */
	public static String getReplacedColRefExpr( String columnStr )
	{
		if ( isColumnExpression( columnStr ) )
		{
			return columnStr.replaceFirst( "\\Qrow\\E", "dataSetRow" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
			return columnStr;
	}

	/**
	 * if the Node is row Node, return true
	 * 
	 * @param refNode
	 * @return
	 */
	private static boolean getDirectColRefExpr( Node refNode )
	{
		assert ( refNode.getType( ) == Token.GETPROP || refNode.getType( ) == Token.GETELEM );

		Node rowName = refNode.getFirstChild( );
		assert ( rowName != null );
		if ( rowName.getType( ) != Token.NAME )
			return false;

		String str = rowName.getString( );
		assert ( str != null );
		if ( !str.equals( STRING_ROW ) )
			return false;

		Node rowColumn = rowName.getNext( );
		assert ( rowColumn != null );

		if ( refNode.getType( ) == Token.GETPROP
				&& rowColumn.getType( ) == Token.STRING )
		{
			return true;
		}
		else if ( refNode.getType( ) == Token.GETELEM )
		{
			if ( rowColumn.getType( ) == Token.NUMBER
					|| rowColumn.getType( ) == Token.STRING )
				return true;
		}

		return false;
	}

	/**
	 * Gets the proper expression for the given model
	 * 
	 * @param model
	 *            the given model
	 * @return Returns the proper expression for the given model, or null if no
	 *         proper one exists
	 */
	public static String getExpression( Object model,
			IExpressionConverter converter )
	{
		if ( model instanceof ComputedColumnHandle )
		{
			if ( DEUtil.isBindingCube( ( (ComputedColumnHandle) model ).getElementHandle( ) ) )
			{
				return getDataExpression( ( (ComputedColumnHandle) model ).getName( ),
						converter );
			}
			else
			{
				return getColumnExpression( ( (ComputedColumnHandle) model ).getName( ),
						converter );
			}

		}
		else if ( model instanceof ResultSetColumnHandle )
		{
			return getDataSetRowExpression( ( (ResultSetColumnHandle) model ).getColumnName( ),
					converter );
		}
		else if ( model instanceof ParameterHandle )
		{
			return getParameterExpression( ( (ParameterHandle) model ).getQualifiedName( ),
					converter );
		}
		return null;
	}

	public static String getFilterExpression( Object model, String value,
			IExpressionConverter converter )
	{
		if ( model instanceof CubeHandle || model instanceof HierarchyHandle)
		{
			return getDataSetRowExpression( value, converter );
		}
		else
		{
			return getColumnExpression( value, converter );
		}
	}

	public static String getDataSetRowExpression( String columnName,
			IExpressionConverter converter )
	{
		if ( StringUtil.isBlank( columnName ) || converter == null )
		{
			return null;
		}
		return converter.getResultSetColumnExpression( columnName );
	}

	public static String getParameterExpression( String columnName,
			IExpressionConverter converter )
	{
		if ( StringUtil.isBlank( columnName ) || converter == null )
		{
			return null;
		}
		return converter.getParameterExpression( columnName );
	}

	/**
	 * Create a row expression base on a binding column name.
	 * 
	 * @param columnName
	 *            the column name
	 * @return the expression, or null if the column name is blank.
	 */
	public static String getColumnExpression( String columnName,
			IExpressionConverter converter )
	{
		if ( StringUtil.isBlank( columnName ) || converter == null )
		{
			return null;
		}
		return converter.getBindingExpression( columnName );
	}

	public static String getDataExpression( String columnName,
			IExpressionConverter converter )
	{
		if ( StringUtil.isBlank( columnName ) || converter == null )
		{
			return null;
		}
		return converter.getCubeBindingExpression( columnName );
	}

	/**
	 * Create a row expression base on a result set column name.
	 * 
	 * @param columnName
	 *            the column name
	 * @return the expression, or null if the column name is blank.
	 */
	public static String getResultSetColumnExpression( String columnName,
			IExpressionConverter converter )
	{
		if ( StringUtil.isBlank( columnName ) || converter == null )
		{
			return null;
		}
		return converter.getResultSetColumnExpression( columnName );
	}

	public static IExpressionConverter getExpressionConverter( String scriptType )
	{
		IExpressionSupport[] exts = ExpressionSupportManager.getExpressionSupports( );

		if ( exts != null )
		{
			for ( IExpressionSupport ex : exts )
			{
				if ( ex != null
						&& ex.getName( ) != null
						&& ex.getName( ).equals( scriptType ) )
					return ex.getConverter( );
			}
		}

		return null;
	}

	public static void setBindingColumnExpression( Object element,
			ComputedColumn bindingColumn )
	{
		setBindingColumnExpression( element, bindingColumn, false );
	}

	public static void setBindingColumnExpression( Object element,
			ComputedColumn bindingColumn, boolean isOnlySupportJS )
	{
		String defaultScriptType = UIUtil.getDefaultScriptType( );
		IExpressionConverter converter = ExpressionUtility.getExpressionConverter( defaultScriptType );
		String expression = null;
		if ( converter != null && !isOnlySupportJS )
		{
			expression = ExpressionUtility.getExpression( element, converter );
		}
		else
		{
			defaultScriptType = ExpressionType.JAVASCRIPT;
			expression = DEUtil.getExpression( element );
		}

		Expression bindingExpression = new Expression( expression,
				defaultScriptType );
		bindingColumn.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
				bindingExpression );
	}
}
