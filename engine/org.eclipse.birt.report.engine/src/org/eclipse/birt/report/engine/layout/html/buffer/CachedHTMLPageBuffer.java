/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public class CachedHTMLPageBuffer extends HTMLPageBuffer implements IPageBuffer
{

	protected boolean cached = true;

	protected PageNode page = null;

	public CachedHTMLPageBuffer( HTMLLayoutContext context, boolean cached )
	{
		super( context );
		this.cached = cached;
	}

	public void startContent( IContent content, IContentEmitter emitter,
			boolean visible ) throws BirtException
	{
		if ( !cached )
		{
			super.startContent( content, emitter, visible );
			return;
		}
		LeafBufferNode leafNode = new LeafBufferNode( content, emitter,
				generator, visible );
		setup( leafNode, true );
	}

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible ) throws BirtException
	{
		if ( !cached )
		{
			super.endContainer( content, finished, emitter, visible );
			return;
		}
		int type = content.getContentType( );
		switch ( type )
		{
			case IContent.TABLE_BAND_CONTENT :
			case IContent.LIST_BAND_CONTENT :
				boolean isFinished = finished && !isRepeated;
				_endContainer( content, isFinished, emitter, visible );
				break;
			case IContent.PAGE_CONTENT :
				page = (PageNode) currentNode;
				_endContainer( content, finished, emitter, visible );
				this.finished = true;
				break;
			default :
				_endContainer( content, finished, emitter, visible );
				break;
		}

	}

	public void clearCache( ) throws BirtException
	{
		if ( currentNode != null )
		{
			currentNode.start( );
			cached = false;
		}
	}

	private void _endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		( (AbstractNode) currentNode ).setFinished( finished );
		currentNode = currentNode.getParent( );
	}

	protected void pageBreakEvent( )
	{
		context.setPageHint( generator.getPageHint( ) );
		
		long pageNumber = context.getPageNumber( );
		ILayoutPageHandler pageHandler = context.getLayoutEngine( )
				.getPageHandler( );
		if ( pageHandler != null )
		{
			pageHandler.onPage( pageNumber, context );
		}
	}
	
	public void addTableColumnHint( TableColumnHint hint )
	{
		columnHints.add( hint );

	}

	private void setup( AbstractNode node, boolean isFirst )
	{
		node.setFirst( isFirst );
		if ( currentNode != null )
		{
			node.setParent( currentNode );
			currentNode.addChild( node );
		}
	}

	public boolean isRepeated( )
	{
		return isRepeated;
	}

	public void setRepeated( boolean isRepeated )
	{
		this.isRepeated = isRepeated;
	}

	public void flush( ) throws BirtException
	{
		// current node should be page node
		if ( page != null )
		{
			context.addTableColumnHints( columnHints );
			context.generatePageRowHints( getTableKeys() );
			page.flush( );
			pageBreakEvent( );
			if ( !page.finished )
			{
				context.setPageNumber( context.getPageNumber( ) + 1 );
				context.setPageCount( context.getPageCount( ) + 1 );
			}
			generator.reset( );
			//context.removeLayoutHint( );
			context.clearPageHint( );
			currentNode = null;

		}

	}

}
