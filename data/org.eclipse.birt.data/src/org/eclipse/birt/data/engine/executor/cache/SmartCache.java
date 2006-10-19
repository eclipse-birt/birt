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
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
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
	
	// open flag
	private boolean isOpen = true;
			
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
			IResultClass rsMeta, DataEngineSession session ) throws DataException
	{
		SmartCacheHelper smartCacheHelper = new SmartCacheHelper( session );
		this.resultSetCache = smartCacheHelper.getResultSetCache( cacheRequest,
				odaResultSet,
				rsMeta );
	}
	
	/**
	 * @param cacheRequest
	 * @param odiAdapter
	 * @param rsMeta
	 * @throws DataException
	 */
	public SmartCache( CacheRequest cacheRequest, OdiAdapter odiAdapter,
			IResultClass rsMeta, DataEngineSession session ) throws DataException
	{
		SmartCacheHelper smartCacheHelper = new SmartCacheHelper( session );
		this.resultSetCache = smartCacheHelper.getResultSetCache( cacheRequest,
				odiAdapter,
				rsMeta );
	}
	
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
			int startIndex, int endIndex, IResultClass rsMeta, DataEngineSession session )
			throws DataException
	{
		SmartCacheHelper smartCacheHelper = new SmartCacheHelper( session );
		this.resultSetCache = smartCacheHelper.getResultSetCache( cacheRequest,
				resultCache,
				startIndex,
				endIndex,
				rsMeta );
	}
	
	/**
	 * @param cacheRequest
	 * @param rowResultSet
	 * @param rsMeta
	 * @throws DataException
	 */
	public SmartCache( CacheRequest cacheRequest, IRowResultSet rowResultSet,
			IResultClass rsMeta, DataEngineSession session ) throws DataException
	{
		SmartCacheHelper smartCacheHelper = new SmartCacheHelper( session );
		this.resultSetCache = smartCacheHelper.getResultSetCache( cacheRequest,
				rowResultSet,
				rsMeta );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCount()
	 */
	public int getCount( ) throws DataException
	{
		assert isOpen;
		
		return resultSetCache.getCount( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentIndex()
	 */
	public int getCurrentIndex( ) throws DataException
	{
		assert isOpen;
		
		return resultSetCache.getCurrentIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentResult()
	 */
	public IResultObject getCurrentResult( ) throws DataException
	{
		assert isOpen;
		
		return resultSetCache.getCurrentResult( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#nextRow()
	 */
	public boolean next( ) throws DataException
	{
		assert isOpen;
		
		return resultSetCache.next( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#fetch()
	 */
	public IResultObject fetch( ) throws DataException
	{
		assert isOpen;
		
		return resultSetCache.fetch( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#moveTo(int)
	 */
	public void moveTo( int destIndex ) throws DataException
	{
		assert isOpen;
		
		resultSetCache.moveTo( destIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#reset()
	 */
	public void reset( ) throws DataException
	{
		assert isOpen;
		
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
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#saveToStream(java.io.OutputStream)
	 */
	public void doSave( OutputStream outputStream, Map cacheRequestMap )
			throws DataException
	{
		this.resultSetCache.doSave( outputStream, cacheRequestMap );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize()
	{
		close();
	}
	
}