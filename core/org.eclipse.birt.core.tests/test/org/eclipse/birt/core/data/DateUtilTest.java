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
package org.eclipse.birt.core.data;

import java.text.ParseException;

import org.junit.Test;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 */
public class DateUtilTest extends TestCase {
	/**
	 * Test DataTypeUtil#checkValid
	 */
	@Test
	public void testCheckValid() {
		ULocale locale;
		DateFormat df;
		String dateStr;
		boolean isValid;

		// ------------test of Locale.UK
		locale = ULocale.UK; // dd/MM/yy

		df = DateFormat.getDateInstance(DateFormat.SHORT, locale.toLocale());
		dateStr = "25/11/16 ";
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);

		dateStr = "25/11/6 ";
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);

		dateStr = "2005/11/16 "; // invalid dd
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		// ------------test of Locale.US
		locale = ULocale.US; // MM/dd/yy
		df = DateFormat.getDateInstance(DateFormat.SHORT, locale.toLocale());
		dateStr = "11/25/16";
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);

		dateStr = "21/11/6"; // invalid MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "2005/11/6"; // invalid MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "11/44/16"; // invalid dd
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "11/31/1990"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "02/29/1990"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "02/28/1990"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);

		// ------------test of Locale.CHINA
		// Because of ICU behavior change, replace "-" for "/"
		locale = ULocale.CHINA; // yy-M-d
		df = DateFormat.getDateInstance(DateFormat.SHORT, locale.toLocale());
		dateStr = "2005/3/3";
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);

		dateStr = "2005/13/6"; // invalid MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "2005/11/36"; // invalid dd
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "5/13/2005"; // invalid dd
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "2005/11/31"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);

		dateStr = "2005/2/29"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertFalse(isValid);

		dateStr = "2005/2/28"; // invalid dd to MM
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			fail("can not reach here");
		}
		isValid = DateUtil.checkValid(df, dateStr);
		assertTrue(isValid);
	}

}
