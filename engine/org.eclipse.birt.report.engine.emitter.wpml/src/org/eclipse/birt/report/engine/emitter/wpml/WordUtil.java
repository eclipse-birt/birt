/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class WordUtil
{

	private static Logger logger = Logger.getLogger( WordUtil.class.getName( ) );

	private static HashSet<Character> splitChar = new HashSet<Character>( );

	static
	{
		splitChar.add( new Character( ' ' ) );
		splitChar.add( new Character( '\r' ) );
		splitChar.add( new Character( '\n' ) );
	};

	private static double Temp_PX;
	public final static double INCH_PX;
	static
	{

		try
		{
			Temp_PX = java.awt.Toolkit
					.getDefaultToolkit( )
					.getScreenResolution( );
		}
		catch ( Exception e )
		{
			Temp_PX = 96;
		}
		INCH_PX = Temp_PX;
	}

	public final static double INCH_PT = 72;

	public final static double PT_TWIPS = 20;

	public final static double INCH_TWIPS = INCH_PT * PT_TWIPS;

	public final static double PX_TWIPS = INCH_TWIPS / INCH_PX;

	public final static double PX_PT = INCH_PT / INCH_PX;

	// Bookmark names must begin with a letter and can contain numbers.
	// spaces can not be included in a bookmark name,
	// but the underscore character can be used to separate words
	public static String validBookmarkName( String name )
	{
		String bookmark = name.replaceAll( " ", "_" );
		bookmark = bookmark.replaceAll( "\"", "_" );
		return bookmark;
	}

	// convert from DimensionType to twips according to prefValue
	public static int convertTo( DimensionType value, int prefValue )
	{
		if ( value == null )
		{
			return prefValue;
		}

		if ( DimensionType.UNITS_PERCENTAGE
				.equalsIgnoreCase( value.getUnits( ) ) )
		{
			return (int) ( prefValue * value.getMeasure( ) / 100 );
		}

		return (int) convertTo( value );
	}

	public static double convertTo( DimensionType value )
	{
		if ( value == null
				|| DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase( value
						.getUnits( ) ) )
		{
			return -1;
		}

		if ( DimensionType.UNITS_PX.equalsIgnoreCase( value.getUnits( ) ) )
		{
			return value.getMeasure( ) * PX_TWIPS;
		}
		
		// FIXME: We should use font size to calculate the EM/EX
		if ( DimensionType.UNITS_EM.equalsIgnoreCase( value.getUnits( ) )
				|| DimensionType.UNITS_EX.equalsIgnoreCase( value.getUnits( ) ) )
		{
			return value.getMeasure( ) * 12 * PT_TWIPS;
		}
		// The conversion is between absolute
		// the units should be one of the absolute units(CM, IN, MM, PT,PC).
		double val = value.convertTo( DimensionType.UNITS_IN );
		return val * INCH_TWIPS;
	}

	// convert image's size from DimensionType to pt according to ref
	public static double convertImageSize( DimensionType value, int ref )
	{

		if ( value == null )
		{
			return ref * PX_PT;
		}

		if ( DimensionType.UNITS_PX.equalsIgnoreCase( value.getUnits( ) ) )
		{
			return value.getMeasure( ) * PX_PT;
		}
		else if ( DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase( value
				.getUnits( ) ) )
		{
			return ( value.getMeasure( ) / 100 ) * ref * PX_PT;
		}
		else
		{
			return value.convertTo( DimensionType.UNITS_IN ) * INCH_PT;
		}
	}

	public static double twipToPt( double t )
	{
		return t / PT_TWIPS;
	}

	// unit change from milliPt to twips
	public static int parseSpacing( float floatValue )
	{
		return (int) ( Math.round( floatValue / 1000 ) * PT_TWIPS );
	}

	// unit change from milliPt to half a point
	public static int parseFontSize( float value )
	{
		return Math.round( value / 500 );
	}

	public static String capitalize( String text )
	{
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray( );
		for ( int i = 0; i < array.length; i++ )
		{
			Character c = new Character( text.charAt( i ) );
			if ( splitChar.contains( c ) )
				capitalizeNextChar = true;
			else if ( capitalizeNextChar )
			{
				array[i] = Character.toUpperCase( array[i] );
				capitalizeNextChar = false;
			}
		}
		return new String( array );
	}

	// convert valid color format from "rgb(0,0,0)" to "000000"
	public static String parseColor( String color )
	{
		if ( "transparent".equalsIgnoreCase( color ) || color == null )
		{
			return null;
		}
		String[] values = color.substring( color.indexOf( "(" ) + 1,
				color.length( ) - 1 ).split( "," );
		String value = "";
		for ( int i = 0; i < values.length; i++ )
		{
			try
			{
				String s = Integer.toHexString( ( Integer.parseInt( values[i]
						.trim( ) ) ) );

				if ( s.length( ) == 1 )
				{
					s = "0" + s;
				}

				value += s;
			}
			catch ( Exception e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
				value = null;
			}
		}

		return value;
	}

	// run border, paragraph borders, table borders, table cell borders
	// birt accept:solid, dotted, dashed, double
	// doc and docx accept: single, dotted, dashed, double
	public static String parseBorderStyle( String style )
	{
		if ( CSSConstants.CSS_SOLID_VALUE.equalsIgnoreCase( style ) )
		{
			return "single";
		}
		return style;
	}

	// image borders style
	// birt accept: solid, dotted, dashed, double
	// doc and docx accept in vml: single, dot, dash, double
	public static String parseImageBorderStyle( String style )
	{
		if ( CSSConstants.CSS_DOTTED_VALUE.equalsIgnoreCase( style ) )
		{
			return "dot";
		}
		if ( CSSConstants.CSS_DASHED_VALUE.equalsIgnoreCase( style ) )
		{
			return "dash";
		}
		if ( CSSConstants.CSS_SOLID_VALUE.equalsIgnoreCase( style ) )
		{
			return "single";
		}
		return style;
	}

	// align: bottom, middle, top
	// doc and docx accept: bottom, center, top
	public static String parseVerticalAlign( String align )
	{
		if ( CSSConstants.CSS_MIDDLE_VALUE.equals( align ) )
		{
			return "center";
		}
		return align;
	}

	public static String removeQuote( String val )
	{
		if ( val.charAt( 0 ) == '"' && val.charAt( val.length( ) - 1 ) == '"' )
		{
			return val.substring( 1, val.length( ) - 1 );
		}
		return val;
	}

	// unit: eights of a point
	public static int parseBorderSize( float size )
	{
		int w = Math.round( size );
		return ( 8 * w ) / 750;
	}
}
