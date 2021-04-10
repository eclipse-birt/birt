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

package org.eclipse.birt.core.framework.parser;

/**
 * Parses any valid XML; handles unimplemented tags. Often used while a parser
 * is under construction to parse and ignore tags that the parser does not yet
 * handle.
 */

public class AnyElementState extends ParseState {

	/**
	 * Constructor.
	 * 
	 * @param theHandler the SAX parser handler
	 */

	public AnyElementState(XMLParserHandler theHandler) {
		super(theHandler);
	}

	public AbstractParseState startElement(String tagName) {
		return new AnyElementState(handler);
	}

}
