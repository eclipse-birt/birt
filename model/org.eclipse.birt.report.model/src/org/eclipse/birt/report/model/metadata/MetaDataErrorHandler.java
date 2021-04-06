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

package org.eclipse.birt.report.model.metadata;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.util.ErrorHandler;
import org.eclipse.birt.report.model.util.XMLParserException;

/**
 * Implements the error handler for the meta-data parser.
 */

public class MetaDataErrorHandler extends ErrorHandler {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(MetaDataHandler.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.ErrorHandler#semanticError(org.eclipse.
	 * birt.report.model.util.XMLParserException)
	 */

	public XMLParserException semanticError(XMLParserException e) {
		assert locator != null;
		e.setLineNumber(locator.getLineNumber());
		MetaLogManager.log(e.getMessage(), e);
		logger.log(Level.SEVERE, e.getMessage());
		errors.add(e);
		return e;
	}

}
