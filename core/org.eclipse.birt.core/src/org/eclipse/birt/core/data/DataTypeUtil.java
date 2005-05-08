/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

	public static ResourceBundle resourceBundle = ( new ResourceHandle( Locale
			.getDefault( ) ) ).getResourceBundle( );

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

	public static Date toDate( Object source, Locale locale ) throws BirtException
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
			return convertStringtoDate( (String) source, locale);
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Date",
					resourceBundle );
	}

	public static Date toDate( Object source ) throws BirtException
	{
		return toDate( source, Locale.US );
	}
	
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

	public static String toString( Object source ) throws BirtException
	{
		if ( source == null )
			return null;
		else if ( source instanceof Integer )
		{
			return ( (Integer) source ).toString( );
		}
		else if ( source instanceof BigDecimal )
		{
			return ( (BigDecimal) source ).toString( );
		}
		else if ( source instanceof Boolean )
		{
			return ( (Boolean) source ).toString( );
		}
		else if ( source instanceof Date )
		{
			return ( (Date) source ).toString( );
		}
		else if ( source instanceof Double )
		{
			return ( (Double) source ).toString( );
		}
		else if ( source instanceof String )
		{
			return (String) source;
		}
		else
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "String",
					resourceBundle );
	}

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

	// A utility method to convert an ODI data type class value to a BIRT data
	// type integer, as defined in org.eclipse.birt.core.data.DataType .
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
	 * convert String with the specified locale to java.util.Date
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	private static Date convertStringtoDate( String source, Locale locale )
			throws BirtException
	{
		DateFormat dateFormat = null;
		Date resultDate = null;

		//For each pattern, we try to format a date for a default Locale
		//If format fails, we format it for Locale.US

		//Date style is SHORT such as 12.13.52
		//Time sytle is MEDIUM such as 3:30:32pm
		try
		{
			dateFormat = DateFormat.getDateTimeInstance( DateFormat.SHORT,
					DateFormat.MEDIUM );
			resultDate = dateFormat.parse( source );
		}
		catch ( ParseException e )
		{
			try
			{
				dateFormat = DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.MEDIUM, locale );
				resultDate = dateFormat.parse( source );
			}
			catch ( ParseException e1 )
			{
			}
		}

		if ( resultDate == null )
		{
			//Date style is SHORT such as 12.13.52
			//Time style is SHORT such as 3:30pm
			try
			{
				dateFormat = DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.SHORT );
				resultDate = dateFormat.parse( source );
			}
			catch ( ParseException e )
			{
				try
				{
					dateFormat = DateFormat.getDateTimeInstance(
							DateFormat.SHORT, DateFormat.SHORT, locale );
					resultDate = dateFormat.parse( source );
				}
				catch ( ParseException e1 )
				{
				}
			}
		}

		if ( resultDate == null )
		{
			//Date style is SHORT such as 12.13.52
			//No Time sytle
			try
			{
				dateFormat = DateFormat.getDateInstance( DateFormat.SHORT );
				resultDate = dateFormat.parse( source );
			}
			catch ( ParseException e )
			{
				try
				{
					dateFormat = DateFormat.getDateInstance( DateFormat.SHORT,
							locale );
					resultDate = dateFormat.parse( source );
				}
				catch ( ParseException e1 )
				{
				}
			}
		}
		
		// for the String can not be parsed, throws a BirtException
		if ( resultDate == null )
		{
			throw new BirtException( ResourceConstants.CONVERT_FAILS, "Date",
					resourceBundle );
		}
		
		return resultDate;
	}

}