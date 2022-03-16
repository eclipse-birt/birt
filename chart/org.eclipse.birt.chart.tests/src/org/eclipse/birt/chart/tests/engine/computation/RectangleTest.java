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
import org.eclipse.birt.chart.computation.Rectangle;

import junit.framework.TestCase;

public class RectangleTest extends TestCase {

	Rectangle r;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		r = new Rectangle();
		r.setRect(1.0, 1.0, 4.0, 4.0);
	}

	@Override
	protected void tearDown() throws Exception {
		r = null;
		super.tearDown();
	}

	public void testGet() {
		assertEquals(r.getHeight(), 4.0, 0);
		assertEquals(r.getWidth(), 4.0, 0);
		assertEquals(r.getX(), 1.0, 0);
		assertEquals(r.getY(), 1.0, 0);
		assertEquals(r.getMinX(), 1.0, 0);
		assertEquals(r.getMinY(), 1.0, 0);
		assertEquals(r.getMaxX(), 5.0, 0);
		assertEquals(r.getMaxY(), 5.0, 0);
	}

	public void testOutcode() {
		assertEquals(r.outcode(0.0, 1.0), 1);
		assertEquals(r.outcode(1.0, 0.0), 2);
		assertEquals(r.outcode(0.0, 0.0), 3);
		assertEquals(r.outcode(6.0, 5.0), 4);
		assertEquals(r.outcode(6.0, 0.0), 6);
		assertEquals(r.outcode(5.0, 6.0), 8);
		assertEquals(r.outcode(0.0, 6.0), 9);
	}

	public void testIsEmpty() {
		assertEquals(r.isEmpty(), false);
	}

	public void testContain() {
		assertEquals(r.contains(new Point(0.0, 0.0)), false);
		assertEquals(r.contains(new Point(2.0, 2.0)), true);
	}

}
