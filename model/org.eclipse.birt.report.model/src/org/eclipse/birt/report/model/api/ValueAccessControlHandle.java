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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IValueAccessControlModel;

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

public class ValueAccessControlHandle extends AccessControlHandle implements IValueAccessControlModel {

	/**
	 * Constructs a autotext handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ValueAccessControlHandle(Module module, DesignElement element) {
		super(module, element);

	}

	/**
	 * Adds the given value to the list.
	 * 
	 * @param value the value
	 * @throws SemanticException
	 */

	public void addValue(String value) throws SemanticException {
		PropertyHandle propHandle = getPropertyHandle(VALUES_PROP);
		propHandle.addItem(value);

	}

	/**
	 * Removes the given value from the list.
	 * 
	 * @param value the value to remove
	 * 
	 * @throws SemanticException
	 */

	public void removeValue(String value) throws SemanticException {
	}
}
