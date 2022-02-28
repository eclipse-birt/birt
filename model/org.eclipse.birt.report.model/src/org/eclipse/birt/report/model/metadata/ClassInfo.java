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
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the script object definition. This definition defines one
 * constructor, several members and methods. It also includes the name, display
 * name ID, and tool tip ID.
 */

public class ClassInfo extends LocalizableInfo implements IClassInfo {

	/**
	 * The constructor definition.
	 */

	private IMethodInfo constructor;

	/**
	 * The list of method definitions.
	 */

	private LinkedHashMap<String, IMethodInfo> methods;

	/**
	 * The list of member definitions.
	 */

	private LinkedHashMap<String, IMemberInfo> members;

	/**
	 * The flag indicates if an object is native or not.
	 */

	private boolean isNative = false;

	/**
	 * Adds one method definition to this class definition.
	 *
	 * @param methodInfo the definition of the method to add
	 * @throws MetaDataException if the duplicate method name exists.
	 */

	public void addMethod(IMethodInfo methodInfo) throws MetaDataException {
		if (methods == null) {
			methods = new LinkedHashMap<>();
		}

		if (StringUtil.isBlank(methodInfo.getName())) {
			throw new MetaDataException(new String[] { methodInfo.getName() },
					MetaDataException.DESIGN_EXCEPTION_MISSING_METHOD_NAME);
		}

		methods.put(methodInfo.getName(), methodInfo);
	}

	/**
	 * Adds one member definition to this class definition.
	 *
	 * @param memberDefn the definition of the member to add
	 * @throws MetaDataException if the duplicate member name exists.
	 */

	public void addMemberDefn(IMemberInfo memberDefn) throws MetaDataException {
		if (members == null) {
			members = new LinkedHashMap<>();
		}

		if (StringUtil.isBlank(memberDefn.getName())) {
			throw new MetaDataException(new String[] { memberDefn.getName() },
					MetaDataException.DESIGN_EXCEPTION_MISSING_MEMBER_NAME);
		}

		if (findMember(memberDefn.getName()) != null) {
			throw new MetaDataException(new String[] { memberDefn.getName(), name },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_MEMBER_NAME);
		}

		members.put(memberDefn.getName(), memberDefn);
	}

	/**
	 * Returns the method definition list. For methods that have the same name, only
	 * return one method.
	 *
	 * @return a list of method definitions
	 */

	@Override
	public List<IMethodInfo> getMethods() {
		if (methods != null) {
			return new ArrayList<>(methods.values());
		}

		return Collections.emptyList();
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
	 * Returns the list of member definitions.
	 *
	 * @return the list of member definitions
	 */

	@Override
	public List<IMemberInfo> getMembers() {
		if (members != null) {
			return new ArrayList<>(members.values());
		}

		return Collections.emptyList();
	}

	/**
	 * Returns the member definition given method name.
	 *
	 * @param name name of the member to get
	 * @return the member definition to get
	 */

	@Override
	public IMemberInfo getMember(String name) {
		return findMember(name);
	}

	/**
	 * Returns the member definition given method name.
	 *
	 * @param name name of the member to find
	 * @return the member definition to find
	 */

	private IMemberInfo findMember(String name) {
		return (MemberInfo) findInfo(members, name);
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
	 * Returns the member definition given method name.
	 *
	 * @param name name of the member to find
	 * @return the member definition to find
	 */

	MethodInfo findMethod(String name) {
		return (MethodInfo) findInfo(methods, name);
	}

	/**
	 * Finds out the member/method information of a <code>ClassInfo</code>.
	 *
	 * @param objs the collection contains member/method information
	 * @param name the name of a member/method
	 *
	 * @return a <code>MemberInfo</code> or a <code>MethodInfo</code> corresponding
	 *         to <code>objs</code>
	 */

	private Object findInfo(LinkedHashMap<String, ? extends Object> objs, String name) {
		if (objs == null || name == null) {
			return null;
		}

		return objs.get(name);
	}

	/**
	 * Adds constructor since some class has more than one constructor with
	 * different arguments.
	 *
	 * @param constructor the constructor definition to add
	 * @throws MetaDataException if the constructor's name is empty.
	 */

	public void setConstructor(IMethodInfo constructor) throws MetaDataException {
		assert constructor != null;

		if (StringUtil.isBlank(constructor.getName())) {
			throw new MetaDataException(new String[] { constructor.getName() },
					MetaDataException.DESIGN_EXCEPTION_MISSING_METHOD_NAME);
		}

		this.constructor = constructor;
	}

	/**
	 * Returns whether a class object is native.
	 *
	 * @return <code>true</code> if an object of this class is native, otherwise
	 *         <code>false</code>
	 */

	@Override
	public boolean isNative() {
		return isNative;
	}

	/**
	 * Sets the native attribute of this class.
	 *
	 * @param isNative <code>Boolean.TRUE</code> if an object of this class is
	 *                 native, otherwise <code>Boolean.FALSE</code>
	 */

	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}
}
