/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class StyleBuilder
{
	public static final String C_PATTERN = "(rgb\\()(\\d+)\\,(\\s?\\d+)\\,(\\s?\\d+)\\)";

	public static final Pattern colorp = Pattern.compile( C_PATTERN,
			Pattern.CASE_INSENSITIVE );

	private static Logger logger = Logger.getLogger( StyleBuilder.class
			.getName( ) );

	public static StyleEntry createStyleEntry( IStyle style )
	{
		StyleEntry entry = new StyleEntry( );

		populateColor( style, StyleConstants.STYLE_BACKGROUND_COLOR, entry,
				StyleConstant.BACKGROUND_COLOR_PROP );

		float width = Float.parseFloat( style.getBorderBottomWidth( ) );
		if ( width > 0 )
		{
			populateColor( style, StyleConstants.STYLE_BORDER_BOTTOM_COLOR,
					entry, StyleConstant.BORDER_BOTTOM_COLOR_PROP );

			entry.setProperty( StyleConstant.BORDER_BOTTOM_STYLE_PROP,
					convertBorderStyle( style.getBorderBottomStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_BOTTOM_WIDTH_PROP,
					convertBorderWeight( style.getBorderBottomWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderTopWidth( ) );
		if ( width > 0 )
		{
			populateColor( style, StyleConstants.STYLE_BORDER_TOP_COLOR, entry,
					StyleConstant.BORDER_TOP_COLOR_PROP );

			entry.setProperty( StyleConstant.BORDER_TOP_STYLE_PROP,
					convertBorderStyle( style.getBorderTopStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_TOP_WIDTH_PROP,
					convertBorderWeight( style.getBorderTopWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderLeftWidth( ) );
		if ( width > 0 )
		{
			populateColor( style, StyleConstants.STYLE_BORDER_LEFT_COLOR,
					entry, StyleConstant.BORDER_LEFT_COLOR_PROP );

			entry.setProperty( StyleConstant.BORDER_LEFT_STYLE_PROP,
					convertBorderStyle( style.getBorderLeftStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_LEFT_WIDTH_PROP,
					convertBorderWeight( style.getBorderLeftWidth( ) ) );
		}

		width = Float.parseFloat( style.getBorderRightWidth( ) );
		if ( width > 0 )
		{
			populateColor( style, StyleConstants.STYLE_BORDER_RIGHT_COLOR,
					entry, StyleConstant.BORDER_RIGHT_COLOR_PROP );

			entry.setProperty( StyleConstant.BORDER_RIGHT_STYLE_PROP,
					convertBorderStyle( style.getBorderRightStyle( ) ) );

			entry.setProperty( StyleConstant.BORDER_RIGHT_WIDTH_PROP,
					convertBorderWeight( style.getBorderRightWidth( ) ) );
		}
		
		populateColor( style, StyleConstants.STYLE_COLOR, entry,
				StyleConstant.COLOR_PROP );

		entry.setProperty( StyleConstant.FONT_FAMILY_PROP, ExcelUtil
				.getValue( style.getFontFamily( ) ) );

		entry.setProperty( StyleConstant.FONT_SIZE_PROP, convertFontSize( style
				.getFontSize( ) ) );

		entry.setProperty( StyleConstant.FONT_STYLE_PROP, "italic"
				.equalsIgnoreCase( style.getFontStyle( ) ) );

		entry.setProperty( StyleConstant.FONT_WEIGHT_PROP, "bold"
				.equalsIgnoreCase( style.getFontWeight( ) ) );

		entry.setProperty( StyleConstant.TEXT_LINE_THROUGH_PROP, "line-through"
				.equalsIgnoreCase( style.getTextLineThrough( ) ) );

		entry.setProperty( StyleConstant.TEXT_UNDERLINE_PROP, "underline"
				.equalsIgnoreCase( style.getTextUnderline( ) ) );

		entry.setProperty( StyleConstant.H_ALIGN_PROP, convertHAlign( style
				.getTextAlign( ), style.getDirection( ) ) );

		entry.setProperty( StyleConstant.V_ALIGN_PROP, convertVAlign( style
				.getVerticalAlign( ) ) );
       
		entry.setProperty( StyleConstant.DATE_FORMAT_PROP, style
				.getDateFormat( ) );
		entry.setProperty( StyleConstant.NUMBER_FORMAT_PROP, style
				.getNumberFormat( ));
		entry.setProperty( StyleConstant.STRING_FORMAT_PROP, style
				.getStringFormat( ) );
		
        entry.setProperty( StyleConstant.TEXT_TRANSFORM, style
				.getTextTransform( ) );

		entry.setProperty( StyleConstant.DIRECTION_PROP, style
				.getDirection( ) );

		entry.setProperty( StyleConstant.WHITE_SPACE, style.getWhiteSpace( ) );

		return entry;
	}

	public static StyleEntry createEmptyStyleEntry( )
	{
		StyleEntry entry = new StyleEntry( );

		// for ( int i = 0; i < StyleEntry.COUNT; i++ )
		// {
		// entry.setProperty( i, StyleEntry.NULL );
		// }

		return entry;
	}

	public static StyleEntry applyDiagonalLine( StyleEntry entry, Color color,
			String style, int width )
	{
		if ( width > 0 )
		{
			entry.setProperty( StyleConstant.BORDER_DIAGONAL_COLOR_PROP, color );
			entry.setProperty( StyleConstant.BORDER_DIAGONAL_STYLE_PROP,
					convertBorderStyle( style ) );
			entry.setProperty( StyleConstant.BORDER_DIAGONAL_WIDTH_PROP,
					convertBorderWeight( width ) );
		}
		return entry;
	}

	private static void populateColor( IStyle style, int styleIndex,
			StyleEntry entry, int index )
	{
		CSSValue value = style.getProperty( styleIndex );
		entry.setProperty( index, PropertyUtil.getColor( value ) );
	}

	public static Integer convertFontSize( String size )
	{
		Integer fsize = null;
		try
		{
			fsize = Math.round( Float.parseFloat( size ) / 1000 );
		}
		catch ( NumberFormatException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}

		return fsize;
	}

	public static String convertBorderWeight( String linestyle )
	{
		String w = StyleConstant.NULL;

		if ( linestyle != null && !"0".equalsIgnoreCase( linestyle ) )
		{
			linestyle = ExcelUtil.getValue( linestyle );
			int weight = (int) Double.parseDouble( linestyle );
			w = convertBorderWeight( weight );
		}
		return w;
	}

	public static String convertBorderWeight( double width )
	{
		String w = StyleConstant.NULL;
		if ( width >= 749 && width < 1499 )
		{
			w = "1";
		}
		else if ( width >= 1499 && width < 2249 )
		{
			w = "2";
		}
		else if ( width >= 2249 )
		{
			w = "3";
		}
		else
		{
			w = "2";
		}
		return w;
	}

	public static String convertBorderStyle( String style )
	{
		String bs = ExcelUtil.getValue( style );

		if ( !StyleEntry.isNull( bs ) )
		{
			if ( "dotted".equalsIgnoreCase( bs ) )
			{
				bs = "Dot";
			}
			else if ( "dashed".equalsIgnoreCase( bs ) )
			{
				bs = "DashDot";
			}
			else if ( "double".equalsIgnoreCase( bs ) )
			{
				bs = "Double";
			}
			else
			{
				bs = "Continuous";
			}
		}

		return bs;
	}

	public static String convertHAlign( String align, String direction )
	{
		String ha = null; 
			//"Left";
		align = ExcelUtil.getValue( align );

		if ( "left".equalsIgnoreCase( align ) )
		{
			ha = "Left";
		}
		else if ( "right".equalsIgnoreCase( align ) )
		{
			ha = "Right";
		}
		else if ( "center".equalsIgnoreCase( align ) )
		{
			ha = "Center";
		}
		else if ( "rtl".equalsIgnoreCase( direction ) )
			ha = "Right";
		else
			ha = "Left";

		return ha;
	}

	public static String convertVAlign( String align )
	{
		String va = "Top";
		align = ExcelUtil.getValue( align );

		if ( "bottom".equalsIgnoreCase( align ) )
		{
			va = "Bottom";
		}
		else if ( "middle".equalsIgnoreCase( align ) )
		{
			va = "Center";
		}

		return va;
	}

	public static boolean isHeritable( int id )
	{
		if ( ( id >= StyleConstant.BORDER_BOTTOM_COLOR_PROP && id <= StyleConstant.BORDER_RIGHT_WIDTH_PROP )
				|| ( id >= StyleConstant.BORDER_DIAGONAL_COLOR_PROP && id <= StyleConstant.BORDER_ANTIDIAGONAL_WIDTH_PROP ) )
			return false;
		return true;
	}

	public static void mergeInheritableProp( StyleEntry cEntry, StyleEntry entry )
	{
		for ( int i = 0; i < StyleConstant.COUNT; i++ )
		{
			if ( StyleBuilder.isHeritable( i )
					&& StyleEntry.isNull( entry.getProperty( i )) )
			{
				entry.setProperty( i, cEntry.getProperty( i ) );
			}
		}
	}

	public static void applyRightBorder( StyleEntry cEntry, StyleEntry entry )
	{
		if ( entry == null )
		{
			return;
		}
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_RIGHT_WIDTH_PROP );
	}

	public static void applyLeftBorder( StyleEntry cEntry, StyleEntry entry )
	{
		if ( entry == null )
		{
			return;
		}
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_LEFT_WIDTH_PROP );
	}

	public static void applyTopBorder( StyleEntry cEntry, StyleEntry entry )
	{
		if ( entry == null )
		{
			return;
		}
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_TOP_WIDTH_PROP );
	}

	public static void applyBottomBorder( StyleEntry cEntry, StyleEntry entry )
	{
		if ( entry == null )
		{
			return;
		}
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_COLOR_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_STYLE_PROP );
		overwriteProp( cEntry, entry, StyleConstant.BORDER_BOTTOM_WIDTH_PROP );
	}

	private static void overwriteProp( StyleEntry cEntry, StyleEntry entry,
			int id )
	{
		if (  StyleEntry.isNull( entry.getProperty( id ) ) )
		{
			entry.setProperty( id, cEntry.getProperty( id ) );
		}
	}	
}
