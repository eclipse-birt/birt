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

import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
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
	protected PreparedDataSourceQuery 	reportQuery;
	protected PreparedQuery				query;
	protected ResultIterator			iterator;
	protected PreparedQuery.Executor	queryExecutor;
	
	/**
	 * @param reportQuery The associated report query.
	 * @param query The actual query (either report query or subquery)
	 * @param odiResult associated Odi result iterator.
	 * @param aggrResult associated aggregate results
	 * @param scope scope used for this result set
	 */
	
	QueryResults( PreparedDataSourceQuery reportQuery,
			PreparedQuery query,
			PreparedQuery.Executor executor )
	{
		assert executor != null;
	    assert query != null;
	    assert reportQuery != null;
	    assert executor.odiResult != null;
	    assert executor.scope != null;
	    assert executor.aggregates != null;
	    
	    this.reportQuery = reportQuery;
	    this.query = query;
	    this.queryExecutor = executor;
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
	    if ( iterator == null )
	    {
			iterator = new ResultIterator( this, queryExecutor.odiResult, queryExecutor.scope);
	    }
		return iterator;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws DataException
	{
		try
		{
			return new ResultMetaData( queryExecutor.odiResult.getResultClass() );
		}
		catch ( DataException e )
		{
			throw e;
		}
	}
	
	/**
	 * Closes all query result set(s) associated with this object;  
	 * provides a hint to the query that it can safely release
	 * all associated resources. 
	 * The query results might have iterators open on them. 
	 * Iterators associated with the query result sets are invalidated
	 * and can no longer be used.
	 */
	public void close()
	{
		if ( queryExecutor == null )
			// already closed
			return;
		
	    if ( iterator != null )
	    {
	        iterator.close();
	        iterator = null;
	    }
	    
	    reportQuery = null;
	    query = null;

	    queryExecutor.close();
	    queryExecutor = null;
	}
	
	AggrCalc getAggrResult()
	{
		return queryExecutor.aggregates;
	}
	
	/**
	 * Gets the query that was prepared and executed to produce this result set.
	 */
	PreparedQuery getQuery()
	{
		return this.query;
	}
	
	org.eclipse.birt.data.engine.odi.IResultIterator getOdiResult()
	{
		return queryExecutor.odiResult;
	}
	
	Scriptable getScope()
	{
		return queryExecutor.scope;
	}
}

