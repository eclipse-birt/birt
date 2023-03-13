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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;

/**
 * Represents the handle of property mask structure. The property mask defines
 * whether this property can be accessed or modified. It includes property name
 * and mask value.
 */

public class PropertyMaskHandle extends StructureHandle {

	/**
	 * Constructs the handle of property mask.
	 *
	 * @param valueHandle the value handle for property mask list of one property
	 * @param index       the position of this property mask in the list
	 */

	public PropertyMaskHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the property mask. The possible values are defined in
	 * {org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they are:
	 * <ul>
	 * <li>PROPERTY_MASK_TYPE_CHANGE
	 * <li>PROPERTY_MASK_TYPE_LOCK
	 * <li>PROPERTY_MASK_TYPE_HIDE
	 * </ul>
	 *
	 * @return the property mask
	 */

	public String getMask() {
		return getStringProperty(PropertyMask.MASK_MEMBER);
	}

	/**
	 * Sets the property mask. The allowed values are defined in
	 * {org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they are:
	 * <ul>
	 * <li>PROPERTY_MASK_TYPE_CHANGE
	 * <li>PROPERTY_MASK_TYPE_LOCK
	 * <li>PROPERTY_MASK_TYPE_HIDE
	 * </ul>
	 *
	 * @param mask the mask to set
	 * @throws SemanticException if the mask is not in the choice list.
	 */

	public void setMask(String mask) throws SemanticException {
		setProperty(PropertyMask.MASK_MEMBER, mask);
	}

	/**
	 * Returns the property name.
	 *
	 * @return the property name
	 */

	public String getName() {
		return getStringProperty(PropertyMask.NAME_MEMBER);
	}

	/**
	 * Sets the property name.
	 *
	 * @param name the property name to set
	 * @throws SemanticException value required exception
	 */

	public void setName(String name) throws SemanticException {
		setProperty(PropertyMask.NAME_MEMBER, name);
	}
}
