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

package org.eclipse.birt.core.script;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import junit.framework.TestCase;

/**
 * @Created on Dec 27, 2004
 * 
 * NativeDateTimeSpanTest Class
 * 
 * This class is the unit test for class NativeDateTimeSpan.
 */
public class NativeDateTimeSpanTest extends TestCase
{

	/**
	 * Create a Context instance
	 */
	Context cx;
	/**
	 * Create a Scriptable instance
	 */
	Scriptable scope;
	/**
	 * Record whether there exists an error
	 */
	boolean exceptionValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void setUp() throws Exception
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
	@After
    public void tearDown()
	{
		Context.exit( );
	}

	/**
	 * Evaluate a JavaScript source string.
	 * 
	 * @param script
	 * @return the result
	 */
	protected Object eval( String script )
	{
		Object result = null;
		try
		{
			result = cx.evaluateString( scope, script, "inline", 1, null );
		}
		catch ( Exception ex )
		{
			exceptionValue = true;
		}
		return result;
	}
	
	protected Calendar getCalendarInstance( )
	{
		Calendar cal = Calendar.getInstance( );
		cal.clear( );
		return cal;
	}
	@Test
    public void testYears( )
	{
		/*
		 * Just one day off a whole year
		 */
		String script2 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"2/28/09 8:01 PM\" ;DateTimeSpan.years(startDate,endDate);";
		Object value2 = eval( script2 );
		assertEquals( 1, ( (Integer) value2 ).intValue( ) );
		

		/*
		 * A whole year
		 */
		String script1 = "var startDate=\"2/3/08 8:01 PM\" ;var endDate=\"9/13/09 8:01 PM\" ;DateTimeSpan.years(startDate,endDate);";
		Object value1 = eval( script1 );
		assertEquals( 1, ( (Integer) value1 ).intValue( ) );
		
		/*
		 * A whole year within a leap year
		 */
		String script3 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"3/1/09 8:01 PM\" ;DateTimeSpan.years(startDate,endDate);";
		Object value3 = eval( script3 );
		assertEquals( 1, ( (Integer) value3 ).intValue( ) );
		/*
		 * Many years, include three leap years
		 */
		String script4 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"2/29/16 8:01 PM\" ;DateTimeSpan.years(startDate,endDate);";
		Object value4 = eval( script4 );
		assertEquals( 8, ( (Integer) value4 ).intValue( ) );
		/*
		 * Random dates
		 */
		String script5 = "var startDate=\"9/5/08 8:01 PM\" ;var endDate=\"4/30/10 8:01 PM\" ;DateTimeSpan.years(startDate,endDate);";
		Object value5 = eval( script5 );
		assertEquals( 1, ( (Integer) value5 ).intValue( ) );

	}
	@Test
    public void testMonths( )
	{
		/*
		 * Just one day off twelve months.
		 */
		String script2 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"2/28/09 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value2 = eval( script2 );
		assertEquals( 12, ( (Integer) value2 ).intValue( ) );
		/*
		 * Just one minute off twelve months.
		 */
		String script11 = "var startDate=\"3/12/08 8:01 PM\" ;var endDate=\"3/12/09 8:00 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value11 = eval( script11 );
		assertEquals( 11, ( (Integer) value11 ).intValue( ) );

		/*
		 * Just a whole years, twelve months, a leap year
		 */
		String script3 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"3/1/09 8:02 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value3 = eval( script3 );
		assertEquals( 12, ( (Integer) value3 ).intValue( ) );
		String script9 = "var startDate=\"1/5/08 8:01 PM\" ;var endDate=\"1/5/09 8:05 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value9 = eval( script9 );
		if ( value9 instanceof Integer )
			assertEquals( 12, ( (Integer) value9 ).intValue( ) );

		/*
		 * Many months within two different years
		 */
		String script1 = "var startDate=\"2/3/08 8:01 PM\" ;var endDate=\"9/13/09 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value1 = eval( script1 );
		assertEquals( 19, ( (Integer) value1 ).intValue( ) );
		String script4 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"2/29/16 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value4 = eval( script4 );
		assertEquals( 96, ( (Integer) value4 ).intValue( ) );
		/*
		 * In two diffent years, and the first month is less than the second
		 * month
		 */
		String script5 = "var startDate=\"1/5/08 8:01 PM\" ;var endDate=\"4/30/10 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value5 = eval( script5 );
		assertEquals( 27, ( (Integer) value5 ).intValue( ) );
		/*
		 * In two diffent years, and the first month is more than the second
		 * month
		 */
		String script6 = "var startDate=\"9/5/08 8:01 PM\" ;var endDate=\"4/30/09 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value6 = eval( script6 );
		assertEquals( 7, ( (Integer) value6 ).intValue( ) );
		String script7 = "var startDate=\"9/5/08 8:01 PM\" ;var endDate=\"4/30/10 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value7 = eval( script7 );
		assertEquals( 19, ( (Integer) value7 ).intValue( ) );
		/*
		 * In the same year, and the first month is less than the second one
		 */
		String script8 = "var startDate=\"1/5/08 8:01 PM\" ;var endDate=\"4/30/08 8:01 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value8 = eval( script8 );
		assertEquals( 3, ( (Integer) value8 ).intValue( ) );

		/*
		 * Less than one day
		 */
		String script10 = "var startDate=\"3/12/08 8:01 PM\" ;var endDate=\"3/13/08 8:05 PM\" ;DateTimeSpan.months(startDate,endDate);";
		Object value10 = eval( script10 );
		assertEquals( 0, ( (Integer) value10 ).intValue( ) );

	}
	@Test
    public void testDays( )
	{
		/*
		 * Just a whole day.
		 */
		String script1 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"1/3/04 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value1 = eval( script1 );
		if ( value1 instanceof Integer )
			assertEquals( 1, ( (Integer) value1 ).intValue( ) );
		/*
		 * Just an hour off a day.
		 */
		String script2 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"1/3/04 7:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value2 = eval( script2 );
		if ( value2 instanceof Integer )
			assertEquals( 0, ( (Integer) value2 ).intValue( ) );
		/*
		 * Just one minute over a day, two days
		 */
		String script3 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"1/3/04 8:02 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value3 = eval( script3 );
		if ( value3 instanceof Integer )
			assertEquals( 1, ( (Integer) value3 ).intValue( ) );
		String script5 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"1/4/04 8:02 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value5 = eval( script5 );
		if ( value5 instanceof Integer )
			assertEquals( 2, ( (Integer) value5 ).intValue( ) );
		/*
		 * The second date is only a minute more than the first one, in the same
		 * day.
		 */
		String script4 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"1/2/04 8:02 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value4 = eval( script4 );
		if ( value4 instanceof Integer )
			assertEquals( 0, ( (Integer) value4 ).intValue( ) );

		/*
		 * Though a whole year.
		 */
		String script6 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"2/4/05 8:02 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value6 = eval( script6 );
		if ( value6 instanceof Integer )
			assertEquals( 399, ( (Integer) value6 ).intValue( ) );
		String script7 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"2/4/05 8:00 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value7 = eval( script7 );
		if ( value7 instanceof Integer )
			assertEquals( 398, ( (Integer) value7 ).intValue( ) );
		String script8 = "var startDate=\"1/2/04 8:01 PM\" ;var endDate=\"3/1/05 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value8 = eval( script8 );
		if ( value8 instanceof Integer )
			assertEquals( 424, ( (Integer) value8 ).intValue( ) );
		String script9 = "var startDate=\"2/2/04 8:01 PM\" ;var endDate=\"1/1/05 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value9 = eval( script9 );
		if ( value9 instanceof Integer )
			assertEquals( 334, ( (Integer) value9 ).intValue( ) );
		String script10 = "var startDate=\"1/1/04 8:01 PM\" ;var endDate=\"1/1/05 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value10 = eval( script10 );
		if ( value10 instanceof Integer )
			assertEquals( 366, ( (Integer) value10 ).intValue( ) );
		String script11 = "var startDate=\"1/1/04 8:01 PM\" ;var endDate=\"1/3/09 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value11 = eval( script11 );
		if ( value11 instanceof Integer )
			assertEquals( 1829, ( (Integer) value11 ).intValue( ) );
		/*
		 * The first day is more than the second one
		 */
		String script12 = "var startDate=\"2/4/09 0:00 PM\" ;var endDate=\"2/2/10 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value12 = eval( script12 );
		if ( value12 instanceof Integer )
			assertEquals( 363, ( (Integer) value12 ).intValue( ) );
		/*
		 * The first month is more than the second one.
		 */
		String script13 = "var startDate=\"3/5/09 0:00 PM\" ;var endDate=\"2/1/10 8:01 PM\" ;DateTimeSpan.days(startDate,endDate);";
		Object value13 = eval( script13 );
		if ( value13 instanceof Integer )
			assertEquals( 333, ( (Integer) value13 ).intValue( ) );

	}
	@Test
    public void testHours( )
	{
		/*
		 * A whole day in the normal year
		 */
		String script1 = "var startDate=\"2/3/09 8:01 PM\" ;var endDate=\"2/4/09 8:01 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value1 = eval( script1 );
		if ( value1 instanceof Integer )
			assertEquals( 24, ( (Integer) value1 ).intValue( ) );
		/*
		 * A whole day in a leap year
		 */
		String script3 = "var startDate=\"2/29/08 8:01 PM\" ;var endDate=\"3/1/08 8:01 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value3 = eval( script3 );
		if ( value3 instanceof Integer )
			assertEquals( 24, ( (Integer) value3 ).intValue( ) );
		/*
		 * In the same day, less than an hour, equal to an hour, more than an
		 * hour
		 */
		String script2 = "var startDate=\"3/1/08 8:01 PM\" ;var endDate=\"3/1/08 9:02 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value2 = eval( script2 );
		if ( value2 instanceof Integer )
			assertEquals( 1, ( (Integer) value2 ).intValue( ) );
		String script4 = "var startDate=\"2/2/08 8:01 PM\" ;var endDate=\"2/2/08 9:00 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value4 = eval( script4 );
		if ( value4 instanceof Integer )
			assertEquals( 0, ( (Integer) value4 ).intValue( ) );
		String script5 = "var startDate=\"2/2/08 8:01 PM\" ;var endDate=\"2/2/08 9:02 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value5 = eval( script5 );
		if ( value5 instanceof Integer )
			assertEquals( 1, ( (Integer) value5 ).intValue( ) );

		/*
		 * More than one day.
		 */
		String script6 = "var startDate=\"2/2/08 8:01 PM\" ;var endDate=\"2/3/08 9:02 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value6 = eval( script6 );
		if ( value6 instanceof Integer )
			assertEquals( 25, ( (Integer) value6 ).intValue( ) );
		/*
		 * Just one minute off a whole day.
		 */
		String script7 = "var startDate=\"2/2/08 8:03 PM\" ;var endDate=\"2/3/08 8:02 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value7 = eval( script7 );
		if ( value7 instanceof Integer )
			assertEquals( 23, ( (Integer) value7 ).intValue( ) );
		/*
		 * More than one year, a normal year.
		 */
		String script8 = "var startDate=\"2/2/09 8:01 PM\" ;var endDate=\"2/2/10 8:01 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value8 = eval( script8 );
		if ( value8 instanceof Integer )
			assertEquals( 8760, ( (Integer) value8 ).intValue( ) );
		/*
		 * More than one yare, a leap year.
		 */
		String script9 = "var startDate=\"2/2/08 8:01 PM\" ;var endDate=\"2/3/09 8:00 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value9 = eval( script9 );
		if ( value9 instanceof Integer )
			assertEquals( 8807, ( (Integer) value9 ).intValue( ) );
		/*
		 * The first hour is more than the second one.
		 */
		String script10 = "var startDate=\"2/2/09 9:01 PM\" ;var endDate=\"2/3/09 8:00 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value10 = eval( script10 );
		if ( value10 instanceof Integer )
			assertEquals( 22, ( (Integer) value10 ).intValue( ) );
		/*
		 * The first hour is less than the second one.
		 */
		String script11 = "var startDate=\"2/2/09 3:01 PM\" ;var endDate=\"2/3/09 8:00 PM\" ;DateTimeSpan.hours(startDate,endDate);";
		Object value11 = eval( script11 );
		if ( value11 instanceof Integer )
			assertEquals( 28, ( (Integer) value11 ).intValue( ) );

		/*
		 * Use Date instance to test this method
		 */
		Calendar dateCal1 = getCalendarInstance( );
		dateCal1.set( 2008, 8, 13, 20, 1, 44 );
        
		Date date1 = dateCal1.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );

