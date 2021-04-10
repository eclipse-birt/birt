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

	public boolean isStatic() {
		return isStatic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMemberInfo#getClassType()
	 */
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