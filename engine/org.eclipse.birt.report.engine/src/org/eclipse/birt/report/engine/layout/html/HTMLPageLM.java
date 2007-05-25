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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;
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
		this.pageNumber = Math.max(  context.getPageNumber( ) - 1, 0);
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

	public boolean layout( )
	{
		if ( context.getCancelFlag( ) )
		{
			close( );
			isLastPage = true;
			return false;
		}
		boolean hasNextPage = layoutChildren( );
		if ( isChildrenFinished( ) )
		{
			isLastPage = true;
		}
		if ( isLastPage && !hasStartPage && pageNumber == 0 )
		{
			start( );
		}
		end( );
		return hasNextPage;
	}

	boolean hasStartPage = false;

	protected boolean layoutChildren( )
	{
		boolean hasNext = false;
		hasStartPage = false;
		// first we need layout the remain content
		if ( childLayout != null )
		{
			// we have handle the childLayout, so just ouptut the content
			start( );
			hasStartPage = true;
			hasNext = childLayout.layout( );
			if ( childLayout.isFinished( ) )
			{
				childLayout.close( );
				childExecutor.close( );
				childLayout = null;
				childExecutor = null;
			}
			if ( hasNext )
			{
				return true;
			}
		}
		// then layout the next content
		while ( executor.hasNextChild( ) && !context.getCancelFlag( ) )
		{
			childExecutor = (IReportItemExecutor) executor.getNextChild( );
			IContent childContent = childExecutor.execute( );
			if ( childContent != null )
			{
				if ( hasStartPage == false )
				{
					hasStartPage = true;
					String masterPage = childContent.getStyle( )
							.getMasterPage( );
					if ( masterPage != null )
					{
						context.setMasterPage( masterPage );
					}
					start( );
				}
				childLayout = engine.createLayoutManager( this, childContent,
						childExecutor, emitter );
				hasNext = childLayout.layout( );
				if ( hasNext )
				{
					if ( childLayout.isFinished( ) )
					{
						childLayout.close( );
						childExecutor.close( );
						childLayout = null;
						childExecutor = null;
					}
					return true;
				}
				childLayout.close( );
				childLayout = null;
			}
			childExecutor.close( );
			childExecutor = null;
		}
		return false;
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
			pageContent = ReportExecutorUtil.executeMasterPage( reportExecutor,
					pageNumber, pageDesign );
			if ( emitter != null && pageContent!=null )
			{
				emitter.startPage( pageContent );
			}
		}
		isOutput = true;
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
			//assert pageContent != null;
			if ( emitter != null && pageContent!=null )
			{
				emitter.endPage( pageContent );
			}
			pageBreakEvent( );
			pageContent = null;
			context.setPageEmpty( true );
			context.clearPageHint( );
		}
		context.removeLayoutHint( );
	}
}
