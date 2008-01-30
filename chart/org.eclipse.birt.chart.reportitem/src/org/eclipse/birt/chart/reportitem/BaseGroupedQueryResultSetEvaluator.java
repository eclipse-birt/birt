/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;


/**
 * The class defines basic functions and sub-class must override evaluate method.
 * @since BIRT 2.3
 */
public class BaseGroupedQueryResultSetEvaluator implements
		IGroupedDataRowExpressionEvaluator
{
	protected static ILogger fLogger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	protected IResultIterator fResultIterator;

	protected List fGroupDefinitions;

	protected boolean fIsGrouped = false;

	protected int fGroupCount;

	protected List[] faGroupBreaks;

	protected int fCountOfAvaiableRows = 0;

	protected boolean fHasAggregation = false;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 */
	public BaseGroupedQueryResultSetEvaluator( IResultIterator resultIterator,
			boolean hasAggregation )
	{
		fHasAggregation = hasAggregation;

		fResultIterator = resultIterator;
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
	 * Constructor.
	 * 
	 * @param resultIterator
	 * @param hasAggregation
	 * @param isSubQuery
	 * @since 2.3
	 */
	public BaseGroupedQueryResultSetEvaluator(
			IResultIterator resultIterator, boolean hasAggregation,
			boolean isSubQuery )
	{
		fHasAggregation = hasAggregation;

		fResultIterator = resultIterator;
		if ( isSubQuery )
		{
			List subQuerys = new ArrayList( );
			// Get all sub queries defined on detail item.
			Collection c = fResultIterator.getQueryResults( )
					.getPreparedQuery( )
					.getReportQueryDefn( )
					.getSubqueries( );
			if ( c != null )
			{
				subQuerys.addAll( c );
			}
			
			// Get all sub queries defined on group item.
			List groups = fResultIterator.getQueryResults( )
					.getPreparedQuery( )
					.getReportQueryDefn( )
					.getGroups( );
			if ( groups != null )
			{
				for ( Iterator iter = groups.iterator( ); iter.hasNext( ); )
				{
					c = ( (IGroupDefinition) iter.next( ) ).getSubqueries( );
					if ( c != null )
					{
						subQuerys.addAll( c );
					}
				}
			}
			
			// Iterator all sub queries and find chart sub query to get group defintions.
			int i = 0;
			for ( ; i < subQuerys.size( ); i++ )
			{
				if ( ChartReportItemConstants.CHART_SUBQUERY.equals( ( (ISubqueryDefinition) subQuerys.get( i ) ).getName( ) ) )
				{
					fGroupDefinitions = ( (ISubqueryDefinition) subQuerys.get( i ) ).getGroups( );
					break;
				}
			}
		}
		else
		{
			fGroupDefinitions = fResultIterator.getQueryResults( )
					.getPreparedQuery( )
					.getReportQueryDefn( )
					.getGroups( );
		}
		
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
	 * Get list of group breaks, the group level is base on 0th index, 0
	 * index means outermost group.
	 * 
	 * @param groupLevel
	 * @return
	 */
	protected List getGroupBreaksList( int groupLevel )
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
		try
		{
			fResultIterator.close( );
		}
		catch ( BirtException e )
		{
			fLogger.log( e );
		}
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
			// Here, the expression should be binding name.
			return fResultIterator.getValue( expression );
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
	protected boolean findFirst( ) throws BirtException
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
	protected boolean findNext( ) throws BirtException
	{
		while ( fResultIterator.next( ) )
		{
			int startIndex = fResultIterator.getStartingGroupLevel( );
			if ( startIndex > 0 && startIndex <= fGroupCount )
			{
				fCountOfAvaiableRows++;
				// Add break point to current grouping.
				getGroupBreaksList( startIndex - 1 ).add( new Integer( fCountOfAvaiableRows ) );
				// Also the sub-groupings of current grouping should be
				// added the break point.
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

