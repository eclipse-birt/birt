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

import junit.framework.Assert;
import junit.framework.TestCase;

import com.ibm.icu.util.ULocale;

/**
 * 
 */
public class NumberFormatterTest extends TestCase
{

	public void testNumericFormat( )
	{
		NumberFormatter numFormat = new NumberFormatter( );
		assertNull( numFormat.getPattern( ) );
		NumberFormat number = NumberFormat.getInstance( Locale.getDefault( ) );
		number.setGroupingUsed( false );
		assertEquals( number.format( 1002.234 ), numFormat.format( 1002.234 ) );
		assertEquals( "NaN", numFormat.format( Double.NaN ) );
	}

	/**
	 * Class under test for void applyPattern(String)
	 */
	public void testApplyPattern( )
	{
		NumberFormatter numFormat = new NumberFormatter( );
		DecimalFormat dec = new DecimalFormat( );

		//test format with different style.
		numFormat.applyPattern( "#" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1002" );

		numFormat.applyPattern( "0" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1002" );

		numFormat.applyPattern( "###,##0" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1,002" );

		numFormat.applyPattern( "#.0#" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1002.2" );

		numFormat.applyPattern( "###,##0.00 'm/s'" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1,002.20 m/s" );

		numFormat.applyPattern( "#.##" );
		Assert.assertEquals( numFormat.format( 1002 ), "1002" );

		//		test format with ; deliminated.
		numFormat.applyPattern( "###.#';'" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1002.2;" );
		Assert.assertEquals( numFormat.format( -1002.2 ), "-1002.2;" );

		numFormat.applyPattern( "###.#\';';#" );
		Assert.assertEquals( numFormat.format( 1002.2 ), "1002.2;" );
		Assert.assertEquals( numFormat.format( -1002.2 ), "1002.2" );
		dec.applyPattern( "#.00%" );
		Assert.assertEquals( "1002200.00%", dec.format( 10022 ) );
		dec.applyPattern( "0.00E00" );
		dec.applyPattern( "" );

		//test format with user-defined pattern
		numFormat.applyPattern( "General Number" );
		NumberFormat number = NumberFormat.getInstance( Locale.getDefault( ) );
		number.setGroupingUsed( false );
		Assert.assertEquals( number.format( 1002.20 ), numFormat
				.format( 1002.20 ) );
		Assert.assertEquals( number.format( -1002.2 ), numFormat
				.format( -1002.2 ) );
		Assert.assertEquals( number.format( 0.004 ), numFormat.format( 0.004 ) );
		Assert.assertEquals( number.format( 0.004123456 ), numFormat
				.format( 0.004123456 ) );
		Assert
				.assertEquals( number.format( -0.004 ), numFormat
						.format( -0.004 ) );
		Assert.assertEquals( number.format( 0 ), numFormat.format( 0 ) );

		//Test for currency has passed, perhaps the result will be changed in
		// different local,so we ignore these test case
		//numFormat.applyPattern( "Currency" );
		//Assert.assertEquals("��1,002.20" ,numFormat.format( 1002.2 ));
		//Assert.assertEquals("-��1,002.20" ,numFormat.format( -1002.2 ));
		//Assert.assertEquals("��0.00" ,numFormat.format( 0.004 ));
		//Assert.assertEquals("����3,333,333,333.33"
		// ,numFormat.format(3333333333.33 ));
		//Assert.assertEquals("��0.00" ,numFormat.format(0 ));
		numFormat.applyPattern( "C" );
		//Assert.assertEquals("��1,002.20" ,numFormat.format( 1002.2 ));
		number = NumberFormat.getCurrencyInstance( Locale.getDefault( ) );
		assertEquals( number.format( 1290.8889 ), numFormat.format( 1290.8889 ) );

		numFormat.applyPattern( "Fixed" );
		Assert.assertEquals( "1002.20", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-1002.20", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "0.00", numFormat.format( 0.004 ) );
		//Assert.assertEquals("0.004123456" ,numFormat.format( 0.004123456 ));
		//Assert.assertEquals("-0.004" ,numFormat.format(-0.004 ));
		Assert
				.assertEquals( "3333333333.33", numFormat
						.format( 3333333333.33 ) );
		Assert.assertEquals( "0.00", numFormat.format( 0 ) );

		numFormat.applyPattern( "Standard" );
		Assert.assertEquals( "1,002.20", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-1,002.20", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "0.00", numFormat.format( 0.004 ) );
		Assert.assertEquals( "0.00", numFormat.format( 0.004123456 ) );
		Assert.assertEquals( "-0.00", numFormat.format( -0.004 ) );
		Assert.assertEquals( "3,333,333,333.33", numFormat
				.format( 3333333333.33 ) );
		Assert.assertEquals( "0.00", numFormat.format( 0 ) );

		numFormat.applyPattern( "Percent" );
		Assert.assertEquals( "100220.00%", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-100220.00%", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "0.40%", numFormat.format( 0.004 ) );
		Assert.assertEquals( "0.41%", numFormat.format( 0.004123456 ) );
		Assert.assertEquals( "-0.40%", numFormat.format( -0.004 ) );
		Assert.assertEquals( "333333333333.00%", numFormat
				.format( 3333333333.33 ) );
		Assert.assertEquals( "0.00%", numFormat.format( 0 ) );

		numFormat.applyPattern( "P" );
		Assert.assertEquals( "100,220.00 %", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-100,220.00 %", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "0.40 %", numFormat.format( 0.004 ) );
		Assert.assertEquals( "0.41 %", numFormat.format( 0.004123456 ) );
		Assert.assertEquals( "-0.40 %", numFormat.format( -0.004 ) );
		Assert.assertEquals( "333,333,333,333.00 %", numFormat
				.format( 3333333333.33 ) );
		Assert.assertEquals( "0.00 %", numFormat.format( 0 ) );

		numFormat.applyPattern( "Scientific" );
		Assert.assertEquals( "1.00E03", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-1.00E03", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "4.00E-03", numFormat.format( 0.004 ) );
		Assert.assertEquals( "4.12E-03", numFormat.format( 0.004123456 ) );
		Assert.assertEquals( "-4.00E-03", numFormat.format( -0.004 ) );
		Assert.assertEquals( "3.33E09", numFormat.format( 3333333333.33 ) );
		Assert.assertEquals( "0.00E00", numFormat.format( 0 ) );
		Assert.assertEquals( "1.00E00", numFormat.format( 1 ) );

		numFormat.applyPattern( "e" );
		Assert.assertEquals( "1.002200E03", numFormat.format( 1002.2 ) );
		Assert.assertEquals( "-1.002200E03", numFormat.format( -1002.2 ) );
		Assert.assertEquals( "4.000000E-03", numFormat.format( 0.004 ) );
		Assert.assertEquals( "4.123456E-03", numFormat.format( 0.004123456 ) );
		Assert.assertEquals( "-4.000000E-03", numFormat.format( -0.004 ) );
		Assert.assertEquals( "3.333333E09", numFormat.format( 3333333333.33 ) );
		Assert.assertEquals( "0.000000E00", numFormat.format( 0 ) );
		Assert.assertEquals( "1.000000E00", numFormat.format( 1 ) );

		numFormat.applyPattern( "x" );
		Assert.assertEquals( "3ea", numFormat.format( 1002 ) );
		Assert.assertEquals( "fffffffffffffc16", numFormat.format( -1002 ) );
		Assert.assertEquals( "3ea", numFormat.format( 1002.22 ) );

		numFormat.applyPattern( "d" );
		Assert.assertEquals( "1,002", numFormat.format( 1002 ) );
		Assert.assertEquals( "-1,002", numFormat.format( -1002 ) );
		Assert.assertEquals( "1,002.009", numFormat.format( 1002.009 ) );
		numFormat = new NumberFormatter( "tttt" );
		numFormat.applyPattern( "zzz" );
	}

	/**
	 * Class under test for String getPattern();
	 */
	public void testGetPattern( )
	{
		//test $ prefix, subformat, "..", \ in format
		NumberFormatter numFormat = new NumberFormatter(
				"$###,##0.00;'Negative'" );
		Assert.assertEquals( numFormat.getPattern( ), "$###,##0.00;'Negative'" );

	}

	/**
	 * Class under test for String Format(BigDecimal)
	 */
	public void testFormatBigDecimal( )
	{
		NumberFormatter numFormat = new NumberFormatter(
				"$###,##0.00;'Negative'" );

		Assert.assertEquals( numFormat.format( new BigDecimal( 2139.3 ) ),
				"$2,139.30" );
		Assert.assertEquals( numFormat.format( new BigDecimal( 2.139 ) ),
				"$2.14" );
		Assert.assertEquals( numFormat.format( new BigDecimal( -2.13 ) ),
				"Negative2.13" );
		Assert
				.assertEquals( numFormat.format( new BigDecimal( 0.0 ) ),
						"$0.00" );

		Assert.assertEquals( numFormat.format( new BigDecimal( 2000 ) ),
				"$2,000.00" );
		Assert
				.assertEquals( numFormat.format( new BigDecimal( 20 ) ),
						"$20.00" );
		Assert.assertEquals( numFormat.format( new BigDecimal( -2000 ) ),
				"Negative2,000.00" );
		Assert.assertEquals( numFormat.format( new BigDecimal( 0 ) ), "$0.00" );
		numFormat.applyPattern( "d" );
		Assert.assertEquals( numFormat.format( new BigDecimal( 2.139 ) ),
				"2.139" );

	}

	public void testMinusZero( )
	{
		NumberFormatter numFormat = new NumberFormatter( );
		
		double[] smallValues = { -0.49, -0.049, -0.0049, -0.00049, -0.00000049 };
		double[] bigValues = { -0.51, -0.051, -0.0051, -0.00051, -0.00000051 };
		
		numFormat.applyPattern( "#" );
		Assert.assertEquals( numFormat.format( smallValues[0] ), "-0" );
		Assert.assertEquals( numFormat.format( bigValues[0] ), "-1" );

		numFormat.applyPattern( "0" );
		Assert.assertEquals( numFormat.format( smallValues[0] ), "-0" );
		Assert.assertEquals( numFormat.format( bigValues[0] ), "-1" );

		numFormat.applyPattern( "###,##0" );
		Assert.assertEquals( numFormat.format( smallValues[3] ), "-0" );
		Assert.assertEquals( numFormat.format( bigValues[3] ), "-0" );

		numFormat.applyPattern( "#.0#" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-.0" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-.01" );

		numFormat.applyPattern( "###,##0.00 'm/s'" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0.00 m/s" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.01 m/s" );

		numFormat.applyPattern( "#.##" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.01" );

		numFormat.applyPattern( "###.#';'" );
		Assert.assertEquals( numFormat.format( smallValues[1] ), "-0;" );
		Assert.assertEquals( numFormat.format( bigValues[1] ), "-0.1;" );

		numFormat.applyPattern( "###.#\';';#" );
		Assert.assertEquals( numFormat.format( smallValues[1] ), "0" );
		Assert.assertEquals( numFormat.format( bigValues[1] ), "0.1" );
		
		numFormat.applyPattern( "#.00%" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-.49%" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-.51%" );

		numFormat.applyPattern( "Fixed" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0.00" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.01" );

		numFormat.applyPattern( "Standard" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0.00" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.01" );

		numFormat.applyPattern( "Percent" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0.49%" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.51%" );

		numFormat.applyPattern( "P" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-0.49 %" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-0.51 %" );

		numFormat.applyPattern( "Scientific" );
		Assert.assertEquals( numFormat.format( smallValues[2] ), "-4.90E-03" );
		Assert.assertEquals( numFormat.format( bigValues[2] ), "-5.10E-03" );

		numFormat.applyPattern( "e" );
		Assert.assertEquals( numFormat.format( smallValues[4] ), "-4.900000E-07" );
		Assert.assertEquals( numFormat.format( bigValues[4] ), "-5.100000E-07" );
	}

	public void testPatternAttributes( )
	{
		// test DigitSubstitution
		// Arabic Hindic has different behavior
		Locale arabic = new Locale( "ar" );
		Locale english = new Locale( "en" );
		String[] patterns = {"General Number",
				"General Number{DigitSubstitution=true}"};
		double[] values = {123.12, 902.023};
		String[][] araGoldens = new String[][]{{"123.12", "902.023"},
				{"١٢٣٫١٢", "٩٠٢٫٠٢٣"}};
		String[][] engGoldens = new String[][]{{"123.12", "902.023"},
				{"123.12", "902.023"}};
		NumberFormatter nf = null;
		for ( int pindex = 0; pindex < patterns.length; pindex++ )
		{
			String pattern = patterns[pindex];
			nf = new NumberFormatter( pattern, arabic );
			for ( int vindex = 0; vindex < values.length; vindex++ )
			{
				double value = values[vindex];
				String res = nf.format( value );
				assertTrue( res.equals( araGoldens[pindex][vindex] ) );
			}
		}
		for ( int pindex = 0; pindex < patterns.length; pindex++ )
		{
			String pattern = patterns[pindex];
			nf = new NumberFormatter( pattern, english );
			for ( int vindex = 0; vindex < values.length; vindex++ )
			{
				double value = values[vindex];
				String res = nf.format( value );
				assertTrue( res.equals( engGoldens[pindex][vindex] ) );
			}
		}
	}

	public void testRoundingMode( )
	{
		// support
		// roundingMode:HALF_EVEN,HALF_UP,HALF_DOWN,UP,DOWN,FLOOR,CEILING,UNNECESSARY
		// Default value is :HALF_UP
		String pattern = "###0.0{RoundingMode=HALF_EVEN;}";
		double[] values = {3.00, 3.01, 3.02, 3.03, 3.04, 3.05, 3.06, 3.07,
				3.08, 3.09};
		String[] upGoldens = {"3.0", "3.0", "3.0", "3.0", "3.0", "3.1", "3.1",
				"3.1", "3.1", "3.1"};
		String[] evenGoldens = {"3.0", "3.0", "3.0", "3.0", "3.0", "3.0",
				"3.1", "3.1", "3.1", "3.1"};
		NumberFormatter nf = new NumberFormatter( pattern, ULocale.getDefault( ) );
		for ( int index = 0; index < values.length; index++ )
		{
			String result = nf.format( values[index] );
			assertTrue( result.equals( evenGoldens[index] ) );
		}
		pattern = "###0.0{RoundingMode=HALF_UP;}";
		nf = new NumberFormatter( pattern, ULocale.getDefault( ) );
		for ( int index = 0; index < values.length; index++ )
		{
			String result = nf.format( values[index] );
			assertTrue( result.equals( upGoldens[index] ) );
		}
	}

}