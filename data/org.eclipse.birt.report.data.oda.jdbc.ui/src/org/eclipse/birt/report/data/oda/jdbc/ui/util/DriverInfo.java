
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.Serializable;

/**
 * Driver information including driver name ,display name ,URL template
 */
public class DriverInfo implements Serializable {
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -6628864223068044321L;

	/**
	 * Diver Name
	 */
	private String driverName;

	/**
	 * User set display name
	 */
	private String displayName;

	/**
	 * URL template eg. jdbc:odbc:test
	 */
	private String urlTemplate;

	/**
	 * Constractor
	 *
	 * @param driverName
	 * @param displayName
	 * @param urlTemplate
	 */
	public DriverInfo(String driverName, String displayName, String urlTemplate) {
		this.driverName = driverName;
		if (displayName == null) {
			this.displayName = "";
		} else {
			this.displayName = displayName;
		}
		if (urlTemplate == null) {
			this.urlTemplate = "";
		} else {
			this.urlTemplate = urlTemplate;
		}
	}

	/**
	 * getter for displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * setter for displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * getter for urlTemplate
	 */
	public String getUrlTemplate() {
		return urlTemplate;
	}

	/**
	 * setter for urlTemplate
	 */
	public void setUrlTemplate(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	/**
	 * getter for driverName
	 */
	public String getDriverName() {
		return driverName;
	}
}
