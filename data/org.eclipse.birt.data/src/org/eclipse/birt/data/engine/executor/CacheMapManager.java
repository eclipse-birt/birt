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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.CacheUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.IIncreCacheDataSetDesign;
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
	private static Map cacheMap = new HashMap( );
	
	/**
	 * cache directory map for disk based cache( disk cache and incremental
	 * cache )
	 */
	private Map cacheDirMap = new HashMap( );
	
	private String tempDir;
	/**
	 * construction
	 */
	CacheMapManager( String tempDir )
	{
		this.tempDir = tempDir;
	}
	
	/**
	 * @param appContext 
	 * @param collection 
	 * @param baseDataSetDesign 
	 * @param baseDataSourceDesign 
	 * @return
	 */
	boolean doesSaveToCache( DataSourceAndDataSet dsAndDs, int mode,
			IBaseDataSourceDesign baseDataSourceDesign,
			IBaseDataSetDesign baseDataSetDesign, Collection parameterHints,
			Map appContext )
	{
		Object cacheObject = null;
		
		synchronized ( cacheMap )
		{
			cacheObject = cacheMap.get( dsAndDs );
		}
		
		if ( cacheObject != null  )
		{
			return needSaveToCache( cacheObject );
		}
		else
		{
			synchronized ( cacheMap )
			{
				cacheObject = (String) cacheMap.get( dsAndDs );
				if ( cacheObject != null )
						
				{
					return needSaveToCache( cacheObject );					
				}
				
				IDataSetCacheObject dsCacheObject = null;
				String cacheDir = (String) cacheDirMap.get( baseDataSetDesign );
				if ( baseDataSetDesign instanceof IIncreCacheDataSetDesign )
				{
					dsCacheObject = (IDataSetCacheObject) new IncreDataSetCacheObject( cacheDir );
				}
				else
				{
					switch ( mode )
					{
						case DataEngineContext.CACHE_MODE_IN_MEMORY :
							dsCacheObject = (IDataSetCacheObject) new MemoryDataSetCacheObject( );
							break;
						case DataEngineContext.CACHE_MODE_IN_DISK :
							String tempRootDir = CacheUtil.createTempRootDir( tempDir );
							String sessionTempDir = CacheUtil.createSessionTempDir( tempRootDir );
							dsCacheObject = (IDataSetCacheObject) new DiskDataSetCacheObject( sessionTempDir );
							break;
						default :
							return false;
					}
				}
				cacheMap.put( dsAndDs, dsCacheObject );
				return true;
			}
		}
	}

	/**
	 * 
	 * @param cacheObject
	 * @return
	 */
	private boolean needSaveToCache( Object cacheObject )
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			DiskDataSetCacheObject diskCacheObject = (DiskDataSetCacheObject) cacheObject;
			return !( diskCacheObject.getDataFile( ).exists( ) && diskCacheObject.getMetaFile( )
					.exists( ) );
		}
		else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return ( (MemoryDataSetCacheObject) cacheObject ).needPopulateResult( );
		}
		else if ( cacheObject instanceof IncreDataSetCacheObject )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param dsAndDs
	 * @return
	 */
	boolean doesLoadFromCache( DataSourceAndDataSet dsAndDs )
	{
		Object cacheObject = null;
		synchronized ( cacheMap )
		{
			cacheObject = cacheMap.get( dsAndDs );
		}
		if ( cacheObject != null )
		{
			return ( cacheObject instanceof IncreDataSetCacheObject ) ? true
					: !needSaveToCache( cacheObject );
		}
		return false;
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
		List cacheDir = new ArrayList( );
		synchronized ( cacheMap )
		{
			while ( getKey( dsAndDs ) != null )
				cacheDir.add( cacheMap.remove( getKey( dsAndDs ) ) );
		}
		for ( int i = 0; i < cacheDir.size( ); i++ )
		{
			Object cacheObject = cacheDir.get( i );
			if ( cacheObject instanceof DiskDataSetCacheObject )
			{
				// assume the following statement is thread-safe
				DiskDataSetCacheObject diskObject = (DiskDataSetCacheObject) cacheObject;
				deleteDir( diskObject.getTempDir( ) );
			}
			else if ( cacheObject instanceof IncreDataSetCacheObject )
			{
				IncreDataSetCacheObject psObject = (IncreDataSetCacheObject) cacheObject;
				deleteDir( psObject.getCacheDir( ) );
			}
		}

	}
	
	/**
	 * Reset for test case
	 */
	void resetForTest( )
	{
		synchronized ( this )
		{
			cacheMap = new HashMap( );
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
		Object cacheObject = null;
		Object key = getKey( dsAndDs );
		if ( key != null )
		{
			cacheObject = cacheMap.get( key );
			// TODO

		}

		if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return ( (MemoryDataSetCacheObject) cacheObject ).getResultClass( );
		}
		else if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			IResultClass rsClass;
			FileInputStream fis1 = null;
			BufferedInputStream bis1 = null;
			try
			{
				fis1 = new FileInputStream( ( (DiskDataSetCacheObject) cacheObject ).getMetaFile( ) );
				bis1 = new BufferedInputStream( fis1 );
				IOUtil.readInt( bis1 );
				rsClass = new ResultClass( bis1 );
				bis1.close( );
				fis1.close( );

				return rsClass;
			}
			catch ( FileNotFoundException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}

		return null;
	}
	
	/**
	 * 
	 * @param dsAndDs
	 * @return
	 */
	private Object getKey ( DataSourceAndDataSet dsAndDs )
	{
		for ( Iterator it = cacheMap.keySet().iterator(); it.hasNext(); )
		{
			DataSourceAndDataSet temp = ( DataSourceAndDataSet )it.next();
			if( temp.isDataSourceDataSetEqual( dsAndDs, false ) )
			{
				return temp;
			}
			
		}
		return null;
	}
	
	/**
	 * Delete folder
	 * 
	 * @param dirStr
	 */
	private void deleteDir( String dirStr )
	{
		File curDir = new File( dirStr );
		if ( !curDir.exists( ) )
			return;
		File[] files = curDir.listFiles( );
		for ( int i = 0; i < files.length; i++ )
			files[i].delete( );
		curDir.delete( );
	}

	
	/**
	 * @return the cacheDirMap
	 */
	Map getCacheDirMap( )
	{
		return cacheDirMap;
	}
	
	
}
