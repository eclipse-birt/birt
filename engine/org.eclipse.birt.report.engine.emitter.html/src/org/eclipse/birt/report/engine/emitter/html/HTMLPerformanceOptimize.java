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

import java.util.Stack;
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
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 */

public class HTMLPerformanceOptimize extends HTMLEmitter
{

	/**
	 * The <code>cellDisplayStack</code> that stores the display value of cell.
	 */
	private Stack cellDisplayStack = new Stack( );
	
	public HTMLPerformanceOptimize( HTMLReportEmitter parentEmitter,
			HTMLWriter writer, boolean isEmbeddable )
	{
		super( parentEmitter, writer, isEmbeddable );
	}

	/**
	 * Build the style of table content
	 */
	public void buildTableStyle( ITableContent table, StringBuffer styleBuffer,
			String layoutPreference )
	{
		IStyle style = table.getStyle( );

		addDefaultTableStyles( styleBuffer );

		// table doesn¡¯t support shrink
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

		//implement table-layout
		if ( HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals( layoutPreference ) )
		{
			// shrink table will not output table-layout;
			if ( ( null == style )
					|| !"true".equalsIgnoreCase( style.getCanShrink( ) ) )
			{
				// build the table-layout
				styleBuffer.append( " table-layout:fixed;" );
			}
		}

		buildStyle( table, styleBuffer );
	}

	/**
	 * Build the style of column
	 */
	public void buildColumnStyle( IColumn column, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, column.getWidth( ) );
		IStyle style;
		if ( isEmbeddable )
		{
			style = column.getStyle( );
		}
		else
		{
			// "column.getInlineStyle( )" maybe return a null value;
			style = column.getInlineStyle( );
		}
		buildStyle( style, styleBuffer );
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
		// implement the cell's clip.
		styleBuffer.append( "overflow:hidden;" );
		buildStyle( cell, styleBuffer );
	}

	/**
	 * the vertical-align and text-align has already been build in the method
	 * buildCellStyle. this method should be empty.
	 */
	public void handleCellAlign( ICellContent cell )
	{
	}

	/**
	 * Open the container tag.
	 */
	public void openContainerTag( IContainerContent container )
	{
		DimensionType x = container.getX( );
		DimensionType y = container.getY( );
		DimensionType width = container.getWidth( );
		DimensionType height = container.getHeight( );
		int display = getElementType( x, y, width, height, container.getStyle( ) );
		// The display value is pushed in Stack. It will be popped when close the container tag.
		cellDisplayStack.push( new Integer( display ) );
		if ( ( ( display & HTMLEmitterUtil.DISPLAY_INLINE ) > 0 )
				|| ( ( display & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK ) > 0 ) )
		{
			// Open the inlineBox tag when implement the inline box.
			openInlineBoxTag( );
		}
		writer.openTag( HTMLTags.TAG_DIV );
	}

	/**
	 * Close the container tag.
	 */
	public void closeContainerTag( )
	{
		writer.closeTag( HTMLTags.TAG_DIV );
		int display = ( (Integer) cellDisplayStack.pop( ) ).intValue( );
		if ( ( ( display & HTMLEmitterUtil.DISPLAY_INLINE ) > 0 )
				|| ( ( display & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK ) > 0 ) )
		{
			// Close the inlineBox tag when implement the inline box.
			closeInlineBoxTag( );
		}
	}

	/**
	 * Build the style of contianer content.
	 */
	public void buildContainerStyle( IContainerContent container,
			StringBuffer styleBuffer )
	{
		IStyle style = container.getStyle( );
		DimensionType x = container.getX( );
		DimensionType y = container.getY( );
		DimensionType width = container.getWidth( );
		DimensionType height = container.getHeight( );
		int display = getElementType( x, y, width, height, style );
		handleShrink( display, style, height, width, styleBuffer );
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
		buildStyle( foreign, styleBuffer );
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
		buildStyle( image, styleBuffer );
	}

	/**
	 * Handle the text-align.
	 * the vertical-align and text-align has already been build in the method
	 * buildStyle. this method should be empty.
	 */
	public void handleHorizontalAlign( IStyle style )
	{
	}

	/**
	 * Handle the vertical-align.
	 * the vertical-align and text-align has already been build in the method
	 * buildStyle. this method should be empty.
	 */
	public void handleVerticalAlign( IStyle style )
	{
	}

	/**
	 * the vertical-align and text-align has already been build in the method
	 * buildStyle. this method should be empty.
	 */
	public void handleVerticalAlignBegine( IContent element )
	{
	}

	/**
	 * the vertical-align and text-align has already been build in the method
	 * buildStyle. this method should be empty.
	 */
	public void handleVerticalAlignEnd( IContent element )
	{
	}

	/**
	 * Build size style string say, "width: 10.0mm;".
	 */
	public void buildStyle( IContent element, StringBuffer styleBuffer )
	{
		IStyle style = element.getStyle( );
		if ( isEmbeddable )
		{
			buildStyle( style, styleBuffer );
		}
		else
		{
			IStyle inlineStyle = element.getInlineStyle( );
			buildStyle( inlineStyle, styleBuffer );
		}

		// Build the Text Decoration for no embeddable view
		AttributeBuilder.checkHyperlinkTextDecoration( style, styleBuffer );

		// Build the display
		String value = style.getDisplay( );
		if ( null != value )
		{
			styleBuffer.append( " display:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}
	}

	protected void buildStyle( IStyle style, StringBuffer styleBuffer )
	{
		if ( null == style )
		{
			return;
		}

		AttributeBuilder.buildStyle( styleBuffer, style, parentEmitter );

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
}
