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

public class SegmentTest extends TestCase
{



	public void testSegment( )
	{
		Comparator comparator = new LongComparator( );
		// ALL
		Segment segment = new Segment( comparator );
		segment.startSegment( Segment.LEFT_MOST_EDGE );
		segment.endSegment( Segment.RIGHT_MOST_EDGE );
		assertEquals( "[ALL]", segment.toString( ) );

		// NONE
		segment = new Segment( comparator );
		segment.startSegment( Segment.LEFT_MOST_EDGE );
		segment.endSegment( Segment.LEFT_MOST_EDGE );
		assertEquals( "[NONE]", segment.toString( ) );

		// SINGLE ELMENT
		segment = new Segment( comparator );
		segment.startSegment( new Long( 3 ) );
		segment.endSegment( new Long( 3 ) );
		assertEquals( "[3-3]", segment.toString( ) );

		segment = new Segment( comparator );
		segment.endSegment( new Long( 3 ) );
		segment.startSegment( new Long( 3 ) );
		assertEquals( "[ALL]", segment.toString( ) );

		// left open segment
		segment = new Segment( comparator );;
		segment.endSegment( new Long( 3 ) );
		assertEquals( "[-3]", segment.toString( ) );

		// right open segment
		segment = new Segment( comparator );
		segment.startSegment( new Long( 3 ) );
		assertEquals( "[3-]", segment.toString( ) );

		// cross segment
		segment = new Segment( comparator );
		segment.endSegment( new Long( 3 ) );
		segment.startSegment( new Long( 6 ) );
		segment.endSegment( new Long( 10 ) );
		segment.startSegment( new Long( 10 ) );
		segment.endSegment( new Long( 12 ) );
		segment.startSegment( new Long( 15 ) );
		assertEquals( "[-3][6-12][15-]", segment.toString( ) );

	}
}
