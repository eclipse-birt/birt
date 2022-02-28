/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.computation;

import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Polygon;

import junit.framework.TestCase;

public class PolygonTest extends TestCase {

	Polygon po;

	@Override
	protected void setUp() throws Exception {
		po = new Polygon();
		po.add(-117, -55);
		po.add(0, -55);
		po.add(0, -36);
		po.add(-117, -36);
	}

	@Override
	protected void tearDown() throws Exception {
		po = null;
	}

	public void testContains() {
		assertTrue("Test inside the polygon", po.contains(new Point(-4.1, -48)));

		assertTrue("Test in the boundary line", po.contains(new Point(0, -48)));

		assertTrue("Test in the vertext", po.contains(new Point(0, -55)));

		assertFalse("Test outside the polygon", po.contains(new Point(0.1, -55)));

		assertFalse("Test outside the polygon", po.contains(new Point(-0.1, -148)));

		assertFalse("Test outside the polygon", po.contains(new Point(0, -148)));
	}

}
