
package org.eclipse.birt.report.engine.internal.executor.doc;

import org.eclipse.birt.report.engine.content.IContent;

class PageReader extends ReportItemReader
{
	long pageNumber;
	ReportItemReaderManager manager;

	PageReader( AbstractReportReader reportReader, long pageNumber )
	{
		super( reportReader.context );
		this.reader = reportReader.pageReader;
		this.manager = reportReader.manager;
	}

	public IContent execute( )
	{
		context.setPageNumber( pageNumber );
		context.setExecutingMasterPage( true );

		content = super.execute( );
		return content;
	}

	public void close( )
	{
		context.setExecutingMasterPage( false );
		super.close( );
	}
	
	ReportItemReader createExecutor( ReportItemReader parent, long offset,
			Fragment fragment )
	{
		return manager.createExecutor( parent, offset, fragment );
	}
}
