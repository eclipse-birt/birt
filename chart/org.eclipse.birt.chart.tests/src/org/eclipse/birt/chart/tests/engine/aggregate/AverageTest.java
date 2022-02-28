/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.aggregate;

import org.eclipse.birt.chart.extension.aggregate.Average;

import junit.framework.TestCase;

/**
 * Test accumulate() and getAggregatedValue() methods of Average.java
 */

public class AverageTest extends TestCase {

	private Object averageResult() {
		final int seriesCount = 3;
		Average ave = new Average();
		final Object[] indexes = new Object[seriesCount];
		Object tuple;

		ave.initialize();

		for (int i = 0; i < seriesCount; i++) {
			indexes[i] = Integer.valueOf(i + 3);
			ave.accumulate(indexes[i]);
		}

		tuple = ave.getAggregatedValue();
		return tuple;
	}

	public void testAverage() throws Exception {

		Object expect = new Double(4.0);
		assertEquals(expect, this.averageResult());
	}

}
