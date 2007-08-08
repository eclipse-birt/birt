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
		return hasNextPage;
	}


	public boolean isFinished( )
	{
		return isLastPage;
	}


	protected void start( boolean isFirst )
	{
		MasterPageDesign pageDesign = getMasterPage( report );
		pageContent = ReportExecutorUtil.executeMasterPage( reportExecutor,
				context.getPageNumber( ), pageDesign );
		if ( emitter != null  )
		{
			context.getPageBufferManager( ).startContainer( pageContent, isFirst, emitter );
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
			context.getPageBufferManager( ).endContainer( pageContent, finished, emitter );
		}
	}
}
