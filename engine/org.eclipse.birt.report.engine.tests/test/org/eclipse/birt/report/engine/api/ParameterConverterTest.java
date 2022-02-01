/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

/**
 *
 */

public class ParameterConverterTest extends TestCase {

	/**
	 * test for formating a string
	 */
	public void testStringFormat() {
		Locale locale = Locale.US;
		ReportParameterConverter rpc;

		rpc = new ReportParameterConverter("@@@@", locale);
		assertEquals("1234fggggg", rpc.format("1234fggggg"));
		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("1234fggggg", rpc.format("1234fggggg"));

		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("123 ", rpc.format("123"));
		rpc = new ReportParameterConverter("@@@@!!!", locale);
		assertEquals("123 ", rpc.format("123"));
		rpc = new ReportParameterConverter("@@@@", locale);
		assertEquals("123456", rpc.format("123456"));
		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("123456", rpc.format("123456"));
		rpc = new ReportParameterConverter("(@@)@@@", locale);
		assertEquals("(  )123", rpc.format("123"));
		rpc = new ReportParameterConverter("&&&&!", locale);
		assertEquals("123", rpc.format("123"));
		rpc = new ReportParameterConverter("&&&&&&", locale);
		assertEquals("123", rpc.format("123"));
		rpc = new ReportParameterConverter("@@@&!", locale);
		assertEquals("123", rpc.format("123"));
		rpc = new ReportParameterConverter("&@@@&!", locale);
		assertEquals("123 ", rpc.format("123"));
		rpc = new ReportParameterConverter("@@@&", locale);
		assertEquals(" 123", rpc.format("123"));

		rpc = new ReportParameterConverter("@@@@@aaa!", locale);
		assertEquals("123  aaa", rpc.format("123"));
		rpc = new ReportParameterConverter("@@aaa!", locale);
		assertEquals("12aaa3", rpc.format("123"));
		rpc = new ReportParameterConverter("@@aaa&&&!", locale);
		assertEquals("12aaa3", rpc.format("123"));
		rpc = new ReportParameterConverter("@@@@@aaa>!", locale);
		assertEquals("123  aaa", rpc.format("123"));

		rpc = new ReportParameterConverter("@@@@<!", locale);
		assertEquals("1234fggggg", rpc.format("1234fggggg"));
		rpc = new ReportParameterConverter("@@@@<!", locale);
		assertEquals("1234fggggg", rpc.format("1234fggGgG"));
		rpc = new ReportParameterConverter("@@@@>!", locale);
		assertEquals("1234FGGGGG", rpc.format("1234fggggg"));
		rpc = new ReportParameterConverter("@@@@&&&>!", locale);
		assertEquals("1234FGGGGG", rpc.format("1234fggggg"));

		rpc = new ReportParameterConverter("@@@@&&@@@<<<>>>@@@&>", locale);
		assertEquals("   1234FGGGGG", rpc.format("1234fggggg"));

		rpc = new ReportParameterConverter("!", locale);
		assertEquals("123", rpc.format("123"));
		rpc = new ReportParameterConverter("", locale);
		assertEquals("123", rpc.format("123"));
		rpc = new ReportParameterConverter(">", locale);
		assertEquals("123AAA", rpc.format("123aaA"));

		rpc = new ReportParameterConverter("***\"!", locale);
		assertEquals("***\"123", rpc.format("123"));
		rpc = new ReportParameterConverter("***&YY&&&!", locale);
		assertEquals("***1YY23", rpc.format("123"));
		rpc = new ReportParameterConverter("***&YY@@@!", locale);
		assertEquals("***1YY23 ", rpc.format("123"));

		// test for SSN
		rpc = new ReportParameterConverter("@@@-@@-@@@@!", locale);
		assertEquals("600-00-03274", rpc.format("6000003274"));
		// test for zipcode+4
		rpc = new ReportParameterConverter("@@@@@-@@@@!", locale);
		assertEquals("94305-0110", rpc.format("943050110"));
		rpc = new ReportParameterConverter("@@@@@-@@@@", locale);
		assertEquals("94305-0110", rpc.format("943050110"));
		// test for zipcode
		rpc = new ReportParameterConverter("@@@@@!", locale);
		assertEquals("94305", rpc.format("94305"));
		rpc = new ReportParameterConverter("@@@@@", locale);
		assertEquals("94305", rpc.format("94305"));
		// test for phonenumber
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@!", locale);
		assertEquals("(650)-837-2345,", rpc.format("6508372345,"));
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@", locale);
		assertEquals("(650)-837-2345", rpc.format("6508372345"));
	}

