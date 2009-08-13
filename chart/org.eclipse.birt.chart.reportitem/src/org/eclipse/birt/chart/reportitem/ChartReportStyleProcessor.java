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

package org.eclipse.birt.chart.reportitem;

import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.StringFormatSpecifierImpl;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

/**
 * ChartReportStyleProcessor
 */
public class ChartReportStyleProcessor implements IStyleProcessor
{

	private static final String[][] fontSizes = {
			{
					DesignChoiceConstants.FONT_SIZE_XX_SMALL, "7"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_X_SMALL, "8"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_SMALL, "9"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_MEDIUM, "10"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_LARGE, "11"}, //$NON-NLS-1$  
			{
					DesignChoiceConstants.FONT_SIZE_X_LARGE, "12"}, //$NON-NLS-1$ 
			{
					DesignChoiceConstants.FONT_SIZE_XX_LARGE, "13"}, //$NON-NLS-1$ 
	};

	private DesignElementHandle handle;

	private boolean useCache;

	private org.eclipse.birt.report.engine.content.IStyle dstyle;

	private SimpleStyle cache = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	protected static final IGObjectFactory goFactory = GObjectFactory.instance( );

	/**
	 * The constructor. Default not using cache.
	 * 
	 * @param handle
	 */
	public ChartReportStyleProcessor( DesignElementHandle handle )
	{
		this( handle, false, null );
	}

	/**
	 * The constructor.
	 * 
	 * @param handle
	 * @param style
	 */
	public ChartReportStyleProcessor( DesignElementHandle handle,
			org.eclipse.birt.report.engine.content.IStyle style )
	{
		this( handle, false, style );
	}

	/**
	 * The constructor.
	 * 
	 * @param handle
	 * @param useCache
	 *            specify if use cache.
	 */
	public ChartReportStyleProcessor( DesignElementHandle handle,
			boolean useCache )
	{
		this( handle, useCache, null );
	}

