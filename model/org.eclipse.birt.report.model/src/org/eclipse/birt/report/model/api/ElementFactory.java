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