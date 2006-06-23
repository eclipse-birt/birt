/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;

public class HTMLPageLM extends HTMLBlockStackingLM
{

	protected IReportContent report;

	protected long pageNumber = 0;
	protected long startOffset;
	protected long endOffset;

	protected IPageContent pageContent;

	protected IReportExecutor reportExecutor = null;

	public HTMLPageLM( HTMLReportLayoutEngine engine, IReportContent report,
			IReportExecutor executor, IContentEmitter emitter )
	{
		super( engine.getFactory( ) );
		this.report = report;
		this.reportExecutor = executor;
		this.emitter = emitter;
		this.executor = new IReportItemExecutor( ) {

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

	public boolean layout( )
	{
		if(context.getCancelFlag( ))
		{
			close( );
			isLastPage = true;
			return false;
		}
		
		start( );
		boolean hasNextPage = layoutChildren( );
		if ( isChildrenFinished( ) )
		{
			isLastPage = true;
		}
		end( );
		return hasNextPage;
	}
	
	public boolean isFinished( )
	{
		return isLastPage;
	}

	protected void pageBreakEvent( )
	{
		ILayoutPageHandler pageHandler = engine.getPageHandler( );
		if ( pageHandler != null )
		{
			pageHandler.onPage( this.pageNumber, context );
		}
	}

	protected void start( )
	{
		if ( pageContent == null )
		{
			MasterPageDesign pageDesign = getMasterPage( report );
			pageNumber = pageNumber + 1;
			context.setPageNumber( pageNumber );
			pageContent = reportExecutor.createPage( pageNumber, pageDesign );
			if ( emitter != null )
			{
				emitter.startPage( pageContent );
			}
		}
	}

	protected void end( )
	{
		if ( isLastPage )
		{
			if ( pageNumber == 1 )
			{
				context.setPageEmpty( false );
			}
			if ( context.isPageEmpty( ) )
			{
				// remove the last page number
				pageNumber--;
				context.setPageNumber( pageNumber );
			}
		}
		if ( !context.isPageEmpty( ) )
		{
			assert pageContent != null;
			if ( emitter != null )
			{
				emitter.endPage( pageContent );
			}
			pageBreakEvent( );
			pageContent = null;
			context.setPageEmpty( true );
			context.clearPageHint( );
		}
	}
}
