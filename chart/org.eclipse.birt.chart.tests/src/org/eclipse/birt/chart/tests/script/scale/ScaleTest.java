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

import org.eclipse.birt.chart.script.api.scale.IScale;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class ScaleTest extends BaseChartTestCase {

	public void testAuto() {
		assertTrue(getCategoryAxisScale().isAuto());
		assertFalse(getValueAxisScale(0).isAuto());
		assertFalse(getValueAxisScale(1).isAuto());

		getValueAxisScale(0).setAuto();
		assertTrue(getValueAxisScale(0).isAuto());
	}

	public void testCategory() {
		assertTrue(getCategoryAxisScale().isCategory());
		assertFalse(getValueAxisScale(0).isCategory());

		getCategoryAxisScale().setCategory(false);
		assertFalse(getCategoryAxisScale().isCategory());
	}

	protected IScale getCategoryAxisScale() {
		return getChartWithAxes().getCategoryAxis().getScale();
	}

	protected IScale getValueAxisScale(int index) {
		return getChartWithAxes().getValueAxes()[index].getScale();
	}
}
