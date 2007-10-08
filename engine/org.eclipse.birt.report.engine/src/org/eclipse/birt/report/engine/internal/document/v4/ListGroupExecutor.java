
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IContent;

public class ListGroupExecutor extends GroupExecutor
{

	protected ListGroupExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.LISTGROUPITEM );
	}
	
	protected IContent doCreateContent( )
	{
		return report.createListGroupContent( );
	}
}
