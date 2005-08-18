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
import org.eclipse.birt.chart.model.attribute.AxisType;

public class AxisTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( AxisType.LINEAR, 0 );
		assertEquals( AxisType.LOGARITHMIC, 1 );		
		assertEquals( AxisType.TEXT, 2 );
		assertEquals( AxisType.DATE_TIME, 3 );
	}
	
	public void testGet() 
	{
		assertEquals( AxisType.LINEAR_LITERAL, AxisType.get(AxisType.LINEAR) );
		assertEquals( AxisType.LOGARITHMIC_LITERAL, AxisType.get(AxisType.LOGARITHMIC) );
		assertEquals( AxisType.TEXT_LITERAL, AxisType.get(2) );
		assertEquals( AxisType.DATE_TIME_LITERAL, AxisType.get(3) );
		assertNull(AxisType.get(4) );
		
		assertEquals( AxisType.LINEAR_LITERAL, AxisType.get("Linear") );
		assertEquals( AxisType.LOGARITHMIC_LITERAL, AxisType.get("Logarithmic") );
		assertEquals( AxisType.TEXT_LITERAL, AxisType.get("Text") );
		assertEquals( AxisType.DATE_TIME_LITERAL, AxisType.get("DateTime") );
		assertNull(AxisType.get("No Match") );
		
	}
}
