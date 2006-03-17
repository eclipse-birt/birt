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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/** 
 * A report query's results opened and ready for data retrieval.  
 * A query results could contain multiple result sets.
 * This is intended to be used by both Factory and Presentation Engines
 * in BIRT, including later releases when a report document persists.
 * Beyond Release 1, this would include methods to save and restore
 * results in a persisted Report Document.
 */
class QueryResults implements IQueryResults
{
	// context of data engine
	private DataEngineContext 			context;
	private Scriptable 					queryScope;
	private int 						nestedLevel;
	// id of this instance
	private String 						queryResultID;

	// query service instance
	private IQueryService 				queryService;
	private ResultIterator				iterator;
		
	private static Logger logger = Logger.getLogger( QueryResults.class.getName( ) );
	
	/**
	 * @param queryService
	 * @param queryScope
	 * @param nestedLevel
	 */
	QueryResults( IQueryService queryService, Scriptable queryScope,
			int nestedLevel )
	{
		assert queryService != null;

		this.context = queryService.getContext( );
		this.queryService = queryService;
		this.queryScope = queryScope;
		this.nestedLevel = nestedLevel;

		logger.logp( Level.FINER,
				QueryResults.class.getName( ),
				"QueryResults",
				"QueryResults starts up" );
	}
		
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		if ( queryResultID == null )
			queryResultID = IDUtil.nextQursID( );
		
		return queryResultID;
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
		finally
		{
			logger.logp( Level.FINE,
					QueryResults.class.getName( ),
					"getResultMetaData",
					"return the result metadata" );
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
		logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getResultIterator",
				"start" );
		
		if ( queryService == null )
			throw new DataException( ResourceConstants.RESULT_CLOSED );

		if ( iterator == null )
		{
			iterator = new ResultIterator( new ResultService( context, this ),
					queryService.executeQuery( ),
					this.queryScope );
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
			logger.logp( Level.FINE,
					QueryResults.class.getName( ),
					"close",
					"QueryResults is closed" );
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
	 * @return
	 */
	boolean isClosed( )
	{
		return queryService == null;
	}

	/**
	 * @return
	 */
	Scriptable getQueryScope( )
	{
		return this.queryScope;
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
	
	/**
	 * @return
	 */
	int getNestedLevel( )
	{
		return this.nestedLevel;
	}
	
	/**
	 * @param count
	 * @return
	 */
	DataSetRuntime[] getDataSetRuntime( int count )
	{
		return this.queryService.getDataSetRuntimes( count );
	}
	
	/**
	 * Set current queryresult ID for sub query. Sub query result ID can not be
	 * generated independently, and it is needs to be attached with its parent
	 * query.
	 * 
	 * @param queryResultID
	 */
	void setID( String queryResultID )
	{
		this.queryResultID = queryResultID;
	}

	/**
	 * 
	 */
	private static class ResultService implements IResultService
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
		public QueryResults execSubquery(
				org.eclipse.birt.data.engine.odi.IResultIterator iterator,
				String subQueryName, Scriptable subScope ) throws DataException
		{
			return queryResults.queryService.execSubquery( iterator,
					subQueryName,
					subScope );
		}
	}
	
}

