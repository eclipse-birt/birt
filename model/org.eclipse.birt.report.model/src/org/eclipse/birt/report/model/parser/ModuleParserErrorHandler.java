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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.util.ErrorHandler;
import org.eclipse.birt.report.model.util.XMLParserException;

/**
 * Implements the error handler for the module parser.
 */

public class ModuleParserErrorHandler extends ErrorHandler {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.util.ErrorHandler#semanticError(org.eclipse.
	 * birt.report.model.util.XMLParserException)
	 */

	@Override
	public XMLParserException semanticError(XMLParserException e) {
		if (locator != null) {
			e.setLineNumber(locator.getLineNumber());
		} else {
			e.setLineNumber(1);
		}
		e.setTag(currentElement);
		errors.add(e);
		return e;
	}

}
