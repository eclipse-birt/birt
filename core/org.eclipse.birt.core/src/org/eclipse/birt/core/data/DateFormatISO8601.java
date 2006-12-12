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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;

/**
 * DateFormatISO8601 is a utility class for formatting and parsing dates
 * according to date format defined by ISO8601.
 */

public class DateFormatISO8601
{

	/**
	 * Parse a date/time string.
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date parse( String source ) throws BirtException,
			ParseException
	{
		if( source == null )
		{
			return null;
		}
		Date resultDate = null;
		source = cleanDate( source );
		SimpleDateFormat simpleDateFormatter = DateFormatFactory.getPatternInstance( PatternKey.getPatterKey( source ) );
		if ( simpleDateFormatter != null )
		{
			try
			{
				resultDate = simpleDateFormatter.parse( source );
				return resultDate;
			}
			catch ( ParseException e1 )
			{
			}
		}
		// for the String can not be parsed, throws a BirtException
		if ( resultDate == null )
		{
			throw new ParseException( "an not convert the value of " + source,
					0 );
		}

		// never access here
		return resultDate;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	private static String cleanDate( String s )
	{
		s = s.trim( );
		if ( s.indexOf( 'T' ) < 12 )
		{
			s = s.replaceFirst( "T", " " );
		}
		int zoneIndex = s.indexOf( 'Z' );
		if ( zoneIndex == s.length( ) - 1 )
		{
			return s.substring( 0, zoneIndex );
		}
		
		return s.trim( );
	}

	

}
