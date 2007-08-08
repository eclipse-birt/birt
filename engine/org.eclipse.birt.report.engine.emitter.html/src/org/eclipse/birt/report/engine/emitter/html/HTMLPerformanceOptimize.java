/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.css.CSSValue;

/**
 * 
 */

public class HTMLPerformanceOptimize extends HTMLEmitter
{
	public HTMLPerformanceOptimize( HTMLReportEmitter reportEmitter,
			HTMLWriter writer, boolean isEmbeddable, String layoutPreference )
	{
		super( reportEmitter, writer, isEmbeddable, layoutPreference );
	}
	
	/**
	 * Build the report default style
	 */
	public void buildDefaultStyle( StringBuffer styleBuffer, IStyle style )
	{
		if ( style == null || style.isEmpty( ) )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
		
		// Build the textAlign
		String value = style.getTextAlign( );
		if ( null != value )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
	}

	/**
	 * Build attribute class
	 */
	public void buildStyle( StringBuffer styleBuffer, IStyle style )
	{
		if ( style == null || style.isEmpty( ) )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}
	
	/**
	 * Build the style of the page head and page footer
	 */
	public void buildPageBandStyle( StringBuffer styleBuffer,
			IStyle style )
	{
		if ( style == null || style.isEmpty( ) )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
		
		// Build the vertical-align
		String value = style.getVerticalAlign( );
		if ( null != value )
		{
			styleBuffer.append( " vertical-align:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
		// Build the textAlign
		value = style.getTextAlign( );
		if ( null != value )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
	}
	
	/**
	 * Build the style of table content
	 */
	public void buildTableStyle( ITableContent table, StringBuffer styleBuffer )
	{
		addDefaultTableStyles( styleBuffer );

		// The method getStyle( ) will nevel return a null value;
		IStyle style = table.getStyle( );
		
		// output the display
		CSSValue display = null;
		display = style.getProperty( IStyle.STYLE_DISPLAY );
		if ( IStyle.NONE_VALUE == display )
		{
			styleBuffer.append( " display: none;" );
		}
		else if ( IStyle.INLINE_VALUE == display || IStyle.INLINE_BLOCK_VALUE == display )
		{
			styleBuffer.append( " display:-moz-inline-box !important; display:inline;" );
		}

		// Table doesn¡¯t support shrink. The shrink of table is only used to
		// judge outputting table-layout or not.
		// height
		DimensionType height = table.getHeight( );
		if ( null != height )
		{
			buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, height );
		}
		// width
		DimensionType width = table.getWidth( );
		if ( null != width )
		{
			buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, width );
		}
		else
		{
			styleBuffer.append( " width: 100%;" );
		}

		// implement table-layout
		if ( HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals( layoutPreference ) )
		{
			// shrink table will not output table-layout;
			if ( !"true".equalsIgnoreCase( style.getCanShrink( ) ) )
			{
				// build the table-layout
				styleBuffer.append( " table-layout:fixed;" );
			}
		}
		
		// Build the textAlign
		String value = style.getTextAlign( );
		if ( null != value )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
		// Table doesn¡¯t support vertical-align.

		style = getElementStyle( table );
		if ( style == null )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}

	/**
	 * Build the style of column
	 */
	public void buildColumnStyle( IColumn column, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, column.getWidth( ) );
		
		// The method getStyle( ) will nevel return a null value;
		IStyle style = column.getStyle( );
		// Build the vertical-align
		// In performance optimize model the vertical-align can't be setted to
		// the column. Because we output the vertical-align directly here, and
		// it will cause the conflict with the BIRT, CSS, IE, Firefox. The user
		// should set the vertical-align to the row or cells in this model.
		String value = style.getVerticalAlign( );
		if ( null != value )
		{
			styleBuffer.append( " vertical-align:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
		
		if ( !isEmbeddable )
		{
			style = column.getInlineStyle( );
			if ( style == null )
			{
				return;
			}
		}
		if ( style.isEmpty( ) )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}
	
	/**
	 * Handles the alignment property of the column content.
	 */
	public void handleColumnAlign( IColumn column )
	{
		// Column doesn¡¯t support text-align in BIRT.
	}

	/**
	 * Build the style of row content.
	 */
	public void buildRowStyle( IRowContent row, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, row.getHeight( ) ); //$NON-NLS-1$
		
		IStyle style = getElementStyle( row );
		if ( style == null )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}

