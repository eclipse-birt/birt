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
