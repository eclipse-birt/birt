
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;

public class ListGroupExecutor extends GroupExecutor
{

	protected ListGroupExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	public void close( )
	{
		IListGroupContent groupContent = (IListGroupContent) getContent( );

		if ( emitter != null )
		{
			emitter.endListGroup( groupContent );
		}
		finishGroupTOCEntry( );
	}

	public IContent execute( )
	{
		ListGroupDesign groupDesign = (ListGroupDesign) getDesign( );

		IListGroupContent groupContent = report.createListGroupContent( );
		setContent( groupContent );

		restoreResultSet( );
		initializeContent( groupDesign, groupContent );
		handlePageBreakOfGroup( );

		startGroupTOCEntry( );
		if ( emitter != null )
		{
			emitter.startListGroup( groupContent );
		}

		// prepare to execute the children
		prepareToExecuteChildren( );

		return groupContent;
	}
}
