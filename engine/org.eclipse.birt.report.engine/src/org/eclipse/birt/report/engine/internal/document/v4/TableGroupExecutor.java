
package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IContent;

public class TableGroupExecutor extends GroupExecutor
{

	protected TableGroupExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.TABLEGROUPITEM );
	}

	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		ReportItemExecutor executor = super.doCreateExecutor( offset );
		if ( executor instanceof TableBandExecutor )
		{
			TableBandExecutor bandExecutor = (TableBandExecutor) executor;
			bandExecutor
					.setTableExecutor( (TableItemExecutor) getListingExecutor( ) );
		}
		return executor;
	}
	
	protected IContent doCreateContent( )
	{
		return report.createTableGroupContent( );
	}
}
