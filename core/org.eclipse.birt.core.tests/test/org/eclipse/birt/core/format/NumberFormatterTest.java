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

package org.eclipse.birt.core.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 */
public class NumberFormatterTest extends TestCase {
	@Test
	public void testNumericFormat() {
		NumberFormatter numFormat = new NumberFormatter();
		assertNull(numFormat.getPattern());
		NumberFormat number = NumberFormat.getInstance(Locale.getDefault());
		number.setGroupingUsed(false);
		assertEquals(number.format(1002.234), numFormat.format(1002.234));
		assertEquals("NaN", numFormat.format(Double.NaN));
	}

	/**
	 * Class under test for void applyPattern(String)
	 */
	@Test
	public void testApplyPattern() {
		NumberFormatter numFormat = new NumberFormatter(ULocale.US);
		DecimalFormat dec = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);

		// test format with different style.
		numFormat.applyPattern("#");
		assertEquals(numFormat.format(1002.2), "1002");

		numFormat.applyPattern("0");
		assertEquals(numFormat.format(1002.2), "1002");

		numFormat.applyPattern("###,##0");
		assertEquals(numFormat.format(1002.2), "1,002");

		numFormat.applyPattern("#.0#");
		assertEquals(numFormat.format(1002.2), "1002.2");

		numFormat.applyPattern("###,##0.00 'm/s'");
		assertEquals(numFormat.format(1002.2), "1,002.20 m/s");

		numFormat.applyPattern("#.##");
		assertEquals(numFormat.format(1002), "1002");

		// test format with ; deliminated.
		numFormat.applyPattern("###.#';'");
		assertEquals(numFormat.format(1002.2), "1002.2;");
		assertEquals(numFormat.format(-1002.2), "-1002.2;");

		numFormat.applyPattern("###.#\';';#");
		assertEquals(numFormat.format(1002.2), "1002.2;");
		assertEquals(numFormat.format(-1002.2), "1002.2");
		dec.applyPattern("#.00%");
		assertEquals("1002200.00%", dec.format(10022));
		dec.applyPattern("0.00E00");
		dec.applyPattern("");

		// test format with user-defined pattern
		numFormat.applyPattern("General Number");
		NumberFormat number = NumberFormat.getInstance(Locale.US);
		number.setGroupingUsed(false);
		assertEquals(number.format(1002.20), numFormat.format(1002.20));
		assertEquals(number.format(-1002.2), numFormat.format(-1002.2));
		assertEquals(number.format(0.004), numFormat.format(0.004));
		assertEquals(number.format(0.004123456), numFormat.format(0.004123456));
		assertEquals(number.format(-0.004), numFormat.format(-0.004));
		assertEquals(number.format(0), numFormat.format(0));

		// Test for currency has passed, perhaps the result will be changed in
		// different local,so we ignore these test case
		// numFormat.applyPattern( "Currency" );
		// assertEquals("��1,002.20" ,numFormat.format( 1002.2 ));
		// assertEquals("-��1,002.20" ,numFormat.format( -1002.2 ));
		// assertEquals("��0.00" ,numFormat.format( 0.004 ));
		// assertEquals("����3,333,333,333.33"
		// ,numFormat.format(3333333333.33 ));
		// assertEquals("��0.00" ,numFormat.format(0 ));
		numFormat.applyPattern("C");
		// assertEquals("��1,002.20" ,numFormat.format( 1002.2 ));
		number = NumberFormat.getCurrencyInstance(Locale.US);
		assertEquals(number.format(1290.8889), numFormat.format(1290.8889));

		numFormat.applyPattern("Fixed");
		assertEquals("1002.20", numFormat.format(1002.2));
		assertEquals("-1002.20", numFormat.format(-1002.2));
		assertEquals("0.00", numFormat.format(0.004));
		// assertEquals("0.004123456" ,numFormat.format( 0.004123456 ));
		// assertEquals("-0.004" ,numFormat.format(-0.004 ));
		assertEquals("3333333333.33", numFormat.format(3333333333.33));
		assertEquals("0.00", numFormat.format(0));

		numFormat.applyPattern("Standard");
		assertEquals("1,002.20", numFormat.format(1002.2));
		assertEquals("-1,002.20", numFormat.format(-1002.2));
		assertEquals("0.00", numFormat.format(0.004));
		assertEquals("0.00", numFormat.format(0.004123456));
		assertEquals("-0.00", numFormat.format(-0.004));
		assertEquals("3,333,333,333.33", numFormat.format(3333333333.33));
		assertEquals("0.00", numFormat.format(0));

		numFormat.applyPattern("Percent");
		assertEquals("100220.00%", numFormat.format(1002.2));
		assertEquals("-100220.00%", numFormat.format(-1002.2));
		assertEquals("0.40%", numFormat.format(0.004));
		assertEquals("0.41%", numFormat.format(0.004123456));
		assertEquals("-0.40%", numFormat.format(-0.004));
		assertEquals("333333333333.00%", numFormat.format(3333333333.33));
		assertEquals("0.00%", numFormat.format(0));

