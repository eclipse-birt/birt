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

import java.util.Enumeration;
import java.util.Hashtable;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

/**
 * Mock a ServletConfig class for Viewer UnitTest
 *
 */
public class ServletConfigSimulator implements ServletConfig {

	private String servletName;
	private Hashtable parameters;
	private ServletContext context;

	/**
	 * Default Constructor
	 *
	 */
	public ServletConfigSimulator() {
		this.parameters = new Hashtable();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletConfig#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String name) {
		if (name == null) {
			return null;
		}

		Object param = this.parameters.get(name);
		if (param != null) {
			return (String) param;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletConfig#getInitParameterNames()
	 */
	@Override
	public Enumeration getInitParameterNames() {
		return this.parameters.keys();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletConfig#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletConfig#getServletName()
	 */
	@Override
	public String getServletName() {
		return this.servletName;
	}

	/**
	 * @param context the context to set
	 */
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * @param servletName the servletName to set
	 */
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
}
