/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.util;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Font;

public class PropertyUtil
{
	private static Logger logger = Logger.getLogger( PropertyUtil.class.getName() );
	
	/**
	 * Checks if the font is bold
	 * @param value			the CSSValue
	 * @return				true if the font is bold
	 * 						false if not
	 */
    public static boolean isBoldFont(CSSValue value)
    {
    	if(value!=null)
        {
    		if ( IStyle.BOLD_VALUE.equals( value )
					|| IStyle.BOLDER_VALUE.equals( value )
					|| IStyle.NUMBER_600.equals( value )
					|| IStyle.NUMBER_700.equals( value )
					|| IStyle.NUMBER_800.equals( value )
					|| IStyle.NUMBER_900.equals( value ) )
			{
				return true;
			}
        }
    	return false;
    }
    
    
    
    public static boolean isInlineElement(IContent content)
    {
        IStyle style = content.getStyle();
        if(style!=null)
        {
            return IStyle.INLINE_VALUE.equals(style.getProperty( IStyle.STYLE_DISPLAY )); 
        }
        return false;
    }
    
    
    public static int getLineHeight(String lineHeight)
    {
    	try
    	{
    		if( lineHeight.equalsIgnoreCase( "normal" )) //$NON-NLS-1$
    		{
    			//BUG 147861: we return *0* as the default value of the *lineLight*
    			return 0;
    		}
    		
    		return (int)Float.parseFloat( lineHeight );
    	}
    	catch(NumberFormatException ex)
    	{
    		logger.log(Level.WARNING, "invalid line height: {0}", lineHeight ); //$NON-NLS-1$
    		return 0;
    	}
    }
    
    
    public static Color getColor( CSSValue value )
	{
		if ( value != null && value instanceof RGBColorValue )
		{
			RGBColorValue color = (RGBColorValue) value;
			try
			{
				return new Color( color.getRed( ).getFloatValue(
						CSSPrimitiveValue.CSS_NUMBER ) / 255.0f, color
						.getGreen( ).getFloatValue(
								CSSPrimitiveValue.CSS_NUMBER ) / 255.0f, color
						.getBlue( )
						.getFloatValue( CSSPrimitiveValue.CSS_NUMBER ) / 255.0f );
			}
			catch ( RuntimeException ex )
			{
				logger.log( Level.WARNING, "invalid color: {0}", value ); //$NON-NLS-1$
			}
		}
		return null;
	}
    
    /**
     * Gets the color from a CSSValue converted string.
     * 
     * @param color CSSValue converted string.
     * @return java.awt.Color
     */
    public static Color getColor( String color )
	{
		if ( color == null || color.length( ) == 0 )
		{
			return null;
		}
		if ( color.charAt( 0 ) == '#' )
			return hexToColor( color );
		else if ( color.equalsIgnoreCase( "Black" ) )
			return Color.black;
		else if ( color.equalsIgnoreCase( "Gray" ) )
			return Color.gray;
		else if ( color.equalsIgnoreCase( "White" ) )
			return Color.white;
		else if ( color.equalsIgnoreCase( "Red" ) )
			return Color.red;
		else if ( color.equalsIgnoreCase( "Green" ) )
			return Color.green;
		else if ( color.equalsIgnoreCase( "Yellow" ) )
			return Color.yellow;
		else if ( color.equalsIgnoreCase( "Blue" ) )
			return Color.blue;
		else if ( color.equalsIgnoreCase( "Teal" ) )
			return hexToColor( "#008080" );
		else if ( color.equalsIgnoreCase( "Aqua" ) )
			return hexToColor( "#00FFFF" );
		else if ( color.equalsIgnoreCase( "Silver" ) )
			return hexToColor( "#C0C0C0" );
		else if ( color.equalsIgnoreCase( "Navy" ) )
			return hexToColor( "#000080" );
		else if ( color.equalsIgnoreCase( "Lime" ) )
			return hexToColor( "#00FF00" );
		else if ( color.equalsIgnoreCase( "Olive" ) )
			return hexToColor( "#808000" );
		else if ( color.equalsIgnoreCase( "Purple" ) )
			return hexToColor( "#800080" );
		else if ( color.equalsIgnoreCase( "Fuchsia" ) )
			return hexToColor( "#FF00FF" );
		else if ( color.equalsIgnoreCase( "Maroon" ) )
			return hexToColor( "#800000" );
		else
		{
			Pattern p = Pattern.compile( "rgb\\(.+,.+,.+\\)" );
			Matcher m = p.matcher( color );
			if ( m.find( ) )
			{
				String[] rgb = color.substring( m.start( ) + 4, m.end( ) - 1 )
						.split( "," );
				if ( rgb.length == 3 )
				{
					try
					{
						int red = Integer.parseInt( rgb[0].trim( ) );
						int green = Integer.parseInt( rgb[1].trim( ) );
						int blue = Integer.parseInt( rgb[2].trim( ) );
						return new Color( red, green, blue );
					}
					catch ( IllegalArgumentException ex )
					{
						return null;
					}
				}
			}
		}
		return null;
	}
    
	static final Color hexToColor( String value )
	{
		String digits;
		if ( value.startsWith( "#" ) )
		{
			digits = value.substring( 1, Math.min( value.length( ), 7 ) );
		}
		else
		{
			digits = value;
		}
		String hstr = "0x" + digits;
		Color c;
		try
		{
			c = Color.decode( hstr );
		}
		catch ( NumberFormatException nfe )
		{
			c = null;
		}
		return c;
	}
    
