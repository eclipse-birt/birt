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
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the user property list.
 */

public class UserPropertyListState extends ListPropertyState {

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler the design parser handler
	 * @param element    the element holding this list property
	 */

	UserPropertyListState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.STRUCTURE_TAG == tagValue)
			return new UserPropertyStructureState(handler, element, list);

		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		// To avoid set list to property
	}
}