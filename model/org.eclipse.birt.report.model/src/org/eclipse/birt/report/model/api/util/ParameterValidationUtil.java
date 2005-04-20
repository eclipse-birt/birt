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

package org.eclipse.birt.report.model.api.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;

/**
 * Validates the parameter value with the given data type and format pattern
 * string. This util class can validate the parameter of the following types:
 * 
 * <ul>
 * <li><code>PARAM_TYPE_DATETIME</code></li>
 * <li><code>PARAM_TYPE_FLOAT</code></li>
 * <li><code>PARAM_TYPE_DECIMAL</code></li>
 * <li><code>PARAM_TYPE_BOOLEAN</code></li>
 * <li><code>PARAM_TYPE_STRING</code></li>
 * </ul>
 * 
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public class ParameterValidationUtil
{

	/**
	 * Validates a input parameter value with the given data type. The returned
	 * value is locale and format independent. The data type can be one of the
	 * following:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_DATETIME</code></li>
	 * <li><code>PARAM_TYPE_FLOAT</code></li>
	 * <li><code>PARAM_TYPE_DECIMAL</code></li>
	 * <li><code>PARAM_TYPE_BOOLEAN</code></li>
	 * <li><code>PARAM_TYPE_STRING</code></li>
	 * </ul>
	 * 
	 * @param dataType
	 *            the data type of the value
	 * @param value
	 *            the input value to validate
	 * @param locale
	 *            the locale information
	 * @return the validated value if the input value is valid for the given
	 *         data type
	 * @throws ValidationValueException
	 *             if the input value is not valid with the given data type
	 */

	static private Object validate( String dataType, String value, Locale locale )
			throws ValidationValueException
	{
		if ( value == null )
			return null;

		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equalsIgnoreCase( dataType ) )
			return doVidateDateTime( value, locale );
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT
				.equalsIgnoreCase( dataType )
				|| DesignChoiceConstants.PARAM_TYPE_DECIMAL
						.equalsIgnoreCase( dataType ) )
		{
			return doValidateNumber( dataType, value, locale );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN
				.equalsIgnoreCase( dataType ) )
		{
			return doValidateBoolean( value, locale );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType ) )
		{
			return value;
		}
		else
		{
			assert false;
			return null;
		}

	}

	/**
	 * Validates the input value at the given locale. The format is: short date
	 * and medium time.
	 * 
	 * @param value
	 *            the value to validate
	 * @param locale
	 *            the locale information
	 * @return the date value if validation is successful
	 * @throws ValidationValueException
	 *             if the value is invalid
	 */

	static final Date doVidateDateTime( String value, Locale locale )
			throws ValidationValueException
	{

		DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.SHORT, DateFormat.MEDIUM, locale );
		try
		{
			return formatter.parse( value );
		}
		catch ( ParseException e )
		{
			throw new ValidationValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME );
		}
	}

	/**
	 * Validates the input value at the given locale. The format is: general
	 * number.
	 * 
	 * @param dataType
	 *            the data type
	 * @param value
	 *            the value to validate
	 * @param locale
	 *            the locale information
	 * @return the double value if validation is successful
	 * @throws ValidationValueException
	 *             if the value is invalid
	 */

	static final Double doValidateNumber( String dataType, String value,
			Locale locale ) throws ValidationValueException
	{
		assert DesignChoiceConstants.PARAM_TYPE_FLOAT
				.equalsIgnoreCase( dataType )
				|| DesignChoiceConstants.PARAM_TYPE_DECIMAL
						.equalsIgnoreCase( dataType );
		value = StringUtil.trimString( value );
		if ( value == null )
			return null;

		NumberFormat localeFormatter = NumberFormat.getNumberInstance( locale );
		Number number = null;
		try
		{
			// Parse in locale-dependent way.
			// Use the decimal separator from the locale.
			number = localeFormatter.parse( value );
		}
		catch ( ParseException e )
		{
			throw new ValidationValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					dataType );
		}
		if ( number != null )
			return new Double( number.doubleValue( ) );
		return null;
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string. The returned value is locale and pattern independent. The data
	 * type and the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd
	 * HH:mm:ss" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType
	 *            the data type of the value
	 * @param format
	 *            the format choice string
	 * @param value
	 *            the input value to validate
	 * @param locale
	 *            the locale information
	 * @return the validated value if the input value is valid for the given
	 *         data type and format choice string
	 * @throws ValidationValueException
	 *             if the input value is not valid with the given data type and
	 *             format string
	 */

	static public Object validate( String dataType, String format,
			String value, Locale locale ) throws ValidationValueException
	{
		if ( value == null )
			return null;

		if ( StringUtil.isBlank( format ) )
			return validate( dataType, value, locale );
		try
		{
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
					.equalsIgnoreCase( dataType ) )
				return doValidateDateTimeByPattern( format, value, locale );
			else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT
					.equalsIgnoreCase( dataType )
					|| DesignChoiceConstants.PARAM_TYPE_DECIMAL
							.equalsIgnoreCase( dataType ) )
				return doValidateNumberByPattern( dataType, format, value,
						locale );
			else if ( DesignChoiceConstants.PARAM_TYPE_STRING
					.equalsIgnoreCase( dataType ) )
			{
				if ( StringUtil.isBlank( value ) )
					return value;
				else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE
						.equalsIgnoreCase( format ) )
					return value.toLowerCase( locale );
				else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE
						.equalsIgnoreCase( format ) )
					return value.toUpperCase( locale );
				else
				{
					assert false;
					return value;
				}
			}
			else
			{
				assert false;
				return null;
			}
		}
		catch ( ValidationValueException e )
		{
			return validate( dataType, value, locale );
		}
	}

	/**
	 * Validates the input boolean value with the given locale.
	 * 
	 * @param value
	 *            the input value to validate
	 * @param locale
	 *            the locale information
	 * @return the <code>Boolean</code> object if the input is valid,
	 *         otherwise <code>null</code>
	 * @throws ValidationValueException
	 */

	static private Boolean doValidateBoolean( String value, Locale locale )
			throws ValidationValueException
	{
		if ( StringUtil.isBlank( value ) )
			return null;

		// 1. Internal boolean name.

		if ( value.equalsIgnoreCase( BooleanPropertyType.TRUE ) )
			return Boolean.TRUE;
		else if ( value.equalsIgnoreCase( BooleanPropertyType.FALSE ) )
			return Boolean.FALSE;

		// 2. A localized Boolean name. Convert the localized
		// Boolean name into Boolean instance.

		if ( value.equalsIgnoreCase( getMessage( locale,
				BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY ) ) )
		{
			return Boolean.TRUE;
		}
		else if ( value.equalsIgnoreCase( getMessage( locale,
				BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY ) ) )
		{
			return Boolean.FALSE;
		}

		throw new ValidationValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				DesignChoiceConstants.PARAM_TYPE_BOOLEAN );
	}

	/**
	 * Gets the message with the given locale and key.
	 * 
	 * @param locale
	 *            the locale information
	 * @param key
	 *            the message key
	 * @return the message if found, otherwise the message key
	 */

	static private String getMessage( Locale locale, String key )
	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle(
				ModelMessages.class.getPackage( ).getName( ) + ".Messages", //$NON-NLS-1$
				locale, ModelMessages.class.getClassLoader( ) );
		if ( resourceBundle != null )
			return resourceBundle.getString( key );
		return key;
	}

	/**
	 * Validates the input date time string with the given format. The format
	 * can be pre-defined choices or the pattern string.
	 * 
	 * @param format
	 *            the format to validate
	 * @param value
	 *            the value to validate
	 * @param locale
	 *            the locale information
	 * @return the date value if validation is successful
	 * @throws ValidationValueException
	 *             if the value to validate is invalid
	 */

	static private Date doValidateDateTimeByPattern( String format,
			String value, Locale locale ) throws ValidationValueException
	{
		assert !StringUtil.isBlank( format );
		if ( StringUtil.isBlank( value ) )
			return null;

		try
		{
			DateFormatter formatter = new DateFormatter( locale );
			formatter.applyPattern( format );
			return formatter.parse( value );
		}
		catch ( ParseException e )
		{
			throw new ValidationValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME );
		}
	}

	/**
	 * Validates the input date time string with the given format.
	 * 
	 * @param dataType
	 *            the data type of the value
	 * @param format
	 *            the format pattern
	 * @param value
	 *            the value to validate
	 * @param locale
	 *            the locale information
	 * @return the double value if the validation is successful
	 * @throws ValidationValueException
	 *             if the value to validate is invalid
	 */

	static private Double doValidateNumberByPattern( String dataType,
			String format, String value, Locale locale )
			throws ValidationValueException
	{
		assert DesignChoiceConstants.PARAM_TYPE_FLOAT
		.equalsIgnoreCase( dataType )
		|| DesignChoiceConstants.PARAM_TYPE_DECIMAL
				.equalsIgnoreCase( dataType );
		assert !StringUtil.isBlank( format );
		if ( StringUtil.isBlank( value ) )
			return null;
		NumberFormatter formatter = new NumberFormatter( locale );
		formatter.applyPattern( format );
		Number number = null;
		try
		{
			number = formatter.parse( value );
		}
		catch ( ParseException e )
		{
			throw new ValidationValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					dataType );
		}
		if ( number != null )
			return new Double( number.doubleValue( ) );
		return null;
	}

	/**
	 * Gets the display string for the value with the given data type, format,
	 * locale. The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value
	 * must be <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must
	 * be <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param dataType
	 *  		the data type of the input value
	 * @param format
	 *  		the format pattern to validate
	 * @param value
	 *  		the input value to validate
	 * @param locale
	 *  		the locale information
	 * @return the formatted string
	 */

	static public String getDisplayValue( String dataType, String format,
			Object value, Locale locale )
	{
		if ( value == null )
			return null;

		if ( StringUtil.isBlank( format ) )
		{
			return getDisplayValue( dataType, value, locale );
		}
		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equalsIgnoreCase( dataType ) )
		{
			DateFormatter formatter = new DateFormatter( locale );
			formatter.applyPattern( format );
			return formatter.format( (Date) value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT
				.equalsIgnoreCase( dataType ) )
		{
			NumberFormatter formatter = new NumberFormatter( locale );
			formatter.applyPattern( format );
			return formatter.format( ( (Double) value ).doubleValue( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL
				.equalsIgnoreCase( dataType ) )
		{
			NumberFormatter formatter = new NumberFormatter( locale );
			formatter.applyPattern( format );
			return formatter.format( ( (BigDecimal) value ).doubleValue( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN
				.equalsIgnoreCase( dataType ) )
		{
			if ( ( (Boolean) value ).booleanValue( ) )
			{
				return getMessage( locale,
						BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY );
			}

			return getMessage( locale,
					BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType ) )
		{
			StringFormatter formatter = new StringFormatter( locale );
			formatter.applyPattern( format );
			return formatter.format( (String) value );
		}
		else
		{
			assert false;
			return null;
		}

	}

	/**
	 * Gets the display string for the value with the given data type and the
	 * locale. The value must be the valid data type.
	 * 
	 * @param dataType
	 *            the data type of the input value
	 * @param value
	 *            the input value to validate
	 * @param locale
	 *            the locale information
	 * @return the formatted string
	 *  
	 */

	static private String getDisplayValue( String dataType, Object value,
			Locale locale )
	{
		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equalsIgnoreCase( dataType ) )
		{
			DateFormat formatter = DateFormat.getDateTimeInstance(
					DateFormat.SHORT, DateFormat.MEDIUM, locale );
			return formatter.format( (Date) value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT
				.equalsIgnoreCase( dataType ) )
		{
			NumberFormat formatter = NumberFormat.getNumberInstance( locale );
			return formatter.format( ( (Double) value ).doubleValue( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL
				.equalsIgnoreCase( dataType ) )
		{
			NumberFormat formatter = NumberFormat.getNumberInstance( locale );
			return formatter.format( ( (BigDecimal) value ).doubleValue( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN
				.equalsIgnoreCase( dataType ) )
		{
			if ( ( (Boolean) value ).booleanValue( ) )
			{
				return getMessage( locale,
						BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY );
			}

			return getMessage( locale,
					BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType ) )
		{
			return (String) value;
		}
		else
		{
			assert false;
			return null;
		}
	}
}