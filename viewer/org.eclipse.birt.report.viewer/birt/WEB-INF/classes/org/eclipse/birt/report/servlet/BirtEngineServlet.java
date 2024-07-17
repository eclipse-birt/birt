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

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.layout.EngineFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RequesterFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;
import org.eclipse.birt.report.utility.BirtUtility;

public class BirtEngineServlet extends BaseReportEngineServlet {

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

		engine = new EngineFragment();

		requester = new RequesterFragment();
		requester.buildComposite();
		requester.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$
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
	 * Local authentication.
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
	 * Local do get.
	 */
	@Override
	protected void __doGet(IContext context) throws ServletException, IOException, BirtException {
		ViewerAttributeBean bean = (ViewerAttributeBean) context.getBean();
		assert bean != null;

		if ((IBirtConstants.SERVLET_PATH_PREVIEW.equalsIgnoreCase(context.getRequest().getServletPath())
				|| IBirtConstants.SERVLET_PATH_DOCUMENT.equalsIgnoreCase(context.getRequest().getServletPath())
				|| IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase(context.getRequest().getServletPath()))
				&& bean.isShowParameterPage()) {
			requester.service(context.getRequest(), context.getResponse());
		} else if (IBirtConstants.SERVLET_PATH_PARAMETER.equalsIgnoreCase(context.getRequest().getServletPath())) {
			requester.service(context.getRequest(), context.getResponse());
		} else {
			engine.service(context.getRequest(), context.getResponse());
		}
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
		response.setContentType("text/html; charset=utf-8"); //$NON-NLS-1$
		BirtUtility.appendErrorMessage(response.getOutputStream(), exception);
	}
}
