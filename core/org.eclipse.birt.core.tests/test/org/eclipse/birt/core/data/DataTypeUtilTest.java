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

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Types;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.BaseScriptable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Wrapper;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 * Test case for DataTypeUtil
 */
public class DataTypeUtilTest extends TestCase {

	public Object[] testObject;
	public Object[] resultInteger;
	public Object[] resultBigDecimal;
	public Object[] resultBoolean;
	public Object[] resultDate;
	public Object[] resultDouble;
	public Object[] resultString;
	public Object[] resultLocaleNeutralString;
	// variables for testToAutoValue method
	public Object[] autoValueInputObject;
	public Object[] autoValueExpectedResult;

	// for test of toBigDecimal
	public Object[] testObjectDecimal;
	public Object[] resultObjectDecimal;

	// for test of toDouble
	public Object[] testObjectDouble;
	public Object[] resultObjectDouble;

	private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());

	/*
	 * @see TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {

		// input Data
		testObject = new Object[] { new Integer(1), new Integer(0), BigDecimal.valueOf(Integer.MAX_VALUE),
				BigDecimal.valueOf(Integer.MAX_VALUE + 1), BigDecimal.valueOf(Integer.MIN_VALUE),
				BigDecimal.valueOf(Integer.MIN_VALUE - 1), BigDecimal.valueOf(0l), Boolean.valueOf(true),
				Boolean.valueOf(false), (new GregorianCalendar(2004 + 1900, 1, 1)).getTime(), Double.valueOf("1.1"),
				Double.valueOf("0"), null, "testString", "12345", "10/11/2005", "10/11/2005 2:30 am",
				"10/11/2005 2:25:46 pm" };
		autoValueInputObject = new Object[] { "1", "0", String.valueOf(Integer.MAX_VALUE),
				String.valueOf(Integer.MAX_VALUE + 1), String.valueOf(Integer.MIN_VALUE),
				String.valueOf(Integer.MIN_VALUE - 1), String.valueOf(0l), "true", "false",
				((new GregorianCalendar(2004 + 1900, 1, 1)).getTime()).toString(), "1.1", "0", null, "1.00000000001",
				"testString", "12345", "10/11/2005", "10/11/2005 2:30 am", "10/11/2005 2:25:46 pm" };
		// the expected results of toDate()
		resultDate = new Object[] { new Exception(""), new Exception(""), new Exception(""), new Exception(""),
				new Exception(""), new Exception(""), new Exception(""), new Exception(""), new Exception(""),
				(new GregorianCalendar(2004 + 1900, 1, 1)).getTime(), new Date(1L), new Date(0L), null,
				new Exception(""), new Exception(""), (new GregorianCalendar(2005, 10 - 1, 11)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 2, 30)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 14, 25, 46)).getTime() };
		// the expected results of toInteger()
		resultInteger = new Object[] { new Integer(1), new Integer(0), new Integer(Integer.MAX_VALUE),
				new Integer(Integer.MAX_VALUE + 1), new Integer(Integer.MIN_VALUE), new Integer(Integer.MIN_VALUE - 1),
				new Integer(0), new Integer(1), new Integer(0), new Exception(""), new Integer((int) 1.1),
				new Integer(0), null, new Exception(""), new Integer("12345"), new Integer(10), new Integer(10),
				new Integer(10) };
		// the expected results of toBigDecimal()
		resultBigDecimal = new Object[] { new BigDecimal("1"), new BigDecimal("0"), new BigDecimal(Integer.MAX_VALUE),
				new BigDecimal(Integer.MAX_VALUE + 1), new BigDecimal(Integer.MIN_VALUE),
				new BigDecimal(Integer.MIN_VALUE - 1), new BigDecimal(0), new BigDecimal(1), new BigDecimal(0),
				new BigDecimal(((Date) resultDate[9]).getTime()), new BigDecimal("1.1"), new BigDecimal("0.0"), null,
				new Exception(""), new BigDecimal("12345"), new BigDecimal(10), new BigDecimal(10),
				new BigDecimal(10) };
		// the expected results of toBoolean()
		resultBoolean = new Object[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
				Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, new Exception(""), Boolean.TRUE,
				Boolean.FALSE, null, new Exception(""), Boolean.TRUE, new Exception(""), new Exception(""),
				new Exception("") };
		// the expected results of toDouble()
		resultDouble = new Object[] { new Double(1), new Double(0), new Double(Integer.MAX_VALUE),
				new Double(Integer.MAX_VALUE + 1), new Double(Integer.MIN_VALUE), new Double(Integer.MIN_VALUE - 1),
				new Double(0), new Double(1), new Double(0), new Double(((Date) resultDate[9]).getTime()),
				new Double(1.1), new Double(0), null, new Exception(""), new Double("12345"), new Double(10),
				new Double(10), new Double(10) };
		// the expected results of toString()
		resultString = new Object[] { "1", "0", String.valueOf(Integer.MAX_VALUE),
				String.valueOf(Integer.MAX_VALUE + 1), String.valueOf(Integer.MIN_VALUE),
				String.valueOf(Integer.MIN_VALUE - 1), "0", "true", "false", df.format(resultDate[9]), "1.1", "0.0",
				null, "testString", "12345", "10/11/2005", "10/11/2005 2:30 am", "10/11/2005 2:25:46 pm" };
		autoValueExpectedResult = new Object[] { new Integer(1), new Integer(0), new Integer(Integer.MAX_VALUE),
				new Integer(Integer.MAX_VALUE + 1), new Integer(Integer.MIN_VALUE), new Integer(Integer.MIN_VALUE - 1),
				new Integer(0), "true", "false", ((Date) resultDate[9]).toString(), new Double(1.1), new Integer(0),
				null, new Integer(1), "testString", new Integer("12345"),
				(new GregorianCalendar(2005, 10 - 1, 11)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 2, 30)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 14, 25, 46)).getTime(), };

		// the expected results of toString()
		resultLocaleNeutralString = new Object[] { "1", "0", String.valueOf(Integer.MAX_VALUE),
				String.valueOf(Integer.MAX_VALUE + 1), String.valueOf(Integer.MIN_VALUE),
				String.valueOf(Integer.MIN_VALUE - 1), "0", "true", "false", "3904-02-01 00:00:00.000", "1.1", "0.0",
				null, "testString", "12345", "10/11/2005", "10/11/2005 2:30 am", "10/11/2005 2:25:46 pm" };
		autoValueExpectedResult = new Object[] { new Integer(1), new Integer(0), new Integer(Integer.MAX_VALUE),
				new Integer(Integer.MAX_VALUE + 1), new Integer(Integer.MIN_VALUE), new Integer(Integer.MIN_VALUE - 1),
				new Integer(0), "true", "false", ((Date) resultDate[9]).toString(), new Double(1.1), new Integer(0),
				null, new Integer(1), "testString", new Integer("12345"),
				(new GregorianCalendar(2005, 10 - 1, 11)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 2, 30)).getTime(),
				(new GregorianCalendar(2005, 10 - 1, 11, 14, 25, 46)).getTime(), };
		// for test of toBigDecimal
		testObjectDecimal = new Object[] { new Double(Double.NaN), new Double(Double.POSITIVE_INFINITY),
				new Double(Double.NEGATIVE_INFINITY), new Double(Double.MAX_VALUE), new Double(Double.MIN_VALUE) };
		// Behavior change, will return null rather than throwing exception
		resultObjectDecimal = new Object[] { null, null, null, new BigDecimal(new Double(Double.MAX_VALUE).toString()),
				new BigDecimal(new Double(Double.MIN_VALUE).toString()) };

		// for test of toDouble
		testObjectDouble = new Object[] { new Float(1), };
		resultObjectDouble = new Object[] { new Double(1), };
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		testObject = null;
	}

	@Test
	public void testToInteger() throws BirtException {
		Integer result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toInteger(testObject[i]);
				if (resultInteger[i] instanceof Exception)
					fail("Should throw Exception.");
				assertEquals(result, resultInteger[i]);
			} catch (BirtException e) {
				if (!(resultInteger[i] instanceof Exception))
					fail("Should not throw Exception.");
			}
		}
		assertEquals(new Integer(1), DataTypeUtil.toInteger("1.8", ULocale.US));

		// test overflow check
		try {
			result = DataTypeUtil.toInteger(Long.valueOf(Long.MAX_VALUE));
			fail("Should throw exception ");
		} catch (BirtException e) {

		}

		try {
			result = DataTypeUtil.toInteger(Long.valueOf(Long.MIN_VALUE));
			fail("Should throw exception ");
		} catch (BirtException e) {

		}
	}

	@Test
	public void testToBigDecimal() {
		BigDecimal result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toBigDecimal(testObject[i]);
				if (resultBigDecimal[i] instanceof Exception)
					fail("Should throw Exception.");
				assertEquals(result, resultBigDecimal[i]);
			} catch (BirtException e) {
				if (!(resultBigDecimal[i] instanceof Exception))
					fail("Should not throw Exception.");
			}
		}
		for (int i = 0; i < testObjectDecimal.length; i++) {
			try {
				result = DataTypeUtil.toBigDecimal(testObjectDecimal[i]);
				if (resultObjectDecimal[i] instanceof Exception)
					fail("Should throw Exception.");
				assertEquals(result, resultObjectDecimal[i]);
			} catch (BirtException e) {
				if (!(resultObjectDecimal[i] instanceof Exception))
					fail("Should not throw Exception.");
			} catch (Exception e) {
				fail("Should throw BirtException.");
			}
		}
	}

	@Test
	public void testToBoolean() {
		Boolean result;
		for (int i = 0; i < testObject.length; i++) {
			System.out.println(i);
			try {
				result = DataTypeUtil.toBoolean(testObject[i]);
				if (resultBoolean[i] instanceof Exception)
					fail("Should throw Exception.");
				assertEquals(result, resultBoolean[i]);
			} catch (BirtException dteEx) {
				if (!(resultBoolean[i] instanceof Exception))
					fail("Should not throw Exception.");
			}
		}

		try {
			assertTrue(DataTypeUtil.toBoolean(new Double(0.1)).booleanValue());
			assertTrue(DataTypeUtil.toBoolean(new Double(-0.1)).booleanValue());
			assertTrue(DataTypeUtil.toBoolean(new Double(1)).booleanValue());
			assertTrue(DataTypeUtil.toBoolean(new Double(1)).booleanValue());
			assertFalse(DataTypeUtil.toBoolean(new Double(0)).booleanValue());
		} catch (BirtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testToSqlDate() throws BirtException {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		{

			java.sql.Date date = DataTypeUtil.toSqlDate("1999-2-11");
			cal.set(1999, 1, 11);
			assertEquals(cal.getTime(), date);
		}

		{

			java.sql.Date date1 = DataTypeUtil.toSqlDate("99-2-11");
			cal.set(99, 1, 11);
			assertEquals(cal.getTime(), date1);
		}

		try {
			DataTypeUtil.toSqlDate("9921111");
			fail("Should not arrive here");
		} catch (BirtException e) {
			// exception expected
			e.printStackTrace();
		}

		{
			java.sql.Date date1 = DataTypeUtil.toSqlDate(100000000000.123D);
			cal.set(1973, 2, 3);
			assertEquals(cal.getTime(), date1);
		}
	}

	@Test
	public void testToSqlTime() throws BirtException {
		Time temp = getTime(11, 11, 25, 0);
		Time time = DataTypeUtil.toSqlTime("11:11:25");
		assertEquals(time.toString(), temp.toString());
		time = DataTypeUtil.toSqlTime("11:11:25 am");
		assertEquals(time.toString(), temp.toString());
		time = DataTypeUtil.toSqlTime("11:11:25am");
		assertEquals(time.toString(), temp.toString());

		temp = getTime(18, 11, 25, 0);
		time = DataTypeUtil.toSqlTime("18:11:25");
		assertEquals(time.toString(), temp.toString());

		temp = getTime(18, 11, 25, 12);
		time = DataTypeUtil.toSqlTime("18:11:25.12");
		assertEquals(time.toString(), temp.toString());

		time = DataTypeUtil.toSqlTime("6:11:25 pm");
		assertEquals(time, temp);
		time = DataTypeUtil.toSqlTime("6:11:25pm");
		assertEquals(time, temp);
		failSqlTimeString("99dfa-2-11");
		failSqlTimeString("18:11:25 pm");
		failSqlTimeString("18:11:25 am");
		failSqlTimeString("1:11:25 pmm");
		failSqlTimeString("1:11:65 am");
		failSqlTimeString("1:61:25 pm");

		time = DataTypeUtil.toSqlTime(0D);
		assertEquals(getTimeUTC(0, 0, 0, 0), time);

		time = DataTypeUtil.toSqlTime(1000D);
		assertEquals(getTimeUTC(0, 0, 1, 0), time);

		time = DataTypeUtil.toSqlTime(999.789D);
		assertEquals(getTimeUTC(0, 0, 1, 0), time);

		time = DataTypeUtil.toSqlTime(100000000000.123D);
		assertEquals(getTimeUTC(9, 46, 40, 0), time);
	}

	private Time getTime(int hour, int minute, int second, int millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return new java.sql.Time(calendar.getTimeInMillis());
	}

	private Time getTimeUTC(int hour, int minute, int second, int millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.setTimeZone(TimeZone.GMT_ZONE);
		return new java.sql.Time(calendar.getTimeInMillis());
	}

	/**
	 *
	 * @param value
	 */
	private void failSqlTimeString(String value) {
		try {
			DataTypeUtil.toSqlTime(value);
			fail("Should not arrive here");
		} catch (BirtException e) {
		}
	}

	@Test
	public void testToDate() {
		Date result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toDate(testObject[i]);
				if (resultDate[i] instanceof Exception)
					fail("Should throw Exception.");
				assertEquals(result, resultDate[i]);

			} catch (BirtException e) {
				if (!(resultDate[i] instanceof Exception))
					fail("Should not throw Exception.");
			}

		}
	}

	@Test
	public void testToDate1() {
		String[] testStrings = { "1997", "1997-07", "1997-07-16", "1997-07-16T19:20+02", "1997-07-16T19:20:30GMT+01:00",
				"1997-07-16T19:20:30.045+01:00", "1997-07-16 19:20+01:00", "1997-07-16 19:20:30+01:00",
				"1997-07-16 19:20:30.045+01:00", "1997-07-16 19:20:30.045 GMT+01:00", "1997-07-16T19:20:30.045-01:00" };
		Calendar calendar = Calendar.getInstance();

		Date[] resultDates = new Date[11];

		calendar.clear();
		calendar.set(1997, 0, 1);
		resultDates[0] = calendar.getTime();
		calendar.set(1997, 6, 1);
		resultDates[1] = calendar.getTime();
		calendar.set(1997, 6, 16);
		resultDates[2] = calendar.getTime();

		calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		calendar.set(1997, 6, 16, 17, 20, 0);
		resultDates[3] = calendar.getTime();
		calendar.set(1997, 6, 16, 18, 20, 30);
		resultDates[4] = calendar.getTime();
		calendar.set(1997, 6, 16, 18, 20, 30);
		calendar.set(Calendar.MILLISECOND, 45);
		resultDates[5] = calendar.getTime();
		calendar.set(1997, 6, 16, 18, 20, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		resultDates[6] = calendar.getTime();
		calendar.set(1997, 6, 16, 18, 20, 30);
		resultDates[7] = calendar.getTime();
		calendar.set(1997, 6, 16, 18, 20, 30);
		calendar.set(Calendar.MILLISECOND, 45);
		resultDates[8] = calendar.getTime();
		resultDates[9] = calendar.getTime();
		// "1997-07-16T19:20:30.45-01:00"
		calendar.set(1997, 6, 16, 20, 20, 30);
		calendar.set(Calendar.MILLISECOND, 45);
		resultDates[10] = calendar.getTime();

		for (int i = 0; i < testStrings.length; i++) {
			try {
				Date dateResult = DataTypeUtil.toDate(testStrings[i]);
				assertEquals(dateResult, resultDates[i]);
			} catch (BirtException e) {
				e.printStackTrace();
				fail("Should not throw Exception.");
			}

		}
	}

	@Test
	public void testToDate2() {
		String[] dateStrings = { "Jan 11, 2002", "Jan 11, 2002", "Feb 12, 1981 6:17 AM" };
		String[] timeZoneIDs = { "GMT+00:00", "GMT-02:00", "GMT+03:00" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date[] resultDates = new Date[3];
		calendar.clear();
		calendar.set(2002, 0, 11, 0, 0, 0);
		resultDates[0] = calendar.getTime();
		calendar.clear();
		calendar.set(2002, 0, 11, 2, 0, 0);
		resultDates[1] = calendar.getTime();
		calendar.clear();
		calendar.set(1981, 1, 12, 3, 17, 0);
		resultDates[2] = calendar.getTime();

		for (int i = 0; i < dateStrings.length; i++) {
			try {
				Date dateResult = DataTypeUtil.toDate(dateStrings[i], ULocale.US, TimeZone.getTimeZone(timeZoneIDs[i]));
				assertEquals(dateResult, resultDates[i]);
			} catch (BirtException e) {
				fail("Should not throw Exception.");
			}
		}
	}

	@Test
	public void testToDateForMysql() throws BirtException {
		String source = "12/30/2008 13:00";
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2008, 11, 30, 13, 0, 0);
		assertEquals(DataTypeUtil.toDate(source), calendar.getTime());
	}

	@Test
	public void testToDate3() {
		String[] dateStrings = { "Jan 11, 2002", "Jan 11, 2002", "Feb 12, 1981 6:17 AM" };
		String[] timeZoneIDs = { "GMT+00:00", "GMT-02:00", "GMT+03:00" };
		String[] ISODateStrings = { "1997-07-16", "1997-07-16T19:20", "1997-07-16T19:20:30" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date[] resultDates1 = new Date[3];
		calendar.clear();
		calendar.set(2002, 0, 11, 0, 0, 0);
		resultDates1[0] = calendar.getTime();
		calendar.clear();
		calendar.set(2002, 0, 11, 2, 0, 0);
		resultDates1[1] = calendar.getTime();
		calendar.clear();
		calendar.set(1981, 1, 12, 3, 17, 0);
		resultDates1[2] = calendar.getTime();

		for (int i = 0; i < dateStrings.length; i++) {
			try {
				Date dateResult = DataTypeUtil.toDate(dateStrings[i], ULocale.US, TimeZone.getTimeZone(timeZoneIDs[i]));
				assertEquals(dateResult, resultDates1[i]);
			} catch (BirtException e) {
				fail("Should not throw Exception.");
			}
		}

		calendar = Calendar.getInstance();
		Date[] resultDates2 = new Date[3];
		calendar.clear();
		calendar.set(1997, 6, 16, 0, 0, 0);
		resultDates2[0] = calendar.getTime();
		calendar.clear();
		calendar.set(1997, 6, 16, 19, 20, 0);
		resultDates2[1] = calendar.getTime();
		calendar.clear();
		calendar.set(1997, 6, 16, 19, 20, 30);
		resultDates2[2] = calendar.getTime();

		for (int i = 0; i < dateStrings.length; i++) {
			try {
				Date dateResult = DataTypeUtil.toDate(ISODateStrings[i]);
				assertEquals(dateResult, resultDates2[i]);
			} catch (BirtException e) {
				e.printStackTrace();
				fail("Should not throw Exception.");
			}
		}

		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date[] resultDates3 = new Date[3];
		calendar.clear();
		calendar.set(1997, 6, 16, 0, 0, 0);
		resultDates3[0] = calendar.getTime();
		calendar.clear();
		calendar.set(1997, 6, 16, 21, 20, 0);
		resultDates3[1] = calendar.getTime();
		calendar.clear();
		calendar.set(1997, 6, 16, 16, 20, 30);
		resultDates3[2] = calendar.getTime();

		for (int i = 0; i < dateStrings.length; i++) {
			try {
				Date dateResult = DataTypeUtil.toDate(ISODateStrings[i], TimeZone.getTimeZone(timeZoneIDs[i]));
				assertEquals(dateResult, resultDates3[i]);
			} catch (BirtException e) {
				fail("Should not throw Exception.");
			}
		}
	}

	@Test
	public void testToTime() throws BirtException {
		String timeValue = "11:15:38";
		Object obj = DataTypeUtil.convert(timeValue, java.sql.Time.class);
		assertEquals(timeValue, obj.toString());
	}

	@Test
	public void testToTimeFailure() {
		String timeValue = "11:15"; // not a supported format
		boolean hasException = false;
		try {
			// expects to throw BirtException, instead of runtime exception
			DataTypeUtil.convert(timeValue, java.sql.Time.class);
		} catch (BirtException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}

	@Test
	public void testToDouble() {
		Double result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toDouble(testObject[i]);
				if (resultDouble[i] instanceof Exception)
					fail("Should throw exception for " + i + "th object");
				assertEquals(result, resultDouble[i]);
			} catch (BirtException e) {
				if (!(resultDouble[i] instanceof Exception))
					fail("Should not throw exception for " + i + "th object");
			}
		}
		for (int i = 0; i < testObjectDouble.length; i++) {
			try {
				result = DataTypeUtil.toDouble(testObjectDouble[i]);
				if (resultObjectDouble[i] instanceof Exception)
					fail("Should throw exception for " + i + "th object");
				assertEquals(result, resultObjectDouble[i]);
			} catch (BirtException e) {
				if (!(resultObjectDouble[i] instanceof Exception))
					fail("Should not throw exception for " + i + "th object");
			} catch (Exception e) {
				fail("Should throw BirtException.");
			}
		}

		// test overflow check
		try {
			assertTrue(Double.POSITIVE_INFINITY == DataTypeUtil.toDouble(Double.valueOf(Double.POSITIVE_INFINITY)));
			assertTrue(Double.NEGATIVE_INFINITY == DataTypeUtil.toDouble(Double.valueOf(Double.NEGATIVE_INFINITY)));
		} catch (BirtException e1) {
			fail("Should not throw exception ");
		}

		try {
			result = DataTypeUtil.toDouble(Float.valueOf(Float.POSITIVE_INFINITY));
			fail("Should throw exception ");
		} catch (BirtException e) {

		}

		try {
			result = DataTypeUtil.toDouble(new BigDecimal("1.7976931348623157e+309"));
			fail("Should throw exception ");
		} catch (BirtException e) {

		}

		try {
			result = DataTypeUtil.toDouble(new BigDecimal("1.7976931348623157e+309").negate());
			fail("Should throw exception ");
		} catch (BirtException e) {

		}
	}

	/*
	 * Class under test for String toString(Object)
	 */
	@Test
	public void testToStringObject() {
		String result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toString(testObject[i]);
				if (resultString[i] instanceof Exception)
					fail("Should throw Exception.");
				if (testObject[i] instanceof Double) {
					result = DataTypeUtil.toDouble(result).toString();
				} else if (testObject[i] instanceof Integer || testObject[i] instanceof BigDecimal) {
					result = DataTypeUtil.toInteger(result).toString();
				} else if (testObject[i] instanceof Date) {
					result = df.format(DataTypeUtil.toDate(result));
				}
				Object expected = resultString[i];
				assertEquals(expected, result);
			} catch (BirtException e) {
				if (!(resultString[i] instanceof Exception))
					fail("Should not throw Exception.");
			}
		}
	}

	/*
	 * Class under test for String toString(Object)
	 */
	@Test
	public void testToLocaleNeutralStringObject() {
		String result;
		for (int i = 0; i < testObject.length; i++) {
			try {
				result = DataTypeUtil.toLocaleNeutralString(testObject[i]);
				if (resultLocaleNeutralString[i] instanceof Exception)
					fail("Should throw Exception.");
				if (testObject[i] instanceof Double) {
					result = DataTypeUtil.toDouble(result).toString();
				} else if (testObject[i] instanceof Integer || testObject[i] instanceof BigDecimal) {
					result = DataTypeUtil.toInteger(result).toString();
				}

				if (i == 9)
					assertEquals(result.replaceFirst("[+-]\\d{4}", ""), resultLocaleNeutralString[i]);
				else
					assertEquals(result, resultLocaleNeutralString[i]);
			} catch (BirtException e) {
				if (!(resultLocaleNeutralString[i] instanceof Exception))
					fail("Should not throw Exception.");
			}
		}
	}

	/**
	 * Test toDateWithCheck
	 *
	 */
	@Test
	public void testToDateWithCheck() {
		Locale locale;
		String dateStr;

		dateStr = "25/11/16";
		locale = Locale.UK;
		try {
			DataTypeUtil.toDateWithCheck(dateStr, locale);
		} catch (BirtException e) {
			fail("should not throw Exception");
		}

		dateStr = "25/13/16";
		locale = Locale.UK;
		try {
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		} catch (BirtException e) {
		}

		dateStr = "2005/11/11";
		locale = Locale.UK;
		try {
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		} catch (BirtException e) {
		}

		dateStr = "2005/11/11";
		locale = Locale.CHINA;
		try {
			DataTypeUtil.toDateWithCheck(dateStr, locale);
		} catch (BirtException e) {
			fail("should not throw Exception");
		}

		dateStr = "11-11-2005";
		locale = Locale.CHINA;
		try {
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		} catch (BirtException e) {
		}

	}

	/*
	 * this test is to test toAutoValue. it's also included testing toIntegerValue
	 */
	@Test
	public void testToAutoValue() {
		Object result;
		for (int i = 0; i < autoValueInputObject.length; i++) {
			try {
				result = DataTypeUtil.toAutoValue(autoValueInputObject[i]);
				assertEquals("(" + i + ")", result, autoValueExpectedResult[i]);
			} catch (Exception e) {
				fail("Should not throw Exception.");// e.printStackTrace( );
			}
		}

	}

	/**
	 * test convertion between String and Date
	 */
	@Test
	public void testToStringAndDate() {
		// the follow objects represent the same date Jan 25th, 1998
		// the same object use toString() and toDate() several times it won't bring any
		// error
		Date date = new GregorianCalendar(1998, 1 - 1, 25).getTime();
		String str = java.text.DateFormat.getDateInstance().format(date);
		try {
			assertEquals(DataTypeUtil.toDate(str), date);

			assertEquals(DataTypeUtil.toString(DataTypeUtil.toDate(str)), DataTypeUtil.toString(date));
		} catch (BirtException e) {
			fail("Should not throw Exception. " + str);
		}
	}

	/**
	 * Test DataTypeUtil#convert( Object source, Class toTypeClass )
	 *
	 * @throws BirtException
	 *
	 */
	@Test
	public void testConvert() throws BirtException {
		java.sql.Date date = java.sql.Date.valueOf("2006-01-01");
		Object ob = DataTypeUtil.convert(date, java.sql.Date.class);
		assertEquals(date, ob);
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testConvert2() throws BirtException {
		WrappedObject obj = new WrappedObject();
		// Any type
		Object ob = DataTypeUtil.convert(obj, 0);
		assertEquals("I am an unwrapped object", ob);
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testToDate4() throws BirtException {
		try {
			DataTypeUtil.toDate("2014-09-02 12:12:12.999999");
		} catch (Exception e) {
			fail("Should not throw exception");
		}
	}

	/**
	 * Test DataTypeUtil#toApiDataType( int odaDataTypeCode )
	 *
	 * @throws BirtException
	 */
	@Test
	public void testToApiFromOdaDataType() throws BirtException {
		assertEquals(DataType.STRING_TYPE, DataTypeUtil.toApiDataType(Types.CHAR));
		assertEquals(DataType.INTEGER_TYPE, DataTypeUtil.toApiDataType(Types.INTEGER));
		assertEquals(DataType.DOUBLE_TYPE, DataTypeUtil.toApiDataType(Types.DOUBLE));
		assertEquals(DataType.DECIMAL_TYPE, DataTypeUtil.toApiDataType(Types.DECIMAL));
		assertEquals(DataType.SQL_DATE_TYPE, DataTypeUtil.toApiDataType(Types.DATE));
		assertEquals(DataType.SQL_TIME_TYPE, DataTypeUtil.toApiDataType(Types.TIME));
		assertEquals(DataType.DATE_TYPE, DataTypeUtil.toApiDataType(Types.TIMESTAMP));
		assertEquals(DataType.BLOB_TYPE, DataTypeUtil.toApiDataType(Types.BLOB));
		assertEquals(DataType.STRING_TYPE, DataTypeUtil.toApiDataType(Types.CLOB));
		assertEquals(DataType.UNKNOWN_TYPE, DataTypeUtil.toApiDataType(Types.NULL));

		// test invalid ODA data type code
		boolean hasException = false;
		try {
			DataTypeUtil.toApiDataType(Types.OTHER);
		} catch (BirtException e) {
			hasException = true;
		}
		assertTrue(hasException);

	}

	/**
	 * A wrapped scriptable object for test
	 *
	 */
	class WrappedObject extends BaseScriptable implements Wrapper {
		WrappedObject() {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.mozilla.javascript.Wrapper#unwrap()
		 */
		public Object unwrap() {
			return "I am an unwrapped object";
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		public Object get(String name, Scriptable start) {
			return "I am an unwrapped object";
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.mozilla.javascript.Scriptable#getClassName()
		 */
		public String getClassName() {
			return "WrappedObject";
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.mozilla.javascript.Scriptable#has(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		public boolean has(String name, Scriptable start) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.mozilla.javascript.Scriptable#put(java.lang.String,
		 * org.mozilla.javascript.Scriptable, java.lang.Object)
		 */
		public void put(String name, Scriptable start, Object value) {
			// do nothing
		}
	}

}
