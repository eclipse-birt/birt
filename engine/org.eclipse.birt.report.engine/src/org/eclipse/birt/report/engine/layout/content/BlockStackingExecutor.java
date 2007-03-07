package org.eclipse.birt.report.engine.layout.content;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;


public class BlockStackingExecutor extends ReportItemExecutorBase
{
	protected IContent content;
	protected IReportItemExecutor executor;
	protected IReportItemExecutor childExecutor;
	protected IContent childContent;
	protected boolean needUpdate = true;
	protected boolean hasNext = false;
	
	public BlockStackingExecutor(IContent content, IReportItemExecutor executor)
	{
		this.content = content;
		this.executor = executor;
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
		IReportItemExecutor ret = null;
		if(childContent!=null)
		{
			ret = new ItemExecutorWrapper(childExecutor, childContent);
			childContent = null;
			childExecutor = null;
		}
		else
		{
			IReportItemExecutor childExecutor = executor.getNextChild( );
			if(childExecutor!=null)
			{
				IContent childContent = childExecutor.execute( );
				if(childContent!=null)
				{
					if(PropertyUtil.isInlineElement(childContent))
					{
						ret = new LineStackingExecutor(
								new ItemExecutorWrapper(childExecutor, childContent), this);
					}
					else
					{
						ret = new ItemExecutorWrapper(childExecutor, childContent);
					}
				}
			}
		}
		needUpdate = true;
		return ret;
	}

	public boolean hasNextChild( )
	{
		if(needUpdate)
		{
			if(childContent!=null)
			{
				hasNext = true;
			}
			else
			{
				hasNext = executor.hasNextChild( );
			}
			needUpdate = false;
		}
		return hasNext;
	}
	
	public IReportItemExecutor nextInline()
	{
		if(executor.hasNextChild( ))
		{
			IReportItemExecutor nextExecutor = (IReportItemExecutor)executor.getNextChild( );
			IContent nextContent = nextExecutor.execute( );
			
			if(PropertyUtil.isInlineElement(nextContent))
			{
				return new ItemExecutorWrapper(nextExecutor, nextContent);
			}
			else
			{
				this.childContent = nextContent;
				this.childExecutor = nextExecutor;
			}
		}
		return null;
	}
}
