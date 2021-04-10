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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Indicates an error while setting the style of an element.
 * 
 */

public class StyleException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -4383500737464797856L;

	/**
	 * The style name being set.
	 */

	protected String styleName = null;

	/**
	 * Error code indicating the element is not allowed to have style.
	 */

	public static final String DESIGN_EXCEPTION_FORBIDDEN = MessageConstants.STYLE_EXCEPTION_FORBIDDEN;

	/**
	 * Error code indicating no style is found with the given name.
	 */

	public static final String DESIGN_EXCEPTION_NOT_FOUND = MessageConstants.STYLE_EXCEPTION_NOT_FOUND;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param str     the name of the style.
	 * @param errCode the error code.
	 */

	public StyleException(DesignElement obj, String str, String errCode) {
		super(obj, errCode);
		styleName = str;
	}

	/**
	 * Returns the name of the style being set.
	 * 
	 * @return the style name.
	 */

	public Object getStyle() {
		return styleName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_FORBIDDEN) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_NOT_FOUND) {
			return ModelMessages.getMessage(sResourceKey, new String[] { styleName, getElementName(element) });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}