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

package org.eclipse.birt.data.engine.executor.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.disk.DiskCache;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Help SmartCache to get the ResultSetCache, the real data cache.
 */
class SmartCacheHelper
{
	/** concrete implementation of ResultSetCache */
	private ResultSetCache resultSetCache;

	private IEventHandler eventHandler;

	/**
	 * Very important parameter, indicates the max number of row which can be
	 * accomodated by available memory. In future, this vale needs to be changed
	 * to the maximum momery can be used, rather than static value of maimum
	 * result object can be loaded into memory. The unit of this value million
	 * bytes.
	 */
	private static int MemoryCacheSize;

	// log instance
	private static Logger logger = Logger.getLogger( SmartCache.class.getName( ) );

	private DataEngineSession session;
	
	SmartCacheHelper( DataEngineSession session )
	{
		this.session = session;
	}
	/**
	 * Retrieve data from ODA, used in normal query
	 * 
	 * @param odaResultSet
	 * @param query
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	ResultSetCache getResultSetCache( CacheRequest cacheRequest,
			ResultSet odaResultSet, IResultClass rsMeta ) throws DataException
	{
		assert cacheRequest != null;
		assert odaResultSet != null;
		assert rsMeta != null;

		if ( cacheRequest.getDistinctValueFlag( ) == true )
		{
			SmartCacheHelper smartCacheHelper = new SmartCacheHelper( session );
			ResultSetCache smartCache = smartCacheHelper.getDistinctResultSetCache( cacheRequest,
					odaResultSet,
					rsMeta );

			cacheRequest.setDistinctValueFlag( false );
			initInstance( cacheRequest, new OdiAdapter( smartCache ), rsMeta );
		}
		else
		{
			initOdaResult( cacheRequest, odaResultSet, rsMeta );
		}

		return this.resultSetCache;
	}

	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @param ob
	 * @throws DataException
	 */
	private ResultSetCache getDistinctResultSetCache(
			CacheRequest cacheRequest, ResultSet odaResultSet,
			IResultClass rsMeta ) throws DataException
	{
		SmartCacheHelper smartCacheHelper = new SmartCacheHelper( this.session );
		ResultSetCache smartCache = smartCacheHelper.getSortedResultSetCache( new CacheRequest( 0,
				null,
				CacheUtil.getSortSpec( rsMeta ),
				cacheRequest.getEventHandler( ),
				true ),
				odaResultSet,
				rsMeta );

		initInstance( cacheRequest, new OdiAdapter( smartCache ), rsMeta );
		return this.resultSetCache;
	}

	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @param ob
	 * @param ob1
	 * @throws DataException
	 */
	private ResultSetCache getSortedResultSetCache( CacheRequest cacheRequest,
			ResultSet odaResultSet, IResultClass rsMeta ) throws DataException
	{
		initOdaResult( cacheRequest, odaResultSet, rsMeta );
		return this.resultSetCache;
	}

	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @throws DataException
	 */
	private void initOdaResult( CacheRequest cacheRequest,
			ResultSet odaResultSet, IResultClass rsMeta ) throws DataException
	{
		OdiAdapter odiAdpater = new OdiAdapter( odaResultSet );
		initInstance( cacheRequest, odiAdpater, rsMeta );
	}

	/**
	 * Retrieve data from ODI, used in sub query
	 * 
	 * @param query
	 * @param resultCache,
	 *            parent resultSetCache
	 * @param startIndex,
	 *            included
	 * @param endIndex,
	 *            excluded
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	ResultSetCache getResultSetCache( CacheRequest cacheRequest,
			ResultSetCache resultCache, int startIndex, int endIndex,
			IResultClass rsMeta ) throws DataException
	{
		assert cacheRequest != null;
		assert resultCache != null;
		assert rsMeta != null;

		OdiAdapter odiAdpater = new OdiAdapter( resultCache );
		initSubResult( cacheRequest,
				resultCache,
				odiAdpater,
				startIndex,
				endIndex,
				rsMeta );

		return this.resultSetCache;
	}

	/**
	 * Especially used for sub query
	 * 
	 * @param resultCache
	 * @param odiAdpater
	 * @param query
	 * @param startIndex
	 * @param endIndex
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	private void initSubResult( CacheRequest cacheRequest,
			ResultSetCache resultCache, OdiAdapter odiAdpater, int startIndex,
			int endIndex, IResultClass rsMeta ) throws DataException
	{
		int length = endIndex - startIndex;
		if ( cacheRequest.getMaxRow( ) <= 0
				|| length <= cacheRequest.getMaxRow( ) )
			cacheRequest.setMaxRow( length );

		int oldIndex = resultCache.getCurrentIndex( );

		// In OdiAdapter, it fetches the next row, not current row.
		resultCache.moveTo( startIndex - 1 );
		initInstance( cacheRequest, odiAdpater, rsMeta );

		resultCache.moveTo( oldIndex );
	}

	/**
	 * @param cacheRequest
	 * @param odiAdapter
	 * @param rsMeta
	 * @throws DataException
	 */
	ResultSetCache getResultSetCache( CacheRequest cacheRequest,
			OdiAdapter odiAdapter, IResultClass rsMeta ) throws DataException
	{
		initInstance( cacheRequest, odiAdapter, rsMeta );
		return this.resultSetCache;
	}

