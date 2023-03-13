
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 *
 */

public class OrderedDiskArrayTest {
	int N = 100;

	@Test
	public void testAddObject1() {
		List list = new OrderedDiskArray();
		for (int i = 0; i < N; i++) {
			list.add(new Integer(i));
		}
		assertEquals(100, list.size());
		for (int i = 0; i < N; i++) {
			assertEquals(new Integer(i), list.get(i));
		}
	}

	@Test
	public void testAddObject2() {
		List list = new OrderedDiskArray(10, true);
		for (int i = N - 1; i >= 0; i--) {
			list.add(new Integer(i));
		}
		assertEquals(10, list.size());
		for (int i = 0; i < 10; i++) {
			assertEquals(new Integer(90 + i), list.get(i));
		}
	}

	@Test
	public void testAddObject3() {
		List list = new OrderedDiskArray(10, false);
		for (int i = 0; i < N; i++) {
			list.add(new Integer(i));
		}
		assertEquals(10, list.size());
		for (int i = 0; i < 10; i++) {
			assertEquals(new Integer(i), list.get(i));
		}
	}

	@Test
	public void testAddObject4() {
		List list = new OrderedDiskArray(10, false);
		for (int i = 0; i < N; i++) {
			list.add(new Integer(i));
		}
		assertEquals(10, list.size());
		for (int i = 0; i < 10; i++) {
			assertEquals(new Integer(i), list.get(i));
		}
	}
}
