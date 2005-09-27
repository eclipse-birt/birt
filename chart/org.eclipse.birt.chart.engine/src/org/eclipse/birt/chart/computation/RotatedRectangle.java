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

import java.awt.Point;
import java.awt.Polygon;

import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;

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
		super( new int[]{
				(int) dX0, (int) dX1, (int) dX2, (int) dX3
		}, new int[]{
				(int) dY0, (int) dY1, (int) dY2, (int) dY3
		}, 4 );
	}

	/**
	 * Returns the n-th point.
	 * 
	 * @param iOffset
	 * @return
	 */
	public final Point getPoint( int iOffset )
	{
		return new Point( xpoints[iOffset], ypoints[iOffset] );
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
			iaXY[2 * i] = xpoints[i];
			iaXY[2 * i + 1] = ypoints[i];
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
			xpoints[i] += dOffset;
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
			ypoints[i] += dOffset;
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
			xpoints[i] += dOffset;
			ypoints[i] += dOffset;
		}
	}

	/**
	 * Returns points as Location objects.
	 * 
	 * @return
	 */
	public final Location[] asLocations( )
	{
		final Location[] loa = new Location[4];
		for ( int i = 0; i < 4; i++ )
		{
			loa[i] = LocationImpl.create( xpoints[i], ypoints[i] );
		}
		return loa;
	}
}