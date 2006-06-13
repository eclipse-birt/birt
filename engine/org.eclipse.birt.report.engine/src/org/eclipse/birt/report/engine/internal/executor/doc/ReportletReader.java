
package org.eclipse.birt.report.engine.internal.executor.doc;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class ReportletReader extends ReportReader
{

	public ReportletReader( ExecutionContext context, long offset )
	{
		super( context );
		this.offset = offset;
	}

	long offset = -1;

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			IReportItemExecutor reportlet = manager.createExecutor( null,
					offset );
			offset = -1;
			return reportlet;
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		if ( offset != -1 )
		{
			return true;
		}
		return false;
	}
}
