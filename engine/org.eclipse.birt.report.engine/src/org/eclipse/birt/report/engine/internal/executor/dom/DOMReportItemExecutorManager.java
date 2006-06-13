
package org.eclipse.birt.report.engine.internal.executor.dom;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.IContent;

class DOMReportItemExecutorManager
{

	LinkedList freeList = new LinkedList();

	DOMReportItemExecutor createExecutor( IContent content )
	{
		DOMReportItemExecutor executor = null;
		if ( !freeList.isEmpty( ) )
		{
			executor = (DOMReportItemExecutor) freeList.removeFirst( );
		}
		else
		{
			executor = new DOMReportItemExecutor( this );
		}
		executor.setContent( content );
		return executor;
	}

	void releaseExecutor( DOMReportItemExecutor executor )
	{
		freeList.addLast( executor );
	}

}
