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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DynamicFilterParameter;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses dynamic filter parameter.
 *
 */

public class DynamicFilterParameterState extends AbstractScalarParameterState {

	/**
	 * The scalar parameter being created.
	 */

	protected DynamicFilterParameter param;

	/**
	 * Constructs the DynamicFilterParameter state with the design parser handler,
	 * the container element and the container slot of the scalar parameter.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the container of this parameter.
	 * @param slot         the slot ID of the slot where the parameter is stored.
	 */

	public DynamicFilterParameterState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		// First we create the dynamic filter parameter.

		param = new DynamicFilterParameter();

		// Then we initialize the properties.

		initElement(attrs, true);
	}

	/**
	 * Returns the dynamic filter parameter being built.
	 *
	 * @return the parameter instance
	 */

	@Override
	public DesignElement getElement() {
		return param;
	}

}
