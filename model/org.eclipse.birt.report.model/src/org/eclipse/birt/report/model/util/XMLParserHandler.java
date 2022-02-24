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

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Framework for parsing an XML file using a SAX parser. This framework provides
 * a separate class for each element. These classes are called "state" classes
 * because they represent the state of the parser. Generally, a state represents
 * some object being created. This handler manages the stack of active states,
 * and routes the SAX callbacks to the current state.
 * 
 * @see AbstractParseState
 * @see AnyElementState
 * @see ParseState
 * @see XMLParserException
 */

public abstract class XMLParserHandler extends DefaultHandler {

	/**
	 * SAX <code>Locator</code> for reporting errors.
	 */

	protected Locator locator = null;

	/**
	 * Stack of active parse states. Corresponds to the stack of currently active
	 * elements.
	 */

	protected Stack<AbstractParseState> stateStack = new Stack<AbstractParseState>();

	/**
	 * The error handler of the XML parser.
	 */

	protected ErrorHandler errorHandler;

	/**
	 * 
	 */

	protected AbstractParseState topState;

	/**
	 * Constructs the parser handler with the error handler.
	 * 
	 * @param errorHandler the error handler of the XML parser
	 */

	public XMLParserHandler(ErrorHandler errorHandler) {
		assert errorHandler != null;
		this.errorHandler = errorHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */

	public void startDocument() throws SAXException {
		super.startDocument();
		assert stateStack.isEmpty();
		pushState(createStartState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */

	public void endDocument() throws SAXException {
		super.endDocument();
		assert stateStack.size() == 1;
		topState.end();
		popState();
	}

	/**
	 * Gets the error handler of the parser.
	 * 
	 * @return the error handler of the parser
	 */

	public ErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

	/**
	 * Sets the error handler of the parser.
	 * 
	 * @param handler the error handler of the parser
	 */
	public void setErrorHandler(ErrorHandler handler) {
		this.errorHandler = handler;
	}

	/**
	 * Private method to add a parse state to the state stack.
	 * 
	 * @param state the state to push
	 */

	protected void pushState(AbstractParseState state) {
		assert state != null;
		state.context = errorHandler.getCurrentElement();
		stateStack.push(state);
		topState = state;
	}

	/**
	 * Private method to pop a parse state from the stack.
	 * 
	 * @return the state at the top of the stack
	 */

	private AbstractParseState popState() {
		assert !stateStack.isEmpty();
		AbstractParseState state = stateStack.pop();
		if (stateStack.size() > 0) {
			topState = stateStack.lastElement();
		}
		return state;
	}

	/**
	 * Starts an XML element. Delegates to the current state the task of creating a
	 * new parse state for the new element.
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(String, String, String,
	 *      Attributes)
	 */

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		errorHandler.setCurrentElement(qName);
		AbstractParseState newState = topState.startElement(qName);
		newState.elementName = qName;
		pushState(newState);
		newState.parseAttrs(atts);

	}

	/**
	 * Ends the parse state for an element.
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(String, String, String)
	 */

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		AbstractParseState state = topState;
		state.end();
		popState();
		if (!stateStack.isEmpty())
			topState.endElement(state);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (!stateStack.isEmpty()) {
			topState.text.append(ch, start, length);
		}
	}

	/**
	 * Parser handlers must implement this method to return the "start state": the
	 * state that will recognize the top-level element(s) in the XML file.
	 * 
	 * @return the start state specific to the derived parser
	 */

	public abstract AbstractParseState createStartState();

	/**
	 * Base class provides the parse state framework. By default, it reports an
	 * error if an unexpected tag is seen.
	 */

	public class InnerParseState extends ParseState {

		/**
		 * Default constructor.
		 */

		public InnerParseState() {
			super(XMLParserHandler.this);
		}

	}

	/**
	 * Parses any valid XML; handles unimplemented tags. Often used while building a
	 * parser to silently parse and ignore tags that the parser is not yet ready to
	 * handle.
	 */

	public class InnerAnyTagState extends InnerParseState {

		public AbstractParseState startElement(String tagName) {
			return new InnerAnyTagState();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */

	public void setDocumentLocator(Locator theLocator) {
		super.setDocumentLocator(theLocator);
		locator = theLocator;
		errorHandler.setDocumentLocator(theLocator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */

	public void error(SAXParseException e) throws SAXException {
		errorHandler.semanticError(new XMLParserException(e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */

	public void warning(SAXParseException e) throws SAXException {
		errorHandler.semanticError(new XMLParserException(e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */

	public void fatalError(SAXParseException e) throws SAXException {
		errorHandler.semanticError(new XMLParserException(e));
	}

	/**
	 * Gets the document locator.
	 * 
	 * @return the document locator.
	 */

	final public Locator getLocator() {
		return locator;
	}

	/**
	 * Gets current line number.
	 * 
	 * @return current line number.
	 */

	final public int getCurrentLineNo() {
		return locator.getLineNumber();
	}
}
