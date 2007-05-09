
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
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 */

public class HTMLPerformanceOptimize extends HTMLEmitter
{
	public HTMLPerformanceOptimize( HTMLReportEmitter parentEmitter,
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
		if ( null != style )
		{
			buildStyle( style, styleBuffer );
		}
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
		IStyle style;
		if ( isEmbeddable )
		{
			style = element.getStyle( );
		}
		else
		{
			style = element.getInlineStyle( );
		}
		buildStyle( style, styleBuffer );
	}

	protected void buildStyle( IStyle style, StringBuffer styleBuffer )
	{
		if ( null == style )
		{
			return;
		}
		
		AttributeBuilder.buildStyle( styleBuffer, style, parentEmitter );

		AttributeBuilder.checkHyperlinkTextDecoration( style, styleBuffer );

		// Build the display
		String value = style.getDisplay( );
		if ( null != value )
		{
			styleBuffer.append( " display:" );
			styleBuffer.append( value );
			styleBuffer.append( ";" );
		}

		// Build the vertical-align
		value = style.getVerticalAlign( );
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
