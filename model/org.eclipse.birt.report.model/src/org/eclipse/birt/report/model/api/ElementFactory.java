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

import org.eclipse.birt.report.model.core.Module;

/**
 * Creates a new report elements and returns handles to it. Use this to create
 * elements. After creating an element, add it to the design using the
 * <code>add</code> method in the {@link SlotHandle}class. Obtain an instance of
 * this class by calling the <code>getElementFactory</code> method on any
 * element handle.
 * 
 * @see SlotHandle
 */

public class ElementFactory extends ElementFactoryImpl {

	/**
	 * Constructs a element factory with the given module.
	 * 
	 * @param module the module
	 */

	public ElementFactory(Module module) {
		super(module);
	}

}
