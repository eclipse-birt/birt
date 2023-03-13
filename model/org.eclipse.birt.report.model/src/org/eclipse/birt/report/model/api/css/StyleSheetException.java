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

package org.eclipse.birt.report.model.api.css;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Exception thrown if an error occurs when reading an external style sheet.
 */

public class StyleSheetException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 5816843037267500577L;

	/**
	 * The style sheet is not found.
	 */

	public final static String DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND = MessageConstants.STYLE_SHEET_EXCEPTION_STYLE_SHEET_NOT_FOUND;

	/**
	 * The syntax error, when the style sheet file doesn't conform CSS2 grammar.
	 */

	public final static String DESIGN_EXCEPTION_SYNTAX_ERROR = MessageConstants.STYLE_SHEET_EXCEPTION_SYNTAX_ERROR;

	/**
	 * Constructs the style sheet exception with the error code.
	 *
	 * @param errCode the error code of the exception
	 */

	public StyleSheetException(String errCode) {
		super(errCode);
	}

	/**
	 * Constructs the style sheet exception with the error code and the nested
	 * exception.
	 *
	 * @param errCode the error code of the exception
	 * @param cause   the nested exception
	 */

	public StyleSheetException(String errCode, Throwable cause) {
		super(PLUGIN_ID, errCode, null, null, cause);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		return ModelMessages.getMessage(sResourceKey);
	}
}
