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

package org.eclipse.birt.core.data;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 * A utility function The convert method converts the source object, which can
 * be any supported data type, into an object given specified type. If no
 * reasonable conversion can be made, throw a BirtException.
 */
public final class DataTypeUtil
{
	// Defalult Locale, if we have any problem parse string to date for Locale.getDefault()
	// we will try to parse it for Locale.US
	private static Locale DEFAULT_LOCALE = Locale.US;

	// Default Date/Time Style 
	private static int DEFAULT_DATE_STYLE =DateFormat.MEDIUM;

	// resource bundle for exception messages 
	public static ResourceBundle resourceBundle = ( new ResourceHandle( Locale
			.getDefault( ) ) ).getResourceBundle( );

	/**
	 * convert an object to given type
	 * Types supported:
	 * 	 	DataType.INTEGER_TYPE
	 * 		DataType.DECIMAL_TYPE
	 * 		DataType.BOOLEAN_TYPE
	 * 		DataType.DATE_TYPE
	 * 		DataType.DOUBLE_TYPE
	 * 		DataType.STRING_TYPE
	 * 		DataType.BLOB_TYPE
	 * @param source
	 * @param toType
	 * @return
	 * @throws BirtException
	 */
	public static Object convert( Object source, int toType )
			throws BirtException
	{
		switch ( toType )
		{
			case DataType.INTEGER_TYPE :
				return toInteger( source );
			case DataType.DECIMAL_TYPE :
				return toBigDecimal( source );
			case DataType.BOOLEAN_TYPE :
				return toBoolean( source );
			case DataType.DATE_TYPE :
				return toDate( source );
			case DataType.DOUBLE_TYPE :
				return toDouble( source );
			case DataType.STRING_TYPE :
				return toString( source );
			case DataType.BLOB_TYPE :
				return toBlob( source );
			default :
				throw new BirtException( ResourceConstants.INVALID_TYPE,
						resourceBundle );
		}
	}
	
	/**
	 * convert a object to given class
	 * Classes supported:
	 * 		Integer.class
	 * 		BigDecimal.class
	 * 		Boolean.class
	 * 		Date.class
	 * 		Double.class
	 * 		String.class
	 * 		Blob.class
	 * @param source
	 * @param toTypeClass
	 * @return
	 * @throws BirtException
	 */
	public static Object convert( Object source, Class toTypeClass )
		throws BirtException
	{
		if ( toTypeClass == Integer.class )
			return toInteger( source );
		if ( toTypeClass == BigDecimal.class )
			return toBigDecimal( source );
		if ( toTypeClass == Boolean.class )
			return toBoolean( source );
		if ( toTypeClass == Date.class )
			return toDate( source );
		if ( toTypeClass == Double.class )
			return toDouble( source );
		if ( toTypeClass == String.class )
			return toString( source );
		if ( toTypeClass == Blob.class )
			return toBlob( source );
		
		throw new BirtException( ResourceConstants.INVALID_TYPE,
				resourceBundle );
	}

