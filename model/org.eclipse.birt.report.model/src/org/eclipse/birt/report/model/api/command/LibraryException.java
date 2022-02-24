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

import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Indicates an error while operating with library.
 */

public class LibraryException extends SemanticException {

	/**
	 * The serial version UID
	 */

	private static final long serialVersionUID = 5124358913518651257L;

	/**
	 * Indicates the library is not found in module.
	 */

	final public static String DESIGN_EXCEPTION_LIBRARY_NOT_FOUND = MessageConstants.LIBRARY_EXCEPTION_LIBRARY_NOT_FOUND;

	/**
	 * Indicates the namespace one library is using is duplicate.
	 */

	final public static String DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE = MessageConstants.LIBRARY_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE;

	/**
	 * Indicates the library is included recursively.
	 */

	final public static String DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY = MessageConstants.LIBRARY_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY;

	/**
	 * Indicates that library has descendents in the current module.
	 */

	final public static String DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS = MessageConstants.LIBRARY_EXCEPTION_LIBRARY_HAS_DESCENDENTS;

	/**
	 * Indicates that library is already included, a library can not be added twice.
	 */

	final public static String DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED = MessageConstants.LIBRARY_EXCEPTION_LIBRARY_ALREADY_INCLUDED;

	/**
	 * Indicates the namespace of library is invalid, for it is empty or contains
	 * illegal characters.
	 */

	final public static String DESIGN_EXCEPTION_INVALID_LIBRARY_NAMESPACE = MessageConstants.LIBRARY_EXCEPTION_INVALID_LIBRARY_NAMESPACE;

	/**
	 * Constructor.
	 * 
	 * @param module  the module which has errors
	 * @param errCode the error code
	 */

	public LibraryException(Module module, String errCode) {
		super(module, errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param module  the module which has errors
	 * @param values  value array used for error message
	 * @param errCode the error code
	 */

	public LibraryException(Module module, String[] values, String errCode) {
		super(module, values, errCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (DESIGN_EXCEPTION_LIBRARY_NOT_FOUND == sResourceKey
				|| DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY == sResourceKey
				|| DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE == sResourceKey
				|| DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED == sResourceKey
				|| DESIGN_EXCEPTION_INVALID_LIBRARY_NAMESPACE == sResourceKey) {
			return ModelMessages.getMessage(sResourceKey, new String[] { (String) oaMessageArguments[0] });
		} else if (DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS == sResourceKey) {
			LibraryHandle libHandle = (LibraryHandle) element.getHandle((Module) element);
			return ModelMessages.getMessage(sResourceKey,
					new String[] { libHandle.getNamespace(), (String) oaMessageArguments[0] });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}
