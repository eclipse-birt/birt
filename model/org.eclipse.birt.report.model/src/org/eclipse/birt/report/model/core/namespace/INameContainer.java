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

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * 
 */
public interface INameContainer {

	/**
	 * Gets the name helper for this name container.
	 * 
	 * @return the name helper of this container
	 */
	public INameHelper getNameHelper();

	/**
	 * Checks the element name in this name container.
	 * 
	 * <ul>
	 * <li>If the element name is required and duplicate name is found in name
	 * space, rename the element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 * 
	 * @param element the element handle whose name is need to check.
	 */

	public void rename(DesignElement element);

	/**
	 * Makes a unique name for this element.
	 * 
	 * @param element
	 */
	public void makeUniqueName(DesignElement element);
}
