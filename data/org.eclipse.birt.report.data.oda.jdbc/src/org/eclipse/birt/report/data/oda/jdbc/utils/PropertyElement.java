/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.utils;

import java.util.Properties;

public class PropertyElement {

	private Properties properties;

	public PropertyElement() {
		properties = new Properties();
	}

	public String getAttribute(String name) {
		return properties.getProperty(name);
	}

	public void setAttribute(String name, String value) {
		if (value != null) {
			properties.put(name, value);
		}
	}
}
