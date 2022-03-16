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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the script object definition. This definition defines one
 * constructor, several members and methods. It also includes the name, display
 * name ID, and tool tip ID.
 */

public class ClassInfo implements IClassInfo {

	private final Class clazz;

	/**
	 * The list of method definitions.
	 */

	private Map methods;

	/**
	 * The list of member definitions.
	 */

	private Map members;

	/**
	 * The constructor definition.
	 */

	private IMethodInfo constructor;

	/**
	 * @param clazz
	 */

	public ClassInfo(Class clazz) {
		this.clazz = clazz;
		initialize();
	}

	private void initialize() {
		methods = new LinkedHashMap();
		members = new LinkedHashMap();

		Method[] classMethods = clazz.getMethods();
		for (int i = 0; i < classMethods.length; i++) {
			Method classMethod = classMethods[i];

			// filter deprecated methods, use 1.5 feature
			if (classMethod.isAnnotationPresent(Deprecated.class)) {
				continue;
			}

			String methodName = classMethod.getName();

			IMethodInfo method = (IMethodInfo) methods.get(methodName);
			if (method == null) {
				method = createMethodInfo(classMethod);
				if (method != null) {
					methods.put(methodName, method);
				}
			}
		}

		Constructor[] classConstructors = clazz.getConstructors();
		for (int i = 0; i < classConstructors.length; i++) {
			Constructor classMethod = classConstructors[i];
			if (constructor == null) {
				constructor = createConstructorInfo(classMethod);
			}
		}

		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field classField = fields[i];
			IMemberInfo memberInfo = createMemberInfo(classField);
			if (memberInfo != null) {
				members.put(classField.getName(), memberInfo);
			}
		}

	}

	/**
	 * @param classField
	 * @return
	 */

	protected IMemberInfo createMemberInfo(Field classField) {
		return new MemberInfo(classField);
	}

	/**
	 * @param classMethod
	 * @return
	 */

	protected IMethodInfo createConstructorInfo(Constructor classMethod) {
		return new ConstructorInfo(classMethod);
	}

	/**
	 * @param classMethod
	 * @return
	 */

	protected IMethodInfo createMethodInfo(Method classMethod) {
		return new MethodInfo(classMethod);
	}

	/**
	 * Returns the method definition list. For methods that have the same name, only
	 * return one method.
	 *
	 * @return a list of method definitions
	 */

	@Override
	public List getMethods() {
		if (methods != null) {
			return new ArrayList(methods.values());
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * Get the method definition given the method name.
	 *
	 * @param name the name of the method to get
	 * @return the definition of the method to get
	 */

	@Override
	public IMethodInfo getMethod(String name) {
		return (IMethodInfo) findInfo(methods, name);
	}

	/**
	 * Finds out the member/method information of a <code>ClassInfo</code>.
	 *
	 * @param objs the colllection contains member/method information
	 * @param name the name of a member/method
	 *
	 * @return a <code>MemberInfo</code> or a <code>MethodInfo</code> corresponding
	 *         to <code>objs</code>
	 */

	private static Object findInfo(Map objs, String name) {
		if (objs == null || name == null) {
			return null;
		}

		return objs.get(name.toLowerCase());
	}

	/**
	 * Returns the list of member definitions.
	 *
	 * @return the list of member definitions
	 */

	@Override
	public List getMembers() {
		Field[] fields = clazz.getFields();
		List retList = new ArrayList();
		for (int i = 0; i < fields.length; i++) {
			retList.add(new MemberInfo(fields[i]));
		}
		return retList;
	}

	/**
	 * Returns the member definition given method name.
	 *
	 * @param name name of the member to get
	 * @return the member definition to get
	 */

	@Override
	public IMemberInfo getMember(String name) {
		try {
			Field field = clazz.getField(name);
			return new MemberInfo(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	/**
	 * Returns the constructor definition.
	 *
	 * @return the constructor definition
	 */

	@Override
	public IMethodInfo getConstructor() {
		return constructor;
	}

	/**
	 * Returns whether a class object is native.
	 *
	 * @return <code>true</code> if an object of this class is native, otherwise
	 *         <code>false</code>
	 */

	@Override
	public boolean isNative() {
		return false;
	}

	@Override
	public String getDisplayNameKey() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	@Override
	public String getName() {
		return clazz.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getToolTipKey()
	 */

	@Override
	public String getToolTipKey() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return StringUtil.EMPTY_STRING;
	}
}
