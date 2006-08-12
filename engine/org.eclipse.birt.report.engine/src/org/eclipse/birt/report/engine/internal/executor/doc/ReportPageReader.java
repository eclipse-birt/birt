
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintReaderV2;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public class ReportPageReader extends ReportReader
{

	ArrayList outputPages = new ArrayList( );
	ArrayList pageHints = new ArrayList( );
	Fragment fragment = new Fragment( );
	boolean keepPaginate;
	PageHintReaderV2 hintReader;
	CachedReportContentReaderV3 pageReader;

	public ReportPageReader( ExecutionContext context, long pageNumber,
			boolean keepPaginate )
	{
		super( context );
		outputPages.add( new long[]{pageNumber, pageNumber} );
		this.keepPaginate = keepPaginate;
	}

	/**
	 * does the output should keep the pagination.
	 * 
	 * For some emitter, it will has its own pagination, so the report page
	 * reader only read out the page content and merge the contente together.
	 * The emitter will re-paginate the content again. Such as output PDF using
	 * HTML paginhints.
	 * 
	 * Some emitter in the otherside, will use the same pagination with the page
	 * hint. For those emitter, the output will include the master pages. such
	 * as output HTML with the HTML emitter.
	 * 
	 * @param context
	 *            context used to read the report.
	 * @param pages
	 *            page list
	 * @param keepPaginate
	 *            should the output keep pagianted.
	 */
	public ReportPageReader( ExecutionContext context, List pages,
			boolean keepPaginate )
	{
		super( context );
		outputPages.addAll( pages );
		this.keepPaginate = keepPaginate;
	}

	protected void openReaders( ) throws IOException
	{
		super.openReaders( );
		// open the page hints stream and the page content stream
		hintReader = new PageHintReaderV2( reportDoc );
		hintReader.open( );
		IDocArchiveReader archive = reportDoc.getArchive( );
		RAInputStream in = archive
				.getStream( ReportDocumentConstants.PAGE_STREAM );
		pageReader = new CachedReportContentReaderV3( reportContent, in );
	}

	protected void closeReaders( )
	{
		super.closeReaders( );
		if ( hintReader != null )
		{
			hintReader.close( );
			hintReader = null;
		}
		if ( pageReader != null )
		{
			pageReader.close( );
			pageReader = null;
		}
		outputPages.clear( );
	}

	protected IPageHint loadPageHint(long pageNumber)
	{
		try
		{
			IPageHint hint = hintReader.getPageHint( pageNumber );
			return hint;
		}
		catch(IOException ex)
		{
			logger.log( Level.WARNING, "Failed to load page hint" + pageNumber, ex );
		}
		return null;
	}
	protected void loadPageHints( )
	{
		for ( int m = 0; m < outputPages.size( ); m++ )
		{
			long[] ps = (long[]) outputPages.get( m );
			for ( long pageNumber = ps[0]; pageNumber <= ps[1]; pageNumber++ )
			{
				IPageHint pageHint = loadPageHint( pageNumber );
				if ( pageHint == null )
				{
					continue;
				}
				int sectCount = pageHint.getSectionCount( );
				for ( int i = 0; i < sectCount; i++ )
				{
					try
					{
						long left = pageHint.getSectionStart( i );
						long right = pageHint.getSectionEnd( i );
						long[] leftEdges = createEdges( left );
						long[] rightEdges = createEdges( right );
						fragment.addFragment( leftEdges, rightEdges );
					}
					catch ( IOException ex )
					{
						logger.log( Level.SEVERE, "Can't load the page hints",
								ex );
					}
				}
			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.internal.executor.doc.ReportReader#execute()
	 */
	public IReportContent execute( )
	{
		IReportContent content = super.execute( );

		if ( !keepPaginate )
		{
			loadPageHints( );
			nextElement = getFirstElementOffset( );
		}

		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.internal.executor.doc.ReportReader#getNextChild()
	 */
	public IReportItemExecutor getNextChild( )
	{
		if ( keepPaginate )
		{
			return getNextPage( );
		}
		return getNextElement( );
	}

	public boolean hasNextChild( )
	{
		if ( keepPaginate )
		{
			return hasNextPage( );
		}
		return hasNextElement( );
	}

	private int curPageRange = -1;
	private long nextPage = -1;

	private boolean hasNextPage( )
	{
		if ( nextPage == -1 )
		{
			nextPage = getNextPageNumber( );
		}
		return nextPage != -1;
	}

	private IReportItemExecutor getNextPage( )
	{
		if ( hasNextPage( ) )
		{
			assert nextPage != -1;
			IReportItemExecutor pageExecutor = new PageReader( this, nextPage );
			nextPage = getNextPageNumber( );
			return pageExecutor;
		}
		return null;
	}

	private long getNextPageNumber( )
	{
		// return the first page of the first range
		if ( curPageRange == -1 )
		{
			if (outputPages.size( ) > 0)
			{
				curPageRange = 0;
				long[] pageRange = (long[]) outputPages.get( curPageRange );
				return pageRange[0];
			}
			return -1;
		}
		// we still have some pages remain
		if ( curPageRange < outputPages.size( ) )
		{
			long pageNumber = nextPage + 1;
			// test if it is in the current range
			long[] pageRange = (long[]) outputPages.get( curPageRange );
			if ( pageRange[0] <= pageNumber && pageRange[1] >= pageNumber )
			{
				return pageNumber;
			}
			// if it exceed the current page, use the first page of the next
			// page range
			curPageRange++;
			if ( curPageRange < outputPages.size( ) )
			{
				pageRange = (long[]) outputPages.get( curPageRange );
				return pageRange[0];
			}
		}
		// all page has been outputed
		return -1;
	}

	IPageContent loadPageContent( long pageNumber )
	{
		IPageHint pageHint = loadPageHint( pageNumber );
		if ( pageHint != null )
		{
			long offset = pageHint.getOffset( );
			// load the page reader full content
			try
			{
				IContent pageContent = pageReader.loadContent( offset );
				initializeContent( pageContent );
				loadFullContent( pageReader, pageContent, null );
				pageReader.unloadContent( offset );
				return (IPageContent) pageContent;
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "Can't load the page content", ex );
			}
		}
		return null;
	}

	private void loadFullContent( CachedReportContentReaderV3 reader,
			IContent parent, IResultSet prset ) throws IOException
	{
		DocumentExtension docExt = (DocumentExtension) parent
				.getExtension( IContent.DOCUMENT_EXTENSION );
		long offset = docExt.getFirstChild( );
		while ( offset != -1 )
		{
			IContent content = reader.loadContent( offset );
			initializeContent( content );
			IResultSet rset = openQuery( prset, content );
			// execute extra intialization
			initalizeContentVisitor.visit( content, null );

			parent.getChildren( ).add( content );
			loadFullContent( reader, content, rset == null ? prset : rset );
			if ( rset != null )
			{
				closeQuery( rset );
			}
			reader.unloadContent( offset );
			docExt = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			offset = docExt.getNext( );
		}
	}

	Fragment loadPageFragment( long pageNumber )
	{
		Fragment fragment = new Fragment( );
		IPageHint pageHint = loadPageHint( pageNumber );
		if ( pageHint != null )
		{
			int sectionCount = pageHint.getSectionCount( );
			for ( int i = 0; i < sectionCount; i++ )
			{
				try
				{
					long left = pageHint.getSectionStart( i );
					long right = pageHint.getSectionEnd( i );
					long[] leftEdges = createEdges( left );
					long[] rightEdges = createEdges( right );
					fragment.addFragment( leftEdges, rightEdges );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, "Can't load the page hints", ex );
				}
			}
		}
		return fragment;
	}

	private long nextElement = -1;

	IReportItemExecutor getNextElement( )
	{
		if ( hasNextElement( ) )
		{
			assert nextElement != -1;
			Fragment nextFrag = fragment.getFragment( nextElement );
			ReportItemReader reader = manager.createExecutor( null,
					nextElement, nextFrag );
			long nextOffset = reader.findNextSibling( );
			if ( nextOffset != -1 )
			{
				if ( !fragment.inFragment( nextOffset ) )
				{
					// find in next segment
					nextFrag = fragment.getNextFragment( nextOffset );
					if ( nextFrag != null )
					{
						nextOffset = nextFrag.offset;
					}
					else
					{
						nextOffset = -1;
					}
				}
			}
			nextElement = nextOffset;
			return reader;
		}
		return null;

	}

	boolean hasNextElement( )
	{
		return ( nextElement != -1 );
	}

	long getFirstElementOffset( )
	{
		Fragment fstFrag = fragment.getNextFragment( -1 );
		if ( fstFrag != null )
		{
			return fstFrag.offset;
		}
		return -1;
	}
}
