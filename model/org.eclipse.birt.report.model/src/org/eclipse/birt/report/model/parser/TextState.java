/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IPropertySet;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.xml.sax.SAXException;

/**
 * This class parses an element that contains only text, and that text is stored
 * as a property.
 *
 */

public class TextState extends DesignParseState {

	/**
	 * The element or structure that contains the property.
	 */

	protected IPropertySet valueSet;

	/**
	 * Name of the property to set.
	 */

	protected String valueName;

	/**
	 * Constructs the text state with the design file parser handler, the element or
	 * structure that holds the text and the property name of the text.
	 *
	 * @param handler the design file parser handler
	 * @param obj     the element or structure that has the property to set
	 * @param theProp the name of the property to set
	 */

	public TextState(ModuleParserHandler handler, IPropertySet obj, String theProp) {
		super(handler);
		valueSet = obj;
		valueName = theProp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	@Override
	public void end() throws SAXException {
		String value = text.toString();

		PropertyDefn prop = (PropertyDefn) valueSet.getObjectDefn().findProperty(valueName);
		assert prop != null;
		valueSet.setProperty(prop, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return null;
	}

}
