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

package org.eclipse.birt.report.viewer.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * Mock a HttpServletRequest class for Viewer UnitTest
 *
 */
public class HttpServletRequestSimulator implements HttpServletRequest {

	private ServletContext context;
	private HttpSession session;

	private Hashtable parameters;
	private Hashtable attributes;
	private Hashtable headers;
	private List cookies;

	private String scheme = "http"; //$NON-NLS-1$
	private String protocol = "HTTP/1.1"; //$NON-NLS-1$
	private String serverName = "localhost"; //$NON-NLS-1$
	private int serverPort = 80;
	private String remoteUser;
	private String userRole;
	private String remoteAddr;
	private String remoteHost;
	private int remotePort;
	private String requestURI;
	private String requestURL;
	private String localAddr;
	private String localName;
	private int localPort;
	private String authType;
	private Principal userPrincipal;
	private String contextPath;
	private String servletPath;
	private String method = REQUEST_METHOD_GET;
	private String pathInfo;
	private String queryString;
	private String characterEncoding;
	private String contentType;
	private int contentLength;
	private Locale locale;

	public static final String REQUEST_METHOD_GET = "GET"; //$NON-NLS-1$
	public static final String REQUEST_METHOD_POST = "POST"; //$NON-NLS-1$

	/**
	 * Constructor
	 *
	 */
	public HttpServletRequestSimulator() {
		parameters = new Hashtable();
		attributes = new Hashtable();
		headers = new Hashtable();
		cookies = new ArrayList();
	}

