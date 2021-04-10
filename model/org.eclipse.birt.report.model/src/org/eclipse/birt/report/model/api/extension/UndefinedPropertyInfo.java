/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.extension;

/**
 * Class that provides the information for undefined properties set in extended
 * items.
 */
public class UndefinedPropertyInfo {

	/**
	 * Name of the property.
	 */
	protected String propName = null;

	/**
	 * Value of the property.
	 */
	protected Object value = null;

	/**
	 * Extension version of the extended-item.
	 */
	protected String extensionVersion = null;

	/**
	 * Constructs the this information with property name, value and extension
	 * version.
	 * 
	 * @param name
	 * @param value
	 * @param version
	 */
	public UndefinedPropertyInfo(String name, Object value, String version) {
		this.propName = name;
		this.value = value;
		this.extensionVersion = version;
	}

	/**
	 * @return the propName
	 */
	public String getPropName() {
		return propName;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the extensionVersion
	 */
	public String getExtensionVersion() {
		return extensionVersion;
	}
}
