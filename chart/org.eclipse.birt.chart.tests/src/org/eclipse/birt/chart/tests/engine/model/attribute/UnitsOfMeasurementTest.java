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

	public void testConstant() {
		assertEquals(UnitsOfMeasurement.PIXELS, UnitsOfMeasurement.PIXELS_LITERAL.getValue());
		assertEquals(UnitsOfMeasurement.POINTS, UnitsOfMeasurement.POINTS_LITERAL.getValue());
		assertEquals(UnitsOfMeasurement.INCHES, UnitsOfMeasurement.INCHES_LITERAL.getValue());
		assertEquals(UnitsOfMeasurement.CENTIMETERS, UnitsOfMeasurement.CENTIMETERS_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(UnitsOfMeasurement.PIXELS_LITERAL, UnitsOfMeasurement.get(UnitsOfMeasurement.PIXELS));
		assertEquals(UnitsOfMeasurement.POINTS_LITERAL, UnitsOfMeasurement.get(UnitsOfMeasurement.POINTS));

		assertEquals(UnitsOfMeasurement.PIXELS_LITERAL, UnitsOfMeasurement.get("Pixels")); //$NON-NLS-1$
		assertEquals(UnitsOfMeasurement.POINTS_LITERAL, UnitsOfMeasurement.get("Points")); //$NON-NLS-1$
		assertEquals(UnitsOfMeasurement.INCHES_LITERAL, UnitsOfMeasurement.get("Inches")); //$NON-NLS-1$
		assertEquals(UnitsOfMeasurement.CENTIMETERS_LITERAL, UnitsOfMeasurement.get("Centimeters")); //$NON-NLS-1$

		assertNull(UnitsOfMeasurement.get("No Match")); //$NON-NLS-1$
	}
}
