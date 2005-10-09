/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.PolygonImpl;

/**
 * RotatedRectangle
 */
public final class RotatedRectangle extends PolygonImpl
{

	private static final long serialVersionUID = 1L;

	/**
	 * @param dX0
	 * @param dY0
	 * @param dX1
	 * @param dY1
	 * @param dX2
	 * @param dY2
	 * @param dX3
	 * @param dY3
	 */
	RotatedRectangle( double dX0, double dY0, double dX1, double dY1,
			double dX2, double dY2, double dX3, double dY3 )
	{
		super( );

		getPoints( ).add( LocationImpl.create( dX0, dY0 ) );
		getPoints( ).add( LocationImpl.create( dX1, dY1 ) );
		getPoints( ).add( LocationImpl.create( dX2, dY2 ) );
		getPoints( ).add( LocationImpl.create( dX3, dY3 ) );
	}

	/**
	 * Returns points as a single array, [x1,y1,x2,y2,...]
	 * 
	 * @return
	 */
	public final int[] getSwtPoints( )
	{
		int[] iaXY = new int[8];
		for ( int i = 0; i < 4; i++ )
		{
			Location lo = (Location) getPoints( ).get( i );
			iaXY[2 * i] = (int) lo.getX( );
			iaXY[2 * i + 1] = (int) lo.getY( );
		}
		return iaXY;
	}

	/**
	 * Shifts all X-value by given offset.
	 * 
	 * @param dOffset
	 */
	public final void shiftXVertices( double dOffset )
	{
		for ( int i = 0; i < 4; i++ )
		{
			Location lo = (Location) getPoints( ).get( i );
			lo.setX( lo.getX( ) + dOffset );
		}
	}

	/**
	 * Shifts all Y-value by given offset.
	 * 
	 * @param dOffset
	 */
	public final void shiftYVertices( double dOffset )
	{
		for ( int i = 0; i < 4; i++ )
		{
			Location lo = (Location) getPoints( ).get( i );
			lo.setY( lo.getY( ) + dOffset );
		}
	}

	/**
	 * Shifts both X,Y values by given offset.
	 * 
	 * @param dOffset
	 */
	public final void shiftVertices( double dOffset )
	{
		for ( int i = 0; i < 4; i++ )
		{
			Location lo = (Location) getPoints( ).get( i );
			lo.translate( dOffset, dOffset );
		}
	}

	/**
	 * Returns points as Location objects.
	 * 
	 * @return
	 */
	public final Location[] asLocations( )
	{
		return (Location[]) getPoints( ).toArray( new Location[0] );
	}
}