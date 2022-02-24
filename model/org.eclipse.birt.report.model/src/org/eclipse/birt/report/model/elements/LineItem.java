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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LineHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILineItemModel;

/**
 * This class represents a line item. Uses lines to add graphical interest to a
 * report. Most reports are intended for viewing on the web. Web browsers
 * generally support only horizontal and vertical, but not diagonal lines.
 * Diagonal lines require an image which imposes extra cost and is difficult to
 * correctly format. So, implementation limits support to horizontal and
 * vertical lines. Use the {@link org.eclipse.birt.report.model.api.LineHandle}
 * class to change the properties, such as the line size, line color and line
 * pattern using the item's style.
 * 
 */

public class LineItem extends ReportItem implements ILineItemModel {

	/**
	 * Default Constructor.
	 */

	public LineItem() {
		super();
	}

	/**
	 * Constructs the line item with an optional name.
	 * 
	 * @param theName the optional name of the line item
	 */

	public LineItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitLine(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.LINE_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public LineHandle handle(Module module) {
		if (handle == null) {
			handle = new LineHandle(module, this);
		}
		return (LineHandle) handle;
	}

}
