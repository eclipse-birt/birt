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

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * A dimensionNavigator maintains a cursor pointing to its current row of data.
 * Initially the cursor is positioned before the first row. The next method
 * moves the cursor to the next row, it can be used in a while loop to iterate
 * through the result set.
 * 
 */
class DimensionNavigator implements INavigator
{
	protected DimensionAxis dimensionAxis;

	/**
	 * 
	 * @param dimensionAxis
	 */
	DimensionNavigator( DimensionAxis dimensionAxis )
	{
		this.dimensionAxis = dimensionAxis;
	}

	/**
	 * Move cursor to the next row.Return false if the next row does not exist.
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean next( ) throws OLAPException
	{
		return dimensionAxis.next( );
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
		return dimensionAxis.previous( );
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
		return dimensionAxis.relative( arg0 );
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
		return dimensionAxis.first( );
	}

	/**
	 * Moves cursor to last row. Returns false if the result set is empty
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean last( ) throws OLAPException
	{
		return dimensionAxis.last( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBeforeFirst( )
	{
		return dimensionAxis.isBeforeFirst( );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isAfterLast( ) throws OLAPException
	{
		return dimensionAxis.isAfterLast( );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isFirst( ) throws OLAPException
	{
		return dimensionAxis.isFirst( );
	}

	/**
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public boolean isLast( ) throws OLAPException
	{
		return dimensionAxis.isLast( );
	}

	/**
	 * Moves the cursor to the end of the result set, just after the last row
	 * 
	 * @throws OLAPException
	 */
	public void afterLast( ) throws OLAPException
	{
		dimensionAxis.afterLast( );
	}

	/**
	 * Moves the cursor to the front of the result set, just before the first
	 * row.
	 * 
	 * @throws OLAPException
	 */
	public void beforeFirst( ) throws OLAPException
	{
		dimensionAxis.beforeFirst( );
	}

	/**
	 * 
	 * @param position
	 * @throws OLAPException
	 */
	public void setPosition( long position ) throws OLAPException
	{
		dimensionAxis.setPosition( position );
	}

	/**
	 * Returns the cursor¡¯s current position.
	 * 
	 * @return the cursor¡¯s current position.
	 * @throws OLAPException
	 */
	public long getPosition( ) throws OLAPException
	{
		return dimensionAxis.getPosition( );
	}

	/**
	 * Closes the result set and releases all resources.
	 *
	 */
	public void close( )
	{
		dimensionAxis.close( );
	}

	/**
	 * Return the extend of this cursor
	 * 
	 * @return
	 */
	public long getExtend( )
	{
		return dimensionAxis.getExtend( );
	}

	/**
	 * Returns the type of the cursor.
	 * @return
	 */
	public int getType( )
	{
		return dimensionAxis.getType( );
	}
	
	/**
	 * 
	 * @return
	 * @throws OLAPException 
	 */
	public Object getCurrentMemeber( ) throws OLAPException
	{
		return dimensionAxis.getCurrentMember( 0 );
	}

	public long getEdgeEnd( ) throws OLAPException
	{
		return dimensionAxis.getEdgeEnd( );
	}

	public long getEdgeStart( ) throws OLAPException
	{
		return dimensionAxis.getEdgeStart( );
	}
}
