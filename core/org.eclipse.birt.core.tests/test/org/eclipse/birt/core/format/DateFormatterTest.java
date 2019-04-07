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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import static org.junit.Assert.*;

/**
 * DateFormatterTest.
 * 
 * Design for test whether DateFormatter Class can do a correct formating,
 * translate the Date/Time instance to an object of String according to the
 * format string. In this unit test, we pay more attention the new method which
 * we add in the subclss.
 * 
 */
public class DateFormatterTest
{

	/*
	 * test for void DateFormatter()
	 */
	@Test
    public void testDateFormat( )
	{
		String golden = "09/13/1998 08:01:44 PM";
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a", ULocale.ENGLISH );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		
		String fmtDate = sample.format(date);
		assertEquals( golden, fmtDate );
	}

	/*
	 * test for void DateFormatter(String)
	 */
	@Test
    public void testDateFormatString( )
	{
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a" );
		Locale locDef = Locale.getDefault( );
		Calendar dateCal = Calendar.getInstance( locDef );
		dateCal.set( 1998, 8, 13, 20, 1, 44 );
		Date date = dateCal.getTime( );
		SimpleDateFormat sampleJava = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm:ss a", locDef );
		assertEquals( sampleJava.format( date ), sample.format( date ) );
	}

	/*
	 * test for void DateFormatter(String, String) especially test for chinese
	 * support
	 */
	@Test
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
	@Test
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
		assertEquals( "09/13/98 08:01 PM", sample.format( date ) );
		sample = new DateFormatter( "Long Date", ULocale.forLocale(locale) );
		assertEquals( "13 settembre 1998", sample.format( date ) );
		assertTrue( true );

	}

	/*
	 * test for void DateFormatter(String, Locale)
	 */
	@Test
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
	@Test
    public void testGetPattern( )
	{
		Locale locale = new Locale( "en", "us" );
		DateFormatter sample = new DateFormatter( "MM/dd/yyyy hh:mm:ss a",
				ULocale.forLocale(locale) );
		assertEquals( "MM/dd/yyyy hh:mm:ss a", sample.getPattern( ) );
	}

	@Test
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

		assertEquals( "9/13/1998, 8:01:44 PM", strDateTime );
		assertEquals( "9/13/1998", strDate );
		assertEquals( "8:01:44 PM", strTime );
		
		// parse
		try
		{
			Date date1 = format.parse( strDate );
			java.sql.Date date2 = new java.sql.Date( date1.getTime( ) );
			assertEquals( date.toString( ), date2.toString( ) );
			
			Date time1 = format.parse( strTime );
			java.sql.Time time2 = new java.sql.Time( time1.getTime( ) );
			assertEquals( time.toString( ), time2.toString( ) );
			
			Date dateTime1 = format.parse( strDateTime );
			assertEquals( dateTime.toString( ), dateTime1.toString( ) );
		}
		catch( ParseException ex )
		{
			assertTrue( false );
		}
		String tmpDate = "01/02/2003 3:";
		try
		{
			Date tmpD = format.parse( tmpDate );
			java.sql.Date d1 = new java.sql.Date( tmpD.getTime());
		}
		catch( ParseException ex )
		{
			ex.printStackTrace( );
		}
	}
	@Test
    public void testTimeZone()
	{
		String result = null;
		DateFormatter df = null;

		Calendar dateCal = Calendar.getInstance( );
		dateCal.setTimeZone(java.util.TimeZone.getTimeZone("PST"));
		dateCal.set( 1998, 8, 13, 5, 1, 44 );
		Date dateTime = dateCal.getTime( );
		java.sql.Time sqlTime = new java.sql.Time( dateTime.getTime( ) );

		String utcDate = "13 Sep 1998 12:01";
		String utcTime = "12:01:44";
		TimeZone UTCTimeZone = TimeZone.getTimeZone( "UTC" );
		df = new DateFormatter( ULocale.UK, UTCTimeZone );
		result = df.format( dateTime );
		assertTrue( utcDate.equalsIgnoreCase( result ) );
		result = df.format( sqlTime );
		assertTrue( utcTime.equalsIgnoreCase( result ) );

		String japanDate = "1998/09/13 21:01";
		String japanTime = "21:01:44";
		TimeZone japanTimeZone = TimeZone.getTimeZone( "Japan" );
		df = new DateFormatter( ULocale.JAPAN, japanTimeZone );
		result = df.format( dateTime );
		assertTrue( japanDate.equalsIgnoreCase( result ) );
		result = df.format( sqlTime );
		assertTrue( japanTime.equalsIgnoreCase( result ) );
	}
}
