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

package org.eclipse.birt.report.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * Filter class for birt viewer. It is according to Servlet 2.3 specification.
 * Filter http request and set character encoding to UTF-8
 *
 */
public class ViewerFilter implements Filter {

	// default encoding
	protected String encoding = "UTF-8";//$NON-NLS-1$

	// filter config
	protected FilterConfig filterConfig = null;

	/**
	 * Default constructor
	 */
	public ViewerFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.Filter#doFilter(jakarta.servlet.ServletRequest,
	 * jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request.getCharacterEncoding() == null && encoding != null) {
			request.setCharacterEncoding(encoding);
		}
		// for >= 9.3.x jetty needs this property to change request encoding,
		// this might change for future versions
		request.setAttribute("org.eclipse.jetty.server.Request.queryEncoding", encoding);
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.Filter#init(jakarta.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
