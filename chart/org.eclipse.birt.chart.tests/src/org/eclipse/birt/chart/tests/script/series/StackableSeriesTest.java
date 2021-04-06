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

package org.eclipse.birt.chart.tests.script.series;

import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.api.series.IStackableSeries;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class StackableSeriesTest extends BaseChartTestCase {

	public void testStacked() {
		IValueSeries series = getChartWithAxes().getValueSeries()[0][0];
		assertTrue(series instanceof IStackableSeries);
		assertEquals(((IStackableSeries) series).isStacked(), false);

		((IStackableSeries) series).setStacked(true);
		assertEquals(((IStackableSeries) series).isStacked(), true);
	}
}