	/**
	 * Boolean -> Integer
	 * 		true 	-> 1
	 * 		others 	-> 0 
	 * Date -> Integer
	 * 		Date.getTime();
	 * String -> Integer
	 * 		Integer.valueOf();
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Integer toInteger( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Integer )
		{
			return (Integer) source;
		}
		else if ( source instanceof BigDecimal )
		{
			int intValue = ( (BigDecimal) source ).intValue( );
			return new Integer( intValue );
		}
		else if ( source instanceof Boolean )
		{
			if ( true == ( (Boolean) source ).booleanValue( ) )
				return new Integer( 1 );
			return new Integer( 0 );
		}
		else if ( source instanceof Date )
		{
			long longValue = ( (Date) source ).getTime( );
			return new Integer( (int) longValue );
		}
		else if ( source instanceof Double )
		{
			int intValue = ( (Double) source ).intValue( );
			return new Integer( intValue );
		}
		else if ( source instanceof String )
		{
			try
			{
				return Integer.valueOf( (String) source );
			}
			catch ( NumberFormatException e )
			{
				throw new BirtException( ResourceConstants.CONVERT_FAILS,
						"Integer", resourceBundle );
			}
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS,
					"Integer", resourceBundle );
	}

	/**
	 * Boolean -> BigDecimal
	 * 		true 	-> 1
	 * 		others 	-> 0 
	 * Date -> BigDecimal
	 * 		Date.getTime();
	 * String -> BigDecimal
	 * 		new BigDecimal(String);
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static BigDecimal toBigDecimal( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Integer )
		{
			String str = ( (Integer) source ).toString( );
			return new BigDecimal( str );
		}
		else if ( source instanceof BigDecimal )
		{
			return (BigDecimal) source;
		}
		else if ( source instanceof Boolean )
		{
			if ( true == ( (Boolean) source ).booleanValue( ) )
				return new BigDecimal( 1d );
			return new BigDecimal( 0d );
		}
		else if ( source instanceof Date )
		{
			long longValue = ( (Date) source ).getTime( );
			return new BigDecimal( longValue );
		}
		else if ( source instanceof Double )
		{
			double doubleValue = ( (Double) source ).doubleValue( );
			return new BigDecimal( doubleValue );
		}
		else if ( source instanceof String )
		{
			try
			{
				return new BigDecimal( (String) source );
			}
			catch ( NumberFormatException e )
			{
				throw new BirtException( ResourceConstants.CONVERT_FAILS,
						"BigDecimal", resourceBundle );
			}
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS,
					"BigDecimal", resourceBundle );
	}


	/**
	 * Number -> Boolean
	 * 		0 		-> false
	 * 		others 	-> true
	 * String -> Boolean
	 * 		"true" 	-> true (ignore case)
	 * 		"false" -> false (ignore case)
	 * 		other string will throw an exception
	 * Date -> Boolean
	 * 		throw exception
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Boolean toBoolean( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Boolean )
		{
			return (Boolean) source;
		}
		else if ( source instanceof Integer )
		{
			int intValue = ( (Integer) source ).intValue( );
			if ( intValue == 0 )
				return new Boolean( false );
			return new Boolean( true );
		}
		else if ( source instanceof BigDecimal )
		{
			int intValue = ( (BigDecimal) source ).intValue( );
			if ( intValue == 0 )
				return new Boolean( false );
			return new Boolean( true );
		}
		else if ( source instanceof Double )
		{
			int intValue = ( (Double) source ).intValue( );
			if ( intValue == 0 )
				return new Boolean( false );
			return new Boolean( true );
		}
		else if ( source instanceof String )
		{
			if ( ( (String) source ).equalsIgnoreCase( "true" ) )
				return Boolean.valueOf( "true" );
			else if ( ( (String) source ).equalsIgnoreCase( "false" ) )
				return Boolean.valueOf( "false" );
			else
				throw new BirtException( ResourceConstants.CONVERT_FAILS,
						"Boolean" );
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS,
					"Boolean", resourceBundle );
	}

	/**
	 * Number -> Date
	 * 		new Date((long)Number)
	 * String -> Date
	 * 		toDate(String)  
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Date toDate( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Integer )
		{
			long longValue = ( (Integer) source ).longValue( );
			return new Date( longValue );
		}
		else if ( source instanceof BigDecimal )
		{
			long longValue = ( (BigDecimal) source ).longValue( );
			return new Date( longValue );
		}
		else if ( source instanceof Boolean )
		{
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Date",
					resourceBundle );
		}
		else if ( source instanceof Date )
		{
			return new Date( ( (Date) source ).getTime( ) );
		}
		else if ( source instanceof Double )
		{
			long longValue = ( (Double) source ).longValue( );
			return new Date( longValue );
		}
		else if ( source instanceof String )
		{
			return toDate( (String) source  );
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Date",
					resourceBundle );
	}

	/**
	 * convert String with the specified locale to java.util.Date
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	public static Date toDate( String source, Locale locale )
			throws BirtException
	{
		DateFormat dateFormat = null;
		Date resultDate = null;

		for ( int i = DEFAULT_DATE_STYLE; i <= DateFormat.SHORT; i++ )
		{
			for ( int j = DEFAULT_DATE_STYLE; j <= DateFormat.SHORT; j++ )
			{
				dateFormat = DateFormat.getDateTimeInstance( i, j, locale );
				try
				{
					resultDate = dateFormat.parse( source );
					return resultDate;
				}
				catch ( ParseException e1 )
				{
				}
			}
			
			// only Date, no Time 
			dateFormat = DateFormat.getDateInstance( i, locale );
			try
			{
				resultDate = dateFormat.parse( source );
				return resultDate;
			}
			catch ( ParseException e1 )
			{
			}
		}
		
		// for the String can not be parsed, throws a BirtException
		if ( resultDate == null )
		{
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Date",
					resourceBundle );
		}
		
		// never access here
		return resultDate;
	}
	
	/**
	 * Convert string to date with check.
	 * JDK may do incorrect converse, for example:
	 * 		2005/1/1 Local.US, format pattern is MM/dd/YY.
	 * Above conversion can be done without error, but obviously
	 * the result is not right. This method will do such a simple check,
	 * in DateFormat.SHORT case instead of all cases.
	 * 		Year is not lower than 0.
	 * 		Month is from 1 to 12.
	 * 		Day is from 1 to 31.  
	 * @param source
	 * @param locale
	 * @return Date
	 * @throws BirtException
	 */
	public static Date toDateWithCheck( String source, Locale locale )
			throws BirtException
	{
		DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT,
				locale );
		Date resultDate = null;
		try
		{
			resultDate = dateFormat.parse( source );
		}
		catch ( ParseException e )
		{
			return toDate( source, locale );
		}
		
