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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.ibm.icu.util.ULocale;
;
/**
 * 
 */

public class ParameterConverterTest extends TestCase
{

	/*
	 * test for formating a string
	 */
	public void testStringFormat( )
	{
		ULocale locDef = ULocale.getDefault( );
		ReportParameterConverter rpc;

		rpc = new ReportParameterConverter( "@@@@", locDef );
		assertEquals( "1234fggggg", rpc.format( "1234fggggg" ) );
		rpc = new ReportParameterConverter( "@@@@!", locDef );
		assertEquals( "1234fggggg", rpc.format( "1234fggggg" ) );

		rpc = new ReportParameterConverter( "@@@@!", locDef );
		assertEquals( "123 ", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@@@!!!", locDef );
		assertEquals( "123 ", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@@@", locDef );
		assertEquals( "123456", rpc.format( "123456" ) );
		rpc = new ReportParameterConverter( "@@@@!", locDef );
		assertEquals( "123456", rpc.format( "123456" ) );
		rpc = new ReportParameterConverter( "(@@)@@@", locDef );
		assertEquals( "(  )123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "&&&&!", locDef );
		assertEquals( "123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "&&&&&&", locDef );
		assertEquals( "123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@@&!", locDef );
		assertEquals( "123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "&@@@&!", locDef );
		assertEquals( "123 ", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@@&", locDef );
		assertEquals( " 123", rpc.format( "123" ) );

		rpc = new ReportParameterConverter( "@@@@@aaa!", locDef );
		assertEquals( "123  aaa", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@aaa!", locDef );
		assertEquals( "12aaa3", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@aaa&&&!", locDef );
		assertEquals( "12aaa3", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "@@@@@aaa>!", locDef );
		assertEquals( "123  aaa", rpc.format( "123" ) );

		rpc = new ReportParameterConverter( "@@@@<!", locDef );
		assertEquals( "1234fggggg", rpc.format( "1234fggggg" ) );
		rpc = new ReportParameterConverter( "@@@@<!", locDef );
		assertEquals( "1234fggggg", rpc.format( "1234fggGgG" ) );
		rpc = new ReportParameterConverter( "@@@@>!", locDef );
		assertEquals( "1234FGGGGG", rpc.format( "1234fggggg" ) );
		rpc = new ReportParameterConverter( "@@@@&&&>!", locDef );
		assertEquals( "1234FGGGGG", rpc.format( "1234fggggg" ) );

		rpc = new ReportParameterConverter( "@@@@&&@@@<<<>>>@@@&>", locDef );
		assertEquals( "   1234FGGGGG", rpc.format( "1234fggggg" ) );

		rpc = new ReportParameterConverter( "!", locDef );
		assertEquals( "123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "", locDef );
		assertEquals( "123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( ">", locDef );
		assertEquals( "123AAA", rpc.format( "123aaA" ) );

		rpc = new ReportParameterConverter( "^^^\"!", locDef );
		assertEquals( "^^^\"123", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "^^^&YY&&&!", locDef );
		assertEquals( "^^^1YY23", rpc.format( "123" ) );
		rpc = new ReportParameterConverter( "^^^&YY@@@!", locDef );
		assertEquals( "^^^1YY23 ", rpc.format( "123" ) );

		// test for SSN
		rpc = new ReportParameterConverter( "@@@-@@-@@@@!", locDef );
		assertEquals( "600-00-03274", rpc.format( "6000003274" ) );
		// test for zipcode+4
		rpc = new ReportParameterConverter( "@@@@@-@@@@!", locDef );
		assertEquals( "94305-0110", rpc.format( "943050110" ) );
		rpc = new ReportParameterConverter( "@@@@@-@@@@", locDef );
		assertEquals( "94305-0110", rpc.format( "943050110" ) );
		// test for zipcode
		rpc = new ReportParameterConverter( "@@@@@!", locDef );
		assertEquals( "94305", rpc.format( "94305" ) );
		rpc = new ReportParameterConverter( "@@@@@", locDef );
		assertEquals( "94305", rpc.format( "94305" ) );
		// test for phonenumber
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@!", locDef );
		assertEquals( "(650)-837-2345,", rpc.format( "6508372345," ) );
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@", locDef );
		assertEquals( "(650)-837-2345", rpc.format( "6508372345" ) );
	}

	/*
	 * test for formating a data
	 */
	public void testDateFormat( )
	{
		ULocale locDef = ULocale.getDefault( ), locale;
		ReportParameterConverter rpc;
		Calendar dateCal;
		Date date;

		locale = new ULocale( "en", "us" );
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		date = dateCal.getTime( );
		locale = ULocale.ITALY;
		rpc = new ReportParameterConverter( "MM/dd/yy KK:mm aa", locale );
		assertEquals( "09/13/98 08:01 p.", rpc.format( date ) );
		rpc = new ReportParameterConverter( "Long Date", locale );
		assertEquals( "13 settembre 1998", rpc.format( date ) );

		locale = new ULocale( "en", "us" );
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		date = dateCal.getTime( );
		rpc = new ReportParameterConverter( "MM/dd/yyyy hh:mm:ss a", locale );
		assertEquals( "09/13/1998 08:01:44 PM", rpc.format( date ) );

		// test the instance of locale
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		date = dateCal.getTime( );

		locale = new ULocale( "en", "us" );
		rpc = new ReportParameterConverter( "Long Date", locale );
		assertEquals( "September 13, 1998", rpc.format( date ) );
		rpc = new ReportParameterConverter( "D", locale );
		assertEquals( "September 13, 1998", rpc.format( date ) );
		rpc = new ReportParameterConverter( "Medium Date", locale );
		assertEquals( "Sep 13, 1998", rpc.format( date ) );
		rpc = new ReportParameterConverter( "Short Date", locale );
		assertEquals( "9/13/98", rpc.format( date ) );
		rpc = new ReportParameterConverter( "d", locale );
		assertEquals( "9/13/98", rpc.format( date ) );
		rpc = new ReportParameterConverter( "Long Time", locale );
		assertEquals( true, rpc.format( date ).startsWith( "8:01:44 PM GMT" ) );
		rpc = new ReportParameterConverter( "T", locale );
		assertEquals( true, rpc.format( date ).startsWith( "8:01:44 PM GMT" ) );

		SimpleDateFormat javaSample = (SimpleDateFormat) java.text.DateFormat
				.getDateTimeInstance( java.text.DateFormat.LONG,
						java.text.DateFormat.SHORT, locale.toLocale( ) );
		rpc = new ReportParameterConverter( "f", locale );
		assertEquals( javaSample.format( date ), rpc.format( date ) );

		rpc = new ReportParameterConverter( "General Date", locale );
		assertEquals( true, rpc.format( date ).startsWith(
				"September 13, 1998 8:01:44 PM GMT" ) );
		rpc = new ReportParameterConverter( "Short Time", locale );
		assertEquals( "20:01", rpc.format( date ) );
		rpc = new ReportParameterConverter( "Medium Time", locale );
		assertEquals( "8:01:44 PM", rpc.format( date ) );
	}

	/*
	 * test for formating a number or other obj
	 */
	public void testOtherFormat( )
	{
		ULocale locDef = ULocale.getDefault( );
		ReportParameterConverter rpc;

		NumberFormat number = NumberFormat.getInstance( Locale.getDefault( ) );
		number.setGroupingUsed( false );
		rpc = new ReportParameterConverter( null, locDef.toLocale( ) );
		assertEquals( number.format( 1002.234 ),
				rpc.format( new Double( 1002.234 ) ) );

		// test format with different style.
		rpc = new ReportParameterConverter( "#", locDef.toLocale( ) );
		assertEquals( "1002", rpc.format( new Double( 1002.2 ) ) );
		rpc = new ReportParameterConverter( "0", locDef.toLocale( ) );
		assertEquals( "1002", rpc.format( new Double( 1002.2 ) ) );
		rpc = new ReportParameterConverter( "###,##0", locDef.toLocale( ) );
		assertEquals( "1,002", rpc.format( new Double( 1002.2 ) ) );
		rpc = new ReportParameterConverter( "#.0#", locDef.toLocale( ) );
		assertEquals( "1002.2", rpc.format( new Double( 1002.2 ) ) );
		rpc = new ReportParameterConverter( "###,##0.00 'm/s'",
				locDef.toLocale( ) );
		assertEquals( "1,002.20 m/s", rpc.format( new Double( 1002.2 ) ) );
		rpc = new ReportParameterConverter( "#.##", locDef.toLocale( ) );
		assertEquals( "1002", rpc.format( new Long( 1002 ) ) );

		// test format with ; deliminated.
		rpc = new ReportParameterConverter( "###.#';'", locDef.toLocale( ) );
		assertEquals( "1002.2;", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-1002.2;", rpc.format( new Double( -1002.2 ) ) );

		rpc = new ReportParameterConverter( "###.#\';';#", locDef.toLocale( ) );
		assertEquals( "1002.2;", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "1002.2", rpc.format( new Double( -1002.2 ) ) );
		rpc = new ReportParameterConverter( "#.00%", locDef.toLocale( ) );
		assertEquals( "1002200.00%", rpc.format( new Long( 10022 ) ) );

		// test format with user-defined pattern
		rpc = new ReportParameterConverter( "General Number", locDef.toLocale( ) );
		assertEquals( number.format( 1002.20 ),
				rpc.format( new Double( 1002.20 ) ) );
		assertEquals( number.format( -1002.2 ),
				rpc.format( new Double( -1002.2 ) ) );
		assertEquals( number.format( 0.004 ), rpc.format( new Double( 0.004 ) ) );
		assertEquals( number.format( 0.004123456 ),
				rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( number.format( -0.004 ),
				rpc.format( new Double( -0.004 ) ) );
		assertEquals( number.format( 0 ), rpc.format( new Long( 0 ) ) );

		number = NumberFormat.getCurrencyInstance( Locale.getDefault( ) );
		rpc = new ReportParameterConverter( "C", locDef.toLocale( ) );
		assertEquals( number.format( 1290.8889 ),
				rpc.format( new Double( 1290.8889 ) ) );

		rpc = new ReportParameterConverter( "Fixed", locDef.toLocale( ) );
		assertEquals( "1002.20", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-1002.20", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "0.00", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "3333333333.33", rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.00", rpc.format( new Double( 0 ) ) );

		rpc = new ReportParameterConverter( "Standard", locDef.toLocale( ) );
		assertEquals( "1,002.20", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-1,002.20", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "0.00", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "0.00", rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( "-0.00", rpc.format( new Double( -0.004 ) ) );
		assertEquals( "3,333,333,333.33",
				rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.00", rpc.format( new Long( 0 ) ) );

		rpc = new ReportParameterConverter( "Percent", locDef.toLocale( ) );
		assertEquals( "100220.00%", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-100220.00%", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "0.40%", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "0.41%", rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( "-0.40%", rpc.format( new Double( -0.004 ) ) );
		assertEquals( "333333333333.00%",
				rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.00%", rpc.format( new Double( 0 ) ) );

		rpc = new ReportParameterConverter( "P", locDef.toLocale( ) );
		assertEquals( "100,220.00 %", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-100,220.00 %", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "0.40 %", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "0.41 %", rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( "-0.40 %", rpc.format( new Double( -0.004 ) ) );
		assertEquals( "333,333,333,333.00 %",
				rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.00 %", rpc.format( new Double( 0 ) ) );

		rpc = new ReportParameterConverter( "Scientific", locDef.toLocale( ) );
		assertEquals( "1.00E03", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-1.00E03", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "4.00E-03", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "4.12E-03", rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( "-4.00E-03", rpc.format( new Double( -0.004 ) ) );
		assertEquals( "3.33E09", rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.00E00", rpc.format( new Long( 0 ) ) );
		assertEquals( "1.00E00", rpc.format( new Long( 1 ) ) );

		rpc = new ReportParameterConverter( "e", locDef.toLocale( ) );
		assertEquals( "1.002200E03", rpc.format( new Double( 1002.2 ) ) );
		assertEquals( "-1.002200E03", rpc.format( new Double( -1002.2 ) ) );
		assertEquals( "4.000000E-03", rpc.format( new Double( 0.004 ) ) );
		assertEquals( "4.123456E-03", rpc.format( new Double( 0.004123456 ) ) );
		assertEquals( "-4.000000E-03", rpc.format( new Double( -0.004 ) ) );
		assertEquals( "3.333333E09", rpc.format( new Double( 3333333333.33 ) ) );
		assertEquals( "0.000000E00", rpc.format( new Long( 0 ) ) );
		assertEquals( "1.000000E00", rpc.format( new Long( 1 ) ) );

		rpc = new ReportParameterConverter( "x", locDef.toLocale( ) );
		assertEquals( "3ea", rpc.format( new Long( 1002 ) ) );
		assertEquals( "fffffffffffffc16", rpc.format( new Long( -1002 ) ) );
		assertEquals( "3ea", rpc.format( new Double( 1002.22 ) ) );

		rpc = new ReportParameterConverter( "d", locDef.toLocale( ) );
		assertEquals( "1,002", rpc.format( new Long( 1002 ) ) );
		assertEquals( "-1,002", rpc.format( new Long( -1002 ) ) );
		assertEquals( "1,002.009", rpc.format( new Double( 1002.009 ) ) );

		rpc = new ReportParameterConverter( "$###,##0.00;'Negative'",
				locDef.toLocale( ) );
		assertEquals( "$2,139.30", rpc.format( new BigDecimal( 2139.3 ) ) );
		assertEquals( "$2.14", rpc.format( new BigDecimal( 2.139 ) ) );
		assertEquals( "Negative2.13", rpc.format( new BigDecimal( -2.13 ) ) );
		assertEquals( "$0.00", rpc.format( new BigDecimal( 0.0 ) ) );
		assertEquals( "$2,000.00", rpc.format( new BigDecimal( 2000 ) ) );
		assertEquals( "$20.00", rpc.format( new BigDecimal( 20 ) ) );
		assertEquals( "Negative2,000.00", rpc.format( new BigDecimal( -2000 ) ) );
		assertEquals( "$0.00", rpc.format( new BigDecimal( 0 ) ) );

		rpc = new ReportParameterConverter( "d", locDef.toLocale( ) );
		assertEquals( "2.139", rpc.format( new BigDecimal( 2.139 ) ) );

		rpc = new ReportParameterConverter( null, locDef.toLocale( ) );
		assertEquals( "true", rpc.format( new Boolean( true ) ) );
		assertEquals( "false", rpc.format( new Boolean( false ) ) );
	}

	/*
	 * test for parsing a string
	 */
	public void testParseToString( )
	{
		ULocale locDef = ULocale.getDefault( );
		ReportParameterConverter rpc;

		rpc = new ReportParameterConverter( "@@@@", locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@!", locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "@@@@!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "123 ",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@!!!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "123 ",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@", locDef.toLocale( ) );
		assertEquals( "123456", rpc.parse( "123456",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@!", locDef.toLocale( ) );
		assertEquals( "123456", rpc.parse( "123456",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "(@@)@@@", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "(  )123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "(&&)@@@", locDef.toLocale( ) );
		assertEquals( "  123", rpc.parse( "(  )123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "&&&&!", locDef.toLocale( ) );
		assertEquals( "123 ", rpc.parse( "123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "&&&&&&", locDef.toLocale( ) );
		assertEquals( "   123", rpc.parse( "123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@&!", locDef.toLocale( ) );
		assertEquals( "123 ", rpc.parse( "123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@&", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( " 123",
				IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "@@@@@aaa!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "123  aaa",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@&&aaa!", locDef.toLocale( ) );
		assertEquals( "123  ", rpc.parse( "123  aaa",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@aaa!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "12aaa3",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@@aaa>!", locDef.toLocale( ) );
		assertEquals( "123",
				rpc.parse( "123", IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "@@@@<!", locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@<!", locDef.toLocale( ) );
		assertEquals( "1234fggGgG", rpc.parse( "1234fggGgG",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@>!", locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@&&&>!", locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "@@@@&&@@@<<<>>>@@@&>",
				locDef.toLocale( ) );
		assertEquals( "1234fggggg", rpc.parse( "1234fggggg",
				IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "!", locDef.toLocale( ) );
		assertEquals( "123",
				rpc.parse( "123", IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "", locDef.toLocale( ) );
		assertEquals( "123",
				rpc.parse( "123", IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( ">", locDef.toLocale( ) );
		assertEquals( "123aaA", rpc.parse( "123aaA",
				IScalarParameterDefn.TYPE_STRING ) );

		rpc = new ReportParameterConverter( "^^^\"!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "^^^\"123",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "^^^&YY&&&!", locDef.toLocale( ) );
		assertEquals( "123 ", rpc.parse( "^^^1YY23",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "^^^&YY@@@!", locDef.toLocale( ) );
		assertEquals( "123", rpc.parse( "^^^1YY23 ",
				IScalarParameterDefn.TYPE_STRING ) );
		// test for SSN
		rpc = new ReportParameterConverter( "@@@-@@-@@@@!", locDef.toLocale( ) );
		assertEquals( "6000003274", rpc.parse( "600-00-03274",
				IScalarParameterDefn.TYPE_STRING ) );
		// test for zipcode+4
		rpc = new ReportParameterConverter( "@@@@@-@@@@!", locDef.toLocale( ) );
		assertEquals( "943050110", rpc.parse( "94305-0110",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@@-@@@@", locDef.toLocale( ) );
		assertEquals( "943050110", rpc.parse( "94305-0110",
				IScalarParameterDefn.TYPE_STRING ) );
		// test for zipcode
		rpc = new ReportParameterConverter( "@@@@@!", locDef.toLocale( ) );
		assertEquals( "94305", rpc.parse( "94305",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "@@@@@", locDef.toLocale( ) );
		assertEquals( "94305", rpc.parse( "94305",
				IScalarParameterDefn.TYPE_STRING ) );
		// test for phonenumber
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@!",
				locDef.toLocale( ) );
		assertEquals( "6508372345", rpc.parse( "(650)-837-2345",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@", locDef.toLocale( ) );
		assertEquals( "6508372345", rpc.parse( "(650)-837-2345",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@!",
				locDef.toLocale( ) );
		assertEquals( "65083723456", rpc.parse( "(650)-837-23456",
				IScalarParameterDefn.TYPE_STRING ) );
		rpc = new ReportParameterConverter( "(@@@)-@@@-@@@@", locDef.toLocale( ) );
		assertEquals( "346508372345", rpc.parse( "34(650)-837-2345",
				IScalarParameterDefn.TYPE_STRING ) );
	}

	/*
	 * test for parsing a data
	 */
	public void testParseToDate( )
	{
		ULocale locDef = ULocale.getDefault( ), locale;
		ReportParameterConverter rpc;
		Calendar dateCal;
		Date date;

		locale = new ULocale( "en", "us" );
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.clear( );
		dateCal.set( 1998, 8, 13, 20, 1, 0 );
		date = dateCal.getTime( );
		locale = ULocale.ITALY;
		rpc = new ReportParameterConverter( "MM/dd/yy KK:mm aa", locale );
		assertEquals( date, rpc.parse( "09/13/98 08:01 p.",
				IScalarParameterDefn.TYPE_DATE_TIME ) );

		locale = new ULocale( "en", "us" );
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.clear( );
		dateCal.set( 1998, 8, 13, 0, 0, 0 );
		date = dateCal.getTime( );
		locale = ULocale.ITALY;
		rpc = new ReportParameterConverter( "Long Date", locale );
		assertEquals( date, rpc.parse( "13 settembre 1998",
				IScalarParameterDefn.TYPE_DATE_TIME ) );

		locale = new ULocale( "en", "us" );
		dateCal = Calendar.getInstance( locDef.toLocale( ) );
		dateCal.clear( );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		date = dateCal.getTime( );
		rpc = new ReportParameterConverter( "MM/dd/yyyy hh:mm:ss a", locale );
		assertEquals( date, rpc.parse( "09/13/1998 08:01:44 PM",
				IScalarParameterDefn.TYPE_DATE_TIME ) );
	}

	/*
	 * test for parsing a number or other obj
	 */
	public void testParseToOther( )
	{
		ULocale locDef = ULocale.getDefault( );
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter( "#.0#", locDef.toLocale( ) );
		assertEquals( new Double( 1002.2 ), rpc.parse( "1002.2",
				IScalarParameterDefn.TYPE_FLOAT ) );

		rpc = new ReportParameterConverter( "$###,##0.00;'Negative'",
				locDef.toLocale( ) );
		assertEquals( new BigDecimal( "2139.3" ), rpc.parse( "$2,139.30",
				IScalarParameterDefn.TYPE_DECIMAL ) );
		assertEquals( new BigDecimal( -2000 ), rpc.parse( "Negative2,000.00",
				IScalarParameterDefn.TYPE_DECIMAL ) );
		assertEquals( new BigDecimal( 2000 ), rpc.parse( "$2,000.00",
				IScalarParameterDefn.TYPE_DECIMAL ) );
		assertEquals( new BigDecimal( "-2.13" ), rpc.parse( "Negative2.13",
				IScalarParameterDefn.TYPE_DECIMAL ) );

		rpc = new ReportParameterConverter( null, locDef.toLocale( ) );
		assertEquals( new Boolean( true ), rpc.parse( "true",
				IScalarParameterDefn.TYPE_BOOLEAN ) );
		assertEquals( new Boolean( false ), rpc.parse( "false",
				IScalarParameterDefn.TYPE_BOOLEAN ) );
	}
	
	public void testBigDecimal()
	{
		ULocale locale = ULocale.FRENCH;
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter( null, locale.toLocale( ) );
		assertEquals( new BigDecimal( "123456.789012" ), rpc.parse(
				"123456,789012", IScalarParameterDefn.TYPE_DECIMAL ) );
		
		assertEquals( "123456,789012", rpc.format( new BigDecimal(
				"123456.789012" ) ) );
		
		locale = ULocale.US;
		rpc = new ReportParameterConverter( null, locale.toLocale( ) );
		assertEquals( new BigDecimal( "123456.789012" ), rpc.parse(
				"123456.789012", IScalarParameterDefn.TYPE_DECIMAL ) );
		assertEquals( "123456.789012", rpc.format( new BigDecimal(
				"123456.789012" ) ) );
		
	}
	
	public void testFloat()
	{
		ULocale locale = ULocale.FRENCH;
		ReportParameterConverter rpc;
		rpc = new ReportParameterConverter( null, locale.toLocale( ) );
		assertEquals( new Double( "123456.789012" ), rpc.parse(
				"123456,789012", IScalarParameterDefn.TYPE_FLOAT ) );
		
		assertEquals( "123456,789", rpc.format( new Double(
				"123456.789012" ) ) );
		
		locale = ULocale.US;
		rpc = new ReportParameterConverter( null, locale.toLocale( ) );
		assertEquals( new Double( "123456.789012" ), rpc.parse(
				"123456.789012", IScalarParameterDefn.TYPE_FLOAT ) );
		assertEquals( "123456.789", rpc.format( new Double(
				"123456.789012" ) ) );
		
	}
}
