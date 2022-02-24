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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * Lists all constants that an access control element may use.
 * 
 */

public interface IAccessControlModel {

	/**
	 * Name of the member which defines the user name.
	 */

	public static final String USER_NAMES_PROP = "userNames"; //$NON-NLS-1$

	/**
	 * Name of the member which describe the behavior of the user want to perform.
	 */

	public static final String ROLES_PROP = "roles"; //$NON-NLS-1$

	/**
	 * Name of the member which is either "allow" or "disallow".
	 */

	public static final String PERMISSION_PROP = "permission"; //$NON-NLS-1$
}
