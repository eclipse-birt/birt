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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.TemplateArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class PDFTemplateLM extends PDFLeafItemLM
{

	protected IAutoTextContent autoText;
	protected int maxWidth;

	public PDFTemplateLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		assert ( content instanceof IAutoTextContent );
		autoText = (IAutoTextContent) content;
		maxWidth = parent.getMaxAvaWidth( ) - parent.getCurrentIP( );
		handleAutoText( );

	}

	public boolean layoutChildren( )
	{
		ContainerArea con = (ContainerArea) AreaFactory.createInlineContainer(
				autoText, true, true );

		int width = getDimensionValue( autoText.getWidth( ) );
		int height = getDimensionValue( autoText.getHeight( ) );

		IStyle style = autoText.getComputedStyle( );
		int hMarginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
		int hBorderWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
		int hPaddingWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_PADDING_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) );

		int vMarginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_TOP ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_BOTTOM ) );
		int vBorderWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		int vPaddingWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_PADDING_TOP ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) );

		IStyle areaStyle = con.getStyle( );

		if ( width <= 0 )
		{
			width = PropertyUtil.getDimensionValue( style.getFontSize( ) ) * 4
					+ hMarginWidth + hBorderWidth + hPaddingWidth;
		}
		else
		{ // remove the margin if the margin width is larger than the max
			// avaliable width.
			if ( hMarginWidth > maxWidth )
			{
				areaStyle.setProperty( IStyle.STYLE_MARGIN_LEFT,
						IStyle.NUMBER_0 );
				areaStyle.setProperty( IStyle.STYLE_MARGIN_RIGHT,
						IStyle.NUMBER_0 );
				hMarginWidth = 0;
			}
			// remove the border if the border width is larger than the
			// dimension width.
			if ( hBorderWidth > width )
			{
				areaStyle.setProperty( IStyle.STYLE_BORDER_LEFT_WIDTH,
						IStyle.NUMBER_0 );
				areaStyle.setProperty( IStyle.STYLE_BORDER_RIGHT_WIDTH,
						IStyle.NUMBER_0 );
				hBorderWidth = 0;
			}
		}
		if ( height <= 0 )
		{
			height = (int) ( PropertyUtil.getDimensionValue( style
					.getFontSize( ) ) * 1.35 )
					+ vMarginWidth + vBorderWidth + vPaddingWidth;
		}

		int maxContentWidth = maxWidth - hMarginWidth;
		width = ( width > maxContentWidth ) ? maxContentWidth : width;
		Dimension d = new Dimension( );
		d.setDimension( width, height );
		con.setWidth( d.getWidth( ) );
		con.setHeight( d.getHeight( ) );

		Dimension templateDimension = new Dimension( );
		templateDimension.setDimension( con.getContentWidth( ), con
				.getContentHeight( ) );

		TemplateArea templateArea = (TemplateArea) AreaFactory
				.createTemplateArea( autoText, templateDimension );
		con.addChild( templateArea );

		templateArea
				.setPosition(
						getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) ),
						getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) ) );
		parent.addArea( con );

		return false;
	}

	protected void handleAutoText( )
	{
		if ( IAutoTextContent.TOTAL_PAGE == autoText.getType( ) )
		{
			context.addUnresolvedContent( autoText );
		}
		if ( IAutoTextContent.PAGE_NUMBER == autoText.getType( ) )
		{

			String originalPageNumber = autoText.getText( );
			NumberFormatter nf = new NumberFormatter( );
			String patternStr = autoText.getComputedStyle( ).getNumberFormat( );
			nf.applyPattern( patternStr );
			autoText.setText( nf
					.format( Integer.parseInt( originalPageNumber ) ) );
		}
	}

}
