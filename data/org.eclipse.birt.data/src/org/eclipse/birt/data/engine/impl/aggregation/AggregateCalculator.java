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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.Aggregation;
import org.eclipse.birt.data.engine.api.aggregation.IAggregation;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Calculation engine for aggregate expressions. Does runtime calculation of
 * aggregate expressions that appear in a report query or subquery. Stores
 * values of aggregate expressions.
 */
public class AggregateCalculator
{

	/**
	 * Array to store all calculated aggregate values. aggrValue[i] is a list of
	 * values calculated for expression #i in the associated aggregate table.
	 * The aggrgate values are stored in each list as the cursor advances for
	 * the associated ODI result set.
	 */
	private List[] aggrValues;

	/**
	 * Array to store current argument values to all aggregates argrArgs[i] is
	 * the argument array to aggregate expression #i
	 */
	private Object[][] aggrArgs;

	/**
	 * The table contains all aggregate expression
	 */
	private List aggrExprInfoList;

	// The count of aggregate expression
	private int aggrCount;
	
	// The Odi result
	private IResultIterator odiResult;
	// Whether odiResult has data rows
	private boolean hasOdiResultDataRows;
	
	private AccumulatorManager[] accumulatorManagers;
	
	private Set invalidAggrSet;
	private Map invalidAggrMsg;
	
	// Log constant
	protected static Logger logger = Logger.getLogger( AggregateCalculator.class.getName( ) );

	/**
	 * For the given odi resultset, calcaulate the value of aggregate from
	 * aggregateTable
	 * 
	 * @param aggrTable
	 * @param odiResult
	 */
	public AggregateCalculator( AggregateTable aggrTable,
			IResultIterator odiResult )
	{
		assert aggrTable != null;
		assert odiResult != null;
		
		this.aggrExprInfoList = aggrTable.getAggrExprInfoList( );
		this.odiResult = odiResult;

		aggrCount = aggrExprInfoList.size( );
		
		if ( aggrCount > 0 )
		{
			aggrValues = new List[aggrCount];
			aggrArgs = new Object[aggrCount][];
			for ( int i = 0; i < aggrCount; i++ )
			{
				aggrValues[i] = new ArrayList( );
				AggrExprInfo aggrInfo = getAggrInfo( i );
			
				// Initialize argument array for this aggregate expression
				aggrArgs[i] = new Object[aggrInfo.aggregation.getParameterDefn( ).length];
			}
			accumulatorManagers = new AccumulatorManager[aggrCount];
		}
	}
	
	/**
	 * Makes one pass over the odiResult and calculates values for all aggregate
	 * expressions. odiResult must be open, and cursor placed at first row. Upon
	 * return, odiResult is rewinded to first row. Before calling this method, a
	 * Javascript "row" object must be set up in the passed-in JS context and
	 * bound to the passed in odiResult.
	 */
	public void calculate( Scriptable scope ) throws DataException
	{
		logger.entering( AggregateCalculator.class.getName( ), "calculate" );
		List validAggregations = new ArrayList();
		boolean[] populateAggrValue = new boolean[this.aggrCount];
			int count = 1;
		for( int i = 0; i < this.aggrCount; i++)
		{
			validAggregations.add( new Integer(i));
			if( this.getAggrInfo( i ).aggregation instanceof Aggregation 
					&& ((Aggregation) this.getAggrInfo( i ).aggregation ).getNumberOfPasses( ) > 1)
				populateAggrValue[i] = false;
			else
				populateAggrValue[i] = true;
			accumulatorManagers[i] = new AccumulatorManager( this.getAggrInfo( i ).aggregation );
		}
		
		while ( validAggregations.size( ) > 0 )
		{
			int[] validAggregationArray = new int[validAggregations.size( )];
			for ( int i = 0; i < validAggregations.size( ); i++ )
			{
				validAggregationArray[i] = ( (Integer) validAggregations.get( i ) ).intValue( );
			}
			assert odiResult.getCurrentResultIndex( ) == 0;
			if ( odiResult.getCurrentResult( ) == null )
			{
				// Empty result set; nothing to do
				this.hasOdiResultDataRows = false;
				return;
			}
			this.hasOdiResultDataRows = true;

			pass(scope, populateAggrValue, validAggregationArray);

			// Rewind to first row
			odiResult.first( 0 );

			count++;
			prepareNextIteration(validAggregations, populateAggrValue, count);
		}
		logger.exiting( AggregateCalculator.class.getName( ), "calculate" );
	}

