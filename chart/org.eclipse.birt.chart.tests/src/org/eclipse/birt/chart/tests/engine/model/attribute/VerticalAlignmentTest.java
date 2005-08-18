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
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;

public class VerticalAlignmentTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( VerticalAlignment.TOP, 0 );
		assertEquals( VerticalAlignment.CENTER, 1 );		
		assertEquals( VerticalAlignment.BOTTOM, 2 );	
	}
	
	public void testGet() 
	{
		assertEquals( VerticalAlignment.TOP_LITERAL, VerticalAlignment.get(VerticalAlignment.TOP) );
		assertEquals( VerticalAlignment.CENTER_LITERAL, VerticalAlignment.get(VerticalAlignment.CENTER) );
		assertEquals( VerticalAlignment.BOTTOM_LITERAL, VerticalAlignment.get(2) );
		
		assertEquals( VerticalAlignment.TOP_LITERAL, VerticalAlignment.get("Top") );
		assertEquals( VerticalAlignment.CENTER_LITERAL, VerticalAlignment.get("Center") );
		assertEquals( VerticalAlignment.BOTTOM_LITERAL, VerticalAlignment.get("Bottom") );
		
		assertNull(VerticalAlignment.get("No Match") );
		assertNull(VerticalAlignment.get(3) );
	}
}



