
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class ReportReader extends AbstractReportReader
{

	IReportExecutor executor = null;
	protected long offset = 0;

	public ReportReader( ExecutionContext context )
	{
		super( context );
	}

	public IReportContent execute( )
	{
		try
		{
			openReaders( );
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "Fail to open the readers", ex );
			closeReaders( );
		}
		return reportContent;
	}

	public void close( )
	{
		closeReaders( );
	}

	protected void closeReaders()
	{
		super.closeReaders( );
		offset = -1;
	}
	
	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			ReportItemReader childReader = manager
					.createExecutor( null, offset );
			offset = childReader.findNextSibling( );
			return childReader;
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return offset != -1;
	}

	public IPageContent createPage( long pageNumber, MasterPageDesign pageDesign )
	{
		if ( executor == null )
		{
			executor = new ReportExecutor( context, context.getReport( ), null );
			executor = new LocalizedReportExecutor( context, executor );
		}
		return executor.createPage( pageNumber, pageDesign );
	}
}
