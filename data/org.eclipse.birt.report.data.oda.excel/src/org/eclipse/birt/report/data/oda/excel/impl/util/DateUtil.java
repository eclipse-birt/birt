
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * A utility class. The convert method converts the source object into an Date
 * object given specified type. If no reasonable conversion can be made, throw a
 * OdaException.
 */
public final class DateUtil
{

	// Defalult Locale, if we have any problem parse string to date for Locale.getDefault()
	// we will try to parse it for Locale.US
	private static ULocale DEFAULT_LOCALE = ULocale.US;
	
	//JRE Default Locale used for formatting Date 
	private static ULocale JRE_DEFAULT_LOCALE = ULocale.getDefault(ULocale.Category.FORMAT);
	
	private static SimpleDateFormat MysqlUSDateFormatter = new SimpleDateFormat( "M/d/yyyy HH:mm" );

	// Default Date/Time Style 
	private static int DEFAULT_DATE_STYLE = DateFormat.MEDIUM;
	
	private static Pattern p1 = Pattern.compile( ".*[0-9]+:[0-9]+:[0-9]+.*" );//$NON-NLS-1$
	private static Pattern p2 = Pattern.compile( ".*[0-9]+:[0-9]+.*" );//$NON-NLS-1$
	
	private static DateFormat cachedDateFormat = null;
	
	/**
	 * Number -> Date
	 * 		new Date((long)Number)
	 * String -> Date
	 * 		toDate(String)  
	 * @param source
	 * @return
	 * @throws OdaException
	 */
	public static Date toDate( Object source ) throws OdaException
	{
		if ( source == null )
			return null;
		
		if ( source instanceof Date )
		{
			return new Date( ( (Date) source ).getTime( ) );
		}
		else if ( source instanceof String )
		{
			return toDate( (String) source );
		}
		else
		{
			throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$
		}
	}
    
    /**
     * Date -> Time
     * String -> Time
     * @param source
     * @return
     * @throws OdaException
     */
    public static Time toSqlTime( Object source ) throws OdaException
    {
        if ( source == null )
            return null;

        if ( source instanceof Date )
        {
       		return toSqlTime( (Date)source);
        }
        else if ( source instanceof String )
        {
            try
            {
                return toSqlTime( toDate((String ) source) );
            }
            catch( Exception e )
            {
                try
                {
                	return toSqlTime( (String)source );
                }
                catch ( Exception e1 )
                {
                	
                }
            }
        }

        throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$
    }

    /**
     * 
     * @param date
     * @return
     */
    private static java.sql.Time toSqlTime( Date date )
    {
    	Calendar calendar = Calendar.getInstance( );
		calendar.clear( );
		calendar.setTimeInMillis( date.getTime( ) );
		calendar.set( Calendar.YEAR, 1970 );
		calendar.set( Calendar.MONTH, 0 );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		calendar.set( Calendar.MILLISECOND, 0 );
		return new java.sql.Time( calendar.getTimeInMillis( ) );
    }
    
    /**
     * 
     * @param value
     * @return
     */
    private static Time toSqlTime( String s )
    {
		int hour;
		int addHour;
		int minute;
		int second;
		int firstColon;
		int secondColon;
		int marker;
		
		if ( s == null )
			throw new java.lang.IllegalArgumentException( );

		firstColon = s.indexOf( ':' );
		secondColon = s.indexOf( ':', firstColon + 1 );
		for ( marker = secondColon + 1; marker < s.length( ); marker++ )
		{
			if ( !isDigitTen( s.charAt( marker ) ) )
				break;
		}
		addHour = 0;
		String markerValue = null;
		if ( marker < s.length( ) )
		{
			markerValue = s.substring( marker ).trim( );
			if ( "am".compareToIgnoreCase( markerValue ) == 0 ) //$NON-NLS-1$
			{
				addHour = 0;
			}
			else if ( "pm".compareToIgnoreCase( markerValue ) == 0 ) //$NON-NLS-1$
			{
				addHour = 12;
			}
			else
			{
				throw new java.lang.IllegalArgumentException( );
			}
		}
		if ( firstColon <= 0  ||
				secondColon <= 0 || secondColon >= s.length( ) - 1 )
		{
			throw new java.lang.IllegalArgumentException( );
		}
		hour = Integer.parseInt( s.substring( 0, firstColon ) );
		if ( hour < 0 ||
				( hour > 12 && markerValue != null && markerValue.length( ) > 0 ) )
			throw new java.lang.IllegalArgumentException( );
		hour += addHour;
		if( hour > 24 )
			throw new java.lang.IllegalArgumentException( );
		minute = Integer.parseInt( s.substring( firstColon + 1, secondColon ) );
		if( minute < 0 || minute > 60 )
			throw new java.lang.IllegalArgumentException( );
		if ( marker < s.length( ) )
			second = Integer.parseInt( s.substring( secondColon + 1, marker ) );
		else
			second = Integer.parseInt( s.substring( secondColon + 1 ) );
		if( second < 0 || second > 60 )
			throw new java.lang.IllegalArgumentException( );
		
		return toSqlTime( hour, minute, second );
	}
    
