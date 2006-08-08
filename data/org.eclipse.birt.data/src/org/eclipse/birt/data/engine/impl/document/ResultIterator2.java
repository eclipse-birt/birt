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
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * When useDetails==false, this class is used.
 */
public class ResultIterator2 extends ResultIterator
{
	// the value of lower group level
	private int lowestGroupLevel;
	
	private int currRowIndex;
	
	private int cachedStartingGroupLevel;

	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @param lowestGroupLevel
	 * @throws DataException
	 */
	ResultIterator2( DataEngineContext context, IQueryResults queryResults,
			String queryResultID, int lowestGroupLevel ) throws DataException
	{
		super( context, queryResults, queryResultID );
		
		this.lowestGroupLevel = lowestGroupLevel;
		this.currRowIndex = -1;
		this.cachedStartingGroupLevel = 0;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.ResultIterator#next()
	 */
	public boolean next( ) throws DataException
	{
		if ( this.exprResultSet.getCurrentIndex( ) >= 0 ) // not the first row
			exprResultSet.skipToEnd( lowestGroupLevel );

		boolean hasNext = super.next( );
		if ( hasNext )
		{
			currRowIndex++;
			cachedStartingGroupLevel = exprResultSet.getStartingGroupLevel( );
		}

		return hasNext;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		return cachedStartingGroupLevel;		
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.document.ResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws BirtException
	{
		this.exprResultSet.skipToEnd( this.lowestGroupLevel );
		
		return super.getEndingGroupLevel( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
	 */
	public int getRowIndex( ) throws BirtException
	{
		return currRowIndex;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws BirtException
	{
		if ( rowIndex < 0 || rowIndex < this.currRowIndex )
			throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
					new Integer( rowIndex ) );
		else if ( rowIndex == currRowIndex )
			return;

		int gapRows = rowIndex - currRowIndex;
		for ( int i = 0; i < gapRows; i++ )
		{
			if ( this.next( ) == false )
				throw new DataException( ResourceConstants.INVALID_ROW_INDEX,
						new Integer( rowIndex ) );
		}
	}

}