		numFormat.applyPattern("P");
		assertEquals("100,220.00 %", numFormat.format(1002.2));
		assertEquals("-100,220.00 %", numFormat.format(-1002.2));
		assertEquals("0.40 %", numFormat.format(0.004));
		assertEquals("0.41 %", numFormat.format(0.004123456));
		assertEquals("-0.40 %", numFormat.format(-0.004));
		assertEquals("333,333,333,333.00 %", numFormat.format(3333333333.33));
		assertEquals("0.00 %", numFormat.format(0));

		numFormat.applyPattern("Scientific");
		assertEquals("1.00E03", numFormat.format(1002.2));
		assertEquals("-1.00E03", numFormat.format(-1002.2));
		assertEquals("4.00E-03", numFormat.format(0.004));
		assertEquals("4.12E-03", numFormat.format(0.004123456));
		assertEquals("-4.00E-03", numFormat.format(-0.004));
		assertEquals("3.33E09", numFormat.format(3333333333.33));
		assertEquals("0.00E00", numFormat.format(0));
		assertEquals("1.00E00", numFormat.format(1));

		numFormat.applyPattern("e");
		assertEquals("1.002200E03", numFormat.format(1002.2));
		assertEquals("-1.002200E03", numFormat.format(-1002.2));
		assertEquals("4.000000E-03", numFormat.format(0.004));
		assertEquals("4.123456E-03", numFormat.format(0.004123456));
		assertEquals("-4.000000E-03", numFormat.format(-0.004));
		assertEquals("3.333333E09", numFormat.format(3333333333.33));
		assertEquals("0.000000E00", numFormat.format(0));
		assertEquals("1.000000E00", numFormat.format(1));

		numFormat.applyPattern("x");
		assertEquals("3ea", numFormat.format(1002));
		assertEquals("fffffffffffffc16", numFormat.format(-1002));
		assertEquals("3ea", numFormat.format(1002.22));

