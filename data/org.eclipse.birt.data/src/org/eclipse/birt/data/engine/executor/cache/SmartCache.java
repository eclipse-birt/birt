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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.disk.DiskCache;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Smart cache implementation of ResultSetCache. It is also the entry to new
 * instance of ResultSetCache. It will decide which concrete implemenation of
 * ResultCache should be used depending on the size of ResultSet. If all data
 * can be accomondated in memory, then MemoryCache will be used. Otherwise
 * DiskCache will be used.
 */
public class SmartCache implements ResultSetCache
{
	/** concrete implementation of ResultSetCache */
	private ResultSetCache resultSetCache;

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
	
	// open flag
	private boolean isOpen = true;
	
	private IEventHandler eventHandler;
		
	/**
	 * Retrieve data from ODI, used in sub query
	 * 
	 * @param query
	 * @param resultCache, parent resultSetCache
	 * @param startIndex, included
	 * @param endIndex, excluded
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	public SmartCache( CacheRequest cacheRequest, ResultSetCache resultCache,
			int startIndex, int endIndex, IResultClass rsMeta )
			throws DataException
	{
		assert cacheRequest != null;
		assert resultCache != null;
		assert rsMeta != null;

		this.eventHandler = cacheRequest.getEventHandler( );
		OdiAdapter odiAdpater = new OdiAdapter( resultCache );
		initInstance2( cacheRequest,
				resultCache,
				odiAdpater,
				startIndex,
				endIndex,
				rsMeta );
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
		IRowResultSet rowResultSet = new ExpandableRowResultSet( new SmartCacheRequest( cacheRequest.getMaxRow( ),
				cacheRequest.getFetchEvents( ),
				odiAdpater,
				rsMeta,
				cacheRequest.getDistinctValueFlag( ) ) );
		populateData( rsMeta, rowResultSet, cacheRequest.getSortSpec( ) );
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
	private void initInstance2( CacheRequest cacheRequest,
			ResultSetCache resultCache, OdiAdapter odiAdpater, int startIndex,
			int endIndex, IResultClass rsMeta )
			throws DataException
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
	public SmartCache( CacheRequest cacheRequest, OdiAdapter odiAdapter,
			IResultClass rsMeta ) throws DataException
	{		
		initInstance( cacheRequest, odiAdapter, rsMeta );
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
	public SmartCache( CacheRequest cacheRequest, ResultSet odaResultSet,
			IResultClass rsMeta ) throws DataException
	{
		assert cacheRequest != null;
		assert odaResultSet != null;
		assert rsMeta != null;

		if ( cacheRequest.getDistinctValueFlag( ) == true )
		{
			SmartCache smartCache = new SmartCache( cacheRequest,
					odaResultSet,
					rsMeta, false );
			
			cacheRequest.setDistinctValueFlag( false );
			initInstance( cacheRequest, new OdiAdapter( smartCache ), rsMeta );
		}
		else
		{
			init( cacheRequest, odaResultSet, rsMeta );
		}
	}
	
	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @param ob
	 * @throws DataException
	 */
	private SmartCache( CacheRequest cacheRequest, ResultSet odaResultSet,
			IResultClass rsMeta, boolean ob ) throws DataException
	{
		SmartCache smartCache = new SmartCache( new CacheRequest( 0,
				null,
				CacheUtil.getSortSpec( rsMeta ),
				cacheRequest.getEventHandler( ),
				true ), odaResultSet, rsMeta, false, false );

		this.eventHandler = cacheRequest.getEventHandler( );
		OdiAdapter odiAdpater = new OdiAdapter( smartCache );
		initInstance( cacheRequest, odiAdpater, rsMeta );
	}
	
	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @param ob
	 * @param ob1
	 * @throws DataException
	 */
	private SmartCache( CacheRequest cacheRequest, ResultSet odaResultSet,
			IResultClass rsMeta, boolean ob, boolean ob1 ) throws DataException
	{
		init( cacheRequest, odaResultSet, rsMeta );
	}
	
