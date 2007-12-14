/*******************************************************************************
* Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.format;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * DateFormatter.
 * 
 * Design for Class DateFormatter.This version is for open source, so we only
 * apply the function which Java has provided. Beside these basic function, in
 * this version, we also provide some new API for further implementation in the
 * future
 * 
 */
public class DateFormatter
{

	private static final String UNFORMATTED = "Unformatted";
	public static final String DATETIME_UNFORMATTED = "DateTime" + UNFORMATTED;
	public static final String DATE_UNFORMATTED = "Date" + UNFORMATTED;
	public static final String TIME_UNFORMATTED = "Time" + UNFORMATTED;

	/**
	 * Comment for <code>formatPattern</code> record the string pattern
	 */
	private String formatPattern;

	/**
	 * Comment for <code>dateTimeFormat</code> used for two methods,
	 * createDateFormat() and format()
	 */
	com.ibm.icu.text.DateFormat dateTimeFormat;

	com.ibm.icu.text.DateFormat timeFormat;

	com.ibm.icu.text.DateFormat dateFormat;
	/**
	 * Comment for <code>locale</code> used for record Locale information
	 */
	private ULocale locale = ULocale.getDefault( );

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( DateFormatter.class.getName( ) );
	
	static protected final int LOCALE_CACHE_SIZE = 10;
	
	static protected final int PATTERN_CACHE_SIZE = 20;

	static CacheHashMap localeCache = new CacheHashMap( LOCALE_CACHE_SIZE );

	static class CacheHashMap extends LinkedHashMap
	{

		private static final long serialVersionUID = -2740310231997296948L;
		int maxEntry;

		public CacheHashMap( int maxEntry )
		{
			super( maxEntry, 0.75f, true );
			this.maxEntry = maxEntry;
		}

		protected boolean removeEldestEntry( Map.Entry eldest )
		{
			return size( ) > maxEntry;
		}
	}
	
	
	
	static protected synchronized void putCachedFormat( ULocale locale, String pattern,
			com.ibm.icu.text.DateFormat[] formats )
	{
		Map map = (Map) localeCache.get( locale );
		if ( map == null )
		{
			map = new CacheHashMap(PATTERN_CACHE_SIZE);
			localeCache.put( locale, map );
		}
		map.put( pattern, formats );
	}
	
	static protected synchronized com.ibm.icu.text.DateFormat[] getCachedFormat(
			ULocale locale, String pattern )
	{
		Map map = (Map) localeCache.get( locale );
		if ( map == null )
		{
			if ( map == null )
			{
				map = new CacheHashMap(PATTERN_CACHE_SIZE);
				localeCache.put( locale, map );
			}
		}
		return (com.ibm.icu.text.DateFormat[]) map.get( pattern );
	}

	/**
	 * constuctor method with no paremeter
	 */
	public DateFormatter( )
	{
		applyPattern( null );
	}

	/**
	 * constuctor method with String parameter
	 * 
	 * @param pattern
	 */
	public DateFormatter( String pattern )
	{
		applyPattern( pattern );
	}

	/**
	 * constuctor method with Locale parameters
	 * 
	 * @param localeLoc
	 */
	public DateFormatter( ULocale localeLoc )
	{
		// Leave locale to default if none provided
		if ( localeLoc != null )
			locale = localeLoc;
		applyPattern( null );
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public DateFormatter( Locale localeLoc )
	{
		this( ULocale.forLocale( localeLoc ) );
	}

	/**
	 * constuctor method with two parameters, one is String type while the other
	 * is Locale type
	 * 
	 * @param pattern
	 * @param localeLoc
	 */
	public DateFormatter( String pattern, ULocale localeLoc )
	{
		// Leave locale to default if none provided
		if ( localeLoc != null )
			locale = localeLoc;
		applyPattern( pattern );
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public DateFormatter( String pattern, Locale localeLoc )
	{
		this( pattern, ULocale.forLocale( localeLoc ) );
	}

	/**
	 * get the string pattern
	 * 
	 * @return
	 */
	public String getPattern( )
	{
		return this.formatPattern;
	}
	
	
	

	/**
	 * define pattern and locale here
	 * 
	 * @param formatString
	 */
	public void applyPattern( String formatString )
	{
		this.formatPattern = formatString;
		this.dateTimeFormat = null;
		this.dateFormat = null;
		this.timeFormat = null;

		if ( formatString == null || UNFORMATTED.equals( formatString ) )
		{
			formatPattern = UNFORMATTED;
		}
	
		com.ibm.icu.text.DateFormat[] formats = getCachedFormat( locale, formatString );
		if (formats != null)
		{
			this.dateTimeFormat = formats[0];
			this.dateFormat = formats[1];
			this.timeFormat = formats[2];
		}
		else
		{
			doApplyPattern( formatPattern);
			formats = new com.ibm.icu.text.DateFormat[3];
			formats[0] = this.dateTimeFormat;
			formats[1] = this.dateFormat;
			formats[2] = this.timeFormat;
			putCachedFormat(locale, formatString, formats);
		}
		return;
	}
	
	protected void doApplyPattern( String formatString )
	{
		try
		{
			/*
			 * we can seperate these single name-based patterns form those
			 * patterns with multinumber letters
			 */
			if ( UNFORMATTED == formatString )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat
						.getDateTimeInstance(
								com.ibm.icu.text.DateFormat.MEDIUM,
								com.ibm.icu.text.DateFormat.SHORT, locale );
				dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
						com.ibm.icu.text.DateFormat.MEDIUM, locale );
				timeFormat = com.ibm.icu.text.DateFormat.getTimeInstance(
						com.ibm.icu.text.DateFormat.MEDIUM, locale );
				return;
			}
			else if ( formatString.equals( DATETIME_UNFORMATTED ) ) //$NON-NLS-1$
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.MEDIUM,
						com.ibm.icu.text.DateFormat.SHORT,
						locale );
				return;

			}
			else if ( formatString.equals( DATE_UNFORMATTED ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.MEDIUM,
						locale );
				return;
			}
			else if ( formatString.equals( TIME_UNFORMATTED ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.MEDIUM,
						locale );
				return;
			}

