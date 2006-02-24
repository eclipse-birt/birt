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
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.BaseFragment;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Fragment that handle PFE related tasks.
 * <p>
 * 
 * @see FramesetFragment
 */
public class EngineFragment extends BaseFragment
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
		if( "/download".equalsIgnoreCase( request.getServletPath( ) ) ) //$NON-NLS-1$
		{
			response.setContentType( "application/csv;charset=utf-8" ); //$NON-NLS-1$
			response.setHeader ("Content-Disposition","inline; filename=exportdata.csv"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if ( ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( ParameterAccessor.getFormat( request ) ) )
		{
			response.setContentType( "application/pdf" ); //$NON-NLS-1$
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
	protected void doService( HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) request.getAttribute( "attributeBean" ); //$NON-NLS-1$
		assert attrBean != null;
		
		ServletOutputStream out = response.getOutputStream( );
		
		try
		{
			if( "/download".equalsIgnoreCase( request.getServletPath( ) ) ) //$NON-NLS-1$
			{
				ReportEngineService.getInstance( ).extractData( attrBean.getReportDocumentInstance( ),
						ParameterAccessor.getResultSetName( request ), ParameterAccessor.getSelectedColumns( request ),
						attrBean.getLocale( ), out );
			}
			else if ( ParameterAccessor.isGetImageOperator( request ) )
			{
				response.setContentType( "image" ); //$NON-NLS-1$
				String imageId = request.getParameter( ParameterAccessor.PARAM_IMAGEID );
				
				ReportEngineService.getInstance( ).renderImage( imageId, out );
			}
			else
			{
				ReportEngineService.getInstance( ).runAndRenderReport( request, attrBean.getReportRunnable( ), out,
						ParameterAccessor.getFormat( request ), attrBean.getLocale( ), attrBean.getParameters( ),
						attrBean.isMasterPageContent( ), ParameterAccessor.getSVGFlag( request ) );
			}
		}
		catch ( RemoteException e )
		{
			AxisFault fault = ( AxisFault ) e;
			// Special handle since servlet output stream has been
			// retrieved.
			// Any include and forward throws exception.
			// Better to move this error handle into engine.
			String message = "<html><body><font color=\"red\">" //$NON-NLS-1$ 
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
}