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
 * The class is used to adjust vertical axes's location and scale.
 * 
 * @since 2.5
 */

public class VerticalAxesAdjuster implements IAxisAdjuster
{

	private OneAxis[] fVerticalAxes;

	private OneAxis fHorizontalAxis;

	private PlotWithAxes fPlotWithAxes;

	private Bounds fPlotBounds;

	/**
	 * Constructor.
	 * 
	 * @param verticalAxes
	 * @param horizontalAxis
	 * @param plotWithAxes
	 * @param boPlot
	 */
	public VerticalAxesAdjuster( OneAxis[] verticalAxes,
			OneAxis horizontalAxis, PlotWithAxes plotWithAxes, Bounds boPlot )
	{
		fVerticalAxes = verticalAxes;
		fHorizontalAxis = horizontalAxis;
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
		AutoScale scX = fHorizontalAxis.getScale( );
		boolean isBackward = ( scX.getDirection( ) == PlotWithAxes.BACKWARD );

		List<VerticalAxisAdjuster> values = new ArrayList<VerticalAxisAdjuster>( );
		List<VerticalAxisAdjuster> min = new ArrayList<VerticalAxisAdjuster>( );
		List<VerticalAxisAdjuster> max = new ArrayList<VerticalAxisAdjuster>( );

		// Parses all vertical axes and put them into min/max/value origin
		// set.
		for ( OneAxis oa : fVerticalAxes )
		{
			VerticalAxisAdjuster vaa = new VerticalAxisAdjuster( oa,
					fHorizontalAxis,
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

		// 1. Adjusts value origin axes, computes axis location, axis left edge
		// and axis right edge
		double x = Double.NaN;
		double left = Double.NaN;
		double right = Double.NaN;

		double[] endPoints = fHorizontalAxis.getScale( ).getEndPoints( );
		fHorizontalAxis.getScale( ).resetShifts( );

		for ( int i = 0; i < values.size( ); i++ )
		{
			values.get( i ).adjust( );

			double locationDelta = Math.abs( AxesAdjuster.getLocationDelta( fHorizontalAxis.getScale( ),
					values.get( i ).getVerticalAxis( ).getIntersectionValue( ) ) );
			if ( Double.isNaN( x ) )
			{
				x = values.get( i ).getAxisX( );
				if ( !isBackward )
				{
					x = x - locationDelta;
					left = values.get( i ).getAxisLeftEdge( );
					if ( x < left )
					{
						left = x;
					}
					right = x;
				}
				else
				{
					x = x + locationDelta;
					left = x;
					right = values.get( i ).getAxisRightEdge( );
					if ( x > right )
					{
						right = x;
					}
				}
			}
			else
			{
				double deltaX1 = x - left;
				double deltaX2 = right - x;
				double newX = values.get( i ).getAxisX( );
				if ( !isBackward )
				{
					newX -= locationDelta;
				}
				else
				{
					newX += locationDelta;
				}

				if ( !isBackward )
				{
					if ( newX > x )
					{
						x = newX;
					}
					left = x
							- Math.max( deltaX1, x
									- values.get( i ).getAxisLeftEdge( ) );
					right = x;
				}
				else
				{
					if ( newX < x )
					{
						x = newX;
					}
					left = x;
					right = x
							+ Math.max( deltaX2, values.get( i )
									.getAxisRightEdge( )
									- x );
				}
			}

			scX.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 2. Adjusts min origin axes, computes axis location, axis left edge
		// and axis right edge.
		scX.setEndPoints( endPoints[0], endPoints[1] );
		scX.resetShifts( );
		for ( int i = 0; i < min.size( ); i++ )
		{
			min.get( i ).adjust( );

			if ( Double.isNaN( x ) )
			{
				x = min.get( i ).getAxisX( );
				left = min.get( i ).getAxisLeftEdge( );
				right = min.get( i ).getAxisRightEdge( );
			}
			else
			{
				double deltaX1 = x - left;
				double deltaX2 = right - x;

				if ( min.get( i ).getAxisX( ) > x )
				{
					x = min.get( i ).getAxisX( );

				}
				left = x
						- Math.max( deltaX1, min.get( i ).getAxisX( )
								- min.get( i ).getAxisLeftEdge( ) );
				right = x
						+ Math.max( deltaX2, min.get( i ).getAxisRightEdge( )
								- min.get( i ).getAxisX( ) );
			}

			scX.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 3. Adjusts horizontal axis positions according to the positions of
		// min origin & value origin axes.
		scX.setEndPoints( endPoints[0], endPoints[1] );
		double[] positions = adjustOrthogonalAxis( IConstants.MIN,
				fHorizontalAxis,
				x,
				left,
				right );
		x = positions[0];
		left = positions[1];
		right = positions[2];

		// 4.
		// 4.1 Sets value origin axes coordinate and title coordinate according
		// to the axis location, left edge and right edge.
		for ( int i = 0; i < values.size( ); i++ )
		{
			OneAxis oa = values.get( i ).getVerticalAxis( );
			double iYTitleLocation = oa.getTitlePosition( );

			double axisCoordinate = 0;
			double locationDelta = Math.abs( AxesAdjuster.getLocationDelta( fHorizontalAxis.getScale( ),
					values.get( i ).getVerticalAxis( ).getIntersectionValue( ) ) );
			if ( !isBackward )
			{
				axisCoordinate = scX.getEndPoints( )[0];
				axisCoordinate += locationDelta;
			}
			else
			{
				axisCoordinate = scX.getEndPoints( )[1];
				axisCoordinate -= locationDelta;
			}

			double axisTitleCoordinate = ( iYTitleLocation == PlotWithAxes.LEFT ) ? left
					- 1
					+ locationDelta
					: axisCoordinate
							+ 1
							+ ( ( oa.getLabelPosition( ) == PlotWithAxes.LEFT ) ? 0
									: values.get( i ).getAxisLabelThickness( ) );

			oa.setAxisCoordinate( axisCoordinate );
			oa.setTitleCoordinate( axisTitleCoordinate );
		}

		// 4.2 4. Sets min origin axes coordinate and title coordinate according
		// to the axis location, left edge and right edge.
		for ( int i = 0; i < min.size( ); i++ )
		{
			OneAxis oa = min.get( i ).getVerticalAxis( );
			double iYTitleLocation = oa.getTitlePosition( );
			oa.setTitleCoordinate( ( iYTitleLocation == PlotWithAxes.LEFT ) ? left - 1
					: right + 1 - min.get( i ).getAxisTitleThickness( ) );
			oa.setAxisCoordinate( x );
		}

		// 5. Adjusts max origin axes, computes x location, axis left edge and
		// axis right edge.
		x = Double.NaN;
		left = Double.NaN;
		right = Double.NaN;
		endPoints = scX.getEndPoints( );
		scX.resetShifts( );
		for ( int i = 0; i < max.size( ); i++ )
		{
			max.get( i ).adjust( );

			if ( Double.isNaN( x ) )
			{
				x = max.get( i ).getAxisX( );
				left = max.get( i ).getAxisLeftEdge( );
				right = max.get( i ).getAxisRightEdge( );
			}
			else
			{
				double deltaX1 = x - left;
				double deltaX2 = right - x;

				if ( max.get( i ).getAxisX( ) < x )
				{
					x = max.get( i ).getAxisX( );
					left = x
							- Math.max( deltaX1, x
									- max.get( i ).getAxisLeftEdge( ) );
					right = x
							+ Math.max( deltaX2, max.get( i )
									.getAxisRightEdge( )
									- x );
				}
			}

			scX.setEndPoints( endPoints[0], endPoints[1] );
		}

		// 6. Adjusts horizontal axis position according to the position of max
		// origin axes.
		if ( !Double.isNaN( x ) )
		{
			scX.setEndPoints( endPoints[0], endPoints[1] );
			positions = adjustOrthogonalAxis( IConstants.MAX,
					fHorizontalAxis,
					x,
					left,
					right );
			x = positions[0];
			left = positions[1];
			right = positions[2];
		}

		// 7. Sets axis coordinate and title coordinate of max origin axes.
		for ( int i = 0; i < max.size( ); i++ )
		{
			OneAxis oa = max.get( i ).getVerticalAxis( );
			double iYTitleLocation = oa.getTitlePosition( );
			oa.setTitleCoordinate( ( iYTitleLocation == PlotWithAxes.LEFT ) ? left - 1
					: right + 1 - max.get( i ).getAxisTitleThickness( ) );
			oa.setAxisCoordinate( x );
		}

		// 8. Recomputes the ticks according to new start and end of horizontal
		// axis.
		scX.computeTicks( fPlotWithAxes.getDisplayServer( ),
				fHorizontalAxis.getLabel( ),
				fHorizontalAxis.getLabelPosition( ),
				PlotWithAxes.HORIZONTAL,
				scX.getStart( ),
				scX.getEnd( ),
				false,
				fPlotWithAxes.getAxes( ) );

		scX.resetShifts( );
	}

	/**
	 * Adjusts start and end of orthogonal axis, and returns axis coordinates.
	 * 
	 * @param orthogonalAxis
	 * @param dX
	 * @param dLeftEdge
	 * @param dRightEdge
	 * @return
	 * @throws ChartException
	 */
	public double[] adjustOrthogonalAxis( int iv, OneAxis orthogonalAxis,
			double dX, double dLeftEdge, double dRightEdge )
			throws ChartException
	{
		IDisplayServer ids = fPlotWithAxes.getDisplayServer( );
		AutoScale scX = orthogonalAxis.getScale( );
		AllAxes aax = fPlotWithAxes.getAxes( );
		Label laXAxisLabels = orthogonalAxis.getLabel( );
		int iXLabelLocation = orthogonalAxis.getLabelPosition( );

		double dYAxisThickness = dRightEdge - dLeftEdge;
		double dStart;
		double dEnd;

		double dDeltaX2 = dRightEdge - dX;
		double dDeltaX1 = dX - dLeftEdge;
		if ( iv == IConstants.MIN )
		{
			// Check if X-axis thickness requires a plot width resize at the
			// upper end
			scX.computeAxisStartEndShifts( ids,
					laXAxisLabels,
					PlotWithAxes.HORIZONTAL,
					iXLabelLocation,
					aax );

			boolean startEndChanged = false;

			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				if ( dYAxisThickness > scX.getEndShift( ) )
				{
					// Reduce scX's startpoint to fit the Y-axis on the left
					dEnd = dRightEdge;
					startEndChanged = true;
				}
				else
				{
					dEnd = scX.getEnd( );
				}
				dStart = scX.getStart( );
			}
			else
			{
				if ( dYAxisThickness > scX.getStartShift( ) )
				{
					// Reduce scX's startpoint to fit the Y-axis on the left.
					dStart = dRightEdge;
					startEndChanged = true;
				}
				else
				{
					dStart = scX.getStart( );
				}
				dEnd = scX.getEnd( );
			}

			scX.resetShifts( );

			// Reset the X axis end points by the Y axis location.
			scX.setEndPoints( dStart, dEnd );

			// Loop that auto-resizes Y-axis and re-computes Y-axis labels if
			// overlaps occur.
			boolean considerStartLabel = false;
			boolean considerEndLabel = false;
			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				considerEndLabel = !startEndChanged;
			}
			else
			{
				considerStartLabel = !startEndChanged;
			}

			scX.computeTicks( ids,
					laXAxisLabels,
					iXLabelLocation,
					PlotWithAxes.HORIZONTAL,
					dStart,
					dEnd,
					considerStartLabel,
					considerEndLabel,
					aax );

			if ( !scX.isStepFixed( ) )
			{
				final Object[] oaMinMax = scX.getMinMax( );

				while ( !scX.checkFit( ids, laXAxisLabels, iXLabelLocation ) )
				{
					if ( !scX.zoomOut( ) )
					{
						break;
					}
					scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );

					int tickCount = scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							PlotWithAxes.HORIZONTAL,
							dStart,
							dEnd,
							considerStartLabel,
							considerEndLabel,
							aax );

					if ( scX.getUnit( ) != null
							&& PlotWithAxes.asInteger( scX.getUnit( ) ) == Calendar.YEAR
							&& tickCount <= 3
							|| fPlotWithAxes.isSharedScale( ) )
					{
						break;
					}
				}
			}

			// Move the Y-axis to the left edge of the plot if slack space
			// exists or scale is recomputed
			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				if ( dYAxisThickness < scX.getEndShift( ) )
				{
					dX = scX.getEnd( ) - ( dRightEdge - dX );
				}
			}
			else
			{
				if ( dYAxisThickness < scX.getStartShift( ) )
				{
					dX = scX.getStart( ) - ( dRightEdge - dX );
				}
			}

			// 3. Get final x, xLeft, xRight. Set title coordinate.

			dX -= fPlotWithAxes.getPlotInsets( ).getLeft( );
			dRightEdge = dX + dDeltaX2;
			dLeftEdge = dX - dDeltaX1;
		}
		else if ( iv == IConstants.MAX )
		{
			// Check if X-axis thickness requires a plot height resize at the
			// upper end.
			scX.computeAxisStartEndShifts( ids,
					laXAxisLabels,
					PlotWithAxes.HORIZONTAL,
					iXLabelLocation,
					aax );

			boolean startEndChanged = false;

			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				if ( dYAxisThickness > scX.getStartShift( ) )
				{
					// Reduce scX's endpoint to fit the Y-axis on the right
					dStart = dLeftEdge;
					startEndChanged = true;
				}
				else
				{
					dStart = scX.getStart( );
				}
				dEnd = scX.getEnd( );
			}
			else
			{
				if ( dYAxisThickness > scX.getEndShift( ) )
				{
					// Reduce scX's endpoint to fit the Y-axis on the right
					dEnd = dLeftEdge;
					startEndChanged = true;
				}
				else
				{
					dEnd = scX.getEnd( );
				}
				dStart = scX.getStart( );
			}

