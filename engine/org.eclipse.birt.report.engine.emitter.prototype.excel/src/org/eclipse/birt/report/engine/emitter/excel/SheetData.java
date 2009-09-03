/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;

import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;

public abstract class SheetData
{

	// TODO: change type to int
	public static final int DATE = 0;
	public static final int NUMBER = 1;
	public static final int STRING = 2;
	public static final int CALENDAR = 3;
	public static final int CDATETIME = 4;
	public static final int IMAGE = 5;
	public static final int BOOLEAN = 6;

	int rspan = 0;

	Span span;

	int rowIndex;

	private static final Color HYPERLINK_COLOR = Color.blue;

	public Object getValue( )
	{
		return value;
	}

	public void setValue( Object txt )
	{
		this.value = txt;
	}

	StyleEntry style;

	XlsContainer container;

	protected int dataType;

	Object value;

	protected ContainerSizeInfo sizeInfo;

	int styleId;

	int rowSpanInDesign;

	boolean processed;

	HyperlinkDef hyperLink;
	
	BookmarkDef bookmark;
	
	BookmarkDef linkedBookmark;

	protected double height;

	public double getHeight( )
	{
		return Math.max( height, 0 );
	}

	public void setHeight( double height )
	{
		this.height = height;
	}

	public BookmarkDef getLinkedBookmark( )
	{
		return linkedBookmark;
	}

	public void setLinkedBookmark( BookmarkDef linkedBookmark )
	{
		this.linkedBookmark = linkedBookmark;
	}

	public int getRowIndex( )
	{
		return rowIndex;
	}

	public void setRowIndex( int rowIndex )
	{
		this.rowIndex = rowIndex;
	}

	public StyleEntry getStyle( )
	{
		return style;
	}

	public void setStyle( StyleEntry style )
	{
		this.style = style;
	}

	public XlsContainer getContainer( )
	{
		return container;
	}

	public void setContainer( XlsContainer container )
	{
		this.container = container;
	}

	public int getDataType( )
	{
		return dataType;
	}

	public void setDataType( int datatype )
	{
		this.dataType = datatype;
	}

	public ContainerSizeInfo getSizeInfo( )
	{
		return sizeInfo;
	}

	public void setSizeInfo( ContainerSizeInfo sizeInfo )
	{
		this.sizeInfo = sizeInfo;
	}

	public boolean isBlank( )
	{
		return false;
	}

	public void clearContainer( )
	{
		container = null;
	}

	public int getRowSpanInDesign( )
	{
		return rowSpanInDesign;
	}

	public void setRowSpanInDesign( int rowSpan )
	{
		this.rowSpanInDesign = rowSpan;
	}

	public boolean isProcessed( )
	{
		return processed;
	}

	public void setProcessed( boolean i )
	{
		processed = i;
	}

	public void setStyleId( int id )
	{
		this.styleId = id;
	}

	public int getStyleId( )
	{
		return styleId;
	}

	public HyperlinkDef getHyperlinkDef( )
	{
		return hyperLink;
	}

	public void setHyperlinkDef( HyperlinkDef def )
	{
		this.hyperLink = def;
		if ( hyperLink != null )
		{
			style.setProperty( StyleConstant.COLOR_PROP, HYPERLINK_COLOR );
			style.setProperty( StyleConstant.TEXT_UNDERLINE_PROP, true );
			style.setName( StyleEntry.ENTRYNAME_HYPERLINK );
		}
	}

	public void setSpan( Span span )
	{
		this.span = span;
	}

	public Span getSpan( )
	{
		return span;
	}

	public int getRowSpan( )
	{
		return rspan;
	}

	public void setRowSpan( int rs )
	{
		if ( rs > 0 )
		{
			this.rspan = rs;
		}
	}

	public void decreasRowSpanInDesign( )
	{
		rowSpanInDesign--;
	}
	
	public BookmarkDef getBookmark( )
	{
		return bookmark;
	}

	public void setBookmark( BookmarkDef bookmark )
	{
		this.bookmark = bookmark;
	}
}
