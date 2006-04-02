/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.aggregation.IAggregation;
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
 * A registry of aggregate expressions. Stores all aggregate expressions that appears
 * in a report query or subquery
 */
public class AggregateTable
{
	/**
	 * the base query contains aggregate
	 */
	private BaseQuery baseQuery;
	/**
	 * Array of AggrExprInfo objects to record all aggregates
	 */
	private List aggrExprInfoList = new ArrayList( );

	// log instance
	protected static Logger logger = Logger.getLogger( AggregateTable.class.getName( ) );

	private int runStates;
	
	private static int PREPARED_QUERY = 1;
	private static int BASE_QUERY = 2;
	
	private int groupCount;
	private List groupDefns;
	private Scriptable scope;
	
	/**
	 * construct the aggregateTable from preparedQuery
	 * @param query
	 */
	public AggregateTable( Scriptable scope, List groupDefns )
	{
		logger.entering( AggregateTable.class.getName( ), "AggregateTable" );
		logger.exiting( AggregateTable.class.getName( ), "AggregateTable" );
		
		this.groupDefns = groupDefns;
		this.groupCount = groupDefns.size( );		
		this.scope = scope;
		
		runStates = PREPARED_QUERY;
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
		
		for ( int i = 0; i < groupDefns.size(); i++)
		{
			IGroupDefinition group = (IGroupDefinition) groupDefns.get(i);
			if ( groupText.equals( group.getName()) ||
				 groupText.equals( group.getKeyColumn() ) ||
				 groupText.equals( group.getKeyExpression()) )
			 {
				return i + 1;			// Note that group index is 1-based
			 }
		}
		return -1;
	}
	
	/**
	 * construct the aggregateTable from baseQuery
	 * @param query
	 */
	public AggregateTable( BaseQuery query )
	{
		logger.entering( AggregateTable.class.getName( ), "AggregateTable" );
		this.baseQuery = query;
		logger.exiting( AggregateTable.class.getName( ), "AggregateTable" );
		
		runStates = BASE_QUERY;

	}
	
	/**
	 * Used for de-serialization
	 */
	public AggregateTable( )
	{
	}

	/**
	 * Returns an implementation of the AggregateRegistry interface used by
	 * ExpressionCompiler, to register aggregate expressions at the specified
	 * grouping level
	 * 
	 * @param groupLevel
	 * @param afterGroup
	 * @param cx
	 * @return
	 */
	public AggregateRegistry getAggrRegistry( final int groupLevel,
			final boolean afterGroup, final boolean isDetailedRow, final Context cx )
	{
		return new AggrRegistry( groupLevel, afterGroup, isDetailedRow, cx );
	}

	/**
	 * Gets information about one aggregate expression in the table, given its index
	 */
	AggrExprInfo getAggrInfo( int i )
	{
		return (AggrExprInfo) aggrExprInfoList.get( i );
	}

	/**
	 * Gets the number of aggregate expressions in the table
	 * @return
	 */
	int getCount( )
	{
		return aggrExprInfoList.size( );
	}

	/**
	 * Return the index of group according to the given group text.
	 * @param groupText
	 * @return The index of group 
	 */
	private int getGroupIndex( String groupText )
	{
		assert groupText != null;
		assert baseQuery != null;

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

	/**
	 * The aggregate registry, register the aggregate expression into aggregate
	 * table
	 *  
	 */
	class AggrRegistry implements AggregateRegistry
	{

		int groupLevel; // current group level
		boolean afterGroup; 
		boolean isDetailedRow;
		Context cx;

		AggrRegistry( int groupLevel, boolean afterGroup, boolean isDetailedRow, Context cx )
		{
			this.groupLevel = groupLevel;
			this.afterGroup = afterGroup;
			this.isDetailedRow = isDetailedRow;
			this.cx = cx;
		}

		/**
		 * Register the aggregate expression into aggregate table, get the only
		 * aggregate id.
		 */
		public int register( AggregateExpression expr ) throws DataException
		{
			return registerExpression( expr, groupLevel, afterGroup, isDetailedRow, cx );
		}

		/**
		 * Registers one aggregate expression. Returns an ID for the registered aggregate.
		 * If an equivalent aggregate expression had been previously registered, the ID
		 * of the existing expression is returned.
		 * @param expr aggregate expression
		 * @param groupLevel
		 * @param afterGroup
		 * @param cx
		 * @return
		 * @throws DataException
		 */
		private int registerExpression( AggregateExpression expr,
				int groupLevel, boolean afterGroup, boolean isDetailedRow, Context cx )
				throws DataException
		{
			logger.entering( AggregateTable.class.getName( ),
					"registerExpression" );
			AggrExprInfo info = newAggrExprInfo( expr,
					groupLevel,
					afterGroup,
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

			logger.exiting( AggregateTable.class.getName( ),
					"registerExpression" );
			return id;
		}

		/**
		 * Creates a AggrExprInfo structure from the compiler's AggregateExpression output class
		 * @param expr
		 * @param currentGroupLevel
		 * @param afterGroup
		 * @param cx
		 * @return
		 * @throws DataException
		 */
		private AggrExprInfo newAggrExprInfo( AggregateExpression expr,
				int currentGroupLevel, boolean afterGroup, boolean isDetailedRow, Context cx )
				throws DataException
		{
			AggrExprInfo aggr = new AggrExprInfo( );
			assert expr != null;
			assert currentGroupLevel >= 0;

			aggr.aggregation = expr.getAggregation( );
			aggr.afterGroup = afterGroup;
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
				logger.logp( Level.FINE,
						AggregateTable.class.getName( ),
						"newAggrExprInfo",
						"Wrong number of arguments to aggregate function",
						e );
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
					logger.logp( Level.FINE,
							AggregateTable.class.getName( ),
							"newAggrExprInfo",
							"Invalid grouping expression for aggregate function",
							e );
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
					logger.logp( Level.FINE,
							AggregateTable.class.getName( ),
							"newAggrExprInfo",
							"Invalid grouping level for aggregate function",
							e );
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
	}

	/**
	 * Describes one aggregate expression This info contains the full
	 * information about one aggregate expression
	 */
	class AggrExprInfo
	{
		// Aggregate function
		IAggregation aggregation;
		// Grouping level of the aggr expression. 0 = entire list, 1 = outermost
		// group etc.
		int groupLevel = -1;
		// Whether expression is required only at end of group
		boolean afterGroup = false;
		// Filtering condition for the aggregate
		CompiledExpression filter;
		// Arguments to the aggregate function
		CompiledExpression[] args;

		/**
		 *  Compares equivalency of two aggregate expressions
		 */
		public boolean equals( Object other )
		{
			if ( other == null || !( other instanceof AggrExprInfo ) )
				return false;
			AggrExprInfo rhs = (AggrExprInfo) other;
			if ( aggregation != rhs.aggregation
					|| groupLevel != rhs.groupLevel
					|| args.length != rhs.args.length
					|| afterGroup != rhs.afterGroup )
				return false;

			if ( filter == null )
			{
				if ( rhs.filter != null )
					return false;
			}
			else
			{
				if ( !filter.equals( rhs.filter ) )
					return false;
			}

			for ( int i = 0; i < args.length; i++ )
				if ( !args[i].equals( rhs.args[i] ) )
					return false;
			return true;
		}
	}
}