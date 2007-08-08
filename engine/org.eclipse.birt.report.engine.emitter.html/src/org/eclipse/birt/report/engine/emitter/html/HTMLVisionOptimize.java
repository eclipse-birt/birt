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

import java.util.HashMap;
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
import org.eclipse.birt.report.engine.css.dom.CellMergedStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * 
 */

public class HTMLVisionOptimize extends HTMLEmitter
{
	private static HashMap borderStyleMap = null;
	static
	{
		borderStyleMap = new HashMap( );
		borderStyleMap.put( CSSConstants.CSS_NONE_VALUE, new Integer( 0 ) );
		borderStyleMap.put( CSSConstants.CSS_INSET_VALUE, new Integer( 1 ) );
		borderStyleMap.put( CSSConstants.CSS_GROOVE_VALUE, new Integer( 2 ) );
		borderStyleMap.put( CSSConstants.CSS_OUTSET_VALUE, new Integer( 3 ) );
		borderStyleMap.put( CSSConstants.CSS_RIDGE_VALUE, new Integer( 4 ) );
		borderStyleMap.put( CSSConstants.CSS_DOTTED_VALUE, new Integer( 5 ) );
		borderStyleMap.put( CSSConstants.CSS_DASHED_VALUE, new Integer( 6 ) );
		borderStyleMap.put( CSSConstants.CSS_SOLID_VALUE, new Integer( 7 ) );
		borderStyleMap.put( CSSConstants.CSS_DOUBLE_VALUE, new Integer( 8 ) );
	}

	public HTMLVisionOptimize( HTMLReportEmitter reportEmitter,
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
		
		// Table's text-align property will be handled at the row content
		// with the ComputedStyle.
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
	}

