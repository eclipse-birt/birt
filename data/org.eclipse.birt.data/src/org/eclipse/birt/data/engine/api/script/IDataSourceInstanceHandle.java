/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import java.util.Map;

/**
 * A runtime instance of a data source. Java scripts associated with a data
 * source can use this interface to get/set data source properties.
 */
public interface IDataSourceInstanceHandle extends IJavascriptContext {
	/**
	 * Gets the name of this data source
	 */
	public abstract String getName();

	/**
	 * Gets the ID of the ODA extension which defines this type of data source
	 */
	public abstract String getExtensionID();

	/**
	 * Get the extension property value
	 * 
	 * @param name
	 * @return the extention property
	 */
	public abstract String getExtensionProperty(String name);

	/**
	 * Set the extension property value
	 * 
	 * @param name
	 * @param value
	 */
	public abstract void setExtensionProperty(String name, String value);

	/**
	 * Gets all public extension property, in the form of a (name, value) pair. The
	 * property name is of String type. The property value is of string values. The
	 * caller may modify the returned property map. The effect of such modification
	 * depends on the event handler which makes such it.
	 * 
	 * @return Public properties as a Map of name-set pairs. Null if none is
	 *         defined.
	 */
	public abstract Map getAllExtensionProperties();

}
