/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

import java.util.Map;

public interface IDataSourceInstance {
	/**
	 * Gets the name of this data source
	 */
	String getName();

	/**
	 * Gets the ID of the ODA extension which defines this type of data source
	 */
	String getExtensionID();

	/**
	 * Get the extension property value
	 * 
	 * @param name
	 * @return the extention property
	 */
	String getExtensionProperty(String name);

	/**
	 * Set the extension property value
	 * 
	 * @param name
	 * @param value
	 */
	void setExtensionProperty(String name, String value);

	/**
	 * Gets the public connection properties, in the form of a (name, value) pairs.
	 * The property name is of String type. The property value is of String type.
	 * 
	 * @return Public properties as a Map of name-value pairs. Null if none is
	 *         defined.
	 */
	Map getAllExtensionProperties();
}
