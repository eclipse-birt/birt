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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.ChartDimension;

public class ChartDimensionTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( ChartDimension.TWO_DIMENSIONAL, 0 );
		assertEquals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH, 1 );	
		assertEquals( ChartDimension.THREE_DIMENSIONAL, 2 );
	}
	
	public void testGet() 
	{
		assertEquals( ChartDimension.TWO_DIMENSIONAL_LITERAL, ChartDimension.get(ChartDimension.TWO_DIMENSIONAL) );
		assertEquals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL, ChartDimension.get(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH) );
		assertEquals( ChartDimension.THREE_DIMENSIONAL_LITERAL, ChartDimension.get(2) );
		
		assertEquals( ChartDimension.TWO_DIMENSIONAL_LITERAL, ChartDimension.get("Two_Dimensional") );
		assertEquals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL, ChartDimension.get("Two_Dimensional_With_Depth") );
		assertEquals( ChartDimension.THREE_DIMENSIONAL_LITERAL, ChartDimension.get("Three_Dimensional") );
		
		assertNull(ChartDimension.get("No Match") );
		assertNull(ChartDimension.get(4) );
	}
}
