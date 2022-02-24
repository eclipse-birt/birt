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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILineItemModel;

/**
 * Represents the line item. The user can set the line orientation.
 * 
 * @see org.eclipse.birt.report.model.elements.LineItem
 */

public class LineHandle extends ReportItemHandle implements ILineItemModel {

	/**
	 * Constructs a line handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public LineHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the orientation of the line. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LINE_ORIENTATION_HORIZONTAL</code>
	 * <li><code>LINE_ORIENTATION_VERTICAL</code>
	 * </ul>
	 * The default is <code>LINE_ORIENTATION_HORIZONTAL</code>.
	 * 
	 * @return the orientation of the line
	 */

	public String getOrientation() {
		return getStringProperty(ILineItemModel.ORIENTATION_PROP);
	}

	/**
	 * Sets the orientation of the line. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LINE_ORIENTATION_HORIZONTAL</code>
	 * <li><code>LINE_ORIENTATION_VERTICAL</code>
	 * </ul>
	 * 
	 * @param orientation the orientation of the line
	 * @throws SemanticException if the input orientation is not one of the above.
	 */

	public void setOrientation(String orientation) throws SemanticException {
		setStringProperty(ILineItemModel.ORIENTATION_PROP, orientation);
	}
}
