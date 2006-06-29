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

import java.util.List;

import org.eclipse.birt.chart.internal.computations.Polygon;

/**
 * RotatedRectangle
 */
public final class RotatedRectangle extends Polygon
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

		add( dX0, dY0 );
		add( dX1, dY1 );
		add( dX2, dY2 );
		add( dX3, dY3 );
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
			Point lo = this.getPoint( i );
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
			Point lo = getPoint( i );
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
			Point lo = getPoint( i );
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
			Point lo = getPoint( i );
			lo.translate( dOffset, dOffset );
		}
	}

	/**
	 * Returns points as Point objects.
	 * 
	 * @return
	 */
	public final Point[] asPoints( )
	{
		List pts = getPoints( );
		return (Point[]) pts.toArray( new Point[pts.size( )] );
	}
}