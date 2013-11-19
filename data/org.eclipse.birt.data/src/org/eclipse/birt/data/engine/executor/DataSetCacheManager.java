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
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IShutdownListener;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.DataSetCacheUtil;
import org.eclipse.birt.data.engine.impl.IEngineExecutionHints;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.odi.IResultClass;

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
 * to be saved into cache will be skipped. 2.2: if no, then data will be retrieved
 * from data set and then whether saving into cache will be checked 2.2.1: if
 * yes, then data will be saved into cache 2.2.2: if no, then nothing will be
 * done
 * 
 * There are three possible value of cacheRowCount: 1: -1, cache all data set 2:
 * 0, don't cache 3: >0, cache the specified value
 * 
 * Here whether data will be loaded from cache can be observed by external
 * caller, but about saving into cache is not.
 */
public class DataSetCacheManager
{

	// data set id and its cache count
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection parameterHints;
	private Map appContext;
	private String cacheID;
	private boolean enableSamplePreview;
	// map manager instance
	private CacheMapManager jvmLevelCacheMapManager;
	private CacheMapManager dteLevelCacheMapManager;
	private CacheMapManager cacheMapManager;
	
	private IEngineExecutionHints queryExecutionHints;
	
	private DataEngineContext context;
	private DataEngineSession session;
	
	/**
	 * Construction
	 */
	public DataSetCacheManager( DataEngineSession session )
	{
		this.session = session;
		this.context = session.getEngineContext( );
		this.queryExecutionHints = ((DataEngineImpl)session.getEngine( )).getExecutionHints( );
		this.jvmLevelCacheMapManager = new CacheMapManager( true );
		this.dteLevelCacheMapManager = new CacheMapManager( false );
		
		session.getEngine( ).addShutdownListener( new IShutdownListener(){

			public void dataEngineShutdown( )
			{
				try
				{
					dteLevelCacheMapManager.clearCache( );
				}
				catch ( Exception e )
				{
				}

			}} );
	}

	/**
	 * 
	 * @return
	 */
	public IBaseDataSourceDesign getCurrentDataSourceDesign( )
	{
		return this.dataSourceDesign;
	}
	
