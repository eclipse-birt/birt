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

import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;

/**
 * Cache manager for ODA data set and Scripted data set. Since connection to
 * data set and retrieve data are expensive operations, it is unnecessary to do
 * them again and again when user just want to see the data in the same
 * configuration of data set. The basic idea is when user want to use cache, the
 * data will be saved into cache in the first time and it will be loaded in the
 * later time. Once the configuration of data set is changed, the data needs to
 * be retrieved again.
 * 
 * Please notice the whole procedure: 1: first check whether data can be loaded
 * from cache 2.1: if yes, then data will be loaded and check whether data needs
 * to be saved into cache will be skiped. 2.2: if no, then data will be retrived
 * from data set and then whether saving into cache will be checked 2.2.1: if
 * yes, then data will be saved into cache 2.2.2: if no, then nothing will be
 * done
 * 
 * There are three possible value of cacheRowCount: 1: -1, cache all data set 2:
 * 0, don't cache 3: >0, cahe the specified value
 * 
 * Here whether data will be loaded from cache can be observed by external
 * caller, but about saving into cache is not.
 */
public class DataSetCacheManager
{
	// whether cache is needed
	private int cacheOption;

	// how many rows needs to be always cached
	private int alwaysCacheRowCount;
		
	// data set id and its cache count
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection parameterBindings;
	
	// current cache row count, its value is not possible be 0.
	private int cacheRowCount;
	
	// map manager instance
	private CacheMapManager cacheMapManager;
	
	// constant value
	public final static int ALWAYS = 1;
	public final static int DISABLE = 2;
	public final static int DEFAULT = 3;
	
	// instance
	private static DataSetCacheManager cacheManager;

	/**
	 * @return unique instance
	 */
	public static DataSetCacheManager getInstance( )
	{
		if ( cacheManager == null )
		{
			synchronized ( DataSetCacheManager.class )
			{
				if ( cacheManager == null )
					cacheManager = new DataSetCacheManager( );
			}
		}

		return cacheManager;
	}

	/**
	 * Construction
	 */
	private DataSetCacheManager( )
	{
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheRowCount = 0;
		cacheOption = DEFAULT;
		alwaysCacheRowCount = 0;
		
		cacheMapManager = new CacheMapManager( );
	}
	
	/**
	 * Enable cache on data set
	 * @param cacheOption
	 */
	public void setCacheOption( int cacheOption )
	{
		this.cacheOption = cacheOption;
	}
	
	/**
	 * @return
	 */
	public int suspendCache( )
	{
		int lastCacheOption = this.cacheOption;
		this.setCacheOption( DataSetCacheManager.DISABLE );
		return lastCacheOption;
	}

	/**
	 * @param rowCount
	 */
	public void setAlwaysCacheRowCount( int rowCount )
	{
		this.alwaysCacheRowCount = rowCount;
	}
	
	/**
	 * Remember before requesting any service, this function must be called in
	 * advance to make sure using current data source and data set.
	 * 
	 * @param dataSourceDesign
	 * @param datasetDesign
	 */
	public void setDataSourceAndDataSet(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign datasetDesign, Collection parameterBindings )
	{
		this.dataSourceDesign = dataSourceDesign;
		this.dataSetDesign = datasetDesign;
		if ( datasetDesign != null )
			this.cacheRowCount = datasetDesign.getCacheRowCount( );
		this.parameterBindings = parameterBindings;
	}

	/**
	 * @return
	 */
	public boolean doesSaveToCache( )
	{
		if ( needsToCache( this.dataSetDesign,
				this.cacheOption,
				this.alwaysCacheRowCount ) == false )
			return false;

		return cacheMapManager.doesSaveToCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings,
				getCacheCount( this.cacheOption,
						this.alwaysCacheRowCount,
						this.cacheRowCount ) ) );
	}

	/**
	 * @return
	 */
	public boolean doesLoadFromCache( )
	{
		if ( needsToCache( this.dataSetDesign,
				this.cacheOption,
				this.alwaysCacheRowCount ) == false )
			return false;

		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings,
				getCacheCount( this.cacheOption,
						this.alwaysCacheRowCount,
						this.cacheRowCount ) ) );
	}
	
	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @param parameterBindings
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 */
	public boolean doesLoadFromCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign, Collection parameterBindings,
			int cacheOption, int alwaysCacheRowCount )
	{
		if ( needsToCache( dataSetDesign, cacheOption, alwaysCacheRowCount ) == false )
			return false;

		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				dataSetDesign,
				parameterBindings,
				getCacheCount( this.cacheOption,
						this.alwaysCacheRowCount,
						this.cacheRowCount ) ) );
	}
	
	/**
	 * @return
	 */
	private static boolean needsToCache( IBaseDataSetDesign dataSetDesign,
			int cacheOption, int alwaysCacheRowCount )
	{
		if ( dataSetDesign == null )
			return false;

		if ( cacheOption == DISABLE )
		{
			return false;
		}
		else if ( cacheOption == ALWAYS )
		{
			if ( alwaysCacheRowCount == 0 )
				return false;
		}
		else if ( dataSetDesign.getCacheRowCount( ) == 0 )
		{
			return false;
		}

		return true;
	}
	
	/**
	 * @return
	 */
	public int getCacheRowCount( )
	{
		if ( cacheOption == ALWAYS )
		{
			if ( alwaysCacheRowCount <= 0 )
				return Integer.MAX_VALUE;
			else
				return alwaysCacheRowCount;
		}
		else if ( cacheOption == DISABLE )
		{
			return Integer.MAX_VALUE;
		}
		else
		{
			if ( cacheRowCount == -1 )
				return Integer.MAX_VALUE;
			else
				return cacheRowCount;
		}
	}

	/**
	 * Clear cache
	 * 
	 * @param dataSourceDesign2
	 * @param dataSetDesign2
	 */
	public void clearCache( IBaseDataSourceDesign dataSourceDesign2,
			IBaseDataSetDesign dataSetDesign2 )
	{
		if ( dataSourceDesign2 == null || dataSetDesign2 == null )
			return;

		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( dataSourceDesign2,
				dataSetDesign2,
				null,
				dataSetDesign2.getCacheRowCount( ) );
		cacheMapManager.clearCache( ds );
	}

	/**
	 * @return
	 */
	public String getSaveFolder( )
	{
		return cacheMapManager.getSaveFolder( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings,
				getCacheCount( this.cacheOption,
						this.alwaysCacheRowCount,
						this.cacheRowCount ) ) );
	}

	/**
	 * @return
	 */
	public String getLoadFolder( )
	{
		return cacheMapManager.getLoadFolder( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterBindings,
				getCacheCount( this.cacheOption,
						this.alwaysCacheRowCount,
						this.cacheRowCount ) ) );
	}
	
	/**
	 * @return
	 */
	private static int getCacheCount( int cacheOption, int alwaysCacheRowCount,
			int cacheRowCount )
	{
		int cacheCount = 0;
		if ( cacheOption == ALWAYS )
			cacheCount = alwaysCacheRowCount;
		else if ( cacheOption == DISABLE )
			assert false;
		else
			cacheCount = cacheRowCount;
		return cacheCount;
	}

	/**
	 * Notice, this method is only for test, it can not be called unless its use
	 * is for test.
	 */
	public void resetForTest( )
	{		
		dataSourceDesign = null;
		dataSetDesign = null;
		cacheRowCount = 0;
		cacheOption = DEFAULT;
		alwaysCacheRowCount = 0;
		
		this.cacheMapManager.resetForTest( );
	}
	
}
