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

package org.eclipse.birt.report.model.util;

import java.util.Date;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.parser.ModuleParserHandler;
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
		XMLParserHandler handler = getHandler();
		boolean isSetWarning = false;

		if (handler instanceof ModuleParserHandler) {
			ModuleParserHandler moduleHandler = (ModuleParserHandler) handler;
			if (moduleHandler.isLaterVersion() && (moduleHandler.getModule().getOptions() != null
					&& moduleHandler.getModule().getOptions().isSupportedUnknownVersion())) {
				isSetWarning = true;
			}
		}

		if (isSetWarning) {
			getHandler().getErrorHandler()
					.semanticWarning(new XMLParserException(XMLParserException.DESIGN_EXCEPTION_UNKNOWN_TAG));
		} else {
			getHandler().getErrorHandler()
					.semanticError(new XMLParserException(XMLParserException.DESIGN_EXCEPTION_UNKNOWN_TAG));
		}

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
	 * @throws
	 * @see org.xml.sax.helpers.DefaultHandler#endElement
	 */

	public void end() throws SAXException {
	}

	/**
	 * Utility method to parse a Boolean attribute.
	 * 
	 * @param attrs        the SAX attributes object
	 * @param attrName     the name of the attribute to parse
	 * @param defaultValue the default value if the attribute is not provided
	 * @return the parsed Boolean value
	 */

	public boolean getBooleanAttrib(Attributes attrs, String attrName, boolean defaultValue) {
		return parseBoolean(attrs.getValue(attrName), defaultValue);
	}

	/**
	 * Parse a boolean string.
	 * 
	 * @param value        the XML string to parse
	 * @param defaultValue the default value if the string is null or blank
	 * @return the parsed Boolean value
	 */

	public boolean parseBoolean(String value, boolean defaultValue) {
		value = StringUtil.trimString(value);
		if (value == null)
			return defaultValue;
		else if (value.equalsIgnoreCase("true")) //$NON-NLS-1$
			return true;
		else if (value.equalsIgnoreCase("false")) //$NON-NLS-1$
			return false;
		getHandler().getErrorHandler()
				.semanticError(new XMLParserException(XMLParserException.DESIGN_EXCEPTION_INVALID_BOOLEAN));
		return defaultValue;
	}

	/**
	 * Parses an integer attribute.
	 * 
	 * @param attrs    the SAX attributes object
	 * @param attrName the name of the attribute to parse
	 * @return the parsed attribute, or 0 if the attribute is not present
	 */

	public int getIntAttrib(Attributes attrs, String attrName) {
		return getIntAttrib(attrs, attrName, 0);
	}

	/**
	 * Parses an integer attribute.
	 * 
	 * @param attrs        the SAX attributes object
	 * @param attrName     the name of the attribute to parse
	 * @param defaultValue default value to return if the attribute is not present
	 * @return the parsed integer value
	 */

	public int getIntAttrib(Attributes attrs, String attrName, int defaultValue) {
		String value = attrs.getValue(attrName);
		if (value == null)
			return defaultValue;
		try {
			Integer result = Integer.decode(value);
			return result.intValue();
		} catch (NumberFormatException e) {
			getHandler().getErrorHandler()
					.semanticError(new XMLParserException(XMLParserException.DESIGN_EXCEPTION_INVALID_INTEGER));
			return 0;
		}
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
		return StringUtil.trimString(attrs.getValue(attrName));
	}

	/**
	 * Parse a date value. The date is assumed to be in the XML date format.
	 * 
	 * @param attrs    the SAX attributes object
	 * @param attrName the name of the attribute to parse
	 * @return the parsed date
	 */

	protected Date getDateAttrib(Attributes attrs, String attrName) {
		assert false;
		return null;
	}

	/**
	 * Sets the flag to indicate whether the value is a XML CDATA. In default, this
	 * method do nothing.
	 * 
	 * @param isCDataSection <code>true</code> if it is a XML CDATA. Otherwise
	 *                       <code>false</code>.
	 */

	public void setIsCDataSection(boolean isCDataSection) {

	}
}
