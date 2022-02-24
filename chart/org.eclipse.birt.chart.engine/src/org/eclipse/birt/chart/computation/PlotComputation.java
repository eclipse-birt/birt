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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;

/**
 * Used as base class for Plot computation. Abstract useful methods from
 * PlotWithAxes and PlotWithoutAxes.
 */

public abstract class PlotComputation {

	protected final static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/computation"); //$NON-NLS-1$

	protected final static IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * An internal XServer implementation capable of obtaining text metrics, etc.
	 */
	protected final IDisplayServer ids;

	/**
	 * The runtime context associated with chart generation
	 */
	protected final RunTimeContext rtc;

	protected final IChartComputation cComp;

	/**
	 * A final internal reference to the model used in rendering computations
	 */
	protected final Chart cm;

	/**
	 * Insets maintained as pixels equivalent of the points value specified in the
	 * model used here for fast computations
	 */
	protected Insets insCA = null;

	/**
	 * Ratio for converting a point to a pixel
	 */
	protected transient double dPointToPixel = 0;

	public PlotComputation(IDisplayServer ids, RunTimeContext rtc, Chart cm) {
		this.rtc = rtc;
		this.cComp = rtc.getState(StateKey.CHART_COMPUTATION_KEY);
		this.ids = ids;
		this.cm = cm;
		dPointToPixel = ids.getDpiResolution() / 72d;
	}

	/**
	 * A computed plot area based on the block dimensions and the axis attributes
	 * and label values (within axes)
	 */
	protected Bounds boPlotBackground = goFactory.createBounds(0, 0, 100, 100);

	/**
	 * This method computes the entire chart within the given bounds. If the dataset
	 * has changed but none of the axis attributes have changed, simply re-compute
	 * without 'rebuilding axes'.
	 * 
	 * @param bo
	 * 
	 */
	public abstract void compute(Bounds bo) throws ChartException, IllegalArgumentException;

	/**
	 * @param sdOrthogonal
	 * @param seOrthogonal
	 * @return ISeriesRenderingHints
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public abstract ISeriesRenderingHints getSeriesRenderingHints(SeriesDefinition sdOrthogonal, Series seOrthogonal)
			throws ChartException, IllegalArgumentException;

	/**
	 * 
	 * @return The plot bounds in pixels
	 */
	public final Bounds getPlotBounds() {
		return boPlotBackground;
	}

	public Chart getModel() {
		return cm;
	}

	public final Insets getPlotInsets() {
		return insCA;
	}

	public final RunTimeContext getRunTimeContext() {
		return rtc;
	}

	public IChartComputation getChartComputation() {
		return cComp;
	}

	/**
	 * Returns current rate for Point->Pixel.
	 * 
	 * @return
	 * @since 2.5
	 */
	public double getPointToPixel() {
		return dPointToPixel;
	}
}
