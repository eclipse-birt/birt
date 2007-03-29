/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.Methods;

/**
 * Scale context for min/max computation.
 * 
 * @TODO only support computation for linear value, to add support DataTime
 *       value if need be
 */

public class ScaleContext extends Methods
{

	// Min/Max value in the dataset
	private Object oMinAuto;
	private Object oMaxAuto;

	// Min/Max/Step value specified in model
	private Object oMinFixed;
	private Object oMaxFixed;
	private Integer oStepNumber;

	// Percentage of margin area for special charts, such as bubble
	private final int iMarginPercent;

	private final int iType;

	private Object oMin;
	private Object oMax;
	private Object oStep;

	private boolean bMinimumFixed = false;
	private boolean bMaximumFixed = false;
	private boolean bStepFixed = false;
	private boolean bMargin = false;

	public ScaleContext( int iMarginPercent, int iType, Object oMinAuto,
			Object oMaxAuto, Object oStep )
	{
		this.iMarginPercent = iMarginPercent;
		this.iType = iType;

		this.oMinAuto = oMinAuto;
		this.oMaxAuto = oMaxAuto;
		this.oStep = oStep;

		this.bMargin = iMarginPercent > 0;
	}

	public void setFixedValue( boolean bMinimumFixed, boolean bMaximumFixed,
			Object oMinFixed, Object oMaxFixed )
	{
		this.oMinFixed = oMinFixed;
		this.oMaxFixed = oMaxFixed;
		this.bMinimumFixed = bMinimumFixed;
		this.bMaximumFixed = bMaximumFixed;

		this.oMin = oMinFixed;
		this.oMax = oMaxFixed;
	}

	public void setFixedStep( boolean bStepFixed, Integer oStepNumber )
	{
		this.oStepNumber = oStepNumber;

		bStepFixed = bStepFixed || oStepNumber != null;
	}

	public Object getMin( )
	{
		return oMin;
	}

	public Object getMax( )
	{
		return oMax;
	}

	public Object getRealMin( )
	{
		return oMinAuto;
	}

	public Object getRealMax( )
	{
		return oMaxAuto;
	}

	public Object getStep( )
	{
		return oStep;
	}

