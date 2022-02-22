/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.build;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.DataType;

/**
 * Represent a project, include related infomation of this project
 *
 */
public class ProjectInfo extends DataType {
	/**
	 * property array
	 */
	protected ArrayList properties = new ArrayList();

	/**
	 * Map for property in RunTime
	 */
	protected HashMap map = null;

	/**
	 * add property, this action occurs at parser time
	 *
	 * @param property
	 */
	public void addProperty(Property property) {
		properties.add(property);
	}

	/**
	 * get value by key, this action occurs at run time
	 *
	 * @param key
	 * @return the value
	 */
	public String getValue(String key) {
		// first time to call this method, init the map
		if (map == null) {
			map = new HashMap();
			for (int i = 0; i < properties.size(); i++) {
				Property pro = (Property) properties.get(i);
				map.put(pro.getName(), pro.getValue());
			}
		}
		return map.get(key).toString();
	}
}
