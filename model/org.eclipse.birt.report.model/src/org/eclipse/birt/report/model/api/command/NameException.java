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
 * Indicates an error when setting the name of an element.
 * 
 */

public class NameException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -657245298064464513L;

	/**
	 * The new element name.
	 */

	protected String name = null;

	/**
	 * Error code indicating the element miss its name, while the name is required.
	 */

	public static final String DESIGN_EXCEPTION_NAME_REQUIRED = MessageConstants.NAME_EXCEPTION_NAME_REQUIRED;

	/**
	 * Error code indicating the element is not allowed to have name.
	 */

	public static final String DESIGN_EXCEPTION_NAME_FORBIDDEN = MessageConstants.NAME_EXCEPTION_NAME_FORBIDDEN;

	/**
	 * Error code indicating the new name duplicates an existing name in the same
	 * name space.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE = MessageConstants.NAME_EXCEPTION_DUPLICATE;

	/**
	 * Error code indicating the element has references, so it cannot be anonymous.
	 */

	public static final String DESIGN_EXCEPTION_HAS_REFERENCES = MessageConstants.NAME_EXCEPTION_HAS_REFERENCES;

	/**
	 * The character "." is forbidden to NamePropertyType.
	 * 
	 * @deprecated replaced by {@link #DESIGN_EXCEPTION_INVALID_NAME}
	 */

	public static final String DESIGN_EXCEPTION_DOT_FORBIDDEN = MessageConstants.NAME_EXCEPTION_INVALID_NAME;

	/**
	 * 
	 */
	public static final String DESIGN_EXCEPTION_INVALID_NAME = MessageConstants.NAME_EXCEPTION_INVALID_NAME;

	/**
	 * The style name is invalid for CSS2 specification.
	 */
	public static final String DESIGN_EXCEPTION_INVALID_STYLE_NAME = MessageConstants.NAME_EXCEPTION_INVALID_STYLE_NAME;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param str     the name that caused the error.
	 * @param errCode what went wrong.
	 */

	public NameException(DesignElement obj, String str, String errCode) {
		super(obj, errCode);
		name = str;
	}

	/**
	 * Returns the name that caused the error.
	 * 
	 * @return the name.
	 */

	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_NAME_REQUIRED || sResourceKey == DESIGN_EXCEPTION_NAME_FORBIDDEN) {
			return ModelMessages.getMessage(sResourceKey, new String[] { element.getIdentifier() });
		} else if (sResourceKey == DESIGN_EXCEPTION_DUPLICATE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { name });
		} else if (sResourceKey == DESIGN_EXCEPTION_HAS_REFERENCES) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_NAME
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_STYLE_NAME) {
			return ModelMessages.getMessage(sResourceKey, new String[] { name });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