	/**
	 * The constructor. Default not using cache.
	 * 
	 * @param handle
	 * @param useCache
	 *            specify if use cache.
	 * @param style
	 */
	public ChartReportStyleProcessor( DesignElementHandle handle,
			boolean useCache,
			org.eclipse.birt.report.engine.content.IStyle dstyle )
	{
		this.handle = handle;
		this.useCache = useCache;
		this.dstyle = dstyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.model.attribute.StyledComponent)
	 */
	public IStyle getStyle( Chart model, StyledComponent name )
	{
		SimpleStyle ss = null;

		if ( cache == null || !useCache )
		{
			StyleHandle style = handle.getPrivateStyle( );

			ss = new SimpleStyle( );

			String fname = style.getFontFamilyHandle( ).getStringValue( );

			int fsize = getFontSizeIntValue( handle );
			boolean fbold = getFontWeight( style.getFontWeight( ) ) >= 700;
			boolean fitalic = DesignChoiceConstants.FONT_STYLE_ITALIC.equals( style.getFontStyle( ) );
			boolean funder = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( style.getTextUnderline( ) );
			boolean fstrike = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( style.getTextLineThrough( ) );

			if ( dstyle != null )
			{
				CSSValueList valueList = (CSSValueList)dstyle.getProperty( StyleConstants.STYLE_FONT_FAMILY );
				if ( valueList.getLength( ) > 0 )
				{
					fname = valueList.item( 0 ).getCssText( );
				}
				fsize = getSize( dstyle.getProperty( StyleConstants.STYLE_FONT_SIZE ) );
				fbold = isBoldFont( dstyle.getProperty( StyleConstants.STYLE_FONT_WEIGHT));
				fitalic = isItalicFont( dstyle.getFontStyle( ) );
				funder = CSSConstants.CSS_UNDERLINE_VALUE.equals( dstyle.getTextUnderline( ) );
				fstrike = CSSConstants.CSS_LINE_THROUGH_VALUE.equals( dstyle.getTextLineThrough( ) );
			}

			HorizontalAlignment ha = HorizontalAlignment.LEFT_LITERAL;
			if ( DesignChoiceConstants.TEXT_ALIGN_CENTER.equals( style.getTextAlign( ) ) )
			{
				ha = HorizontalAlignment.CENTER_LITERAL;
			}
			else if ( DesignChoiceConstants.TEXT_ALIGN_RIGHT.equals( style.getTextAlign( ) ) )
			{
				ha = HorizontalAlignment.RIGHT_LITERAL;
			}

			VerticalAlignment va = VerticalAlignment.TOP_LITERAL;
			if ( DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals( style.getVerticalAlign( ) ) )
			{
				va = VerticalAlignment.CENTER_LITERAL;
			}
			else if ( DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals( style.getVerticalAlign( ) ) )
			{
				va = VerticalAlignment.BOTTOM_LITERAL;
			}

			TextAlignment ta = goFactory.createTextAlignment( );
			ta.setHorizontalAlignment( ha );
			ta.setVerticalAlignment( va );
			FontDefinition fd = goFactory.createFontDefinition( fname,
					fsize,
					fbold,
					fitalic,
					funder,
					fstrike,
					true,
					0,
					ta );
			ss.setFont( fd );

			ColorHandle ch = style.getColor( );
			if ( dstyle != null )
			{
				ss.setColor( getColor( dstyle.getProperty( StyleConstants.STYLE_COLOR ) ) );
			}
			else if ( ch != null && ch.getRGB( ) != -1 )
			{
				int rgbValue = ch.getRGB( );
				ColorDefinition cd = goFactory.createColorDefinition( ( rgbValue >> 16 ) & 0xff,
						( rgbValue >> 8 ) & 0xff,
						rgbValue & 0xff );
				ss.setColor( cd );
			}
			else
			{
				ss.setColor( goFactory.BLACK( ) );
			}

			ch = style.getBackgroundColor( );
			if ( dstyle != null )
			{
				ss.setBackgroundColor( getColor( dstyle.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) ) );
			}
			else if ( ch != null && ch.getRGB( ) != -1 )
			{
				int rgbValue = ch.getRGB( );
				ColorDefinition cd = goFactory.createColorDefinition( ( rgbValue >> 16 ) & 0xff,
						( rgbValue >> 8 ) & 0xff,
						rgbValue & 0xff );
				ss.setBackgroundColor( cd );
			}

			if ( style.getBackgroundImage( ) != null
					&& style.getBackgroundImage( ).length( ) > 0 )
			{
				String urlString = style.getBackgroundImage( );
				try
				{
					new URL( urlString );

					ss.setBackgroundImage( goFactory.createImage( urlString ) );
				}
				catch ( Exception _ )
				{
					// try with "file" prefix
					if ( urlString != null && urlString.startsWith( "/" ) ) //$NON-NLS-1$
					{
						urlString = "file://" + urlString; //$NON-NLS-1$
					}
					else
					{
						urlString = "file:" + urlString; //$NON-NLS-1$
					}

					try
					{
						new URL( urlString );
						ss.setBackgroundImage( goFactory.createImage( urlString ) );
					}
					catch ( Exception __ )
					{
						logger.log( _ );
					}
				}
			}

			double pt = convertToPixel( style.getPaddingTop( ) );
			double pb = convertToPixel( style.getPaddingBottom( ) );
			double pl = convertToPixel( style.getPaddingLeft( ) );
			double pr = convertToPixel( style.getPaddingRight( ) );
			ss.setPadding( goFactory.createInsets( pt, pl, pb, pr ) );

			String dateTimeFormat = null, stringFormat = null, numberFormat = null;
			if ( dstyle != null )
			{
				dateTimeFormat = dstyle.getDateTimeFormat( );
				stringFormat = dstyle.getStringFormat( );
				numberFormat = dstyle.getNumberFormat( );
			}
			else
			{
				dateTimeFormat = style.getDateTimeFormat( );
				stringFormat = style.getStringFormat( );
				numberFormat = style.getNumberFormat( );
			}
			if ( dateTimeFormat != null )
			{
				ss.setDateTimeFormat( JavaDateFormatSpecifierImpl.create( new DateFormatter( dateTimeFormat ).getFormatCode( ) ) );
			}
			if ( stringFormat != null )
			{
				ss.setStringFormat( StringFormatSpecifierImpl.create( stringFormat ) );
			}
			if ( numberFormat != null )
			{
				ss.setNumberFormat( JavaNumberFormatSpecifierImpl.create( new NumberFormatter( numberFormat ).getFormatCode( ) ) );
			}

			if ( useCache )
			{
				cache = ss;
			}
		}

		if ( useCache )
		{
			ss = cache.copy( );
		}

