
package org.eclipse.birt.report.engine.internal.executor.l18n;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;

class LocalizedReportItemExecutor extends ReportItemExecutorBase
{

	LocalizedContentVisitor l18nVisitor;

	LocalizedReportItemExecutorManager manager;
	IReportItemExecutor executor;

	LocalizedReportItemExecutor( LocalizedReportItemExecutorManager manager )
	{
		this.manager = manager;
		this.l18nVisitor = manager.l18nVisitor;
	}

	void setExecutor(IReportItemExecutor executor)
	{
		this.executor = executor;
	}
	
	public void close( )
	{
		executor.close( );
		manager.releaseExecutor( this );
	}

	public IContent execute( )
	{
		IContent content = executor.execute( );
		if (content != null)
		{
			content = l18nVisitor.localize( content );
		}
		return content;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return manager.createExecutor( childExecutor );
		}
		return childExecutor;
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}
}
