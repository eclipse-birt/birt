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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Test case for URIPropertyType.
 * 
 */

public class URIPropertyTypeTest extends PropertyTypeTestCase {

	URIPropertyType type = new URIPropertyType();
	PropertyDefnFake propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.URI_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.URI_TYPE_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	public void testValidateValue() throws PropertyValueException {
		int value = TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
		propDefn.setTrimOption(value);
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$
		assertEquals("abc", type.validateValue(design, null, propDefn, "abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", type.validateValue(design, null, propDefn, "    abc ")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		// already covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		// already covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, "any-input"), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, "any-input")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals("abc", type.toXml(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals("abc", type.toString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals("abc", type.toDisplayString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, "any-input").doubleValue(), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	public void testToBoolean() {
		assertEquals(false, type.toBoolean(design, "any-input")); //$NON-NLS-1$
	}
}
