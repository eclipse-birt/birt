
package org.eclipse.birt.report.service.actionhandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;

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

public class BirtGetReportletActionHandler
		extends AbstractBaseActionHandler
{
	protected BaseAttributeBean __bean;

	protected String __docName;

	protected String __reportletId;

	/**
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public BirtGetReportletActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		try
		{
			prepareParameters( );
			doExecution( );
			prepareResponse( );
		}
		catch ( ReportServiceException e )
		{
			AxisFault fault = new AxisFault( );
			fault.setFaultReason( e.getLocalizedMessage( ) );
			throw fault;
		}
	}

	protected void prepareParameters( ) throws ReportServiceException,
			RemoteException
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
		options.setOption( InputOptions.OPT_RTL, new Boolean( __bean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, new Boolean( false ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( __bean.isMasterPageContent( ) ) );

		ArrayList activeIds = new ArrayList( );
		OutputStream out = null;
		try
		{
			out = context.getResponse( ).getOutputStream( );
		}
		catch ( IOException e )
		{
			// TODO:
		}
		getReportService( ).renderReportlet( __docName, __reportletId, options, activeIds, out );
	}

	/**
	 * 
	 */
	protected void __checkDocumentExists( ) throws RemoteException
	{
		File file = new File( __docName );
		if ( !file.exists( ) )
		{
			IActionHandler handler = new BirtRunReportActionHandler( context,
					operation, response );
			handler.execute( );
		}

		file = new File( __docName );
		if ( !file.exists( ) )
		{
			AxisFault fault = new AxisFault( );
			fault
					.setFaultReason( BirtResources
							.getString( ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT ) );
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
