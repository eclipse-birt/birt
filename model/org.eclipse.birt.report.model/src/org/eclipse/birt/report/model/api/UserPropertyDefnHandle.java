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

import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.command.UserPropertyCommand;

/**
 * Represents a top-level user-defined property of an element.
 * 
 * @see org.eclipse.birt.report.model.api.core.UserPropertyDefn
 */

public class UserPropertyDefnHandle extends ElementDetailHandle {

	/**
	 * The user property definition of the handle. It must not be null.
	 */

	private UserPropertyDefn propDefn = null;

	/**
	 * Constructs a handle for the user-defined property with the given element
	 * handle and the user-defined property.
	 * 
	 * @param element a handle to a report element
	 * @param prop    The definition of the user-defined property.
	 */

	public UserPropertyDefnHandle(DesignElementHandle element, UserPropertyDefn prop) {
		super(element);
		this.propDefn = prop;
		assert prop != null;
	}

	/**
	 * Constructs a handle for the user-defined property with the given element
	 * handle and the name of the user-defined property.
	 * 
	 * @param element  a handle to a report element
	 * @param propName The name of the user-defined property.
	 */

	public UserPropertyDefnHandle(DesignElementHandle element, String propName) {
		super(element);
		propDefn = element.getElement().getUserPropertyDefn(propName);
		if (propDefn == null)
			throw new IllegalArgumentException("The user property \"" + propName + "\" does not exsit!"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Returns the name of the user-defined property.
	 * 
	 * @return the name of the user-defined property
	 */

	public String getName() {
		return propDefn.getName();
	}

	/**
	 * Returns the type of the user-defined property.
	 * 
	 * @return the type of the user-defined property
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType
	 */

	public int getType() {
		return propDefn.getTypeCode();
	}

	/**
	 * Returns the display name of the user-defined property.
	 * 
	 * @return the display name of the user-defined property
	 */

	public String getDisplayName() {
		return propDefn.getDisplayName();
	}

	/**
	 * Sets the definition for the user-defined property.
	 * 
	 * @param prop the new definition of the user-defined property
	 * @throws UserPropertyException  if the property is not found, is not a user
	 *                                property, or is not defined on this element,
	 *                                or the user property definition is
	 *                                inconsistent.
	 * @throws PropertyValueException if the type changes, the value becomes
	 *                                invalid.
	 */

	public void setUserPropertyDefn(UserPropertyDefn prop) throws UserPropertyException, PropertyValueException {
		UserPropertyCommand cmd = new UserPropertyCommand(elementHandle.getModule(), getElement());
		cmd.setPropertyDefn(propDefn, prop);
	}

	/**
	 * Returns the copy of the property definition for this user-defined property.
	 * 
	 * @return the copy of the property definition
	 */

	public UserPropertyDefn getCopy() {
		UserPropertyDefn prop = (UserPropertyDefn) (propDefn).copy();
		return prop;
	}

	/**
	 * Gets the user-defined property of this handle.
	 * 
	 * @return the user-defined property of this handle
	 */

	public UserPropertyDefn getDefn() {
		return this.propDefn;
	}
}
