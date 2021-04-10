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

import java.util.Iterator;

/**
 * Represents the method information for both class and element. The class
 * includes the argument list, return type, and whether this method is static or
 * constructor,
 */

public class MethodInfo implements IMethodInfo {

	private final IMethodInfo methodInfo;

	private String toolTip;

	private String displayName;

	/**
	 * 
	 * @param isConstructor
	 */
	public MethodInfo(boolean isConstructor) {
		methodInfo = new org.eclipse.birt.report.model.metadata.MethodInfo(isConstructor);
	}

	/**
	 * Adds an optional argument list to the method information.
	 * 
	 * @param argumentList an optional argument list
	 * 
	 */

	protected void addArgumentList(IArgumentInfoList argumentList) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).addArgumentList(argumentList);

	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>ArgumentInfoList</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator<IArgumentInfoList> argumentListIterator() {
		return methodInfo.argumentListIterator();
	}

	/**
	 * Returns the resource key for tool tip.
	 * 
	 * @return the resource key for tool tip
	 */

	public String getToolTipKey() {
		return methodInfo.getToolTipKey();
	}

	/**
	 * Sets the resource key for tool tip.
	 * 
	 * @param toolTipKey the resource key to set
	 */

	public void setToolTipKey(String toolTipKey) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setToolTipKey(toolTipKey);
	}

	/**
	 * Returns the display string for the tool tip of this method.
	 * 
	 * @return the user-visible, localized display name for the tool tip of this
	 *         method.
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

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
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
		String retValue = methodInfo.getDisplayNameKey();
		return retValue != null ? retValue : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	public String getName() {
		return methodInfo.getName();
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param displayNameKey the resource key to set
	 */

	protected void setDisplayNameKey(String displayNameKey) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setDisplayNameKey(displayNameKey);
	}

	/**
	 * Sets the definition name.
	 * 
	 * @param name the name to set
	 */

	protected void setName(String name) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc() {
		return methodInfo.getJavaDoc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getReturnType()
	 */

	public String getReturnType() {
		return methodInfo.getReturnType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#isConstructor()
	 */
	public boolean isConstructor() {
		return methodInfo.isConstructor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#isStatic()
	 */

	public boolean isStatic() {
		return methodInfo.isStatic();
	}

	/**
	 * @param javaDoc
	 * 
	 */
	public void setJavaDoc(String javaDoc) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setJavaDoc(javaDoc);
	}

	/**
	 * 
	 * @param returnType
	 */
	protected void setReturnType(String returnType) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setReturnType(returnType);
	}

	/**
	 * 
	 * @param isStatic
	 */
	protected void setStatic(boolean isStatic) {
		((org.eclipse.birt.report.model.metadata.MethodInfo) methodInfo).setStatic(isStatic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMethodInfo#getClassReturnType ()
	 */

	public IClassInfo getClassReturnType() {
		return null;
	}
}