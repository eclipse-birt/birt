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

package org.eclipse.birt.core.data;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.core.exception.CoreException;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.Token;

/**
 * 
 */

class OlapExpressionCompiler
{
	/**
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	public static String getReferencedMeasure( String expr )
	{
		if ( expr == null )
			return null;
		try
		{
			Context cx = Context.enter( );
			CompilerEnvirons ce = new CompilerEnvirons( );
			Parser p = new Parser( ce, cx.getErrorReporter( ) );
			ScriptOrFnNode tree = p.parse( expr, null, 0 );

			return getScriptObjectName( tree, "measure" );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * 
	 * @param expr
	 * @param bindings
	 * @param onlyFromDirectReferenceExpr
	 * @return
	 * @throws DataException
	 */
	public static Set<IDimLevel> getReferencedDimLevel( String expr )
			throws CoreException
	{
		if ( expr == null  )
			return new HashSet<IDimLevel>( );
		try
		{
			Set<IDimLevel> result = new HashSet<IDimLevel>( );
			Context cx = Context.enter( );
			CompilerEnvirons ce = new CompilerEnvirons( );
			Parser p = new Parser( ce, cx.getErrorReporter( ) );
			ScriptOrFnNode tree = p.parse( expr, null, 0 );

			populateDimLevels( null,
					tree,
					result );
			return result;
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * 
	 * @param n
	 * @param result
	 * @param bindings
	 * @param onlyFromDirectReferenceExpr
	 * @throws DataException
	 */
	private static void populateDimLevels( Node grandpa, Node n, Set<IDimLevel> result )
			throws CoreException
	{
		if ( n == null )
			return;

		if ( n.getFirstChild( ) != null
				&& ( n.getType( ) == Token.GETPROP || n.getType( ) == Token.GETELEM ) )
		{
			if ( n.getFirstChild( ).getFirstChild( ) != null
					&& ( n.getFirstChild( ).getFirstChild( ).getType( ) == Token.GETPROP || n.getFirstChild( )
							.getFirstChild( )
							.getType( ) == Token.GETELEM ) )
			{
				Node dim = n.getFirstChild( ).getFirstChild( );
				if ( "dimension".equals( dim.getFirstChild( ).getString( ) ) )
				{
					String dimName = dim.getLastChild( ).getString( );
					String levelName = dim.getNext( ).getString( );
					String attr = n.getLastChild( ).getString( );

					DimLevel dimLevel = new DimLevel( dimName, levelName, attr );
					if ( !result.contains( dimLevel ) )
						result.add( dimLevel );
				}
			}
			else if ( n.getFirstChild( ) != null
					&& n.getFirstChild( ).getType( ) == Token.NAME )
			{
				if ( "dimension".equals( n.getFirstChild( ).getString( ) ) )
				{
					if ( n.getLastChild( ) != null && n.getNext( ) != null )
					{
						String dimName = n.getLastChild( ).getString( );
						String levelName = n.getNext( ).getString( );
						String attr = null;
						if ( grandpa != null
								&& grandpa.getNext( ) != null
								&& grandpa.getNext( ).getType( ) == Token.STRING )
						{
							attr = grandpa.getNext( ).getString( );
						}
						DimLevel dimLevel = new DimLevel( dimName,
								levelName,
								attr );
						if ( !result.contains( dimLevel ) )
							result.add( dimLevel );
					}
				}
			}
		}
		populateDimLevels( grandpa,
				n.getFirstChild( ),
				result );
		populateDimLevels( grandpa,
				n.getLastChild( ),
				result );
	}

	/**
	 * 
	 * @param n
	 * @param objectName
	 * @return
	 */
	private static String getScriptObjectName( Node n, String objectName )
	{
		if ( n == null )
			return null;
		String result = null;
		if ( n.getType( ) == Token.NAME )
		{
			if ( objectName.equals( n.getString( ) ) )
			{
				Node dimNameNode = n.getNext( );
				if ( dimNameNode == null
						|| dimNameNode.getType( ) != Token.STRING )
					return null;

				return dimNameNode.getString( );
			}
		}

		result = getScriptObjectName( n.getFirstChild( ), objectName );
		if ( result == null )
			result = getScriptObjectName( n.getLastChild( ), objectName );

		return result;
	}
}
