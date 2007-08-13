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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutorUtil;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;


public class DummyPageBuffer implements IPageBuffer
{

	protected HTMLLayoutContext context;
	protected IReportExecutor executor;
	protected boolean isFirstContent = false;
	protected IContent pageContent = null;
	protected IContentEmitter pageEmitter = null;

	public DummyPageBuffer( HTMLLayoutContext context, IReportExecutor executor )
	{
		this.context = context;
		this.executor = executor;
	}

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter )
	{
		if ( emitter != null )
		{
			if(content.getContentType( ) == IContent.PAGE_CONTENT )
			{
				ContentEmitterUtil.endContent( pageContent, emitter );
			}
			else
			{
				ContentEmitterUtil.endContent( content, emitter );
			}
		}

	}

	public void startContainer( IContent content, boolean isFirst,
			IContentEmitter emitter )
	{
		if ( content.getContentType( ) == IContent.PAGE_CONTENT )
		{
			isFirstContent = true;
			pageContent = content;
			pageEmitter = emitter;
		}
		else
		{
			if(	isFirstContent)
			{
				startPageContent(content);
				isFirstContent = false;
			}
			if ( emitter != null )
			{
				ContentEmitterUtil.startContent( content, emitter );
			}
		}
	}

	public void startContent( IContent content, IContentEmitter emitter )
	{
		if(isFirstContent)
		{
			startPageContent(content);
			isFirstContent = false;
		}
		if ( emitter != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
			ContentEmitterUtil.endContent( content, emitter );
		}

	}

	public boolean isRepeated( )
	{
		return false;
	}

	public void setRepeated( boolean isRepeated )
	{

	}

	protected void startPageContent( IContent firstContent )
	{
		String masterPage = null;
		IStyle style = firstContent.getStyle( );
		if(style!=null)
		{
			masterPage = style.getMasterPage( );
		}
		if ( pageContent == null || pageEmitter == null )
		{
			return;
		}
		if ( masterPage == null || "".equals( masterPage ) )
		{
			ContentEmitterUtil.startContent( pageContent, pageEmitter );
		}
		else
		{
			IReportContent report = pageContent.getReportContent( );
			MasterPageDesign defaultMasterPage = LayoutUtil
					.getDefaultMasterPage( report );
			if ( defaultMasterPage.getName( ).equals( masterPage ) )
			{
				ContentEmitterUtil.startContent( pageContent, pageEmitter );
			}
			else
			{
				pageContent = ReportExecutorUtil.executeMasterPage( executor,
						context.getPageNumber( ), LayoutUtil.getMasterPage( report,
								masterPage ) );
				if(pageContent!=null)
				{
					ContentEmitterUtil.startContent( pageContent, pageEmitter );
				}
			}
		}
	}

}
