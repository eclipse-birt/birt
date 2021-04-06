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

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.model.FittingCalculator;

public class FittingCalculatorTest extends TestCase {
	FittingCalculator fc1, fc2;

	double[] x = { 0, 1, 2, 3 };

	double[] y = { 5, 3, 7, 1 };

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		fc1 = new FittingCalculator(x, y, 0.33f);
		fc2 = new FittingCalculator(x, y, 0.10f);
	}

	/**
	 * Collect and empty any objects that are used in multiple tests.
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		fc1 = null;
		fc2 = null;
	}

	/**
	 * Test the Y estimation values.
	 *
	 */
	public void testYEst() {
		double[] result1 = fc1.getFittedValue();
		double[] result2 = fc2.getFittedValue();

		for (int i = 0; i < x.length; i++) {
			assertTrue(y[i] == result1[i]);
		}

		for (int i = 0; i < x.length; i++) {
			assertTrue(y[i] == result2[i]);
		}
	}

}
