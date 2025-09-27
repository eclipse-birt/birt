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
import java.util.Iterator;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.axis.transport.http.AxisServlet;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

abstract public class BirtSoapMessageDispatcherServlet extends AxisServlet {

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
	protected IFragment viewer = null;
	protected IFragment run = null;

	/**
	 * Abstract methods.
	 */
	abstract protected void __init(ServletConfig config);

	abstract protected boolean __authenticate(HttpServletRequest request, HttpServletResponse response);

	abstract protected IContext __getContext(HttpServletRequest request, HttpServletResponse response)
			throws BirtException;

	abstract protected void __doGet(IContext context) throws ServletException, IOException, BirtException;

	abstract protected void __doPost(IContext context) throws ServletException, IOException, BirtException;

	abstract protected void __handleNonSoapException(HttpServletRequest request, HttpServletResponse response,
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
		// Workaround for using axis bundle
		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

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
		// TODO: since eclipse Jetty doesn't support filter, set it here for
		// workaround
		if (req.getCharacterEncoding() == null) {
			req.setCharacterEncoding(IBirtConstants.DEFAULT_ENCODE);
		}

		// workaround for Jetty
		req.setAttribute("ServletPath", ((HttpServletRequest) req).getServletPath()); //$NON-NLS-1$

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
			// create new session
			IViewingSession session = ViewingSessionUtil.createSession(request);
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
		if (!__authenticate(request, response)) {
			return;
		}

		// create SOAP URL with post parameters
		StringBuilder builder = new StringBuilder();
		Iterator it = request.getParameterMap().keySet().iterator();
		while (it.hasNext()) {
			String paramName = (String) it.next();
			if (paramName != null && paramName.startsWith("__")) //$NON-NLS-1$
			{
				String paramValue = ParameterAccessor.urlEncode(ParameterAccessor.getParameter(request, paramName),
						ParameterAccessor.UTF_8_ENCODE);
				builder.append("&" + paramName + "=" + paramValue); //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		String soapURL = request.getRequestURL().toString();
		if (ParameterAccessor.getBaseURL() != null) {
			soapURL = ParameterAccessor.getBaseURL() + request.getContextPath() + request.getServletPath();
		}

		builder.deleteCharAt(0);
		soapURL += "?" + builder.toString(); //$NON-NLS-1$

		request.setAttribute("SoapURL", soapURL); //$NON-NLS-1$

		String requestType = request.getHeader(ParameterAccessor.HEADER_REQUEST_TYPE);
		boolean isSoapRequest = ParameterAccessor.HEADER_REQUEST_TYPE_SOAP.equalsIgnoreCase(requestType);
		// refresh the current BIRT viewing session by accessing it
		IViewingSession session;

		// init context
		IContext context = null;
		try {
			session = ViewingSessionUtil.getSession(request);
			if (session == null && !isSoapRequest) {
				if (ViewingSessionUtil.getSessionId(request) == null) {
					session = ViewingSessionUtil.createSession(request);
				} else {
					// if session id passed through the URL, it means this request
					// was expected to run using a session that has already expired
					throw new ViewerException(
							BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
				}
			}
			context = __getContext(request, response);
		} catch (BirtException e) {
			// throw exception
			__handleNonSoapException(request, response, e);
			return;
		}

		try {
			if (session != null) {
				session.lock();
			}
			__doPost(context);

			if (isSoapRequest) {
				// Workaround for using axis bundle to invoke SOAP request
				Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

				super.doPost(request, response);
			} else {
				try {
					if (context.getBean().getException() != null) {
						__handleNonSoapException(request, response, context.getBean().getException());
					} else {
						__doGet(context);
					}
				} catch (BirtException e) {
					__handleNonSoapException(request, response, e);
				}
			}
		} catch (BirtException e) {
			e.printStackTrace();
		} finally {
			if (session != null && !session.isExpired()) {
				session.unlock();
			}
		}
	}
}
