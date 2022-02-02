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

/**
 * 
 */

public interface IChartComputation {

	void dispose();

	/**
	 * Returns a text metrics.
	 * 
	 * @param xs
	 * @param la
	 * @param wrapping
	 * @return
	 */
	ITextMetrics getTextMetrics(IDisplayServer xs, Label la, double wrapping);

	/**
	 * Dispose the text metrics.
	 * 
	 * @param itm
	 */
	void recycleTextMetrics(ITextMetrics itm);

	/**
	 * Convenient method to compute the font's height of a label. This computation
	 * is costly, but in most case we do not change the font of a label, we just
	 * change the string value, so the font height will not changed. The purpose of
	 * the method is to get the font height overhead for reusing.
	 * 
	 * @param xs
	 * @param la
	 * @return font height
	 */
	double computeFontHeight(IDisplayServer xs, Label la) throws ChartException;

	/**
	 * Compute the size of a label.
	 * 
	 * @param xs
	 * @param la
	 * @param dWrapping
	 * @param fontHeight
	 * @return
	 * @throws ChartException
	 */
	BoundingBox computeLabelSize(IDisplayServer xs, Label la, double dWrapping, Double fontHeight)
			throws ChartException;

	/**
	 * Compute the bounding box ( location and size ) of a label.
	 * 
	 * @param xs
	 * @param iLabelLocation
	 * @param la
	 * @param dX
	 * @param dY
	 * @return
	 * @throws ChartException
	 */
	BoundingBox computeBox(IDisplayServer xs, int iLabelLocation, Label la, double dX, double dY) throws ChartException;

	/**
	 * Compute the bounding polygon of a label.
	 * 
	 * @param xs
	 * @param iLabelLocation
	 * @param la
	 * @param dX
	 * @param dY
	 * @param fontHeight
	 * @return
	 * @throws ChartException
	 */
	RotatedRectangle computePolygon(IDisplayServer xs, int iLabelLocation, Label la, double dX, double dY,
			Double fontHeight) throws ChartException;

	/**
	 * Compute the width of a label.
	 * 
	 * @param xs
	 * @param la
	 * @return
	 * @throws ChartException
	 */
	double computeWidth(IDisplayServer xs, Label la) throws ChartException;

	/**
	 * Compute the height of a label.
	 * 
	 * @param xs
	 * @param la
	 * @return
	 * @throws ChartException
	 */
	double computeHeight(IDisplayServer xs, Label la) throws ChartException;

	/**
	 * Apply the wrapping to a label.
	 * 
	 * @param xs
	 * @param la
	 * @param dWapping
	 * @throws ChartException
	 */
	void applyWrapping(IDisplayServer xs, Label la, double dWapping) throws ChartException;

}
