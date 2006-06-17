package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutor;


public class LineStackingExecutor implements IReportItemExecutor
{
	protected IReportItemExecutor executor;
	protected IReportItemExecutor current;
	protected IReportItemExecutor next;
	
	
	public LineStackingExecutor(IReportItemExecutor first, IReportItemExecutor executor)
	{
		this.next = first;
		this.executor = executor;
	}
	public void close( )
	{
		//do nothing
	}

	public IContent execute( )
	{
		return null;
	}

	public IReportItemExecutor getNextChild( )
	{
		current = next;
		if(executor!=null && executor instanceof BlockStackingExecutor)
		{
			next = ((BlockStackingExecutor)executor).nextInline( );
		}
		else
		{
			next = null;
		}
		return current;
	}

	public boolean hasNextChild( )
	{
		return next!=null;
	}
	
	
}
