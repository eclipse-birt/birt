package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutorBase;


public class ItemExecutorWrapper extends ReportItemExecutorBase
{
	protected IReportItemExecutor executor;
	protected IContent content;
	
	public ItemExecutorWrapper(IReportItemExecutor executor, IContent content)
	{
		this.executor = executor;
		this.content = content;
	}
	public void close( )
	{
		executor.close( );
	}

	public IContent execute( )
	{
		return content;
	}

	public IReportItemExecutor getNextChild( )
	{
		return executor.getNextChild( );
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}
	
}
