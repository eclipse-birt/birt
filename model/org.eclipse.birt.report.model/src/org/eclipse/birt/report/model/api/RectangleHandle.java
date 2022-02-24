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
