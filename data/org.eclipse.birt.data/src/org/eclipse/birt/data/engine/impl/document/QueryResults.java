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
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This class be used in presentation to retrieve ResultIterator. It will have
 * the same ID as its generation QueryResult.
 */
public class QueryResults implements IQueryResults
{	
	// context and ID info
	private DataEngineContext context;
	private String queryResultID;
	
	// result data
	private IResultIterator resultIterator;
	private IResultMetaData resultMetaData;
	
	// sub query info
	private String subQueryName;
	// if this is sub query, it needs to know its parent index and then
	// it can determins the sub query index in its group level.
	private int currParentIndex;
	
	private int[] parentGroupInfo;
	private int[] parentValidRowIDs;
	
	/**
	 * @param context
	 * @param queryResultID
	 */
	public QueryResults( DataEngineContext context, String queryResultID )
	{
		this( context, queryResultID, null, null, -1 );
	}
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param resultMetaData
	 * @param subQueryName
	 * @param currParentIndex
	 * @param groupInfo
	 * @param validIndex
	 */
	public QueryResults( DataEngineContext context, String queryResultID,
			IResultMetaData resultMetaData, String subQueryName,
			int currParentIndex )
	{
		this( context, queryResultID,
				resultMetaData, subQueryName,
				currParentIndex, null, null );
	}
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param resultMetaData
	 * @param subQueryName
	 * @param currParentIndex
	 */
	public QueryResults( DataEngineContext context, String queryResultID,
			IResultMetaData resultMetaData, String subQueryName,
			int currParentIndex , int[] groupInfo, int[] validIndex)
	{
		assert context != null;
		assert queryResultID != null;
		if ( subQueryName != null )
			assert resultMetaData != null;
		
		this.context = context;
		this.queryResultID = queryResultID;
		
		this.resultMetaData = resultMetaData;
		this.subQueryName = subQueryName;
		this.currParentIndex = currParentIndex;
		
		this.parentGroupInfo = groupInfo;
		this.parentValidRowIDs = validIndex;
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
			RDLoad rdLoad = RDUtil.newLoad( context,
					new QueryResultInfo( queryResultID,
							subQueryName,
							currParentIndex ) );
			this.resultMetaData = rdLoad.loadResultMetaData( );
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
			if ( subQueryName == null )
			{
				resultIterator = new ResultIterator( context,
						this,
						queryResultID );
			}
			else
			{
				int[] validIndex = populateValidRowIndex( );
				resultIterator = new ResultIterator( context,
						this,
						queryResultID,
						subQueryName,
						currParentIndex,
						validIndex);
			}
		}

		return resultIterator;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	private int[] populateValidRowIndex( ) throws DataException
	{
		int[] validIndex = null;
		if (this.parentValidRowIDs != null
				&& this.parentGroupInfo != null)
		{
			int start = 0;
			int end = 0;

			int rowIndex = 0;
			for (int i = 0; i < this.parentValidRowIDs.length; i++) {
				if (this.parentValidRowIDs[i] == this.currParentIndex) {
					rowIndex = i;
					break;
				}
			}

			for (int i = 0; i < this.parentGroupInfo.length - 1; i = i + 2) {
				if (rowIndex >= this.parentGroupInfo[i]
						&& rowIndex < this.parentGroupInfo[i + 1]) {
					start = this.parentGroupInfo[i];
					end = this.parentGroupInfo[i + 1];
					break;
				}
			}

			validIndex = new int[end - start];
			for (int i = 0; i < validIndex.length; i++) {
				validIndex[i] = this.parentValidRowIDs[i + start];
			}
			
			RDGroupUtil old = new RDGroupUtil(context.getInputStream(
					queryResultID, null,
					DataEngineContext.GROUP_INFO_STREAM));
			int[] oldGroupInfo = old.getGroupStartAndEndIndex(1);
			int dec = 0;
			for (int i = 0; i < oldGroupInfo.length - 1; i = i + 2) 
			{
				if (validIndex[0] >= oldGroupInfo[i]
						&& (validIndex[0] < oldGroupInfo[i + 1]|| oldGroupInfo[i+1]<0)) 
				{
					dec = oldGroupInfo[i];
					break;
				}
			}
			for( int i = 0; i < validIndex.length; i++ )
			{
				validIndex[i] = validIndex[i] - dec;
			}
		}
		return validIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		resultIterator.close( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		return this.queryResultID;
	}

}

