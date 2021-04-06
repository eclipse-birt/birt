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

package org.eclipse.birt.report.context;

/**
 * Parameter attribute bean serves as the base class of parameter group bean and
 * scalar parameter bean. It carries the common data shared between front-end
 * jsp page and back-end fragment class.
 * <p>
 */
public class ParameterAttributeBean {
	/**
	 * Parameter display name. HTML encoded.
	 */
	private String displayName = null;

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}