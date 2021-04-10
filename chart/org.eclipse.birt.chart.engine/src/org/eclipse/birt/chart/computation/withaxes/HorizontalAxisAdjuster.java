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
 * The class is used to adjust the location and scale of one horizontal axis.
 * 
 * @since 2.5
 */

public class HorizontalAxisAdjuster implements IAxisAdjuster {

	private OneAxis fVerticalAxis;

	private OneAxis fHorizontalAxis;

	private PlotWithAxes fPlotWithAxes;

	private Bounds fPlotBounds;

	private double fAxisY;

	private double fAxisBottom;

	private double fAxisTop;

	// Fields for internal computing.
	private int iXLabelLocation;

	private int iYLabelLocation;

	private int iXTitleLocation;

	private Label laYAxisLabels;

	private double dXAxisLabelsThickness;

	// double dXAxisThickness = 0; // REUSE VARIABLE

	// Compute axes decoration thickness, the value sequence is either
	// [left,right] or
	// [top, bottom]
	private double[] dDecorationThickness = { 0, 0 };

	private double dXAxisTitleThickness = 0;

	private boolean bTicksAbove;

	private boolean bTicksBelow;

	private double dAppliedXAxisPlotSpacing;

	private double dHTotal;

	private double dTopHeight;

	private double dBottomHeight;

	public HorizontalAxisAdjuster(OneAxis horizontalAxis, OneAxis verticalAxis, PlotWithAxes plotWithAxes,
			Bounds boPlot) {
		fVerticalAxis = verticalAxis;
		fHorizontalAxis = horizontalAxis;
		fPlotWithAxes = plotWithAxes;
		fPlotBounds = boPlot;
	}

	double getAxisY() {
		return fAxisY;
	}

	double getAxisTopEdge() {
		return fAxisTop;
	}

	double getAxisBottomEdge() {
		return fAxisBottom;
	}

	public void adjust() throws ChartException {
		init();

		boolean bForwardScale = (fHorizontalAxis.getScale().getDirection() == IConstants.AUTO
				|| fHorizontalAxis.getScale().getDirection() == PlotWithAxes.FORWARD);
		IntersectionValue iv = fHorizontalAxis.getIntersectionValue();
		if ((bForwardScale && iv.iType == IConstants.MIN) || (!bForwardScale && iv.iType == IConstants.MAX)) {
			computeYLocationWithMinOrigin();
		} else if ((bForwardScale && iv.iType == IConstants.MAX) || (!bForwardScale && iv.iType == IConstants.MIN)) {
			computeYLocationWithMaxOrigin();
		} else {
			computeYLocatoinWithValueOrigin();
		}
	}

	private void init() throws ChartException {
		IDisplayServer ids = fPlotWithAxes.getDisplayServer();
		RunTimeContext rtc = fPlotWithAxes.getRunTimeContext();
		AllAxes aax = fPlotWithAxes.aax;

		final OneAxis axPH = fHorizontalAxis;
		final OneAxis axPV = fVerticalAxis;
		final AutoScale scX = axPH.getScale();
		final AutoScale scY = axPV.getScale();
		iXLabelLocation = axPH.getLabelPosition();
		iYLabelLocation = axPV.getLabelPosition();
		iXTitleLocation = axPH.getTitlePosition();

		final Label laXAxisTitle = axPH.getTitle();
		final Label laXAxisLabels = axPH.getLabel();
		laYAxisLabels = axPV.getLabel();
		final int iXTickStyle = axPH.getCombinedTickStyle();
		final IntersectionValue iv = axPH.getIntersectionValue();

		// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
		// AXIS-PLOT SPACING
		dXAxisLabelsThickness = scX.computeAxisLabelThickness(ids, axPH.getLabel(), PlotWithAxes.HORIZONTAL);

		// Update Y-axis endpoints due to axis label shifts
		double dStart = scX.getStart(), dEnd = scX.getEnd();
		scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart, dEnd, true, aax);
		if (!scX.isStepFixed()) {
			final Object[] oaMinMax = scX.getMinMax();
			while (!scX.checkFit(ids, laXAxisLabels, iXLabelLocation)) {
				if (!scX.zoomOut()) {
					break;
				}
				scX.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
				int tickCount = scX.computeTicks(ids, laXAxisLabels, iXLabelLocation, PlotWithAxes.HORIZONTAL, dStart,
						dEnd, true, aax);
				if (scX.getUnit() != null && PlotWithAxes.asInteger(scX.getUnit()) == Calendar.YEAR && tickCount <= 3
						|| fPlotWithAxes.isSharedScale()) {
					break;
				}
			}
		}

