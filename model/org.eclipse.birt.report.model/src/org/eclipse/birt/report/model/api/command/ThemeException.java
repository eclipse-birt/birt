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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Indicates an error while setting the style of an element.
 * 
 */

public class ThemeException extends SemanticException {

	/**
	 * The serial version UID
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * The theme name being set.
	 */

	protected String themeName = null;

	/**
	 * Error code indicating no style is found with the given name.
	 */

	public static final String DESIGN_EXCEPTION_NOT_FOUND = MessageConstants.THEME_EXCEPTION_NOT_FOUND;

	/**
	 * Error code indicating the report item refers a wrong type of report item
	 * theme.
	 */
	public static final String DESIGN_EXCEPTION_WRONG_TYPE = MessageConstants.THEME_EXCEPTION_WRONG_TYPE;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param str     the name of the style.
	 * @param errCode the error code.
	 */

	public ThemeException(DesignElement obj, String str, String errCode) {
		super(obj, errCode);
		themeName = str;
	}

	/**
	 * Returns the name of the style being set.
	 * 
	 * @return the style name.
	 */

	public Object getTheme() {
		return themeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_NOT_FOUND || sResourceKey == DESIGN_EXCEPTION_WRONG_TYPE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { themeName, getElementName(element) });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
