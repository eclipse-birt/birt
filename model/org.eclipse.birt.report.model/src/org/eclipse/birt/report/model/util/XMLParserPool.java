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

package org.eclipse.birt.report.model.util;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;

/**
 * The interface for reusing SAX parsers in a multi-thread environment.
 */

interface XMLParserPool {

	/**
	 * Retrieves a parser from the pool given specified properties. If parser can't
	 * be created using specified properties, an exception can be thrown.
	 * 
	 * @param properties a map of a parser properties and their values.
	 * @return A parser instance with given properties.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	public SAXParser get(Map<String, ?> properties) throws ParserConfigurationException, SAXException;

	/**
	 * Returns the parser to the pool.
	 * 
	 * @param parser     the parser in the pool.
	 * @param properties a map of a parser properties and their values.
	 */
	public void release(SAXParser parser, Map<String, ?> properties);

}