	/**
	 * Build the style of column
	 */
	public void buildColumnStyle( IColumn column, StringBuffer styleBuffer )
	{
		buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, column.getWidth( ) );
	}
	
	/**
	 * Handles the alignment property of the column content.
	 */
	public void handleColumnAlign( IColumn column )
	{
		// Column's vertical-align property will be handled at the cell content
		// with the CellMergedStyle.
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
		AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
		AttributeBuilder.buildText( styleBuffer, style );
		AttributeBuilder.buildVisual( styleBuffer, style );
	}
	
	/**
	 * Handles the alignment property of the row content.
	 */
	public void handleRowAlign( IRowContent row )
	{
		IStyle rowComputedStyle = row.getComputedStyle( );

		// Build the Vertical-Align property of the row content
		CSSValue vAlign = rowComputedStyle.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( null == vAlign || IStyle.BASELINE_VALUE == vAlign )
		{
			// The default vertical-align value of cell is top. And the cell can
			// inherit the valign from parent row.
			vAlign = IStyle.TOP_VALUE;
		}
		writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		
		CSSValue hAlign = rowComputedStyle.getProperty( IStyle.STYLE_TEXT_ALIGN );
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
		IStyle cellMergedStyle = new CellMergedStyle( cell );

		// build the font properties
		if ( null != style )
		{
			AttributeBuilder.buildFont( styleBuffer, style );
		}
		AttributeBuilder.buildFont( styleBuffer, cellMergedStyle );
		// set font weight to be normal if the cell use "th" tag while it is in
		// table header
		if ( isHead )
		{
			String fontWeight = null;
			if ( null != style )
			{
				fontWeight = style.getFontWeight( );
			}
			String mergedFontWeight = cellMergedStyle.getFontWeight( );
			if ( null == fontWeight && null == mergedFontWeight )
			{
				// The method getComputedStyle( ) will nevel return a null
				// value;
				IStyle cellComputedStyle = cell.getComputedStyle( );
				if ( null != cellComputedStyle )
				{
					fontWeight = cellComputedStyle.getFontWeight( );
				}
				if ( fontWeight == null )
				{
					fontWeight = "normal";
				}
				styleBuffer.append( "font-weight: " );
				styleBuffer.append( fontWeight );
				styleBuffer.append( ";" );
			}
		}

		// build the box properties except border
		if ( null != style )
		{
			AttributeBuilder.buildMargins( styleBuffer, style );
			AttributeBuilder.buildPaddings( styleBuffer, style );
		}
		AttributeBuilder.buildMargins( styleBuffer, cellMergedStyle );
		AttributeBuilder.buildPaddings( styleBuffer, cellMergedStyle );
		// build the cell's border
		buildCellBorder( cell, styleBuffer );

		if ( null != style )
		{
			AttributeBuilder.buildBackground( styleBuffer, style, reportEmitter );
			AttributeBuilder.buildText( styleBuffer, style );
			AttributeBuilder.buildVisual( styleBuffer, style );
		}
		AttributeBuilder.buildBackground( styleBuffer,
				cellMergedStyle,
				reportEmitter );
		AttributeBuilder.buildText( styleBuffer, cellMergedStyle );
		AttributeBuilder.buildVisual( styleBuffer, cellMergedStyle );
	}

	/**
	 * Handles the alignment property of the element content.
	 */
	public void handleCellAlign( ICellContent cell )
	{
		
		// The method getStyle( ) will nevel return a null value;
		IStyle style = cell.getStyle( );
		
		// Build the Vertical-Align property.
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if( null == vAlign )
		{
			IStyle cellMergedStyle = new CellMergedStyle( cell );
			vAlign = cellMergedStyle.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		}
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
		
		IStyle textComputedStyle = text.getComputedStyle( );
		if( null != textComputedStyle )
		{
			AttributeBuilder.buildTextDecoration( styleBuffer, textComputedStyle );
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
		
		IStyle textComputedStyle = foreign.getComputedStyle( );
		if( null != textComputedStyle )
		{
			AttributeBuilder.buildTextDecoration( styleBuffer, textComputedStyle );
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
		
		// build the text-align
		String textAlign = style.getTextAlign( );
		if ( textAlign != null )
		{
			styleBuffer.append( " text-align:" );
			styleBuffer.append( textAlign );
			styleBuffer.append( ";" );
		}
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
		
		IStyle imageComputedStyle = image.getComputedStyle( );
		if( null != imageComputedStyle )
		{
			AttributeBuilder.buildTextDecoration( styleBuffer, imageComputedStyle );
		}
		
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
		
		// Image doesn¡¯t support vertical-align and text-align.
		// Text-align has been build in the style class. But the text-align
		// doesn¡¯t work with the image.
	}

	/**
	 * Handles the border of a cell
	 * 
	 * @param cell:
	 *            the cell content
	 * @param styleBuffer:
	 *            the buffer to store the tyle building result.
	 */
	protected void buildCellBorder( ICellContent cell,
			StringBuffer styleBuffer )
	{
		// prepare build the cell's border
		int columnCount = -1;
		IStyle cellStyle = null, cellComputedStyle = null;
		IStyle rowStyle = null, rowComputedStyle = null;

		cellStyle = cell.getStyle( );
		cellComputedStyle = cell.getComputedStyle( );
		IRowContent row = (IRowContent) cell.getParent( );
		if ( null != row )
		{
			rowStyle = row.getStyle( );
			rowComputedStyle = row.getComputedStyle( );
			ITableContent table = row.getTable( );
			if ( null != table )
			{
				columnCount = table.getColumnCount( );
			}
		}

		// build the cell's border
		if ( null == rowStyle || cell.getColumn( ) < 0 || columnCount < 1 )
		{
			if ( null != cellStyle )
			{
				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_TOP,
						cellStyle.getBorderTopWidth( ),
						cellStyle.getBorderTopStyle( ),
						cellStyle.getBorderTopColor( ),
						0,
						null,
						null,
						null,
						0 );

				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_RIGHT,
						cellStyle.getBorderRightWidth( ),
						cellStyle.getBorderRightStyle( ),
						cellStyle.getBorderRightColor( ),
						0,
						null,
						null,
						null,
						0 );

				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_BOTTOM,
						cellStyle.getBorderBottomWidth( ),
						cellStyle.getBorderBottomStyle( ),
						cellStyle.getBorderBottomColor( ),
						0,
						null,
						null,
						null,
						0 );

				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_LEFT,
						cellStyle.getBorderLeftWidth( ),
						cellStyle.getBorderLeftStyle( ),
						cellStyle.getBorderLeftColor( ),
						0,
						null,
						null,
						null,
						0 );
			}
		}
		else if ( null == cellStyle )
		{
			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_TOP,
					null,
					null,
					null,
					0,
					rowStyle.getBorderTopWidth( ),
					rowStyle.getBorderTopStyle( ),
					rowStyle.getBorderTopColor( ),
					0 );

			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_RIGHT,
					null,
					null,
					null,
					0,
					rowStyle.getBorderRightWidth( ),
					rowStyle.getBorderRightStyle( ),
					rowStyle.getBorderRightColor( ),
					0 );

			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_BOTTOM,
					null,
					null,
					null,
					0,
					rowStyle.getBorderBottomWidth( ),
					rowStyle.getBorderBottomStyle( ),
					rowStyle.getBorderBottomColor( ),
					0 );

			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_LEFT,
					null,
					null,
					null,
					0,
					rowStyle.getBorderLeftWidth( ),
					rowStyle.getBorderLeftStyle( ),
					rowStyle.getBorderLeftColor( ),
					0 );
		}
		else
		{
			// We have treat the column span. But we haven't treat the row span.
			// It need to be solved in the future.
			int cellWidthValue = getBorderWidthValue( cellComputedStyle,
					IStyle.STYLE_BORDER_TOP_WIDTH );
			int rowWidthValue = getBorderWidthValue( rowComputedStyle,
					IStyle.STYLE_BORDER_TOP_WIDTH );
			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_TOP,
					cellStyle.getBorderTopWidth( ),
					cellStyle.getBorderTopStyle( ),
					cellStyle.getBorderTopColor( ),
					cellWidthValue,
					rowStyle.getBorderTopWidth( ),
					rowStyle.getBorderTopStyle( ),
					rowStyle.getBorderTopColor( ),
					rowWidthValue );

			if ( ( cell.getColumn( ) + cell.getColSpan( ) ) == columnCount )
			{
				cellWidthValue = getBorderWidthValue( cellComputedStyle,
						IStyle.STYLE_BORDER_RIGHT_WIDTH );
				rowWidthValue = getBorderWidthValue( rowComputedStyle,
						IStyle.STYLE_BORDER_RIGHT_WIDTH );
				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_RIGHT,
						cellStyle.getBorderRightWidth( ),
						cellStyle.getBorderRightStyle( ),
						cellStyle.getBorderRightColor( ),
						cellWidthValue,
						rowStyle.getBorderRightWidth( ),
						rowStyle.getBorderRightStyle( ),
						rowStyle.getBorderRightColor( ),
						rowWidthValue );
			}
			else
			{
				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_RIGHT,
						cellStyle.getBorderRightWidth( ),
						cellStyle.getBorderRightStyle( ),
						cellStyle.getBorderRightColor( ),
						0,
						null,
						null,
						null,
						0 );
			}

			cellWidthValue = getBorderWidthValue( cellComputedStyle,
					IStyle.STYLE_BORDER_BOTTOM_WIDTH );
			rowWidthValue = getBorderWidthValue( rowComputedStyle,
					IStyle.STYLE_BORDER_BOTTOM_WIDTH );
			buildCellRowBorder( styleBuffer,
					HTMLTags.ATTR_BORDER_BOTTOM,
					cellStyle.getBorderBottomWidth( ),
					cellStyle.getBorderBottomStyle( ),
					cellStyle.getBorderBottomColor( ),
					cellWidthValue,
					rowStyle.getBorderBottomWidth( ),
					rowStyle.getBorderBottomStyle( ),
					rowStyle.getBorderBottomColor( ),
					rowWidthValue );

			if ( cell.getColumn( ) == 0 )
			{
				cellWidthValue = getBorderWidthValue( cellComputedStyle,
						IStyle.STYLE_BORDER_LEFT_WIDTH );
				rowWidthValue = getBorderWidthValue( rowComputedStyle,
						IStyle.STYLE_BORDER_LEFT_WIDTH );
				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_LEFT,
						cellStyle.getBorderLeftWidth( ),
						cellStyle.getBorderLeftStyle( ),
						cellStyle.getBorderLeftColor( ),
						cellWidthValue,
						rowStyle.getBorderLeftWidth( ),
						rowStyle.getBorderLeftStyle( ),
						rowStyle.getBorderLeftColor( ),
						rowWidthValue );
			}
			else
			{
				buildCellRowBorder( styleBuffer,
						HTMLTags.ATTR_BORDER_LEFT,
						cellStyle.getBorderLeftWidth( ),
						cellStyle.getBorderLeftStyle( ),
						cellStyle.getBorderLeftColor( ),
						0,
						null,
						null,
						null,
						0 );
			}

		}
	}

	/**
	 * Get the border width from a style. It don't support '%'.
	 * 
	 * @param style
	 * @param borderNum
	 * @return
	 */
	private int getBorderWidthValue( IStyle style, int borderNum )
	{
		if ( null == style )
		{
			return 0;
		}
		if ( IStyle.STYLE_BORDER_TOP_WIDTH != borderNum
				&& IStyle.STYLE_BORDER_RIGHT_WIDTH != borderNum
				&& IStyle.STYLE_BORDER_BOTTOM_WIDTH != borderNum
				&& IStyle.STYLE_BORDER_LEFT_WIDTH != borderNum )
		{
			return 0;
		}
		CSSValue value = style.getProperty( borderNum );
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
			}
		}
		return 0;
	}

	/**
	 * Treat the conflict of cell border and row border
	 * 
	 * @param content
	 * @param borderName
	 * @param cellBorderWidth
	 * @param cellBorderStyle
	 * @param cellBorderColor
	 * @param cellWidthValue
	 * @param rowBorderWidth
	 * @param rowBorderStyle
	 * @param rowBorderColor
	 * @param rowWidthValue
	 */
	private void buildCellRowBorder( StringBuffer content, String borderName,
			String cellBorderWidth, String cellBorderStyle,
			String cellBorderColor, int cellWidthValue, String rowBorderWidth,
			String rowBorderStyle, String rowBorderColor, int rowWidthValue )
	{
		boolean bUseCellBorder = true;// true means choose cell's border;
		// false means choose row's border
		if ( null == rowBorderStyle )
		{
		}
		else if ( null == cellBorderStyle )
		{
			bUseCellBorder = false;
		}
		else if ( cellBorderStyle.matches( "hidden" ) )
		{
		}
		else if ( rowBorderStyle.matches( "hidden" ) )
		{
			bUseCellBorder = false;
		}
		else if ( rowBorderStyle.matches( CSSConstants.CSS_NONE_VALUE ) )
		{
		}
		else if ( cellBorderStyle.matches( CSSConstants.CSS_NONE_VALUE ) )
		{
			bUseCellBorder = false;
		}
		else if ( rowWidthValue < cellWidthValue )
		{
		}
		else if ( rowWidthValue > cellWidthValue )
		{
			bUseCellBorder = false;
		}
		else if ( !cellBorderStyle.matches( rowBorderStyle ) )
		{
			Integer iCellBorderLevel = ( (Integer) borderStyleMap.get( cellBorderStyle ) );
			Integer iRowBorderLevel = ( (Integer) borderStyleMap.get( rowBorderStyle ) );
			if ( null == iCellBorderLevel )
			{
				iCellBorderLevel = new Integer( -1 );
			}
			if ( null == iRowBorderLevel )
			{
				iRowBorderLevel = new Integer( -1 );
			}

			if ( iRowBorderLevel.intValue( ) > iCellBorderLevel.intValue( ) )
			{
				bUseCellBorder = false;
			}
		}

		if ( bUseCellBorder )
		{
			AttributeBuilder.buildBorder( content,
					borderName,
					cellBorderWidth,
					cellBorderStyle,
					cellBorderColor );
		}
		else
		{
			AttributeBuilder.buildBorder( content,
					borderName,
					rowBorderWidth,
					rowBorderStyle,
					rowBorderColor );
		}
	}
}
