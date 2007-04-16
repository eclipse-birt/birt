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

import java.util.HashMap;
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

	/**
	 * The <code>cellDisplayStack</code> that stores the display value of cell.
	 */
	private Stack cellDisplayStack = new Stack( );

	public HTMLVisionOptimize( HTMLReportEmitter parentEmitter,
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

		buildCellBaseStyle( cell, styleBuffer );
	}

	/**
	 * Handles the Vertical-Align property of the element content.
	 */
	public void handleCellAlign( ICellContent cell )
	{
		/*
		 * in fireforx, the text-align is used by text item, it defines the
		 * alignment of the content in the text item instead of the text item in
		 * its container. we can put a text item with a width into the cell to
		 * see the difference. We must use computeStyle as the text-align is not
		 * inherited across the table.
		 */
		IStyle cellStyle = cell.getComputedStyle( );
		CSSValue vAlign = cellStyle.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( null == vAlign || IStyle.BASELINE_VALUE == vAlign )
		{
			// The default vertical-align value of cell is top.
			vAlign = IStyle.TOP_VALUE;
		}
		writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		handleHorizontalAlign( cellStyle );
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
		int display = ( (Integer) cellDisplayStack.peek( ) ).intValue( );
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
	 * Handle the vertical-align.
	 * Using the valign property to implement the text-align.
	 */
	public void handleVerticalAlign( IStyle style )
	{
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( null != vAlign )
		{
			writer.attribute( HTMLTags.ATTR_VALIGN, vAlign.getCssText( ) );
		}
	}

	/**
	 * Open the vertical-align box tag if the element needs implementing the
	 * vertical-align.
	 */
	public void handleVerticalAlignBegine( IContent element )
	{
		IStyle style = element.getStyle( );
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		CSSValue canShrink = style.getProperty( IStyle.STYLE_CAN_SHRINK );
		DimensionType height = element.getHeight( );
		if ( vAlign != null
				&& vAlign != IStyle.BASELINE_VALUE && height != null
				&& canShrink != IStyle.TRUE_VALUE )
		{
			// implement vertical align.
			writer.openTag( HTMLTags.TAG_TABLE );
			StringBuffer nestingTableStyleBuffer = new StringBuffer( );
			nestingTableStyleBuffer.append( " width:100%; height:" );
			nestingTableStyleBuffer.append( height.toString( ) );
			writer.attribute( HTMLTags.ATTR_STYLE, nestingTableStyleBuffer );
			writer.openTag( HTMLTags.TAG_TR );
			writer.openTag( HTMLTags.TAG_TD );

			StringBuffer textStyleBuffer = new StringBuffer( );
			textStyleBuffer.append( " vertical-align:" );
			textStyleBuffer.append( vAlign.getCssText( ) );
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
		CSSValue vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		CSSValue canShrink = style.getProperty( IStyle.STYLE_CAN_SHRINK );
		DimensionType height = element.getHeight( );
		if ( vAlign != null
				&& vAlign != IStyle.BASELINE_VALUE && height != null
				&& canShrink != IStyle.TRUE_VALUE )
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
			if ( HTMLTags.ATTR_MIN_HEIGHT.equals( name ) )
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
	 * Handles the style of a cell
	 * 
	 * @param cell:
	 *            the cell content
	 * @param styleBuffer:
	 *            the buffer to store the tyle building result.
	 */
	protected void buildCellBaseStyle( ICellContent cell,
			StringBuffer styleBuffer )
	{
		IStyle style = null;
		if ( isEmbeddable )
		{
			style = cell.getStyle( );
		}
		else
		{
			style = cell.getInlineStyle( );
		}
		// build the cell's style except border
		AttributeBuilder.buildCellStyle( styleBuffer, style, parentEmitter );

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

		// output in-line style
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
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

	/**
	 * Handles the font-weight property of the cell content while the cell is in
	 * table header
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
