/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.webapplication;

/**
 * Bean defined for Context-Param object in web.xml
 * 
 */
public class ContextParamBean {

	/**
	 * context-param name
	 */
	private String name;

	/**
	 * context-param value
	 */
	private String value;

	/**
	 * context-param description
	 */
	private String description;

	/**
	 * default constructor
	 */
	public ContextParamBean() {
	}

	/**
	 * constructor with name and value
	 * 
	 * @param name
	 * @param value
	 */
	public ContextParamBean(String name, String value) {
		this.name = name;
		this.value = value;
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
