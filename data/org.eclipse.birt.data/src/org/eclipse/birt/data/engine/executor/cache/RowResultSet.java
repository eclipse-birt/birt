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
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * This class simulates the mechanism of java.sql.ResultSet. Such an approach is
 * a passive model, which will give caller more flexibility for upper level
 * control. This feature is showed in DiskMergeSort.
 */
class RowResultSet
{
	// query information provider
	private BaseQuery query;
	
	// basic data provider
	private OdiAdapter odiAdpater;
	
	// result meta data
	private IResultClass resultClass;

	// max rows will be fecthed
	private int maxRows;
	
	// current row index
	private int currIndex;

	/**
	 * Construction
	 * 
	 * @param query
	 * @param odaResultSet
	 * @param resultClass
	 */
	RowResultSet( BaseQuery query, OdiAdapter odiAdpater,
			IResultClass resultClass )
	{
		this.query = query;
		this.odiAdpater = odiAdpater;
		this.resultClass = resultClass;

		maxRows = query.getMaxRows( );
		if ( maxRows <= 0 )
			maxRows = Integer.MAX_VALUE;
	}

	/**
	 * Notice the return value of this function is IResultObject. The null value
	 * indicates the cursor exceeds the end of result set.
	 * 
	 * @return next result data
	 * @throws DataException
	 */
	IResultObject next( ) throws DataException
	{
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
		
		List eventList = query.getFetchEvents( );
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
	 * @return result meta data
	 */
	IResultClass getMetaData( )
	{
		return resultClass;
	}

}