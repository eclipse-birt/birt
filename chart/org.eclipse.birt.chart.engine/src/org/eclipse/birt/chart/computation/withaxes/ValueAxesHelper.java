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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;

/**
 * The helper class provides functions to easy access value axes and its
 * location.
 * 
 * @since 2.5
 */

class ValueAxesHelper {

	private AllAxes fAllAxes;

	private double fLeft;

	private double fTop;

	private double fWidth;

	private double fHeight;

	private double fPercentTotal = 0d;

	private double[] fAllAxesPercents;

	private OneAxis[] fAllValueAxes;

	/**
	 * The field indicates the index of value axis which is crossing with primary
	 * base axis.
	 */
	private int fCrossingIndex = 0;

	/** The field indicates the thickness of primary base axis. */
	private double fBaseAxisThickness = 0d;

	/**
	 * Constructor.
	 * 
	 * @param plotWithAxes
	 * @param aax
	 * @param plotBounds
	 * @throws ChartException
	 */
	public ValueAxesHelper(PlotWith2DAxes plotWithAxes, AllAxes aax, Bounds plotBounds) throws ChartException {
		this.fAllAxes = aax;
		this.fLeft = plotBounds.getLeft();
		this.fTop = plotBounds.getTop();
		this.fWidth = plotBounds.getWidth();
		this.fHeight = plotBounds.getHeight();

		// Gets all value axes.
		fAllValueAxes = new OneAxis[aax.getOverlayCount() + 1];
		fAllValueAxes[0] = aax.getPrimaryOrthogonal();
		for (int i = 1; i < fAllValueAxes.length; i++) {
			fAllValueAxes[i] = aax.getOverlay(i - 1);
		}

		// Computes each axis location percent against plot height or width.
		int i = 0;
		fAllAxesPercents = new double[fAllValueAxes.length];
		for (OneAxis axis : fAllValueAxes) {
			fPercentTotal += getPercent(axis.getModelAxis());
			fAllAxesPercents[i++] = fPercentTotal;
		}

		// Computes actual x,y,w,h, used for value axes.
		if (fAllValueAxes.length > 1) {
			precomputeCrossingAxisDelta(plotWithAxes, aax, plotBounds);
		}
	}

	/**
	 * Adjusts the range of all orthogonal axes according to the computed thickness
	 * of category axis.
	 * 
	 * @param plotWithAxes
	 * @param aax
	 * @param plotBounds
	 * @throws ChartException
	 */
	private void precomputeCrossingAxisDelta(PlotWith2DAxes plotWithAxes, AllAxes aax, Bounds plotBounds)
			throws ChartException {
		boolean isMinOrigin = fAllAxes.getPrimaryBase().getIntersectionValue().iType == IConstants.MIN;
		boolean isMaxOrigin = fAllAxes.getPrimaryBase().getIntersectionValue().iType == IConstants.MAX;
		if (isMaxOrigin) {
			fCrossingIndex = fAllValueAxes.length - 1;
		}

		if (!fAllAxes.areAxesSwapped()) {
			plotWithAxes.updateValueAxis(fAllValueAxes[fCrossingIndex], fTop + fHeight, fTop,
					plotWithAxes.getModel().isReverseCategory());

			HorizontalAxisAdjuster haa = new HorizontalAxisAdjuster(aax.getPrimaryBase(), fAllValueAxes[fCrossingIndex],
					plotWithAxes, plotBounds);
			haa.adjust();
			fBaseAxisThickness = haa.getAxisBottomEdge() - haa.getAxisTopEdge()
					- plotWithAxes.getHorizontalSpacingInPixels();
			if (isMinOrigin) {
				this.fHeight -= fBaseAxisThickness;
			} else if (isMaxOrigin) {
				this.fTop += fBaseAxisThickness;
				this.fHeight -= fBaseAxisThickness;
			}
		} else {
			plotWithAxes.updateValueAxis(fAllValueAxes[fCrossingIndex], fLeft, fLeft + fWidth,
					plotWithAxes.getModel().isReverseCategory());

			VerticalAxisAdjuster haa = new VerticalAxisAdjuster(aax.getPrimaryBase(), fAllValueAxes[fCrossingIndex],
					plotWithAxes, plotBounds);
			haa.adjust();
			fBaseAxisThickness = haa.getAxisRightEdge() - haa.getAxisLeftEdge()
					- plotWithAxes.getVerticalSpacingInPixels();
			if (isMinOrigin) {
				this.fLeft += fBaseAxisThickness;
				this.fWidth -= fBaseAxisThickness;
			} else if (isMaxOrigin) {
				this.fWidth -= fBaseAxisThickness;
			}
		}
		fAllValueAxes[fCrossingIndex].set((AutoScale) null);
	}

	/**
	 * Returns all instances of OneAxis.
	 * 
	 * @return
	 */
	OneAxis[] getValueAxes() {
		return fAllValueAxes;
	}

	/**
	 * Returns the start location for specified value axis.
	 * 
	 * @param valueAxisIndex
	 * @return
	 */
	double getStart(int valueAxisIndex) {
		double start = 0;
		if (fAllValueAxes.length == 1 || valueAxisIndex == 0) {
			start = (!this.fAllAxes.areAxesSwapped()) ? (fTop + fHeight) : fLeft;
		} else {

			if (!this.fAllAxes.areAxesSwapped()) {
				start = fTop + fHeight * (1 - fAllAxesPercents[valueAxisIndex - 1] / fPercentTotal);
			} else {
				start = fLeft + fWidth * fAllAxesPercents[valueAxisIndex - 1] / fPercentTotal;
			}
		}
		return start;
	}

	/**
	 * Returns the end location for specified value axis.
	 * 
	 * @param valueAxisIndex
	 * @return
	 */
	double getEnd(int valueAxisIndex) {
		double end = 0;
		if (fAllValueAxes.length == 1) {
			end = (!this.fAllAxes.areAxesSwapped()) ? fTop : fLeft + fWidth;
		} else {
			if (!this.fAllAxes.areAxesSwapped()) {
				end = fTop + fHeight * (1 - fAllAxesPercents[valueAxisIndex] / fPercentTotal);
			} else {
				end = fLeft + fWidth * fAllAxesPercents[valueAxisIndex] / fPercentTotal;
			}
		}
		return end;
	}

	private double getPercent(Axis axis) {
		if (axis.isSetAxisPercent()) {
			return axis.getAxisPercent();
		}
		return 1;
	}
}
