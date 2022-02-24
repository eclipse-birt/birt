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

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Test case for NumberPropertyType.
 * 
 */

public class NumberPropertyTypeTest extends PropertyTypeTestCase {

	NumberPropertyType type = new NumberPropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.NUMBER_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.NUMBER_TYPE_NAME, type.getName());
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

		Object input = new BigDecimal(1.0d);
		assertTrue(input == type.validateValue(design, null, propDefn, input));

		input = new Double(1.0d);
		assertEquals(1.0d, ((BigDecimal) type.validateValue(design, null, propDefn, input)).doubleValue(), 1.0d);

		input = new Integer(1);
		assertEquals(1.0d, ((BigDecimal) type.validateValue(design, null, propDefn, input)).doubleValue(), 1.0d);

		// String
		ThreadResources.setLocale(ULocale.ENGLISH);
		input = "1.0"; //$NON-NLS-1$
		assertEquals(1.0d, ((BigDecimal) type.validateValue(design, null, propDefn, input)).doubleValue(), 1.0d);

		try {
			input = "abc.abc"; //$NON-NLS-1$
			type.validateValue(design, null, propDefn, input);
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

		// String
		ThreadResources.setLocale(ULocale.ENGLISH);
		String input = "1.0"; //$NON-NLS-1$
		assertEquals(1.0d, ((BigDecimal) type.validateInputString(design, null, propDefn, input)).doubleValue(), 1.0d);

		input = "12,345.67"; //$NON-NLS-1$
		assertEquals(12345.6d, ((BigDecimal) type.validateInputString(design, null, propDefn, input)).doubleValue(),
				1.0d);

		// TODO: parse "1234567890123456789.123456789".

		try {
			input = "abc.abc"; //$NON-NLS-1$
			type.validateInputString(design, null, propDefn, input);
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

		String input = "1.0"; //$NON-NLS-1$
		assertEquals(1.0d, ((BigDecimal) type.validateXml(design, null, propDefn, input)).doubleValue(), 1.0d);

		input = " "; //$NON-NLS-1$
		assertEquals(null, type.validateXml(design, null, propDefn, input));

		input = null;
		assertEquals(null, type.validateXml(design, null, propDefn, input));

		assertEquals("1234567890123456789.123456789", //$NON-NLS-1$
				((BigDecimal) type.validateXml(design, null, propDefn, "1234567890123456789.123456789")).toString()); //$NON-NLS-1$

		try {
			type.validateXml(design, null, propDefn, "1,234"); //$NON-NLS-1$
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
		assertEquals(1.0d, type.toDouble(design, new BigDecimal(1.0d)), 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
		assertEquals(1, type.toInteger(design, new BigDecimal(1.0d)));
		assertEquals(0, type.toInteger(design, null));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals("1234567.123", type.toXml(design, propDefn, new BigDecimal(1234567.123d))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals("1234567.123", type.toString(design, propDefn, new BigDecimal(1234567.123d))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals("1,234,567.123", type.toDisplayString(design, propDefn, new BigDecimal(1234567.123d))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
		Object input = new BigDecimal(1.0d);
		assertEquals(input, type.toNumber(design, input));
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
