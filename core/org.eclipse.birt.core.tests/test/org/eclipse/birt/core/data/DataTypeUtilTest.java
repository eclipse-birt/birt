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

package org.eclipse.birt.core.data;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.BaseScriptable;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Wrapper;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * 
 * Test case for DataTypeUtil
 */
public class DataTypeUtilTest extends TestCase
{

	public Object[] testObject;
	public Object[] resultInteger;
	public Object[] resultBigDecimal;
	public Object[] resultBoolean;
	public Object[] resultDate;
	public Object[] resultDouble;
	public Object[] resultString;
	
	// variables for testToAutoValue method
	public Object[] autoValueInputObject;
	public Object[] autoValueExpectedResult;
	
	// for test of toBigDecimal
	public Object[] testObjectDecimal;
	public Object[] resultObjectDecimal;
	
	// for test of toDouble
	public Object[] testObjectDouble;
	public Object[] resultObjectDouble;
	
	private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT, ULocale.getDefault( ));
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		// input Data
		testObject = new Object[]{
				new Integer( 1 ),
				new Integer( 0 ),
				BigDecimal.valueOf( Integer.MAX_VALUE ),
				BigDecimal.valueOf( Integer.MAX_VALUE + 1 ),
				BigDecimal.valueOf( Integer.MIN_VALUE ),
				BigDecimal.valueOf( Integer.MIN_VALUE - 1 ),
				BigDecimal.valueOf( 0l ),
				Boolean.valueOf( true ),
				Boolean.valueOf( false ),
				( new GregorianCalendar( 2004 + 1900, 1, 1 ) ).getTime( ),
				Double.valueOf( "1.1" ),
				Double.valueOf( "0" ),
				null,
				"testString",
				"12345",
				"10/11/2005",
				"10/11/2005 2:30 am",
				"10/11/2005 2:25:46 pm"};
		autoValueInputObject = new Object[]{
				"1",
				"0",
				String.valueOf( Integer.MAX_VALUE ),
				String.valueOf( Integer.MAX_VALUE + 1 ),
				String.valueOf( Integer.MIN_VALUE ),
				String.valueOf( Integer.MIN_VALUE - 1 ),
				String.valueOf( 0l ), "true",
				"false",
				(( new GregorianCalendar( 2004 + 1900, 1, 1 ) ).getTime( )).toString(),
				"1.1",
				"0",
				null,
				"1.00000000001",
				"testString",
				"12345",
				"10/11/2005",
				"10/11/2005 2:30 am",
				"10/11/2005 2:25:46 pm"};
		// the expected results of toDate()
		resultDate = new Object[]{
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				( new GregorianCalendar( 2004 + 1900, 1, 1 ) ).getTime( ),
				new Exception( "" ),
				new Exception( "" ),
				null,
				new Exception( "" ),
				new Exception( "" ),
				( new GregorianCalendar( 2005, 10 - 1, 11 ) ).getTime( ),
				( new GregorianCalendar( 2005, 10 - 1, 11, 2, 30 ) ).getTime( ),
				( new GregorianCalendar( 2005, 10 - 1, 11, 14, 25, 46 ) ).getTime( )};
		// the expected results of toInteger()
		resultInteger = new Object[]{
				new Integer( 1 ),
				new Integer( 0 ),
				new Integer( Integer.MAX_VALUE ),
				new Integer( Integer.MAX_VALUE + 1 ),
				new Integer( Integer.MIN_VALUE ),
				new Integer( Integer.MIN_VALUE - 1 ), 
				new Integer( 0 ),
				new Integer( 1 ), 
				new Integer( 0 ),
				new Integer( (int) ( (Date) resultDate[9] ).getTime( ) ),
				new Integer( (int) 1.1 ), 
				new Integer( 0 ),
				null,
				new Exception( "" ), 
				new Integer( "12345" ),
				new Exception( "" ), 
				new Exception( "" ), 
				new Exception( "" )};
		// the expected results of toBigDecimal()
		resultBigDecimal = new Object[]{
				new BigDecimal( "1" ),
				new BigDecimal( "0" ), 
				new BigDecimal( Integer.MAX_VALUE ),
				new BigDecimal( Integer.MAX_VALUE + 1 ),
				new BigDecimal( Integer.MIN_VALUE ),
				new BigDecimal( Integer.MIN_VALUE - 1 ), 
				new BigDecimal( 0 ),
				new BigDecimal( 1 ), 
				new BigDecimal( 0 ),
				new BigDecimal( ( (Date) resultDate[9] ).getTime( ) ),
				new BigDecimal( "1.1" ), 
				new BigDecimal( "0.0" ), 
				null,
				new Exception( "" ), 
				new BigDecimal( "12345" ),
				new Exception( "" ), 
				new Exception( "" ), 
				new Exception( "" )};
		// the expected results of toBoolean()
		resultBoolean = new Object[]{
				new Boolean( true ), 
				new Boolean( false ),
				new Boolean( true ), 
				new Boolean( true ), 
				new Boolean( true ),
				new Boolean( true ),
				new Boolean( false ),
				new Boolean( true ),
				new Boolean( false ),
				new Exception( "" ),
				new Boolean( true ),
				new Boolean( false ),
				null, 
				new Exception( "" ),
				new Boolean( true ), 
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" )};
		// the expected results of toDouble()
		resultDouble = new Object[]{
				new Double( 1 ), 
				new Double( 0 ),
				new Double( Integer.MAX_VALUE ),
				new Double( Integer.MAX_VALUE + 1 ),
				new Double( Integer.MIN_VALUE ),
				new Double( Integer.MIN_VALUE - 1 ),
				new Double( 0 ),
				new Double( 1 ),
				new Double( 0 ),
				new Double( ( (Date) resultDate[9] ).getTime( ) ),
				new Double( 1.1 ), new Double( 0 ),
				null,
				new Exception( "" ),
				new Double( "12345" ),
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" )};
		// the expected results of toString()
		resultString = new Object[]{"1", "0",
				String.valueOf( Integer.MAX_VALUE ),
				String.valueOf( Integer.MAX_VALUE + 1 ),
				String.valueOf( Integer.MIN_VALUE ),
				String.valueOf( Integer.MIN_VALUE - 1 ),
				"0",
				"true",
				"false",
				df.format( resultDate[9]), "1.1", "0.0",
				null,
				"testString",
				"12345",
				"10/11/2005",
				"10/11/2005 2:30 am",
				"10/11/2005 2:25:46 pm"};
		autoValueExpectedResult = new Object[]{
				new Integer( 1 ),
				new Integer( 0 ),
				new Integer( Integer.MAX_VALUE ),
				new Integer( Integer.MAX_VALUE + 1 ),
				new Integer( Integer.MIN_VALUE ),
				new Integer( Integer.MIN_VALUE - 1 ),
				new Integer( 0 ),
				"true",
				"false",
			    ((Date) resultDate[9] ).toString(),
				new Double( 1.1 ),
				new Integer( 0 ),
				null,
				new Integer(1),
				"testString",
				new Integer( "12345" ),
				( new GregorianCalendar( 2005, 10 - 1, 11 ) ).getTime( ),
				( new GregorianCalendar( 2005, 10 - 1, 11, 2, 30 ) ).getTime( ),
				( new GregorianCalendar( 2005, 10 - 1, 11, 14, 25, 46 ) ).getTime( ),
				};
		
		// for test of toBigDecimal
		testObjectDecimal = new Object[]{
				new Double( Double.NaN ),
				new Double( Double.POSITIVE_INFINITY ),
				new Double( Double.NEGATIVE_INFINITY ),
				new Double( Double.MAX_VALUE ),
				new Double( Double.MIN_VALUE )
		};
		resultObjectDecimal = new Object[]{
				new Exception( "" ),
				new Exception( "" ),
				new Exception( "" ),
				new BigDecimal( new Double( Double.MAX_VALUE ).toString( ) ),
				new BigDecimal( new Double( Double.MIN_VALUE ).toString( ) )
		};
		
		// for test of toDouble		
		testObjectDouble = new Object[]{
				new Float( 1 ),
		};
		resultObjectDouble = new Object[]{
				new Double( 1 ),
		};
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		testObject = null;
		super.tearDown( );
	}

	public void testToInteger( ) throws BirtException
	{
		Integer result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toInteger( testObject[i] );
				if ( resultInteger[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultInteger[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultInteger[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
		}
		
		assertEquals( DataTypeUtil.toInteger("1.8"),new Integer(1));
	}

	public void testToBigDecimal( )
	{
		BigDecimal result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toBigDecimal( testObject[i] );
				if ( resultBigDecimal[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultBigDecimal[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultBigDecimal[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
		}
		for ( int i = 0; i < testObjectDecimal.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toBigDecimal( testObjectDecimal[i] );
				if ( resultObjectDecimal[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultObjectDecimal[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultObjectDecimal[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
			catch ( Exception e )
			{
				fail( "Should throw BirtException." );
			}
		}
	}

	public void testToBoolean( )
	{
		Boolean result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toBoolean( testObject[i] );
				if ( resultBoolean[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultBoolean[i] );
			}
			catch ( BirtException dteEx )
			{
				if ( !( resultBoolean[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
		}
	}

	public void testToDate( )
	{
		Date result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toDate( testObject[i] );
				if ( resultDate[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultDate[i] );
				
			}
			catch ( BirtException e )
			{
				if ( !( resultDate[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
			
		}
	}
	
	public void testToDate1( )
	{
		String[] testStrings = {"1997",
				"1997-07",
				"1997-07-16",
				"1997-07-16T19:20+02",
				"1997-07-16T19:20:30+01:00",
				"1997-07-16T19:20:30.45+01:00",
				"1997-07-16 19:20+01:00",
				"1997-07-16 19:20:30+01:00",
				"1997-07-16 19:20:30.45+01:00"};
		Calendar calendar = Calendar.getInstance( TimeZone.getTimeZone("GMT+0"));
		
		Date[] resultDates = {
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		};
		calendar.clear();
		calendar.set(1997,0,1);
		resultDates[0] = calendar.getTime( );
		calendar.set(1997,6,1);
		resultDates[1] = calendar.getTime( );
		calendar.set(1997,6,16);
		resultDates[2] = calendar.getTime( );
		calendar.set(1997,6,16,17,20,0);
		resultDates[3] = calendar.getTime( );
		calendar.set(1997,6,16,18,20,30);
		resultDates[4] = calendar.getTime( );
		calendar.set(1997,6,16,18,20,30);
		calendar.set( Calendar.MILLISECOND, 450 );
		resultDates[5] = calendar.getTime( );
		calendar.set(1997,6,16,18,20,0);
		calendar.set( Calendar.MILLISECOND, 0 );
		resultDates[6] = calendar.getTime( );
		calendar.set(1997,6,16,18,20,30);
		resultDates[7] = calendar.getTime( );
		calendar.set(1997,6,16,18,20,30);
		calendar.set( Calendar.MILLISECOND, 450 );
		resultDates[8] = calendar.getTime( );
		
		for ( int i = 0; i < testStrings.length; i++ )
		{
			try
			{
				Date dateResult = DataTypeUtil.toDate( testStrings[i] );
//				System.out.println( "i:" + i );
//				System.out.println( dateResult );
//				System.out.println( resultDates[i] );
//				System.out.println();
//				System.out.println(dateResult.getTime( ));
//				System.out.println(resultDates[i].getTime( ));
				assertEquals( dateResult, resultDates[i] );
			}
			catch ( BirtException e )
			{
				fail( "Should not throw Exception." );
			}
			
		}
	}
    
    public void testToTime( ) throws BirtException
    {
        String timeValue = "11:15:38";
        Object obj = DataTypeUtil.convert( timeValue, java.sql.Time.class );
        assertEquals( timeValue, obj.toString() );
    }
    
    public void testToTimeFailure( ) 
    {
        String timeValue = "11:15";     // not a supported format
        boolean hasException = false;
        try
        {
        	// expects to throw BirtException, instead of runtime exception
            DataTypeUtil.convert( timeValue, java.sql.Time.class );
        }
        catch( BirtException e )
        {
            hasException = true;
        }
        assertTrue( hasException );
    }

	public void testToDouble( )
	{
		Double result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toDouble( testObject[i] );
				if ( resultDouble[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultDouble[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultDouble[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
		}
		for ( int i = 0; i < testObjectDouble.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toDouble( testObjectDouble[i] );
				if ( resultObjectDouble[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultObjectDouble[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultObjectDouble[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
			catch ( Exception e )
			{
				fail( "Should throw BirtException." );
			}
		}
	}

	/*
	 * Class under test for String toString(Object)
	 */
	public void testToStringObject( )
	{
		String result;
		for ( int i = 0; i < testObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toString( testObject[i] );
				if ( resultString[i] instanceof Exception )
					fail( "Should throw Exception." );
				assertEquals( result, resultString[i] );
			}
			catch ( BirtException e )
			{
				if ( !( resultString[i] instanceof Exception ) )
					fail( "Should not throw Exception." );
			}
		}
	}
	
	/**
	 * Test toDateWithCheck
	 *
	 */
	public void testToDateWithCheck( )
	{
		Locale locale;
		String dateStr;
		
		dateStr = "25/11/16";
		locale = Locale.UK;		
		try
		{
			DataTypeUtil.toDateWithCheck(dateStr, locale);
		}
		catch ( BirtException e )
		{
			fail("should not throw Exception");
		}
		
		dateStr = "25/13/16";
		locale = Locale.UK;		
		try
		{
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		}
		catch ( BirtException e )
		{
		}
		
		dateStr = "2005/11/11";
		locale = Locale.UK;		
		try
		{
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		}
		catch ( BirtException e )
		{
		}
		
		dateStr = "2005-11-11";
		locale = Locale.CHINA;		
		try
		{
			DataTypeUtil.toDateWithCheck(dateStr, locale);			
		}
		catch ( BirtException e )
		{
			fail("should throw Exception");
		}
		
		dateStr = "11-11-2005";
		locale = Locale.CHINA;		
		try
		{
			DataTypeUtil.toDateWithCheck(dateStr, locale);
			fail("should throw Exception");
		}
		catch ( BirtException e )
		{	
		}
		
	}
	
	/*
	 * this test is to test toAutoValue. it's also included testing
	 * toIntegerValue
	 */
	public void testToAutoValue( )
	{
		Object result;
		for ( int i = 0; i < autoValueInputObject.length; i++ )
		{
			try
			{
				result = DataTypeUtil.toAutoValue( autoValueInputObject[i] );
				assertEquals( result, autoValueExpectedResult[i] );
			}
			catch ( Exception e )
			{
				fail( "Should not throw Exception." );// e.printStackTrace( );
			}
		}

	}

	/**
	 * test convertion between String and Date  
	 */
	public void testToStringAndDate( )
	{
		//the follow objects represent the same date Jan 25th, 1998
		//the same object use toString() and toDate() several times it won't bring any error
		Date date = new GregorianCalendar( 1998, 1 - 1, 25 ).getTime( );
		String str = "1/25/1998"; 
		try
		{
			assertEquals( DataTypeUtil.toDate( str ), date );

			assertEquals( DataTypeUtil.toString( DataTypeUtil.toDate( str ) ),
					DataTypeUtil.toString( date ) );
		}
		catch ( BirtException e )
		{
			fail( "Should not throw Exception." );
		}
	}
	
	/**
	 * Test DataTypeUtil#convert( Object source, Class toTypeClass )
	 * @throws BirtException 
	 *
	 */
	public void testConvert( ) throws BirtException
	{
		java.sql.Date date = java.sql.Date.valueOf( "2006-1-1" );
		Object ob = DataTypeUtil.convert( date, java.sql.Date.class );
		assertEquals( date, ob );
	}

	/**
	 * 
	 * @throws BirtException
	 */
	public void testConvert2( ) throws BirtException
	{
		WrappedObject obj = new WrappedObject( );
		// Any type
		Object ob = DataTypeUtil.convert( obj, 0 );
		assertEquals( "I am an unwrapped object", ob );
	}

    /**
     * Test DataTypeUtil#toApiDataType( int odaDataTypeCode )
     * @throws BirtException
     */
    public void testToApiFromOdaDataType( ) throws BirtException
    {
        assertEquals( DataType.STRING_TYPE, 
                DataTypeUtil.toApiDataType( Types.CHAR ) );
        assertEquals( DataType.INTEGER_TYPE, 
                DataTypeUtil.toApiDataType( Types.INTEGER ) );
        assertEquals( DataType.DOUBLE_TYPE, 
                DataTypeUtil.toApiDataType( Types.DOUBLE ) );
        assertEquals( DataType.DECIMAL_TYPE, 
                DataTypeUtil.toApiDataType( Types.DECIMAL ) );
        assertEquals( DataType.DATE_TYPE, 
                DataTypeUtil.toApiDataType( Types.DATE ) );
        assertEquals( DataType.DATE_TYPE, 
                DataTypeUtil.toApiDataType( Types.TIME ) );
        assertEquals( DataType.DATE_TYPE, 
                DataTypeUtil.toApiDataType( Types.TIMESTAMP ) );
        assertEquals( DataType.BINARY_TYPE, 
                DataTypeUtil.toApiDataType( Types.BLOB ) );
        assertEquals( DataType.STRING_TYPE, 
                DataTypeUtil.toApiDataType( Types.CLOB ) );
        assertEquals( DataType.UNKNOWN_TYPE, 
                DataTypeUtil.toApiDataType( Types.NULL ) );
        
        // test invalid ODA data type code
        boolean hasException = false;
        try
        {
            DataTypeUtil.toApiDataType( Types.OTHER );
        }
        catch( BirtException e )
        {
            hasException = true;
        }
        assertTrue( hasException );

    }
    
    /**
	 * A wrapped scriptable object for test
	 * 
	 */
	class WrappedObject extends BaseScriptable implements Wrapper
	{
		WrappedObject( )
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.Wrapper#unwrap()
		 */
		public Object unwrap( )
		{
			return "I am an unwrapped object";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String name, Scriptable start )
		{
			return "I am an unwrapped object";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.Scriptable#getClassName()
		 */
		public String getClassName( )
		{
			return "WrappedObject";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.Scriptable#has(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public boolean has( String name, Scriptable start )
		{
			return true;
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.Scriptable#put(java.lang.String, org.mozilla.javascript.Scriptable, java.lang.Object)
		 */
		public void put( String name, Scriptable start, Object value )
		{
			// do nothing
		}
	}

}