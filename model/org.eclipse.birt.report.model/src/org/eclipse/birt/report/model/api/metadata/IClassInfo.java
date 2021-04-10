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

import java.util.List;

/**
 * Represents the script object definition. This definition defines one
 * constructor, several members and methods. It also includes the name, display
 * name ID, and tool tip ID.
 */

public interface IClassInfo extends ILocalizableInfo {

	/**
	 * Returns the method definition list. For methods that have the same name, only
	 * return one method.
	 * 
	 * @return a list of method definitions
	 */

	public List<IMethodInfo> getMethods();

	/**
	 * Get the method definition given the method name.
	 * 
	 * @param name the name of the method to get
	 * @return the definition of the method to get
	 */

	public IMethodInfo getMethod(String name);

	/**
	 * Returns the list of member definitions.
	 * 
	 * @return the list of member definitions
	 */

	public List<IMemberInfo> getMembers();

	/**
	 * Returns the member definition given method name.
	 * 
	 * @param name name of the member to get
	 * @return the member definition to get
	 */

	public IMemberInfo getMember(String name);

	/**
	 * Returns the constructor definition.
	 * 
	 * @return the constructor definition
	 */

	public IMethodInfo getConstructor();

	/**
	 * Returns whether a class object is native.
	 * 
	 * @return <code>true</code> if an object of this class is native, otherwise
	 *         <code>false</code>
	 */

	public boolean isNative();
}
