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

import java.util.List;

public class PropertyGroup {
	private String name;
	private String description;
	private List propertyElemList;

	public PropertyGroup(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public List<PropertyElement> getProperties() {
		return this.propertyElemList;
	}

	public void setProperties(List<PropertyElement> properties) {
		this.propertyElemList = properties;
	}
}
