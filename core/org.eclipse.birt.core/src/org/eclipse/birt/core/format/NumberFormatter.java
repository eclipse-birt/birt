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
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @version $Revision: 1.2 $ $Date: 2005/02/07 02:16:26 $ 
 * 
 * Defines a number formatting class. It does the following:
 * 1. In constructor, convert format string to Java format string. 
 * 2. Expose a format function, which does the following: 
 * 		a. Format number using Java format string 
 *      b. Do some post-processing, i.e., e or E, minus sign handling, etc.
 */
public class NumberFormatter
{
	/**
	 * logger used to log syntax errors.
	 */
	static protected Log logger = LogFactory.getLog( NumberFormatter.class );
	
	/**
	 * the format pattern 
	 */
	protected String formatPattern;

	/**
	 * the locale used for formatting
	 */
	protected Locale locale = Locale.getDefault( );

	/**
	 * a java.text.NumberFormat format object. We want to use the createNumberFormat() 
	 * and format() methods
	 */
	protected NumberFormat numberFormat;

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
	 * @param format format string
	 */
	public NumberFormatter( String format )
	{
		applyPattern( format );
	}

	/**
	 * @param locale the locale used for numer format
	 */
	public NumberFormatter( Locale locale )
	{
		this.locale = locale;
		applyPattern( null );
	}

	/**
	 * constructor that takes a format pattern and a locale
	 * 
	 * @param pattern numeric format pattern
	 * @param locale locale used to format the number
	 */
	public NumberFormatter( String pattern, Locale locale )
	{
		this.locale = locale;
		applyPattern( pattern );
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
	 * @param patternStr ths string used for formatting numeric data
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
				numberFormat = NumberFormat.getInstance( locale );
				numberFormat.setGroupingUsed( false );
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
			logger.error( "The pattern is illeague:" + illeagueE ); //$NON-NLS-1$
		}
	}

	/**
	 * @param num the number to be formatted
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
				return Integer.toHexString( new Long( new Double( num )
						.longValue( ) ).intValue( ) );
			}
			
			return numberFormat.format( num );

		}
		catch ( Exception e )
		{
			logger.error( "Format failed:" + e ); //$NON-NLS-1$
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
	 * @param big decimal value
	 * @return formatted string
	 */
	public String format( BigDecimal bigDecimal )
	{
		return this.format( bigDecimal.doubleValue( ) );
	}

	/**
	 * formats a long integer
	 * 
	 * @param num the number to be formatted
	 * @return the formatted string
	 */
	public String format( long num )
	{
		if ( hexFlag == true )
		{
			return Integer.toHexString( new Long( num ).intValue( ) );
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
				numberFormat = NumberFormat.getInstance( locale );
				return;
			case 'C' :
			case 'c' :
				numberFormat = NumberFormat
						.getCurrencyInstance( locale );
				return;
			case 'F' :
			case 'f' :
				numberFormat = new DecimalFormat( "#0.00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale ) );
				return;
			case 'N' :
			case 'n' :
				numberFormat = new DecimalFormat( "###,##0.00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale ) );
				return;
			case 'P' :
			case 'p' :
				numberFormat = new DecimalFormat( "###,##0.00 %", //$NON-NLS-1$
						new DecimalFormatSymbols( locale ) );
				return;
			case 'E' :
			case 'e' :
				numberFormat = new DecimalFormat( "0.000000E00", //$NON-NLS-1$
						new DecimalFormatSymbols( locale ) );
				return;
			case 'X' :
			case 'x' :
				hexFlag = true;
				return;
			default :
			{
			    char data[] = new char[1];	
			    data[0] = c;
			    String str = new String(data);

				numberFormat = new DecimalFormat( str,
						new DecimalFormatSymbols( locale ) );
				return;
			}
		}
	}
	
	private void handleNamedFormats( String patternStr )
	{
		if ( patternStr.equals( "General Number" ) ) //$NON-NLS-1$
		{
			numberFormat = NumberFormat.getInstance( locale );
			numberFormat.setGroupingUsed( false );
			return;
		}
		if ( patternStr.equals( "Currency" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "###,##0.00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale ) );
			// numberFormat = NumberFormat.getCurrencyInstance( locale );
			return;
		
		}
		if ( patternStr.equals( "Fixed" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "#0.00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale ) );
			return;
		
		}
		if ( patternStr.equals( "Percent" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "0.00%", //$NON-NLS-1$
					new DecimalFormatSymbols( locale ) );
			return;
		}
		if ( patternStr.equals( "Scientific" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "0.00E00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale ) );
			return;
		
		}
		if ( patternStr.equals( "Standard" ) ) //$NON-NLS-1$
		{
			numberFormat = new DecimalFormat( "###,##0.00", //$NON-NLS-1$
					new DecimalFormatSymbols( locale ) );
			return;
		
		}
		numberFormat = new DecimalFormat( patternStr,
				new DecimalFormatSymbols( locale ) );
	}
}