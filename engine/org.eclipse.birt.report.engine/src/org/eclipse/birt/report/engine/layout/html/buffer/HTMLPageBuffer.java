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

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;


public class HTMLPageBuffer implements IPageBuffer
{
	protected INode currentNode;
	protected PageHintGenerator generator;
	
	protected HTMLLayoutContext context;
	
	protected boolean isRepeated = false ;
	
	public HTMLPageBuffer(HTMLLayoutContext context)
	{
		this.context = context;
		this.generator = new PageHintGenerator();
	}
	
	public void startContainer(IContent content, boolean isFirst, IContentEmitter emitter)
	{
		int type = content.getContentType( );
		switch(type)
		{
			case IContent.TABLE_BAND_CONTENT:
			case IContent.LIST_BAND_CONTENT:
				boolean first = isFirst && !isRepeated;
				BlockStackingNode bandNode = new BlockStackingNode(content, emitter, generator);
				setup(bandNode, first);
				currentNode = bandNode;
				break;
			case IContent.ROW_CONTENT:
				InlineStackingNode inlineNode = new InlineStackingNode(content, emitter, generator);
				setup(inlineNode, isFirst);
				currentNode = inlineNode;
				break;
			case IContent.CELL_CONTENT:
				BlockStackingNode cellNode = new BlockStackingNode(content, emitter,generator);
				setup(cellNode, isFirst);
				if(currentNode.isStarted( ))
				{
					cellNode.start(  );
				}
				currentNode = cellNode;
				break;
			case IContent.PAGE_CONTENT:
				PageNode pageNode = new PageNode(content, emitter,generator);
				setup(pageNode, isFirst);
				currentNode = pageNode;
				break;
			default:
				BlockStackingNode blockNode = new BlockStackingNode(content, emitter, generator);
				setup(blockNode, isFirst);
				currentNode = blockNode;
				break;
		}
	}
	
	public void endContainer(IContent content, boolean finished, IContentEmitter emitter)
	{
		int type = content.getContentType( );
		switch(type)
		{
			case IContent.TABLE_BAND_CONTENT:
			case IContent.LIST_BAND_CONTENT:
				boolean isFinished = finished && !isRepeated;
				_endContainer(content, isFinished, emitter);
				break;
			case IContent.PAGE_CONTENT:
				endPage(content, finished, emitter);
				break;
			
			case IContent.CELL_CONTENT:
				endCell(content, finished, emitter);
				break;
			default:
				_endContainer(content, finished, emitter);
				break;
		}
		
	}
	
	private void _endContainer(IContent content, boolean finished, IContentEmitter emitter)
	{
		((AbstractNode)currentNode).setFinished( finished );
		if(currentNode.isStarted( ))
		{
			currentNode.end(  );
		}
		else
		{
			if(finished)
			{
				currentNode.flush( );
			}
		}
		
		currentNode = currentNode.getParent( );
		if(currentNode!=null)
		{
			currentNode.removeChildren( );
		}
	}
	
	public void startContent(IContent content, IContentEmitter emitter)
	{
		currentNode.start(  );
		ContentEmitterUtil.startContent( content, emitter );
		generator.start(content, true );
		generator.end( content, true );
	}
	
	
	private void startPage(IContent content, boolean isFirst, IContentEmitter emitter)
	{

	}
	
	public void endPage(IContent content, boolean finished, IContentEmitter emitter)
	{
		((AbstractNode)currentNode).setFinished( finished );
		if(currentNode.isStarted( ))
		{
			currentNode.end(  );
			pageBreakEvent( );
			if(!finished)
			{
				context.setPageNumber( context.getPageNumber( ) + 1 );
			}
		}
		else
		{
			if(finished && context.getPageNumber( )==1)
			{
				currentNode.end(  );
				pageBreakEvent( );
			}
		}
		
		generator.reset( );
		context.removeLayoutHint( );
		context.clearPageHint( );
		currentNode = null;
	}
	
	protected void pageBreakEvent( )
	{
		context.setPageHint( generator.getPageHint( ) );
		long pageNumber = context.getPageNumber( );
		ILayoutPageHandler pageHandler = context.getLayoutEngine( ).getPageHandler( );
		if ( pageHandler != null )
		{
			pageHandler.onPage( pageNumber, context );
		}
		
	}
	
	private void setup(AbstractNode node, boolean isFirst)
	{
		node.setFirst( isFirst );
		if(currentNode!=null)
		{
			node.setParent( currentNode );
			currentNode.addChild( node );
		}
	}
	
	public void endCell(IContent content, boolean finished, IContentEmitter emitter)
	{
		((AbstractNode)currentNode).setFinished( finished );
		if(currentNode.isStarted( ))
		{
			currentNode.end(  );
		}
		currentNode = currentNode.getParent( );
	}

	public boolean isRepeated( )
	{
		return isRepeated;
	}

	public void setRepeated( boolean isRepeated )
	{
		this.isRepeated = isRepeated;
	}

}

