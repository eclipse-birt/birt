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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * DateFormatISO8601 is a utility class for formatting and parsing dates
 * according to date format defined by ISO8601.
 */

public class DateFormatISO8601
{

	// all SimpleDateFormatter of ICU
	private static Map simpleDateFormatterMap = null;

	static
	{
		// date format pattern defined in ISO8601
		// notice the order is significant.
		String[] dateFormatPattern = {
				"yyyy-MM-dd HH:mm:ss.S z",
				"yyyy-MM-dd HH:mm:ss.Sz",
				"yyyy-MM-dd HH:mm:ss.S",
				"yyyy-MM-dd HH:mm:ss z",
				"yyyy-MM-dd HH:mm:ssz",
				"yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd HH:mm z",
				"yyyy-MM-dd HH:mmz",
				"yyyy-MM-dd HH:mm",
				"yyyy-MM-dd",
				"yyyy-MM",
				"yyyy"
		};
		SimpleDateFormat simpleDateFormatter = null;
		PatternKey patterKey = null;

		simpleDateFormatterMap = new HashMap( 64 );
		for ( int i = 0; i < dateFormatPattern.length; i++ )
		{
			patterKey = getPatterKey( dateFormatPattern[i] );
			simpleDateFormatter = new SimpleDateFormat( dateFormatPattern[i] );
			simpleDateFormatter.setLenient( false );
			//simpleDateFormatter.setCalendar( Calendar.getInstance( TimeZone.getTimeZone( "GMT" ) ) );
			simpleDateFormatterMap.put( patterKey, simpleDateFormatter );
		}
	}

	/**
	 * Parse a date/time string.
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date parse( String source ) throws BirtException,
			ParseException
	{
		Date resultDate = null;
		source = cleanDate( source );
		Object simpleDateFormatter = simpleDateFormatterMap.get( getPatterKey( source ) );
		if ( simpleDateFormatter != null )
		{
			try
			{
				resultDate = ( (SimpleDateFormat) simpleDateFormatter ).parse( source );
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
		
		return s;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	private static PatternKey getPatterKey( String source )
	{
		int colonNumber = 0;
		int blankNumber = 0;
		int hyphenNumber = 0;
		int dotNumber = 0;
		int timeZomeNumber = 0;
		boolean beLastBlank = false;

		for ( int i = 0; i < source.length( ); i++ )
		{
			switch ( source.charAt( i ) )
			{
				case ':' :
				{
					beLastBlank = false;
					colonNumber++;
					break;
				}

				case ' ' :
				{
					if ( !beLastBlank )
					{
						blankNumber++;
					}
					beLastBlank = true;
					break;
				}
				case '-' :
				{
					beLastBlank = false;
					if ( blankNumber == 0 )
					{
						hyphenNumber++;
					}
					else
					{
						timeZomeNumber++;
					}
					break;
				}
				case '.' :
				{
					beLastBlank = false;
					dotNumber++;
					break;
				}
				case '+' :
				case 'z' :
				case 'Z' :
				{
					beLastBlank = false;
					timeZomeNumber++;
					break;
				}
			}
			if ( timeZomeNumber > 0 )
			{
				break;
			}
		}

		if ( hyphenNumber == 0 && colonNumber == 0 && source.length( ) > 4 )
		{
			return null;
		}

		return ( new PatternKey( colonNumber,
				blankNumber,
				hyphenNumber,
				dotNumber,
				timeZomeNumber ) );
	}

}

/**
 * A class used as hash key of date format pattern.
 *
 */
class PatternKey
{
	private int colonNumber;
	private int blankNumber;
	private int hyphenNumber;
	private int dotNumber;
	private int timeZomeNumber;

	PatternKey( int colonNumber, int blankNumber, int hyphenNumber,
			int dotNumber, int timeZomeNumber )
	{
		this.colonNumber = colonNumber;
		this.blankNumber = blankNumber;
		this.hyphenNumber = hyphenNumber;
		this.dotNumber = dotNumber;
		this.timeZomeNumber = timeZomeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		return colonNumber
				* 36 + blankNumber * 12 + hyphenNumber * 4 + dotNumber * 2
				+ timeZomeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object key )
	{
		PatternKey patterKey = (PatternKey) key;
		return patterKey.colonNumber == this.colonNumber
				|| patterKey.blankNumber == this.blankNumber
				|| patterKey.hyphenNumber == this.hyphenNumber
				|| patterKey.dotNumber == this.dotNumber
				|| patterKey.timeZomeNumber == this.timeZomeNumber;
	}
}
