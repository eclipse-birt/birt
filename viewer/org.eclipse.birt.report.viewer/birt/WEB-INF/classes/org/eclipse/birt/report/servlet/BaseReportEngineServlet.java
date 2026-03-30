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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjects;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.endpoint.BirtSoapBindingImpl;
import org.eclipse.birt.report.soapengine.endpoint.BirtSoapException;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

	protected abstract void __doGet(IContext context) throws ServletException, IOException, BirtException;

	/**
	 * Check version.
	 *
	 * @return
	 */
	public static boolean isOpenSource() {
		return openSource;
	}

	/**
	 * Init context.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception BirtException
	 * @return IContext
	 */
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
	protected boolean __authenticate(HttpServletRequest request, HttpServletResponse response) {
		return true;
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
	 * Process exception for non soap request.
	 *
	 * @param request   incoming http request
	 * @param response  http response
	 * @param exception
	 * @throws ServletException
	 * @throws IOException
	 */
	public void __handleNonSoapException(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws ServletException, IOException {
		exception.printStackTrace();
		response.setContentType("text/html; charset=utf-8"); //$NON-NLS-1$
		BirtUtility.appendErrorMessage(response.getOutputStream(), exception);
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
		for (String paramName : request.getParameterMap().keySet()) {
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
				soapService(request, response);
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
			__handleNonSoapException(request, response, e);
		} finally {
			if (session != null && !session.isExpired()) {
				session.unlock();
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void soapService(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			GetUpdatedObjects requestObj = BirtSoapParser.parseGetUpdatedObjects(request);

			BirtSoapBindingImpl binding = new BirtSoapBindingImpl();
			GetUpdatedObjectsResponse responseObj = binding.getUpdatedObjects(requestObj);
			BirtSoapMarshaller.marshalResponse(responseObj, response);
		} catch (BirtSoapException e) {
			writeSoapFault(e.getFaultCode(), e.getMessage(), e, response);
		} catch (Exception e) {
			writeSoapFault("Soap error", e.getMessage(), e, response);
		}
	}

	/**
	 * Function for possible extensions
	 *
	 * @param context
	 */
	public void __doPost(IContext context) throws BirtException {
	}

	/**
	 * Function can be replaced by apache common-text or google guava, but no one is
	 * as dependency of BIRT Just now this looks like easiest way to do what we
	 * need.
	 *
	 * @param s
	 * @return
	 */
	private static String escape(String s) {
		if (s == null) {
			return "";
		}

		StringBuilder out = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '&' -> out.append("&amp;");
			case '<' -> out.append("&lt;");
			case '>' -> out.append("&gt;");
			case '"' -> out.append("&quot;");
			case '\'' -> out.append("&apos;");
			default -> out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * @param faultCode
	 * @param faultMessage
	 * @param exception
	 * @param resp
	 * @throws IOException
	 */
	public void writeSoapFault(String faultCode, String faultMessage, Throwable exception, HttpServletResponse resp)
			throws IOException {

		resp.reset();
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		resp.setContentType("text/xml;charset=UTF-8");

		StringBuilder detailXml = new StringBuilder();
		if (exception != null) {
			// Separate stack trace to "chunks" by exceptions
			Throwable current = exception;
			// log into server log
			current.printStackTrace();
			while (current != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				current.printStackTrace(pw);
				pw.flush();
				detailXml.append("<string>").append(escape(sw.toString())).append("</string>");
				pw.close();
				current = current.getCause();
			}
		}

		String faultXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<soapenv:Body>"
				+ "<soapenv:Fault>" + "<faultcode>" + escape(faultCode) + "</faultcode>" + "<faultstring>"
				+ escape(faultMessage) + "</faultstring>"
				+ (detailXml.length() > 0 ? "<detail>" + detailXml + "</detail>" : "") + "</soapenv:Fault>"
				+ "</soapenv:Body>" + "</soapenv:Envelope>";

		byte[] bytes = faultXml.getBytes(StandardCharsets.UTF_8);
		resp.setContentLength(bytes.length);

		try (ServletOutputStream out = resp.getOutputStream()) {
			out.write(bytes);
		}
	}
}