	/**
	 * Handles the Text-Align property of the row content.
	 */
	public void handleRowAlign( IRowContent row )
	{
		// The method getStyle( ) will nevel return a null value;
		IStyle style = row.getStyle( );

		// Build the Vertical-Align property of the row content
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( null == vAlign || IStyle.BASELINE_VALUE == vAlign )
		{
			// The default vertical-align value of cell is top. And the cell can
			// inherit the valign from parent row.
			vAlign = IStyle.TOP_VALUE;
		}
		writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		
		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty( IStyle.STYLE_TEXT_ALIGN );
		if ( null != hAlign )
		{
			writer.attribute( HTMLTags.ATTR_ALIGN, hAlign.getCssText( ) );
		}
	}
	
	/**
	 * Build the style of cell content.
	 */
	public void buildCellStyle( ICellContent cell, StringBuffer styleBuffer,
			boolean isHead )
	{
		// implement the cell's clip.
		if ( HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals( layoutPreference ) )
		{
			styleBuffer.append( "overflow:hidden;" );
		}
		
		IStyle style = getElementStyle( cell );
		if ( style == null )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}

	/**
	 * Handles the alignment property of the element content.
	 */
	public void handleCellAlign( ICellContent cell )
	{
		// The method getStyle( ) will nevel return a null value;
		IStyle style = cell.getStyle( );

		// Build the Vertical-Align property of the row content
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( IStyle.BASELINE_VALUE == vAlign )
		{
			vAlign = IStyle.TOP_VALUE;
		}
		if ( null != vAlign )
		{
			// The default vertical-align value has already been outputted on
			// the parent row.
			writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		}
		
		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty( IStyle.STYLE_TEXT_ALIGN );
		if ( null != hAlign )
		{
			writer.attribute( HTMLTags.ATTR_ALIGN, hAlign.getCssText( ) );
		}
	}

	/**
	 * Build the style of contianer content.
	 */
	public void buildContainerStyle( IContainerContent container,
			StringBuffer styleBuffer )
	{
		int display = ( (Integer) containerDisplayStack.peek( ) ).intValue( );
		// shrink
		handleShrink( display,
				container.getStyle( ),
				container.getHeight( ),
				container.getWidth( ),
				styleBuffer );
		setDisplayProperty( display,
				HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
				styleBuffer );

		IStyle style = getElementStyle( container );
		if ( style == null )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}
	
	/**
	 * Handles the alignment property of the container content.
	 */
	public void handleContainerAlign( IContainerContent container )
	{
		// The method getStyle( ) will nevel return a null value;
		IStyle style = container.getStyle( );
		// Container doesn¡¯t support vertical-align.
		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty( IStyle.STYLE_TEXT_ALIGN );
		if ( null != hAlign )
		{
			writer.attribute( HTMLTags.ATTR_ALIGN, hAlign.getCssText( ) );
		}
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
		}
		else
		{
			setDisplayProperty( display,
					HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
					styleBuffer );
		}
		
		// build the text-align
		String textAlign = style.getTextAlign( );
		if ( textAlign != null )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( textAlign );
			styleBuffer.append( ";" );
		}

		style = getElementStyle( text );
		if ( style == null )
		{
			return;
		}

		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
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
		}
		else
		{
			setDisplayProperty( display,
					HTMLEmitterUtil.DISPLAY_INLINE_BLOCK,
					styleBuffer );
		}
		
		// build the text-align
		String textAlign = style.getTextAlign( );
		if ( textAlign != null )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( textAlign );
			styleBuffer.append( ";" );
		}

		style = getElementStyle( foreign );
		if ( style == null )
		{
			return;
		}

		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
	}

	/**
	 * Build the style of image content.
	 */
	public void buildImageStyle( IImageContent image, StringBuffer styleBuffer,
			int display )
	{
		// image size
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, image.getWidth( ) ); //$NON-NLS-1$
		buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, image.getHeight( ) ); //$NON-NLS-1$
		// build the none value of display
		setDisplayProperty( display, 0, styleBuffer );
		
		IStyle style = getElementStyle( image );
		if ( style == null )
		{
			return;
		}
		
		AttributeBuilder.buildFont( styleBuffer, style );
		AttributeBuilder.buildBox( styleBuffer, style );
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
		AttributeBuilder.buildTextDecoration( styleBuffer, style );
		
		// Image doesn¡¯t support vertical-align and text-align.
		// Text-align has been build in the style class. But the text-align
		// doesn¡¯t work with the image.
	}
}
