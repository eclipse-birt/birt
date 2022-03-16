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

package org.eclipse.birt.integration.wtp.ui.internal.webapplication;

/**
 * Bean defined for Filter-Mapping object in web.xml
 *
 */
public class FilterMappingBean {

	/**
	 * Filter name
	 */
	private String name;

	/**
	 * servlet name
	 */
	private String servletName;

	/**
	 * url pattern
	 */
	private String uri;

	/**
	 * default constructor
	 */
	public FilterMappingBean() {
	}

	/**
	 * constructor with Filter name and servlet name
	 *
	 * @param name
	 * @param servletName
	 */
	public FilterMappingBean(String name, String servletName) {
		this.name = name;
		this.servletName = servletName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the servletName
	 */
	public String getServletName() {
		return servletName;
	}

	/**
	 * @param servletName the servletName to set
	 */
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
