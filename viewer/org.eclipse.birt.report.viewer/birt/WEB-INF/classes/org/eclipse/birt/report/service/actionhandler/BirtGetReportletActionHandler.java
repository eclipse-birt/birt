
package org.eclipse.birt.report.service.actionhandler;

import java.io.File;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

/**
 * Action handler for get reportlet content.
 * 
 */
public class BirtGetReportletActionHandler extends AbstractBaseActionHandler
{

	protected BaseAttributeBean __bean;

	protected String __docName;

	protected String __reportletId;

	/**
	 * Output stream to store the report.
	 */
	OutputStream os = null;

	/**
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public BirtGetReportletActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response,
			OutputStream os )
	{
		super( context, operation, response );
		this.os = os;
	}

	/**
	 * Do execution.
	 * 
	 * @exception ReportServiceException
	 * @return
	 */
	protected void __execute( ) throws Exception
	{
		prepareParameters( );
		doExecution( );
		prepareResponse( );
	}

	protected void prepareParameters( ) throws Exception, RemoteException
	{
		__bean = context.getBean( );
		__docName = __bean.getReportDocumentName( );
		__reportletId = __bean.getReportletId( );
		__checkDocumentExists( );
	}

	protected void doExecution( ) throws ReportServiceException,
			RemoteException
	{
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_LOCALE, __bean.getLocale( ) );
		options.setOption( InputOptions.OPT_FORMAT, __bean.getFormat( ) );
		options
				.setOption( InputOptions.OPT_RTL, new Boolean( __bean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, new Boolean( false ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( __bean.isMasterPageContent( ) ) );

		ArrayList activeIds = new ArrayList( );
		getReportService( ).renderReportlet( __docName, __reportletId, options,
				activeIds, os );
	}

	/**
	 * 
	 */
	protected void __checkDocumentExists( ) throws Exception
	{
		File file = new File( __docName );
		if ( !file.exists( ) )
		{
			BirtRunReportActionHandler handler = new BirtRunReportActionHandler(
					context, operation, response );
			handler.__execute( );
		}

		file = new File( __docName );
		if ( !file.exists( ) )
		{
			AxisFault fault = new AxisFault( );
			fault
					.setFaultReason( BirtResources
							.getMessage( ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT ) );
			throw fault;
		}
	}

	protected void prepareResponse( ) throws ReportServiceException,
			RemoteException
	{
	}

	/**
	 * 
	 */
	public IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
