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

import java.util.Locale;

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
}
