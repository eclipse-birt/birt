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
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;

public class UnitsOfMeasurementTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( UnitsOfMeasurement.PIXELS, 0 );
		assertEquals( UnitsOfMeasurement.POINTS, 1 );		
		assertEquals( UnitsOfMeasurement.INCHES, 2 );	
		assertEquals( UnitsOfMeasurement.CENTIMETERS, 3 );	
	}
	
	public void testGet() 
	{
		assertEquals( UnitsOfMeasurement.PIXELS_LITERAL, UnitsOfMeasurement.get(UnitsOfMeasurement.PIXELS) );
		assertEquals( UnitsOfMeasurement.POINTS_LITERAL, UnitsOfMeasurement.get(UnitsOfMeasurement.POINTS) );
		assertEquals( UnitsOfMeasurement.INCHES_LITERAL, UnitsOfMeasurement.get(2) );
		assertEquals( UnitsOfMeasurement.CENTIMETERS_LITERAL, UnitsOfMeasurement.get(3) );
		
		assertEquals( UnitsOfMeasurement.PIXELS_LITERAL, UnitsOfMeasurement.get("Pixels") );
		assertEquals( UnitsOfMeasurement.POINTS_LITERAL, UnitsOfMeasurement.get("Points") );
		assertEquals( UnitsOfMeasurement.INCHES_LITERAL, UnitsOfMeasurement.get("Inches") );
		assertEquals( UnitsOfMeasurement.CENTIMETERS_LITERAL, UnitsOfMeasurement.get("Centimeters") );
		
		assertNull(UnitsOfMeasurement.get("No Match") );
		assertNull(UnitsOfMeasurement.get(4) );
	}
}