			scX.resetShifts( );

			// Loop that auto-resizes Y-axis and re-computes Y-axis labels if
			// overlaps occur
			scX.setEndPoints( dStart, dEnd );

			boolean considerStartLabel = false;
			boolean considerEndLabel = false;
			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				considerStartLabel = !startEndChanged;
			}
			else
			{
				considerEndLabel = !startEndChanged;
			}

			scX.computeTicks( ids,
					laXAxisLabels,
					iXLabelLocation,
					PlotWithAxes.HORIZONTAL,
					dStart,
					dEnd,
					considerStartLabel,
					considerEndLabel,
					aax );

			if ( !scX.isStepFixed( ) )
			{
				final Object[] oaMinMax = scX.getMinMax( );
				while ( !scX.checkFit( ids, laXAxisLabels, iXLabelLocation ) )
				{
					if ( !scX.zoomOut( ) )
					{
						break;
					}
					scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );

					int tickCount = scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							PlotWithAxes.HORIZONTAL,
							dStart,
							dEnd,
							considerStartLabel,
							considerEndLabel,
							aax );

					if ( scX.getUnit( ) != null
							&& PlotWithAxes.asInteger( scX.getUnit( ) ) == Calendar.YEAR
							&& tickCount <= 3
							|| fPlotWithAxes.isSharedScale( ) )
					{
						break;
					}
				}
			}

			// Move the Y-axis to the left edge of the plot if slack space
			// exists or scale is recomputed.
			if ( scX.getDirection( ) == PlotWithAxes.BACKWARD )
			{
				if ( dYAxisThickness < scX.getStartShift( ) )
				{
					dX = scX.getStart( ) - ( dLeftEdge - dX );
				}
			}
			else
			{
				if ( dYAxisThickness < scX.getEndShift( ) )
				{
					dX = scX.getEnd( ) - ( dLeftEdge - dX );
				}
			}

			// 3. Get final x, xLeft, xRight. Set title coordinate.

			dX += fPlotWithAxes.getPlotInsets( ).getRight( );
			dRightEdge = dX + dDeltaX2;
			dLeftEdge = dX - dDeltaX1;
		}

		return new double[]{
				dX, dLeftEdge, dRightEdge
		};
	}
}
