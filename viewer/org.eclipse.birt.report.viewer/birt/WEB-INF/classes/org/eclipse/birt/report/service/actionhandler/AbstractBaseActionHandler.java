/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.ReportId;
import org.eclipse.birt.report.soapengine.api.ReportIdType;

abstract public class AbstractBaseActionHandler implements IActionHandler
{

	/**
	 * Current context instance.
	 */
	protected IContext context = null;

	/**
	 * Current operation instance.
	 */
	protected Operation operation = null;

	/**
	 * Current soap response instance.
	 */
	protected GetUpdatedObjectsResponse response = null;

	/**
	 * Abstract methods.
	 */
	abstract protected void __execute( ) throws RemoteException;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractBaseActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		this.context = context;
		this.operation = operation;
		this.response = response;
	}

	/**
	 * Execute action handler.
	 * 
	 * @exception RemoteException
	 * @return
	 */
	public void execute( ) throws RemoteException
	{
		__execute( );
	}

	/**
	 * Check whether the page number is valid or not.
	 * 
	 * @param pageNumber
	 * @param document
	 * @return
	 * @throws RemoteException
	 * @throws ReportServiceException
	 */
	protected boolean isValidPageNumber( HttpServletRequest request,
			long pageNumber, String documentName ) throws RemoteException,
			ReportServiceException
	{
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );
		return pageNumber > 0
				&& pageNumber <= getReportService( ).getPageCount(
						documentName, options, new OutputOptions( ) );
	}

	/**
	 * Get page number from incoming soap request.
	 * 
	 * @param params
	 * @param document
	 * @return
	 * @throws RemoteException
	 * @throws ReportServiceException
	 */
	protected long getPageNumber( HttpServletRequest request, Oprand[] params,
			String documentName ) throws RemoteException,
			ReportServiceException
	{
		long pageNumber = -1;
		if ( params != null && params.length > 0 )
		{
			for ( int i = 0; i < params.length; i++ )
			{
				if ( IBirtConstants.OPRAND_PAGENO.equalsIgnoreCase( params[i]
						.getName( ) ) )
				{
					try
					{
						pageNumber = Integer.parseInt( params[i].getValue( ) );
					}
					catch ( NumberFormatException e )
					{
						pageNumber = -1;
					}
					InputOptions options = new InputOptions( );
					options.setOption( InputOptions.OPT_REQUEST, request );
					if ( pageNumber <= 0
							|| pageNumber > getReportService( ).getPageCount( documentName, 
									options, new OutputOptions( ) ) )
					{
						AxisFault fault = new AxisFault( );
						fault.setFaultCode( new QName(
								"DocumentProcessor.getPageNumber( )" ) ); //$NON-NLS-1$
						fault.setFaultString( "Invalid page number." ); //$NON-NLS-1$
						throw fault;
					}

					break;
				}
			}
		}

		return pageNumber;
	}

	/**
	 * Get page number by bookmark.
	 * 
	 * @param params
	 * @param bean
	 * @param document
	 * @return
	 * @throws RemoteException
	 */
	protected String getBookmark( Oprand[] params, BaseAttributeBean bean )
	{
		assert bean != null;

		String bookmark = null;
		if ( params != null && params.length > 0 )
		{
			for ( int i = 0; i < params.length; i++ )
			{
				if ( IBirtConstants.OPRAND_BOOKMARK.equalsIgnoreCase( params[i]
						.getName( ) ) )
				{
					bookmark = params[i].getValue( );
					break;
				}
			}
		}

		// Then use url bookmark.
		if ( bookmark == null || bookmark.length( ) <= 0 )
		{
			bookmark = bean.getBookmark( );
		}

		return bookmark;
	}

	/**
	 * Paser returned report ids.
	 * 
	 * @param activeIds
	 * @return
	 * @throws RemoteException
	 */
	protected ReportId[] parseReportId( ArrayList activeIds )
			throws RemoteException
	{
		if ( activeIds == null || activeIds.size( ) <= 0 )
		{
			return null;
		}

		java.util.Vector ids = new java.util.Vector( );
		for ( int i = 0; i < activeIds.size( ); i++ )
		{
			String id = ( String ) activeIds.get( i );
			int firstComma = id.indexOf( ',' );
			if ( id == null || firstComma == -1 ) //$NON-NLS-1$
			{
				AxisFault fault = new AxisFault( );
				fault.setFaultCode( new QName(
						"DocumentProcessor.parseReportId( )" ) ); //$NON-NLS-1$
				fault.setFaultString( "Invalid id format. Id= " + id ); //$NON-NLS-1$
				throw fault;
			}

			int secondComma = id.indexOf( ',', firstComma + 1 );
			if ( secondComma == -1 )
			{
				secondComma = id.length( );
			}
			String type = id.substring( firstComma + 1, secondComma );
			if ( ReportIdType._Document.equalsIgnoreCase( type )
					|| ReportIdType._Table.equalsIgnoreCase( type )
					|| ReportIdType._Chart.equalsIgnoreCase( type )
					|| ReportIdType._Extended.equalsIgnoreCase( type ) )
			{
				ReportId reportId = new ReportId( );
				reportId.setId( id.substring( 0, id.indexOf( ',' ) ) ); //$NON-NLS-1$

				if ( ReportIdType._Document.equalsIgnoreCase( type ) )
				{
					reportId.setType( ReportIdType.Document );
				}
				else if ( ReportIdType._Table.equalsIgnoreCase( type ) )
				{
					reportId.setType( ReportIdType.Table );
				}
				else if ( ReportIdType._Chart.equalsIgnoreCase( type )
						|| ReportIdType._Extended.equalsIgnoreCase( type ) )
				{
					reportId.setType( ReportIdType.Chart );
				}

				try
				{
					reportId.setRptElementId( new Long( Long.parseLong( id
							.substring( secondComma + 1 ) ) ) );
				}
				catch ( Exception e )
				{
					reportId.setRptElementId( null );
				}

				ids.add( reportId );
			}
		}

		ReportId[] reportIds = new ReportId[ids.size( )];
		for ( int i = 0; i < ids.size( ); i++ )
		{
			reportIds[i] = ( ReportId ) ids.get( i );
		}

		return reportIds;
	}

	public boolean canExecute( )
	{
		return true;
	}

	public boolean canUndo( )
	{
		return false;
	}

	public boolean canRedo( )
	{
		return false;
	}

	public void undo( )
	{
	}

	public void redo( )
	{
	}

	public boolean prepare( )
	{
		return true;
	}

	protected abstract IViewerReportService getReportService( );

	/**
	 * Get svg flag from incoming soap message.
	 * 
	 * @param params
	 * @return
	 * @throws RemoteException
	 */
	protected boolean getSVGFlag( Oprand[] params )
	{
		boolean flag = false;

		if ( params != null && params.length > 0 )
		{
			for ( int i = 0; i < params.length; i++ )
			{
				if ( IBirtConstants.OPRAND_SVG.equalsIgnoreCase( params[i]
						.getName( ) ) )
				{
					flag = "true".equalsIgnoreCase( params[i].getValue( ) ); //$NON-NLS-1$
					break;
				}
			}
		}

		return flag;
	}

}
