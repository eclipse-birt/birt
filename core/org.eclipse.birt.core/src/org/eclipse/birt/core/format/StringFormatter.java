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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/25 $ 
 * 
 * Defines a string formatting class. Notice that unlike numeric or Date formatting, 
 * locale is irrelevant in string formatting
 *  
 */
public class StringFormatter
{
	/**
	 * logger used to log syntax errors.
	 */
	static protected Log logger = LogFactory.getLog( StringFormatter.class );
	
	//	original format string
	protected String formatPattern;
    //	Locale
	private Locale locale;

	// uppercase or lowercase;
	private char chcase;

	// number of & in format string;
	private int nand;

	//number of @ in format string;
	private int natt;

	//from left to right.
	private boolean dir;

	/**
	 * resets all the member variable to initial value;
	 */
	private void init( )
	{
		formatPattern = "";
		chcase = ' ';
		nand = 0;
		natt = 0;
		dir = false;
	}

	/**
	 * constructor with no formatting string 
	 */
	public StringFormatter( )
	{
		init( );
	}

	/**
	 * constructor with a format string argument 
	 
	 * @param format the format string
	 */
	public StringFormatter( String format )
	{
		init( );
		applyPattern( format );
	}

	/**
	 * @param format the format pattern
	 */
	public void applyPattern( String format )
	{
		init( );
		char c = ' ';
		StringBuffer scan = new StringBuffer( format );
		int len = scan.length( );

		for ( int i = 0; i < len; i++ )
		{
			c = scan.charAt( i );
			switch ( c )
			{
				case ( '@' ) :
					natt++;
					break;

				case ( '&' ) :
					nand++;
					break;

				case ( '<' ) :
				case ( '>' ) :
					chcase = c;
					break;

				case ( '!' ) :
					dir = true;
					break;
			}
		}
		formatPattern = format;
	}

	/**
	 * returns the original format string.
	 */
	public String getPattern( )
	{
		return this.formatPattern;
	}
	/**
	 * 
	 * getLoacle() method, return the locale value.
	 *  
	 */
	public Locale getLocale( )
	{
		return this.locale;
	}

	/**
	 * 
	 * setLoacle() method, set the locale value.
	 *  
	 */
	public void setLocale( Locale theLocale )
	{
		locale = theLocale;
	}

	/**
	 * @param val string to be handled
	 * @param option to upper case or to lower case
	 * @return
	 */
	private String handleCase( String val, char option )
	{
		if ( option == '<' )
			return val.toLowerCase( );
		else if ( option == '>' )
			return val.toUpperCase( );
		else
			return val;

	}

	/**
	 * 
	 * returns the formated string for the string parameter. //
	 * '@' - character or space // '&' - character or empty // ' <' - tolower //
	 * '>' - toupper // '!' - left to right
	 *  
	 */
	public String format( String str )
	{
		int len = str.length( );
		int col = natt + nand;
		int ext = 0;
		StringBuffer orig = new StringBuffer( str );
		StringBuffer fstr = new StringBuffer( this.formatPattern );
		StringBuffer ret = new StringBuffer( "" );
		int i = 0;
		//offset of the process position.
		int pos = 0;

		//length of the format string;
		int len2 = 0;
	
		char fc = ' ';

		String sc = null;

		if (!dir )
		{
			if ( len > col )
			{
				ret.append( handleCase( orig.substring( 0, len - col ), chcase ) );
				pos = len - col;
				len = col;

			}
			ext = col - len;
		}
		len2 = this.formatPattern.length( );
		for ( i = 0; i < len2; i++ )
		{

			fc = fstr.charAt( i );
			switch ( fc )
			{
				case ( '@' ) :
				case ( '&' ) :
					// character or space
					if ( ext > 0 || len == 0 )
					{
						if ( fc == '@' )
							ret.append( ' ' );
						ext--;
					}
					else
					{
						sc = orig.substring( pos, pos + 1 );
						ret.append( handleCase( sc, chcase ) );
						pos++;
						len--;
					}
					break;

				case ( '<' ) :
				case ( '>' ) :
				case ( '!' ) :
					// ignore
					break;

				default :
					ret.append( fc );
					break;
			}
		}

		while ( --len >= 0 )
		{
			sc = orig.substring( pos, pos + 1 );
			ret.append( handleCase( sc, chcase ) );
			pos++;
		}

		return ret.toString( );
	}

}