    public static int getFontStyle(String fontStyle, String fontWeight )
    {
		int styleValue = Font.NORMAL;
		
		if ( CSSConstants.CSS_OBLIQUE_VALUE.equals( fontStyle )
				|| CSSConstants.CSS_ITALIC_VALUE.equals( fontStyle ) )
		{
			styleValue |= Font.ITALIC;
		}

   		if ( CSSConstants.CSS_BOLD_VALUE.equals( fontWeight )
			|| CSSConstants.CSS_BOLDER_VALUE.equals( fontWeight )
			|| CSSConstants.CSS_600_VALUE.equals( fontWeight )
			|| CSSConstants.CSS_700_VALUE.equals( fontWeight )
			|| CSSConstants.CSS_800_VALUE.equals( fontWeight )
			|| CSSConstants.CSS_900_VALUE.equals( fontWeight ) )
		{
			styleValue |= Font.BOLD;
		}
		return styleValue;
    }
    
    public static String getBackgroundImage( CSSValue value )
	{
		if ( value != null && value instanceof StringValue )
		{
			String strValue = ( (StringValue) value ).getStringValue( );
			if ( strValue != null
					&& ( !CSSConstants.CSS_NONE_VALUE.equals( strValue ) ) )
			{
				return strValue;
			}
		}
		return null;
	}

	public static int getDimensionValue( CSSValue value )
	{
		return getDimensionValue( value, 0 );
	}

	public static int getDimensionValue( CSSValue value, int referenceLength )
	{
		if ( value != null && ( value instanceof FloatValue ) )
		{
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue( );
			switch ( fv.getPrimitiveType( ) )
			{
				case CSSPrimitiveValue.CSS_CM :
					return (int) ( v * 72000 / 2.54 );

				case CSSPrimitiveValue.CSS_IN :
					return (int) ( v * 72000 );

				case CSSPrimitiveValue.CSS_MM :
					return (int) ( v * 7200 / 2.54 );

				case CSSPrimitiveValue.CSS_PT :
					return (int) ( v * 1000 );
				case CSSPrimitiveValue.CSS_NUMBER :
					return (int) v;
				case CSSPrimitiveValue.CSS_PERCENTAGE :

					return (int) ( referenceLength * v/100.0 );
			}
		}
		return 0;
	}
	
	public static int getDimensionValue( IContent content, DimensionType d )
	{
		return getDimensionValue(content,  d, 0, 0 );
	}
	
	public static int getDimensionValue(  IContent content, DimensionType d,  int dpi, int referenceLength )
	{
		if ( d == null )
		{
			return 0;
		}
		try
		{
			String units = d.getUnits( );
			if ( units.equals( EngineIRConstants.UNITS_PT )
					|| units.equals( EngineIRConstants.UNITS_CM )
					|| units.equals( EngineIRConstants.UNITS_MM )
					|| units.equals( EngineIRConstants.UNITS_PC )
					|| units.equals( EngineIRConstants.UNITS_IN ) )
			{
				double point = d.convertTo( EngineIRConstants.UNITS_PT ) * 1000;
				return (int) point;
			}
			else if ( units.equals( EngineIRConstants.UNITS_PX ) )
			{
				if( dpi == 0 )
				{
					dpi = 96;
				}
				double point = d.getMeasure( ) / dpi * 72000d;
				return (int) point;
			}
			else if ( units.equals( EngineIRConstants.UNITS_PERCENTAGE ) )
			{
				double point = referenceLength * d.getMeasure( ) / 100.0;
				return (int) point;
			}
			else if ( units.equals( EngineIRConstants.UNITS_EM )
					|| units.equals( EngineIRConstants.UNITS_EX ) )
			{
				int size = 9000;
				if ( content != null )
				{
					IStyle style = content.getComputedStyle( );
					CSSValue fontSize = style
							.getProperty( IStyle.STYLE_FONT_SIZE );
					size = getDimensionValue( fontSize );
				}
				double point = size * d.getMeasure( );
				return (int) point;
			}
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getLocalizedMessage( ) );
			return 0;
		}
		return 0;
	}
	

	public static int getDimensionValue( IContent content, DimensionType d, int referenceLength )
	{
		return getDimensionValue( content, d, 0, referenceLength );
	}

    
	public static int getIntAttribute( Element element, String attribute )
	{
		String value = element.getAttribute( attribute );
		int result = 1;
		if ( value != null && value.length( ) != 0 )
		{
			result = Integer.parseInt( value );
		}
		return result;
	}

	public static DimensionType getDimensionAttribute( Element ele,
			String attribute )
	{
		String value = ele.getAttribute( attribute );
		if ( value == null || 0 == value.length( ) )
		{
			return null;
		}
		return DimensionType.parserUnit( value, DimensionType.UNITS_PX );
	}
	
	public static int getIntValue( CSSValue value )
	{
		if ( value != null && ( value instanceof FloatValue ) )
		{
			FloatValue fv = (FloatValue) value;
			return (int) fv.getFloatValue( );
		}
		return 0;
	}

    public static float getPercentageValue(CSSValue value)
    {
    	if(value!=null && (value instanceof FloatValue))
    	{
    		FloatValue fv = (FloatValue)value;
    		float v = fv.getFloatValue();
    		if (CSSPrimitiveValue.CSS_PERCENTAGE == fv.getPrimitiveType())
    		{
	    		return v/100.0f;
    		}
    	}
    	return 0.0f;
    }
    
    public static boolean isWhiteSpaceNoWrap( CSSValue value )
    {
    	return IStyle.CSS_NOWRAP_VALUE.equals( value.getCssText( ) );
    }
    
}
