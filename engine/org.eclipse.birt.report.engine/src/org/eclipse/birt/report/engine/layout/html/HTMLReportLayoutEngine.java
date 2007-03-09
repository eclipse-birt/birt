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

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;

public class HTMLReportLayoutEngine implements IReportLayoutEngine
{

	/**
	 * context used to layout the report. each engine will have the only one
	 * context.
	 */
	protected HTMLLayoutContext context;

	/**
	 * factory used to store the layout manager. each engine will have its own
	 * factory.
	 */
	protected HTMLLayoutManagerFactory factory;

	protected ILayoutPageHandler pageHandler;
	
	protected HashMap options;
	
	protected Locale locale;

	/**
	 * executor used to create the master page
	 */
	protected IReportExecutor executor;

	public HTMLReportLayoutEngine( )
	{
		options = new HashMap();
		context = new HTMLLayoutContext( this );
		factory = new HTMLLayoutManagerFactory( this );
	}

	HTMLLayoutContext getContext( )
	{
		return context;
	}

	HTMLLayoutManagerFactory getFactory( )
	{
		return factory;
	}

	public void layout( IReportExecutor executor, IContentEmitter emitter, boolean pagination )
	{
		this.executor = executor;
		
		this.context.setAllowPageBreak( pagination );
		
		IReportContent report = executor.execute( );
		if ( emitter != null )
		{
			emitter.start( report );
		}

		context.setFinish(  false );
		HTMLPageLM pageLM = new HTMLPageLM( this, report, executor, emitter );

		boolean finished = false;
		do
		{
			pageLM.layout( );
			finished = pageLM.isFinished( );
		} while ( !finished );
		
		pageLM.close( );

		if ( emitter != null )
		{
			emitter.end( report );
		}
		context.setFinish(  true );
		if ( pageHandler != null )
		{
			pageHandler.onPage( context.getPageNumber( ), context );
		}
		executor.close( );
	}

	public void layout( IReportItemExecutor executor, IContentEmitter emitter )
	{
		IContent content = executor.execute( );
		ILayoutManager layoutManager = factory.createLayoutManager( null,
				content, executor, emitter );
		boolean hasNext = layoutManager.layout( );
		while ( hasNext )
		{
			hasNext = layoutManager.layout( );
		}
		layoutManager.close( );
	}

	public void layout( IContent content, IContentEmitter output )
	{
		IReportItemExecutor executor = new DOMReportItemExecutor( content );
		layout( executor, output );
		executor.close( );
	}

	ILayoutManager createLayoutManager( HTMLAbstractLM parent,
			IContent content, IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		return factory.createLayoutManager( parent, content, executor, emitter );
	}

	public void setPageHandler( ILayoutPageHandler handler )
	{
		this.pageHandler = handler;
	}

	public ILayoutPageHandler getPageHandler( )
	{
		return this.pageHandler;
	}
	
	public void cancel()
	{
		if(context!=null)
		{
			context.setCancelFlag( true );
		}
	}
	
	public void setOption(String name, Object value)
	{
		options.put(name, value);
	}
	
	public Object getOption(String name)
	{
		return options.get(name);
	}
	
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
}
