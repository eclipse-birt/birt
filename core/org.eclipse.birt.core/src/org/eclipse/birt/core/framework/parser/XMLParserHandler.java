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

package org.eclipse.birt.core.framework.parser;

import java.util.ArrayList;
import java.util.List;
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
	 * The current element being parsed.
	 */

	protected String currentElement = null;

	/**
	 * Stack of active parse states. Corresponds to the stack of currently active
	 * elements.
	 */

	protected Stack stateStack = new Stack();

	/**
	 * SAX <code>Locator</code> for reporting errors.
	 */

	protected Locator locator = null;

	/**
	 * The list contains errors encountered when parsing a XML file.
	 */

	protected ArrayList errors = new ArrayList();

	/**
	 * Constructor
	 */

	public XMLParserHandler() {
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
		topState().end();
		popState();
	}

	/**
	 * Add a recoverable semantic error to the error list.
	 * 
	 * @param e The exception to log.
	 */

	public void semanticError(Exception e) {
		semanticError(new XMLParserException(e));
	}

	/**
	 * Add a recoverable semantic error to the error list.
	 * 
	 * @param e The exception to log.
	 */

	public abstract void semanticError(XMLParserException e);

	/**
	 * Private method to add a parse state to the state stack.
	 * 
	 * @param state the state to push
	 */

	protected void pushState(AbstractParseState state) {
		assert state != null;
		state.context = currentElement;
		stateStack.push(state);
	}

	/**
	 * Private method to pop a parse state from the stack.
	 * 
	 * @return the state at the top of the stack
	 */

	private AbstractParseState popState() {
		assert !stateStack.isEmpty();
		AbstractParseState state = (AbstractParseState) stateStack.pop();
		if (stateStack.size() > 0) {
			topState().endElement(state);
		}
		return state;
	}

	/**
	 * Private method to return the top of the state stack.
	 * 
	 * @return the state at the top of the state stack
	 */

	protected AbstractParseState topState() {
		assert !stateStack.isEmpty();
		return (AbstractParseState) stateStack.lastElement();
	}

	/**
	 * Starts an XML element. Delegates to the current state the task of creating a
	 * new parse state for the new element.
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(String, String, String,
	 *      Attributes)
	 */

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		currentElement = qName;
		AbstractParseState newState = topState().startElement(qName);
		newState.elementName = currentElement;
		pushState(newState);
		newState.parseAttrs(atts);

	}

	/**
	 * Ends the parse state for an element.
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(String, String, String)
	 */

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		AbstractParseState state = topState();
		state.end();
		popState();
		if (!stateStack.isEmpty())
			topState().endElement(state);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (!stateStack.isEmpty()) {
			topState().text.append(ch, start, length);
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
		super.setDocumentLocator(locator);
		locator = theLocator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */

	public void error(SAXParseException e) throws SAXException {
		semanticError(new XMLParserException(e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */

	public void warning(SAXParseException e) throws SAXException {
		semanticError(new XMLParserException(e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */

	public void fatalError(SAXParseException e) throws SAXException {
		semanticError(new XMLParserException(e));
	}

	/**
	 * Returns the error list when parsing xml file.
	 * 
	 * @return the errors
	 */

	public List getErrors() {
		return errors;
	}
}
