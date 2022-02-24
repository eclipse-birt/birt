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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Parses the "xml-property" tag. We use the "xml-property" tag if the element
 * property or structure member contains XML, which is defined as xml type.
 */

class XmlPropertyState extends PropertyState {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.PropertyState#AbstractPropertyState(
	 * DesignParserHandler theHandler, DesignElement element, )
	 */

	XmlPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.PropertyState#AbstractPropertyState(
	 * DesignParserHandler theHandler, DesignElement element, String propName,
	 * IStructure struct)
	 */

	XmlPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn, IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);
	}
}
