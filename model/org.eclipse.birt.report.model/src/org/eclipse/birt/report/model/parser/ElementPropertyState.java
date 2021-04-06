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

import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the "property" tag and its type is "element". The state must parse the
 * element property value, not structure memeber value.
 */
public class ElementPropertyState extends AbstractPropertyState {

	protected int lineNumber = 1;
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * AbstractPropertyState(DesignParserHandler theHandler, DesignElement element,
	 * )
	 */

	public ElementPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
		lineNumber = handler.getCurrentLineNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	public AbstractParseState startElement(String tagName) {
		if (valid) {
			PropertyDefn propDefn = element.getPropertyDefn(name);
			List<IElementDefn> allowedElements = propDefn.getAllowedElements();
			for (int i = 0; i < allowedElements.size(); i++) {
				IElementDefn elementDefn = allowedElements.get(i);
				AbstractParseState state = ParseStateFactory.getInstance().createParseState(tagName, elementDefn,
						handler, element, propDefn);
				if (state != null)
					return state;
			}

			// this child is not allowed in the container
			if (element instanceof ExtendedItem) {
				AbstractParseState state = ParseStateFactory.getInstance().createParseState(tagName, handler, element,
						propDefn);
				if (state != null)
					return state;
			}
		}
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		super.end();
		if (handler.markLineNumber)
			handler.tempLineNumbers.put(new ContainerContext(element, name), lineNumber);

	}

}
