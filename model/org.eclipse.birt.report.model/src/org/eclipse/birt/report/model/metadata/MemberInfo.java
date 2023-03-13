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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.scripts.ScriptableClassInfo;

/**
 * Represents the definition of class member. The class member defines the
 * member type besides name, display name ID and tool tip ID.
 */

public class MemberInfo extends LocalizableInfo implements IMemberInfo {

	/**
	 * The script data type
	 */

	private String dataType;

	protected IClassInfo classType;

	/**
	 * Whether this memeber is static.
	 */

	private boolean isStatic;

	/**
	 * Returns the script data type of this member.
	 *
	 * @return the script data type of this member
	 */

	@Override
	public String getDataType() {
		return dataType;
	}

	/**
	 * Sets the script data type of this member.
	 *
	 * @param type the script data type to set
	 */

	public void setDataType(String type) {
		this.dataType = type;
	}

	/**
	 * Sets whether this member is static.
	 *
	 * @param isStatic the flag set
	 */

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * Returns whether this member is static.
	 *
	 * @return <code>true</code> if this member is true.
	 */

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IMemberInfo#getClassType()
	 */
	@Override
	public IClassInfo getClassType() {
		if (classType != null) {
			return classType;
		}
		IClassInfo tmpInfo = new ScriptableClassInfo().getClass(dataType);
		return tmpInfo;
	}

	public void setClassType(IClassInfo classType) {
		this.classType = classType;
	}

}
