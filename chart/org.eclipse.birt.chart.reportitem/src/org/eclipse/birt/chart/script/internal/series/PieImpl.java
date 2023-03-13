/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

	@Override
	public double getMinSlice() {
		return getChart().getMinSlice();
	}

	@Override
	public String getMinSliceLabel() {
		return getChart().getMinSliceLabel();
	}

	@Override
	public void setMinSlice(double value) {
		getChart().setMinSlice(value);
	}

	@Override
	public void setMinSliceLabel(String label) {
		getChart().setMinSliceLabel(label);
	}

	@Override
	public String getExplosionExpr() {
		return getPie().getExplosionExpression();
	}

	@Override
	public void setExplosionExpr(String expr) {
		getPie().setExplosionExpression(expr);
	}

}
