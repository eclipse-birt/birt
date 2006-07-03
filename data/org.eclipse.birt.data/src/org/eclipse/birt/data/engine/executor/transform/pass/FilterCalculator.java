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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.script.FilterPassController;

/**
 * MultiPass filter processor.Used to apply filters to result data.
 */
class FilterCalculator
{

	private ResultSetPopulator populator;
	private FilterByRow filterByRow;

	/**
	 * 
	 * @param populator
	 * @param iccState
	 * @param filterByRow
	 */
	private FilterCalculator( ResultSetPopulator populator, FilterByRow filterByRow )
	{
		this.populator = populator;
		this.filterByRow = filterByRow;
	}

	/**
	 * This method filters out all unnecessary rows from result set.
	 * 
	 * @param populator
	 * @param iccState
	 * @param filterByRow
	 * @throws DataException
	 */
	static void applyFilters( ResultSetPopulator populator, FilterByRow filterByRow )
			throws DataException
	{
		new FilterCalculator( populator, filterByRow ).applyFilters( );
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void applyFilters( ) throws DataException
	{
		FilterPassController filterPass = new FilterPassController( );
		// Prepare filter expression for top/bottom(n) evaluation
		Iterator filterIt = filterByRow.getFilterList( ).iterator( );
		while ( filterIt.hasNext( ) )
		{
			FilterUtil.prepareFilterExpression( ( (IFilterDefinition) filterIt.next( ) ).getExpression( ),
					filterPass,
					populator.getEventHandler( ).getExecutorHelper( ) );
		}

/*		populator.getExpressionProcessor( )
				.setResultSetMetaData( populator.getResultSetMetadata( ) );

		populator.getExpressionProcessor( )
				.compileFilter( filterByRow.getFilterList( ), iccState );*/

		// Actually carry out the filter job.
		doFiltering( filterPass );
	}

	/**
	 * Carry out the actual filtering job.
	 * 
	 * @param filterPass
	 * @throws DataException
	 */
	private void doFiltering( FilterPassController filterPass )
			throws DataException
	{
		boolean needMultiPass = false;
		needMultiPass = FilterUtil.hasMutipassFilters( filterByRow.getFilterList( ) );

		// When the given filter list starting from a filter with top n/ bottom
		// n filters,
		// the filter list needs multipass. Otherwise the multipass is not
		// necessary.
		if ( needMultiPass )
		{
			makeMultiPassToFilter( filterPass );
		}
		else
		{
			//Grouping is done here
			PassUtil.pass( populator,
					new OdiResultSetWrapper( populator.getResultIterator( ) ),
					true );
		}

		/*
		 * filterByRow.setWorkingFilterSet( FilterByRow.NO_FILTER );
		 * PassUtil.pass( populator, new OdiResultSetWrapper(
		 * populator.getResultIterator( ) ), true );
		 */
	}

	/**
	 * Make a multi-pass to a filter that needs multipass, meanly TopN and
	 * BottomN.The pass actually contains two steps. 1.FIRST PASS: in this step
	 * we just go through all result rows and make preparation work for the
	 * second step 2.SECOND PASS: in this step the rows that are not qualified
	 * is filtered out
	 * 
	 * @param filterPass
	 * @throws DataException
	 */
	private void makeMultiPassToFilter( FilterPassController filterPass )
			throws DataException
	{
		int max = populator.getQuery( ).getMaxRows( );

		populator.getQuery( ).setMaxRows( 0 );
		// Cache the current ResultSetCache instance, as after start() is called
		// the current
		// ResultSetCache will be overwrite.
		ResultSetCache sCache = populator.getCache( );

		makeFirstPassToMultiPassFilter( filterPass );

		populator.setCache( sCache );
		// Reset the smartCache and make cursor stay in first row.
		sCache.reset( );
		sCache.next( );

		makeSecondPassToMultiPassFilter( filterPass );

		// Prepare filter expression for top/bottom(n) evaluation
		Iterator filterIt = filterByRow.getFilterList( ).iterator( );
		while ( filterIt.hasNext( ) )
		{
			IFilterDefinition fd = (IFilterDefinition) filterIt.next( );
			if ( FilterUtil.isFilterNeedMultiPass( fd ) )
			{
				fd.getExpression( ).setHandle( null );
				// this.prepareFilterExpression( fd.getExpression() );
			}
		}

		filterPass.setSecondPassRowCount( 0 );

		populator.getQuery( ).setMaxRows( max );
	}

	/**
	 * 
	 * @param filterPass
	 * @throws DataException
	 */
	private void makeFirstPassToMultiPassFilter( FilterPassController filterPass )
			throws DataException
	{
		filterPass.setForceReset( true );
		// First set pass level to first, the value of pass level will finally
		// affect the behavior of TopN/BottomN filters, that is, the instance of
		// org.eclipse.birt.data.engine.script.NEvaluator

		filterPass.setPassLevel( FilterPassController.FIRST_PASS );
		filterPass.setRowCount( populator.getCache( ).getCount( ) );

		List temp = new ArrayList( );
		temp.addAll( filterByRow.getFilterList( ) );
		filterByRow.getFilterList( ).clear( );
		for ( int i = 0; i < temp.size( ); i++ )
		{
			if ( FilterUtil.isFilterNeedMultiPass( (IFilterDefinition) temp.get( i ) ) )
			{
				filterByRow.getFilterList( ).add( temp.get( i ) );
			}
		}
		PassUtil.pass( populator,
				new OdiResultSetWrapper( populator.getResultIterator( ) ),
				false );
		filterByRow.getFilterList( ).clear( );
		filterByRow.getFilterList( ).addAll( temp );
	}

	/**
	 * 
	 * @param filterPass
	 * @throws DataException
	 */
	private void makeSecondPassToMultiPassFilter(
			FilterPassController filterPass ) throws DataException
	{
		// Set pass level to second
		filterPass.setPassLevel( FilterPassController.SECOND_PASS );

		// Grouping is done here.
		PassUtil.pass( populator,
				new OdiResultSetWrapper( populator.getResultIterator( ) ),
				true );

		filterPass.setPassLevel( FilterPassController.DEFAULT_PASS );
		filterPass.setRowCount( FilterPassController.DEFAULT_ROW_COUNT );
	}
}
