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

/**
 * Interface for the definition of a property structure: an object that is
 * "managed" by the model to allow generic member access and undo/redo support
 * for updates.
 */

public interface IStructureDefn extends IObjectDefn {

	/**
	 * Gets a structure member by name.
	 * 
	 * @param name the name of the member to fine
	 * @return the member definition, or null if the member was not found
	 */

	public IPropertyDefn getMember(String name);
}
