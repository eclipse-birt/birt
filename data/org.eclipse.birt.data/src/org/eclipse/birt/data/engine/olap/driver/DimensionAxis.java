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

package org.eclipse.birt.data.engine.olap.driver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.olap.OLAPException;
import javax.olap.cursor.RowDataMetaData;

import org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor;
import org.eclipse.birt.data.engine.olap.cursor.RowDataMetaDataImpl;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;

/**
 * A DimensionAxis represents an axis based on certain level. It provides
 * methods to point to current position, and get the value at this position.
 * 
 */
public class DimensionAxis
{

	private IResultSetMetaData metaData;
	private IAggregationResultSet rs;
	private int dimAxisIndex, levelIndex;
	private IRowDataAccessor accessor;
	private boolean isMirrored = false; 
	private int aggrSortType = IDimensionSortDefn.SORT_ASC;
	private Vector valueObjects = null;

	/**
	 * 
	 * @param container
	 * @param rs
	 * @param dimAxisIndex 
	 * @param levelIndex
	 * @param attrIndex
	 */
	public DimensionAxis( EdgeAxis container, IAggregationResultSet rs,
			int dimAxisIndex, int levelIndex )
	{
		this( container, rs, dimAxisIndex, levelIndex, false, null );
	}
	
	/**
	 * 
	 * @param container
	 * @param rs
	 * @param dimAixsIndex
	 * @param levelIndex
	 * @param attrIndex
	 */
	public DimensionAxis( EdgeAxis container, IAggregationResultSet rs,
			int dimAixsIndex, int levelIndex, boolean isMirrored,
			AggrSortDefinition aggrSortDefinition )
	{
		this.metaData = new ResultSetMetadata( rs, levelIndex );
		this.rs = rs;
		this.levelIndex = levelIndex;
		this.accessor = container.getRowDataAccessor( );
		this.dimAxisIndex = dimAixsIndex;
		this.isMirrored = isMirrored;
		if ( this.isMirrored )
		{
			valueObjects = populateValueVector( aggrSortDefinition );
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMirrored( )
	{
		return this.isMirrored;
	}
	
	/**
	 * 
	 * @return
	 */
	private Vector populateValueVector( AggrSortDefinition aggrSortDefinition )
	{
		Set valueSet = new HashSet( );
		if ( aggrSortDefinition != null )
		{
			if ( aggrSortDefinition.getAxisQualifierLevel( ).length == 0 )
				aggrSortType = aggrSortDefinition.getSortDirection( );
			else
				aggrSortType = IDimensionSortDefn.SORT_UNDEFINED;
		}

		for ( int i = 0; i < this.rs.length( ); i++ )
		{
			try
			{
				this.rs.seek( i );
			}
			catch ( IOException e )
			{
			}
			valueSet.add( this.rs.getLevelKeyValue( levelIndex )[ this.rs.getLevelKeyColCount( levelIndex )-1] );
		}
		
		final int sortType = this.rs.getSortType( levelIndex );
		Object[] value = valueSet.toArray( );
		Arrays.sort( value, new Comparator( ) {

			public int compare( final Object arg0, final Object arg1 )
			{
				if ( sortType == IDimensionSortDefn.SORT_ASC
						|| sortType == IDimensionSortDefn.SORT_UNDEFINED )
					return ( (Comparable) arg0 ).compareTo( arg1 );
				else
					return ( (Comparable) arg0 ).compareTo( arg1 ) * -1;
			}
		} );
		Vector v = new Vector( );
		v.addAll( Arrays.asList( value ) );
		return v;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vector getDisctinctValue( )
	{
		return this.valueObjects;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLevelIndex( )
	{
		return this.levelIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDimensionAxisIndex( )
	{
		return this.dimAxisIndex;
	}

	/**
	 * 
	 * @return
	 */
	public IRowDataAccessor getRowDataAccessor( )
	{
		return this.accessor;
	}

	/**
	 * 
	 * @return
	 */
	public IAggregationResultSet getAssociationQueryResultSet( )
	{
		return this.rs;
	}

	/**
	 * Get dimension's metadata
	 * @return
	 */
	public RowDataMetaData getRowDataMetaData( )
	{
		return new RowDataMetaDataImpl( metaData );
	}

	/**
	 * Move cursor to the next row.Return false if the next row does not exist.
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean next( ) throws OLAPException
	{
		return this.accessor.dim_next( dimAxisIndex );
	}

	/**
	 * Moves cursor to previous row. Return false if the previous row does not
	 * exist
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean previous( ) throws OLAPException
	{
		return this.accessor.dim_previous( dimAxisIndex );
	}

	/**
	 * Moves cursor offset positions relative to current. Returns false if the
	 * indicated position does not exist
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean relative( int arg0 ) throws OLAPException
	{
		return this.accessor.dim_relative( arg0, dimAxisIndex );
	}

	/**
	 * Moves the cursor to the first row in the result set. Returns false if the
	 * result set is empty.
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean first( ) throws OLAPException
	{
		return this.accessor.dim_first( dimAxisIndex );
	}

	/**
	 * Moves cursor to last row. Returns false if the result set is empty
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean last( ) throws OLAPException
	{
		return this.accessor.dim_last( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBeforeFirst( )
	{
		return this.accessor.dim_isBeforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isAfterLast( ) throws OLAPException
	{
		return this.accessor.dim_isAfterLast( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isFirst( ) throws OLAPException
	{
		return this.accessor.dim_isFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isLast( ) throws OLAPException
	{
		return this.accessor.dim_isLast( dimAxisIndex );
	}

	/**
	 * Moves the cursor to the end of the result set, just after the last row
	 * 
	 * @throws OLAPException
	 */
	public void afterLast( ) throws OLAPException
	{
		this.accessor.dim_afterLast( dimAxisIndex );
	}

	/**
	 * Moves the cursor to the front of the result set, just before the first
	 * row.
	 * 
	 * @throws OLAPException
	 */
	public void beforeFirst( ) throws OLAPException
	{
		this.accessor.dim_beforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException
	 */
	public void setPosition( long position ) throws OLAPException
	{
		this.accessor.dim_setPosition( dimAxisIndex, position );
	}

	/**
	 * Returns the cursor in current position.
	 * 
	 * @return the cursor in current position.
	 * @throws OLAPException
	 */
	public long getPosition( ) throws OLAPException
	{
		return this.accessor.dim_getPosition( dimAxisIndex );
	}

	/**
	 * Closes the result set and releases all resources.
	 * @throws OLAPException 
	 *
	 */
	public void close( ) throws OLAPException
	{
		try
		{
			this.rs.close( );
		}
		catch ( IOException e )
		{
			throw new OLAPException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Return the extend of this cursor
	 * 
	 * @return
	 */
	public long getExtend( )
	{
		return 0;
	}

	/**
	 * Returns the type of the cursor.
	 * @return
	 */
	public int getType( )
	{
		return 0;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public long getEdgeEnd( ) throws OLAPException
	{
		return this.accessor.getEdgeEnd( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public long getEdgeStart( ) throws OLAPException
	{
		return this.accessor.getEdgeStart( dimAxisIndex );
	}

	/**
	 * 
	 * @param attr
	 * @return
	 * @throws OLAPException
	 */
	public Object getCurrentMember( int attr ) throws OLAPException
	{
		return this.accessor.dim_getCurrentMember( dimAxisIndex, attr, aggrSortType );
	}
	
	/**
	 * 
	 * @param attrName
	 * @return
	 * @throws OLAPException
	 */
	public Object getCurrentMember( String attrName ) throws OLAPException
	{
		return this.accessor.dim_getCurrentMember( dimAxisIndex, attrName, aggrSortType );
	}
	
	/**
	 * 
	 * @param edgeInfoUtil
	 */
	public void setEdgeInfo( IRowDataAccessor accessor )
	{
		this.accessor = accessor;
	}
}
