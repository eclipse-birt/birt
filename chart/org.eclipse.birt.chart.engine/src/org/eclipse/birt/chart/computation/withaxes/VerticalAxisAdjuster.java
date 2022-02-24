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

import java.util.EnumSet;
import java.util.Map;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.IAxesDecorator;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.util.Calendar;

/**
 * The class is used to adjust the location and scale of one vertical axis.
 * 
 * @since 2.5
 */

public class VerticalAxisAdjuster implements IAxisAdjuster {

	private OneAxis fVerticalAxis;

	private OneAxis fHorizontalAxis;

	private PlotWithAxes fPlotWithAxes;

	private Bounds fPlotBounds;

	private double fAxisX;

	private double fAxisLeftEdge;

	private double fAxisRightEdge;

	// Following fields used for computation.
	private AutoScale scX;

	private AutoScale scY;

	private int iXLabelLocation;

	private int iYLabelLocation;

	private int iYTitleLocation;

	private Label laXAxisLabels;

	private Label laYAxisLabels;

	private Label laYAxisTitle;

	private int iYTickStyle;

	private double dX;

	private double dX1;

	private double dX2;

	private double dAppliedYAxisPlotSpacing;

	private boolean bTicksLeft;

	private boolean bTicksRight;

	private double dWTotal;

	private double dYAxisLabelsThickness;

	// Compute axes decoration thickness, the value sequence is either
	// [left,right] or
	// [top, bottom]
	private double[] dDecorationThickness = { 0, 0 };

	private double dYAxisTitleThickness = 0;

	private IDisplayServer ids;

	private AllAxes aax;

	private IntersectionValue iv;

	private double dLeftWidth;

	private double dRightWidth;

	/**
	 * Constructor.
	 * 
	 * @param verticalAxis
	 * @param horizontalAxis
	 * @param plotWithAxes
	 * @param boPlot
	 */
	public VerticalAxisAdjuster(OneAxis verticalAxis, OneAxis horizontalAxis, PlotWithAxes plotWithAxes,
			Bounds boPlot) {
		fVerticalAxis = verticalAxis;
		fHorizontalAxis = horizontalAxis;
		fPlotWithAxes = plotWithAxes;
		fPlotBounds = boPlot;

	}

	/**
	 * Returns vertical axis.
	 * 
	 * @return
	 */
	OneAxis getVerticalAxis() {
		return fVerticalAxis;
	}

	/**
	 * Returns the x of axis.
	 * 
	 * @return
	 */
	double getAxisX() {
		return fAxisX;
	}

	/**
	 * Returns the axis left edge.
	 * 
	 * @return
	 */
	double getAxisLeftEdge() {
		return fAxisLeftEdge;
	}

	/**
	 * Returns the axis right edge.
	 * 
	 * @return
	 */
	double getAxisRightEdge() {
		return fAxisRightEdge;
	}

	/**
	 * Returns the axis label thickness.
	 * 
	 * @return
	 */
	double getAxisLabelThickness() {

		return dYAxisLabelsThickness;
	}

	/**
	 * Returns the axis title thickness.
	 * 
	 * @return
	 */
	double getAxisTitleThickness() {
		return dYAxisTitleThickness;
	}

