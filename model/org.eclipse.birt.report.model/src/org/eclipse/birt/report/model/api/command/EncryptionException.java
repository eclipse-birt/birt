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

public class EncryptionException extends SemanticException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3552054627554489034L;

	/**
	 * Error code indicating the property is not encryptable but wanna set
	 * encryption.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ENCRYPTABLE_PROPERTY = MessageConstants.ENCRYPTION_EXCEPTION_INVALID_ENCRYPTABLE_PROPERTY;

	/**
	 * Error code indicating the property is not encryptable but wanna set
	 * encryption.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ENCRYPTION = MessageConstants.ENCRYPTION_EXCEPTION_INVALID_ENCRYPTION;

	/**
	 * Constructor.
	 *
	 * @param obj     the element being changed.
	 * @param str     the name that caused the error.
	 * @param errCode what went wrong.
	 * @param args
	 */

	public EncryptionException(DesignElement obj, String errCode, String[] args) {
		super(obj, args, errCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_INVALID_ENCRYPTABLE_PROPERTY
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_ENCRYPTION) {
			return ModelMessages.getMessage(sResourceKey, this.oaMessageArguments);
		}
		return ModelMessages.getMessage(sResourceKey);
	}
}
