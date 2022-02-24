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

package org.eclipse.birt.chart.tests.script.component;

import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 *
 */

public class LegendTest extends BaseChartTestCase {

	public void testVisible() {
		assertTrue(getChartWithAxes().getLegend().isVisible());
		assertFalse(getChartWithoutAxes().getLegend().isVisible());

		getChartWithAxes().getLegend().setVisible(false);
		assertFalse(getChartWithAxes().getLegend().isVisible());
	}

	public void testTitle() {
		ILabel title = getChartWithAxes().getLegend().getTitle();
		assertTrue(title.isVisible());
		assertEquals(title.getCaption().getValue(), "LegendTitle");

		title.setVisible(false);
		assertFalse(title.isVisible());
	}

	public void testShowValue() {
		assertFalse(getChartWithAxes().getLegend().isShowValue());

		getChartWithAxes().getLegend().setShowValue(true);
		assertTrue(getChartWithAxes().getLegend().isShowValue());
	}
}
