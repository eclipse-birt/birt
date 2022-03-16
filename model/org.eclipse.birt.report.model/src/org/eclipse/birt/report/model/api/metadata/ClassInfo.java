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

import java.util.List;

import org.eclipse.birt.report.model.metadata.MetaDataException;

/**
 * Represents the script object definition. This definition defines one
 * constructor, several members and methods. It also includes the name, display
 * name ID, and tool tip ID.
 */

public class ClassInfo implements IClassInfo {

	private final IClassInfo classInfo;
	private String toolTip;

	public ClassInfo() {
		classInfo = new org.eclipse.birt.report.model.metadata.ClassInfo();
	}

	/**
	 * Adds one method definition to this class definition.
	 *
	 * @param methodInfo the definition of the method to add
	 * @throws MetaDataException if the duplicate method name exists.
	 */

	protected void addMethod(IMethodInfo methodInfo) {
		try {
			((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).addMethod(methodInfo);
		} catch (MetaDataException e) {

		}
	}

	/**
	 * Adds one member definition to this class definition.
	 *
	 * @param memberDefn the definition of the member to add
	 * @throws MetaDataException if the duplicate member name exists.
	 */

	protected void addMember(IMemberInfo memberDefn) {
		try {
			((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).addMemberDefn(memberDefn);
		} catch (MetaDataException e) {

		}
	}

	/**
	 * Returns the method definition list. For methods that have the same name, only
	 * return one method.
	 *
	 * @return a list of method definitions
	 */

	@Override
	public List<IMethodInfo> getMethods() {
		return classInfo.getMethods();
	}

	/**
	 * Get the method definition given the method name.
	 *
	 * @param name the name of the method to get
	 * @return the definition of the method to get
	 */

	@Override
	public IMethodInfo getMethod(String name) {
		return classInfo.getMethod(name);
	}

	/**
	 * Returns the list of member definitions.
	 *
	 * @return the list of member definitions
	 */

	@Override
	public List<IMemberInfo> getMembers() {
		return classInfo.getMembers();
	}

	/**
	 * Returns the member definition given method name.
	 *
	 * @param name name of the member to get
	 * @return the member definition to get
	 */

	@Override
	public IMemberInfo getMember(String name) {
		return classInfo.getMember(name);
	}

	/**
	 * Returns the constructor definition.
	 *
	 * @return the constructor definition
	 */

	@Override
	public IMethodInfo getConstructor() {
		return classInfo.getConstructor();
	}

	/**
	 * Adds constructor since some class has more than one constructor with
	 * different arguments.
	 *
	 * @param constructor the constructor definition to add
	 * @throws MetaDataException if the constructor's name is empty.
	 */

	protected void setConstructor(IMethodInfo constructor) {
		try {
			((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).setConstructor(constructor);
		} catch (MetaDataException e) {

		}
	}

	/**
	 * Returns whether a class object is native.
	 *
	 * @return <code>true</code> if an object of this class is native, otherwise
	 *         <code>false</code>
	 */

	@Override
	public boolean isNative() {
		return classInfo.isNative();
	}

	/**
	 * Sets the native attribute of this class.
	 *
	 * @param isNative <code>Boolean.TRUE</code> if an object of this class is
	 *                 native, otherwise <code>Boolean.FALSE</code>
	 */

	protected void setNative(boolean isNative) {
		((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).setNative(isNative);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayNameKey
	 * ()
	 */

	@Override
	public String getDisplayNameKey() {
		String retValue = classInfo.getDisplayNameKey();
		return retValue != null ? retValue : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	@Override
	public String getName() {
		return classInfo.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getToolTipKey ()
	 */
	@Override
	public String getToolTipKey() {
		return classInfo.getToolTipKey();
	}

	/**
	 * Sets the resource key for display name.
	 *
	 * @param displayNameKey the resource key to set
	 */

	protected void setDisplayNameKey(String displayNameKey) {
		((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).setDisplayNameKey(displayNameKey);
	}

	/**
	 * Sets the definition name.
	 *
	 * @param name the name to set
	 */

	protected void setName(String name) {
		((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).setName(name);
	}

	/**
	 * Sets the resource key for tool tip.
	 *
	 * @param toolTipKey the resource key to set
	 */

	protected void setToolTipKey(String toolTipKey) {
		((org.eclipse.birt.report.model.metadata.ClassInfo) classInfo).setToolTipKey(toolTipKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	@Override
	public String getDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	@Override
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
}
