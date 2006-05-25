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

package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.w3c.dom.css.CSSValue;

/**
 * Represents style of cell with the style of column.
 *
 */
public class CellMergedStyle extends AbstractStyle
{
	IStyle cellStyle;
	IStyle rowStyle;
	IStyle columnStyle;

	/**
	 * Constructor.
	 *
	 * @param cell the cell.
	 */
	public CellMergedStyle( ICellContent cell )
	{
		super( cell.getCSSEngine( ) );
		this.cellStyle = cell.getStyle( );
		IRowContent row = (IRowContent) cell.getParent( );
		if ( row != null )
		{
			IElement parentElt = row.getParent( );
			rowStyle = row.getStyle();
			if ( parentElt instanceof ITableBandContent )
			{
				parentElt = parentElt.getParent( );
			}
			ITableContent table = (ITableContent) parentElt;
			if ( table != null )
			{
				int columnId = cell.getColumn( );
				if ( columnId >= 0 && columnId < table.getColumnCount( ) )
				{
					IColumn column = table.getColumn( columnId );
					columnStyle = column.getStyle( );
				}
			}
		}
	}

	public CSSValue getProperty( int index )
	{
		CSSValue value = null;
		if ( ( !hasProperty( cellStyle, index ) )
				&& ( isBackgroundProperties( index ) || !hasProperty( rowStyle,
						index ) ) )
		{
			value = getColumnStyleValue( index );
		}
		return value;
	}

	public boolean isEmpty( )
	{
		for ( int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++)
		{
			if ( getProperty ( i ) != null )
			{
				return false;
			}
		}
		return true;
	}

	public void setProperty( int index, CSSValue value )
	{
	}

	private boolean isBackgroundProperties( int index )
	{
		if (StyleConstants.STYLE_BACKGROUND_COLOR==index 
				||StyleConstants.STYLE_BACKGROUND_ATTACHMENT==index
				||StyleConstants.STYLE_BACKGROUND_IMAGE==index
				||StyleConstants.STYLE_BACKGROUND_REPEAT==index)
		{
			return true;
		}
		return false;
	}

	private CSSValue getColumnStyleValue( int index )
	{
		return columnStyle == null ? null : columnStyle.getProperty( index );
	}

	private boolean hasProperty( IStyle style, int index )
	{
		return style != null && style.getProperty( index ) != null;
	}
}
