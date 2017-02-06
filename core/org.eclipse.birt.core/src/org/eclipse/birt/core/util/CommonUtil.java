/*******************************************************************************
 * Copyright (c) 2004-2017 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.util;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * To provide common utility method in BIRT
 */

public class CommonUtil
{

	/**
	 * Creates SAX parser and disables XXE
	 * 
	 * @return new SAX parser
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static SAXParser createSAXParser( )
			throws ParserConfigurationException, SAXException
	{

		SAXParserFactory factory = SAXParserFactory.newInstance( );
		// Disable XML External Entity to avoid hack
		factory.setFeature(
				"http://apache.org/xml/features/disallow-doctype-decl", true ); //$NON-NLS-1$
		SAXParser parser = factory.newSAXParser( );

		return parser;
	}
}
