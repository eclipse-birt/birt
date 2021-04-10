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

import org.eclipse.birt.chart.script.api.component.IMarkerRange;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class MarkerRangeTest extends BaseChartTestCase {

	public void testTitle() {
		IMarkerRange range = getChartWithAxes().getCategoryAxis().getMarkerRanges()[0];
		assertEquals(range.getTitle().getCaption().getValue(), "");
		assertTrue(range.getTitle().isVisible());

		range.getTitle().getCaption().setValue("m");
		assertEquals(range.getTitle().getCaption().getValue(), "m");
	}

	public void testVisible() {
		IMarkerRange range = getChartWithAxes().getCategoryAxis().getMarkerRanges()[0];
		assertTrue(range.isVisible());

		range.setVisible(false);
		assertFalse(range.isVisible());
	}

	public void testStartValue() {
		IMarkerRange range = getChartWithAxes().getCategoryAxis().getMarkerRanges()[0];
		assertTrue(range.getStartValue() instanceof INumberDataElement);

		INumberDataElement data = (INumberDataElement) range.getStartValue();
		assertTrue(data.getValue() == 0);

		range.setStartValue(getChartWithAxes().getFactory().createNumberElement(1));
		data = (INumberDataElement) range.getStartValue();
		assertTrue(data.getValue() == 1);
	}

	public void testEndValue() {
		IMarkerRange range = getChartWithAxes().getCategoryAxis().getMarkerRanges()[0];
		assertTrue(range.getEndValue() instanceof INumberDataElement);

		INumberDataElement data = (INumberDataElement) range.getEndValue();
		assertTrue(data.getValue() == 5);

		range.setEndValue(getChartWithAxes().getFactory().createNumberElement(6));
		data = (INumberDataElement) range.getEndValue();
		assertTrue(data.getValue() == 6);
	}

}
