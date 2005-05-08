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

package org.eclipse.birt.core.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DateFormatter.
 * 
 * Design for Class DateFormatter.This version is for open source, so we only
 * apply the function which Java has provided. Beside these basic function, in
 * this version, we also provide some new API for further implementation in the
 * future
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:07:16 $
 */
public class DateFormatter
{

	/**
	 * Comment for <code>formatPattern</code> record the string pattern
	 */
	private String formatPattern;

	/**
	 * Comment for <code>dateFormat</code> used for two methods,
	 * createDateFormat() and format()
	 */
	private java.text.DateFormat dateFormat;

	/**
	 * Comment for <code>locale</code> used for record Locale information
	 */
	private Locale locale = Locale.getDefault( );

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( DateFormatter.class
			.getName( ) );

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
	public DateFormatter( Locale localeLoc )
	{
		locale = localeLoc;
		applyPattern( null );
	}

	/**
	 * constuctor method with two parameters, one is String type while the other
	 * is Locale type
	 * 
	 * @param pattern
	 * @param localeLoc
	 */
	public DateFormatter( String pattern, Locale localeLoc )
	{
		locale = localeLoc;
		applyPattern( pattern );
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
		try
		{
			this.formatPattern = formatString;

			/*
			 * we can seperate these single name-based patterns form those
			 * patterns with multinumber letters
			 */
			if ( formatString == null )
			{
				formatPattern = "";
				dateFormat = java.text.DateFormat.getDateTimeInstance(
						java.text.DateFormat.SHORT, java.text.DateFormat.SHORT,
						locale );
				return;

			}
			if ( formatString.length( ) == 1 )
			{
				char patternTemp = formatString.charAt( 0 );
				switch ( patternTemp )
				{
					case 'G' :
						dateFormat = java.text.DateFormat.getDateTimeInstance(
								java.text.DateFormat.LONG,
								java.text.DateFormat.LONG, locale );
						return;
					case 'D' :

						dateFormat = java.text.DateFormat.getDateInstance(
								java.text.DateFormat.LONG, locale );
						return;
					case 'd' :

						dateFormat = java.text.DateFormat.getDateInstance(
								java.text.DateFormat.SHORT, locale );
						return;
					case 'T' :

						dateFormat = java.text.DateFormat.getTimeInstance(
								java.text.DateFormat.LONG, locale );
						return;
					case 't' :
						dateFormat = new SimpleDateFormat( "HH:mm", locale );
						return;
					case 'f' :

						dateFormat = java.text.DateFormat.getDateTimeInstance(
								java.text.DateFormat.LONG,
								java.text.DateFormat.SHORT, locale );
						return;
					case 'F' :
						dateFormat = java.text.DateFormat.getDateTimeInstance(
								java.text.DateFormat.LONG,
								java.text.DateFormat.LONG, locale );
						return;
					case 'g' :

						dateFormat = java.text.DateFormat.getDateTimeInstance(
								java.text.DateFormat.SHORT,
								java.text.DateFormat.SHORT, locale );
						return;
					case 'M' :
					case 'm' :
						dateFormat = new SimpleDateFormat( "MM/dd", locale );
						return;
					case 'R' :
					case 'r' :
						dateFormat = new SimpleDateFormat(
								"yyyy.MM.dd HH:mm:ss a", locale );
						dateFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
						return;
					case 's' :
						dateFormat = new SimpleDateFormat(
								"yyyy.MM.dd HH:mm:ss", locale );
						return;
					case 'u' :
						dateFormat = new SimpleDateFormat(
								"yyyy.MM.dd HH:mm:ss  Z", locale );
						return;
					//TODO:the defination is not clear enough
					/*
					 * case 'U': return;
					 */
					case 'Y' :
					case 'y' :
						dateFormat = new SimpleDateFormat( "yyyy/mm", locale );
						return;
					default :
						dateFormat = new SimpleDateFormat( formatString, locale );
						return;
				}
			}

			/*
			 * including the patterns which Java accepted and those name-based
			 * patterns with multinumber letters
			 */
			if ( formatString.equals( "General Date" ) )
			{
				dateFormat = java.text.DateFormat.getDateTimeInstance(
						java.text.DateFormat.LONG, java.text.DateFormat.LONG,
						locale );
				return;
			}
			if ( formatString.equals( "Long Date" ) )
			{
				dateFormat = java.text.DateFormat.getDateInstance(
						java.text.DateFormat.LONG, locale );
				return;

			}
			if ( formatString.equals( "Medium Date" ) )
			{
				dateFormat = java.text.DateFormat.getDateInstance(
						java.text.DateFormat.MEDIUM, locale );
				return;

			}
			if ( formatString.equals( "Short Date" ) )
			{
				dateFormat = java.text.DateFormat.getDateInstance(
						java.text.DateFormat.SHORT, locale );
				return;

			}
			if ( formatString.equals( "Long Time" ) )
			{
				dateFormat = java.text.DateFormat.getTimeInstance(
						java.text.DateFormat.LONG, locale );
				return;

			}
			if ( formatString.equals( "Medium Time" ) )
			{
				dateFormat = java.text.DateFormat.getTimeInstance(
						java.text.DateFormat.MEDIUM, locale );
				return;

			}
			if ( formatString.equals( "Short Time" ) )
			{
				dateFormat = new SimpleDateFormat( "kk:mm", locale );
				return;

			}
			dateFormat = new SimpleDateFormat( formatString, locale );

		}

		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	/*
	 * transfer the format string pattern from msdn to the string pattern which
	 * java can recognize
	 */
	public String format( Date date )
	{
		try
		{
			return dateFormat.format( date );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
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
		return dateFormat.parse( date );
	}
}