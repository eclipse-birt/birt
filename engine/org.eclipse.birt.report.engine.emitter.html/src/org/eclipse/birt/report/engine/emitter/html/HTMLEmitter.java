
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
import org.eclipse.birt.report.engine.ir.EngineIRConstants;

/**
 * 
 */

public abstract class HTMLEmitter
{
	protected HTMLReportEmitter parentEmitter;
	protected HTMLWriter writer;
	protected boolean isEmbeddable;
	protected String layoutPreference;

	public HTMLEmitter( HTMLReportEmitter parentEmitter, HTMLWriter writer,
			boolean isEmbeddable, String layoutPreference )
	{
		this.parentEmitter = parentEmitter;
		this.writer = writer;
		this.isEmbeddable = isEmbeddable;
		this.layoutPreference = layoutPreference;
	}
	
	public abstract void buildStyle( IContent element, StringBuffer styleBuffer );

	public abstract void buildTableStyle( ITableContent table,
			StringBuffer styleBuffer );
	
	public abstract void buildColumnStyle( IColumn column,
			StringBuffer styleBuffer );
	
	public abstract void buildRowStyle( IRowContent row,
			StringBuffer styleBuffer );
	
	public abstract void buildCellStyle( ICellContent cell,
			StringBuffer styleBuffer, boolean isInTableHead );
	
	public abstract void handleCellAlign( ICellContent cell );
	
	public abstract void buildContainerStyle( IContainerContent container,
			StringBuffer styleBuffer, int display );
	
	public abstract void buildTextStyle( ITextContent text,
			StringBuffer styleBuffer, int display, String url );

	public abstract void buildForeignStyle( IForeignContent foreign,
			StringBuffer styleBuffer, int display, String url );

	public abstract void buildImageStyle( IImageContent image,
			StringBuffer styleBuffer );
	
	public abstract void handleHorizontalAlign( IStyle style );
	
	public abstract void handleVerticalAlignBegine( IContent element );

	public abstract void handleVerticalAlignEnd( IContent element );
	
	/**
	 * Build size style string say, "width: 10.0mm;".
	 * 
	 * @param content
	 *            The <code>StringBuffer</code> to which the result is output.
	 * @param name
	 *            The property name
	 * @param value
	 *            The values of the property
	 */
	public void buildSize( StringBuffer content, String name,
			DimensionType value )
	{
		if ( value != null )
		{
			content.append( ' ' );
			content.append( name );
			content.append( ": " );
			content.append( value.toString( ) );
			content.append( ';' );
		}
	}
	
	/**
	 * adds the default table styles
	 * 
	 * @param styleBuffer
	 */
	protected void addDefaultTableStyles( StringBuffer styleBuffer )
	{
		styleBuffer.append( "border-collapse: collapse; empty-cells: show;" ); //$NON-NLS-1$
	}
	
	/**
	 * Checks whether the element is block, inline or inline-block level. In
	 * BIRT, the absolute positioning model is used and a box is explicitly
	 * offset with respect to its containing block. When an element's x or y is
	 * set, it will be treated as a block level element regardless of the
	 * 'Display' property set in style. When designating width or height value
	 * to an inline element, it will be treated as inline-block.
	 * 
	 * @param x
	 *            Specifies how far a box's left margin edge is offset to the
	 *            right of the left edge of the box's containing block.
	 * @param y
	 *            Specifies how far an absolutely positioned box's top margin
	 *            edge is offset below the top edge of the box's containing
	 *            block.
	 * @param width
	 *            The width of the element.
	 * @param height
	 *            The height of the element.
	 * @param style
	 *            The <code>IStyle</code> object.
	 * @return The display type of the element.
	 */
	public int getElementType( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height, IStyle style )
	{
		int type = 0;
		String display = null;
		if ( style != null )
		{
			display = style.getDisplay( );
		}

		if ( EngineIRConstants.DISPLAY_NONE.equalsIgnoreCase( display ) )
		{
			type |= HTMLEmitterUtil.DISPLAY_NONE;
		}

		if ( x != null || y != null )
		{
			return type | HTMLEmitterUtil.DISPLAY_BLOCK;
		}
		else if ( EngineIRConstants.DISPLAY_INLINE.equalsIgnoreCase( display ) )
		{
			type |= HTMLEmitterUtil.DISPLAY_INLINE;
			if ( width != null || height != null )
			{
				type |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
			}
			return type;
		}

		return type | HTMLEmitterUtil.DISPLAY_BLOCK;
	}
	
	/**
	 * Checks the 'CanShrink' property and sets the width and height according
	 * to the table below:
	 * <p>
	 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart showing
	 * symbol, location, localized, and meaning.">
	 * <tr bgcolor="#ccccff">
	 * <th align=left>CanShrink</th>
	 * <th align=left>Element Type</th>
	 * <th align=left>Width</th>
	 * <th align=left>Height</th>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2"><code>true(by default)</code></td>
	 * <td>in-line</td>
	 * <td>ignor</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>ignor</td>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2" bgcolor="#eeeeff"><code>false</code></td>
	 * <td>in-line</td>
	 * <td>replaced by 'min-width' property</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>replaced by 'min-height' property</td>
	 * </tr>
	 * </table>
	 * 
	 * @param type
	 *            The display type of the element.
	 * @param style
	 *            The style of an element.
	 * @param height
	 *            The height property.
	 * @param width
	 *            The width property.
	 * @param styleBuffer
	 *            The <code>StringBuffer</code> object that returns 'style'
	 *            content.
	 * @return A <code>boolean</code> value indicating 'Can-Shrink' property
	 *         is set to <code>true</code> or not.
	 */
	protected boolean handleShrink( int type, IStyle style,
			DimensionType height, DimensionType width, StringBuffer styleBuffer )
	{
		boolean canShrink = style == null
				|| !"false".equalsIgnoreCase( style.getCanShrink( ) ); //$NON-NLS-1$

		if ( ( type & HTMLEmitterUtil.DISPLAY_BLOCK ) > 0 )
		{
			buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, width );
			if ( !canShrink )
			{
				buildSize( styleBuffer,
						HTMLTags.ATTR_MIN_HEIGHT, height );
			}
		}
		else if ( ( type & HTMLEmitterUtil.DISPLAY_INLINE ) > 0 )
		{
			buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT,
					height );
			if ( !canShrink )
			{
				buildSize( styleBuffer,
						HTMLTags.ATTR_MIN_WIDTH, width );
			}

		}
		else
		{
			assert false;
		}
		return canShrink;
	}
}