	public void computeMinMax( )
	{
		// These min/max is the value for the real boundary. If users
		// set the fixed value, to clip it.
		final double dMinReal, dMaxReal;
		// These min/max is the value that displays on axis after
		// considering the fixed value
		final double dMinValue, dMaxValue;
		double dMargin = 0;
		if ( bMargin )
		{
			// Margin for client area to render chart, such as bubbles
			dMargin = Math.abs( asDouble( oMaxAuto ).doubleValue( )
					- asDouble( oMinAuto ).doubleValue( ) )
					* iMarginPercent / 100;
		}
		dMinReal = asDouble( oMinAuto ).doubleValue( ) - dMargin;
		dMaxReal = asDouble( oMaxAuto ).doubleValue( ) + dMargin;
		dMinValue = bMinimumFixed ? asDouble( oMinFixed ).doubleValue( )
				: dMinReal;
		dMaxValue = bMaximumFixed ? asDouble( oMaxFixed ).doubleValue( )
				: dMaxReal;

		// These min/max is the value after auto adjusting
		double dMinAxis = dMinValue;
		double dMaxAxis = dMaxValue;
		double dStep = 0;

		if ( bStepFixed && oStepNumber != null )
		{
			// Compute step size
			oStep = new Double( Math.abs( dMaxValue - dMinValue )
					/ ( oStepNumber.intValue( ) ) );
			dStep = asDouble( oStep ).doubleValue( );
		}
		else
		{
			dStep = asDouble( oStep ).doubleValue( );

			if ( bMargin )
			{
				dMinAxis = ( ( dStep >= 1 ) ? Math.floor( dMinAxis / dStep )
						: Math.round( dMinAxis / dStep ) )
						* dStep;
				dMaxAxis = ( ( ( dStep >= 1 ) ? Math.floor( dMaxAxis / dStep )
						: Math.round( dMaxAxis / dStep ) ) + 1 )
						* dStep;
				if ( dMaxAxis - dMaxValue >= dStep )
				{
					// To minus extra step because of Math.floor
					dMaxAxis -= dStep;
				}

				// // To set 0 when all values are positive or negative
				// according to MS Excel behavior
				// if ( dMinAxis > 0 && dMaxAxis > 0 )
				// {
				// dMinAxis = 0;
				// }
				// else if ( dMinAxis < 0 && dMaxAxis < 0 )
				// {
				// dMaxAxis = 0;
				// }
			}
			else
			{
				// Auto adjust min and max by step if step number is not fixed
				final double dAbsMax = Math.abs( dMaxValue );
				final double dAbsMin = Math.abs( dMinValue );

				dMinAxis = ( ( dStep > 1 ) ? Math.floor( dAbsMin / dStep )
						: Math.round( dAbsMin / dStep ) )
						* dStep;
				dMaxAxis = ( ( dStep > 1 ) ? Math.floor( dAbsMax / dStep )
						: Math.round( dAbsMax / dStep ) )
						* dStep;

				if ( dMinAxis == dAbsMin )
				{
					dMinAxis += dStep;
					if ( dMinValue < 0 )
					{
						dMinAxis = -dMinAxis;
					}
					else if ( dMinValue == 0 )
					{
						dMinAxis = 0;
					}
				}
				else
				{
					if ( dMinValue < 0 )
					{
						dMinAxis = -( dMinAxis + dStep );
					}
					else if ( dMinAxis >= dMinValue && dMinAxis != 0 )
					{
						dMinAxis -= dStep;
					}
				}

				if ( dMaxAxis == dAbsMax )
				{
					dMaxAxis += dStep;
					if ( dMaxValue == 0 )
					{
						dMaxAxis = 0;
					}
				}
				else if ( dMinAxis != dMaxValue )
				{
					if ( dMaxValue < 0 )
					{
						dMaxAxis = -( dMaxAxis - dStep );
					}
					else if ( dMaxValue > 0 )
					{
						if ( dMaxAxis < dMaxValue )
						{
							dMaxAxis += dStep;
						}
					}
				}

				if ( dMinValue < 0 && dMaxValue < 0 )
				{
					if ( dMaxAxis <= dMaxValue - dStep )
					{
						dMaxAxis += 2 * dStep;
					}
				}
				if ( dMinValue > 0 && dMaxValue > 0 )
				{
					if ( dMinAxis >= dMinValue + dStep )
					{
						dMinAxis -= 2 * dStep;
					}
				}
			}

		}

		// handle special case for min/max are both zero
		if ( dMinValue == 0 && dMaxValue == 0 )
		{
			if ( dMinAxis >= 0 )
			{
				dMinAxis = -1;
			}
			if ( dMaxAxis <= 0 )
			{
				dMaxAxis = 1;
			}
		}

		// To make sure the boundary is always 100, -100 in percent type
		if ( ( iType & PERCENT ) == PERCENT )
		{
			if ( dMaxAxis > 0 )
			{
				dMaxAxis = 100;
			}
			if ( dMinAxis < 0 )
			{
				dMinAxis = -100;
			}
		}
		if ( !bMaximumFixed )
		{
			oMax = new Double( dMaxAxis );
		}
		if ( !bMinimumFixed )
		{
			oMin = new Double( dMinAxis );
		}

		if ( bMargin )
		{
			// If users specify a smaller range, to save the real range for
			// clipping later
			if ( bMinimumFixed && dMinValue > dMinReal )
			{
				oMinAuto = new Double( dMinReal );
			}
			else
			{
				oMinAuto = null;
			}

			if ( bMaximumFixed && dMaxValue < dMaxReal )
			{
				oMaxAuto = new Double( dMaxReal );
			}
			else
			{
				oMaxAuto = null;
			}
		}
		else
		{
			oMinAuto = null;
			oMaxAuto = null;
		}
	}
}
