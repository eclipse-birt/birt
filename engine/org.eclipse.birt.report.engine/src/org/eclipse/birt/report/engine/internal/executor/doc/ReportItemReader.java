
package org.eclipse.birt.report.engine.internal.executor.doc;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.ReportItemExecutorBase;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

public class ReportItemReader extends ReportItemExecutorBase
{

	protected ReportItemReaderManager manager;

	protected AbstractReportReader reader;

	protected ReportItemReader parent;

	protected Fragment fragment;

	ReportItemReader( ReportItemReaderManager manager )
	{
		this.manager = manager;
	}

	void initialize( AbstractReportReader reader, ReportItemReader parent,
			long offset, Fragment frag )
	{
		assert reader != null;
		assert offset != -1;
		this.reader = reader;
		this.parent = parent;
		this.offset = offset;
		this.content = null;
		this.child = -1;
		this.rsets = null;
		this.fragment = frag;
	}

	long offset;
	IContent content;
	long child;
	IBaseResultSet[] rsets;

	public void close( )
	{
		unloadContent( );
		manager.releaseExecutor( this );
	}

	public IContent execute( )
	{
		try
		{
			// load it from the content stream
			loadContent( );
			// setup the report design
			initializeContent( );
			return content;
		}
		catch ( Exception ex )
		{
			return null;
		}
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			Fragment childFrag = fragment == null ? null : fragment
					.getFragment( child );
			ReportItemReader childReader = manager.createExecutor( this, child,
					childFrag );
			child = childReader.findNextSibling( );
			if ( child != -1 && fragment != null )
			{
				if ( !fragment.inFragment( child ) )
				{
					Fragment nextFragment = fragment
							.getNextFragment( child );
					if ( nextFragment != null )
					{
						child = nextFragment.offset;
					}
					else
					{
						child = -1;
					}
				}
			}
			return childReader;
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return child != -1;
	}

	protected long findFirstChild( )
	{
		loadContent( );
		if ( content != null )
		{
			DocumentExtension docExt = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			long firstChild = docExt.getFirstChild( );
			if ( firstChild != -1 && fragment != null )
			{
				if ( !fragment.inFragment( firstChild ) )
				{
					Fragment childFragment = fragment.getNextFragment( -1 );
					if ( childFragment != null )
					{
						return childFragment.offset;
					}
					return -1;
				}
			}
			return firstChild;
		}
		return -1;
	}

	protected long findNextSibling( )
	{
		loadContent( );
		if ( content != null )
		{
			DocumentExtension docExt = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			return docExt.getNext( );
		}
		return -1;
	}

	protected IBaseResultSet getResultSet( )
	{
		if ( rsets == null || rsets[0] == null )
		{
			if ( parent != null )
			{
				return parent.getResultSet( );
			}
			return null;
		}
		return rsets[0];
	}

	protected void loadContent( )
	{
		if ( content == null )
		{
			content = reader.loadContent( offset );
			if ( content != null )
			{
				DocumentExtension docExt = (DocumentExtension) content
						.getExtension( IContent.DOCUMENT_EXTENSION );
				child = docExt.getFirstChild( );
				if (child != -1 && fragment != null)
				{
					if ( !fragment.inFragment( child ) )
					{
						Fragment childFragment = fragment.getNextFragment( -1 );
						if ( childFragment != null )
						{
							child = childFragment.offset;
						}
						else
						{
							child = -1;
						}
					}
				}
			}
		}
	}

	/**
	 * intialize the content loaded from the report document.
	 * 
	 * Once the report content is loaded, it is not associated with the report
	 * design, so it almost contains nothing, most of the data should be
	 * retetrived from the design element.
	 * 
	 * In the intialization, it first search the report design to see which
	 * design element creates the report content, then re-load the data from the
	 * report document and uses the data to re-fill some fields of the content.
	 * 
	 * Each content can be intailzied only once.
	 * 
	 */
	private void initializeContent( ) throws BirtException
	{
		assert content != null;
		reader.initializeContent( content );

		/**
		 * filter emtpy group content, a special case is the group content is the ending of page hint.
		 * if not filter it, group header will diplay at this page.
		 */
		if ( fragment != null && hasNextChild())
		{
			Object genBy = content.getGenerateBy( );
			if ( content instanceof ITableContent )
			{
				if ( genBy instanceof TableItemDesign )
				{
					TableItemDesign tableDesign = (TableItemDesign) genBy;
					if ( ( (ITableContent) content ).isHeaderRepeat( )
							&& tableDesign.getHeader( ) != null )
					{
						addHeaderToFragment( content );
					}
				}
			}
			else if ( content instanceof IGroupContent )
			{
				if ( genBy instanceof GroupDesign )
				{
					GroupDesign groupDesign = (GroupDesign) genBy;
					if ( ( (IGroupContent) content ).isHeaderRepeat( )
							&& groupDesign.getHeader( ) != null )
					{
						addHeaderToFragment( content );
					}
				}
			}
			else if ( content instanceof IListContent )
			{
				if ( genBy instanceof ListItemDesign )
				{
					ListItemDesign listDesign = (ListItemDesign) genBy;
					if ( ( (IListContent) content ).isHeaderRepeat( )
							&& listDesign.getHeader( ) != null )
					{
						addHeaderToFragment( content );
					}
				}
			}
		}

		IBaseResultSet prset = parent == null ? null : parent
					.getResultSet( );
		// restore the parent result set
		reader.context.setResultSet( prset );
		// open the query used by the content, locate the resource
		rsets = reader.openQueries( prset, content );
		if ( rsets != null )
		{
			reader.context.setResultSets( rsets );
		}
		// execute extra intialization
		reader.initalizeContentVisitor.visit( content, null );
	}

	private void addHeaderToFragment( IContent content )
	{
		assert fragment != null;
		DocumentExtension docExt = (DocumentExtension) content
				.getExtension( IContent.DOCUMENT_EXTENSION );
		if ( docExt != null )
		{
			long headerOffset = docExt.getFirstChild( );
			if ( headerOffset != -1 )
			{
				fragment.addFragment( new long[]{headerOffset},
						new long[]{headerOffset} );
				Fragment headerFrag = fragment.getFragment( headerOffset );
				headerFrag.addFragment( new long[]{},
						new long[]{Long.MAX_VALUE} );
				//reset the child offset
				child = headerOffset;
			}
		}
	}

	/**
	 * the content is loaded, and it will not be used by any one else.
	 */
	protected void unloadContent( )
	{
		if ( rsets != null )
		{
			reader.closeQueries( rsets );
			rsets = null;
		}
		reader.unloadContent( offset );
	}
}