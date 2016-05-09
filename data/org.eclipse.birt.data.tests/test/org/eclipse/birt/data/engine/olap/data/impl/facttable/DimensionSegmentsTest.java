 
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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 * 
 */

public class DimensionSegmentsTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
/**
	 * 
	 * @throws IOException
	 * @throws BirtException 
	 */
	@Test
    public void testDimensionSegments( ) throws IOException, BirtException
	{
		DimensionDivision dimSegments = new DimensionDivision( 2, 1 );
		assertEquals( dimSegments.getRanges().length, 1 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 1 );
		
		dimSegments = new DimensionDivision( 1, 1 );
		assertEquals( dimSegments.getRanges().length, 1 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 0 );
		
		dimSegments = new DimensionDivision( 3, 3 );
		assertEquals( dimSegments.getRanges().length, 3 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 0 );
		assertEquals( dimSegments.getRanges()[1].getStart(), 1 );
		assertEquals( dimSegments.getRanges()[1].getEnd(), 1 );
		assertEquals( dimSegments.getRanges()[2].getStart(), 2 );
		assertEquals( dimSegments.getRanges()[2].getEnd(), 2 );
		
		dimSegments = new DimensionDivision( 3, 4 );
		assertEquals( dimSegments.getRanges().length, 3 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 0 );
		assertEquals( dimSegments.getRanges()[1].getStart(), 1 );
		assertEquals( dimSegments.getRanges()[1].getEnd(), 1 );
		assertEquals( dimSegments.getRanges()[2].getStart(), 2 );
		assertEquals( dimSegments.getRanges()[2].getEnd(), 2 );
		
		dimSegments = new DimensionDivision( 10, 3 );
		assertEquals( dimSegments.getRanges().length, 3 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 3 );
		assertEquals( dimSegments.getRanges()[1].getStart(), 4 );
		assertEquals( dimSegments.getRanges()[1].getEnd(), 6 );
		assertEquals( dimSegments.getRanges()[2].getStart(), 7 );
		assertEquals( dimSegments.getRanges()[2].getEnd(), 9 );
		
		dimSegments = new DimensionDivision( 11, 3 );
		assertEquals( dimSegments.getRanges().length, 3 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 3 );
		assertEquals( dimSegments.getRanges()[1].getStart(), 4 );
		assertEquals( dimSegments.getRanges()[1].getEnd(), 7 );
		assertEquals( dimSegments.getRanges()[2].getStart(), 8 );
		assertEquals( dimSegments.getRanges()[2].getEnd(), 10 );
		
		dimSegments = new DimensionDivision( 12, 3 );
		assertEquals( dimSegments.getRanges().length, 3 );
		assertEquals( dimSegments.getRanges()[0].getStart(), 0 );
		assertEquals( dimSegments.getRanges()[0].getEnd(), 3 );
		assertEquals( dimSegments.getRanges()[1].getStart(), 4 );
		assertEquals( dimSegments.getRanges()[1].getEnd(), 7 );
		assertEquals( dimSegments.getRanges()[2].getStart(), 8 );
		assertEquals( dimSegments.getRanges()[2].getEnd(), 11 );
	}
}
