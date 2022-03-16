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

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parse a data set.
 *
 */

public class SimpleDataSetState extends ReportElementState {

	/**
	 * The data set being built.
	 */

	protected SimpleDataSet element;

	/**
	 * Constructs the data set state with the design parser handler, the container
	 * element and the container slot of the data set.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public SimpleDataSetState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	@Override
	public void end() throws SAXException {
		DesignElement element = getElement();
		TemplateParameterDefinition refTemplateParam = element.getTemplateParameterElement(handler.getModule());
		if (refTemplateParam != null) {
			DesignElement defaultElement = refTemplateParam.getDefaultElement();
			IElementDefn elementDefn = element.getDefn();
			IElementDefn defaultElementDefn = defaultElement.getDefn();

			if (elementDefn != defaultElementDefn) {
				handler.getErrorHandler()
						.semanticError(new DesignParserException(
								new String[] { element.getIdentifier(), refTemplateParam.getIdentifier() },
								DesignParserException.DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE));
			}
		}

		super.end();
	}
}
