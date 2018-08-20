/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 

package org.eclipse.birt.data.engine.api;

import java.io.File;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.mozilla.javascript.Scriptable;

/**
 * Data Engine API class.
 * <br>
 * Provides methods to define data sources and data sets, and to prepare a
 * {@link org.eclipse.birt.data.engine.api.IQueryDefinition}. An application
 * typically needs only one instance of this class, which can be used to 
 * prepare and execute multiple data queries.
 * <p>
 * User of this class must always call the <code>shutdown</code> method when it is
 * done with an instance of this class to ensure release of all data source 
 * connections and related resources.
 */
abstract public class DataEngine
{
	
	/**
	 * Indicates whether data set cache function needs to be used. If yes, put a
	 * true value of this property to appContext and set it by the method of
	 * prepare.
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.data.engine.api.DataEngine.prepare(
	 *      IQueryDefinition querySpec, Map appContext)
	 */
	public static String DATASET_CACHE_OPTION = "org.eclipse.birt.data.engine.dataset.cache.option";
	
	/**
	 * Indicates whether data set cache function need to be used:
	 * 
	 * Negative integer: Enable data set cache. The row limit will not be set and all data set rows are fetched.
	 * 
	 * Positive integer: Enable data set cache. This row limit will be used for ALL
     * data sets, regardless of settings on individual data set designs.
     * 
     * Zero:Disable data set cache regardless of report design settings and
	 * DataEngineContext settings. 
	 */
	public static String DATA_SET_CACHE_ROW_LIMIT = "org.eclipse.birt.data.cache.RowLimit";

	/**
	 * Indicate the size of data cached for each result set.We only accept non-negative integer as input, 
	 * the unit of which would be MB. 
	 * If this setting is 0, all temporary rows will be cached in memory during query processing.
	 */
	public static String MEMORY_BUFFER_SIZE = "org.eclipse.birt.data.query.ResultBufferSize";
	
	/**
	 * This setting allow the administrator to set a limit on the amount of data that a query against 
	 * a data object is allowed to access. Queries should be aborted with error if it exceeds this limit.
	 */
	public static String MAX_DATA_OBJECT_ROWS = "org.eclipse.birt.data.query.MaxDataObjectRows";
	
	
	public static String DATA_SET_CACHE_DELTA_FILE = "org.eclipse.birt.data.cache.DeltaFile";
	
	/**
	 * Indicates whether memory data set cache function need to be used:
	 * 
	 * Positive integer: Enable memory data set cache. This row limit will be used for ALL
     * data sets, regardless of settings on individual data set designs.
     * 
     * Zero:Disable memory data set cache regardless of report design settings and
	 * DataEngineContext settings. 
	 */
	public static String MEMORY_DATA_SET_CACHE = "org.eclipse.birt.data.cache.memory";
	
	/**
	 * Indicates the session id of which a set of queries will be executed. 
	 */
	public static String QUERY_EXECUTION_SESSION_ID = "org.eclipse.birt.data.query.session.id";
	
	/**
	 * Indicates whether enable sample data preview in current session.
	 * If the setting is set to true, we will match the cache based on session id and data source/data set name. We will also generate dummy data to avoid. 
	 */
	public static String QUERY_EXECUTION_SESSION_ENABLE_SAMPLEDATAPREVIEW = "org.eclipse.birt.data.query.session.enableSampleDataPreview";
	
	/**
	 * Indicates data engine memory usage hint level
	 */
	public static String MEMORY_USAGE = "org.eclipse.birt.data.memoryusage";
	
	/**
	 *  This level is corresponding to 100% memory based
	 */
	public static String MEMORY_USAGE_AGGRESSIVE = "org.eclipse.birt.data.memoryusage.Aggressive";
	
	/**
	 * This level is corresponding to partly disk based
	 */
	public static String MEMORY_USAGE_NORMAL = "org.eclipse.birt.data.memoryusage.normal";
	
	/**
	 * This level is corresponding to mainly disk based 
	 */
	public static String MEMORY_USAGE_CONSERVATIVE = "org.eclipse.birt.data.memoryusage.conservative";
	
	/**
	 * data set incremental cache file URL.
	 */
	public static String INCREMENTAL_CACHE_CONFIG = "org.eclipse.birt.data.cache.incremental.config";

	/**
	 * The fetch size on column and row edge in cube cursor.
	 * 
	 * Positive integer: the number setting on edge. 
	 * Zero or Negative number:Disable the number setting on edge.
	 */
	public static String CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE = "org.eclipse.birt.data.engine.olap.cursor.onColumn";
	public static String CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE = "org.eclipse.birt.data.engine.olap.cursor.onRow";
	
