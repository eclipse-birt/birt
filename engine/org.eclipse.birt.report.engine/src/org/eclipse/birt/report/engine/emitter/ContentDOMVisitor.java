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

package org.eclipse.birt.report.engine.emitter;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * visit the content and output the content to emitter
 * 
 * @version $Revision:$ $Date:$
 */
public class ContentDOMVisitor extends ContentVisitorAdapter
{

	protected IContentEmitter emitter;

	public ContentDOMVisitor( )
	{
	}

	public void emit( IContent content, IContentEmitter emitter )
	{
		this.emitter = emitter;
		visit( content, null );
	}

	public void visitPage( IPageContent page, Object value )
	{
		emitter.startPage( page );
		visitChildren( page.getPageBody( ), value );
		emitter.endPage( page );
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
		visitChildren( table, value );
		emitter.endTable( table );
	}

	public void visitTableBand( ITableBandContent tableBand, Object value )
	{
		switch ( tableBand.getType( ) )
		{
			case ITableBandContent.BAND_HEADER :
				emitter.startTableHeader( tableBand );
				break;
			case ITableBandContent.BAND_FOOTER :
				emitter.startTableFooter( tableBand );
				break;
			default :
				emitter.startTableBody( tableBand );
		}

		visitChildren( tableBand, value );
		switch ( tableBand.getType( ) )
		{
			case ITableBandContent.BAND_HEADER :
				emitter.endTableHeader( tableBand );
				break;
			case ITableBandContent.BAND_FOOTER :
				emitter.endTableFooter( tableBand );
				break;
			default :
				emitter.endTableBody( tableBand );
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

	public void visitData( IDataContent data, Object value )
	{
		emitter.startData( data );
	}

	public void visitImage( IImageContent image, Object value )
	{
		emitter.startImage( image );
	}

	public void visitForeign( IForeignContent content, Object value )
	{
		emitter.startForeign( content );
	}

	protected void visitChildren( IContent container, Object value )
	{
		Iterator iter = container.getChildren( ).iterator( );
		while ( iter.hasNext( ) )
		{
			IContent content = (IContent) iter.next( );
			visit( content, value );
		}

	}

}
