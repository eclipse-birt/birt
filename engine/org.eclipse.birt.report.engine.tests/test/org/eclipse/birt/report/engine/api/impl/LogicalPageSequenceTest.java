/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import junit.framework.TestCase;

public class LogicalPageSequenceTest extends TestCase {

	public void testMerge() {
		long[][] pages1 = { new long[] { 200, 700 }, new long[] { 900, 1200 }, new long[] { 1500, 1600 } };

		long[][] pages2 = { new long[] { 100, 300 }, new long[] { 400, 500 }, new long[] { 600, 800 },
				new long[] { 1000, 1100 }, new long[] { 1300, 1400 } };

		ArrayList<long[][]> pages = new ArrayList<>();
		pages.add(pages1);
		pages.add(pages2);

		LogicalPageSequence sequence = new LogicalPageSequence(pages);
		assertEquals(404, sequence.getTotalVisiblePageCount());
		assertEquals("[200-300],[400-500],[600-700],[1000-1100]", toString(sequence.getVisiblePages()));
	}

	public void testMergeWithMax() {
		long[][] pages1 = { new long[] { 200, 700 }, new long[] { 900, 1200 }, new long[] { 1500, 1600 } };

		long[][] pages2 = { new long[] { 100, 300 }, new long[] { 400, 500 }, new long[] { 600, 800 },
				new long[] { 1000, 1100 }, new long[] { 1300, 1400 } };

		ArrayList<long[][]> pages = new ArrayList<>();
		pages.add(pages1);
		pages.add(pages2);

		LogicalPageSequence sequence = new LogicalPageSequence(pages, 659);
		assertEquals(262, sequence.getTotalVisiblePageCount());
		assertEquals("[200-300],[400-500],[600-659]", toString(sequence.getVisiblePages()));
	}

	public void testGetLogicalPageNumber() {
		LogicalPageSequence sequence = new LogicalPageSequence(
				new long[][] { new long[] { 100, 199 }, new long[] { 300, 399 } });
		assertEquals(200, sequence.getTotalVisiblePageCount());
		long logicalNumber = sequence.getLogicalPageNumber(1);
		assertEquals(-1, logicalNumber);
		assertEquals(1, sequence.getLogicalPageNumber(100));
		assertEquals(100, sequence.getLogicalPageNumber(199));
		assertEquals(-1, sequence.getLogicalPageNumber(201));
		assertEquals(101, sequence.getLogicalPageNumber(300));
		assertEquals(200, sequence.getLogicalPageNumber(399));
		assertEquals(-1, sequence.getLogicalPageNumber(400));
	}

	public void testGetPhysicalPageSequence() {
		LogicalPageSequence sequence = new LogicalPageSequence(
				new long[][] { new long[] { 100, 199 }, new long[] { 300, 399 } });

		long[][] pages = sequence.getPhysicalPageNumbers(new long[][] { new long[] { 1, 200 } });
		assertEquals("[100-199],[300-399]", toString(pages));
		pages = sequence.getPhysicalPageNumbers(
				new long[][] { new long[] { 10, 20 }, new long[] { 99, 102 }, new long[] { 200, 200 } });
		assertEquals("[109-119],[198-199],[300-301],[399-399]", toString(pages));
	}

	public String toString(long[][] pages) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < pages.length; i++) {
			builder.append("[");
			builder.append(pages[i][0]);
			builder.append("-");
			builder.append(pages[i][1]);
			builder.append("]");
			builder.append(",");
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

}