	/**
	 * test for formating a date
	 */
	public void testDateFormat() {
		ReportParameterConverter rpc;
		Calendar dateCal;
		Date date;
		Locale locale = Locale.ITALY;

		dateCal = Calendar.getInstance();
		dateCal.set(1998, 8, 13, 20, 1, 44);
		date = dateCal.getTime();
		rpc = new ReportParameterConverter("MM/dd/yy KK:mm aa", locale);
		assertEquals("09/13/98 08:01 PM", rpc.format(date));
		rpc = new ReportParameterConverter("Long Date", locale);
		assertEquals("13 settembre 1998", rpc.format(date));

		locale = Locale.US;
		dateCal = Calendar.getInstance();
		dateCal.set(1998, 8, 13, 20, 1, 44);
		date = dateCal.getTime();
		rpc = new ReportParameterConverter("MM/dd/yyyy hh:mm:ss a", locale);
		assertEquals("09/13/1998 08:01:44 PM", rpc.format(date));

		// test the instance of locale
		dateCal = Calendar.getInstance();
		dateCal.set(1998, 8, 13, 20, 1, 44);
		date = dateCal.getTime();

		rpc = new ReportParameterConverter("Long Date", locale);
		assertEquals("September 13, 1998", rpc.format(date));
		rpc = new ReportParameterConverter("D", locale);
		assertEquals("September 13, 1998", rpc.format(date));
		rpc = new ReportParameterConverter("Medium Date", locale);
		assertEquals("Sep 13, 1998", rpc.format(date));
		rpc = new ReportParameterConverter("Short Date", locale);
		assertEquals("9/13/98", rpc.format(date));
		rpc = new ReportParameterConverter("d", locale);
		assertEquals("9/13/98", rpc.format(date));
		rpc = new ReportParameterConverter("Long Time", locale);
		assertEquals(true, rpc.format(date).startsWith("8:01:44 PM"));
		rpc = new ReportParameterConverter("T", locale);
		assertEquals(true, rpc.format(date).startsWith("8:01:44 PM"));

		/*
		 * icu and java are not synced. SimpleDateFormat javaSample = (SimpleDateFormat)
		 * java.text.DateFormat .getDateTimeInstance( java.text.DateFormat.LONG,
		 * java.text.DateFormat.SHORT, locale.toLocale( ) ); rpc = new
		 * ReportParameterConverter( "f", locale ); assertEquals( javaSample.format(
		 * date ), rpc.format( date ) );
		 */

		rpc = new ReportParameterConverter("General Date", locale);
		assertEquals(rpc.format(date), true, rpc.format(date).startsWith("September 13, 1998 at 8:01:44 PM"));
		rpc = new ReportParameterConverter("Short Time", locale);
		assertEquals("20:01", rpc.format(date));
		rpc = new ReportParameterConverter("Medium Time", locale);
		assertEquals("8:01:44 PM", rpc.format(date));
	}

