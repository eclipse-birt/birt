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
 * Represents a rectangle element. The Rectangle element describes a simple
 * rectangle. The user can set the line size, line color, line pattern and fill
 * color using style properties.
 */

public class RectangleHandle extends ReportItemHandle {

	/**
	 * Constructs the handle for a group parameters with the given design and
	 * element. The application generally does not create handles directly. Instead,
	 * it uses one of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the rectangle
	 */

	public RectangleHandle(Module module, DesignElement element) {
		super(module, element);
	}

}