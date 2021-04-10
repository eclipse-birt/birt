/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal.series;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.script.api.series.IPie;
import org.eclipse.birt.chart.script.internal.component.ValueSeriesImpl;

/**
 * 
 */

public class PieImpl extends ValueSeriesImpl implements IPie {

	public PieImpl(SeriesDefinition sd, Chart cm) {
		super(sd, cm);
		assert series instanceof PieSeries;
	}

	private ChartWithoutAxes getChart() {
		return ((ChartWithoutAxes) cm);
	}

	private PieSeries getPie() {
		return (PieSeries) series;
	}

	public double getMinSlice() {
		return getChart().getMinSlice();
	}

	public String getMinSliceLabel() {
		return getChart().getMinSliceLabel();
	}

	public void setMinSlice(double value) {
		getChart().setMinSlice(value);
	}

	public void setMinSliceLabel(String label) {
		getChart().setMinSliceLabel(label);
	}

	public String getExplosionExpr() {
		return getPie().getExplosionExpression();
	}

	public void setExplosionExpr(String expr) {
		getPie().setExplosionExpression(expr);
	}

}
