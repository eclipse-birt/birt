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
import org.eclipse.birt.chart.model.attribute.RiserType;

public class RiserTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( RiserType.RECTANGLE, 0 );
		assertEquals( RiserType.TRIANGLE, 1 );		
	}
	
	public void testGet() 
	{
		assertEquals( RiserType.RECTANGLE_LITERAL, RiserType.get(RiserType.RECTANGLE) );
		assertEquals( RiserType.TRIANGLE_LITERAL, RiserType.get(1) );
		
		assertEquals( RiserType.RECTANGLE_LITERAL, RiserType.get("Rectangle") );
		assertEquals( RiserType.TRIANGLE_LITERAL, RiserType.get("Triangle") );
		
		assertNull( RiserType.get("No Match") );
		assertNull( RiserType.get(2) );
	}
}
