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
 * Reports an error during a user-defined message operation.
 * 
 */

public class CustomMsgException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 2747010046327832852L;

	/**
	 * The resource key with error.
	 */

	private String resourceKey;

	/**
	 * The locale with error.
	 */

	private String locale;

	/**
	 * The resource key must be specified for the Translation.
	 */

	public static final String DESIGN_EXCEPTION_RESOURCE_KEY_REQUIRED = MessageConstants.CUSTOM_MSG_EXCEPTION_RESOURCE_KEY_REQUIRED;

	/**
	 * Duplicated locale for one single message.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_LOCALE = MessageConstants.CUSTOM_MSG_EXCEPTION_DUPLICATE_LOCALE;

	/**
	 * Invalid locale.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_LOCALE = MessageConstants.CUSTOM_MSG_EXCEPTION_INVALID_LOCALE;

	/**
	 * translation is not found in the report.
	 */

	public static final String DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND = MessageConstants.CUSTOM_MSG_EXCEPTION_TRANSLATION_NOT_FOUND;

	/**
	 * Constructs the exception with error code.
	 * 
	 * @param element the element
	 * @param errCode the error code
	 */

	public CustomMsgException(DesignElement element, String errCode) {
		super(element, errCode);
	}

	/**
	 * Constructs the exception with error code.
	 * 
	 * @param element     the element
	 * @param resourceKey the resource key which is involved in this exception
	 * @param locale      the locale which is involved in this exception
	 * @param errCode     the error code
	 */

	public CustomMsgException(DesignElement element, String resourceKey, String locale, String errCode) {
		super(element, errCode);

		this.resourceKey = resourceKey;
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_DUPLICATE_LOCALE
				|| sResourceKey == DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND) {
			return ModelMessages.getMessage(sResourceKey, new String[] { locale, resourceKey });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_LOCALE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { locale });
		}
		return ModelMessages.getMessage(sResourceKey);
	}

}