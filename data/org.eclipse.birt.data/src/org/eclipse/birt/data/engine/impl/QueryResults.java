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
	protected PreparedReportQuery 		reportQuery;
	protected PreparedQuery				query;
	protected ResultIterator			iterator;
	protected org.eclipse.birt.data.engine.odi.IResultIterator			
										odiResult;
	protected Scriptable				scope;
	protected AggrCalc					aggrResult;
	
	/**
	 * @param reportQuery The associated report query.
	 * @param query The actual query (either report query or subquery)
	 * @param odiResult associated Odi result iterator.
	 * @param aggrResult associated aggregate results
	 * @param scope scope used for this result set
	 */
	
	QueryResults( PreparedReportQuery reportQuery, 
			PreparedQuery query,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult, 
			AggrCalc aggrResult,
			Scriptable scope )
	{
	    assert query != null;
	    assert reportQuery != null;
	    assert odiResult != null;
	    assert scope != null;
	    assert aggrResult != null;
	    
	    this.reportQuery = reportQuery;
	    this.query = query;
	    this.odiResult = odiResult;
	    this.scope = scope;
	    this.aggrResult = aggrResult;
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
			iterator = new ResultIterator( this, odiResult, scope);
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
			return new ResultMetaData( odiResult.getResultClass() );
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
	    if ( iterator != null )
	    {
	        iterator.close();
	        iterator = null;
	    }
	    reportQuery = null;
	    query = null;
	    odiResult = null;
	    scope = null;
	    aggrResult = null;
	}
	
	AggrCalc getAggrResult()
	{
		return this.aggrResult;
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
		return this.odiResult;
	}
	
	Scriptable getScope()
	{
		return this.scope;
	}
}

