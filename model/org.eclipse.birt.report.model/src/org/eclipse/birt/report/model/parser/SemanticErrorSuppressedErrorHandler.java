/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.metadata.MetaLogManager;
import org.eclipse.birt.report.model.util.ErrorHandler;
import org.eclipse.birt.report.model.util.XMLParserException;

public class SemanticErrorSuppressedErrorHandler extends ErrorHandler {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(SemanticErrorSuppressedErrorHandler.class.getName());

	/**
	 * Adds an error to the warning list inherited from XMLParserHandler during
	 * parsing the design file, and logs the error.
	 * 
	 * @param e the exception to log
	 */
	public XMLParserException semanticError(XMLParserException e) {
		assert locator != null;
		e.setLineNumber(locator.getLineNumber());
		MetaLogManager.log(e.getMessage(), e);
		warnings.add(e);
		logger.log(Level.SEVERE, e.getMessage());
		return e;
	}

	/**
	 * Adds a warning to the warning list inherited from XMLParserHandler during
	 * parsing the design file, and logs the warning.
	 * 
	 * @param e the exception to log
	 */
	public void semanticWarning(Exception e) {
		super.semanticWarning(e);
		logger.log(Level.SEVERE, e.getMessage());
	}

}
