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
 * Bean defined for TagLib object in web.xml
 *
 */
public class TagLibBean {

	/**
	 * taglib uri
	 */
	private String uri;

	/**
	 * taglib location
	 */
	private String location;

	/**
	 * default constructor
	 */
	public TagLibBean() {

	}

	/**
	 * constructor with taglib-uri and tablib-location
	 *
	 * @param uri
	 * @param location
	 */
	public TagLibBean(String uri, String location) {
		this.uri = uri;
		this.location = location;
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

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
