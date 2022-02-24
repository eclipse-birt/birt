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

import java.util.Date;

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Test case for DateTimePropertyType.
 * 
 */

public class DateTimePropertyTypeTest extends PropertyTypeTestCase {

	DateTimePropertyType type = new DateTimePropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	Calendar calendar = Calendar.getInstance();

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.DATE_TIME_TYPE, type.getTypeCode());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.DATE_TIME_TYPE_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	public void testValidateValue() throws PropertyValueException {
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$

		ModuleOption options = new ModuleOption();
		design.setOptions(options);

		// Date
		Date date = calendar.getTime();
		assertTrue(date == type.validateValue(design, null, propDefn, date));

		// String
		options.setLocale(ULocale.ENGLISH);
		Date value = (Date) type.validateValue(design, null, propDefn, "08/25/2004"); //$NON-NLS-1$

		calendar.setTime(value);
		assertEquals(2004 - 1900, calendar.get(Calendar.YEAR) - 1900);
		assertEquals(7, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));

		options.setLocale(ULocale.CHINA);
		// The icu version change cause this to fail, change the input string
		// (previous was "2004-08-25") to accommodate
		value = (Date) type.validateValue(design, null, propDefn, "2004/08/25"); //$NON-NLS-1$
		assertEquals(2004 - 1900, calendar.get(Calendar.YEAR) - 1900);
		assertEquals(7, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));

		try {
			type.validateValue(design, null, propDefn, "wrong-datetime-value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// Wrong java type
		try {
			type.validateValue(design, null, propDefn, new Object());
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		assertEquals(null, type.validateInputString(design, null, propDefn, null));
		assertEquals(null, type.validateInputString(design, null, propDefn, "")); //$NON-NLS-1$

		ModuleOption options = new ModuleOption();
		design.setOptions(options);

		// String
		options.setLocale(ULocale.ENGLISH);
		Date value = (Date) type.validateInputString(design, null, propDefn, "08/25/2004"); //$NON-NLS-1$
		calendar.setTime(value);
		assertEquals(2004 - 1900, calendar.get(Calendar.YEAR) - 1900);
		assertEquals(7, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));

		options.setLocale(ULocale.CHINA);
		// The icu version change cause this to fail, change the input string
		// (previous was "2004-08-25") to accommodate
		value = (Date) type.validateInputString(design, null, propDefn, "2004/08/25"); //$NON-NLS-1$
		assertEquals(2004 - 1900, calendar.get(Calendar.YEAR) - 1900);
		assertEquals(7, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));

		try {
			type.validateInputString(design, null, propDefn, "wrong-datetime-value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$

		Date value = (Date) type.validateXml(design, null, propDefn, "2004-10-18 10:34:22"); //$NON-NLS-1$
		calendar.setTime(value);
		assertEquals(2004 - 1900, calendar.get(Calendar.YEAR) - 1900);
		assertEquals(9, calendar.get(Calendar.MONTH));
		assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));

		// wrong value
		try {
			type.validateXml(design, null, propDefn, "wrong-datetime-value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals(null, type.toXml(design, propDefn, null));
		calendar.set(2004, Calendar.OCTOBER, 18);

		String value = type.toXml(design, propDefn, calendar.getTime());

		// in format "yyyy-MM-dd HH:mm:ss"
		int index = value.indexOf(" "); //$NON-NLS-1$
		value = value.substring(0, index);

		assertEquals("2004-10-18", value); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));

		calendar.set(2004, Calendar.OCTOBER, 18);

		ULocale preULocale = ULocale.getDefault();

		ULocale.setDefault(ULocale.ENGLISH);
		String value = type.toString(design, propDefn, calendar.getTime());
		assertTrue(value.startsWith("2004-10-18")); //$NON-NLS-1$

		ULocale.setDefault(ULocale.GERMAN);
		value = type.toString(design, propDefn, calendar.getTime());
		assertTrue(value.startsWith("2004-10-18")); //$NON-NLS-1$

		ULocale.setDefault(preULocale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals(null, type.toDisplayString(design, propDefn, null));

		ThreadResources.setLocale(ULocale.ENGLISH);
		calendar.set(2004, Calendar.OCTOBER, 18);

		String value = type.toDisplayString(design, propDefn, calendar.getTime());
		assertEquals("10/18/04", value); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	public void testToBoolean() {
	}

}
