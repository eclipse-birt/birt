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
 *     Refactored by ChatGPT - Removed SOAP dependency.
 ************************************************************************************/

package org.eclipse.birt.report.servlet;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.FramesetFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RunFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation of BIRT Web Viewer (SOAP-free version).
 */
public class ViewerServlet extends BaseReportEngineServlet {

	private static final long serialVersionUID = 1L;

	IFragment viewer;
	IFragment run;


	/**
	 * @return
	 */
	public IFragment getViewer() {
		return viewer;
	}
	
	/**
	 * Local initialization.
	 *
	 * @return
	 */
	@Override
	public void __init(ServletConfig config) {
		BirtReportServiceFactory.init(new BirtViewerReportService(config.getServletContext()));

		// initialize fragments
		viewer = new FramesetFragment();
		viewer.buildComposite();
		viewer.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$

		run = new RunFragment();
		run.buildComposite();
		run.setJSPRootPath("/webcontent/birt"); //$NON-NLS-1$
	}


	/** Local process http request with GET method.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	@Override
	public void __doGet(IContext context) throws ServletException, IOException, BirtException {
		try {
			String servletPath = context.getRequest().getServletPath();

			IFragment activeFragment = null;
			if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)) {
				activeFragment = viewer;
			} else if (IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(servletPath)) {
				activeFragment = run;
			}

			if (activeFragment != null) {
				activeFragment.service(context.getRequest(), context.getResponse());
			} else {
				context.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
						"Unknown servlet path: " + servletPath);
			}
		} catch (BirtException e) {
			__handleNonSoapException(context.getRequest(), context.getResponse(), e);
		}
	}
}
