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
import org.eclipse.birt.chart.model.attribute.IntersectionType;

public class IntersectionTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( IntersectionType.MIN, 0 );
		assertEquals( IntersectionType.MAX, 1 );
		assertEquals( IntersectionType.VALUE, 2 );		
	}
	
	public void testGet() 
	{
		assertEquals( IntersectionType.MIN_LITERAL, IntersectionType.get(IntersectionType.MIN) );
		assertEquals( IntersectionType.MAX_LITERAL, IntersectionType.get(IntersectionType.MAX) );
		assertEquals( IntersectionType.VALUE_LITERAL, IntersectionType.get(2) );
		assertNull(IntersectionType.get("3") );
		
		assertEquals( IntersectionType.MIN_LITERAL, IntersectionType.get("Min") );
		assertEquals( IntersectionType.MAX_LITERAL, IntersectionType.get("Max") );
		assertEquals( IntersectionType.VALUE_LITERAL, IntersectionType.get("Value") );
		assertNull(IntersectionType.get("No Match") );
	}
}