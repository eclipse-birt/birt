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

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class WordUtil
{

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

	public static String validBookmarkName( String name )
	{
		String bookmark = name.replaceAll( " ", "_" );
		bookmark = bookmark.replaceAll( "\"", "_" );
		return bookmark;
	}

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

		double val = value.convertTo( DimensionType.UNITS_IN );
		return val * INCH_TWIPS;
	}

	public static int getWidth( int cw, IStyle style )
	{
		float left = WordUtil.getPadding( style.getPaddingLeft( ) );
		float right = WordUtil.getPadding( style.getPaddingRight( ) );

		if ( left > cw )
		{
			left = 0;
		}

		if ( right > cw )
		{
			right = 0;
		}

		if ( ( left + right ) > cw )
		{
			right = 0;
		}

		return (int) ( cw - left - right );
	}

	public static float getPadding( String padding )
	{
		float value = 0;
		//Percentage value will be omitted		
		try
		{
			value = Float.parseFloat( padding ) / 50;
		}
		catch ( Exception e )
		{

		}

		return value;
	}

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
}