	/**
	 * test for formating a number or other obj
	 */
	public void testOtherFormat() {

		Locale defaultLocale = Locale.getDefault();
		try {

			Locale locale = Locale.US;
			Locale.setDefault(locale);
			ReportParameterConverter rpc;

			NumberFormat number = NumberFormat.getInstance(locale);
			number.setGroupingUsed(false);
			rpc = new ReportParameterConverter(null, locale);
			assertEquals(number.format(1002.234), rpc.format(1002.234));

			// test format with different style.
			rpc = new ReportParameterConverter("#", locale);
			assertEquals("1002", rpc.format(1002.2));
			rpc = new ReportParameterConverter("0", locale);
			assertEquals("1002", rpc.format(1002.2));
			rpc = new ReportParameterConverter("###,##0", locale);
			assertEquals("1,002", rpc.format(1002.2));
			rpc = new ReportParameterConverter("#.0#", locale);
			assertEquals("1002.2", rpc.format(1002.2));
			rpc = new ReportParameterConverter("###,##0.00 'm/s'", locale);
			assertEquals("1,002.20 m/s", rpc.format(1002.2));
			rpc = new ReportParameterConverter("#.##", locale);
			assertEquals("1002", rpc.format(1002L));

			// test format with ; deliminated.
			rpc = new ReportParameterConverter("###.#';'", locale);
			assertEquals("1002.2;", rpc.format(1002.2));
			assertEquals("-1002.2;", rpc.format(-1002.2));

			rpc = new ReportParameterConverter("###.#\';';#", locale);
			assertEquals("1002.2;", rpc.format(1002.2));
			assertEquals("1002.2", rpc.format(-1002.2));
			rpc = new ReportParameterConverter("#.00%", locale);
			assertEquals("1002200.00%", rpc.format(10022L));

			// test format with user-defined pattern
			rpc = new ReportParameterConverter("General Number", locale);
			assertEquals(number.format(1002.20), rpc.format(1002.20));
			assertEquals(number.format(-1002.2), rpc.format(-1002.2));
			assertEquals(number.format(0.004), rpc.format(0.004));
			assertEquals(number.format(0.004123456), rpc.format(0.004123456));
			assertEquals(number.format(-0.004), rpc.format(-0.004));
			assertEquals(number.format(0), rpc.format(0L));

			number = NumberFormat.getCurrencyInstance(Locale.getDefault());
			rpc = new ReportParameterConverter("C", locale);
			assertEquals(number.format(1290.8889), number.format(1290.8889), rpc.format(1290.8889));

			rpc = new ReportParameterConverter("Fixed", locale);
			assertEquals("1002.20", rpc.format(1002.2));
			assertEquals("-1002.20", rpc.format(-1002.2));
			assertEquals("0.00", rpc.format(0.004));
			assertEquals("3333333333.33", rpc.format(3333333333.33));
			assertEquals("0.00", rpc.format(0));

			rpc = new ReportParameterConverter("Standard", locale);
			assertEquals("1,002.20", rpc.format(1002.2));
			assertEquals("-1,002.20", rpc.format(-1002.2));
			assertEquals("0.00", rpc.format(0.004));
			assertEquals("0.00", rpc.format(0.004123456));
			assertEquals("-0.00", rpc.format(-0.004));
			assertEquals("3,333,333,333.33", rpc.format(3333333333.33));
			assertEquals("0.00", rpc.format(0L));

			rpc = new ReportParameterConverter("Percent", locale);
			assertEquals("100220.00%", rpc.format(1002.2));
			assertEquals("-100220.00%", rpc.format(-1002.2));
			assertEquals("0.40%", rpc.format(0.004));
			assertEquals("0.41%", rpc.format(0.004123456));
			assertEquals("-0.40%", rpc.format(-0.004));
			assertEquals("333333333333.00%", rpc.format(3333333333.33));
			assertEquals("0.00%", rpc.format(0));

			rpc = new ReportParameterConverter("P", locale);
			assertEquals("100,220.00 %", rpc.format(1002.2));
			assertEquals("-100,220.00 %", rpc.format(-1002.2));
			assertEquals("0.40 %", rpc.format(0.004));
			assertEquals("0.41 %", rpc.format(0.004123456));
			assertEquals("-0.40 %", rpc.format(-0.004));
			assertEquals("333,333,333,333.00 %", rpc.format(3333333333.33));
			assertEquals("0.00 %", rpc.format(0));

			rpc = new ReportParameterConverter("Scientific", locale);
			assertEquals("1.00E03", rpc.format(1002.2));
			assertEquals("-1.00E03", rpc.format(-1002.2));
			assertEquals("4.00E-03", rpc.format(0.004));
			assertEquals("4.12E-03", rpc.format(0.004123456));
			assertEquals("-4.00E-03", rpc.format(-0.004));
			assertEquals("3.33E09", rpc.format(3333333333.33));
			assertEquals("0.00E00", rpc.format(0L));
			assertEquals("1.00E00", rpc.format(1L));

			rpc = new ReportParameterConverter("e", locale);
			assertEquals("1.002200E03", rpc.format(1002.2));
			assertEquals("-1.002200E03", rpc.format(-1002.2));
			assertEquals("4.000000E-03", rpc.format(0.004));
			assertEquals("4.123456E-03", rpc.format(0.004123456));
			assertEquals("-4.000000E-03", rpc.format(-0.004));
			assertEquals("3.333333E09", rpc.format(3333333333.33));
			assertEquals("0.000000E00", rpc.format(0L));
			assertEquals("1.000000E00", rpc.format(1L));

			rpc = new ReportParameterConverter("x", locale);
			assertEquals("3ea", rpc.format(1002L));
			assertEquals("fffffffffffffc16", rpc.format(-1002L));
			assertEquals("3ea", rpc.format(1002.22));

			rpc = new ReportParameterConverter("d", locale);
			assertEquals("1,002", rpc.format(1002L));
			assertEquals("-1,002", rpc.format(-1002L));
			assertEquals("1,002.009", rpc.format(1002.009));

			rpc = new ReportParameterConverter("$###,##0.00;'Negative'", locale);
			assertEquals("$2,139.30", rpc.format(new BigDecimal(2139.3)));
			assertEquals("$2.14", rpc.format(new BigDecimal(2.139)));
			assertEquals("Negative2.13", rpc.format(new BigDecimal(-2.13)));
			assertEquals("$0.00", rpc.format(new BigDecimal(0.0)));
			assertEquals("$2,000.00", rpc.format(new BigDecimal(2000)));
			assertEquals("$20.00", rpc.format(new BigDecimal(20)));
			assertEquals("Negative2,000.00", rpc.format(new BigDecimal(-2000)));
			assertEquals("$0.00", rpc.format(new BigDecimal(0)));

			rpc = new ReportParameterConverter("d", locale);
			assertEquals("2.139", rpc.format(new BigDecimal(2.139)));

			rpc = new ReportParameterConverter(null, locale);
			assertEquals("true", rpc.format(true));
			assertEquals("false", rpc.format(false));
		} finally {
			Locale.setDefault(defaultLocale);
		}
	}

