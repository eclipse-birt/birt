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

package org.eclipse.birt.report.model.api;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Tests the ParameterUtil.
 */

public class ParameterValidationUtilTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the validation of the boolean type.
	 * 
	 * @throws Exception
	 */

	public void testBoolean() throws Exception {
		// in the JAPAN locale

		ThreadResources.setLocale(ULocale.ENGLISH);
		String value = null;

		assertEquals(null,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, null, ULocale.JAPAN));
		assertEquals(null,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, "", ULocale.JAPAN)); //$NON-NLS-1$

		// the input value is locale dependent

		value = "\u771f"; //$NON-NLS-1$
		assertEquals(Boolean.TRUE, ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null,
				value, new ULocale("aa"))); //$NON-NLS-1$
		assertEquals(Boolean.TRUE, ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null,
				value, new ULocale("aa"))); //$NON-NLS-1$
		value = "\u5047"; //$NON-NLS-1$
		assertEquals(Boolean.FALSE, ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null,
				value, new ULocale("aa"))); //$NON-NLS-1$
		assertEquals(Boolean.FALSE, ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null,
				value, new ULocale("aa"))); //$NON-NLS-1$

		// the input value is locale independent

		value = "true"; //$NON-NLS-1$
		assertEquals(Boolean.TRUE,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, value, ULocale.JAPAN));
		assertEquals(Boolean.TRUE,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, value, ULocale.JAPAN));
		value = "false"; //$NON-NLS-1$
		assertEquals(Boolean.FALSE,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, value, ULocale.JAPAN));
		assertEquals(Boolean.FALSE,
				ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, value, ULocale.JAPAN));

		// catch some exception
		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, "tru", ULocale.JAPAN); //$NON-NLS-1$
			fail();
		} catch (ValidationValueException e) {
			System.out.println(e.getMessage());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, "fals", ULocale.JAPAN); //$NON-NLS-1$
			fail();
		} catch (ValidationValueException e) {
			System.out.println(e.getMessage());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/**
	 * Tests the validation of the float and decimal type.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testNumber() throws Exception {
		double input = 123.4564d;

		// validates the input and get the locale and format independent result
		String value = "123.0"; //$NON-NLS-1$
		ThreadResources.setLocale(ULocale.FRANCE);
		NumberFormatter formatter = new NumberFormatter(ThreadResources.getLocale());
		formatter.applyPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER);
		value = formatter.format(input);
		assertEquals("123.456", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_FLOAT, DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER, value,
				ULocale.FRANCE).toString());
		ThreadResources.setLocale(ULocale.CHINA);
		formatter = new NumberFormatter(ThreadResources.getLocale());
		formatter.applyPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER);
		value = formatter.format(input);
		assertEquals("123.456", //$NON-NLS-1$
				ParameterValidationUtil
						.validate(DesignChoiceConstants.PARAM_TYPE_FLOAT,
								DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER, value, ULocale.CHINA)
						.toString());

		// Decimal type parameter will be converted into BigDecimal.
		Object resultVal = ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DECIMAL,
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER, value, ULocale.CHINA);
		assertTrue(resultVal instanceof BigDecimal);
		assertEquals("123.456", resultVal.toString()); //$NON-NLS-1$

		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DECIMAL,
					DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC, "ergh", //$NON-NLS-1$
					ULocale.FRANCE);
			fail();
		} catch (ValidationValueException e) {
			System.out.println(e.getMessage());
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// test valid integer value
		formatter.applyPattern(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER);
		value = formatter.format(input);
		resultVal = ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_INTEGER,
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED, value, ULocale.CHINA);
		assertTrue(resultVal instanceof Integer);
		assertEquals("123", resultVal.toString()); //$NON-NLS-1$

		assertEquals("13", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_INTEGER, DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				"13c1.9ab2", //$NON-NLS-1$
				ULocale.CHINA).toString());
	}

	/**
	 * Tests the validation of the time type.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testTime() throws Exception {
		String value1 = "12:30:31"; //$NON-NLS-1$

		java.sql.Time date = (java.sql.Time) ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_TIME,
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, value1);
		assertEquals("12:30:31", date.toString());//$NON-NLS-1$

		String value2 = "122a:30:31";//$NON-NLS-1$
		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_TIME,
					DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, value2);
			fail();
		} catch (ValidationValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/**
	 * Tests the validation of date type.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testDate() throws Exception {
		String value1 = "1998-09-13"; //$NON-NLS-1$

		java.sql.Date date = (java.sql.Date) ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATE,
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, value1);
		assertEquals("1998-09-13", date.toString());//$NON-NLS-1$

		String value2 = "1992a-123-12";//$NON-NLS-1$
		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATE,
					DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, value2);
			fail();
		} catch (ValidationValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/**
	 * Tests validation of date time type.
	 * 
	 * @throws Exception
	 */

	public void testDateTime() throws Exception {
		// validates the input locale-dependent date time
		// and get the result locale-independent and standard
		// output string, test the result is the same with
		// the two different locale and input string about the same
		// date time

		String value1 = "1998-09-13 20:01:44"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE, value1);
		String value2 = "1998-09-13 00:00:00"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE, value2);
		String value3 = "1998-09-13 00:00:00"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE, value3);
		String value4 = "1998-09-13 00:00:00"; //$NON-NLS-1$

		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE, value4);
		String value5 = "1970-01-01 19:01:44"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME, value5);
		String value6 = "1970-01-01 20:01:44"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME, value6);
		String value7 = "1970-01-01 20:01:00"; //$NON-NLS-1$
		testDateTimeByFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME, value7);
	}

	/**
	 * Tests the validation of the date time type.
	 * 
	 * @param format the format choice string
	 * @param result the validation result string
	 * 
	 * @throws Exception
	 * 
	 */

	private void testDateTimeByFormat(String format, String result) throws Exception {
		// in JAP locale

		ThreadResources.setLocale(ULocale.JAPAN);
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Berlin");
		String value = null;
		final SimpleDateFormat formatPattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		formatPattern.setTimeZone(timeZone);
		Calendar dateCal = Calendar.getInstance(ThreadResources.getLocale());
		dateCal.set(1998, 8, 13, 20, 1, 44);
		dateCal.setTimeZone(timeZone);
		DateFormatter formatter = new DateFormatter(ThreadResources.getLocale(), timeZone);
		formatter.applyPattern(format);
		value = formatter.format(dateCal.getTime());
		String resultJAP = formatPattern.format(ParameterValidationUtil
				.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, format, value, ULocale.JAPAN, timeZone));
		assertEquals(result, resultJAP);

		// in EN locale
		ThreadResources.setLocale(ULocale.ENGLISH);
		dateCal = Calendar.getInstance(ThreadResources.getLocale());
		dateCal.set(1998, 8, 13, 20, 1, 44);
		dateCal.setTimeZone(timeZone);
		formatter = new DateFormatter(ThreadResources.getLocale(), timeZone);
		formatter.applyPattern(format);
		value = formatter.format(dateCal.getTime());
		String resultEN = formatPattern.format(ParameterValidationUtil
				.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, format, value, ULocale.ENGLISH, timeZone));
		assertEquals(result, resultEN);

		// the two result value is equal.

		assertEquals(resultJAP, resultEN);

		// Test two kind of date format .

		Object obj = ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, null,
				"1/1/1999 4:50:10 am", ULocale.US); //$NON-NLS-1$
		assertNotNull(obj);
		assertTrue(obj instanceof Date);

		try {
			ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_DATETIME, null, "1999-2-27", ULocale.US); //$NON-NLS-1$
			fail();

		} catch (ValidationValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

	}

	/**
	 * Tests the validation of the string type.
	 * 
	 * @throws Exception
	 */

	public void testString() throws Exception {
		ThreadResources.setLocale(ULocale.ENGLISH);
		String value = null;

		// upper case

		// lower case

		// validate without format

		assertEquals(null, ParameterValidationUtil.validate(DesignChoiceConstants.PARAM_TYPE_STRING, null, value,
				ULocale.ENGLISH));
		value = ""; //$NON-NLS-1$
		assertEquals("", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, null, value, ULocale.ENGLISH));
		value = "ab CD e"; //$NON-NLS-1$
		assertEquals("ab CD e", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, null, value, ULocale.ENGLISH));
		value = "ab $#"; //$NON-NLS-1$
		assertEquals("ab $#", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, null, value, ULocale.ENGLISH));
		value = "(444)444-4444"; //$NON-NLS-1$
		assertEquals("4444444444", ParameterValidationUtil.validate( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER, value,
				ULocale.ENGLISH));
	}

	/**
	 * Tests the function of getDisplayValue().
	 * 
	 */
	public void testGetDisplayValue() {
		// date time type
		Calendar dateCal = Calendar.getInstance(ThreadResources.getLocale());
		dateCal.set(1998, 8, 13, 20, 1, 44);
		Date dateValue = dateCal.getTime();

		assertEquals("13 septembre 1998", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DATETIME, DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE,
				dateValue, ULocale.FRANCE));
		// no format, then we display in (medium, short) pattern
		assertEquals("Sep 13, 1998 8:01 PM", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DATETIME, null, dateValue));

		// date type
		dateValue = new java.sql.Date(100, 0, 1);

		assertEquals("1 janvier 2000", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DATE, DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE, dateValue,
				ULocale.FRANCE));

		assertEquals("January 1, 2000", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DATE, DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE, dateValue,
				ULocale.ENGLISH));

		// no format
		assertEquals("Jan 1, 2000", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DATE, null, dateValue));

		// time type

		java.sql.Time timeValue = new java.sql.Time(14, 20, 30);
		assertEquals("2:20:30 PM", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_TIME, DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
				timeValue, ULocale.ENGLISH));

		assertEquals("14:20:30", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_TIME, DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
				timeValue, ULocale.FRANCE));

		// no format
		assertEquals("2:20:30 PM", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_TIME, null, timeValue));

		// float type
		Double doubleValue = new Double("12345.456"); //$NON-NLS-1$
		assertEquals("12345.456", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_FLOAT, DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				doubleValue, ULocale.ENGLISH));
		assertEquals("12345,456", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_FLOAT, DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				doubleValue, ULocale.GERMAN));
		assertEquals("Currency12345", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_FLOAT, DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, doubleValue,
				ULocale.GERMAN));

		// integer type
		Integer integerValue = new Integer("12345"); //$NON-NLS-1$
		assertEquals("12345", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_INTEGER, DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				integerValue, ULocale.ENGLISH));
		assertEquals("12345", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_INTEGER, DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				integerValue, ULocale.GERMAN));
		assertEquals("Currency12345", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_FLOAT, DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, integerValue,
				ULocale.GERMAN));

		// decimal type, standard format has the precision control

		BigDecimal decimalValue = new BigDecimal("12345678.1234"); //$NON-NLS-1$
		assertEquals("12.345.678,12", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DECIMAL, DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD,
				decimalValue, ULocale.GERMAN));
		assertEquals("12,345,678.12", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_DECIMAL, DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD,
				decimalValue, ULocale.ENGLISH));

		// string type
		String stringValue = "0211234567"; //$NON-NLS-1$
		assertEquals("(021)123-4567", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER,
				stringValue, ULocale.GERMAN));
		assertEquals("(021)123-4567", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER,
				stringValue, ULocale.ENGLISH));
		assertEquals("021123-4567", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_STRING, DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4,
				stringValue, ULocale.GERMAN));

		// boolean type
		assertEquals("false", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, Boolean.FALSE, ULocale.ENGLISH));
		assertEquals("\u5047", ParameterValidationUtil.getDisplayValue( //$NON-NLS-1$
				DesignChoiceConstants.PARAM_TYPE_BOOLEAN, null, Boolean.FALSE, new ULocale("aa"))); //$NON-NLS-1$
	}
}