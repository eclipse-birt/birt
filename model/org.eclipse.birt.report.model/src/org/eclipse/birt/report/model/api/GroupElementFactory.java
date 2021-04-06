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

import java.util.List;

/**
 * This class is a factory class to instantiate a group element handle.
 */

public class GroupElementFactory {

	/**
	 * Instantiates a group element handle with the given module handle and selected
	 * design element list. Both the module handle and the element list can be null
	 * or empty.
	 * 
	 * @param moduleHandle     the module handle of the selected element list,
	 *                         generally it is the root module of them
	 * @param selectedElements the selected design element list
	 * @return the generated group element handle
	 */

	public static GroupElementHandle newGroupElement(ModuleHandle moduleHandle, List selectedElements) {
		if (moduleHandle == null || selectedElements == null)
			return new EmptyGroupElementHandle();
		return new SimpleGroupElementHandle(moduleHandle, selectedElements);
	}
}
