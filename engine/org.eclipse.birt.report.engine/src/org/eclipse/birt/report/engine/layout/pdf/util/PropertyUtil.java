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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;



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
    
}
