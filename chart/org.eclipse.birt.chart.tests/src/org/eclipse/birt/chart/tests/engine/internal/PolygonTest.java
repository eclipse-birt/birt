/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.tests.engine.internal;

import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Polygon;

import junit.framework.TestCase;

public class PolygonTest extends TestCase {

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 *
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Collect and empty any objects that are used in multiple tests.
	 *
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testContain() {
		Polygon p1 = new Polygon();

		p1.add(0, 0);
		p1.add(100, 0);
		p1.add(50, 50);

		assertTrue(p1.contains(new Point(50, 10)));
		assertFalse(p1.contains(new Point(10, 20)));
	}

}