		Series[] sea = fPlotWithAxes.getModel().getSeries(IConstants.ORTHOGONAL);
		Map<Series, LegendItemRenderingHints> seriesRenderingHints = rtc.getSeriesRenderers();
		for (int i = 0; i < sea.length; i++) {
			LegendItemRenderingHints lirh = seriesRenderingHints.get(sea[i]);

			if (lirh != null && lirh.getRenderer() instanceof AxesRenderer) {
				IAxesDecorator iad = ((AxesRenderer) lirh.getRenderer()).getAxesDecorator(axPH);

				if (iad != null) {
					double[] thickness = iad.computeDecorationThickness(ids, axPH);

					if (thickness[0] > dDecorationThickness[0]) {
						dDecorationThickness[0] = thickness[0];
					}
					if (thickness[1] > dDecorationThickness[1]) {
						dDecorationThickness[1] = thickness[1];
					}
				}
			}
		}

		if (laXAxisTitle.isVisible()) {
			final String sPreviousValue = laXAxisTitle.getCaption().getValue();
			laXAxisTitle.getCaption().setValue(rtc.externalizedMessage(sPreviousValue)); // EXTERNALIZE

			double maxWidth = Math.abs(scX.getEnd() - scX.getStart());
			double maxHeight = PlotWithAxes.AXIS_TITLE_PERCENT * fPlotBounds.getHeight();
			LabelLimiter lblLimit = new LabelLimiter(maxWidth, maxHeight, 0);
			lblLimit.computeWrapping(ids, laXAxisTitle);
			lblLimit = lblLimit.limitLabelSize(fPlotWithAxes.getChartComputation(), ids, laXAxisTitle,
					EnumSet.of(LabelLimiter.Option.FIX_WIDTH));
			fPlotWithAxes.putLabelLimiter(axPH.getModelAxis().getTitle(), lblLimit);
			dXAxisTitleThickness = lblLimit.getMaxHeight();
			laXAxisTitle.getCaption().setValue(sPreviousValue);
		}

		dHTotal = Math.abs(scY.getStart() - scY.getEnd());

		// handle fixed label thickness #177744
		if (axPH.getModelAxis().isSetLabelSpan()) {
			double dFixedLabelThickness = axPH.getModelAxis().getLabelSpan() * fPlotWithAxes.getPointToPixel();

			// if the fixed label thickness is to great, it will not take
			// affect.
			if (dFixedLabelThickness < dHTotal - 2 * fPlotWithAxes.getTickSize()) {
				if (dXAxisTitleThickness + dXAxisLabelsThickness > dFixedLabelThickness) {
					axPH.setShowLabels(false);
				}
				if (dXAxisTitleThickness > dFixedLabelThickness) {
					laXAxisTitle.setVisible(false);
					dXAxisLabelsThickness = 0;
				}
				dXAxisLabelsThickness = dFixedLabelThickness;
			}
		}

