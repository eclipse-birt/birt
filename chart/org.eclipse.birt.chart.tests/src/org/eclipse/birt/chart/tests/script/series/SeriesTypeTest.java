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

import org.eclipse.birt.chart.script.api.IChart;
import org.eclipse.birt.chart.script.api.IChartWithAxes;
import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.api.series.IStock;
import org.eclipse.birt.chart.script.api.series.data.IStockData;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class SeriesTypeTest extends BaseChartTestCase {

	public void testStockSeries() {
		IChart ichart = null;
		try {
			ichart = (IChart) getReportDesign().getReportElement("Stock");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(ichart instanceof IChartWithAxes);

		IValueSeries series = ((IChartWithAxes) ichart).getValueSeries()[0][0];
		assertTrue(series instanceof IStock);

		IStock stock = (IStock) series;
		assertTrue(stock.getDataExpr() instanceof IStockData);

		IStockData data = (IStockData) stock.getDataExpr();
		assertEquals("row.__rownum/2+10", data.getHighExpr());
		assertEquals("0", data.getLowExpr());

		data.setOpenExpr("3");
		assertEquals(data.getOpenExpr(), "3");
	}

}
