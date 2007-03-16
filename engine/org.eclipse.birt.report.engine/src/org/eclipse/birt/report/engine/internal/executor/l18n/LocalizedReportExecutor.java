
package org.eclipse.birt.report.engine.internal.executor.l18n;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class LocalizedReportExecutor implements IReportExecutor
{

	IReportExecutor executor;
	LocalizedContentVisitor l18nVisitor;
	LocalizedReportItemExecutorManager manager;

	public LocalizedReportExecutor( ExecutionContext context,
			IReportExecutor executor )
	{
		this.l18nVisitor = new LocalizedContentVisitor( context );
		this.manager = new LocalizedReportItemExecutorManager( l18nVisitor );
		this.executor = executor;
	}

	public void close( )
	{
		executor.close( );
	}

	public IReportContent execute( )
	{
		return executor.execute( );
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return manager.createExecutor( childExecutor );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		IPageContent pageContent = executor.createPage( pageNumber, pageDesign );
		if ( pageContent != null )
		{
			l18nVisitor.localize( pageContent );
		}
		return pageContent;
	}

	public void execute( ReportDesignHandle reportDesign,
			IContentEmitter emitter )
	{
		IReportContent reportContent = execute( );
		if ( emitter != null )
		{
			emitter.start( reportContent );
		}
		while ( hasNextChild( ) )
		{
			IReportItemExecutor executor = getNextChild( );
			execute( executor, emitter );
			executor.close( );
		}
		if ( emitter != null )
		{
			emitter.end( reportContent );
		}
		close( );
	}

	protected void execute( IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		IContent content = executor.execute( );
		if ( emitter != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
		}
		while ( executor.hasNextChild( ) )
		{
			IReportItemExecutor child = executor.getNextChild( );
			execute( child, emitter );
			child.close( );
		}
		if ( emitter != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
	}
}
