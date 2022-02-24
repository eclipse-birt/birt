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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the method information for both class and element. The class
 * includes the argument list, return type, and whether this method is static or
 * constructor,
 */

public class MethodInfo implements IMethodInfo {

	/**
	 * 
	 */

	private List arguments;

	private final Method method;

	/**
	 * @param method
	 */

	protected MethodInfo(Method method) {
		this.method = method;

		addArgumentList(method.getParameterTypes());
	}

	/**
	 * Returns the internal Java Method instance.
	 * 
	 * @return the internal Java Method instance
	 */

	protected Method getMethod() {
		return method;
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>ArgumentInfoList</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator argumentListIterator() {
		if (arguments == null)
			return Collections.EMPTY_LIST.iterator();

		return arguments.iterator();
	}

	/**
	 * Returns the resource key for tool tip.
	 * 
	 * @return the resource key for tool tip
	 */

	public String getToolTipKey() {
		return StringUtil.EMPTY_STRING;
	}

	/**
	 * Returns the display string for the tool tip of this method.
	 * 
	 * @return the user-visible, localized display name for the tool tip of this
	 *         method.
	 */

	public String getToolTip() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName()
	 */

	public String getDisplayName() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayNameKey
	 * ()
	 */

	public String getDisplayNameKey() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	public String getName() {
		return method.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getReturnType()
	 */

	public String getReturnType() {
		return method.getReturnType().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#isConstructor()
	 */

	public boolean isConstructor() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#isStatic()
	 */

	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

	public IClassInfo getClassReturnType() {
		return new ClassInfo(method.getReturnType());
	}

	/**
	 * Adds an optional argument list to the method information.
	 * 
	 * @param argumentList an optional argument list
	 * 
	 */

	void addArgumentList(Class[] argumentList) {
		if (arguments == null)
			arguments = new ArrayList();

		ArgumentInfoList argumentInfoList = new ArgumentInfoList(argumentList);
		arguments.add(argumentInfoList);
	}

}
