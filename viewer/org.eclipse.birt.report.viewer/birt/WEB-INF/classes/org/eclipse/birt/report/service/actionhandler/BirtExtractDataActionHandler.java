package org.eclipse.birt.report.service.actionhandler;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtExtractDataActionHandler extends AbstractBaseActionHandler
{

	public BirtExtractDataActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws Exception
	{
		ViewerAttributeBean attrBean = ( ViewerAttributeBean ) context
				.getBean( );
		String docName = attrBean.getReportDocumentName( );
		String resultSetName = ParameterAccessor.getResultSetName( context
				.getRequest( ) );
		Collection columns = ParameterAccessor.getSelectedColumns( context
				.getRequest( ) );
		Set filters = Collections.EMPTY_SET;
		Locale locale = attrBean.getLocale( );
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL, new Boolean( attrBean.isRtl( ) ) );
		
		ServletOutputStream out = context.getResponse( ).getOutputStream( );
		getReportService( ).extractResultSet( docName, resultSetName,
				columns, filters, options, out );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
