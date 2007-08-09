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
import java.io.File;
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
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.actionhandler.BirtExtractDataActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtGetReportletActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderImageActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderReportActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunAndRenderActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunReportActionHandler;
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
		else if ( IBirtConstants.SERVLET_PATH_DOCUMENT
				.equalsIgnoreCase( request.getServletPath( ) ) )
		{
			// generate document file from report design file.
			BaseAttributeBean attrBean = (BaseAttributeBean) request
					.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
			String docFile = attrBean.getReportDocumentName( );
			if ( docFile == null || docFile.length( ) <= 0 )
			{
				String fileName = ParameterAccessor
						.generateFileNameWithoutExtension( attrBean
								.getReportDesignName( ) )
						+ "." + IBirtConstants.SUFFIX_DESIGN_DOCUMENT; //$NON-NLS-1$
				// output rptdocument file
				response.setContentType( "application/octet-stream" ); //$NON-NLS-1$
				response
						.setHeader(
								"Content-Disposition", "attachment; filename=" + fileName ); //$NON-NLS-1$ //$NON-NLS-2$				
			}
			else
			{
				response.setContentType( "text/html; charset=utf-8" ); //$NON-NLS-1$
			}
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

			if ( !ParameterAccessor.isGetImageOperator( request ) )
			{
				String filename = ParameterAccessor.generateFileName( request,
						format );
				response
						.setHeader(
								"Content-Disposition", openType + "; filename=\"" + filename + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
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
			else if ( IBirtConstants.SERVLET_PATH_DOCUMENT
					.equalsIgnoreCase( request.getServletPath( ) ) )
			{
				String docFile = attrBean.getReportDocumentName( );
				if ( docFile == null || docFile.length( ) <= 0 )
				{
					// generate the temp document file
					docFile = ParameterAccessor.getReportDocument( request,
							"", true ); //$NON-NLS-1$
					attrBean.setReportDocumentName( docFile );
					BirtRunReportActionHandler runReport = new BirtRunReportActionHandler(
							context, op, upResponse );
					runReport.execute( );

					// output rptdocument file
					BirtUtility.outputFile( docFile, out, true );
				}
				else
				{
					BirtRunReportActionHandler runReport = new BirtRunReportActionHandler(
							context, op, upResponse );
					runReport.execute( );
					BirtUtility
							.writeMessage(
									out,
									BirtResources
											.getMessage( "birt.viewer.message.document.successful" ), //$NON-NLS-1$
									IBirtConstants.MSG_COMPLETE );
				}
			}
			else if ( ParameterAccessor.isGetImageOperator( request ) )
			{
				BirtRenderImageActionHandler renderImageHandler = new BirtRenderImageActionHandler(
						context, op, upResponse );
				renderImageHandler.execute( );
			}
			else
			{
				// if use OUTPUT pattern, it will generate document from report
				// design file
				if ( IBirtConstants.SERVLET_PATH_OUTPUT
						.equalsIgnoreCase( request.getServletPath( ) ) )
				{
					File file = new File( attrBean.getReportDocumentName( ) );
					if ( !file.exists( ) )
					{
						BirtRunReportActionHandler handler = new BirtRunReportActionHandler(
								context, op, upResponse );
						handler.execute( );
					}

					file = new File( attrBean.getReportDocumentName( ) );
					if ( !file.exists( ) )
					{
						AxisFault fault = new AxisFault( );
						fault
								.setFaultReason( BirtResources
										.getMessage( ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT ) );
						throw fault;
					}
					else
					{
						// If document isn't completed, throw Exception
						if ( attrBean.isDocumentProcessing( ) )
						{
							AxisFault fault = new AxisFault( );
							fault
									.setFaultReason( BirtResources
											.getMessage( ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_PROCESSING ) );
							throw fault;
						}
					}
					
					attrBean.setDocumentInUrl( true );
				}

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
				else if ( attrBean.isDocumentInUrl( ) )
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
					InputStream inputStream = new ByteArrayInputStream(
							( (ByteArrayOutputStream) out ).toByteArray( ) );
					BirtUtility.doPrintAction( inputStream, request, response );
				}
			}
		}
		catch ( RemoteException e )
		{
			// if get image, don't write exception into output stream.
			if ( !ParameterAccessor.isGetImageOperator( request ) )
			{
				response.setContentType( "text/html; charset=utf-8" ); //$NON-NLS-1$
				BirtUtility.appendErrorMessage( response.getOutputStream( ), e );
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