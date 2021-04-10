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
