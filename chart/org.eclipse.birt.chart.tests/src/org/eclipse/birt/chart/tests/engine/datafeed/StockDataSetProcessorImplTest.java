/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.tests.engine.datafeed;

import junit.framework.TestCase;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.extension.datafeed.StockDataSetProcessorImpl;
import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;

public class StockDataSetProcessorImplTest extends TestCase {
	StockDataSet sds;
	StockDataSetProcessorImpl sdsp;

	protected void setUp() throws Exception {
		super.setUp();
		StockEntry[] se = { new StockEntry(2.0, 1.0, 4.0, 3.0), new StockEntry(12.0, 11.0, 14.0, 13.0) };

		sds = StockDataSetImpl.create(se);
		sdsp = new StockDataSetProcessorImpl();
	}

	protected void tearDown() throws Exception {
		sds = null;
		sdsp = null;
		super.tearDown();
	}

	public void testGetMinimum() {
		try {
			assertEquals(new Double(1.0), sdsp.getMinimum(sds));
		} catch (ChartException ce) {
			ce.printStackTrace();
		}
	}

	public void testGetMaximum() {
		try {
			assertEquals(new Double(14.0), sdsp.getMaximum(sds));
		} catch (ChartException ce) {
			ce.printStackTrace();
		}
	}

}
