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
import org.eclipse.birt.chart.model.attribute.Orientation;

public class OrientationTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( Orientation.HORIZONTAL, 0 );
		assertEquals( Orientation.VERTICAL, 1 );		
	}
	
	public void testGet() 
	{
		assertEquals( Orientation.HORIZONTAL_LITERAL, Orientation.get(Orientation.HORIZONTAL) );
		assertEquals( Orientation.VERTICAL_LITERAL, Orientation.get(1) );
		
		assertEquals( Orientation.HORIZONTAL_LITERAL, Orientation.get("Horizontal") );
		assertEquals( Orientation.VERTICAL_LITERAL, Orientation.get("Vertical") );
		
		assertNull( Orientation.get("No Match") );
		assertNull( Orientation.get(2) );
	}
}