	/**
	 * The fetch size of the member numbers for all levels.
	 * 
	 * Positive integer: the member numbers will be fetched from levels.
	 * Zero or Negative integer:Disable this number setting on levels.
	 * 
	 * @deprecated use 
                   DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE and
	 *             DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE instead
	 */
	public static String CUBECURSOR_FETCH_LIMIT_ON_LEVEL = "org.eclipse.birt.data.engine.olap.cursor.onLevel";
	
	/**
	 * The size of cube to be saved in memory rather than in disk. The unit is MB. In case that the size
	 * of materialized cube exceed the assigned memory size, the disk will automatically be used. 
	 */
	public static String IN_MEMORY_CUBE_SIZE = "org.eclipse.birt.data.engine.cube.inmemory.size";
	
	/**
	 * Creates a new instance of DataEngine, using the specified
	 * DataEngineContext as its running environment
	 * 
	 * @param context,
	 *            When this value is null, a default context will be used. The
	 *            default context is DataEngineContext.MODE_DIRECTPRESENT.
	 * @return an instance of DataEngine under specified context
	 */
    public static DataEngine newDataEngine( DataEngineContext context )
    {
    	try
		{
			return newDataEngine( null, context );
		}
		catch ( BirtException e )
		{
			// impossible to get here in normal case
			throw new RuntimeException( e );
		}
    }
    
    /**
     * @param context
     * @return
     * @throws BirtException 
     */
    public static DataEngine newDataEngine( PlatformConfig platformConfig,
			DataEngineContext dataContext ) throws BirtException
	{
		Platform.startup( platformConfig );

		Object factory = Platform.createFactoryObject( IDataEngineFactory.EXTENSION_DATA_ENGINE_FACTORY );
		if ( factory instanceof IDataEngineFactory )
		{
			return ( (IDataEngineFactory) factory ).createDataEngine( dataContext );
		}
		else
		{
			throw new DataException( ResourceConstants.LOAD_FACTORY_ERROR );
		}
	}
    
