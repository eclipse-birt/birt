/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.datafeed;

import junit.framework.TestCase;

import org.eclipse.birt.chart.extension.datafeed.StockEntry;

public class StockEntryTest extends TestCase {

	StockEntry stock1 = new StockEntry(3.12, 2.84, 3.22, 3.18);

	public void testStockValue1() {

		assertEquals(3.12, stock1.getOpen(), 0.00);
		assertEquals(2.84, stock1.getLow(), 0.00);
		assertEquals(3.22, stock1.getHigh(), 0.00);
		assertEquals(3.18, stock1.getClose(), 0.00);
	}

	Object[] VALUE = { new Double(1.07), new Double(0.99), new Double(1.00), new Double(1.05) };
	StockEntry stock2 = new StockEntry(VALUE);

	public void testStockValue2() {

		assertEquals(1.00, stock2.getOpen(), 0.00);
		assertEquals(0.99, stock2.getLow(), 0.00);
		assertEquals(1.07, stock2.getHigh(), 0.00);
		assertEquals(1.05, stock2.getClose(), 0.00);
	}
}
