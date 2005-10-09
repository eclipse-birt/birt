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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.birt.report.designer.util.CSSUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * ChartReportStyleProcessor
 */
public class ChartReportStyleProcessor implements IStyleProcessor
{

	private DesignElementHandle handle;

	/**
	 * @param handle
	 */
	public ChartReportStyleProcessor( DesignElementHandle handle )
	{
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.model.attribute.StyledComponent)
	 */
	public IStyle getStyle( StyledComponent name )
	{
		StyleHandle style = handle.getPrivateStyle( );

		SimpleStyle ss = new SimpleStyle( );

		String fname = style.getFontFamilyHandle( ).getStringValue( );
		int fsize = DEUtil.getFontSizeIntValue( handle );
		boolean fbold = CSSUtil.getFontWeight( style.getFontWeight( ) ) >= 700;
		boolean fitalic = DesignChoiceConstants.FONT_STYLE_ITALIC.equals( style.getFontStyle( ) );
		boolean funder = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( style.getTextUnderline( ) );
		boolean fstrike = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( style.getTextLineThrough( ) );

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

		TextAlignment ta = TextAlignmentImpl.create( );
		ta.setHorizontalAlignment( ha );
		ta.setVerticalAlignment( va );
		FontDefinition fd = FontDefinitionImpl.create( fname,
				(float) fsize,
				fbold,
				fitalic,
				funder,
				fstrike,
				true,
				0,
				ta );
		ss.setFont( fd );

		ColorHandle ch = style.getColor( );
		if ( ch != null && ch.getRGB( ) != -1 )
		{
			int rgb = ch.getRGB( );
			ColorDefinition cd = ColorDefinitionImpl.create( ( rgb >> 16 ) & 0xff,
					( rgb >> 8 ) & 0xff,
					rgb & 0xff );
			ss.setColor( cd );
		}
		else
		{
			ss.setColor( ColorDefinitionImpl.BLACK( ) );
		}

		ch = style.getBackgroundColor( );
		if ( ch != null && ch.getRGB( ) != -1 )
		{
			int rgb = ch.getRGB( );
			ColorDefinition cd = ColorDefinitionImpl.create( ( rgb >> 16 ) & 0xff,
					( rgb >> 8 ) & 0xff,
					rgb & 0xff );
			ss.setBackgroundColor( cd );
		}

		if ( style.getBackgroundImage( ) != null
				&& style.getBackgroundImage( ).length( ) > 0 )
		{
			ss.setBackgroundImage( ImageImpl.create( style.getBackgroundImage( ) ) );
		}

		double pt = CSSUtil.convertToPixel( style.getPaddingTop( ) );
		double pb = CSSUtil.convertToPixel( style.getPaddingBottom( ) );
		double pl = CSSUtil.convertToPixel( style.getPaddingLeft( ) );
		double pr = CSSUtil.convertToPixel( style.getPaddingRight( ) );
		ss.setPadding( InsetsImpl.create( pt, pl, pb, pr ) );

		return ss;
	}
}
