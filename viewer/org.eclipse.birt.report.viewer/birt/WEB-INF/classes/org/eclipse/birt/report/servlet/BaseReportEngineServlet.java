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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.transport.http.AxisServlet;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.utility.ParameterAccessor;

abstract public class BaseReportEngineServlet extends AxisServlet
{

	/**
	 * TODO: what's this?
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Versioning.
	 */
	protected static boolean openSource = true;

	/**
	 * Viewer fragment references.
	 */
	protected IFragment engine = null;

	protected IFragment requester = null;

	/**
	 * Abstract methods.
	 */
	abstract protected void __init( ServletConfig config );

	abstract protected boolean __authenticate( HttpServletRequest request,
			HttpServletResponse response );

	abstract protected IContext __getContext( HttpServletRequest request,
			HttpServletResponse response ) throws BirtException;

	abstract protected void __doGet( IContext context )
			throws ServletException, IOException, BirtException;

	abstract protected void __handleNonSoapException(
			HttpServletRequest request, HttpServletResponse response,
			Exception exception ) throws ServletException, IOException;

	/**
	 * Check version.
	 * 
	 * @return
	 */
	public static boolean isOpenSource( )
	{
		return openSource;
	}

	/**
	 * Servlet init.
	 * 
	 * @param config
	 * @exception ServletException
	 * @return
	 */
	public void init( ServletConfig config ) throws ServletException
	{
		// Workaround for using axis bundle
		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
		
		super.init( config );
		ParameterAccessor.initParameters( config );
		BirtResources.setLocale( ParameterAccessor.getWebAppLocale( ) );
		__init( config );
	}

	/**
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	public void service( ServletRequest req, ServletResponse res )
			throws ServletException, IOException
	{
		// TODO: since eclipse Jetty doesn't support filter, set it here for
		// workaround
		if ( req.getCharacterEncoding( ) == null )
			req.setCharacterEncoding( IBirtConstants.DEFAULT_ENCODE );

		super.service( req, res );
	}

	/**
	 * Handle HTTP GET method.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		if ( !__authenticate( request, response ) )
		{
			return;
		}

		try
		{

			IContext context = __getContext( request, response );

			if ( context.getBean( ).getException( ) != null )
			{
				__handleNonSoapException( request, response, context.getBean( )
						.getException( ) );
			}
			else
			{
				String requestType = request
						.getHeader( ParameterAccessor.HEADER_REQUEST_TYPE );
				if ( ParameterAccessor.HEADER_REQUEST_TYPE_SOAP
						.equalsIgnoreCase( requestType ) )
				{
					// Workaround for using axis bundle to invoke SOAP request
					Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
					
					super.doPost( request, response );
				}
				else
				{

					__doGet( context );
				}
			}

		}
		catch ( BirtException e )
		{
			__handleNonSoapException( request, response, e );
		}

	}

	/**
	 * Handle HTTP POST method.
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		doGet( request, response );
	}
}