	/**
	 * Gets information about one aggregate expression in the table, given its
	 * index
	 */
	private AggrExprInfo getAggrInfo( int i )
	{
		return (AggrExprInfo) aggrExprInfoList.get( i );
	}
	
	/**
	 * Make a pass to all aggregations. Iterator over entire result set. At each row, call
	 * each aggregate aggregationtion.
	 * 
	 * @param scope
	 * @param populateAggrValue
	 * @param validAggregationArray
	 * @throws DataException
	 */
	private void pass(Scriptable scope, boolean[] populateAggrValue, int[] validAggregationArray) throws DataException {
		do
		{
			int startingGroupLevel = odiResult.getStartingGroupLevel( );
			int endingGroupLevel = odiResult.getEndingGroupLevel( );

			for ( int i = 0; i < validAggregationArray.length; i++ )
			{
				int index = validAggregationArray[i];
				if ( invalidAggrSet != null
						&& invalidAggrSet.contains( new Integer( index ) ) )
				{
					addInvalidAggrMsg( index, endingGroupLevel );
					continue;
				}

				if ( onRow( index,
						startingGroupLevel,
						endingGroupLevel,
						scope,
						populateAggrValue[index] ) == false )
				{
					addInvalidAggrMsg( index, endingGroupLevel );

					if ( invalidAggrSet == null )
						invalidAggrSet = new HashSet( );
					invalidAggrSet.add( new Integer( index ) );
				}
			}
		} while ( odiResult.next( ) );
	}

	/**
	 * 
	 * @param index
	 * @param endingGroupLevel
	 */
	private void addInvalidAggrMsg( int index, int endingGroupLevel )
	{
		assert invalidAggrMsg != null;

		if ( getAggrInfo( index ).aggregation.getType( ) == IAggregation.RUNNING_AGGR
				|| endingGroupLevel <= getAggrInfo( index ).groupLevel )
			aggrValues[index].add( invalidAggrMsg.get( new Integer( index ) ) );
	}
	
	/**
	 * Prepare next run of aggregation pass.
	 * 
	 * @param validAggregations
	 * @param populateAggrValue
	 * @param count
	 */
	private void prepareNextIteration(List validAggregations, boolean[] populateAggrValue, int count) {
		validAggregations.clear( );
		for ( int i = 0; i < this.aggrCount; i++ )
		{
			this.accumulatorManagers[i].restart();
			IAggregation temp = this.getAggrInfo( i ).aggregation;
			populateAggrValue[i] = false;
			if ( temp instanceof Aggregation )
			{
				int passesNumber = ( (Aggregation) temp ).getNumberOfPasses( );
				if ( count <= passesNumber )
				{
					validAggregations.add( new Integer( i ) );
					if( count == passesNumber )
					{
						populateAggrValue[i] = true;
					}
				}
			}
		}
	}

	/**
	 * Get the aggregate value
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	Object getAggregateValue( int aggrIndex ) throws DataException
	{
		logger.entering( AggregateCalculator.class.getName( ),
				"getAggregateValue",
				new Integer( aggrIndex ) );

		assert aggrIndex >= 0 && aggrIndex < this.aggrCount;

		AggrExprInfo aggrInfo = getAggrInfo( aggrIndex );

		if ( !hasOdiResultDataRows )
		{
			if ( ( aggrInfo.aggregation  ).getName( ).equalsIgnoreCase( "COUNT" ) )

				return new Integer( 0 );
			else
				return null;
		}

		try
		{
			/*
			 * if ( aggrInfo.afterGroup ) { if ( odiResult.getEndingGroupLevel() >
			 * aggrInfo.groupLevel ) { // Caller has broken the contract that
			 * this aggregate can only be // requested at the end of a
			 * group.Even though we can furnish the // result given current
			 * implementation, the API requires that we // throw an error
			 * DataException e = new DataException(
			 * ResourceConstants.NOT_END_GROUP ); logger.logp( Level.FINE ,
			 * DataEngineImpl.class.getName( ), "getAggregateValue", "This
			 * aggregate expression is only available at the end of a group", e );
			 * throw e; } }
			 */