		numFormat.applyPattern("d");
		assertEquals("1,002", numFormat.format(1002));
		assertEquals("-1,002", numFormat.format(-1002));
		assertEquals("1,002.009", numFormat.format(1002.009));
		numFormat = new NumberFormatter("tttt");
		numFormat.applyPattern("zzz");
	}

	/**
	 * Class under test for String getPattern();
	 */
	@Test
	public void testGetPattern() {
		// test $ prefix, subformat, "..", \ in format
		NumberFormatter numFormat = new NumberFormatter("$###,##0.00;'Negative'");
		assertEquals(numFormat.getPattern(), "$###,##0.00;'Negative'");

	}

	/**
	 * Class under test for String Format(BigDecimal)
	 */
	@Test
	public void testFormatBigDecimal() {
		NumberFormatter numFormat = new NumberFormatter("$###,##0.00;'Negative'", ULocale.US);
		assertEquals(numFormat.format(new BigDecimal(2139.3)), "$2,139.30");
		assertEquals(numFormat.format(new BigDecimal(2.139)), "$2.14");
		assertEquals(numFormat.format(new BigDecimal(-2.13)), "Negative2.13");
		assertEquals(numFormat.format(new BigDecimal(0.0)), "$0.00");
		assertEquals(numFormat.format(new BigDecimal(2000)), "$2,000.00");
		assertEquals(numFormat.format(new BigDecimal(20)), "$20.00");
		assertEquals(numFormat.format(new BigDecimal(-2000)), "Negative2,000.00");
		assertEquals(numFormat.format(new BigDecimal(0)), "$0.00");
		numFormat.applyPattern("d");
		assertEquals(numFormat.format(new BigDecimal(2.139)), "2.139");

	}

	@Test
	public void testMinusZero() {
		NumberFormatter numFormat = new NumberFormatter(ULocale.US);

		double[] smallValues = { -0.49, -0.049, -0.0049, -0.00049, -0.00000049 };
		double[] bigValues = { -0.51, -0.051, -0.0051, -0.00051, -0.00000051 };

		numFormat.applyPattern("#");
		assertEquals(numFormat.format(smallValues[0]), "-0");
		assertEquals(numFormat.format(bigValues[0]), "-1");

		numFormat.applyPattern("0");
		assertEquals(numFormat.format(smallValues[0]), "-0");
		assertEquals(numFormat.format(bigValues[0]), "-1");

		numFormat.applyPattern("###,##0");
		assertEquals(numFormat.format(smallValues[3]), "-0");
		assertEquals(numFormat.format(bigValues[3]), "-0");

		numFormat.applyPattern("#.0#");
		assertEquals(numFormat.format(smallValues[2]), "-.0");
		assertEquals(numFormat.format(bigValues[2]), "-.01");

		numFormat.applyPattern("###,##0.00 'm/s'");
		assertEquals(numFormat.format(smallValues[2]), "-0.00 m/s");
		assertEquals(numFormat.format(bigValues[2]), "-0.01 m/s");

		numFormat.applyPattern("#.##");
		assertEquals(numFormat.format(smallValues[2]), "-0");
		assertEquals(numFormat.format(bigValues[2]), "-0.01");

		numFormat.applyPattern("###.#';'");
		assertEquals(numFormat.format(smallValues[1]), "-0;");
		assertEquals(numFormat.format(bigValues[1]), "-0.1;");

		numFormat.applyPattern("###.#\';';#");
		assertEquals(numFormat.format(smallValues[1]), "0");
		assertEquals(numFormat.format(bigValues[1]), "0.1");

		numFormat.applyPattern("#.00%");
		assertEquals(numFormat.format(smallValues[2]), "-.49%");
		assertEquals(numFormat.format(bigValues[2]), "-.51%");

		numFormat.applyPattern("Fixed");
		assertEquals(numFormat.format(smallValues[2]), "-0.00");
		assertEquals(numFormat.format(bigValues[2]), "-0.01");

		numFormat.applyPattern("Standard");
		assertEquals(numFormat.format(smallValues[2]), "-0.00");
		assertEquals(numFormat.format(bigValues[2]), "-0.01");

		numFormat.applyPattern("Percent");
		assertEquals(numFormat.format(smallValues[2]), "-0.49%");
		assertEquals(numFormat.format(bigValues[2]), "-0.51%");

		numFormat.applyPattern("P");
		assertEquals(numFormat.format(smallValues[2]), "-0.49 %");
		assertEquals(numFormat.format(bigValues[2]), "-0.51 %");

		numFormat.applyPattern("Scientific");
		assertEquals(numFormat.format(smallValues[2]), "-4.90E-03");
		assertEquals(numFormat.format(bigValues[2]), "-5.10E-03");

		numFormat.applyPattern("e");
		assertEquals(numFormat.format(smallValues[4]), "-4.900000E-07");
		assertEquals(numFormat.format(bigValues[4]), "-5.100000E-07");
	}

	@Test
	public void testPatternAttributes() {
		// test DigitSubstitution
		// Arabic Hindic has different behavior
		ULocale arabic = new ULocale("ar");
		ULocale english = new ULocale("en");
		String[] patterns = { "General Number", "General Number{DigitSubstitution=true}" };
		double[] values = { 123.12, 902.023 };
		String[][] araGoldens = new String[][] { { "123.12", "902.023" },
				{ "\u0661\u0662\u0663\u066b\u0661\u0662", "\u0669\u0660\u0662\u066b\u0660\u0662\u0663" } };
		String[][] engGoldens = new String[][] { { "123.12", "902.023" }, { "123.12", "902.023" } };
		NumberFormatter nf = null;
		for (int pindex = 0; pindex < patterns.length; pindex++) {
			String pattern = patterns[pindex];
			nf = new NumberFormatter(pattern, arabic);
			for (int vindex = 0; vindex < values.length; vindex++) {
				double value = values[vindex];
				String res = nf.format(value);
				try {
					assertEquals(araGoldens[1][vindex],
							new String(res.getBytes("UTF-8"), "UTF-8"));
				} catch (Exception e1) {
					fail(e1.toString());
				}
			}
		}
		for (int pindex = 0; pindex < patterns.length; pindex++) {
			String pattern = patterns[pindex];
			nf = new NumberFormatter(pattern, english);
			for (int vindex = 0; vindex < values.length; vindex++) {
				double value = values[vindex];
				String res = nf.format(value);
				assertTrue(res.equals(engGoldens[pindex][vindex]));
			}
		}
	}

	@Test
	public void testRoundingMode() {
		// support
		// roundingMode:HALF_EVEN,HALF_UP,HALF_DOWN,UP,DOWN,FLOOR,CEILING,UNNECESSARY
		// Default value is :HALF_UP
		String pattern = "###0.0{RoundingMode=HALF_EVEN;}";
		double[] values = { 3.00, 3.01, 3.02, 3.03, 3.04, 3.05, 3.06, 3.07, 3.08, 3.09 };
		String[] upGoldens = { "3.0", "3.0", "3.0", "3.0", "3.0", "3.1", "3.1", "3.1", "3.1", "3.1" };
		String[] evenGoldens = { "3.0", "3.0", "3.0", "3.0", "3.0", "3.0", "3.1", "3.1", "3.1", "3.1" };
		NumberFormatter nf = new NumberFormatter(pattern, ULocale.US);
		for (int index = 0; index < values.length; index++) {
			String result = nf.format(values[index]);
			assertTrue(result.equals(evenGoldens[index]));
		}
		pattern = "###0.0{RoundingMode=HALF_UP;}";
		nf = new NumberFormatter(pattern, ULocale.US);
		for (int index = 0; index < values.length; index++) {
			String result = nf.format(values[index]);
			assertTrue(result.equals(upGoldens[index]));
		}
	}

}
