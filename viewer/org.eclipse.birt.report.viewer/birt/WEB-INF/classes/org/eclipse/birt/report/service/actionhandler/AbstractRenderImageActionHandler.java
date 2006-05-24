
package org.eclipse.birt.report.service.actionhandler;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.ServletOutputStream;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
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

	protected void __execute( ) throws RemoteException
	{
		context.getResponse( ).setContentType( "image" ); //$NON-NLS-1$
		String imageId = context.getRequest( ).getParameter(
				ParameterAccessor.PARAM_IMAGEID );
		ServletOutputStream out;
		String docName = null;// TODO: Do we need document name?
		try
		{
			InputOptions options = new InputOptions( );
			options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
			out = context.getResponse( ).getOutputStream( );
			getReportService( ).getImage( docName, imageId, out, options );
		}
		catch ( IOException e )
		{
			// TODO: Maybe not catch IOException here...
			throwAxisFault( e );
		}
		catch ( ReportServiceException e )
		{
			throwAxisFault( e );
		}
	}

	private void throwAxisFault( Exception e ) throws RemoteException
	{
		AxisFault fault = new AxisFault( );
		fault.setFaultCode( new QName(
				"BirtRenderImageActionHandler.execute( )" ) ); //$NON-NLS-1$
		fault.setFaultString( e.getLocalizedMessage( ) );
		throw fault;
	}
}
