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
 * Test case for <code>FloatPropertyType</code>.
 *
 */
public class FloatPropertyTypeTest extends PropertyTypeTestCase {

	FloatPropertyType type = new FloatPropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	@Override
	public void testGetTypeCode() {
		assertEquals(PropertyType.FLOAT_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.FLOAT_TYPE_NAME, type.getName());
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
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$

		assertEquals(12.34d, ((Double) type.validateValue(design, null, propDefn, new Double(12.34d))).doubleValue(),
				2);
		assertEquals(12.34d, ((Double) type.validateValue(design, null, propDefn, new Float(12.34f))).doubleValue(), 2);

		assertEquals(12.34d, ((Double) type.validateValue(design, null, propDefn, new BigDecimal(12.34))).doubleValue(),
				2);
		assertEquals(12, ((Double) type.validateValue(design, null, propDefn, new Integer(12))).intValue());
		assertEquals(1, ((Double) type.validateValue(design, null, propDefn, new Boolean(true))).intValue());
		assertEquals(0, ((Double) type.validateValue(design, null, propDefn, new Boolean(false))).intValue());

		// String
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals(1234.123d, ((Double) type.validateValue(design, null, propDefn, "1,234.123")).doubleValue(), 3); //$NON-NLS-1$
		assertEquals(1234.123d, ((Double) type.validateValue(design, null, propDefn, "1234.123")).doubleValue(), 3); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	@Override
	public void testValidateInputString() throws PropertyValueException {

		assertEquals(null, type.validateInputString(design, null, propDefn, null));
		assertEquals(null, type.validateInputString(design, null, propDefn, "")); //$NON-NLS-1$

		// String
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals(1234.123d, ((Double) type.validateInputString(design, null, propDefn, "1,234.123")).doubleValue(), //$NON-NLS-1$
				3);
		assertEquals(1234.123d, ((Double) type.validateInputString(design, null, propDefn, "1234.123")).doubleValue(), //$NON-NLS-1$
				3);
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
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$
		assertEquals(null, type.validateXml(design, null, propDefn, "  ")); //$NON-NLS-1$

		assertEquals(12.34d, ((Double) type.validateXml(null, null, null, "12.34")).doubleValue(), 2); //$NON-NLS-1$

		try {
			type.validateXml(design, null, propDefn, "abc.abc"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	@Override
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, null), 1);
		assertEquals(0.0d, type.toDouble(design, new Double(0.0d)), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	@Override
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, null));
		assertEquals(0, type.toInteger(design, new Double(0.0d)));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
		ULocale preULocale = ULocale.getDefault();

		ULocale.setDefault(ULocale.GERMAN);
		assertEquals(null, type.toXml(design, propDefn, null));
		assertEquals("123.456", type.toXml(design, propDefn, new Double(123.456d))); //$NON-NLS-1$
		assertEquals("123456.789", type.toXml(design, propDefn, new Double("123456.789"))); //$NON-NLS-1$ //$NON-NLS-2$
		ULocale.setDefault(ULocale.ENGLISH);
		assertEquals(null, type.toXml(design, propDefn, null));
		assertEquals("123.456", type.toXml(design, propDefn, new Double(123.456d))); //$NON-NLS-1$
		assertEquals("123456.789", type.toXml(design, propDefn, new Double("123456.789"))); //$NON-NLS-1$ //$NON-NLS-2$

		ULocale.setDefault(preULocale);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	@Override
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("123.456", type.toString(design, propDefn, new Double(123.456d))); //$NON-NLS-1$
		assertEquals("3.0", type.toString(design, propDefn, new Double(3.0d))); //$NON-NLS-1$
		assertEquals("1234567890123456800000000.0", //$NON-NLS-1$
				type.toString(design, propDefn, new Double(1234567890123456789012345.12345678d)));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("123456.789", type.toString(design, propDefn, new Double(123456.789d))); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	@Override
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, null).doubleValue(), 1);
		assertEquals(0.0d, type.toNumber(design, new Double(0.0d)).doubleValue(), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	@Override
	public void testToBoolean() {
	}

}
