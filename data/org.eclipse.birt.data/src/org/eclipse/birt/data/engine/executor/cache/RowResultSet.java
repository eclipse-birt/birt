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
package org.eclipse.birt.data.engine.executor.cache;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * This class simulates the mechanism of java.sql.ResultSet. Such an approach is
 * a passive model, which will give caller more flexibility for upper level
 * control. This feature is showed in DiskMergeSort.
 */
public class RowResultSet implements IRowResultSet
{
	//
	private List eventList;
	
	// basic data provider
	private OdiAdapter odiAdpater;
	
	// result meta data
	private IResultClass resultClass;

	// max rows will be fecthed
	private int maxRows;
	
	// current row index
	private int currIndex;
	
	// distinct value flag
	private boolean distinctValueFlag;
	
	// result object
	private IResultObject lastResultObject;
	
	private IResultObject nextResultObject;

	/**
	 * Construction
	 * 
	 * @param query
	 * @param odaResultSet
	 * @param resultClass
	 */
	public RowResultSet( SmartCacheRequest smartCacheRequest )
	{
		this.eventList = smartCacheRequest.getEventList( );
		this.odiAdpater = smartCacheRequest.getOdiAdapter( );
		this.resultClass = smartCacheRequest.getResultClass( );

		this.maxRows = smartCacheRequest.getMaxRow( );
		if ( maxRows <= 0 )
			maxRows = Integer.MAX_VALUE;
		
		this.distinctValueFlag = smartCacheRequest.getDistinctValueFlag( );		
	}

	/**
	 * @return result meta data
	 */
	public IResultClass getMetaData( )
	{
		return resultClass;
	}
	
	/**
	 * Notice the return value of this function is IResultObject. The null value
	 * indicates the cursor exceeds the end of result set.
	 * 
	 * @param stopSign
	 * @return next result data
	 * @throws DataException
	 */
	public IResultObject next( ) throws DataException
	{
		if ( this.nextResultObject != null )
		{
			this.lastResultObject = this.nextResultObject;
			this.nextResultObject = null;
			return this.lastResultObject;
		}
		
		if ( currIndex >= maxRows )
			return null;

		IResultObject odaObject = null;
		while ( true )
		{
			odaObject = odiAdpater.fetch( );
			if ( odaObject == null )
			{
				break;
			}
			else if ( processFetchEvent( odaObject, currIndex ) == true )
			{
				if ( this.distinctValueFlag == true
						&& isDuplicatedObject( odaObject ) )
					continue;
				
				currIndex++;
				break;
			}
		}

		return odaObject;
	}

	/**
	 * Process onFetchEvent in such a time window that closely after data gotten
	 * from data source and closely before data will be done grouping and
	 * sorting
	 * 
	 * @param resultObject
	 *            row object
	 * @return boolean indicate whether passed resultObject is accepted or
	 *         refused
	 * @throws DataException
	 */
	private boolean processFetchEvent( IResultObject resultObject,
			int currentIndex ) throws DataException
	{
		assert resultObject != null;
		
		if ( eventList != null )
		{
			int size = eventList.size( );
			for ( int i = 0; i < size; i++ )
			{
				IResultObjectEvent onFetchEvent = (IResultObjectEvent) eventList.get( i );
				if ( onFetchEvent.process( resultObject, currentIndex ) == false )
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @param currRowObject
	 * @return
	 */
	private boolean isDuplicatedObject( IResultObject currRowObject )
	{
		if ( currRowObject.equals( lastResultObject ) )
			return true;
		
		lastResultObject = currRowObject;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowResultSet#getIndex()
	 */
	public int getIndex() throws DataException 
	{
		if( this.nextResultObject != null )
			return this.currIndex - 2;
		return this.currIndex-1;
	}

	public boolean hasNext() throws DataException
	{
		if ( nextResultObject != null )
			return true;
		nextResultObject = this.next( );
		if( nextResultObject != null )
			return true;
		else
			return false;
	}
}