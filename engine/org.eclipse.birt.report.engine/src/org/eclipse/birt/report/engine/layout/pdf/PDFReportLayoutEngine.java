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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.pdf.font.FontHandler;

public class PDFReportLayoutEngine implements IReportLayoutEngine
{

	protected IReportExecutor executor;
	protected PDFLayoutEngineContext context;
	protected PDFLayoutManagerFactory factory;
	protected ILayoutPageHandler handle;

	public PDFReportLayoutEngine( )
	{
		context = new PDFLayoutEngineContext( this );
		factory = new PDFLayoutManagerFactory( context );
		context.setFactory( factory );
	}

	protected PDFLayoutManagerFactory createLayoutManagerFactory(
			PDFLayoutEngineContext context )
	{
		return new PDFLayoutManagerFactory( this.context );
	}

	protected void layoutReport( IReportContent report,
			IReportExecutor executor, IContentEmitter output )
	{
		if ( output == null )
		{
			return;
		}
		FontHandler.prepareFonts( );
		PDFPageLM pageLM = new PDFPageLM( this, context, report, output,
				executor );
		while ( pageLM.layout( ) );

	}

	public void layout( IReportExecutor executor, IContentEmitter output, boolean pagination )
	{
		context.setAllowPageBreak(pagination);
		this.executor = executor;
		IReportContent report = executor.execute( );
		if ( output != null )
		{
			output.start( report );
		}
		layoutReport( report, executor, output );
		if ( output != null )
		{
			output.end( report );
		}
		executor.close( );
	}

	public void layout( IReportItemExecutor executor, IContentEmitter emitter )
	{
		IContent content = executor.execute( );
		PDFAbstractLM layoutManager = factory.createLayoutManager( null,
				content, emitter, executor );
		layoutManager.layout( );
		layoutManager.close( );
	}

	public void layout( IContent content, IContentEmitter output )
	{
		IReportItemExecutor executor = new DOMReportItemExecutor( content );
		layout( executor, output );
		executor.close( );
	}

	public void setPageHandler( ILayoutPageHandler handle )
	{
		this.handle = handle;
	}

	public ILayoutPageHandler getPageHandler( )
	{
		return this.handle;
	}

}
