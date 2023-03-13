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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.util.StringUtil;

import junit.framework.TestCase;

/**
 * TestCases for StringUtil class.
 * <p>
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testIsBlank()}</td>
 * <td>Tests whether input strings are empty (null or "") or not.</td>
 * <td>Returns test results correctly.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testTrimString()}</td>
 * <td>Tests whether trim function works properly.</td>
 * <td>Return values are correct.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testToRgbText()}</td>
 * <td>Tests the function to transform an integer to the format #FFFFFF.</td>
 * <td>Return values are correct.</td>
 * </tr>
 *
 * </table>
 */

public class StringUtilTest extends TestCase {

	/**
	 * test the string trimmed.
	 *
	 */
	public void testTrimString() {
		assertNull(StringUtil.trimString(null));
		assertNull(StringUtil.trimString("")); //$NON-NLS-1$
		assertNull(StringUtil.trimString("   ")); //$NON-NLS-1$
		assertNull(StringUtil.trimString("\t")); //$NON-NLS-1$
		assertEquals("abc d ef", StringUtil.trimString("\tabc d ef  ")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * test the string converted from rgb value.
	 *
	 */
	public void testToRgbText() {
		assertEquals("#000000", StringUtil.toRgbText(0)); //$NON-NLS-1$
		assertEquals("#00ff00", StringUtil.toRgbText(0xff00)); //$NON-NLS-1$
		assertEquals("#ffffff", StringUtil.toRgbText(0xffffff)); //$NON-NLS-1$

		// if the rgb is larger than 0xffffff
		assertEquals("#ffffff", StringUtil.toRgbText(0xffffff + 0xf)); //$NON-NLS-1$
	}

	/**
	 * test whether it is blank.
	 *
	 */
	public void testIsBlank() {
		assertTrue(StringUtil.isBlank(null));
		assertTrue(StringUtil.isBlank("")); //$NON-NLS-1$
		assertTrue(StringUtil.isBlank("   ")); //$NON-NLS-1$
		assertFalse(StringUtil.isBlank(" abc d ef  ")); //$NON-NLS-1$
	}

	/**
	 * test whether it is empty.
	 *
	 */
	public void testIsEmpty() {
		assertTrue(StringUtil.isEmpty(null));
		assertTrue(StringUtil.isEmpty("")); //$NON-NLS-1$
		assertFalse(StringUtil.isEmpty("   ")); //$NON-NLS-1$
		assertFalse(StringUtil.isEmpty(" abc d ef  ")); //$NON-NLS-1$
	}

	/**
	 * Tests <code>doubleToString(double)</code>.
	 */

	public void testDoubleToString() {
		assertEquals("123456.789", StringUtil.doubleToString(123456.7890123, 3)); //$NON-NLS-1$
		assertEquals("1234567.89", StringUtil.doubleToString(1234567.890123, 3));//$NON-NLS-1$
		assertEquals("12345678.901", StringUtil.doubleToString(12345678.90123, 3));//$NON-NLS-1$
		assertEquals("123456789.012", StringUtil.doubleToString(123456789.0123, 3));//$NON-NLS-1$
		assertEquals("1234567890.123", StringUtil.doubleToString(1234567890.123, 3));//$NON-NLS-1$
		assertEquals("12345678901.23", StringUtil.doubleToString(12345678901.23, 3));//$NON-NLS-1$
		assertEquals("1234567890123", StringUtil.doubleToString(1234567890123.0, 3));//$NON-NLS-1$

		assertEquals("1234567890123.457", StringUtil.doubleToString(1234567890123.4567890, 3));//$NON-NLS-1$
		assertEquals("12345678901234.568", StringUtil.doubleToString(12345678901234.567890, 3));//$NON-NLS-1$
		assertEquals("123456789012345.67", StringUtil.doubleToString(123456789012345.67890, 3));//$NON-NLS-1$
		assertEquals("1234567890123456.8", StringUtil.doubleToString(1234567890123456.7890, 3));//$NON-NLS-1$
		assertEquals("12345678901234568", StringUtil.doubleToString(12345678901234567.890, 3));//$NON-NLS-1$
		assertEquals("123456789012345680", StringUtil.doubleToString(123456789012345678.90, 3));//$NON-NLS-1$

		assertEquals("123456", StringUtil.doubleToString(123456, 0)); //$NON-NLS-1$
		assertEquals("123456", StringUtil.doubleToString(123456, 3)); //$NON-NLS-1$
		assertEquals("123457", StringUtil.doubleToString(123456.7890123, 0)); //$NON-NLS-1$
		assertEquals("123457", StringUtil.doubleToString(123456.7890123, -1)); //$NON-NLS-1$

		// test the E-expo format double

		assertEquals("123000", StringUtil.doubleToString(123.0000E3, 3)); //$NON-NLS-1$
		assertEquals("1234560", StringUtil.doubleToString(123.456E4, 3)); //$NON-NLS-1$
		assertEquals("123.456", StringUtil.doubleToString(1.23456E2, 3)); //$NON-NLS-1$
		assertEquals("123.457", StringUtil.doubleToString(1.2345678E2, 3)); //$NON-NLS-1$
		assertEquals("123.456", StringUtil.doubleToString(123456E-3, 10)); //$NON-NLS-1$
		assertEquals("0.00001235", StringUtil.doubleToString(1.235E-5, 10)); //$NON-NLS-1$
		assertEquals("0.0021", StringUtil.doubleToString(2.1E-3, 10)); //$NON-NLS-1$
		assertEquals("0.0005678", StringUtil.doubleToString(0.5678E-3, 10)); //$NON-NLS-1$
		assertEquals("0.001", StringUtil.doubleToString(0.5678E-3, 3)); //$NON-NLS-1$

	}

	/**
	 * Tests <code>isEqual(String, String)</code>.
	 */

	public void testIsEqual() {
		assertTrue(StringUtil.isEqual(null, null));
		assertFalse(StringUtil.isEqual(null, "abc")); //$NON-NLS-1$
		assertTrue(StringUtil.isEqual("abc", "abc")); //$NON-NLS-1$//$NON-NLS-2$
		assertFalse(StringUtil.isEqual("abcd", "abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(StringUtil.isEqual("Abc", "abc")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests <code>isEqualIgnoreCase(String, String)</code>.
	 */

	public void testIsEqualIgnoreCase() {
		assertTrue(StringUtil.isEqualIgnoreCase(null, null));
		assertFalse(StringUtil.isEqualIgnoreCase(null, "abc")); //$NON-NLS-1$
		assertTrue(StringUtil.isEqualIgnoreCase("abc", "abc")); //$NON-NLS-1$//$NON-NLS-2$
		assertFalse(StringUtil.isEqualIgnoreCase("abcd", "abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(StringUtil.isEqualIgnoreCase("Abc", "abc")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests extractFileName method.
	 */

	public void testExtractFileName() {
		assertEquals("abc", StringUtil.extractFileName("c:\\home\\abc.xml")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", StringUtil.extractFileName("c:\\home\\abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", StringUtil.extractFileName("/home/user/abc.xml")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", StringUtil.extractFileName("/home/user/abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", StringUtil.extractFileName("/home/user.cliff/abc.xml")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", StringUtil.extractFileName("/home/user.cliff/abc")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests trimQuotes method.
	 */

	public void testTrimQuotes() {
		assertEquals("abc.bca", StringUtil.trimQuotes("\"abc.bca\"")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ab\"c.bca", StringUtil.trimQuotes("\"ab\"c.bca\"")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("\"abc.bca", StringUtil.trimQuotes("\"abc.bca")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc.bca\"", StringUtil.trimQuotes("abc.bca\"")); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