		bTicksAbove = (iXTickStyle & PlotWithAxes.TICK_ABOVE) == PlotWithAxes.TICK_ABOVE;
		bTicksBelow = (iXTickStyle & PlotWithAxes.TICK_BELOW) == PlotWithAxes.TICK_BELOW;
		dAppliedXAxisPlotSpacing = (iv.iType == IConstants.MAX || iv.iType == IConstants.MIN)
				? fPlotWithAxes.getHorizontalSpacingInPixels()
				: 0;
	}

	private void computeYLocationWithMinOrigin() throws ChartException, IllegalArgumentException {
		// 1. Compute y, yAbove, yBelow
		IntersectionValue iv = fHorizontalAxis.getIntersectionValue();
		double dY = PlotWithAxes.getLocation(fVerticalAxis.getScale(), iv);
		double dY1 = dY;
		double dY2 = dY;
		double dY1Delta = 0;
		// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'

		dY -= dAppliedXAxisPlotSpacing;
		dHTotal -= dAppliedXAxisPlotSpacing;
		dY1 = dY;
		dY2 = dY;

		double dTickSize = fPlotWithAxes.getTickSize();

		if (bTicksAbove && dTickSize <= dHTotal) {
			dY1 -= dTickSize;
			dHTotal -= dTickSize;
		} else {
			// axPH.setShowTicks( false );
			dTickSize = 0;
		}

		double dH1 = 0;
		double dH2 = 0;

		if (iXLabelLocation == PlotWithAxes.ABOVE) {
			double delta = Math.max(dXAxisLabelsThickness, dDecorationThickness[0]);
			dY1Delta = delta - dH1;

			dH2 = Math.max(bTicksBelow ? fPlotWithAxes.getTickSize() : 0, dAppliedXAxisPlotSpacing);
		} else if (iXLabelLocation == PlotWithAxes.BELOW) {
			dH1 = dDecorationThickness[0];
			dH2 += Math.max((bTicksBelow ? fPlotWithAxes.getTickSize() : 0) + dXAxisLabelsThickness,
					dAppliedXAxisPlotSpacing);

		}

		if (dH1 + dH2 <= dHTotal) {
			dY1 -= dH1;
			dY2 += dH2;
			dHTotal -= (dH1 + dH2);
		} else {
			fHorizontalAxis.setShowLabels(false);
		}

		if (iXTitleLocation == PlotWithAxes.ABOVE) {
			dY1Delta += dXAxisTitleThickness;
		} else if (iXTitleLocation == PlotWithAxes.BELOW) {
			dY2 += dXAxisTitleThickness;
		}

		// ENSURE THAT WE DON'T GO ABOVE THE UPPER PLOT BLOCK EDGE
		double dBlockY = fPlotBounds.getTop() + fPlotBounds.getHeight();
		final double dDelta = (dY2 - dBlockY);
		if (dY2 != dBlockY) {
			dY2 = dBlockY;
			dY -= dDelta;
			dY1 -= dDelta;
		}

		// 2. Compute and set endpoints of orthogonal axis.
		// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW

		// if ( iXLabelLocation == PlotWithAxes.ABOVE )
		// {
		// // X-AXIS BAND IS (y1 -> y2)
		// dXAxisThickness = dY2 - dY1;
		// }
		// else if ( iXLabelLocation == PlotWithAxes.BELOW )
		// {
		// dXAxisThickness = dY2 - dY1;
		// }

		fAxisY = dY;
		fAxisTop = dY1;
		fAxisBottom = dY2;

		dTopHeight = dY - dY1 + dY1Delta;
		dBottomHeight = dY2 - dY;
	}

	private void computeYLocationWithMaxOrigin() throws ChartException, IllegalArgumentException {
		// Compute y, yAbove, yBelow.
		IntersectionValue iv = fHorizontalAxis.getIntersectionValue();
		double dY = PlotWithAxes.getLocation(fVerticalAxis.getScale(), iv);
		double dY1 = dY;
		double dY2 = dY;
		double dY2Delta = 0;
		// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'

		dY += dAppliedXAxisPlotSpacing;
		dHTotal -= dAppliedXAxisPlotSpacing;

		dY1 = dY;
		dY2 = dY;

		double dTickSize = fPlotWithAxes.getTickSize();
		if (bTicksBelow && dTickSize < dHTotal) {
			dY2 += dTickSize;
			dHTotal -= dTickSize;
		} else {
			dTickSize = 0;
			// axPH.setShowTicks( false );
		}

		if (iXLabelLocation == PlotWithAxes.ABOVE) {
			double dXLabelHeight = Math.max((bTicksAbove ? dTickSize : 0) + dXAxisLabelsThickness,
					dAppliedXAxisPlotSpacing);

			if (dXLabelHeight + dDecorationThickness[1] < dHTotal) {
				dY1 -= dXLabelHeight;
				dY2 += dDecorationThickness[1];
				dHTotal -= (dXLabelHeight + dDecorationThickness[1]);
			} else {
				dXLabelHeight = 0;
				fHorizontalAxis.setShowLabels(false);
			}
		} else if (iXLabelLocation == PlotWithAxes.BELOW) {
			double dXLabelHeight = 0;
			dY2Delta = Math.max(dXAxisLabelsThickness, dDecorationThickness[1]) - dXLabelHeight;
			double dHt1 = Math.max(bTicksAbove ? dTickSize : 0, dAppliedXAxisPlotSpacing);
			if (dXLabelHeight + dHt1 <= dHTotal) {
				dY2 += dXLabelHeight;
				dY1 -= dHt1;
				dHTotal -= (dXLabelHeight + dHt1);
			} else {
				dXLabelHeight = 0;
				fHorizontalAxis.setShowLabels(false);
			}
		}
		if (iXTitleLocation == PlotWithAxes.ABOVE) {
			dY1 -= dXAxisTitleThickness;
		} else if (iXTitleLocation == PlotWithAxes.BELOW) {
			dY2Delta += dXAxisTitleThickness;
		}

		// ENSURE THAT WE DON'T GO BELOW THE LOWER PLOT BLOCK EDGE
		double dBlockY = fPlotBounds.getTop();
		final double dDelta = (dBlockY - dY1);
		if (dY1 != dBlockY) {
			dY1 = dBlockY;
			dY += dDelta;
			dY2 += dDelta;
		}

		// 2. Compute and set endpoints of orthogonal axis.
		// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
		// if ( iXLabelLocation == PlotWithAxes.ABOVE )
		// {
		// dXAxisThickness = dY2 - dY1;
		// }
		// else if ( iXLabelLocation == PlotWithAxes.BELOW )
		// {
		// // X-AXIS BAND IS (y1 -> y2)
		// dXAxisThickness = dY2 - dY1;
		// }

		fAxisY = dY;
		fAxisTop = dY1;
		fAxisBottom = dY2;

		dTopHeight = dY - dY1;
		dBottomHeight = dY2 - dY + dY2Delta;
	}

	private void computeYLocatoinWithValueOrigin() throws ChartException, IllegalArgumentException {
		IntersectionValue iv = fHorizontalAxis.getIntersectionValue();
		double dY = PlotWithAxes.getLocation(fVerticalAxis.getScale(), iv);
		double dY1 = dY;
		double dY2 = dY;
		double dDeltaY1 = 0, dDeltaY2 = 0;
		double dBlockY = fPlotBounds.getTop();
		double dBlockHeight = fPlotBounds.getHeight();
		AutoScale scY = fVerticalAxis.getScale();
		boolean bForwardScale = fHorizontalAxis.getScale().getDirection() == PlotWithAxes.FORWARD;
		IDisplayServer ids = fPlotWithAxes.getDisplayServer();

		if (iXLabelLocation == PlotWithAxes.ABOVE) {
			double dH1 = (bTicksAbove ? fPlotWithAxes.getTickSize() : 0)
					+ Math.max(dXAxisLabelsThickness, dDecorationThickness[0]);
			double dH2 = (bTicksBelow ? fPlotWithAxes.getTickSize() : 0);
			if (dH1 + dH2 <= dHTotal) {
				dY1 -= dH1;
				dY2 += dH2;
				dHTotal -= (dH1 + dH2);
			} else {
				// axPH.setShowTicks( false );
				fHorizontalAxis.setShowLabels(false);
			}

			if (iXTitleLocation == PlotWithAxes.ABOVE) {
				dY1 -= dXAxisTitleThickness;
			} else if (iXTitleLocation == PlotWithAxes.BELOW) {
				dY2 += dXAxisTitleThickness;
			}
			dDeltaY1 = dY - dY1;
			dDeltaY2 = dY2 - dY;

			// CHECK IF UPPER EDGE OF X-AXIS BAND GOES ABOVE PLOT UPPER EDGE
			if (dY1 < dBlockY) {
				final Object[] oaMinMax = scY.getMinMax();
				boolean bForceBreak = false;

				// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
				// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
				// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
				// POSITIONED

				do {
					// CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
					// Y-AXIS
					scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
					scY.resetShifts();

					// APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
					// ENDPOINT
					double[] da = scY.getEndPoints();
					double dT_RI = dBlockY - dY1; // THRESHOLD -
					// REQUESTEDINTERSECTION

					double dStart, dEnd;

					if (bForwardScale) {
						double dAMin_AMax = da[1] - da[0];
						double dAMin_RI = da[1] - dY;
						dEnd = da[1];
						dStart = (dT_RI / dAMin_RI) * dAMin_AMax + da[0];
						if (dStart < dBlockY) {
							dStart = dBlockY;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					} else {
						double dAMin_AMax = da[0] - da[1];
						double dAMin_RI = da[0] - dY;
						dStart = da[0];
						dEnd = (dT_RI / dAMin_RI) * dAMin_AMax + da[1];
						if (dEnd < dBlockY) {
							dEnd = dBlockY;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					}

					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					scY.setEndPoints(dStart, dEnd);
					scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL, dStart, dEnd, true,
							fPlotWithAxes.getAxes());
					if (!scY.isStepFixed()) {
						while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
							if (!scY.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL,
									dStart, dEnd, true, fPlotWithAxes.getAxes());
							if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR
									&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}

					dY = PlotWithAxes.getLocation(scY, iv);
					dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
					// UPPER
					// EDGE
					dY2 = dY + dDeltaY2; // REDUNDANT: RE-CALCULATE
					// X-AXIS
					// BAND LOWER EDGE
				} while (Math.abs(dY1 - dBlockY) > 1 && !bForceBreak);
			}
		} else if (iXLabelLocation == PlotWithAxes.BELOW) {
			double dH1 = (bTicksAbove ? fPlotWithAxes.getTickSize() : 0);
			double dH2 = (bTicksBelow ? fPlotWithAxes.getTickSize() : 0)
					+ Math.max(dXAxisLabelsThickness, dDecorationThickness[1]);
			if (dH1 + dH2 <= dHTotal) {
				dY1 -= dH1;
				dY2 += dH2;
				dHTotal -= (dH1 + dH2);
			}

			if (iXTitleLocation == PlotWithAxes.ABOVE) {
				dY1 -= dXAxisTitleThickness;
			} else if (iXTitleLocation == PlotWithAxes.BELOW) {
				dY2 += dXAxisTitleThickness;
			}
			dDeltaY1 = dY - dY1;
			dDeltaY2 = dY2 - dY;

			// CHECK IF LOWER EDGE OF X-AXIS BAND GOES BELOW PLOT LOWER EDGE
			if (dY2 > dBlockY + dBlockHeight) {
				final Object[] oaMinMax = scY.getMinMax();
				boolean bForceBreak = false;

				// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
				// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
				// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
				// POSITIONED

				do {
					// CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
					// Y-AXIS
					scY.setEndPoints(scY.getStart() + scY.getStartShift(), scY.getEnd() - scY.getEndShift()); // RESTORE
					scY.resetShifts();

					// APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
					// ENDPOINT
					double[] da = scY.getEndPoints();
					double dX2_X1 = dY2 - (dBlockY + dBlockHeight); // THRESHOLD
					// -
					// REQUESTEDINTERSECTION

					double dStart, dEnd;

					if (bForwardScale) {
						double dAMin_AMax = da[1] - da[0];
						double dX2_AMax = dY - da[0];
						dEnd = da[1] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dStart = da[0];

						if (dEnd > dBlockY + dBlockHeight) {
							dEnd = dBlockY + dBlockHeight;
							bForceBreak = true;
						}
					} else {
						double dAMin_AMax = da[0] - da[1];
						double dX2_AMax = dY - da[1];
						dStart = da[0] - (dX2_X1 / dX2_AMax) * dAMin_AMax;
						dEnd = da[1];

						if (dStart > dBlockY + dBlockHeight) {
							dStart = dBlockY + dBlockHeight;
							bForceBreak = true; // ADJUST THE TOP EDGE OF
							// THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}
					}

					if (ChartUtil.mathEqual(Math.abs(dEnd - dStart), 0)) {
						// too small space to adjust, break here.
						bForceBreak = true;
					}

					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					scY.setEndPoints(dStart, dEnd);
					scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL, dStart, dEnd, true,
							fPlotWithAxes.getAxes());
					if (!scY.isStepFixed()) {
						while (!scY.checkFit(ids, laYAxisLabels, iYLabelLocation)) {
							if (!scY.zoomOut()) {
								bForceBreak = true;
								break;
							}
							scY.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
							int tickCount = scY.computeTicks(ids, laYAxisLabels, iYLabelLocation, PlotWithAxes.VERTICAL,
									dStart, dEnd, true, fPlotWithAxes.getAxes());
							if (scY.getUnit() != null && PlotWithAxes.asInteger(scY.getUnit()) == Calendar.YEAR
									&& tickCount <= 3 || fPlotWithAxes.isSharedScale()) {
								bForceBreak = true;
								break;
							}
						}
					}

					dY = PlotWithAxes.getLocation(scY, iv);
					dY2 = dY + dDeltaY2; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
					dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
					// LOWER
					// EDGE
				} while (Math.abs(dY2 - (dBlockY + dBlockHeight)) > 1 && !bForceBreak);
			}
		}

		if (dY2 > (dBlockY + dBlockHeight)) {
			final double dDelta = (dY2 - dBlockY - dBlockHeight);
			dY2 = dBlockY + dBlockHeight;
			dY -= dDelta;
			dY1 -= dDelta;
		}

		fAxisY = dY;
		fAxisTop = dY1;
		fAxisBottom = dY2;

		dTopHeight = dY - dY1;
		dBottomHeight = dY2 - dY;
	}

	/**
	 * Returns current horizontal axis.
	 * 
	 * @return
	 */
	OneAxis getHorizontalAxis() {
		return fHorizontalAxis;
	}

	/**
	 * Returns label thickness of current axis.
	 * 
	 * @return
	 */
	double getAxisLabelThickness() {
		return dXAxisLabelsThickness;
	}

	/**
	 * Returns title thickness of current axis.
	 * 
	 * @return
	 */
	double getAxisTitleThickness() {
		return dXAxisTitleThickness;
	}

	double getTopHeight() {
		return dTopHeight;
	}

	double getBottomHeight() {
		return dBottomHeight;
	}

	/**
	 * Computes the title coordinate.
	 * 
	 * @param axisCoordinate
	 * @return
	 */
	double getTitleCoordinate(double axisCoordinate) {
		return (iXTitleLocation == PlotWithAxes.BELOW)
				? axisCoordinate + 1 + (bTicksBelow ? fPlotWithAxes.getTickSize() : 0)
						+ ((iXLabelLocation == PlotWithAxes.BELOW) ? getAxisLabelThickness() : 0)
				: axisCoordinate - getTopHeight() - 1;
	}
}
