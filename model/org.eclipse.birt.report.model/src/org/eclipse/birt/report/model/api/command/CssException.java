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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Included css style sheet exception
 * 
 */

public class CssException extends SemanticException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5798109731640445551L;

	/**
	 * Indicates the css is not found in module.
	 */

	public final static String DESIGN_EXCEPTION_CSS_NOT_FOUND = MessageConstants.CSS_EXCEPTION_CSS_NOT_FOUND;

	/**
	 * Indicates the css is using is duplicate.
	 */

	public final static String DESIGN_EXCEPTION_DUPLICATE_CSS = MessageConstants.CSS_EXCEPTION_DUPLICATE_CSS;

	/**
	 * Bad css file.
	 */

	public final static String DESIGN_EXCEPTION_BADCSSFILE = MessageConstants.CSS_EXCEPTION_BADCSSFILE;

	/**
	 * Read-only style
	 */

	public final static String DESIGN_EXCEPTION_READONLY = MessageConstants.CSS_EXCEPTION_READONLY;

	/**
	 * 
	 */
	private IncludedCssStyleSheet styleSheet = null;

	/**
	 * Constructor.
	 * 
	 * @param module  the module which has errors
	 * @param values  value array used for error message
	 * @param errCode the error code
	 */

	public CssException(Module module, String[] values, String errCode) {
		super(module, values, errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param module     the module which has errors
	 * @param styleSheet
	 * @param values     value array used for error message
	 * @param errCode    the error code
	 */

	public CssException(Module module, IncludedCssStyleSheet styleSheet, String[] values, String errCode) {
		super(module, values, errCode);
		this.styleSheet = styleSheet;
	}

	/**
	 * Gets the included CSS style sheet for this exception.
	 * 
	 * @return included CSS style sheet
	 */
	public IncludedCssStyleSheet getIncludedStyleSheet() {
		return this.styleSheet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (DESIGN_EXCEPTION_CSS_NOT_FOUND == sResourceKey || DESIGN_EXCEPTION_DUPLICATE_CSS == sResourceKey
				|| DESIGN_EXCEPTION_BADCSSFILE == sResourceKey || DESIGN_EXCEPTION_READONLY == sResourceKey) {
			return ModelMessages.getMessage(sResourceKey, new String[] { (String) oaMessageArguments[0] });
		}

		return ModelMessages.getMessage(sResourceKey);
	}

}
