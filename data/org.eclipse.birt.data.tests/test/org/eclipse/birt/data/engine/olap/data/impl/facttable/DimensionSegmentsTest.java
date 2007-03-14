
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivision;

import junit.framework.TestCase;


/**
 * 
 */

public class DimensionSegmentsTest extends TestCase
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException 
	 */
	public void testDimensionSegments( ) throws IOException, BirtException
	{
		DimensionDivision dimSegments = new DimensionDivision( 2, 1 );
		assertEquals( dimSegments.ranges.length, 1 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 1 );
		
		dimSegments = new DimensionDivision( 1, 1 );
		assertEquals( dimSegments.ranges.length, 1 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 0 );
		
		dimSegments = new DimensionDivision( 3, 3 );
		assertEquals( dimSegments.ranges.length, 3 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 0 );
		assertEquals( dimSegments.ranges[1].start, 1 );
		assertEquals( dimSegments.ranges[1].end, 1 );
		assertEquals( dimSegments.ranges[2].start, 2 );
		assertEquals( dimSegments.ranges[2].end, 2 );
		
		dimSegments = new DimensionDivision( 3, 4 );
		assertEquals( dimSegments.ranges.length, 3 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 0 );
		assertEquals( dimSegments.ranges[1].start, 1 );
		assertEquals( dimSegments.ranges[1].end, 1 );
		assertEquals( dimSegments.ranges[2].start, 2 );
		assertEquals( dimSegments.ranges[2].end, 2 );
		
		dimSegments = new DimensionDivision( 10, 3 );
		assertEquals( dimSegments.ranges.length, 3 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 3 );
		assertEquals( dimSegments.ranges[1].start, 4 );
		assertEquals( dimSegments.ranges[1].end, 6 );
		assertEquals( dimSegments.ranges[2].start, 7 );
		assertEquals( dimSegments.ranges[2].end, 9 );
		
		dimSegments = new DimensionDivision( 11, 3 );
		assertEquals( dimSegments.ranges.length, 3 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 3 );
		assertEquals( dimSegments.ranges[1].start, 4 );
		assertEquals( dimSegments.ranges[1].end, 7 );
		assertEquals( dimSegments.ranges[2].start, 8 );
		assertEquals( dimSegments.ranges[2].end, 10 );
		
		dimSegments = new DimensionDivision( 12, 3 );
		assertEquals( dimSegments.ranges.length, 3 );
		assertEquals( dimSegments.ranges[0].start, 0 );
		assertEquals( dimSegments.ranges[0].end, 3 );
		assertEquals( dimSegments.ranges[1].start, 4 );
		assertEquals( dimSegments.ranges[1].end, 7 );
		assertEquals( dimSegments.ranges[2].start, 8 );
		assertEquals( dimSegments.ranges[2].end, 11 );
	}
}
