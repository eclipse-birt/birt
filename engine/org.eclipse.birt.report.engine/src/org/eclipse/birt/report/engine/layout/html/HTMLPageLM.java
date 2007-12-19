/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;

public class HTMLPageLM extends HTMLBlockStackingLM
{

	protected IReportContent report;

	protected IPageContent pageContent;

	protected IReportExecutor reportExecutor = null;

	public HTMLPageLM( HTMLReportLayoutEngine engine, IReportContent report,
			IReportExecutor executor, IContentEmitter emitter )
	{
		super( engine.getFactory( ) );
		this.report = report;
		this.reportExecutor = executor;
		this.emitter = emitter;
		this.executor = new ReportItemExecutorBase( ) {

			public void close( )
			{
			}

			public IContent execute( )
			{
				return pageContent;
			}

			public IReportItemExecutor getNextChild( )
			{
				return reportExecutor.getNextChild( );
			}

			public boolean hasNextChild( )
			{
				return reportExecutor.hasNextChild( );
			}
		};
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_PAGE;
	}

	boolean isLastPage = false;
	boolean isFirstPage = true;

	public boolean layout( )
	{
		if ( context.getCancelFlag( ) )
		{
			close( );
			isLastPage = true;
			return false;
		}
		start(isFirstPage);
		boolean hasNextPage = layoutNodes( );
		if ( isChildrenFinished( ) )
		{
			isLastPage = true;
		}
		if(hasNextPage && !isLastPage)
		{
			context.addLayoutHint( pageContent, false );
		}
		isFirstPage = false;
		end(isLastPage );
		context.initilizePage( );
		return hasNextPage;
	}


	public boolean isFinished( )
	{
		return isLastPage;
	}

	protected void layoutPageContent(IPageContent pageContent)
	{
		IContent header = pageContent.getPageHeader( );
		if(header!=null)
		{
			pageContent.setPageHeader( layoutContent(header) );
		}
		IContent footer = pageContent.getPageFooter( );
		if(footer!=null)
		{
			pageContent.setPageFooter( layoutContent(footer) );
		}
		
	}
	
	protected IContent layoutContent(IContent content)
	{
		if(content==null)
		{
			return null;
		}
		ContentDOMEmitter domEmitter = new ContentDOMEmitter(content, emitter);		
		boolean pageBreak = context.allowPageBreak( );
		IPageBuffer pageBuffer = context.getPageBufferManager( );
		context.setPageBufferManager( new PageContentBuffer() );
		context.setAllowPageBreak( false );
		engine.layout(this, content, domEmitter );
		context.setAllowPageBreak( pageBreak );
		context.setPageBufferManager( pageBuffer );
		domEmitter.refreshChildren( );
		return content;
	}
	
	
	protected void start( boolean isFirst )
	{
		MasterPageDesign pageDesign = getMasterPage( report );
		pageContent = ReportExecutorUtil.executeMasterPage( reportExecutor,
				context.getPageNumber( ), pageDesign );
		if ( pageContent != null && context.needLayoutPageContent( ) )
		{
			layoutPageContent( pageContent );
		}
		if ( emitter != null )
		{
			context.getPageBufferManager( ).startContainer( pageContent,
					isFirst, emitter, true );
		}
	}
	
	protected IContent getContent()
	{
		return pageContent;
	}

	protected void end( boolean finished )
	{
		if ( emitter != null  )
		{
			context.getPageBufferManager( ).endContainer( pageContent, finished, emitter, true );
		}
	}
	
	public class ContentDOMEmitter extends ContentEmitterAdapter
	{

		protected ArrayList nodes = new ArrayList();
		protected BufferNode current;
		protected IContentEmitter emitter;
		
		public ContentDOMEmitter( IContent root, IContentEmitter emitter )
		{
			this.emitter = emitter;
			current = new BufferNode(root, null);
			nodes.add( current );
		}
		
		public String getOutputFormat( )
		{
			return emitter.getOutputFormat( );
		}

		
		public void refreshChildren()
		{
			for(int i=0; i<nodes.size( ); i++)
			{
				BufferNode node = (BufferNode)nodes.get( i );
				node.content.getChildren( ).clear( );
				node.content.getChildren( ).addAll( node.children );
				//FIMXE need set parent?
			}
		}

		public void startContent( IContent content )
		{
			if ( current != null )
			{
				if(content!=null)
				{
					current.children.add( content );
					current = new BufferNode(content, current);
					nodes.add( current );
				}
				else
				{
					current = null;
				}
			}
				
		}

		public void endContent( IContent content )
		{
			if(current!=null)
			{
				if(content!=null)
				{
					current = current.parent;
				}
			}
		}
	}
	
	class BufferNode
	{
		IContent content;
		ArrayList children = new ArrayList();
		BufferNode parent;
		public BufferNode(IContent content, BufferNode parent)
		{
			this.content = content;
			this.parent = parent;
		}
		
		public void addChild(IContent child)
		{
			children.add( child );
		}
	}
	
	public class PageContentBuffer implements IPageBuffer
	{

		public boolean isRepeated( )
		{
			return false;
		}

		public void setRepeated( boolean isRepeated )
		{
		}


		public void endContainer( IContent content, boolean finished,
				IContentEmitter emitter, boolean visible )
		{
			if(content!=null && visible)
			{
				ContentEmitterUtil.endContent( content, emitter );
			}
		}

		public void startContainer( IContent content, boolean isFirst,
				IContentEmitter emitter, boolean visible )
		{
			if(content!=null && visible)
			{
				ContentEmitterUtil.startContent( content, emitter );
			}
			
		}

		public void startContent( IContent content, IContentEmitter emitter,
				boolean visible )
		{
			if(content!=null && visible)
			{
				ContentEmitterUtil.startContent( content, emitter );
				ContentEmitterUtil.endContent( content, emitter );
			}
			
		}
	}

}
