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

package org.eclipse.birt.report.engine.internal.executor.doc;

import junit.framework.TestCase;

public class FragmentTest extends TestCase {

	public void testFragment() {

		Fragment fragment = new Fragment(new LongComparator());
		Object[] leftEdge = new Long[] { 0L };
		Object[] rightEdge = new Long[] { 2L };
		fragment.addSection(leftEdge, rightEdge);

		leftEdge = new Long[] { 4L };
		rightEdge = new Long[] { 5L };
		fragment.addSection(leftEdge, rightEdge);

		leftEdge = new Long[] { 7L };
		rightEdge = new Long[] { 7L };
		fragment.addSection(leftEdge, rightEdge);

		fragment.build();

		assertTrue(fragment.inFragment(0L));
		assertTrue(fragment.inFragment(1L));
		assertTrue(fragment.inFragment(2L));
		assertTrue(!fragment.inFragment(3L));
		assertTrue(fragment.inFragment(4L));
		assertTrue(fragment.inFragment(5L));
		assertTrue(!fragment.inFragment(6L));
		assertTrue(fragment.inFragment(7L));

		assertEquals(0L, fragment.getFragment(0L).index);
		assertEquals(null, fragment.getFragment(1L));
		assertEquals(2L, fragment.getFragment(2L).index);
		assertEquals(null, fragment.getFragment(3L));
		assertEquals(4L, fragment.getFragment(4L).index);
		assertEquals(5L, fragment.getFragment(5L).index);
		assertEquals(null, fragment.getFragment(6L));
		assertEquals(7L, fragment.getFragment(7L).index);

		assertEquals(2L, fragment.getNextFragment(0L).index);
		assertEquals(2L, fragment.getNextFragment(1L).index);
		assertEquals(4L, fragment.getNextFragment(2L).index);
		assertEquals(4L, fragment.getNextFragment(3L).index);
		assertEquals(5L, fragment.getNextFragment(4L).index);
		assertEquals(7L, fragment.getNextFragment(5L).index);
		assertEquals(7L, fragment.getNextFragment(6L).index);
		assertEquals(null, fragment.getNextFragment(7L));
	}

	public void testEdgeInsert() {
		Fragment fragment = new Fragment(new LongComparator());
		Object[] leftEdge = new Long[] { 4L };
		Object[] rightEdge = new Long[] { 5L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[4, 5]", fragment.printEdges());

		leftEdge = new Long[] { 5L };
		rightEdge = new Long[] { 7L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[4, 7]", fragment.printEdges());

		leftEdge = new Long[] { 10L };
		rightEdge = new Long[] { 15L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[4, 7][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 0L };
		rightEdge = new Long[] { 1L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][4, 7][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 2L };
		rightEdge = new Long[] { 4L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 7][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 5L };
		rightEdge = new Long[] { 6L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 7][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 8L };
		rightEdge = new Long[] { 9L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 7][8, 9][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 6L };
		rightEdge = new Long[] { 9L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 9][10, 15]", fragment.printEdges());

		leftEdge = new Long[] { 17L };
		rightEdge = new Long[] { 19L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 9][10, 15][17, 19]", fragment.printEdges());

		leftEdge = new Long[] { 16L };
		rightEdge = new Long[] { 16L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 1][2, 9][10, 15][16, 16][17, 19]", fragment.printEdges());

		leftEdge = new Long[] { 1L };
		rightEdge = new Long[] { 2L };
		fragment.addSection(leftEdge, rightEdge);
		assertEquals("[0, 9][10, 15][16, 16][17, 19]", fragment.printEdges());

	}

}
