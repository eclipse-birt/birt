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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.icu.util.ULocale;

/**
 * 
 * 
 * Defines a number formatting class. It does the following: 1. In constructor,
 * convert format string to Java format string. 2. Expose a format function,
 * which does the following: a. Format number using Java format string b. Do
 * some post-processing, i.e., e or E, minus sign handling, etc.
 */
public class NumberFormatter
{

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( NumberFormatter.class
			.getName( ) );

	/**
	 * the format pattern
	 */
	protected String formatPattern;

	/**
	 * the locale used for formatting
	 */
	protected ULocale locale = ULocale.getDefault( );

	/**
	 * a java.text.NumberFormat format object. We want to use the
	 * createNumberFormat() and format() methods
	 */
	protected NumberFormat numberFormat;
	protected NumberFormat decimalFormat;

	/**
	 * Do we use hex pattern?
	 */
	private boolean hexFlag;

	/**
	 * constructor with no argument
	 */
	public NumberFormatter( )
	{
		applyPattern( null );
	}

	/**
	 * constructor with a format string as parameter
	 * 
	 * @param format
	 *            format string
	 */
	public NumberFormatter( String format )
	{
		applyPattern( format );
	}

	/**
	 * @param locale
	 *            the locale used for numer format
	 */
	public NumberFormatter( ULocale locale )
	{
		this.locale = locale;
		applyPattern( null );
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public NumberFormatter( Locale locale )
	{
		this( ULocale.forLocale( locale ) );
	}

	/**
	 * constructor that takes a format pattern and a locale
	 * 
	 * @param pattern
	 *            numeric format pattern
	 * @param locale
	 *            locale used to format the number
	 */
	public NumberFormatter( String pattern, ULocale locale )
	{
		this.locale = locale;
		applyPattern( pattern );
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public NumberFormatter( String pattern, Locale locale )
	{
		this( pattern, ULocale.forLocale( locale ) );
	}

	/**
	 * returns the original format string.
	 */
	public String getPattern( )
	{
		return this.formatPattern;
	}

	/**
	 * initializes numeric format pattern
	 * 
	 * @param patternStr
	 *            ths string used for formatting numeric data
	 */
	public void applyPattern( String patternStr )
	{
		try
		{
			this.formatPattern = patternStr;
			hexFlag = false;

			// null format String
			if ( this.formatPattern == null )
			{
				numberFormat = NumberFormat.getInstance( locale.toLocale( ) );
				numberFormat.setGroupingUsed( false );
				decimalFormat = new DecimalFormat( "", //$NON-NLS-1$
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				decimalFormat.setGroupingUsed( false );
				return;
			}

			// Single character format string
			if ( patternStr.length( ) == 1 )
			{
				handleSingleCharFormatString( patternStr.charAt( 0 ) );
				return;
			}

			// Named formats and arbitrary format string
			handleNamedFormats( patternStr );
		}
		catch ( Exception illeagueE )
		{
			logger.log( Level.WARNING, illeagueE.getMessage( ), illeagueE );
		}
	}

	/**
	 * @param num
	 *            the number to be formatted
	 * @return the formatted string
	 */
	public String format( double num )
	{
		try
		{
			if ( Double.isNaN( num ) )
			{
				return "NaN"; //$NON-NLS-1$
			}

			if ( hexFlag == true )
			{
				return Long.toHexString( new Double( num ).longValue( ) );
			}

			return numberFormat.format( num );

		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e ); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * format(BigDecimal) method, return the format string for the BigDecimal
	 * parameter.
	 */
	/**
	 * formats a BigDecimal value into a string
	 * 
	 * @param big
	 *            decimal value
	 * @return formatted string
	 */
	public String format( BigDecimal bigDecimal )
	{
		try
		{
			if ( hexFlag == true )
			{
				return Long.toHexString( bigDecimal.longValue( ) );
			}

			if ( this.formatPattern == null )
			{
				return decimalFormat.format( bigDecimal );
			}

			return numberFormat.format( bigDecimal );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e ); //$NON-NLS-1$
			return null;
		}
	}

	public String format( Number number )
	{
		try
		{
			if ( Double.isNaN( number.doubleValue( ) ) )
			{
				return "NaN";
			}
			if ( hexFlag == true )
			{
				return Long.toHexString( number.longValue( ) );
			}
			if ( this.formatPattern == null && number instanceof BigDecimal )
			{
				return decimalFormat.format( number );
			}

			return numberFormat.format( number );

		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e ); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * formats a long integer
	 * 
	 * @param num
	 *            the number to be formatted
	 * @return the formatted string
	 */
	public String format( long num )
	{
		if ( hexFlag == true )
		{
			return Long.toHexString( num );
		}
		return numberFormat.format( num );
	}

	private void handleSingleCharFormatString( char c )
	{
		switch ( c )
		{
			case 'G' :
			case 'g' :
			case 'D' :
			case 'd' :
				numberFormat = NumberFormat.getInstance( locale.toLocale( ) );
				return;
			case 'C' :
			case 'c' :
				numberFormat = NumberFormat.getCurrencyInstance( locale.toLocale( ) );
				return;
			case 'F' :
			case 'f' :
				numberFormat = new DecimalFormat( "#0.00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				return;
			case 'N' :
			case 'n' :
				numberFormat = new DecimalFormat( "###,##0.00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				return;
			case 'P' :
			case 'p' :
				numberFormat = new DecimalFormat( "###,##0.00 %", //$NON-NLS-1$
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				return;
			case 'E' :
			case 'e' :
				numberFormat = new DecimalFormat( "0.000000E00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				return;
			case 'X' :
			case 'x' :
				hexFlag = true;
				return;
			default :
			{
				char data[] = new char[1];
				data[0] = c;
				String str = new String( data );

				numberFormat = new DecimalFormat( str,
						new DecimalFormatSymbols( locale.toLocale( ) ) );
				return;
			}
		}
	}

	private void handleNamedFormats( String patternStr )
	{
		if ( patternStr.equals( "General Number" ) || patternStr.equals( "Unformatted" ) ) //$NON-NLS-1$ //$NON-NLS-2$
		{
			numberFormat = NumberFormat.getInstance( locale.toLocale( ) );
			numberFormat.setGroupingUsed( false );
			return;
		}
		if ( patternStr.equals( "Fixed" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "#0.00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale.toLocale( ) ) );
			return;

		}
		if ( patternStr.equals( "Percent" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "0.00%", //$NON-NLS-1$
					new DecimalFormatSymbols( locale.toLocale( ) ) );
			return;
		}
		if ( patternStr.equals( "Scientific" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "0.00E00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale.toLocale( ) ) );
			return;

		}
		if ( patternStr.equals( "Standard" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "###,##0.00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale.toLocale( ) ) );
			return;

		}
		numberFormat = new DecimalFormat( patternStr, new DecimalFormatSymbols(
				locale.toLocale( ) ) );
	}

	/**
	 * Parses the input string into a formatted date type.
	 * 
	 * @param number
	 *            the input string to parse
	 * @return the formatted date
	 * @throws ParseException
	 *             if the beginning of the specified string cannot be parsed.
	 */

	public Number parse( String number ) throws ParseException
	{
		return numberFormat.parse( number );
	}
}
