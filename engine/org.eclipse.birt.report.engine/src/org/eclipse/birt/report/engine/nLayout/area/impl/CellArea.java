/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.awt.Color;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.w3c.dom.css.CSSValue;

public class CellArea extends BlockContainerArea implements IContainerArea
{

	static int DEFAULT_PADDING = 1500;
	static LocalProperties CELL_DEFAULT = new LocalProperties( );
	protected int rowSpan = 1;
	protected int colSpan = 1;
	protected int columnID = 0;
	protected int rowID = 0;
	
	static
	{
		CELL_DEFAULT.setPaddingTop( DEFAULT_PADDING );
		CELL_DEFAULT.setPaddingRight( DEFAULT_PADDING );
		CELL_DEFAULT.setPaddingBottom( DEFAULT_PADDING );
		CELL_DEFAULT.setPaddingLeft( DEFAULT_PADDING );
	};

	public CellArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
	}

	public CellArea( )
	{
		super( );
		localProperties = CELL_DEFAULT;
	}

	public CellArea( CellArea cell )
	{
		super( cell );
		rowSpan = cell.rowSpan;
		colSpan = cell.colSpan;
		columnID = cell.columnID;
		rowID = cell.rowID;
	}

	public int getColumnID( )
	{
		return columnID;
	}

	public void setColumnID( int columnID )
	{
		this.columnID = columnID;
	}

	public int getRowID( )
	{
		return rowID;
	}

	public void setRowID( int rowID )
	{
		this.rowID = rowID;
	}

	public int getColSpan( )
	{
		return colSpan;
	}

	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}

	public int getRowSpan( )
	{
		return rowSpan;
	}

	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	public void close( ) throws BirtException
	{
		height = currentBP + getOffsetY( ) + localProperties.getPaddingBottom( );
		checkPageBreak();
		parent.update( this );
		finished = true;
	}

	public void initialize( ) throws BirtException
	{
		ICellContent cellContent = (ICellContent) content;
		rowSpan = cellContent.getRowSpan( );
		columnID = cellContent.getColumn( );
		colSpan = cellContent.getColSpan( );
		TableArea table = getTable( );
		hasStyle = true;
		width = table.getCellWidth( columnID, columnID + colSpan );
		this.buildProperties( cellContent, context );
		table.resolveBorderConflict( this, true );

		maxAvaWidth = getContentWidth( );

		boolean isLastColumn = ( columnID + colSpan == table.getColumnCount( ) );
		if ( !table.isInInlineStacking && isLastColumn )
		{
			isInInlineStacking = false;
		}
		else
		{
			isInInlineStacking = true;
		}
		this.bookmark = content.getBookmark( );
		this.action = content.getHyperlinkAction( );
		parent.add( this );
	}

	protected void buildProperties( IContent content, LayoutContext context )
	{
		IStyle style = content.getComputedStyle( );
		boxStyle = new BoxStyle( );
		Color color = PropertyUtil.getColor( style
				.getProperty( IStyle.STYLE_BACKGROUND_COLOR ) );
		if ( color != null )
		{
			boxStyle.setBackgroundColor( color );
		}
		String url = content.getStyle().getBackgroundImage( );
		if ( url != null )
		{
			boxStyle.setBackgroundImage( new BackgroundImageInfo( getImageUrl( url ), style
					.getProperty( IStyle.STYLE_BACKGROUND_REPEAT ),
					getDimensionValue( style
							.getProperty( IStyle.STYLE_BACKGROUND_POSITION_X ),
							width ), getDimensionValue( style
							.getProperty( IStyle.STYLE_BACKGROUND_POSITION_Y ),
							width ) ) );

		}
		localProperties = new LocalProperties( );
		int maw = parent.getMaxAvaWidth( );
		IStyle cs = content.getStyle( );
		CSSValue padding = cs.getProperty( IStyle.STYLE_PADDING_TOP );
		if ( padding == null )
		{
			localProperties.setPaddingTop( DEFAULT_PADDING );
		}
		else
		{
			localProperties.setPaddingTop( PropertyUtil.getDimensionValue(
					padding, width ) );
		}
		padding = cs.getProperty( IStyle.STYLE_PADDING_BOTTOM );
		if ( padding == null )
		{
			localProperties.setPaddingBottom( DEFAULT_PADDING );
		}
		else
		{
			localProperties.setPaddingBottom( PropertyUtil.getDimensionValue(
					padding, width ) );
		}
		padding = cs.getProperty( IStyle.STYLE_PADDING_LEFT );
		if ( padding == null )
		{
			localProperties.setPaddingLeft( DEFAULT_PADDING );
		}
		else
		{
			localProperties.setPaddingLeft( PropertyUtil.getDimensionValue(
					padding, width ) );
		}
		padding = cs.getProperty( IStyle.STYLE_PADDING_RIGHT);
		if ( padding == null )
		{
			localProperties.setPaddingRight( DEFAULT_PADDING );
		}
		else
		{
			localProperties.setPaddingRight( PropertyUtil.getDimensionValue(
					padding, width ) );
		}
		textAlign = content.getComputedStyle( ).getProperty( IStyle.STYLE_TEXT_ALIGN );
	}

	public CellArea cloneArea( )
	{
		CellArea cell = new CellArea(this);
		cell.setBoxStyle( new BoxStyle( cell.getBoxStyle( ) ) );
		return cell;
	}

	public void update( AbstractArea area )
			throws BirtException
	{
		super.update( area );
		if ( currentIP + area.getAllocatedWidth( ) > getContentWidth( ) )
		{
			setNeedClip( true );
		}
	}


	public boolean isPageBreakAfterAvoid( )
	{
		return false;
	}

	public boolean isPageBreakBeforeAvoid( )
	{
		return false;
	}

	public boolean isPageBreakInsideAvoid( )
	{
		return false;
	}
	
	public CellArea deepClone( )
	{
		CellArea cell = (CellArea) super.deepClone( );
		cell.setBoxStyle( new BoxStyle( cell.getBoxStyle( ) ) );
		return cell;
	}


}
