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
package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.List;

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.expression.AggregateExpression;
import org.eclipse.birt.data.engine.expression.AggregationConstantsUtil;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ConstantExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Aggregation registry implemenation
 */
final class AggrRegistry implements AggregateRegistry
{
	private int groupLevel; // current group level
	private boolean isDetailedRow;
	private Context cx;
	
	private BaseQuery baseQuery;

	private List groupDefns;
	private int groupCount;
	private Scriptable scope;
	
	private int runStates;
	
	private List aggrExprInfoList;
	
	private static int PREPARED_QUERY = 1;
	private static int BASE_QUERY = 2;
	
	/**
	 * @param groupLevel
	 * @param isDetailedRow
	 * @param cx
	 */
	AggrRegistry( int groupLevel, boolean isDetailedRow, Context cx )
	{
		this.groupLevel = groupLevel;
		this.isDetailedRow = isDetailedRow;
		this.cx = cx;
	}

	/**
	 * @param groupDefns
	 * @param scope
	 * @param baseQuery
	 * @param aggrExprInfoList
	 */
	void prepare( List groupDefns, Scriptable scope, BaseQuery baseQuery,
			List aggrExprInfoList )
	{
		this.groupDefns = groupDefns;
		this.scope = scope;
		this.baseQuery = baseQuery;
		this.aggrExprInfoList = aggrExprInfoList;

		if ( baseQuery == null )
			this.runStates = PREPARED_QUERY;
		else
			this.runStates = BASE_QUERY;
		
		if ( groupDefns != null )
			this.groupCount = groupDefns.size( );
	}
	
	/**
	 * Register the aggregate expression into aggregate table, get the only
	 * aggregate id.
	 */
	public int register( AggregateExpression expr ) throws DataException
	{
		return registerExpression( expr, groupLevel, isDetailedRow, cx );
	}

