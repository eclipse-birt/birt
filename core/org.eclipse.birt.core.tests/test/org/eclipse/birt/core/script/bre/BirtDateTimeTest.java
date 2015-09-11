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

package org.eclipse.birt.core.script.bre;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 *
 */

public class BirtDateTimeTest extends TestCase
{

	private Context cx;
	private Scriptable scope;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp( ) throws Exception
	{
		/*
		 * Creates and enters a Context. The Context stores information about
		 * the execution environment of a script.
		 */


		cx = Context.enter( );
		/*
		 * Initialize the standard objects (Object, Function, etc.) This must be
		 * done before scripts can be executed. Returns a scope object that we
		 * use in later calls.
		 */
		scope = cx.initStandardObjects( );
		new CoreJavaScriptInitializer().initialize( cx, scope );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown( )
	{
		Context.exit( );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testYear( )
	{
		/*assertTrue( NativeBirtDateTime.jsStaticFunction_year( new Date(105, 10, 15) ) == 2005);
		assertTrue( NativeBirtDateTime.jsStaticFunction_year( new Date(0, 10, 15) ) == 1900);
		assertTrue( NativeBirtDateTime.jsStaticFunction_year( new Date(0, 12, 15) ) == 1901);*/
		String script1 = "BirtDateTime.year(new Date(23,11,11))";

		assertTrue( ( (Number) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ).intValue( ) == 1923 );

	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testQuarter( )
	{
		String script1 = "BirtDateTime.quarter(\"1905-10-11\")";
		String script2 = "BirtDateTime.quarter( new Date( 05,11,15))";
		String script3 = "BirtDateTime.quarter(\"1900-3-15\")";
		String script4 = "BirtDateTime.quarter( new Date( 0,11,15))";
		assertEquals( ( (Number) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ).intValue( ), 4 );
		assertEquals( ( (Number) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ).intValue( ), 4 );
		assertEquals( ( (Number) cx.evaluateString( scope,
				script3,
				"inline",
				1,
				null ) ).intValue( ), 1 );
		assertEquals( ( (Number) cx.evaluateString( scope,
				script4,
				"inline",
				1,
				null ) ).intValue( ), 4 );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testMonthDate( )
	{
		String script1 = "BirtDateTime.month(new Date(75,0,15),1)";
		String script2 = "BirtDateTime.month( new Date( 105,11,15),1)";
		String script3 = "BirtDateTime.month(\"1900-3-15\",1)";
		String script4 = "BirtDateTime.month( new Date( 10,11,15),1)";
		assertEquals( ( (String) cx.evaluateString( scope,
				script1,
				"inline",
				1,
				null ) ), "1" );
		assertEquals( ( (String) cx.evaluateString( scope,
				script2,
				"inline",
				1,
				null ) ), "12" );
		assertEquals( ( (String) cx.evaluateString( scope,
				script3,
				"inline",
				1,
				null ) ), "3" );
		assertEquals( ( (String) cx.evaluateString( scope,
				script4,
				"inline",
				1,
				null ) ), "12" );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.week(new Date(2006, 0, 1) )",
				"BirtDateTime.week(new Date(2006, 0, 3) )",
				"BirtDateTime.week(new Date(2006, 0, 7) )",
				"BirtDateTime.week(new Date(2006, 0, 8) )",
				"BirtDateTime.week(new Date(2006, 0, 14))",
				"BirtDateTime.week(\"1855-1-1\")",
				"BirtDateTime.week( new Date(1780, 0, 2))",
				"BirtDateTime.week(new Date(1780, 0, 8))",
				"BirtDateTime.week(new Date(1780, 0, 9))",
				"BirtDateTime.week(new Date(1780, 1, 9))",
				"BirtDateTime.week( new Date(1780, 2, 9))",
				"BirtDateTime.week(new Date(1780, 3, 9, 11, 0, 0) )",
				"BirtDateTime.week(new Date(1780, 4, 9, 23, 0, 0) )"
		};

		int[] values = new int[]{
				1, 1, 1, 2, 2, 1, 2, 2, 3, 7, 11, 16, 20
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testDay( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.day(new Date(2006, 0, 1) )",
				"BirtDateTime.day(new Date(2006, 0, 3) )",
				"BirtDateTime.day( new Date(1780, 0, 1))",
				"BirtDateTime.day(new Date(1780, 3, 9, 11, 0, 0) )",
				"BirtDateTime.day(new Date(1780, 4, 9, 23, 0, 0) )"
		};

		int[] values = new int[]{
				1, 3, 1, 9, 9
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testWeekDayDate( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.weekDay(new Date(2006, 0, 1), 1 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 3), 1 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 7), 1 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 8), 1 )",
				"BirtDateTime.weekDay(new Date(1780, 0, 1))",
				"BirtDateTime.weekDay(new Date(1780, 0, 2))",
				"BirtDateTime.weekDay(new Date(1780, 0, 8))",
				"BirtDateTime.weekDay(new Date(1780, 0, 9))",

				"BirtDateTime.weekDay(new Date(2006, 0, 1), 2 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 3), 2 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 7), 2 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 8), 2 )",
				"BirtDateTime.weekDay(new Date(1780, 0, 1), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 2), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 8), 2)",
				"BirtDateTime.weekDay(new Date(1780, 0, 9), 2)",

				"BirtDateTime.weekDay(new Date(2006, 0, 1), 3 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 3), 3 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 7), 3 )",
				"BirtDateTime.weekDay(new Date(2006, 0, 8), 3 )",
				"BirtDateTime.weekDay(new Date(1780, 0, 1), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 2), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 8), 3)",
				"BirtDateTime.weekDay(new Date(1780, 0, 9), 3)"
		};

		String[] values = new String[]{
				"1",
				"3",
				"7",
				"1",
				"7",
				"1",
				"7",
				"1",
				"7",
				"2",
				"6",
				"7",
				"6",
				"7",
				"6",
				"7",
				"6",
				"1",
				"5",
				"6",
				"5",
				"6",
				"5",
				"6"
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_today()'
	 */
	public void testToday( )
	{
		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date d = (Date) cx.evaluateString( scope,
				"BirtDateTime.today()",
				"inline",
				1,
				null );
		c.setTime( d );
		System.out.println( "year:"
				+ c.get( Calendar.YEAR ) + " month:" + c.get( Calendar.MONTH )
				+ " day:" + c.get( Calendar.DATE ) + " hour:"
				+ c.get( Calendar.HOUR ) );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_now()'
	 */
	public void testNow( )
	{
		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date d = (Date) cx.evaluateString( scope,
				"BirtDateTime.now()",
				"inline",
				1,
				null );
		c.setTime( d );
		System.out.println( "year:"
				+ c.get( Calendar.YEAR ) + " month:" + c.get( Calendar.MONTH )
				+ " day:" + c.get( Calendar.DATE ) + " hour:"
				+ c.get( Calendar.HOUR ) + " minute:" + c.get( Calendar.MINUTE )
				+ "second:" + c.get( Calendar.SECOND ) );
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffYear(Date, Date)'
	 */
	public void testDiffYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 0, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffYear(new Date(2007, 10, 15),new Date(2005, 0, 15) )",
				"BirtDateTime.diffYear(new Date(2005, 10, 15),new Date(1793, 0, 15) )"
		};

		int[] values = new int[]{
				2, 2, 3, -2, -212
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffMonth(Date, Date)'
	 */
	public void testDiffMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 0, 8) )",
				"BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffMonth(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffMonth(new Date(2007, 10, 15),new Date(2005, 0, 1) )",
				"BirtDateTime.diffMonth(new Date(1910, 10, 15),new Date(1890, 0, 15) )"
		};

		int[] values = new int[]{
				14, 25, 26, -34, -250
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffQuarter(Date, Date)'
	 */
	public void testDiffQuarter( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 0, 8) )",
				"BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 11, 15) )",
				"BirtDateTime.diffQuarter(new Date(2005, 10, 15),new Date(2007, 12, 15) )",
				"BirtDateTime.diffQuarter(new Date(2007, 10, 15),new Date(2005, 0, 1) )",
				"BirtDateTime.diffQuarter(new Date(1910, 10, 15),new Date(1890, 0, 15) )"
		};

		int[] values = new int[]{
				5, 8, 9, -11, -83
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffWeek(Date, Date)'
	 */
	public void testDiffWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffWeek(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 7) )",
				"BirtDateTime.diffWeek(new Date(2006, 0, 1),new Date(2006, 0, 8) )",
				"BirtDateTime.diffWeek(new Date(1779, 11, 31),new Date(1780, 0, 1) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1),new Date(1780, 0, 2) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1),new Date(1780, 0, 8) )",
				"BirtDateTime.diffWeek(new Date(1780, 0, 1),new Date(1780, 0, 9) )"
		};

		int[] values = new int[]{
				-1, -1, 0, 0, 1, 0, 1, 1, 2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	/*	assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 0, 0, 8 ),
				new Date( 0, 0, 6 ) ) == -1 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ),
				new Date( 105, 11, 31 ) ) == -1 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ),
				new Date( 106, 0, 3 ) ) == 0 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ),
				new Date( 106, 0, 7 ) ) == 0 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( 106, 0, 1 ),
				new Date( 106, 0, 8 ) ) == 1 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -121, 11, 31 ),
				new Date( -120, 0, 1 ) ) == 0 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ),
				new Date( -120, 0, 2 ) ) == 1 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ),
				new Date( -120, 0, 8 ) ) == 1 );
		assertTrue( NativeBirtDateTime.jsStaticFunction_diffWeek( new Date( -120, 0, 1 ),
				new Date( -120, 0, 9 ) ) == 2 );*/
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffDay(Date, Date) throws BirtException'
	 */
	public void testDiffDay( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffDay(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 2, 7) )",
				"BirtDateTime.diffDay(new Date(2006, 0, 1),new Date(2006, 2, 8) )",
				"BirtDateTime.diffDay(new Date(1993, 0, 1),new Date(1994, 2, 8) )"
		};

		int[] values = new int[]{
				-2, -1, 2, 37, 65, 66, 431
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffHour(Date, Date)'
	 */
	public void testDiffHour( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffHour(new Date(1900, 0, 8),new Date(1900, 0, 6) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 2, 7,11,2,0) )",
				"BirtDateTime.diffHour(new Date(2006, 0, 1),new Date(2006, 2, 8,22,0,0) )",
				"BirtDateTime.diffHour(new Date(1993, 0, 1),new Date(1994, 2, 8) )"
		};

		int[] values = new int[]{
				-2 * 24,
				-1 * 24,
				2 * 24,
				37 * 24,
				65 * 24 + 11,
				66 * 24 + 22,
				( 66 + 365 ) * 24
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffMinute(Date, Date)'
	 */
	public void testDiffMinute( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffMinute(\"1930-1-8 12:1:1\",\"1930-1-8 12:2:58\" )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2005, 11, 31) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 1, 7) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 2, 7,11,2,0) )",
				"BirtDateTime.diffMinute(new Date(2006, 0, 1),new Date(2006, 2, 8,22,3,0) )",
				"BirtDateTime.diffMinute(\"1993-1-1\",new Date(1994, 2, 8) )"
		};

		int[] values = new int[]{
				1,
				-1 * 24 * 60,
				2 * 24 * 60,
				37 * 24 * 60,
				( 65 * 24 + 11 ) * 60 + 2,
				( 66 * 24 + 22 ) * 60 + 3,
				( ( 66 + 365 ) * 24 ) * 60
		};

		for ( int i = 0; i < values.length; i++ )
		{
			System.out.println( i );
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_diffSecond(Date, Date)'
	 */
	public void testDiffSecond( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.diffSecond(new Date(1900, 0, 8,12,1,1),new Date(1900, 0, 8,12,2,58) )",
				"BirtDateTime.diffSecond(new Date(1900, 0, 8,12,1,58),new Date(1900, 0, 8,12,2,1) )",
				"BirtDateTime.diffSecond(new Date(2006, 0, 1),new Date(2006, 0, 3) )",
				"BirtDateTime.diffSecond(new Date(2006, 0, 1),new Date(2006, 2, 8,22,3,0) )",
				"BirtDateTime.diffSecond(\"1993-1-1\",new Date(1994, 2, 8) )"
		};

		int[] values = new int[]{
				60 + 57,
				3,
				2 * 24 * 60 * 60,
				( ( 66 * 24 + 22 ) * 60 + 3 ) * 60,
				( 66 + 365 ) * 24 * 60 * 60
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( ( (Number) cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) ).intValue( ), values[i] );
		}

	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addMonth(Date, int)'
	 */
	public void testAddYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addYear(new Date(2005, 10, 15),10 )",
				"BirtDateTime.addYear(new Date(1795, 10, 15),10 )",
				"BirtDateTime.addYear(\"1910-11-15\",10 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2015, 10, 15 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1805, 10, 15, 0, 0, 0 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1920, 10, 15 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 1; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addMonth(Date, int)'
	 */
	public void testAddMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addMonth(new Date(2005, 10, 15),10 )",
				"BirtDateTime.addMonth(\"1995-1-15\",10 )",
				"BirtDateTime.addMonth(\"1940-2-15\",11 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2006, 8, 15 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 15 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1941, 0, 15 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addQuarter(Date, int)'
	 */
	public void testAddQuarter( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addQuarter(new Date(2005, 10, 15),2 )",
				"BirtDateTime.addQuarter(\"1995-1-15\",9 )",
				"BirtDateTime.addQuarter(\"1930-6-15\",11 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2006, 4, 15 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1997, 3, 15 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1933, 2, 15 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addWeek(Date, int)'
	 */
	public void testAddWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addWeek(new Date(2005, 10, 15),1 )",
				"BirtDateTime.addWeek(new Date(2006, 9, 15),3 )",
				"BirtDateTime.addWeek(\"1995-11-15\",2 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 10, 22 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 10, 5 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 29 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addDay(Date, int)'
	 */
	public void testAddDay( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addDay(new Date(2005, 10, 15),7 )",
				"BirtDateTime.addDay(new Date(2006, 9, 15),21 )",
				"BirtDateTime.addDay(\"1995-11-15\",10 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 10, 22 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 10, 5 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 25 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addDay(Date, int)'
	 */
	public void testAddHour( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addHour(new Date(2005, 10, 15),7*24 )",
				"BirtDateTime.addHour(new Date(2006, 8, 15),21*24 )",
				"BirtDateTime.addHour(\"1995-11-15\",10 )",
				"BirtDateTime.addHour(null,21*24 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 10, 22 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 9, 6 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 15, 10, 0, 0 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1970, 0, 22, 0, 0, 0 );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3, null
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) , values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addMinute(Date, int)'
	 */
	public void testAddMinute( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addMinute(new Date(2005, 10, 15),7*24*60 )",
				"BirtDateTime.addMinute(new Date(2006, 9, 15),-21*24*60 )",
				"BirtDateTime.addMinute(\"1995-11-15\",10*60+10 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 10, 22 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 8, 24 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 15, 10, 10, 0 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_addSecond(Date, int)'
	 */
	public void testAddSecond( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.addSecond(new Date(2005, 10, 15),7*24*60*60 )",
				"BirtDateTime.addSecond(new Date(2006, 8, 15),21*24*60*60 )",
				"BirtDateTime.addSecond(\"1995-11-15\",(10*60+10)*60+9 )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 10, 22 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 9, 6 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 1995, 10, 15, 10, 10, 9 );

		Date d3 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		Date[] values = new Date[]{
				d1, d2, d3
		};

		for ( int i = 0; i < values.length; i++ )
		{
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_firstDayOfYear(Date)'
	 */
	public void testFirstDayOfYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfYear(new Date(2005, 10, 15) )",
				"BirtDateTime.firstDayOfYear(new Date(2006, 9, 15) )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 0, 1 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 0, 1 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		Date[] values = new Date[]{
				d1, d2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			System.out.println( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) );
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_firstDayOfQuarter(Date)'
	 */
	public void testFirstDayOfQuarter( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfQuarter(new Date(2005, 9, 15) )",
				"BirtDateTime.firstDayOfQuarter(new Date(2006, 8, 15) )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 9, 1 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 6, 1 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		Date[] values = new Date[]{
				d1, d2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			System.out.println( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) );
			System.out.println( "result:" + values[i] );
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_firstDayOfMonth(Date)'
	 */
	public void testFirstDayOfMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfMonth(new Date(2005, 9, 15) )",
				"BirtDateTime.firstDayOfMonth(new Date(2006, 8, 15) )"
		};

		Calendar c = Calendar.getInstance( );

		c.clear( );

		c.set( 2005, 9, 1 );

		Date d1 = new Date( c.getTimeInMillis( ) );

		c.clear( );

		c.set( 2006, 8, 1 );

		Date d2 = new Date( c.getTimeInMillis( ) );

		Date[] values = new Date[]{
				d1, d2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			System.out.println( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) );
			System.out.println( "result:" + values[i] );
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_firstDayOfWeek(Date)'
	 */
	public void testFirstDayOfWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfWeek(new Date(2011, 7, 25) )",
				"BirtDateTime.firstDayOfWeek(new Date(2011, 7, 15) )"
		};

		com.ibm.icu.util.Calendar c = com.ibm.icu.util.Calendar.getInstance( TimeZone.getDefault( ), ULocale.getDefault() );

		c.clear( );
		c.setMinimalDaysInFirstWeek( 1 );
		c.set( 2011, 7, 21 );
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek( ) );

		Date d1 = c.getTime( );

		c.clear( );
		c.setMinimalDaysInFirstWeek( 1 );
		c.set( 2011, 7, 14 );
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek( ) );
		Date d2 =  c.getTime( );

		Date[] values = new Date[]{
				d1, d2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			System.out.println( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ) );
			System.out.println( "result:" + values[i] );
			assertEquals( cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null ), values[i] );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_dayOfWeek(Date)'
	 */
	public void testDayOfWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 1) )",
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 2) )",
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 3) )",
				"BirtDateTime.dayOfWeek(new Date(2015, 4, 4) )",
		};

		int[] values = new int[]{
				6, 7, 1, 2
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_dayOfYeark(Date)'
	 */
	public void testDayOfYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.dayOfYear(new Date(2012, 0, 1) )",
				"BirtDateTime.dayOfYear(new Date(2012, 1, 29) )",
				"BirtDateTime.dayOfYear(new Date(2012, 11, 31) )",
				"BirtDateTime.dayOfYear(new Date(2015, 0, 1) )",
				"BirtDateTime.dayOfYear(new Date(2015, 1, 28) )",
				"BirtDateTime.dayOfYear(new Date(2015, 11, 31) )"
		};

		int[] values = new int[]{
				1, 60, 366, 1, 59, 365
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}

	/*
	 * Test method for 'org.eclipse.birt.core.script.bre.NativeBirtDateTime.jsStaticFunction_weekOfMonth(Date)'
	 */
	public void testWeekOfMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 1) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 2) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 3) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 9) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 10) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 16) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 17) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 23) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 24) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 30) )",
				"BirtDateTime.weekOfMonth(new Date(2015, 4, 31) )",
		};

		int[] values = new int[]{
				1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6
		};


		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalYear</code>
	 */
	public void testFiscalYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.fiscalYear(new Date(2015, 6, 1), new Date(2012, 6, 1 ))",// leap year
				"BirtDateTime.fiscalYear(new Date(2015, 6, 1), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalYear(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalYear(new Date(2015, 6, 12), new Date(2017, 7, 1 ))",
				"BirtDateTime.fiscalYear(new Date(2014, 8, 15))",
				"BirtDateTime.fiscalYear(new Date(2015, 6, 12))",
				"BirtDateTime.fiscalYear(new Date(2016, 8, 11))",
		};

		int[] values = new int[]{
				2015, 2015, 2014, 2014, 2014, 2015, 2016
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}

	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalQuarter</code>
	 */
	public void testFiscalQuarter( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.fiscalQuarter(new Date(2015, 8, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 0, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 5, 15))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 6, 12))",
				"BirtDateTime.fiscalQuarter(new Date(2015, 0, 11))",
		};

		int[] values = new int[]{
				1, 4, 2, 2, 3, 1
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalMonth</code>
	 */
	public void testFiscalMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.fiscalMonth(new Date(2015, 8, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 0, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalMonth(new Date(2015, 5, 15))",
				"BirtDateTime.fiscalMonth(new Date(2015, 6, 12))",
				"BirtDateTime.fiscalMonth(new Date(2015, 0, 11))",
		};

		int[] values = new int[]{
				3, 12, 4, 6, 7, 1
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalWeek</code>
	 */
	public void testFiscalWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.fiscalWeek(new Date(2015, 6, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2015, 9, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalWeek(new Date(2015, 0, 7))",
				"BirtDateTime.fiscalWeek(new Date(2015, 1, 1))",
		};

		int[] values = new int[]{
				3, 50, 3, 2, 6
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( String.valueOf( i ), values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FiscalDay</code>
	 */
	public void testFiscalDay( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.fiscalDay(new Date(2015, 6, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 6, 12), new Date(2015, 7, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 9, 11), new Date(2015, 9, 1 ))",
				"BirtDateTime.fiscalDay(new Date(2015, 0, 7))",
				"BirtDateTime.fiscalDay(new Date(2015, 1, 1))",
		};

		int[] values = new int[]{
				15, 346, 11, 7, 32
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalYear</code>
	 */
	public void testFirstDayOfFiscalYear( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfFiscalYear(2015)",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalYear(2015, new Date(2014, 6, 1))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 6, 15), new Date(2015, 6, 1 ))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15), new Date(2014, 6, 1 ))",
				"BirtDateTime.firstDayOfFiscalYear(new Date(2015, 5, 15), new Date(2015, 6, 1 ))",
		};

		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date[] values = new Date[]{
				date( c, 2015, 0, 1 ),
				date( c, 2015, 0, 1 ),
				date( c, 2015, 6, 1 ),
				date( c, 2015, 6, 1 ),
				date( c, 2014, 6, 1 ),
				date( c, 2014, 6, 1 ),
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( String.valueOf( i ), values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalMonth</code>
	 */
	public void testFirstDayOfFiscalMonth( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalMonth(2, new Date(2014, 6, 15))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 6, 15), new Date(2015, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2015, 6, 9), new Date(2014, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalMonth(new Date(2014, 0, 1), new Date(2015, 0, 10 ))",
		};

		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date[] values = new Date[]{
				date( c, 2015, 5, 1 ),
				date( c, 2015, 5, 1 ),
				date( c, 2014, 7, 15 ),
				date( c, 2015, 6, 10 ),
				date( c, 2015, 5, 10 ),
				date( c, 2013, 11, 10 )
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( String.valueOf( i ), values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalQuarter</code>
	 */
	public void testFirstDayOfFiscalQuarter( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalQuarter(3, new Date(2014, 6, 15))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 6, 15), new Date(2015, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2015, 6, 9), new Date(2014, 6, 10 ))",
				"BirtDateTime.firstDayOfFiscalQuarter(new Date(2014, 0, 1), new Date(2015, 1, 10 ))",
		};

		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date[] values = new Date[]{
				date( c, 2015, 3, 1 ),
				date( c, 2015, 3, 1 ),
				date( c, 2015, 0, 15 ),
				date( c, 2015, 6, 10 ),
				date( c, 2015, 3, 10 ),
				date( c, 2013, 10, 10 )
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( String.valueOf( i ), values[i], result );
		}
	}
	
	/**
	 * Test method for
	 * <code>org.eclipse.birt.core.script.function.bre.BirtDateTime.Function_FirstDayOfFiscalWeek</code>
	 */
	public void testFirstDayOfFiscalWeek( ) throws BirtException
	{
		String[] scripts = new String[]{
				"BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15))",
				"BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15), new Date(2015, 0, 1))",
				"BirtDateTime.firstDayOfFiscalWeek(5, new Date(2015, 6, 15))",
				"BirtDateTime.firstDayOfFiscalWeek(new Date(2015, 5, 15), new Date(2015, 6, 10 ))",
		};

		Calendar c = Calendar.getInstance( );
		c.clear( );
		Date[] values = new Date[]{
				date( c, 2015, 5, 14 ),
				date( c, 2015, 5, 14 ),
				date( c, 2015, 7, 9 ),
				date( c, 2015, 5, 14 ),
		};

		for ( int i = 0; i < values.length; i++ )
		{
			Object result = cx.evaluateString( scope,
					scripts[i],
					"inline",
					1,
					null );
			assertEquals( String.valueOf( i ), values[i], result );
		}
	}
	
	private Date date( Calendar c, int year, int month, int day )
	{
		c.set( year, month, day );
		return c.getTime( );
	}

}