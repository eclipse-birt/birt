/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.aggregate;

import org.eclipse.birt.chart.extension.aggregate.Sum;

import junit.framework.TestCase;

/**
 * Test accumulate() and getAggregatedValue() methods of Sum.java
 */

public class SumTest extends TestCase {

	private Object sumResult() {
		final int seriesCount = 3;
		Sum sum = new Sum();
		final Object[] indexes = new Object[seriesCount];
		Object tuple;

		sum.initialize();

		for (int i = 0; i < seriesCount; i++) {
			indexes[i] = Integer.valueOf(i + 3);
			sum.accumulate(indexes[i]);
		}

		tuple = sum.getAggregatedValue();
		return tuple;
	}

	public void testAverage() throws Exception {

		Object expect = new Double(12.0);
		assertEquals(expect, this.sumResult());
	}

}
