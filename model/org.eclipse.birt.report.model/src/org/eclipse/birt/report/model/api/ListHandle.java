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
 * Represents a list report item. A list is traditional report structure: a data
 * set, a set of groups and a detail line. The data set provides the data to
 * display. The detail frame prints each row from the data set. Groups provide
 * optional grouping levels for headings and totals.
 *
 *
 * @see org.eclipse.birt.report.model.elements.ListItem
 */

public class ListHandle extends ListingHandle {

	/**
	 * Constructs a list handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ListHandle(Module module, DesignElement element) {
		super(module, element);
	}

}
