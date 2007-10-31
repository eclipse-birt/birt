/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.exception.ViewerValidationException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * Provides data convert and format services
 * 
 */
public class DataUtil
{

	/**
	 * Convert Object to String
	 * 
	 * @param object
	 * @return String
	 */
	public static String getString( Object object )
	{
		if ( object == null )
			return null;

		return object.toString( );
	}

	/**
	 * Returns trim string, not null
	 * 
	 * @param str
	 * @return
	 */
	public static String trimString( String str )
	{
		if ( str == null )
			return ""; //$NON-NLS-1$

		return str.trim( );
	}

	/**
	 * Trim the end separator
	 * 
	 * @param path
	 * @return
	 */
	public static String trimSepEnd( String path )
	{
		path = trimString( path );
		if ( path.endsWith( File.separator ) )
		{
			path = path.substring( 0, path.length( ) - 1 );
		}

		return path;
	}

	/**
	 * Trim the end separator
	 * 
	 * @param path
	 * @return
	 */
	public static String trimSepFirst( String path )
	{
		path = trimString( path );
		if ( path.startsWith( File.separator ) )
		{
			path = path.substring( 1, path.length( ) );
		}

		return path;
	}

	/**
	 * Returns the default date/time format
	 * 
	 * @param dataType
	 * @return
	 */
	private static String getDefaultDateFormat( String dataType )
	{
		String defFormat = null;
		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equalsIgnoreCase( dataType ) )
		{
			defFormat = ParameterValidationUtil.DEFAULT_DATETIME_FORMAT;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE
				.equalsIgnoreCase( dataType ) )
		{
			defFormat = ParameterValidationUtil.DEFAULT_DATE_FORMAT;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME
				.equalsIgnoreCase( dataType ) )
		{
			defFormat = ParameterValidationUtil.DEFAULT_TIME_FORMAT;
		}

