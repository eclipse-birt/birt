/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.mock;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.descriptor.JspConfigDescriptor;

/**
 * Mock a ServletContext class for Viewer UnitTest
 *
 */
public class ServletContextSimulator implements ServletContext {

	/**
	 * Init parameters of Servlet Context
	 */
	private Hashtable initParameters;

	/**
	 * Attributes of Servlet Context
	 */
	private Hashtable attributes;

	/**
	 * The defined root context directory
	 */
	private File contextDir;

	/**
	 * Request Dispatcher Object
	 */
	private RequestDispatcher dispatcher;

	/**
	 * Constructor
	 *
	 */
	public ServletContextSimulator() {
		this.initParameters = new Hashtable();
		this.attributes = new Hashtable();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getAttributeNames()
	 */
	@Override
	public Enumeration getAttributeNames() {
		return this.attributes.keys();
	}

	@Override
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getContext(java.lang.String)
	 */
	@Override
	public ServletContext getContext(String uri) {
		throw new UnsupportedOperationException("Do not support getContext operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String name) {
		if (name == null) {
			return null;
		}

		Object param = this.initParameters.get(name);
		if (param != null) {
			return (String) param;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getInitParameterNames()
	 */
	@Override
	public Enumeration getInitParameterNames() {
		return this.initParameters.keys();
	}

	/**
	 * Set init parameter
	 *
	 * @param name
	 * @param value
	 */
	@Override
	public boolean setInitParameter(String name, String value) {
		this.initParameters.put(name, value);
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getMajorVersion()
	 */
	@Override
	public int getMajorVersion() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	@Override
	public String getMimeType(String arg0) {
		throw new UnsupportedOperationException("Do not support getMimeType operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getMinorVersion()
	 */
	@Override
	public int getMinorVersion() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	@Override
	public RequestDispatcher getNamedDispatcher(String uri) {
		throw new UnsupportedOperationException("Do not support getNamedDispatcher operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	@Override
	public String getRealPath(String path) {
		if (contextDir == null || path == null) {
			return null;
		}

		return new File(contextDir, path).getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String uri) {
		return this.dispatcher;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getResource(java.lang.String)
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		throw new UnsupportedOperationException("Do not support getResource operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(String arg0) {
		throw new UnsupportedOperationException("Do not support getResourceAsStream operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	@Override
	public Set getResourcePaths(String arg0) {
		throw new UnsupportedOperationException("Do not support getResourcePaths operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getServerInfo()
	 */
	@Override
	public String getServerInfo() {
		return "BirtMockServletEngine"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getServlet(java.lang.String)
	 */
	@Override
	public Servlet getServlet(String name) throws ServletException {
		throw new UnsupportedOperationException("Do not support getServlet operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getServletContextName()
	 */
	@Override
	public String getServletContextName() {
		throw new UnsupportedOperationException("Do not support getServletContextName operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getServletNames()
	 */
	@Override
	public Enumeration getServletNames() {
		throw new UnsupportedOperationException("Do not support getServletNames operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getServlets()
	 */
	@Override
	public Enumeration getServlets() {
		throw new UnsupportedOperationException("Do not support getServlets operation!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#log(java.lang.String)
	 */
	@Override
	public void log(String content) {
		System.out.println(content);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
	 */
	@Override
	public void log(Exception exception, String content) {
		System.out.println(content + "--" + exception.getMessage()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(String content, Throwable throwable) {
		System.out.println(content + "--" + throwable.getMessage()); //$NON-NLS-1$
	}

	/**
	 * @return the contextDir
	 */
	public File getContextDir() {
		return contextDir;
	}

	/**
	 * @param contextDir the contextDir to set
	 */
	public void setContextDir(File contextDir) {
		this.contextDir = contextDir;
	}

	/**
	 * @return the dispatcher
	 */
	public RequestDispatcher getRequestDispatcher() {
		return dispatcher;
	}

	/**
	 * @param dispatcher the dispatcher to set
	 */
	public void setDispatcher(RequestDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * Pass a Servlet object to create RequestDispatcher
	 *
	 * @param servlet
	 */
	public void setDispatcher(Servlet servlet) {
		this.dispatcher = new RequestDispatcherSimulator(servlet);
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEffectiveMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEffectiveMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Dynamic addServlet(String servletName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName, Servlet servlet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(String className) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends EventListener> void addListener(T t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void declareRoles(String... roleNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVirtualServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addJspFile(String servletName, String jspFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSessionTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSessionTimeout(int sessionTimeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRequestCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRequestCharacterEncoding(String encoding) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getResponseCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResponseCharacterEncoding(String encoding) {
		// TODO Auto-generated method stub

	}
}
