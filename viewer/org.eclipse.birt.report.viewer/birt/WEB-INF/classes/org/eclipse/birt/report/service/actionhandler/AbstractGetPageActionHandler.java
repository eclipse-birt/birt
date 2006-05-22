package org.eclipse.birt.report.service.actionhandler;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Page;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.soapengine.api.UpdateData;

public abstract class AbstractGetPageActionHandler extends
		AbstractBaseActionHandler
{
	protected BaseAttributeBean __bean;
	
	protected String __docName;
	
	protected long __pageNumber;
	
	protected long __totalPageNumber;

	protected boolean __useBookmark = false;
	
	protected String __bookmark;
	
	protected boolean __svgFlag;
	
	protected ByteArrayOutputStream __page = null;

	protected ArrayList __activeIds = null;
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	abstract protected String __getReportDocument( );
	
	/**
	 * 
	 * @param docName
	 * @throws RemoteException
	 */
	abstract protected void __checkDocumentExists( ) throws RemoteException;
	
	/**
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractGetPageActionHandler( IContext context, Operation operation,
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
		__checkDocumentExists( );
		__bookmark = getBookmark( operation.getOprand( ), __bean );
		
		__pageNumber = getPageNumber( context.getRequest( ), operation
				.getOprand( ), __docName );
		
		// No valid page number check bookmark from soap message.
		if ( !isValidPageNumber( context.getRequest( ), __pageNumber, __docName ) )
		{
			InputOptions options = new InputOptions( );
			options.setOption( InputOptions.OPT_REQUEST, context
					.getRequest( ) );
			__pageNumber = getReportService( ).getPageNumberByBookmark(
					__docName, __bookmark, options );

			if ( !isValidPageNumber( context.getRequest( ), __pageNumber,
					__docName ) )
			{
				AxisFault fault = new AxisFault( );
				fault.setFaultReason( "Invalid bookmark: " + __bookmark ); //$NON-NLS-1$
				throw fault;
			}
			__useBookmark = true;
		}

		// Verify the page number again.
		if ( !isValidPageNumber( context.getRequest( ), __pageNumber, __docName ) )
		{
			AxisFault fault = new AxisFault( );
			fault.setFaultReason( "Invalid page number." ); //$NON-NLS-1$
			throw fault;
		}
		
		__svgFlag = getSVGFlag( operation.getOprand( ) );
	}

	protected void doExecution( ) throws ReportServiceException, RemoteException
	{
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_LOCALE, __bean.getLocale( ) );
		options.setOption( InputOptions.OPT_RTL, new Boolean( __bean.isRtl( ) ) );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_SVG_FLAG, new Boolean( __svgFlag ) );
		options.setOption( InputOptions.OPT_IS_MASTER_PAGE_CONTENT,
				new Boolean( __bean.isMasterPageContent( ) ) );
	
		__activeIds = new ArrayList( );
		__page = getReportService( ).getPage( __docName, __pageNumber + "", options,
				__activeIds );
		__totalPageNumber = getReportService( ).getPageCount( __docName, options,
				new OutputOptions( ) );
	}

	protected void prepareResponse( ) throws ReportServiceException, RemoteException
	{
		// Update instruction for document part.
		UpdateContent content = new UpdateContent( );
		content.setContent( __page.toString( ) );
		content.setTarget( "Document" ); //$NON-NLS-1$
		content.setInitializationId( parseReportId( __activeIds ) );
		if ( __useBookmark )
		{
			content.setBookmark( __bookmark );
		}
		Update updateDocument = new Update( );
		updateDocument.setUpdateContent( content );

		// Update instruction for nav bar.
		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "navigationBar" ); //$NON-NLS-1$
		Page pageObj = new Page( );
		pageObj.setPageNumber( String.valueOf( __pageNumber ) );
		pageObj.setTotalPage( String.valueOf( __totalPageNumber ) );
		Data data = new Data( );
		data.setPage( pageObj );
		updateData.setData( data );
		Update updateNavbar = new Update( );
		updateNavbar.setUpdateData( updateData );

		response.setUpdate( new Update[] { updateDocument, updateNavbar } );
	}
}