	/**
	 * 
	 * @return
	 */
	public IBaseDataSetDesign getCurrentDataSetDesign()
	{
		return this.dataSetDesign;
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection getCurrentParameterHints()
	{
		if ( this.parameterHints != null )
			return this.parameterHints;
		else
			return new ArrayList();
	}
	
	/**
	 * 
	 * @return
	 */
	public Map getCurrentAppContext()
	{
		return this.appContext;
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
			IBaseDataSetDesign dataSetDesign, Collection parameterHints, Map appContext )
	{
		this.dataSourceDesign = dataSourceDesign;
		this.dataSetDesign = dataSetDesign;
		this.parameterHints = parameterHints;
		this.appContext = appContext;
		this.cacheID = CacheIDFetcher.getInstance( ).getCacheID( appContext );
		this.enableSamplePreview =  CacheIDFetcher.getInstance( ).enableSampleDataPreivew( appContext );
	}

	/**
	 * @return
	 * @throws DataException 
	 */
	public boolean doesSaveToCache( ) throws DataException
	{
		DataSetCacheConfig dscc = getDataSetCacheConfig(dataSetDesign, appContext);
		if ( dscc == null)
		{
			return false;
		}
		switchCacheMap( dataSetDesign );
		return cacheMapManager.doesSaveToCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ),
				dscc);
	}

	/**
	 * @return
	 * @throws DataException 
	 */
	public boolean needsToCache( ) throws DataException
	{
		return needsToCache( dataSetDesign, appContext );
	}

	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @param parameterBindings
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 * @throws DataException 
	 */
	public boolean doesLoadFromCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign,
			Collection parameterHints, Map appContext ) throws DataException
	{
		DataSetCacheConfig dscc = getDataSetCacheConfig(dataSetDesign, appContext);
		if ( dscc == null)
		{
			return false;
		}
		this.setDataSourceAndDataSet( dataSourceDesign,
				dataSetDesign,
				parameterHints,
				appContext);
		switchCacheMap( dataSetDesign );
		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				dataSetDesign,
				parameterHints, this.cacheID, this.enableSamplePreview ),
				dscc.getCacheCapability( ));
	}

	/**
	 * @param dataSetDesign
	 * @param cacheOption
	 * @param alwaysCacheRowCount
	 * @return
	 * @throws DataException 
	 */
	public boolean needsToCache( IBaseDataSetDesign dataSetDesign, Map appContext) throws DataException
	{
		return getDataSetCacheConfig(dataSetDesign, appContext) != null;
 	}
	
	private DataSetCacheConfig getDataSetCacheConfig(IBaseDataSetDesign dataSetDesign, Map appContext) throws DataException
	{
		DataSetCacheConfig result = DataSetCacheUtil.getJVMDataSetCacheConfig( appContext, context, dataSetDesign );
		if (result == null)
		{
			result = DataSetCacheUtil.getDteDataSetCacheConfig( queryExecutionHints, dataSetDesign, session, appContext );
		}
		return result;
	}
	
	/**
	 * @return
	 * @throws DataException 
	 */
	public int getCacheCapability( ) throws DataException
	{
		DataSetCacheConfig dscc = this.getDataSetCacheConfig( dataSetDesign, appContext );
		if (dscc != null)
		{
			return dscc.getCacheCapability( );
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * @return >0: the max row count to be cached
	 *         <0: unlimited cache capability
	 *         =0: do not use cache at all 
	 * @throws DataException 
	 */
	public int getCacheCountConfig( ) throws DataException
	{
		DataSetCacheConfig dscc = this.getDataSetCacheConfig( dataSetDesign, appContext );
		if (dscc != null)
		{
			return dscc.getCountConfig( );
		}
		else
		{
			//do not use cache at all
			return 0;
		}
	}

	/**
	 * Clear cache
	 * 
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @throws DataException 
	 */
	public void clearCache( IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign ) throws DataException
	{
		if ( dataSourceDesign == null || dataSetDesign == null )
			return;

		DataSourceAndDataSet ds = DataSourceAndDataSet.newInstance( dataSourceDesign,
				dataSetDesign,
				null, this.cacheID, this.enableSamplePreview );
		//switchCacheMap( dataSetDesign );
		//cacheMapManager.clearCache( ds );
		jvmLevelCacheMapManager.clearCache( ds );
		dteLevelCacheMapManager.clearCache( ds );
	}

	public void clearCache( String cacheID )
	{
		Set<String> temp = new HashSet<String>();
		temp.add( cacheID );
		jvmLevelCacheMapManager.clearCache( temp );
	}
	
	/**
	 * @return
	 * @throws DataException 
	 */
	public IDataSetCacheObject getSavedCacheObject( ) throws DataException
	{
		switchCacheMap( dataSetDesign );	
				
		IDataSetCacheObject cached = cacheMapManager.getSavedCacheObject( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ) ); 
		if( this.cacheID != null && cached instanceof MemoryDataSetCacheObject && ((MemoryDataSetCacheObject)cached).getSize( ) > 0 )
		{
			cached = new DataSetCacheObjectWithDummyData( dataSetDesign, cached );
		}	
		return cached;
	}
	
	/**
	 * @return
	 * @throws DataException 
	 */
	public void saveFinished( IDataSetCacheObject dsco ) throws DataException
	{
		switchCacheMap( dataSetDesign );	
		
		cacheMapManager.saveFinishOnCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ), dsco );
	}
	
	/**
	 * 
	 * @param dsco
	 * @throws DataException
	 */
	public void loadStart( ) throws DataException
	{
		switchCacheMap( dataSetDesign );	
		
		cacheMapManager.loadStart( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ) );
	}
	
	/**
	 * @return
	 * @throws DataException 
	 */
	public void loadFinished( ) throws DataException
	{
		switchCacheMap( dataSetDesign );	
		
		cacheMapManager.loadFinishOnCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ) );
	}
	
	/**
	 * @return
	 * @throws DataException 
	 */
	public IDataSetCacheObject getLoadedCacheObject( ) throws DataException
	{
		switchCacheMap( dataSetDesign );
				
		IDataSetCacheObject cached = cacheMapManager.getloadedCacheObject( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ) ); 
		if( this.cacheID != null && cached instanceof MemoryDataSetCacheObject && ((MemoryDataSetCacheObject)cached).getSize( ) > 0 )
		{
			cached = new DataSetCacheObjectWithDummyData( dataSetDesign, cached );
		}	
		return cached;
	}

	/**
	 * only for test
	 * 
	 * @return
	 * @throws DataException 
	 */
	public boolean doesLoadFromCache( ) throws DataException
	{
		DataSetCacheConfig dscc = getDataSetCacheConfig(dataSetDesign, appContext);
		if (dscc == null)
		{
			return false;
		}
		switchCacheMap( dataSetDesign );
		return cacheMapManager.doesLoadFromCache( DataSourceAndDataSet.newInstance( this.dataSourceDesign,
				this.dataSetDesign,
				this.parameterHints, this.cacheID, this.enableSamplePreview ),
				dscc.getCacheCapability( ));
	}

	/**
	 * Notice, this method is only for test, it can not be called unless its use
	 * is for test.
	 */
	public void resetForTest( )
	{
		dataSourceDesign = null;
		dataSetDesign = null;
		if ( this.cacheMapManager != null )
		{
			this.cacheMapManager.resetForTest( );
		}
	}

	/**
	 * Return the cached result metadata. Please note that parameter hint will
	 * not change the returned metadata.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData getCachedResultMetadata(
			IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet )
			throws DataException
	{
		//switchCacheMap( dataSet );
		//meta data is always from jvmLevelCacheMapManager
		IResultClass resultClass = this.jvmLevelCacheMapManager.getCachedResultClass( DataSourceAndDataSet.newInstance( dataSource,
				dataSet,
				null, this.cacheID, this.enableSamplePreview ) );
		if ( resultClass != null )
			return new ResultMetaData( resultClass );
		else
			return null;
	}

	
	/**
	 * 
	 * @param dataSetDesign
	 * @throws DataException 
	 */
	private void switchCacheMap( IBaseDataSetDesign dataSetDesign ) throws DataException
	{
		if( DataSetCacheUtil.getJVMDataSetCacheConfig( appContext, context, dataSetDesign ) != null )
		{
			cacheMapManager = jvmLevelCacheMapManager;
		}
		else
		{
			cacheMapManager = dteLevelCacheMapManager;
		}
	}
}
