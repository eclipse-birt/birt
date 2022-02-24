
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class SetUtilTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testintersect1() throws IOException {
		int LIST_BUFFER_SIZE = 4000;
		PrimitiveDiskSortedStack[] stacks = new PrimitiveDiskSortedStack[4];
		stacks[0] = new PrimitiveDiskSortedStack(LIST_BUFFER_SIZE, true, true);
		stacks[0].push(new Integer(10));
		stacks[0].push(new Integer(11));
		stacks[0].push(new Integer(12));
		stacks[0].push(new Integer(13));
		stacks[0].push(new Integer(14));
		stacks[0].push(new Integer(15));
		stacks[0].push(new Integer(16));
		stacks[1] = new PrimitiveDiskSortedStack(LIST_BUFFER_SIZE, true, true);
		stacks[1].push(new Integer(1));
		stacks[1].push(new Integer(2));
		stacks[1].push(new Integer(3));
		stacks[1].push(new Integer(16));
		stacks[1].push(new Integer(14));
		stacks[1].push(new Integer(15));
		stacks[1].push(new Integer(13));
		stacks[2] = new PrimitiveDiskSortedStack(LIST_BUFFER_SIZE, true, true);
		stacks[2].push(new Integer(1));
		stacks[2].push(new Integer(2));
		stacks[2].push(new Integer(3));
		stacks[2].push(new Integer(13));
		stacks[2].push(new Integer(12));
		stacks[2].push(new Integer(15));
		stacks[2].push(new Integer(11));
		stacks[3] = new PrimitiveDiskSortedStack(LIST_BUFFER_SIZE, true, true);
		stacks[3].push(new Integer(1));
		stacks[3].push(new Integer(2));
		stacks[3].push(new Integer(3));
		stacks[3].push(new Integer(13));
		stacks[3].push(new Integer(14));
		stacks[3].push(new Integer(17));
		stacks[3].push(new Integer(10));
		IDiskArray result = SetUtil.getIntersection(stacks);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), new Integer(13));
		result.close();
		for (PrimitiveDiskSortedStack s : stacks) {
			s.close();
		}
	}

	@Test
	public void testintersect2() throws IOException {
		BufferedPrimitiveDiskArray[] lists = new BufferedPrimitiveDiskArray[4];
		lists[0] = new BufferedPrimitiveDiskArray();
		lists[0].add(new Integer(101));
		lists[0].add(new Integer(11));
		lists[0].add(new Integer(12));
		lists[0].add(new Integer(13));
		lists[0].add(new Integer(14));
		lists[0].add(new Integer(15));
		lists[0].add(new Integer(16));
		lists[1] = new BufferedPrimitiveDiskArray();
		lists[1].add(new Integer(11));
		lists[1].add(new Integer(2));
		lists[1].add(new Integer(3));
		lists[1].add(new Integer(16));
		lists[1].add(new Integer(14));
		lists[1].add(new Integer(15));
		lists[1].add(new Integer(13));
		lists[2] = new BufferedPrimitiveDiskArray();
		lists[2].add(new Integer(1));
		lists[2].add(new Integer(2));
		lists[2].add(new Integer(3));
		lists[2].add(new Integer(13));
		lists[2].add(new Integer(12));
		lists[2].add(new Integer(15));
		lists[2].add(new Integer(11));
		lists[3] = new BufferedPrimitiveDiskArray();
		lists[3].add(new Integer(1));
		lists[3].add(new Integer(2));
		lists[3].add(new Integer(3));
		lists[3].add(new Integer(13));
		lists[3].add(new Integer(14));
		lists[3].add(new Integer(17));
		lists[3].add(new Integer(10));
		IDiskArray result = SetUtil.getIntersection(lists);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), new Integer(13));
		result.close();
		for (BufferedPrimitiveDiskArray s : lists) {
			s.close();
		}
	}

	@Test
	public void testintersect3() throws IOException {
		BufferedPrimitiveDiskArray[] lists = new BufferedPrimitiveDiskArray[4];
		lists[0] = new BufferedPrimitiveDiskArray();
		lists[0].add(new Integer(13));
		lists[1] = new BufferedPrimitiveDiskArray();
		lists[1].add(new Integer(11));
		lists[1].add(new Integer(2));
		lists[1].add(new Integer(3));
		lists[1].add(new Integer(16));
		lists[1].add(new Integer(14));
		lists[1].add(new Integer(15));
		lists[1].add(new Integer(13));
		lists[2] = new BufferedPrimitiveDiskArray();
		lists[2].add(new Integer(1));
		lists[2].add(new Integer(2));
		lists[2].add(new Integer(3));
		lists[2].add(new Integer(13));
		lists[2].add(new Integer(12));
		lists[2].add(new Integer(15));
		lists[2].add(new Integer(11));
		lists[3] = new BufferedPrimitiveDiskArray();
		lists[3].add(new Integer(1));
		lists[3].add(new Integer(2));
		lists[3].add(new Integer(3));
		lists[3].add(new Integer(13));
		lists[3].add(new Integer(14));
		lists[3].add(new Integer(17));
		lists[3].add(new Integer(10));
		IDiskArray result = SetUtil.getIntersection(lists);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), new Integer(13));
	}

	@Test
	public void testintersect4() throws IOException {
		BufferedPrimitiveDiskArray[] lists = new BufferedPrimitiveDiskArray[4];
		lists[0] = new BufferedPrimitiveDiskArray();
		lists[0].add(new Integer(13));
		lists[1] = new BufferedPrimitiveDiskArray();
		lists[1].add(new Integer(11));
		lists[1].add(new Integer(2));
		lists[1].add(new Integer(3));
		lists[1].add(new Integer(16));
		lists[1].add(new Integer(14));
		lists[1].add(new Integer(15));
		lists[1].add(new Integer(13));
		lists[2] = new BufferedPrimitiveDiskArray();
		lists[2].add(new Integer(1));
		lists[2].add(new Integer(2));
		lists[2].add(new Integer(3));
		lists[2].add(new Integer(13));
		lists[2].add(new Integer(12));
		lists[2].add(new Integer(15));
		lists[2].add(new Integer(11));
		lists[3] = new BufferedPrimitiveDiskArray();
		lists[3].add(new Integer(1));
		lists[3].add(new Integer(2));
		lists[3].add(new Integer(3));
		lists[3].add(new Integer(19));
		lists[3].add(new Integer(14));
		lists[3].add(new Integer(17));
		lists[3].add(new Integer(10));
		IDiskArray result = SetUtil.getIntersection(lists);
		assertEquals(result.size(), 0);
		result.close();
		for (BufferedPrimitiveDiskArray s : lists) {
			s.close();
		}
	}

	@Test
	public void testintersect5() throws IOException {
		BufferedPrimitiveDiskArray[] lists = new BufferedPrimitiveDiskArray[4];
		lists[0] = new BufferedPrimitiveDiskArray();
		lists[0].add(new Integer(13));
		lists[1] = new BufferedPrimitiveDiskArray();
		lists[1].add(new Integer(11));
		lists[1].add(new Integer(2));
		lists[1].add(new Integer(3));
		lists[1].add(new Integer(16));
		lists[1].add(new Integer(14));
		lists[1].add(new Integer(15));
		lists[1].add(new Integer(13));
		lists[2] = new BufferedPrimitiveDiskArray();
		lists[2].add(new Integer(1));
		lists[2].add(new Integer(2));
		lists[2].add(new Integer(3));
		lists[2].add(new Integer(13));
		lists[2].add(new Integer(12));
		lists[2].add(new Integer(15));
		lists[2].add(new Integer(11));
		lists[3] = new BufferedPrimitiveDiskArray();
		lists[3].add(new Integer(12));
		IDiskArray result = SetUtil.getIntersection(lists);
		assertEquals(result.size(), 0);
		result.close();
		for (BufferedPrimitiveDiskArray s : lists) {
			s.close();
		}
	}
}
