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
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.FramesetFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Servlet implementation of BIRT Web Viewer.
 * <p>
 * There are four servlet mappings defined for ViewerServlet in the web.xml.
 * <ul>
 * <li>Frameset - Displays the whole web viewer frameset. (Public)</li>
 * <li>Run - Runs the report and displays the output as a stand-alone HTML
 * page, or as a PDF document. (Public)</li>
 * <li>Navigation - Displays the leftside navigation frame that contains the
 * report parameter page. (Internal)</li>
 * <li>Toolbar - Displays the toolbar above the report content. (Internal)</li>
 * </ul>
 * <p>
 * Each public mapping expects some URL parameters,
 * <ul>
 * <li>Frameset
 * <ul>
 * <li>__report</li>
 * <li>__locale</li>
 * <li><i>report parameter</i></li>
 * </ul>
 * <li>Run
 * <ul>
 * <li>__report</li>
 * <li>__format</li>
 * <li>__locale</li>
 * <li>__page</li>
 * <li><i>report parameter</i></li>
 * </ul>
 * </ul>
 * <p>
 * Each URL parameter is described below. <table border=1>
 * <tr>
 * <td><b>Parameter</b></td>
 * <td><b>Description</b></td>
 * <td><b>Values</b></td>
 * <td><b>Default</b></td>
 * </tr>
 * <tr>
 * <td>__report</td>
 * <td>The path to the report document</td>
 * <td>&nbsp;</td>
 * <td>required</td>
 * </tr>
 * <tr>
 * <td>__format</td>
 * <td>The output format</td>
 * <td>html or pdf</td>
 * <td>optional, default to html</td>
 * </tr>
 * <tr>
 * <td>__locale</td>
 * <td>Report locale</td>
 * <td>Java locale value such as en or ch-zh.</td>
 * <td>optional, default to JVM locale</td>
 * </tr>
 * <tr>
 * <td>__page</td>
 * <td>Report page number</td>
 * <td>Report page to be viewed.</td>
 * <td>optional, default to 0</td>
 * </tr>
 * <tr>
 * <td><i>reportParam</i></td>
 * <td>User defined report parameter.</td>
 * <td>As specified in the report design.</td>
 * <td>As specified in the report design.</td>
 * </tr>
 * </table>
 * <p>
 */
public class ViewerServlet extends BirtSoapMessageDispatcherServlet
{

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Viewer fragment references.
	 */

	private IFragment viewer = null;
	private IFragment preview = null;
	private IFragment engine = null;
	private IFragment parameter = null;

	/**
	 * Servlet path for parameter model.
	 */
	public static final String SERVLET_PATH_PARAMETER = "/parameter"; //$NON-NLS-1$

	/**
	 * Servlet path for preview model.
	 */
	public static final String SERVLET_PATH_PREVIEW = "/preview"; //$NON-NLS-1$

	/**
	 * Servlet path for frameset model.
	 */
	public static final String SERVLET_PATH_FRAMESET = "/frameset"; //$NON-NLS-1$

	/**
	 * Servlet path for running model.
	 */
	public static final String SERVLET_PATH_RUN = "/run"; //$NON-NLS-1$

	/**
	 * The error page.
	 */
	private static final String EORROR_PAGE = "pages/common/Error.jsp";

	/**
	 * Servlet initialization. Initialize engine, parameter, resources, and
	 * viewer fragment. Birt viewer uses a composite of fragments to control the
	 * layout renderring. The Composite is initialized here and will be shared
	 * by all incoming servlet requests. Four fragment references are stored as
	 * properties of servlet instance. Each fragments is read only.
	 * 
	 * @see org.eclipse.birt.report.viewer.aggregation.BaseFragment
	 * @param config
	 *            servlet configuration
	 */

	public void init( ServletConfig config ) throws ServletException
	{
		super.init( config );

		ParameterAccessor.initParameters( config );
		BirtResources.initResource( ParameterAccessor.getWebAppLocale( ) );

		viewer = FramesetFragment.getFramesetFragment( );
		preview = FramesetFragment.getPreviewFragment( );
		engine = FramesetFragment.getEngineFragment( );
		parameter = FramesetFragment.getParameterFragment( );
	}

	/**
	 * Process http request with GET method
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */

	public void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		ReportEngineService.getInstance( ).setEngineContext(
				getServletContext( ) );

		// Exception happened during context init.
		BirtContext context = new BirtContext( request );
		if ( context.getBean( ).getException( ) != null )
		{
			context.finalize( );
			displayException( request, response, context.getBean( )
					.getException( ) );
		}
		else
		{
			try
			{
				if ( SERVLET_PATH_FRAMESET.equalsIgnoreCase( request
						.getServletPath( ) ) )
				{
					viewer.service( request, response );
				}
				else if ( SERVLET_PATH_PREVIEW.equalsIgnoreCase( request
						.getServletPath( ) ) )
				{
					preview.service( request, response );
				}
				else if ( SERVLET_PATH_RUN.equalsIgnoreCase( request
						.getServletPath( ) ) )
				{
					engine.service( request, response );
				}
				else if ( SERVLET_PATH_PARAMETER.equalsIgnoreCase( request
						.getServletPath( ) ) )
				{
					parameter.service( request, response );
				}
			}
			catch ( BirtException e )
			{
				displayException( request, response, e );
			}
			finally
			{
				context.finalize( );
			}
		}

	}

	/**
	 * Process http request with POST method. Four different servlet paths are
	 * expected: "/frameset", "/navigation", "/toolbar", and "/run".
	 * 
	 * @param request
	 *            incoming http request
	 * @param response
	 *            http response
	 * @exception ServletException
	 * @exception IOException
	 */

	public void doPost( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String requestType = request
				.getHeader( ParameterAccessor.HEADER_REQUEST_TYPE );

		if ( ParameterAccessor.HEADER_REQUEST_TYPE_SOAP
				.equalsIgnoreCase( requestType ) )
		{
			ReportEngineService.getInstance( ).setEngineContext(
					getServletContext( ) );
			BirtContext context = new BirtContext( request );

			if ( "/download".equalsIgnoreCase( request.getServletPath( ) ) ) //$NON-NLS-1$
			{
				if ( context.getBean( ).getException( ) != null )
				{
					context.finalize( );
					displayException( request, response, context.getBean( )
							.getException( ) );
				}
				else
				{
					try
					{
						engine.service( request, response );
					}
					catch ( BirtException e )
					{
						displayException( request, response, e );
					}
					finally
					{
						context.finalize( );
					}
				}
			}
			else
			{
				super.doPost( request, response );
				context.finalize( );
			}
		}
		else
		{
			doGet( request, response );
		}

	}

	/**
	 * Display exception.
	 * 
	 * @param request
	 *            the request.
	 * @param response
	 *            the response.
	 * @param exception
	 *            the exception to display.
	 * @throws ServletException
	 * @throws IOException
	 */

	private void displayException( HttpServletRequest request,
			HttpServletResponse response, Exception exception )
			throws ServletException, IOException
	{
		String target = EORROR_PAGE;
		request.setAttribute( "error", exception ); //$NON-NLS-1$
		RequestDispatcher rd = request.getRequestDispatcher( target );
		rd.include( request, response );
	}

}