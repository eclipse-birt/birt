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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.AggrDefnRoundManager;
import org.eclipse.birt.data.engine.executor.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;

/**
 * The entry point of this package.
 */
public class PassManager
{
	//
	private ResultSetPopulator populator;

	private ComputedColumnHelper computedColumnHelper;

	private FilterByRow filterByRow;
	
	private DataEngineSession session;
	/**
	 * Constructor.
	 * 
	 * @param populator
	 */
	private PassManager( ResultSetPopulator populator, DataEngineSession session )
	{
		this.populator = populator;
		this.session = session;
	}

	/**
	 * 
	 * @param populator
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	public static void populateResultSet( ResultSetPopulator populator,
			OdiResultSetWrapper odaResultSet, DataEngineSession session ) throws DataException
	{
		new PassManager( populator, session ).pass( odaResultSet );
	}
	
	/**
	 * Pass the oda result set.
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void pass( OdiResultSetWrapper odaResultSet ) throws DataException
	{
		prepareFetchEventList( );

		PassStatusController psController = new PassStatusController( populator,
				filterByRow,
				computedColumnHelper );

		if ( !psController.needMultipassProcessing( ) )
		{
			doSinglePass( odaResultSet );
		}
		else
		{
			doMultiPass( odaResultSet, psController );
		}
		
		// TODO remove me
		calculateAggregationsInColumnBinding( );

		/************************************/
		// TODO remove me
		// Temp code util model makes the backward comp.
		ExpressionCompiler compiler = new ExpressionCompiler( );
		compiler.setDataSetMode( false );
		for ( Iterator it = this.populator.getEventHandler( )
				.getColumnBindings( )
				.values( )
				.iterator( ); it.hasNext( ); )
		{
			try
			{
				IBinding binding = (IBinding) it.next( );
				compiler.compile( binding.getExpression( ),
						this.populator.getSession( )
								.getEngineContext( ).getScriptContext( ));
			}
			catch ( DataException e )
			{
				// do nothing
			}
		}

		/*************************************/
		//
		populateAggregationInBinding( );
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void populateAggregationInBinding( ) throws DataException
	{
		this.populator.getExpressionProcessor( )
				.setResultIterator( this.populator.getResultIterator( ) );
		this.populator.getResultIterator( ).clearAggrValueHolder( );
		List aggrDefns = this.populator.getEventHandler( ).getAggrDefinitions( );

		AggrDefnRoundManager factory = new AggrDefnRoundManager( aggrDefns );
		for ( int i = 0; i < factory.getRound( ); i++ )
		{
			AggregationHelper helper = new AggregationHelper( factory.getAggrDefnManager( i ),
					this.populator );
			this.populator.getResultIterator( ).addAggrValueHolder( helper );

		}
	}