		return defFormat;
	}

	/**
	 * 
	 * Convert parameter to Object
	 * 
	 * @param paramName
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale
	 *            indicate whether it is a locale string
	 * @return Object
	 * @throws ViewerValidationException
	 */
	public static Object validate( String paramName, String dataType,
			String format, String value, Locale locale, boolean isLocale )
			throws ViewerValidationException
	{
		if ( paramName == null || value == null )
			return null;

		if ( !DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType )
				&& value.trim( ).length( ) <= 0 )
		{
			throw new ViewerValidationException( BirtResources.getMessage(
					ResourceConstants.GENERAL_ERROR_PARAMETER_NOTBLANK,
					new String[]{paramName} ) );
		}

		try
		{
			return validate( dataType, format, value, locale, isLocale );
		}
		catch ( ValidationValueException e )
		{
			throw new ViewerValidationException( BirtResources.getMessage(
					ResourceConstants.GENERAL_ERROR_PARAMETER_INVALID,
					new String[]{paramName} )
					+ " " + e.getLocalizedMessage( ), e ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * Convert parameter to Object with pattern
	 * 
	 * @param paramName
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale
	 *            indicate whether it is a locale string
	 * @return Object
	 * @throws ViewerValidationException
	 */
	public static Object validateWithPattern( String paramName,
			String dataType, String format, String value, Locale locale,
			boolean isLocale ) throws ViewerValidationException
	{
		if ( paramName == null )
			return null;

		try
		{
			return validateWithPattern( dataType, format, value, locale,
					isLocale );
		}
		catch ( ValidationValueException e )
		{
			throw new ViewerValidationException( BirtResources.getMessage(
					ResourceConstants.GENERAL_ERROR_PARAMETER_INVALID,
					new String[]{paramName} )
					+ " " + e.getLocalizedMessage( ), e ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * Convert parameter to Object
	 * 
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale
	 *            indicate whether it is a locale string
	 * @return Object
	 * @throws ValidationValueException
	 */
	public static Object validate( String dataType, String format,
			String value, Locale locale, boolean isLocale )
			throws ValidationValueException
	{
		Object obj = null;
		if ( value == null )
			return obj;

		// if parameter value equals display text, should use local/format to
		// format parameter value first
		if ( isLocale )
		{
			obj = validateWithLocale( dataType, format, value, locale );
		}
		else
		{
			// Convert string to object using default format/local
			obj = ParameterValidationUtil.validate( dataType,
					getDefaultDateFormat( dataType ), value );
		}

		return obj;
	}

	/**
	 * 
	 * Convert parameter to Object with pattern
	 * 
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale
	 *            indicate whether it is a locale string
	 * @return Object
	 * @throws ValidationValueException
	 */
	public static Object validateWithPattern( String dataType, String format,
			String value, Locale locale, boolean isLocale )
			throws ValidationValueException
	{
		Object obj = null;
		if ( value == null )
			return obj;

		// if parameter value equals display text, should use local/format to
		// format parameter value first
		if ( isLocale )
		{
			obj = validateWithLocale( dataType, format, value, locale );
		}
		else
		{
			// Default format
			if ( format == null )
				format = getDefaultDateFormat( dataType );

			// Convert string to object using default locale
			obj = ParameterValidationUtil.validate( dataType, format, value );
		}

		return obj;
	}

	/**
	 * Convert parameter to Object with locale setting
	 * 
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @return
	 * @throws ValidationValueException
	 */
	public static Object validateWithLocale( String dataType, String format,
			String value, Locale locale ) throws ValidationValueException
	{
		Object obj = null;
		if ( value == null )
			return obj;

		try
		{
			if ( format == null )
			{
				if ( DesignChoiceConstants.PARAM_TYPE_DATE
						.equalsIgnoreCase( dataType ) )
				{
					format = ParameterValidationUtil.DISPLAY_DATE_FORMAT;
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_TIME
						.equalsIgnoreCase( dataType ) )
				{
					format = ParameterValidationUtil.DISPLAY_TIME_FORMAT;
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
						.equalsIgnoreCase( dataType ) )
				{
					format = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED;
				}
			}

			// Convert locale string to object
			obj = ParameterValidationUtil.validate( dataType, format, value,
					locale );
		}
		catch ( Exception e )
		{
			// Convert string to object using default format/local
			obj = ParameterValidationUtil.validate( dataType,
					getDefaultDateFormat( dataType ), value );
		}

		return obj;
	}

	/**
	 * Gets the display string for the value with default locale and default
	 * format, The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value
	 * must be <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must
	 * be <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param value
	 *  		the input value to validate
	 * @return the formatted string
	 */

	public static String getDisplayValue( Object value )
	{
		if ( value == null )
			return null;

		if ( value instanceof Float || value instanceof Double
				|| value instanceof BigDecimal
				|| value instanceof com.ibm.icu.math.BigDecimal )
		{
			return value.toString( );
		}
		return ParameterValidationUtil.getDisplayValue( value );
	}

	/**
	 * Try convert an object to given type Types supported:
	 * <p>
	 * <ul>
	 * <li>IScalarParameterDefn.TYPE_INTEGER</li>
	 * <li>IScalarParameterDefn.TYPE_DECIMAL</li>
	 * <li>IScalarParameterDefn.TYPE_BOOLEAN</li>
	 * <li>IScalarParameterDefn.TYPE_DATE_TIME</li>
	 * <li>IScalarParameterDefn.TYPE_FLOAT</li>
	 * <li>IScalarParameterDefn.TYPE_STRING</li>
	 * <li>IScalarParameterDefn.TYPE_DATE</li>
	 * <li>IScalarParameterDefn.TYPE_TIME</li>
	 * <ul>
	 * </p>
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

		// if any type, return directly.
		if ( toType == IScalarParameterDefn.TYPE_ANY )
			return source;

		switch ( toType )
		{
			case IScalarParameterDefn.TYPE_INTEGER :
				return DataTypeUtil.toInteger( source );
			case IScalarParameterDefn.TYPE_DECIMAL :
				return DataTypeUtil.toBigDecimal( source );
			case IScalarParameterDefn.TYPE_BOOLEAN :
				return DataTypeUtil.toBoolean( source );
			case IScalarParameterDefn.TYPE_DATE_TIME :
				return DataTypeUtil.toDate( source );
			case IScalarParameterDefn.TYPE_FLOAT :
				return DataTypeUtil.toDouble( source );
			case IScalarParameterDefn.TYPE_STRING :
				return DataTypeUtil.toString( source );
			case IScalarParameterDefn.TYPE_DATE :
				return DataTypeUtil.toSqlDate( source );
			case IScalarParameterDefn.TYPE_TIME :
				return DataTypeUtil.toSqlTime( source );
			default :
				throw new CoreException( "Invalid type." ); //$NON-NLS-1$
		}
	}

	/**
	 * Convert to UTF-8 bytes
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toUTF8( byte[] bytes )
	{
		assert bytes != null;
		String str = null;
		try
		{
			str = new String( bytes, "utf-8" ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e )
		{
		}
		return str;
	}

	/**
	 * Returns oda type name
	 * 
	 * @param odaTypeCode
	 * @return
	 */
	public static String getOdaTypeName( int odaTypeCode )
	{
		switch ( odaTypeCode )
		{
			case Types.INTEGER :
				return "INT"; //$NON-NLS-1$
			case Types.DOUBLE :
			case Types.FLOAT :
				return "DOUBLE"; //$NON-NLS-1$
			case Types.VARCHAR :
				return "STRING"; //$NON-NLS-1$
			case Types.DATE :
				return "DATE"; //$NON-NLS-1$
			case Types.TIME :
				return "TIME"; //$NON-NLS-1$
			case Types.TIMESTAMP :
				return "TIMESTAMP"; //$NON-NLS-1$
			case Types.NUMERIC :
			case Types.DECIMAL :
				return "BIGDECIMAL"; //$NON-NLS-1$
			case Types.BLOB :
				return "BLOB"; //$NON-NLS-1$
			case Types.CLOB :
				return "CLOB"; //$NON-NLS-1$
			case Types.BOOLEAN :
				return "BOOLEAN"; //$NON-NLS-1$
			default :
				return "STRING"; //$NON-NLS-1$
		}
	}

	/**
	 * Check the passed String whether be contained in list
	 * 
	 * @param values
	 * @param value
	 * @param ifDelete
	 * @return
	 */
	public static boolean contain( List values, String value, boolean ifDelete )
	{
		if ( values == null )
			return false;

		for ( Iterator it = values.iterator( ); it.hasNext( ); )
		{
			Object obj = it.next( );
			if ( obj == null )
			{
				if ( value == null )
				{
					if ( ifDelete )
						values.remove( obj );
					return true;
				}

				continue;
			}

			if ( obj instanceof String && ( (String) obj ).equals( value ) )
			{
				if ( ifDelete )
					values.remove( obj );
				return true;
			}
		}

		return false;
	}
}
