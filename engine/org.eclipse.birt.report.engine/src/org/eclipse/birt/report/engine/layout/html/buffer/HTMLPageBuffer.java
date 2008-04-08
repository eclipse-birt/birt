/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public class HTMLPageBuffer implements IPageBuffer
{

	protected IContainerNode currentNode;
	protected PageHintGenerator generator;

	protected HTMLLayoutContext context;

	protected boolean isRepeated = false;
	protected boolean finished = false;

	protected ArrayList columnHints = new ArrayList( );

	public HTMLPageBuffer( HTMLLayoutContext context )
	{
		this.context = context;
		this.generator = new PageHintGenerator( );
	}

	public void startContainer( IContent content, boolean isFirst,
			IContentEmitter emitter, boolean visible )
	{
		int type = content.getContentType( );
		switch ( type )
		{
			case IContent.TABLE_BAND_CONTENT :
			case IContent.LIST_BAND_CONTENT :
				boolean first = isFirst && !isRepeated;
				ContainerBufferNode bandNode = new ContainerBufferNode(
						content, emitter, generator, visible );
				setup( bandNode, first );
				currentNode = bandNode;
				break;
			case IContent.CELL_CONTENT :
				ContainerBufferNode cellNode = new ContainerBufferNode(
						content, emitter, generator, visible );
				setup( cellNode, isFirst );
				if ( currentNode.isStarted( ) )
				{
					cellNode.start( );
				}
				currentNode = cellNode;
				break;
			case IContent.PAGE_CONTENT :
				PageNode pageNode = new PageNode( content, emitter, generator,
						visible );
				setup( pageNode, isFirst );
				currentNode = pageNode;
				break;
			default :
				ContainerBufferNode node = new ContainerBufferNode( content,
						emitter, generator, visible );
				setup( node, isFirst );
				currentNode = node;
				break;
		}
	}

	protected boolean isParentStarted( )
	{
		INode parentNode = currentNode.getParent( );
		if ( parentNode != null )
		{
			return parentNode.isStarted( );
		}
		return false;
	}

	public void startContent( IContent content, IContentEmitter emitter,
			boolean visible )
	{
		if ( isRepeated || ( !visible && !currentNode.isStarted( ) ) )
		{
			LeafBufferNode leafNode = new LeafBufferNode( content, emitter,
					generator, visible );
			setup( leafNode, true );
		}
		else
		{
			LeafBufferNode leafNode = new LeafBufferNode( content, emitter,
					generator, visible );
			setup( leafNode, true );
			currentNode.start( );
			if ( visible )
			{
				ContentEmitterUtil.startContent( content, emitter );
			}
			generator.start( content, true );
			generator.end( content, true );
			currentNode.removeChildren( );
		}
	}

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		int type = content.getContentType( );
		switch ( type )
		{
			case IContent.TABLE_BAND_CONTENT :
			case IContent.LIST_BAND_CONTENT :
				boolean isFinished = finished && !isRepeated;
				_endContainer( content, isFinished, emitter, visible );
				break;
			case IContent.PAGE_CONTENT :
				endPage( content, finished, emitter );
				break;

			case IContent.CELL_CONTENT :
				endCell( content, finished, emitter, visible );
				break;
			default :
				_endContainer( content, finished, emitter, visible );
				break;
		}

	}

	private void _endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		( (AbstractNode) currentNode ).setFinished( finished );
		if ( currentNode.isStarted( ) )
		{
			currentNode.end( );
		}
		else
		{
			if ( finished && !isRepeated )
			{
				if ( visible )
				{
					currentNode.flush( );
				}
				else if ( isParentStarted( ) )
				{
					currentNode.flush( );
				}
			}
		}

		currentNode = currentNode.getParent( );
		if ( currentNode != null && finished && !isRepeated )
		{
			if ( visible )
			{
				currentNode.removeChildren( );
			}
			else if ( isParentStarted( ) )
			{
				currentNode.removeChildren( );
			}
		}
	}

	protected void endCell( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		AbstractNode current = (AbstractNode) currentNode ;
		//Fix 213900
		if(!current.isFirst)
		{
			current.setFinished( false );
		}
		else
		{
			current.setFinished( finished );
		}
		if ( currentNode.isStarted( ) )
		{
			currentNode.end( );
		}
		currentNode = currentNode.getParent( );
	}

	public void endPage( IContent content, boolean finished,
			IContentEmitter emitter )
	{
		( (AbstractNode) currentNode ).setFinished( finished );
		if ( currentNode.isStarted( ) )
		{
			currentNode.end( );
			pageBreakEvent( );
			if ( !finished )
			{
				context.setPageNumber( context.getPageNumber( ) + 1 );
				context.setPageCount( context.getPageCount( ) + 1 );
			}
		}
		else
		{
			if ( finished )
			{
				if ( context.getPageNumber( ) == 1 )
				{
					currentNode.flush( );
					pageBreakEvent( );
				}
				else
				{
					context.setPageNumber( context.getPageNumber( ) - 1 );
					context.setPageCount( context.getPageCount( ) - 1 );
				}
			}
		}
		this.finished = true;
		generator.reset( );
		context.removeLayoutHint( );
		context.clearPageHint( );
		currentNode = null;
	}

	protected void pageBreakEvent( )
	{
		context.setPageHint( generator.getPageHint( ) );
		context.addTableColumnHints( this.columnHints );
		long pageNumber = context.getPageNumber( );
		ILayoutPageHandler pageHandler = context.getLayoutEngine( )
				.getPageHandler( );
		if ( pageHandler != null )
		{
			pageHandler.onPage( pageNumber, context );
		}

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

	public void flush( )
	{

	}

	public boolean finished( )
	{
		return finished;
	}

	public void closePage( IContent[] contentList, IContentEmitter emitter )
	{
		int length = contentList.length;
		if ( length > 0 )
		{
			for ( int i = 0; i < length; i++ )
			{
				endContainer( contentList[i], false, emitter, true );
			}
		}
		finished = true;
	}

	public void openPage( IContent[] contentList, IContentEmitter emitter )
	{
		int length = contentList.length;
		if ( length > 0 )
		{
			for ( int i = length - 1; i >= 0; i-- )
			{
				startContainer( contentList[i], false, emitter, true );
			}
		}
	}

	public IContent[] getContentStack( )
	{
		ArrayList<IContent> contentList = new ArrayList<IContent>( );
		if ( currentNode != null )
		{
			contentList.add( currentNode.getContent( ) );
			INode parent = currentNode.getParent( );
			while ( parent != null )
			{
				contentList.add( parent.getContent( ) );
				parent = parent.getParent( );
			}
		}
		IContent[] list = new IContent[contentList.size( )];
		contentList.toArray( list );
		return list;
	}

	public void addTableColumnHint( TableColumnHint hint )
	{
		columnHints.add( hint );

	}

}
