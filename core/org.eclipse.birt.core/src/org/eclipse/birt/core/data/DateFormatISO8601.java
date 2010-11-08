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
import java.util.Date;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.TimeZone;

/**
 * DateFormatISO8601 is a utility class for formatting and parsing dates
 * according to date format defined by ISO8601.
 */

public class DateFormatISO8601
{

	private static Pattern T_PATTERN = Pattern.compile( "T" );
	
	/**
	 * Parse a date/time string.
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date parse( String source, TimeZone timeZone ) throws BirtException,
			ParseException
	{
		if ( source == null || source.trim( ).length( ) == 0 )
		{
			return null;
		}
		Date resultDate = null;
		source = cleanDate( source );
		Object simpleDateFormatter = DateFormatFactory.getPatternInstance( PatternKey.getPatterKey( source ) );
		if ( simpleDateFormatter != null )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) simpleDateFormatter;
			TimeZone savedTimeZone = null;
			try
			{
				if ( timeZone != null )
				{
					savedTimeZone = dateFormat.getTimeZone( );
					dateFormat.setTimeZone( timeZone );
				}
				resultDate = dateFormat.parse( source );
				return resultDate;
			}
			catch ( ParseException e1 )
			{
			}
			finally
			{
				if ( savedTimeZone != null )
					dateFormat.setTimeZone( savedTimeZone );
			}
		}
		// for the String can not be parsed, throws a BirtException
		if ( resultDate == null )
		{
			throw new CoreException( ResourceConstants.CONVERT_FAILS,
					new Object[]{source.toString( ), "Date"} );
		}

		// never access here
		return resultDate;
	}
	
	
	/**
	 * Parse a date/time string.
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static String format( Date date ) throws BirtException
	{
		String result = null;
		if ( date == null  )
		{
			return null;
		}
		
		Object simpleDateFormatter = DateFormatFactory.getPatternInstance( PatternKey.getPatterKey( "yyyy-MM-dd HH:mm:ss.sZ" ) );
		if ( simpleDateFormatter != null )
		{
			try
			{
				result = ( (SimpleDateFormat) simpleDateFormatter ).format( date );
				return result;
			}
			catch ( Exception e1 )
			{
			}
		}
		// for the String can not be parsed, throws a BirtException
		throw new CoreException( ResourceConstants.CONVERT_FAILS, new Object[]{
				date.toString( ), "ISO8601 Format"
		} );
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
			s = T_PATTERN.matcher( s ).replaceFirst( " " );//$NON-NLS-1$ //$NON-NLS-2$
		}
		
//		int zoneIndex = s.indexOf( "GMT" ); //$NON-NLS-1$
//		if( zoneIndex > 0 )
//		{
//			return s.substring( 0, zoneIndex ).trim( );
//		}
		int zoneIndex = s.indexOf( 'Z' );
		if ( zoneIndex == s.length( ) - 1 )
		{
			return s.substring( 0, zoneIndex ).trim( );
		}
//		zoneIndex = getZoneIndex( s );
//		if ( zoneIndex > 0 )
//		{
//			return s.substring( 0, zoneIndex ).trim( );
//		}
		
		return s;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private static int getZoneIndex( String s )
	{
		int index = s.indexOf( '+' );
		if ( index > 0 )
		{
			return index;
		}
		
		index = s.indexOf( '-' ); //first '-'
		if ( index > 0 )
		{
			index = s.indexOf( '-', index + 1 ); //second '-'
		}
		else
		{
			return index;
		}
		if ( index > 0 )
		{
			index = s.indexOf( '-', index + 1 ); //third '-'
		}
		else
		{
			return index;
		}
		return index;
	}
	

}
