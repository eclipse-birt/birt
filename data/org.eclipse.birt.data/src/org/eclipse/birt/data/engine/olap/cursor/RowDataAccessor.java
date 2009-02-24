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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class is to populate the relationship between dimension and its
 * belonging edge cursor. It also provide the method to navigate on
 * edge/dimension cursor. Changing the position of an EdgeCursor affects both
 * the values of the dimension cursor of the edgeCursor and the value of the
 * data of the cubeCursor.
 * 
 */
public class RowDataAccessor implements IRowDataAccessor
{
	
	// result set for this edge
	private IAggregationResultSet rs;
	// the dimension axis on this edge
	protected DimensionAxis[] dimAxis;
	private int mirrorStartPosition, fetchRowLimit = -1;
	protected DimensionTraverse dimTraverse;
	protected EdgeTraverse edgeTraverse;
	protected EdgeDimensionRelation edgeDimensRelation;
	private RowDataAccessorService service;

	/**
	 * 
	 * @param resultSet
	 * @param axis
	 */
	public RowDataAccessor( RowDataAccessorService service )
	{
		assert service.getAggregationResultSet( ) != null;
		if ( service.getDimensionAxis( ).length == 0 )
			return;
		this.service = service;
		this.rs = service.getAggregationResultSet( );
		this.dimAxis = service.getDimensionAxis( );
		this.mirrorStartPosition = service.getMirrorStartPosition( );
	}

	/**
	 * Populate edgeInfo, EdgeInfo represents the startPosition and endPosition
	 * to its children. Here, it will distinguish the non-mirrored level and
	 * mirrored level. Only non-mirrored level will generate its EdgeInfo
	 * 
	 * @param isCalculatedMember
	 * @throws IOException
	 */
	public void initialize( boolean isPage ) throws IOException
	{
		ResultSetFetcher fetcher = new ResultSetFetcher( this.rs );

		service.setFetchSize( fetchRowLimit );
		edgeDimensRelation = new EdgeDimensionRelation( service,
				fetcher,
				this.fetchRowLimit, isPage );
		dimTraverse = new DimensionTraverse( dimAxis, edgeDimensRelation );
		edgeTraverse = new EdgeTraverse( edgeDimensRelation );
	}
	
	/**
	 * 
	 * @return
	 */
	public RowDataAccessorService getDataAccessorService( )
	{
		return this.service;
	}

