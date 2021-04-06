/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.radar.render;

import java.util.ArrayList;

import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

/**
 * Helper function for computing and getting scale properties
 */

public class RadarScaleHelper {

	private int scaleCount = 6;
	private double axisMin = Double.MAX_VALUE;
	private double axisMax = Double.MIN_VALUE;
	private boolean autoscale = false;

	private RadarSeries radarSeries;
	private ChartWithoutAxes chartWithoutAxes;

	public RadarScaleHelper(RadarSeries series, ChartWithoutAxes chartWithoutAxes) {
		this.radarSeries = series;
		this.chartWithoutAxes = chartWithoutAxes;
	}

	public void compute() throws ChartException {
		// Currently only using the base series to store web/radar specific
		// information
		int psc = radarSeries.getPlotSteps().intValue();
		if (psc > 20) {
			psc = 20;
		}
		if (psc < 1) {
			psc = 1;
		}
		scaleCount = psc;

		computeDsMinMax();
	}

	public int getScaleCount() {
		return scaleCount;
	}

	public boolean getAutoScale() {
		return autoscale;
	}

	public double getAxisMin() {
		if (autoscale) {
			double nmin = axisMin - (axisMin * (1.0 / (scaleCount * 5)));
			return nmin;
		} else {
			return axisMin;
		}
	}

	public double getAxisMax() {
		if (autoscale) {
			double nmax = axisMax + (axisMax * (1.0 / (scaleCount * 5)));
			return nmax;
		} else {
			return axisMax;
		}
	}

	private void computeDsMinMax() throws ChartException {
		double calcMin = Double.MAX_VALUE;
		double calcMax = Double.MIN_VALUE;

		// Auto Scale
		if (!radarSeries.isSetRadarAutoScale() || radarSeries.isRadarAutoScale()) {
			autoscale = true;
		} else {
			double taxisMin = radarSeries.getWebLabelMin();
			double taxisMax = radarSeries.getWebLabelMax();
			if ((taxisMax - taxisMin) == 0.0) {
				autoscale = true;
			}
		}

		PluginSettings ps = PluginSettings.instance();
		IDataSetProcessor iDSP = null;
		DataSet dst;

		EList<SeriesDefinition> el = chartWithoutAxes.getSeriesDefinitions();
		ArrayList<Series> al = new ArrayList<Series>();
		((ChartWithoutAxesImpl) chartWithoutAxes).recursivelyGetSeries(el, al, 0, 0);
		final Series[] sea = al.toArray(new Series[al.size()]);

		for (int i = 0; i < sea.length; i++) {
			iDSP = ps.getDataSetProcessor(sea[i].getClass());
			dst = sea[i].getDataSet();
			Object oMin = iDSP.getMinimum(dst);
			Object oMax = iDSP.getMaximum(dst);
			Double min = oMin == null ? null : ((Number) oMin).doubleValue();
			Double max = oMax == null ? null : ((Number) oMax).doubleValue();

			if (min != null && min < calcMin) {
				calcMin = min;
			}
			if (max != null && max > calcMax) {
				calcMax = max;
			}
		}
		if (autoscale) {
			this.axisMin = calcMin;
			this.axisMax = calcMax;
		} else {
			this.axisMin = radarSeries.getWebLabelMin();
			this.axisMax = radarSeries.getWebLabelMax();
		}
	}
}