		return ss;
	}

	/**
	 * Gets the int value of a String described font weight.
	 * 
	 * @param fontWeight
	 *            The String deccribed font weight.s
	 */
	private static int getFontWeight( String fontWeight )
	{
		int weight = 400;
		if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_100 ) )
		{
			weight = 100;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_200 ) )
		{
			weight = 200;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_300 ) )
		{
			weight = 300;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_400 ) )
		{
			weight = 400;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_500 ) )
		{
			weight = 500;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_600 ) )
		{
			weight = 600;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_700 ) )
		{
			weight = 700;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_800 ) )
		{
			weight = 800;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_900 ) )
		{
			weight = 900;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_NORMAL ) )
		{
			weight = 400;
		}
		else if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_BOLD ) )
		{
			weight = 700;
		}
		return weight;
	}

	/**
	 * Get the handle's font size int value. if the font size is relative,
	 * calculate the actual size according to its parent.
	 * 
	 * @param handle
	 *            The style handle to work with the style properties of this
	 *            element.
	 * @return The font size int value
	 */
	private static int getFontSizeIntValue( DesignElementHandle handle )
	{
		if ( handle == null )
		{
			// defulat Medium size.
			return 10;
		}

		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// defulat Medium size.
				return 10;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		StyleHandle style = handle.getPrivateStyle( );
		if ( style != null )
		{
			Object fontSizeValue = style.getFontSize( ).getValue( );
			if ( fontSizeValue instanceof DimensionValue )
			{
				// use parent's font size as the base size for converting
				// sizeValue
				// to a int value.
				int size = getFontSizeIntValue( handle.getContainer( ) );
				return (int) convertToPoint( fontSizeValue, size );
			}
			else if ( fontSizeValue instanceof String )
			{
				String fontSize = (String) fontSizeValue;

				if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
				{
					return getLargerFontSizeIntValue( handle.getContainer( ) );
				}
				else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
				{
					return getSmallerFontSizeIntValue( handle.getContainer( ) );
				}
				else
				{
					for ( int i = 0; i < fontSizes.length; i++ )
					{
						if ( fontSizes[i][0].equals( fontSize ) )
						{
							return Integer.parseInt( fontSizes[i][1] );
						}
					}
				}
			}
		}

		// return Medium default.
		return 10;
	}

	private static int getLargerFontSizeIntValue( DesignElementHandle handle )
	{
		if ( handle == null )
		{
			// defulat Medium size + 1.
			return 10 + 1;
		}

		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// return Medium default + 1.
				return 10 + 1;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = handle.getPrivateStyle( )
				.getFontSize( )
				.getValue( );
		if ( fontSizeValue instanceof DimensionValue )
		{
			int size = getFontSizeIntValue( handle.getContainer( ) );
			return (int) convertToPoint( fontSizeValue, size ) + 1;
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSizeIntValue( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSizeIntValue( handle.getContainer( ) );
			}
			else
			{
				for ( int i = 0; i < fontSizes.length - 1; i++ )
				{
					if ( fontSize.equals( fontSizes[i][0] ) )
					{
						return Integer.parseInt( fontSizes[i + 1][1] );
					}
				}
				return Integer.parseInt( fontSizes[fontSizes.length - 1][1] );
			}
		}
		else
		{
			// return Medium default + 1.
			return 10 + 1;
		}
	}

	private static int getSmallerFontSizeIntValue( DesignElementHandle handle )
	{
		if ( handle == null )
		{
			// defulat Medium size - 1.
			return 10 - 1;
		}

		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// return Medium default - 1.
				return 10 - 1;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = handle.getPrivateStyle( )
				.getFontSize( )
				.getValue( );
		if ( fontSizeValue instanceof DimensionValue )
		{
			int gParentFontSize = getFontSizeIntValue( handle.getContainer( ) );
			int size = (int) convertToPoint( fontSizeValue, gParentFontSize ) - 1;
			if ( size < 1 )
			{
				return 1;
			}
			else
			{
				return size;
			}
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSizeIntValue( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSizeIntValue( handle.getContainer( ) );
			}
			else
			{
				for ( int i = fontSizes.length - 1; i > 0; i-- )
				{
					if ( fontSize.equals( fontSizes[i][0] ) )
					{
						return Integer.parseInt( fontSizes[i - 1][1] );
					}
				}
				return Integer.parseInt( fontSizes[0][1] );
			}
		}
		else
		{
			// return Medium default - 1.
			return 10 - 1;
		}
	}

	/**
	 * Converts object 's units to pixel.
	 * 
	 * @param object
	 * @return The pixel value.
	 */
	private static double convertToPoint( Object object, int baseSize )
	{
		return convertToInch( object, baseSize ) * 72;
	}

	/**
	 * Converts object 's units to pixel.
	 * 
	 * @param object
	 * @return The pixel value.
	 */
	private static double convertToPixel( Object object )
	{
		return convertToInch( object, 0 ) * 72;
	}

	/**
	 * Converts object 's units to inch, with baseSize to compute the relative
	 * unit.
	 * 
	 * @param object
	 *            The origine object, may be DimensionValue or DimensionHandle.
	 * @param baseSize
	 *            The given baseSize used to compute relative unit.
	 * @return The inch value.
	 */
	private static double convertToInch( Object object, int baseSize )
	{
		double inchValue = 0;
		double measure = 0;
		String units = ""; //$NON-NLS-1$

		if ( object instanceof DimensionValue )
		{
			DimensionValue dimension = (DimensionValue) object;
			measure = dimension.getMeasure( );
			units = dimension.getUnits( );
		}
		else if ( object instanceof DimensionHandle )
		{
			DimensionHandle dimension = (DimensionHandle) object;
			measure = dimension.getMeasure( );
			units = dimension.getUnits( );
		}
		else
		{
			// assert false;
		}

		if ( "".equalsIgnoreCase( units ) )//$NON-NLS-1$
		{
			units = DesignChoiceConstants.UNITS_IN;
		}
		if ( DesignChoiceConstants.UNITS_IN.equals( units ) )
		{
			return measure;
		}

		// sets default baseSize to JFace Resources 's default font data 's
		// height.
		if ( baseSize == 0 )
		{
			baseSize = 10;
		}

		// converts relative units to inch.
		if ( DesignChoiceConstants.UNITS_EM.equals( units ) )
		{
			inchValue = DimensionUtil.convertTo( measure * baseSize,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( DesignChoiceConstants.UNITS_EX.equals( units ) )
		{
			inchValue = DimensionUtil.convertTo( measure * baseSize / 3,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( units ) )
		{
			inchValue = DimensionUtil.convertTo( measure * baseSize / 100,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( DesignChoiceConstants.UNITS_PX.equals( units ) )
		{
			inchValue = measure / 72d;
		}
		else
		{ // converts absolute units to inch.
			inchValue = DimensionUtil.convertTo( measure,
					units,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		return inchValue;
	}

	private ColorDefinition getColor( CSSValue value )
	{
		if ( value != null && value instanceof RGBColorValue )
		{
			RGBColorValue color = (RGBColorValue) value;
			try
			{
				return goFactory.createColorDefinition( Math.round( color.getRed( )
						.getFloatValue( CSSPrimitiveValue.CSS_NUMBER ) ),
						Math.round( color.getGreen( )
								.getFloatValue( CSSPrimitiveValue.CSS_NUMBER ) ),
						Math.round( color.getBlue( )
								.getFloatValue( CSSPrimitiveValue.CSS_NUMBER ) ) );
			}
			catch ( RuntimeException ex )
			{
				logger.log( Level.WARNING.intValue( ),
						"invalid color: {0}" + value.toString( ) ); //$NON-NLS-1$
			}
		}
		return null;
	}
	
    private int getSize( CSSValue value )
	{
		if ( value != null && ( value instanceof FloatValue ) )
		{
			return (int) ( ( (FloatValue) value ).getFloatValue( ) / 1000 );
		}
		return 0;
	}
	
    private boolean isBoldFont(CSSValue value)
    {
    	if(value instanceof StringValue && value!=null)
        {
    		String weight = ((StringValue)value).getStringValue();
    		if("bold".equals(weight.toLowerCase()) || "bolder".equals(weight.toLowerCase()) //$NON-NLS-1$ //$NON-NLS-2$
    	            || "600".equals(weight) || "700".equals(weight)  //$NON-NLS-1$//$NON-NLS-2$
    	            || "800".equals(weight) || "900".equals(weight))  //$NON-NLS-1$//$NON-NLS-2$
    	        {
    	            return true;
    	        }
        }
    	return false;
    }
  
    private boolean isItalicFont( String italic )
    {
    	if (CSSConstants.CSS_OBLIQUE_VALUE.equals( italic )
                || CSSConstants.CSS_ITALIC_VALUE.equals(italic))
        {
        	return true;
        }
    	return false;
    }

 
}
