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

	private double dValueMax = 0;

	private double dValueMin = 0;

	private double dPositiveTotal = 0;

	private double dNegativeTotal = 0;

	private double dLastValue = 0;

	/** The field stores orthogonal position of series in axes. */
	private double dLastPosition = Double.NaN;
	
	AxisSubUnit( )
	{
	}

	public final void reset( )
	{
		dValueMax = 0;
		dValueMin = 0;
		dLastValue = 0;
	}

	public final double getLastValue( )
	{
		return dLastValue;
	}

	public final void setLastValue( double dLastValue )
	{
		this.dLastValue = dLastValue;
	}

	/**
	 * @return Returns the valueMax.
	 */
	public final double getValueMax( )
	{
		return dValueMax;
	}

	/**
	 * @param dValueMax
	 *            The valueMax to set.
	 */
	public final void setValueMax( double dValueMax )
	{
		this.dValueMax = dValueMax;
	}

	/**
	 * @return Returns the valueMin.
	 */
	public final double getValueMin( )
	{
		return dValueMin;
	}

	/**
	 * @param dValueMin
	 *            The valueMin to set.
	 */
	public final void setValueMin( double dValueMin )
	{
		this.dValueMin = dValueMin;
	}

	/**
	 * 
	 * @param dPositiveTotal
	 */
	public final void setPositiveTotal( double dPositiveTotal )
	{
		this.dPositiveTotal = dPositiveTotal;
	}

	/**
	 * 
	 * @return
	 */
	public final double getPositiveTotal( )
	{
		return dPositiveTotal;
	}

	/**
	 * 
	 * @param dPositiveTotal
	 */
	public final void setNegativeTotal( double dNegativeTotal )
	{
		this.dNegativeTotal = dNegativeTotal;
	}

	/**
	 * 
	 * @return
	 */
	public final double getNegativeTotal( )
	{
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
		return ( dValue * 100d ) / ( dPositiveTotal - dNegativeTotal );
	}

	
	public final double getLastPosition( )
	{
		return dLastPosition;
	}

	
	public final void setLastPosition( double dLastPosition )
	{
		this.dLastPosition = dLastPosition;
	}
}
