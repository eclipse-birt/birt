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

package org.eclipse.birt.report.model.css;

import org.eclipse.birt.report.model.css.property.PropertyParser;
import org.w3c.flute.parser.Parser;

/**
 * Creates a new parser to analyze an input CSS2 file or a short-hand property
 * values.
 * 
 * @see org.eclipse.birt.report.model.css.StyleSheetLoader
 * @see CssParser
 */

public class ParserFactory {

	/**
	 * Creates a Flute parser for the CSS2 grammar.
	 * 
	 * @return a flute parser for the CSS2 grammar
	 */

	static public Parser createCSS2Parser() {
		return new Parser();
	}

	/**
	 * Creates a parser for the short hand properties.
	 * 
	 * @param inputProperty the input short-hand property value to parse
	 * @return a parser for the short-hand properties
	 */

	static public PropertyParser createPropertyParser(String inputProperty) {
		return new PropertyParser(inputProperty);
	}

	/**
	 * Creates an error handler for the css parser.
	 * 
	 * @return the instance of <code>CssErrorHandler</code>
	 */

	static public CssErrorHandler createErrorHandler() {
		return new CssErrorHandler();
	}

}