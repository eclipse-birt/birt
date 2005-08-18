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
import org.eclipse.birt.chart.model.attribute.Position;

public class PositionTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( Position.ABOVE, 0 );
		assertEquals( Position.BELOW, 1 );
		assertEquals( Position.LEFT, 2 );	
		assertEquals( Position.RIGHT, 3 );
		assertEquals( Position.INSIDE, 4 );
		assertEquals( Position.OUTSIDE, 5 );
	}
	
	public void testGet() 
	{
		assertEquals( Position.ABOVE_LITERAL, Position.get(Position.ABOVE) );
		assertEquals( Position.BELOW_LITERAL, Position.get(Position.BELOW) );
		assertEquals( Position.LEFT_LITERAL, Position.get(Position.LEFT) );
		assertEquals( Position.RIGHT_LITERAL, Position.get(Position.RIGHT) );
		assertEquals( Position.INSIDE_LITERAL, Position.get(4) );
		assertEquals( Position.OUTSIDE_LITERAL, Position.get(5) );
		assertNull(Position.get("6") );
		
		assertEquals( Position.ABOVE_LITERAL, Position.get("Above") );
		assertEquals( Position.BELOW_LITERAL, Position.get("Below") );
		assertEquals( Position.LEFT_LITERAL, Position.get("Left") );
		assertEquals( Position.RIGHT_LITERAL, Position.get("Right") );
		assertEquals( Position.INSIDE_LITERAL, Position.get("Inside") );
		assertEquals( Position.OUTSIDE_LITERAL, Position.get("Outside") );
		assertNull(Position.get("No Match") );
	}
}

