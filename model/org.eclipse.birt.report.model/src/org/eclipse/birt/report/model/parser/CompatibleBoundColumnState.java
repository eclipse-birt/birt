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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;

/**
 * Parses the bound data structure list if the version is 3.1.0.
 */

final class CompatibleBoundColumnState extends CompatibleListPropertyState {

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this list property to parse is a property of one
	 * element.
	 *
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 */

	CompatibleBoundColumnState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.parser.ListPropertyState#setName(java.lang.
	 * String)
	 */

	@Override
	protected void setName(String name) {
		super.setName(name);

		assert element instanceof ReportItem || element instanceof ScalarParameter || element instanceof GroupElement;

		List tmpList = (List) element.getLocalProperty(handler.getModule(), name);

		if (tmpList != null) {
			list = (ArrayList) tmpList;
		}
	}
}
