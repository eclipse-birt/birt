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

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.scale.ICategoryScale;
import org.eclipse.birt.chart.script.api.scale.ILinearScale;
import org.eclipse.birt.chart.script.api.scale.ITimeScale;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class AxisTest extends BaseChartTestCase {

	public void testVisible() {
		assertTrue(getChartWithAxes().getCategoryAxis().isVisible());
		assertTrue(getChartWithAxes().getValueAxes()[0].isVisible());
		assertFalse(getChartWithAxes().getValueAxes()[2].isVisible());

		getChartWithAxes().getValueAxes()[2].setVisible(true);
		assertTrue(getChartWithAxes().getValueAxes()[2].isVisible());
	}

	public void testTitle() {
		assertEquals(getChartWithAxes().getCategoryAxis().getTitle().getCaption().getValue(), "X-Axis Title");
		assertEquals(getChartWithAxes().getValueAxes()[0].getTitle().getCaption().getValue(), "Y-Axis Title");
		assertFalse(getChartWithAxes().getValueAxes()[1].getTitle().isVisible());

		ILabel title = getChartWithAxes().getValueAxes()[2].getTitle();
		title.setVisible(true);
		String text = "Y Axis 3";
		title.getCaption().setValue(text);
		assertTrue(title.isVisible());
		assertEquals(title.getCaption().getValue(), text);
	}

	public void testGetScale() {
		assertTrue(getChartWithAxes().getCategoryAxis().getScale() instanceof ICategoryScale);
		assertTrue(getChartWithAxes().getValueAxes()[0].getScale() instanceof ITimeScale);
		assertTrue(getChartWithAxes().getValueAxes()[1].getScale() instanceof ILinearScale);
	}

	public void testGetMarkerLines() {
		assertEquals(getChartWithAxes().getCategoryAxis().getMarkerLines().length, 0);
		assertEquals(getChartWithAxes().getValueAxes()[0].getMarkerLines().length, 0);
		assertEquals(getChartWithAxes().getValueAxes()[1].getMarkerLines().length, 1);
		assertEquals(getChartWithAxes().getValueAxes()[2].getMarkerLines().length, 0);
	}

	public void testGetMarkerRanges() {
		assertEquals(getChartWithAxes().getCategoryAxis().getMarkerRanges().length, 1);
		assertEquals(getChartWithAxes().getValueAxes()[0].getMarkerRanges().length, 0);
		assertEquals(getChartWithAxes().getValueAxes()[1].getMarkerRanges().length, 0);
		assertEquals(getChartWithAxes().getValueAxes()[2].getMarkerRanges().length, 0);
	}

	public void testType() {
		assertEquals(getChartWithAxes().getCategoryAxis().getType(), AxisType.DATE_TIME_LITERAL.getName());
		assertEquals(getChartWithAxes().getValueAxes()[0].getType(), AxisType.DATE_TIME_LITERAL.getName());
		assertEquals(getChartWithAxes().getValueAxes()[1].getType(), AxisType.LINEAR_LITERAL.getName());
		assertEquals(getChartWithAxes().getValueAxes()[2].getType(), AxisType.LINEAR_LITERAL.getName());

		getChartWithAxes().getCategoryAxis().setType(AxisType.TEXT_LITERAL.getName());
		assertEquals("Test setting axis type", getChartWithAxes().getCategoryAxis().getType(),
				AxisType.TEXT_LITERAL.getName());

		getChartWithAxes().getCategoryAxis().setType("");
		assertEquals("Test invalid axis type", getChartWithAxes().getCategoryAxis().getType(),
				AxisType.LINEAR_LITERAL.getName());
	}

}
