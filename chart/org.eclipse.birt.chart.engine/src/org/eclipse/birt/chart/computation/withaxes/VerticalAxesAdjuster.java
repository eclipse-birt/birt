/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

public class VerticalAxesAdjuster implements IAxisAdjuster {

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
	public VerticalAxesAdjuster(OneAxis[] verticalAxes, OneAxis horizontalAxis, PlotWithAxes plotWithAxes,
			Bounds boPlot) {
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
	@Override
	public void adjust() throws ChartException {
		this.adjust(true);
	}

	/**
	 * Adjusts the axes.
	 *
	 * @param checkAxisLabel whether the axis label should be considered as a factor
	 *                       while computing coordinates and size of axis.
	 * @throws ChartException
	 */
	public void adjust(boolean checkAxisLabel) throws ChartException {
		AutoScale scX = fHorizontalAxis.getScale();

		List<VerticalAxisAdjuster> values = new ArrayList<>();
		List<VerticalAxisAdjuster> min = new ArrayList<>();
		List<VerticalAxisAdjuster> max = new ArrayList<>();

		// Parses all vertical axes and put them into min/max/value origin
		// set.
		for (OneAxis oa : fVerticalAxes) {
			VerticalAxisAdjuster vaa = new VerticalAxisAdjuster(oa, fHorizontalAxis, fPlotWithAxes, fPlotBounds);
			int iv = oa.getIntersectionValue().getType();
			switch (iv) {
			case IConstants.MIN:
				min.add(vaa);
				break;
			case IConstants.MAX:
				max.add(vaa);
				break;
			default:
				values.add(vaa);
			}
		}

		boolean onlyValueOrigin = (values.size() > 0 && min.size() == 0 && max.size() == 0);
		// 1. Adjusts value origin axes, divides left/right/cross cases of axes place.
		double x = Double.NaN;
		double left = Double.NaN;
		double right = Double.NaN;

		double[] endPoints = scX.getEndPoints();

		for (int i = 0; i < values.size(); i++) {
			// Reset start and end of horizontal axis.
			scX.setEndPoints(endPoints[0], endPoints[1]);
			scX.resetShifts();
			// Computes the locations of vertical axis.
			VerticalAxisAdjuster vaa = values.get(i);
			vaa.adjust();

			double locationDelta = AxesAdjuster.getLocationDelta(scX, vaa.getVerticalAxis().getIntersectionValue());
			if (locationDelta <= 0 || vaa.getAxisLeftEdge() <= endPoints[0]) {
				// The axis should be added into Min set and computed in Min
				// set.
				min.add(vaa);
			} else if (vaa.getAxisRightEdge() >= endPoints[1]) {
				// The axis should be added into Max set and computed in Max
				// set.
				max.add(vaa);
			}
		}

		// 2. Adjusts min/vaue origin axes, computes axis location, axis left edge
		// and axis right edge.
		for (int i = 0; i < min.size(); i++) {
			scX.setEndPoints(endPoints[0], endPoints[1]);
			scX.resetShifts();
			VerticalAxisAdjuster vaa = min.get(i);
			vaa.adjust();

			boolean isMinOrigin = (vaa.getVerticalAxis().getIntersectionValue().getType() == IConstants.MIN);
			if (Double.isNaN(x)) {
				if (isMinOrigin) {
					x = vaa.getAxisX();
					left = vaa.getAxisLeftEdge();
					right = vaa.getAxisRightEdge();
				} else {
					// It is Value origin type.
					if (vaa.getLeftWidth() <= scX.getStart()) {
						x = vaa.getAxisX();
					} else {
						x = scX.getStart();
					}
					left = vaa.getAxisLeftEdge() < x ? vaa.getAxisLeftEdge() : x;
					right = x;
				}
			} else {
				double deltaX1 = x - left;
				double deltaX2 = right - x;

				if (isMinOrigin) {
					if (vaa.getAxisX() > x) {
						x = vaa.getAxisX();
					}
					left = x - Math.max(deltaX1, vaa.getAxisX() - vaa.getAxisLeftEdge());
					right = x + Math.max(deltaX2, vaa.getAxisRightEdge() - vaa.getAxisX());
				} else {
					// It is Value origin type.
					if (scX.getEndPoints()[0] > x) {
						x = scX.getEndPoints()[0];
					}
					left = x - Math.max(deltaX1, scX.getEndPoints()[0] - vaa.getAxisLeftEdge());
					right = x + deltaX2;
				}
			}
		}

		// 3. Adjusts horizontal axis positions according to the positions of
		// min origin & value origin axes.
		if (!Double.isNaN(x)) {
			scX.setEndPoints(x, endPoints[1]);
			scX.resetShifts();
			double[] positions = adjustAcrossAxis(onlyValueOrigin ? IConstants.VALUE : IConstants.MIN, fHorizontalAxis,
					x, left, right, checkAxisLabel);
			x = positions[0];
			left = positions[1];
			right = positions[2];
		}

		// 4. Sets min origin axes coordinate and title coordinate according
		// to the axis location, left edge and right edge.
		for (int i = 0; i < min.size(); i++) {
			VerticalAxisAdjuster vaa = min.get(i);
			OneAxis oa = vaa.getVerticalAxis();
			oa.setAxisCoordinate(x);
			oa.setTitleCoordinate(vaa.getTitleCoordinate(x));
		}

		// 5. Adjusts max origin axes, computes x location, axis left edge and
		// axis right edge.
		x = Double.NaN;
		left = Double.NaN;
		right = Double.NaN;
		endPoints = scX.getEndPoints();
		for (int i = 0; i < max.size(); i++) {
			scX.setEndPoints(endPoints[0], endPoints[1]);
			scX.resetShifts();
			VerticalAxisAdjuster vaa = max.get(i);
			vaa.adjust();

			if (Double.isNaN(x)) {
				x = vaa.getAxisX();
				left = vaa.getAxisLeftEdge();
				right = vaa.getAxisRightEdge();
			} else {
				double deltaX1 = x - left;
				double deltaX2 = right - x;

				if (vaa.getAxisX() < x) {
					x = vaa.getAxisX();
					left = x - Math.max(deltaX1, x - vaa.getAxisLeftEdge());
					right = x + Math.max(deltaX2, vaa.getAxisRightEdge() - x);
				}
			}
		}

		// 6. Adjusts horizontal axis position according to the position of max
		// origin axes.
		if (!Double.isNaN(x)) {
			scX.setEndPoints(endPoints[0], endPoints[1]);
			scX.resetShifts();
			double[] positions = adjustAcrossAxis(onlyValueOrigin ? IConstants.VALUE : IConstants.MAX, fHorizontalAxis,
					x, left, right, checkAxisLabel);
			x = positions[0];
			left = positions[1];
			right = positions[2];
		}

		// 7. Sets axis coordinate and title coordinate of max origin axes.
		for (int i = 0; i < max.size(); i++) {
			VerticalAxisAdjuster vaa = max.get(i);
			OneAxis oa = vaa.getVerticalAxis();
			oa.setAxisCoordinate(x);
			oa.setTitleCoordinate(vaa.getTitleCoordinate(x));
		}

		// 8 Sets value origin axes coordinate and title coordinate according
		// to the axis location, left edge and right edge.
		for (int i = 0; i < values.size(); i++) {
			VerticalAxisAdjuster vaa = values.get(i);
			OneAxis oa = vaa.getVerticalAxis();
			double axisCoordinate;
			double locationDelta = AxesAdjuster.getLocationDelta(scX, vaa.getVerticalAxis().getIntersectionValue());
			axisCoordinate = scX.getEndPoints()[0] + locationDelta;
			oa.setAxisCoordinate(axisCoordinate);
			oa.setTitleCoordinate(vaa.getTitleCoordinate(axisCoordinate));
		}

		// 9. Recomputes the ticks according to new start and end of horizontal
		// axis.
		scX.computeTicks(fPlotWithAxes.getDisplayServer(), fHorizontalAxis.getLabel(),
				fHorizontalAxis.getLabelPosition(), PlotWithAxes.HORIZONTAL, scX.getStart(), scX.getEnd(), false,
				fPlotWithAxes.getAxes());

		scX.resetShifts();
	}

	/**
	 * Adjusts start and end of across axis, and returns axis coordinates.
	 *
	 * @param acrossAxis
	 * @param dX
	 * @param dLeftEdge
	 * @param dRightEdge
	 * @param checkAxisLabel whether the axis label should be considered as a factor
	 *                       while computing coordinates and size of axis.
	 * @return
	 * @throws ChartException
	 */
	double[] adjustAcrossAxis(int iv, OneAxis acrossAxis, double dX, double dLeftEdge, double dRightEdge,
			boolean checkAxisLabel) throws ChartException {
		IDisplayServer ids = fPlotWithAxes.getDisplayServer();
		AutoScale scX = acrossAxis.getScale();
		AllAxes aax = fPlotWithAxes.getAxes();
		Label laXAxisLabels = acrossAxis.getLabel();
		int iXLabelLocation = acrossAxis.getLabelPosition();

		double dYAxisThickness = dRightEdge - dLeftEdge;
		double dStart;
		double dEnd;

		double dDeltaX2 = dRightEdge - dX;
		double dDeltaX1 = dX - dLeftEdge;
		if (iv == IConstants.MIN) {
			// Check if X-axis thickness requires a plot width resize at the
			// upper end
			scX.computeAxisStartEndShifts(ids, laXAxisLabels, PlotWithAxes.HORIZONTAL, iXLabelLocation, aax);

			boolean startEndChanged = false;

			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				// Reduce scX's startpoint to fit the Y-axis on the left
				dEnd = dRightEdge;
				dStart = scX.getStart();
				startEndChanged = true;
			} else {
				// Reduce scX's startpoint to fit the Y-axis on the left.
				dStart = dRightEdge;
				dEnd = scX.getEnd();
				startEndChanged = true;
			}

			scX.resetShifts();

			// Reset the X axis end points by the Y axis location.
			scX.setEndPoints(dStart, dEnd);

			// Loop that auto-resizes Y-axis and re-computes Y-axis labels if
			// overlaps occur.
			boolean considerStartLabel = false;
			boolean considerEndLabel = false;
			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				considerEndLabel = !startEndChanged;
			} else {
				considerStartLabel = !startEndChanged;
			}

			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd,
					considerStartLabel && checkAxisLabel, considerEndLabel && checkAxisLabel, aax);

