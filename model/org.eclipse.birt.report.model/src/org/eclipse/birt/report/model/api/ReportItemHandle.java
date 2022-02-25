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
 * Represents a report item: any element that can appear within a section of the
 * report. Report items have a size and position that are used in some
 * containers. Report items also have a style. Report items can references to
 * the data set to use for itself. Many report items can be the target of
 * hyperlinks. The bookmark property identifies the item location. It also has a
 * set of visibility rules that say when a report item should be hidden. The
 * bindings allow a report item to pass data into its data source. Call
 * {@link DesignElementHandle#getPrivateStyle}( ) to get a handle with
 * getter/setter methods for the style properties.
 *
 * @see org.eclipse.birt.report.model.elements.ReportItem
 */

public abstract class ReportItemHandle extends ReportItemHandleImpl {

	/**
	 * Constructs the handle for a report item with the given design and element.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ReportItemHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Get the suppress duplicates property of this column.
	 *
	 * @return a boolean value which indicates if this column is suppress
	 *         duplicates.
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getProperty
	 * (java.lang.String)
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ModuleHandle#setTheme(org.eclipse.birt
	 * .report.model.api.ThemeHandle)
	 */
}
