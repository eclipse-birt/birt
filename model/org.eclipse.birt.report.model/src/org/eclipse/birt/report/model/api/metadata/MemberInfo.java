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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.api.metadata.IMemberInfo;

/**
 * Represents the definition of class member. The class member defines the
 * member type besides name, display name ID and tool tip ID.
 */

public class MemberInfo implements IMemberInfo {

	private String toolTip;

	private String displayName;

	private final IMemberInfo memberInfo;

	/**
	 * Default constructor.
	 */
	public MemberInfo() {
		memberInfo = new org.eclipse.birt.report.model.metadata.MemberInfo();
	}

	public String getDataType() {
		return memberInfo.getDataType();
	}

	/**
	 * Sets the script data type of this member.
	 * 
	 * @param type the script data type to set
	 */

	protected void setDataType(String type) {
		((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).setDataType(type);
	}

	/**
	 * Sets whether this member is static.
	 * 
	 * @param isStatic the flag set
	 */

	protected void setStatic(boolean isStatic) {
		((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).setStatic(isStatic);
	}

	/**
	 * Returns whether this member is static.
	 * 
	 * @return <code>true</code> if this member is true.
	 */

	public boolean isStatic() {
		return memberInfo.isStatic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Sets the display string for the tool tip of this method.
	 * 
	 * @param toolTip the user-visible, localized display name for the tool tip of
	 *                this method.
	 */

	protected void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getToolTipKey ()
	 */
	public String getToolTipKey() {
		return memberInfo.getToolTipKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * 
	 * @param displayName the display value
	 */

	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayNameKey
	 * ()
	 */

	public String getDisplayNameKey() {
		String retValue = memberInfo.getDisplayNameKey();
		return retValue != null ? retValue : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	public String getName() {
		return memberInfo.getName();
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param displayNameKey the resource key to set
	 */

	protected void setDisplayNameKey(String displayNameKey) {
		((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).setDisplayNameKey(displayNameKey);
	}

	/**
	 * Sets the definition name.
	 * 
	 * @param name the name to set
	 */

	protected void setName(String name) {
		((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).setName(name);
	}

	/**
	 * Sets the resource key for tool tip.
	 * 
	 * @param toolTipKey the resource key to set
	 */

	protected void setToolTipKey(String toolTipKey) {
		((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).setToolTipKey(toolTipKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMemberInfo#getClassType()
	 */
	public IClassInfo getClassType() {
		return ((org.eclipse.birt.report.model.metadata.MemberInfo) memberInfo).getClassType();
	}
}