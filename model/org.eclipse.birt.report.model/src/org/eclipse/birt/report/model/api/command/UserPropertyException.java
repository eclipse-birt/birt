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

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.MetaDataException;

/**
 * Reports an error during a user property operation.
 *
 */

public class UserPropertyException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -2257635814080094408L;

	/**
	 * The name of the user property affected.
	 */

	protected String propertyName = null;

	/**
	 * Error code indicating the use property definition is missing name, while it
	 * must have a name.
	 */

	public static final String DESIGN_EXCEPTION_NAME_REQUIRED = MessageConstants.USER_PROPERTY_EXCEPTION_NAME_REQUIRED;

	/**
	 * Error code indicating the new user property duplicates an existing property
	 * name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_NAME = MessageConstants.USER_PROPERTY_EXCEPTION_DUPLICATE_NAME;

	/**
	 * Error code indicating the user property type is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_TYPE = MessageConstants.USER_PROPERTY_EXCEPTION_INVALID_TYPE;

	/**
	 * Error code indicating the user property definition is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DEFINITION = MessageConstants.USER_PROPERTY_EXCEPTION_INVALID_DEFINITION;

	/**
	 * Error code indicating the user property type is choice, but no choice is
	 * defined.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_CHOICES = MessageConstants.USER_PROPERTY_EXCEPTION_MISSING_CHOICES;

	/**
	 * Error code indicating the display name ID is provided, and display name can
	 * not be found.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DISPLAY_ID = MessageConstants.USER_PROPERTY_EXCEPTION_INVALID_DISPLAY_ID;

	/**
	 * Error code indicating the user property definition is not found.
	 */

	public static final String DESIGN_EXCEPTION_NOT_FOUND = MessageConstants.USER_PROPERTY_EXCEPTION_NOT_FOUND;

	/**
	 * Error code indicating the element is not allowed to have user property.
	 */

	public static final String DESIGN_EXCEPTION_USER_PROP_DISALLOWED = MessageConstants.USER_PROPERTY_EXCEPTION_USER_PROP_DISALLOWED;

	/**
	 * Error code indicating the value of the user choice is missing.
	 */

	public static final String DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED = MessageConstants.USER_PROPERTY_EXCEPTION_CHOICE_VALUE_REQUIRED;

	/**
	 * Error code indicating the name of the user choice is missing.
	 */

	public static final String DESIGN_EXCEPTION_CHOICE_NAME_REQUIRED = MessageConstants.USER_PROPERTY_EXCEPTION_CHOICE_NAME_REQUIRED;

	/**
	 * Error code indicating the choice value is invalid for the user property type,
	 * which is not choice.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CHOICE_VALUE = MessageConstants.USER_PROPERTY_EXCEPTION_INVALID_CHOICE_VALUE;

	/**
	 * Error code indicating the default value is invalid for the user property
	 * type.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE = MessageConstants.USER_PROPERTY_EXCEPTION_INVALID_DEFAULT_VALUE;

	/**
	 * Constructor.
	 *
	 * @param obj     the element to be changed.
	 * @param name    the name of the user property.
	 * @param errCode what went wrong.
	 */

	public UserPropertyException(DesignElement obj, String name, String errCode) {
		super(obj, errCode);
		propertyName = name;
	}

	/**
	 * Constructor.
	 *
	 * @param obj     the element to be changed
	 * @param name    the name of the user property
	 * @param errCode the error code
	 * @param cause   the nested exception
	 */

	public UserPropertyException(DesignElement obj, String name, String errCode, MetaDataException cause) {
		super(obj, errCode, cause);
		propertyName = name;
	}

	/**
	 * Constructor.
	 *
	 * @param obj     the element to be changed
	 * @param name    the name of the user property
	 * @param errCode the error code
	 * @param cause   the nested exception
	 * @param args    argument array used for error message
	 */

	public UserPropertyException(DesignElement obj, String name, String errCode, ModelException cause, String[] args) {
		super(obj, args, errCode, cause);
		propertyName = name;
	}

	/**
	 * Gets the name of the property that caused the problem.
	 *
	 * @return the property name.
	 */

	public String getPropertyName() {
		return propertyName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_NOT_FOUND || sResourceKey == DESIGN_EXCEPTION_DUPLICATE_NAME
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_DISPLAY_ID
				|| sResourceKey == DESIGN_EXCEPTION_CHOICE_NAME_REQUIRED
				|| sResourceKey == DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_CHOICE_VALUE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { propertyName });
		} else if (sResourceKey == DESIGN_EXCEPTION_USER_PROP_DISALLOWED) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE) {
			assert oaMessageArguments.length == 2;
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element), propertyName,
					(String) oaMessageArguments[0], (String) oaMessageArguments[1] });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
