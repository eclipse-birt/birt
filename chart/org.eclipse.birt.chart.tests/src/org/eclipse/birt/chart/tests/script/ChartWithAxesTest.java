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

package org.eclipse.birt.chart.tests.script;

import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 * 
 */

public class ChartWithAxesTest extends BaseChartTestCase {

	public void testHorizontal() {
		assertFalse("IChartWithAxes.isHorizontal", getChartWithAxes().isHorizontal());

		getChartWithAxes().setHorizontal(true);
		assertTrue("IChartWithAxes.isHorizontal", getChartWithAxes().isHorizontal());
	}

	public void testGetCategoryAxis() {
		assertNotNull("IChartWithAxes.getCategoryAxis", getChartWithAxes().getCategoryAxis());
	}

	public void testGetValueAxes() {
		assertEquals("IChartWithAxes.getValueAxes", getChartWithAxes().getValueAxes().length, 3);
	}

	public void testGetValueSeries() {
		IValueSeries[][] series = getChartWithAxes().getValueSeries();
		assertEquals("IChartWithAxes.getValueSeries axis number", series.length, 3);
		assertEquals("IChartWithAxes.getValueSeries axis 1", series[0].length, 1);
		assertEquals("IChartWithAxes.getValueSeries axis 2", series[1].length, 2);
	}
}
