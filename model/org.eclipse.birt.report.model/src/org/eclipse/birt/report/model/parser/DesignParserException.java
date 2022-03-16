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

import org.eclipse.birt.report.model.api.ErrorCodes;
import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * This class describes a parse error. Many errors are reported using the same
 * exceptions used for API operations.
 *
 */

public class DesignParserException extends ModelException implements ErrorCodes {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 586491893198640178L;

	/**
	 * The design/library file was not found.
	 */

	public static final String DESIGN_EXCEPTION_FILE_NOT_FOUND = MessageConstants.DESIGN_PARSER_EXCEPTION_FILE_NOT_FOUND;

	/**
	 * The design/library file was not found.
	 */

	public static final String DESIGN_EXCEPTION_FILE_FORMAT_NOT_SUPPORT = MessageConstants.DESIGN_PARSER_EXCEPTION_FILE_FORMAT_NOT_SUPPORT;

	/**
	 * A custom color did not have a correct RGB value.
	 */

	public static final String DESIGN_EXCEPTION_RGB_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_RGB_REQUIRED;

	/**
	 * A custom color is missing the color name.
	 */

	public static final String DESIGN_EXCEPTION_COLOR_NAME_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_COLOR_NAME_REQUIRED;

	/**
	 * Use of "extends" to reference a style when "name" should be used.
	 */

	public static final String DESIGN_EXCEPTION_ILLEGAL_EXTENDS = MessageConstants.DESIGN_PARSER_EXCEPTION_ILLEGAL_EXTENDS;

	/**
	 * Image item has more then one kind of reference type.
	 */

	public static final String DESIGN_EXCEPTION_IMAGE_REF_CONFLICT = MessageConstants.DESIGN_PARSER_EXCEPTION_IMAGE_REF_CONFLICT;

	/**
	 * One property is not encryptable.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE = MessageConstants.DESIGN_PARSER_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE;

	/**
	 * An action Drillthrough is missing the "reportName" value.
	 *
	 * @deprecated no such error
	 */

	@Deprecated
	public static final String DESIGN_EXCEPTION_ACTION_REPORTNAME_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_ACTION_REPORTNAME_REQUIRED;

	/**
	 * An parameter in an Action is missing the "name" value.
	 */

	public static final String DESIGN_EXCEPTION_ACTION_PARAMETER_NAME_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_ACTION_PARAMETER_NAME_REQUIRED;

	/**
	 * Break the restriction that "occurrence == 1" for a choice type.
	 *
	 * @deprecated no such error
	 */

	@Deprecated
	public static final String DESIGN_EXCEPTION_CHOICE_RESTRICTION_VIOLATION = MessageConstants.DESIGN_PARSER_EXCEPTION_CHOICE_RESTRICTION_VIOLATION;

	/**
	 * User-defined message is missing the "resource-Key" value.
	 */

	public static final String DESIGN_EXCEPTION_MESSAGE_KEY_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_MESSAGE_KEY_REQUIRED;

	/**
	 * Two translations with the same locale appeared in a User-defined message.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE = MessageConstants.DESIGN_PARSER_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE;

	/**
	 * The property name or member name is required.
	 */

	public static final String DESIGN_EXCEPTION_NAME_REQUIRED = MessageConstants.DESIGN_PARSER_EXCEPTION_NAME_REQUIRED;

	/**
	 * The property is not a structure list.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_STRUCTURE_LIST_TYPE = MessageConstants.DESIGN_PARSER_EXCEPTION_WRONG_STRUCTURE_LIST_TYPE;

	/**
	 * The property is not an extended property.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_EXTENDED_PROPERTY_TYPE = MessageConstants.DESIGN_PARSER_EXCEPTION_WRONG_EXTENDED_PROPERTY_TYPE;

	/**
	 * The structure name is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_STRUCTURE_NAME = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_STRUCTURE_NAME;

	/**
	 * The property syntax is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_PROPERTY_SYNTAX;

	/**
	 * The property is not defined.
	 */

	public static final String DESIGN_EXCEPTION_UNDEFINED_PROPERTY = MessageConstants.DESIGN_PARSER_EXCEPTION_UNDEFINED_PROPERTY;

	/**
	 * A unsupported exception occurred. This happens that the unicode signature in
	 * the design file is not UTF-8.
	 */

	public static final String DESIGN_EXCEPTION_UNSUPPORTED_ENCODING = MessageConstants.DESIGN_PARSER_EXCEPTION_UNSUPPORTED_ENCODING;

	/**
	 * The report version is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_VERSION = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_VERSION;

	/**
	 * The element id is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ELEMENT_ID = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_ELEMENT_ID;

	/**
	 * The virtual parent element reference by baseId is not found in the parent.
	 */

	public static final String DESIGN_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND = MessageConstants.DESIGN_PARSER_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND;

	/**
	 * The element id is duplicate.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_ELEMENT_ID = MessageConstants.DESIGN_PARSER_EXCEPTION_DUPLICATE_ELEMENT_ID;

	/**
	 * The default element is not the same type of template element.
	 */

	public static final String DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE = MessageConstants.DESIGN_PARSER_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE;

	/**
	 * Error code indicating template parameter definition have no default element.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT = MessageConstants.DESIGN_PARSER_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT;

	/**
	 * The simple list property has no definition in the element.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_SIMPLE_LIST_TYPE = MessageConstants.DESIGN_PARSER_EXCEPTION_WRONG_SIMPLE_LIST_TYPE;

	/**
	 * Constructs the design parser exception with the error code.
	 *
	 * @param errCode the error condition
	 */

	public DesignParserException(String errCode) {
		super(errCode);
	}

	/**
	 * Constructs the design parser exception with the file name, the property name
	 * and the error code.
	 *
	 * @param values  the values for message
	 * @param errCode the error condition
	 */

	public DesignParserException(String[] values, String errCode) {
		super(errCode, values, null);
	}

	/**
	 * Constructs the design parser exception with the error code, the exception
	 * argument lists and the caused exception.
	 *
	 * @param errCode
	 * @param values
	 * @param ex
	 */
	public DesignParserException(String errCode, String[] values, Throwable ex) {
		super(errCode, values, ex);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_FILE_NOT_FOUND || sResourceKey == DESIGN_EXCEPTION_FILE_FORMAT_NOT_SUPPORT
				|| sResourceKey == DESIGN_EXCEPTION_UNDEFINED_PROPERTY
				|| sResourceKey == DESIGN_EXCEPTION_PROPERTY_IS_NOT_ENCRYPTABLE
				|| sResourceKey == DESIGN_EXCEPTION_UNSUPPORTED_VERSION
				|| sResourceKey == DESIGN_EXCEPTION_VIRTUAL_PARENT_NOT_FOUND) {
			assert oaMessageArguments.length == 1;

			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_ELEMENT_ID
				|| sResourceKey == DESIGN_EXCEPTION_DUPLICATE_ELEMENT_ID) {
			assert oaMessageArguments.length == 2;
			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		} else if (sResourceKey == DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE) {
			assert oaMessageArguments.length == 2;
			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		} else if (sResourceKey == DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT) {
			assert oaMessageArguments.length == 1;
			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		}

		return ModelMessages.getMessage(sResourceKey);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getMessage()
	 */

	@Override
	public String getMessage() {
		return getLocalizedMessage();
	}
}
