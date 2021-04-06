/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.ChartType;

public class ChartTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(ChartType.PIE, ChartType.PIE_LITERAL.getValue());
		assertEquals(ChartType.BAR, ChartType.BAR_LITERAL.getValue());
		assertEquals(ChartType.LINE, ChartType.LINE_LITERAL.getValue());
		assertEquals(ChartType.COMBO, ChartType.COMBO_LITERAL.getValue());
		assertEquals(ChartType.SCATTER, ChartType.SCATTER_LITERAL.getValue());
		assertEquals(ChartType.STOCK, ChartType.STOCK_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(ChartType.PIE_LITERAL, ChartType.get(ChartType.PIE));
		assertEquals(ChartType.BAR_LITERAL, ChartType.get(ChartType.BAR));
		assertEquals(ChartType.LINE_LITERAL, ChartType.get(ChartType.LINE));
		assertEquals(ChartType.COMBO_LITERAL, ChartType.get(ChartType.COMBO));

		assertEquals(ChartType.PIE_LITERAL, ChartType.get("Pie")); //$NON-NLS-1$
		assertEquals(ChartType.BAR_LITERAL, ChartType.get("Bar")); //$NON-NLS-1$
		assertEquals(ChartType.LINE_LITERAL, ChartType.get("Line")); //$NON-NLS-1$
		assertEquals(ChartType.COMBO_LITERAL, ChartType.get("Combo")); //$NON-NLS-1$
		assertEquals(ChartType.SCATTER_LITERAL, ChartType.get("Scatter")); //$NON-NLS-1$
		assertEquals(ChartType.STOCK_LITERAL, ChartType.get("Stock")); //$NON-NLS-1$
		assertNull(ChartType.get("No Match")); //$NON-NLS-1$
	}
}