	/**
	 * @param cacheRequest
	 * @param rowResultSet
	 * @param rsMeta
	 * @throws DataException
	 */
	ResultSetCache getResultSetCache( CacheRequest cacheRequest,
			IRowResultSet rowResultSet, IResultClass rsMeta )
			throws DataException
	{
		this.eventHandler = cacheRequest.getEventHandler( );
		populateData( rowResultSet, rsMeta, cacheRequest.getSortSpec( ) );
		return this.resultSetCache;
	}

	/**
	 * Init resultSetCache
	 * 
	 * @param odiAdpater
	 * @param query
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	private void initInstance( CacheRequest cacheRequest,
			OdiAdapter odiAdpater, IResultClass rsMeta ) throws DataException
	{
		this.eventHandler = cacheRequest.getEventHandler( );
		IRowResultSet rowResultSet = new ExpandableRowResultSet( new SmartCacheRequest( cacheRequest.getMaxRow( ),
				cacheRequest.getFetchEvents( ),
				odiAdpater,
				rsMeta,
				cacheRequest.getDistinctValueFlag( ) ) );
		populateData( rowResultSet, rsMeta, cacheRequest.getSortSpec( ) );
	}

	/**
	 * Populate the smartCache.
	 * 
	 * @param rsMeta
	 * @param rowResultSet
	 * @param sortSpec
	 * @throws DataException
	 */
	private void populateData( IRowResultSet rowResultSet, IResultClass rsMeta,
			SortSpec sortSpec ) throws DataException
	{
		long startTime = System.currentTimeMillis( );
		SizeOfUtil sizeOfUtil = new SizeOfUtil( rsMeta );

		// compute the number of rows which can be cached in memory
		long memoryCacheSize = computeCacheMemorySize( rsMeta );
		logger.info( "memoryCacheRowCount is " + memoryCacheSize/1000000 + "M" );

		IResultObject odaObject;
		IResultObject[] resultObjects;
		List resultObjectsList = new ArrayList( );

		int dataCount = 0;
		int usedMemorySize = 0;
		int rowSize = 0;
		while ( ( odaObject = rowResultSet.next( ) ) != null )
		{
			dataCount++;
			// notice. it is less than or equal
			rowSize = sizeOfUtil.sizeOf( odaObject );
			if ( rowSize <= ( MemoryCacheSize - usedMemorySize ) )
			{
				//the followed variable is for performance
				int odaObjectFieldCount = odaObject.getResultClass( ).getFieldCount( );
				int metaFieldCount = rsMeta.getFieldCount( );
				if(odaObjectFieldCount < metaFieldCount)
				{
					//Populate Data according to the given meta data.
					Object[] obs = new Object[metaFieldCount];
					for ( int i = 1; i <= odaObjectFieldCount; i++ )
					{
						obs[i - 1] = odaObject.getFieldValue( i );
					}
					resultObjectsList.add( new ResultObject( rsMeta, obs ) );
				}
				else
				{
					resultObjectsList.add( odaObject );
				}
				usedMemorySize += rowSize;
			}
			else
			{
				logger.info( "DisckCache is used" );

				resultObjects = (IResultObject[]) resultObjectsList.toArray( new IResultObject[0] );
				// the order is: resultObjects, odaObject, rowResultSet
				resultSetCache = new DiskCache( resultObjects,
						odaObject,
						rowResultSet,
						rsMeta,
						CacheUtil.getComparator( sortSpec, eventHandler ),
						dataCount - 1,
						this.session );
				break;
			}
		}

		if ( resultSetCache == null )
		{
			logger.info( "MemoryCache is used" );

			resultObjects = (IResultObject[]) resultObjectsList.toArray( new IResultObject[0] );

			resultSetCache = new MemoryCache( resultObjects,
					rsMeta,
					CacheUtil.getComparator( sortSpec, eventHandler ) );
		}

		odaObject = null;
		resultObjects = null;
		resultObjectsList = null;
		rowResultSet = null;

		long consumedTime = ( System.currentTimeMillis( ) - startTime ) / 1000;
		logger.info( "Time consumed by cache is: " + consumedTime + " second" );
	}

	/**
	 * According to avilable memory and specified rsMeta, the max number of rows
	 * which can be cached in memory. There is a potential issue associate with
	 * this value that current DtE is called in only one thread, but in the
	 * future it is probably that DtE is called by multi threads. Then available
	 * memory is shared by all thread, so the cacheSize needs to be adjusted.
	 * 
	 * @param rsMeta
	 * @return row count
	 */
	private long computeCacheMemorySize( IResultClass rsMeta )
	{
		if ( MemoryCacheSize == 0 )
		{
			synchronized ( this )
			{
				if ( MemoryCacheSize == 0 )
				{
					MemoryCacheSize = CacheUtil.computeCacheSize( ) * 1000000;
				}
			}
		}

		return MemoryCacheSize;
	}

}
