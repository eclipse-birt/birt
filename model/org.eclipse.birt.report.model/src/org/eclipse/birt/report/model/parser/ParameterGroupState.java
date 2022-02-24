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
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the parameter group element.
 * 
 */

public class ParameterGroupState extends ReportElementState {

	/**
	 * The ParameterGroup instance.
	 */

	protected ParameterGroup paramGroup;

	/**
	 * Constructs the parameter group state with the design file parser handler.
	 * 
	 * @param handler the design parser handler.
	 * @param slot    the slot.
	 */
	public ParameterGroupState(ModuleParserHandler handler, int slot) {
		super(handler, handler.module, slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return paramGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.PARAMETERS_TAG == tagValue)
			return new ParametersState(handler, paramGroup, ParameterGroup.PARAMETERS_SLOT);

		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		paramGroup = new ParameterGroup();
		initElement(attrs, true);
	}
}
