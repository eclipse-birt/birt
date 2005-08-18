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
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;

public class LeaderLineStyleTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( LeaderLineStyle.FIXED_LENGTH, 0 );
		assertEquals( LeaderLineStyle.STRETCH_TO_SIDE, 1 );		
	}
	
	public void testGet() 
	{
		assertEquals( LeaderLineStyle.FIXED_LENGTH_LITERAL, LeaderLineStyle.get(LeaderLineStyle.FIXED_LENGTH) );
		assertEquals( LeaderLineStyle.STRETCH_TO_SIDE_LITERAL, LeaderLineStyle.get(1) );
		
		assertEquals( LeaderLineStyle.FIXED_LENGTH_LITERAL, LeaderLineStyle.get("Fixed_Length") );
		assertEquals( LeaderLineStyle.STRETCH_TO_SIDE_LITERAL, LeaderLineStyle.get("Stretch_To_Side") );
		
		assertNull(LeaderLineStyle.get("No Match") );
		assertNull(LeaderLineStyle.get(2));
	}
}