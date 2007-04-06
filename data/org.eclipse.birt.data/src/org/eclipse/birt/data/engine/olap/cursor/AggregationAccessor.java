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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Date;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.CalculatedMember;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;
import org.eclipse.birt.data.engine.olap.query.view.RelationShip;

/**
 * This class is to access all aggregation value's according to its resultset ID
 * and its index. Aggregation with same aggrOn level list will be assigned with
 * same resultset ID during preparation.
 * 
 * Firstly, we will do match on its member level. It's better to define one
 * aggragation in sequence of that in consideration of efficiency.It will match
 * values with its associated edge. If they are matched, return accessor's
 * current value, or move down/up to do the match again based on the logic of
 * sort dircetion on this level.
 * 
 * If there is no match find in cube cursor, 'null' value will be returned.
 */
public class AggregationAccessor implements Accessor
{
	private BirtCubeView view;
	private IResultSet resultSet;
	private Map relationMap;
	private MeasureNameManager manager;
	private int[] currentPosition;
	
	/**
	 * 
	 * @param view
	 * @param result
	 * @param relationMap
	 * @param manager
	 */
	public AggregationAccessor( BirtCubeView view, IResultSet result,
			Map relationMap, MeasureNameManager manager )
	{
		this.resultSet = result;
		this.view = view;
		this.relationMap = relationMap;
		this.manager = manager;

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

	/**
	 * 
	 * @param aggrIndex
	 * @throws OLAPException
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean populateRelation( int aggrIndex, String aggrName )
			throws OLAPException, IOException, DataException
	{
		IAggregationResultSet rs = this.resultSet.getMeasureResult( )[aggrIndex].getQueryResultSet( );
	
		if ( rs == null || rs.length( )<=0 )
			return false;
				
		EdgeCursor rowEdgeCursor = null, columnEdgeCursor = null;
		List columnDimList = null, rowDimList = null;
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

		CalculatedMember member = manager.getCalculatedMember( aggrName );
		List memberList = member.getAggrOnList( );

		RelationShip relation = (RelationShip) this.relationMap.get( aggrName );
		List columnLevelList = relation.getLevelListOnColumn( );
		List rowLevelList = relation.getLevelListOnRow( );

		Map valueMap = new HashMap( );

		for ( int i = 0; i < columnLevelList.size( ); i++ )
		{
			String levelName = columnLevelList.get( i ).toString( );
			DimensionCursor cursor = (DimensionCursor) columnDimList.get( i );
			Object value = cursor.getObject( levelName );
			valueMap.put( levelName, value );
		}
		for ( int i = 0; i < rowLevelList.size( ); i++ )
		{
			String levelName = rowLevelList.get( i ).toString( );
			DimensionCursor cursor = (DimensionCursor) rowDimList.get( i );
			Object value = cursor.getObject( levelName );
			valueMap.put( levelName, value );
		}

		if ( columnLevelList.isEmpty( ) && rowLevelList.isEmpty( ) )
			return true;

		return findValueMatcher( rs, memberList, valueMap, aggrIndex );
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
		for ( ; start < levelList.size( ); )
		{
			String levelName = levelList.get( start ).toString( );

			Object value1 = valueMap.get( levelName );
			Object value2 = rs.getLevelKeyValue( rs.getLevelIndex( levelName ) )[0];
			int sortType = rs.getSortType( rs.getLevelIndex( levelName ) ) == IDimensionSortDefn.SORT_DESC
					? -1 : 1;
			int direction = sortType
					* ( (Comparable) value1 ).compareTo( value2 ) < 0 ? -1
					: ( (Comparable) value1 ).compareTo( value2 ) == 0 ? 0 : 1;
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
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#close()
	 */
	public void close( )
	{
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBlob(int)
	 */
	public Blob getBlob( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBlob(java.lang.String)
	 */
	public Blob getBlob( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBoolean(int)
	 */
	public boolean getBoolean( int arg0 ) throws OLAPException
	{
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getBoolean(java.lang.String)
	 */
	public boolean getBoolean( String arg0 ) throws OLAPException
	{
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDate(int)
	 */
	public Date getDate( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDate(java.lang.String)
	 */
	public Date getDate( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDate(int, java.util.Calendar)
	 */
	public Date getDate( int arg0, Calendar arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate( String arg0, Calendar arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDouble(int)
	 */
	public double getDouble( int arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDouble(java.lang.String)
	 */
	public double getDouble( String arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getFloat(int)
	 */
	public float getFloat( int arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getFloat(java.lang.String)
	 */
	public float getFloat( String arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getInt(int)
	 */
	public int getInt( int arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getInt(java.lang.String)
	 */
	public int getInt( String arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getLong(int)
	 */
	public long getLong( int arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getLong(java.lang.String)
	 */
	public long getLong( String arg0 ) throws OLAPException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getMetaData()
	 */
	public RowDataMetaData getMetaData( ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int)
	 */
	public Object getObject( int arg0 ) throws OLAPException
	{
		try
		{
			String aggrName = this.manager.getAggrName( arg0 );
			int index = this.manager.getAggregationIndex( aggrName );
			int id = this.manager.getAggregationResultID( aggrName );

			if ( populateRelation( index, aggrName ) )
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
		try
		{
			int id = this.manager.getAggregationResultID( arg0 );
			int index = this.manager.getAggregationIndex( arg0 );
			if ( populateRelation( id, arg0 ) )
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
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int, java.util.Map)
	 */
	public Object getObject( int arg0, Map arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject( String arg0, Map arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getString(int)
	 */
	public String getString( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getString(java.lang.String)
	 */
	public String getString( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(int)
	 */
	public Time getTime( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(java.lang.String)
	 */
	public Time getTime( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(int, java.util.Calendar)
	 */
	public Time getTime( int arg0, Calendar arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime( String arg0, Calendar arg1 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(int)
	 */
	public Timestamp getTimestamp( int arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp( String arg0 ) throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp( int arg0, Calendar arg1 )
			throws OLAPException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp( String arg0, Calendar arg1 )
			throws OLAPException
	{
		return null;
	}

}
