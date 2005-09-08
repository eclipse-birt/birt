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
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
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
	
	/**
	 * Retrieve data from ODA, used in normal query
	 * 
	 * @param odaResultSet
	 * @param query
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	public SmartCache( BaseQuery query, ResultSet odaResultSet,
			IResultClass rsMeta, SortSpec sortSpec ) throws DataException
	{
		assert odaResultSet != null;
		assert query != null;
		assert rsMeta != null;

		OdiAdapter odiAdpater = new OdiAdapter( odaResultSet );
		initInstance( odiAdpater, query, rsMeta, sortSpec );
	}

	/**
	 * Retrieve data from ODI, used in candidate query, such as Scripted DataSet
	 * 
	 * @param customDataSet
	 * @param query
	 * @param rsMeta
	 * @param sortSpec
	 * @throws DataException
	 */
	public SmartCache( BaseQuery query, ICustomDataSet customDataSet,
			IResultClass rsMeta, SortSpec sortSpec ) throws DataException
	{
		assert customDataSet != null;
		assert query != null;
		assert rsMeta != null;

		OdiAdapter odiAdpater = new OdiAdapter( customDataSet );
		initInstance( odiAdpater, query, rsMeta, sortSpec );
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
	public SmartCache( BaseQuery query, ResultSetCache resultCache,
			int startIndex, int endIndex, IResultClass rsMeta, SortSpec sortSpec )
			throws DataException
	{
		assert resultCache != null;
		assert query != null;
		assert rsMeta != null;

		OdiAdapter odiAdpater = new OdiAdapter( resultCache );
		initInstance2( resultCache, odiAdpater, query, startIndex, endIndex, rsMeta, sortSpec );
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
	private void initInstance2( ResultSetCache resultCache,
			OdiAdapter odiAdpater, BaseQuery query, int startIndex,
			int endIndex, IResultClass rsMeta, SortSpec sortSpec )
			throws DataException
	{
		int length = endIndex - startIndex;
		if ( query.getMaxRows( ) == 0 || length <= query.getMaxRows( ) )
			query.setMaxRows( length );

		int oldIndex = resultCache.getCurrentIndex( );

		// In OdiAdapter, it fetches the next row, not current row.
		resultCache.moveTo( startIndex - 1 );
		initInstance( odiAdpater, query, rsMeta, sortSpec );

		resultCache.moveTo( oldIndex );
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
	private void initInstance( OdiAdapter odiAdpater, BaseQuery query,
			IResultClass rsMeta, SortSpec sortSpec ) throws DataException
	{	
		// compute the number of rows which can be cached in memory
		int memoryCacheRowCount = computeCacheRowCount( rsMeta );
		logger.info( "memoryCacheRowCount is " + memoryCacheRowCount );
		
		IResultObject odaObject;
		IResultObject[] resultObjects;
		List resultObjectsList = new ArrayList( );
		RowResultSet rowResultSet = new RowResultSet( query, odiAdpater, rsMeta );
		
		int dataCount = 0;
		while ( ( odaObject = rowResultSet.next( ) ) != null )
		{
			dataCount++;
			// notice. it is less than or equal
			if ( dataCount <= memoryCacheRowCount ) 
			{
				resultObjectsList.add( odaObject );
			}
			else
			{
				logger.info( "DisckCache is used" );
				
				resultObjects = (IResultObject[]) resultObjectsList.toArray( new IResultObject[0] );
				// the order is: resultObjects, odaObject, rowResultSet
				resultSetCache = new DiskCache( resultObjects,
						odaObject,
						rowResultSet,
						getComparator( sortSpec ),
						memoryCacheRowCount );
				break;
			}
		}

		if ( resultSetCache == null )
		{
			logger.info( "MemoryCache is used" );
			
			resultObjects = (IResultObject[]) resultObjectsList.toArray( new IResultObject[0] );
			
			resultSetCache = new MemoryCache( resultObjects,
					getComparator( sortSpec ) );
		}
		
		odaObject = null;
		resultObjects = null;
		resultObjectsList = null;
		rowResultSet = null;
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
			MemoryCacheSize = 10; // default minimum value is 10M.
			String memcachesize = System.getProperty( "birt.data.engine.memcachesize" );
			if ( memcachesize != null )
			{
				try
				{
					MemoryCacheSize = Integer.parseInt( memcachesize );
				}
				catch ( Exception e )
				{
					// ignore it
				}

				if ( MemoryCacheSize < 10 )
				{
					throw new IllegalArgumentException( "the value of memcachesize should be at least 10" );
				}
			}
		}

		return computeCacheRowCount( MemoryCacheSize, rsMeta );
	}

	/**
	 * @param cacheSize,
	 *            the size of cache
	 * @param rsMeta,
	 *            the meta data of result set
	 * @return row count of memory cached
	 */
	private int computeCacheRowCount( int cacheSize, IResultClass rsMeta )
	{
		// below code only for unit test
		String memcachesizeOfTest = System.getProperty( "birt.data.engine.test.memcachesize" );
		if ( memcachesizeOfTest != null )
			return Integer.parseInt( memcachesizeOfTest );

		// here a simple assumption, that 1M memory can accomondate 2000 rows
		return cacheSize * 2000;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCount()
	 */
	public int getCount( )
	{
		return resultSetCache.getCount( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentIndex()
	 */
	public int getCurrentIndex( ) throws DataException
	{
		return resultSetCache.getCurrentIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#getCurrentResult()
	 */
	public IResultObject getCurrentResult( ) throws DataException
	{
		return resultSetCache.getCurrentResult( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#nextRow()
	 */
	public boolean next( ) throws DataException
	{
		return resultSetCache.next( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#fetch()
	 */
	public IResultObject fetch( ) throws DataException
	{
		return resultSetCache.fetch( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#moveTo(int)
	 */
	public void moveTo( int destIndex ) throws DataException
	{
		resultSetCache.moveTo( destIndex );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#reset()
	 */
	public void reset( ) throws DataException
	{
		resultSetCache.reset( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.ResultSetCache#close()
	 */
	public void close( )
	{
		resultSetCache.close( );
		resultSetCache = null;
	}

	/**
	 * @param sortSpec
	 * @return Comparator based on specified sortSpec, null indicates there is
	 *         no need to do sorting
	 */
	private Comparator getComparator( SortSpec sortSpec )
	{
		if ( sortSpec == null )
			return null;

		final int[] sortKeyIndexes = sortSpec.sortKeyIndexes;
		if ( sortKeyIndexes == null || sortKeyIndexes.length == 0 )
			return null;

		final boolean[] sortAscending = sortSpec.sortAscending;

		Comparator comparator = new Comparator( ) {

			/**
			 * compares two row indexes, actually compares two rows pointed by
			 * the two row indexes
			 */
			public int compare( Object obj1, Object obj2 )
			{
				IResultObject row1 = (IResultObject) obj1;
				IResultObject row2 = (IResultObject) obj2;

				// compare group keys first
				for ( int i = 0; i < sortKeyIndexes.length; i++ )
				{
					int colIndex = sortKeyIndexes[i];

					try
					{
						Object colObj1 = row1.getFieldValue( colIndex );
						Object colObj2 = row2.getFieldValue( colIndex );
						int result = doCompare( colObj1, colObj2 );
						if ( result != 0 )
						{
							return sortAscending[i] ? result : -result;
						}
					}
					catch ( DataException e )
					{
						// Should never get here
						// colIndex is always valid
					}
				}

				// all equal, so return 0
				return 0;
			}

			/**
			 * Compare two objects
			 * 
			 * @param colObj1
			 * @param colObj2
			 * @return true colObj1 equals colObj2
			 */
			private int doCompare( Object colObj1, Object colObj2 )
			{
				// default value is 0
				int result = 0;

				// 1: the case of reference is the same
				if ( colObj1 == colObj2 )
				{
					return result;
				}

				// 2: the case of one of two object is null
				if ( colObj1 == null || colObj2 == null )
				{
					// keep null value at the first position in ascending order
					if ( colObj1 == null )
					{
						result = -1;
					}
					else
					{
						result = 1;
					}
					return result;
				}

				// 3: other cases
				if ( colObj1.equals( colObj2 ) )
				{
					return result;
				}
				else if ( colObj1 instanceof Comparable
						&& colObj2 instanceof Comparable )
				{
					Comparable comp1 = (Comparable) colObj1;
					Comparable comp2 = (Comparable) colObj2;
					result = comp1.compareTo( comp2 );
				}
				else if ( colObj1 instanceof Boolean
						&& colObj2 instanceof Boolean )
				{
					// false is less than true
					Boolean bool = (Boolean) colObj1;
					if ( bool.equals( Boolean.TRUE ) )
						result = 1;
					else
						result = -1;
				}
				else
				{
					// Should never get here
					//throw new UnsupportedOperationException( );
				}

				return result;
			}
		};

		return comparator;
	}
	
}