			if ( formatString.length( ) == 1 )
			{
				char patternTemp = formatString.charAt( 0 );
				switch ( patternTemp )
				{
					case 'G' :
						dateTimeFormat = com.ibm.icu.text.DateFormat
								.getDateTimeInstance(
										com.ibm.icu.text.DateFormat.LONG,
										com.ibm.icu.text.DateFormat.LONG,
										locale );
						dateFormat = com.ibm.icu.text.DateFormat
								.getDateInstance(
										com.ibm.icu.text.DateFormat.LONG,
										locale );
						timeFormat = com.ibm.icu.text.DateFormat
								.getTimeInstance(
										com.ibm.icu.text.DateFormat.LONG,
										locale );
						return;
					case 'D' :

						dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.LONG,
								locale );
						return;
					case 'd' :

						dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.SHORT,
								locale );
						return;
					case 'T' :

						dateTimeFormat = com.ibm.icu.text.DateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.LONG,
								locale );
						return;
					case 't' :
						dateTimeFormat = new SimpleDateFormat( "HH:mm", locale );
						return;
					case 'f' :
						dateTimeFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.LONG,
								com.ibm.icu.text.DateFormat.SHORT,
								locale );
						return;
					case 'F' :
						dateTimeFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.LONG,
								com.ibm.icu.text.DateFormat.LONG,
								locale );
						return;

						// I/i produces a short (all digit) date format with 4-
						// digit years
						// and a medium/long time
						// Unfortunately SHORT date format returned by
						// DateFormat is always 2-digits
						// We will need to create our own SimpleDateFormat based
						// on what the
						// DateTime factory gives us
					case 'i' :
					case 'I' :
						int timeForm = ( patternTemp == 'i' )
								? com.ibm.icu.text.DateFormat.MEDIUM
								: com.ibm.icu.text.DateFormat.LONG;
						timeFormat = com.ibm.icu.text.DateFormat
								.getTimeInstance( timeForm, locale );

						com.ibm.icu.text.DateFormat factoryFormat = com.ibm.icu.text.DateFormat
								.getDateInstance(
										com.ibm.icu.text.DateFormat.SHORT,
										locale );
						dateFormat = hackYear( factoryFormat );

						factoryFormat = com.ibm.icu.text.DateFormat
								.getDateTimeInstance(
										com.ibm.icu.text.DateFormat.SHORT,
										timeForm, locale );
						dateTimeFormat = hackYear( factoryFormat );
						return;

					case 'g' :

						dateTimeFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.SHORT,
								com.ibm.icu.text.DateFormat.SHORT,
								locale );
						return;
					case 'M' :
					case 'm' :
						dateTimeFormat = new SimpleDateFormat( "MM/dd", locale );
						return;
					case 'R' :
					case 'r' :
						dateTimeFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss a",
								locale );
						dateTimeFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
						return;
					case 's' :
						dateTimeFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss",
								locale );
						return;
					case 'u' :
						dateTimeFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss  Z",
								locale );
						return;
						// TODO:the defination is not clear enough
						/*
						 * case 'U': return;
						 */
					case 'Y' :
					case 'y' :
						dateTimeFormat = new SimpleDateFormat( "yyyy/mm", locale );
						return;
					default :
						dateTimeFormat = new SimpleDateFormat( formatString, locale );
						return;
				}
			}

			/*
			 * including the patterns which Java accepted and those name-based
			 * patterns with multinumber letters
			 */
			if ( formatString.equals( "General Date" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.LONG,
						com.ibm.icu.text.DateFormat.LONG,
						locale );
				return;
			}
			if ( formatString.equals( "Long Date" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.LONG,
						locale );
				return;

			}
			if ( formatString.equals( "Medium Date" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.MEDIUM,
						locale );
				return;

			}
			if ( formatString.equals( "Short Date" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getDateInstance( com.ibm.icu.text.DateFormat.SHORT,
						locale );
				return;

			}
			if ( formatString.equals( "Long Time" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.LONG,
						locale );
				return;

			}
			if ( formatString.equals( "Medium Time" ) )
			{
				dateTimeFormat = com.ibm.icu.text.DateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.MEDIUM,
						locale );
				return;

			}
			if ( formatString.equals( "Short Time" ) )
			{
				dateTimeFormat = new SimpleDateFormat( "kk:mm", locale );
				return;

			}
			dateTimeFormat = new SimpleDateFormat( formatString, locale );

		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	private com.ibm.icu.text.DateFormat hackYear(
			com.ibm.icu.text.DateFormat factoryFormat )
	{
		// Try cast this to SimpleDateFormat - DateFormat
		// JavaDoc says this should
		// succeed in most cases
		if ( factoryFormat instanceof SimpleDateFormat )
		{
			SimpleDateFormat factorySimpleFormat = (SimpleDateFormat) factoryFormat;

			String pattern = factorySimpleFormat.toPattern( );
			// Search for 'yy', then add a 'y' to make the year 4
			// digits
			if ( pattern.indexOf( "yyyy" ) == -1 )
			{
				int idx = pattern.indexOf( "yy" );
				if ( idx >= 0 )
				{
					StringBuffer strBuf = new StringBuffer( pattern );
					strBuf.insert( idx, 'y' );
					pattern = strBuf.toString( );
				}
			}
			return new SimpleDateFormat( pattern, locale );
		}
		return factoryFormat;
	}
	/*
	 * transfer the format string pattern from msdn to the string pattern which
	 * java can recognize
	 */
	public String format( Date date )
	{
		try
		{
			if ( date instanceof java.sql.Date )
			{
				if ( dateFormat != null )
				{
					return dateFormat.format( date );
				}
			}
			else if ( date instanceof java.sql.Time )
			{
				if ( timeFormat != null )
				{
					return timeFormat.format( date );
				}
			}
			return dateTimeFormat.format( date );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}

	/**
	 * Returns format code according to format type and current locale
	 */
	public String getFormatCode( )
	{
		String formatCode = null;
		if ( formatPattern.equals( "General Date" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance( com.ibm.icu.text.DateFormat.LONG,
					com.ibm.icu.text.DateFormat.LONG,
					locale );
			formatCode = dateFormat.toPattern( );
		}
		if ( formatPattern.equals( "Long Date" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance( com.ibm.icu.text.DateFormat.LONG,
					locale );
			formatCode = dateFormat.toPattern( );

		}
		if ( formatPattern.equals( "Medium Date" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance( com.ibm.icu.text.DateFormat.MEDIUM,
					locale );
			formatCode = dateFormat.toPattern( );

		}
		if ( formatPattern.equals( "Short Date" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance( com.ibm.icu.text.DateFormat.SHORT,
					locale );
			formatCode = dateFormat.toPattern( );

		}
		if ( formatPattern.equals( "Long Time" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.LONG,
					locale );
			formatCode = dateFormat.toPattern( );

		}
		if ( formatPattern.equals( "Medium Time" ) )
		{
			SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance( com.ibm.icu.text.DateFormat.MEDIUM,
					locale );
			formatCode = dateFormat.toPattern( );
		}
		if ( formatPattern.equals( "Short Time" ) )
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat( "kk:mm", locale );
			formatCode = dateFormat.toPattern( );
		}
		if ( UNFORMATTED.equals( formatPattern ) ||
				DATETIME_UNFORMATTED.equals( formatPattern ) ||
				DATE_UNFORMATTED.equals( formatPattern ) ||
				TIME_UNFORMATTED.equals( formatPattern ) )
		{
			formatCode = "";
		}
			
		return formatCode;
	}

	/**
	 * Parses the input string into a formatted date type.
	 * 
	 * @param date
	 *            the input string to parse
	 * @return the formatted date
	 * @throws ParseException
	 *             if the beginning of the specified string cannot be parsed.
	 */

	public Date parse( String date ) throws ParseException
	{
		return dateTimeFormat.parse( date );
	}
}