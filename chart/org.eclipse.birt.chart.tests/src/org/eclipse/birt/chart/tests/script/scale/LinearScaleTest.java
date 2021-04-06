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

package org.eclipse.birt.chart.tests.script.scale;

import org.eclipse.birt.chart.script.api.scale.ILinearScale;
import org.eclipse.birt.chart.script.api.scale.IScale;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class LinearScaleTest extends BaseChartTestCase {

	protected IScale getValueAxisScale(int index) {
		return getChartWithAxes().getValueAxes()[index].getScale();
	}

	public void testStepSize() {
		IScale scale = getValueAxisScale(1);
		assertTrue(scale instanceof ILinearScale);

		ILinearScale ls = (ILinearScale) scale;
		assertEquals(ls.getStepSize(), 2);

		ls.setStepSize(3);
		assertEquals(ls.getStepSize(), 3);

		scale = getValueAxisScale(2);
		assertTrue(scale instanceof ILinearScale);
		assertEquals(((ILinearScale) scale).getStepSize(), 0);
	}

	public void testNumberOfSteps() {
		IScale scale = getValueAxisScale(2);
		assertTrue(scale instanceof ILinearScale);

		ILinearScale ls = (ILinearScale) scale;
		assertEquals(ls.getNumberOfSteps(), 5);

		ls.setNumberOfSteps(6);
		assertEquals(ls.getNumberOfSteps(), 6);

		scale = getValueAxisScale(1);
		assertTrue(scale instanceof ILinearScale);
		assertEquals(((ILinearScale) scale).getNumberOfSteps(), 0);
	}

	public void testMin() {
		IScale scale = getValueAxisScale(1);
		assertTrue(scale instanceof ILinearScale);

		ILinearScale ls = (ILinearScale) scale;
		assertEquals((int) ls.getMin(), 10100);

		ls.setMin(10101);
		assertEquals((int) ls.getMin(), 10101);

		scale = getValueAxisScale(2);
		assertTrue("Axis 3 doesn't set min", Double.isNaN(((ILinearScale) scale).getMin()));
	}

	public void testMax() {
		IScale scale = getValueAxisScale(1);
		assertTrue(scale instanceof ILinearScale);

		ILinearScale ls = (ILinearScale) scale;
		assertEquals((int) ls.getMax(), 10110);

		ls.setMax(10111);
		assertEquals((int) ls.getMax(), 10111);

		scale = getValueAxisScale(2);
		assertTrue("Axis 3 doesn't set max", Double.isNaN(((ILinearScale) scale).getMax()));
	}
}
