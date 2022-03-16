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

package org.eclipse.birt.report.designer.util;

import junit.framework.TestCase;

/**
 * Test class for TableBorderCollisionArbiter, which implemented the CSS2.0
 * border collision algorithm.
 */

public class TableBorderCollisionArbiterTest extends TestCase {

	private int[] data;

	public void testRefreshBorderData() {
		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: none, 0px, black, x=0, y=0;
		TableBorderCollisionArbiter.refreshBorderData(data, 0, 0, 0, 0, 0);

		assertEquals(data[0], 1);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: none, 3px, black, x=0, y=0;
		TableBorderCollisionArbiter.refreshBorderData(data, 0, 3, 0, 0, 0);

		assertEquals(data[0], 1);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: solid, 1px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 1, 1, 255, 1, 1);

		assertEquals(data[0], 1);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: solid, 3px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 1, 3, 255, 1, 1);

		assertEquals(data[0], 1);
		assertEquals(data[1], 3);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: double, 1px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, -2, 1, 255, 1, 1);

		assertEquals(data[0], -2);
		assertEquals(data[1], 1);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: double, 4px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, -2, 4, 255, 1, 1);

		assertEquals(data[0], -2);
		assertEquals(data[1], 4);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: dashed, 1px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 2, 1, 255, 1, 1);

		assertEquals(data[0], 1);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: dashed, 2px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 2, 2, 255, 1, 1);

		assertEquals(data[0], 2);
		assertEquals(data[1], 2);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: dotted, 1px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 3, 1, 255, 1, 1);

		assertEquals(data[0], 1);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0, 0 };
		// New: dotted, 2px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 3, 2, 255, 1, 1);

		assertEquals(data[0], 3);
		assertEquals(data[1], 2);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: none, 0px, black, x=0, y=0.
		data = new int[] { 0, 0, 0, 0, 0, 0 };
		// New: dotted, 2px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 3, 2, 255, 1, 1);

		assertEquals(data[0], 3);
		assertEquals(data[1], 2);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: none, 6px, black, x=0, y=0.
		data = new int[] { 0, 6, 0, 0, 0, 0 };
		// New: dotted, 2px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 3, 2, 255, 1, 1);

		assertEquals(data[0], 3);
		assertEquals(data[1], 2);
		assertEquals(data[2], 255);
		assertEquals(data[3], 1);
		assertEquals(data[4], 1);

		// Source: none, 0px, black, x=0, y=0.
		data = new int[] { 0, 0, 0, 0, 0, 0 };
		// New: none, 2px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 0, 2, 255, 1, 1);

		assertEquals(data[0], 0);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

		// Source: none, 0px, black, x=0, y=0.
		data = new int[] { 0, 0, 0, 0, 0, 0 };
		// New: none, 0px, blue, x=1, y=1;
		TableBorderCollisionArbiter.refreshBorderData(data, 0, 0, 255, 1, 1);

		assertEquals(data[0], 0);
		assertEquals(data[1], 1);
		assertEquals(data[2], 0);
		assertEquals(data[3], 0);
		assertEquals(data[4], 0);

	}

	public void testCanExtend() {
		// Source: none, 0px, black, x=0, y=0.
		data = new int[] { 0, 0, 0, 0, 0 };
		// Neighbour:
		// opposite: 1px solid
		// left: 1px solid x=0 y=0
		// right: 1px solid x=0 y=1
		// Head: false
		// Vertical: false;
		assertEquals(false, TableBorderCollisionArbiter.canExtend(data, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, false, false));

		// Source: 1, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0 };
		// Neighbour:
		// opposite: 1px solid
		// left: 1px solid x=0 y=0
		// right: 1px solid x=0 y=1
		// Head: false
		// Vertical: false;
		assertEquals(false, TableBorderCollisionArbiter.canExtend(data, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, false, false));
	}

	public void testIsBrotherWin() {
		// Source: none, 0px, black, x=0, y=0.
		data = new int[] { 0, 0, 0, 0, 0 };
		// Neighbour:
		// opposite: 1px solid x=1 y=0
		// left: 1px solid x=0 y=0
		// right: 1px solid x=0 y=1
		// Head: false
		// Vertical: false;
		assertEquals(false,
				TableBorderCollisionArbiter.isBrotherWin(data, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, false, false));

		// Source: solid, 1px, black, x=0, y=0.
		data = new int[] { 1, 1, 0, 0, 0 };
		// Neighbour:
		// opposite: 1px solid x=1 y=0
		// left: 1px solid x=0 y=0
		// right: 1px solid x=0 y=1
		// Head: false
		// Vertical: false;
		assertEquals(false,
				TableBorderCollisionArbiter.isBrotherWin(data, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, false, false));

	}
}
