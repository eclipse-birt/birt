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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.core.data.DateFormatISO8601;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;

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
	 * 
	 * Convert parameter to Object
	 * 
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @return Object
	 * @throws ValidationValueException
	 */
	public static Object validate( String dataType, String format,
			String value, Locale locale ) throws ValidationValueException
	{
		Object obj = null;

		try
		{
			// Convert locale string to object
			obj = ParameterValidationUtil.validate( dataType, format, value,
					locale );
		}
		catch ( ValidationValueException e1 )
		{
			// Convert string to object using default format/local
			format = null;
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
					.equalsIgnoreCase( dataType ) )
			{
				format = ParameterValidationUtil.DEFAULT_DATETIME_FORMAT;
			}

			obj = ParameterValidationUtil.validate( dataType, format, value );
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
	 * Change default value format from design file.
	 * <p>
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then convert
	 * old ISO8601 datetime format to standard format.</li>
	 * </li>
	 * </ul>
	 * 
	 * @param dataType
	 *            the parameter data type
	 * @param defaultValue
	 *            the default value from design file
	 * @return
	 */
	public static String getDefaultValue( String dataType, String defaultValue )
	{
		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equalsIgnoreCase( dataType ) )
		{
			Date obj = null;
			try
			{
				// Current datetime format is ISO8601
				obj = DateFormatISO8601.parse( defaultValue );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			return getDisplayValue( obj );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN
				.equalsIgnoreCase( dataType ) )
		{
			return getDisplayValue( Boolean.valueOf( defaultValue ) );
		}

		return defaultValue;
	}
}
