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
 * The class is used to adjust horizontal axes's location and scale.
 *
 * @since 2.5
 */

public class HorizontalAxesAdjuster implements IAxisAdjuster {

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
	public HorizontalAxesAdjuster(OneAxis[] horizontalAxes, OneAxis verticalAxis, PlotWithAxes plotWithAxes,
			Bounds boPlot) {
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
	@Override
	public void adjust() throws ChartException {
		this.adjust(true);
	}

	/**
	 * Adjust the axes.
	 *
	 * @param checkAxisLabel whether the axis label should be considered as a factor
	 *                       while computing coordinates and size of axis.
	 * @throws ChartException
	 */
	public void adjust(boolean checkAxisLabel) throws ChartException {
		AutoScale scY = fVerticalAxis.getScale();
		boolean isForward = (scY.getDirection() == PlotWithAxes.FORWARD);

		List<HorizontalAxisAdjuster> values = new ArrayList<>();
		List<HorizontalAxisAdjuster> min = new ArrayList<>();
		List<HorizontalAxisAdjuster> max = new ArrayList<>();

		// Parses all horizontal axes and put them into min/max/value origin
		// set.
		for (OneAxis oa : fHorizontalAxes) {
			HorizontalAxisAdjuster vaa = new HorizontalAxisAdjuster(oa, fVerticalAxis, fPlotWithAxes, fPlotBounds);
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
		// 1. Adjusts value origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		double y = Double.NaN;
		double bottom = Double.NaN;
		double top = Double.NaN;

		double[] endPoints = scY.getEndPoints();
		scY.resetShifts();

		for (int i = 0; i < values.size(); i++) {
			scY.setEndPoints(endPoints[0], endPoints[1]);
			HorizontalAxisAdjuster haa = values.get(i);
			haa.adjust();

			double locationDelta = AxesAdjuster.getLocationDelta(scY, haa.getHorizontalAxis().getIntersectionValue());
			if (locationDelta >= 0 || haa.getAxisBottomEdge() >= endPoints[0]) {
				min.add(haa);
			} else if (haa.getAxisTopEdge() <= endPoints[1]) {
				max.add(haa);
			}
		}

		// 2. Adjusts min origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		scY.resetShifts();
		for (int i = 0; i < min.size(); i++) {
			scY.setEndPoints(endPoints[0], endPoints[1]);
			HorizontalAxisAdjuster haa = min.get(i);
			haa.adjust();

			boolean isMinOrigin = (haa.getHorizontalAxis().getIntersectionValue().getType() == IConstants.MIN);
			if (Double.isNaN(y)) {
				if (isMinOrigin) {
					y = haa.getAxisY();
					bottom = haa.getAxisBottomEdge();
					top = haa.getAxisTopEdge();
				} else {
					if (haa.getAxisBottomEdge() >= scY.getStart()) {
						y = haa.getAxisY();
					} else {
						y = scY.getStart();
					}
					bottom = haa.getAxisBottomEdge() > y ? haa.getAxisBottomEdge() : y;
					top = y;
				}
			} else {
				double deltaY1 = bottom - y;
				double deltaY2 = y - top;

				if (isMinOrigin) {
					if (haa.getAxisY() < y) {
						y = haa.getAxisY();
					}
					bottom = y + Math.max(deltaY1, haa.getAxisBottomEdge() - haa.getAxisY());
					top = y - Math.max(deltaY2, haa.getAxisY() - haa.getAxisTopEdge());
				} else {
					if (scY.getEndPoints()[0] < haa.getAxisY()) {
						y = scY.getEndPoints()[0];
					}
					bottom = y + Math.max(deltaY1, haa.getAxisBottomEdge() - scY.getEndPoints()[0]);
					top = y - deltaY2;
				}
			}
		}

		// 3. Adjusts vertical axis positions according to the positions of
		// min origin & value origin axes.
		if (!Double.isNaN(y)) {
			scY.setEndPoints(endPoints[0], endPoints[1]);
			scY.resetShifts();
			double[] positions = adjustAcrossAxis(onlyValueOrigin ? IConstants.VALUE : IConstants.MIN, fVerticalAxis, y,
					bottom, top, checkAxisLabel);
			y = positions[0];
			top = positions[1];
			bottom = positions[2];
		}

		// 4 Set min origin axis coordinate and title coordinate according to
		// the axis y location, axis top edge and axis bottom edge.
		for (int i = 0; i < min.size(); i++) {
			HorizontalAxisAdjuster haa = min.get(i);
			OneAxis oa = haa.getHorizontalAxis();
			oa.setAxisCoordinate(y);
			oa.setTitleCoordinate(haa.getTitleCoordinate(y));
		}

		// 5. Adjusts max origin axes, computes the axis y location, axis top
		// edge and axis bottom edge.
		y = Double.NaN;
		bottom = Double.NaN;
		top = Double.NaN;
		endPoints = scY.getEndPoints();
		scY.resetShifts();
		for (int i = 0; i < max.size(); i++) {
			scY.setEndPoints(endPoints[0], endPoints[1]);
			HorizontalAxisAdjuster haa = max.get(i);
			haa.adjust();

			if (Double.isNaN(y)) {
				y = haa.getAxisY();
				bottom = haa.getAxisBottomEdge();
				top = haa.getAxisTopEdge();
			} else {
				double deltaY1 = bottom - y;
				double deltaY2 = y - top;

				if (haa.getAxisY() > y) {
					y = haa.getAxisY();
					bottom = y + Math.max(deltaY1, haa.getAxisBottomEdge() - y);
					top = y - Math.max(deltaY2, y - haa.getAxisTopEdge());
				}
			}

		}

		// 6. Adjusts horizontal axis position according to the position of max
		// origin axes.
		if (!Double.isNaN(y)) {
			scY.setEndPoints(endPoints[0], endPoints[1]);
			double[] positions = adjustAcrossAxis(onlyValueOrigin ? IConstants.VALUE : IConstants.MAX, fVerticalAxis, y,
					bottom, top, checkAxisLabel);
			y = positions[0];
			top = positions[1];
			bottom = positions[2];
		}

		// 7. Sets max origin axes coordinate and title coordinate according the
		// axis y location, axis top edge and axis bottom edge.
		for (int i = 0; i < max.size(); i++) {
			HorizontalAxisAdjuster haa = max.get(i);
			OneAxis oa = haa.getHorizontalAxis();
			oa.setAxisCoordinate(y);
			oa.setTitleCoordinate(haa.getTitleCoordinate(y));
		}

		// 8. Set value origin axis coordinate and title coordinate according
		// to the axis y location, axis top edge and axis bottom edge.
		for (int i = 0; i < values.size(); i++) {
			HorizontalAxisAdjuster haa = values.get(i);
			OneAxis oa = haa.getHorizontalAxis();
			double axisCoordinate = 0;
			double locationDelta = AxesAdjuster.getLocationDelta(scY, haa.getHorizontalAxis().getIntersectionValue());
			if (!isForward) {
				axisCoordinate = scY.getEndPoints()[0] + locationDelta;
			} else {
				axisCoordinate = scY.getEndPoints()[1] + locationDelta;
			}

			oa.setAxisCoordinate(axisCoordinate);
			oa.setTitleCoordinate(haa.getTitleCoordinate(axisCoordinate));
		}

		// 9. Recomputes ticks according to new start and end of vertical axis.
		scY.computeTicks(fPlotWithAxes.getDisplayServer(), fVerticalAxis.getLabel(), fVerticalAxis.getLabelPosition(),
				PlotWithAxes.VERTICAL, scY.getStart(), scY.getEnd(), false, fPlotWithAxes.getAxes());
		scY.resetShifts();
	}

	/**
	 * Adjusts start and end of across axis, and returns axis coordinates.
	 *
	 * @param iv
	 * @param orthogonalAxis
	 * @param dY
	 * @param dBottom
	 * @param dTop
	 * @param checkAxisLabel whether the axis label should be considered as a factor
	 *                       while computing coordinates and size of axis.
	 * @return
	 * @throws ChartException
	 */
	double[] adjustAcrossAxis(int iv, OneAxis orthogonalAxis, double dY, double dBottom, double dTop,
			boolean checkAxisLabel) throws ChartException {
		IDisplayServer ids = fPlotWithAxes.getDisplayServer();
		AutoScale scY = orthogonalAxis.getScale();
		AllAxes aax = fPlotWithAxes.getAxes();
		Label laYAxisLabels = orthogonalAxis.getLabel();
		int iYLabelLocation = orthogonalAxis.getLabelPosition();
		boolean bForwardScale = scY.getDirection() == PlotWithAxes.FORWARD;

		double dXAxisThickness = dBottom - dTop;

		double dDeltaY1 = dY - dTop;
		double dDeltaY2 = dBottom - dY;

		boolean startEndChanged = false;
		if ((bForwardScale && iv == IConstants.MIN) || (!bForwardScale && iv == IConstants.MAX)) {
			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// UPPER END
			if ((bForwardScale && dXAxisThickness > scY.getStartShift())
					|| (!bForwardScale && dXAxisThickness > scY.getEndShift())) {
				// REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = scY.getStart();
				double dEnd = dBottom - scY.getEndShift();

				if (bForwardScale) {
					dStart = dBottom - scY.getStartShift();
					dEnd = scY.getEnd();
				}

				scY.resetShifts();

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints(dStart, dEnd);
				scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL, dStart, dEnd,
						startEndChanged && checkAxisLabel, aax);
				if (!scY.isStepFixed()) {
					final Object[] oaMinMax = scY.getMinMax();
					while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
						if (!scY.zoomOut()) {
							break;
						}
						scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL,
								dStart, dEnd, startEndChanged && checkAxisLabel, aax);
						if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							break;
						}
					}
				}
			}

