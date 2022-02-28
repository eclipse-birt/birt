/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;

import junit.framework.TestCase;

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
