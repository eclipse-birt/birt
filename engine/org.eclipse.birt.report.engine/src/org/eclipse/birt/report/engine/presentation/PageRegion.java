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

import java.util.ArrayList;

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
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class PageRegion extends WrappedEmitter
{

	private StartEmitterVisitor startEmitterVisitor;
	private EndEmitterVisitor endEmitterVisitor;

	// private DOMBuildingEmitter domBuilderEmitter;

	public PageRegion( Page page )
	{
		super( page.getEmitter( ) );

		startEmitterVisitor = new StartEmitterVisitor( emitter );
		endEmitterVisitor = new EndEmitterVisitor( emitter );
	}

	public void open( IContent content )
	{
		if ( content != null )
		{
			ArrayList contents = getAncestors( content );
			int size = contents.size( ) - 1;
			for ( int i = size; i >= 0; i-- )
			{
				openContent( (IContent) contents.get( i ) );
			}
		}
	}

	public void close( IContent content )
	{
		if ( content != null )
		{
			ArrayList contents = getAncestors( content );
			int size = contents.size( );
			for ( int i = 0; i < size; i++ )
			{
				closeContent( (IContent) contents.get( i ) );
			}
		}
	}

	private ArrayList getAncestors( IContent content )
	{
		ArrayList list = new ArrayList( );
		//Top level content is a virtual element, not a real ancestor.
		while ( content.getParent( ) != null )
		{
			list.add( content );
			content = (IContent)content.getParent( ) ;
		}
		return list;
	}

	protected void openContent( IContent content )
	{
		startEmitterVisitor.visit( content, null );
	}

	protected void closeContent( IContent content )
	{
		endEmitterVisitor.visit( content, null );
	}

	private class StartEmitterVisitor extends ContentVisitorAdapter
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

	private class EndEmitterVisitor extends ContentVisitorAdapter
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

	private class DOMBuildingEmitter extends ContentEmitterAdapter
	{

		private IContent parent;

		public DOMBuildingEmitter( )
		{
		}

		public void startContent( IContent content )
		{
			if ( parent != null )
			{
				parent.getChildren( ).add( content );
				parent = content;
			}
			openContent( content );
		}

		public void endContent( IContent content )
		{
			if ( parent != null )
			{
				parent = (IContent) parent.getParent( );
			}
			closeContent( content );
		}
	}
}