    /**
	 * Creates a new instance of DataEngine, using the specified Javascript
	 * scope and home directory setting.
	 * 
	 * @param sharedScope
	 *            a Javascript scope to be used as the "shared" scope to
	 *            evaluate Javascript expressions by the data engine.
	 * @deprecated use newDataEngine( DataEngineContext context ) instead
	 */
    public static DataEngine newDataEngine( Scriptable sharedScope )
	{
		try
		{
			return newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					sharedScope,
					null,
					null ) );
		}
		catch ( BirtException e )
		{
			// impossible get here
			return null;
		}
	}
	
    /**
     * @deprecated Use newDataEngine(Scriptable) instead. Home Dir is no longer used.
     */
    public static DataEngine newDataEngine( Scriptable sharedScope, File homeDir )
    {
        return newDataEngine( sharedScope );
    }
        
    /**
	 * If and only if current mode is DataEngineContext.MODE_PRESENTATION, query
	 * result can be retrieved from report document. Otherwise a BirtException
	 * will be thrown immediatelly.
	 * 
	 * @param queryResultID
	 * @return an instanceof IQueryResults
	 * @throws BirtException
	 */
	public abstract IQueryResults getQueryResults( String queryResultID )
			throws BirtException;
    
	/**
	 * Provides the definition of a data source to Data Engine. A data source
	 * must be defined using this method prior to preparing any report query
	 * that uses such data source. <br>
	 * Data sources are uniquely identified name. If specified data source has
	 * already been defined, its definition will be updated with the content of
	 * the provided definition object.
	 */
	abstract public void defineDataSource( IBaseDataSourceDesign dataSource ) 
			throws BirtException;

	/**
	 * Provides the definition of a data set to Data Engine. A data set must be
	 * defined using this method prior to preparing any report query that uses such data set.
	 * <br>
	 * Data sets are uniquely identified name. If specified data set has already
	 * been defined, its definition will be updated with the content of the provided definition object.
	 */
	abstract public void defineDataSet( IBaseDataSetDesign dataSet ) 
			throws BirtException;
	
	/**
	 * This method will return NULL if the named data set is not cached. Otherwise, it
	 * will return an IResultMetaData instance which provides at least the ColumnName
	 * and ColumnType information for all cached columns.
	 * 
	 * Please note that the ParameterHint information will usually essential to the result of data set design is
	 * omitted for it has nothing to do with the metadata.
	 * @param dataSource
	 * @param dataSet
	 * @param parameterHints
	 * @return
	 * @throws BirtException
	 */
	abstract public IResultMetaData getCachedDataSetMetaData( IBaseDataSourceDesign dataSource,
                   IBaseDataSetDesign dataSet ) throws BirtException;
	
	/**
	 * If user wants to use the latest data from data set rather than local
	 * cache, user needs to call this clearCache method to delete the cache
	 * content from update local environment. After it is called, in the next
	 * time the data will be retrieved again from data source to update cache.
	 * 
	 * @param dataSource, which is associated with the data set
	 * @param dataSet, which cache needs to be cleared
	 * @throws BirtException
	 */
	abstract public void clearCache( IBaseDataSourceDesign dataSource,
			IBaseDataSetDesign dataSet ) throws BirtException;
	
	/**
	 * Clear the cache base on cacheID
	 * 
	 * @param cacheID
	 * @throws BirtException
	 */
	abstract public void clearCache( String cacheID ) throws BirtException;
	
	abstract public void clearCache() throws BirtException;
	
	/**
	 * Verifies the elements of a report query spec
	 * and provides a hint to the query to prepare and optimize 
	 * an execution plan.
	 * The given querySpec could be a <code>IQueryDefinition</code> 
	 * (raw data transform) spec  
	 * based on static definition found in a report design.
	 * <p> 
	 * This report query spec could be further refined 
	 * during engine execution after having resolved any related
	 * runtime condition.
	 * For example, a nested report item might not be rendered based
	 * on a runtime condition.  Thus its associated data expression
	 * could be removed from the report query defn given to 
	 * DtE to prepare.
	 * <p>
	 * During prepare, the DTE does not open a data set. 
	 * In other words, any <code>beforeOpen</code> script on a data set will not be
	 * evaluated at this stage. 
	 * @param	querySpec	Specifies
	 * 				the data access and data transforms services
	 * 				needed from DtE to produce a set of query results.
	 * @return		The <code>IPreparedQuery</code> object that contains a prepared 
	 * 				query ready for execution.
	 * @throws 		DataException if error occurs during the preparation of querySpec
	 */
	abstract public IPreparedQuery prepare( IQueryDefinition querySpec )
			throws BirtException;

	/**
	 * Verifies the elements of a report query spec,
	 * and provides a hint and application context object(s) to the 
	 * query to prepare and optimize an execution plan.
	 * <br>This has the same behavior as the 
	 * prepare( IQueryDefinition querySpec ) method,
	 * with an additional argument for an application to pass in 
	 * a context map to the underlying data provider, 
	 * e.g. an ODA run-time driver.
	 * @param	querySpec	Specifies
	 * 				the data access and data transforms services
	 * 				needed from DtE to produce a set of query results.
	 * @param appContext	The application context map for 
	 * 				preparation and execution of the querySpec; 
	 * 				could be null.
	 * @return		The <code>IPreparedQuery</code> object that contains a prepared 
	 * 				query ready for execution.
	 * @throws 		BirtException if error occurs during the preparation of querySpec
	 * @since		2.0
	 */
	abstract public IPreparedQuery prepare( IQueryDefinition querySpec, 
	        								Map appContext )
			throws BirtException;

	/**
	 * Optimize query execution by analyze a list of query definitions.
	 * @param queryDefns
	 * @throws DataException
	 */
	abstract public void registerQueries( IDataQueryDefinition[] queryDefns ) throws DataException;
	
	/**
	 * Provides a hint to DtE that the consumer is done with the given 
	 * data source, and 
	 * that its resources can be safely released as appropriate.
	 * This tells DtE that there is to be no more query
	 * that uses such data source.
	 * @param	dataSourceName	The name of a data source. The named data source
	 *          must have been previously defined.
	 */
	abstract public void closeDataSource( String dataSourceName )
			throws BirtException;
	
	/**
	 * Prepare the cube query definition, return an IPreparedCubeQuery instance 
	 * @param query
	 * @param appContext
	 * @return
	 * @throws BirtException
	 */
	abstract public IPreparedCubeQuery prepare( ICubeQueryDefinition query,
			Map appContext ) throws BirtException;
	
	/**
	 * Prepare the sub cube query definition, return an IPreparedCubeQuery instance
	 * 
	 * @param query
	 * @param appContext
	 * @return
	 * @throws BirtException
	 */
	abstract public IPreparedCubeQuery prepare( ISubCubeQueryDefinition query,
			Map appContext ) throws BirtException;
	
	/**
	 * Add a shut down listener. When the data engine shut down the listener
	 * will be notified.
	 */
	abstract public void addShutdownListener( IShutdownListener listener );
	
	/**
	 * Remove a shut down listener. 
	 * @param listener
	 */
	abstract public void removeListener( IShutdownListener listener );
	
	/**
	 * Cancel this data engine execution
	 */
	abstract public void cancel();
	
	/**
	 * Shuts down this instance of data engine, and releases all associated resources.
	 * This method should be called when the caller is done with an instance of the data engine.
	 */
	abstract public void shutdown();
	
}

