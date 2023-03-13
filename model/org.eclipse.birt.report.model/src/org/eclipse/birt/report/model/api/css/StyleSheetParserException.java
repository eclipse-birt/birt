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
 * Exception thrown if an error occurs when translating an external style sheet
 * to our own <code>CssStyleSheet</code>. It records all the details about the
 * handler.
 */

public class StyleSheetParserException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 6470924439843008936L;

	/**
	 * The name of the style or rule which has the errors.
	 */

	private String propName = null;

	/**
	 * String value of the property.
	 */

	private String value = null;

	/**
	 * Style name or the rule name.
	 */

	private String name = null;

	/**
	 * The rule is not supported. BIRT only supports the style rules other than
	 * media rules, page rules, charset rules and so on.
	 */

	public final static String DESIGN_EXCEPTION_RULE_NOT_SUPPORTED = MessageConstants.STYLE_SHEET_PARSER_EXCEPTION_RULE_NOT_SUPPORTED;

	/**
	 * The style is not supported. BIRT only support selectors like H1, p.table and
	 * .table. All other kinds of selectors are not supported.
	 */

	public final static String DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED = MessageConstants.STYLE_SHEET_PARSER_EXCEPTION_STYLE_NOT_SUPPORTED;

	/**
	 * The CSS property is not supported by BIRT.
	 */

	public final static String DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED = MessageConstants.STYLE_SHEET_PARSER_EXCEPTION_PROPERTY_NOT_SUPPORTED;

	/**
	 * The value of the short-hand property is invalid to CSS2.
	 */

	public final static String DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE = MessageConstants.STYLE_SHEET_PARSER_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE;

	/**
	 * The property value is invalid to Model ROM.
	 */

	public final static String DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE = MessageConstants.STYLE_SHEET_PARSER_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE;

	/**
	 * Constructs the parser exception with the error code.
	 *
	 * @param name    the name of the style or rule which has errors
	 * @param errCode the error code of the exception
	 */

	public StyleSheetParserException(String name, String errCode) {
		super(errCode);
		this.name = name;
	}

	/**
	 * Constructs a parser exception with the error code and string arguments used
	 * to format error messages.
	 *
	 * @param errCode  used to retrieve a piece of externalized message displayed to
	 *                 end user
	 * @param propName the property name
	 * @param value    the property value
	 */

	public StyleSheetParserException(String errCode, String propName, String value) {
		super(errCode);
		this.propName = propName;
		this.value = value;
	}

	/**
	 * Constructs a parser exception with the error code, string arguments used to
	 * format error messages and nested exception.
	 *
	 * @param errCode  used to retrieve a piece of externalized message displayed to
	 *                 end user
	 * @param propName the property name
	 * @param value    the property value
	 * @param cause    the nested exception
	 */

	public StyleSheetParserException(String errCode, String propName, String value, Throwable cause) {
		super(errCode, null, cause);
		this.propName = propName;
		this.value = value;
	}

	/**
	 * Gets the name of the style or rule which has the errors.
	 *
	 * @return the name of the style or the rule
	 */

	public String getName() {
		return this.name;
	}

	/**
	 * Gets the CSS property name.
	 *
	 * @return the CSS property name
	 */

	public String getCSSPropertyName() {
		return this.propName;
	}

	/**
	 * Gets the CSS property text.
	 *
	 * @return the CSS property text
	 */

	public String getCSSValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { propName, value });
		} else if (sResourceKey == DESIGN_EXCEPTION_RULE_NOT_SUPPORTED
				|| sResourceKey == DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED) {
			return ModelMessages.getMessage(sResourceKey, new String[] { name });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
