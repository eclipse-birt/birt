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
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;

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
	protected PreparedDataSourceQuery 	reportQuery;
	protected PreparedQuery				query;
	protected ResultIterator			iterator;
	protected PreparedQuery.Executor	queryExecutor;
	protected static Logger logger = Logger.getLogger( QueryResults.class.getName( ) );

	// context of data engine
	private DataEngineContext context;
	
	// id of this instance
	private String queryResultID;
	
	/**
	 * @param reportQuery The associated report query.
	 * @param query The actual query (either report query or subquery)
	 * @param odiResult associated Odi result iterator.
	 * @param aggrResult associated aggregate results
	 * @param scope scope used for this result set
	 */
	
	QueryResults( DataEngineContext context,
			PreparedDataSourceQuery reportQuery, PreparedQuery query,
			PreparedQuery.Executor executor )
	{
		assert executor != null;
	    assert query != null;
	    assert reportQuery != null;
	    assert executor.scope != null;
	    
	    this.context = context;
	    this.reportQuery = reportQuery;
	    this.query = query;
	    this.queryExecutor = executor;

	    logger.logp( Level.FINER,
				QueryResults.class.getName( ),
				"QueryResults",
				"QueryResults starts up" );
	}
	
	/**
	 * Returns the PreparedQuery that contains the execution plan
	 * for producing this.
	 * A convenience method for the API consumer.
	 * @return	The PreparedQuery object used to produce this object. 
	 */
	public IPreparedQuery getPreparedQuery()
	{ 
		return reportQuery;
	}
	
	/**
	 * Returns the current result's iterator.  
	 * Repeated call of this method without having advanced to the next
	 * result would return the same iterator at its current state.
	 * @return	The current result's iterator.
	 * @throws 	DataException if error occurs in Data Engine
	 */
	public IResultIterator getResultIterator()
			throws DataException
	{ 
		logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getResultIterator",
				"start" );
	    if ( iterator == null )
	    {
	    	queryExecutor.execute();
	    	iterator = new ResultIterator( context,
					this,
					this.query,
					queryExecutor.odiResult,
					queryExecutor.scope );
	    }
		logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"getResultIterator",
				"finished" );
		return iterator;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws DataException
	{
		try
		{
			return queryExecutor.getResultMetaData();
		}
		catch ( DataException e )
		{
			throw e;
		}
		finally{
			logger.logp( Level.FINE,
					QueryResults.class.getName( ),
					"getResultMetaData",
					"return the result metadata" );			
		}
	}
	
	/**
	 * Closes all query result set(s) associated with this object;  
	 * provides a hint to the query that it can safely release
	 * all associated resources. 
	 * The query results might have iterators open on them. 
	 * Iterators associated with the query result sets are invalidated
	 * and can no longer be used.
	 * @throws BirtException 
	 */
	public void close() throws BirtException
	{
		if ( queryExecutor == null )
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
	        iterator.close();
	        iterator = null;
	    }
	    
	    reportQuery = null;
	    query = null;

	    queryExecutor.close();
	    queryExecutor = null;
	    logger.logp( Level.FINE,
				QueryResults.class.getName( ),
				"close",
				"QueryResults is closed" );
	}
	
	/**
	 * If current query results is associated with a sub query, its result
	 * iterator needs to know which group level this sub query belongs to.
	 * 
	 * @return group level of sub query
	 */
	int getGroupLevel( )
	{
		if ( query instanceof PreparedSubquery )
		{
			PreparedSubquery subQuery = (PreparedSubquery) query;
			return subQuery.getGroupLevel( );
		}
		else
		{
			return 0;
		}
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
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		if ( queryResultID == null )
			queryResultID = IDUtil.nextQursID( );
		
		return queryResultID;
	}

}

