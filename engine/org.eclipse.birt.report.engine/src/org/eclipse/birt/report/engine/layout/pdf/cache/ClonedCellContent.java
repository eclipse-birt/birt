
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

package org.eclipse.birt.report.engine.layout.pdf.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.ir.DimensionType;


public class ClonedCellContent implements ICellContent
{
	protected ICellContent cellContent;
	protected int rowSpan = -1;
	protected int colSpan = -1;
	protected int column = -1;
	
	public ICellContent getCellContent()
	{
		return cellContent;
	}
	
	public ClonedCellContent(ICellContent cellContent, int rowSpan)
	{
		this.cellContent = cellContent;
		this.rowSpan = rowSpan;
	}
	
	public int getColSpan( )
	{
		return cellContent.getColSpan( );
	}

	public int getColumn( )
	{
		return cellContent.getColumn( );
	}

	public IColumn getColumnInstance( )
	{
		return cellContent.getColumnInstance( );
	}

	public boolean getDisplayGroupIcon( )
	{
		return cellContent.getDisplayGroupIcon( );
	}

	public int getRow( )
	{
		return cellContent.getRow( );
	}

	public int getRowSpan( )
	{
		if(rowSpan == -1)
		{
			return cellContent.getRowSpan( );
		}
		return rowSpan;
	}

	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}

	public void setColumn( int column )
	{
		this.column = column;

	}

	public void setDisplayGroupIcon( boolean displayGroupIcon )
	{
		cellContent.setDisplayGroupIcon( displayGroupIcon );
	}

	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	public Object accept( IContentVisitor visitor, Object value )
	{
		return cellContent.accept( visitor, value );
	}

	public String getBookmark( )
	{
		return cellContent.getBookmark( );
	}

	public int getContentType( )
	{
		return cellContent.getContentType( );
	}

	public Object getExtension( int extension )
	{
		return cellContent.getExtension( extension );
	}

	public Object getGenerateBy( )
	{
		return cellContent.getGenerateBy( );
	}

	public DimensionType getHeight( )
	{
		return cellContent.getHeight( );
	}

	public String getHelpText( )
	{
		return cellContent.getHelpText( );
	}

	public IHyperlinkAction getHyperlinkAction( )
	{
		return cellContent.getHyperlinkAction( );
	}

	public IStyle getInlineStyle( )
	{
		return cellContent.getInlineStyle( );
	}

	public InstanceID getInstanceID( )
	{
		return cellContent.getInstanceID( );
	}

	public String getName( )
	{
		return cellContent.getName( );
	}

	public IReportContent getReportContent( )
	{
		return cellContent.getReportContent( );
	}

	public String getStyleClass( )
	{
		return cellContent.getStyleClass( );
	}

	public Object getTOC( )
	{
		return cellContent.getTOC( );
	}

	public DimensionType getWidth( )
	{
		return cellContent.getWidth( );
	}

	public DimensionType getX( )
	{
		return cellContent.getX( );
	}

	public DimensionType getY( )
	{
		return cellContent.getY( );
	}

	public void readContent( DataInputStream in ) throws IOException
	{
		cellContent.readContent( in );

	}

	public void setBookmark( String bookmark )
	{
		cellContent.setBookmark( bookmark );

	}

	public void setExtension( int extension, Object value )
	{
		cellContent.setExtension( extension, value );

	}

	public void setGenerateBy( Object generateBy )
	{
		cellContent.setGenerateBy( generateBy );
	}

	public void setHeight( DimensionType height )
	{
		cellContent.setHeight( height );

	}

	public void setHelpText( String help )
	{
		cellContent.setHelpText( help );

	}

	public void setHyperlinkAction( IHyperlinkAction hyperlink )
	{
		cellContent.setHyperlinkAction( hyperlink );

	}

	public void setInlineStyle( IStyle style )
	{
		cellContent.setInlineStyle( style );

	}

	public void setInstanceID( InstanceID id )
	{
		cellContent.setInstanceID( id );

	}

	public void setName( String name )
	{
		cellContent.setName( name );

	}

	public void setReportContent( IReportContent report )
	{
		cellContent.setReportContent( report );

	}

	public void setStyleClass( String styleClass )
	{
		cellContent.setStyleClass( styleClass );

	}

	public void setTOC( Object toc )
	{
		cellContent.setTOC( toc );

	}

	public void setWidth( DimensionType width )
	{
		cellContent.setWidth( width );

	}

	public void setX( DimensionType x )
	{
		cellContent.setX( x );

	}

	public void setY( DimensionType y )
	{
		cellContent.setY( y );

	}

	public void writeContent( DataOutputStream out ) throws IOException
	{
		cellContent.writeContent( out );

	}

	public Collection getChildren( )
	{
		return cellContent.getChildren( );
	}

	public IElement getParent( )
	{
		return cellContent.getParent( );
	}

	public void setParent( IElement parent )
	{
		cellContent.setParent( parent );
	}

	public CSSEngine getCSSEngine( )
	{
		return cellContent.getCSSEngine( );
	}

	public IStyle getComputedStyle( )
	{
		return cellContent.getComputedStyle( );
	}

	public IStyle getStyle( )
	{
		return cellContent.getStyle( );
	}

}