		dateCal1 = getCalendarInstance( );
		dateCal1.set( 2008, 8, 13, 21, 1, 44 );
		Date date2 = dateCal1.getTime( );
		Object jsNumber2 = Context.javaToJS( date2, scope );
		ScriptableObject.putProperty( scope, "date2", jsNumber2 );
		String script12 = "DateTimeSpan.hours(date1,date2)";
		Object value12 = eval( script12 );
		if ( value12 instanceof Integer )
		{
			assertEquals( 1, ( (Integer) value12 ).intValue( ) );
		}

	}
	@Test
    public void testMinutes( )
	{
		/*
		 * Just a whole hour, 60 minutes
		 */
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 21, 1, 44 );
		Date date2 = dateCal.getTime( );
		Object jsNumber2 = Context.javaToJS( date2, scope );
		ScriptableObject.putProperty( scope, "date2", jsNumber2 );
		String script1 = "DateTimeSpan.minutes(date1,date2)";
		Object value1 = eval( script1 );
		if ( value1 instanceof Integer )
			assertEquals( 60, ( (Integer) value1 ).intValue( ) );

		/*
		 * Just one second off an hour
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 45 );
		Date date3 = dateCal.getTime( );
		Object jsNumber3 = Context.javaToJS( date3, scope );
		ScriptableObject.putProperty( scope, "date3", jsNumber3 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 21, 1, 44 );
		Date date4 = dateCal.getTime( );
		Object jsNumber4 = Context.javaToJS( date4, scope );
		ScriptableObject.putProperty( scope, "date4", jsNumber4 );
		String script2 = "DateTimeSpan.minutes(date3,date4)";
		Object value2 = eval( script2 );
		if ( value2 instanceof Integer )
			assertEquals( 59, ( (Integer) value2 ).intValue( ) );

		/*
		 * Just one second over an hour
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 45 );
		Date date5 = dateCal.getTime( );
		Object jsNumber5 = Context.javaToJS( date5, scope );
		ScriptableObject.putProperty( scope, "date5", jsNumber5 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 21, 2, 45 );
		Date date6 = dateCal.getTime( );
		Object jsNumber6 = Context.javaToJS( date6, scope );
		ScriptableObject.putProperty( scope, "date6", jsNumber6 );
		String script3 = "DateTimeSpan.minutes(date5,date6)";
		Object value3 = eval( script3 );
		if ( value3 instanceof Integer )
			assertEquals( 61, ( (Integer) value3 ).intValue( ) );

		/*
		 * In leap year
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 20, 1, 45 );
		Date date7 = dateCal.getTime( );
		Object jsNumber7 = Context.javaToJS( date7, scope );
		ScriptableObject.putProperty( scope, "date7", jsNumber7 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 2, 1, 20, 2, 45 );
		Date date8 = dateCal.getTime( );
		Object jsNumber8 = Context.javaToJS( date8, scope );
		ScriptableObject.putProperty( scope, "date8", jsNumber8 );
		String script4 = "DateTimeSpan.minutes(date7,date8)";
		Object value4 = eval( script4 );
		if ( value4 instanceof Integer )
			assertEquals( 2881, ( (Integer) value4 ).intValue( ) );

		/*
		 * In the same hour, and the first minute is more than the second one.
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 20, 4, 45 );
		Date date9 = dateCal.getTime( );
		Object jsNumber9 = Context.javaToJS( date9, scope );
		ScriptableObject.putProperty( scope, "date9", jsNumber9 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 21, 2, 45 );
		Date date10 = dateCal.getTime( );
		Object jsNumber10 = Context.javaToJS( date10, scope );
		ScriptableObject.putProperty( scope, "date10", jsNumber10 );
		String script5 = "DateTimeSpan.minutes(date9,date10)";
		Object value5 = eval( script5 );
		if ( value5 instanceof Integer )
			assertEquals( 58, ( (Integer) value5 ).intValue( ) );
		/*
		 * In the same hour, and the first hour is more than the second one.
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2007, 12, 31, 20, 4, 45 );
		Date date11 = dateCal.getTime( );
		Object jsNumber11 = Context.javaToJS( date11, scope );
		ScriptableObject.putProperty( scope, "date11", jsNumber11 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 1, 16, 2, 45 );
		Date date12 = dateCal.getTime( );
		Object jsNumber12 = Context.javaToJS( date12, scope );
		ScriptableObject.putProperty( scope, "date12", jsNumber12 );
		String script6 = "DateTimeSpan.minutes(date11,date12)";
		Object value6 = eval( script6 );
		if ( value6 instanceof Integer )
			assertEquals( 1198, ( (Integer) value6 ).intValue( ) );

	}
	@Test
    public void testSeconds( )
	{
		/*
		 * Just one second.
		 */
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 45 );
		Date date2 = dateCal.getTime( );
		Object jsNumber2 = Context.javaToJS( date2, scope );
		ScriptableObject.putProperty( scope, "date2", jsNumber2 );
		String script1 = "DateTimeSpan.seconds(date1,date2)";
		Object value1 = eval( script1 );
		if ( value1 instanceof Integer )
			assertEquals( 1, ( (Integer) value1 ).intValue( ) );

		/*
		 * Just one second off one minute
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 45 );
		Date date3 = dateCal.getTime( );
		Object jsNumber3 = Context.javaToJS( date3, scope );
		ScriptableObject.putProperty( scope, "date3", jsNumber3 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 2, 44 );
		Date date4 = dateCal.getTime( );
		Object jsNumber4 = Context.javaToJS( date4, scope );
		ScriptableObject.putProperty( scope, "date4", jsNumber4 );
		String script2 = "DateTimeSpan.seconds(date3,date4)";
		Object value2 = eval( script2 );
		if ( value2 instanceof Integer )
			assertEquals( 59, ( (Integer) value2 ).intValue( ) );

		/*
		 * Just one second over one minute
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 45 );
		Date date5 = dateCal.getTime( );
		Object jsNumber5 = Context.javaToJS( date5, scope );
		ScriptableObject.putProperty( scope, "date5", jsNumber5 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 2, 46 );
		Date date6 = dateCal.getTime( );
		Object jsNumber6 = Context.javaToJS( date6, scope );
		ScriptableObject.putProperty( scope, "date6", jsNumber6 );
		String script3 = "DateTimeSpan.seconds(date5,date6)";
		Object value3 = eval( script3 );
		if ( value3 instanceof Integer )
			assertEquals( 61, ( (Integer) value3 ).intValue( ) );

		/*
		 * In leap year
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 20, 1, 45 );
		Date date7 = dateCal.getTime( );
		Object jsNumber7 = Context.javaToJS( date7, scope );
		ScriptableObject.putProperty( scope, "date7", jsNumber7 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 2, 1, 20, 1, 45 );
		Date date8 = dateCal.getTime( );
		Object jsNumber8 = Context.javaToJS( date8, scope );
		ScriptableObject.putProperty( scope, "date8", jsNumber8 );
		String script4 = "DateTimeSpan.seconds(date7,date8)";
		Object value4 = eval( script4 );
		if ( value4 instanceof Integer )
			assertEquals( 172800, ( (Integer) value4 ).intValue( ) );

		/*
		 * In the same hour, and the first second is more than the second one.
		 */
		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 20, 6, 45 );
		Date date9 = dateCal.getTime( );
		Object jsNumber9 = Context.javaToJS( date9, scope );
		ScriptableObject.putProperty( scope, "date9", jsNumber9 );

		dateCal = getCalendarInstance( );
		dateCal.set( 2008, 1, 28, 21, 5, 40 );
		Date date10 = dateCal.getTime( );
		Object jsNumber10 = Context.javaToJS( date10, scope );
		ScriptableObject.putProperty( scope, "date10", jsNumber10 );
		String script5 = "DateTimeSpan.seconds(date9,date10)";
		Object value5 = eval( script5 );
		if ( value5 instanceof Integer )
			assertEquals( 3535, ( (Integer) value5 ).intValue( ) );
	}
	@Test
    public void testAddDate( )
	{
		/*
		 * Add a single year
		 */
		String script1 = "var startDate=\"2/3/08 8:01 PM\" ;var years=1;var months=0;var days=0;"
				+ "DateTimeSpan.addDate(startDate,years,months,days);";
		Object value1 = eval( script1 );
		Calendar cal = getCalendarInstance( );
		cal.setTime( (Date) value1 );
		assertEquals( 2009, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 3, cal.get( Calendar.DATE ) );
		System.out.println( "1" );
		
		/*
		 * Use Date instance to test this method.
		 */
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );
		String script2 = "DateTimeSpan.addDate(date1,1,0,0);";
		Object value2 = eval( script2 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value2 );
		assertEquals( 2009, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		System.out.println( "2" );
		
		/*
		 * Add one year to the startDate, and since 2008 is leap year, so the
		 * result date is 3/1/09.
		 */
		String script3 = "var startDate=\"2/29/08 8:01 PM\" ;var years=1;var months=0;var days=0;"
				+ "DateTimeSpan.addDate(startDate,years,months,days);";
		Object value3 = eval( script3 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value3 );
		assertEquals( 2009, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 28, cal.get( Calendar.DATE ) );
		System.out.println( "3" );
		
		/*
		 * Adding one month to Jan.31 would produce the invalid date Feb.31. The
		 * method adjusts the date to be valid, in this case, if the year is not
		 * a leap year, then Feb. has 28 days and the resulting date would be
		 * Mar.3.
		 */
		String script4 = "var startDate=\"1/31/07 8:01 PM\" ;var years=0;var months=1;var days=0;"
				+ "DateTimeSpan.addDate(startDate,years,months,days);";
		Object value4 = eval( script4 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value4 );
		assertEquals( 2007, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 28, cal.get( Calendar.DATE ) );
		System.out.println( "4" );
		

		dateCal.set( 2004, 11, 31, 20, 1, 44 );
		Date date2 = dateCal.getTime( );
		Object jsNumber2 = Context.javaToJS( date2, scope );
		ScriptableObject.putProperty( scope, "date2", jsNumber2 );
		String script5 = "DateTimeSpan.addDate(date2,1,0,0);";
		String script6 = "DateTimeSpan.addDate(date2,0,1,0);";
		String script7 = "DateTimeSpan.addDate(date2,0,0,1);";
		String script8 = "DateTimeSpan.addDate(date2,1,1,1);";
		Object value5 = eval( script5 );
		Object value6 = eval( script6 );
		Object value7 = eval( script7 );
		Object value8 = eval( script8 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value5 );
		assertEquals( 2005, cal.get( Calendar.YEAR ) );
		assertEquals( 11, cal.get( Calendar.MONTH ) );
		assertEquals( 31, cal.get( Calendar.DATE ) );
		System.out.println( "5" );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value6 );
		assertEquals( 2005, cal.get( Calendar.YEAR ) );
		assertEquals( 0, cal.get( Calendar.MONTH ) );
		assertEquals( 31, cal.get( Calendar.DATE ) );
		System.out.println( "6" );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value7 );
		assertEquals( 2005, cal.get( Calendar.YEAR ) );
		assertEquals( 0, cal.get( Calendar.MONTH ) );
		assertEquals( 1, cal.get( Calendar.DATE ) );
		System.out.println( "7" );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value8 );
		assertEquals( 2006, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 1, cal.get( Calendar.DATE ) );
		System.out.println( "8" );
		
		/*
		 * Add an negtive argument.
		 */
		String script9 = "var startDate=\"1/31/07 8:01 PM\" ;var years=-1;var months=-1;var days=0;"
				+ "DateTimeSpan.addDate(startDate,years,months,days);";
		Object value9 = eval( script9 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value9 );
		assertEquals( 2005, cal.get( Calendar.YEAR ) );
		assertEquals( 11, cal.get( Calendar.MONTH ) );
		assertEquals( 31, cal.get( Calendar.DATE ) );
		System.out.println( "9" );
	}
	@Test
    public void testAddTime( )
	{
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );
		String script1 = "DateTimeSpan.addTime(date1,1,1,1);";
		Object value1 = eval( script1 );
		Calendar cal = getCalendarInstance( );
		cal.setTime( (Date) value1 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 21, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 2, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "1" );
		
		/*
		 * More than 24 hours
		 */
		String script2 = "DateTimeSpan.addTime(date1,25,1,1);";
		Object value2 = eval( script2 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value2 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 14, cal.get( Calendar.DATE ) );
		assertEquals( 21, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 2, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "2" );
		
		/*
		 * More than 60 minutes, 60 seconds
		 */
		String script3 = "DateTimeSpan.addTime(date1,0,61,61);";
		Object value3 = eval( script3 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value3 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 21, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 3, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "3" );
		
		String script4 = "DateTimeSpan.addTime(date1,4,61,61);";
		Object value4 = eval( script4 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value4 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 14, cal.get( Calendar.DATE ) );
		assertEquals( 1, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 3, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "4" );
		
		/*
		 * Add negtive arguments.
		 */
		String script5 = "DateTimeSpan.addTime(date1,-1,-1,-1);";
		Object value5 = eval( script5 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value5 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 19, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 0, cal.get( Calendar.MINUTE ) );
		assertEquals( 43, cal.get( Calendar.SECOND ) );
		System.out.println( "5" );
		
		String script6 = "DateTimeSpan.addTime(date1,-1,-1,-61);";
		Object value6 = eval( script6 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value6 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 18, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 59, cal.get( Calendar.MINUTE ) );
		assertEquals( 43, cal.get( Calendar.SECOND ) );
		System.out.println( "6" );
	}
	@Test
    public void testSubDate( )
	{
		/*
		 * Add a single year
		 */
		String script1 = "var startDate=\"2/3/08 8:01 PM\" ;var years=1;var months=0;var days=0;"
				+ "DateTimeSpan.subDate(startDate,years,months,days);";
		Object value1 = eval( script1 );
		Calendar cal = getCalendarInstance( );
		cal.setTime( (Date) value1 );
		assertEquals( 2007, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 3, cal.get( Calendar.DATE ) );
		System.out.println( "1" );
		

		/*
		 * Use Date instance to test this method.
		 */
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );
		String script2 = "DateTimeSpan.subDate(date1,1,0,0);";
		Object value2 = eval( script2 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value2 );
		assertEquals( 2007, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		System.out.println( "2" );
		
		/*
		 * Add one year to the startDate, and since 2008 is leap year, so the
		 * result date is 3/1/09.
		 */
		String script3 = "var startDate=\"2/29/08 8:01 PM\" ;var years=1;var months=0;var days=0;"
				+ "DateTimeSpan.subDate(startDate,years,months,days);";
		Object value3 = eval( script3 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value3 );
		assertEquals( 2007, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 28, cal.get( Calendar.DATE ) );
		System.out.println( "3" );
		
		/*
		 * Adding one month to Jan.31 would produce the invalid date Feb.31. The
		 * method adjusts the date to be valid, in this case, if the year is not
		 * a leap year, then Feb. has 28 days and the resulting date would be
		 * Mar.3.
		 */
		String script4 = "var startDate=\"3/30/07 8:01 PM\" ;var years=0;var months=1;var days=3;"
				+ "DateTimeSpan.subDate(startDate,years,months,days);";
		Object value4 = eval( script4 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value4 );
		assertEquals( 2007, cal.get( Calendar.YEAR ) );
		assertEquals( 1, cal.get( Calendar.MONTH ) );
		assertEquals( 25, cal.get( Calendar.DATE ) );
		System.out.println( "4" );
		
	}
	@Test
    public void testSubTime( )
	{
		Calendar dateCal = getCalendarInstance( );
		dateCal.set( 2008, 8, 13, 20, 1, 44 );
		Date date1 = dateCal.getTime( );
		Object jsNumber1 = Context.javaToJS( date1, scope );
		ScriptableObject.putProperty( scope, "date1", jsNumber1 );
		String script1 = "DateTimeSpan.subTime(date1,-1,-1,-1);";
		Object value1 = eval( script1 );
		Calendar cal = getCalendarInstance( );
		cal.setTime( (Date) value1 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 21, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 2, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "1" );
		

		/*
		 * More than 24 hours
		 */
		String script2 = "DateTimeSpan.subTime(date1,-25,-1,-1);";
		Object value2 = eval( script2 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value2 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 14, cal.get( Calendar.DATE ) );
		assertEquals( 21, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 2, cal.get( Calendar.MINUTE ) );
		assertEquals( 45, cal.get( Calendar.SECOND ) );
		System.out.println( "2" );
		
		/*
		 * Add negtive arguments.
		 */
		String script5 = "DateTimeSpan.subTime(date1,1,1,1);";
		Object value5 = eval( script5 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value5 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 19, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 0, cal.get( Calendar.MINUTE ) );
		assertEquals( 43, cal.get( Calendar.SECOND ) );
		System.out.println( "5" );
		
		String script6 = "DateTimeSpan.subTime(date1,1,1,61);";
		Object value6 = eval( script6 );
		cal = getCalendarInstance( );
		cal.setTime( (Date) value6 );
		assertEquals( 2008, cal.get( Calendar.YEAR ) );
		assertEquals( 8, cal.get( Calendar.MONTH ) );
		assertEquals( 13, cal.get( Calendar.DATE ) );
		assertEquals( 18, cal.get( Calendar.HOUR_OF_DAY ) );
		assertEquals( 59, cal.get( Calendar.MINUTE ) );
		assertEquals( 43, cal.get( Calendar.SECOND ) );
		System.out.println( "6" );
		
	}
}
