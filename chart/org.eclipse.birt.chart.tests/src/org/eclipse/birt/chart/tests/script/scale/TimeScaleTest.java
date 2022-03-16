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

package org.eclipse.birt.chart.tests.script.scale;

import java.util.Date;

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.script.api.scale.IScale;
import org.eclipse.birt.chart.script.api.scale.ITimeScale;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 *
 */

public class TimeScaleTest extends BaseChartTestCase {

	protected IScale getValueAxisScale() {
		return getChartWithAxes().getValueAxes()[0].getScale();
	}

	public void testStepSize() {
		IScale scale = getValueAxisScale();
		assertTrue(scale instanceof ITimeScale);

		ITimeScale ls = (ITimeScale) scale;
		assertEquals(ls.getStepSize(), 2);

		ls.setStepSize(3);
		assertEquals(ls.getStepSize(), 3);
	}

	public void testStepTimeUnit() {
		IScale scale = getValueAxisScale();
		assertTrue(scale instanceof ITimeScale);

		ITimeScale ls = (ITimeScale) scale;
		assertEquals(ls.getStepTimeUnit(), ScaleUnitType.WEEKS_LITERAL.getName());

		ls.setStepTimeUnit(ScaleUnitType.MONTHS_LITERAL.getName());
		assertEquals(ls.getStepTimeUnit(), ScaleUnitType.MONTHS_LITERAL.getName());
	}

	public void testMin() {
		IScale scale = getValueAxisScale();
		assertTrue(scale instanceof ITimeScale);

		ITimeScale ls = (ITimeScale) scale;
		assertEquals(1037635200984L, ls.getMin().getTime());

		ls.setMin(null);
		assertNull(ls.getMin());
	}

	public void testMax() {
		IScale scale = getValueAxisScale();
		assertTrue(scale instanceof ITimeScale);

		ITimeScale ls = (ITimeScale) scale;
		assertNull(ls.getMax());

		Date date = new Date();
		ls.setMax(date);
		assertEquals(ls.getMax(), date);

	}
}
