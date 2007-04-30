
package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class ReportExecutorV4 extends AbstractReportExecutor
{

	protected ReportItemExecutor bodyExecutor;

	public ReportExecutorV4( ExecutionContext context ) throws IOException
	{
		super( context );
		bodyExecutor = new ReportBodyExecutor( manager, null );
	}

	public void close( )
	{
		bodyExecutor.close( );
		super.close( );
	}

	public boolean hasNextChild( )
	{
		return bodyExecutor.hasNextChild( );
	}

	public IReportItemExecutor getNextChild( )
	{
		return bodyExecutor.getNextChild( );
	}
}
