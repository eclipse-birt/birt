
package org.eclipse.birt.report.engine.internal.executor.doc;

import org.eclipse.birt.report.engine.content.IContent;

class PageReader extends ReportItemReader
{

	ReportPageReader pageReader;
	long pageNumber;

	PageReader( ReportPageReader pageReader, long pageNumber, Fragment fragment )
	{
		super( pageReader.manager );
		this.pageReader = pageReader;
		this.pageNumber = pageNumber;
		this.fragment = fragment;
	}

	public IContent execute( )
	{	
		pageReader.context.setPageNumber( pageNumber );
		pageReader.context.setExecutingMasterPage( true );
		// return the PageContent which is loaded from the content
		IContent content = pageReader.loadLocalizedPageContent( pageNumber );
		pageReader.initializeContent( content );
		// execute extra intialization
		pageReader.initalizeContentVisitor.visit( content, null );
		pageReader.context.setExecutingMasterPage( false );
		//setup the first child
		Fragment childFrag = fragment.getNextFragment( -1 );
		if (childFrag != null)
		{
			this.child = childFrag.offset;
		}
		else
		{
			this.child = -1;
		}
		return content;
	}

	public void close( )
	{
	}
}
