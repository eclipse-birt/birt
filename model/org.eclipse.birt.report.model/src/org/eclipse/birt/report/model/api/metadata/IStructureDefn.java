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