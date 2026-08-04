/*******************************************************************************
 * Copyright (c) 2004-2017 Actuate Corporation.
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

package org.eclipse.birt.core.util;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.DOMImplementation;
import org.xml.sax.SAXException;

/**
 * To provide common utility method in BIRT
 */

public class CommonUtil {

	/**
	 * Instantiate SAX parser and disable XML vectors.
	 *
	 * @return a new SAX parser
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static SAXParser createSAXParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Disable XML External Entity to avoid hack
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
		return factory.newSAXParser();
	}

	/**
	 * Instantiate a new DocumentBuilderFactory and disable XML vectors.
	 *
	 * @return a new document builder factory
	 *
	 * @throws ParserConfigurationException
	 *
	 * @since 4.25
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
		return factory;
	}

	/**
	 * Instantiate a new DocumentBuilder with disabled XML vectors
	 *
	 * @return a new document builder.
	 *
	 * @throws ParserConfigurationException
	 *
	 * @since 4.25
	 */
	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		return newDocumentBuilderFactory().newDocumentBuilder();
	}

	/**
	 * Instantiate a new DocumentBuilder with disabled XML attack vector and with
	 * the given namespace awareness.
	 *
	 * @param namespaceAware the namespace awareness.
	 *
	 * @return a new document builder with the given namespace awareness.
	 *
	 * @throws ParserConfigurationException
	 *
	 * @since 4.25
	 */
	public static DocumentBuilder newDocumentBuilder(boolean namespaceAware) throws ParserConfigurationException {
		DocumentBuilderFactory factory = newDocumentBuilderFactory();
		factory.setNamespaceAware(namespaceAware);
		return factory.newDocumentBuilder();
	}

	/**
	 * Instantiate a new {@link DOMImplementation}.
	 *
	 * @return a new DOM implementation.
	 *
	 * @throws ParserConfigurationException
	 *
	 * @since 4.25
	 */
	public static DOMImplementation newDOMImplementation() throws ParserConfigurationException {
		return newDocumentBuilder().getDOMImplementation();
	}
}
