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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Date util class, which is used to check whether String can be correctly
 * converted to Date.
 */
public class DateUtil
{
	
	/**
	 * All possible split char in differnt Locales.
	 * '/'
	 * 	Locale_US
	 * 	Locale_UK
	 * 	...
	 * '-'
	 * 	Locale_CHINA
	 * '.'
	 * 	Locale_GERMAN
	 */
	private static String[] splitStrs = new String[]{
			"/", "-", "."
	};
	
	/**
	 * Check whether dateStr can be correctly converted to Date in 
	 * format of DateFormat.SHORT. Here one point must be noticed that
	 * dateStr should firstly be able to be converted to Date.
	 * 
	 * @param df
	 * @param dateStr
	 * @return checkinfo
	 */
	public static boolean checkValid( DateFormat df, String dateStr )
	{
		assert df != null;
		assert dateStr != null;
		
		boolean isValid = true;
		if ( df instanceof SimpleDateFormat )
		{			
			String[] dateResult = splitDateStr( dateStr );

			SimpleDateFormat sdf = (SimpleDateFormat) df;
			String pattern = sdf.toPattern( );
			String[] patternResult = splitDateStr( pattern );
			
			if ( dateResult != null && patternResult != null )
			{
				isValid = isMatch( dateResult, patternResult );
			}
		}

		return isValid;
	}
	
	/**
	 * Split date string to 3 size of string array
	 * example:
	 * 		05/04/2005 [05, 04, 2005]
	 * 		MM/dd/yy [MM, dd, yy]
	 * 
	 * @param dateStr
	 * @return
	 */
	private static String[] splitDateStr( String dateStr )
	{
		String splitChar = null;
		for ( int i = 0; i < splitStrs.length; i++ )
		{
			if ( dateStr.indexOf( splitStrs[i] ) >= 0 )
			{
				splitChar = splitStrs[i];
				break;
			}
		}
		if ( splitChar == null )
		{
			return null;
		}
		
		String[] result = dateStr.split( splitChar );
		if ( result.length != 3 )
		{
			return null;
		}

		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = result[i].trim();
		}
		
		return result;
	}
	
	/**
	 * Check whether dateStr matches patterStr
	 * 
	 * @param dateStr
	 * @param patternStr
	 * @return  true match
	 * 			false does not match
	 */
	private static boolean isMatch( String[] dateStr, String[] patternStr )
	{
		assert dateStr != null;
		assert patternStr != null;
		
		boolean result = true;
		
		for ( int i = 0; i < dateStr.length; i++ )
		{
			int value = Integer.valueOf( dateStr[i] ).intValue( );
			if ( patternStr[i].startsWith( "y" )
					|| patternStr[i].startsWith( "Y" ) )
			{
				if ( value < 0 )
				{
					result = false;
					break;
				}
			}
			else if ( patternStr[i].startsWith( "M" )
					|| patternStr[i].startsWith( "m" ) )
			{
				if ( value < 1 || value > 12 )
				{
					result = false;
					break;
				}
			}
			else if ( patternStr[i].startsWith( "d" )
					|| patternStr[i].startsWith( "D" ) )
			{
				if ( value < 1 || value > 31 )
				{
					result = false;
					break;
				}
			}
		}

		return result;
	}
	
}
