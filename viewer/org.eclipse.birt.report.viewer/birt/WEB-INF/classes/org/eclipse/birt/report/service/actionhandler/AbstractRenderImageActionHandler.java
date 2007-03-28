
package org.eclipse.birt.report.service.actionhandler;

import javax.servlet.ServletOutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

abstract public class AbstractRenderImageActionHandler
		extends
			AbstractBaseActionHandler
{

	public AbstractRenderImageActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	public void __execute( ) throws Exception
	{
		context.getResponse( ).setContentType( "image" ); //$NON-NLS-1$
		String imageId = context.getRequest( ).getParameter(
				ParameterAccessor.PARAM_IMAGEID );
		ServletOutputStream out;
		String docName = null;// TODO: Do we need document name?

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		out = context.getResponse( ).getOutputStream( );
		getReportService( ).getImage( docName, imageId, out, options );
	}
}
