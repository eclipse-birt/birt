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
