/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base class for all report item parse states.
 * 
 */

public abstract class ReportItemState extends ReportElementState {

	/**
	 * Constructs the report item state with the design parser handler, the
	 * container element and the container slot of the report item.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ReportItemState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs the report item state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ReportItemState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/**
	 * Intializes a report item with the properties common to all report items.
	 * 
	 * @param attrs the SAX attributes object
	 */

	protected void initElement(Attributes attrs) {
		super.initElement(attrs, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		DesignElement element = getElement();
		TemplateParameterDefinition refTemplateParam = element.getTemplateParameterElement(handler.getModule());
		if (refTemplateParam != null) {
			DesignElement defaultElement = refTemplateParam.getDefaultElement();

			if (!(defaultElement instanceof ReportItem)) {
				handler.getErrorHandler()
						.semanticError(new DesignParserException(
								new String[] { element.getIdentifier(), refTemplateParam.getIdentifier() },
								DesignParserException.DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE));
			}
		} else {
			// fire an error
		}
		super.end();
	}

}