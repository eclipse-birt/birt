/*******************************************************************************
 * Copyright (c) 2006 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

/**
 * A helper class to compute coordinates for Axis Ticks
 */

public class AxisTickCoordinates implements Cloneable
{

	private final int size;
	private double dStart, dEnd, dStep;
	private boolean isTickBetweenCategory;

	AxisTickCoordinates( int size, double dStart, double dEnd, double dStep,
			boolean isTickBetweenCategory )
	{
		this.size = size;
		this.dStart = dStart;
		this.dEnd = dEnd;
		this.dStep = dStep;
		this.isTickBetweenCategory = isTickBetweenCategory;
	}

	AxisTickCoordinates( int size, double dStart, double dEnd, double dStep )
	{
		this( size, dStart, dEnd, dStep, true );
	}

	/**
	 * Resets the start and end coordinates.
	 * 
	 * @param dStart
	 * @param dEnd
	 */
	void setEndPoints( double dStart, double dEnd )
	{
		this.dStart = dStart;
		this.dEnd = dEnd;
		if ( isTickBetweenCategory )
		{
			this.dStep = ( dEnd - dStart ) / size;
		}
		else
		{
			this.dStep = ( dEnd - dStart ) / ( size - 1 );
		}
	}

	public int size( )
	{
		return size;
	}

	public double getStart( )
	{
		return dStart;
	}

	public double getEnd( )
	{
		return dEnd;
	}

	public double getStep( )
	{
		return dStep;
	}

	/**
	 * Returns the coordinates of specified ticks. For the sake of performance,
	 * invokers need to ensure the index correct.
	 * 
	 * @param index
	 *            tick index
	 * @return
	 */
	public double getCoordinate( int index )
	{
		if ( index == 0 )
		{
			return dStart;
		}
		else if ( index == size - 1 )
		{
			return dEnd;
		}
		return dStart
				+ index
				* dStep
				- ( isTickBetweenCategory ? 0 : dStep / 2 );
	}

	/**
	 * Returns the normalized tick coordinates. that means the start point is
	 * always zero, and the array lines forward. For the sake of performance,
	 * invokers need to ensure the index correct.
	 * 
	 * @param index
	 *            tick index
	 * @return
	 */
	public double getNormalizedCoordinate( int index )
	{
		return getCoordinate( index ) - dStart;
	}

	public Object clone( )
	{
		return new AxisTickCoordinates( size,
				dStart,
				dEnd,
				dStep,
				isTickBetweenCategory );
	}
}
