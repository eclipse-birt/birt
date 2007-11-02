/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.format;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.TimeZone;

/**
 * DateFormatterTest.
 * 
 * Design for test whether DateFormatter Class can do a correct formating,
 * translate the Date/Time instance to an object of String according to the
 * format string. In this unit test, we pay more attention the new method which
 * we add in the subclss.
 * 
 */
public class DateFormatterTest extends TestCase
{

	/*
	 * test for void DateFormatter()
	 */
	public void testDateFormat( )
	{
		String golden = "09/13/0008 08:01:44 PM";
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a", ULocale.ENGLISH );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 8, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		
		String fmtDate = sample.format(date);
		assertEquals( golden, fmtDate );
	}

	/*
	 * test for void DateFormatter(String)
	 */
	public void testDateFormatString( )
	{
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a" );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 8, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		SimpleDateFormat sampleJava = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm:ss a", locDef );
		assertEquals( sampleJava.format( date ), sample.format( date ) );
	}

	/*
	 * test for void DateFormatter(String, String) especially test for chinese
	 * support
	 */
	public void testDateFormatStringStringCHN( )
	{
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a",
				new ULocale( "CHINESE" ) );
		SimpleDateFormat sampleJava = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm:ss a", new Locale( "CHINESE" ) );
		assertEquals( sampleJava.format( date ), sample.format( date ) );
		sample.applyPattern( "MM/dd/yyyy hh:mm:ss a" );
		assertEquals( sampleJava.format( date ), sample.format( date ) );

	}

	/*
	 * test for void DateFormatter(Locale)
	 */
	public void testDateFormatLocale( )
	{
		Locale locale = new Locale( "en", "us" );
		DateFormatter sample = new DateFormatter( ULocale.forLocale(locale) );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		//assertEquals(sampleJava.format(date), sample.format(date));
		locale = Locale.ITALY;
		sample = new DateFormatter( ULocale.forLocale(locale) );
		SimpleDateFormat sampleJava = new SimpleDateFormat( "MM/dd/yy KK:mm aa", locale );
		//assertEquals(sampleJava.format(date), sample.format(date));
		sample.applyPattern( "MM/dd/yy KK:mm aa" );
		assertEquals( "09/13/98 08:01 p.", sample.format( date ) );
		sample = new DateFormatter( "Long Date", ULocale.forLocale(locale) );
		assertEquals( "13 settembre 1998", sample.format( date ) );
		assertTrue( true );

	}

	/*
	 * test for void DateFormatter(String, Locale)
	 */
	public void testDateFormatStringLocale( )
	{
		Locale locale = new Locale( "en", "us" );
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a",
				ULocale.forLocale(locale) );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		assertEquals( "09/13/1998 08:01:44 PM", sample.format( date ) );
	}

	/*
	 * test for void GetPattern()
	 */
	public void testGetPattern( )
	{
		Locale locale = new Locale( "en", "us" );
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a",
				ULocale.forLocale(locale) );
		assertEquals( "MM/dd/yyyy hh:mm:ss a", sample.getPattern( ) );
	}

	public void testApplyPattern( )
	{
		//test the instance of locale
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );

		Locale locale = new Locale( "en", "us" );
		DateFormatter sample = new DateFormatter( ULocale.forLocale(locale) );
		sample.applyPattern( "Long Date" );
		assertEquals( "September 13, 1998", sample.format( date ) );
		sample.applyPattern( "D" );
		assertEquals( "September 13, 1998", sample.format( date ) );
		sample.applyPattern( "Medium Date" );
		assertEquals( "Sep 13, 1998", sample.format( date ) );
		sample.applyPattern( "Short Date" );
		assertEquals( "9/13/98", sample.format( date ) );
		sample.applyPattern( "d" );
		assertEquals( "9/13/98", sample.format( date ) );
		sample.applyPattern( "Long Time" );
		assertEquals( true, sample.format( date ).startsWith( "8:01:44 PM GMT+" ) );
		sample.applyPattern( "T" );
		assertEquals( true, sample.format( date ).startsWith( "8:01:44 PM GMT+" ) );
		SimpleDateFormat javaSample = (SimpleDateFormat) java.text.DateFormat
				.getDateTimeInstance( java.text.DateFormat.LONG,
						java.text.DateFormat.SHORT, locale );
		sample.applyPattern( "f" );
		assertEquals( javaSample.format( date ), sample.format( date ) );
		sample.applyPattern( "General Date" );
		assertEquals( true, sample.format( date ).startsWith( "September 13, 1998 8:01:44 PM GMT+" ) );

		sample.applyPattern( "Short Time" );
		assertEquals( "20:01", sample.format( date ) );
		sample.applyPattern( "Medium Time" );
		assertEquals( "8:01:44 PM", sample.format( date ) );
	}
	
	public void testIFormat( )
	{
		Calendar dateCal = Calendar.getInstance( Locale.US );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date dateTime = dateCal.getTime( );
		java.sql.Date date = new java.sql.Date( dateTime.getTime( ) );
		java.sql.Time time = new java.sql.Time( dateTime.getTime( ) );

		DateFormatter format = new DateFormatter( "i", ULocale.US );
		String strDateTime = format.format( dateTime );
		String strTime = format.format( time );
		String strDate = format.format( date );

		assertEquals( "9/13/1998 8:01:44 PM", strDateTime );
		assertEquals( "9/13/1998", strDate );
		assertEquals( "8:01:44 PM", strTime );
	}
	
	public void testTimeZone()
	{
		String result = null;
		DateFormatter df = null;

		Calendar dateCal = Calendar.getInstance( );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date dateTime = dateCal.getTime( );

		String goldenUTC = "13 Sep 1998 12:01";
		TimeZone UTCTimeZone = TimeZone.getTimeZone( "UTC" );
		df = new DateFormatter( ULocale.UK, UTCTimeZone );
		result = df.format( dateTime );
		assertTrue( goldenUTC.equalsIgnoreCase( result ) );

		String goldenJapan = "1998/09/13 21:01";
		TimeZone japanTimeZone = TimeZone.getTimeZone( "Japan" );
		df = new DateFormatter( ULocale.JAPAN, japanTimeZone );
		result = df.format( dateTime );
		assertTrue( goldenJapan.equalsIgnoreCase( result ) );
	}
}