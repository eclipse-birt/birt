/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.Comparator;

import junit.framework.TestCase;

public class SegmentTest extends TestCase {

	public void testSegment() {
		Comparator comparator = new LongComparator();
		// ALL
		Segment segment = new Segment(comparator);
		segment.startSegment(Segment.LEFT_MOST_EDGE);
		segment.endSegment(Segment.RIGHT_MOST_EDGE);
		assertEquals("[ALL]", segment.toString());

		// NONE
		segment = new Segment(comparator);
		segment.startSegment(Segment.LEFT_MOST_EDGE);
		segment.endSegment(Segment.LEFT_MOST_EDGE);
		assertEquals("[NONE]", segment.toString());

		// SINGLE ELMENT
		segment = new Segment(comparator);
		segment.startSegment(3L);
		segment.endSegment(3L);
		assertEquals("[3-3]", segment.toString());

		segment = new Segment(comparator);
		segment.endSegment(3L);
		segment.startSegment(3L);
		assertEquals("[ALL]", segment.toString());

		// left open segment
		segment = new Segment(comparator);
		;
		segment.endSegment(3L);
		assertEquals("[-3]", segment.toString());

		// right open segment
		segment = new Segment(comparator);
		segment.startSegment(3L);
		assertEquals("[3-]", segment.toString());

		// cross segment
		segment = new Segment(comparator);
		segment.endSegment(3L);
		segment.startSegment(6L);
		segment.endSegment(10L);
		segment.startSegment(10L);
		segment.endSegment(12L);
		segment.startSegment(15L);
		assertEquals("[-3][6-12][15-]", segment.toString());

	}
}