	/**
	 * @param cacheRequest
	 * @param odaResultSet
	 * @param rsMeta
	 * @throws DataException
	 */
	private void init( CacheRequest cacheRequest, ResultSet odaResultSet,
			IResultClass rsMeta ) throws DataException
	{
		this.eventHandler = cacheRequest.getEventHandler( );
		OdiAdapter odiAdpater = new OdiAdapter( odaResultSet );
		initInstance( cacheRequest, odiAdpater, rsMeta );
	}
	
	/**
	 * @param cacheRequest
	 * @param rowResultSet
	 * @param rsMeta
	 * @throws DataException
	 */
	public SmartCache( CacheRequest cacheRequest, IRowResultSet rowResultSet,
			IResultClass rsMeta ) throws DataException
	{
		populateData( rsMeta, rowResultSet, cacheRequest.getSortSpec( ) );
	}
		
	/**
	 * Populate the smartCache.
	 * @param rsMeta
	 * @param rowResultSet
	 * @param sortSpec
	 * @throws DataException
	 */
	private void populateData( IResultClass rsMeta, IRowResultSet rowResultSet,
			SortSpec sortSpec ) throws DataException
	{
		long startTime = System.currentTimeMillis( );

		// compute the number of rows which can be cached in memory
		int memoryCacheRowCount = computeCacheRowCount( rsMeta );
		logger.info( "memoryCacheRowCount is " + memoryCacheRowCount );
		
		IResultObject odaObject;
		IResultObject[] resultObjects;
		List resultObjectsList = new ArrayList( );
		
		int dataCount = 0;
		while ( ( odaObject = rowResultSet.next( ) ) != null )
		{
			dataCount++;
			// notice. it is less than or equal
			if ( dataCount <= memoryCacheRowCount ) 
			{
				//Populate Data according to the given meta data.
				Object[] obs = new Object[rsMeta.getFieldCount()];
				for(int i = 1; i <= rsMeta.getFieldCount(); i++)
				{
					if( i <= odaObject.getResultClass().getFieldCount() )
						obs[i-1] = odaObject.getFieldValue( rsMeta.getFieldName(i));
					else
						obs[i-1] = null;
				}
				resultObjectsList.add( new ResultObject( rsMeta, obs) );
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
						memoryCacheRowCount );
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
	private int computeCacheRowCount( IResultClass rsMeta )
	{
		if ( MemoryCacheSize == 0 )
		{
			synchronized ( this )
			{
				if ( MemoryCacheSize == 0 )
				{
					MemoryCacheSize = CacheUtil.computeCacheRowCount( );
				}
			}
		}

		return MemoryCacheSize;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCount()
	 */
	public int getCount( ) throws DataException
	{
		checkOpenStates( );
		
		return resultSetCache.getCount( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentIndex()
	 */
	public int getCurrentIndex( ) throws DataException
	{
		checkOpenStates( );
		
		return resultSetCache.getCurrentIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentResult()
	 */
	public IResultObject getCurrentResult( ) throws DataException
	{
		checkOpenStates( );
		
		return resultSetCache.getCurrentResult( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#nextRow()
	 */
	public boolean next( ) throws DataException
	{
		checkOpenStates( );
		
		return resultSetCache.next( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#fetch()
	 */
	public IResultObject fetch( ) throws DataException
	{
		checkOpenStates( );
		
		return resultSetCache.fetch( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#moveTo(int)
	 */
	public void moveTo( int destIndex ) throws DataException
	{
		checkOpenStates( );
		
		resultSetCache.moveTo( destIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#reset()
	 */
	public void reset( ) throws DataException
	{
		checkOpenStates( );
		
		resultSetCache.reset( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#close()
	 */
	public void close( )
	{
		if ( isOpen == false )
			return;
		
		resultSetCache.close( );
		resultSetCache = null;
		isOpen = false;
	}
	
	/**
	 * @throws DataException
	 */
	private void checkOpenStates( ) throws DataException
	{
		if ( isOpen == false )
			throw new DataException( ResourceConstants.RESULT_CLOSED );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream(java.io.OutputStream)
	 */
	public void doSave( OutputStream outputStream ) throws DataException
	{
		this.resultSetCache.doSave( outputStream );
	}
	
}