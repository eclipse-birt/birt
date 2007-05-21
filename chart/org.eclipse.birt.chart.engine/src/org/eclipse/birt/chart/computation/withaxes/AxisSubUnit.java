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

package org.eclipse.birt.chart.computation.withaxes;

/**
 * AxisSubUnit
 */
public final class AxisSubUnit
{

	/** Value to accumulate all values or positive values */
	private double dValueLast = 0;
	/** Value to accumulate negative values */
	private double dValueLastNegative = 0;

	/** Total value of all positive values */
	private double dPositiveTotal = 0;
	/** Total value of all negative values */
	private double dNegativeTotal = 0;
	/** Total value of all values, only used when stack together */
	private double dTotal = 0;

	/** The field stores max position of series in axes. */
	private double dLastMaxPosition = Double.NaN;
	
	/** The field stores min position of series in axes. */
	private double dLastMinPosition = Double.NaN;
	
	/** Return positive and negative values should be accumulated together or not */
	private final boolean bStackTogether;

	/**
	 * Constructor
	 * 
	 * @param bStackTogether
	 *            indicates if stack together
	 */
	AxisSubUnit( boolean bStackTogether )
	{
		this.bStackTogether = bStackTogether;
	}

	public final void reset( )
	{
		dValueLastNegative = 0;
		dValueLast = 0;
	}
	
	/**
	 * Returns if current positive and negative values are aggregated together
	 * or not
	 * 
	 * @return true: together, false: by sign respectively
	 */
	public boolean isStackTogether( )
	{
		return bStackTogether;
	}
	
	/**
	 * Accumulates the value and returns the result.
	 * 
	 * @param dValue
	 *            the value to accumulate
	 * @return the result value after accumulating
	 * @see #isStackTogether()
	 * @see #getStackedValue(double)
	 */
	public final double stackValue( double dValue )
	{
		if ( bStackTogether )
		{
			// Stack values together
			this.dValueLast += dValue;
		}
		else
		{
			if ( dValue > 0 )
			{
				// POSITIVE STACK ACCUMULATION
				this.dValueLast += dValue;
			}
			else if ( dValue < 0 )
			{
				// NEGATIVE STACK ACCUMULATION
				this.dValueLastNegative += dValue;
			}
		}
		return getStackedValue( dValue );
	}

	/**
	 * Returns the current accumulated value.
	 * 
	 * @param dValue
	 *            value to check the sign. If stack together, it's no use.
	 * @return the current accumulated value.
	 */
	public final double getStackedValue( double dValue )
	{
		if ( bStackTogether )
		{
			return dValueLast;
		}

		if ( dValue > 0 )
		{
			return dValueLast;
		}
		if ( dValue < 0 )
		{
			return dValueLastNegative;
		}
		return dValueLast;
	}
	
	
	public final void computeTotal( double dValue )
	{
		if ( bStackTogether )
		{
			this.dTotal += dValue;
		}

		if ( dValue > 0 )
		{
			this.dPositiveTotal += dValue;
		}
		else if ( dValue < 0 )
		{
			this.dNegativeTotal += dValue;
		}
	}

	/**
	 * 
	 * @return
	 */
	public final double getPositiveTotal( )
	{
		if ( bStackTogether )
		{
			return Math.max( dTotal, 0 );
		}
		return dPositiveTotal;
	}

	/**
	 * 
	 * @return
	 */
	public final double getNegativeTotal( )
	{
		if ( bStackTogether )
		{
			return Math.min( dTotal, 0 );
		}
		return dNegativeTotal;
	}

	/**
	 * 
	 * @param dValue
	 * @return
	 */
	public final double valuePercentage( double dValue )
	{
		if ( dPositiveTotal - dNegativeTotal == 0 )
		{
			return 0;
		}
		// Do not use dTotal to compute percentage to avoid data out of bound
		return ( dValue * 100d ) / ( dPositiveTotal - dNegativeTotal );
	}

	/**
	 * Saves the last position and uses to compute current position by adding
	 * margin
	 * 
	 * @param dValue
	 *            value to check the max or min location
	 * @param dBaseLocation
	 *            base location when last position is null
	 * @param dMargin
	 *            margin location
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=182279"
	 */
	public final void setLastPosition( double dValue, double dBaseLocation,
			double dMargin )
	{
		if ( bStackTogether || dValue >= 0 )
		{
			if ( !Double.isNaN( dLastMaxPosition ) )
			{
				dBaseLocation = dLastMaxPosition;
			}

			dBaseLocation += dMargin;
			dLastMaxPosition = dBaseLocation;
		}
		else
		{
			if ( !Double.isNaN( dLastMinPosition ) )
			{
				dBaseLocation = dLastMinPosition;
			}

			dBaseLocation += dMargin;
			dLastMinPosition = dBaseLocation;
		}
	}
	
	/**
	 * Gets the last position
	 * 
	 * @param dValue
	 *            value to check the max or min location
	 * @return
	 */
	public final double getLastPosition( double dValue )
	{
		if ( bStackTogether || dValue >= 0 )
		{
			return dLastMaxPosition;
		}
		return dLastMinPosition;
	}
	
}
