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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IAccessControlModel;

/**
 * Describes the privilege to access resources. Members are
 * 
 * <p>
 * <dl>
 * <dt><strong>user name </strong></dt>
 * <dd>The user name of the privilege.</dd>
 * 
 * <dt><strong>role </strong></dt>
 * <dd>what behavior a user can perform</dd>
 * 
 * <dt><strong>permission </strong></dt>
 * <dd>"allow" or "disallow" the specified behavior to the user</dd>
 * 
 * </dl>
 * 
 * @deprecated
 */

public class AccessControlHandle extends ContentElementHandle implements IAccessControlModel {

	/**
	 * Constructs a autotext handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AccessControlHandle(Module module, DesignElement element) {
		super(module, element);

	}

	/**
	 * Adds the given role to the list.
	 * 
	 * @param role the role
	 * @throws SemanticException
	 */

	public void addRole(String role) throws SemanticException {
	}

	/**
	 * Adds the given user name to the list.
	 * 
	 * @param userName the user name
	 * @throws SemanticException
	 */

	public void addUserName(String userName) throws SemanticException {
	}

	/**
	 * Returns whether the user can perform the role. The return value is one of
	 * following:
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.ACCESS_PERMISSION_ALLOW
	 * <li>DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW
	 * </ul>
	 * 
	 * @return the permission to perform the role
	 */

	public String getPermission() {
		return null;
	}

	/**
	 * Removes the given role from the list.
	 * 
	 * @param role the role to remove
	 * 
	 * @throws SemanticException
	 */

	public void removeRole(String role) throws SemanticException {
	}

	/**
	 * Removes the given user name from the list.
	 * 
	 * @param userName the user name to remove
	 * 
	 * @throws SemanticException
	 */

	public void removeUserName(String userName) throws SemanticException {
	}

	/**
	 * Sets the permission. The input parameter should be one of following:
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.ACCESS_PERMISSION_ALLOW
	 * <li>DesignChoiceConstants.ACCESS_PERMISSION_DISALLOW
	 * </ul>
	 * 
	 * @param permission the permission to perform the role
	 * 
	 * @throws SemanticException
	 */
	public void setPermission(String permission) throws SemanticException {
	}
}
