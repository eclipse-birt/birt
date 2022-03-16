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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.scripts.ScriptableClassInfo;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Represents the method information for both class and element. The class
 * includes the argument list, return type, and whether this method is static or
 * constructor,
 */

public class MethodInfo extends LocalizableInfo implements IMethodInfo {

	/**
	 * The script type for return.
	 */

	private String returnType;

	private IClassInfo returnClassType;

	/**
	 * Whether this method is static.
	 */

	private boolean isStatic = false;

	/**
	 * Whether this method is constructor.
	 */

	private boolean isConstructor = false;

	/**
	 *
	 */

	private List<IArgumentInfoList> arguments;

	private String javaDoc;

	private String context;

	private IElementDefn elementDefn;

	/**
	 * Constructs method definition.
	 *
	 * @param isConstructor whether this method is constructor
	 */

	public MethodInfo(boolean isConstructor) {
		super();

		this.isConstructor = isConstructor;
	}

	public MethodInfo() {
		this(false);
	}

	/**
	 * Adds an optional argument list to the method information.
	 *
	 * @param argumentList an optional argument list
	 *
	 */

	public void addArgumentList(IArgumentInfoList argumentList) {
		if (arguments == null) {
			arguments = new ArrayList<>();
		}

		arguments.add(argumentList);
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>ArgumentInfoList</code>.
	 *
	 * @return iterator of argument definition.
	 */

	@Override
	public Iterator<IArgumentInfoList> argumentListIterator() {
		if (arguments == null) {
			return Collections.EMPTY_LIST.iterator();
		}

		return arguments.iterator();
	}

	/**
	 * Returns the script type for return.
	 *
	 * @return the script type for return
	 */

	@Override
	public String getReturnType() {
		return returnType;
	}

	/**
	 * Sets the script type for return.
	 *
	 * @param returnType the script type to set
	 */

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * Returns the resource key for tool tip.
	 *
	 * @return the resource key for tool tip
	 */

	@Override
	public String getToolTipKey() {
		return toolTipKey;
	}

	/**
	 * Sets the resource key for tool tip.
	 *
	 * @param toolTipKey the resource key to set
	 */

	@Override
	public void setToolTipKey(String toolTipKey) {
		this.toolTipKey = toolTipKey;
	}

	/**
	 * Returns the display string for the tool tip of this method.
	 *
	 * @return the user-visible, localized display name for the tool tip of this
	 *         method.
	 */

	@Override
	public String getToolTip() {
		assert toolTipKey != null;
		return ModelMessages.getMessage(toolTipKey);

	}

	/**
	 * Returns whether this method is constructor.
	 *
	 * @return true, if this method is constructor
	 */

	@Override
	public boolean isConstructor() {
		return isConstructor;
	}

	public void Constructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	/**
	 * Returns whether this method is static.
	 *
	 * @return true if this method is static
	 */

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * Sets whether this method is static.
	 *
	 * @param isStatic true if this method is static
	 */

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * Returns the javadoc for the method.
	 *
	 * @return the javaDoc
	 */

	@Override
	public String getJavaDoc() {
		return javaDoc;
	}

	/**
	 * Sets the javadoc for the method.
	 *
	 * @param javaDoc the method javaDoc in string
	 */

	public void setJavaDoc(String javaDoc) {
		this.javaDoc = javaDoc;
	}

	/**
	 * Sets the method context. The method is supposed to run only in specified
	 * context.
	 *
	 * @param context the method context
	 */

	void setContext(String context) {
		this.context = context;
	}

	/**
	 * Returns the method context. The method is supposed to run only in specified
	 * context.
	 *
	 * @return the method context
	 */

	String getContext() {
		return context;
	}

	/**
	 * Sets the element definition so that the scriptable factory can be retrieved.
	 * This method is only for peer extension elements.
	 *
	 * @param elementDefn the element definition
	 */

	void setElementDefn(IElementDefn elementDefn) {
		assert elementDefn instanceof PeerExtensionElementDefn;

		this.elementDefn = elementDefn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IMethodInfo#getClassReturnType ()
	 */

	@Override
	public IClassInfo getClassReturnType() {
		if (returnClassType != null) {
			return returnClassType;
		}
		IClassInfo tmpInfo = new ScriptableClassInfo().getClass(returnType);
		if (tmpInfo != null) {
			return tmpInfo;
		}

		if (elementDefn == null) {
			return null;
		}

		IScriptableObjectClassInfo factory = ((PeerExtensionElementDefn) elementDefn).getScriptableFactory();
		if (factory == null) {
			return null;
		}

		return factory.getScriptableClass(returnType);
	}

	public void setClassReturnType(IClassInfo returnClassType) {
		this.returnClassType = returnClassType;
	}

}
