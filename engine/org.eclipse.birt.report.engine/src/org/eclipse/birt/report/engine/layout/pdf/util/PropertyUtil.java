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
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;



public class PropertyUtil
{
	private static Logger logger = Logger.getLogger( PropertyUtil.class.getName() );
	public final static int LEFT = 0;
	public final static int TOP = 1;
	public final static int RIGHT = 2;
	public final static int BOTTOM = 3;
	
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
    		String weight = value.getCssText( );
    		if("bold".equals(weight.toLowerCase()) || "bolder".equals(weight.toLowerCase()) //$NON-NLS-1$ //$NON-NLS-2$
    	            || "600".equals(weight) || "700".equals(weight)  //$NON-NLS-1$//$NON-NLS-2$
    	            || "800".equals(weight) || "900".equals(weight))  //$NON-NLS-1$//$NON-NLS-2$
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
            return "inline".equals(style.getDisplay()); //$NON-NLS-1$
        }
        return false;
    }
    
    
    //FIXME here may contain the info of the line. eg: the line color, style, and width
    public static void getTextDecoration(String textDecoration)
    {
    	
    }
    public static int getLineHeight(String lineHeight)
    {
    	try
    	{
    		if( lineHeight.equalsIgnoreCase( "normal" ))
    		{
    			//BUG 147861: we return *0* as the default value of the *lineLight*
    			return 0;
    		}
    		return Integer.parseInt(lineHeight);
    	}
    	catch(NumberFormatException ex)
    	{
    		logger.log(Level.WARNING, "invalid line height: {0}", lineHeight ); //$NON-NLS-1$
    		return 0;
    	}
    }
    
    
    public static Color getColor(CSSValue value)
    {
    	if(value!=null && value instanceof RGBColorValue)
    	{
    		RGBColorValue color = (RGBColorValue)value;
    		try
    		{
    		return new Color(color.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)/255.0f,
    				color.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)/255.0f,
    				color.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)/255.0f);
    		}
    		catch(RuntimeException ex)
    		{
    			 logger.log(Level.WARNING, "invalid color: {0}", value ); //$NON-NLS-1$
    		}
    	}
    	return null;
    }
    
    public static String getBackgroundImage(CSSValue value)
    {
    	if(value!=null && value instanceof StringValue)
    	{
    		String strValue = ((StringValue)value).getStringValue();
    		if(strValue!=null && (!CSSConstants.CSS_NONE_VALUE.equals(strValue)))
    		{
    			return strValue;
    		}
    	}
    	return null;
    }



   /**
     * if invalid value return -1.
     * @param d
     * @return
     */
    public static int getDimensionValue(String d)
    {
    	
    	if(d==null)
    	{
    		return 0;
    	}
    	try
    	{
	    	if(d.endsWith("in") || d.endsWith("in")) //$NON-NLS-1$ //$NON-NLS-2$
	    	{
	    		return (int)((Float.valueOf(d.substring(0, d.length()-2)).floatValue())*72000.0f);
	    	}
	    	else if(d.endsWith("cm") || d.endsWith("CM"))  //$NON-NLS-1$//$NON-NLS-2$
	    	{
	    		return (int)((Float.valueOf(d.substring(0, d.length()-2)).floatValue())*72000.0f/2.54f);
	    	}
	    	else if(d.endsWith("mm") || d.endsWith("MM")) //$NON-NLS-1$ //$NON-NLS-2$
	    	{
	    		return (int)((Float.valueOf(d.substring(0, d.length()-2)).floatValue())*7200.0f/2.54f);
	    	}
	    	else if(d.endsWith("px") || d.endsWith("PX"))  //$NON-NLS-1$//$NON-NLS-2$
	    	{
	    		return (int)((Float.valueOf(d.substring(0, d.length()-2)).floatValue())/96.0f*72000.0f);//set as 96dpi
	    	}
	    	else
	    	{
	    		return (int)((Float.valueOf(d).floatValue( )));
	    	}
    	}
    	catch(NumberFormatException ex)
    	{
    		ex.printStackTrace();
    		return 0;
    	}
    }
    
    /**
     * FIXME
     * until now we only support absolute dimension value
     * @param d
     * @return
     */
    public static int getDimensionValue(DimensionType d)
    {
    	if(d!=null)
    	{
    		try
    		{
    			String units = d.getUnits();
    			if(units.equals(EngineIRConstants.UNITS_PT)
    					|| units.equals(EngineIRConstants.UNITS_CM)
    					|| units.equals(EngineIRConstants.UNITS_MM)
    					|| units.equals(EngineIRConstants.UNITS_PC)
    					|| units.equals(EngineIRConstants.UNITS_IN))
    			{		
    				double point = d.convertTo(EngineIRConstants.UNITS_PT) * 1000;
    				return (int)point;
    			}
    			else if(units.equals(EngineIRConstants.UNITS_PX))
    			{
    				double point = d.getMeasure()/72.0d * 72000d ;
    				return (int)point;
    			}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			return 0;
    		}
    	}
    	return 0;
    }
    
    
    public static int getDimensionValue(CSSValue value)
    {
    	if(value!=null && (value instanceof FloatValue))
    	{
    		FloatValue fv = (FloatValue)value;
    		float v = fv.getFloatValue();
    		switch(fv.getPrimitiveType())
    		{
    		case CSSPrimitiveValue.CSS_CM:
    			return (int)(v*72000/2.54);
    			
    		case CSSPrimitiveValue.CSS_IN:
    			return (int)(v*72000);
    			
    		case CSSPrimitiveValue.CSS_MM:
    			return (int)(v*7200/2.54);
    			
    		case CSSPrimitiveValue.CSS_PT:
    			return (int)(v*1000);
    		case CSSPrimitiveValue.CSS_NUMBER:
    			return (int)v;
    		}
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
    
    public static int getTopAllocatedSpace(IContent content)
    {
    	if(content!=null)
    	{
    		IStyle style = content.getComputedStyle( );
    		return getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP)) 
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP));
    	}
    	return 0;
    }
    
    public static int getLeftAllocatedSpace(IStyle style)
    {
    	if(style!=null)
    	{
    		return getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT)) 
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH))
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT));
    	}
    	return 0;
    }
    
    public static int getRightAllocatedSpace(IStyle style)
    {
    	if(style!=null)
    	{
    		return getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT)) 
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH))
    			+ getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT));
    	}
    	return 0;
    }
}
