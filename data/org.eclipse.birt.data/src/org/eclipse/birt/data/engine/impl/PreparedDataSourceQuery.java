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

import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
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
		
		preparedQuery = new PreparedQuery( dataEngine.getContext( ),
				dataEngine.getSharedScope( ),
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
		
		preparedQuery = new PreparedQuery( dataEngine.getContext( ),
				dataEngine.getSharedScope( ),
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
		return preparedQuery.doPrepare( outerResults,
				scope,
				newExecutor( ),
				this );
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
					preparedQuery.getAggrTable( ) );
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
