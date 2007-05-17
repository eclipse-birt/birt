
package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * execute the master page.
 * 
 * the master page's children include page header and footer.
 */
public class MasterPageExecutor extends ContainerExecutor
{

	private static final int HEADER_BAND = 0;
	private static final int BODY_BAND = 1;
	private static final int FOOTER_BAND = 2;

	private long pageNumber;
	private long offset;
	private SimpleMasterPageDesign masterPage;

	private int nextBand;

	protected MasterPageExecutor( ExecutorManager manager, long pageNumber )
	{
		super( manager, -1 );
		this.reader = manager.getPageReader( );
		this.pageNumber = pageNumber;
		this.nextBand = 0;
	}

	public void close( )
	{
		context.setExecutingMasterPage( false );
		pageNumber = 0;
		nextBand = 0;
		reader.unloadContent(offset);
		super.close( );
	}

	public IContent execute( )
	{
		if ( executed )
		{
			return content;
		}
		context.setExecutingMasterPage( true );
		context.setPageNumber( pageNumber );
		executed = true;
		try
		{
			long pageNo = pageNumber;
			PageHintReader hintReader = manager.getPageHintReader( );
			long totalPage = hintReader.getTotalPage( );
			if ( pageNumber > totalPage )
			{
				pageNo = totalPage;
			}
			IPageHint hint = hintReader.getPageHint( pageNo );
			offset = hint.getOffset( );
			content = reader.loadContent( offset );
			InstanceID iid = content.getInstanceID( );
			long id = iid.getComponentID( );
			masterPage = (SimpleMasterPageDesign) context.getReport( )
					.getReportItemByID( id );
			content.setGenerateBy( masterPage );

			IPageContent pageContent = (IPageContent) content;
			pageContent.setPageNumber( pageNumber );

			return content;
		}
		catch ( IOException ex )
		{
			context.addException( new EngineException(
					ex.getLocalizedMessage( ), ex ) );
		}
		return null;
	}

	protected ReportItemExecutor doCreateExecutor( long offset )
			throws Exception
	{
		if ( nextBand >= HEADER_BAND && nextBand <= FOOTER_BAND )
		{
			ArrayList band = null;
			switch ( nextBand )
			{
				case HEADER_BAND :
					band = masterPage.getHeaders( );
					break;
				case FOOTER_BAND :
					band = masterPage.getFooters( );
					break;
				case BODY_BAND :
					band = new ArrayList( );
					break;
			}
			nextBand++;
			PageBandExecutor bandExecutor = new PageBandExecutor( this, band );
			bandExecutor.setParent( this );
			bandExecutor.setOffset( offset );
			return bandExecutor;
		}
		return null;

	}

	/**
	 * adjust the nextItem to the nextContent.
	 * 
	 * before call this method, both the nextContent and the nextFragment can't
	 * be NULL.
	 * 
	 * @return
	 */
	protected void doSkipToExecutor( InstanceID id, long offset )
			throws Exception
	{
		throw new IllegalStateException(
				"master page never comes with page hints" );
	}

}
