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

package org.eclipse.birt.chart.tests.script;

import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 *
 */

public class ChartWithoutAxesTest extends BaseChartTestCase {

	public void testGetValueSeries() {
		IValueSeries[] series = getChartWithoutAxes().getValueSeries();
		assertEquals("IChartWithoutAxes.getValueSeries", series.length, 2);
	}
}
