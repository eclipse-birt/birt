/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.mozilla.javascript.Scriptable;

/** 
 * A report query's results opened and ready for data retrieval.  
 * A query results could contain multiple result sets.
 * This is intended to be used by both Factory and Presentation Engines
 * in BIRT, including later releases when a report document persists.
 * Beyond Release 1, this would include methods to save and restore
 * results in a persisted Report Document.
 */
public class QueryResults implements IQueryResults, IQueryService
{
	// query service instance
	private IServiceForQueryResults 	queryService;
	
	// context of data engine
	private DataEngineContext 			context;
	private Scriptable 					queryScope;
	private int 						nestedLevel;
	
	// id of this instance
	private String                      rootQueryResultID;
	private String 						selfQueryResultID;

	private ResultIterator				iterator;
		
	private static Logger logger = Logger.getLogger( QueryResults.class.getName( ) );
	
	/**
	 * @param queryService
	 * @param queryScope
	 * @param nestedLevel
	 */
	QueryResults( IServiceForQueryResults queryService )
	{
		assert queryService != null;

		this.queryService = queryService;
		this.context = queryService.getContext( );
		this.queryScope = queryService.getScope( );
		this.nestedLevel = queryService.getNestedLevel( );
	}
		
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		if ( selfQueryResultID == null )
			selfQueryResultID = QueryResultIDUtil.nextID( );

		return QueryResultIDUtil.buildID( rootQueryResultID, selfQueryResultID );
	}
	
	/*
	 * Returns the PreparedQuery that contains the execution plan for producing
	 * this. A convenience method for the API consumer.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery()
	{ 
		return queryService.getPreparedQuery( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		if ( queryService == null )
			throw new DataException( ResourceConstants.RESULT_CLOSED );
		
		try
		{
			return queryService.getResultMetaData( );
		}
		catch ( DataException e )
		{
			throw e;
		}
	}
	
	/*
	 * 
	 * Returns the current result's iterator. Repeated call of this method
	 * without having advanced to the next result would return the same iterator
	 * at its current state.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
	 */
	public IResultIterator getResultIterator( ) throws DataException
	{ 
		if ( queryService == null )
			throw new DataException( ResourceConstants.RESULT_CLOSED );

		if ( iterator == null )
		{
			// data row binding
			this.queryService.initAutoBinding( );
			this.queryService.validateQueryColumBinding( );
			
			if ( queryService.getQueryDefn( ).usesDetails( ) == true )
				iterator = new ResultIterator( new ResultService( context, this ),
						queryService.executeQuery( ),
						this.queryScope );
			else
				iterator = new ResultIterator2( new ResultService( context,
						this ), queryService.executeQuery( ), this.queryScope );
		}
		
		logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getResultIterator",
				"finished" );
		return iterator;
	}
	
	/*
	 * Closes all query result set(s) associated with this object; provides a
	 * hint to the query that it can safely release all associated resources.
	 * The query results might have iterators open on them. Iterators associated
	 * with the query result sets are invalidated and can no longer be used.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		if ( this.queryService == null )
		{
			// already closed
			return;
		}

		if ( iterator != null )
		{
			iterator.close( );
			iterator = null;
		}
	    
		queryService.close( );
		queryService = null;
		logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"close",
				"QueryResults is closed" );
	}
	
	/**
	 * @param rootQueryResultID
	 * @param selfQueryResultID
	 */
	void setID( String rootQueryResultID, String selfQueryResultID )
	{
		this.rootQueryResultID = rootQueryResultID;
		this.selfQueryResultID = selfQueryResultID;
	}
	
	/**
	 * Set current queryresult ID for sub query. Sub query result ID can not be
	 * generated independently, and it is needs to be attached with its parent
	 * query.
	 * 
	 * @param selfQueryResultID
	 */
	void setID( String selfQueryResultID )
	{
		this.setID( null, selfQueryResultID );
	}
	
	/**
	 * If current query results is associated with a sub query, its result
	 * iterator needs to know which group level this sub query belongs to.
	 * 
	 * @return group level of sub query
	 */
	int getGroupLevel( )
	{
		return queryService.getGroupLevel( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#isClosed()
	 */
	public boolean isClosed( )
	{
		return queryService == null;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getNestedLevel()
	 */
	public int getNestedLevel( )
	{
		return this.nestedLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryScope()
	 */
	public Scriptable getQueryScope( )
	{
		return this.queryScope;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getExecutorHelper()
	 */
	public IExecutorHelper getExecutorHelper( ) throws DataException
	{
		return ( (ResultIterator) this.getResultIterator( ) ).getOdiResult( )
				.getExecutorHelper( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
	 */
	public DataSetRuntime[] getDataSetRuntime( int count )
	{
		return this.queryService.getDataSetRuntimes( count );
	}

	/**
	 * 
	 */
	private static class ResultService implements IServiceForResultSet
	{
		/** */
		private DataEngineContext context;
		private QueryResults queryResults;
		
		/**
		 * @param queryResults
		 */
		public ResultService( DataEngineContext context,
				QueryResults queryResults )
		{
			this.context = context;
			this.queryResults = queryResults;
		}		

		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getContext()
		 */
		public DataEngineContext getContext( )
		{
			return context;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getQueryResults()
		 */
		public IQueryResults getQueryResults( )
		{
			return queryResults;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getQueryDefn()
		 */
		public IBaseQueryDefinition getQueryDefn( )
		{
			return queryResults.queryService.getQueryDefn( );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#execSubquery(org.eclipse.birt.data.engine.odi.IResultIterator,
		 *      java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public IQueryResults execSubquery(
				org.eclipse.birt.data.engine.odi.IResultIterator iterator,
				String subQueryName, Scriptable subScope ) throws DataException
		{
			return queryResults.queryService.execSubquery( iterator,
					subQueryName,
					subScope );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getBaseExpression(java.lang.String)
		 */
		public IBaseExpression getBindingExpr( String exprName )
		{
			return queryResults.queryService.getBindingExpr( exprName );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getAutoBindingExpr(java.lang.String)
		 */
		public IScriptExpression getAutoBindingExpr( String exprName )
		{
			return queryResults.queryService.getAutoBindingExpr( exprName );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IServiceForResultSet#getAllBindingExprs()
		 */
		public List getAllBindingExprs( )
		{
			return queryResults.queryService.getAllBindingExprs( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IServiceForResultSet#getAllAutoBindingExprs()
		 */
		public Map getAllAutoBindingExprs( )
		{
			return queryResults.queryService.getAllAutoBindingExprs( );
		}
	}
	
}

