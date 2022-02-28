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

package org.eclipse.birt.chart.tests.script.series;

import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.api.series.IPie;
import org.eclipse.birt.chart.script.api.series.data.ISimpleData;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 *
 */

public class PieSeriesTest extends BaseChartTestCase {

	public void testGetDataExpr() {
		IValueSeries series = getChartWithoutAxes().getValueSeries()[0];
		assertTrue(series instanceof IPie);

		assertTrue(series.getDataExpr() instanceof ISimpleData);
		assertEquals(((ISimpleData) series.getDataExpr()).getExpr(), "row[\"ORDERNUMBER\"]");
	}

	public void testMinSlice() {
		IPie pie = (IPie) getChartWithoutAxes().getValueSeries()[0];
		assertTrue(pie.getMinSlice() == 10000);

		pie.setMinSlice(10001);
		assertTrue(pie.getMinSlice() == 10001);
	}

	public void testMinSliceLabel() {
		IPie pie = (IPie) getChartWithoutAxes().getValueSeries()[0];
		assertEquals(pie.getMinSliceLabel(), "Label");

		pie.setMinSliceLabel("");
		assertEquals(pie.getMinSliceLabel(), "");
	}

	public void testExplosionExpr() {
		IPie pie = (IPie) getChartWithoutAxes().getValueSeries()[0];
		assertEquals(pie.getExplosionExpr(), "valueData>10005");

		pie.setExplosionExpr("");
		assertEquals(pie.getExplosionExpr(), "");
	}
}
