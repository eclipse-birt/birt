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
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;

public class HorizontalAlignmentTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( HorizontalAlignment.LEFT, 0 );
		assertEquals( HorizontalAlignment.CENTER, 1 );
		assertEquals( HorizontalAlignment.RIGHT, 2 );		
	}
	
	public void testGet() 
	{
		assertEquals( HorizontalAlignment.LEFT_LITERAL, HorizontalAlignment.get(HorizontalAlignment.LEFT) );
		assertEquals( HorizontalAlignment.CENTER_LITERAL, HorizontalAlignment.get(1) );
		assertEquals( HorizontalAlignment.RIGHT_LITERAL, HorizontalAlignment.get(2) );
		assertNull(HorizontalAlignment.get("3") );
		
		assertEquals( HorizontalAlignment.LEFT_LITERAL, HorizontalAlignment.get("Left") );
		assertEquals( HorizontalAlignment.CENTER_LITERAL, HorizontalAlignment.get("Center") );
		assertEquals( HorizontalAlignment.RIGHT_LITERAL, HorizontalAlignment.get("Right") );
		assertNull(HorizontalAlignment.get("No Match") );
	}
}
