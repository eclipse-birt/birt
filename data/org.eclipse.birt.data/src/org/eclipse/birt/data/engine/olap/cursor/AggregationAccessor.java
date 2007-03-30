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
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;
import org.eclipse.birt.data.engine.olap.query.view.RelationShip;

/**
 * This class is to access all aggregation value's. It will match values with
 * its associated edge. If they are matched, return accessor's current value, or
 * move next to do the match again.
 * 
 */
public class AggregationAccessor implements Accessor
{
	private BirtCubeView view;
	private IResultSet resultSet;
	private Map relationMap;
	private MeasureNameManager manager;
	
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
		if ( result == null )
			return;
		this.resultSet = result;
		this.view = view;
		this.relationMap = relationMap;
		this.manager = manager;
	}

	/**
	 * 
	 * @param aggrIndex
	 * @throws OLAPException
	 * @throws IOException
	 */
	private void populateRelation( int aggrIndex, String aggrName ) throws OLAPException,
			IOException
	{
		DimensionAxis axis = this.resultSet.getMeasureResult( )[aggrIndex].getDimensionAxis( 0 );
		EdgeCursor rowEdgeCursor = null, columnEdgeCursor = null;
		if ( this.view.getRowEdgeView( ) != null )
			rowEdgeCursor = (EdgeCursor) ( (BirtEdgeView) this.view.getRowEdgeView( ) ).getEdgeCursor( );
		if ( this.view.getColumnEdgeView( ) != null )
			columnEdgeCursor = (EdgeCursor) ( (BirtEdgeView) this.view.getColumnEdgeView( ) ).getEdgeCursor( );

		BirtEdgeView edgeView = view.getMeasureEdgeView( )[aggrIndex];

		RelationShip relation = (RelationShip) this.relationMap.get( aggrName );

		List columnLevelList = relation.getLevelListOnColumn( );
		List rowLevelList = relation.getLevelListOnRow( );

		List columnDimList =null, rowDimList= null;
		if ( columnEdgeCursor != null )
			columnDimList = columnEdgeCursor.getDimensionCursor( );
		if ( rowEdgeCursor != null )
			rowDimList = rowEdgeCursor.getDimensionCursor( );

		boolean findColumn = false, findRow = false;
		Map columnValueMap = new HashMap( );
		Map rowValueMap = new HashMap( );
		for ( int i = 0; i < columnLevelList.size( ); i++ )
		{
			String levelName = columnLevelList.get( i ).toString( );

			DimensionCursor cursor = (DimensionCursor) columnDimList.get( i );
			Object value = cursor.getObject( levelName );
			columnValueMap.put( levelName, value );
		}
		for ( int i = 0; i < rowLevelList.size( ); i++ )
		{
			String levelName = rowLevelList.get( i ).toString( );

			DimensionCursor cursor = (DimensionCursor) rowDimList.get( i );
			Object value = cursor.getObject( levelName );
			rowValueMap.put( levelName, value );
		}

		if( columnLevelList.isEmpty( ) )
			findColumn = true;
		if( rowLevelList.isEmpty( ) )
			findRow = true;
		
		int position = 0;
		if ( axis.getAssociationQueryResultSet( ).length( ) <= 0 )
			return;
		else
			axis.getAssociationQueryResultSet( ).seek( position );
		while ( !findColumn || !findRow )
		{
			for ( int i = 0; i < columnLevelList.size( ); i++ )
			{
				String levelName = columnLevelList.get( i ).toString( );

				Object value1 = columnValueMap.get( levelName );
				Object value2 = axis.getAssociationQueryResultSet( )
						.getLevelKeyValue( axis.getAssociationQueryResultSet( )
								.getLevelIndex( levelName ) )[0];
				if ( value1.equals( value2 ) )
				{
					if ( i == columnLevelList.size( ) - 1 )
					{
						findColumn = true;
						break;
					}
					else
						continue;
				}
				else
				{
					findColumn = false;
					break;
				}
			}
			if ( findColumn )
			{
				for ( int j = 0; j < rowLevelList.size( ); j++ )
				{
					String levelName = rowLevelList.get( j ).toString( );
					Object value1 = rowValueMap.get( levelName );

					Object value2 = axis.getAssociationQueryResultSet( )
							.getLevelKeyValue( axis.getAssociationQueryResultSet( )
									.getLevelIndex( levelName ) )[0];
					if ( value1.equals( value2 ) )
					{
						if ( j == rowLevelList.size( ) - 1 )
						{
							findRow = true;
							break;
						}
						continue;
					}
					else
					{
						findRow = false;
						break;
					}
				}
			}
			if ( findRow && findColumn )
			{
				break;
			}
			else if ( position < axis.getAssociationQueryResultSet( ).length( ) - 1 )
			{
				axis.getAssociationQueryResultSet( ).seek( ++position );
			}
			else
			{
				throw new OLAPException( ResourceConstants.CURSOR_SEEK_ERROR );
			}
		}
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

			populateRelation( index, aggrName );
			return this.resultSet.getMeasureResult( )[id].getQueryResultSet( )
					.getAggregationValue( index );
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
			populateRelation( id, arg0 );
			return this.resultSet.getMeasureResult( )[id].getQueryResultSet( )
					.getAggregationValue( index );
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