    /**
     * 
     * @param c
     * @return
     */
    private static boolean isDigitTen( char c )
	{
		if ( c <= '9' && c >= '0' )
			return true;
		return false;
	}
    
    /**
     * 
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    private static Time toSqlTime( int hour, int minute, int second )
    {
    	Calendar calendar = Calendar.getInstance( );
		calendar.clear( );
		calendar.set( Calendar.HOUR_OF_DAY, hour );
		calendar.set( Calendar.MINUTE, minute );
		calendar.set( Calendar.SECOND, second );
		return new java.sql.Time( calendar.getTimeInMillis( ) );
    }
    
    /**
     * Date -> Time
     * String -> Time
     * @param source
     * @return
     * @throws OdaException
     */
    public static java.sql.Date toSqlDate( Object source ) throws OdaException
    {
        if ( source == null )
            return null;

        if ( source instanceof Date )
        {
    		return toSqlDate( (Date)source );
        }
        else if ( source instanceof String )
        {
            try
            {
                return toSqlDate( toDate((String ) source) );
            }
            catch( Exception e )
            {
                try
                {
                	return java.sql.Date.valueOf( (String)source );
                }
                catch ( Exception e1 )
                {
                	
                }
            }
        }

        throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$ 
    }
    
    /**
     * 
     * @param date
     * @return
     */
    private static java.sql.Date toSqlDate( Date date )
    {
    	Calendar calendar = Calendar.getInstance( );
		calendar.clear( );
		calendar.setTimeInMillis( date.getTime( ) );
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );		
		return new java.sql.Date( calendar.getTimeInMillis( ) );
    }
    
    /**
	 * A temp solution to the adoption of ICU4J to BIRT. Simply delegate
	 * toDate( String, Locale) method.
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	public static Date toDate( String source, Locale locale )
			throws OdaException
	{
		return toDate( source, ULocale.forLocale( locale ) );
	}

	/**
	 * convert String with the specified locale to java.util.Date
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	public static Date toDate( String source, ULocale locale )
			throws OdaException
	{
		if ( source == null )
			return null;

		DateFormat dateFormat = null;
		Date resultDate = null;
		
		boolean existTime = p1.matcher( source ).matches( )
				|| p2.matcher( source ).matches( );
		
		if ( cachedDateFormat != null )
		{
			try
			{
				resultDate = cachedDateFormat.parse( source );
				return resultDate;
			}
			catch ( ParseException e1 )
			{
				cachedDateFormat = null;
			}
		}

		for ( int i = DEFAULT_DATE_STYLE; i <= DateFormat.SHORT; i++ )
		{
			for ( int j = DEFAULT_DATE_STYLE; j <= DateFormat.SHORT; j++ )
			{
				dateFormat = DateFormatFactory.getDateTimeInstance( i,
						j,
						locale );
				try
				{
					resultDate = dateFormat.parse( source );
					cachedDateFormat = dateFormat;
					return resultDate;
				}
				catch ( ParseException e1 )
				{
				}
			}

			// only Date, no Time
			if ( !existTime )
			{
				dateFormat = DateFormatFactory.getDateInstance( i, locale );
				try
				{
					resultDate = dateFormat.parse( source );
					return resultDate;
				}
				catch ( ParseException e1 )
				{
				}
			}
		}
		throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$
	}

	/**
	 * Convert String without specified locale to java.util.Date
	 * Try to format the given String for JRE default Locale,
	 * if it fails, try to format the String for Locale.US 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	private static Date toDate( String source ) throws OdaException
	{
		try
		{
			return toDateISO8601( source );
		}
		catch ( OdaException e )
		{
			try
			{
				// format the String for JRE default locale
				return toDate( source, JRE_DEFAULT_LOCALE );
			}
			catch ( OdaException use )
			{
				try
				{
					// format the String for Locale.US
					return toDate( source, DEFAULT_LOCALE );
				}
				catch ( OdaException de )
				{
					return toDateForSpecialFormat( source );
				}
			}
		}
	}

	private static Date toDateForSpecialFormat( String source ) throws OdaException
	{
		try
		{
			return MysqlUSDateFormatter.parse( source );
		}
		catch ( ParseException e1 )
		{
			throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$
		}
	}
	
	/**
	 * convert String with ISO8601 date format to java.util.Date
	 * 
	 * @param source
	 *            the String to be convert
	 * @param locate
	 * 			  the locate of the string
	 * @return result Date
	 */
	private static Date toDateISO8601( String source ) throws OdaException
	{
		Date resultDate = null;

		try
		{
			resultDate = DateFormatISO8601.parse( source );
			return resultDate;
		}
		catch ( ParseException e1 )
		{
			throw new OdaException( Messages.getString( "dateUtil.ConvertFails" ) + source.toString( ) ); //$NON-NLS-1$ 
		}
	}
}
