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
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Jakarta-compatible BaseReportEngineServlet (Axis-free).
 */
public abstract class BaseReportEngineServlet extends HttpServlet {

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

	protected abstract void __init(ServletConfig config);

	protected abstract boolean __authenticate(HttpServletRequest request, HttpServletResponse response);

	protected abstract IContext __getContext(HttpServletRequest request, HttpServletResponse response)
			throws BirtException;

	protected abstract void __doGet(IContext context) throws ServletException, IOException, BirtException;

	protected abstract void __handleNonSoapException(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws ServletException, IOException;

	/**
	 * Check version.
	 *
	 * @return
	 */
	public static boolean isOpenSource() {
		return openSource;
	}

	/**
	 * Servlet init.
	 *
	 * @param config
	 * @exception ServletException
	 * @return
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ParameterAccessor.initParameters(config);
		BirtResources.setLocale(ParameterAccessor.getWebAppLocale());
		__init(config);
	}


	/**
	 * @see jakarta.servlet.http.HttpServlet#service(jakarta.servlet.ServletRequest,
	 *      jakarta.servlet.ServletResponse)
	 */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		if (req.getCharacterEncoding() == null) {
			req.setCharacterEncoding(IBirtConstants.DEFAULT_ENCODE);
		}
		req.setAttribute("ServletPath", ((HttpServletRequest) req).getServletPath());
		super.service(req, res);
	}

	/**
	 * Handle HTTP GET method.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!__authenticate(request, response)) {
			return;
		}

		try {
			IViewingSession session = ViewingSessionUtil.getSession(request);
			if (session == null) {
				session = ViewingSessionUtil.createSession(request);
			}
			session.lock();
			try {
				IContext context = __getContext(request, response);
				if (context.getBean().getException() != null) {
					__handleNonSoapException(request, response, context.getBean().getException());
				} else {
					__doGet(context);
				}
			} finally {
				session.unlock();
				if (!session.isLocked() && !__getContext(request, response).getBean().isShowParameterPage()) {
					session.invalidate();
				}
			}
		} catch (BirtException e) {
			__handleNonSoapException(request, response, e);
		}
	}

	/**
	 * Handle HTTP POST method.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 * @return
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
