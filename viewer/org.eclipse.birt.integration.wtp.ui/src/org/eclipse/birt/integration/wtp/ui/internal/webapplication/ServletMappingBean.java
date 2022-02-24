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
 * Bean defined for Servlet-Mapping object in web.xml
 * 
 */
public class ServletMappingBean {

	/**
	 * servlet name
	 */
	private String name;

	/**
	 * url pattern
	 */
	private String uri;

	/**
	 * default constructor
	 */
	public ServletMappingBean() {
	}

	/**
	 * constructor with servlet name and url pattern
	 * 
	 * @param name
	 * @param uri
	 */
	public ServletMappingBean(String name, String uri) {
		this.name = name;
		this.uri = uri;
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
