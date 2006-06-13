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

package org.eclipse.birt.report.presentation.aggregation.layout;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.service.actionhandler.BirtExtractDataActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetReportletActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderImageActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderReportActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunAndRenderActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment that handle PFE related tasks.
 * <p>
 * 
 * @see FramesetFragment
 */
public class EngineFragment extends BirtBaseFragment
{

	/**
	 * Anything before do service.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void doPreService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		if ( IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase( request
				.getServletPath( ) ) )
		{
			response.setContentType( "application/csv;charset=utf-8" ); //$NON-NLS-1$
			response.setHeader(
					"Content-Disposition", "inline; filename=exportdata.csv" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if ( ParameterAccessor.PARAM_FORMAT_PDF
				.equalsIgnoreCase( ParameterAccessor.getFormat( request ) ) )
		{
			response.setContentType( "application/pdf" ); //$NON-NLS-1$
			String filename = "BIRTReport" + System.currentTimeMillis( ); //$NON-NLS-1$
			response
					.setHeader(
							"Content-Disposition", "inline; filename=" + filename + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		else
		{
			response.setContentType( "text/html;charset=utf-8" ); //$NON-NLS-1$
			response.setHeader( "cache-control", "no-cache" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Render the report in html/pdf format by calling engine service.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void doService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		ServletOutputStream out = response.getOutputStream( );
		GetUpdatedObjectsResponse upResponse = new GetUpdatedObjectsResponse( );
		IContext context = new BirtContext( request, response );
		Operation op = null;
		try
		{
			if ( IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase( request
					.getServletPath( ) ) )
			{
				BirtExtractDataActionHandler extractDataHandler = new BirtExtractDataActionHandler(
						context, op, upResponse );
				extractDataHandler.execute( );
			}
			else if ( ParameterAccessor.isGetImageOperator( request ) )
			{
				BirtRenderImageActionHandler renderImageHandler = new BirtRenderImageActionHandler(
						context, op, upResponse );
				renderImageHandler.execute( );
			}
			else if ( ParameterAccessor.isGetReportlet( request ) )
			{
				BirtGetReportletActionHandler getReportletHandler = new BirtGetReportletActionHandler(
						context, op, upResponse );
				getReportletHandler.execute( );
			}
			else if ( context.getBean( ).documentInUrl )
			{
				BirtRenderReportActionHandler runReportHandler = new BirtRenderReportActionHandler(
						context, op, upResponse, out );
				runReportHandler.execute( );
			}
			else
			{
				BirtRunAndRenderActionHandler runAndRenderHandler = new BirtRunAndRenderActionHandler(
						context, op, upResponse );
				runAndRenderHandler.execute( );
			}
		}
		catch ( RemoteException e )
		{
			AxisFault fault = (AxisFault) e;
			// Special handle since servlet output stream has been
			// retrieved.
			// Any include and forward throws exception.
			// Better to move this error handle into engine.
			String message = "<html><head><title>" + BirtResources.getString( "birt.viewer.title.error" ) + "</title><body><font color=\"red\">" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
					+ ParameterAccessor.htmlEncode( fault.getFaultString( ) )
					+ "</font></body></html>"; //$NON-NLS-1$
			out.write( message.getBytes( ) );
		}
	}

	/**
	 * Override implementation of doPostService.
	 */
	protected String doPostService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException
	{
		return null;
	}

	/**
	 * Override build method.
	 */
	protected void build( )
	{
		addChild( new SidebarFragment( ) );
		addChild( new DocumentFragment( ) );
	}
}