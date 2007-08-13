
package org.eclipse.birt.report.engine.internal.executor.dom;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;

public class DOMReportItemExecutor extends ReportItemExecutorBase
{

	IContent content;
	DOMReportItemExecutorManager manager;

	DOMReportItemExecutor( DOMReportItemExecutorManager manager )
	{
		this.manager = manager;
	}
	
	public DOMReportItemExecutor(IContent content)
	{
		this.content = content;
		this.manager = new DOMReportItemExecutorManager();
	}

	void setContent( IContent content )
	{
		this.content = content;
	}

	public void close( )
	{
		manager.releaseExecutor( this );
	}

	public IContent execute( )
	{
		if ( null == content )
		{
			return null;
		}
		childIterator = content.getChildren( ).iterator( );
		return content;
	}

	Iterator childIterator;

	public IReportItemExecutor getNextChild( )
	{
		if ( null != childIterator && childIterator.hasNext( ) )
		{
			IContent child = (IContent) childIterator.next( );
			return manager.createExecutor( child );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		if ( null == childIterator )
		{
			return false;
		}
		return childIterator.hasNext( );
	}

}