		// check whether conversion is correct
		if ( DateUtil.checkValid( dateFormat, source ) == false )
		{
			throw new BirtException( ResourceConstants.CONVERT_FAILS,
					"Date",
					resourceBundle );
		}

		return resultDate;
	}
	
	/**
	 * Boolean -> Double
	 * 		true 	-> 1
	 * 		others 	-> 0 
	 * Date -> Double
	 * 		Date.getTime();
	 * String -> Double
	 * 		Double.valueOf(String);
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Double toDouble( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Integer )
		{
			double doubleValue = ( (Integer) source ).doubleValue( );
			return new Double( doubleValue );
		}
		else if ( source instanceof BigDecimal )
		{
			double doubleValue = ( (BigDecimal) source ).doubleValue( );
			return new Double( doubleValue );
		}
		else if ( source instanceof Boolean )
		{
			if ( true == ( (Boolean) source ).booleanValue( ) )
				return new Double( 1d );
			return new Double( 0d );
		}
		else if ( source instanceof Date )
		{
			double doubleValue = ( (Date) source ).getTime( );
			return new Double( doubleValue );
		}
		else if ( source instanceof Double )
		{
			return (Double) source;
		}
		else if ( source instanceof String )
		{
			try
			{
				return Double.valueOf( (String) source );
			}
			catch ( NumberFormatException e )
			{
				throw new BirtException( ResourceConstants.CONVERT_FAILS,
						"Double", resourceBundle );
			}
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Double",
					resourceBundle );
	}

	/**
	 * Number -> String
	 * 		Number.toString()
	 * Boolean -> String
	 * 		Boolean.toString()
	 * Date	-> String
	 * 		toString(Date)
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		
		if(source instanceof Date )
		{
			return toString( (Date) source );
		}
		else
		{
			return source.toString();
		}
	}

	/**
	 * Converting Blob to/from other types is not currently supported
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Blob toBlob( Object source ) throws BirtException
	{
		// Converting Blob to/from other types is not currently supported
		if ( source == null )
			return null;
		else if ( source instanceof Blob )
			return (Blob) source;
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Blob",
					resourceBundle );
	}

	/**
	 * A utility method to convert an ODI data type class value to a BIRT data
	 * type integer, as defined in org.eclipse.birt.core.data.DataType .
	 */
	public static int toApiDataType( Class odiDataType )
	{
		if ( odiDataType == null )
			return DataType.UNKNOWN_TYPE;

		// maps odi data type to BIRT DataType
		if ( odiDataType == DataType.AnyType.class )
			return DataType.ANY_TYPE;
		if ( odiDataType == Integer.class )
			return DataType.INTEGER_TYPE;
		if ( odiDataType == Double.class )
			return DataType.DOUBLE_TYPE;
		if ( odiDataType == String.class )
			return DataType.STRING_TYPE;
		if ( odiDataType == BigDecimal.class )
			return DataType.DECIMAL_TYPE;
		if ( odiDataType == Date.class || odiDataType == Time.class
				|| odiDataType == Timestamp.class )
			return DataType.DATE_TYPE;

		// any other types are not recognized nor supported;
		// BOOLEAN_TYPE and BLOB_TYPE are not supported yet
		assert false;
		return DataType.UNKNOWN_TYPE;
	}
	
	/**
	 * Convert object to a suitable type from its value 
	 * Object -> Integer -> Double -> BigDecimal -> Date -> String
	 */
	public static Object toAutoValue( Object evaValue )
	{
		if ( evaValue == null )
			return null;

		Object value = null;
		if ( evaValue instanceof String )
		{
			// 1: to Integer
			String stringValue = (String) evaValue;
			value = toIntegerValue( evaValue );
			if ( value == null )
			{
				try
				{
					// 2: to Double
					value = Double.valueOf( stringValue );
				}
				catch ( NumberFormatException e1 )
				{
					try
					{
						// 3: to BigDecimal
						value = new BigDecimal( stringValue );
					}
					catch ( NumberFormatException e2 )
					{
						try
						{
							// 4: to Date
							value = toDate( stringValue );
						}
						catch ( BirtException e3 )
						{
							value = evaValue;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * convert object to Integer. If fails, return null. 
	 * Object -> Integer
	 */
	public static Integer toIntegerValue( Object evaValue )
	{
		// to Integer
		Integer value = null;
		if ( evaValue instanceof String )
		{
			String stringValue = evaValue.toString( );
			try
			{
				// 1: to Integer
				value = Integer.valueOf( stringValue );
			}
			catch ( NumberFormatException e1 )
			{
				try
				{
					Double ddValue = Double.valueOf( stringValue );
					int intValue = ddValue.intValue( );
					double doubleValue = ddValue.doubleValue( );
					// TODO: improve this implementation
					// here examine whether the two values are equal.1.0e-5
					if ( Math.abs( intValue - doubleValue ) < 0.0000001 )
					{
						value = Integer.valueOf( String.valueOf( intValue ) );
					}
					else
					{
						value = null;
					}
				}
				catch ( NumberFormatException e2 )
				{
					value = null;
				}
			}
		}
		return value;
	}
	
	/**
	 * Convert String without specified locale to java.util.Date
	 * Try to format the given String for JRE default Locale,
	 * if it fails, try to format the String for Locale.US 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	private static Date toDate( String source )
			throws BirtException
	{
		try
		{
			//Try to format the given String for JRE default Locale
			return toDate( source, Locale.getDefault( ) );
		}
		catch ( BirtException e )
		{
			//format the String for Locale.US
			return toDate( source, DEFAULT_LOCALE );
		}
	}

	/**
	 * format Date to String
	 * e.g. Jan 12, 1952 3:30:32pm 
	 * @param source
	 * @return
	 */
	private static String toString( Date source ){
		DateFormat df = DateFormat.getDateTimeInstance( DEFAULT_DATE_STYLE,
				DEFAULT_DATE_STYLE );
		return df.format( (Date) source );
	}
}