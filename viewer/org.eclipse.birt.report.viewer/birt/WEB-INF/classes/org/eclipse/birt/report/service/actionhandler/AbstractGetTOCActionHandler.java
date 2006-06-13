package org.eclipse.birt.report.service.actionhandler;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.service.api.ToC;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.TOC;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.ParameterAccessor;

public abstract class AbstractGetTOCActionHandler extends
		AbstractBaseActionHandler
{
	protected BaseAttributeBean __bean;
	
	protected String __docName;
	
	protected ToC __node = null;

	/**
	 * 
	 * @param bean
	 * @return
	 */
	abstract protected String __getReportDocument( );
	

	public AbstractGetTOCActionHandler( IContext context, Operation operation,
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

	protected void prepareParameters( ) throws ReportServiceException, RemoteException
	{
		__bean = context.getBean( );
		__docName = __getReportDocument( );
	}

	protected void doExecution( ) throws ReportServiceException, RemoteException
	{
		Oprand[] oprands = operation.getOprand( );
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		if ( oprands != null && oprands.length > 0 )
		{
			__node = getReportService( ).getTOC( __docName, oprands[0].getValue( ), options );
		}
		else
		{
			__node = getReportService( ).getTOC( __docName, null, options );
		}
	}

	protected void prepareResponse( ) throws ReportServiceException, RemoteException
	{
		TOC toc = new TOC( );
		List children = __node.getChildren( );
		if ( children != null && children.size( ) > 0 )
		{
			TOC[] childTOCNodes = new TOC[children.size( )];
			for ( int i = 0; i < children.size( ); i++ )
			{
				ToC child = ( ToC ) children.get( i );
				childTOCNodes[i] = new TOC( );
				childTOCNodes[i].setId( child.getID( ) );
				childTOCNodes[i].setDisplayName( child.getDisplayName( ) );
				childTOCNodes[i].setBookmark( child.getBookmark( ) );
				childTOCNodes[i].setIsLeaf( new Boolean(
						child.getChildren( ) == null
								|| child.getChildren( ).size( ) <= 0 ) );
			}
			toc.setChild( childTOCNodes );
		}

		Data data = new Data( );
		data.setTOC( toc );
		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "birtToc" ); //$NON-NLS-1$
		updateData.setData( data );
		Update update = new Update( );
		update.setUpdateData( updateData );
		response.setUpdate( new Update[] { update } );
	}
}
