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
 * Represents a named, shared style. A named, shared style is a style defined in
 * components slot of a report design.
 * 
 */

public class SharedStyleHandle extends StyleHandle {

	/**
	 * Constructs a handle for a shared style. The application generally does not
	 * create handles directly. Instead, it uses one of the navigation methods
	 * available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public SharedStyleHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getQualifiedName()
	 */

	public String getQualifiedName() {
		return getName();
	}
}