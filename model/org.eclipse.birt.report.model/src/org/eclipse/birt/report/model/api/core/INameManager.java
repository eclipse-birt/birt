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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Interface to do all managements about the element names.
 */

public interface INameManager {

	/**
	 * Gets the host module of this name manager. The host module is what adapts all
	 * the managements for element names to assure that all the child elements have
	 * a unqiue name.
	 * 
	 * @return the host module of this name manager
	 */

	Module getHostModule();

	/**
	 * Makes a unique name for the given element.
	 * 
	 * @param element the element to make a unique name
	 */

	void makeUniqueName(DesignElement element);

	/**
	 * Clears the data in name manage and re-initialize it.
	 * 
	 */

	void clear();

	/**
	 * Deletes the element from the name manager.
	 * 
	 * @param element the element to drop
	 */

	void dropElement(DesignElement element);

	/**
	 * Returns a unique name for the given element.
	 * 
	 * @param element the given element.
	 * 
	 * @return unique name.
	 */

	String getUniqueName(DesignElement element);

	/**
	 * Adds a content name to the name-manager.
	 * 
	 * @param id   name space id
	 * @param name the name of the content
	 */

	void addContentName(int id, String name);
}
