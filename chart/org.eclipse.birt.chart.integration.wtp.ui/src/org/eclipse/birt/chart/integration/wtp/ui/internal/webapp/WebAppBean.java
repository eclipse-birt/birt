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

package org.eclipse.birt.chart.integration.wtp.ui.internal.webapp;

/**
 * Bean defined for Web App object in web.xml
 * 
 */
public class WebAppBean {

	/**
	 * Web appliction description
	 */
	private String description;

	/**
	 * Constructor
	 * 
	 * @param description
	 */
	public WebAppBean() {
	}

	/**
	 * Constructor
	 * 
	 * @param description
	 */
	public WebAppBean(String description) {
		this.description = description;
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