			// 3. Get final y, yAbove, yBelow, set title cooidinate.
			dY -= fPlotWithAxes.getPlotInsets().getTop();
			dTop = dY - dDeltaY1;
			dBottom = dY + dDeltaY2;
		} else if ((bForwardScale && iv == IConstants.MAX) || (!bForwardScale && iv == IConstants.MIN)) {
			// 2. Compute and set endpoints of orthogonal axis.
			// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// LOWER END
			if ((bForwardScale && dXAxisThickness > scY.getEndShift())
					|| (!bForwardScale && dXAxisThickness > scY.getStartShift())) {
				// REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = dTop + scY.getStartShift();
				double dEnd = scY.getEnd();

				if (bForwardScale) {
					dStart = scY.getStart();
					dEnd = dTop + scY.getEndShift();
				}
				scY.resetShifts();

				if (dStart < dEnd + 1) {
					dStart = dEnd + 1;
					startEndChanged = true;
				}

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints(dStart, dEnd);
				scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL, dStart, dEnd,
						startEndChanged && checkAxisLabel, false, aax);
				if (!scY.isStepFixed()) {
					final Object[] oaMinMax = scY.getMinMax();
					while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
						if (!scY.zoomOut()) {
							break;
						}
						double dOldStep = ((Number) scY.getStep()).doubleValue();
						scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL,
								dStart, dEnd, startEndChanged && checkAxisLabel, false, aax);
						double dNewStep = ((Number) scY.getStep()).doubleValue();
						if (dNewStep < dOldStep) {
							break;
						}

						if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							break;
						}
					}
				}
			}

			// 3. Get final y, yAbove, yBelow. set title coordinate.
			// MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
			dY += fPlotWithAxes.getPlotInsets().getBottom();
			dTop = dY - dDeltaY1;
			dBottom = dY + dDeltaY2;
		} else {
			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// LOWER END
			if ((bForwardScale && dXAxisThickness > scY.getEndShift())
					|| (!bForwardScale && dXAxisThickness > scY.getStartShift())) {
				// REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = scY.getStart();
				double dEnd = scY.getEnd();
				if (dBottom >= scY.getStart()) {
					// It uses the bottom space, the dStart should be adjusted.
					dStart = dY;
					startEndChanged = true;
				} else if (dY <= scY.getEnd()) {
					// It uses the top space, the dEnd should be adjusted.
					dEnd = dY;
					startEndChanged = true;
				}

				if (bForwardScale) {
					dStart = scY.getStart();
					dEnd = dTop + scY.getEndShift();
					startEndChanged = true;
				}
				scY.resetShifts();

				if (dStart < dEnd + 1) {
					dStart = dEnd + 1;
					startEndChanged = true;
				}

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints(dStart, dEnd);
				scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL, dStart, dEnd,
						startEndChanged, false, aax);
				if (!scY.isStepFixed()) {
					final Object[] oaMinMax = scY.getMinMax();
					while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
						if (!scY.zoomOut()) {
							break;
						}
						double dOldStep = ((Number) scY.getStep()).doubleValue();
						scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL,
								dStart, dEnd, startEndChanged, false, aax);
						double dNewStep = ((Number) scY.getStep()).doubleValue();
						if (dNewStep < dOldStep) {
							break;
						}

						if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							break;
						}
					}
				}
			}

			// 3. Get final y, yAbove, yBelow. set title coordinate.
			// MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
			dY += fPlotWithAxes.getPlotInsets().getBottom();
			dTop = dY - dDeltaY1;
			dBottom = dY + dDeltaY2;
		}

		return new double[] { dY, dTop, dBottom };
	}
}
