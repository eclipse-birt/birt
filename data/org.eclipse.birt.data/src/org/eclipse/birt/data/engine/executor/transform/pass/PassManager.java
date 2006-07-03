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
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.odi.IEventHandler;

/**
 * The entry point of this package.
 */
public class PassManager
{
	//
	private ResultSetPopulator populator;

	private ComputedColumnHelper computedColumnHelper;

	private FilterByRow filterByRow;

	/**
	 * Constructor.
	 * 
	 * @param populator
	 */
	private PassManager( ResultSetPopulator populator )
	{
		this.populator = populator;
	}

	/**
	 * 
	 * @param populator
	 * @param odaResultSet
	 * @throws DataException
	 */
	public static void populateResultSet( ResultSetPopulator populator,
			OdiResultSetWrapper odaResultSet ) throws DataException
	{
		new PassManager( populator ).pass( odaResultSet );
	}
	
	/**
	 * Pass the oda result set.
	 * @param odaResultSet
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
				computedColumnHelper.setExecutorHelper( this.populator.getEventHandler( )
						.getExecutorHelper( ) );
			}
			else if ( fetchEventsList[i] instanceof FilterByRow )
			{
				filterByRow = (FilterByRow) fetchEventsList[i];
				filterByRow.setExecutorHelper( this.populator.getEventHandler( )
						.getExecutorHelper( ) );
			}
		}
	}

	/**
	 * 
	 * @param odaResultSet
	 * @throws DataException
	 */
	private void doSinglePass( OdiResultSetWrapper odaResultSet ) throws DataException
	{
		if ( computedColumnHelper != null )
			computedColumnHelper.setModel( TransformationConstants.DATA_SET_MODEL );
		PassUtil.pass( this.populator, odaResultSet, false );
		this.populator.getExpressionProcessor( ).setDataSetMode( false );
		handleEndOfDataSetProcess( );
	}

	/**
	 * 
	 * @param odaResultSet
	 * @param iccState
	 * @param psController
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
	 * @throws DataException
	 */
	private void populateResultSetCacheInResultSetPopulator(
			OdiResultSetWrapper odaResultSet ) throws DataException
	{
		int max = 0;
		
		if ( computedColumnHelper != null )
			computedColumnHelper.setModel( TransformationConstants.NONE_MODEL );

		if ( filterByRow != null )
		{
			filterByRow.setWorkingFilterSet( FilterByRow.NO_FILTER );
		}

		max = this.populator.getQuery( ).getMaxRows( );
		
		if ( filterByRow != null )
			this.populator.getQuery( ).setMaxRows( 0 );
		
		PassUtil.pass( this.populator, odaResultSet, false );

		this.populator.getQuery( ).setMaxRows( max );
	}

	/**
	 * 
	 *
	 */
	private void handleEndOfDataSetProcess( )
	{
		IEventHandler eventHandler = this.populator.getEventHandler( );

		if ( eventHandler != null )
			eventHandler.handleEndOfDataSetProcess( this.populator.getResultIterator( ) );
	}
}
