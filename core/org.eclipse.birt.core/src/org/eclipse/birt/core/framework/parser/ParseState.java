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
 * Base class provides the parse state framework. By default, it reports an
 * error if an unexpected tag is seen.
 */

public class ParseState extends AbstractParseState {
	/**
	 * The SAX parser handler associated with this parse state.
	 */
	protected final XMLParserHandler handler;

	/**
	 * Constructor.
	 *
	 * @param theHandler the associated SAX parser handler
	 */
	public ParseState(XMLParserHandler theHandler) {
		handler = theHandler;
	}

	@Override
	public XMLParserHandler getHandler() {
		return handler;
	}

}
