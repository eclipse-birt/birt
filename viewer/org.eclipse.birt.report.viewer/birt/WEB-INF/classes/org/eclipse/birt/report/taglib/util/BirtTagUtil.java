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

package org.eclipse.birt.report.taglib.util;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Utilities for Birt tags
 * 
 */
public class BirtTagUtil
{

	/**
	 * Convert String to correct boolean value.
	 * 
	 * @param bool
	 * @return
	 */
	public static String convertBooleanValue( String bool )
	{
		boolean b = Boolean.valueOf( bool ).booleanValue( );
		return String.valueOf( b );
	}

	/**
	 * Convert String to boolean.
	 * 
	 * @param bool
	 * @return
	 */
	public static boolean convertToBoolean( String bool )
	{
		if ( bool == null )
			return false;

		return Boolean.valueOf( bool ).booleanValue( );
	}

	/**
	 * Returns the output format.Default value is html.
	 * 
	 * @param format
	 * @return
	 */
	public static String getFormat( String format )
	{
		if ( format == null || format.length( ) <= 0 )
			return ParameterAccessor.PARAM_FORMAT_HTML;

		if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_HTM ) )
			return ParameterAccessor.PARAM_FORMAT_HTML;

		return format;
	}

	/**
	 * Get report locale.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param locale
	 *            String
	 * @return locale
	 */

	public static Locale getLocale( HttpServletRequest request, String sLocale )
	{
		Locale locale = null;

		// Get Locale from String value
		locale = ParameterAccessor.getLocaleFromString( sLocale );

		// Get Locale from client browser
		if ( locale == null )
			locale = request.getLocale( );

		// Get Locale from Web Context
		if ( locale == null )
			locale = ParameterAccessor.webAppLocale;

		return locale;
	}
}
