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

package org.eclipse.birt.report.model.metadata;

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Test case for <code>IntegerPropertyType</code>.
 * 
 * 
 * @see org.eclipse.birt.report.model.metadata.IntegerPropertyType
 */

public class IntegerPropertyTypeTest extends PropertyTypeTestCase {

	IntegerPropertyType type = new IntegerPropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */

	public void testGetTypeCode() {
		assertEquals(PropertyType.INTEGER_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */

	public void testGetName() {
		assertEquals(PropertyType.INTEGER_TYPE_NAME, type.getName());
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
		assertEquals(100, ((Integer) type.validateValue(design, null, propDefn, new Integer(100))).intValue());
		assertEquals(100, ((Integer) type.validateValue(design, null, propDefn, new Float(100.01f))).intValue());
		assertEquals(100, ((Integer) type.validateValue(design, null, propDefn, new Double(100.01d))).intValue());
		assertEquals(1001, ((Integer) type.validateValue(design, null, propDefn, new BigDecimal(1001.01))).intValue());
		assertEquals(1, ((Integer) type.validateValue(design, null, propDefn, Boolean.TRUE)).intValue());
		assertEquals(0, ((Integer) type.validateValue(design, null, propDefn, Boolean.FALSE)).intValue());
		assertEquals(100, ((Integer) type.validateValue(design, null, propDefn, "100")).intValue()); //$NON-NLS-1$

		try {
			type.validateValue(design, null, propDefn, "abcdef"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e1) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */

	public void testValidateInputString() throws PropertyValueException {
		assertEquals(100, ((Integer) type.validateInputString(design, null, propDefn, "100")).intValue()); //$NON-NLS-1$
		assertEquals(1234, ((Integer) type.validateInputString(design, null, propDefn, "1,234")).intValue()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */

	public void testValidateXml() throws PropertyValueException {
		assertEquals(100, ((Integer) type.validateXml(design, null, propDefn, "100")).intValue()); //$NON-NLS-1$

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
		assertEquals(0.0d, type.toDouble(design, null), 1);
		assertEquals(100.0d, type.toDouble(design, new Integer(100)), 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */

	public void testToInteger() {
		assertEquals(0, type.toInteger(design, null));
		assertEquals(100, type.toInteger(design, new Integer(100)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */

	public void testToXml() {
		assertEquals(null, type.toXml(design, propDefn, null));
		assertEquals("123456", type.toXml(design, propDefn, new Integer(123456))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */

	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("123456", type.toString(design, propDefn, new Integer(123456))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */

	public void testToDisplayString() {
		// ThreadResources.setLocale( ULocale.)
		assertEquals(null, type.toDisplayString(design, propDefn, null));
		assertEquals("123,456", type.toDisplayString(design, propDefn, new Integer("123456"))); //$NON-NLS-1$//$NON-NLS-2$
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

	/**
	 * Tests validating valid xml string.
	 * 
	 * @throws PropertyValueException
	 * 
	 */
	public void testValidXml() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$

		Integer n = null;

		n = (Integer) type.validateXml(null, null, null, "100"); //$NON-NLS-1$
		assertTrue(100 == n.intValue());
	}

	/**
	 * Tests validating invalid value.
	 * 
	 * @throws PropertyValueException
	 */
	public void testInvalidValue() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$

		try {
			type.validateValue(null, null, null, "abcdef"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e1) {
		}
	}

	/**
	 * Tests validating invalid xml value.
	 * 
	 * @throws PropertyValueException
	 */
	public void testInvalidXml() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$

		try {
			type.validateXml(null, null, null, "abcdef"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
		}
	}
}