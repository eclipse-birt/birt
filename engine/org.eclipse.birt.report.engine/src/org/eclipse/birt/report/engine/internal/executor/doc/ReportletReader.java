
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

public class ReportletReader extends ReportReader
{

	Fragment reportletFragment = null;

	public ReportletReader( ExecutionContext context, long offset )
	{
		super( context );
		this.offset = offset;
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			IReportItemExecutor reportlet = manager.createExecutor( null,
					offset, reportletFragment );
			offset = -1;
			return reportlet;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.internal.executor.doc.AbstractReportReader#openReaders()
	 */
	protected void openReaders( ) throws IOException
	{
		super.openReaders( );
		initializeReportlet( );
	}

	public boolean hasNextChild( )
	{
		if ( offset != -1 )
		{
			return true;
		}
		return false;
	}

	protected void initializeReportlet( ) throws IOException
	{
		long[] leftEdge = createEdges( offset );
		long[] rightEdge = new long[leftEdge.length + 1];
		System.arraycopy( leftEdge, 0, rightEdge, 0, leftEdge.length );
		rightEdge[leftEdge.length] = Long.MAX_VALUE;
		Fragment fragment = new Fragment( );
		fragment.addFragment( leftEdge, rightEdge );
		reportletFragment = fragment.getNextFragment( -1 );
		offset = reportletFragment.offset;
	}

	protected long[] createEdges( long offset ) throws IOException
	{
		LinkedList parents = new LinkedList( );
		IContent content = reader.loadContent( offset );
		while ( content != null )
		{
			DocumentExtension ext = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			if ( ext != null )
			{
				parents.addFirst( new Long( ext.getIndex( ) ) );
			}
			content = (IContent) content.getParent( );
		}
		long[] edges = new long[parents.size( )];
		Iterator iter = parents.iterator( );
		int length = 0;
		while ( iter.hasNext( ) )
		{
			Long value = (Long) iter.next( );
			edges[length++] = value.longValue( );
		}
		return edges;
	}

}
