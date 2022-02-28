/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 *
 */

public class BIRTChartComputation implements IChartComputation {

	@Override
	public double computeFontHeight(IDisplayServer xs, Label la) throws ChartException {
		return Methods.computeFontHeight(xs, la);
	}

	@Override
	public ITextMetrics getTextMetrics(IDisplayServer xs, Label la, double wrapping) {
		ITextMetrics itm = xs.getTextMetrics(la);
		itm.reuse(la, wrapping);
		return itm;
	}

	@Override
	public BoundingBox computeLabelSize(IDisplayServer xs, Label la, double dWrapping, Double fontHeight)
			throws ChartException {
		return Methods.computeLabelSize(xs, la, dWrapping, fontHeight);
	}

	@Override
	public BoundingBox computeBox(IDisplayServer xs, int iLabelLocation, Label la, double dX, double dY)
			throws ChartException {
		try {
			return Methods.computeBox(xs, iLabelLocation, la, dX, dY);
		} catch (IllegalArgumentException uiex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
		}
	}

	@Override
	public RotatedRectangle computePolygon(IDisplayServer xs, int iLabelLocation, Label la, double dX, double dY,
			Double fontHeight) throws ChartException {
		try {
			return Methods.computePolygon(xs, iLabelLocation, la, dX, dY, fontHeight);
		} catch (IllegalArgumentException uiex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
		}
	}

	@Override
	public double computeWidth(IDisplayServer xs, Label la) throws ChartException {
		return Methods.computeWidth(xs, la);
	}

	@Override
	public double computeHeight(IDisplayServer xs, Label la) throws ChartException {
		return Methods.computeHeight(xs, la);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void applyWrapping(IDisplayServer xs, Label la, double dWrapping) throws ChartException {
		ITextMetrics itm = xs.getTextMetrics(la);
		itm.reuse(la, dWrapping);
		itm.dispose();
	}

	@Override
	public void recycleTextMetrics(ITextMetrics itm) {
		if (itm != null) {
			itm.dispose();
		}
	}

}
