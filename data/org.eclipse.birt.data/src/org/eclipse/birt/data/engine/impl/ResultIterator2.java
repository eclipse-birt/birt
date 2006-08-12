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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;

/**
 * When useDetails==false, this class is used.
 */
class ResultIterator2 extends ResultIterator
{
	// the value of lower group level
	private int lowestGroupLevel;
	
	private int currRowIndex;
	
	private int cachedStartingGroupLevel;
	
	private int cachedRowId;
		
	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @param useDetails
	 * @param lowestGroupLevel
	 * @throws DataException
	 */
	ResultIterator2( IServiceForResultSet rService,
			org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
		super( rService, odiResult, scope );

		this.lowestGroupLevel = rService.getQueryDefn( ).getGroups( ).size( );
		this.currRowIndex = -1;
		this.cachedStartingGroupLevel = 0;
		this.cachedRowId = 0;

	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#next()
	 */
	public boolean next( ) throws BirtException
	{
		boolean hasNext = super.next( );
		if ( hasNext )
			currRowIndex++;
		return hasNext;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#hasNextRow()
	 */
	protected boolean hasNextRow( ) throws DataException
	{
		// make sure that the first row of group is used, instead of the last
		// row
		this.odiResult.last( lowestGroupLevel );
		
		boolean result = odiResult.next( );

		if ( result )
		{
			cachedStartingGroupLevel = odiResult.getStartingGroupLevel( );
			
			if ( rowIDUtil == null )
				rowIDUtil = new RowIDUtil( );
			
			if ( this.rowIDUtil.getMode( this.odiResult ) == RowIDUtil.MODE_NORMAL )
				cachedRowId = this.odiResult.getCurrentResultIndex( );
			else
			{
				IResultObject ob = this.odiResult.getCurrentResult( );
				if ( ob == null )
					cachedRowId = -1;
				else
					cachedRowId = ( (Integer) ob.getFieldValue( rowIDUtil.getRowIdPos( ) ) ).intValue( );
			}
		}
		
		return result;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
	 */
	public int getRowId( ) throws BirtException
	{
		return this.cachedRowId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		return cachedStartingGroupLevel;		
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		// make sure that the ending group level value is also correct
		this.odiResult.last( this.lowestGroupLevel );
		
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
