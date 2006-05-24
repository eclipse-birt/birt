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

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.control.ToolbarFragment;
import org.eclipse.birt.report.service.actionhandler.BirtRenderReportActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunReportActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Root fragment for web viewer composite.
 * <p>
 * 
 * @see BaseFragment
 */
public class FramesetFragment extends BirtBaseFragment
{

	/**
	 * Override build method.
	 */
	protected void build( )
	{
		addChild( new ToolbarFragment( ) );
		addChild( new ReportFragment( ) );
	}

	/**
	 * Service provided by the fragment. This is the entry point of engine
	 * framgent. It generally includes a JSP page to render a certain part of
	 * web viewer.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */
	public void service( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException,
			IOException, BirtException
	{
		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		if ( attrBean != null
				&& !attrBean.isMissingParameter( )
				&& ParameterAccessor.PARAM_FORMAT_PDF
						.equalsIgnoreCase( attrBean.getFormat( ) ) )
		{
			this.doPreService( request, response );
			this.doService( request, response );
			this.doPostService( request, response );
		}
		else
		{
			super.doPreService( request, response );
			super.doService( request, response );
			String target = super.doPostService( request, response );

			if ( target != null && target.length( ) > 0 )
			{
				RequestDispatcher rd = request.getRequestDispatcher( target );
				rd.include( request, response );
			}
		}
	}

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
		assert ParameterAccessor.PARAM_FORMAT_PDF
				.equalsIgnoreCase( ParameterAccessor.getFormat( request ) );
		response.setContentType( "application/pdf" ); //$NON-NLS-1$
	}

	/**
	 * Render the report in html/pdf format by calling frameset service.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void doService( HttpServletRequest request,
			HttpServletResponse response ) throws ServletException,
			IOException, BirtException
	{
		assert ParameterAccessor.PARAM_FORMAT_PDF
				.equalsIgnoreCase( ParameterAccessor.getFormat( request ) );

		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		assert attrBean != null;

		ServletOutputStream out = response.getOutputStream( );
		IContext context = new BirtContext( request, response );
		GetUpdatedObjectsResponse upResponse = new GetUpdatedObjectsResponse( );
		Operation op = null;
		File file = new File( attrBean.getReportDocumentName( ) );
		if ( !file.exists( ) )
		{
			BirtRunReportActionHandler runReport = new BirtRunReportActionHandler(
					context, op, upResponse );
			runReport.execute( );
		}
		try
		{
			BirtRenderReportActionHandler renderReport = new BirtRenderReportActionHandler(
					context, op, upResponse, out );
			renderReport.execute( );
		}
		catch ( RemoteException e )
		{
			AxisFault fault = (AxisFault) e;
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
		assert ParameterAccessor.PARAM_FORMAT_PDF
				.equalsIgnoreCase( ParameterAccessor.getFormat( request ) );
		return null;
	}
}
