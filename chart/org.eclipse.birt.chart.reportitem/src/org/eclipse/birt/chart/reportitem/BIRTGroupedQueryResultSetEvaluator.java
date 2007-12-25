/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;

/**
 * The implementation of <code>IGroupedDataResultSet</code> for chart.
 * 
 */
public class BIRTGroupedQueryResultSetEvaluator
		implements
			IGroupedDataRowExpressionEvaluator
{

	private static ILogger fLogger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private IQueryResultSet fQueryResultSet;

	private IResultIterator fResultIterator;

	private List fGroupDefinitions;

	private boolean fIsGrouped = false;

	private int fGroupCount;

	private List[] faGroupBreaks;

	private int fCountOfAvaiableRows = 0;

	private boolean fHasAggregation = false;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
     * @param hasAggregation
	 */
	public BIRTGroupedQueryResultSetEvaluator( IQueryResultSet resultSet, boolean hasAggregation )
	{
		fHasAggregation  = hasAggregation;
		fQueryResultSet = resultSet;
		fResultIterator = resultSet.getResultIterator( );
		fGroupDefinitions = fResultIterator.getQueryResults( )
				.getPreparedQuery( )
				.getReportQueryDefn( )
				.getGroups( );
		if ( fGroupDefinitions != null && fGroupDefinitions.size( ) > 0 )
		{
			fIsGrouped = true;
			fGroupCount = fGroupDefinitions.size( );
			faGroupBreaks = new List[fGroupDefinitions.size( )];
			for ( int i = 0; i < faGroupBreaks.length; i++ )
			{
				faGroupBreaks[i] = new ArrayList( );
			}
		}
	}
	
	/**
	 * Get list of group breaks, the group level is base on 0th index, 0 index
	 * means outermost group.
	 * 
	 * @param groupLevel
	 * @return
	 */
	private List getGroupBreaksList( int groupLevel )
	{
		return faGroupBreaks[groupLevel];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IGroupedDataResultSet#getGroupBreaks(int)
	 */
	public int[] getGroupBreaks( int groupLevel )
	{
		Object[] breaksArray = getGroupBreaksList( groupLevel ).toArray( );
		int[] breaks = new int[breaksArray.length];
		for ( int i = 0; i < breaksArray.length; i++ )
		{
			breaks[i] = ( (Integer) breaksArray[i] ).intValue( );
		}
		return breaks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		fQueryResultSet.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
	 */
	public Object evaluate( String expression )
	{
		try
		{
			return fQueryResultSet.evaluate( expression );
		}
		catch ( BirtException e )
		{
			fLogger.log( e );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(java.lang.String)
	 */
	public Object evaluateGlobal( String expression )
	{
		return evaluate( expression );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first( )
	{
		try
		{
			fCountOfAvaiableRows = 0;

			if ( !fIsGrouped )
			{
				if ( fResultIterator.next( ) )
				{
					return true;
				}
			}
			else
			{
				if ( findFirst( ) )
				{
					return true;
				}
			}
		}
		catch ( BirtException e )
		{
			fLogger.log( e );
		}
		return false;
	}

	/**
	 * Find the first row position.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private boolean findFirst( ) throws BirtException
	{
		if ( !fResultIterator.next( ) )
		{
			return false;
		}

		int groupLevel = fResultIterator.getStartingGroupLevel( );
		if ( groupLevel == 0 ) // It means the start of current row data.
		{
			return true;
		}
		else
		{
			return findFirst( );
		}
	}

	/**
	 * Find next available row position. If it has grouped-enabled, should
	 * ignore non-grouped/non-aggregation row.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private boolean findNext( ) throws BirtException
	{
		while ( fResultIterator.next( ) )
		{
			int startIndex = fResultIterator.getStartingGroupLevel( );
			if ( startIndex > 0 && startIndex <= fGroupCount )
			{
				fCountOfAvaiableRows++;
				// Add break point to current grouping.
				getGroupBreaksList( startIndex - 1 ).add( new Integer( fCountOfAvaiableRows ) );
				// Also the sub-groupings of current grouping should be added
				// the break point.
				for ( int i = startIndex; i < fGroupCount; i++ )
				{
					getGroupBreaksList( i ).add( new Integer( fCountOfAvaiableRows ) );
				}
				
				return true;
			}
			
			if ( !fHasAggregation )
			{
				fCountOfAvaiableRows++;
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next( )
	{
		try
		{
			if ( !fIsGrouped )
			{
				if ( fResultIterator.next( ) )
				{
					fCountOfAvaiableRows++;
					return true;
				}
			}
			else
			{
				return findNext( );
			}
		}
		catch ( BirtException e )
		{
			fLogger.log( e );
		}
		return false;
	}
}
