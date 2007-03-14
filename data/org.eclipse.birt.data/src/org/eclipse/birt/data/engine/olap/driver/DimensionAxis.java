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

import javax.olap.OLAPException;
import javax.olap.cursor.RowDataMetaData;

import org.eclipse.birt.data.engine.olap.cursor.EdgeInfoGenerator;
import org.eclipse.birt.data.engine.olap.cursor.RowDataMetaDataImpl;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * 
 *
 */
public class DimensionAxis
{

	private IResultSetMetaData metaData;
	private IAggregationResultSet rs;
	private int dimAxisIndex, levelIndex, attrIndex;
	private EdgeInfoGenerator edgeInfo;

	/**
	 * 
	 * @param container
	 * @param rs
	 * @param dimAixsIndex
	 * @param levelIndex
	 * @param attrIndex
	 */
	public DimensionAxis( EdgeAxis container, IAggregationResultSet rs,
			int dimAixsIndex, int levelIndex, int attrIndex )
	{
		this.metaData = new ResultSetMetadata( rs, levelIndex );
		this.rs = rs;
		this.levelIndex = levelIndex;
		this.attrIndex = attrIndex;
		this.edgeInfo = container.getEdgeInfoUtil( );
		this.dimAxisIndex = dimAixsIndex;
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
	public int getAttributeName( )
	{
		return this.attrIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	public EdgeInfoGenerator getEdgeInfo( )
	{
		return this.edgeInfo;
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
		return this.edgeInfo.dim_next( dimAxisIndex );
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
		return this.edgeInfo.dim_previous( dimAxisIndex );
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
		return this.edgeInfo.dim_relative( arg0, dimAxisIndex );
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
		return this.edgeInfo.dim_first( dimAxisIndex );
	}

	/**
	 * Moves cursor to last row. Returns false if the result set is empty
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean last( ) throws OLAPException
	{
		return this.edgeInfo.dim_last( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBeforeFirst( )
	{
		return this.edgeInfo.dim_isBeforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isAfterLast( ) throws OLAPException
	{
		return this.edgeInfo.dim_isAfterLast( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isFirst( ) throws OLAPException
	{
		return this.edgeInfo.dim_isFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isLast( ) throws OLAPException
	{
		return this.edgeInfo.dim_isLast( dimAxisIndex );
	}

	/**
	 * Moves the cursor to the end of the result set, just after the last row
	 * 
	 * @throws OLAPException
	 */
	public void afterLast( ) throws OLAPException
	{
		this.edgeInfo.dim_afterLast( dimAxisIndex );
	}

	/**
	 * Moves the cursor to the front of the result set, just before the first
	 * row.
	 * 
	 * @throws OLAPException
	 */
	public void beforeFirst( ) throws OLAPException
	{
		this.edgeInfo.dim_beforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException
	 */
	public void setPosition( long position ) throws OLAPException
	{
		this.edgeInfo.dim_setPosition( dimAxisIndex, position );
	}

	/**
	 * Returns the cursor in current position.
	 * 
	 * @return the cursor in current position.
	 * @throws OLAPException
	 */
	public long getPosition( ) throws OLAPException
	{
		return this.edgeInfo.dim_getPosition( dimAxisIndex );
	}

	/**
	 * Closes the result set and releases all resources.
	 *
	 */
	public void close( )
	{
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

	public long getEdgeEnd( ) throws OLAPException
	{
		return this.edgeInfo.getEdgeEnd( dimAxisIndex );
	}

	public long getEdgeStart( ) throws OLAPException
	{
		return this.edgeInfo.getEdgeStart( dimAxisIndex );
	}

	public Object getCurrentMember( int attr ) throws OLAPException
	{
		return this.edgeInfo.dim_getCurrentMember( dimAxisIndex, attr );
	}
	
	public Object getCurrentMember( String attrName ) throws OLAPException
	{
		return this.edgeInfo.dim_getCurrentMember( dimAxisIndex, attrName );
	}
	
	public Object getCurrentAggregation( int index ) throws IOException
	{
		return this.edgeInfo.dim_getCurrentAggregation( index );
	}

	public void setEdgeInfo( EdgeInfoGenerator edgeInfoUtil )
	{
		this.edgeInfo = edgeInfoUtil;
	}
}
