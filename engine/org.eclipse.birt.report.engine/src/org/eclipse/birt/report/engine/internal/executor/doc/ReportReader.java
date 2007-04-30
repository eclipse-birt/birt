
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class ReportReader extends AbstractReportReader
{

	IReportExecutor executor = null;

	BodyReader bodyReader;

	public ReportReader( ExecutionContext context ) throws IOException
	{
		super( context );
		bodyReader = new BodyReader( this, null );
	}

	public IReportContent execute( )
	{
		return reportContent;
	}

	public IReportItemExecutor getNextChild( )
	{
		return bodyReader.getNextChild( );
	}

	public boolean hasNextChild( )
	{
		return bodyReader.hasNextChild( );
	}

	public void close( )
	{
		bodyReader.close( );
		super.close( );

	}
}
