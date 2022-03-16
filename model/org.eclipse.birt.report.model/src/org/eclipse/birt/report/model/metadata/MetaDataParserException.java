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

package org.eclipse.birt.report.model.metadata;

import java.util.List;

import org.eclipse.birt.report.model.util.XMLParserException;

/**
 * Exception thrown if an error occurs when reading the meta-data description
 * file. A comprehensive set of error codes detail the problem.
 */

public class MetaDataParserException extends XMLParserException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 6591085093091880807L;

	/**
	 * The name of the file being parsed.
	 */

	protected String fileName = null;

	/**
	 * Error code constant indicating that the metadata definition file was not
	 * found.
	 */

	public static final String DESIGN_EXCEPTION_FILE_NOT_FOUND = "FILE_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that an error during parsing of the meta
	 * definition file.
	 */

	public static final String DESIGN_EXCEPTION_PARSER_ERROR = "PARSER_ERROR"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that an error during loading extension.
	 */

	public static final String DESIGN_EXCEPTION_EXTENSION_ERROR = "EXTENSION_ERROR"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the name attribute is required for an
	 * Element, Member, Property, etc.
	 */

	public static final String DESIGN_EXCEPTION_NAME_REQUIRED = "NAME_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the display name resource key is required
	 * for an Element, Member, Property, etc.
	 */

	public static final String DESIGN_EXCEPTION_DISPLAY_NAME_ID_REQUIRED = "DISPLAY_NAME_ID_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the cardinality attribute is required for
	 * a slot.
	 */

	public static final String DESIGN_EXCEPTION_MULTIPLE_CARDINALITY_REQUIRED = "MULTIPLE_CARDINALITY_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that type attribute is required for a Property
	 * or Member.
	 */

	public static final String DESIGN_EXCEPTION_TYPE_REQUIRED = "TYPE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that name attribute is required for a Choice.
	 */

	public static final String DESIGN_EXCEPTION_XML_NAME_REQUIRED = "XML_NAME_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constants indicating that the default value of a propety is
	 * invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DEFAULT = "INVALID_DEFAULT"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the type attribute is invalid for a
	 * Property or Member.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_TYPE = "INVALID_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that metadata dictionary build failed.
	 */

	public static final String DESIGN_EXCEPTION_BUILD_FAILED = "BUILD_FAILED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the display name resource key attribute
	 * is required for group.
	 */

	public static final String DESIGN_EXCEPTION_GROUP_NAME_ID_REQUIRED = "GROUP_NAME_ID_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that a choice property definition referenced
	 * an undefined choice set.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CHOICE_TYPE = "INVALID_CHOICE_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the detail type (choice set name)
	 * attribute must be specified for a choice property.
	 */

	public static final String DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED = "CHOICE_TYPE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the detail type (structure name)
	 * attribute must be specified for a structure list property.
	 */

	public static final String DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED = "STRUCT_TYPE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that a structure list property referenced an
	 * undefined structure name.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_STRUCT_TYPE = "INVALID_STRUCT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the detail type (element type name)
	 * attribute must be specified for an an element reference property.
	 */

	public static final String DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED = "ELEMENT_REF_TYPE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the restriction(Wrapped in <Allowed> tag)
	 * is not valid. May be because that choice is not found in the choice set or
	 * unit not in the choice set.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_RESTRICTION = "INVALID_RESTRICTION"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the restriction can not be set on the
	 * property. Only dimension and choice property can apply this restriction.
	 */

	public static final String DESIGN_EXCEPTION_RESTRICTION_NOT_ALLOWED = "RESTRICTION_NOT_ALLOWED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the validator missing the class name
	 * attribute.
	 */

	public static final String DESIGN_EXCEPTION_CLASS_NAME_REQUIRED = "CLASS_NAME_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the validator can not be instantiated.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_META_VALIDATOR = "INVALID_META_VALIDATOR"; //$NON-NLS-1$

	/**
	 * The data type of the member of one class is missing.
	 */

	public static final String DESIGN_EXCEPTION_DATA_TYPE_REQUIRED = "DATA_TYPE_REQUIRED"; //$NON-NLS-1$

	/**
	 * The validator name is missing.
	 */

	public static final String DESIGN_EXCEPTION_VALIDATOR_NAME_REQUIRED = "VALIDATOR_NAME_REQUIRED"; //$NON-NLS-1$

	/**
	 * The default unit can only be set on the dimension property type.
	 */

	public static final String DESIGN_EXCEPTION_DEFAULT_UNIT_NOT_ALLOWED = "DEFAULT_UNIT_NOT_ALLOWED"; //$NON-NLS-1$

	/**
	 * Sub-type is required for list type property.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SUB_TYPE = "MISSING_SUB_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constants indicating that the value of a property is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_TRIM_OPTION = "INVALID_TRIM_OPTION"; //$NON-NLS-1$

	/**
	 * Constructs an exception given the file name and error code.
	 *
	 * @param name    file name
	 * @param errCode error code
	 */

	public MetaDataParserException(String name, String errCode) {
		super(errCode);
		fileName = name;
		errorCode = errCode;
	}

	/**
	 * Constructs an exception given an exception and error code.
	 *
	 * @param e       an exception
	 * @param errCode error code
	 */

	public MetaDataParserException(Exception e, String errCode) {
		super(e, errCode);
	}

	/**
	 * Constructs an exception given the error code.
	 *
	 * @param errCode error code
	 */

	public MetaDataParserException(String errCode) {
		super(errCode);
	}

	/**
	 * Constructor.
	 *
	 * @param errors list of errors
	 */

	public MetaDataParserException(List<XMLParserException> errors) {
		super(errors);
	}

	/**
	 * Sets the file name.
	 *
	 * @param name the file name to set
	 */

	public void setFileName(String name) {
		fileName = name;
	}

	/**
	 * Gets the content message of this exception. The return message will contain
	 * the information of the file name, affected source file line number, error
	 * code and the wrapped exception.
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {

		StringBuilder sb = new StringBuilder();

		String SEPARATOR = " "; //$NON-NLS-1$

		if (fileName != null) {
			sb.append("FileName:"); //$NON-NLS-1$
			sb.append(fileName);
			sb.append(SEPARATOR);
		}

		sb.append("Line Number:");//$NON-NLS-1$
		sb.append(getLineNumber());
		sb.append(SEPARATOR);
		sb.append("Error Code:"); //$NON-NLS-1$
		sb.append(errorCode);
		sb.append(SEPARATOR);

		if (getException() != null) {
			sb.append("Exception:");//$NON-NLS-1$
			sb.append(getException());
			sb.append(SEPARATOR);
		}

		return sb.toString();
	}

}
