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
import org.eclipse.birt.chart.model.attribute.MarkerType;

public class MarkerTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( MarkerType.CROSSHAIR, 0 );
		assertEquals( MarkerType.TRIANGLE, 1 );		
		assertEquals( MarkerType.BOX, 2 );	
		assertEquals( MarkerType.CIRCLE, 3 );	
	}
	
	public void testGet() 
	{
		assertEquals( MarkerType.CROSSHAIR_LITERAL, MarkerType.get(MarkerType.CROSSHAIR) );
		assertEquals( MarkerType.TRIANGLE_LITERAL, MarkerType.get(MarkerType.TRIANGLE) );
		assertEquals( MarkerType.BOX_LITERAL, MarkerType.get(2) );
		assertEquals( MarkerType.CIRCLE_LITERAL, MarkerType.get(3) );
		
		assertEquals( MarkerType.CROSSHAIR_LITERAL, MarkerType.get("Crosshair") );
		assertEquals( MarkerType.TRIANGLE_LITERAL, MarkerType.get("Triangle") );
		assertEquals( MarkerType.BOX_LITERAL, MarkerType.get("Box") );
		assertEquals( MarkerType.CIRCLE_LITERAL, MarkerType.get("Circle") );
		
		assertNull(MarkerType.get("No Match") );
		assertNull(MarkerType.get(4) );
	}
}

