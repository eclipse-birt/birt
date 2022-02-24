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

package org.eclipse.birt.report.model.metadata;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for DimensionValue.
 * 
 */
public class DimensionValueTest extends BaseTestCase {

	private DimensionValue value = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * test getters and setters.
	 * 
	 */
	public void testGetterAndSetter() {
		try {
			value = new DimensionValue(12.89, "unsupportedUnit"); //$NON-NLS-1$
			fail();
		} catch (RuntimeException e) {
			assertTrue(e instanceof IllegalArgumentException);
		}

		value = new DimensionValue(12.89, DesignChoiceConstants.UNITS_MM);
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_MM == value.getUnits());

		value = new DimensionValue(12.89, DesignChoiceConstants.UNITS_IN);
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_IN == value.getUnits());

	}

	/**
	 * test parsing.
	 * 
	 * @throws PropertyValueException if failed to parse value
	 * 
	 */
	public void testParse() throws PropertyValueException {
		value = DimensionValue.parse(null);
		assertNull(value);

		value = DimensionValue.parse("         "); //$NON-NLS-1$
		assertNull(value);

		value = DimensionValue.parse("12.89mm"); //$NON-NLS-1$
		assertTrue(12.89 == value.getMeasure());
		assertEquals(DesignChoiceConstants.UNITS_MM, value.getUnits());
		assertEquals("12.89mm", value.toString()); //$NON-NLS-1$

		value = DimensionValue.parse("12.89 cm"); //$NON-NLS-1$
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_CM == value.getUnits());
		assertEquals("12.89cm", value.toString()); //$NON-NLS-1$

		value = DimensionValue.parse("12.89mm"); //$NON-NLS-1$
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_MM == value.getUnits());
		assertEquals("12.89mm", value.toString()); //$NON-NLS-1$

		value = DimensionValue.parse("12.89in"); //$NON-NLS-1$
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_IN == value.getUnits());
		assertEquals("12.89in", value.toString()); //$NON-NLS-1$

		value = DimensionValue.parse("12.89 pt"); //$NON-NLS-1$
		assertTrue(12.89 == value.getMeasure());
		assertTrue(DesignChoiceConstants.UNITS_PT == value.getUnits());
		assertEquals("12.89pt", value.toString()); //$NON-NLS-1$

		// Parse the string with invalid format
		try {
			value = DimensionValue.parse("12.89 m"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e1) {
		}

	}

	/**
	 * test toString().
	 */

	public void testToString() {
		DimensionValue dim = new DimensionValue(120000000.12345656d, DesignChoiceConstants.UNITS_CM);
		assertEquals("120000000.12345655cm", dim.toString()); //$NON-NLS-1$
	}

	/**
	 * Test toDisplayString().
	 * 
	 */

	public void testToDisplayString() {
		ThreadResources.setLocale(ULocale.ENGLISH);

		DimensionValue dim = new DimensionValue(120000000.12345656d, DesignChoiceConstants.UNITS_CM);
		assertEquals("120,000,000.123cm", dim.toDisplayString()); //$NON-NLS-1$
	}

	/**
	 * Tests the equal between two dimension values.
	 */

	public void testEquals() {
		DimensionValue v1 = new DimensionValue(25.4, DesignChoiceConstants.UNITS_MM);
		DimensionValue v2 = DimensionUtil.convertTo(1.0, DesignChoiceConstants.UNITS_IN,
				DesignChoiceConstants.UNITS_MM);
		DimensionValue v3 = new DimensionValue(25.4, DesignChoiceConstants.UNITS_MM);
		DimensionValue v4 = new DimensionValue(1.0, DesignChoiceConstants.UNITS_IN);

		assertFalse(v1.equals(null));
		assertFalse(v1.equals("abc")); //$NON-NLS-1$

		assertFalse(v1.equals(v4));

		assertTrue(v1.equals(v1));
		assertTrue(v1.equals(v2));
		assertTrue(v1.hashCode() == v2.hashCode());

		assertTrue(v2.equals(v1));

		assertTrue(v2.equals(v3));
		assertTrue(v2.hashCode() == v3.hashCode());

		assertTrue(v3.equals(v1));
		assertTrue(v1.hashCode() == v3.hashCode());

		v1 = new DimensionValue(10.098, "mm"); //$NON-NLS-1$
		v2 = new DimensionValue(10.098, "mm"); //$NON-NLS-1$
		assertTrue(v1.equals(v2));
		assertTrue(v1.hashCode() == v2.hashCode());

		v1 = new DimensionValue(10.098, "in"); //$NON-NLS-1$
		v2 = new DimensionValue(10.098, "mm"); //$NON-NLS-1$
		assertFalse(v1.equals(v2));
		assertFalse(v1.hashCode() == v3.hashCode());

		v1 = new DimensionValue(10.098, "in"); //$NON-NLS-1$
		v2 = new DimensionValue(10.098, "IN"); //$NON-NLS-1$
		assertTrue(v1.equals(v2));
		assertTrue(v1.hashCode() == v2.hashCode());
	}
}
