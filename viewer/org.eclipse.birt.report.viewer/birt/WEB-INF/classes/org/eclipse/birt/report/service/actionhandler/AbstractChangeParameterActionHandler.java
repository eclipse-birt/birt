
package org.eclipse.birt.report.service.actionhandler;

import java.rmi.RemoteException;
import java.util.Locale;

import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public abstract class AbstractChangeParameterActionHandler
		extends
			AbstractBaseActionHandler
{

	public AbstractChangeParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean( );
		boolean svgFlag = getSVGFlag( operation.getOprand( ) );

		// First generate report document.
		runReport( );

		String bookmark = null;
		boolean useBookmark = false;

		String docName = attrBean.getReportDocumentName( );

		try
		{
			long pageNumber = getPageNumber( context.getRequest( ), operation
					.getOprand( ), docName );

			if ( !isValidPageNumber( context.getRequest( ), pageNumber, docName ) )
			{
				bookmark = getBookmark( operation.getOprand( ), attrBean );
				if ( bookmark != null && bookmark.length( ) > 0 )
				{
					InputOptions options = new InputOptions( );
					options.setOption( InputOptions.OPT_REQUEST, context
							.getRequest( ) );
					pageNumber = getReportService( ).getPageNumberByBookmark(
							docName, bookmark, options );
					useBookmark = true;
				}
				if ( !isValidPageNumber( context.getRequest( ), pageNumber,
						docName ) )
				{
					pageNumber = 1;
					useBookmark = false;
				}
			}

			doRenderPage( docName, pageNumber, svgFlag, attrBean
					.isMasterPageContent( ), useBookmark, bookmark, attrBean
					.getLocale( ), attrBean.isRtl( ) );
		}
		catch ( ReportServiceException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

	}

	protected abstract void runReport( ) throws RemoteException;

	protected abstract void doRenderPage( String docName, long pageNumber,
			boolean svgFlag, boolean isMasterContent, boolean useBookmark,
			String bookmark, Locale locale, boolean isRtl )
			throws ReportServiceException, RemoteException;
}