	/**
	 * 
	 *
	 */
	private void prepareFetchEventList( )
	{
		Object[] fetchEventsList = getFetchEventListFromQuery( this.populator );

		for ( int i = 0; i < fetchEventsList.length; i++ )
		{
			if ( fetchEventsList[i] instanceof ComputedColumnHelper )
			{
				computedColumnHelper = (ComputedColumnHelper) fetchEventsList[i];
			}
			else if ( fetchEventsList[i] instanceof FilterByRow )
			{
				filterByRow = (FilterByRow) fetchEventsList[i];
			}
		}
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void doSinglePass( OdiResultSetWrapper odaResultSet ) throws DataException
	{
		if ( computedColumnHelper != null )
			computedColumnHelper.setModel( TransformationConstants.DATA_SET_MODEL );
		PassUtil.pass( this.populator, odaResultSet, false );
		this.populator.getExpressionProcessor( ).setDataSetMode( false );
		
		removeOnFetchScriptHelper( );
		handleEndOfDataSetProcess( );
		saveDataSetResult( odaResultSet );
	}

	/**
	 * The OnFetchScript should only be calcualted one time.
	 */
	private void removeOnFetchScriptHelper( )
	{
		if ( this.populator.getQuery( ).getFetchEvents( ) == null )
			return;
		for( Iterator it = this.populator.getQuery( ).getFetchEvents( ).iterator( ); it.hasNext( );)
		{
			Object o = it.next( );
			if( o instanceof OnFetchScriptHelper )
			{
				it.remove( );
			}
		}
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param iccState
	 * @param psController
	 * @param stopSign
	 * @throws DataException
	 */
	private void doMultiPass( OdiResultSetWrapper odaResultSet, PassStatusController psController ) throws DataException
	{
		// The data member which record the state of each computed column.
		// The order that computed columns are cached in iccState is significant
		// and only after all
		// the computed columns in higherindex is marked as "avaliable" that the
		// computed columns in lower
		// index can be marked as available
		ComputedColumnsState iccState = null;
		
		if ( computedColumnHelper != null )
		{
			iccState = new ComputedColumnsState( computedColumnHelper );
		}
		doPopulation( odaResultSet, iccState, psController );
	}
	
	/**
	 * Saves the calculated data set result set to cache if needed
	 * @param rsWrapper
	 * @throws DataException
	 */
	private void saveDataSetResult( OdiResultSetWrapper rsWrapper ) throws DataException
	{
		Object resultSource = rsWrapper.getWrappedOdiResultSet( );
		assert resultSource != null;
		if ( resultSource instanceof DataSetToCache )
		{
			DataSetToCache dstc = (DataSetToCache) resultSource;
			IResultIterator itr = populator.getResultIterator( );
			dstc.saveDataSetResult( itr );
		}
	}

	/**
	 * Return the fetch event list from the given query.
	 * 
	 * @param rsp
	 * @return
	 */
	private static Object[] getFetchEventListFromQuery( ResultSetPopulator rsp )
	{
		// Temp variable which is used to store the FetchEvents of a query.
		// If a query does not hava a FetchEvent then return an empty array.
		Object[] fetchEventsList = null;

		if ( rsp.getQuery( ).getFetchEvents( ) == null )
		{
			fetchEventsList = new Object[]{};
		}
		else
		{
			fetchEventsList = rsp.getQuery( ).getFetchEvents( ).toArray( );
		}
		return fetchEventsList;
	}

	/**
	 * Do the actual population job of result set.
	 * 
	 * @param odaResultSet
	 * @param iccState
	 * @param psController
	 * @param stopSign
	 * @throws DataException
	 */
	private void doPopulation( OdiResultSetWrapper odaResultSet,
			ComputedColumnsState iccState, PassStatusController psController )
			throws DataException
	{
		List cachedSorting = Arrays.asList( this.populator.getQuery( )
				.getOrdering( ) == null ? new Object[0]
				: this.populator.getQuery( ).getOrdering( ) );
		this.populator.getQuery( ).setOrdering( new ArrayList( ) );
		/****************************Populate Data Set Rows************************************/

		this.populator.getExpressionProcessor( ).setDataSetMode( true );
		
		populateResultSetCacheInResultSetPopulator( odaResultSet );

		DataSetProcessUtil.doPopulate( this.populator,
				iccState,
				computedColumnHelper,
				filterByRow,
				psController );
	
		handleEndOfDataSetProcess( );
		
		saveDataSetResult( odaResultSet );
		//		
		/****************************Populate Result Set Rows***********************************/
		this.populator.getExpressionProcessor( ).setDataSetMode( false );
		ResultSetProcessUtil.doPopulate( this.populator,
				iccState,
				computedColumnHelper,
				filterByRow,
				psController,
				cachedSorting );
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateResultSetCacheInResultSetPopulator(
			OdiResultSetWrapper odaResultSet ) throws DataException
	{
		int max = 0;
		
		if ( computedColumnHelper != null )
			computedColumnHelper.setModel( TransformationConstants.PRE_CALCULATE_MODEL );

		if ( filterByRow != null )
		{
			filterByRow.setWorkingFilterSet( FilterByRow.NO_FILTER );
		}

		max = this.populator.getQuery( ).getMaxRows( );
		
		if ( filterByRow != null )
			this.populator.getQuery( ).setMaxRows( 0 );
		
		PassUtil.pass( this.populator, odaResultSet, false );
		this.removeOnFetchScriptHelper( );
		this.populator.getQuery( ).setMaxRows( max );
	}

	/**
	 * @throws DataException 
	 * 
	 *
	 */
	private void handleEndOfDataSetProcess( ) throws DataException
	{
		IEventHandler eventHandler = this.populator.getEventHandler( );

		if ( eventHandler != null )
			eventHandler.handleEndOfDataSetProcess( this.populator.getResultIterator( ) );
	}
	
	/**
	 * Calculate the aggregations in column binding. Those aggregation might be explicitly defined
	 * by user, or implicitly defined by engine ( use in highlight, TOC, mapping, etc.)
	 * 
	 * @throws DataException
	 */
	private void calculateAggregationsInColumnBinding( ) throws DataException
	{
		IExpressionProcessor ep = populator.getExpressionProcessor();

		Map results = populator.getEventHandler( ).getColumnBindings( );
	
		DummyICCState iccState = new DummyICCState( results );

		ep.setResultIterator( populator.getResultIterator( ) );
		
		while ( !iccState.isFinish( ) )
		{
			ep.evaluateMultiPassExprOnCmp( iccState, false );
		}
	}
	/**
	 * Class DummyICCState is used by ExpressionProcessor to calculate multipass 
	 * aggregations.
	 *
	 */
	private class DummyICCState implements IComputedColumnsState
	{
		private Object[] exprs;
		private Object[] names;
		private boolean[] isValueAvailable;
		
		/**
		 * 
		 * @param exprs
		 * @param names
		 * @throws DataException 
		 */
		DummyICCState( Map columnMappings ) throws DataException
		{
			this.exprs = columnMappings.values( ).toArray( );
			this.names = columnMappings.keySet( ).toArray( );
			this.isValueAvailable= new boolean[exprs.length];
/*			for( int i = 0; i < exprs.length; i ++ )
			{
				IBinding binding = ((IBinding)exprs[i]);
				
				if( binding.getExpression( ).getHandle( )== null )
				{
					this.isValueAvailable[i] = false;
				}else
				{
					this.isValueAvailable[i] = true;
				}
				
			}*/
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#isValueAvailable(int)
		 */
		public boolean isValueAvailable( int index )
		{
			return this.isValueAvailable[index];
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getName(int)
		 */
		public String getName( int index )
		{
			return this.names[index].toString( );
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getExpression(int)
		 */
		public IBaseExpression getExpression( int index ) throws DataException
		{
			return ((IBinding) exprs[index]).getExpression( );
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#setValueAvailable(int)
		 */
		public void setValueAvailable( int index )
		{
			this.isValueAvailable[index] = true;		
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getCount()
		 */
		public int getCount( )
		{
			return this.isValueAvailable.length;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getComputedColumn(int)
		 */
		public IComputedColumn getComputedColumn( int index )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#setModel(int)
		 */
		public void setModel( int model )
		{
				
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isFinish()
		{
			for( int i = 0; i < isValueAvailable.length; i++ )
			{
				if( !isValueAvailable[i] )
					return false;
			}
			return true;
		}
	}
	
}
