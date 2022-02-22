/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;

/**
 * Defines the callback methods to be implemented by a custom series renderer to
 * compute and render a specific series implementation.
 */
public interface ISeriesRenderer {

	/**
	 * Sends out a first pass notification to the series renderer implementation to
	 * perform any necessary pre-computations prior to a second pass rendering.
	 *
	 * @param bo
	 * @param p
	 * @param isrh
	 *
	 * @throws ChartException
	 */
	void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException;

	/**
	 * Notifies the series renderer to render itself in the provided plot area. Each
	 * of the individual series renderers will have to implement their own graphic
	 * element rendering routines w.r.t. the plot background.
	 *
	 * @param ipr
	 * @param p
	 * @param isrh
	 *
	 * @throws ChartException
	 */
	void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException;

	/**
	 * Notifies the series renderer to render a legend graphic element in the legend
	 * content that represents the series associated with the renderer.
	 *
	 * @param ipr
	 * @param lg
	 * @param fPaletteEntry
	 * @param bo
	 *
	 * @throws ChartException
	 */
	void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo) throws ChartException;
}
