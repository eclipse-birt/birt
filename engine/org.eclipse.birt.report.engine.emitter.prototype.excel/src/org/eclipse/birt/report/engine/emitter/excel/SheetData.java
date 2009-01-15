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

	int rspan = 0;

	Span span;

	public int getRspan( )
	{
		return rspan;
	}

	public void setRspan( int rspan )
	{
		this.rspan = rspan;
	}

	public Object getTxt( )
	{
		return txt;
	}

	public void setTxt( Object txt )
	{
		this.txt = txt;
	}

	public HyperlinkDef getUrl( )
	{
		return url;
	}

	public void setUrl( HyperlinkDef url )
	{
		this.url = url;
	}

	StyleEntry style;

	XlsContainer container;

	int datatype;

	Object txt;

	ContainerSizeInfo sizeInfo;

	int styleId;

	int rowSpanInDesign;

	boolean processed;

	HyperlinkDef url;
	
	BookmarkDef bookmark;
	
	int rowIndex;

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

	public int getDatatype( )
	{
		return datatype;
	}

	public void setDatatype( int datatype )
	{
		this.datatype = datatype;
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

	/**
	 * @return
	 */
	public Object getText( )
	{
		return this.txt;
	}

	public HyperlinkDef getHyperlinkDef( )
	{
		return url;
	}

	public void setHyperlinkDef( HyperlinkDef def )
	{
		this.url = def;
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
