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

/**
 * 
 * @version $Revision: 1.9 $ $Date: 2005/12/27 09:07:28 $
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

}