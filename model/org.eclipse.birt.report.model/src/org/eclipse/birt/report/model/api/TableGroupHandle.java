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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represent a handle to a table group. Like a list report item, a table report
 * item can contain groups.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.TableGroup
 */

public class TableGroupHandle extends GroupHandle {

	/**
	 * Constructs a handle for the table group with the given design and element.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TableGroupHandle(Module module, DesignElement element) {
		super(module, element);
	}

}