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

package org.eclipse.birt.report.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.layout.EngineFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RequesterFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.servlet.BaseReportEngineServlet;

public class BirtEngineServlet extends BaseReportEngineServlet
{

	/**
	 * TODO: what's this?
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Local initialization.
	 * 
	 * @return
	 */
	protected void __init( ServletConfig config )
	{
		IViewerReportService instance = new BirtViewerReportService( config );
		BirtReportServiceFactory.init( instance );

		engine = new EngineFragment( );

		requester = new RequesterFragment( );
		requester.buildComposite( );
		requester.setJSPRootPath( "/webcontent/birt" ); //$NON-NLS-1$
	}

	/**
	 * Init context.
	 * 
	 * @param request
	 *            incoming http request
	 * @return
	 */
	protected IContext __getContext( HttpServletRequest request,
			HttpServletResponse response )
	{
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, request );
		BirtReportServiceFactory.getReportService( ).setContext(
				getServletContext( ), options );
		return new BirtContext( request, response );
	}

	/**
	 * Local authentication.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @return
	 */
	protected boolean __authenticate( HttpServletRequest request,
			HttpServletResponse response )
	{
		return true;
	}

	/**
	 * Local do get.
	 */
	protected void __doGet( IContext context ) throws ServletException,
			IOException, BirtException
	{
		ViewerAttributeBean bean = (ViewerAttributeBean) context.getBean( );
		assert bean != null;

		if ( IBirtConstants.SERVLET_PATH_PREVIEW.equalsIgnoreCase( context
				.getRequest( ).getServletPath( ) )
				&& ( bean.isMissingParameter( ) || bean
						.isForceParameterPrompting( ) ) )
		{
			requester.service( context.getRequest( ), context.getResponse( ) );
		}
		else if ( IBirtConstants.SERVLET_PATH_PARAMETER
				.equalsIgnoreCase( context.getRequest( ).getServletPath( ) ) )
		{
			requester.service( context.getRequest( ), context.getResponse( ) );
		}
		else
		{
			engine.service( context.getRequest( ), context.getResponse( ) );
		}
	}

	/**
	 * Process exception for non soap request.
	 * 
	 * @param context
	 * @param exception
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void __handleNonSoapException( IContext context,
			Exception exception ) throws ServletException, IOException
	{
		exception.printStackTrace( );
		String target = "webcontent/birt/pages/common/Error.jsp"; //$NON-NLS-1$
		context.getRequest( ).setAttribute( "error", exception ); //$NON-NLS-1$
		RequestDispatcher rd = context.getRequest( ).getRequestDispatcher(
				target );
		rd.include( context.getRequest( ), context.getResponse( ) );
	}
}