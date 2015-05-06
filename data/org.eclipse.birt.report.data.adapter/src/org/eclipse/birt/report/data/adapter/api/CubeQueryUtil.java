/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

/**
 * 
 */

public class CubeQueryUtil
{

	/**
	 * 
	 * @param targetLevel
	 * @param bindingExpr
	 * @param bindings
	 * @param rowEdgeExprList
	 * @param columnEdgeExprList
	 * @return
	 * @throws AdapterException
	 */
	public static List getReferencedLevels( String targetLevel,
			String bindingExpr, List bindings, List rowEdgeExprList,
			List columnEdgeExprList ) throws AdapterException
	{
		try
		{
			List result = new ArrayList( );
			DimensionLevel target = getTargetDimLevel( targetLevel );
			String bindingName = getReferencedScriptObject( bindingExpr, "data" );
			if ( bindingName == null )
				return result;
			IBinding binding = null;
			for ( int i = 0; i < bindings.size( ); i++ )
			{
				IBinding bd = (IBinding) bindings.get( i );
				if ( bd.getBindingName( ).equals( bindingName ) )
				{
					binding = bd;
					break;
				}
			}

			if ( binding == null )
			{
				return result;
			}

			List aggrOns = binding.getAggregatOns( );
			boolean isMeasure = false;
			if ( aggrOns.size( ) == 0 )
			{
				isMeasure = getReferencedScriptObject( binding.getExpression( ),
						"measure" ) != null;
				if ( !isMeasure )
				{
					// is derived measure?
					isMeasure = getReferencedScriptObject( binding.getExpression( ),
							"data" ) != null;
				}
			}

			int candidateEdge = getAxisQualifierEdgeType( rowEdgeExprList,
					columnEdgeExprList,
					target );

			if ( candidateEdge == -1 )
				return result;

			if ( isMeasure )
			{
				switch ( candidateEdge )
				{
					case ICubeQueryDefinition.ROW_EDGE :
						populateLevels( rowEdgeExprList, result );
						break;
					case ICubeQueryDefinition.COLUMN_EDGE :
						populateLevels( columnEdgeExprList, result );
						break;
				}
			}
			else
			{
				switch ( candidateEdge )
				{
					case ICubeQueryDefinition.ROW_EDGE :
						populateAxisLevels( aggrOns, rowEdgeExprList, result );
						populateAnotherAxisLevels( aggrOns,
								columnEdgeExprList,
								result,
								targetLevel );
						break;
					case ICubeQueryDefinition.COLUMN_EDGE :
						populateAxisLevels( aggrOns, columnEdgeExprList, result );
						populateAnotherAxisLevels( aggrOns,
								rowEdgeExprList,
								result,
								targetLevel );
						break;
				}
			}
			return result;
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * 
	 * @param targetLevel
	 * @param bindingExpr
	 * @param bindings
	 * @param rowEdgeExprList
	 * @param columnEdgeExprList
	 * @return
	 * @throws AdapterException
	 */
	public static List getReferencedLevelsForLinkedCube( String targetLevel,
			String bindingExpr, List bindings, List rowEdgeExprList,
			List columnEdgeExprList ) throws AdapterException
	{
		return getReferencedLevels( targetLevel,
				bindingExpr,
				bindings,
				rowEdgeExprList,
				columnEdgeExprList );
	}
	
	/**
	 * Get all aggregation binding from <code>bindings</code> 
	 * @param bindings: input bindings
	 * @return aggregation bindings 
	 * @throws AdapterException
	 */
	public static IBinding[] getAggregationBindings( IBinding[] bindings ) throws AdapterException
	{
		assert bindings != null;
		List<IBinding> result = new ArrayList<IBinding>( );
		for ( IBinding b : bindings )
		{
			try
			{
				if ( b.getAggrFunction( ) != null )
				{
					result.add( b );
				}
			}
			catch ( DataException e )
			{
				throw new AdapterException( e.getLocalizedMessage( ), e );
			}
		}
		return result.toArray( new IBinding[0] );
	}
	
	/**
	 * Populate axis levels to the <code>result</code> for the aggregate on
	 * levels only if they are on the specified level.
	 * 
	 * @param aggrOns
	 * @param edgeExprList
	 * @param result
	 * @throws AdapterException
	 */
	private static void populateAxisLevels( List aggrOns, List edgeExprList,
			List result ) throws AdapterException
	{
		for ( int i = 0; i < aggrOns.size( ); i++ )
		{
			final String levelExpr = aggrOns.get( i ).toString( );
			if ( isAxisQualifierLevel( levelExpr, edgeExprList ) )
			{
				result.add( getTargetDimLevel( levelExpr ) );
			}
		}
	}
	
	private static void populateAnotherAxisLevels( List aggrOns, List edgeExprList,
			List result, String targetLevel ) throws AdapterException
	{
		boolean issubLevel = false;
		for ( int i = 0; i < aggrOns.size( ); i++ )
		{
			final String levelExpr = aggrOns.get( i ).toString( );
			if ( levelExpr.equals( targetLevel ) )
			{
				issubLevel = true;
			}
			if ( isAxisQualifierLevel( levelExpr, edgeExprList )
					&& isSubLevel( levelExpr, targetLevel, issubLevel ) )
			{
				result.add( getTargetDimLevel( levelExpr ) );
			}
		}
	}
	
	private static boolean isSubLevel( String levelExpr, String targetLevel,
			boolean issubLevel ) throws AdapterException
	{
		if ( levelExpr.equals( targetLevel ) )
		{
			return false;
		}
		return issubLevel;
	}

	/**
	 * 
	 * @param levelExpr
	 * @param rowEdgeExprList
	 * @return
	 */
	private static boolean isAxisQualifierLevel( String levelExpr,
			List rowEdgeExprList )
	{
		for ( Iterator i = rowEdgeExprList.iterator( ); i.hasNext( ); )
		{
			String expr = (String) i.next( );
			if ( expr.equals( levelExpr ) )
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param levelExprList
	 * @param result
	 * @throws AdapterException 
	 */
	private static void populateLevels( List levelExprList, List result )
			throws AdapterException
	{
		for ( Iterator i = levelExprList.iterator( ); i.hasNext( ); )
		{
			String levelExpr = (String) i.next( );
			result.add( getTargetDimLevel( levelExpr ) );
		}
	}

	/**
	 * 
	 * @param rowEdgeList
	 * @param columnEdgeList
	 * @param target
	 * @return
	 * @throws AdapterException
	 */
	private static int getAxisQualifierEdgeType( List rowEdgeList,
			List columnEdgeList, DimensionLevel target ) throws AdapterException
	{
		if ( rowEdgeList != null )
		{
			for ( Iterator i = rowEdgeList.iterator( ); i.hasNext( ); )
			{
				String levelExpr = (String) i.next( );
				DimensionLevel level = getTargetDimLevel( levelExpr );
				if ( target.getDimensionName( )
						.equals( level.getDimensionName( ) ) )
				{
					return ICubeQueryDefinition.COLUMN_EDGE;
				}
			}
		}
		if ( columnEdgeList != null )
		{
			for ( Iterator i = columnEdgeList.iterator( ); i.hasNext( ); )
			{
				String levelExpr = (String) i.next( );
				DimensionLevel level = getTargetDimLevel( levelExpr );
				if ( target.getDimensionName( )
						.equals( level.getDimensionName( ) ) )
				{
					return ICubeQueryDefinition.ROW_EDGE;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Get referenced Script Object (dimension, data, measure, etc) according to
	 * given object name.
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	private static String getReferencedScriptObject( IBaseExpression expr,
			String objectName )
	{
		if ( expr instanceof IScriptExpression )
		{
			return getReferencedScriptObject( ( (IScriptExpression) expr ),
					objectName );
		}
		else if ( expr instanceof IConditionalExpression )
		{
			String dimName = null;
			IScriptExpression expr1 = ( (IConditionalExpression) expr ).getExpression( );
			dimName = getReferencedScriptObject( expr1, objectName );
			if ( dimName != null )
				return dimName;
			IBaseExpression op1 = ( (IConditionalExpression) expr ).getOperand1( );
			dimName = getReferencedScriptObject( op1, objectName );
			if ( dimName != null )
				return dimName;

			IBaseExpression op2 = ( (IConditionalExpression) expr ).getOperand2( );
			dimName = getReferencedScriptObject( op2, objectName );
			return dimName;
		}

		return null;
	}
	
	/**
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	private static String getReferencedScriptObject( IScriptExpression expr,
			String objectName )
	{
		if ( expr == null )
			return null;
		else
			return getReferencedScriptObject( expr.getText( ), objectName );
	}

	/**
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	private static String getReferencedScriptObject( String expr,
			String objectName )
	{
		if ( expr == null )
			return null;
		try
		{
			Context cx = Context.enter( );
			CompilerEnvirons ce = new CompilerEnvirons( );
			Parser p = new Parser( ce, cx.getErrorReporter( ) );
			AstRoot tree = p.parse( expr, null, 0 );

			return getScriptObjectName( tree, objectName );
		}
		finally
		{
			Context.exit( );
		}
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

	/**
	 * 
	 * @param expr
	 * @return
	 */
	private static String[] getTargetLevel( String expr )
	{
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ) )
			return null;

		expr = expr.replaceFirst( "\\Qdimension\\E", "" );
		String[] result = expr.split( "\\Q\"][\"\\E" );
		result[0] = result[0].replaceAll( "\\Q[\"\\E", "" );
		result[1] = result[1].replaceAll( "\\Q\"]\\E", "" );
		return result;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	private static DimensionLevel getTargetDimLevel( String expr )
			throws AdapterException
	{
		final String[] target = getTargetLevel( expr );
		if ( target == null || target.length < 2 )
		{
			throw new AdapterException( ResourceConstants.INVALID_LEVEL_EXPRESSION, expr );
		}
		return new DimensionLevel( target[0], target[1] );
	}
}
