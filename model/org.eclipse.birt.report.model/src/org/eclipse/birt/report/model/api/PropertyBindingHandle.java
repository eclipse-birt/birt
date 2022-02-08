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

import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;

/**
 * Represents the handle of property binding structure. The property binding
 * defines the overridable property value. It includes property name, element ID
 * and overridden value.
 */

public class PropertyBindingHandle extends StructureHandle {

	/**
	 * Constructs the handle of property binding.
	 * 
	 * @param valueHandle the value handle for property binding list of one property
	 * @param index       the position of this property binding in the list
	 */

	public PropertyBindingHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the overridden value of property binding.
	 * 
	 * @return the overridden value
	 */

	public String getValue() {
		return getStringProperty(PropertyBinding.VALUE_MEMBER);
	}

	/**
	 * Sets the property binding value.
	 * 
	 * @param expression the value expression to set
	 */

	public void setValue(String expression) {
		setPropertySilently(PropertyBinding.VALUE_MEMBER, expression);
	}
}
