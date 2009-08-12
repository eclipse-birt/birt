/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.CalculatedMember;
import org.eclipse.birt.data.engine.olap.query.view.Relationship;

/**
 * This class is to access all aggregation value's according to its result set
 * ID and its index. Aggregation with same aggrOn level list will be assigned
 * with same result set ID during preparation.
 * 
 * Firstly, we will do match on its member level. It's better to define one
 * aggregation in sequence of that in consideration of efficiency.It will match
 * values with its associated edge. If they are matched, return accessor's
 * current value, or move down/up to do the match again based on the logic of
 * sort direction on this level.
 * 
 * If there is no match find in cube cursor, 'null' value will be returned.
 */
public class AggregationAccessor extends Accessor
{
	private BirtCubeView view;
	private IResultSet resultSet;
	private Map relationMap;
	private int[] currentPosition;
	
	/**
	 * 
	 * @param view
	 * @param result
	 * @param relationMap
	 * @param manager
	 */
	public AggregationAccessor( BirtCubeView view, IResultSet result,
			Map relationMap )
	{
		this.resultSet = result;
		this.view = view;
		this.relationMap = relationMap;

		if ( result == null || result.getMeasureResult( ) == null )
			return;

		this.currentPosition = new int[this.resultSet.getMeasureResult( ).length];
		// initial aggregation resultset position to 0 if possible
		for ( int i = 0; i < this.resultSet.getMeasureResult( ).length; i++ )
		{
			try
			{
				if ( this.resultSet.getMeasureResult( )[i].getQueryResultSet( )
						.length( ) > 0 )
				{
					this.resultSet.getMeasureResult( )[i].getQueryResultSet( )
							.seek( 0 );
					currentPosition[i] = 0;
				}
				else
				{
					currentPosition[i] = -1;
				}
			}
			catch ( IOException e )
			{
				//do nothing
			}
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#close()
	 */
	public void close( ) throws OLAPException
	{
		if ( this.resultSet == null || this.resultSet.getMeasureResult( ) == null )
			return;
		List errorList = new ArrayList( );
		for ( int i = 0; i < this.resultSet.getMeasureResult( ).length; i++ )
		{
			try
			{
				this.resultSet.getMeasureResult( )[i].getQueryResultSet( )
						.close( );
			}
			catch ( IOException e )
			{
				errorList.add( e );
			}
		}
		if ( !errorList.isEmpty( ) )
		{
			throw new OLAPException( ( (IOException) errorList.get( 0 ) ).getLocalizedMessage( ) );
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int)
	 */
	public Object getObject( int arg0 ) throws OLAPException
	{
		if ( this.resultSet == null || this.resultSet.getMeasureResult( ) == null )
			return null;
		
		try
		{
			String aggrName = this.view.getAggregationRegisterTable( ).getAggrName( arg0 );
			int index = this.view.getAggregationRegisterTable( ).getAggregationIndex( aggrName );
			int id = this.view.getAggregationRegisterTable( ).getAggregationResultID( aggrName );

			if ( synchronizedWithEdge( index,
					aggrName,
					getCurrentValueOnEdge( aggrName ) ) )
				return this.resultSet.getMeasureResult( )[id].getQueryResultSet( )
						.getAggregationValue( index );
			else
			{
				return null;
			}
		}
		catch ( IOException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
		catch ( DataException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(java.lang.String)
	 */
	public Object getObject( String arg0 ) throws OLAPException
	{
		if ( this.resultSet == null || this.resultSet.getMeasureResult( ) == null )
			return null;
		
		try
		{
			int id = this.view.getAggregationRegisterTable( )
					.getAggregationResultID( arg0 );
			int index = this.view.getAggregationRegisterTable( )
					.getAggregationIndex( arg0 );
			if ( synchronizedWithEdge( id, arg0, getCurrentValueOnEdge( arg0 ) ) )
				return this.resultSet.getMeasureResult( )[id].getQueryResultSet( )
						.getAggregationValue( index );
			else
			{
				return null;
			}
		}
		catch ( IOException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
		catch ( DataException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
	}
	

	/**
	 * 
	 * @param aggrIndex
	 * @throws OLAPException
	 * @throws IOException
	 * @throws DataException
	 */
	public boolean synchronizedWithEdge( int aggrIndex, String aggrName, Map valueMap )
			throws OLAPException, IOException, DataException
	{
		IAggregationResultSet rs = this.resultSet.getMeasureResult( )[aggrIndex].getQueryResultSet( );
		if ( rs == null || rs.length( ) <= 0 )
			return false;

		if ( valueMap == null )
			return true;

		CalculatedMember member = this.view.getAggregationRegisterTable( ).getCalculatedMember( aggrName );
		List memberList = member.getCubeAggrDefn( ).getAggrLevelsInAggregationResult( );
		
		if ( Arrays.deepEquals( rs.getAllLevels( ), member.getCubeAggrDefn( ).getAggrLevelsInDefinition( ).toArray( ) ))
		{
			return findValueMatcher( rs, memberList, valueMap, aggrIndex );
		}
		else
		{
			//AggregationResultSet for running aggregation
			return findValueMatcherOneByOne( rs, memberList, valueMap, aggrIndex );
		}
	}
	
	private Map getCurrentValueOnEdge( String aggrName ) throws OLAPException
	{

		EdgeCursor rowEdgeCursor = null, columnEdgeCursor = null, pageEdgeCursor = null;
		List columnDimList = null, rowDimList = null, pageDimList = null;
		if ( this.view.getRowEdgeView( ) != null )
		{
			rowEdgeCursor = (EdgeCursor) ( (BirtEdgeView) this.view.getRowEdgeView( ) ).getEdgeCursor( );
			if ( rowEdgeCursor != null )
				rowDimList = rowEdgeCursor.getDimensionCursor( );
		}
		if ( this.view.getColumnEdgeView( ) != null )
		{
			columnEdgeCursor = (EdgeCursor) ( (BirtEdgeView) this.view.getColumnEdgeView( ) ).getEdgeCursor( );
			if ( columnEdgeCursor != null )
				columnDimList = columnEdgeCursor.getDimensionCursor( );
		}
		if ( this.view.getPageEdgeView( ) != null )
		{
			pageEdgeCursor = (EdgeCursor) ( (BirtEdgeView) this.view.getPageEdgeView( ) ).getEdgeCursor( );
			if ( pageEdgeCursor != null )
				pageDimList = pageEdgeCursor.getDimensionCursor( );
		}

		Relationship relation = (Relationship) this.relationMap.get( aggrName );
		List pageLevelList = relation.getLevelListOnPage( );
		List columnLevelList = relation.getLevelListOnColumn( );
		List rowLevelList = relation.getLevelListOnRow( );

		Map valueMap = new HashMap( );

		if ( columnLevelList.isEmpty( )
				&& rowLevelList.isEmpty( ) && pageLevelList.isEmpty( ) )
			return null;

		for ( int index = 0; index < pageLevelList.size( ); index++ )
		{
			DimLevel level = (DimLevel) pageLevelList.get( index );
			DimensionCursor cursor = (DimensionCursor) pageDimList.get( index );
			Object value = cursor.getObject( level.getLevelName( ) );
			valueMap.put( level, value );
		}

		for ( int i = 0; i < columnLevelList.size( ); i++ )
		{
			DimLevel level = (DimLevel) columnLevelList.get( i );
			DimensionCursor cursor = (DimensionCursor) columnDimList.get( i );
			Object value = cursor.getObject( level.getLevelName( ) );
			valueMap.put( level, value );
		}
		for ( int i = 0; i < rowLevelList.size( ); i++ )
		{
			DimLevel level = (DimLevel) rowLevelList.get( i );
			DimensionCursor cursor = (DimensionCursor) rowDimList.get( i );
			Object value = cursor.getObject( level.getLevelName( ) );
			valueMap.put( level, value );
		}
		return valueMap;
	}
	
	/**
	 * Find the value matcher in cube cursor. Based on sort direction and
	 * compared result, decide to move on/back along resultset.
	 * 
	 * @param rs
	 * @param levelList
	 * @param valueMap
	 * @param aggrIndex
	 * @return
	 * @throws IOException 
	 */
	private boolean findValueMatcherOneByOne( IAggregationResultSet rs, List levelList,
			Map valueMap, int aggrIndex ) throws IOException
	{
		int position = 0;
		if ( rs.length( ) <= 0 || levelList.isEmpty( ))
			return true;
		while ( position < rs.length( ) )
		{
			rs.seek( position );
			boolean match = true;
			for ( int i = 0; i < levelList.size( ); i++ )
			{
				DimLevel level = (DimLevel) levelList.get( i );
				Object value1 = valueMap.get( level );
				Object value2 = rs.getLevelKeyValue( rs.getLevelIndex( level ) )[rs.getLevelKeyColCount( rs.getLevelIndex( level ) ) - 1];;
				if ( !value1.equals( value2 ) )
				{
					match = false;
					break;
				}
			}
			if ( match )
			{
				return true;
			}
			else
			{
				++position;
			}
		}

		return false;
	}
	
	/**
	 * Find the value matcher in cube cursor. Based on sort direction and
	 * compared result, decide to move on/back along resultset.
	 * 
	 * @param rs
	 * @param levelList
	 * @param valueMap
	 * @param aggrIndex
	 * @return
	 */
	private boolean findValueMatcher( IAggregationResultSet rs, List levelList,
			Map valueMap, int aggrIndex )
	{
		if ( levelList.isEmpty( ) )
			return true;
		int start = 0, state = 0;
		boolean find = false;
		currentPosition[aggrIndex] = rs.getPosition( );
		
		for ( ; start < levelList.size( ); )
		{
			DimLevel level = (DimLevel) levelList.get( start );

			Object value1 = valueMap.get( level );
			Object value2 = rs.getLevelKeyValue( rs.getLevelIndex( level ) )[rs.getLevelKeyColCount( rs.getLevelIndex( level ) ) - 1];
			int sortType = rs.getSortType( rs.getLevelIndex( level ) ) == IDimensionSortDefn.SORT_DESC
					? -1 : 1;
			int direction = sortType
					* compare( value1, value2 ) < 0 ? -1
					: compare( value1, value2 ) == 0 ? 0 : 1;
			if ( direction < 0
					&& currentPosition[aggrIndex] > 0
					&& ( state == 0 || state == direction ) )
			{
				state = direction;
				try
				{
					rs.seek( --currentPosition[aggrIndex] );
				}
				catch ( IOException e )
				{
					find = false;
				}
				start = 0;
				continue;
			}
			else if ( direction > 0
					&& currentPosition[aggrIndex] < rs.length( )-1
					&& ( state == 0 || state == direction ) )
			{
				state = direction;
				try
				{
					rs.seek( ++currentPosition[aggrIndex] );
				}
				catch ( IOException e )
				{
					find = false;
				}
				start = 0;
				continue;
			}
			else if ( direction == 0 )
			{
				if ( start == levelList.size( ) - 1 )
				{
					find = true;
					break;
				}
				else
				{
					start++;
					continue;
				}
			}
			else if ( currentPosition[aggrIndex] < 0
					|| currentPosition[aggrIndex] >= rs.length( ) )
			{
				return false;
			}
			else
				return false;
		}
		return find;
	}
	
	private static int compare( Object value1, Object value2 )
	{
		if ( value1 == value2 )
		{
			return 0;
		}
		if ( value1 == null )
		{
			return -1;
		}
		if ( value2 == null )
		{
			return 1;
		}
		if ( value1 instanceof Comparable )
		{
			return ( (Comparable) value1 ).compareTo( value2 );
		}
		return value1.toString( ).compareTo( value2.toString( ) );
	}

}
