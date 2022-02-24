/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;

/**
 * Represents the handle of configuration variable. A configuration variable is
 * simply a name/value pair very similar to an environment variable on Unix.
 * Indeed, configuration variables include environment variables, along with
 * other BIRT-specific values.
 */

public class ConfigVariableHandle extends StructureHandle {

	/**
	 * Constructs the handle of configuration variable.
	 * 
	 * @param valueHandle the value handle for configuration variable list of one
	 *                    property
	 * @param index       the position of this configuration variable in the list
	 */

	public ConfigVariableHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the variable name.
	 * 
	 * @return the variable name
	 */

	public String getName() {
		return getStringProperty(ConfigVariable.NAME_MEMBER);
	}

	/**
	 * Sets the variable name.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		setPropertySilently(ConfigVariable.NAME_MEMBER, name);
	}

	/**
	 * Returns the variable value.
	 * 
	 * @return the variable value
	 */

	public String getValue() {
		return getStringProperty(ConfigVariable.VALUE_MEMBER);
	}

	/**
	 * Sets the variable value.
	 * 
	 * @param value the value to set
	 */

	public void setValue(String value) {
		setPropertySilently(ConfigVariable.VALUE_MEMBER, value);
	}
}
