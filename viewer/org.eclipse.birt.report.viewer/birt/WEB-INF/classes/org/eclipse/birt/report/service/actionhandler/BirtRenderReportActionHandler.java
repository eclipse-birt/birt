
package org.eclipse.birt.report.service.actionhandler;

import java.io.OutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public class BirtRenderReportActionHandler extends AbstractBaseActionHandler
{

	/**
	 * Output stream to store the report.
	 */
	private OutputStream os = null;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtRenderReportActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response,
			OutputStream os )
	{
		super( context, operation, response );
		assert os != null;
		this.os = os;
	}

	/**
	 * Local execution.
	 * 
	 * @exception ReportServiceException
	 * @return
	 */
	protected void __execute( ) throws Exception
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		assert attrBean != null;

		String docName = attrBean.getReportDocumentName( );
		boolean master = attrBean.isMasterPageContent( );

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, attrBean.getLocale( ) );
		options.setOption( InputOptions.OPT_RTL,
				new Boolean( attrBean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_FORMAT, attrBean.getFormat( ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( master ) );
		options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean( attrBean
				.isDesigner( ) ) );

		getReportService( ).renderReport( docName, attrBean.getReportPage( ),
				attrBean.getReportPageRange( ), options, os );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}

}
