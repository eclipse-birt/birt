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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.actionhandler.BirtExtractDataActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetReportletActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderImageActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderReportActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunAndRenderActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.BirtUtility;
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
		String format = ParameterAccessor.getFormat( request );
		String openType = ParameterAccessor.getOpenType( request );
		if ( IBirtConstants.SERVLET_PATH_DOWNLOAD.equalsIgnoreCase( request
				.getServletPath( ) ) )
		{
			response.setContentType( "text/plain; charset=utf-8" ); //$NON-NLS-1$
			response
					.setHeader(
							"Content-Disposition", "attachment; filename=exportdata.csv" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			if ( ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( format ) )
			{
				response.setContentType( "application/pdf" ); //$NON-NLS-1$
			}
			else
			{
				String mimeType = ReportEngineService.getInstance( )
						.getMIMEType( format );
				if ( mimeType != null && mimeType.length( ) > 0 )
					response.setContentType( mimeType );
				else
					response.setContentType( "application/octet-stream" ); //$NON-NLS-1$
			}

			String filename = ParameterAccessor.generateFileName( request );
			response
					.setHeader(
							"Content-Disposition", openType + "; filename=\"" + filename + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );

		OutputStream out = response.getOutputStream( );
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
			else
			{
				// Print report on server
				boolean isPrint = false;
				if ( IBirtConstants.ACTION_PRINT.equalsIgnoreCase( attrBean
						.getAction( ) ) )
				{
					isPrint = true;
					out = new ByteArrayOutputStream( );
				}

				if ( ParameterAccessor.isGetReportlet( request ) )
				{
					BirtGetReportletActionHandler getReportletHandler = new BirtGetReportletActionHandler(
							context, op, upResponse, out );
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
							context, op, upResponse, out );
					runAndRenderHandler.execute( );
				}

				if ( isPrint )
				{
					InputStream inputStream = new ByteArrayInputStream( out
							.toString( ).getBytes( ) );
					BirtUtility.doPrintAction( inputStream, request, response );
				}
			}
		}
		catch ( RemoteException e )
		{
			// if get image, don't write exception into output stream.
			if ( !ParameterAccessor.isGetImageOperator( request ) )
			{
				AxisFault fault = (AxisFault) e;
				// Special handle since servlet output stream has been
				// retrieved.
				// Any include and forward throws exception.
				// Better to move this error handle into engine.
				response.setContentType( "text/html; charset=utf-8" ); //$NON-NLS-1$
				BirtUtility
						.writeMessage( response.getOutputStream( ),
								ParameterAccessor.htmlEncode( fault
										.getFaultString( ) ),
								IBirtConstants.MSG_ERROR );
			}
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
}