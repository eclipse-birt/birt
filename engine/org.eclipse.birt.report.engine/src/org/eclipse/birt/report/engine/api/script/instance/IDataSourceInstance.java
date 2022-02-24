/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