	/**
	 * Initializes internal fields values.
	 * 
	 * @throws ChartException
	 */
	private void init() throws ChartException {
		final OneAxis axPH = fHorizontalAxis;
		final OneAxis axPV = fVerticalAxis;
		aax = fPlotWithAxes.aax;
		double dYAxisPlotSpacing = fPlotWithAxes.getVerticalSpacingInPixels();
		ids = fPlotWithAxes.getDisplayServer();
		RunTimeContext rtc = fPlotWithAxes.getRunTimeContext();
		iv = fVerticalAxis.getIntersectionValue();

		scX = axPH.getScale();
		scY = axPV.getScale();

		iXLabelLocation = axPH.getLabelPosition();
		iYLabelLocation = axPV.getLabelPosition();

		iYTitleLocation = axPV.getTitlePosition();

		laXAxisLabels = axPH.getLabel();

		laYAxisLabels = axPV.getLabel();
		laYAxisTitle = axPV.getTitle();

		iYTickStyle = axPV.getCombinedTickStyle();
		final IntersectionValue iv = axPV.getIntersectionValue();

		// Compute the thickness of the axis including axis label bounds and
		// axis-plot spacing.
		bTicksLeft = (iYTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT;
		bTicksRight = (iYTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT;
		// If axis labels should be within axes, do not set default spacing, so
		// value axis labels can be aligned with category axis.
		dAppliedYAxisPlotSpacing = (iv.iType == IConstants.MAX || iv.iType == IConstants.MIN)
				&& !fVerticalAxis.getModelAxis().isLabelWithinAxes() ? dYAxisPlotSpacing : 0;

		// Update Y-axis endpoints due to axis label shifts
		double dStart = scY.getStart(), dEnd = scY.getEnd();
		scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, IConstants.VERTICAL, dStart, dEnd, true, aax);
		if (!scY.isStepFixed()) {
			final Object[] oaMinMax = scY.getMinMax();
			while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
				if (!scY.zoomOut()) {
					break;
				}
				scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
				int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, IConstants.VERTICAL, dStart, dEnd,
						true, aax);
				if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR && tickCount <= 3
						|| fPlotWithAxes.isSharedScale()) {
					break;
				}
			}
		}

		dYAxisLabelsThickness = scY.computeAxisLabelThickness(ids, axPV.getLabel(), IConstants.VERTICAL);

		Series[] sea = fPlotWithAxes.getModel().getSeries(IConstants.ORTHOGONAL);
		Map<Series, LegendItemRenderingHints> seriesRenderingHints = rtc.getSeriesRenderers();
		for (int i = 0; i < sea.length; i++) {
			LegendItemRenderingHints lirh = seriesRenderingHints.get(sea[i]);

			if (lirh != null && lirh.getRenderer() instanceof AxesRenderer) {
				IAxesDecorator iad = ((AxesRenderer) lirh.getRenderer()).getAxesDecorator(axPV);

				if (iad != null) {
					double[] thickness = iad.computeDecorationThickness(ids, axPV);

					if (thickness[0] > dDecorationThickness[0]) {
						dDecorationThickness[0] = thickness[0];
					}
					if (thickness[1] > dDecorationThickness[1]) {
						dDecorationThickness[1] = thickness[1];
					}
				}
			}
		}

		dYAxisTitleThickness = 0;
		computeTitleThickness();

		dX = PlotWithAxes.getLocation(scX, iv);
		dX1 = dX;
		dX2 = dX;
		dWTotal = Math.abs(scX.getStart() - scX.getEnd());

		// handle fixed label thickness #177744
		if (axPV.getModelAxis().isSetLabelSpan()) {
			double dFixedLabelThickness = axPV.getModelAxis().getLabelSpan() * fPlotWithAxes.getPointToPixel();

			// if the fixed label thickness is to greate, it will not take
			// affect.
			if (dFixedLabelThickness < dWTotal - 2 * fPlotWithAxes.getTickSize()) {
				if (dYAxisLabelsThickness + dYAxisTitleThickness > dFixedLabelThickness) {
					axPV.setShowLabels(false);
				}
				if (dYAxisTitleThickness > dFixedLabelThickness) {
					laYAxisTitle.setVisible(false);
					dYAxisTitleThickness = 0;
				}
				dYAxisLabelsThickness = dFixedLabelThickness;
			}
		}
	}

	/**
	 * Computes the axis title thickness.
	 * 
	 * @throws ChartException
	 */
	private void computeTitleThickness() throws ChartException {
		if (laYAxisTitle.isVisible()) {
			final String sPreviousValue = laYAxisTitle.getCaption().getValue();
			laYAxisTitle.getCaption().setValue(fPlotWithAxes.getRunTimeContext().externalizedMessage(sPreviousValue));

			// compute and save the limit of vertical axis title;
			double maxWidth = PlotWithAxes.AXIS_TITLE_PERCENT * fPlotBounds.getWidth();
			double maxHeight = fPlotBounds.getHeight();
			if (ChartUtil.isStudyLayout(fPlotWithAxes.getModel())) {
				// The max height should be the range of orthogonal axis scale
				// if it is study layout.
				maxHeight = Math.abs(scY.getStart() - scY.getEnd());
			}
			LabelLimiter lblLimit = new LabelLimiter(maxWidth, maxHeight, 0);
			lblLimit.computeWrapping(ids, laYAxisTitle);
			lblLimit = lblLimit.limitLabelSize(fPlotWithAxes.getChartComputation(), ids, laYAxisTitle,
					EnumSet.of(LabelLimiter.Option.FIX_HEIGHT));
			fPlotWithAxes.putLabelLimiter(fVerticalAxis.getModelAxis().getTitle(), lblLimit);
			dYAxisTitleThickness = lblLimit.getMaxWidth();
			laYAxisTitle.getCaption().setValue(sPreviousValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.IAxesAdjuster#adjust()
	 */
	public void adjust() throws ChartException {
		init();

		int iv = fVerticalAxis.getIntersectionValue().getType();
		switch (iv) {
		case IConstants.MIN:
			computeXLocationWithMinOrigin();
			break;
		case IConstants.MAX:
			computeXLocationWithMaxOrigin();
			break;
		default:
			computeXLocatoinWithValueOrigin();
		}

	}

	private void computeXLocationWithMinOrigin() throws ChartException, IllegalArgumentException {
		// Computes x, xLeft, xRight.
		double dX1;
		double dX2;
		double dW2Delta = 0;
		if (scX.getDirection() == PlotWithAxes.BACKWARD) {
			// switch if scale is backward.
			dX = PlotWithAxes.getLocation(scX, IntersectionValue.MAX_VALUE);
		}

		dX -= dAppliedYAxisPlotSpacing;
		dX1 = dX;
		dX2 = dX;

		double dTickSize = fPlotWithAxes.getTickSize();

		if (bTicksLeft && dTickSize <= dWTotal) {
			dX1 -= fPlotWithAxes.getTickSize();
			dWTotal -= dTickSize;
		} else {
			// drop the ticks of vertical axis
			dTickSize = 0;
		}

		double dW1 = 0, dW2 = 0;

		if (iYLabelLocation == PlotWithAxes.LEFT) {
			dW1 = Math.max(dYAxisLabelsThickness, dDecorationThickness[0]);
			dW2 = Math.max( // IF LABELS ARE LEFT, THEN RIGHT SPACING IS
					// MAX(RT_TICK_SIZE, HORZ_SPACING)
					bTicksRight ? fPlotWithAxes.getTickSize() : 0, dAppliedYAxisPlotSpacing);
		} else if (iYLabelLocation == PlotWithAxes.RIGHT) {
			dW1 = dDecorationThickness[0];
			// IF LABELS ARE RIGHT, THEN RIGHT SPACING IS
			// MAX(RT_TICK_SIZE+AXIS_LBL_THCKNESS, HORZ_SPACING)
			double dAcutalW2 = Math.max((bTicksRight ? fPlotWithAxes.getTickSize() : 0) + dYAxisLabelsThickness,
					dAppliedYAxisPlotSpacing);
			dW2 = Math.max(bTicksRight ? fPlotWithAxes.getTickSize() : 0, dAppliedYAxisPlotSpacing);
			dW2Delta = dAcutalW2 - dW2;
		}

		if (dW1 + dW2 <= dWTotal) {
			dX1 -= dW1;
			dX2 += dW2;
			dWTotal -= (dW1 + dW2);
		} else {
			// drop the labels of vertical axis
			fVerticalAxis.setShowLabels(false);
		}

		if (iYTitleLocation == PlotWithAxes.LEFT) {
			dX1 -= dYAxisTitleThickness;
		} else if (iYTitleLocation == PlotWithAxes.RIGHT) {
			dW2Delta += dYAxisTitleThickness;
		}

		// Ensure that we don't go behind the left plot block edge.
		double dBlockX = fPlotBounds.getLeft();
		final double dDelta = (dBlockX - dX1);
		if (dX1 != dBlockX) {
			dX1 = dBlockX;
			dX += dDelta;
			dX2 += dDelta;
		}

		fAxisX = dX;
		fAxisLeftEdge = dX1;
		fAxisRightEdge = dX2;

		dLeftWidth = dX - dX1;
		dRightWidth = dX2 - dX + dW2Delta;
	}

	private void computeXLocationWithMaxOrigin() throws ChartException, IllegalArgumentException {
		// Computes x, xLeft, xRight.
		double dX1;
		double dX2;
		double dW1Delta = 0;
		if (scX.getDirection() == PlotWithAxes.BACKWARD) {
			// switch if scale is backward.
			dX = PlotWithAxes.getLocation(scX, IntersectionValue.MIN_VALUE);
		}

		dX += dAppliedYAxisPlotSpacing;
		dWTotal -= dAppliedYAxisPlotSpacing;
		dX1 = dX;
		dX2 = dX;

		if (bTicksRight && fPlotWithAxes.getTickSize() <= dWTotal) {
			dX2 += fPlotWithAxes.getTickSize();
			dWTotal -= fPlotWithAxes.getTickSize();
		}

		double dW1 = 0, dW2 = 0;
		if (iYLabelLocation == PlotWithAxes.RIGHT) {
			dW1 = Math.max((bTicksLeft ? fPlotWithAxes.getTickSize() : 0) + dDecorationThickness[0],
					dAppliedYAxisPlotSpacing);
			dW2 = Math.max(dYAxisLabelsThickness, dDecorationThickness[1]);
		} else if (iYLabelLocation == PlotWithAxes.LEFT) {
			double dActualW1 = Math.max((bTicksLeft ? fPlotWithAxes.getTickSize() : 0)
					+ Math.max(dYAxisLabelsThickness, dDecorationThickness[0]), dAppliedYAxisPlotSpacing);
			dW1 = Math.max(bTicksLeft ? fPlotWithAxes.getTickSize() : 0, dAppliedYAxisPlotSpacing);
			dW1Delta = dActualW1 - dW1;
			dW2 = dDecorationThickness[1];
		}

		if (dW1 + dW2 <= dWTotal) {
			dX1 -= dW1;
			dX2 += dW2;
			dWTotal -= (dW1 + dW2);
		} else {
			// drop the vertical axis labels
			fVerticalAxis.setShowLabels(false);
		}

		if (iYTitleLocation == PlotWithAxes.RIGHT) {
			dX2 += dYAxisTitleThickness;
		} else if (iYTitleLocation == PlotWithAxes.LEFT) {
			dW1Delta += dYAxisTitleThickness;
		}

		// Ensure that we don't do ahead of the right plot block edge.
		double dBlockX = fPlotBounds.getLeft();
		double dBlockWidth = fPlotBounds.getWidth();
		final double dDelta = dX2 - (dBlockX + dBlockWidth);
		if (dX2 != (dBlockX + dBlockWidth)) {
			dX2 = dBlockX + dBlockWidth;
			dX -= dDelta;
			dX1 -= dDelta;
		}

		fAxisX = dX;
		fAxisLeftEdge = dX1;
		fAxisRightEdge = dX2;

		dLeftWidth = dX - dX1 + dW1Delta;
		dRightWidth = dX2 - dX1;
	}

	private void computeXLocatoinWithValueOrigin() throws ChartException {
		double dStart;
		double dEnd;
		double dDeltaX1 = 0, dDeltaX2 = 0;
		double dBlockX = fPlotBounds.getLeft();
		double dBlockWidth = fPlotBounds.getWidth();

		if (iYTitleLocation == PlotWithAxes.RIGHT) {
			dX2 += dYAxisTitleThickness;
		} else if (iYTitleLocation == PlotWithAxes.LEFT) {
			dX1 -= dYAxisTitleThickness;
		}

		double dW1 = 0, dW2 = 0;
		if (iYLabelLocation == PlotWithAxes.LEFT) {
			dW1 = (bTicksLeft ? fPlotWithAxes.getTickSize() : 0)
					+ Math.max(dYAxisLabelsThickness, dDecorationThickness[0]);
			dW2 = (bTicksRight ? fPlotWithAxes.getTickSize() : 0);

			if (dW1 + dW2 <= dWTotal) {
				dX1 -= dW1;
				dX2 += dW2;
				dWTotal -= (dW1 + dW2);
			} else {
				fVerticalAxis.setShowLabels(false);
			}

			dDeltaX1 = dX - dX1;
			dDeltaX2 = dX2 - dX;

			// Check if left edge of Y-axis band goes behind the plot left edge
			if (dX1 < dBlockX) {
				final Object[] oaMinMax = scX.getMinMax();
				boolean bForceBreak = false;

				// A loop that iteratively attempts to adjust the left edge
				// of the Y-axis labels with the left edge of the plot
				// and/or ensure that the start point of the X-axis scale is
				// Suitably positioned
				do {
					// Cancel out the endpoint label shift computations from
					// the X-axis
					scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
					scX.resetShifts();

					// Apply the axis reduction formula W.R.T. X-axis
					// startpoint
					double[] da = scX.getEndPoints();
					double dT_RI = dBlockX - dX1; // Threshold requested
					// intersection

					if (scX.getDirection() == PlotWithAxes.BACKWARD) {
						double dAMin_AMax = da[0] - da[1];
						double dAMax_RI = Math.abs(da[0] - dX);
						double dDelta = (dT_RI / dAMax_RI) * dAMin_AMax;
						dEnd = da[1] + dDelta;
						dStart = da[0];

						if (dEnd < dBlockX) {
							dEnd = dBlockX;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[1] - da[0];
						double dAMax_RI = Math.abs(da[1] - dX);
						double dDelta = (dT_RI / dAMax_RI) * dAMin_AMax;
						dStart = da[0] + dDelta;
						dEnd = da[1];

						if (dStart < dBlockX) {
							dStart = dBlockX;
							bForceBreak = true;
						}
					}

					// Loop that auto-resizes Y-axis and re-computes Y-axis
					// labels if overlaps occur
					scX.setEndPoints(dStart, dEnd);
					scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd, true,
							aax);
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							bForceBreak = true;
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
								dStart, dEnd, true, aax);
						if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							bForceBreak = true;
							break;
						}
					}
					dX = PlotWithAxes.getLocation(scX, iv);
					dX1 = dX - dDeltaX1;// Re-calculate X-axis band left edge
				} while (Math.abs(dX1 - dBlockX) > 1 && !bForceBreak);
			} else {
				// Loop that auto-resizes Y-axis and re-computes Y-axis labels
				// if overlaps occur
				dStart = scX.getStart();
				dEnd = scX.getEnd();
				scX.setEndPoints(dStart, dEnd);
				scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd, true, aax);
				if (!scX.isStepFixed()) {
					final Object[] oaMinMax = scX.getMinMax();
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
								dStart, dEnd, true, aax);
						if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							break;
						}
					}
				}
				dX = PlotWithAxes.getLocation(scX, iv);
			}
			dX1 = dX - dDeltaX1;
			dX2 = dX + dDeltaX2;
		} else if (iYLabelLocation == PlotWithAxes.RIGHT) {
			dW1 = (bTicksLeft ? fPlotWithAxes.getTickSize() : 0);
			dW2 = (bTicksRight ? fPlotWithAxes.getTickSize() : 0)
					+ Math.max(dYAxisLabelsThickness, dDecorationThickness[1]);

			if (dW1 + dW2 <= dWTotal) {
				dX1 -= dW1;
				dX2 += dW2;
				dWTotal -= (dW1 + dW2);
			}

			dDeltaX1 = dX - dX1;
			dDeltaX2 = dX2 - dX;

			// Check if right edge of Y-axis band goes behind the plot right
			// edge
			if (dX2 > dBlockX + dBlockWidth) {
				final Object[] oaMinMax = scX.getMinMax();
				boolean bForceBreak = false;

				// A loop that iteratively attempts to adjust the right edge
				// of the Y-axis labels with the right edge of the plot
				// and/or
				// Ensure that the start point of the X-axis scale is
				// Suitably positioned

				do {
					// Cancel out the endpoint label shift computations from
					// the X-axis.
					scX.setEndPoints(scX.getStart() - scX.getStartShift(), scX.getEnd() + scX.getEndShift()); // RESTORE
					scX.resetShifts();

					// Apply the axis reduction formula W.R.T. X-axis
					// endpoint
					double[] da = scX.getEndPoints();
					double dT_RI = dX2 - (dBlockX + dBlockWidth); // Threshold
					// requested
					// intersection

					if (scX.getDirection() == PlotWithAxes.BACKWARD) {
						double dAMin_AMax = da[0] - da[1];
						double dAMin_RI = Math.abs(dX - da[1]);
						double dDelta = Math.abs(dT_RI / dAMin_RI) * dAMin_AMax;
						dStart = da[0] - dDelta;
						dEnd = da[1];

						if (dStart > dBlockX + dBlockWidth) {
							dStart = dBlockX + dBlockWidth;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[1] - da[0];
						double dAMin_RI = Math.abs(dX - da[0]);
						double dDelta = (dT_RI / dAMin_RI) * dAMin_AMax;
						dEnd = da[1] - dDelta;
						dStart = da[0];

						if (dEnd > dBlockX + dBlockWidth) {
							dEnd = dBlockX + dBlockWidth;
							bForceBreak = true;
						}
					}

					// Loop that auto-resizes Y-axis and re-computes Y-axis
					// labels if overlaps occur
					scX.setEndPoints(dStart, dEnd);
					scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd, true,
							aax);
					if (!scX.isStepFixed()) {
						while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
							if (!scX.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation,
									PlotWithAxes.HORIZONTAL, dStart, dEnd, true, aax);
							if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
									&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}
					dX = PlotWithAxes.getLocation(scX, iv);
					dX2 = dX + dDeltaX2; // Re-calculate X-axis band right edge
				} while (Math.abs(dX2 - (dBlockX + dBlockWidth)) > 1 && !bForceBreak);
			} else {
				// Loop that auto-resizes Y-axis and re-computes Y-axis
				// labels if overlaps occur
				double delta = dBlockX - dX1;
				if (dX1 < dBlockX && (dX2 + delta) < (dBlockX + dBlockWidth)) {
					dStart = scX.getStart() + delta;
				} else {
					dStart = scX.getStart();
				}
				dEnd = scX.getEnd();
				scX.setEndPoints(dStart, dEnd);
				scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd, true, aax);
				if (!scX.isStepFixed()) {
					final Object[] oaMinMax = scX.getMinMax();
					while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
						if (!scX.zoomOut()) {
							break;
						}
						scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
						int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL,
								dStart, dEnd, true, aax);
						if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR
								&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
							break;
						}
					}
				}
				dX = PlotWithAxes.getLocation(scX, iv);
			}
			dX2 = dX + dDeltaX2;
			dX1 = dX - dDeltaX1;
		}

		if (dX1 < fPlotBounds.getLeft()) {
			final double dDelta = (dBlockX - dX1);
			dX1 = dBlockX;
			dX += dDelta;
			dX2 += dDelta;
		}

		fAxisX = dX;
		fAxisLeftEdge = dX1;
		fAxisRightEdge = dX2;

		dLeftWidth = dX - dX1;
		dRightWidth = dX2 - dX;
	}

	double getLeftWidth() {
		return dLeftWidth;
	}

	double getRightWidth() {
		return dRightWidth;
	}

	/**
	 * Computes the title coordinate .
	 * 
	 * @param axisCoordinate
	 * @return
	 */
	double getTitleCoordinate(double axisCoordinate) {
		return (iYTitleLocation == PlotWithAxes.LEFT) ? axisCoordinate - getLeftWidth() - 1
				: axisCoordinate + (bTicksRight ? fPlotWithAxes.getTickSize() : 0)
						+ ((iYLabelLocation == PlotWithAxes.LEFT) ? 0 : getAxisLabelThickness()) + 1;
	}
}