	/**
	 * Constructor
	 *
	 * @param context
	 */
	public HttpServletRequestSimulator(ServletContext context) {
		this();
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		if (name == null) {
			return null;
		}

		Object value = parameters.get(name);
		if (value == null) {
			return null;
		}

		if (value.getClass().isArray()) {
			return ((String[]) value)[0];
		}

		return (String) value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	@Override
	public Map getParameterMap() {
		return this.parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	@Override
	public Enumeration getParameterNames() {
		return this.parameters.keys();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getParameterValues( java.lang.String )
	 */
	@Override
	public String[] getParameterValues(String name) {
		if (name == null) {
			return null;
		}

		Object value = parameters.get(name);
		if (value == null) {
			return null;
		}

		if (value.getClass().isArray()) {
			return (String[]) value;
		} else {
			return new String[] { (String) value };
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setAttribute(String key, Object value) {
		if (key != null) {
			this.attributes.put(key, value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	@Override
	public Enumeration getAttributeNames() {
		return this.attributes.keys();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return (String) this.headers.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	@Override
	public Enumeration getHeaderNames() {
		return this.headers.keys();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	@Override
	public Enumeration getHeaders(String name) {
		throw new UnsupportedOperationException("Do not support getHeaders operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	@Override
	public int getIntHeader(String name) {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	@Override
	public long getDateHeader(String name) {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	@Override
	public Cookie[] getCookies() {
		if (cookies == null || cookies.isEmpty()) {
			return null;
		}

		Cookie[] array = new Cookie[cookies.size()];
		return (Cookie[]) cookies.toArray(array);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	@Override
	public String getScheme() {
		return this.scheme;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return this.protocol;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setCharacterEncoding(String encoding) {
		this.characterEncoding = encoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	@Override
	public int getContentLength() {
		return this.contentLength;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	@Override
	public String getAuthType() {
		return this.authType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	@Override
	public String getContextPath() {
		return this.contextPath;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	@Override
	public String getMethod() {
		return this.method;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	@Override
	public String getPathInfo() {
		return this.pathInfo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException("Do not support getPathTranslated operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return this.queryString;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	@Override
	public String getRemoteUser() {
		return this.remoteUser;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	@Override
	public String getRequestURI() {
		return this.requestURI;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer(this.requestURL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	@Override
	public String getRequestedSessionId() {
		if (this.session != null) {
			return this.session.getId();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	@Override
	public String getServletPath() {
		return this.servletPath;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	@Override
	public HttpSession getSession() {
		return this.session;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getSession( boolean )
	 */
	@Override
	public HttpSession getSession(boolean flag) {
		boolean isValid = true;
		if (this.session != null) {
			isValid = ((HttpSessionSimulator) this.session).isValid();
		}

		if (flag && this.session == null) {
			this.session = new HttpSessionSimulator(this.context);
		} else if (flag && !isValid) {
			this.session = new HttpSessionSimulator(this.context);
		}

		if (isValid) {
			this.session = null;
		}

		return this.session;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal
	 */
	@Override
	public Principal getUserPrincipal() {
		return this.userPrincipal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie
	 */
	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL
	 */
	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl
	 */
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return isRequestedSessionIdFromURL();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid
	 */
	@Override
	public boolean isRequestedSessionIdValid() {
		if (this.session == null) {
			return false;
		}

		return ((HttpSessionSimulator) this.session).isValid();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole( java.lang.String )
	 */
	@Override
	public boolean isUserInRole(String role) {
		if (role == null) {
			return false;
		}

		return role.equals(userRole);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getInputStream
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("Do not support getInputStream operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getLocalAddr
	 */
	@Override
	public String getLocalAddr() {
		return this.localAddr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getLocalName
	 */
	@Override
	public String getLocalName() {
		return this.localName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getLocalPort
	 */
	@Override
	public int getLocalPort() {
		return this.localPort;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getLocale
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getLocales
	 */
	@Override
	public Enumeration getLocales() {
		throw new UnsupportedOperationException("Do not support getLocales operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getReader
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException("Do not support getReader operation!"); //$NON-NLS-1$
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRealPath( java.lang.String )
	 * @deprecated
	 */
	@Deprecated
	@Override
	public String getRealPath(String path) {
		if (path == null) {
			return null;
		}

		return this.context.getRealPath(path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRemoteAddr
	 */
	@Override
	public String getRemoteAddr() {
		return this.remoteAddr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRemoteHost
	 */
	@Override
	public String getRemoteHost() {
		return this.remoteHost;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRemotePort
	 */
	@Override
	public int getRemotePort() {
		return this.remotePort;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getRequestDispatcher(
	 * java.ang.String )
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String url) {
		return this.context.getRequestDispatcher(url);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getServerName
	 */
	@Override
	public String getServerName() {
		return this.serverName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#getServerPort
	 */
	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServletRequest#isSecure
	 */
	@Override
	public boolean isSecure() {
		return scheme.equalsIgnoreCase("HTTPS"); //$NON-NLS-1$
	}

	/**
	 * Add parameter into Request object
	 *
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value) {
		if (key != null) {
			this.parameters.put(key, value);
		}
	}

	/**
	 * Add parameter array into Request object
	 *
	 * @param key
	 * @param values
	 */
	public void addParameterValues(String key, String[] values) {
		if (key != null) {
			this.parameters.put(key, values);
		}
	}

	/**
	 * Remove parameter in Request object
	 *
	 * @param key
	 */
	public void removeParameter(String key) {
		this.parameters.remove(key);
	}

	/**
	 * Set Request Header
	 *
	 * @param key
	 * @param header
	 */
	public void addHeader(String key, String header) {
		this.headers.put(key, header);
	}

	/**
	 * Remove header in Request object
	 *
	 * @param key
	 */
	public void removeHeader(String key) {
		this.headers.remove(key);
	}

	/**
	 * Add Cookie
	 *
	 * @param cookie
	 */
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	/**
	 * Set Cookies
	 *
	 * @param cookies
	 */
	public void setCookie(Cookie[] cookies) {
		if (cookies == null) {
			return;
		}

		this.cookies = new ArrayList();
		for (int i = 0; i < cookies.length; i++) {
			this.cookies.add(cookies[i]);
		}
	}

	/**
	 * Set Scheme
	 *
	 * @param scheme
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * Set Content Type
	 *
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Set Authorize Type
	 *
	 * @param authType
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * Set Context Path
	 *
	 * @param contextPath
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Set Request Method
	 *
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Set Path Information
	 *
	 * @param pathInfo
	 */
	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	/**
	 * Set Request Query String
	 *
	 * @param queryString
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Set Remote User
	 *
	 * @param remoteUser
	 */
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	/**
	 * Set request Server Port
	 *
	 * @param port
	 */
	public void setServerPort(int port) {
		this.serverPort = port;
	}

	/**
	 * Set request Server Name
	 *
	 * @param serverName
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Set request URI
	 *
	 * @param requestURI
	 */
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	/**
	 * Set request URL
	 *
	 * @param url
	 */
	public void setRequestURL(String url) {
		if (url == null) {
			return;
		}

		// Set Scheme
		int posScheme = url.indexOf("://"); //$NON-NLS-1$
		setScheme(url.substring(0, posScheme));

		// Set Request URL
		int pos = url.indexOf("?"); //$NON-NLS-1$
		if (pos <= 0) {
			pos = url.length();
		}
		this.requestURL = url.substring(0, pos);

		// Set Query String
		if (pos != url.length()) {
			setQueryString(url.substring(pos + 1));
		}

		// Set Request URI
		int posURI = url.indexOf("/", posScheme + 3); //$NON-NLS-1$
		setRequestURI(url.substring(posURI, pos));

		// Set Server Name and Server Port
		int posPort = url.indexOf(":", posScheme + 3); //$NON-NLS-1$
		if (posPort > 0) {
			setServerName(url.substring(posScheme + 3, posPort));
			String port = url.substring(posPort + 1, posURI);
			if (port.length() > 0) {
				setServerPort(Integer.parseInt(port));
			}
		} else {
			setServerName(url.substring(posScheme + 3, posURI));
			if (isSecure()) {
				setServerPort(443);
			} else {
				setServerPort(80);
			}
		}
	}

	/**
	 * Set Request Servlet Path
	 *
	 * @param servletPath
	 */
	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	/**
	 * @param localAddr the localAddr to set
	 */
	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	/**
	 * @param localName the localName to set
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	/**
	 * @param localPort the localPort to set
	 */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	/**
	 * @param userPrincipal the userPrincipal to set
	 */
	public void setUserPrincipal(Principal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @param remoteHost the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @param remotePort the remotePort to set
	 */
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> arg0) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}
}
