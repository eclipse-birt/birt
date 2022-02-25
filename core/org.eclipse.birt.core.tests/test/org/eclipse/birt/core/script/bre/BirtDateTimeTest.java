/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.core.script.bre;

import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 */

public class BirtDateTimeTest extends TestCase {

	private Context cx;
	private Scriptable scope;
	private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		/*
		 * Creates and enters a Context. The Context stores information about the
		 * execution environment of a script.
		 */

		cx = Context.enter();
		/*
		 * Initialize the standard objects (Object, Function, etc.) This must be done
		 * before scripts can be executed. Returns a scope object that we use in later
		 * calls.
		 */
		scope = cx.initStandardObjects();
		scope.put(IScriptFunctionContext.FUNCTION_BEAN_NAME, scope, new IScriptFunctionContext() {

			@Override
			public Object findProperty(String name) {
				return null;
			}
		});
		new CoreJavaScriptInitializer().initialize(cx, scope);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	@After
	public void tearDown() {
		Context.exit();
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testYear() {
		/*
		 * assertTrue( NativeBirtDateTime.jsStaticFunction_year( new Date(105, 10, 15) )
		 * == 2005); assertTrue( NativeBirtDateTime.jsStaticFunction_year( new Date(0,
		 * 10, 15) ) == 1900); assertTrue( NativeBirtDateTime.jsStaticFunction_year( new
		 * Date(0, 12, 15) ) == 1901);
		 */
		String script1 = "BirtDateTime.year(new Date(23,11,11))";

		assertTrue(((Number) cx.evaluateString(scope, script1, "inline", 1, null)).intValue() == 1923);

	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testQuarter() {
		String script1 = "BirtDateTime.quarter(\"1905-10-11\")";
		String script2 = "BirtDateTime.quarter( new Date( 05,11,15))";
		String script3 = "BirtDateTime.quarter(\"1900-3-15\")";
		String script4 = "BirtDateTime.quarter( new Date( 0,11,15))";
		assertEquals(((Number) cx.evaluateString(scope, script1, "inline", 1, null)).intValue(), 4);
		assertEquals(((Number) cx.evaluateString(scope, script2, "inline", 1, null)).intValue(), 4);
		assertEquals(((Number) cx.evaluateString(scope, script3, "inline", 1, null)).intValue(), 1);
		assertEquals(((Number) cx.evaluateString(scope, script4, "inline", 1, null)).intValue(), 4);
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testMonthDate() {
		String script1 = "BirtDateTime.month(new Date(75,0,15),1)";
		String script2 = "BirtDateTime.month( new Date( 105,11,15),1)";
		String script3 = "BirtDateTime.month(\"1900-3-15\",1)";
		String script4 = "BirtDateTime.month( new Date( 10,11,15),1)";
		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), "1");
		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), "12");
		assertEquals(((String) cx.evaluateString(scope, script3, "inline", 1, null)), "3");
		assertEquals(((String) cx.evaluateString(scope, script4, "inline", 1, null)), "12");
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.week(new Date(2006, 0, 1) )", "BirtDateTime.week(new Date(2006, 0, 3) )",
				"BirtDateTime.week(new Date(2006, 0, 7) )", "BirtDateTime.week(new Date(2006, 0, 8) )",
				"BirtDateTime.week(new Date(2006, 0, 14))", "BirtDateTime.week(\"1855-1-1\")",
				"BirtDateTime.week( new Date(1780, 0, 2))", "BirtDateTime.week(new Date(1780, 0, 8))",
				"BirtDateTime.week(new Date(1780, 0, 9))", "BirtDateTime.week(new Date(1780, 1, 9))",
				"BirtDateTime.week( new Date(1780, 2, 9))", "BirtDateTime.week(new Date(1780, 3, 9, 11, 0, 0) )",
				"BirtDateTime.week(new Date(1780, 4, 9, 23, 0, 0) )" };

		int[] values = { weekOfYear(2006, 0, 1), weekOfYear(2006, 0, 3), weekOfYear(2006, 0, 7), weekOfYear(2006, 0, 8),
				weekOfYear(2006, 0, 14), weekOfYear(1855, 0, 1), weekOfYear(1780, 0, 2), weekOfYear(1780, 0, 8),
				weekOfYear(1780, 0, 9), weekOfYear(1780, 1, 9), weekOfYear(1780, 2, 9), weekOfYear(1780, 3, 9),
				weekOfYear(1780, 4, 9) };

		for (int i = 0; i < values.length; i++) {
			assertEquals("" + i, values[i],
					((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue());
		}

		// Test France locale
		ULocale oldDefault = ULocale.getDefault();
		ULocale.setDefault(ULocale.FRANCE);
		scripts = new String[] { "BirtDateTime.week(new Date(2016, 0, 1) )", "BirtDateTime.week(new Date(2016, 0, 3) )",
				"BirtDateTime.week(new Date(2016, 0, 7) )" };
		values = new int[] { 53, 53, 1 };
		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}

		// Test US Locale
		ULocale.setDefault(ULocale.US);
		values = new int[] { 1, 2, 2 };
		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
		ULocale.setDefault(oldDefault);
	}

	private int weekOfYear(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testDay() throws BirtException {
		String[] scripts = { "BirtDateTime.day(new Date(2006, 0, 1) )", "BirtDateTime.day(new Date(2006, 0, 3) )",
				"BirtDateTime.day(new Date(1980, 0, 1, 0, 0, 10))", "BirtDateTime.day(new Date(1980, 3, 9, 11, 0, 0) )",
				"BirtDateTime.day(new Date(1980, 4, 9, 23, 0, 0) )" };

		int[] values = { dayOfMonth(2006, 0, 1), dayOfMonth(2006, 0, 3), dayOfMonth(1980, 0, 1), dayOfMonth(1980, 3, 9),
				dayOfMonth(1980, 4, 9) };

		for (int i = 0; i < values.length; i++) {
			assertEquals("" + i, values[i],
					((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue());
		}
	}

	private int dayOfMonth(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day, 0, 0, 0);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testWeekDayDate() throws BirtException {
		String[] scripts = { "BirtDateTime.weekDay(new Date(2006, 0, 1), 1 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 3), 1 )", "BirtDateTime.weekDay(new Date(2006, 0, 7), 1 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 8), 1 )", "BirtDateTime.weekDay(new Date(1780, 0, 1, 0, 30))",
				"BirtDateTime.weekDay(new Date(1780, 0, 2, 0, 30))",
				"BirtDateTime.weekDay(new Date(1780, 0, 8, 0, 30))",
				"BirtDateTime.weekDay(new Date(1780, 0, 9, 0, 30))",

				"BirtDateTime.weekDay(new Date(2006, 0, 1), 2 )", "BirtDateTime.weekDay(new Date(2006, 0, 3), 2 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 7), 2 )", "BirtDateTime.weekDay(new Date(2006, 0, 8), 2 )",
				"BirtDateTime.weekDay(new Date(1780, 0, 1, 0, 30), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 2, 0, 30), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 8, 0, 30), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 9, 0, 30), 2)",

				"BirtDateTime.weekDay(new Date(2006, 0, 1), 3 )", "BirtDateTime.weekDay(new Date(2006, 0, 3), 3 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 7), 3 )", "BirtDateTime.weekDay(new Date(2006, 0, 8), 3 )",
				"BirtDateTime.weekDay(new Date(1780, 0, 1, 0, 30), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 2, 0, 30), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 8, 0, 30), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 9, 0, 30), 3)" };

		String[] values = { "1", "3", "7", "1", "7", "1", "7", "1", "7", "2", "6", "7", "6", "7", "6", "7", "6", "1",
				"5", "6", "5", "6", "5", "6" };

		for (int i = 0; i < values.length; i++) {
			assertEquals("" + i, values[i], cx.evaluateString(scope, scripts[i], "inline", 1, null));
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()
	 * '
	 */
	@Test
	public void testToday() {
		Calendar c = Calendar.getInstance();
		c.clear();
		Date d = (Date) cx.evaluateString(scope, "BirtDateTime.today()", "inline", 1, null);
		c.setTime(d);
		System.out.println("year:" + c.get(Calendar.YEAR) + " month:" + c.get(Calendar.MONTH) + " day:"
				+ c.get(Calendar.DATE) + " hour:" + c.get(Calendar.HOUR));
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_now()'
	 */
	@Test
	public void testNow() {
		Calendar c = Calendar.getInstance();
		c.clear();
		Date d = (Date) cx.evaluateString(scope, "BirtDateTime.now()", "inline", 1, null);
		c.setTime(d);
		System.out.println("year:" + c.get(Calendar.YEAR) + " month:" + c.get(Calendar.MONTH) + " day:"
				+ c.get(Calendar.DATE) + " hour:" + c.get(Calendar.HOUR) + " minute:" + c.get(Calendar.MINUTE)
				+ "second:" + c.get(Calendar.SECOND));
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffYear(Date, Date)'
	 */
	@Test
	public void testDiffYear() throws BirtException {
		String[] scripts = { "BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 0, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffYear(new Date(2007, 10, 15),new Date(2005, 0, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(1793, 0, 15) )" };

		int[] values = { 2, 2, 3, -2, -212 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffMonth(Date, Date)'
	 */
	@Test
	public void testDiffMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 0, 8) )",
				"BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffMonth(new Date(2007, 10, 15),new Date(2005, 0, 1) )",
				"BirtDateTime.diffMonth(new Date(1910, 10, 15),new Date(1890, 0, 15) )" };

		int[] values = { 14, 25, 26, -34, -250 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffQuarter(Date, Date)'
	 */
	@Test
	public void testDiffQuarter() throws BirtException {
		String[] scripts = { "BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 0, 8) )",
				"BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffQuarter(new Date(2007, 10, 15),new Date(2005, 0, 1) )",
				"BirtDateTime.diffQuarter(new Date(1910, 10, 15),new Date(1890, 0, 15) )" };

		int[] values = { 5, 8, 9, -11, -83 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffWeek(Date, Date)'
	 */
	@Test
	public void testDiffWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.diffWeek(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 7) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 8) )",
				"BirtDateTime.diffWeek(new Date(1779, 11, 31),new Date(1780, 0, 1) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1, 0, 30),new Date(1780, 0, 2, 0, 30) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1, 0, 30),new Date(1780, 0, 8, 0, 30) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1, 0, 30),new Date(1780, 0, 9, 0, 30) )" };

		int[] values = { -1, -1, 0, 0, 1, 0, 1, 1, 2 };

		for (int i = 0; i < values.length; i++) {
			assertEquals("" + i, values[i],
					((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue());
		}
		/*
		 * assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 0, 0, 8
		 * ), new Date( 0, 0, 6 ) ) == -1 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ), new
		 * Date( 105, 11, 31 ) ) == -1 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ), new
		 * Date( 106, 0, 3 ) ) == 0 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ), new
		 * Date( 106, 0, 7 ) ) == 0 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ), new
		 * Date( 106, 0, 8 ) ) == 1 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -121, 11, 31 ), new
		 * Date( -120, 0, 1 ) ) == 0 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ), new
		 * Date( -120, 0, 2 ) ) == 1 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ), new
		 * Date( -120, 0, 8 ) ) == 1 ); assertTrue(
		 * NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ), new
		 * Date( -120, 0, 9 ) ) == 2 );
		 */
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffDay
	 * (Date, Date) throws BirtException'
	 */
	@Test
	public void testDiffDay() throws BirtException {
		String[] scripts = { "BirtDateTime.diffDay(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 2, 7) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 2, 8) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2007, 2, 8) )", };

		int[] values = { -2, -1, 2, 37, 65, 66, 431 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffHour(Date, Date)'
	 */
	@Test
	public void testDiffHour() throws BirtException {
		String[] scripts = { "BirtDateTime.diffHour(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 2, 7,11,2,0) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 2, 8,22,0,0) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2007, 2, 8) )" };

		int[] values = { -2 * 24, -1 * 24, 2 * 24, 37 * 24, 65 * 24 + 11, 66 * 24 + 22, (66 + 365) * 24 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffMinute(Date, Date)'
	 */
	@Test
	public void testDiffMinute() throws BirtException {
		String[] scripts = { "BirtDateTime.diffMinute(\"1930-1-8 12:1:1\",\"1930-1-8 12:2:58\" )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 2, 7,11,2,0) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 2, 8,22,3,0) )",
				"BirtDateTime.diffMinute(\"1993-1-1\",new Date(1994, 2, 8) )" };

		int[] values = { 1, -1 * 24 * 60, 2 * 24 * 60, 37 * 24 * 60, (65 * 24 + 11) * 60 + 2, (66 * 24 + 22) * 60 + 3,
				((66 + 365) * 24) * 60 };

		for (int i = 0; i < values.length; i++) {
			System.out.println(i);
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_diffSecond(Date, Date)'
	 */
	@Test
	public void testDiffSecond() throws BirtException {
		String[] scripts = { "BirtDateTime.diffSecond(new Date(1900, 0, 8,12,1,1),new Date(1900, 0, 8,12,2,58) )",
				"BirtDateTime.diffSecond(new Date(1900, 0, 8,12,1,58),new Date(1900, 0, 8,12,2,1) )",
				"BirtDateTime.diffSecond(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffSecond(new Date(2006, 0, 1),new Date(2006, 2, 8,22,3,0) )",
				"BirtDateTime.diffSecond(\"1993-1-1\",new Date(1994, 2, 8) )" };

		int[] values = { 60 + 57, 3, 2 * 24 * 60 * 60, ((66 * 24 + 22) * 60 + 3) * 60, (66 + 365) * 24 * 60 * 60 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).intValue(), values[i]);
		}

	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_addMonth(Date, int)'
	 */
	@Test
	public void testAddYear() throws BirtException {
		String[] scripts = { "BirtDateTime.addYear(new Date(2005, 10, 15),10 )",
				"BirtDateTime.addYear(new Date(1795, 10, 15),10 )", "BirtDateTime.addYear(\"1910-11-15\",10 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2015, 10, 15);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1805, 10, 15, 0, 0, 0);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1920, 10, 15);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 1; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_addMonth(Date, int)'
	 */
	@Test
	public void testAddMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.addMonth(new Date(2005, 10, 15),10 )",
				"BirtDateTime.addMonth(\"1995-1-15\",10 )", "BirtDateTime.addMonth(\"1940-2-15\",11 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2006, 8, 15);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 15);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1941, 0, 15);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_addQuarter(Date, int)'
	 */
	@Test
	public void testAddQuarter() throws BirtException {
		String[] scripts = { "BirtDateTime.addQuarter(new Date(2005, 10, 15),2 )",
				"BirtDateTime.addQuarter(\"1995-1-15\",9 )", "BirtDateTime.addQuarter(\"1930-6-15\",11 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2006, 4, 15);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1997, 3, 15);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1933, 2, 15);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addWeek
	 * (Date, int)'
	 */
	@Test
	public void testAddWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.addWeek(new Date(2005, 10, 15),1 )",
				"BirtDateTime.addWeek(new Date(2006, 9, 15),3 )", "BirtDateTime.addWeek(\"1995-11-15\",2 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 10, 22);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 10, 5);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 29);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addDay(
	 * Date, int)'
	 */
	@Test
	public void testAddDay() throws BirtException {
		String[] scripts = { "BirtDateTime.addDay(new Date(2005, 10, 15),7 )",
				"BirtDateTime.addDay(new Date(2006, 9, 15),21 )", "BirtDateTime.addDay(\"1995-11-15\",10 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 10, 22);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 10, 5);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 25);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addDay(
	 * Date, int)'
	 */
	@Test
	public void testAddHour() throws BirtException {
		String[] scripts = { "BirtDateTime.addHour(new Date(2005, 10, 15),7*24 )",
				"BirtDateTime.addHour(new Date(2006, 9, 15),21*24 )", "BirtDateTime.addHour(\"1995-11-15\",10 )",
				"BirtDateTime.addHour(null,21*24 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 10, 15);
		c.add(Calendar.HOUR, 7 * 24);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 9, 15);
		c.add(Calendar.HOUR, 21 * 24);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 15);
		c.add(Calendar.HOUR, 10);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		c.add(Calendar.HOUR, 21 * 24);

		c.clear();

		Date[] values = { d1, d2, d3, null };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_addMinute(Date, int)'
	 */
	@Test
	public void testAddMinute() throws BirtException {
		String[] scripts = { "BirtDateTime.addMinute(new Date(2005, 10, 15),7*24*60 )",
				"BirtDateTime.addMinute(new Date(2006, 9, 15),21*24*60 )",
				"BirtDateTime.addMinute(\"1995-11-15\",10*60+10 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 10, 15);
		c.add(Calendar.MINUTE, 7 * 24 * 60);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 9, 15);
		c.add(Calendar.MINUTE, 21 * 24 * 60);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 15);
		c.add(Calendar.MINUTE, 10 * 60 + 10);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_addSecond(Date, int)'
	 */
	@Test
	public void testAddSecond() throws BirtException {
		String[] scripts = { "BirtDateTime.addSecond(new Date(2005, 10, 15),7*24*60*60 )",
				"BirtDateTime.addSecond(new Date(2006, 9, 15),21*24*60*60 )",
				"BirtDateTime.addSecond(\"1995-11-15\",(10*60+10)*60+9 )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 10, 15);
		c.add(Calendar.SECOND, 7 * 24 * 60 * 60);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 9, 15);
		c.add(Calendar.SECOND, 21 * 24 * 60 * 60);

		Date d2 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(1995, 10, 15);
		c.add(Calendar.SECOND, (10 * 60 + 10) * 60 + 9);

		Date d3 = new Date(c.getTimeInMillis());

		c.clear();

		Date[] values = { d1, d2, d3 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_firstDayOfYear(Date)'
	 */
	@Test
	public void testFirstDayOfYear() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfYear(new Date(2005, 10, 15) )",
				"BirtDateTime.firstDayOfYear(new Date(2006, 9, 15) )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 0, 1);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 0, 1);

		Date d2 = new Date(c.getTimeInMillis());

		Date[] values = { d1, d2 };

		for (int i = 0; i < values.length; i++) {
			System.out.println(cx.evaluateString(scope, scripts[i], "inline", 1, null));
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_firstDayOfQuarter(Date)'
	 */
	@Test
	public void testFirstDayOfQuarter() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfQuarter(new Date(2005, 9, 15) )",
				"BirtDateTime.firstDayOfQuarter(new Date(2006, 8, 15) )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 9, 1);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 6, 1);

		Date d2 = new Date(c.getTimeInMillis());

		Date[] values = { d1, d2 };

		for (int i = 0; i < values.length; i++) {
			System.out.println(cx.evaluateString(scope, scripts[i], "inline", 1, null));
			System.out.println("result:" + values[i]);
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_firstDayOfMonth(Date)'
	 */
	@Test
	public void testFirstDayOfMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfMonth(new Date(2005, 9, 15) )",
				"BirtDateTime.firstDayOfMonth(new Date(2006, 8, 15) )" };

		Calendar c = Calendar.getInstance();

		c.clear();

		c.set(2005, 9, 1);

		Date d1 = new Date(c.getTimeInMillis());

		c.clear();

		c.set(2006, 8, 1);

		Date d2 = new Date(c.getTimeInMillis());

		Date[] values = { d1, d2 };

		for (int i = 0; i < values.length; i++) {
			System.out.println(cx.evaluateString(scope, scripts[i], "inline", 1, null));
			System.out.println("result:" + values[i]);
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_firstDayOfWeek(Date)'
	 */
	@Test
	public void testFirstDayOfWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfWeek(new Date(2011, 7, 25) )",
				"BirtDateTime.firstDayOfWeek(new Date(2011, 7, 15) )" };

		// com.ibm.icu.util.Calendar c = com.ibm.icu.util.Calendar.getInstance(
		// TimeZone.getDefault( ), ULocale.getDefault() );
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getDefault());

		c.clear();
		c.setMinimalDaysInFirstWeek(1);
		c.set(2011, 7, 25);
		c.get(Calendar.DAY_OF_WEEK);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

		Date d1 = c.getTime();

		c.clear();
		c.setMinimalDaysInFirstWeek(1);
		c.set(2011, 7, 15);
		c.get(Calendar.DAY_OF_WEEK);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		Date d2 = c.getTime();

		Date[] values = { d1, d2 };

		for (int i = 0; i < values.length; i++) {
			System.out.println(cx.evaluateString(scope, scripts[i], "inline", 1, null));
			System.out.println("result:" + values[i]);
			assertEquals(cx.evaluateString(scope, scripts[i], "inline", 1, null), values[i]);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_dayOfWeek(Date)'
	 */
	@Test
	public void testDayOfWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.dayOfWeek(new Date(2015, 4, 1) )",
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 2) )", "BirtDateTime.dayOfWeek(new Date(2015, 4, 3) )",
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 4) )", };

		int[] values = { 6, 7, 1, 2 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_dayOfYeark(Date)'
	 */
	@Test
	public void testDayOfYear() throws BirtException {
		String[] scripts = { "BirtDateTime.dayOfYear(new Date(2012, 0, 1) )",
				"BirtDateTime.dayOfYear(new Date(2012, 1, 29) )", "BirtDateTime.dayOfYear(new Date(2012, 11, 31) )",
				"BirtDateTime.dayOfYear(new Date(2015, 0, 1) )", "BirtDateTime.dayOfYear(new Date(2015, 1, 28) )",
				"BirtDateTime.dayOfYear(new Date(2015, 11, 31) )" };

		int[] values = { 1, 60, 366, 1, 59, 365 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.
	 * jsStaticFunction_weekOfMonth(Date)'
	 */
	@Test
	public void testWeekOfMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.weekOfMonth(new Date(2015, 4, 1) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 2) )", "BirtDateTime.weekOfMonth(new Date(2015, 4, 3) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 9) )", "BirtDateTime.weekOfMonth(new Date(2015, 4, 10) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 16) )", "BirtDateTime.weekOfMonth(new Date(2015, 4, 17) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 23) )", "BirtDateTime.weekOfMonth(new Date(2015, 4, 24) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 30) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 31) )", };

		int[] values = { weekOfMonth(2015, 4, 1), weekOfMonth(2015, 4, 2), weekOfMonth(2015, 4, 3),
				weekOfMonth(2015, 4, 9), weekOfMonth(2015, 4, 10), weekOfMonth(2015, 4, 16), weekOfMonth(2015, 4, 17),
				weekOfMonth(2015, 4, 23), weekOfMonth(2015, 4, 24), weekOfMonth(2015, 4, 30),
				weekOfMonth(2015, 4, 31) };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	private int weekOfMonth(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalYear</code>
	 */
	@Test
	public void testFiscalYear() throws BirtException {
		String[] scripts = { "BirtDateTime.fiscalYear(new Date(2015, 6, 1), new Date(2012, 6, 1 ))", // leap
																										// year
				"BirtDateTime.fiscalYear(new Date(2015, 6, 1), new Date(2015, 6, 1 ))", // non leap year
				"BirtDateTime.fiscalYear(new Date(2015, 6, 12), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalYear(new Date(2015, 7, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalYear(new Date(2015, 7, 12), new Date(2015, 0, 1 ))", // calendar year
				"BirtDateTime.fiscalYear(new Date(2014, 5, 15))", "BirtDateTime.fiscalYear(new Date(2015, 6, 12))", };

		int[] values = { 2016, 2016, 2015, 2016, 2015, 2014, 2016 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalQuarter</code>
	 */
	@Test
	public void testFiscalQuarter() throws BirtException {
		String[] scripts = { "BirtDateTime.fiscalQuarter(new Date(2015, 8, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 0, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 5, 15))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 6, 12))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 0, 11))", };

		int[] values = { 1, 4, 2, 4, 1, 3 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalMonth</code>
	 */
	@Test
	public void testFiscalMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.fiscalMonth(new Date(2015, 8, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 0, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 5, 15))", "BirtDateTime.fiscalMonth(new Date(2015, 6, 12))",
				"BirtDateTime.fiscalMonth(new Date(2015, 0, 11))", };

		int[] values = { 3, 12, 4, 12, 1, 7 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalWeek</code>
	 */
	@Test
	public void testFiscalWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.fiscalWeek(new Date(2011, 6, 2), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2011, 6, 3), new Date(2015, 6, 1 ))", // week start with 7/3
				"BirtDateTime.fiscalWeek(new Date(2011, 5, 30), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2011, 5, 25), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2011, 11, 31), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2012, 0, 1), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2015, 0, 7))", // default start week is 7/1
				"BirtDateTime.fiscalWeek(new Date(2015, 1, 1))", "BirtDateTime.fiscalWeek(new Date(2015, 6, 1))", };

		int[] values = { fiscalWeek(2011, 6, 2, 2015, 6, 1), fiscalWeek(2011, 6, 3, 2015, 6, 1),
				fiscalWeek(2011, 5, 30, 2015, 6, 1), fiscalWeek(2011, 5, 25, 2015, 6, 1),
				fiscalWeek(2011, 11, 31, 2015, 6, 1), fiscalWeek(2012, 0, 1, 2015, 6, 1),
				fiscalWeek(2015, 0, 7, 2015, 6, 1), fiscalWeek(2015, 1, 1, 2015, 6, 1),
				fiscalWeek(2015, 6, 1, 2015, 6, 1) };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	private int fiscalWeek(int year, int month, int day, int year2, int month2, int day2) {
		int weekOfYear = weekOfYear(year, month, day);
		int fiscalYearStartWeek = weekOfYear(year, month2, day2);
		if (weekOfYear >= fiscalYearStartWeek) {
			return weekOfYear - fiscalYearStartWeek + 1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year - 1, 11, 31);
		while (calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		return calendar.get(Calendar.WEEK_OF_YEAR) - weekOfYear(year - 1, month2, day2) + weekOfYear + 1;
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalDay</code>
	 */
	@Test
	public void testFiscalDay() throws BirtException {
		String[] scripts = { "BirtDateTime.fiscalDay(new Date(2015, 6, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 9, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 0, 7))", "BirtDateTime.fiscalDay(new Date(2015, 1, 1))",
				"BirtDateTime.fiscalDay(new Date(2015, 6, 1))", };

		int[] values = { 15, 346, 11, 191, 216, 1 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalYear</code>
	 */
	@Test
	public void testFirstDayOfFiscalYear() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfFiscalYear(2015)",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalYear(2015, new Date(2014, 6, 1))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 6, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15), new Date(2014, 6, 1 ))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15), new Date(2015, 6, 1 ))", };

		Calendar c = Calendar.getInstance();
		c.clear();
		Date[] values = { date(c, 2014, 6, 1), date(c, 2014, 6, 1), date(c, 2014, 6, 1), date(c, 2015, 6, 1),
				date(c, 2014, 6, 1), date(c, 2014, 6, 1), };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalMonth</code>
	 */
	@Test
	public void testFirstDayOfFiscalMonth() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalMonth(4.0)",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalMonth(2, new Date(2014, 6, 15))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 6, 15), new Date(2015, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 6, 9), new Date(2014, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2014, 0, 1), new Date(2015, 0, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2014, 1, 1), new Date(2015, 0, 31 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2014, 9, 10), new Date(2015, 6, 31 ))", };

		Calendar c = Calendar.getInstance();
		c.clear();
		Date[] values = { date(c, 2015, 5, 1), date(c, CURRENT_YEAR, 9, 1), date(c, 2015, 5, 1), date(c, 2014, 7, 15),
				date(c, 2015, 6, 10), date(c, 2015, 5, 10), date(c, 2013, 11, 10), date(c, 2014, 0, 31),
				date(c, 2014, 8, 30), };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalQuarter</code>
	 */
	@Test
	public void testFirstDayOfFiscalQuarter() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalQuarter(2)",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalQuarter(3, new Date(2014, 6, 15))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 6, 15), new Date(2015, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 6, 9), new Date(2014, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2014, 0, 1), new Date(2015, 1, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2014, 9, 10), new Date(2015, 6, 31 ))", };

		Calendar c = Calendar.getInstance();
		c.clear();
		Date[] values = { date(c, 2015, 3, 1), date(c, CURRENT_YEAR, 9, 1), date(c, 2015, 3, 1), date(c, 2015, 0, 15),
				date(c, 2015, 6, 10), date(c, 2015, 3, 10), date(c, 2013, 10, 10), date(c, 2014, 8, 30), };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalWeek</code>
	 */
	@Test
	public void testFirstDayOfFiscalWeek() throws BirtException {
		String[] scripts = { "BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalWeek(6, new Date(2015, 6, 15))",
				"BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15), new Date(2015, 6, 10 ))", };

		Calendar c = Calendar.getInstance();
		c.clear();
		Date[] values = { firstDayOfWeek(c, 2015, 5, 15), firstDayOfWeek(c, 2015, 5, 15),
				firstDayOfWeek(c, 2015, 7, 17), firstDayOfWeek(c, 2015, 5, 15), };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
	}

	private Date date(Calendar c, int year, int month, int day) {
		c.set(year, month, day);
		return c.getTime();
	}

	@SuppressWarnings("deprecation")
	private Date firstDayOfWeek(Calendar c, int year, int month, int day) {
		Date date = new Date(year - 1900, month, day);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}

	@Test
	public void testFiscalYearStartDate() {
		String PROPERTY_FISCAL_YEAR_START_DATE = "FISCAL_YEAR_START_DATE";
		System.setProperty(PROPERTY_FISCAL_YEAR_START_DATE, "2000-10-01");
		String[] scripts = { "BirtDateTime.fiscalYear(new Date(2015, 6, 15))",
				"BirtDateTime.fiscalYear(new Date(2015, 8, 12))", "BirtDateTime.fiscalYear(new Date(2015, 9, 1))",
				"BirtDateTime.fiscalYear(new Date(2015, 10, 12))", };

		int[] values = { 2015, 2015, 2016, 2016 };

		for (int i = 0; i < values.length; i++) {
			Object result = cx.evaluateString(scope, scripts[i], "inline", 1, null);
			assertEquals(String.valueOf(i), values[i], result);
		}
		System.clearProperty(PROPERTY_FISCAL_YEAR_START_DATE);
	}

}