	/**
	 * Registers one aggregate expression. Returns an ID for the registered aggregate.
	 * If an equivalent aggregate expression had been previously registered, the ID
	 * of the existing expression is returned.
	 * @param expr aggregate expression
	 * @param groupLevel
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	private int registerExpression( AggregateExpression expr,
			int groupLevel, boolean isDetailedRow, Context cx )
			throws DataException
	{
		AggrExprInfo info = newAggrExprInfo( expr,
				groupLevel,
				isDetailedRow,
				cx );

		// See if an existing aggregate expression is equivalent to this one
		int id;
		for ( id = 0; id < aggrExprInfoList.size( ); id++ )
		{
			if ( info.equals( aggrExprInfoList.get( id ) ) )
				break;
		}

		if ( id == aggrExprInfoList.size( ) )
			aggrExprInfoList.add( info );

		expr.setRegId( id );

		return id;
	}

	/**
	 * Creates a AggrExprInfo structure from the compiler's AggregateExpression output class
	 * @param expr
	 * @param currentGroupLevel
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	private AggrExprInfo newAggrExprInfo( AggregateExpression expr,
			int currentGroupLevel, boolean isDetailedRow, Context cx )
			throws DataException
	{
		AggrExprInfo aggr = new AggrExprInfo( );
		assert expr != null;
		assert currentGroupLevel >= 0;

		aggr.aggregation = expr.getAggregation( );
		List exprArgs = expr.getArguments( );

		// Find out how many fixed arguments this aggregate function takes
		// Optional filter and group arguments follow the fixed arguments
		int nFixedArgs = aggr.aggregation.getParameterDefn( ).length;

		// Verify that the expression has the right # of arguments
		int nArgs = exprArgs.size( );
		if ( nArgs < nFixedArgs || nArgs > nFixedArgs + 2 )
		{
			DataException e = new DataException( ResourceConstants.INVALID_AGGR_PARAMETER,
					aggr.aggregation.getName( ) );
			throw e;
		}
		
		// Determine grouping level for this aggregate
		// Look at the group level argument. If it is not present, or is null, the group level
		// is the same group in which the aggregate expression is defined.
		aggr.groupLevel = currentGroupLevel;
		if ( nArgs == nFixedArgs + 2 )
		{
			CompiledExpression groupExpr = (CompiledExpression) exprArgs.get( nArgs - 1 );
			if ( !( groupExpr instanceof ConstantExpression ) )

			{
				DataException e = new DataException( ResourceConstants.INVALID_AGGR_GROUP_EXPRESSION,
						aggr.aggregation.getName( ) );
				throw e;
			}

			// Note that we use the data engine's shared scope for this
			// evaluation. Group expression
			// is not expected to depend on any query execution. In fact it
			// should just be a constant
			// expression most of the case
			Object groupLevelObj = groupExpr.evaluate( cx,
					runStates == BASE_QUERY ? cx.initStandardObjects( )
							: scope );
			if ( groupLevelObj == null )
			{
				// null argument; use default level
			}
			else if ( groupLevelObj instanceof String )
			{
				int innerMostGroup = 0;
				if ( runStates == PREPARED_QUERY )
				{
					innerMostGroup = groupCount;
				}
				else
				{
					innerMostGroup = baseQuery.getGrouping( ) != null
							? baseQuery.getGrouping( ).length : 0;
				}	
				int groupLevel = AggregationConstantsUtil.getGroupLevel( (String)groupLevelObj, currentGroupLevel,innerMostGroup, isDetailedRow);
				//When the groupLevelObj can be recognized, it will return a non-negative value.Else return -1.
				if ( groupLevel != -1 )
				{
					aggr.groupLevel = groupLevel;
				}
				else
				{
					aggr.groupLevel = ( runStates == BASE_QUERY )
							? getGroupIndex( groupLevelObj.toString( ) )
							: getGroupIndexFromPreparedQuery( (String) groupLevelObj );
				}
			}
			else if ( groupLevelObj instanceof Number )
			{
				int offset = ( (Number) groupLevelObj ).intValue( );
				if ( offset < 0 )
					aggr.groupLevel = currentGroupLevel + offset;
				else
					aggr.groupLevel = offset;
			}

			if ( aggr.groupLevel < 0
					|| aggr.groupLevel > ( runStates == BASE_QUERY
							? ( baseQuery.getGrouping( ) == null ? 0
									: baseQuery.getGrouping( ).length )
							: groupCount ) )
			{
				DataException e = new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
						aggr.aggregation.getName( ) );
				throw e;
			}
		}

		// Extract filter parameter
		if ( nArgs > nFixedArgs )
		{
			aggr.filter = (CompiledExpression) exprArgs.get( nFixedArgs );
			// If filter expression is a constant "null", ignore it
			if ( aggr.filter instanceof ConstantExpression
					&& ( (ConstantExpression) aggr.filter ).getValue( ) == null )
			{
				aggr.filter = null;
			}
		}

		aggr.args = new CompiledExpression[nFixedArgs];
		if ( nFixedArgs > 0 )
		{
			exprArgs.subList( 0, nFixedArgs ).toArray( aggr.args );
		}
		return aggr;
	}
	
	/**
	 * 
	 * Finds a group given a text identifier of a group. Returns index of group
	 * found (1 = outermost group, 2 = second level group etc.). The text
	 * identifier can be the group name, the group key column name, or the group
	 * key expression text. Returns -1 if no matching group is found
	 * 
	 * @param groupText
	 * @return
	 */
	private int getGroupIndexFromPreparedQuery( String groupText )
	{
		assert groupText != null;
		assert groupDefns != null;

		for ( int i = 0; i < groupDefns.size( ); i++ )
		{
			IGroupDefinition group = (IGroupDefinition) groupDefns.get( i );
			if ( groupText.equals( group.getName( ) )
					|| groupText.equals( group.getKeyColumn( ) )
					|| groupText.equals( group.getKeyExpression( ) ) )
			{
				return i + 1; // Note that group index is 1-based
			}
		}
		return -1;
	}
	
	/**
	 * Return the index of group according to the given group text.
	 * @param groupText
	 * @return The index of group 
	 */
	private int getGroupIndex( String groupText )
	{
		assert groupText != null;
		
		GroupSpec[] groups = baseQuery.getGrouping( );
		for ( int i = 0; i < groups.length; i++ )
		{
			GroupSpec group = groups[i];
			if ( groupText.equals( group.getName( ) )
					|| groupText.equals( group.getKeyColumn( ) ) )
			{
				return i + 1; // Note that group index is 1-based
			}
		}
		return -1;
	}
	
}
