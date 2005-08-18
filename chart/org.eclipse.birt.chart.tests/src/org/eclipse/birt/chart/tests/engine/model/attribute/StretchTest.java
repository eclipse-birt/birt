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
import org.eclipse.birt.chart.model.attribute.Stretch;

public class StretchTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( Stretch.HORIZONTAL, 0 );
		assertEquals( Stretch.VERTICAL, 1 );	
		assertEquals( Stretch.BOTH, 2 );
	}
	
	public void testGet() 
	{
		assertEquals( Stretch.HORIZONTAL_LITERAL, Stretch.get(Stretch.HORIZONTAL) );
		assertEquals( Stretch.VERTICAL_LITERAL, Stretch.get(Stretch.VERTICAL) );
		assertEquals( Stretch.BOTH_LITERAL, Stretch.get(2) );
		
		assertEquals( Stretch.HORIZONTAL_LITERAL, Stretch.get("Horizontal") );
		assertEquals( Stretch.VERTICAL_LITERAL, Stretch.get("Vertical") );
		assertEquals( Stretch.BOTH_LITERAL, Stretch.get("Both") );
		
		assertNull(Stretch.get("No Match") );
		assertNull(Stretch.get(3) );
	}
}