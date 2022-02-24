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

import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.report.model.api.extension.IColor;
import org.eclipse.birt.report.model.api.extension.IFont;

/**
 *
 */

public class ChartTest extends BaseChartTestCase {

	public void testTitle() {
		assertEquals("IChart.getTitle", getChartWithAxes().getTitle().getCaption().getValue(), "Bar Chart Title");

		String newTitle = "Test title";
		getChartWithAxes().getTitle().getCaption().setValue(newTitle);
		assertEquals("IChart.SetTitle", getChartWithAxes().getTitle().getCaption().getValue(), newTitle);

	}

	public void testTitleCaptionFont() {
		IFont font = getChartWithAxes().getTitle().getCaption().getFont();
		assertNotNull(font);
		assertEquals(new Float(font.getSize()), new Float(16));
		font.setSize(9);
		assertEquals(new Float(font.getSize()), new Float(9));

		assertTrue(font.isBold());
		font.setBold(false);
		assertFalse(font.isBold());
	}

	public void testTitleCaptionColor() {
		IColor color = getChartWithAxes().getTitle().getCaption().getColor();
		assertNotNull(color);

		assertEquals("Dummy color - Red", color.getRed(), 0);
		color.setRed(255);
		assertEquals(color.getRed(), 255);

		assertEquals("Dummy color - transparency", color.getTransparency(), 255);
		color.setTransparency(0);
		assertEquals(color.getTransparency(), 0);
	}

	public void testDescription() {
		assertEquals("IChart.getDescription", getChartWithAxes().getDescription().getValue(), "Description");

		String newDesc = "Test description";
		getChartWithAxes().getDescription().setValue(newDesc);
		assertEquals("IChart.SetDescription", getChartWithAxes().getDescription().getValue(), newDesc);

		// Description is null by default, simpe api will create a new one
		assertEquals("IChart.getDescription test null", getChartWithoutAxes().getDescription().getValue(), "");
		getChartWithoutAxes().getDescription().setValue(newDesc);
		assertEquals("IChart.SetDescription test null", getChartWithoutAxes().getDescription().getValue(), newDesc);
	}

	public void testColorByCategory() {
		assertFalse("IChart.isColorByCategory", getChartWithAxes().isColorByCategory());

		getChartWithAxes().setColorByCategory(true);
		assertTrue("IChart.setColorByCategory", getChartWithAxes().isColorByCategory());
	}

	public void testOutput() {
		assertEquals("IChart.getOutputType", getChartWithAxes().getOutputType().toUpperCase(), "PNG");

		getChartWithAxes().setOutputType("SVG");
		assertEquals("IChart.setOutputType", getChartWithAxes().getOutputType().toUpperCase(), "SVG");
	}

	public void testDimension() {
		assertEquals("IChart.getDimension", getChartWithAxes().getDimension(),
				ChartDimension.TWO_DIMENSIONAL_LITERAL.getName());

		getChartWithAxes().setDimension("ThreeDimensional");
		assertEquals("IChart.setDimension", getChartWithAxes().getDimension(),
				ChartDimension.THREE_DIMENSIONAL_LITERAL.getName());

		getChartWithAxes().setDimension("3d");
		assertEquals("Test invalid chart dimension", getChartWithAxes().getDimension(),
				ChartDimension.TWO_DIMENSIONAL_LITERAL.getName());
	}

	public void testGetCategory() {
		assertNotNull("IChart.getCategorySeries", getChartWithAxes().getCategory());
	}

	public void testFactory() {
		assertNotNull("IChart.getFactory", getChartWithAxes().getFactory());
	}

}
