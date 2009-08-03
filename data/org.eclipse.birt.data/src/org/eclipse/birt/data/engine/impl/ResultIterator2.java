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

import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
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
	
	private int cachedRowId;

	private boolean isSummary;
	private SummaryGroupLevelCalculator groupLevelCalculator;
	private static Logger logger = Logger.getLogger( ResultIterator2.class.getName( ) );

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
			Scriptable scope, int rawIdStartingValue ) throws DataException
	{
		super( rService, odiResult, scope, rawIdStartingValue );
		Object[] params = {
				rService, odiResult, scope
		};
		logger.entering( ResultIterator2.class.getName( ),
				"ResultIterator2",
				params );

		this.lowestGroupLevel = rService.getQueryDefn( ).getGroups( ).size( );
		this.currRowIndex = -1;
		this.cachedRowId = 0;
		this.isSummary = ( rService.getQueryDefn( ) instanceof IQueryDefinition )
				? ( (IQueryDefinition) rService.getQueryDefn( ) ).isSummaryQuery( )
				: false;
		if( this.isSummary )
		{
			if ( lowestGroupLevel == 0 )
				this.groupLevelCalculator = new SummaryGroupLevelCalculator( null );
			else
			{
				int[][] groupIndex = new int[lowestGroupLevel + 1][];
				for ( int i = 0; i <= lowestGroupLevel; i++ )
				{
					groupIndex[i] = this.odiResult.getGroupStartAndEndIndex( i );
				}

				this.groupLevelCalculator = new SummaryGroupLevelCalculator( groupIndex );
			}
		}
		logger.exiting( ResultIterator2.class.getName( ), "ResultIterator2" );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#next()
	 */
	public boolean next( ) throws BirtException
	{
		boolean hasNext = super.next( );
		if ( hasNext )
			currRowIndex++;
		else if ( currRowIndex == -1)
		{
			//If empty result set, the cachedRowId should be -1.
			this.cachedRowId = -1;
		}
		return hasNext;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#hasNextRow()
	 */
	protected boolean hasNextRow( ) throws DataException
	{
		boolean result = false;
		
	
		int index = this.odiResult.getCurrentResultIndex( );
		this.odiResult.last( lowestGroupLevel );

		if ( this.isSummary )
		{
			result = this.odiResult.next( );
		}
		else
		{
			boolean shouldMoveForward = false;
			if ( index != this.odiResult.getCurrentResultIndex( ) )
			{
				result = odiResult.getCurrentResult( ) == null ? false : true;
				shouldMoveForward = false;
			}
			else
			{
				shouldMoveForward = true;
			}

			if ( shouldMoveForward )
			{
				result = this.odiResult.next( );
			}
		}
		if ( result )
		{
			// cachedStartingGroupLevel = odiResult.getStartingGroupLevel( );

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
	 
	public int getStartingGroupLevel( ) throws DataException
	{
		return this.odiResult.getStartingGroupLevel( );		
	}
	*/
	
	
	 
	public int getEndingGroupLevel( ) throws DataException
	{
		// make sure that the ending group level value is also correct
		if( this.isSummary )
		{
			return this.groupLevelCalculator.getEndingGroupLevel( this.odiResult.getCurrentResultIndex( ) );
		}
		
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
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#goThroughGapRows(int)
	 */
	protected void goThroughGapRows( int groupLevel ) throws DataException,
			BirtException
	{
		odiResult.last( groupLevel );
	}
}
