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

package org.eclipse.birt.report.engine.presentation;

import java.util.List;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class ContentEmitterVisitor implements IContentVisitor
{

	IContentEmitter emitter;

	public ContentEmitterVisitor( IContentEmitter emitter )
	{
		this.emitter = emitter;
	}

	public void visit( IContent content, Object value )
	{
		content.accept( this, value );
	}

	public void visitContent( IContent content, Object value )
	{
		emitter.startContent( content );
	}

	public void visitPage( IPageContent page, Object value )
	{
		// emitter.startPage( page );
		// emitter.endPage( page );
	}

	public void visitContainer( IContainerContent container, Object value )
	{
		emitter.startContainer( container );
		visitChildren( container, value );
		emitter.endContainer( container );
	}

	public void visitTable( ITableContent table, Object value )
	{
		emitter.startTable( table );
		visitTableBand( table.getHeader( ), value );
		visitTableBand( table.getBody( ), value );
		visitTableBand( table.getFooter( ), value );
		emitter.endTable( table );
	}

	public void visitTableBand( ITableBandContent tableBand, Object value )
	{
		if ( tableBand == null )
		{
			return;
		}

		switch ( tableBand.getType( ) )
		{
			case ITableBandContent.BAND_HEADER :
				emitter.startTableHeader( tableBand );
				visitChildren( tableBand, value );
				emitter.endTableHeader( tableBand );
				break;

			case ITableBandContent.BAND_BODY :
				emitter.startTableBody( tableBand );
				visitChildren( tableBand, value );
				emitter.endTableBody( tableBand );
				break;

			case ITableBandContent.BAND_FOOTER :
				emitter.startTableFooter( tableBand );
				visitChildren( tableBand, value );
				emitter.endTableFooter( tableBand );
				break;

			default :
				assert false;
		}
	}

	public void visitRow( IRowContent row, Object value )
	{
		emitter.startRow( row );
		visitChildren( row, value );
		emitter.endRow( row );
	}

	public void visitCell( ICellContent cell, Object value )
	{
		emitter.startCell( cell );
		visitChildren( cell, value );
		emitter.endCell( cell );
	}

	public void visitText( ITextContent text, Object value )
	{
		emitter.startText( text );
	}

	public void visitLabel( ILabelContent label, Object value )
	{
		emitter.startLabel( label );
	}
	
	public void visitAutoText( IAutoTextContent autoText, Object value )
	{
		emitter.startAutoText( autoText );
	}

	public void visitData( IDataContent data, Object value )
	{
		emitter.startData( data );
	}

	public void visitImage( IImageContent image, Object value )
	{
		emitter.startImage( image );
	}

	public void visitForeign( IForeignContent foreign, Object value )
	{
		emitter.startForeign( foreign );
	}

	protected void visitChildren( IContent content, Object value )
	{
		visitList( content.getChildren( ), value );
	}

	public void visitList( List list, Object value )
	{
		Object content;
		if ( list == null )
		{
			return;
		}

		for ( int n = 0; n < list.size( ); n++ )
		{
			content = list.get( n );
			if ( content instanceof IContent )
			{
				visit( (IContent) content, value );
			}
		}
	}

}