	/**
	 * test for parsing a string
	 */
	public void testParseToString() {
		Locale locale = Locale.US;
		ReportParameterConverter rpc;

		rpc = new ReportParameterConverter("@@@@", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("123", rpc.parse("123 ", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@!!!", locale);
		assertEquals("123", rpc.parse("123 ", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@", locale);
		assertEquals("123456", rpc.parse("123456", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@!", locale);
		assertEquals("123456", rpc.parse("123456", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("(@@)@@@", locale);
		assertEquals("123", rpc.parse("(  )123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("(&&)@@@", locale);
		assertEquals("  123", rpc.parse("(  )123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("&&&&!", locale);
		assertEquals("123 ", rpc.parse("123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("&&&&&&", locale);
		assertEquals("   123", rpc.parse("123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@&!", locale);
		assertEquals("123 ", rpc.parse("123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@&", locale);
		assertEquals("123", rpc.parse(" 123", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("@@@@@aaa!", locale);
		assertEquals("123", rpc.parse("123  aaa", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@&&aaa!", locale);
		assertEquals("123  ", rpc.parse("123  aaa", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@aaa!", locale);
		assertEquals("123", rpc.parse("12aaa3", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@@aaa>!", locale);
		assertEquals("123", rpc.parse("123", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("@@@@<!", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@<!", locale);
		assertEquals("1234fggGgG", rpc.parse("1234fggGgG", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@>!", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@&&&>!", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("@@@@&&@@@<<<>>>@@@&>", locale);
		assertEquals("1234fggggg", rpc.parse("1234fggggg", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("!", locale);
		assertEquals("123", rpc.parse("123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("", locale);
		assertEquals("123", rpc.parse("123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter(">", locale);
		assertEquals("123aaA", rpc.parse("123aaA", IParameterDefn.TYPE_STRING));

		rpc = new ReportParameterConverter("***\"!", locale);
		assertEquals("123", rpc.parse("***\"123", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("***&YY&&&!", locale);
		assertEquals("123 ", rpc.parse("***1YY23", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("***&YY@@@!", locale);
		assertEquals("123", rpc.parse("***1YY23 ", IParameterDefn.TYPE_STRING));
		// test for SSN
		rpc = new ReportParameterConverter("@@@-@@-@@@@!", locale);
		assertEquals("6000003274", rpc.parse("600-00-03274", IParameterDefn.TYPE_STRING));
		// test for zipcode+4
		rpc = new ReportParameterConverter("@@@@@-@@@@!", locale);
		assertEquals("943050110", rpc.parse("94305-0110", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@@-@@@@", locale);
		assertEquals("943050110", rpc.parse("94305-0110", IParameterDefn.TYPE_STRING));
		// test for zipcode
		rpc = new ReportParameterConverter("@@@@@!", locale);
		assertEquals("94305", rpc.parse("94305", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("@@@@@", locale);
		assertEquals("94305", rpc.parse("94305", IParameterDefn.TYPE_STRING));
		// test for phonenumber
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@!", locale);
		assertEquals("6508372345", rpc.parse("(650)-837-2345", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@", locale);
		assertEquals("6508372345", rpc.parse("(650)-837-2345", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@!", locale);
		assertEquals("65083723456", rpc.parse("(650)-837-23456", IParameterDefn.TYPE_STRING));
		rpc = new ReportParameterConverter("(@@@)-@@@-@@@@", locale);
		assertEquals("346508372345", rpc.parse("34(650)-837-2345", IParameterDefn.TYPE_STRING));
	}

	/**
	 * test for parsing a data
	 */
	public void testParseToDate() {
		Locale locale = Locale.US;
		ReportParameterConverter rpc;
		Calendar dateCal;
		Date date;

		dateCal = Calendar.getInstance();
		dateCal.clear();
		dateCal.set(1998, 8, 13, 20, 1, 0);
		date = dateCal.getTime();
		locale = Locale.ITALY;
		rpc = new ReportParameterConverter("MM/dd/yy KK:mm aa", locale);
		assertEquals(date, rpc.parse("09/13/98 08:01 pm", IParameterDefn.TYPE_DATE_TIME));

		locale = Locale.US;
		dateCal = Calendar.getInstance();
		dateCal.clear();
		dateCal.set(1998, 8, 13, 0, 0, 0);
		date = dateCal.getTime();
		locale = Locale.ITALY;
		rpc = new ReportParameterConverter("Long Date", locale);
		assertEquals(date, rpc.parse("13 settembre 1998", IParameterDefn.TYPE_DATE_TIME));

		locale = Locale.US;
		dateCal = Calendar.getInstance();
		dateCal.clear();
		dateCal.set(1998, 8, 13, 20, 1, 44);
		date = dateCal.getTime();
		rpc = new ReportParameterConverter("MM/dd/yyyy hh:mm:ss a", locale);
		assertEquals(date, rpc.parse("09/13/1998 08:01:44 PM", IParameterDefn.TYPE_DATE_TIME));

		String date1 = "2005/05/06 03:45:25";
		ReportParameterConverter converter = new ReportParameterConverter("yyyy/MM/dd hh:mm:ss", Locale.US);
		assertEquals("2005-05-06", converter.parse(date1, IParameterDefn.TYPE_DATE).toString());
		assertEquals("03:45:25", converter.parse(date1, IParameterDefn.TYPE_TIME).toString());
	}

	/**
	 * test for parsing a number or other obj
	 */
	public void testParseToOther() {
		Locale locale = Locale.US;
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter("#.0#", locale);
		assertEquals(1002.2, rpc.parse("1002.2", IParameterDefn.TYPE_FLOAT));

		rpc = new ReportParameterConverter("$###,##0.00;'Negative'", locale);
		assertEquals(new BigDecimal("2139.3"), rpc.parse("$2,139.30", IParameterDefn.TYPE_DECIMAL));
		assertEquals(new BigDecimal(-2000), rpc.parse("Negative2,000.00", IParameterDefn.TYPE_DECIMAL));
		assertEquals(new BigDecimal(2000), rpc.parse("$2,000.00", IParameterDefn.TYPE_DECIMAL));
		assertEquals(new BigDecimal("-2.13"), rpc.parse("Negative2.13", IParameterDefn.TYPE_DECIMAL));

		rpc = new ReportParameterConverter(null, locale);
		assertEquals(true, rpc.parse("true", IParameterDefn.TYPE_BOOLEAN));
		assertEquals(false, rpc.parse("false", IParameterDefn.TYPE_BOOLEAN));
	}

	/**
	 *
	 */
	public void testBigDecimal() {
		Locale locale = Locale.FRENCH;
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter(null, locale);
		assertEquals(new BigDecimal("123456.789012"), rpc.parse("123456,789012", IParameterDefn.TYPE_DECIMAL));

		assertEquals("123456,789012", rpc.format(new BigDecimal("123456.789012")));

		locale = Locale.US;
		rpc = new ReportParameterConverter(null, locale);
		assertEquals(new BigDecimal("123456.789012"), rpc.parse("123456.789012", IParameterDefn.TYPE_DECIMAL));
		assertEquals("123456.789012", rpc.format(new BigDecimal("123456.789012")));

	}

	/**
	 *
	 */
	public void testFloat() {
		Locale locale = Locale.FRENCH;
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter(null, locale);
		assertEquals(Double.parseDouble("123456.789012"), rpc.parse("123456,789012", IParameterDefn.TYPE_FLOAT));

		assertEquals("123456,789012", rpc.format(Double.parseDouble("123456.789012")));

		locale = Locale.US;
		rpc = new ReportParameterConverter(null, locale);
		assertEquals(Double.parseDouble("123456.789012"), rpc.parse("123456.789012", IParameterDefn.TYPE_FLOAT));
		assertEquals("123456.789012", rpc.format(Double.parseDouble("123456.789012")));

	}
}
