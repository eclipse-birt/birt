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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Test case for ColorPropertyType.
 * 
 */
public class ColorPropertyTypeTest extends PropertyTypeTestCase {

	ColorPropertyType type = new ColorPropertyType();
	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ColorPropertyTypeTest.xml", TEST_LOCALE); //$NON-NLS-1$
	}

	public void testFormatRGBValue() {
		// rgbValue = 16711680

		int rgbValue = Integer.decode("#FF0000").intValue(); //$NON-NLS-1$

		String value = ColorUtil.format(rgbValue, ColorUtil.INT_FORMAT);
		assertEquals("16711680", value); //$NON-NLS-1$

		value = ColorUtil.format(rgbValue, ColorUtil.HTML_FORMAT);
		assertEquals("#FF0000", value); //$NON-NLS-1$

		value = ColorUtil.format(rgbValue, ColorUtil.JAVA_FORMAT);
		assertEquals("0xFF0000", value); //$NON-NLS-1$

		value = ColorUtil.format(rgbValue, ColorUtil.CSS_ABSOLUTE_FORMAT);
		assertEquals("RGB(255,0,0)", value); //$NON-NLS-1$

		value = ColorUtil.format(rgbValue, ColorUtil.CSS_RELATIVE_FORMAT);
		assertEquals("RGB(100.0%,0.0%,0.0%)", value); //$NON-NLS-1$

		value = ColorUtil.format(rgbValue, ColorUtil.DEFAULT_FORMAT);
		assertEquals("RGB(255,0,0)", value); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		String value = null;
		assertEquals(null, type.validateXml(design, null, null, null));
		assertEquals(null, type.validateXml(design, null, null, "")); //$NON-NLS-1$

		value = "RGB(255,0,0)"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF0000"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "RGB(255%,0%,0%)"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF0000"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "RGB(65%,0%,0%)"; //$NON-NLS-1$
		assertNotNull(type.validateXml(design, null, null, value));

		value = "RGB(65.01%,0%,0%)"; //$NON-NLS-1$
		assertNotNull(type.validateXml(design, null, null, value));

		value = "RGB(  255 % , 0 % , 0 % )"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF0000"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "RGB(  255.01 % , 0.0 % , 0.0 % )"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF0000"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "RGB(450,350,0)"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FFFF00"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "red"; //$NON-NLS-1$
		assertEquals("red", type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "123134"; //$NON-NLS-1$
		assertEquals("123134", type.validateXml(design, null, null, value).toString()); //$NON-NLS-1$

		value = "1234567890"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FFFFFF").toString(), type.validateXml(design, null, null, value).toString()); //$NON-NLS-1$

		value = "#FF00FF"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF00FF"), type.validateXml(design, null, null, value)); //$NON-NLS-1$

		value = "#F0F"; //$NON-NLS-1$
		assertEquals(Integer.decode("#FF00FF"), type.validateXml(design, null, null, value)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.COLOR_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.COLOR_TYPE_NAME, type.getName());
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

		// String
		assertEquals(null, type.validateValue(design, null, propDefn, " ")); //$NON-NLS-1$

		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "#F0F")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "#FF00FF")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF"), type.validateValue(design, null, propDefn, "#FFFFFFF")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "0xFF00FF")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(new Integer(1234567), type.validateValue(design, null, propDefn, "1234567")); //$NON-NLS-1$

		assertEquals("red", type.validateValue(design, null, propDefn, "red")); //$NON-NLS-1$//$NON-NLS-2$

		assertEquals(Integer.decode("#FFFFFF"), //$NON-NLS-1$
				type.validateValue(design, null, propDefn, "rgb(100.0%,100.0%,100.0%)")); //$NON-NLS-1$
		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "rgb(100.0%,0.0%,100.0%)")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "rgb(100.0%,0.0%,500.0%)")); //$NON-NLS-1$//$NON-NLS-2$

		assertEquals(Integer.decode("#FFFFFF"), type.validateValue(design, null, propDefn, "rgb(255,255,255)")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF"), type.validateValue(design, null, propDefn, "rgb(255,0,255)")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF"), type.validateValue(design, null, propDefn, "rgb(400,400,400)")); //$NON-NLS-1$//$NON-NLS-2$

		// Integer
		Integer value = Integer.decode("#FF00FF"); //$NON-NLS-1$
		assertEquals(value, type.validateValue(design, null, propDefn, value));

		try {
			type.validateValue(design, null, propDefn, Integer.decode("#FFFFFFF")); //$NON-NLS-1$
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
		assertEquals(null, type.validateInputString(design, null, null, null));
		assertEquals(null, type.validateInputString(design, null, null, "")); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals("red", type.validateInputString(design, null, propDefn, "\u7ea2\u8272")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("myColor1", type.validateInputString(design, null, propDefn, "myColor1")); //$NON-NLS-1$ //$NON-NLS-2$

		// other points have been covered in validateValue.
	}

	public void testToCSSCompatibleColor() {
		assertEquals(null, type.toCSSCompatibleColor(design, null));
		assertEquals("red", type.toCSSCompatibleColor(design, "red")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("RGB(18,52,250)", type.toCSSCompatibleColor(design, "myColor1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("RGB(255,255,255)", type.toCSSCompatibleColor(design, "myColor2")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("RGB(255,0,255)", type.toCSSCompatibleColor(design, Integer.decode("#FF00FF"))); //$NON-NLS-1$//$NON-NLS-2$

		// other type
		assertEquals(null, type.toCSSCompatibleColor(design, new Object()));
	}

	public void testToCssColor() {
		assertEquals(null, type.toCssColor(design, null));
		assertEquals("red", type.toCssColor(design, "red")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("#ff00ff", type.toCssColor(design, Integer.decode("#FF00FF"))); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("#ffffff", type.toCssColor(design, "myColor2")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
		assertEquals(-1.0d, type.toDouble(design, null), 0);
		assertEquals(123.0d, type.toDouble(design, new Integer(123)), 0);
		assertEquals(Integer.decode("#FF0000").doubleValue(), type.toDouble(design, "red"), 0); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF").doubleValue(), type.toDouble(design, "myColor2"), 0); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
		assertEquals(-1, type.toInteger(design, null));
		assertEquals(123, type.toInteger(design, new Integer(123)));
		assertEquals(Integer.decode("#FF0000").intValue(), type.toInteger(design, "red")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF").intValue(), type.toInteger(design, "myColor2")); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals("red", type.toXml(design, propDefn, "red")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("#FF00FF", type.toXml(design, propDefn, Integer.decode("#FF00FF"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("red", type.toString(design, propDefn, "red")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("#FF00FF", type.toString(design, propDefn, Integer.decode("#FF00FF"))); //$NON-NLS-1$ //$NON-NLS-2$

		// other type.
		assertEquals(null, type.toString(design, propDefn, new Object()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */

	public void testToDisplayString() throws PropertyValueException {
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals("Red", type.toDisplayString(design, propDefn, "red")); //$NON-NLS-1$ //$NON-NLS-2$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals("\u7ea2\u8272", type.toDisplayString(design, propDefn, "red")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("Color 2", type.toDisplayString(design, propDefn, "myColor2")); //$NON-NLS-1$ //$NON-NLS-2$

		design.getSession().setColorFormat(ColorUtil.CSS_ABSOLUTE_FORMAT);
		assertEquals("RGB(255,0,255)", type.toDisplayString(design, propDefn, Integer.decode("#FF00FF"))); //$NON-NLS-1$ //$NON-NLS-2$

		design.getSession().setColorFormat(ColorUtil.CSS_RELATIVE_FORMAT);
		assertEquals("RGB(100.0%,0.0%,100.0%)", type.toDisplayString(design, propDefn, Integer.decode("#FF00FF"))); //$NON-NLS-1$ //$NON-NLS-2$
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
