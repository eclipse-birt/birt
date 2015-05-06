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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.birt.core.exception.CoreException;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

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
	public static Set<String> getReferencedMeasure( String expr )
	{
		if ( expr == null )
			return Collections.EMPTY_SET;
		try
		{
			Set<String> result = new LinkedHashSet<String>( );
			Context cx = Context.enter( );
			CompilerEnvirons ce = new CompilerEnvirons( );
			Parser p = new Parser( ce, cx.getErrorReporter( ) );
			AstRoot tree = p.parse( expr, null, 0 );

			getScriptObjectName( tree, "measure", result );
			
			return result;
		}
		catch( Exception e )
		{
			return Collections.EMPTY_SET;
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
			AstRoot tree = p.parse( expr, null, 0 );

			populateDimLevels( null,
					tree,
					result );
			return result;
		}
		catch( Exception e )
		{
			return Collections.EMPTY_SET;
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
				n.getNext( ),
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
	private static void getScriptObjectName( Node n, String objectName, Set nameSet )
	{
		if ( n == null )
			return;
		String result = null;
		if ( n.getType( ) == Token.NAME )
		{
			if ( objectName.equals( n.getString( ) ) )
			{
				Node dimNameNode = n.getNext( );
				if ( dimNameNode == null
						|| dimNameNode.getType( ) != Token.STRING )
					return;

				nameSet.add( dimNameNode.getString( ) );
			}
		}

		getScriptObjectName( n.getFirstChild( ), objectName, nameSet );
		getScriptObjectName( n.getNext( ), objectName, nameSet );
		getScriptObjectName( n.getLastChild( ), objectName, nameSet );
	}
}