	/**
	 * Move certain dimension cursor to the next row.Return false if the next
	 * row does not exist.
	 * 
	 * @param dimAxisIndex certain dimension cursor
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_next( int dimAxisIndex ) throws OLAPException
	{
		return dimTraverse.next( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_previous( int dimAxisIndex ) throws OLAPException
	{
		return dimTraverse.previous( dimAxisIndex );
	}
	
	/**
	 * 
	 * @param offset
	 * @param dimAxisIndex
	 * @return
	 * @throws OLAPException
	 */
	public boolean dim_relative( int offset, int dimAxisIndex )
			throws OLAPException
	{
		return dimTraverse.relative( offset, dimAxisIndex );
	}
	
	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_first( int dimAxisIndex )
	{
		return dimTraverse.first( dimAxisIndex );
	}
	
	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_last( int dimAxisIndex )
	{
		return dimTraverse.last( dimAxisIndex );
	}
	
	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isBeforeFirst( int dimAxisIndex )
	{
		return dimTraverse.isBeforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isAfterLast( int dimAxisIndex )
	{
		return dimTraverse.isAfterLast( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isFirst( int dimAxisIndex )
	{
		return dimTraverse.isFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public boolean dim_isLast( int dimAxisIndex )
	{
		return dimTraverse.isLast( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 */
	public void dim_afterLast( int dimAxisIndex )
	{
		dimTraverse.afterLast( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 */
	public void dim_beforeFirst( int dimAxisIndex )
	{
		dimTraverse.beforeFirst( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param position
	 */
	public void dim_setPosition( int dimAxisIndex, long position )
	{
		dimTraverse.setPosition( dimAxisIndex, position );
	}
	
	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public long dim_getPosition( int dimAxisIndex )
	{
		return dimTraverse.getPosition( dimAxisIndex );
	}
	
	/**
	 * 
	 * @param dimAxisIndex
	 * @param attr
	 * @return
	 * @throws OLAPException 
	 */
	public Object dim_getCurrentMember( int dimAxisIndex, int attr, int sortType )
			throws OLAPException
	{
		if ( !this.dimAxis[dimAxisIndex].isMirrored( ) )
		{
			try
			{
				int position = dimTraverse.getCurrentRowPosition( dimAxisIndex );
				if ( position == -1 )
				{
					throw new OLAPException( ResourceConstants.RD_EXPR_RESULT_SET_NOT_START );
				}
				rs.seek( position );
			}
			catch ( IOException e )
			{
				throw new OLAPException( e.getLocalizedMessage( ) );
			}
			return this.dimTraverse.getCurrentMember( dimAxisIndex, attr );
		}
		else
		{
			return fetchValueFromMirror( dimAxisIndex, sortType );
		}
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param attrName
	 * @return
	 * @throws OLAPException
	 */
	public Object dim_getCurrentMember( int dimAxisIndex, String attrName,
			int sortType ) throws OLAPException
	{

		if ( !this.dimAxis[dimAxisIndex].isMirrored( ) )
		{
			try
			{
				int position = dimTraverse.getCurrentRowPosition( dimAxisIndex );
				if ( position == -1 )
				{
					throw new OLAPException( ResourceConstants.RD_EXPR_RESULT_SET_NOT_START );
				}
				rs.seek( position );
			}
			catch ( IOException e )
			{
				throw new OLAPException( e.getLocalizedMessage( ) );
			}
			return this.dimTraverse.getCurrentMember( dimAxisIndex, attrName );
		}
		else
		{
			return fetchValueFromMirror( dimAxisIndex, sortType );
		}
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @param sortType
	 * @return
	 * @throws OLAPException
	 */
	protected Object fetchValueFromMirror( int dimAxisIndex, int sortType )
			throws OLAPException
	{
		if ( this.dimTraverse.dimensionCursorPosition[dimAxisIndex] < 0
				|| this.dimTraverse.dimensionCursorPosition[dimAxisIndex] >= edgeDimensRelation.mirrorLength[dimAxisIndex] )
			throw new OLAPException( ResourceConstants.RD_EXPR_RESULT_SET_NOT_START );

		if ( sortType == IDimensionSortDefn.SORT_UNDEFINED )
		{
			// find the result if there is aggregation sort definition
			Collection collection = null;
			try
			{
				collection = fetchValueCollectionInEdgeInfo( dimAxisIndex );
			}
			catch ( IOException e )
			{
			}

			Vector v = this.dimAxis[dimAxisIndex].getDisctinctValue( );
			v.removeAll( collection );
			Iterator iter = collection.iterator( );

			for ( int i = 0, startSize = v.size( ); i < collection.size( ); i++ )
			{
				v.insertElementAt( iter.next( ), startSize );
				startSize++;
			}
			return v.get( this.dimTraverse.dimensionCursorPosition[dimAxisIndex] );
		}
		else
			return this.dimAxis[dimAxisIndex].getDisctinctValue( )
					.get( this.dimTraverse.dimensionCursorPosition[dimAxisIndex] );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 * @throws IOException
	 */
	private Collection fetchValueCollectionInEdgeInfo( int dimAxisIndex )
			throws IOException
	{
		Set value = new LinkedHashSet( );
		EdgeInfo info = this.dimTraverse.findCurrentEdgeInfo( this.mirrorStartPosition - 1 );
		int index = this.edgeDimensRelation.currentRelation[this.mirrorStartPosition - 1].indexOf( info );
		EdgeInfo nextEdgeInfo = null;
		if ( index < this.edgeDimensRelation.currentRelation[this.mirrorStartPosition - 1].size( ) - 1 )
		{
			nextEdgeInfo = (EdgeInfo) this.edgeDimensRelation.currentRelation[this.mirrorStartPosition - 1].get( index + 1 );
		}

		int edgeStart = info == null ? this.rs.length( )-1 : info.firstChild;
		int edgeEnd = nextEdgeInfo == null ? this.rs.length( )-1
				: nextEdgeInfo.firstChild;
		if ( edgeStart >= 0 )
		{
			while ( edgeStart <= edgeEnd )
			{
				this.rs.seek( edgeStart );
				value.add( this.rs.getLevelKeyValue( dimAxisIndex )[this.rs.getLevelKeyColCount( dimAxisIndex ) - 1] );
				edgeStart++;
			}
		}
		return value;
	}
	
	/**
	 * 
	 */
	public void edge_afterLast( )
	{
		this.edgeTraverse.afterLast( );
		int[] lastDimLength = this.getLastDiemsionLength( );
		for ( int i = 0; i < this.dimAxis.length; i++ )
		{
			this.dimTraverse.dimensionCursorPosition[i] = lastDimLength[i];
		}
	}

	/**
	 * 
	 */
	public void edge_beforeFirst( )
	{
		this.edgeTraverse.beforeFirst( );
		this.dimTraverse.beforeFirst( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_first( )
	{
		if ( this.edgeTraverse.first( ) )
		{
			this.dimTraverse.first( );
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public long getEdgePostion( )
	{
		if( this.edgeTraverse== null )
		{
			return -1;
		}
		return this.edgeTraverse.getEdgePostion( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isAfterLast( )
	{
		return this.edgeTraverse.isAfterLast( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isBeforeFirst( )
	{
		return this.edgeTraverse.isBeforeFirst( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isFirst( )
	{
		return this.edgeTraverse.isFirst( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_isLast( )
	{
		return this.edgeTraverse.isLast( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean edge_last( )
	{
		if ( this.edgeTraverse.last( ) )
		{
			int[] lastDimLength = getLastDiemsionLength( );
			for ( int i = 0; i < this.dimAxis.length; i++ )
			{
				this.dimTraverse.dimensionCursorPosition[i] = lastDimLength[i] - 1;
			}
			return true;
		}
		else
			return false;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean edge_next( ) throws OLAPException
	{
		if ( !this.edgeTraverse.next( ) )
		{
			this.edge_afterLast( );
			return false;
		}
		if ( this.dimTraverse.isInitialStatus( ) )
		{
			for ( int i = this.dimAxis.length - 1; i >= 0; i-- )
			{
				this.dimTraverse.first( i );
			}
		}
		else
			for ( int i = this.dimAxis.length - 1; i >= 0; i-- )
			{
				if ( this.dimTraverse.next( i ) )
				{
					break;
				}
				else
				{
					this.dimTraverse.first( i );
				}
			}
		return true;
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean edge_previous( ) throws OLAPException
	{
		this.edgeTraverse.previous( );
		if ( this.edgeTraverse.currentPosition >= this.edgeDimensRelation.traverseLength - 1 )
		{
			for ( int i = 0; i < this.dimAxis.length; i++ )
			{
				this.dimTraverse.previous( i );
			}
			return true;
		}
		else if ( this.edgeTraverse.currentPosition >= 0 )
		{
			for ( int i = this.dimAxis.length - 1; i >= 0; i-- )
			{
				if ( this.dimTraverse.previous( i ) )
				{
					for ( int k = i + 1; k < this.dimAxis.length; k++ )
					{
						this.dimTraverse.last( k );
					}
					break;
				}
				else
				{
					this.dimTraverse.first( i );
				}
			}
			return true;
		}
		else
		{
			this.edgeTraverse.currentPosition = -1;
			this.dimTraverse.beforeFirst( );
			return false;
		}
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException 
	 */
	public boolean edge_relative( int arg0 ) throws OLAPException
	{
		if ( arg0 == 0 )
			return true;
		int position = this.edgeTraverse.currentPosition + arg0;
		if ( position >= this.edgeDimensRelation.traverseLength )
		{
			this.edge_afterLast( );
			return false;
		}
		else if ( position < 0 )
		{
			this.dimTraverse.beforeFirst( );
			this.edgeTraverse.currentPosition = -1;
			return false;
		}
		else
		{
			for ( int i = 0; i < Math.abs( arg0 ); i++ )
			{
				if ( arg0 > 0 )
					this.edge_next( );
				else
					this.edge_previous( );
			}
			return true;
		}
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException 
	 * @throws OLAPException
	 */
	public void edge_setPostion( long position ) throws OLAPException
	{
		if ( position < 0 )
		{
			this.dimTraverse.beforeFirst( );
			this.edgeTraverse.currentPosition = -1;
			return;
		}
		int offSet = (int) position - this.edgeTraverse.currentPosition;
		this.edge_relative( offSet );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public int getEdgeStart( int dimAxisIndex )
	{
		return this.dimTraverse.getEdgeStart( dimAxisIndex );
	}

	/**
	 * 
	 * @param dimAxisIndex
	 * @return
	 */
	public int getEdgeEnd( int dimAxisIndex )
	{
		return this.dimTraverse.getEdgeEnd( dimAxisIndex );
	}
	
	/**
	 * 
	 * @param fetchSize
	 */
	public void setFetchSize( int fetchSize )
	{
		this.fetchRowLimit = fetchSize;
	}
	
	/**
	 * 
	 * @return
	 */
	private int[] getLastDiemsionLength( )
	{
		int[] lastDimensionLength = new int[this.dimAxis.length];
		if ( this.mirrorStartPosition == 0 )
		{
			for ( int i = 0; i < this.dimAxis.length; i++ )
			{
				lastDimensionLength[i] = this.getRangeInLastDimension( i );
			}
		}
		else
		{
			for ( int i = 0; i < this.mirrorStartPosition; i++ )
			{
				lastDimensionLength[i] = this.getRangeInLastDimension( i );
			}
			for ( int i = this.mirrorStartPosition; i < this.dimAxis.length; i++ )
			{
				lastDimensionLength[i] = this.edgeDimensRelation.mirrorLength[i];
			}
		}
		return lastDimensionLength;
	}
	

	
	/**
	 * 
	 * @param dimIndex
	 * @return
	 */
	private int getRangeInLastDimension( int dimIndex )
	{
		if ( dimIndex == 0 )
			return this.edgeDimensRelation.currentRelation[0].size( );
		int size = this.edgeDimensRelation.currentRelation[dimIndex].size( );
		if ( size == 0 )
			return -1;
		int count = 1;
		EdgeInfo edgeInfo = (EdgeInfo) this.edgeDimensRelation.currentRelation[dimIndex].get( size - 1 );
		EdgeInfo previousInfo;
		for ( int i = size - 2; i >= 0; i-- )
		{
			previousInfo = (EdgeInfo) this.edgeDimensRelation.currentRelation[dimIndex].get( i );
			if ( previousInfo.parent == edgeInfo.parent )
				count++;
		}
		return count;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor#sychronizedWithPage(int)
	 */
	public void sychronizedWithPage( int position )
	{
		this.edgeDimensRelation.synchronizedWithPage( position );
		this.dimTraverse = new DimensionTraverse( this.service.getDimensionAxis( ),
				this.edgeDimensRelation );
		this.edgeTraverse = new EdgeTraverse( this.edgeDimensRelation );
		this.edge_beforeFirst( );
	}
}
