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
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Manage the cache map
 */
class CacheMapManager
{
	/**
	 * Please notice that we must use static variable here for the sharing of
	 * cached data set would be cross data set session.
	 */
	private static Map JVMLevelCacheMap = Collections.synchronizedMap( new HashMap( ) );
	
	private Map cacheMap;
	
	/**
	 * construction
	 */
	CacheMapManager( boolean useJVMLevelCache )
	{
		if( useJVMLevelCache )
		{
			cacheMap = JVMLevelCacheMap;
		}
		else
		{
			cacheMap = new HashMap( );
		}
	}
	
	/**
	 * @param appContext 
	 * @param collection 
	 * @param baseDataSetDesign 
	 * @param baseDataSourceDesign 
	 * @return
	 * @throws DataException 
	 */
	boolean doesSaveToCache( DataSourceAndDataSet dsAndDs,
			DataSetCacheConfig dscc) throws DataException
	{		
		synchronized ( cacheMap )
		{
			IDataSetCacheObject cacheObject = (IDataSetCacheObject)cacheMap.get( dsAndDs );
			if (cacheObject != null)
			{
				return cacheObject.needUpdateCache( dscc.getCacheCapability( ) );
			}
			else
			{
				IDataSetCacheObject dsco = dscc.createDataSetCacheObject( );
				cacheMap.put( dsAndDs, dsco );
				return true;
			}
		}
	}
	
	/**
	 * @param dsAndDs
	 * @return
	 */
	boolean doesLoadFromCache( DataSourceAndDataSet dsAndDs, int requiredCapability )
	{
		synchronized ( cacheMap )
		{
			IDataSetCacheObject cacheObject = (IDataSetCacheObject)cacheMap.get( dsAndDs );
			if (cacheObject != null)
			{
				boolean reusable = cacheObject.isCachedDataReusable( requiredCapability );
				if ( !reusable )
				{
					cacheObject.release( );
					cacheMap.remove( dsAndDs );
				}
				return reusable;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * @return
	 */
	IDataSetCacheObject getCacheObject( DataSourceAndDataSet dsAndDs )
	{
		return (IDataSetCacheObject) cacheMap.get( dsAndDs );
	}
	
	/**
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	void clearCache( DataSourceAndDataSet dsAndDs )
	{
		List cacheObjects = new ArrayList( );
		synchronized ( cacheMap )
		{
			Object key = getKey(dsAndDs);
			while ( key != null )
			{
				cacheObjects.add( cacheMap.remove( key ) );
				key = getKey(dsAndDs);
			}
		}
		for ( int i = 0; i < cacheObjects.size( ); i++ )
		{
			IDataSetCacheObject cacheObject = (IDataSetCacheObject)cacheObjects.get( i );
			cacheObject.release( );
		}

	}
	
	/**
	 * Reset for test case
	 */
	void resetForTest( )
	{
		synchronized ( this )
		{
			cacheMap.clear( );
		}
	}
	
	/**
	 * Return the cached result metadata featured by the given
	 * DataSourceAndDataSet. Please note that the paramter would have no impact
	 * to DataSourceAndDataSet so that will be omited.
	 * 
	 * @param dsAndDs
	 * @return
	 * @throws DataException
	 */
	IResultClass getCachedResultClass( DataSourceAndDataSet dsAndDs )
			throws DataException
	{
		IDataSetCacheObject cacheObject = null;
		Object key = getKey( dsAndDs );
		if ( key != null )
		{
			cacheObject = (IDataSetCacheObject)cacheMap.get( key );
		}
		if (cacheObject != null)
		{
			return cacheObject.getResultClass( );
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @param dsAndDs
	 * @return
	 */
	private Object getKey ( DataSourceAndDataSet dsAndDs )
	{
		synchronized ( cacheMap )
		{
			for ( Iterator it = cacheMap.keySet( ).iterator( ); it.hasNext( ); )
			{
				DataSourceAndDataSet temp = (DataSourceAndDataSet) it.next( );
				if ( temp.isDataSourceDataSetEqual( dsAndDs, false ) )
				{
					return temp;
				}
			}
			return null;
		}
	}
}
