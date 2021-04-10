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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * The class is used to adjust axes's location and scale.
 * 
 * @since 2.5
 */

public class AxesAdjuster implements IAxisAdjuster, IConstants {

	private AllAxes fAllAxes;

	private ValueAxesHelper fValueAxesHelper;

	private PlotWithAxes fPlotWithAxes;

	private Bounds fPlotBounds;

	/**
	 * Constructor.
	 * 
	 * @param plotWithAxes
	 * @param valueAxesHelper
	 * @param boPlot
	 */
	public AxesAdjuster(PlotWithAxes plotWithAxes, ValueAxesHelper valueAxesHelper, Bounds boPlot) {
		fPlotWithAxes = plotWithAxes;
		fValueAxesHelper = valueAxesHelper;
		fPlotBounds = boPlot;

		fAllAxes = plotWithAxes.getAxes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.IAxesAdjuster#adjust()
	 */
	public void adjust() throws ChartException {
		// Get the index of value axis which is 1th axis crossing with the
		// primary base axis.
		int index = 0;
		if (fAllAxes.getPrimaryBase().getIntersectionValue().iType == IConstants.MAX) {
			index = fValueAxesHelper.getValueAxes().length - 1;
		}

		if (!fAllAxes.areAxesSwapped()) {
			// Adjust vertical axes.
			new VerticalAxesAdjuster(fValueAxesHelper.getValueAxes(), fAllAxes.getPrimaryBase(), fPlotWithAxes,
					fPlotBounds).adjust();

			// Adjust horizontal axis according to the 1th vertical axis.
			new HorizontalAxesAdjuster(new OneAxis[] { fAllAxes.getPrimaryBase() },
					fValueAxesHelper.getValueAxes()[index], fPlotWithAxes, fPlotBounds).adjust(false);
		} else {
			// Adjust horizontal axes.
			new HorizontalAxesAdjuster(fValueAxesHelper.getValueAxes(), fAllAxes.getPrimaryBase(), fPlotWithAxes,
					fPlotBounds).adjust();

			// Adjust vertical axis according to the 1th horizontal axis.
			new VerticalAxesAdjuster(new OneAxis[] { fAllAxes.getPrimaryBase() },
					fValueAxesHelper.getValueAxes()[index], fPlotWithAxes, fPlotBounds).adjust(false);
		}

	}

	/**
	 * Returns the location delta between the axis location and the start of related
	 * orthogonal axis.
	 * 
	 * @param sc
	 * @param iv
	 * @return
	 */
	static final double getLocationDelta(AutoScale sc, IntersectionValue iv) {
		AxisTickCoordinates da = sc.getTickCordinates();
		if (iv.getType() == IConstants.MIN) {
			return 0;
		} else if (iv.getType() == IConstants.MAX) {
			return 0;
		}

		if ((sc.getType() & TEXT) == TEXT || sc.isCategoryScale()) {
			double dValue = iv.getValueAsDouble(sc);
			return da.getStep() * dValue;
		} else if ((sc.getType() & DATE_TIME) == DATE_TIME) {
			CDateTime cdtValue = Methods.asDateTime(iv.getValue());
			CDateTime cdt = Methods.asDateTime(sc.getMinimum()), cdtPrev = null;
			int iUnit = Methods.asInteger(sc.getUnit());
			int iStep = Methods.asInteger(sc.getStep());

			for (int i = 0; i < da.size(); i++) {
				if (cdt.after(cdtValue)) {
					if (cdtPrev == null) {
						return 0;
					}
					/*
					 * SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); String
					 * sMin = sdf.format(cdtPrev.getTime()); String sMax =
					 * sdf.format(cdt.getTime()); String sVal = sdf.format(cdtValue.getTime());
					 */

					long l1 = cdtPrev.getTimeInMillis();
					long l2 = cdt.getTimeInMillis();
					long l = cdtValue.getTimeInMillis();
					double dUnitSize = da.getStep();

					double dOffset = (dUnitSize / (l2 - l1)) * (l - l1);
					return dUnitSize * (i - 1) + dOffset;

				}
				cdtPrev = cdt;
				cdt = cdt.forward(iUnit, iStep);
			}
			double distance = da.getStep() * da.size();
			double axisDisc = sc.getEnd() - sc.getStart();
			if (da.getStep() > 0) {
				if (distance > axisDisc) {
					distance = axisDisc;
				}
			} else {
				if (distance < axisDisc) {
					distance = axisDisc;
				}
			}
			return distance;

		} else if ((sc.getType() & LOGARITHMIC) == LOGARITHMIC) {
			double dValue = iv.getValueAsDouble(sc);
			if (dValue == 0) // CANNOT GO TO '0'
			{
				return sc.getStart();
			}
			if (dValue < 0) {
				return 0;
			}
			double dMinimumLog = Math.log(Methods.asDouble(sc.getMinimum()).doubleValue()) / LOG_10;
			double dStepLog = Math.log(Methods.asDouble(sc.getStep()).doubleValue()) / LOG_10;
			double dValueLog = Math.log(dValue) / LOG_10;
			return (((dValueLog - dMinimumLog) / dStepLog) * da.getStep());
		} else {
			double dValue = iv.getValueAsDouble(sc);
			double dMinimum = Methods.asDouble(sc.getMinimum()).doubleValue();
			double dMaximum = Methods.asDouble(sc.getMaximum()).doubleValue();
			double[] ea = sc.getEndPoints();

			if (dMaximum == dMinimum) {
				return 0;
			} else {
				return (((dValue - dMinimum) / (dMaximum - dMinimum)) * (ea[1] - ea[0]));
			}
		}
	}
}
