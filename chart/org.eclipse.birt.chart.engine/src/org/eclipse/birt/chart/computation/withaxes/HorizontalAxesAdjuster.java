/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Label;

import com.ibm.icu.util.Calendar;

/**
 * The class is used to adjust horizontal axes's location and scale.
 * 
 * @since 2.5
 */

public class HorizontalAxesAdjuster implements IAxisAdjuster
{

	private OneAxis[] fHorizontalAxes;

	private OneAxis fVerticalAxis;

	private PlotWithAxes fPlotWithAxes;

	private Bounds fPlotBounds;

	/**
	 * Constructor.
	 * 
	 * @param horizontalAxes
	 * @param verticalAxis
	 * @param plotWithAxes
	 * @param boPlot
	 */
	public HorizontalAxesAdjuster( OneAxis[] horizontalAxes,
			OneAxis verticalAxis, PlotWithAxes plotWithAxes, Bounds boPlot )
	{
		fHorizontalAxes = horizontalAxes;
		fVerticalAxis = verticalAxis;
		fPlotWithAxes = plotWithAxes;
		fPlotBounds = boPlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.IAxisAdjuster#adjust()
	 */
	public void adjust( ) throws ChartException
	{
		AutoScale scY = fVerticalAxis.getScale( );
		boolean isForward = ( scY.getDirection( ) == PlotWithAxes.FORWARD );

		List<HorizontalAxisAdjuster> values = new ArrayList<HorizontalAxisAdjuster>( );
		List<HorizontalAxisAdjuster> min = new ArrayList<HorizontalAxisAdjuster>( );
		List<HorizontalAxisAdjuster> max = new ArrayList<HorizontalAxisAdjuster>( );

		// Parses all horizontal axes and put them into min/max/value origin
		// set.
		for ( OneAxis oa : fHorizontalAxes )
		{
			HorizontalAxisAdjuster vaa = new HorizontalAxisAdjuster( oa,
					fVerticalAxis,
					fPlotWithAxes,
					fPlotBounds );
			int iv = oa.getIntersectionValue( ).getType( );
			switch ( iv )
			{
				case IConstants.MIN :
					min.add( vaa );
					break;
				case IConstants.MAX :
					max.add( vaa );
					break;
				default :
					values.add( vaa );
			}
		}

		// 1. Adjusts value origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		double y = Double.NaN;
		double bottom = Double.NaN;
		double top = Double.NaN;

		double[] endPoints = fVerticalAxis.getScale( ).getEndPoints( );
		fVerticalAxis.getScale( ).resetShifts( );

		for ( int i = 0; i < values.size( ); i++ )
		{
			values.get( i ).adjust( );

			double locationDelta = Math.abs( AxesAdjuster.getLocationDelta( fVerticalAxis.getScale( ),
					values.get( i ).getHorizontalAxis( ).getIntersectionValue( ) ) );
			if ( Double.isNaN( y ) )
			{
				y = values.get( i ).getAxisY( );
				if ( !isForward )
				{
					y = y + locationDelta;
					bottom = values.get( i ).getAxisBottomEdge( );
					if ( y > bottom )
					{
						bottom = y;
					}
					top = y;
				}
				else
				{
					y = y - locationDelta;
					bottom = y;
					top = values.get( i ).getAxisTopEdge( );
					if ( y < top )
					{
						top = y;
					}
				}
			}
			else
			{
				double deltaY1 = bottom - y;
				double deltaY2 = y - top;
				double newY = values.get( i ).getAxisY( );
				if ( !isForward )
				{
					newY += locationDelta;
				}
				else
				{
					newY -= locationDelta;
				}

				if ( !isForward )
				{
					if ( newY < y )
					{
						y = newY;
					}
					bottom = y
							+ Math.max( deltaY1, values.get( i )
									.getAxisBottomEdge( )
									- y );
					top = y;
				}
				else
				{
					if ( newY > y )
					{
						y = newY;
					}
					bottom = y;
					top = y
							- +Math.max( deltaY2, y
									- values.get( i ).getAxisTopEdge( ) );
				}
			}

			scY.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 2. Adjusts min origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		scY.setEndPoints( endPoints[0], endPoints[1] );
		scY.resetShifts( );
		for ( int i = 0; i < min.size( ); i++ )
		{
			min.get( i ).adjust( );

			if ( Double.isNaN( y ) )
			{
				y = min.get( i ).getAxisY( );
				bottom = min.get( i ).getAxisBottomEdge( );
				top = min.get( i ).getAxisTopEdge( );
			}
			else
			{
				double deltaY1 = bottom - y;
				double deltaY2 = y - top;

				if ( min.get( i ).getAxisY( ) < y )
				{
					y = min.get( i ).getAxisY( );

				}
				bottom = y
						+ Math.max( deltaY1, min.get( i ).getAxisBottomEdge( )
								- min.get( i ).getAxisY( ) );
				top = y
						- Math.max( deltaY2, min.get( i ).getAxisY( )
								- min.get( i ).getAxisTopEdge( ) );
			}

			scY.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 3. Adjusts vertical axis positions according to the positions of
		// min origin & value origin axes.
		scY.setEndPoints( endPoints[0], endPoints[1] );
		if ( !Double.isNaN( y ) )
		{
			double[] positions = adjustOrthogonalAxis( IConstants.MIN,
					fVerticalAxis,
					y,
					bottom,
					top );
			y = positions[0];
			top = positions[1];
			bottom = positions[2];
		}

		// 4
		// 4.1 Set value origin axis coordinate and title coordinate according
		// to the axis y location, axis top edge and axis bottom edge.
		for ( int i = 0; i < values.size( ); i++ )
		{
			OneAxis oa = values.get( i ).getHorizontalAxis( );
			double iXTitleLocation = oa.getTitlePosition( );

			double axisCoordinate = 0;
			double locationDelta = Math.abs( AxesAdjuster.getLocationDelta( fVerticalAxis.getScale( ),
					values.get( i ).getHorizontalAxis( ).getIntersectionValue( ) ) );
			if ( !isForward )
			{
				axisCoordinate = scY.getEndPoints( )[0];
				axisCoordinate -= locationDelta;
			}
			else
			{
				axisCoordinate = scY.getEndPoints( )[1];
				axisCoordinate += locationDelta;
			}

			double axisTitleCoordinate = ( iXTitleLocation == PlotWithAxes.BELOW ) ? bottom
					- 1
					- locationDelta
					- values.get( i ).getAxisTitleThickness( )
					: axisCoordinate
							- values.get( i ).getAxisTitleThickness( )
							+ 1
							- ( ( oa.getLabelPosition( ) == PlotWithAxes.BELOW ) ? 0
									: values.get( i ).getAxisLabelThickness( ) );

			oa.setAxisCoordinate( axisCoordinate );
			oa.setTitleCoordinate( axisTitleCoordinate );
		}

		// 4.2 Set min origin axis coordinate and title coordinate according to
		// the axis y location, axis top edge and axis bottom edge.
		for ( int i = 0; i < min.size( ); i++ )
		{
			OneAxis oa = min.get( i ).getHorizontalAxis( );
			double iXTitleLocation = oa.getTitlePosition( );
			oa.setTitleCoordinate( ( iXTitleLocation == PlotWithAxes.BELOW ) ? bottom
					- 1
					- min.get( i ).getAxisTitleThickness( )
					: top + 1 );
			oa.setAxisCoordinate( y );
		}

		// 5. Adjusts max origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		y = Double.NaN;
		bottom = Double.NaN;
		top = Double.NaN;
		endPoints = scY.getEndPoints( );
		scY.resetShifts( );
		for ( int i = 0; i < max.size( ); i++ )
		{
			max.get( i ).adjust( );

			if ( Double.isNaN( y ) )
			{
				y = max.get( i ).getAxisY( );
				bottom = max.get( i ).getAxisBottomEdge( );
				top = max.get( i ).getAxisTopEdge( );
			}
			else
			{
				double deltaY1 = bottom - y;
				double deltaY2 = y - top;

				if ( max.get( i ).getAxisY( ) > y )
				{
					y = max.get( i ).getAxisY( );
					bottom = y
							+ Math.max( deltaY1, max.get( i )
									.getAxisBottomEdge( )
									- y );
					top = y
							- Math.max( deltaY2, y
									- max.get( i ).getAxisTopEdge( ) );
				}
			}

			scY.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 6. Adjusts horizontal axis position according to the position of max
		// origin axes.
		if ( !Double.isNaN( y ) )
		{
			scY.setEndPoints( endPoints[0], endPoints[1] );
			double[] positions = adjustOrthogonalAxis( IConstants.MAX,
					fVerticalAxis,
					y,
					bottom,
					top );
			y = positions[0];
			top = positions[1];
			bottom = positions[2];
		}

		// 7. Sets max origin axes coordinate and title coordinate according the
		// axis y location, axis top edge and axis bottom edge.
		for ( int i = 0; i < max.size( ); i++ )
		{
			OneAxis oa = max.get( i ).getHorizontalAxis( );
			double iXTitleLocation = oa.getTitlePosition( );
			oa.setTitleCoordinate( ( iXTitleLocation == PlotWithAxes.BELOW ) ? bottom
					- 1
					- max.get( i ).getAxisTitleThickness( )
					: top + 1 );
			oa.setAxisCoordinate( y );
		}

		// 8. Recomputes ticks according to new start and end of vertical axis.
		scY.computeTicks( fPlotWithAxes.getDisplayServer( ),
				fVerticalAxis.getLabel( ),
				fVerticalAxis.getLabelPosition( ),
				PlotWithAxes.VERTICAL,
				scY.getStart( ),
				scY.getEnd( ),
				false,
				fPlotWithAxes.getAxes( ) );
		scY.resetShifts( );
	}

	/**
	 * Adjusts start and end of orthogonal axis, and returns axis coordinates.
	 * 
	 * @param iv
	 * @param orthogonalAxis
	 * @param dY
	 * @param dBottom
	 * @param dTop
	 * @return
	 * @throws ChartException
	 */
	public double[] adjustOrthogonalAxis( int iv, OneAxis orthogonalAxis,
			double dY, double dBottom, double dTop ) throws ChartException
	{
		IDisplayServer ids = fPlotWithAxes.getDisplayServer( );
		AutoScale scY = orthogonalAxis.getScale( );
		AllAxes aax = fPlotWithAxes.getAxes( );
		Label laYAxisLabels = orthogonalAxis.getLabel( );
		int iYLabelLocation = orthogonalAxis.getLabelPosition( );
		boolean bForwardScale = scY.getDirection( ) == PlotWithAxes.FORWARD;

		double dXAxisThickness = dBottom - dTop;

		double dDeltaY1 = dY - dTop;
		double dDeltaY2 = dBottom - dY;

		boolean startEndChanged = false;
		if ( ( bForwardScale && iv == IConstants.MIN )
				|| ( !bForwardScale && iv == IConstants.MAX ) )
		{
			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// UPPER END
			if ( ( bForwardScale && dXAxisThickness > scY.getStartShift( ) )
					|| ( !bForwardScale && dXAxisThickness > scY.getEndShift( ) ) )
			{
				// REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = scY.getStart( );
				double dEnd = dBottom - scY.getEndShift( );

				if ( bForwardScale )
				{
					dStart = dBottom - scY.getStartShift( );
					dEnd = scY.getEnd( );
				}

				scY.resetShifts( );

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints( dStart, dEnd );
				scY.computeTicks( ids,
						laYAxisLabels,
						iYLabelLocation,
						PlotWithAxes.VERTICAL,
						dStart,
						dEnd,
						startEndChanged,
						aax );
				if ( !scY.isStepFixed( ) )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
					{
						if ( !scY.zoomOut( ) )
						{
							break;
						}
						scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
						int tickCount = scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								PlotWithAxes.VERTICAL,
								dStart,
								dEnd,
								startEndChanged,
								aax );
						if ( scY.getUnit( ) != null
								&& PlotWithAxes.asInteger( scY.getUnit( ) ) == Calendar.YEAR
								&& tickCount <= 3
								|| fPlotWithAxes.isSharedScale( ) )
						{
							break;
						}
					}
				}
			}

			// 3. Get final y, yAbove, yBelow, set title cooidinate.
			dY -= fPlotWithAxes.getPlotInsets( ).getTop( );
			dTop = dY - dDeltaY1;
			dBottom = dY + dDeltaY2;
		}
		else if ( ( bForwardScale && iv == IConstants.MAX )
				|| ( !bForwardScale && iv == IConstants.MIN ) )
		{
			// 2. Compute and set endpoints of orthogonal axis.
			// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// LOWER END
			if ( ( bForwardScale && dXAxisThickness > scY.getEndShift( ) )
					|| ( !bForwardScale && dXAxisThickness > scY.getStartShift( ) ) )
			{
				// REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = dTop + scY.getStartShift( );
				double dEnd = scY.getEnd( );

				if ( bForwardScale )
				{
					dStart = scY.getStart( );
					dEnd = dTop + scY.getEndShift( );
				}
				scY.resetShifts( );

				if ( dStart < dEnd + 1 )
				{
					dStart = dEnd + 1;
					startEndChanged = true;
				}

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints( dStart, dEnd );
				scY.computeTicks( ids,
						laYAxisLabels,
						iYLabelLocation,
						PlotWithAxes.VERTICAL,
						dStart,
						dEnd,
						startEndChanged,
						false,
						aax );
				if ( !scY.isStepFixed( ) )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
					{
						if ( !scY.zoomOut( ) )
						{
							break;
						}
						double dOldStep = ( (Number) scY.getStep( ) ).doubleValue( );
						scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
						int tickCount = scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								PlotWithAxes.VERTICAL,
								dStart,
								dEnd,
								startEndChanged,
								false,
								aax );
						double dNewStep = ( (Number) scY.getStep( ) ).doubleValue( );
						if ( dNewStep < dOldStep )
						{
							break;
						}

						if ( scY.getUnit( ) != null
								&& PlotWithAxes.asInteger( scY.getUnit( ) ) == Calendar.YEAR
								&& tickCount <= 3
								|| fPlotWithAxes.isSharedScale( ) )
						{
							break;
						}
					}
				}
			}

			// 3. Get final y, yAbove, yBelow. set title coordinate.
			// MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
			dY += fPlotWithAxes.getPlotInsets( ).getBottom( );
			dTop = dY - dDeltaY1;
			dBottom = dY + dDeltaY2;
		}

		return new double[]{
				dY, dTop, dBottom
		};
	}
}
