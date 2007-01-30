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
import com.ibm.icu.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.TimeZone;
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
	private com.ibm.icu.text.DateFormat dateFormat;

	/**
	 * Comment for <code>locale</code> used for record Locale information
	 */
	private ULocale locale = ULocale.getDefault( );

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
		this(ULocale.forLocale(localeLoc));
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
		this(pattern, ULocale.forLocale(localeLoc));
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
			if ( formatString == null || "Unformatted".equals(formatString)) //$NON-NLS-1$
			{
				formatPattern = "";
				dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
						com.ibm.icu.text.DateFormat.MEDIUM, com.ibm.icu.text.DateFormat.SHORT,
						locale );
				return;

			}
			if ( formatString.length( ) == 1 )
			{
				char patternTemp = formatString.charAt( 0 );
				switch ( patternTemp )
				{
					case 'G' :
						dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
								com.ibm.icu.text.DateFormat.LONG,
								com.ibm.icu.text.DateFormat.LONG, locale );
						return;
					case 'D' :

						dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
								com.ibm.icu.text.DateFormat.LONG, locale );
						return;
					case 'd' :

						dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
								com.ibm.icu.text.DateFormat.SHORT, locale );
						return;
					case 'T' :

						dateFormat = com.ibm.icu.text.DateFormat.getTimeInstance(
								com.ibm.icu.text.DateFormat.LONG, locale );
						return;
					case 't' :
						dateFormat = new SimpleDateFormat( "HH:mm", locale );
						return;
					case 'f' :
						dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
								com.ibm.icu.text.DateFormat.LONG,
								com.ibm.icu.text.DateFormat.SHORT, locale );
						return;
					case 'F' :
						dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
								com.ibm.icu.text.DateFormat.LONG,
								com.ibm.icu.text.DateFormat.LONG, locale );
						return;
						
						// I/i produces a short (all digit) date format with 4- digit years
						// and a medium/long time
						// Unfortunately SHORT date format returned by DateFormat is always 2-digits
						// We will need to create our own SimpleDateFormat based on what the
						// DateTime factory gives us
                    case 'i' :
                    case 'I' :
                    	int timeForm =  ( patternTemp == 'i' ) ? com.ibm.icu.text.DateFormat.MEDIUM
                    			:com.ibm.icu.text.DateFormat.LONG;
                    		
                    	com.ibm.icu.text.DateFormat factoryFormat = 
                    		com.ibm.icu.text.DateFormat.getDateTimeInstance(
                                                com.ibm.icu.text.DateFormat.SHORT,
                                                timeForm, locale );
                        // Try cast this to SimpleDateFormat - DateFormat JavaDoc says this should
                    	// succeed in most cases
                    	SimpleDateFormat factorySimpleFormat;
                    	try
                    	{
                    		factorySimpleFormat = (SimpleDateFormat) factoryFormat;
                    	}
                    	catch (ClassCastException e)
                    	{
                    		// no help; stuck with what the factory gives us
                    		dateFormat = factoryFormat;
                    		return;
                    	}
                        
                    	String pattern = factorySimpleFormat.toPattern();
                    	// Search for 'yy', then add a 'y' to make the year 4 digits
                    	if (pattern.indexOf( "yyyy" ) == -1)
                    	{
							int idx = pattern.indexOf( "yy" );
							if ( idx >= 0 )
							{
								StringBuffer strBuf = new StringBuffer( pattern );
								strBuf.insert( idx, 'y' );
								pattern = strBuf.toString( );
							}
                    	}
                    	
                        dateFormat = new SimpleDateFormat(pattern, locale);
                        return;
                        
					case 'g' :

						dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
								com.ibm.icu.text.DateFormat.SHORT,
								com.ibm.icu.text.DateFormat.SHORT, locale );
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
				dateFormat = com.ibm.icu.text.DateFormat.getDateTimeInstance(
						com.ibm.icu.text.DateFormat.LONG, com.ibm.icu.text.DateFormat.LONG,
						locale );
				return;
			}
			if ( formatString.equals( "Long Date" ) )
			{
				dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
						com.ibm.icu.text.DateFormat.LONG, locale );
				return;

			}
			if ( formatString.equals( "Medium Date" ) )
			{
				dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
						com.ibm.icu.text.DateFormat.MEDIUM, locale );
				return;

			}
			if ( formatString.equals( "Short Date" ) )
			{
				dateFormat = com.ibm.icu.text.DateFormat.getDateInstance(
						com.ibm.icu.text.DateFormat.SHORT, locale );
				return;

			}
			if ( formatString.equals( "Long Time" ) )
			{
				dateFormat = com.ibm.icu.text.DateFormat.getTimeInstance(
						com.ibm.icu.text.DateFormat.LONG, locale );
				return;

			}
			if ( formatString.equals( "Medium Time" ) )
			{
				dateFormat = com.ibm.icu.text.DateFormat.getTimeInstance(
						com.ibm.icu.text.DateFormat.MEDIUM, locale );
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
