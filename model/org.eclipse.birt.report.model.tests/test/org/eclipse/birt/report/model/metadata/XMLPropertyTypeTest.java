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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Test for XMLPropertyType.
 *
 *
 */
public class XMLPropertyTypeTest extends PropertyTypeTestCase {

	XMLPropertyType type = new XMLPropertyType();
	PropertyDefnFake propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	@Override
	public void testGetTypeCode() {
		assertEquals(PropertyType.XML_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.XML_TYPE_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	@Override
	public void testValidateValue() throws PropertyValueException {
		int optionValue = TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
		propDefn.setTrimOption(optionValue);
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
	@Override
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
	@Override
	public void testValidateXml() throws PropertyValueException {
		// already covered.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	@Override
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, "any-input"), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	@Override
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, "any-input")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
		assertEquals("abc", type.toXml(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	@Override
	public void testToString() {
		assertEquals("abc", type.toString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		assertEquals("abc", type.toDisplayString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	@Override
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, "any-input").doubleValue(), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	@Override
	public void testToBoolean() {
		assertEquals(false, type.toBoolean(design, "any-input")); //$NON-NLS-1$
	}

}
