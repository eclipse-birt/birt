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
package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * This class be used in presentation to retrieve ResultIterator. It will have
 * the same ID as its generation QueryResult.
 */
public class QueryResults implements IQueryResults
{	
	// context and ID info
	private DataEngineContext context;
	private String queryResultID, baseQueryResultID;
	
	// result data
	private IResultIterator resultIterator;
	private IResultMetaData resultMetaData;
	
	// sub query info
	private String subQueryName;
	// if this is sub query, it needs to know its parent index and then
	// it can determins the sub query index in its group level.
	private int currParentIndex;
	
	/**
	 * @param context
	 * @param queryResultID
	 */
	public QueryResults( DataEngineContext context, String queryResultID )
	{
		this( context, null, queryResultID, null, null, -1 );
	}
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param resultMetaData
	 * @param subQueryName
	 * @param currParentIndex
	 */
	QueryResults( DataEngineContext context, String baseResultID, String queryResultID,
			IResultMetaData resultMetaData, String subQueryName,
			int currParentIndex )
	{
		assert context != null;
		assert queryResultID != null;
		if ( subQueryName != null )
			assert resultMetaData != null;
		
		this.context = context;
		this.queryResultID = queryResultID;
		this.baseQueryResultID = baseResultID;
		
		this.resultMetaData = resultMetaData;
		this.subQueryName = subQueryName;
		this.currParentIndex = currParentIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery( )
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws BirtException
	{		
		if ( resultMetaData == null )
		{
			this.resultMetaData = getRDLoad( subQueryName, queryResultID ).loadResultMetaData( );
		}

		return resultMetaData;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
	 */
	public IResultIterator getResultIterator( ) throws BirtException
	{
		if ( resultIterator == null )
		{
			if ( subQueryName == null ) // not a sub query
			{
				IBaseQueryDefinition queryDefn = this.getRDLoad( null, queryResultID )
						.loadQueryDefn( StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE );
				
				if ( queryDefn.usesDetails( ) == true )
					resultIterator = new ResultIterator( context,
							this,
							queryResultID );
				else
					resultIterator = new ResultIterator2( context,
							this,
							queryResultID,
							queryDefn.getGroups( ).size( ) );
			}
			else
			{
				ISubqueryDefinition subQuery = this.getRDLoad( null,
						this.baseQueryResultID )
						.loadSubQueryDefn( StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE,
								subQueryName );
				if ( subQuery.usesDetails( ) == true )
					resultIterator = new ResultIterator( context,
							this,
							queryResultID,
							subQueryName,
							currParentIndex );
				else
					resultIterator = new ResultIterator2( context,
							this,
							queryResultID,
							subQueryName,
							currParentIndex,
							subQuery.getGroups( ).size( ) );

			}
		}

		return resultIterator;
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	private RDLoad getRDLoad( String subQueryName, String queryResultID ) throws DataException
	{
		String baseID = QueryResultIDUtil.get1PartID( queryResultID );
		if ( baseID == null )
			baseID = queryResultID;
		RDLoad rdLoad = RDUtil.newLoad( context, new QueryResultInfo( baseID,
				subQueryName,
				currParentIndex ) );
		return rdLoad;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		if( resultIterator!= null )
			resultIterator.close( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		return this.queryResultID;
	}

	public void cancel( )
	{
		// TODO Auto-generated method stub
		
	}

}
