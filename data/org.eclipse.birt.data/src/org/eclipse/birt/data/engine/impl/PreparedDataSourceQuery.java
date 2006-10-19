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
package org.eclipse.birt.data.engine.impl;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Base class for a top-level prepared query that has its own data source (either extended data source, or 
 * scripted data source)  
 */
abstract class PreparedDataSourceQuery
		implements
			IPreparedQuery,
			IPreparedQueryService
{
	private IBaseDataSetDesign dataSetDesign;
	
	protected DataEngineImpl dataEngine;
	protected IQueryDefinition queryDefn;
	protected PreparedQuery preparedQuery;
	protected Map appContext;
	
	protected static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );
	
	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @param dataSetDesign
	 * @throws DataException
	 */
	PreparedDataSourceQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext )
			throws DataException
	{
		this.dataSetDesign = dataSetDesign;
		this.queryDefn = queryDefn;
		this.dataEngine = dataEngine;
		this.appContext = appContext;
		
		preparedQuery = new PreparedQuery( dataEngine.getSession( ),dataEngine.getContext( ),
				queryDefn,
				this,
				appContext );
	}
	
	/**
	 * @param dataEngine
	 * @param baseQueryDefn
	 * @param queryDefn
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	PreparedDataSourceQuery( DataEngineImpl dataEngine,
			IBaseQueryDefinition baseQueryDefn, IQueryDefinition queryDefn,
			IBaseDataSetDesign dataSetDesign,
			Map appContext )
			throws DataException
	{
		this.dataSetDesign = dataSetDesign;
		this.queryDefn = queryDefn;
		this.dataEngine = dataEngine;
		this.appContext = appContext;
		
		preparedQuery = new PreparedQuery( dataEngine.getSession( ), dataEngine.getContext( ),
				baseQueryDefn,
				this,
				appContext );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
	 */
	public IQueryDefinition getReportQueryDefn()
	{
		return this.queryDefn;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IPreparedQueryService#getDataSourceQuery()
	 */
	public PreparedDataSourceQuery getDataSourceQuery( )
	{
		return this;
	}
	
	/**
	 * Executes the prepared execution plan.  This returns
	 * a QueryResult object at a state ready to return its 
	 * current result iterator, or evaluate an aggregate expression.
	 * <p>
	 * This includes setup runtime state, and evaluation of
	 * any beforeOpen and afterOpen scripts on a data set. 
	 * @return The QueryResults object opened and ready to return
	 * 		the results of a report query. 
	 */
	public IQueryResults execute( Scriptable scope ) throws DataException
	{
		return this.execute( null, scope );
	}

	/**
	 * Executes the prepared execution plan as an inner query that appears
	 * within the scope of another report query. The outer query must have been
	 * prepared and executed, and its results given as a parameter to this
	 * method.
	 * 
	 * @param outerResults
	 *            QueryResults for the executed outer query
	 * @return The QueryResults object opened and ready to return the results of
	 *         a report query.
	 */
	public IQueryResults execute( IQueryResults outerResults, Scriptable scope )
			throws DataException
	{
		this.configureDataSetCache( queryDefn, appContext, scope != null
				? scope : dataEngine.getSession( ).getSharedScope( ) );

		return preparedQuery.doPrepare( outerResults,
				scope,
				newExecutor( ),
				this );
	}
	
	/**
	 * @param appContext
	 * @throws DataException
	 */
	private void configureDataSetCache( IQueryDefinition querySpec,
			Map appContext, Scriptable scope ) throws DataException
	{		
		if ( querySpec == null )
			return;

		String queryResultID = querySpec.getQueryResultsID( );
		if ( queryResultID != null )
			return;

		String dataSetName = querySpec.getDataSetName( );
		IBaseDataSetDesign dataSetDesign = this.dataEngine.getDataSetDesign( dataSetName );

		if ( dataSetDesign == null )
			return;

		if ( getDataSetCacheManager( ).needsToCache( dataSetDesign,
				DataSetCacheUtil.getCacheOption( dataEngine.getContext( ),
						appContext ),
				dataEngine.getContext( ).getCacheCount( ) ) == false )
			return;
		
		Collection parameterHints = null;
		
		IBaseDataSourceDesign dataSourceDesign = null;
		DataSourceRuntime dsRuntime = this.dataEngine.getDataSourceRuntime( dataSetDesign.getDataSourceName( ) );
		if ( dsRuntime != null )
		{
			dataSourceDesign = dsRuntime.getDesign( );
			DataSetRuntime dataSet = DataSetRuntime.newInstance( dataSetDesign,
					null );
			parameterHints = new ParameterUtil( null,
					dataSet,
					this.queryDefn,
					scope ).resolveDataSetParameters( true );
		}

		getDataSetCacheManager( )
				.setDataSourceAndDataSet( dataSourceDesign,
						dataSetDesign,
						parameterHints );

		if ( dataEngine.getContext( ).getCacheOption( ) == DataEngineContext.CACHE_USE_ALWAYS )
		{
			getDataSetCacheManager( )
					.setAlwaysCacheRowCount( dataEngine.getContext( )
							.getCacheCount( ) );
		}

		getDataSetCacheManager( )
				.setCacheOption( DataSetCacheUtil.getCacheOption( dataEngine.getContext( ),
						appContext ) );
	}

	/**
	 * 
	 * @return
	 */
	protected DataSetCacheManager getDataSetCacheManager( )
	{
		return this.dataEngine.getSession( ).getDataSetCacheManager( );
	}
	
	/**
	 * @return the appropriate subclass of the Executor
	 */
	protected abstract QueryExecutor newExecutor( );
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IPreparedQueryService#execSubquery(org.eclipse.birt.data.engine.odi.IResultIterator,
	 *      java.lang.String, org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execSubquery( IResultIterator iterator,
			String subQueryName, Scriptable subScope ) throws DataException
	{
		return this.preparedQuery.execSubquery( iterator,
				subQueryName,
				subScope );
	}
	
	/**
	 * 
	 */
	abstract class DSQueryExecutor extends QueryExecutor
	{

		public DSQueryExecutor( )
		{
			super( preparedQuery.getSharedScope( ),
					preparedQuery.getBaseQueryDefn( ),
					preparedQuery.getAggrTable( ),
					dataEngine.getSession( ));
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.QueryExecutor#findDataSource()
		 */
		protected DataSourceRuntime findDataSource( ) throws DataException
		{
			assert dataSetDesign != null;
			DataSourceRuntime dsRT = dataEngine.getDataSourceRuntime( dataSetDesign.getDataSourceName( ) );
			return dsRT;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.QueryExecutor#newDataSetRuntime()
		 */
		protected DataSetRuntime newDataSetRuntime( ) throws DataException
		{
			return DataSetRuntime.newInstance( dataSetDesign, this );
		}
	}
	
}
