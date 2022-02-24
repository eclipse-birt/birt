/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
