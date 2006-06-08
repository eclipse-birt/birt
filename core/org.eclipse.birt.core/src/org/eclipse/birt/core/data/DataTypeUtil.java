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
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.i18n.ResourceHandle;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;

/**
 * A utility function The convert method converts the source object, which can
 * be any supported data type, into an object given specified type. If no
 * reasonable conversion can be made, throw a BirtException.
 */
public final class DataTypeUtil
{

	// Defalult Locale, if we have any problem parse string to date for
	// Locale.getDefault()
	// we will try to parse it for Locale.US
	private static ULocale DEFAULT_LOCALE = ULocale.US;

	// Default Date/Time Style
	private static int DEFAULT_DATE_STYLE = DateFormat.MEDIUM;

	// resource bundle for exception messages
	public static ResourceBundle resourceBundle = ( new ResourceHandle( ULocale
			.getDefault( ) ) ).getResourceBundle( );

	public static long count = 0;
	private final static String pluginId = "org.eclipse.birt.core";

	/**
	 * convert an object to given type Types supported: DataType.INTEGER_TYPE
	 * DataType.DECIMAL_TYPE DataType.BOOLEAN_TYPE DataType.DATE_TYPE
	 * DataType.DOUBLE_TYPE DataType.STRING_TYPE DataType.BLOB_TYPE
	 * 
	 * @param source
	 * @param toType
	 * @return
	 * @throws BirtException
	 */
	public static Object convert( Object source, int toType )
			throws BirtException
	{
		if ( source == null )
			return null;

		// here we assume the efficiency of if else is higher than switch case
		if ( toType == DataType.UNKNOWN_TYPE || toType == DataType.ANY_TYPE )
			return source;

		source = JavascriptEvalUtil.convertJavascriptValue( source );

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
			case DataType.BINARY_TYPE :
				return toBytes( source );
			default :
				throw new BirtException( pluginId,
						ResourceConstants.INVALID_TYPE, resourceBundle );
		}
	}

	/**
	 * convert a object to given class Classes supported: Integer.class
	 * BigDecimal.class Boolean.class Date.class Double.class String.class
	 * Blob.class
	 * 
	 * @param source
	 * @param toTypeClass
	 * @return
	 * @throws BirtException
	 */
	public static Object convert( Object source, Class toTypeClass )
			throws BirtException
	{
		if ( toTypeClass == DataType.getClass( DataType.ANY_TYPE ) )
			return source;
		if ( toTypeClass == Integer.class )
			return toInteger( source );
		if ( toTypeClass == BigDecimal.class )
			return toBigDecimal( source );
		if ( toTypeClass == Boolean.class )
			return toBoolean( source );
		if ( Date.class.isAssignableFrom( toTypeClass ) )
			return toDate( source );
		if ( toTypeClass == Double.class )
			return toDouble( source );
		if ( toTypeClass == String.class )
			return toString( source );
		if ( toTypeClass == Blob.class )
			return toBlob( source );

		throw new BirtException( pluginId, ResourceConstants.INVALID_TYPE,
				resourceBundle );
	}

	/**
	 * Boolean -> Integer true -> 1 others -> 0 Date -> Integer Date.getTime();
	 * String -> Integer Integer.valueOf();
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Integer toInteger( Object source ) throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Integer )
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
				throw new BirtException( pluginId,
						ResourceConstants.CONVERT_FAILS, new Object[]{
								source.toString( ), "Integer"}, resourceBundle );
			}
		}
		else
		{
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Integer"}, resourceBundle );
		}
	}

	/**
	 * Boolean -> BigDecimal true -> 1 others -> 0 Date -> BigDecimal
	 * Date.getTime(); String -> BigDecimal new BigDecimal(String);
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static BigDecimal toBigDecimal( Object source ) throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Integer )
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
				throw new BirtException( pluginId,
						ResourceConstants.CONVERT_FAILS, new Object[]{
								source.toString( ), "BigDecimal"},
						resourceBundle );
			}
		}
		else
		{
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "BigDecimal"},
					resourceBundle );
		}
	}

	/**
	 * Number -> Boolean 0 -> false others -> true String -> Boolean "true" ->
	 * true (ignore case) "false" -> false (ignore case) other string will throw
	 * an exception Date -> Boolean throw exception
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Boolean toBoolean( Object source ) throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Boolean )
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
			try
			{
				if ( ( (String) source ).equalsIgnoreCase( "true" ) )
					return Boolean.valueOf( "true" );
				else if ( ( (String) source ).equalsIgnoreCase( "false" ) )
					return Boolean.valueOf( "false" );
				else if ( Integer.parseInt( (String) source ) == 0 )
					return Boolean.valueOf( "false" );
				else if ( Integer.parseInt( (String) source ) != 0 )
					return Boolean.valueOf( "true" );
			}
			catch ( NumberFormatException e )
			{
				throw new BirtException( pluginId,
						ResourceConstants.CONVERT_FAILS, new Object[]{
								source.toString( ), "Boolean"} );
			}

			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Boolean"} );
		}
		else
		{
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Boolean"}, resourceBundle );
		}
	}

	/**
	 * Number -> Date new Date((long)Number) String -> Date toDate(String)
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Date toDate( Object source ) throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Date )
		{
			return new Date( ( (Date) source ).getTime( ) );
		}
		else if ( source instanceof String )
		{
			return toDate( (String) source );
		}
		else
		{
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Date"}, resourceBundle );
		}
	}

	/**
	 * A temp solution to the adoption of ICU4J to BIRT. Simply delegate toDate(
	 * String, Locale) method.
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 *            the locate of the string
	 * @return result Date
	 */
	public static Date toDate( String source, Locale locale )
			throws BirtException
	{
		return toDate( source, ULocale.forLocale( locale ) );
	}

	/**
	 * convert String with the specified locale to java.util.Date
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 *            the locate of the string
	 * @return result Date
	 */
	public static Date toDate( String source, ULocale locale )
			throws BirtException
	{
		if ( source == null )
			return null;

		DateFormat dateFormat = null;
		Date resultDate = null;

		for ( int i = DEFAULT_DATE_STYLE; i <= DateFormat.SHORT; i++ )
		{
			for ( int j = DEFAULT_DATE_STYLE; j <= DateFormat.SHORT; j++ )
			{
				dateFormat = DateFormatHolder
						.getDateTimeInstance( i, j, locale );
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
			dateFormat = DateFormatHolder.getDateInstance( i, locale );
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
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Date"}, resourceBundle );
		}

		// never access here
		return resultDate;
	}

	/**
	 * A temp solution to the adoption of ICU4J in BIRT. It is a simple
	 * delegation to toDateWithCheck( String, Locale ).
	 * 
	 * @param source
	 * @param locale
	 * @return Date
	 * @throws BirtException
	 */
	public static Date toDateWithCheck( String source, Locale locale )
			throws BirtException
	{
		return toDateWithCheck( source, ULocale.forLocale( locale ) );
	}

	/**
	 * Convert string to date with check. JDK may do incorrect converse, for
	 * example: 2005/1/1 Local.US, format pattern is MM/dd/YY. Above conversion
	 * can be done without error, but obviously the result is not right. This
	 * method will do such a simple check, in DateFormat.SHORT case instead of
	 * all cases. Year is not lower than 0. Month is from 1 to 12. Day is from 1
	 * to 31.
	 * 
	 * @param source
	 * @param locale
	 * @return Date
	 * @throws BirtException
	 */
	public static Date toDateWithCheck( String source, ULocale locale )
			throws BirtException
	{
		DateFormat dateFormat = DateFormatHolder.getDateInstance(
				DateFormat.SHORT, locale );
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
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Date"}, resourceBundle );
		}

		return resultDate;
	}

	/**
	 * Boolean -> Double true -> 1 others -> 0 Date -> Double Date.getTime();
	 * String -> Double Double.valueOf(String);
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Double toDouble( Object source ) throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Integer )
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
				throw new BirtException( pluginId,
						ResourceConstants.CONVERT_FAILS, new Object[]{
								source.toString( ), "Double"}, resourceBundle );
			}
		}
		else
		{
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Double"}, resourceBundle );
		}
	}

	/**
	 * Number -> String Number.toString() Boolean -> String Boolean.toString()
	 * Date -> String toString(Date)
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString( Object source ) throws BirtException
	{
		return toString( source, ULocale.getDefault( ) );
	}

	/**
	 * A temp solution to the adoption of ICU4J. It is a simple delegation to
	 * toString( Object, Locale ).
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString( Object source, Locale locale )
			throws BirtException
	{
		return toString( source, ULocale.forLocale( locale ) );
	}

	/**
	 * Number -> String Number.toString() Boolean -> String Boolean.toString()
	 * Date -> String toString(Date,locale)
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString( Object source, ULocale locale )
			throws BirtException
	{
		if ( source == null )
			return null;

		if ( source instanceof Date )
		{
			return toString( (Date) source, locale );
		}
		else
		{
			String str = "";
			if ( source instanceof byte[] )
			{
				final int strLength = 8;

				byte[] sourceValue = (byte[]) source;
				int length = Math.min( sourceValue.length, strLength );
				for ( int i = 0; i < length; i++ )
				{
					str += Integer.toHexString( sourceValue[i] ).toUpperCase( )
							+ " ";
				}
				if ( sourceValue.length > strLength )
				{
					str += "...";
				}
			}
			else
			{
				str = source.toString( );
			}

			return str;
		}
	}

	/**
	 * Converting Blob to/from other types is not currently supported
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Blob toBlob( Object source ) throws BirtException
	{
		// Converting Blob to/from other types is not currently supported
		if ( source == null )
			return null;

		if ( source instanceof Blob )
			return (Blob) source;
		else
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Blob"}, resourceBundle );
	}

	/**
	 * @param source
	 * @return byte array
	 * @throws BirtException
	 */
	public static byte[] toBytes( Object source ) throws BirtException
	{
		// Converting Blob to/from other types is not currently supported
		if ( source == null )
			return null;

		if ( source instanceof byte[] )
			return (byte[]) source;
		else
			throw new BirtException( pluginId, ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Binary"}, resourceBundle );
	}

	/**
	 * Converts a Java class to its corresponding data type constant defined in
	 * DataType
	 */
	public static int toApiDataType( Class clazz )
	{
		if ( clazz == null )
			return DataType.UNKNOWN_TYPE;

		if ( clazz == DataType.AnyType.class )
			return DataType.ANY_TYPE;
		else if ( Integer.class.isAssignableFrom( clazz ) )
			return DataType.INTEGER_TYPE;
		else if ( Double.class.isAssignableFrom( clazz ) )
			return DataType.DOUBLE_TYPE;
		else if ( String.class.isAssignableFrom( clazz ) )
			return DataType.STRING_TYPE;
		else if ( BigDecimal.class.isAssignableFrom( clazz ) )
			return DataType.DECIMAL_TYPE;
		else if ( Date.class.isAssignableFrom( clazz ) )
			return DataType.DATE_TYPE;
		else if ( byte[].class.isAssignableFrom( clazz ) )
			return DataType.BINARY_TYPE;
		else if ( Clob.class.isAssignableFrom( clazz )
				|| clazz.getName( ).equals(
						"org.eclipse.datatools.connectivity.oda.IClob" ) )
			return DataType.STRING_TYPE;
		else if ( Blob.class.isAssignableFrom( clazz )
				|| clazz.getName( ).equals(
						"org.eclipse.datatools.connectivity.oda.IBlob" ) )
			return DataType.BINARY_TYPE;

		// any other types are not recognized nor supported;
		return DataType.UNKNOWN_TYPE;
	}

	/**
	 * Converts an ODA data type code to its corresponding Data Engine API data
	 * type constant defined in DataType.
	 * 
	 * @param odaDataTypeCode
	 *            an ODA data type code
	 * @throws BirtException
	 *             if the specified ODA data type code is not a supported type
	 */
	public static int toApiDataType( int odaDataTypeCode ) throws BirtException
	{
		Class odiTypeClass = toOdiTypeClass( odaDataTypeCode );
		return toApiDataType( odiTypeClass );
	}

	/**
	 * Convert object to a suitable type from its value Object -> Integer ->
	 * Double -> BigDecimal -> Date -> String
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
	 * convert object to Integer. If fails, return null. Object -> Integer
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
	 * Convert String without specified locale to java.util.Date Try to format
	 * the given String for JRE default Locale, if it fails, try to format the
	 * String for Locale.US
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 *            the locate of the string
	 * @return result Date
	 */
	private static Date toDate( String source ) throws BirtException
	{
		try
		{
			// Try to format the given String for JRE default Locale
			return toDate( source, ULocale.getDefault( ) );
		}
		catch ( BirtException e )
		{
			// format the String for Locale.US
			return toDate( source, DEFAULT_LOCALE );
		}
	}

	/**
	 * Call org.eclipse.birt.core.format.DateFormatter
	 * 
	 * @param source
	 * @return
	 */
	private static String toString( Date source, ULocale locale )
	{
		DateFormatter df = new DateFormatter( locale );
		return df.format( (Date) source );
	}

	/**
	 * Converts an ODA data type code to the Java class of its corresponding
	 * Data Engine ODI data type. <br>
	 * <br>
	 * <b>ODA Data Type -> ODI Type Class</b><br>
	 * <i>Integer -> java.lang.Integer<br>
	 * Double -> java.lang.Double<br>
	 * Character -> java.lang.String<br>
	 * Decimal -> java.math.BigDecimal<br>
	 * Date -> java.util.Date<br>
	 * Time -> java.sql.Time<br>
	 * Timestamp -> java.sql.Timestamp<br>
	 * Blob -> java.sql.Blob<br>
	 * Clob -> java.sql.Clob<br>
	 * </i>
	 * 
	 * @param odaDataTypeCode
	 *            an ODA data type code
	 * @return the ODI type class that corresponds with the specified ODA data
	 *         type
	 * @throws BirtException
	 *             if the specified ODA data type is not a supported type
	 */
	public static Class toOdiTypeClass( int odaDataTypeCode )
			throws BirtException
	{
		if ( odaDataTypeCode != Types.CHAR && odaDataTypeCode != Types.INTEGER
				&& odaDataTypeCode != Types.DOUBLE
				&& odaDataTypeCode != Types.DECIMAL
				&& odaDataTypeCode != Types.DATE
				&& odaDataTypeCode != Types.TIME
				&& odaDataTypeCode != Types.TIMESTAMP
				&& odaDataTypeCode != Types.BLOB
				&& odaDataTypeCode != Types.CLOB
				&& odaDataTypeCode != Types.NULL )
		{
			throw new BirtException( pluginId, ResourceConstants.INVALID_TYPE,
					resourceBundle );
		}

		Class fieldClass = null;
		switch ( odaDataTypeCode )
		{
			case Types.CHAR :
				fieldClass = String.class;
				break;

			case Types.INTEGER :
				fieldClass = Integer.class;
				break;

			case Types.DOUBLE :
				fieldClass = Double.class;
				break;

			case Types.DECIMAL :
				fieldClass = BigDecimal.class;
				break;

			case Types.DATE :
				fieldClass = Date.class;
				break;

			case Types.TIME :
				fieldClass = Time.class;
				break;

			case Types.TIMESTAMP :
				fieldClass = Timestamp.class;
				break;

			case Types.BLOB :
				fieldClass = Blob.class;
				break;

			case Types.CLOB :
				fieldClass = Clob.class;
				break;

			case Types.NULL :
				fieldClass = null;
				break;
		}

		return fieldClass;
	}

	/**
	 * Converts an ODI type class to its corresponding ODA data type code. <br>
	 * <b>ODI Type Class -> ODA Data Type</b><br>
	 * <i>java.lang.Integer -> Integer<br>
	 * java.lang.Double -> Double<br>
	 * java.lang.String -> Character<br>
	 * java.math.BigDecimal -> Decimal<br>
	 * java.util.Date -> Date<br>
	 * java.sql.Time -> Time<br>
	 * java.sql.Timestamp -> Timestamp<br>
	 * java.sql.Blob -> Blob<br>
	 * java.sql.Clob -> Clob<br>
	 * </i><br>
	 * All other type classes are mapped to the ODA String data type.
	 * 
	 * @param odiTypeClass
	 *            a type class used by the Data Engine ODI component
	 * @return the ODA data type that maps to the ODI type class.
	 */
	public static int toOdaDataType( Class odiTypeClass )
	{
		int odaType = Types.CHAR; // default

		if ( odiTypeClass == null )
			odaType = Types.CHAR;
		else if ( odiTypeClass == String.class )
			odaType = Types.CHAR;
		else if ( odiTypeClass == Integer.class )
			odaType = Types.INTEGER;
		else if ( odiTypeClass == Double.class )
			odaType = Types.DOUBLE;
		else if ( odiTypeClass == BigDecimal.class )
			odaType = Types.DECIMAL;
		else if ( odiTypeClass == Date.class )
			odaType = Types.DATE;
		else if ( odiTypeClass == Time.class )
			odaType = Types.TIME;
		else if ( odiTypeClass == Timestamp.class )
			odaType = Types.TIMESTAMP;
		else if ( odiTypeClass == Blob.class )
			odaType = Types.BLOB;
		else if ( odiTypeClass == Clob.class )
			odaType = Types.CLOB;

		return odaType;
	}
}

