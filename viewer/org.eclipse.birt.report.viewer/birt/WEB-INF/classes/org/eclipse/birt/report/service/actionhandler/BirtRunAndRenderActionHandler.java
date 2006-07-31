
package org.eclipse.birt.report.service.actionhandler;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtRunAndRenderActionHandler extends AbstractBaseActionHandler
{

	public BirtRunAndRenderActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws Exception
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		String format = ParameterAccessor.getFormat( context.getRequest( ) );
		Locale locale = attrBean.getLocale( );
		boolean master = attrBean.isMasterPageContent( );
		Map params = attrBean.getParameters( );
		Map displayTexts = attrBean.getDisplayTexts( );
		IViewerReportDesignHandle reportDesignHandle = attrBean
				.getReportDesignHandle( context.getRequest( ) );
		boolean svgFlag = ParameterAccessor.getSVGFlag( context.getRequest( ) );
		String outputDocName = attrBean.getReportDocumentName( );

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL,
				new Boolean( attrBean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( master ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, new Boolean( svgFlag ) );
		options.setOption( InputOptions.OPT_FORMAT, format );
		options.setOption( InputOptions.OPT_IS_DESIGNER, new Boolean( attrBean
				.isDesigner( ) ) );

		ServletOutputStream out = context.getResponse( ).getOutputStream( );
		getReportService( ).runAndRenderReport( reportDesignHandle,
				outputDocName, options, params, out, new ArrayList( ),
				displayTexts );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