			if (!scX.isStepFixed()) {
				final Object[] oaMinMax = scX.getMinMax();

				while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
					if (!scX.zoomOut()) {
						break;
					}
					scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);

					int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
							dStart, dEnd, considerStartLabel && checkAxisLabel, considerEndLabel && checkAxisLabel,
							aax);

					if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
							&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
						break;
					}
				}
			}

			// Move the Y-axis to the left edge of the plot if slack space
			// exists or scale is recomputed
			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				if (dYAxisThickness < scX.getEndShift()) {
					dX = scX.getEnd() - (dRightEdge - dX);
				}
			} else if (dYAxisThickness < scX.getStartShift()) {
				dX = scX.getStart() - (dRightEdge - dX);
			}

			// 3. Get final x, xLeft, xRight. Set title coordinate.

			dX -= fPlotWithAxes.getPlotInsets().getLeft();
			dRightEdge = dX + dDeltaX2;
			dLeftEdge = dX - dDeltaX1;
		} else if (iv == IConstants.MAX) {
			// Check if X-axis thickness requires a plot height resize at the
			// upper end.
			scX.computeAxisStartEndShifts(ids, laXAxisLabels, PlotWithAxes.HORIZONTAL, iXLabelLocation, aax);

			boolean startEndChanged = false;

			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				if (dYAxisThickness > scX.getStartShift()) {
					// Reduce scX's endpoint to fit the Y-axis on the right
					dStart = dLeftEdge;
					startEndChanged = true;
				} else {
					dStart = scX.getStart();
				}
				dEnd = scX.getEnd();
			} else {
				if (dYAxisThickness > scX.getEndShift()) {
					// Reduce scX's endpoint to fit the Y-axis on the right
					dEnd = dLeftEdge;
					startEndChanged = true;
				} else {
					dEnd = scX.getEnd();
				}
				dStart = scX.getStart();
			}

			scX.resetShifts();

			// Loop that auto-resizes Y-axis and re-computes Y-axis labels if
			// overlaps occur
			scX.setEndPoints(dStart, dEnd);

			boolean considerStartLabel = false;
			boolean considerEndLabel = false;
			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				considerStartLabel = !startEndChanged;
			} else {
				considerEndLabel = !startEndChanged;
			}

			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd,
					considerStartLabel && checkAxisLabel, considerEndLabel && checkAxisLabel, aax);

			if (!scX.isStepFixed()) {
				final Object[] oaMinMax = scX.getMinMax();
				while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
					if (!scX.zoomOut()) {
						break;
					}
					scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);

					int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
							dStart, dEnd, considerStartLabel, considerEndLabel, aax);

					if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
							&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
						break;
					}
				}
			}

			// Move the Y-axis to the left edge of the plot if slack space
			// exists or scale is recomputed.
			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				if (dYAxisThickness < scX.getStartShift()) {
					dX = scX.getStart() - (dLeftEdge - dX);
				}
			} else if (dYAxisThickness < scX.getEndShift()) {
				dX = scX.getEnd() - (dLeftEdge - dX);
			}

			// 3. Get final x, xLeft, xRight. Set title coordinate.

			dX += fPlotWithAxes.getPlotInsets().getRight();
			dRightEdge = dX + dDeltaX2;
			dLeftEdge = dX - dDeltaX1;
		} else {
			// Check if X-axis thickness requires a plot width resize at the
			// upper end
			scX.computeAxisStartEndShifts(ids, laXAxisLabels, PlotWithAxes.HORIZONTAL, iXLabelLocation, aax);

			boolean startEndChanged = false;

			// Reduce scX's startpoint to fit the Y-axis on the left.
			dStart = scX.getStart();
			dEnd = scX.getEnd();
			if (dLeftEdge <= scX.getStart()) {
				// It uses left space, the dStart should be adjusted.
				dStart = dX;
				startEndChanged = true;
			} else if (dX >= scX.getEnd()) {
				// It used right space, the dEnd should be adjusted.
				dEnd = dX;
				startEndChanged = true;
			}
			scX.resetShifts();

			// Reset the X axis end points by the Y axis location.
			scX.setEndPoints(dStart, dEnd);

			// Loop that auto-resizes Y-axis and re-computes Y-axis labels if
			// overlaps occur.
			boolean considerStartLabel = false;
			boolean considerEndLabel = false;
			if (scX.getDirection() == PlotWithAxes.BACKWARD) {
				considerEndLabel = !startEndChanged;
			} else {
				considerStartLabel = !startEndChanged;
			}

			scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd,
					considerStartLabel, considerEndLabel, aax);

			if (!scX.isStepFixed()) {
				final Object[] oaMinMax = scX.getMinMax();

				while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
					if (!scX.zoomOut()) {
						break;
					}
					scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);

					int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
							dStart, dEnd, considerStartLabel, considerEndLabel, aax);

					if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
							&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
						break;
					}
				}
			}

			// Move the Y-axis to the left edge of the plot if slack space
			// exists or scale is recomputed
			if (dYAxisThickness < scX.getStartShift()) {
				dX = scX.getStart() - (dRightEdge - dX);
			}

			// 3. Get final x, xLeft, xRight. Set title coordinate.

			dX -= fPlotWithAxes.getPlotInsets().getLeft();
			dRightEdge = dX + dDeltaX2;
			dLeftEdge = dX - dDeltaX1;
		}

		return new double[] { dX, dLeftEdge, dRightEdge };
	}
}
