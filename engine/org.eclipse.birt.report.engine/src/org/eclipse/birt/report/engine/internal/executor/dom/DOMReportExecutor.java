
package org.eclipse.birt.report.engine.internal.executor.dom;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class DOMReportExecutor implements IReportExecutor
{

	DOMReportItemExecutorManager manager;
	IReportContent reportContent;

	public DOMReportExecutor( IReportContent reportContent )
	{
		manager = new DOMReportItemExecutorManager( );
	}

	public IReportContent execute( )
	{
		childIterator = new ArrayList( ).iterator( );
		return reportContent;
	}

	public void close( )
	{
	}

	Iterator childIterator;

	public IReportItemExecutor getNextChild( )
	{
		if ( childIterator.hasNext( ) )
		{
			IContent child = (IContent) childIterator.next( );
			return manager.createExecutor( child );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return childIterator.hasNext( );
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		return null;
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
		}
		if ( emitter != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
	}
}
