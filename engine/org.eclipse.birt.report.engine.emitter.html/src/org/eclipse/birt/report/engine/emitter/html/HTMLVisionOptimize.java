
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.html;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.dom.CellMergedStyle;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.css.CSSValue;

/**
 * 
 */

public class HTMLVisionOptimize extends HTMLEmitter
{
	public HTMLVisionOptimize( HTMLReportEmitter parentEmitter,
			HTMLWriter writer, boolean isEmbeddable )
	{
		super( parentEmitter, writer, isEmbeddable );
	}
	
	/**
	 * Build the style of table content.
	 */
	public void buildTableStyle( ITableContent table, StringBuffer styleBuffer,
			String layoutPreference )
	{
		IStyle style = table.getStyle( );

		addDefaultTableStyles( styleBuffer );

		// display
		DimensionType x = table.getX( );
		DimensionType y = table.getY( );
		int display = getElementType( x, y, null, null, style );
		setDisplayProperty( display,
				HTMLEmitterUtil.DISPLAY_INLINE,
				styleBuffer );

		// shrink
		handleShrink( HTMLEmitterUtil.DISPLAY_BLOCK,
				style,
				table.getHeight( ),
				table.getWidth( ),
				styleBuffer );

		//implement table-layout
		if ( HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals( layoutPreference ) )
		{
			// build the table-layout
			styleBuffer.append( " table-layout:fixed;" );
		}

		buildStyle( table, styleBuffer );
	}
	
