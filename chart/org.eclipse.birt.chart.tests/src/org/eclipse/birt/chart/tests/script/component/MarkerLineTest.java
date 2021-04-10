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

import org.eclipse.birt.chart.script.api.component.IMarkerLine;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class MarkerLineTest extends BaseChartTestCase {

	public void testTitle() {
		IMarkerLine line = getChartWithAxes().getValueAxes()[1].getMarkerLines()[0];
		assertEquals(line.getTitle().getCaption().getValue(), "");
		assertTrue(line.getTitle().isVisible());

		line.getTitle().getCaption().setValue("m");
		assertEquals(line.getTitle().getCaption().getValue(), "m");
	}

	public void testVisible() {
		IMarkerLine line = getChartWithAxes().getValueAxes()[1].getMarkerLines()[0];
		assertTrue(line.isVisible());

		line.setVisible(false);
		assertFalse(line.isVisible());
	}

	public void testValue() {
		IMarkerLine line = getChartWithAxes().getValueAxes()[1].getMarkerLines()[0];
		assertTrue(line.getValue() instanceof INumberDataElement);

		INumberDataElement data = (INumberDataElement) line.getValue();
		assertTrue(data.getValue() == 10105);

		line.setValue(getChartWithAxes().getFactory().createNumberElement(10106));
		data = (INumberDataElement) line.getValue();
		assertTrue(data.getValue() == 10106);
	}

}
