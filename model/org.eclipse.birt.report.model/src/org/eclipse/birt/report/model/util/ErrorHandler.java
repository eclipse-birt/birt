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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Locator;

/**
 * Implements the error handler during the XML parser.
 * 
 */

public abstract class ErrorHandler {

	/**
	 * The current element being parsed.
	 */

	protected String currentElement = null;

	/**
	 * The list contains errors encountered when parsing a XML file.
	 */

	protected List<XMLParserException> errors = new ArrayList<XMLParserException>();

	/**
	 * The list contains warnings encountered when parsing a XML file.
	 */

	protected List<XMLParserException> warnings = new ArrayList<XMLParserException>();

	/**
	 * SAX <code>Locator</code> for reporting errors.
	 */

	protected Locator locator = null;

	/**
	 * Returns the error list when parsing xml file.
	 * 
	 * @return the errors
	 */

	public List<XMLParserException> getErrors() {
		return errors;
	}

	/**
	 * Gets the warning list when parsing xml file.
	 * 
	 * @return the warnings
	 */

	public List<XMLParserException> getWarnings() {
		return this.warnings;
	}

	/**
	 * Add a recoverable semantic error to the error list.
	 * 
	 * @param e The exception to log.
	 * @return the added semantic error
	 */

	public XMLParserException semanticError(Exception e) {
		return semanticError(new XMLParserException(e));
	}

	/**
	 * Add a recoverable semantic error to the error list.
	 * 
	 * @param e The exception to log
	 * @return the added semantic error
	 */

	public abstract XMLParserException semanticError(XMLParserException e);

	/**
	 * Receive a Locator object for document events.
	 * 
	 * @param theLocator a locator for all SAX document events
	 * @see org.xml.sax.Locator
	 */

	public void setDocumentLocator(Locator theLocator) {
		locator = theLocator;
	}

	/**
	 * Sets the current element name.
	 * 
	 * @param theCurrentElement the current element name
	 */

	public void setCurrentElement(String theCurrentElement) {
		this.currentElement = theCurrentElement;
	}

	/**
	 * Gets the current element name.
	 * 
	 * @return the current element name
	 */

	String getCurrentElement() {
		return this.currentElement;
	}

	/**
	 * Adds a warning to the warning list inherited from XMLParserHandler during
	 * parsing the design file.
	 * 
	 * @param e the exception to log
	 */

	public void semanticWarning(Exception e) {
		XMLParserException xmlException = new XMLParserException(e);
		xmlException.setLineNumber(locator.getLineNumber());
		xmlException.setTag(currentElement);
		warnings.add(xmlException);
	}
}
