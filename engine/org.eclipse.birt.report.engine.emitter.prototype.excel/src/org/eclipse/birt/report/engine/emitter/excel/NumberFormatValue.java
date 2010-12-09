/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

public class NumberFormatValue
{

	private int fractionDigits;

	private String format;
	private RoundingMode roundingMode;
	private static Pattern pattern = Pattern.compile( "^(.*?)\\{RoundingMode=(.*?)\\}",
			Pattern.CASE_INSENSITIVE );

	private NumberFormatValue( )
	{
	}

	public static NumberFormatValue getInstance( String numberFormat )
	{
		if ( numberFormat != null )
		{
			NumberFormatValue value = new NumberFormatValue( );
			Matcher matcher = pattern.matcher( numberFormat );
			if ( matcher.matches( ) )
			{
				String f = matcher.group( 1 );
				if ( f != null )
				{
					value.format = f;
					int index = f.lastIndexOf( '.' );
					if ( index > 0 )
					{
						value.fractionDigits = f.length( ) - 1 - index;
					}
				}
				String m = matcher.group( 2 );
				if ( m != null )
				{
					value.roundingMode = RoundingMode.valueOf( m );
				}
			}
			return value;
		}
		return null;
	}

	public static void main( String[] args )
	{
		NumberFormatValue v = NumberFormatValue.getInstance( "###0.0{RoundingMode=HALF_EVEN}" );
		System.out.println( v.getFormat( ) );
		System.out.println( v.getRoundingMode( ) );
		System.out.println( v.getFractionDigits( ) );
	}

	public int getFractionDigits( )
	{
		return fractionDigits;
	}

	public void setFractionDigits( int fractionDigits )
	{
		this.fractionDigits = fractionDigits;
	}

	public String getFormat( )
	{
		return format;
	}

	public void setFormat( String format )
	{
		this.format = format;
	}

	public RoundingMode getRoundingMode( )
	{
		return roundingMode;
	}

	public void setRoundingMode( RoundingMode roundingMode )
	{
		this.roundingMode = roundingMode;
	}

}
