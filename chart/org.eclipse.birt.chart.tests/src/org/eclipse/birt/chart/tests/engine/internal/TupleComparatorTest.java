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

import java.util.Date;

import org.eclipse.birt.chart.internal.datafeed.TupleComparator;

import junit.framework.TestCase;

/**
 * Test class for checking the correctness of compareObjects() method in
 * TupleComparator.java class.
 */

public class TupleComparatorTest extends TestCase {

	public void testCompareObjects() {

		assertEquals(0, TupleComparator.compareObjects(null, null, null));
		assertEquals(-1, TupleComparator.compareObjects(null, "abc", null)); //$NON-NLS-1$
		assertEquals(1, TupleComparator.compareObjects("abc", null, null)); //$NON-NLS-1$

		assertEquals(1, TupleComparator.compareObjects("abc", "a", null)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-1, TupleComparator.compareObjects("ab", "b", null)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, TupleComparator.compareObjects("abc", "abc", null)); //$NON-NLS-1$ //$NON-NLS-2$

		Integer n1 = 80;
		Integer n2 = 60;
		assertEquals(1, TupleComparator.compareObjects(n1, n2, null));
		assertEquals(-1, TupleComparator.compareObjects(n2, n1, null));

		Date a = new Date((long) 8E8);
		Date b = new Date((long) 7E9);
		assertEquals(-1, TupleComparator.compareObjects(a, b, null));
		assertEquals(1, TupleComparator.compareObjects(b, a, null));
	}
}
