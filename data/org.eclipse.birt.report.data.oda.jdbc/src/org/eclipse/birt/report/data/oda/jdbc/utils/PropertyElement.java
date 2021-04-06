/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		if (value != null)
			properties.put(name, value);
	}
}
