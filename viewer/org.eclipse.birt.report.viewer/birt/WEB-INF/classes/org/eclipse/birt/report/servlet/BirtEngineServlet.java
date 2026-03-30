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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.presentation.aggregation.layout.EngineFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RequesterFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class BirtEngineServlet extends BaseReportEngineServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Local initialization.
	 *
	 * @return
	 */
	@Override
	public void __init(ServletConfig config) {
		BirtReportServiceFactory.init(new BirtViewerReportService(config.getServletContext()));

		engine = new EngineFragment();

		requester = new RequesterFragment();
		requester.buildComposite();
		requester.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$
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
}
