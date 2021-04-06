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

package org.eclipse.birt.report.engine.ir;

import java.util.Random;

import junit.framework.TestCase;

/**
 * Dimension Type test
 * 
 */
public class DimensionTypeTest extends TestCase {

	/**
	 * Test get/setChoice methods
	 * 
	 * set a string as a choice
	 * 
	 * then get the choice and check the value type to test if they work correctly
	 */

	public void testChoice() {
		DimensionType d = new DimensionType("Test");

		assertEquals(d.getValueType(), DimensionType.TYPE_CHOICE);
		assertEquals(d.getChoice(), "Test");

	}

	/**
	 * Test get/setDimension methods
	 * 
	 * set a random of double numbers with diffrent unit
	 * 
	 * then get the dimensions one by one ,check the dimension type and convert them
	 * into string to test if they work correctly
	 */
	public void testValue() {
		double v = (new Random()).nextDouble();
		String[] suffix = { DimensionType.UNITS_CM, DimensionType.UNITS_EM, DimensionType.UNITS_EX,
				DimensionType.UNITS_IN, DimensionType.UNITS_MM, DimensionType.UNITS_PC, DimensionType.UNITS_PERCENTAGE,
				DimensionType.UNITS_PT, DimensionType.UNITS_PX, DimensionType.UNITS_PERCENTAGE };

		// Set
		DimensionType d[] = new DimensionType[suffix.length];
		for (int i = 0; i < suffix.length; i++) {
			d[i] = new DimensionType(v, suffix[i]);
		}
		;

		// Get
		for (int i = 0; i < d.length; i++) {
			assertEquals(d[i].getValueType(), DimensionType.TYPE_DIMENSION);
			assertEquals(d[i].getUnits(), suffix[i]);
			assertEquals(d[i].getMeasure(), v, 0.01);
		}
	}
}
