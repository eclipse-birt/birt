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

package org.eclipse.birt.report.model.util;

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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	@Override
	public XMLParserHandler getHandler() {
		return handler;
	}

}
