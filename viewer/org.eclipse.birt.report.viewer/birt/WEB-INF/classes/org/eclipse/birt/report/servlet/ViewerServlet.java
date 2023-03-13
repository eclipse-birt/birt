/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.FramesetFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RunFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;
import org.eclipse.birt.report.utility.BirtUtility;

/**
 * Servlet implementation of BIRT Web Viewer.
 * <p>
 * There are four servlet mappings defined for ViewerServlet in the web.xml.
 * <ul>
 * <li>Frameset - Displays the whole web viewer frameset. (Public)</li>
 * <li>Run - Runs the report and displays the output as a stand-alone HTML page,
 * or as a PDF document. (Public)</li>
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
 * Each URL parameter is described below.
 * <table border=1>
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
public class ViewerServlet extends BirtSoapMessageDispatcherServlet {

	/**
	 * TODO: what's this?
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Local initialization.
	 *
	 * @return
	 */
	@Override
	protected void __init(ServletConfig config) {
		BirtReportServiceFactory.init(new BirtViewerReportService(config.getServletContext()));

		// handle 'frameset' pattern
		viewer = new FramesetFragment();
		viewer.buildComposite();
		viewer.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$

		// handle 'run' pattern
		run = new RunFragment();
		run.buildComposite();
		run.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$
	}

	/**
	 * Init context.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception BirtException
	 * @return IContext
	 */
	@Override
	protected IContext __getContext(HttpServletRequest request, HttpServletResponse response) throws BirtException {
		BirtReportServiceFactory.getReportService().setContext(getServletContext(), null);
		return new BirtContext(request, response);
	}

	/**
	 * Local process http request with GET method.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	@Override
	protected void __doGet(IContext context) throws ServletException, IOException, BirtException {
		IFragment activeFragment = null;
		String servletPath = context.getRequest().getServletPath();
		if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)) {
			activeFragment = viewer;
		} else if (IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(servletPath)) {
			activeFragment = run;
		}

		if (activeFragment != null) {
			activeFragment.service(context.getRequest(), context.getResponse());
		}
	}

	/**
	 * Locale process http request with POST method. Four different servlet paths
	 * are expected: "/frameset", "/navigation", "/toolbar", and "/run".
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	@Override
	protected void __doPost(IContext context) throws ServletException, IOException, BirtException {
	}

	/**
	 * Local authentication. Alwasy returns true.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @return
	 */
	@Override
	protected boolean __authenticate(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	/**
	 * Process exception for non soap request.
	 *
	 * @param request   incoming http request
	 * @param response  http response
	 * @param exception
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void __handleNonSoapException(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws ServletException, IOException {
		exception.printStackTrace();
		BirtUtility.appendErrorMessage(response.getOutputStream(), exception);
	}
}
