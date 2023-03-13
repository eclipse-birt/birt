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

package org.eclipse.birt.core.framework.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Abstract parse state for the <code>XMLParserHandler</code> class. Derived
 * classes create parse states based on this class.
 *
 * @see ParseState
 * @see AnyElementState
 */

public abstract class AbstractParseState {

	/**
	 * SAX context string.
	 */

	protected String context;

	/**
	 * The name of the element being parsed.
	 */

	protected String elementName = null;

	/**
	 * Accumulates any text that appears within the element tags.
	 */

	protected StringBuffer text = new StringBuffer();

	/**
	 * Sets the element name.
	 *
	 * @param name the name of the element.
	 */

	public void setElementName(String name) {

		this.elementName = name;

	}

	/**
	 * Jumps to the specified state that the current state needs to go.
	 *
	 * @return the other state.
	 */

	public AbstractParseState jumpTo() {

		return null;
	}

	/**
	 * Called to parse attributes. This is the first method called after the state
	 * is created.Returns the value of attribute name.
	 *
	 * @param attrs the SAX attributes object
	 * @throws XMLParserException if any parse exception
	 * @see org.xml.sax.helpers.DefaultHandler#startElement
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
	}

	/**
	 * Start a new tag. Derived classes override this to create a state to handle
	 * the element. Call this method to issue an error for, and ignore, any
	 * unrecognized tags.
	 *
	 * @param tagName the name of the starting element
	 * @return the state to parse the given tag
	 * @see org.xml.sax.helpers.DefaultHandler#startElement
	 */

	public AbstractParseState startElement(String tagName) {
		getHandler().semanticError(new XMLParserException(XMLParserException.UNKNOWN_TAG));
		return new AnyElementState(getHandler());
	}

	/**
	 * Returns the parser handler. Required to be implemented by derived states.
	 * States will implement this differently depending on whether the state is a
	 * normal or inner class.
	 *
	 * @return the XML parser handler
	 */

	public abstract XMLParserHandler getHandler();

	/**
	 * Called when a child element is ending.
	 *
	 * @param state the child state that is ending
	 */

	public void endElement(AbstractParseState state) {
	}

	/**
	 * Called when the element for this state is ending.
	 *
	 * @throws SAXException if the SAX exception is encountered.
	 * @see org.xml.sax.helpers.DefaultHandler#endElement
	 */

	public void end() throws SAXException {
	}

	/**
	 * Parse a string value. Normalizes the string: blank strings are converted to a
	 * null string.
	 *
	 * @param attrs    the SAX attributes object
	 * @param attrName the name of the attribute to parse
	 * @return the parsed string
	 */

	protected String getAttrib(Attributes attrs, String attrName) {
		return attrs.getValue(attrName);
	}

}
