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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo implements IArgumentInfo {

	private final IArgumentInfo arguInfo;

	public ArgumentInfo() {
		arguInfo = new org.eclipse.birt.report.model.metadata.ArgumentInfo();
	}

	@Override
	public String getName() {
		return arguInfo.getName();
	}

	/**
	 * Returns the display name for the property if the resource key of display name
	 * is defined. Otherwise, return empty string.
	 *
	 * @return the user-visible, localized display name for the property
	 */

	@Override
	public String getDisplayName() {
		String retValue = arguInfo.getDisplayName();
		return retValue != null ? retValue : ""; //$NON-NLS-1$
	}

	/**
	 * Sets the internal name of the property.
	 *
	 * @param theName the internal property name
	 */

	protected void setName(String theName) {
		((org.eclipse.birt.report.model.metadata.ArgumentInfo) arguInfo).setName(theName);
	}

	/**
	 * Returns the resource key for the display name.
	 *
	 * @return The display name message ID.
	 */

	@Override
	public String getDisplayNameKey() {
		String retValue = arguInfo.getDisplayNameKey();
		return retValue != null ? retValue : ""; //$NON-NLS-1$
	}

	/**
	 * Sets the message ID for the display name.
	 *
	 * @param id message ID for the display name
	 */

	protected void setDisplayNameKey(String id) {
		((org.eclipse.birt.report.model.metadata.ArgumentInfo) arguInfo).setDisplayNameKey(id);
	}

	/**
	 * Returns the script type of this argument.
	 *
	 * @return the script type to set
	 */

	@Override
	public String getType() {
		return arguInfo.getType();
	}

	/**
	 * Returns the class type of this argument.
	 *
	 * @return the class type to set
	 */

	@Override
	public IClassInfo getClassType() {
		return null;
	}

	/**
	 * Sets the script type of this argument.
	 *
	 * @param type the script type to set
	 */

	protected void setType(String type) {
		((org.eclipse.birt.report.model.metadata.ArgumentInfo) arguInfo).setType(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		if (!StringUtil.isBlank(getName())) {
			return getName();
		}
		return super.toString();
	}
}
