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

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class PageRegion extends WrappedEmitter

{

	Page page;

	PageRegion parent;

	PageFlow pageFlow;

	public PageRegion( Page page, PageFlow pageFlow )
	{
		super( page.getEmitter( ) );

		this.page = page;
		this.parent = null;
		this.pageFlow = pageFlow;
	}

	PageRegion createRegion( PageFlow pageFlow )
	{
		PageRegion region = new PageRegion( page, pageFlow );
		region.parent = this;
		return region;
	}

	PageFlow getPageFlow( )
	{
		return this.pageFlow;
	}

	Page getPage( )
	{
		return page;
	}

	PageRegion getParent( )
	{
		return parent;
	}

	boolean closed;
	boolean opened;

	public void open( )
	{
		if ( opened == false )
		{
			IContent root = null;
			if ( parent != null )
			{
				root = parent.getContent( );
			}
			IContent content = getContent( );
			openContent( content, root );
			// output the content
			opened = true;
		}
	}

	IContent getContent( )
	{
		if ( pageFlow != null )
		{
			return pageFlow.getContent( );
		}
		return null;
	}

	public void close( )
	{
		if ( closed == false )
		{
			// output the content
			IContent root = null;
			if ( parent != null )
			{
				root = parent.getContent( );
			}
			IContent content = getContent( );

			closeContent( content, root );

			closed = true;
		}
	}

	protected void openContent( IContent content, IContent root )
	{
		LinkedList contents = new LinkedList( );
		while ( content != root && content != null )
		{
			contents.addFirst( content );
			content = (IContent) content.getParent( );
		}

		Iterator iter = contents.iterator( );
		while ( iter.hasNext( ) )
		{
			content = (IContent) iter.next( );
			new StartEmitterVisitor( emitter ).visit( content, null );
		}
	}

	protected void closeContent( IContent content, IContent root )
	{
		while ( content != null && content != root )
		{
			new EndEmitterVisitor( emitter ).visit( content, null );
			content = (IContent) content.getParent( );
		}
	}

	class StartEmitterVisitor extends ContentVisitorAdapter
	{

		IContentEmitter emitter;

		public StartEmitterVisitor( IContentEmitter emitter )
		{
			this.emitter = emitter;
		}

		public void visitCell( ICellContent cell, Object value )
		{
			emitter.startCell( cell );
		}

		public void visitContainer( IContainerContent container, Object value )
		{
			emitter.startContainer( container );
		}

		public void visitContent( IContent content, Object value )
		{
			emitter.startContent( content );
		}

		public void visitForeign( IForeignContent content, Object value )
		{
			emitter.startForeign( content );
		}

		public void visitImage( IImageContent image, Object value )
		{
			emitter.startImage( image );
		}

		public void visitPage( IPageContent page, Object value )
		{
			emitter.startPage( page );
		}

		public void visitRow( IRowContent row, Object value )
		{
			emitter.startRow( row );
		}

		public void visitTable( ITableContent table, Object value )
		{
			emitter.startTable( table );
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
				case ITableBandContent.BAND_BODY :
				default :
					emitter.startTableBody( tableBand );
					break;
			}
		}

		public void visitText( ITextContent text, Object value )
		{
			emitter.startText( text );
		}

	};

	class EndEmitterVisitor extends ContentVisitorAdapter
	{

		IContentEmitter emitter;

		public EndEmitterVisitor( IContentEmitter emitter )
		{
			this.emitter = emitter;
		}

		public void visitCell( ICellContent cell, Object value )
		{
			emitter.endCell( cell );
		}

		public void visitContainer( IContainerContent container, Object value )
		{
			emitter.endContainer( container );
		}

		public void visitPage( IPageContent page, Object value )
		{
			emitter.endPage( page );
		}

		public void visitRow( IRowContent row, Object value )
		{
			emitter.endRow( row );
		}

		public void visitTable( ITableContent table, Object value )
		{
			emitter.endTable( table );
		}

		public void visitTableBand( ITableBandContent tableBand, Object value )
		{
			switch ( tableBand.getType( ) )
			{
				case ITableBandContent.BAND_HEADER :
					emitter.endTableHeader( tableBand );
					break;
				case ITableBandContent.BAND_FOOTER :
					emitter.endTableFooter( tableBand );
					break;
				case ITableBandContent.BAND_BODY :
				default :
					emitter.endTableBody( tableBand );
					break;
			}
		}

	}
}