	/**
	 * Build the style of column.
	 */
	public void buildColumnStyle( IColumn column, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, column.getWidth( ) );
	}
	
	/**
	 * Build the style of row content.
	 */
	public void buildRowStyle( IRowContent row, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, row.getHeight( ) ); //$NON-NLS-1$
		buildStyle( row, styleBuffer );
	}
	
	/**
	 * Build the style of cell content.
	 */
	public void buildCellStyle( ICellContent cell, StringBuffer styleBuffer,
			boolean isInTableHead )
	{
		//build column related style
		IStyle style = new CellMergedStyle( cell );
		AttributeBuilder.buildStyle( styleBuffer, style, parentEmitter );

		// set font weight to be normal if the cell use "th" tag while it is in
		// table header.
		if ( isInTableHead )
		{
			handleCellFont( cell, styleBuffer );
		}

		buildStyle( cell, styleBuffer );
	}
	
	/**
	 * Handles the Vertical-Align property of the element content
	 * 
	 * @param element
	 *            the styled element content
	 */
	public void handleCellAlign( ICellContent element )
	{
		/* in fireforx, the text-align is used by text item, it defines the alignment
		 * of the content in the text item instead of the text item in its container.
		 * we can put a text item with a width into the cell to see the difference.
		 * We must use computeStyle as the text-align is not inherited across the table.
		 */
		IStyle cellStyle = element.getComputedStyle( );
		CSSValue vAlign = cellStyle.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( null == vAlign || IStyle.BASELINE_VALUE == vAlign )
		{
			vAlign = IStyle.TOP_VALUE;
		}
		writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		CSSValue hAlign = cellStyle.getProperty( IStyle.STYLE_TEXT_ALIGN );
		if ( null != hAlign )
		{
			writer.attribute( HTMLTags.ATTR_ALIGN, hAlign.getCssText( ) );
		}
	}
	
	/**
	 * Build the style of contianer content.
	 */
	public void buildContainerStyle( IContainerContent container,
			StringBuffer styleBuffer, int display )
	{
		// shrink
		handleShrink( display,
				container.getStyle( ),
				container.getHeight( ),
				container.getWidth( ),
				styleBuffer );
		setDisplayProperty( display,
				HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
				styleBuffer );
		buildStyle( container, styleBuffer );
	}
	
	/**
	 * Build the style of text content.
	 */
	public void buildTextStyle( ITextContent text, StringBuffer styleBuffer,
			int display, String url )
	{
		IStyle style = text.getStyle( );
		// check 'can-shrink' property
		handleShrink( display,
				style,
				text.getHeight( ),
				text.getWidth( ),
				styleBuffer );

		if ( url != null )
		{
			setDisplayProperty( display, HTMLEmitterUtil.DISPLAY_BLOCK
					| HTMLEmitterUtil.DISPLAY_INLINE_BLOCK, styleBuffer );
			AttributeBuilder.checkHyperlinkTextDecoration( style, styleBuffer );
		}
		else
		{
			setDisplayProperty( display,
					HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
					styleBuffer );
		}

		// build the text-align
		String textAlign = text.getComputedStyle( ).getTextAlign( );
		if ( textAlign != null )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( textAlign );
			styleBuffer.append( ";" );
		}
		buildStyle( text, styleBuffer );
	}

	/**
	 * Build the style of foreign content.
	 */
	public void buildForeignStyle( IForeignContent foreign,
			StringBuffer styleBuffer, int display, String url )
	{
		IStyle style = foreign.getStyle( );
		// check 'can-shrink' property
		handleShrink( display,
				style,
				foreign.getHeight( ),
				foreign.getWidth( ),
				styleBuffer );

		if ( url != null )
		{
			setDisplayProperty( display, HTMLEmitterUtil.DISPLAY_BLOCK
					| HTMLEmitterUtil.DISPLAY_INLINE_BLOCK, styleBuffer );
			AttributeBuilder.checkHyperlinkTextDecoration( style, styleBuffer );
		}
		else
		{
			setDisplayProperty( display,
					HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
					styleBuffer );
		}

		// build the text-align
		String textAlign = foreign.getComputedStyle( ).getTextAlign( );
		if ( textAlign != null )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( textAlign );
			styleBuffer.append( ";" );
		}
		buildStyle( foreign, styleBuffer );
	}
	
	/**
	 * Build the style of image content.
	 */
	public void buildImageStyle( IImageContent image, StringBuffer styleBuffer )
	{
		// image size
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, image.getWidth( ) ); //$NON-NLS-1$
		buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, image.getHeight( ) ); //$NON-NLS-1$
		buildStyle( image, styleBuffer );
	}
	
	/**
	 * Handle the text-align.
	 * Using the align property to implement the text-align.
	 */
	public void handleHorizontalAlign( IStyle style )
	{
		CSSValue hAlign = style.getProperty( IStyle.STYLE_TEXT_ALIGN );
		if ( null != hAlign )
		{
			writer.attribute( HTMLTags.ATTR_ALIGN, hAlign.getCssText( ) );
		}
	}
	
	/**
	 * Open the vertical-align box tag if the element needs implementing the
	 * vertical-align.
	 */
	public void handleVerticalAlignBegine( IContent element )
	{
		IStyle style = element.getStyle( );
		CSSValue vAlign = style.getProperty(IStyle.STYLE_VERTICAL_ALIGN);
		DimensionType height = element.getHeight( );
		if (vAlign != null &&  vAlign != IStyle.BASELINE_VALUE && height != null )
		{
			// implement vertical align.
			writer.openTag( HTMLTags.TAG_TABLE );
			writer.attribute( HTMLTags.ATTR_STYLE, " width:100%; height:100%;" );
			writer.openTag( HTMLTags.TAG_TR );
			writer.openTag( HTMLTags.TAG_TD );

			StringBuffer textStyleBuffer = new StringBuffer( );
			textStyleBuffer.append( " vertical-align:" );
			textStyleBuffer.append( vAlign.getCssText() );
			textStyleBuffer.append( ";" );
			writer.attribute( HTMLTags.ATTR_STYLE, textStyleBuffer );
		}
	}

	/**
	 * Close the vertical-align box tag if the element needs implementing the
	 * vertical-align.
	 */
	public void handleVerticalAlignEnd( IContent element )
	{
		IStyle style = element.getStyle( );
		CSSValue vAlign = style.getProperty(IStyle.STYLE_VERTICAL_ALIGN);
		DimensionType height = element.getHeight( );
		if (vAlign != null &&  vAlign != IStyle.BASELINE_VALUE && height != null )
		{
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
			writer.closeTag( HTMLTags.TAG_TABLE );
		}
	}
	
	/**
	 * Set the display property to style.
	 * 
	 * @param display
	 *            The display type.
	 * @param mask
	 *            The mask.
	 * @param styleBuffer
	 *            The <code>StringBuffer</code> object that returns 'style'
	 *            content.
	 */
	protected void setDisplayProperty( int display, int mask,
			StringBuffer styleBuffer )
	{
		int flag = display & mask;
		if ( ( display & HTMLEmitterUtil.DISPLAY_NONE ) > 0 )
		{
			styleBuffer.append( "display: none;" ); //$NON-NLS-1$
		}
		else if ( flag > 0 )
		{
			if ( ( flag & HTMLEmitterUtil.DISPLAY_BLOCK ) > 0 )
			{
				styleBuffer.append( "display: block;" ); //$NON-NLS-1$
			}
			else if ( ( flag & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK ) > 0 )
			{
				styleBuffer.append( "display: inline-block;" ); //$NON-NLS-1$
			}
			else if ( ( flag & HTMLEmitterUtil.DISPLAY_INLINE ) > 0 )
			{
				styleBuffer.append( "display: inline;" ); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * Build size style string say, "width: 10.0mm;".
	 * The min-height should be implemented by sepcial way.
	 */
	public void buildSize( StringBuffer content, String name,
			DimensionType value )
	{
		if ( value != null )
		{
			if(HTMLTags.ATTR_MIN_HEIGHT.equals(name))
			{
				//To solve the problem that IE do not support min-height.
				//Use this way to make Firefox and IE both work well.
				content.append( " height: auto !important; height: " );
				content.append( value.toString( ) );
				content.append( "; min-height: " );
				content.append( value.toString( ) );
				content.append( ';' );
			}
			else
			{
				super.buildSize( content, name, value );
			}
		}
	}
	
	public void buildStyle( IContent element, StringBuffer styleBuffer )
	{
		IStyle style;
		if ( isEmbeddable )
		{
			style = element.getStyle( );
		}
		else
		{
			style = element.getInlineStyle( );
		}
		AttributeBuilder.buildStyle( styleBuffer, style, parentEmitter );

		AttributeBuilder.checkHyperlinkTextDecoration( style, styleBuffer );
	}
	
	/**
	 * Handles the font-weight property of the cell content
	 * while the cell is in table header
	 * 
	 * @param element
	 *            the styled element content
	 * @param styleBuffer
	 *            the StringBuffer instance
	 */
	protected void handleCellFont( ICellContent element,
			StringBuffer styleBuffer )
	{
		IStyle style = element.getStyle( );
		String fontWeight = style.getFontWeight( );
		if ( fontWeight == null )
		{
			style = element.getComputedStyle( );
			fontWeight = style.getFontWeight( );
			if ( fontWeight == null )
			{
				fontWeight = "normal";
			}
			styleBuffer.append( "font-weight: " );
			styleBuffer.append( fontWeight );
			styleBuffer.append( ";" );
		}
	}
}
