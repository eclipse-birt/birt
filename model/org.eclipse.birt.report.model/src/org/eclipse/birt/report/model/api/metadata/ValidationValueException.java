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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Indicates an invalid validation value.
 */

public class ValidationValueException extends PropertyValueException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -5090192631774412136L;

	/**
	 * The name of the data type being validated.
	 */

	protected String dataType = null;

	/**
	 * Constructs an exception given an invalid value, error code and the property
	 * type constants.
	 * 
	 * @param value    The invalid value.
	 * @param errCode  description of the problem
	 * @param dataType the parameter data type
	 */

	public ValidationValueException(Object value, String errCode, String dataType) {
		super(value, errCode);
		this.invalidValue = value;
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		String value = ""; //$NON-NLS-1$

		if (invalidValue != null)
			value = invalidValue.toString();

		if (sResourceKey == DESIGN_EXCEPTION_INVALID_VALUE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { value, this.dataType });
		}
		return ModelMessages.getMessage(sResourceKey);
	}
}