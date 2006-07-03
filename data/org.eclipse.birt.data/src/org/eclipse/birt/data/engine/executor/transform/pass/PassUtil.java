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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.cache.CacheRequest;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.SmartCache;
import org.eclipse.birt.data.engine.executor.cache.SortSpec;
import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * The pass util.
 */
class PassUtil
{
	/**
	 * 
	 */
	private static final String RESULT_SET_COMPUTED_COLUMN_NAME_PATTERN = "\\Q_{$TEMP\\E.*\\d*\\Q$}_\\E";

	/**
	 * 
	 * 
	 */
	private PassUtil( )
	{
	}

	/**
	 * Pass the result source, create a new smartCache, do grouping.
	 * 
	 * @param populator
	 * @param resultSource
	 * @param doGroup
	 * @throws DataException
	 */
	public static void pass( ResultSetPopulator populator,
			OdiResultSetWrapper resultSource, boolean doGroup )
			throws DataException
	{
		if ( doGroup )
			populator.getGroupProcessorManager( )
					.getGroupCalculationUtil( )
					.initGroupSpec( );

		populateOdiResultSet( populator, resultSource, doGroup
				? populator.getGroupProcessorManager( )
						.getGroupCalculationUtil( )
						.getSortSpec( ) : null );

		if ( doGroup )
			populator.getGroupProcessorManager( )
					.getGroupCalculationUtil( )
					.getGroupInformationUtil( )
					.doGrouping( );

		populator.getCache( ).next( );
	}

	/**
	 * 
	 * @param populator
	 * @param rsWrapper
	 * @param sortSpec
	 * @throws DataException
	 */
	private static void populateOdiResultSet( ResultSetPopulator populator,
			OdiResultSetWrapper rsWrapper, SortSpec sortSpec )
			throws DataException
	{
		Object resultSource = rsWrapper.getWrappedOdiResultSet( );
		assert resultSource != null;

		ResultSetCache smartCache = null;
		BaseQuery query = populator.getQuery( );
		IResultClass rsMeta = populator.getResultSetMetadata( );

		if ( resultSource instanceof ResultSet )
		{
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					query.getDistinctValueFlag( ) ),
					(ResultSet) resultSource,
					rsMeta );
		}
		else if ( resultSource instanceof ICustomDataSet )
		{
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					query.getDistinctValueFlag( ) ),
					new OdiAdapter( (ICustomDataSet) resultSource ),
					rsMeta );
		}
		else if ( resultSource instanceof IDataSetPopulator )
		{
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					query.getDistinctValueFlag( ) ),
					new OdiAdapter( (IDataSetPopulator) resultSource ),
					rsMeta );
		}
		else if ( resultSource instanceof DataSetResultCache )
		{
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					false ), // must be true
					new OdiAdapter( (DataSetResultCache) resultSource ),
					rsMeta );
		}
		else if ( resultSource instanceof IResultIterator )
		{
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					false ), // must be true
					new OdiAdapter( (IResultIterator) resultSource ),
					rsMeta );
		}
		else if ( resultSource instanceof Object[] )
		{
			Object[] obs = (Object[]) resultSource;
			smartCache = new SmartCache( new CacheRequest( query.getMaxRows( ),
					query.getFetchEvents( ),
					sortSpec,
					populator.getEventHandler( ),
					false ), // must be true
					(ResultSetCache) obs[0],
					( (int[]) obs[1] )[0],
					( (int[]) obs[1] )[1],
					rsMeta );
		}

		populator.getGroupProcessorManager( )
				.getGroupCalculationUtil( )
				.setResultSetCache( smartCache );
		populator.setCache( smartCache );
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isTemporaryResultSetComputedColumn( String name )
	{
		return name.matches( RESULT_SET_COMPUTED_COLUMN_NAME_PATTERN );
	}
}
