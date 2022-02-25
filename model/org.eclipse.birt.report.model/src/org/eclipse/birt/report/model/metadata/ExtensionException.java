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

/**
 * Indicates the error when loading extensions.
 */

public class ExtensionException extends MetaDataException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */

	private static final long serialVersionUID = 3977016262414907448L;

	/**
	 * Error code indicating the extension point is not found.
	 */

	public static final String DESIGN_EXCEPTION_EXTENSION_POINT_NOT_FOUND = "EXTENSION_POINT_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code indicating the instance can not be created.
	 */

	public static final String DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE = "FAILED_TO_CREATE_INSTANCE"; //$NON-NLS-1$

	/**
	 * Error code indicating the value is required.
	 */

	public static final String DESIGN_EXCEPTION_VALUE_REQUIRED = "VALUE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Error code indicating the choice value is invalid for the user property type,
	 * which is not choice.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CHOICE_VALUE = "INVALID_CHOICE_VALUE"; //$NON-NLS-1$

	/**
	 * Error code indicating the element type of the extension is invalid or not
	 * supported.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ELEMENT_TYPE = "INVALID_ELEMENT_TYPE"; //$NON-NLS-1$

	/**
	 * Error code indicating that an extension choice property defines both
	 * "detailType" to refer a rom-defined choice set and its own choice list.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CHOICE_PROPERTY = "INVALID_CHOICE_PROPERTY"; //$NON-NLS-1$

	/**
	 * Error code indicating that an encryption extension wants to set itself
	 * default when the default is specified.
	 */
	public static final String DESIGN_EXCEPTION_DEFAULT_ENCRYPTION_EXIST = "DEFAULT_ENCRYPTION_EXIST"; //$NON-NLS-1$

	/**
	 * Error code indicating that The defaultStyle for extension element should not
	 * be empty.
	 */
	public static final String DESIGN_EXCEPTION_EMPTY_STYLE_NAME = "DEFAULT_EMPTY_STYLE_NAME"; //$NON-NLS-1$

	/**
	 * Error code indicating that the input stream returned for delta-rom is empty.
	 */
	public static final String DESIGN_EXCEPTION_EMPTY_DELTA_ROM_STREAM = "EMPTY_DELTA_ROM_STREAM"; //$NON-NLS-1$

	/**
	 * Constructs an extension exception with error code.
	 *
	 * @param params    the parameters for building error message
	 * @param errorCode the error code
	 */

	public ExtensionException(String[] params, String errorCode) {
		super(params, errorCode);
	}

}
