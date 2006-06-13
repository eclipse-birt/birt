package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.LinkedList;

/**
 * Manager used to create the report item readers.
 * 
 * It use a free list to store the unused readres, and after the 
 * reader is closed, it should be return to the freelist, so that
 * it can be resued by others.
 * 
 *  Once the caller close the report item reader, the reader can't be
 *  used any more.
 */
class ReportItemReaderManager
{
	protected LinkedList freeList = new LinkedList();
	protected AbstractReportReader reader;

	ReportItemReaderManager (AbstractReportReader reader )
	{
		this.reader = reader;
	}
	
	ReportItemReader createExecutor(ReportItemReader parent, long offset)
	{
		return createExecutor(parent, offset, null);
	}
	ReportItemReader createExecutor(ReportItemReader parent, long offset, Fragment frag)
	{
		ReportItemReader executor = null;
		if ( !freeList.isEmpty( ) )
		{
			executor = (ReportItemReader) freeList.removeFirst( );
		}
		else
		{
			executor = new ReportItemReader( this );
		}
		executor.initialize( reader, parent, offset , frag);
		return executor;
	}

	void releaseExecutor( ReportItemReader executor )
	{
		freeList.addLast( executor );
	}
}
