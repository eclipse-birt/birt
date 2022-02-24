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

package org.eclipse.birt.report.model.api.scripts;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo implements IArgumentInfo {

	private Class clazz;

	/**
	 * Constructor.
	 * 
	 * @param argumentType the argument type.
	 */

	protected ArgumentInfo(Class argumentType) {
		this.clazz = argumentType;
	}

	/**
	 * Returns the arguement type in class.
	 * 
	 * @return the arguement type in class
	 */

	protected Class getArgumentClass() {
		return clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IArgumentInfo#getName()
	 */

	public String getName() {
		return StringUtil.EMPTY_STRING;
	}

	/**
	 * Returns the display name for the property if the resource key of display name
	 * is defined. Otherwise, return empty string.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the resource key for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameKey() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the script type of this argument.
	 * 
	 * @return the script type to set
	 */

	public String getType() {
		return clazz.getName();
	}

	/**
	 * Returns the class type of this argument.
	 * 
	 * @return the class type to set
	 */

	public IClassInfo getClassType() {
		return new ClassInfo(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}
}