			int groupIndex;

			if ( aggrInfo.aggregation.getType( ) == IAggregation.SUMMARY_AGGR )
			{
				if ( aggrInfo.groupLevel == 0 )
					// Aggregate on the whole list: there is only one group
					groupIndex = 0;
				else
					groupIndex = odiResult.getCurrentGroupIndex( aggrInfo.groupLevel );
			}
			else
				groupIndex = odiResult.getCurrentResultIndex( );

			Object value = this.aggrValues[aggrIndex].get( groupIndex );
			logger.exiting( AggregateCalculator.class.getName( ),
					"getAggregateValue",
					value );
		
			return value;

		}
		catch ( DataException e )
		{
			throw e;
		}
	}
	
	/**
	 * Returns a scriptable object that implements the JS "_aggr_value" internal
	 * object
	 */
	public Scriptable getJSAggrValueObject( )
	{
		return new JSAggrValueObject( );
	}

	/**
	 * Calculate the value by row
	 * 
	 * @param aggrIndex
	 * @param startingGroupLevel
	 * @param endingGroupLevel
	 * @param context
	 * @param scope
	 * @throws DataException
	 */
	private boolean onRow( int aggrIndex, int startingGroupLevel,
			int endingGroupLevel, Scriptable scope, boolean populateValue )
			throws DataException
	{
		AggrExprInfo aggrInfo = getAggrInfo( aggrIndex );
		Accumulator acc = null;

		boolean newGroup = false;
		if (startingGroupLevel <= aggrInfo.groupLevel) 
		{
			// A new group starts for this aggregate; call start() on
			// accumulator
			newGroup = true;
			acc = accumulatorManagers[aggrIndex].next( );
			acc.start();
		}
		else 
		{
			acc = accumulatorManagers[aggrIndex].getCurrentAccumulator( );
		}	

		
		// Apply filtering on row
		boolean accepted = true;
		if ( aggrInfo.filter != null )
		{
			try
			{
				Object filterResult = ExprEvaluateUtil.evaluateCompiledExpression( aggrInfo.filter,
						odiResult,
						scope );
				if ( filterResult == null )
					accepted = true;
				else

					accepted = DataTypeUtil.toBoolean( filterResult )
							.booleanValue( );
			}
			catch ( BirtException e )
			{
				logger.logp( Level.FINE,
						DataEngineImpl.class.getName( ),
						"onRow",
						"An error is thrown by DataTypeUtil.",
						e );
				if ( invalidAggrMsg == null )
					invalidAggrMsg = new HashMap( );
				invalidAggrMsg.put( new Integer( aggrIndex ), e );

				return false;
			}
		}

		if ( accepted )
		{
			// Calculate arguments to the aggregate aggregationtion
			boolean[] argDefs = aggrInfo.aggregation.getParameterDefn( );
			assert argDefs.length == aggrArgs[aggrIndex].length;
			try
			{
				for ( int i = 0; i < argDefs.length; i++ )
				{
					// Note that static arguments only need to be calculated
					// once at
					// the
					// start of the iteration
					if ( argDefs[i] || newGroup )
					{
						CompiledExpression argExpr = aggrInfo.args[i];
						aggrArgs[aggrIndex][i] = ExprEvaluateUtil.evaluateCompiledExpression( argExpr,
								odiResult,
								scope );
					}
				}

				acc.onRow( aggrArgs[aggrIndex] );
			}
			catch ( DataException e )
			{
				if ( invalidAggrMsg == null )
					invalidAggrMsg = new HashMap( );
				invalidAggrMsg.put( new Integer( aggrIndex ), e );

				return false;
			}
		}
		// If this is a running aggregate, get value for current row
		boolean isRunning = ( aggrInfo.aggregation.getType( ) == IAggregation.RUNNING_AGGR );
		if ( isRunning && populateValue )
		{
			Object value = acc.getValue( );
			aggrValues[aggrIndex].add( value );
//			assert aggrValues[aggrIndex].size( ) == odiResult.getCurrentResultIndex( ) + 1;
		}

		if ( endingGroupLevel <= aggrInfo.groupLevel )
		{
			// Current group ends for this aggregate; call finish() on
			// accumulator
			acc.finish( );

			// For non-running aggregates, this is the time to call getValue
			if ( (!isRunning) && populateValue )
			{
				Object value = acc.getValue( );
				aggrValues[aggrIndex].add( value );
//				assert aggrInfo.groupLevel == 0
//						? ( aggrValues[aggrIndex].size( ) == 1 )
//						: ( aggrValues[aggrIndex].size( ) == odiResult.getCurrentGroupIndex( aggrInfo.groupLevel ) + 1 );
			}
		}
		return true;
	}
	
	/**
	 * populate the aggregate value
	 * 
	 * @param scope
	 */
	public void populateValue( JSAggrValueObject aggrValue )
	{
		if ( aggrValue == null)
			return;
		for ( int i = 0; i < this.aggrCount; i++ )
		{
			Object value = aggrValue.get( i, aggrValue );
			if ( value != null )
				aggrValues[i].add( value );
		}
	}

	/**
	 * Implements the Javascript object _aggr_value to assist in the evaluation
	 * of expression containing aggregates
	 */
	public class JSAggrValueObject extends ScriptableObject
	{

		/**
		 * serialVersionUID = 1L;
		 */
		private static final long serialVersionUID = 1L;

		/*
		 * @see org.mozilla.javascript.Scriptable#get(int,
		 *      org.mozilla.javascript.Scriptable)
		 */
		public Object get( int index, Scriptable start )
		{
			if ( index < 0 || index >= aggrCount )
			{
				// Should never get here
//				assert false;
				return null;
			}

			try
			{
				return getAggregateValue( index );
			}
			catch ( DataException e )
			{
				throw Context.reportRuntimeError( e.getLocalizedMessage( ) );
			}
		}

		/*
		 * @see org.mozilla.javascript.Scriptable#getClassName()
		 */
		public String getClassName( )
		{
			return "_RESERVED_AGGR_VALUE";
		}

		/*
		 * @see org.mozilla.javascript.Scriptable#has(int,
		 *      org.mozilla.javascript.Scriptable)
		 */
		public boolean has( int index, Scriptable start )
		{
			return index > 0 && index < aggrCount;
		}
	}
	
	/**
	 * A helper class that is used to manage the Accumulators of aggregations. 
	 *
	 */
	class AccumulatorManager{
		//
		private IAggregation aggregation;
		int cursor;
		List cachedAcc;
		
		/**
		 * Constructor.
		 * @param aggregation
		 */
		AccumulatorManager( IAggregation aggregation )
		{
			this.aggregation = aggregation;
			this.cursor = -1;
			cachedAcc = new ArrayList();
		}
		
		/**
		 * Get the current accumulator. 
		 * @return
		 */
		Accumulator getCurrentAccumulator( )
		{
			if( cachedAcc.size() == 0 )
			{
				cachedAcc.add( aggregation.newAccumulator() );
			}
			return (Accumulator)cachedAcc.get(cursor);
		}
		
		/**
		 * Get the next accumulator. If there is no next accumulator, populate one.
		 * @return
		 */
		Accumulator next( )
		{
			cursor++;
			if( cachedAcc.size() > cursor )
			{
				return (Accumulator)cachedAcc.get(cursor);
			}else
			{
				cachedAcc.add( aggregation.newAccumulator() );
				return (Accumulator)cachedAcc.get(cursor);
			}
		}
		
		/**
		 * Reset the cursor to unstart state ( = -1)
		 *
		 */
		void restart( )
		{
			this.cursor = -1;
		}
	}
}
