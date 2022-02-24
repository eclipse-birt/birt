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
 * Bean defined for Filter object in web.xml
 * 
 */
public class FilterBean {

	/**
	 * Filter name
	 */
	private String name;

	/**
	 * Filter class name
	 */
	private String className;

	/**
	 * description for Filter
	 */
	private String description;

	/**
	 * default constructor
	 */
	public FilterBean() {
	}

	/**
	 * constructor with name and class name
	 * 
	 * @param name
	 * @param className
	 */
	public FilterBean(String name, String className) {
		this.name = name;
		this.className = className;
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
