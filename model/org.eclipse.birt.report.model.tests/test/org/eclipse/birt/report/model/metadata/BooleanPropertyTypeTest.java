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
 * Test case for BooleanPropertyType.
 * <p>
 * In this setUp method , prepare test datas and expected datas for supporting
 * testGetterSetters , testBuild , testValidateValue method in
 * PropertyTypeTestCase class
 *
 */
public class BooleanPropertyTypeTest extends PropertyTypeTestCase {

	BooleanPropertyType type = new BooleanPropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode()
	 */
	@Override
	public void testGetTypeCode() {
		assertEquals(PropertyType.BOOLEAN_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.BOOLEAN_TYPE_NAME, type.getName());
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
		assertEquals(null, type.validateValue(design, null, propDefn, null));

		// String
		assertEquals(null, type.validateValue(design, null, propDefn, " ")); //$NON-NLS-1$
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, "true")); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, "false")); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, "\u771f")); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, "\u5047")); //$NON-NLS-1$

		// Boolean
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, Boolean.TRUE));
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, Boolean.FALSE));

		// Integer
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new Integer(1)));
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new Integer(2)));
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, new Integer(0)));

		// Double
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new Double(1.0d)));
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new Double(2.0d)));
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, new Double(0.0d)));

		// BigDecimal
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new BigDecimal(1.0d)));
		assertEquals(Boolean.TRUE, type.validateValue(design, null, propDefn, new BigDecimal(2.0d)));
		assertEquals(Boolean.FALSE, type.validateValue(design, null, propDefn, new BigDecimal(0.0d)));

		// Wrong type
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
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	@Override
	public void testValidateInputString() throws PropertyValueException {
		assertEquals(null, type.validateInputString(design, null, propDefn, null));

		assertEquals(null, type.validateInputString(design, null, propDefn, " ")); //$NON-NLS-1$
		assertEquals(Boolean.TRUE, type.validateInputString(design, null, propDefn, "true")); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, type.validateInputString(design, null, propDefn, "false")); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals(Boolean.TRUE, type.validateInputString(design, null, propDefn, "\u771f")); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, type.validateInputString(design, null, propDefn, "\u5047")); //$NON-NLS-1$

		// Wrong type
		try {
			type.validateInputString(design, null, propDefn, "wrong-type-value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml()
	 */
	@Override
	public void testValidateXml() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, " ")); //$NON-NLS-1$
		assertEquals(Boolean.TRUE, type.validateXml(design, null, propDefn, "true")); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, type.validateXml(design, null, propDefn, "false")); //$NON-NLS-1$

		try {
			type.validateXml(design, null, propDefn, "wrong-xml-value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble()
	 */
	@Override
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, null), 1);
		assertEquals(1.0d, type.toDouble(design, Boolean.TRUE), 1);
		assertEquals(0.0d, type.toDouble(design, Boolean.FALSE), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger()
	 */
	@Override
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, null));
		assertEquals(1, type.toInteger(design, Boolean.TRUE));
		assertEquals(0, type.toInteger(design, Boolean.FALSE));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
		assertEquals("true", type.toXml(design, propDefn, Boolean.TRUE)); //$NON-NLS-1$
		assertEquals("false", type.toXml(design, propDefn, Boolean.FALSE)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString()
	 */
	@Override
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));

		assertEquals("true", type.toString(design, propDefn, Boolean.TRUE)); //$NON-NLS-1$
		assertEquals("false", type.toString(design, propDefn, Boolean.FALSE)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		assertEquals(null, type.toDisplayString(design, propDefn, null));

		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals("true", type.toDisplayString(design, propDefn, Boolean.TRUE)); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals("\u771f", type.toDisplayString(design, propDefn, Boolean.TRUE)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber()
	 */
	@Override
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, null).doubleValue(), 1);
		assertEquals(1.0d, type.toNumber(design, Boolean.TRUE).doubleValue(), 1);
		assertEquals(0.0d, type.toNumber(design, Boolean.FALSE).doubleValue(), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean()
	 */
	@Override
	public void testToBoolean() {
		assertEquals(false, type.toBoolean(design, null));
		assertEquals(true, type.toBoolean(design, Boolean.TRUE));
		assertEquals(false, type.toBoolean(design, Boolean.FALSE));
	}

}
