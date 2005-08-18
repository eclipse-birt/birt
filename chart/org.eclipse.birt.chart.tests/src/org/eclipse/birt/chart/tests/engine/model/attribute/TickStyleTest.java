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
import org.eclipse.birt.chart.model.attribute.TickStyle;

public class TickStyleTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( TickStyle.LEFT, 0 );
		assertEquals( TickStyle.RIGHT , 1 );
		assertEquals( TickStyle.ABOVE, 2 );		
		assertEquals( TickStyle.BELOW, 3 );
		assertEquals( TickStyle.ACROSS, 4 );
	}
	
	public void testGet() 
	{
		assertEquals( TickStyle.LEFT_LITERAL, TickStyle.get(TickStyle.LEFT) );
		assertEquals( TickStyle.RIGHT_LITERAL, TickStyle.get(TickStyle.RIGHT) );
		assertEquals( TickStyle.ABOVE_LITERAL, TickStyle.get(2) );
		assertEquals( TickStyle.BELOW_LITERAL, TickStyle.get(3) );
		assertEquals( TickStyle.ACROSS_LITERAL, TickStyle.get(4) );
		assertNull(TickStyle.get("-1") );
		
		assertEquals( TickStyle.LEFT_LITERAL, TickStyle.get("Left") );
		assertEquals( TickStyle.RIGHT_LITERAL, TickStyle.get("Right") );
		assertEquals( TickStyle.ABOVE_LITERAL, TickStyle.get("Above") );
		assertEquals( TickStyle.BELOW_LITERAL, TickStyle.get("Below") );
		assertEquals( TickStyle.ACROSS_LITERAL, TickStyle.get("Across") );
		assertNull(TickStyle.get("No Match") );
	}
}
