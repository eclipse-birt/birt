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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.eclipse.birt.report.model.api.ModelException;

/**
 * Indicates that the data in the meta-data file does not match the data in the
 * MetaDataConstants file. Also indicates inconsistencies in the element
 * meta-data.
 * <p>
 * A comprehensive set of error codes distinguishes the problem. The detailed
 * description of the error is provided in <code>MetaError.properties</code>,
 * keyed by its error code.
 */

public class MetaDataException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 3787774664483885333L;

	/**
	 * Holds the metadata error informations.
	 */

	protected Properties props = null;

	/**
	 * Error property file.
	 */

	protected final static String ERROR_FILE = "MetaError.properties"; //$NON-NLS-1$

	/**
	 * Detailed error message.
	 */

	protected String message = null;

	/**
	 * Error code constant indicating that name space attribute is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_NAME_SPACE = "INVALID_NAME_SPACE"; //$NON-NLS-1$

	/**
	 * Error code indicating the encryption extension already exists.
	 */

	public static final String DESIGN_EXCEPTION_ENCYRPTION_EXTENSION_EXISTS = "ENCYRPTION_EXTENSION_EXISTS"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing element name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_ELEMENT_NAME = "MISSING_ELEMENT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate element name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME = "DUPLICATE_ELEMENT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing extension element name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_EXTENSION_NAME = "MISSING_EXTENSION_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate extension element name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_EXTENSION_NAME = "DUPLICATE_EXTENSION_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing style name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_STYLE_NAME = "MISSING_STYLE_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate style name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_STYLE_NAME = "DUPLICATE_STYLE_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the Style element is missing from the
	 * meta-data dictionary.
	 */

	public static final String DESIGN_EXCEPTION_STYLE_TYPE_MISSING = "STYLE_TYPE_MISSING"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that extended item is missing from the
	 * meta-data dictionary.
	 */

	public static final String DESIGN_EXCEPTION_EXTENDED_ITEM_MISSING = "EXTENDED_ITEM_MISSING"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the report design is missing from the
	 * meta-data dictionary.
	 */

	public static final String DESIGN_EXCEPTION_REPORT_MISSING = "REPORT_MISSING"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the Style element should be defined
	 * before any styled element.
	 */

	public static final String DESIGN_EXCEPTION_STYLE_NOT_DEFINED = "STYLE_NOT_DEFINED"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate property name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_PROPERTY = "DUPLICATE_PROPERTY"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the element extends from another element
	 * that was not found.
	 */

	public static final String DESIGN_EXCEPTION_ELEMENT_PARENT_NOT_FOUND = "ELEMENT_PARENT_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the element has illegal style properties
	 * defined on it. There are two possible reasons:
	 * <ul>
	 * <li>1. "hasStyle" attribute of an element is false, but it has
	 * StyleProperties defined on it.</li>
	 * <li>2. "hasStyle" attribute of an element is true, and it is an
	 * container(slot count>0), but it has StyleProperties defined on it.</li>
	 * </ul>
	 */

	public static final String DESIGN_EXCEPTION_ILLEGAL_STYLE_PROPS = "ILLEGAL_STYLE_PROPS"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that that an abstract element extends from a
	 * non-abstract element.
	 */

	public static final String DESIGN_EXCEPTION_ILLEGAL_ABSTRACT_ELEMENT = "ILLEGAL_ABSTRACT_ELEMENT"; //$NON-NLS-1$

	/**
	 * Error code constant indicating an invalid name option for element.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_NAME_OPTION = "INVALID_NAME_OPTION"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that an element other than the style element
	 * attempted to define style properties.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_STYLE_PROP_OPTION = "INVALID_STYLE_PROP_OPTION"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that an element definition referenced a style
	 * property that was not actually defined on the style element.
	 */

	public static final String DESIGN_EXCEPTION_STYLE_PROP_NOT_FOUND = "STYLE_PROP_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that type of the property has not been set.
	 */

	public static final String DESIGN_EXCEPTION_PROP_TYPE_ERROR = "PROP_TYPE_ERROR"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that missing choices a choice property.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_PROP_CHOICES = "MISSING_PROP_CHOICES"; //$NON-NLS-1$

	/**
	 * Error code constant indicating missing slot content types for an element slot
	 * definition.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SLOT_TYPE = "MISSING_SLOT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a slot resource key is missing.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SLOT_NAME = "MISSING_SLOT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that content type for the a slot is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_SLOT_TYPE = "INVALID_SLOT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that an element has not been defined.
	 */

	public static final String DESIGN_EXCEPTION_ELEMENT_NAME_CONST = "ELEMENT_NAME_CONST"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing choice set name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_CHOICE_SET_NAME = "MISSING_CHOICE_SET_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating the choice name already exists.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_CHOICE_NAME = "DUPLICATE_CHOICE_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate choice set name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_CHOICE_SET_NAME = "DUPLICATE_CHOICE_SET_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a property visibility name defined on element
	 * doesn't point to an existing property.
	 */

	public static final String DESIGN_EXCEPTION_VISIBILITY_PROPERTY_NOT_FOUND = "VISIBILITY_PROPERTY_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing structure name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_STRUCT_NAME = "MISSING_STRUCT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate structure name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_STRUCT_NAME = "DUPLICATE_STRUCT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing structure definition for a structure
	 * list type property.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_STRUCT_DEFN = "MISSING_STRUCT_DEFN"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a structure definition is unreferencable for a
	 * structure reference type property.
	 */

	public static final String DESIGN_EXCEPTION_UNREFERENCABLE_STRUCT_DEFN = "UNREFERENCABLE_STRUCT_DEFN"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing element reference for element
	 * reference type property.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE = "MISSING_ELEMENT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the element referenced by a property is
	 * not defined.
	 */

	public static final String DESIGN_EXCEPTION_UNDEFINED_ELEMENT_TYPE = "UNDEFINED_ELEMENT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the element referenced by a property is
	 * an unnamed element.
	 */

	public static final String DESIGN_EXCEPTION_UNNAMED_ELEMENT_TYPE = "UNNAMED_ELEMENT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating an inconsistent property or that a style
	 * property cannot be intrinsic.
	 */

	public static final String DESIGN_EXCEPTION_INCONSISTENT_PROP_TYPE = "INCONSISTENT_PROP_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing Java class attribute for a element
	 * definition that is not abstract.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_JAVA_CLASS = "MISSING_JAVA_CLASS"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the specified element Java class cannot
	 * be instantiated, possibly because it is an interface or is an abstract class
	 * or that the specified java class doesn't not provide a default constructor
	 * (or a constructor that takes no argument).
	 */

	public static final String DESIGN_EXCEPTION_JAVA_CLASS_INITIALIZE_ERROR = "JAVA_CLASS_INITIALIZE_ERROR"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the specified element Java class can not
	 * be found in the current class path.
	 */

	public static final String DESIGN_EXCEPTION_JAVA_CLASS_LOAD_ERROR = "JAVA_CLASS_LOAD_ERROR"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the specified element Java class is not a
	 * Design Element, that is, the class is not a kind of
	 * <code>DesignElement</code>.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ELEMENT_JAVA_CLASS = "INVALID_ELEMENT_JAVA_CLASS"; //$NON-NLS-1$

	/**
	 * Error code constant indicating missing method name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_METHOD_NAME = "MISSING_METHOD_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating duplicate method name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_METHOD_NAME = "DUPLICATE_METHOD_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating duplicate argument name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME = "DUPLICATE_ARGUMENT_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the default value for a property is not
	 * valid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE = "INVALID_DEFAULT_VALUE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing class name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_CLASS_NAME = "MISSING_CLASS_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate class name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_CLASS_NAME = "DUPLICATE_CLASS_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating the constructor of one class existing.
	 */

	public static final String DESIGN_EXCEPTION_CONSTRUCTOR_EXISTING = "CONSTRUCTOR_EXISTING"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a duplicate member name of one class.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_MEMBER_NAME = "DUPLICATE_MEMBER_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating a missing member name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_MEMBER_NAME = "MISSING_MEMBER_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that this list property is not supported.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_LIST_TYPE = "INVALID_LIST_TYPE"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the validator is missing of its name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_VALIDATOR_NAME = "MISSING_VALIDATOR_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the validator duplicates an existing one.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_VALIDATOR_NAME = "DUPLICATE_VALIDATOR_NAME"; //$NON-NLS-1$

	/**
	 * Error code constant indicating that the validator referenced by a property is
	 * not found in the dictionary.
	 */

	public static final String DESIGN_EXCEPTION_VALIDATOR_NOT_FOUND = "VALIDATOR_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code constant indicating the property type is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PROPERTY_TYPE = "INVALID_PROPERTY_TYPE"; //$NON-NLS-1$

	/**
	 * The default unit set on the dimension type is not valid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_UNIT = "INVALID_UNIT"; //$NON-NLS-1$

	/**
	 * If the type is list, sub-type can not be null.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SUB_TYPE = "MISSING_SUB_TYPE"; //$NON-NLS-1$

	/**
	 * The sub-type is not the supported simple types when the type is list.
	 */

	public static final String DESIGN_EXCEPTION_UNSUPPORTED_SUB_TYPE = "UNSUPPORTED_SUB_TYPE"; //$NON-NLS-1$

	/**
	 * Only when type is list, the sub-type can be set. Otherwise, sub-type must be
	 * null.
	 */

	public static final String DESIGN_EXCEPTION_SUB_TYPE_FORBIDDEN = "SUB_TYPE_FORBIDDEN"; //$NON-NLS-1$

	/**
	 * Error code indicates that a concrete element misses defines the xml name.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_XML_NAME = "MISSING_XML_NAME"; //$NON-NLS-1$

	/**
	 * Error code indicates that a concrete element duplicates the xml name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_XML_NAME = "DUPLICATE_XML_NAME"; //$NON-NLS-1$

	/**
	 * Constructs an exception given parameters and its error code.
	 *
	 * @param params  string arrays that will be placed into the error message.
	 * @param errCode exception error code that identify the error.
	 */

	public MetaDataException(String[] params, String errCode) {
		this(params, errCode, null);
	}

	/**
	 * Constructs an exception given parameters, its error code and nested
	 * exception.
	 *
	 * @param params  string arrays that will be placed into the error message.
	 * @param errCode exception error code that identify the error.
	 * @param cause   the nested exception
	 */

	public MetaDataException(String[] params, String errCode, Throwable cause) {
		super(errCode, params, null);

		if (props == null) {
			props = loadResourceFile(ERROR_FILE);
		}

		if (props != null) {
			String msg = props.getProperty(errCode);

			assert msg != null : "Error information for error code: " + errCode + " not found in MetaError.properties."; //$NON-NLS-1$ //$NON-NLS-2$
			this.message = params == null ? msg : MessageFormat.format(msg, (Object[]) params);
		}
	}

	/**
	 * Constructs an exception given the error code.
	 *
	 * @param errCode exception error code
	 */

	public MetaDataException(String errCode) {
		this(null, errCode);
	}

	/**
	 * Loads the resource string that describes the error messages.
	 *
	 * @param fileName file name of the resource file.
	 * @return <code>Properties</code> that has load the resource file. Return
	 *         <code>null</code> if error in loading the file.
	 */

	protected Properties loadResourceFile(String fileName) {
		Properties props = new Properties();
		try {
			InputStream in = MetaDataException.class.getResourceAsStream(fileName);
			props.load(in);
			in.close();
		} catch (IOException e) {
			// ignore.
			props = null;
		}

		return props;
	}

	/**
	 * Gets the content message for this exception. The return message will contain
	 * the information of the error code and the detailed error message.
	 *
	 * @return error message for this exception
	 */

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();

		String SEPARATOR = " "; //$NON-NLS-1$

		if (message != null) {
			sb.append("Message:").append(message).append(SEPARATOR); //$NON-NLS-1$
		}

		sb.append("Error code:").append(sResourceKey).append(SEPARATOR); //$NON-NLS-1$

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}
}