/**
 * 
 * 
 */
class DateFormatHolder
{

	//
	private static Map dateTimeholder = new HashMap( );
	private static Map dateHolder = new HashMap( );

	/**
	 * 
	 * 
	 */
	private DateFormatHolder( )
	{
	}

	/**
	 * 
	 * @param dateStyle
	 * @param timeStyle
	 * @param locale
	 * @return
	 */
	public static DateFormat getDateTimeInstance( int dateStyle, int timeStyle,
			ULocale locale )
	{
		// DateFormatIdentifier key = new
		// DateFormatIdentifier(dateStyle,timeStyle,locale) ;
		String key = String.valueOf( dateStyle ) + ":"
				+ String.valueOf( timeStyle ) + ":" + locale.getName( );
		DateFormat result = (DateFormat) dateTimeholder.get( key );
		if ( result == null )
		{
			result = DateFormat.getDateTimeInstance( dateStyle, timeStyle,
					locale );
			dateTimeholder.put( key, result );
		}
		return result;
	}

	/**
	 * 
	 * @param dateStyle
	 * @param locale
	 * @return
	 */
	public static DateFormat getDateInstance( int dateStyle, ULocale locale )
	{
		String key = String.valueOf( dateStyle ) + ":" + locale.getName( );
		// DateFormatIdentifier key = new
		// DateFormatIdentifier(dateStyle,0,locale) ;
		DateFormat result = (DateFormat) dateHolder.get( key );
		if ( result == null )
		{
			result = DateFormat.getDateInstance( dateStyle, locale );
			dateHolder.put( key, result );
		}
		return result;
	}
}
