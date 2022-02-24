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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Provides the specific property searching route for <code>TableRow</code>.
 */

public class TableRowPropSearchStrategy extends PropertySearchStrategy {

	private final static TableRowPropSearchStrategy instance = new TableRowPropSearchStrategy();

	/**
	 * Protected constructor.
	 */
	protected TableRowPropSearchStrategy() {
	}

	/**
	 * Returns the instance of <code>TableRowPropSearchStrategy</code> which provide
	 * the specific property searching route for <code>TableRow</code>.
	 * 
	 * @return the instance of <code>TableRowPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getNonIntrinsicPropertyFromElement
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	protected Object getNonIntrinsicPropertyFromElement(Module module, DesignElement element,
			ElementPropertyDefn prop) {
		if (IStyleModel.TEXT_ALIGN_PROP.equalsIgnoreCase(prop.getName())) {
			Object value = super.getNonIntrinsicPropertyFromElement(module, element, prop);
			if (value != null)
				return value;

			DesignElement container = element.getContainer();
			if (container instanceof TableItem
					&& element.getContainerInfo().getSlotID() == IListingElementModel.HEADER_SLOT) {
				value = getPropertyExceptRomDefault(module, container, prop);

				// if the cell is table-header-cell and no property is set in
				// the property search path, then return context default
				// 'center'
				if (value == null)
					return DesignChoiceConstants.TEXT_ALIGN_CENTER;
			}
			return null;

		}

		return super.getNonIntrinsicPropertyFromElement(module, element, prop);
	}

	public Object getPropertyFromSlotSelector(Module module, DesignElement element, ElementPropertyDefn prop,
			PropertyValueInfo valueInfo) {
		// 1. try to get predefined value from slot selector
		Object value = super.getPropertyFromSlotSelector(module, element, prop, valueInfo);
		if (value != null)
			return value;

		// 2. try to get the predefined value for all table rows
		DesignElement rowContainer = element.getContainer();
		if (rowContainer == null)
			return null;
		String selector = null;
		if (rowContainer instanceof TableItem) {
			selector = "table-row";
		}
		return super.getPropertyFromSelector(module, element, prop, selector, valueInfo);
	}

}
